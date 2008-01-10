package org.apache.uima.aae.error.handler;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.util.Level;

public class DefaultDelegateErrorHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = DefaultDelegateErrorHandler.class;

	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		
		if ( t instanceof UimaEEServiceException)
		{
			t.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { t });

			if ( anErrorContext.containsKey(AsynchAEMessage.Endpoint) )
			{
				Endpoint endpoint = (Endpoint)anErrorContext.get(AsynchAEMessage.Endpoint);
				String casReferenceId = (String)anErrorContext.get(AsynchAEMessage.CasReference);

				try
				{
					aController.getOutputChannel().sendReply(t, casReferenceId, endpoint, AsynchAEMessage.Process );
				}
				catch( Throwable e)
				{
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

}
