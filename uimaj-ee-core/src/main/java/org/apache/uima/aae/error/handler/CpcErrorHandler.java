package org.apache.uima.aae.error.handler;

import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.util.Level;

public class CpcErrorHandler  extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = CpcErrorHandler.class;

	private Map delegateMap = null;
	
	public CpcErrorHandler( Map aDelegateMap )
	{
		delegateMap = aDelegateMap;
	}
	
	public Map getEndpointThresholdMap()
	{
		return delegateMap;
	}

	
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		
		if (  !isHandlerForError(anErrorContext, AsynchAEMessage.CollectionProcessComplete) )
		{
			return false;
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", t);

		Endpoint endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
		if ( endpoint != null )
		{
			Threshold threshold = null;
			if ( aController instanceof PrimitiveAnalysisEngineController )
			{
				threshold = (Threshold)delegateMap.get("");
			}
			else
			{
				threshold = super.getThreshold(endpoint, delegateMap, aController);
			}
			try
			{
				if ( aController instanceof AggregateAnalysisEngineController )
				{
					endpoint = ((AggregateAnalysisEngineController)aController).getClientEndpoint();
				}
				aController.getOutputChannel().sendReply(t, null, endpoint, AsynchAEMessage.CollectionProcessComplete);
			}
			catch( Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}

			if ( threshold != null && threshold.getAction().equalsIgnoreCase(ErrorHandler.TERMINATE ))
	    	{
				aController.takeAction(threshold.getAction(), endpoint.getEndpoint(), anErrorContext);
	    	}
	    	else if ( threshold == null )
	    	{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_threshold_for_endpoint__CONFIG", new Object[] { aController.getName(), "Collection Processing Complete",  aController.getName() });
	    	}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint_for_getmeta_retry__INFO", new Object[] { aController.getName()});
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	

}
