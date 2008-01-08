package org.apache.uima.aae.message;


import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;

public interface MessageContext
{
	public String getMessageStringProperty( String aMessagePropertyName ) throws AsynchAEException;
	public int getMessageIntProperty( String aMessagePropertyName ) throws AsynchAEException;
	public long getMessageLongProperty( String aMessagePropertyName ) throws AsynchAEException;
	public Object getMessageObjectProperty( String aMessagePropertyName ) throws AsynchAEException;
	public Endpoint getEndpoint();

	public String getStringMessage() throws AsynchAEException;
	public Object getObjectMessage() throws AsynchAEException;
	public byte[] getByteMessage() throws AsynchAEException;
	public Object getRawMessage();
	public boolean propertyExists(String aKey) throws AsynchAEException;
	public void setMessageArrivalTime( long anArrivalTime );
	public long getMessageArrivalTime();
	public String getEndpointName();

	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
