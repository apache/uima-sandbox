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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ObjectName;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQBytesMessage;
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
import org.apache.uima.aae.controller.UimacppServiceController;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.delegate.Delegate.DelegateEntry;
import org.apache.uima.aae.error.UimaASMetaRequestTimeout;
import org.apache.uima.aae.jmx.JmxManager;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.SpringContainerDeployer;
import org.apache.uima.adapter.jms.activemq.UimaEEAdminSpringContext;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngineCommon_impl.ClientRequest;
import org.apache.uima.adapter.jms.service.Dd2spring;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.internal.util.UUIDGenerator;
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

	//private Connection connection = null;
	private volatile boolean serviceInitializationException;
	private volatile boolean serviceInitializationCompleted;
	
	private Object serviceMonitor = new Object();
	
	private Queue consumerDestination = null;
	private Session producerSession = null;
	private JmxManager jmxManager = null;
	private String applicationName = "UimaASClient";
	//private volatile boolean usesSharedConnection = false;
	private static SharedConnection sharedConnection = null;
	
	public BaseUIMAAsynchronousEngine_impl() {
        UIMAFramework.getLogger(CLASS_NAME).log(Level.INFO, "UIMA-AS version " + UIMAFramework.getVersionString());
	}


	protected TextMessage createTextMessage() throws ResourceInitializationException
	{
		return 	new ActiveMQTextMessage();
	}
  protected BytesMessage createBytesMessage() throws ResourceInitializationException
  {
    return  new ActiveMQBytesMessage();
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
	protected void setMetaRequestMessage(Message msg) throws Exception
	{
		msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());

		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.GetMeta);
		msg.setJMSReplyTo(consumerDestination);
		if ( msg instanceof TextMessage ) {
	    ((ActiveMQTextMessage) msg).setText("");
		}
	}
	/**
	 * Initialize JMS Message with properties relevant to Process CAS request.
	 */
	protected void setCASMessage(String aCasReferenceId, CAS aCAS, Message msg) throws ResourceProcessException
	{
		try{
			setCommonProperties(aCasReferenceId, msg, "xmi");
			((TextMessage)msg).setText(serializeCAS(aCAS));
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
	
	protected void setCASMessage(String aCasReferenceId, String aSerializedCAS, Message msg) throws ResourceProcessException
	{
		try{
			setCommonProperties(aCasReferenceId, msg, "xmi");
			((TextMessage)msg).setText(aSerializedCAS);
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}
  protected void setCASMessage(String aCasReferenceId, byte[] aSerializedCAS, Message msg) throws ResourceProcessException
  {
    try{
      setCommonProperties(aCasReferenceId, msg, "binary");
      ((BytesMessage)msg).writeBytes(aSerializedCAS);
    }
    catch (Exception e)
    {
      throw new ResourceProcessException(e);
    }
  }

  protected void setCommonProperties( String aCasReferenceId, Message msg, String aSerializationStrategy) throws ResourceProcessException
    {
		try{
			msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());
	
			msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
			msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
			msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.Process);
			msg.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
			
			if ( aSerializationStrategy.equals("binary")) {
	      msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.BinaryPayload);
			} else if ( aSerializationStrategy.equals("xmi")) {
        msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.XMIPayload);
			}

			msg.setBooleanProperty(AsynchAEMessage.AcceptsDeltaCas, true);
			msg.setJMSReplyTo(consumerDestination);
			
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
    	
    }
	public void stop()
	{
	  if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST) ) {
	    UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});
	  }
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
			  try {
	        consumerSession.close();
	        consumer.close();
			  } catch ( JMSException exx) {}
	      // SharedConnection object manages a single JMS connection to the 
	      // broker. If the client is scaled out in the same JVM, the connection
	      // is shared by all instances of the client to reduce number of threads
	      // in the broker. The SharedConnection object also maintains the number
	      // of client instances to determine when it is ok to close the connection.
	      // The connection is closed when the last client calls stop().
	      if ( sharedConnection != null ) {
	        // Decrement number of active clients
	        sharedConnection.decrementClientCount();
	        // The destroy method closes the JMS connection when the number of
	        // clients becomes 0, otherwise it is a no-op
	        sharedConnection.destroy();
	      }
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

	public void setCPCMessage(Message msg) throws Exception
	{
		msg.setStringProperty(AsynchAEMessage.MessageFrom, consumerDestination.getQueueName());
		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
		msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.CollectionProcessComplete);
		msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None);
		msg.setBooleanProperty(AsynchAEMessage.RemoveEndpoint, true);
		msg.setJMSReplyTo(consumerDestination);
    if ( msg instanceof TextMessage ) {
      ((TextMessage)msg).setText("");
    }
	}
	protected synchronized void setupConnection( String aBrokerURI ) throws Exception
	{
		if (sharedConnection == null )
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(aBrokerURI);
			Connection connection = factory.createConnection();
			// This only effects Consumer
			addPrefetch((ActiveMQConnection)connection);
			connection.start();
			sharedConnection = new SharedConnection();
			sharedConnection.setConnection(connection);
		} 
	}

	private void addPrefetch(ActiveMQConnection aConnection ) {
    ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
    prefetchPolicy.setQueuePrefetch(5);
    ((ActiveMQConnection)aConnection).setPrefetchPolicy(prefetchPolicy);
	}
	private void validateConnection(String aBrokerURI) throws Exception
	{
		if (sharedConnection == null)	{
			setupConnection(aBrokerURI);
		}
	}
	protected Session getSession(String aBrokerURI) throws Exception
	{
		validateConnection(aBrokerURI);
		return getSession(sharedConnection.getConnection());
	}
  protected Session getSession(Connection aConnection) throws Exception
  {
    session = aConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    return session;
  }

	protected MessageProducer lookupProducerForEndpoint( Endpoint anEndpoint ) throws Exception
	{
		if ( sharedConnection == null || producerSession == null )
		{
			throw new ResourceInitializationException();
		}
		Destination dest = producerSession.createQueue(anEndpoint.getEndpoint());
		return producerSession.createProducer(dest);
	}
  public void initializeProducer(String aBrokerURI, String aQueueName) throws Exception {
    setupConnection(aBrokerURI);
    initializeProducer(aBrokerURI, aQueueName, sharedConnection.getConnection());
  }

  public void initializeProducer(String aBrokerURI, String aQueueName, Connection aConnection) throws Exception
	{
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "initializeProducer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_jms_producer_INFO", new Object[] { aBrokerURI, aQueueName });
    }
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
			new ActiveMQMessageSender( aConnection, super.pendingMessageList, aQueueName, this);
		producerInitialized = false;
		Thread t = new Thread( (BaseMessageSender) sender);
		//	Start the worker thread. The jms session and message producer are created. Once
		//	the message producer is created, the worker thread notifies this thread by
		//	calling onProducerInitialized() where the global flag 'producerInitialized' is 
		//	set to true. After the notification, the worker thread notifies this instance
		//	that the producer is fully initialized and finally begins to wait for messages
		//	in pendingMessageList. Upon arrival, each message is removed from 
		//	pendingMessageList and it is sent to a destination.
		
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
	    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "initializeProducer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_worker_thread_failed_to_initialize__WARNING", new Object[] { sender.getReasonForFailure() });
	    }
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
  public void initializeConsumer(String aBrokerURI) throws Exception {
    setupConnection(aBrokerURI);
    initializeConsumer(aBrokerURI, sharedConnection.getConnection());
  }

  public void initializeConsumer(String aBrokerURI, Connection connection) throws Exception
	{
		consumerSession = getSession(connection);
		consumerDestination = consumerSession.createTemporaryQueue();
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "initializeConsumer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_jms_consumer_INFO", new Object[] { aBrokerURI, consumerDestination.getQueueName() });
    }
		consumer = consumerSession.createConsumer(consumerDestination);
		consumer.setMessageListener(this);
		System.out.println(">>>> Client Activated Temp Reply Queue:"+consumerDestination.getQueueName());
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
    if (anApplicationContext.containsKey(UimaAsynchronousEngine.SerializationStrategy))
    {
      super.serializationStrategy = (String) anApplicationContext.get(UimaAsynchronousEngine.SerializationStrategy);
    }

    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.CONFIG) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_init_uimaee_client__CONFIG", new Object[] { brokerURI, 0, casPoolSize, processTimeout, metadataTimeout, cpcTimeout });
    }
    super.serviceDelegate = new ClientServiceDelegate(endpoint,applicationName,this);
    super.serviceDelegate.setCasProcessTimeout(processTimeout);
    super.serviceDelegate.setGetMetaTimeout(metadataTimeout);
		try
		{
		  //  Generate unique identifier
		  String uuid = UUIDGenerator.generate();
		  //  JMX does not allow ':' in the ObjectName so replace these with underscore
		  uuid = uuid.replaceAll(":", "_");
      uuid = uuid.replaceAll("-", "_");
		  applicationName += "_"+uuid;
		  jmxManager = new JmxManager("org.apache.uima");
			clientSideJmxStats.setApplicationName(applicationName);
			ObjectName on = new ObjectName("org.apache.uima:name="+applicationName);
			jmxManager.registerMBean(clientSideJmxStats, on);

      // Reuse existing JMS connection if available 
	    if (sharedConnection != null )  {
        initializeProducer(brokerURI, endpoint, sharedConnection.getConnection());
        initializeConsumer(brokerURI, sharedConnection.getConnection());
	    } else {
	      //  This call creates a SharedConnection object and a JMS Connection
	      initializeProducer(brokerURI, endpoint);
	      initializeConsumer(brokerURI);
	    }
	    // Increment number of client instances. SharedConnection object is a static
	    // and is used to share a single JMS connection. The connection is closed 
	    // when the last client finishes processing and calls stop().
	    sharedConnection.incrementClientCount();
			running = true;
			sendMetaRequest();
			waitForMetadataReply();
			if (abort || !running)
			{
		    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_aborting_as_WARNING", new Object[] { "Metadata Timeout" });
		    }
				throw new ResourceInitializationException(new UimaASMetaRequestTimeout());
			}
			else
			{
				if (collectionReader != null)
				{
					asynchManager.addMetadata(collectionReader.getProcessingResourceMetaData());
				}

				asynchManager.initialize(casPoolSize, "ApplicationCasPoolContext", performanceTuningSettings);

				//	Create a special CasPool of size 1 to be used for deserializing CASes from a Cas Multiplier
				if ( super.resourceMetadata != null && super.resourceMetadata instanceof AnalysisEngineMetaData )
				{
					if ( ((AnalysisEngineMetaData) super.resourceMetadata).getOperationalProperties().getOutputsNewCASes() )
					{
						//	Create a Shadow CAS Pool used to de-serialize CASes produced by a CAS Multiplier
						asynchManager.initialize(1, SHADOW_CAS_POOL, performanceTuningSettings);
					}
				}
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
		  notifyOnInitializationFailure(e);
		  throw e;
		}
		catch (Exception e)
		{
      notifyOnInitializationFailure(e);
			throw new ResourceInitializationException(e);
		}
		if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	    UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_as_initialized__INFO", new Object[] { super.serializationStrategy });
		}

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

  public void undeploy(String aSpringContainerId) throws Exception {
    this.undeploy( aSpringContainerId, SpringContainerDeployer.STOP_NOW);
  }

	/**
	 * Undeploys Spring container with a given container Id. All deployed Spring
	 * containers are registered in the local registry under a unique id.
	 * 
	 */
	public void undeploy(String aSpringContainerId, int stop_level) throws Exception
		
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
			  return;
				//throw new InvalidContainerException("Invalid Spring container Id:" + aSpringContainerId + ". Unable to undeploy the Spring container");
			}
			// Fetch an administrative context which contains a Spring Container
			adminContext = (UimaEEAdminSpringContext) springContainerRegistry.get(aSpringContainerId);
			if ( adminContext == null )
			{
				throw new InvalidContainerException("Spring Container Does Not Contain Valid UimaEEAdminSpringContext Object");
			}
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
			  boolean topLevelController = false;
        ControllerLifecycle ctrer = null;
        int indx = 0;
			  while( !topLevelController ) {
			    ctrer = (ControllerLifecycle) ctx.getBean(asyncServiceList[indx++]);
			    if ( ctrer instanceof UimacppServiceController || 
			         ((AnalysisEngineController)ctrer).isTopLevelComponent() ) {
			      topLevelController = true;
			    }
			  }
				// Send a trigger to initiate shutdown.
				if (ctrer != null )	{
				  if ( ctrer instanceof AnalysisEngineController) {
	          ((AnalysisEngineController) ctrer).getControllerLatch().release();
				  }
				  switch( stop_level ) {
				    case SpringContainerDeployer.QUIESCE_AND_STOP:
				      ((AnalysisEngineController)ctrer).quiesceAndStop();
				      break;
            case SpringContainerDeployer.STOP_NOW:
              ((AnalysisEngineController)ctrer).terminate();
              break;
				  }
				}
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
		    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "waitForServiceNotification", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_awaiting_container_init__INFO", new Object[] {});
		    }

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
		msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
		msg.setJMSReplyTo(consumerDestination);
	}

  public void notifyOnInitializationFailure(Exception e) {
	  notifyOnInitializationFailure(null, e);
  }

  public void notifyOnInitializationSuccess() {
	  notifyOnInitializationSuccess(null);
  }

  public void notifyOnInitializationFailure(AnalysisEngineController aController, Exception e) {

	    //  Initialization exception. Notify blocking thread and indicate a problem
	    serviceInitializationException = true;
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "notifyOnInitializationFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_container_init_exception__WARNING", new Object[] {e});
      }
	    synchronized(serviceMonitor)
	    {
	      serviceMonitor.notifyAll();
	    }
	  }

	  public void notifyOnInitializationSuccess(AnalysisEngineController aController) {
	    serviceInitializationCompleted =  true;
	    synchronized(serviceMonitor)
	    {
	      serviceMonitor.notifyAll();
	    }
	  }

	  public void notifyOnTermination(String message) {
    
  }

	protected MessageProducer getMessageProducer( Destination destination ) throws Exception
	{
		return sender.getMessageProducer(destination);
	}
  /**
   * Request Uima AS client to initiate sending Stop requests to a service for all outstanding
   * CASes awaiting reply. 
   * 
   */
  public void stopProducingCases() {
    List<DelegateEntry> outstandingCasList = 
      serviceDelegate.getDelegateCasesPendingRepy();
    for( DelegateEntry entry : outstandingCasList) {
        // The Cas is still being processed
        ClientRequest clientCachedRequest =
          (ClientRequest)clientCache.get(entry.getCasReferenceId());
        if ( clientCachedRequest != null && 
             !clientCachedRequest.isMetaRequest() && 
             clientCachedRequest.getCasReferenceId() != null) {
          stopProducingCases(clientCachedRequest);
        }
    }
  }
  /**
   * Request Uima AS client to initiate sending Stop request to a service for a given CAS id
   * If the service is a Cas Multiplier, it will stop producing new CASes, will wait until all 
   * child CASes finish and finally returns the input CAS. 
   * 
   */
  public void stopProducingCases(String aCasReferenceId) {
    // The Cas is still being processed
    ClientRequest clientCachedRequest =
        (ClientRequest)clientCache.get(aCasReferenceId);
    if ( clientCachedRequest != null ) {
       stopProducingCases(clientCachedRequest);
    }
  }
  private void stopProducingCases(ClientRequest clientCachedRequest) {
    try {
      if ( clientCachedRequest.getFreeCasNotificationQueue() != null ) {
        TextMessage msg = createTextMessage();
        msg.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
        msg.setStringProperty(AsynchAEMessage.CasReference, clientCachedRequest.getCasReferenceId());
        msg.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request); 
        msg.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.Stop);
        msg.setStringProperty(UIMAMessage.ServerURI, brokerURI);
        try {
          MessageProducer msgProducer = 
            getMessageProducer(clientCachedRequest.getFreeCasNotificationQueue());
          if ( msgProducer != null ) {
            System.out.println(">>> Client Sending Stop to Service for CAS:"+clientCachedRequest.getCasReferenceId()+" Destination:"+clientCachedRequest.getFreeCasNotificationQueue() );
            //  Send STOP message to Cas Multiplier Service
            msgProducer.send(msg);
          }
        } catch( Exception ex) {
          System.out.println("Client Unable to send STOP Request to Service. Reason:");
          ex.printStackTrace();
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "notifyOnInitializationFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[] {Thread.currentThread().getId(), ex});
          }
        }
      }
    } catch ( Exception e) {
      e.printStackTrace();
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "notifyOnInitializationFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[] {Thread.currentThread().getId(), e});
      }
    }
  }
}
