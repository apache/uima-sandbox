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

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.Endpoint_impl;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class UimaDefaultMessageListenerContainer extends DefaultMessageListenerContainer
implements ExceptionListener
{
	private static final Class CLASS_NAME = UimaDefaultMessageListenerContainer.class;
	private String destinationName="";
	private Endpoint endpoint;
	
	public UimaDefaultMessageListenerContainer()
	{
		super();
		setRecoveryInterval(60000);
		setAcceptMessagesWhileStopping(false);
		setExceptionListener(this);
	}
	private boolean disableListener( Throwable t)
	{
		
		if ( t instanceof AbstractJmsListeningContainer.SharedConnectionNotInitializedException ||
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
			

			if ( disableListener(t) )
			{
				if ( endpoint != null )
				{
					endpoint.setReplyDestinationFailed();
				}
				System.out.println("****** Unable To Connect Listener To Broker:"+getBrokerUrl());
				System.out.println("****** Closing Listener on Queue:"+endpointName);
				setRecoveryInterval(0);
				
				//	Spin a shutdown thread to terminate listener. The thread is needed due
				//	to Spring. 
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
			}
			else
			{
				super.handleListenerSetupFailure(t, false);
			}
		}
		
		
	}

	protected void handleListenerException( Throwable t )
	{
		  t.printStackTrace();
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
