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
			final String endpoint = ((ActiveMQQueue)getDestination()).getPhysicalName();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_listener_exception__WARNING",
	                new Object[] {  endpoint, getBrokerUrl() });

			if ( disableListener(t) )
			{
				System.out.println("****** Unable To Connect Listener To Broker:"+getBrokerUrl());
				System.out.println("****** Closing Listener on Queue:"+endpoint);
				setRecoveryInterval(0);
				
				//	Spin a shutdown thread to terminate listener. The thread is needed due
				//	to Spring. 
				new Thread() {
					public void run()
					{
						try
						{
							UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
					                "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_diable_listener__WARNING",
					                new Object[] {  endpoint, getBrokerUrl() });
							
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
		
		return ((ActiveMQDestination)getDestination()).getPhysicalName();
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
		System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	public void setTargetEndpoint( Endpoint anEndpoint )
	{
		endpoint = anEndpoint;
	}


}
