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
import org.apache.uima.aae.controller.LocalCache.CasStateEntry;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.ExpiredMessageException;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.spi.transport.UimaMessage;
import org.apache.uima.aae.spi.transport.UimaTransport;
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
			   if ( !anEndpoint.isRemote())
			   {
			      anEndpoint.setReplyEndpoint(true);
            UimaTransport vmTransport = aController.getTransport(anEndpoint.getEndpoint()) ;
		        UimaMessage message = 
		          vmTransport.produceMessage(AsynchAEMessage.Process,AsynchAEMessage.Response,aController.getName());
		        message.addIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.Exception); 
		        message.addStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
		        
		        Throwable wrapper = null;
		        if ( !(t instanceof UimaEEServiceException) )
		        {
		          //  Strip off AsyncAEException and replace with UimaEEServiceException
		          if ( t instanceof AsynchAEException && t.getCause() != null )
		          {
		            wrapper = new UimaEEServiceException(t.getCause());
		          }
		          else
		          {
		            wrapper = new UimaEEServiceException(t);
		          }
		        }
		        if ( wrapper == null )
		        {
		          message.addObjectProperty(AsynchAEMessage.Cargo, t);
		        }
		        else
		        {
              message.addObjectProperty(AsynchAEMessage.Cargo, wrapper);
		        }
		        vmTransport.getUimaMessageDispatcher(anEndpoint.getEndpoint()).dispatch( message );
			   }
			   else
			   {
			     CasStateEntry stateEntry = null;
           String parentCasReferenceId = null;
            try {
              stateEntry = aController.getLocalCache().lookupEntry(aCasReferenceId);
              if ( stateEntry != null && stateEntry.isSubordinate()) {
                parentCasReferenceId = stateEntry.getInputCasReferenceId();
              }
            } catch ( Exception e){}
		        aController.getOutputChannel().sendReply(t, aCasReferenceId, parentCasReferenceId, anEndpoint, AsynchAEMessage.Process);
			   }
			}
			catch( Exception e)
			{
			  e.printStackTrace();
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "sendExceptionToParent", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
        }
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
    org.apache.uima.aae.controller.LocalCache.CasStateEntry parentCasStateEntry = null;
    String delegateKey = null;
		if ( !isHandlerForError(anErrorContext, AsynchAEMessage.Process))
		{
			return false;
		}
		
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", t);
    }
		
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
    				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_ignore_error__INFO", new Object[] { aController.getComponentName(), t.getClass().getName()});
      }
    		if ( casReferenceId !=  null)
    		{
    			//	Cleanup resources associated with a CAS and then release the CAS
    			try
    			{
        			if ( aController instanceof AggregateAnalysisEngineController )
        			{
            			((AggregateAnalysisEngineController)aController).dropFlow(casReferenceId, true);
            			((AggregateAnalysisEngineController)aController).removeMessageOrigin(casReferenceId);
        			}
        			aController.dropStats(casReferenceId, aController.getName());
    			}
    			catch( Exception e)
    			{
    				//	Throwing this CAS away, ignore exception
    			}
    			finally
    			{
        			if ( aController.isTopLevelComponent())
        			{
            			aController.dropCAS(casReferenceId, true);	
        			}
    			}
    		}

    		return true;   // handled here. This message will not processed
		}

		String key = ""; 
		Threshold threshold = null;
		boolean delegateDisabled = false;
		Delegate delegate = null;
		//                       R E T R Y
		//	Do retry first if this an Aggregate Controller
		if ( !isEndpointTheClient && aController instanceof AggregateAnalysisEngineController )
		{
			Endpoint endpoint = null;
			
			if ( anErrorContext.get(AsynchAEMessage.Endpoint) != null )
			{
				endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
        key = ((AggregateAnalysisEngineController)aController).lookUpDelegateKey(endpoint.getEndpoint());
        delegate = ((AggregateAnalysisEngineController)aController).lookupDelegate(key);
			}
			threshold = super.getThreshold(endpoint, delegateMap, aController);
			if ( endpoint != null )
			{
		    //	key = ((AggregateAnalysisEngineController)aController).lookUpDelegateKey(endpoint.getEndpoint());
		    	delegateDisabled = ((AggregateAnalysisEngineController)aController).isDelegateDisabled(key);
		    	if ( threshold != null && threshold.getMaxRetries() > 0 && !delegateDisabled)
		    	{
		    		//	If max retry count is not reached, send the last command again and return true
		    		if ( super.retryLastCommand(AsynchAEMessage.Process, endpoint, aController, key, threshold, anErrorContext) )
			    	{
	            if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
		            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", 
			    				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retry_cas__FINE", new Object[] { aController.getComponentName(), key, casReferenceId });
		          }
				    	return true;   // Command has been retried. Done here.
			    	}
		    	}
		    	else if ( threshold == null )
		    	{
		         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.CONFIG)) {
		           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_threshold_for_endpoint__CONFIG", new Object[] { aController.getComponentName(), "Process",  key });
		         }
		    	}
		    	if ( delegate != null ) {
		    		//	Received reply from the delegate. Remove the CAS from the 
		    		//	delegate's list of CASes pending reply
	        //  Delegate delegate = ((AggregateAnalysisEngineController)aController).lookupDelegate(key);
	          delegate.removeCasFromOutstandingList(casReferenceId);
		    	}
			}
			else
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint_provided__INFO", new Object[] { aController.getComponentName() });
        }
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_retries_exceeded__FINE", new Object[] { aController.getComponentName(), key, casReferenceId });
      }
		}
		boolean disabledDueToExceededThreshold = false;
		
		//	Dont increment errors for destinations that are clients of this service.
		if ( key != null && !aController.isStopped() && (isRequest || !isEndpointTheClient ) )
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
							if (serviceErrs != null )
							{
								serviceErrs.incrementProcessErrors();
							}
						}
					}

					/***
					if (threshold != null && threshold.getThreshold() > 0 && super.exceedsThresholdWithinWindow(aController.getMonitor(), Monitor.ProcessErrorCount, key, threshold) )
					*/

					long procCount = aController.getMonitor().getLongNumericStatistic(key, Monitor.ProcessCount).getValue();
					if (threshold != null && threshold.exceededWindow(procCount))
					{
            if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
              UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
								UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_process_cas_exceeded_threshold__INFO", new Object[] { aController.getComponentName(), key, casReferenceId, threshold.getThreshold(), threshold.getAction() });
            }
            //  Add new property to skip handling of CASes in pending lists. Those CASes
            //  will be handled later in this method, once we complete processing of the CAS
            //  that caused the exception currently being processed. During handling of the
            //  CASes in pending state, this error handler is called for each CAS to force
            //  its timeout. 
            disabledDueToExceededThreshold = ErrorHandler.DISABLE.equalsIgnoreCase(threshold.getAction());
            if ( disabledDueToExceededThreshold ) {
              delegateKey = key;
              anErrorContext.add( AsynchAEMessage.SkipPendingLists, "true");
            }
						aController.takeAction(threshold.getAction(), key, anErrorContext);
					}
				}
				else
				{
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
							UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_delegate_already_disabled__INFO", new Object[] { aController.getComponentName(), key, casReferenceId });
          }
				}
			}
			
		}
		else
		{
			Endpoint endpt = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
			if ( endpt != null )
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_process_exception__INFO", new Object[] { aController.getComponentName(), endpt.getEndpoint(), casReferenceId });
        }
			}
		}
		int totalNumberOfParallelDelegatesProcessingCas = 1; // default
		CacheEntry cacheEntry = null;
		CasStateEntry casStateEntry = null;
		try
		{
		  casStateEntry = aController.getLocalCache().lookupEntry(casReferenceId);
			cacheEntry = aController.getInProcessCache().getCacheEntryForCAS(casReferenceId);
			if ( cacheEntry != null )
			{
				totalNumberOfParallelDelegatesProcessingCas = casStateEntry.getNumberOfParallelDelegates();
			}

		}
		catch( Exception e) {}
		//	Determine where to send the message
		Endpoint endpoint = getDestination(aController, anErrorContext);
		//	If the error happened during a parallel step, treat the exception as response from the delegate
		//	When all responses from delegates are accounted for we allow the CAS to move on to the next
		//	step in the flow. Dont increment parallel delegate response count if a delegate was just
		//  disabled above. The count has been already incremented in handleAction() method of the 
		//  AnalysisEngineController.
    if ( !disabledDueToExceededThreshold  &&
		     casStateEntry != null && 
		     totalNumberOfParallelDelegatesProcessingCas > 1 && 
		     ( casStateEntry.howManyDelegatesResponded() < totalNumberOfParallelDelegatesProcessingCas))
		{
			casStateEntry.incrementHowManyDelegatesResponded();
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
			  catch( Exception exc) 
			  {
			    exc.printStackTrace();
			    
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
							UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", exc);
          }
			  }
			}
			//	Check if the caller has already decremented number of subordinates. This property is only
			//	set in the Aggregate's finalStep() method before the CAS is sent back to the client. If
			//	there was a problem sending the CAS to the client, we dont want to update the counter 
			//	again. If an exception is reported elsewhere ( not in finalStep()), the default action is
			//	to decrement the number of subordinates associated with the parent CAS.
			if (!flowControllerContinueFlag && !anErrorContext.containsKey(AsynchAEMessage.SkipSubordinateCountUpdate)) 
			{
				//	Check if the CAS is a subordinate (has parent CAS).
				if ( casStateEntry != null && casStateEntry.isSubordinate())
				{
					String parentCasReferenceId = casStateEntry.getInputCasReferenceId();
					if ( parentCasReferenceId != null )
					{
						try
						{
              CacheEntry parentCasCacheEntry = aController.getInProcessCache().
                getCacheEntryForCAS(parentCasReferenceId);
						  parentCasStateEntry = aController.getLocalCache().lookupEntry(parentCasReferenceId);
              synchronized( parentCasStateEntry )
              {
                if ( parentCasStateEntry.getSubordinateCasInPlayCount() == 0 && parentCasStateEntry.isPendingReply())
                {
                  //  Complete processing of the Input CAS
                  if ( flowControllerContinueFlag ==  false )
                  {
                    aController.process(parentCasCacheEntry.getCas(), parentCasCacheEntry.getCasReferenceId());
                  }
                } else {
                  parentCasStateEntry.decrementSubordinateCasInPlayCount();
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
		    //  The Exception has been almost fully handled. Check if the delegate was disabled above.
		    //  If it was, we need to force timeout on all CASes in pending state associated with that
		    //  delegate.
        if ( disabledDueToExceededThreshold && delegateKey != null) {
          aController.forceTimeoutOnPendingCases(delegateKey);
        }   
				if (totalNumberOfParallelDelegatesProcessingCas == 1 || ( casStateEntry.howManyDelegatesResponded() == totalNumberOfParallelDelegatesProcessingCas) )
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
				  //  Dont send TimeoutExceptions to client
				  if ( deliverExceptionToClient(t) ) {
				    sendExceptionToClient( t, casReferenceId, endpoint, aController );
				  }
				}
				catch( Exception e) 
				{
					e.printStackTrace();
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
							UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
          }
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
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
        }
			}
		}
		else  // Primitive Controller
		{
			try
			{
        if ( deliverExceptionToClient(t) ) {
          sendExceptionToClient( t, casReferenceId, endpoint, aController );
        }
			}
			catch( Exception e) 
			{
				e.printStackTrace();
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
        }
			}
		}
		//  The Exception has been almost fully handled. Check if the delegate was disabled above.
		//  If it was, we need to force timeout on all CASes in pending state associated with that
		//  delegate.
    if ( disabledDueToExceededThreshold && delegateKey != null) {
      aController.forceTimeoutOnPendingCases(delegateKey);
    }		
		
		try
		{
			//	Only top level component can Drop the CAS. 
			if ( aController.isTopLevelComponent() )
			{
        if (totalNumberOfParallelDelegatesProcessingCas == 1 || ( casStateEntry.howManyDelegatesResponded() == totalNumberOfParallelDelegatesProcessingCas) ){
          aController.takeAction( ErrorHandler.DROPCAS, key, anErrorContext);
        }
			}			
			if ( casReferenceId != null && aController instanceof AggregateAnalysisEngineController )
			{
				if ( parentCasStateEntry != null && parentCasStateEntry.getSubordinateCasInPlayCount() == 0 &&
						 parentCasStateEntry.isPendingReply())
					{
					((AggregateAnalysisEngineController)aController).finalStep(parentCasStateEntry.getFinalStep(), parentCasStateEntry.getCasReferenceId());
					}
				//	Cleanup state information from local caches
				((AggregateAnalysisEngineController)aController).dropFlow(casReferenceId, true);
				((AggregateAnalysisEngineController)aController).removeMessageOrigin(casReferenceId);
			}

			aController.dropStats(casReferenceId, aController.getName());
		}
		catch( Exception e)
		{
			e.printStackTrace();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);
      }
		}

		
		return true;
	}
	private boolean deliverExceptionToClient( Throwable t) {
    //  Dont send TimeOutExceptions to client
	  if ( t instanceof MessageTimeoutException ||
	       t instanceof UimaEEServiceException && 
	       t.getCause() != null && 
	       t.getCause() instanceof MessageTimeoutException)  {
      return false;
    }
    return true;
	}
}
