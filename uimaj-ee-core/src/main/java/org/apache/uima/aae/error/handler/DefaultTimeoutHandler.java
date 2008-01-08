package org.apache.uima.aae.error.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;

public class DefaultTimeoutHandler extends ErrorHandlerBase implements ErrorHandler 
{
	public DefaultTimeoutHandler( Map anEndpointThreasholdMap )
	{
		super(anEndpointThreasholdMap);
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if (t instanceof MessageTimeoutException)
		{
			if (anErrorContext.containsKey(AsynchAEMessage.Command) && anErrorContext.containsKey(AsynchAEMessage.Endpoint))
			{
				Endpoint endpoint = null;
				try
				{
					endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
					int command = ((Integer) anErrorContext.get(AsynchAEMessage.Command)).intValue();
					if (AsynchAEMessage.Process == command)
					{
						if ( anErrorContext.containsKey(AsynchAEMessage.CasReference ) )
						{
							
							if (!super.exceedsThreshold(Monitor.ProcessRequestTimeoutCount, endpoint.getEndpoint(), aController))
							{
								if ( endpoint.isRetryEnabled() && aController instanceof AggregateAnalysisEngineController )
								{
									String casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);
									//System.out.println(Thread.currentThread().getName() + " DefaultTimeoutHandler sending request to process CAS to endpoint::" + endpoint.getEndpoint()+" CasReferenceId::"+casReferenceId);
									((AggregateAnalysisEngineController) aController).retryProcessCASRequest(casReferenceId, endpoint, false);
									return true; // Handled the exception
								}
							}
							else
							{
								String casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);
								String action = getAction(Monitor.MetadataRequestTimeoutCount, endpoint.getEndpoint());

								if ( ErrorHandler.DISABLE.equalsIgnoreCase(action))
								{
									List list = new ArrayList();
									list.add(endpoint.getEndpoint());
									((AggregateAnalysisEngineController)aController).disableDelegates(list);
									
									//	The following should be done after consultation with the Flow
									//((AggregateAnalysisEngineController)aController).dropCAS(casReferenceId, true);
									
									
									
									return true;
								}
								
								
								
								
								/*
								CacheEntry cachedEntry =
									((AggregateAnalysisEngineController) aController).getInProcessCache().getCacheEntryForCAS(casReferenceId);
								if ( cachedEntry.getNumberOfParallelDelegates() > 1 )
								{
									if ( ErrorHandler.CONTINUE.equalsIgnoreCase(action) )
									{
									}
								}
								*/
							}
							
						}
						else
						{
							//	unhandled error. Missing CasReference Id in error context
						}
					}
					else if (AsynchAEMessage.GetMeta == command)
					{
						String key = ((AggregateAnalysisEngineController) aController).lookUpDelegateKey(endpoint.getEndpoint());
						//System.out.println(Thread.currentThread().getName() + " DefaultTimeoutHandler handles Timeout For GetMetadata ");
						if (!exceedsThreshold(Monitor.MetadataRequestTimeoutCount, key, aController))
						{
							//System.out.println(Thread.currentThread().getName() + " DefaultTimeoutHandler sending request for metadata to endpoint::" + endpoint.getEndpoint());
							if (aController instanceof AggregateAnalysisEngineController)
							{
								((AggregateAnalysisEngineController) aController).retryMetadataRequest(endpoint);
								return true; // Handled the exception
							}
						}
						else
						{
							String action = getAction(Monitor.MetadataRequestTimeoutCount, key); //endpoint.getEndpoint());
							if ( action != null )
							{
//								System.out.println(Thread.currentThread().getName() + " DefaultTimeoutHandler Exceeded Threshold - Taking Action:::"+action+" endpoint::" + endpoint.getEndpoint());
							    aController.takeAction(action, endpoint.getEndpoint(), anErrorContext);
								return true; // Handled the exception
							}
						}
					}
				}
				catch ( Exception e)
				{
					//e.printStackTrace();
					// Log this Exception and return false. Let the Default
					// Catch All
					// Handler handle this situation
				}
			}
		}
		return false;
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
