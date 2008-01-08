package org.apache.uima.aae.error.handler;

import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.MessageTimeoutException;

public class AsynchAEExceptionHandler extends ErrorHandlerBase implements ErrorHandler
{
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if (t instanceof AsynchAEException)
		{
			//System.out.println("AsynchAEException Stack::\n");
			t.printStackTrace();
			
		}
		return false;
	}


}
