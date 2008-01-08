package org.apache.uima.aae.error.handler;

import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.ExpiredMessageException;
import org.apache.uima.aae.error.UnknownDestinationException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.util.Level;

public class UnknownDestinationHandler extends ErrorHandlerBase implements ErrorHandler
{
private static final Class CLASS_NAME = UnknownDestinationHandler.class;

	public UnknownDestinationHandler(Map anEndpointThreasholdMap)
	{
		super(anEndpointThreasholdMap);
	}

	public UnknownDestinationHandler()
	{
	}

	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if (t instanceof UnknownDestinationException)
		{
			String casReferenceId = null;
			if (anErrorContext.containsKey(AsynchAEMessage.CasReference))
			{
				casReferenceId = (String) anErrorContext.get(AsynchAEMessage.CasReference);
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_handling_bad_destination__INFO", new Object[] { null, casReferenceId });

			return true;
		}
		return false;
	}



}
