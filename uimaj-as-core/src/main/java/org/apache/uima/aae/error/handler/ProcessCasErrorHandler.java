/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.aae.error.handler;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.ExpiredMessageException;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.util.Level;

public class ProcessCasErrorHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = ProcessCasErrorHandler.class;

	private Map delegateMap = null;
	private Object monitor = new Object();
	
	public ProcessCasErrorHandler()
	{
		delegateMap = new HashMap();
	}
	public ProcessCasErrorHandler( Map aDelegateMap )
	{
		delegateMap = aDelegateMap;
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
	private boolean isDisabled( AggregateAnalysisEngineController aController, String aDelegateKey )
	{
		return aController.isDelegateDisabled(aDelegateKey);
	}
	
	private boolean ignoreError(Throwable t, ErrorContext anErrorContext, boolean isClient )
	{
		//	Ignores Invalid Messages, expired messages and ConnectExceptions IFF a connection
		//	to a client cannot be established. Clients can be killed in the middle of a run
		//	and that should not be an error.
		if ( t instanceof InvalidMessageException || 
			 t instanceof ExpiredMessageException ||	
			 ( isClient && t.getCause() != null && t.getCause() instanceof ConnectException ) 	
		)
		{
			return true;
		}
		return false;
	}
	private void sendExceptionToClient(Throwable t, String aCasReferenceId, Endpoint anEndpoint, AnalysisEngineController aController) throws Exception 
	{
		//	Notify the parent of the exception
		if ( anEndpoint != null && aCasReferenceId != null && !anEndpoint.isCasMultiplier())
		{
			try
			{
				aController.getOutputChannel().sendReply(t, aCasReferenceId, anEndpoint, AsynchAEMessage.Process);
				//aController.dropStats(aCasReferenceId, anEndpoint.getEndpoint());
			}
			catch( Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "sendExceptionToParent", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}
		}

	}

	private boolean isClient( Endpoint anEndpoint, AnalysisEngineController aController, String aCasReferenceId )
	{
		Endpoint clientEndpoint = null;
		
		if (aController.isTopLevelComponent())
		{
			clientEndpoint = aController.
				getInProcessCache().
					getEndpoint(null, aCasReferenceId);
		}
		else  if ( aController instanceof AggregateAnalysisEngineController )
		{
			clientEndpoint = 
				((AggregateAnalysisEngineController)aController).
					getMessageOrigin(aCasReferenceId);
		}
		if (anEndpoint != null && clientEndpoint != null )
		{
			return anEndpoint.getEndpoint().equalsIgnoreCase(clientEndpoint.getEndpoint());
		}
		return false;
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		
		if ( !isHandlerForError(anErrorContext, AsynchAEMessage.Process))
		{
			return false;
		}
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", t);

		
		String casReferenceId = null;
		if ( anErrorContext.containsKey(AsynchAEMessage.CasReference)) 
		{
			casReferenceId = (String) anErrorContext.get(AsynchAEMessage.CasReference);
		}
		else
		{
			return true; // No CAS, nothing to do
		}
		boolean isRequest = false;
		
		if ( anErrorContext.containsKey(AsynchAEMessage.MessageType)) 
		{
			int msgType = ((Integer)anErrorContext.get(AsynchAEMessage.MessageType)).intValue();
			if (msgType == AsynchAEMessage.Request  )
			{
				isRequest = true;
			}
		}

		//	Determine if the exception occured while sending a reply to the client
		boolean isEndpointTheClient = 
			isClient( (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint), aController, casReferenceId);
		
		
		
		if ( ignoreError( t, anErrorContext, isEndpointTheClient ))
		{
    		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
    				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_ignore_error__INFO", new Object[] { aController.getComponentName(), t.getClass().getName()});
		
//			Endpoint endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
//			if ( endpoint != null && endpoint.getReplyToEndpoint() != null &&
//				 t.getCause() != null && t.getCause() instanceof ConnectException ) 	
//			{
//	    		//	Special case: disable a listener on queue that no longer exists
//				aController.getUimaEEAdminContext().stopListener(endpoint.getReplyToEndpoint());
//			}
    		return true;   // handled here. This message will not processed
		}

		String key = ""; 
		Threshold threshold = null;
		boolean delegateDisabled = false;
		//                       R E T R Y
		//	Do retry first if this an Aggregate Controller
		if ( !isEndpointTheClient && aController instanceof AggregateAnalysisEngineController )
		{
			Endpoint endpoint = null;
			
			if ( anErrorContext.get(AsynchAEMessage.Endpoint) != null )
			{
				endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
			}
			threshold = super.getThreshold(endpoint, delegateMap, aController);
			if ( endpoint != null )
			{
		    	key = ((AggregateAnalysisEngineController)aController).lookUpDelegateKey(endpoint.getEndpoint());
		    	delegateDisabled = ((AggregateAnalysisEngineController)aController).isDelegateDisabled(key);
		    	if ( threshold != null && threshold.getMaxRetries() > 0 && !delegateDisabled)
		    	{
		    		//	If max retry count is not reached, send the last command again and return true
		    		if ( super.retryLastCommand(AsynchAEMessage.Process, endpoint, aController, key, threshold, anErrorContext) )
			    	{
			    		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", 
			    				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retry_cas__FINE", new Object[] { aController.getComponentName(), key, casReferenceId });
				    	return true;   // Command has been retried. Done here.
			    	}
		    	}
		    	else if ( threshold == null )
		    	{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_threshold_for_endpoint__CONFIG", new Object[] { aController.getComponentName(), "Process",  key });
		    	}
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint_provided__INFO", new Object[] { aController.getComponentName() });
			}
		}
		else
		{
	    	if ( delegateMap != null && delegateMap.containsKey(key))
	    	{
	        	threshold = (Threshold)delegateMap.get(key);
	    	}
		}

		
		if ( key != null && key.trim().length() > 0)
		{
			//	Retries either exceeded or not configured for retry
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_retries_exceeded__FINE", new Object[] { aController.getComponentName(), key, casReferenceId });
		}

		//	Dont increment errors for destinations that are clients of this service.
		if ( !aController.isStopped() && (isRequest || !isEndpointTheClient ) )
		{
			synchronized( monitor )
			{
				//	Dont increment errors for delegates that have been already disabled 
				if ( !delegateDisabled )
				{
					//	Process Error Count is only incremented after retries are done. 
					super.incrementStatistic(aController.getMonitor(), key, Monitor.ProcessErrorCount);
					super.incrementStatistic(aController.getMonitor(), key, Monitor.TotalProcessErrorCount);
					aController.getServiceErrors().incrementProcessErrors();
					if ( aController instanceof AggregateAnalysisEngineController && anErrorContext.get(AsynchAEMessage.Endpoint) != null )
					{
						Endpoint endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
						if ( endpoint.isRemote())
						{
							ServiceErrors serviceErrs = 
								((AggregateAnalysisEngineController)aController).getDelegateServiceErrors(key);
//							ServiceErrors serviceErrs = ((AggregateAnalysisEngineController)aController).getServiceErrors(key);
							if (serviceErrs != null )
							{
								serviceErrs.incrementProcessErrors();
							}
						}
					}
					if (threshold != null && threshold.getThreshold() > 0 && super.exceedsThresholdWithinWindow(aController.getMonitor(), Monitor.ProcessErrorCount, key, threshold) )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
								UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_process_cas_exceeded_threshold__INFO", new Object[] { aController.getComponentName(), key, casReferenceId, threshold.getThreshold(), threshold.getAction() });
						aController.takeAction(threshold.getAction(), key, anErrorContext);
					}
				}
				else
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
							UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_delegate_already_disabled__INFO", new Object[] { aController.getComponentName(), key, casReferenceId });
				}
			}
			
		}
		else
		{
			Endpoint endpt = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
			if ( endpt != null )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception_while_sending_reply_to_client__FINE", new Object[] { aController.getComponentName(), endpt.getEndpoint(), casReferenceId });
			}
		}
		
		int totalNumberOfParallelDelegatesProcessingCas = 1; // default
		CacheEntry cacheEntry = null;
		try
		{
			cacheEntry = aController.getInProcessCache().getCacheEntryForCAS(casReferenceId);
			if ( cacheEntry != null )
			{
				totalNumberOfParallelDelegatesProcessingCas = cacheEntry.getNumberOfParallelDelegates();
			}

		}
		catch( Exception e) {}
		//	Determine where to send the message
		Endpoint endpoint = getDestination(aController, anErrorContext);
/*
		//	Notify the parent of the exception
		if ( endpoint != null && casReferenceId != null && !endpoint.isCasMultiplier())
		{
			try
			{
				aController.getOutputChannel().sendReply(t, casReferenceId, endpoint, AsynchAEMessage.Process);
				aController.dropStats(casReferenceId, endpoint.getEndpoint());
			}
			catch( Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}
		}
*/		
		//	If the error occured during parallel step, treat the exception as response from the delegate
		//	When all responses from delegates are accounted for we allow the CAS to move on to the next
		//	step in the flow
		if ( cacheEntry != null && totalNumberOfParallelDelegatesProcessingCas > 1 && ( cacheEntry.howManyDelegatesResponded() < totalNumberOfParallelDelegatesProcessingCas))
		{
			synchronized( cacheEntry )
			{
				cacheEntry.incrementHowManyDelegatesResponded();
			}
		}

		if (aController instanceof AggregateAnalysisEngineController && t instanceof Exception)
		{
			boolean flowControllerContinueFlag = false;
			// if the deployment descriptor says no retries, dont care what the Flow Controller says
			if ( threshold != null && threshold.getContinueOnRetryFailure() )
			{
			  try
			  {
			    //	Consult Flow Controller to determine if it is ok to continue despite the error
			    flowControllerContinueFlag = 
			      ((AggregateAnalysisEngineController) aController).continueOnError(casReferenceId, key, (Exception) t );
			  }
			  catch( Exception exc) {}
			}
	
			//	Check if the caller has already decremented number of subordinates. This property is only
			//	set in the Aggregate's finalStep() method before the CAS is sent back to the client. If
			//	there was a problem sending the CAS to the client, we dont want to update the counter 
			//	again. If an exception is reported elsewhere ( not in finalStep()), the default action is
			//	to decrement the number of subordinates associated with the parent CAS.
			if ( !anErrorContext.containsKey(AsynchAEMessage.SkipSubordinateCountUpdate)) 
			{
				//	Check if the CAS is a subordinate (has parent CAS).
				if ( cacheEntry != null && cacheEntry.isSubordinate())
				{
					String parentCasReferenceId = cacheEntry.getInputCasReferenceId();
					if ( parentCasReferenceId != null )
					{
						try
						{
							CacheEntry parentCasCacheEntry = aController.getInProcessCache().
																getCacheEntryForCAS(parentCasReferenceId);
							synchronized( parentCasCacheEntry )
							{
								((AggregateAnalysisEngineController)aController).
								decrementCasSubordinateCount( parentCasCacheEntry);
								if ( parentCasCacheEntry.getSubordinateCasInPlayCount() == 0 &&
									 parentCasCacheEntry.isPendingReply())
								{
									System.out.println("!!!!!!!!! ProcessCasErrorHandler: Controller:"+aController.getComponentName()+" Parent CAS has NO Subordinates.");
									//	Complete processing of the Input CAS
									if ( flowControllerContinueFlag ==  false )
									{
										aController.process(parentCasCacheEntry.getCas(), parentCasCacheEntry.getCasReferenceId());
									}
								}
							}
						}
						catch( Exception ex)
						{
							//	Input CAS doesnt exist. Nothing to update, move on
						}
					}
				}
			}
			
			
			
			if ( threshold != null && flowControllerContinueFlag )
			{
				if (totalNumberOfParallelDelegatesProcessingCas == 1 || ( cacheEntry.howManyDelegatesResponded() == totalNumberOfParallelDelegatesProcessingCas) )
				{
					aController.process(aController.getInProcessCache().getCasByReference(casReferenceId), casReferenceId);
				}
				//	Dont send request to release the CAS to remote CM. This will happen in the final step. We are continuing
				//	despite the error here.
				return true;
			}
			else
			{
				try
				{
					sendExceptionToClient( t, casReferenceId, endpoint, aController );
				}
				catch( Exception e) 
				{
					e.printStackTrace();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
							UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
				}
			}
			
			//	Now check if the CAS origin is a remote CAS Multiplier. If so, send a request to release the CAS. Remote
			//	CAS Multipliers must be "gated" to prevent flooding the Aggregate queue with too many CASes. There is
			//	an explicit protocol between an Aggregate AS and its CM. The Aggregate AS sends a request to free a CAS
			//	in the CM whenever the Aggregate has capacity to process more CASes. Here we are recovering from an
			//	an error but we still need to send a request to free the CAS in the remote CM to prevent a hang in the CM
			try
			{
				//	First check if the current controller is the one that first produced the CAS
				if ( cacheEntry != null && aController.getName().equalsIgnoreCase(cacheEntry.getCasProducerAggregateName() ) )
				{
					//	Fetch the key of the Cas Multiplier
					String casProducerKey = cacheEntry.getCasMultiplierKey();
					if ( casProducerKey != null )
					{
						//	Create an endpoint object from the key. This object will be cloned from the Endpoint object
						//	defined in the spring configuration file.
						Endpoint cmEndpoint = ((AggregateAnalysisEngineController)aController).lookUpEndpoint(casProducerKey, true);
						
						//	CAS reference id will be different if the CAS originated from a remote Cas Multiplier. 
						//String cmCasReferenceId = cacheEntry.getRemoteCMCasReferenceId();
						//	If the Cas Multiplier is remote send a request to free a CAS with a given cas id
						if ( cmEndpoint != null && cmEndpoint.isCasMultiplier() && cmEndpoint.isRemote() )
						{
							cmEndpoint.setEndpoint(cmEndpoint.getEndpoint()+"__CasSync");
							aController.getOutputChannel().sendRequest(AsynchAEMessage.ReleaseCAS, cmEndpoint);
						}
					}
				}
			}
			catch( Exception e) 
			{
				e.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}
		}
		else  // Primitive Controller
		{
			try
			{
				sendExceptionToClient( t, casReferenceId, endpoint, aController );
			}
			catch( Exception e) 
			{
				e.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
			}
		}

		try
		{
			//	Only top level component can Drop the CAS. 
			if ( aController.isTopLevelComponent() )
			{
				aController.takeAction( ErrorHandler.DROPCAS, key, anErrorContext);
			}			
			if ( casReferenceId != null && aController instanceof AggregateAnalysisEngineController )
			{
				//	Cleanup state information from local caches
				((AggregateAnalysisEngineController)aController).dropFlow(casReferenceId, true);
				((AggregateAnalysisEngineController)aController).removeMessageOrigin(casReferenceId);
			}

			aController.dropStats(casReferenceId, aController.getName());
		}
		catch( Exception e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
		}

		
		return true;
	}

}
