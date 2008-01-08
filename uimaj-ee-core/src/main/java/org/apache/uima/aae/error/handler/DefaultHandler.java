package org.apache.uima.aae.error.handler;

import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.util.Level;

public class DefaultHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = DefaultHandler.class;

	public DefaultHandler()
	{
	}

	public DefaultHandler( Map anEndpointThreasholdMap )
	{
		super(anEndpointThreasholdMap);
	}
    
	private Endpoint getDestination( AnalysisEngineController aController, ErrorContext anErrorContext)
	{
		Endpoint endpoint = null;
		String casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);
		if ( aController instanceof AggregateAnalysisEngineController )
		{
			endpoint = ((AggregateAnalysisEngineController)aController).getMessageOrigin(casReferenceId);

			//	Remove the entry from the Message Origin Map since it is no longer needed. The CAS will be
			//	dropped as soon as the exception is sent up to the client.
			if (endpoint != null && aController.isTopLevelComponent())
			{
				((AggregateAnalysisEngineController)aController).removeMessageOrigin( casReferenceId);
			}
		}
		else if ( anErrorContext.containsKey(AsynchAEMessage.Endpoint))
		{
			endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
		}
		return endpoint;
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		String casReferenceId = null;
		Endpoint endpoint = null;
		String key = null;
		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", t);
			
			endpoint = getDestination(aController, anErrorContext);
			casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);

			//	Notify the parent of the exception
			if ( endpoint != null && !endpoint.isCasMultiplier())
			{
				aController.getOutputChannel().sendReply(t, casReferenceId, endpoint, AsynchAEMessage.Process);

				//	Lookup Delegate's key
				key = super.getDelegateKey(endpoint, aController);
				
				if (super.exceedsThreshold(Monitor.ErrorCount, key, aController))
				{
					String action = getAction(Monitor.ErrorCount, key);
					aController.takeAction( action, key, anErrorContext);
				}
			}
			else if ( endpoint == null)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint__INFO", new Object[] { aController.getName() });
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
		}
		finally
		{
			try
			{
				//	Only top level component can Drop the CAS. 
				if ( aController.isTopLevelComponent() )
				{
					aController.takeAction( ErrorHandler.DROPCAS, key, anErrorContext);
				}			
				else if ( casReferenceId != null && aController instanceof AggregateAnalysisEngineController )
				{
					((AggregateAnalysisEngineController)aController).dropFlow(casReferenceId, true);
					((AggregateAnalysisEngineController)aController).removeMessageOrigin(casReferenceId);

				}

				aController.dropStats(casReferenceId, aController.getName());
				
				endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
				if ( endpoint != null )
				{
					aController.dropStats(casReferenceId, endpoint.getEndpoint());
				}
			}
			catch( Exception e)
			{
				e.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}
		}
		return true;
	}

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
