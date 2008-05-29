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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.Endpoint_impl;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.error.handler.GetMetaErrorHandler;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class UimaDefaultMessageListenerContainer extends DefaultMessageListenerContainer
implements ExceptionListener
{
	private static final Class CLASS_NAME = UimaDefaultMessageListenerContainer.class;
	private String destinationName="";
	private Endpoint endpoint;
	private AnalysisEngineController controller;
	private int retryCount = 2;
	public UimaDefaultMessageListenerContainer()
	{
		super();
		setRecoveryInterval(20000);
		//setAcceptMessagesWhileStopping(false);
		setExceptionListener(this);
		
	}
	public void setController( AnalysisEngineController aController)
	{
		controller = aController;
	}
	private boolean disableListener( Throwable t)
	{
		System.out.println(t.toString());
		if ( t.toString().indexOf("SharedConnectionNotInitializedException") > 0 ||	
			 ( t instanceof JMSException && t.getCause() != null && t.getCause() instanceof ConnectException ) )
			return true;
		return false;
	}
	protected void handleListenerSetupFailure( Throwable t, boolean alreadyHandled )
	{
		if ( !(t instanceof javax.jms.IllegalStateException ) )
		{
			t.printStackTrace();
			final String endpointName = 
				(getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
				
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
	                new Object[] {  endpointName, getBrokerUrl(), t });

/*
				while ( retryCount > 0 )
			{
				retryCount--;
				String brokerURL = ((ActiveMQConnectionFactory)getConnectionFactory()).getBrokerURL();
				System.out.println(">>>>> Retrying Connection To Broker:"+brokerURL+" Endpoint:"+endpoint.getEndpoint()+" Sleeping For 1 Minute Before Retry. Retries Remaining:"+retryCount);
				//	Retry the connection
				try
				{
					establishSharedConnection();
					synchronized(this)
					{
						wait(60000);
					}
					break;
				}
				catch( Exception e) {}
			}
*/
			boolean terminate = true;
			if (  disableListener(t) )
			{
				if ( endpoint != null )
				{
					endpoint.setReplyDestinationFailed();
					//	If this is a listener attached to the Aggregate Controller, use GetMeta Error
					//	Thresholds defined to determine what to do next after failure. Either terminate
					//	the service or disable the delegate with which this listener is associated with
					if ( controller != null && controller instanceof AggregateAnalysisEngineController )
					{
						ErrorHandler handler = null;
						Iterator it = controller.getErrorHandlerChain().iterator();
						//	Find the error handler for GetMeta in the Error Handler List provided in the 
						//	deployment descriptor
						while ( it.hasNext() )
						{
							handler = (ErrorHandler)it.next();
							if ( handler instanceof GetMetaErrorHandler )
							{
								break;
							}
						}
						//	Fetch a Map containing thresholds for GetMeta for each delegate. 
						java.util.Map thresholds = handler.getEndpointThresholdMap(); 
						//	Lookup delegate's key using delegate's endpoint name
						String delegateKey = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(endpoint.getEndpoint());
						//	If the delegate has a threshold defined on GetMeta apply Action defined 
						if ( delegateKey != null && thresholds.containsKey(delegateKey))
						{
							//	Fetcg the Threshold object containing error configuration
							Threshold threshold = (Threshold) thresholds.get(delegateKey);
							//	Check if the delegate needs to be disabled
							if (threshold.getAction().equalsIgnoreCase(ErrorHandler.DISABLE)) {
								//	The disable delegate method takes a list of delegates
								List list = new ArrayList();
								//	Add the delegate to disable to the list
								list.add(delegateKey);
								try {
									System.out.println(">>>> Controller:"+controller.getComponentName()+" Disabling Listener On Queue:"+endpoint.getEndpoint()+". Component's "+delegateKey+" Broker:"+getBrokerUrl()+" is Invalid");
									UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
							                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_disabled_delegate_bad_broker__INFO",
							                new Object[] {  controller.getComponentName(), delegateKey, getBrokerUrl() });
									//	Remove the delegate from the routing table. 
									((AggregateAnalysisEngineController) controller).disableDelegates(list);
								} catch (Exception e) {
									e.printStackTrace();
								}
								terminate = false; //just disable the delegate and continue
							}
						}
					}
				}

				System.out.println("****** Unable To Connect Listener To Broker:"+getBrokerUrl());
				if ( endpoint != null )
				{
					System.out.println("****** Closing Listener on Queue:"+endpoint.getEndpoint());
				}
				setRecoveryInterval(0);
				
				//	Spin a shutdown thread to terminate listener. 
				new Thread() {
					public void run()
					{
						try
						{
							UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
					                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_disable_listener__WARNING",
					                new Object[] {  endpointName, getBrokerUrl() });
							
							shutdown();
						}
						catch( Exception e) { e.printStackTrace();}
					}
				}.start();

				if ( terminate )
				{
					// ****************************************
					//	terminate the service
					// ****************************************
					System.out.println(">>>> Terminating Controller:"+controller.getComponentName()+" Unable To Initialize Listener Due to Invalid Broker URL:"+getBrokerUrl());
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
			                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_terminate_service_dueto_bad_broker__WARNING",
			                new Object[] {  controller.getComponentName(), getBrokerUrl() });
					controller.stop();
					controller.notifyListenersWithInitializationStatus(new ResourceInitializationException(t));
				}
			}
			else
			{
				super.handleListenerSetupFailure(t, false);
			}
		}
		
		
	}

	protected void handleListenerException( Throwable t )
	{
      String endpointName = 
        (getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
      
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                "handleListenerException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
                new Object[] {  endpointName, getBrokerUrl(), t });
			super.handleListenerException(t);
		
	}

	public void afterPropertiesSet()
	{
		super.afterPropertiesSet();
		try
		{
			System.setProperty("BrokerURI", ((ActiveMQConnectionFactory)super.getConnectionFactory()).getBrokerURL());

			Destination destination = super.getDestination();
			if (destination != null && destination instanceof ActiveMQDestination )
			{
				destinationName = ((ActiveMQDestination)destination).getPhysicalName();
			}
			if ( getMessageListener() instanceof JmsInputChannel )
			{
				((JmsInputChannel)getMessageListener()).setListenerContainer(this);
			}
			else if ( getMessageListener() instanceof ModifiableListener)
			{
				((ModifiableListener)getMessageListener()).setListener(this);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "afterPropertiesSet", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                new Object[] {Thread.currentThread().getId(), e});
		}
	}
	
	public String getDestinationName()
	{
		
		return destinationName;
	}
	public String getEndpointName()
	{
		if ( getDestination() != null )
		{
			return ((ActiveMQDestination)getDestination()).getPhysicalName();
		}
		return null;
	}
	public String getBrokerUrl()
	{
		return ((ActiveMQConnectionFactory)super.getConnectionFactory()).getBrokerURL();
	}
	
	public void setDestinationResolver( DestinationResolver resolver )
	{
		((TempDestinationResolver)resolver).setListener(this);
		super.setDestinationResolver(resolver);
	}
	public void closeConnection() throws Exception
	{
		try
		{
			setRecoveryInterval(0);
			setAutoStartup(false);
			getSharedConnection().close();
		}
		catch( Exception e)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "closeConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                new Object[] {Thread.currentThread().getId(), e});
			
		}
	}
	public void setDestination( Destination aDestination )
	{
		super.setDestination(aDestination);
		if ( endpoint != null)
		{
			endpoint.setDestination(aDestination);
			endpoint.setServerURI(getBrokerUrl());
		}
	}
	public Destination getListenerEndpoint()
	{
		return getDestination(); 
	}

	public void onException(JMSException arg0)
	{
       String endpointName = 
      (getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
    
    UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
              "onException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
              new Object[] {  endpointName, getBrokerUrl(), arg0});
	}

	public void setTargetEndpoint( Endpoint anEndpoint )
	{
		endpoint = anEndpoint;
	}

}
