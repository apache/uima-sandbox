package org.apache.uima.aae.error;

import org.apache.uima.aae.controller.AnalysisEngineController;

public class ErrorHandlerListenerBase implements ErrorHandlerListener
{
	private ErrorHandlerChain handlerChain;
	
	public void setErrorHandlerChain( ErrorHandlerChain aChainOfErrorHandlers)
	{
		handlerChain = aChainOfErrorHandlers;
	}
	public void onError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		handlerChain.handle( t, anErrorContext, aController);
	}



}
