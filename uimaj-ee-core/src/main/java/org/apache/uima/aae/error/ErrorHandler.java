package org.apache.uima.aae.error;

import java.util.Map;

import org.apache.uima.aae.controller.AnalysisEngineController;

public interface ErrorHandler
{
	public static final String CONTINUE = "Continue";
	public static final String RETRY = "Retry";
	public static final String DROPCAS = "DropCas";
	public static final String TERMINATE = "Terminate";
	public static final String DISABLE = "Disable";
	public static final String ENABLE = "Enable";
	public static final String PROPAGATE = "Propagate";
	
	public static final String RETRY_COUNT = "RetryCount";
	
	public boolean handleError( Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController);
	public Map getEndpointThresholdMap();
	
public static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
