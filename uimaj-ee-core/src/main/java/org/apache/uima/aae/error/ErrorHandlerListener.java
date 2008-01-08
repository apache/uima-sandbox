package org.apache.uima.aae.error;

import org.apache.uima.aae.controller.AnalysisEngineController;

public interface ErrorHandlerListener
{
	public void onError( Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController );

	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
