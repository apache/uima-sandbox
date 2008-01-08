package org.apache.uima.jms.error.handler;

import java.util.Map;

import javax.jms.Message;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;

public class InvalidJMSMessageHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = InvalidJMSMessageHandler.class;

	public InvalidJMSMessageHandler( Map anEndpointThreasholdMap )
	{
		super(anEndpointThreasholdMap);
	}
	public InvalidJMSMessageHandler( )
	{
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if (t instanceof InvalidMessageException )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "handleError", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_handling_invalid_jms_message__INFO",
	                new Object[] {});

			if ( anErrorContext.containsKey(UIMAMessage.RawMsg ))
			{
				
				
				Message invalidMessage = (Message)anErrorContext.get(UIMAMessage.RawMsg);
				//	Handle the message here.
			}
			return true;
		}
		return false;
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
