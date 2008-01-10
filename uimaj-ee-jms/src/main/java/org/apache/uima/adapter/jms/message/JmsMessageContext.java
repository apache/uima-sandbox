package org.apache.uima.adapter.jms.message;

import java.util.StringTokenizer;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.Endpoint_impl;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;

public class JmsMessageContext implements MessageContext
{
	private static final Class CLASS_NAME = JmsMessageContext.class;

	private Message message;
	private Endpoint endpoint;
	private long messageArrivalTime = 0L;
	private String endpointName;
	
	public JmsMessageContext()
	{
		endpoint = new Endpoint_impl();
		
	}

	public String getEndpointName()
	{
		return endpointName;
	}

	private String chooseServerURI( String aServerURIList)
	{
		String serverURI = aServerURIList;
		
		if( serverURI.indexOf(',') > 0 )
		{
			StringTokenizer st = new StringTokenizer(serverURI, ",");
			
			while( st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				if ( token.startsWith("tcp") )
				{
					//	Normally this is the server URI for a java service.
					serverURI = token;
					break;
				}
				else if ( token.startsWith("http"))
				{
					//	http will only be in the list of uri's if the service is bahind the firewall.
					//	If present, this service needs to respond with http protocol as well.
					serverURI = token;
					break;  // dont need to analyze this further. Will respond via http
				}
			}
		}
		
		return serverURI;
	}
	public JmsMessageContext(Message aMessage, String anEndpointName) throws AsynchAEException
	{
		this();
		endpointName = anEndpointName;
		message = aMessage;
		try
		{
			String msgFrom = (String)aMessage.getStringProperty(AsynchAEMessage.MessageFrom); 
			if ( msgFrom != null )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_recvd_message_from__FINE",
	                    new Object[] { msgFrom, aMessage.getStringProperty(UIMAMessage.ServerURI) });
				endpoint.setEndpoint( msgFrom);
			}
			else
			{
				//	Undefined sender of the message. This may be ok.
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
	                    "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_reply_queue_not_defined__WARNING");
			}
			if ( aMessage.getJMSReplyTo() != null )
			{
				endpoint.setDestination(aMessage.getJMSReplyTo());
			}
			if ( aMessage.getStringProperty(UIMAMessage.ServerURI) != null )
			{
				
				String selectedServerURI = chooseServerURI(aMessage.getStringProperty(UIMAMessage.ServerURI));
				
				endpoint.setServerURI(selectedServerURI);
				endpoint.setRemote(endpoint.getServerURI().startsWith("vm")==false);
			}
			else if ( aMessage.getIntProperty(AsynchAEMessage.MessageType) != AsynchAEMessage.Response)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
	                    "JmsMessageContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_reply_queue_server_not_defined__WARNING");
			}
//			if ( aMessage.getBooleanProperty(AsynchAEMessage.RemoveEndpoint))
//			{
//endpoint.setRemove(true);
//System.out.println("Remove Endpoint is set:"+endpoint.remove());
//			}
//			else
//			{
//				System.out.println("Remove Endpoint is not set");				
//				
//			}
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	public boolean propertyExists(String aKey) throws AsynchAEException
	{
		try
		{
			return message.propertyExists(aKey);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	public Endpoint getEndpoint()
	{
		return endpoint;
	}
    public Object getRawMessage()
    {
    	return message;
    }
	public void setMessageArrivalTime( long anArrivalTime )
	{
		messageArrivalTime = anArrivalTime;
	}
	public long getMessageArrivalTime()
	{
		return messageArrivalTime;
	}
    public byte[] getByteMessage() throws AsynchAEException
	{
		try
		{
			if ( message instanceof BytesMessage)
			{
				long payloadSize = ((BytesMessage) message).getBodyLength();
				byte[] payloadByteArray = new byte[(int)payloadSize];
				((BytesMessage) message).readBytes(payloadByteArray);
				return payloadByteArray;
			}
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		return new byte[0];
	}

	public int getMessageIntProperty(String aMessagePropertyName) throws AsynchAEException
	{
		try
		{
			return message.getIntProperty(aMessagePropertyName);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	public long getMessageLongProperty( String aMessagePropertyName ) throws AsynchAEException
	{
		try
		{
			return message.getLongProperty(aMessagePropertyName);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	public Object getMessageObjectProperty(String aMessagePropertyName) throws AsynchAEException
	{
		try
		{
			return message.getObjectProperty(aMessagePropertyName);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	public String getMessageStringProperty(String aMessagePropertyName) throws AsynchAEException
	{
		try
		{
			return message.getStringProperty(aMessagePropertyName);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	public Object getObjectMessage() throws AsynchAEException
	{
		try
		{
			if ( message instanceof ObjectMessage)
			{
				return ((ObjectMessage)message).getObject();
			}
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		return null;
	}

	public String getStringMessage() throws AsynchAEException
	{
		try
		{
			if ( message instanceof TextMessage )
			{
				return ((TextMessage)message).getText();
			}
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		return null;
	}

}
