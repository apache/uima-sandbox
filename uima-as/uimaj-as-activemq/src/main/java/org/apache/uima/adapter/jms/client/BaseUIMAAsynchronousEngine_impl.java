/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.adapter.jms.client;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ObjectName;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.aae.AsynchAECasManager_impl;
import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.ControllerCallbackListener;
import org.apache.uima.aae.controller.ControllerLifecycle;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.UimaASMetaRequestTimeout;
import org.apache.uima.aae.jmx.JmxManager;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.SpringContainerDeployer;
import org.apache.uima.adapter.jms.activemq.UimaEEAdminSpringContext;
import org.apache.uima.adapter.jms.service.Dd2spring;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BaseUIMAAsynchronousEngine_impl extends BaseUIMAAsynchronousEngineCommon_impl implements UimaAsynchronousEngine, MessageListener, ControllerCallbackListener 
{
	private static final Class CLASS_NAME = BaseUIMAAsynchronousEngine_impl.class;
	private MessageSender sender = null;
	private MessageProducer producer;
	private String brokerURI = null;
	private Session session = null;
	private Session consumerSession = null;

	private Connection connection = null;
	private boolean serviceInitializationException;
	private boolean serviceInitializationCompleted;
	
	private Object serviceMonitor = new Object();
	
	private Queue consumerDestination = null;
	private Session producerSession = null;
	private JmxManager jmxManager = null;
	private String applicationName = "UimaASClient";
	
	public BaseUIMAAsynchronousEngine_impl() {
        UIMAFramework.getLogger(CLASS_NAME).log(Level.INFO, "UIMA-AS version " + UIMAFramework.getVersionString());
	}


	protected TextMessage createTextMessage() throws ResourceInitializationException
	{
		return 	new ActiveMQTextMessage();
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
		try{
			return ((ActiveMQDestination)sender.getMessageProducer().getDestination()).getPhysicalName();
			//return (((ActiveMQDestination) producer.getDestination()).getPhysicalName());
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
	protected void setMetaRequestMessage(TextMessage msg) throws Exception
	{
		msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());

		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.GetMeta);
		msg.setJMSReplyTo(consumerDestination);
		((ActiveMQTextMessage) msg).setText("");
	}
	/**
	 * Initialize JMS Message with properties relevant to Process CAS request.
	 */
	protected void setCASMessage(String aCasReferenceId, CAS aCAS, TextMessage msg) throws ResourceProcessException
	{
		try{
			setCommonProperties(aCasReferenceId, msg);
			msg.setText(serializeCAS(aCAS));
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
	
	protected void setCASMessage(String aCasReferenceId, String aSerializedCAS, TextMessage msg) throws ResourceProcessException
	{
		try{
			setCommonProperties(aCasReferenceId, msg);
			msg.setText(aSerializedCAS);
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
    protected void setCommonProperties( String aCasReferenceId, TextMessage msg) throws ResourceProcessException
    {
		try{
			msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());
	
			msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
			msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
			msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.Process);
			msg.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
			msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.XMIPayload);
			msg.setJMSReplyTo(consumerDestination);
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
    	
    }
	public void stop()
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});

		if (!running)
		{
			return;
		}
		super.stop();
		running = false;

		try
		{
			if ( sender != null )
			{
				sender.doStop();
			}
			if ( initialized )
			{
	      consumerSession.close();
	      consumer.close();
	      try
	      {
		      connection.close();
	      }
	      catch(Exception ex) {}
	      connection = null;
			}
			if ( jmxManager != null )
			{
				jmxManager.destroy();
			}
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

	public void setCPCMessage(TextMessage msg) throws Exception
	{
		msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());
		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.CollectionProcessComplete);
		msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None);
		msg.setBooleanProperty(AsynchAEMessage.RemoveEndpoint, true);
		msg.setJMSReplyTo(consumerDestination);
		msg.setText("");
	}
	protected Connection getConnection( String aBrokerURI ) throws Exception
	{
		if (connection == null )
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(aBrokerURI);
			connection = factory.createConnection();
			connection.start();
		}
		return connection;
	}

	private void validateConnection(String aBrokerURI) throws Exception
	{
		if (connection == null)
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(aBrokerURI);
			System.out.println(">>>>>>>>>>>>> BaseUIMAAsynchronousEngine_impl.validateConnection() Adding policy");
	        RedeliveryPolicy policy = new RedeliveryPolicy();
	        policy.setMaximumRedeliveries(1);
	        policy.setBackOffMultiplier((short) 1);
	        policy.setInitialRedeliveryDelay(10);
	        policy.setUseExponentialBackOff(false);
	        factory.setRedeliveryPolicy(policy);

			connection = factory.createConnection();
			connection.start();
		}
	}
	protected Session getSession(String aBrokerURI) throws Exception
	{
		validateConnection(aBrokerURI);
		session = connection.createSession(false, 0);
		return session;
	}

	protected MessageProducer lookupProducerForEndpoint( Endpoint anEndpoint ) throws Exception
	{
		if ( connection == null || producerSession == null )
		{
			throw new ResourceInitializationException();
		}
		Destination dest = producerSession.createQueue(anEndpoint.getEndpoint());
		return producerSession.createProducer(dest);
	}
	public void initializeProducer(String aBrokerURI, String aQueueName) throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "initializeProducer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_jms_producer_INFO", new Object[] { aBrokerURI, aQueueName });

		brokerURI = aBrokerURI;
		//	Create a worker thread for sending messages. Jms sessions are single threaded
		//	and it is illegal (per JMS spec) to use the same sesssion from multiple threads.
		//  The worker thread solves this problem. As it is the only thread that owns the
		//	session and uses it to create message producer.
		//	The worker thread blocks waiting for messages from application threads. The 
		//  application	threads add messages to the shared "queue" (in-memory queue not 
		//  jms queue) and the worker thread consumes them. The worker thread is not 
		//	serialializing CASes. This work is done in application threads. 

		//	create a worker object. This doesnt start the thread yet
		sender = 
			new ActiveMQMessageSender( getConnection(aBrokerURI), super.pendingMessageList, aQueueName, this);
		producerInitialized = false;
		Thread t = new Thread( (BaseMessageSender) sender);
		//	Start the worker thread. The jms session and message producer are created. Once
		//	the message producer is created, the worker thread notifies this thread by
		//	calling onProducerInitialized() where the global flag 'producerInitialized' is 
		//	set to true. After the notification, the worker thread notifies this instance
		//	that the producer is fully initialized and finally begins to wait for messages
		//	in pendingMessageList. Upon arrival, each message is removed from 
		//	pendingMessageList and it is sent to a destination.
		
////		Thread t = new Thread((BaseMessageSender)messageDispatcher); //.doStart();
		t.start();
		//	Wait until the worker thread is fully initialized
			synchronized( sender )
			{
				while( !producerInitialized )
				{
					//	blocks here. The worker thread will signal when it is fully initialized 
					sender.wait();
				}
			}
		//	Check if the worker thread failed to initialize
		if ( sender.failed())
		{
			//	Worker thread failed to initialize. Log the reason and stop the uima ee client
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "initializeProducer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_worker_thread_failed_to_initialize__WARNING", new Object[] { sender.getReasonForFailure() });
			stop();
			return;
		}
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
	/**
	 * Initialize the uima ee client. Takes initialization parameters from the
	 * <code>anApplicationContext</code> map.
	 */
	public synchronized void initialize(Map anApplicationContext) throws ResourceInitializationException
	{
		if ( running )
		{
			throw new ResourceInitializationException(new UIMA_IllegalStateException());
		}
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
		clientSideJmxStats.setEndpointName(endpoint);
		int casPoolSize = 1;

//		if (anApplicationContext.containsKey(UimaAsynchronousEngine.ReplyWindow))
//		{
//			receiveWindow = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.ReplyWindow)).intValue();
//			clientSideJmxStats.setReplyWindowSize(receiveWindow);
//		}

		if (anApplicationContext.containsKey(UimaAsynchronousEngine.CasPoolSize))
		{
			casPoolSize = ((Integer) anApplicationContext.get(UimaAsynchronousEngine.CasPoolSize)).intValue();
			clientSideJmxStats.setCasPoolSize(casPoolSize);
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
		if (anApplicationContext.containsKey(UimaAsynchronousEngine.ApplicationName))
		{
			applicationName = (String) anApplicationContext.get(UimaAsynchronousEngine.ApplicationName);
		}

		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_uimaee_client__CONFIG", new Object[] { brokerURI, 0, casPoolSize, processTimeout, metadataTimeout, cpcTimeout });

		try
		{
			jmxManager = new JmxManager("org.apache.uima");
			clientSideJmxStats.setApplicationName(applicationName);
			ObjectName on = new ObjectName("org.apache.uima:name="+applicationName);
			jmxManager.registerMBean(clientSideJmxStats, on);

			initializeProducer(brokerURI, endpoint);
			initializeConsumer(brokerURI);
			running = true;
			sendMetaRequest();
			waitForMetadataReply();
			if (abort || !running)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_aborting_as_WARNING", new Object[] { "Metadata Timeout" });
				throw new ResourceInitializationException(new UimaASMetaRequestTimeout());
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
					((UimaASStatusCallbackListener) listeners.get(i)).initializationComplete(null);
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
	public String deploy(String aDeploymentDescriptor, Map anApplicationContext) throws Exception {
		String springContext = generateSpringContext(aDeploymentDescriptor, anApplicationContext);
		
		SpringContainerDeployer springDeployer =
			new SpringContainerDeployer(springContainerRegistry);
		try
		{
			return springDeployer.deploy(springContext );
		}
		catch( ResourceInitializationException e)
		{
			running = true;
			throw e;
		}
		//return deploySpringContainer(new String[] { springContext });
	}


	/**
	 * 
	 */
	public String deploy(String[] aDeploymentDescriptorList, Map anApplicationContext) throws Exception {
		if (aDeploymentDescriptorList == null) {
			throw new ResourceConfigurationException(UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT, new Object[] { "Null", "DeploymentDescriptorList", "deploy()" });
		}

		if (aDeploymentDescriptorList.length == 0) {
			throw new ResourceConfigurationException(ResourceConfigurationException.MANDATORY_VALUE_MISSING, new Object[] { "DeploymentDescriptorList" });
		}
		String[] springContextFiles = new String[aDeploymentDescriptorList.length];

		for (int i = 0; i < aDeploymentDescriptorList.length; i++) {
			springContextFiles[i] = generateSpringContext(aDeploymentDescriptorList[i], anApplicationContext);
		}

		SpringContainerDeployer springDeployer =
			new SpringContainerDeployer(springContainerRegistry);
		try
		{
			return springDeployer.deploy(springContextFiles);
		}
		catch( ResourceInitializationException e)
		{
			running = true;
			throw e;
		}

	}


	/**
	 * Undeploys Spring container with a given container Id. All deployed Spring
	 * containers are registered in the local registry under a unique id.
	 * 
	 */
	public void undeploy(String aSpringContainerId) throws Exception
		
	{
		if ( aSpringContainerId == null )
		{
			return;
		}

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

	/**
	 * Deploys provided context files ( and beans) in a new Spring container.
	 * 
	 */
	protected String deploySpringContainer(String[] springContextFiles) throws ResourceInitializationException {

		SpringContainerDeployer springDeployer =
			new SpringContainerDeployer();
		try
		{
			return springDeployer.deploy(springContextFiles);
		}
		catch( ResourceInitializationException e)
		{
			// turn on the global flag so that the stop() can do the cleanup
			running = true;
			throw e;
		}	
	}



	protected void waitForServiceNotification() throws Exception
	{
	  
	    synchronized( serviceMonitor )
	    {
	  	  while( !serviceInitializationCompleted )
		  {
		    if ( serviceInitializationException )
		    {
		      throw new ResourceInitializationException();
		    }
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "waitForServiceNotification", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_awaiting_container_init__INFO", new Object[] {});


	      serviceMonitor.wait();
	      if ( serviceInitializationException )
	      {
	        throw new ResourceInitializationException();
	      }
	    }
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
	public void setReleaseCASMessage(TextMessage msg, String aCasReferenceId)
	throws Exception 
	{
		msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
		msg.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request); 
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.ReleaseCAS); 
	}

  public void notifyOnInitializationFailure(Exception e) {

    //  Initialization exception. Notify blocking thread and indicate a problem
    serviceInitializationException = true;
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "notifyOnInitializationFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_container_init_exception__WARNING", new Object[] {e});
    synchronized(serviceMonitor)
    {
      serviceMonitor.notifyAll();
    }
    
  }

  public void notifyOnInitializationSuccess() {
    serviceInitializationCompleted =  true;
    synchronized(serviceMonitor)
    {
      serviceMonitor.notifyAll();
    }
  }

  public void notifyOnTermination(String message) {
    
  }



}
