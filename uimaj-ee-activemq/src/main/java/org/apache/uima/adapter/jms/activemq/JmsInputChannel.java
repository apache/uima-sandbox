package org.apache.uima.adapter.jms.activemq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.handler.Handler;
import org.apache.uima.aae.handler.HandlerBase;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
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
	private Handler handler;
	//	The name of the queue this Listener is expecting to receive messages from
	private String endpointName;
	//	Reference to the Controller Object
	private AnalysisEngineController controller;
	
	private int sessionAckMode;
	
	private UimaDefaultMessageListenerContainer messageListener;
	
	private Session jmsSession;
	
	private String brokerURL="";
	
	private ServiceInfo serviceInfo = null;
	
	private boolean stopped = false;
	private boolean	channelRegistered = false;
	
	private List listenerContainerList = new ArrayList();
	
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
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "setEndpointName", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_service_listening__INFO",
                new Object[] { anEndpointName });
		
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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validMessageType", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_msgtype_in_message__INFO",
	                    new Object[] { msgType, endpointName });
				return false;
				
			}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validMessageType", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_msgtype_notin_message__INFO",
                    new Object[] { endpointName });
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
				 command != AsynchAEMessage.CollectionProcessComplete )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validCommand", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_command_in_message__INFO",
	                    new Object[] { command, endpointName });
				return false;
			}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validCommand", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_command_notin_message__INFO",
                    new Object[] { endpointName });
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
				 payload != AsynchAEMessage.CASRefID &&
				 payload != AsynchAEMessage.Exception &&
				 payload != AsynchAEMessage.Metadata 
				)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "validPayload", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_payload_in_message__INFO",
	                    new Object[] { payload, endpointName });
				
				
				return false;
			}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "validPayload", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_payload_notin_message__INFO",
                    new Object[] { endpointName });
			return false;
		}

		return true;
	}
	
	private boolean isStaleMessage( Message aMessage ) throws JMSException
	{
		int command = aMessage.getIntProperty(AsynchAEMessage.Command);
		int msgType = aMessage.getIntProperty(AsynchAEMessage.MessageType);

		if ( command == AsynchAEMessage.Process && msgType == AsynchAEMessage.Response )
		{
			String casReferenceId = aMessage.getStringProperty(AsynchAEMessage.CasReference);
			if (!getController().getInProcessCache().entryExists(casReferenceId))
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "isStaleMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stale_message__FINE",
	                    new Object[] { endpointName, casReferenceId, aMessage.getStringProperty(AsynchAEMessage.MessageFrom) });
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
			}
			
		}
		else if ( AsynchAEMessage.Payload.equals(aTypeToDecode ) )
		{
			switch( aValueToDecode )
			{
			case AsynchAEMessage.XMIPayload:
				return "XMIPayload";
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
	
	private synchronized void computeIdleTime()
	{
		try
		{
			boolean isAggregate = getController() instanceof AggregateAnalysisEngineController;
			if ( isAggregate || !((PrimitiveAnalysisEngineController)getController()).isMultiplier() )
			{
				long lastReplyTime = getController().getReplyTime();
				if ( lastReplyTime > 0 )
				{
					long t = System.nanoTime();
					long delta = t-(long)lastReplyTime;
					getController().saveIdleTime(delta, "", true);

				}
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
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

		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_msg__FINE",
                new Object[] { endpointName });
		JmsMessageContext messageContext = null;
		
		try
		{
			if ( isProcessRequest(aMessage) )
			{
				computeIdleTime();
			}

			//	Wrap JMS Message in MessageContext
			messageContext = new JmsMessageContext( aMessage, endpointName );

			if ( jmsSession == null )
			{
				jmsSession = aJmsSession;
				sessionAckMode = jmsSession.getAcknowledgeMode();
				if ( sessionAckMode == Session.AUTO_ACKNOWLEDGE)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
			                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_ack_mode__CONFIG",
			                new Object[] { endpointName, "AUTO_ACKNOWLEDGE" });
				}
				else if ( sessionAckMode == Session.CLIENT_ACKNOWLEDGE)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
			                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_ack_mode__CONFIG",
			                new Object[] { endpointName, "CLIENT_ACKNOWLEDGE" });
				}
				else 
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
			                "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_ack_mode__CONFIG",
			                new Object[] { endpointName, sessionAckMode });
				}
			}
			String casRefId = null;
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
//				System.out.println("***********************************************************************************" +
//						"           \n**CONTROLLER::"+controller.getName()+"**** Received New Message From [ "+aMessage.getStringProperty(AsynchAEMessage.MessageFrom)+" ]**************" +
//						"           \n**MSGTYPE::"+messageType+" COMMAND:"+command + " Cas Reference Id::"+casRefId+
//				        "           \n******************************************************************************");
				
				String msgFrom = (String)aMessage.getStringProperty(AsynchAEMessage.MessageFrom); 

				if ( controller != null && msgFrom != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_new_message__FINE",
		                    new Object[] { controller.getComponentName(), msgFrom, messageType, command, casRefId });
				}
				//	Delegate processing of the message contained in the MessageContext to the
				//	chain of handlers
				
				if ( handler != null )
				{
					handler.handle( messageContext );
				}
			}
			else
			{
				controller.getErrorHandlerChain().handle(new InvalidMessageException(), HandlerBase.populateErrorContext( messageContext ), controller);			
			}
		
		}
		catch( Throwable t)
		{
			t.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", t);

			controller.getErrorHandlerChain().handle(t, HandlerBase.populateErrorContext( messageContext ), controller);			
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
		if ( getController() != null )
		{
			try
			{
				getController().addInputChannel(this);
			} catch( Exception e) {}
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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
	                    "ackMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                    new Object[] { Thread.currentThread().getName(), e });

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
			return messageListener.getDestinationName();//getEndpointName();
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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_jms_transport__INFO",
	                    new Object[] { mL.getEndpointName() /*endpointName */});
				mL.closeConnection();
				mL.stop();
			}
			else
			{
				if ( getController() != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_transport_not_stopped__INFO",
		                    new Object[] { getController().getComponentName() });
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
/*	
	private void spinThreadForListenerShutdown(final UimaDefaultMessageListenerContainer listenerContainer)
	{
		new Thread("Shutdown Thread For Listener:"+listenerContainer.getEndpointName()) {
			public void run()
			{
				try
				{
					listenerContainer.stop();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
			                "spinThreadForListenerShutdown.run()", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stop_listener__INFO",
			                new Object[] {  listenerContainer.getEndpointName() });
				}
				catch( Exception e) { e.printStackTrace();}
			}
		}.start();
	}
*/	

}
