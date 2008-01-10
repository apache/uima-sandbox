package org.apache.uima.aae.handler;

import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.cas.CAS;

public interface Handler
{
	public void setDelegate(Handler aHandler);
	public Handler getDelegate();
	public void removeDelegate(String aHandlerName);
	public void resequenceDelegateHandler( String aHandlerName, int aNewPositionInHandlerChain);
	public void delegate( MessageContext aMessageContext) throws AsynchAEException;
	public void invokeProcess(CAS aCAS, String casReferenceId1, String aCasReferenceId2, MessageContext aMessageContext, String aNewCasProducedBy) throws AsynchAEException;
	public boolean isHandlerForMessage( MessageContext aMessageContext, int anExpectedMessageType, int anExpectedCommand ) throws AsynchAEException;

	public void handle(Object anObjectToHandle) throws AsynchAEException;

	//Deprecate below handlers
	public void handle( Object anObjectToHandle, String expectedOutputType ) throws AsynchAEException;
	public boolean hasDelegateHandler();
	public String getName();

}
