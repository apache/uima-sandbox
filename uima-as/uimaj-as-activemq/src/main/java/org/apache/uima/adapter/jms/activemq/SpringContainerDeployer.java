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

package org.apache.uima.adapter.jms.activemq;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIDGenerator;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.ControllerCallbackListener;
import org.apache.uima.aae.controller.ControllerLifecycle;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.UimacppServiceController;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.support.destination.DestinationResolver;


public class SpringContainerDeployer implements ControllerCallbackListener {
	private static final Class CLASS_NAME = SpringContainerDeployer.class;

	private boolean serviceInitializationCompleted;
	private boolean serviceInitializationException;
	private Object serviceMonitor = new Object();
	private ConcurrentHashMap springContainerRegistry=null;
	private FileSystemXmlApplicationContext context = null;
	public SpringContainerDeployer(){
	}

	public SpringContainerDeployer( ConcurrentHashMap aSpringContainerRegistry )	{
		springContainerRegistry = aSpringContainerRegistry;
	}
	private UimaDefaultMessageListenerContainer produceListenerConnector(ActiveMQConnectionFactory cf)
	{
		DestinationResolver resolver = new TempDestinationResolver();
		UimaDefaultMessageListenerContainer connector = 
			new UimaDefaultMessageListenerContainer(true);
		
		connector.setConnectionFactory(cf);
		connector.setConcurrentConsumers(1);
		connector.setDestinationResolver(resolver);
		connector.initialize();
		connector.start();
	
		while( connector.getListenerEndpoint() == null )
		{
			synchronized(connector)
			{
				try
				{
					connector.wait(50);
				}
				catch( InterruptedException e) {}
			}
		}
		return connector;
	}
	private ActiveMQConnectionFactory getTopLevelQueueConnectionFactory( ApplicationContext ctx  )
	{
		ActiveMQConnectionFactory factory = null;
		String[] inputChannelBeanIds = ctx.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.JmsInputChannel.class);
		String beanId = null;
		for( int i=0; i < inputChannelBeanIds.length; i++)
		{
			JmsInputChannel inputChannel = (JmsInputChannel)ctx.getBean(inputChannelBeanIds[i]);
			if ( inputChannel.getName().startsWith("top_level_input_queue_service") &&
				 inputChannel instanceof JmsInputChannel )
			{
				factory = ((JmsInputChannel)inputChannel).getConnectionFactory();
				break;
			}
		} 
		return factory;
	}
	
	private int getConcurrentConsumerCount( ApplicationContext ctx )
	{
    String[] listenerBeanIds = ctx.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.UimaDefaultMessageListenerContainer.class);
    String beanId = null;
    int concurrentConsumerCount = -1;
    for( int i=0; i < listenerBeanIds.length; i++)
    {
      UimaDefaultMessageListenerContainer lsnr = (UimaDefaultMessageListenerContainer)ctx.getBean(listenerBeanIds[i]);
      if ( lsnr.getDestinationName().startsWith("asynAggr_retQ"))
      {
        return lsnr.getConcurrentConsumers();
      }
    } 
	  
    return -1;
	  
	}
	
	private void initializeTopLevelController( AnalysisEngineController cntlr, ApplicationContext ctx) 
	throws Exception
	{
		((FileSystemXmlApplicationContext) ctx).setDisplayName(cntlr.getComponentName());
		cntlr.addControllerCallbackListener(this);
		
		String inputQueueName = cntlr.getServiceEndpointName();
		if ( inputQueueName != null )
		{
			if ( ctx.containsBean(inputQueueName) )
			{
				ActiveMQQueue queue = (ActiveMQQueue)ctx.getBean(inputQueueName);
				if ( cntlr.getServiceInfo() != null )
				{
					cntlr.getServiceInfo().setInputQueueName(queue.getQueueName());
				}
			}
			else
			{
				if ( cntlr.getServiceInfo() != null )
				{
					cntlr.getServiceInfo().setInputQueueName(inputQueueName);
				}
			}
		}
		
		
		//	If this is a Cas Multiplier add a special temp queue for receiving Free CAS
		//	notifications.
		if ( cntlr.isCasMultiplier() )
		{
			
			ActiveMQConnectionFactory cf = getTopLevelQueueConnectionFactory( ctx );
			//	Create a listener and a temp queue for Free CAS notifications. 
			UimaDefaultMessageListenerContainer connector =	produceListenerConnector(cf);
			System.out.println(">>>> Cas Multiplier Controller:"+cntlr.getComponentName()+" Activated Listener to Receive Free CAS Notifications - Temp Queue Name:"+connector.getEndpointName());
			//	Direct all messages to the InputChannel 
			connector.setMessageListener(((JmsInputChannel)cntlr.getInputChannel()));
			((JmsInputChannel)cntlr.getInputChannel()).setListenerContainer(connector);
			//	Save the temp queue reference in the Output Channel. The output channel will
			//	add this queue to every outgoing message containing a CAS generated by the
			//	Cas Multiplier.
			((JmsOutputChannel)cntlr.getOutputChannel()).setFreeCasQueue(connector.getListenerEndpoint());
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "initializeTopLevelController", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_activated_fcq__CONFIG", new Object[] { cntlr.getComponentName(), connector.getEndpointName() });
		}

		if (cntlr instanceof AggregateAnalysisEngineController) {
			// Get a map of delegates for the top level aggregate
			Map destinationMap = ((AggregateAnalysisEngineController) cntlr).getDestinations();
			Set set = destinationMap.entrySet();
			// iterate over endpoints (delegates) to find those that
			// need to reply to a temp queue.
			for (Iterator it = set.iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				Endpoint endpoint = (Endpoint) entry.getValue();

				// Will this endpoint reply to a temp queue
				if (endpoint != null && endpoint.isTempReplyDestination()) {
					// block here until the Resolver creates a temp
					// queue and the endpoint initialization is
					// complete. When the resolver creates a temp quee it will
					// notify a listener container with a reference
					// to the temp queue. The listener container will in
					// turn call setDestination on the endpoint to inject
					// the destination object ( a temp queue) for the
					// delegate to reply to.
					while (!endpoint.replyDestinationFailed() && endpoint.getDestination() == null) {
						synchronized (endpoint) {
							try {
								endpoint.wait(200);
							} catch (InterruptedException e) {
							}
						}
					}
					System.out.println("Endpoint:" + endpoint.getEndpoint() + " Configured to Reply To temp queue:" + endpoint.getDestination());

				} else {
					if (endpoint != null) {
						System.out.println("Endpoint:" + endpoint.getEndpoint() + " Configured to Reply To fixed queue");
					}

				}
			}
			int concurrentConsumerCountOnReplies = getConcurrentConsumerCount(ctx);
			// Configure and initialize vm transport in the top level aggregate.
			// The aggregate will initialize all delegates with the vm transport.
			if ( System.getProperty("UseVmTransport") != null )
			{
	      ((AggregateAnalysisEngineController) cntlr).initializeVMTransport(concurrentConsumerCountOnReplies);
			}
			// Complete initialization of the aggregate by sending
			// getMeta requests to
			// all remote delegates (if any). Collocated delegates
			// have already
			// returned their metadata to the aggregate.
			((AggregateAnalysisEngineController) cntlr).sendRequestForMetadataToRemoteDelegates();
		}

	}

	private String initializeContainer( ApplicationContext ctx ) throws Exception
	{
		serviceInitializationCompleted = false;
		serviceInitializationException = false;
		// Wrap Spring context
		UimaEEAdminSpringContext springAdminContext = new UimaEEAdminSpringContext((FileSystemXmlApplicationContext) ctx);
		// Find all deployed instances of the Broker Deployer
		String[] brokerDeployer = ctx.getBeanNamesForType(org.apache.uima.adapter.jms.activemq.BrokerDeployer.class);
		// Find all deployed Controllers
		String[] controllers = ctx.getBeanNamesForType(org.apache.uima.aae.controller.AnalysisEngineController.class);

		for (int i = 0; controllers != null && i < controllers.length; i++) {
			AnalysisEngineController cntlr = (AnalysisEngineController) ctx.getBean(controllers[i]);
			// Pass a reference to the context to each of the Controllers
			cntlr.setUimaEEAdminContext(springAdminContext);
			if (cntlr.isTopLevelComponent()) {
				initializeTopLevelController( cntlr, ctx);
			}
		}

    String[] cppcontrollers = ctx.getBeanNamesForType(org.apache.uima.aae.controller.UimacppServiceController.class);
    for (int i = 0; cppcontrollers != null && i < cppcontrollers.length; i++) {
      UimacppServiceController cntlr = (UimacppServiceController) ctx.getBean(cppcontrollers[i]);
      // register listener
      cntlr.addControllerCallbackListener(this);
    }
    
		// blocks until the top level controller sends a notification.
		// Notification is send
		// when either the controller successfully initialized or it failed
		// during initialization
		try {
			waitForServiceNotification();
		} catch (Exception e) {
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
			if (asyncServiceList != null && asyncServiceList.length > 0) {
				ControllerLifecycle ctrer = (ControllerLifecycle) ctx.getBean(asyncServiceList[0]);
				// Send a trigger to initiate shutdown.
				if (ctrer instanceof AnalysisEngineController) {
					((AnalysisEngineController) ctrer).getControllerLatch().release();
				}
				ctrer.terminate();
			}

			if (ctx instanceof FileSystemXmlApplicationContext) {
				((FileSystemXmlApplicationContext) ctx).destroy();
			}
			throw e;
		}
		String containerId = new UIDGenerator().nextId();
		if ( springContainerRegistry != null )
		{
			// Register each container in a global map. When stopping the
			// client, each container in the map will be stopped.
			synchronized (springContainerRegistry) {
				springContainerRegistry.put(containerId, springAdminContext);
			}
		}
		return containerId;
	}
	
	public String deploy(String springContextFile ) throws ResourceInitializationException {
		if ( springContextFile == null )
		{
			throw new ResourceInitializationException(new Exception("Spring Context File Not Specified"));		
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "deploy", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_deploy_container__CONFIG", new Object[] { springContextFile });
		try {
			// Deploy beans in the Spring container. Although the deployment is
			// synchronous ( one bean at a time), some beans run in a separate
			// threads. The completion of container deployment doesnt
			// necessarily mean that all beans have initialized completely.
		    if (!springContextFile.startsWith("file:")) {
		      springContextFile = "file:" + springContextFile;
		    }
			context = new FileSystemXmlApplicationContext(springContextFile);
			return initializeContainer(context);
		} catch (ResourceInitializationException e) {
			//e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}

	}

	public String deploy(String[] springContextFiles) throws ResourceInitializationException {
		if ( springContextFiles == null )
		{
			throw new ResourceInitializationException(new Exception("Spring Context File List is Empty"));
		}
		//	Log context files
		for (int i = 0; i < springContextFiles.length; i++) {
          if (!springContextFiles[i].startsWith("file:")) {
            springContextFiles[i] = "file:" + springContextFiles[i];
          }
		  UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "deploySpringContainer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_deploy_container__CONFIG", new Object[] { springContextFiles[i] });
		}
		try {
			// Deploy beans in the Spring container. Although the deployment is
			// synchronous ( one bean at a time), some beans run in a separate
			// threads. The completion of container deployment doesnt
			// necessarily mean that all beans have initialized completely.
			context = new FileSystemXmlApplicationContext(springContextFiles);
			return initializeContainer(context);
		} catch (ResourceInitializationException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}

	}
	protected void waitForServiceNotification() throws Exception {

			synchronized (serviceMonitor) {
				while (!serviceInitializationCompleted) {
					if (serviceInitializationException) {
						throw new ResourceInitializationException();
					}
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "waitForServiceNotification", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_awaiting_container_init__INFO", new Object[] {});

				serviceMonitor.wait();
				if (serviceInitializationException) {
					throw new ResourceInitializationException();
				}
			}
		}
	}
	public void notifyOnInitializationFailure(AnalysisEngineController aController, Exception e) {

		// Initialization exception. Notify blocking thread and indicate a
		// problem
		serviceInitializationException = true;
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "notifyOnInitializationFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_container_init_exception__WARNING", new Object[] {e});
		synchronized (serviceMonitor) {
			serviceMonitor.notifyAll();
		}

	}

	public void notifyOnInitializationSuccess(AnalysisEngineController aController) {
		serviceInitializationCompleted = true;
		
		synchronized (serviceMonitor) {
			serviceMonitor.notifyAll();
		}
	}
	public void notifyOnInitializationFailure( Exception e)
	{
		notifyOnInitializationFailure(null, e);
	}
	public void notifyOnInitializationSuccess()
	{
		notifyOnInitializationSuccess(null);
	}

	public void notifyOnTermination(String message) {
	}

	public FileSystemXmlApplicationContext getSpringContext()
	{
		return context;
	}
	
	public boolean isInitialized()
	{
		return serviceInitializationCompleted;
	}
	
	public boolean initializationFailed()
	{
		return serviceInitializationException;
	}
}
