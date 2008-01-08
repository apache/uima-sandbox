package org.apache.uima.adapter.jms.client;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.aae.AsynchAECasManager_impl;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.client.UimaEEStatusCallbackListener;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.ControllerLifecycle;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.Level;
import org.apache.uima.adapter.jms.service.Dd2spring;
import org.apache.uima.aae.UIDGenerator;

import javax.jms.DeliveryMode;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.UimaEEAdminSpringContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Uima EE client code
 * 
 * 
 */
public class BaseUIMAAsynchronousEngine_impl extends BaseUIMAAsynchronousEngineCommon_impl implements UimaAsynchronousEngine, MessageListener
{
	private static final Class CLASS_NAME = BaseUIMAAsynchronousEngine_impl.class;

	/**
	 * Helper method to create JMS Text Message
	 */
	protected TextMessage createTextMessage() throws ResourceInitializationException
	{
		return new ActiveMQTextMessage();
	}

	/**
	 * Called at the end of collectionProcessingComplete - WAS closes receiving
	 * thread here
	 */
	protected void cleanup() throws Exception
	{
	}

	/**
	 * Return a name of the queue to which the JMS Producer is connected to.
	 */
	public String getEndPointName() throws ResourceProcessException
	{
		try
		{
			return (((ActiveMQDestination) producer.getDestination()).getPhysicalName());
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
	/**
	 * Helper method to set common properties in the JMS Message
	 * @param msg
	 * @throws Exception
	 */
	private void setCommonProperties( TextMessage msg ) throws Exception
	{
		//	Identify this client's queue
		msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());
		//	Identify client's broker 
		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		//	Indicate that this is Request message
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
		//	Indicate that this is GetMeta command
		//	Set the Consumer destination. The service must reply to this destination
		msg.setJMSReplyTo(consumerDestination);
	}
	/**
	 * Initialize JMS Message with properties relevant to GetMeta request.
	 */
	protected void setMetaRequestMessage(TextMessage msg) throws Exception
	{
		setCommonProperties(msg);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.GetMeta);
		//	Set carge to empty String. This is needed for non-tcp protocols.
		((ActiveMQTextMessage) msg).setText("");
	}

	/**
	 * Initialize JMS Message with properties relevant to Process CAS request.
	 */
	protected void setCASMessage(String casReferenceId, CAS aCAS, TextMessage msg) throws ResourceProcessException
	{
		try
		{
			setCommonProperties(msg);
			msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.Process);
			msg.setStringProperty(AsynchAEMessage.CasReference, casReferenceId);
			msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.XMIPayload);
			msg.setText(serializeCAS(aCAS));
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
	/**
	 * Initialize JMS Message with properties relevant to CPC request.
	 */
	public void setCPCMessage(TextMessage msg) throws Exception
	{
		setCommonProperties(msg);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.CollectionProcessComplete);
		msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None);
		msg.setBooleanProperty(AsynchAEMessage.RemoveEndpoint, true);
		msg.setText("");
	}
	
	
	/**
	 * Force all threads to stop and than do cleanup of resources.
	 * 
	 */
	public void stop()
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});

		if (!running)
		{
			return;
		}

		running = false;

		try
		{
			//	Close JMS 
			producerSession.close();
			consumerSession.close();
			consumer.close();
			session.close();
			connection.close();
			connection = null;

			// Unblock threads
			if (threadMonitorMap.size() > 0)
			{
				Iterator it = threadMonitorMap.keySet().iterator();
				while (it.hasNext())
				{
					long key = ((Long) it.next()).longValue();
					ThreadMonitor threadMonitor = (ThreadMonitor) threadMonitorMap.get(key);
					synchronized (threadMonitor.getMonitor())
					{
						threadMonitor.setWasSignaled();
						threadMonitor.getMonitor().notifyAll();
					}
				}
			}

			// Unlock all 3 gates
			synchronized (cpcGate)
			{
				cpcGate.notifyAll();
			}
			synchronized (endOfCollectionMonitor)
			{
				receivedCpcReply = true;
				endOfCollectionMonitor.notifyAll();
			}
			synchronized (metadataReplyMonitor)
			{
				receivedMetaReply = true;
				metadataReplyMonitor.notifyAll();
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_as_client_INFO", new Object[] {});
			//	Undeploy all containers from the registry in case the services
			//	are collocated with this client
			for (Iterator i = springContainerRegistry.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entry = (Map.Entry) i.next();
				Object key = entry.getKey();
				undeploy((String) key);
			}
			asynchManager = null;
			springContainerRegistry.clear();
			listeners.clear();

			// Cancel any timers
			Iterator iter = clientCache.values().iterator();
			while (iter.hasNext())
			{
				((ClientRequest) iter.next()).cancelTimer();
			}
			clientCache.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			synchronized (this)
			{
				try
				{
					wait(2000); // Let asynch shutdown threads to stop
				}
				catch (Exception e)
				{
				}
			}

		}
	}

	/**
	 * Return a new session from an established connection. If the connection is
	 * not established yet, it will be created.
	 *
	 * @param aBrokerURI - URL of the broker
	 * @return - valid session object
	 * @throws Exception -
	 */
	protected Session getSession(String aBrokerURI) throws Exception
	{
		if (connection == null)
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(aBrokerURI);
			connection = factory.createConnection();
			connection.start();
		}
		session = connection.createSession(false, 0);
		return session;
	}
	/**
	 * Create a JMS producer that is connected to a given queue. 
	 * 
	 * @param aBrokerURI - broker managing a service queue
	 * @param aQueueName - service queue to which connection is made
	 * 
	 * @throws Exception
	 */
	public void initializeProducer(String aBrokerURI, String aQueueName) throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "initializeProducer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_jms_producer_INFO", new Object[] { aBrokerURI, aQueueName });

		brokerURI = aBrokerURI;
		producerSession = getSession(aBrokerURI);
		Queue producerDestination = producerSession.createQueue(aQueueName);
		producer = producerSession.createProducer(producerDestination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		return;
	}
	/**
	 * Create a JMS Consumer on a temporary queue. Service replies will be handled by 
	 * this consumer. 
	 * 
	 * @param aBrokerURI 
	 * @throws Exception
	 */
	public void initializeConsumer(String aBrokerURI) throws Exception
	{
		consumerSession = getSession(aBrokerURI);
		consumerDestination = consumerSession.createTemporaryQueue();
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "initializeConsumer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_jms_consumer_INFO", new Object[] { aBrokerURI, consumerDestination.getQueueName() });
		consumer = consumerSession.createConsumer(consumerDestination);
		consumer.setMessageListener(this);
	}

	public void initialize(String[] configFiles, Map anApplicationContext) throws ResourceInitializationException
	{
		reset();

		if (anApplicationContext != null)
		{
			Properties p = new Properties();
			p.put(UimaAsynchronousEngine.ApplicationContext, anApplicationContext);
			System.setProperties(p);
		}

		try
		{

			deployEmbeddedBroker();
			deploySpringContainer(configFiles);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}
		running = true;
		initialized = true;
	}

	public void initialize(Map anApplicationContext) throws ResourceInitializationException
	{
		reset();
		Properties performanceTuningSettings = null;

		if (!anApplicationContext.containsKey(UimaAsynchronousEngine.ServerUri))
		{
			throw new ResourceInitializationException();
		}
		if (!anApplicationContext.containsKey(UimaAsynchronousEngine.Endpoint))
		{
			throw new ResourceInitializationException();
		}
		ResourceManager rm = null;
		if (anApplicationContext.containsKey(Resource.PARAM_RESOURCE_MANAGER))
		{
			rm = (ResourceManager) anApplicationContext.get(Resource.PARAM_RESOURCE_MANAGER);
		}
		else
		{
			rm = UIMAFramework.newDefaultResourceManager();
		}

		if (anApplicationContext.containsKey(UIMAFramework.CAS_INITIAL_HEAP_SIZE))
		{
			String cas_initial_heap_size = (String) anApplicationContext.get(UIMAFramework.CAS_INITIAL_HEAP_SIZE);
			performanceTuningSettings = new Properties();
			performanceTuningSettings.put(UIMAFramework.CAS_INITIAL_HEAP_SIZE, cas_initial_heap_size);
		}

		asynchManager = new AsynchAECasManager_impl(rm);

		brokerURI = (String) anApplicationContext.get(UimaAsynchronousEngine.ServerUri);
		String endpoint = (String) anApplicationContext.get(UimaAsynchronousEngine.Endpoint);
		int casPoolSize = 1;

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.ReplyWindow))
		{
			receiveWindow = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.ReplyWindow)).intValue();
		}

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.CasPoolSize))
		{
			casPoolSize = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.CasPoolSize)).intValue();
		}

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.Timeout))
		{
			processTimeout = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.Timeout)).intValue();
		}

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.GetMetaTimeout))
		{
			metadataTimeout = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.GetMetaTimeout)).intValue();
		}

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.CpcTimeout))
		{
			cpcTimeout = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.CpcTimeout)).intValue();
		}

		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_uimaee_client__CONFIG", new Object[] { brokerURI, receiveWindow, casPoolSize, processTimeout, metadataTimeout, cpcTimeout });

		try
		{
			initializeProducer(brokerURI, endpoint);
			initializeConsumer(brokerURI);
			running = true;
			sendMetaRequest();
			waitForMetadataReply();
			if (abort || !running)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_aborting_as_WARNING", new Object[] { "Metadata Timeout" });
				throw new ResourceInitializationException();
			}
			else
			{
				if (collectionReader != null)
				{
					asynchManager.addMetadata(collectionReader.getProcessingResourceMetaData());
				}

				asynchManager.initialize(casPoolSize, "ApplicationCasPoolContext", performanceTuningSettings);
				initialized = true;
				remoteService = true;
				// running = true;

				for (int i = 0; listeners != null && i < listeners.size(); i++)
				{
					((UimaEEStatusCallbackListener) listeners.get(i)).initializationComplete(null);
				}
			}
		}
		catch (ResourceInitializationException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new ResourceInitializationException(e);
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_as_initialized_CONFIG", new Object[] { "" });

	}

	/**
	 * 
	 */
	public String deploy(String[] aDeploymentDescriptorList, Map anApplicationContext) throws Exception
	{
		if (aDeploymentDescriptorList == null)
		{
			throw new ResourceConfigurationException(UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT, new Object[] { "Null", "DeploymentDescriptorList", "deploy()" });
		}

		if (aDeploymentDescriptorList.length == 0)
		{
			throw new ResourceConfigurationException(ResourceConfigurationException.MANDATORY_VALUE_MISSING, new Object[] { "DeploymentDescriptorList" });
		}
		String[] springContextFiles = new String[aDeploymentDescriptorList.length];

		for (int i = 0; i < aDeploymentDescriptorList.length; i++)
		{
			springContextFiles[i] = generateSpringContext(aDeploymentDescriptorList[i], anApplicationContext);
		}
		return deploySpringContainer(springContextFiles);
	}

	/**
	 * First generates a Spring context from a given deploy descriptor and than
	 * deploys the context into a Spring Container.
	 * 
	 * @param aDeploymentDescriptor -
	 *            deployment descriptor to generate Spring Context from
	 * @param anApplicationContext -
	 *            a Map containing properties required by dd2spring
	 * 
	 * @return - a unique spring container id
	 * 
	 */
	public String deploy(String aDeploymentDescriptor, Map anApplicationContext) throws Exception
	{
		String springContext = generateSpringContext(aDeploymentDescriptor, anApplicationContext);
		return deploySpringContainer(new String[] { springContext });
	}

	/**
	 * Undeploys Spring container with a given container Id. All deployed Spring
	 * containers are registered in the local registry under a unique id.
	 * 
	 */
	public void undeploy(String aSpringContainerId) throws Exception
	{
		UimaEEAdminSpringContext adminContext = null;
		synchronized (springContainerRegistry)
		{
			if (!springContainerRegistry.containsKey(aSpringContainerId))
			{
				throw new InvalidContainerException("Invalid Spring container Id:" + aSpringContainerId + ". Unable to undeploy the Spring container");
			}
			// Fetch an administrative context which contains a Spring Container
			adminContext = (UimaEEAdminSpringContext) springContainerRegistry.get(aSpringContainerId);
			// Fetch instance of the Container from its context
			ApplicationContext ctx = adminContext.getSpringContainer();
			// Query the container for objects that implement
			// ControllerLifecycle interface. These
			// objects are typically of type AnalysisEngineController or
			// UimacppServiceController.
			String[] asyncServiceList = ctx.getBeanNamesForType(org.apache.uima.aae.controller.ControllerLifecycle.class);
			// Given a valid list of controllers select the first from the list
			// and
			// initiate a shutdown. We don't care which controller will be
			// invoked. In case of
			// AggregateAnalysisEngineController the terminate event will
			// propagate all the way
			// to the top controller in the hierarchy and the shutdown will take
			// place from there.
			// If the controller is of kind UimecppServiceController or
			// PrimitiveAnalysisController
			// the termination logic will be immediately triggered in the
			// terminate() method.
			if (asyncServiceList != null && asyncServiceList.length > 0)
			{
				ControllerLifecycle ctrer = (ControllerLifecycle) ctx.getBean(asyncServiceList[0]);
				// Send a trigger to initiate shutdown.
				if (ctrer instanceof AnalysisEngineController)
				{
					((AnalysisEngineController) ctrer).getControllerLatch().release();
				}
				ctrer.terminate();
			}

			if (ctx instanceof FileSystemXmlApplicationContext)
			{
				((FileSystemXmlApplicationContext) ctx).destroy();
			}

			// Remove the container from a local registry
			springContainerRegistry.remove(aSpringContainerId);
		}

	}

	/**
	 * Use dd2spring to generate Spring context file from a given deployment
	 * descriptor file.
	 * 
	 * @param aDeploymentDescriptor -
	 *            deployment descriptor to generate Spring Context from
	 * @param anApplicationContext -
	 *            a Map containing properties required by dd2spring
	 * @return - an absolute path to the generated Spring Context file
	 * 
	 * @throws Exception -
	 *             if failure occurs
	 */
	private String generateSpringContext(String aDeploymentDescriptor, Map anApplicationContext) throws Exception
	{

		String dd2SpringXsltFilePath = null;
		String saxonClasspath = null;

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.DD2SpringXsltFilePath))
		{
			dd2SpringXsltFilePath = (String) anApplicationContext.get(UimaAsynchronousEngine.DD2SpringXsltFilePath);
		}
		else
		{
			throw new ResourceConfigurationException(ResourceConfigurationException.MANDATORY_VALUE_MISSING, new Object[] { "Xslt File Path" });
		}
		if (anApplicationContext.containsKey(UimaAsynchronousEngine.SaxonClasspath))
		{
			saxonClasspath = (String) anApplicationContext.get(UimaAsynchronousEngine.SaxonClasspath);
		}
		else
		{
			throw new ResourceConfigurationException(ResourceConfigurationException.MANDATORY_VALUE_MISSING, new Object[] { "Saxon Classpath" });
		}

		Dd2spring dd2Spring = new Dd2spring();
		File springContextFile = dd2Spring.convertDd2Spring(aDeploymentDescriptor, dd2SpringXsltFilePath, saxonClasspath, (String) anApplicationContext.get(UimaAsynchronousEngine.UimaEeDebug));

		return springContextFile.getAbsolutePath();
	}

	protected String deploySpringContainer(String[] springContextFiles) throws ResourceInitializationException
	{
		try
		{
			for (int i = 0; i < springContextFiles.length; i++)
			{
				springContextFiles[i] = "file:" + springContextFiles[i];
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "deploySpringContainer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_deploy_container__CONFIG", new Object[] { springContextFiles[i] });
			}

			ApplicationContext ctx = new FileSystemXmlApplicationContext(springContextFiles);
			UimaEEAdminSpringContext springAdminContext = new UimaEEAdminSpringContext((FileSystemXmlApplicationContext) ctx);
			String[] brokerDeployer = ctx.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.BrokerDeployer.class);
			String[] controllers = ctx.getBeanNamesForType(org.apache.uima.aae.controller.AnalysisEngineController.class);
			for (int i = 0; controllers != null && i < controllers.length; i++)
			{
				AnalysisEngineController cntlr = (AnalysisEngineController) ctx.getBean(controllers[i]);
				cntlr.setUimaEEAdminContext(springAdminContext);
				if (cntlr.isTopLevelComponent())
				{
					((FileSystemXmlApplicationContext) ctx).setDisplayName(cntlr.getComponentName());
				}
			}

			String containerId = new UIDGenerator().nextId();
			synchronized (springContainerRegistry)
			{
				springContainerRegistry.put(containerId, springAdminContext);
			}
			return containerId;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}

	}

	protected void deployEmbeddedBroker() throws Exception
	{
		// TBI
	}

	public static void main(String[] args)
	{
		try
		{

			BaseUIMAAsynchronousEngineCommon_impl uimaee = new BaseUIMAAsynchronousEngine_impl();

			Map appContext = new HashMap();
			appContext.put(UimaAsynchronousEngine.DD2SpringXsltFilePath, args[1]);
			appContext.put(UimaAsynchronousEngine.SaxonClasspath, args[2]);
			String containerId = uimaee.deploy(args[0], appContext); // args[1],
			// args[2]);

			uimaee.undeploy(containerId);
		}
		catch (Exception e)
		{
		}
	}

	private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
