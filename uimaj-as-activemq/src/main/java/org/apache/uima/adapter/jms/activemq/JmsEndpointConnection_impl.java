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

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.advisory.ConsumerEvent;
import org.apache.activemq.advisory.ConsumerListener;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;
import org.springframework.util.Assert;

public class JmsEndpointConnection_impl implements ConsumerListener
{
	private static final Class CLASS_NAME = JmsEndpointConnection_impl.class;

	private Destination destination;

	private Session producerSession;

	private MessageProducer producer;

	private Connection conn;

	private Timer timer;

	private String serverUri;

	private String endpoint;

    private Endpoint delegateEndpoint;

//	private boolean open;

	private long inactivityTimeout = 3600000;

	private Map connectionMap;

	private boolean retryEnabled;

	private AnalysisEngineController controller = null;

//	private boolean remove;

	private boolean connectionAborted = false;

	private long connectionCreationTimestamp = 0L;

	private Object semaphore = new Object();

	private boolean isReplyEndpoint;
	
	public JmsEndpointConnection_impl(Map aConnectionMap, Endpoint anEndpoint)
	{
		connectionMap = aConnectionMap;
		serverUri = anEndpoint.getServerURI();
		if ( anEndpoint.isReplyEndpoint() && 
			 anEndpoint.getDestination() != null && 
			 anEndpoint.getDestination() instanceof ActiveMQDestination )
		{
			endpoint = ((ActiveMQDestination)anEndpoint.getDestination()).getPhysicalName();
		}
		else
		{
			endpoint = anEndpoint.getEndpoint();
		}
		isReplyEndpoint = anEndpoint.isReplyEndpoint();
		anEndpoint.remove();
		delegateEndpoint = anEndpoint;
	}
	public void setAnalysisEngineController(AnalysisEngineController aController)
	{
		controller = aController;
	}

	public boolean isRetryEnabled()
	{
		return retryEnabled;
	}

	public void setRetryEnabled(boolean retryEnabled)
	{
		this.retryEnabled = retryEnabled;
	}

	public boolean isOpen()
	{
		if (producerSession == null)
		{
			return false;
		}
		return ((ActiveMQSession) producerSession).isRunning();
	}

	public void setInactivityTimeout(long aTimeout)
	{
		inactivityTimeout = aTimeout;
	}

	private void openChannel() throws AsynchAEException, ServiceShutdownException
	{
		try
		{
//			if (connectionAborted)
//			{
//				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
//		                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_aborted__FINE",
//		                new Object[] { endpoint, serverUri });
//				throw new ServiceShutdownException();
//			}
			String brokerUri = getServerUri();
			String endpointId = endpoint;
			
			
			
			
			//	If replying to http request, reply to a queue managed by this service broker using tcp protocol
			if ( isReplyEndpoint && brokerUri.startsWith("http") && controller != null && controller.getInputChannel() != null )
			{
				brokerUri = controller.getInputChannel().getServerUri();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "open", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_override_connection_to_endpoint__FINE", new Object[] { controller.getComponentName(), getEndpoint(), controller.getInputChannel().getServerUri() });
			}

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_activemq_open__FINE",
	                new Object[] { endpoint, serverUri });

			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUri);
			factory.setCopyMessageOnSend(false);
			conn = factory.createConnection();
			connectionCreationTimestamp = System.nanoTime();
			producerSession = conn.createSession(false, 0);
			if ( isReplyEndpoint && delegateEndpoint.getDestination() != null )
			{
				producer = producerSession.createProducer(null); 
				if ( controller != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_temp_conn_starting__FINE",
			                new Object[] { controller.getComponentName(), endpoint, serverUri });
				}
			}
			else
			{
				destination = producerSession.createQueue(getEndpoint());
				producer = producerSession.createProducer(destination);
				if ( controller != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_conn_starting__FINE",
		                new Object[] { controller.getComponentName(), endpoint, serverUri });
				}
			}
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// System.out.println("Creating New Connection To Endpoint::"+getEndpoint());
			// ConsumerEventSource evSource = new ConsumerEventSource(conn, (ActiveMQDestination)destination);
			// evSource.setConsumerListener(this);
			// evSource.start();
			conn.start();
			if ( controller != null )
			{

				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_conn_started__FINE",
	                new Object[] { endpoint, serverUri });
			}
			if ( controller != null && controller.getInputChannel() != null)
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_open_to_endpoint__FINE", new Object[] { controller.getComponentName(), getEndpoint(), serverUri });

//			setOpen(true);
			startTimer(connectionCreationTimestamp);
		}
		catch ( Exception e)
		{
			//e.printStackTrace();
			throw new AsynchAEException(e);
		}
		
	}
	public synchronized void open() throws AsynchAEException, ServiceShutdownException
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "open", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_open__FINE",
                new Object[] { endpoint, serverUri });
		if ( !connectionAborted )
		{
		    openChannel();
    }

	}

	public synchronized void abort()
	{
		connectionAborted = true;
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
		this.close();
	}

	public synchronized void close()
	{

//		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "close", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_closing_connection_to_endpoint__FINE", new Object[] { getEndpoint() });
		// if ( remove )
		// {
		// try
		// {
		// // System.out.println("Got HTTP - based Service:"+end.getServerURI());
		// String brokerURI = end.getServerURI().trim();
		// int startPos = brokerURI.indexOf("//")+2;
		// int endPos = brokerURI.lastIndexOf(":");
		// String jmxServer = brokerURI.substring(startPos, endPos)+":1099";
		// JmxManager jmxMgr = new JmxManager("service:jmx:rmi:///jndi/rmi://"+jmxServer+"/jmxrmi", end.getEndpoint());
		// jmxMgr.initialize();
		// jmxMgr.removeQueue();
		// }
		// catch(Exception e){}
		//				
		// }
//		synchronized (semaphore)
//		{
			if (producer != null)
			{
				try
				{
					producer.close();
				}
				catch ( Exception e)
				{
					//	Ignore we are shutting down
				}
			}
			if (producerSession != null)
			{
				try
				{
					producerSession.close();
				}
				catch ( Exception e)
				{
					//	Ignore we are shutting down
				}
				producerSession = null;
			}
			if (destination != null)
			{
				destination = null;
			}
			if (conn != null)// && !( (ActiveMQConnection)conn).isClosed() )
			{
				try
				{
					conn.stop();
					conn.close();
				}
				catch ( Exception e)
				{
					// Ignore this for now. Attempting to close connection that has been closed
					//	Ignore we are shutting down
				}
			}
			conn = null;

//		}
		//setOpen(false);
//		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "close", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_closed_to_endpoint__FINE", new Object[] { getEndpoint() });
	}

	protected String getEndpoint()
	{
		return endpoint;
	}

	protected void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	protected String getServerUri()
	{
		return serverUri;
	}

	protected void setServerUri(String serverUri)
	{
		this.serverUri = serverUri;
	}

	public TextMessage produceTextMessage(String aTextMessage) throws AsynchAEException
	{
		Assert.notNull(producerSession);
		boolean done = false;
		int retryCount = 4;
		while (retryCount > 0)
		{
			try
			{
				retryCount--;
				if (!((ActiveMQSession) producerSession).isRunning())
				{
					open();
				}
				if (aTextMessage == null)
				{
					return producerSession.createTextMessage();
				}
				else
				{
					return producerSession.createTextMessage(aTextMessage);
				}

			}
			catch ( javax.jms.IllegalStateException e)
			{
				try
				{
					open();
				}
				catch ( ServiceShutdownException ex)
				{
					ex.printStackTrace();
				}

			}
			catch ( Exception e)
			{
				throw new AsynchAEException(e);
			}
		}
		throw new AsynchAEException(new InvalidMessageException("Unable to produce Message Object"));
	}

	public ObjectMessage produceObjectMessage() throws AsynchAEException
	{
		Assert.notNull(producerSession);

		try
		{
			if (!((ActiveMQSession) producerSession).isRunning())
			{
				open();
			}
			return producerSession.createObjectMessage();
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	private synchronized void startTimer(long aConnectionCreationTimestamp)
	{
		final long cachedConnectionCreationTimestamp = aConnectionCreationTimestamp;
		Date timeToRun = new Date(System.currentTimeMillis() + inactivityTimeout);
		if (timer != null)
		{
			timer.cancel();
		}
		if (controller != null)
		{
			timer = new Timer("Controller:" + controller.getComponentName() + ":TimerThread-JmsEndpointConnection_impl:" + endpoint + ":" + System.nanoTime());
		}
		else
		{
			timer = new Timer("TimerThread-JmsEndpointConnection_impl:" + endpoint + ":" + System.nanoTime());
		}
		timer.schedule(new TimerTask() {

			public void run()
			{
//				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "startTimer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_inactivity_timer_expired_INFO", new Object[] { Thread.currentThread().getName(), inactivityTimeout, endpoint });
					if (connectionCreationTimestamp <= cachedConnectionCreationTimestamp)
					{
						try
						{
							close();
						}
						finally
						{
							if (connectionMap.containsKey(getEndpoint()))
							{
								connectionMap.remove(getEndpoint());
							}
						}
					}
				timer.cancel();
				timer.purge();
			}
		}, timeToRun);

	}

	public synchronized void stopTimer()
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
	}

	public synchronized boolean send(Message aMessage, boolean startTimer) //throws AsynchAEException
	{
		String destinationName = "";

		Exception lastException = null;
		// Send a message to the destination. Retry 10 times if unable to send.
		// After 10 tries give up and throw exception
		for (int i = 0; i < 10; i++)
		{
			try
			{
				stopTimer();

					if (conn == null || producerSession == null || !((ActiveMQSession) producerSession).isRunning())
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_open_connection_to_endpoint__FINE", new Object[] { getEndpoint() });
						openChannel();
					}
					//	Send a reply to a queue provided by the client
					if ( isReplyEndpoint && delegateEndpoint.getDestination() != null  )
					{
						destinationName = ((ActiveMQDestination)delegateEndpoint.getDestination()).getPhysicalName();
						producer.send((Destination)delegateEndpoint.getDestination(), aMessage);
					}
					else
					{
						
						destinationName = ((ActiveMQQueue) producer.getDestination()).getPhysicalName();
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_endpoint__FINE", new Object[] {destinationName });
						
						
						
						
						
						
						producer.send(aMessage);
					}
				if (startTimer)
				{
					startTimer(connectionCreationTimestamp);
				}
				lastException = null;
				return true;
			}
			catch ( Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_not_ableto_send_msg_INFO", new Object[] { controller.getComponentName(), destinationName, i+1, 10 });
				//e.printStackTrace();
				lastException = e;
			}
			try
			{
				wait(50);
			}
			catch ( Exception ex)
			{
			}
		}
		if ( lastException != null )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[] { controller.getComponentName(), lastException});
		}
		stopTimer();
		return false;
//		throw new AsynchAEException(lastException);
	}

	public void onConsumerEvent(ConsumerEvent arg0)
	{
		// System.out.println(" Received Event from "+((ActiveMQDestination)arg0.getDestination()).getPhysicalName()+" Consumer Count::"+arg0.getConsumerCount());
		if (controller != null)
		{
			controller.handleDelegateLifeCycleEvent(getEndpoint(), arg0.getConsumerCount());
		}
	}

	protected void finalize() throws Throwable
	{
		if ( timer != null)
		{
			timer.cancel();
		}
		controller = null;
	}
	
	
}
