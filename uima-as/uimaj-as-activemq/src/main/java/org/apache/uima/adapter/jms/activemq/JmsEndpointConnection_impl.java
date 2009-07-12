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

import javax.activity.InvalidActivityException;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.advisory.ConsumerEvent;
import org.apache.activemq.advisory.ConsumerListener;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.JmsOutputChannel.BrokerConnectionEntry;
import org.apache.uima.util.Level;
import org.springframework.jms.JmsException;
import org.springframework.util.Assert;

public class JmsEndpointConnection_impl implements ConsumerListener
{
	private static final Class CLASS_NAME = JmsEndpointConnection_impl.class;

	private Destination destination;

	private Session producerSession;

	private MessageProducer producer;

	private BrokerConnectionEntry brokerDestinations;
	
	private Timer timer;

	private String serverUri;

	private String endpoint;

	private String endpointName;

    private Endpoint delegateEndpoint;

	private long inactivityTimeout = 3600000;

	private volatile boolean retryEnabled;

	private AnalysisEngineController controller = null;

	private volatile boolean connectionAborted = false;

	private long connectionCreationTimestamp = 0L;

	private Object semaphore = new Object();

	private boolean isReplyEndpoint;
	
	private volatile boolean  failed = false;
	
	private Object recoveryMux = new Object();
	
	
	
  public JmsEndpointConnection_impl(BrokerConnectionEntry aBrokerDestinationMap, Endpoint anEndpoint)
	{
    brokerDestinations = aBrokerDestinationMap;
		serverUri = anEndpoint.getServerURI();
    isReplyEndpoint = anEndpoint.isReplyEndpoint();

    if ( ( anEndpoint.getCommand() == AsynchAEMessage.Stop || isReplyEndpoint ) && 
			 anEndpoint.getDestination() != null && 
			 anEndpoint.getDestination() instanceof ActiveMQDestination )
		{
			endpoint = ((ActiveMQDestination)anEndpoint.getDestination()).getPhysicalName();
		}
		else
		{
			endpoint = anEndpoint.getEndpoint();
		}
		anEndpoint.remove();
		delegateEndpoint = anEndpoint;
	}
	public synchronized void setAnalysisEngineController(AnalysisEngineController aController)
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
	
	private synchronized void openChannel() throws AsynchAEException, ServiceShutdownException
	{
		try
		{
			String brokerUri = getServerUri();
			
			//	If replying to http request, reply to a queue managed by this service broker using tcp protocol
			if ( isReplyEndpoint && brokerUri.startsWith("http") && controller != null && controller.getInputChannel() != null )
			{
				org.apache.uima.aae.InputChannel iC = controller.getInputChannel(controller.getName());
				if ( ( brokerUri == null || brokerUri.trim().length() == 0 ) && iC != null )
				{
					brokerUri = iC.getServiceInfo().getBrokerURL();
				}
				
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "open", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_override_connection_to_endpoint__FINE", new Object[] { controller.getComponentName(), getEndpoint(), controller.getInputChannel().getServerUri() });
        }
			}

      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_activemq_open__FINE",
	                new Object[] { endpoint, serverUri });
      }
      
      if ( brokerDestinations.getConnection() == null ) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUri);
        factory.setDispatchAsync(true);
        factory.setUseAsyncSend(true);
        factory.setCopyMessageOnSend(false);
        Connection conn = factory.createConnection();
        brokerDestinations.setConnection(conn);
      }
			connectionCreationTimestamp = System.nanoTime();
			producerSession = brokerDestinations.getConnection().createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
			if ( (delegateEndpoint.getCommand() == AsynchAEMessage.Stop || isReplyEndpoint ) && delegateEndpoint.getDestination() != null )
			{
				producer = producerSession.createProducer(null); 
				if ( controller != null )
				{
	        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_temp_conn_starting__FINE",
			                new Object[] { controller.getComponentName(), endpoint, serverUri });
	        }
				}
			}
			else
			{
				destination = producerSession.createQueue(getEndpoint());
				producer = producerSession.createProducer(destination);
				if ( controller != null )
				{
	        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_conn_starting__FINE",
		                new Object[] { controller.getComponentName(), endpoint, serverUri });
	        }
				}
			}
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// Since the connection is shared, start it only once 
			if ( !((ActiveMQConnection)brokerDestinations.getConnection()).isStarted() ) {
	      brokerDestinations.getConnection().start();
			}
			if ( controller != null )
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_conn_started__FINE",
	                new Object[] { endpoint, serverUri });
          if ( controller.getInputChannel() != null ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "openChannel", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_open_to_endpoint__FINE", new Object[] { controller.getComponentName(), getEndpoint(), serverUri });
          }
        }
			}
		}
		catch ( Exception e)
		{
      if ( e instanceof JMSException ) {
        handleJmsException( (JMSException)e );
      }
			throw new AsynchAEException(e);
		}
	}
	public synchronized void open() throws AsynchAEException, ServiceShutdownException
	{
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "open", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_open__FINE",
                new Object[] { endpoint, serverUri });
    }
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
		try {
	    this.close();
		  
		} catch( Exception e) {
		}
	}

	public synchronized void close() throws Exception
	{
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
			if (brokerDestinations.getConnection() != null &&
			    !((ActiveMQConnection)brokerDestinations.getConnection()).isClosed() )
			{
				try
				{
				  brokerDestinations.getConnection().stop();
				  brokerDestinations.getConnection().close();
				}
				catch ( Exception e)
				{
					// Ignore this for now. Attempting to close connection that has been closed
					//	Ignore we are shutting down
				}
			}
			brokerDestinations.setConnection(null);

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
  public BytesMessage produceByteMessage() throws AsynchAEException
  {
    Assert.notNull(producerSession);
    boolean done = false;
    int retryCount = 4;
    while (retryCount > 0)
    {
      try
      {
        retryCount--;
        return producerSession.createBytesMessage();
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
    throw new AsynchAEException(new InvalidMessageException("Unable to produce BytesMessage Object"));
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
	private long getConnectionCreationTimeout()
	{
		return connectionCreationTimestamp;
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
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.CONFIG)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "startTimer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_inactivity_timer_expired_CONFIG", new Object[] { Thread.currentThread().getName(), inactivityTimeout, endpoint });
        }
				if (connectionCreationTimestamp <= cachedConnectionCreationTimestamp)
					{
						try
						{
							close();
						}
						catch( Exception e) {
						}
						finally
						{
	              String key = delegateEndpoint.getEndpoint()+delegateEndpoint.getServerURI();
	              String destination = delegateEndpoint.getEndpoint();
	              if ( delegateEndpoint.getDestination() != null && delegateEndpoint.getDestination() instanceof ActiveMQDestination )
	              {
	                destination = ((ActiveMQDestination)delegateEndpoint.getDestination()).getPhysicalName();
	                key = destination;
	              }
	              if ( brokerDestinations.endpointExists(key) ) {
	                brokerDestinations.removeEndpoint(key);
	              }
						}
					}
					cancelTimer();
			}
		}, timeToRun);
	}
	private synchronized void cancelTimer()
	{
		if (timer != null)
		{
			timer.cancel();
			timer.purge();
		}
	}
	public  void stopTimer()
	{
		cancelTimer();
		synchronized(this)
		{
			timer = null;
		}
	}

	public boolean send(final Message aMessage, long msgSize, boolean startTimer) 
	{
		String destinationName = "";

		try
		{
			  stopTimer();
        int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
        int command = aMessage.getIntProperty(AsynchAEMessage.Command);

				if ( failed || brokerDestinations.getConnection() == null || producerSession == null || !((ActiveMQSession) producerSession).isRunning())
				{
	        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_open_connection_to_endpoint__FINE", new Object[] { getEndpoint() });
	        }
					openChannel();
					// The connection has been successful. Now check if we need to create a new listener
					// and a temp queue to receive replies. A new listener will be created only if the
					// endpoint for the delegate is marked as FAILED. This will be the case if the listener
					// on the reply queue for the endpoint has failed.
					synchronized( recoveryMux ) {
	          if ( controller instanceof AggregateAnalysisEngineController ) {
	            // Using the queue name lookup the delegate key
	            String key = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(delegateEndpoint.getEndpoint());
	            if ( key != null && destination != null && !isReplyEndpoint ) {
	              // For Process Requests check the state of the delegate that is to receive
	              // the CAS. If the delegate state = TIMEOUT_STATE, push the CAS id onto
	              // delegate's list of delayed CASes. The state of the delegate was 
	              // changed to TIMEOUT when a previous CAS timed out.
	              if (msgType != AsynchAEMessage.Request && command == AsynchAEMessage.Process ) {
	                String casReferenceId = aMessage.getStringProperty(AsynchAEMessage.CasReference);
	                if ( casReferenceId != null && 
	                     ((AggregateAnalysisEngineController)controller).delayCasIfDelegateInTimedOutState(casReferenceId, delegateEndpoint.getEndpoint()) ) {
                    return true;
	                }
	              }
	              // The aggregate has a master list of endpoints which are typically cloned during processing
	              // This object uses a copy of the master. When a listener fails, the status of the master
	              // endpoint is changed. To check the status, fetch the master endpoint, check its status
	              // and if marked as FAILED, create a new listener, a new temp queue and override this
	              // object endpoint copy destination property. It *IS* a new replyTo temp queue.
	              Endpoint masterEndpoint = ((AggregateAnalysisEngineController)controller).lookUpEndpoint(key, false);
	              if ( masterEndpoint.getStatus() == Endpoint.FAILED ) {
	                // Create a new Listener Object to receive replies
	                createListener(key);
	                destination = (Destination)masterEndpoint.getDestination();
	                delegateEndpoint.setDestination(destination);              
	                // Override the reply destination. A new listener has been created along with a new temp queue for replies.
	                aMessage.setJMSReplyTo(destination);
	              }
	            }
	          }
					}
				}

				//	Send a reply to a queue provided by the client
				
				//  Stop messages and replies are sent to the endpoint provided in the destination object
				if ( (command == AsynchAEMessage.Stop || command == AsynchAEMessage.ReleaseCAS || isReplyEndpoint) && delegateEndpoint.getDestination() != null  )
				{
					destinationName = ((ActiveMQDestination)delegateEndpoint.getDestination()).getPhysicalName();
					if ( UIMAFramework.getLogger().isLoggable(Level.FINE))
					{
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_endpoint__FINE", new Object[] {destinationName });
					}
          logMessageSize(aMessage, msgSize, destinationName);
					synchronized(producer)
					{
             producer.send((Destination)delegateEndpoint.getDestination(), aMessage);
					}
				}
				else
				{
					destinationName = ((ActiveMQQueue) producer.getDestination()).getPhysicalName();
           if ( UIMAFramework.getLogger().isLoggable(Level.FINE))
           {
             UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "send", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_endpoint__FINE", new Object[] {destinationName });
           }
           logMessageSize(aMessage, msgSize, destinationName);
           synchronized(producer)
           {
             producer.send(aMessage);
           }
				}
				if (startTimer)
				{
					startTimer(connectionCreationTimestamp);
				}
				return true;
		}
		catch ( Exception e)
		{
			//	If the controller has been stopped no need to send messages
			if ( controller.isStopped())
			{
				return true;
			}
			else
			{
			  if ( e instanceof JMSException ) {
			    handleJmsException( (JMSException)e );
			  } else {
	        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleJmsException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[] { controller.getComponentName(), e});
	        }
			  }
			    
			}
		}
		stopTimer();
		return false;
	}

	private void logMessageSize( Message aMessage, long msgSize, String destinationName ) {
    if ( UIMAFramework.getLogger().isLoggable(Level.FINE)) {
      boolean isReply = false;
      if ( isReplyEndpoint  ) {
        isReply = true;
      }
      String type="Text";
      if ( aMessage instanceof BytesMessage ) {
        type = "Binary";
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "logMessageSize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_msg_size__FINE", new Object[] { controller.getComponentName(), isReply==true?"Reply":"Request", "Binary", destinationName,msgSize});
      } else if ( aMessage instanceof TextMessage ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "logMessageSize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_msg_size__FINE", new Object[] { controller.getComponentName(), isReply==true?"Reply":"Request", "XMI", destinationName,msgSize});
      }
    }
	}
	/**
	 * This method is called during recovery of failed connection. It is only called if the endpoint
	 * associated with a given delegate is marked as FAILED. It is marked that way when a listener
	 * attached to the reply queue fails. This method creates a new listener and a new temp queue.
	 * 
	 * @param delegateKey
	 * @throws Exception
	 */
	private void createListener(String delegateKey) throws Exception {
    if ( controller instanceof AggregateAnalysisEngineController ) {
      //  Fetch an InputChannel that handles messages for a given delegate
      InputChannel iC = controller.getReplyInputChannel(delegateKey);
      //  Create a new Listener, new Temp Queue and associate the listener with the Input Channel
      iC.createListener(delegateKey);
    }
	}

	private synchronized void handleJmsException( JMSException ex) {
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
      //  Check if the exception is due to deleted queue. ActiveMQ does not identify
      //  this condition in the cause, so we need to parse the exception message and
      //  compare against "Cannot publish to a deleted Destination" text. If match is
      //  found, extract the name of the deleted queue from the exception and log it.
      if ( ex.getMessage() != null && ex.getMessage().startsWith("Cannot publish to a deleted Destination")) {
        String destName = endpointName;
        int startPos = ex.getMessage().indexOf(':');
        if ( startPos > 0 ) {
          destName = ex.getMessage().substring(startPos);
        }
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "handleJmsException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_failed_deleted_queue_INFO",
                new Object[] { controller.getName(), destName});
        return;
      } else {
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleJmsException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", new Object[] { controller.getComponentName(), ex});
        }
        ex.printStackTrace();
      }
        
    }
    if ( failed ) {
      return;   // Already marked failed
    }
    failed = true;
	}
	public void onConsumerEvent(ConsumerEvent arg0)
	{
		if (controller != null)
		{
			controller.handleDelegateLifeCycleEvent(getEndpoint(), arg0.getConsumerCount());
		}
	}

	protected synchronized void finalize() throws Throwable
	{
		if ( timer != null)
		{
			timer.cancel();
		}
	}
	
	
}
