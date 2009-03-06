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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.Endpoint_impl;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.handler.Handler;
import org.apache.uima.aae.handler.HandlerBase;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.message.JmsMessageContext;
import org.apache.uima.util.Level;
import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * Thin adapter for receiving JMS messages from Spring. It delegates processing of all 
 * messages to the {@link MessageHandler}. Each JMS Message is wrapped in transport neutral
 * MessageContext wrapper.
 *
 */
public class JmsInputChannel 
implements InputChannel, JmsInputChannelMBean, SessionAwareMessageListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3318400773113552290L;

	private static final Class CLASS_NAME = JmsInputChannel.class;
	
	private final CountDownLatch msgHandlerLatch = new CountDownLatch(1);

	private final CountDownLatch controllerLatch = new CountDownLatch(1);
	//	Reference to the first Message Handler in the Chain. 
	private transient Handler handler;
	//	The name of the queue this Listener is expecting to receive messages from
	private String endpointName;
	//	Reference to the Controller Object
	private transient AnalysisEngineController controller;
	
	private int sessionAckMode;
	
	private transient UimaDefaultMessageListenerContainer messageListener;
	
	private transient Session jmsSession;
	
	private String brokerURL="";
	
	private ServiceInfo serviceInfo = null;
	
	private volatile boolean stopped = false;
	private volatile boolean	channelRegistered = false;
	
	private List listenerContainerList = new ArrayList();
	
	private Object mux = new Object();
	
	private ConcurrentHashMap<String, UimaDefaultMessageListenerContainer> failedListenerMap = 
	  new ConcurrentHashMap<String, UimaDefaultMessageListenerContainer>();
	
	public AnalysisEngineController getController()
	{
		return controller;
	}
	
	public String getName()
	{
		return endpointName;
	}
	
	public void setController(AnalysisEngineController aController) throws Exception
	{
	
		this.controller = aController;
		if ( !channelRegistered )
		{
			controller.addInputChannel(this);
		}
		controller.setInputChannel(this);
		controllerLatch.countDown();
	}
	
	public void setMessageHandler( Handler aHandler )
	{
		handler = aHandler;
		msgHandlerLatch.countDown();
	}
	
	public void setEndpointName( String anEndpointName )
	{
		endpointName = anEndpointName;
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "setEndpointName", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_service_listening__INFO",
                new Object[] { anEndpointName });
      
    }		
	}
	
	/**
	 * Validate message type contained in the JMS header.
	 * 
	 * @param aMessage - jms message retrieved from queue
	 * @param properties - map containing message properties
	 * @return
	 * @throws Exception
	 */
	
	private boolean validMessageType( Message aMessage, Map properties ) throws Exception
	{
		if ( properties.containsKey(AsynchAEMessage.MessageType))
		{
			int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
			if ( msgType != AsynchAEMessage.Response && msgType != AsynchAEMessage.Request )
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validMessageType", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_msgtype_in_message__INFO",
	                    new Object[] { msgType, endpointName });
		    }
				return false;
				
			}
		}
		else
		{
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validMessageType", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_msgtype_notin_message__INFO",
                    new Object[] { endpointName });
	    }
			return false;
		}

		return true;
	}

	private boolean isProcessRequest( Message aMessage ) throws Exception
	{
		Map properties = ((ActiveMQMessage)aMessage).getProperties();
		if ( properties.containsKey(AsynchAEMessage.MessageType) &&
			 properties.containsKey(AsynchAEMessage.Command) )
		{
			int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
			int command = aMessage.getIntProperty(AsynchAEMessage.Command);

			if (msgType != AsynchAEMessage.Request || command != AsynchAEMessage.Process )
			{
				return false;
			}
			return true;
		}
		return false;
	}
	private boolean isRemoteRequest( Message aMessage ) throws Exception
	{
		
		//	Dont do checkpoints if a message was sent from a Cas Multiplier
		if ( aMessage.propertyExists(AsynchAEMessage.CasSequence))
		{
			return false;
		}

		Map properties = ((ActiveMQMessage)aMessage).getProperties();
		if ( properties.containsKey(AsynchAEMessage.MessageType) &&
			 properties.containsKey(AsynchAEMessage.Command) &&
			 properties.containsKey(UIMAMessage.ServerURI))
		{
			int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
			int command = aMessage.getIntProperty(AsynchAEMessage.Command);
			boolean isRemote = aMessage.getStringProperty(UIMAMessage.ServerURI).startsWith("vm") == false;
			if ( isRemote && msgType == AsynchAEMessage.Request &&
					(command == AsynchAEMessage.Process ||
					 command == AsynchAEMessage.CollectionProcessComplete) )
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean acceptsDeltaCas( Message aMessage ) throws Exception
	{
		Map properties = ((ActiveMQMessage)aMessage).getProperties();
		boolean acceptsDeltaCas = false;
		if ( properties.containsKey(AsynchAEMessage.AcceptsDeltaCas) )
		{
			acceptsDeltaCas = aMessage.getBooleanProperty(AsynchAEMessage.AcceptsDeltaCas);
		}
		return acceptsDeltaCas;
	}
	
	/**
	 * Validate command contained in the header of the JMS Message
	 * 
	 * @param aMessage - JMS Message received
	 * @param properties - Map containing header properties
	 * @return - true if the command received is a valid one, false otherwise
	 * @throws Exception
	 */
	private boolean validCommand( Message aMessage, Map properties ) throws Exception
	{
		if ( properties.containsKey(AsynchAEMessage.Command))
		{
			int command = aMessage.getIntProperty(AsynchAEMessage.Command);
			if ( command != AsynchAEMessage.Process && 
				 command != AsynchAEMessage.GetMeta && 
				 command != AsynchAEMessage.ReleaseCAS && 
         command != AsynchAEMessage.Stop && 
         command != AsynchAEMessage.Ping && 
				 command != AsynchAEMessage.CollectionProcessComplete )
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validCommand", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_command_in_message__INFO",
	                    new Object[] { command, endpointName });
		    }
				return false;
			}
		}
		else
		{
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validCommand", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_command_notin_message__INFO",
                    new Object[] { endpointName });
	    }
			return false;
		}

		return true;
	}
	/**
	 * Validates payload in the JMS Message.
	 * 
	 * @param aMessage - JMS Message received
	 * @param properties - Map containing header properties
	 * @return - true if the payload is valid, false otherwise
	 * @throws Exception
	 */
	private boolean validPayload( Message aMessage, Map properties ) throws Exception
	{
		if ( properties.containsKey(AsynchAEMessage.Command))
		{
			int command = aMessage.getIntProperty(AsynchAEMessage.Command);
			if ( command == AsynchAEMessage.GetMeta ||
				 command == AsynchAEMessage.CollectionProcessComplete ||
         command == AsynchAEMessage.Stop ||
         command == AsynchAEMessage.Ping ||
				 command == AsynchAEMessage.ReleaseCAS)
			{
				//	Payload not included in GetMeta Request
				return true;
			}
		}
			
		if ( properties.containsKey(AsynchAEMessage.Payload))
		{
			int payload = aMessage.getIntProperty(AsynchAEMessage.Payload);
			if ( payload != AsynchAEMessage.XMIPayload && 
	         payload != AsynchAEMessage.BinaryPayload &&
	         payload != AsynchAEMessage.CASRefID &&
				 payload != AsynchAEMessage.Exception &&
				 payload != AsynchAEMessage.Metadata 
				)
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validPayload", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_payload_in_message__INFO",
	                    new Object[] { payload, endpointName });
		    }
				
				return false;
			}
		}
		else
		{
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validPayload", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_payload_notin_message__INFO",
                    new Object[] { endpointName });
	    }
			return false;
		}

		return true;
	}
	
	private boolean isStaleMessage( Message aMessage ) throws JMSException
	{
		int command = aMessage.getIntProperty(AsynchAEMessage.Command);
		int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
		if ( isStopped() || getController() == null || getController().getInProcessCache() == null )
		{
			//	Shutting down 
			return true;
		}
		if ( command == AsynchAEMessage.Process && msgType == AsynchAEMessage.Response  )
		{
			String casReferenceId = aMessage.getStringProperty(AsynchAEMessage.CasReference);
			if (!getController().getInProcessCache().entryExists(casReferenceId))
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "isStaleMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stale_message__FINE",
	                    new Object[] { endpointName, casReferenceId, aMessage.getStringProperty(AsynchAEMessage.MessageFrom) });
		    }
				return true;
			}
		}
		return false;
	}
	/**
	 * Validates contents of the message. It checks if command, payload and message types contain
	 * valid data.
	 * 
	 * @param aMessage - JMS Message to validate
	 * @return - true if message is valid, false otherwise
	 * @throws Exception
	 */
	public boolean validMessage( Message aMessage ) throws Exception
	{
		if ( aMessage instanceof ActiveMQMessage )
		{
			Map properties = ((ActiveMQMessage)aMessage).getProperties();
			if ( !validMessageType(aMessage, properties) )
			{
				return false;
			}
			if ( !validCommand(aMessage, properties) )
			{
				return false;
			}
			if ( !validPayload(aMessage, properties) )
			{
				return false;
			}
			
			if ( isStaleMessage(aMessage) )
			{
				if ( sessionAckMode == Session.CLIENT_ACKNOWLEDGE) 
				{
					aMessage.acknowledge();
				}
				return false;
			}
		}
		return true;
	}
	public void abort()
	{
//		if ( messageListener != null )
//		{
//			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
//                    "abort", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_jms_transport__INFO",
//                    new Object[] { endpointName });
//			try
//			{
//				messageListener.shutdown();
//			}
//			catch( Exception e) {}
//		}
	}
	private String decodeIntToString( String aTypeToDecode, int aValueToDecode )
	{
		if ( AsynchAEMessage.MessageType.equals(aTypeToDecode) )
		{
			switch( aValueToDecode )
			{
			case AsynchAEMessage.Request:
				return "Request";
			case AsynchAEMessage.Response:
				return "Response";
			}
		}
		else if ( AsynchAEMessage.Command.equals(aTypeToDecode ) )
		{
			switch( aValueToDecode )
			{
			case AsynchAEMessage.Process:
				return "Process";
			case AsynchAEMessage.GetMeta:
				return "GetMetadata";
			case AsynchAEMessage.CollectionProcessComplete:
				return "CollectionProcessComplete";
			case AsynchAEMessage.ReleaseCAS:
				return "ReleaseCAS";
      case AsynchAEMessage.Stop:
        return "Stop";
      case AsynchAEMessage.Ping:
        return "Ping";
			}
			
		}
		else if ( AsynchAEMessage.Payload.equals(aTypeToDecode ) )
		{
			switch( aValueToDecode )
			{
			case AsynchAEMessage.XMIPayload:
				return "XMIPayload";
      case AsynchAEMessage.BinaryPayload:
        return "BinaryPayload";
			case AsynchAEMessage.CASRefID:
				return "CASRefID";
			case AsynchAEMessage.Metadata:
				return "Metadata";
			case AsynchAEMessage.Exception:
				return "Exception";
			case AsynchAEMessage.XCASPayload:
				return "XCASPayload";
			case AsynchAEMessage.None:
				return "None";
			}
		}
		return "UNKNOWN";
	}

	private boolean ackMessageNow( Message aMessage ) throws JMSException
	{
		if ( sessionAckMode != Session.CLIENT_ACKNOWLEDGE )
		{
			return false;
		}
		
		
		if ( aMessage.getIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.GetMeta || 
			 aMessage.getIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.CollectionProcessComplete ||
			 aMessage.getIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.ReleaseCAS ||
			 aMessage.getIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.ACK )
		{
			return true;
		}
		return false;
	}
	
	private boolean isCheckpointWorthy( Message aMessage ) throws Exception
	{
		synchronized( mux )
		{
			//	Dont do checkpoints if a message was sent from a Cas Multiplier
			if ( aMessage.propertyExists(AsynchAEMessage.CasSequence))
			{
				return false;
			}
			Map properties = ((ActiveMQMessage)aMessage).getProperties();
			if ( properties.containsKey(AsynchAEMessage.MessageType) &&
				 properties.containsKey(AsynchAEMessage.Command) &&
				 properties.containsKey(UIMAMessage.ServerURI))
			{
				int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
				int command = aMessage.getIntProperty(AsynchAEMessage.Command);
				if ( msgType == AsynchAEMessage.Request &&
						(command == AsynchAEMessage.Process ||
						 command == AsynchAEMessage.CollectionProcessComplete) )
				{
					return true;
				}
			}
			return false;
			
		}
	}
	/**
	 * Receives Messages from the JMS Provider. It checks the message header
	 * to determine the type of message received. Based on the type,
	 * a MessageContext is created to facilitate access to the transport specific message.
	 * Once the MessageContext is determined this routine delegates handling
	 * of the message to the chain of MessageHandlers.  
	 * 
	 * @param aMessage - JMS Message containing header and payload
	 * @param aSession - JMSSession object
	 */
	public void onMessage(Message aMessage, Session aJmsSession )
	{
		String casRefId = null;

		if ( isStopped() )
		{
			return;
		}
		
		
		try
		{
			//	wait until message handlers are plugged in
			msgHandlerLatch.await();
		}
		catch( InterruptedException e) {}
		try
		{
			//	wait until the controller is plugged in
			controllerLatch.await();
		}
		catch( InterruptedException e) {}
		long idleTime = 0;

		boolean doCheckpoint = false;
		
		String eN = endpointName;
		if ( getController() != null )
		{
			eN = getController().getComponentName();
			if (eN == null )
			{
				eN = "";
			}
		}
		

    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_msg__FINE",
                new Object[] { eN });
    }
		JmsMessageContext messageContext = null;

		int requestType = 0;
		try
		{
			//	Wrap JMS Message in MessageContext
			messageContext = new JmsMessageContext( aMessage, endpointName );
			if ( aMessage.getStringProperty(AsynchAEMessage.CasReference) == null )
			{
				casRefId = "CasReferenceId Not In Message";
			}
			else
			{
				casRefId = aMessage.getStringProperty(AsynchAEMessage.CasReference);
			}
			if ( validMessage(aMessage) )
			{
				String command = decodeIntToString(AsynchAEMessage.Command, aMessage.getIntProperty(AsynchAEMessage.Command) );
				String messageType =  decodeIntToString(AsynchAEMessage.MessageType, aMessage.getIntProperty(AsynchAEMessage.MessageType) );
				if ( ackMessageNow(aMessage))
				{
					aMessage.acknowledge();
				}
				String msgSentFromIP = null;

				if ( aMessage.getIntProperty(AsynchAEMessage.MessageType) == AsynchAEMessage.Response && 
					 aMessage.propertyExists(AsynchAEMessage.ServerIP)) 
				{
					msgSentFromIP = aMessage.getStringProperty(AsynchAEMessage.ServerIP);
				}
//				System.out.println("***********************************************************************************" +
//						"           \n**CONTROLLER::"+controller.getName()+"**** Received New Message From [ "+aMessage.getStringProperty(AsynchAEMessage.MessageFrom)+" ]**************" +
//						"           \n**MSGTYPE::"+messageType+" COMMAND:"+command + " Cas Reference Id::"+casRefId+
//				        "           \n******************************************************************************");
				
				String msgFrom = (String)aMessage.getStringProperty(AsynchAEMessage.MessageFrom); 

				if ( controller != null && msgFrom != null )
				{
					if ( msgSentFromIP != null )
					{
				    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
				      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_new_message_with_ip__FINE",
			                    new Object[] { controller.getComponentName(), msgFrom, msgSentFromIP, messageType, command, casRefId });
				    }						
					}
					else
					{
            if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
              UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_new_message__FINE",
			                    new Object[] { controller.getComponentName(), msgFrom, messageType, command, casRefId });
            }
					}
				}
				else
				{
				}
				//	Delegate processing of the message contained in the MessageContext to the
				//	chain of handlers
				try
				{
					if ( isRemoteRequest( aMessage ))
					{
						//	Compute the idle time waiting for this request 
						idleTime = getController().getIdleTime();
						
						//	This idle time is reported to the client thus save it in the endpoint
						//	object. This value will be fetched and added to the outgoing reply.
						messageContext.getEndpoint().setIdleTime(idleTime);
					}
				}
				catch( Exception e ) {}

				//	Determine if this message is a request and either GetMeta, CPC, or Process
				doCheckpoint = isCheckpointWorthy( aMessage );
				requestType = aMessage.getIntProperty(AsynchAEMessage.Command);
				//	Checkpoint
				if ( doCheckpoint )
				{
					getController().beginProcess(requestType);
				}

        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_new_msg_in__FINEST",
                new Object[] { getController().getComponentName(), msgFrom, command, messageType, casRefId });
        }
				if ( handler != null )
				{
					handler.handle( messageContext );
				}
			}
			else
			{
			  if ( !isStaleMessage(aMessage))
			  {
	        controller.getErrorHandlerChain().handle(new InvalidMessageException(), HandlerBase.populateErrorContext( messageContext ), controller);      
			  }
			}
		
		}
		catch( Throwable t)
		{
			t.printStackTrace();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", t);
      }
			controller.getErrorHandlerChain().handle(t, HandlerBase.populateErrorContext( messageContext ), controller);			
		}
		finally
		{
			//	Call the end checkpoint for non-aggregates. For primitives the CAS has been fully processed if we are here
			if ( doCheckpoint && getController() instanceof PrimitiveAnalysisEngineController )
			{
				getController().endProcess(requestType);
			}
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_msg_processed__FINE",
                    new Object[] { getController().getComponentName(), casRefId });
      }
		}
	}
	
	public int getSessionAckMode()
	{
		return sessionAckMode;
	}
	public String getServerUri()
	{
		return brokerURL;
	}
	public synchronized void setListenerContainer(UimaDefaultMessageListenerContainer messageListener)
	{
		this.messageListener = messageListener;
		System.setProperty("BrokerURI", messageListener.getBrokerUrl());
		brokerURL = messageListener.getBrokerUrl();
		listenerContainerList.add(messageListener);
		this.messageListener = messageListener;
		if ( getController() != null )
		{
			try
			{
				getController().addInputChannel(this);
				messageListener.setController(getController());
			} catch( Exception e) {}
		}
	}
	public ActiveMQConnectionFactory getConnectionFactory()
	{
		if (messageListener == null )
		{
			return null;
		}
		else
		{
			return (ActiveMQConnectionFactory)messageListener.getConnectionFactory();
		}
	}
	public void ackMessage( MessageContext aMessageContext )
	{
		if ( aMessageContext != null && sessionAckMode == Session.CLIENT_ACKNOWLEDGE )
		{
			try
			{
				((Message)aMessageContext.getRawMessage()).acknowledge();
			}
			catch( Exception e)
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
	                    "ackMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                    new Object[] { Thread.currentThread().getName(), e });
        }
			}
		}
	}
	public String getBrokerURL()
	{
		return brokerURL;
	}
	
	public String getInputQueueName()
	{
		if ( messageListener != null )
		  if ( messageListener.getDestination() != null ) {
		    return messageListener.getDestination().toString();
		  } else {
	      return messageListener.getDestinationName();//getEndpointName();
		  }
		else
		{
			return "";
		}
	}
	public ServiceInfo getServiceInfo()
	{
		if ( serviceInfo == null )
		{
			serviceInfo = new ServiceInfo();
			serviceInfo.setBrokerURL(getBrokerURL());
			serviceInfo.setInputQueueName(getName());
			serviceInfo.setState("Active");
		}
		return serviceInfo;
	}
	public void setServerUri(String serverUri)
	{
		brokerURL = serverUri;
		if ( getController() != null && getController() instanceof AggregateAnalysisEngineController )
		{
			((AggregateAnalysisEngineController)getController()).getServiceInfo().setBrokerURL(brokerURL);
		}
		else
		{
			((PrimitiveAnalysisEngineController)getController()).getServiceInfo().setBrokerURL(brokerURL);
		}
	}
	public void stop() throws Exception
	{
		
		for( int i=0; i < listenerContainerList.size(); i++ )
		{
			final UimaDefaultMessageListenerContainer mL =
				(UimaDefaultMessageListenerContainer) listenerContainerList.get(i);
			if ( mL != null && mL.isRunning() )
			{
				stopped = true;
				String eName = mL.getEndpointName();
				if ( eName != null )
				{
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_jms_transport__INFO",
		                    new Object[] { eName /* mL.getEndpointName()*/ /*endpointName */});
          }
				}
				mL.closeConnection();
				mL.stop();
			}
			else
			{
				if ( getController() != null )
				{
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_transport_not_stopped__INFO",
		                    new Object[] { getController().getComponentName() });
          }
				}
			}
		}
		messageListener = null;
		controller = null;
		handler = null;
	}
	public boolean isStopped()
	{
		return stopped;
	}
  public int getConcurrentConsumerCount()
  {
    return messageListener.getConcurrentConsumers();
  }

  public void createListener( String aDelegateKey ) throws Exception {
    
    UimaDefaultMessageListenerContainer failedListener =
      failedListenerMap.get(aDelegateKey);
    UimaDefaultMessageListenerContainer newListener = 
      new UimaDefaultMessageListenerContainer();
    ActiveMQConnectionFactory f = new ActiveMQConnectionFactory(failedListener.getBrokerUrl());
    newListener.setConnectionFactory(f);
//    newListener.setConnectionFactory(failedListener.getConnectionFactory());
    newListener.setMessageListener(this);
    newListener.setController(getController());
    
    TempDestinationResolver resolver = new TempDestinationResolver();
//    resolver.setConnectionFactory((ActiveMQConnectionFactory)failedListener.getConnectionFactory());
    resolver.setConnectionFactory(f); 
    resolver.setListener(newListener);
    newListener.setDestinationResolver(resolver);

    org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor executor = new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor();
    executor.setCorePoolSize(failedListener.getConcurrentConsumers());
    executor.setMaxPoolSize(failedListener.getMaxConcurrentConsumers());
    executor.setQueueCapacity(failedListener.getMaxConcurrentConsumers());
    executor.initialize();

    newListener.setTaskExecutor(executor);
    newListener.initialize();
    newListener.start();
    //  Wait until the resolver plugs in the destination
    while (newListener.getDestination() == null) {
      synchronized( newListener ) {
        
        System.out.println(".... Waiting For Destination ...");
        newListener.wait(100);
      }
    }
    newListener.afterPropertiesSet();
    //  Get the endpoint object for a given delegate key from the Aggregate
    Endpoint endpoint = ((AggregateAnalysisEngineController)getController()).lookUpEndpoint(aDelegateKey, false);
    endpoint.setStatus(Endpoint.OK);
    //  Override the reply destination. 
    endpoint.setDestination(newListener.getDestination());
    Object clone = ((Endpoint_impl) endpoint).clone();
    newListener.setTargetEndpoint((Endpoint)clone);
  }
  
  private UimaDefaultMessageListenerContainer getListenerForEndpoint( String anEndpointName ) {
    for( int i=0; i < listenerContainerList.size(); i++ ) {
      UimaDefaultMessageListenerContainer mListener = 
        (UimaDefaultMessageListenerContainer) listenerContainerList.get(i);
      if ( mListener.getDestination() != null && mListener.getDestination().toString().equals( anEndpointName)) {
        return mListener;
      }
    }
    return null;
  }
  public synchronized void destroyListener( String anEndpointName, String aDelegateKey ) {
    final UimaDefaultMessageListenerContainer mListener = 
      getListenerForEndpoint(anEndpointName);
    if ( mListener == null ) {
      System.out.println("--- Listener For Endpoint: "+aDelegateKey+" Not Found");
      return;
    }
    if ( !mListener.isRunning() ) {
      return; // Already Stopped
    }
    
    try {
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) && mListener.getDestination() != null) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "destroyListener", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stop_listener__INFO",
                    new Object[] { mListener.getDestination().toString() });
        }
        System.out.println("++++ Stopping Listener ...");
        mListener.closeConnection();
        mListener.stop();
        System.out.println("++++ Destroying Listener ...");
        new Thread() {
          public void run() {
            mListener.destroy();
          }
        };
        while( mListener.isRunning());      
        System.out.println("Thread:"+Thread.currentThread().getId()+"++++ Listener on Queue:"+anEndpointName+" Has Been Stopped...");
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) && mListener.getDestination() != null) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "destroyListener", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_listener_INFO",
                    new Object[] {controller.getComponentName(), mListener.getDestination().toString() });
        }
        if ( getController() != null ) {
          Endpoint endpoint = ((AggregateAnalysisEngineController)getController()).lookUpEndpoint(aDelegateKey, false);
          endpoint.setStatus(Endpoint.FAILED);
          if ( mListener.getConnectionFactory() != null) {
            if ( getController() instanceof AggregateAnalysisEngineController ) {
              if ( !failedListenerMap.containsKey(aDelegateKey )) {
                failedListenerMap.put( aDelegateKey, mListener);
                listenerContainerList.remove(mListener);
              }
            }
          }
        }
      //}
    } catch( Exception e) {
      e.printStackTrace();
    }
  }
  public boolean isFailed(String aDelegateKey) {
    return failedListenerMap.containsKey(aDelegateKey);
  }
  public boolean isListenerForDestination( String anEndpointName) {
    UimaDefaultMessageListenerContainer mListener = 
      getListenerForEndpoint(anEndpointName);
    if ( mListener == null ) {
      return false;
    }
    return true;
  }
}
