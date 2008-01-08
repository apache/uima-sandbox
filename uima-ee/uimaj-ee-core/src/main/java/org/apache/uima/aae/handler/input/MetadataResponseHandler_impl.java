package org.apache.uima.aae.handler.input;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.handler.HandlerBase;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.util.Level;

public class MetadataResponseHandler_impl extends HandlerBase
{
	private static final Class CLASS_NAME = MetadataResponseHandler_impl.class;

	public MetadataResponseHandler_impl(String aName)
	{
		super(aName);
	}

	/**
	 * This method handles both incoming request for metadata and outgoing response
	 * containing metadata.
	 * 
	 */
	public synchronized void handle(Object anObjectToHandle)
	{

		if (anObjectToHandle instanceof MessageContext)
		{
			try
			{
				int messageType = ((MessageContext)anObjectToHandle).getMessageIntProperty(AsynchAEMessage.MessageType);
				int command = ((MessageContext)anObjectToHandle).getMessageIntProperty(AsynchAEMessage.Command);
				int payload = ((MessageContext)anObjectToHandle).getMessageIntProperty(AsynchAEMessage.Payload);
				
				if ( AsynchAEMessage.Response == messageType && AsynchAEMessage.GetMeta == command)
				{
					//	Metadata Response is only applicable to the Aggregate Controller
					if (getController() instanceof AggregateAnalysisEngineController)
					{
						if (AsynchAEMessage.Exception == payload)
						{
							return;
						}

						String analysisEngineMetadata = ((MessageContext)anObjectToHandle).getStringMessage();
						String fromEndpoint = ((MessageContext)anObjectToHandle).getMessageStringProperty(AsynchAEMessage.MessageFrom);
						((AggregateAnalysisEngineController) getController()).mergeTypeSystem(analysisEngineMetadata, fromEndpoint);
					}
				}
				else
				{

					if ( super.hasDelegateHandler() )
					{
						super.getDelegate().handle(anObjectToHandle);
					}
				}
			}
			catch( Exception e)
			{
				getController().getErrorHandlerChain().handle(e, HandlerBase.populateErrorContext( (MessageContext)anObjectToHandle ), getController());			
			}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "handle", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_invalid_context_object__INFO",
	                new Object[] {getController().getName(), anObjectToHandle.getClass().getName() });
			
		}
	}

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
