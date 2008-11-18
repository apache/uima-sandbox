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

package org.apache.uima.aae.handler.input;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.controller.LocalCache.CasStateEntry;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ExpiredMessageException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.handler.HandlerBase;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.AllowPreexistingFS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.util.Level;

public class ProcessResponseHandler extends HandlerBase
{
	private static final Class CLASS_NAME = ProcessResponseHandler.class;

  private UimaSerializer uimaSerializer = new UimaSerializer();
  
	public ProcessResponseHandler(String aName)
	{
		super(aName);
	}

	private Endpoint lookupEndpoint(String anEndpointName, String aCasReferenceId)
	{
		return getController().getInProcessCache().getEndpoint(anEndpointName, aCasReferenceId);
	}

	private void cancelTimer(MessageContext aMessageContext, String aCasReferenceId, boolean removeEndpoint) throws AsynchAEException
	{
		if (aMessageContext != null && aMessageContext.getEndpoint() != null)
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "cancelTimer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cancel_timer__FINE",
	                new Object[] { aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId });
      }
			// Retrieve the endpoint from the cache using endpoint name
			// and casRefereceId
			if ( aCasReferenceId == null && 
				 aMessageContext.propertyExists(AsynchAEMessage.Command) &&
				 aMessageContext.getMessageIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.CollectionProcessComplete
			    )
			{
				aCasReferenceId = ":CpC";
			}
			if ( aMessageContext != null && aMessageContext.getEndpoint() != null )
			{
				Endpoint endpoint = lookupEndpoint(aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId);

				if (endpoint != null)
				{
					// Received the response within timeout interval so
					// cancel the running timer
					endpoint.cancelTimer();
					if ( removeEndpoint )
					{
						getController().getInProcessCache().removeEndpoint(aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId);
					}
				}
				else
				{
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "cancelTimer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_endpoint_not_found__INFO",
			                new Object[] { aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId });
          }
				}
			}
		}
	}

	private void cancelTimerAndProcess(MessageContext aMessageContext, String aCasReferenceId, CAS aCAS) throws AsynchAEException
	{
		computeStats(aMessageContext, aCasReferenceId);

		cancelTimer(aMessageContext, aCasReferenceId, true);
		super.invokeProcess(aCAS, aCasReferenceId, null, aMessageContext, null);

	}

	private boolean isMessageExpected(String aCasReferenceId, Endpoint anEndpointWithTimer)

	{
		if (getController().getInProcessCache().entryExists(aCasReferenceId) && anEndpointWithTimer.isWaitingForResponse())
		{
			return true;
		}
		return false;
	}

	private void handleUnexpectedMessage(String aCasReferenceId, Endpoint anEndpoint)
	{
		// Cas does not exist in the CAS Cache. This would be possible if the
		// CAS has been dropped due to timeout and the delegate
		// sends the response later. In asynch communication this scenario is
		// possible. The service may not be up when the client sends
		// the message. Messages accumulate in the service queue until the
		// service becomes available. When this happens, the service
		// will pickup messages from the queue, process them and send respones
		// to an appropriate response queue. Most likely such
		// respones should be thrown away. Well perhaps logged first.
		ErrorContext errorContext = new ErrorContext();
		errorContext.add(AsynchAEMessage.CasReference, aCasReferenceId);
		errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
		errorContext.add(AsynchAEMessage.Endpoint, anEndpoint);
		AnalysisEngineController controller = getController();
		controller.getErrorHandlerChain().handle(new ExpiredMessageException(), errorContext, controller);

	}

	private synchronized void handleProcessResponseFromRemoteDelegate(MessageContext aMessageContext, String aDelegateKey)
	{
		CAS cas = null;
		String casReferenceId = null;
		Endpoint endpointWithTimer = null;
		try
		{
			casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			endpointWithTimer = lookupEndpoint(aMessageContext.getEndpoint().getEndpoint(), casReferenceId);

			if ( endpointWithTimer == null )
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
		                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_invalid_endpoint__WARNING",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint(), casReferenceId});
        }
				return;
			}
			
			// Check if this reply message is expected. A message is expected
			// when it contains a reply for request
			// that has not been already received nor the timeout for the
			// request has been handled. This is done
			// to catch duplicate replies for the same request. If a cache entry
			// does not exist with a given CAS reference id
			// it means that the initial request has been canceled and the CAS
			// has either been processed or dropped. If a duplicate
			// reply arrives containing CAS that is still being processed in
			// this aggregate, the endpoint state will show that
			// it is not waiting for a response.
			if (!isMessageExpected(casReferenceId, endpointWithTimer))
			{
				handleUnexpectedMessage(casReferenceId, aMessageContext.getEndpoint());
				return;
			}
			//	Increment number of CASes processed by this delegate
			if ( aDelegateKey != null)
			{
				ServicePerformance delegateServicePerformance =
					((AggregateAnalysisEngineController)getController()).
						getServicePerformance(aDelegateKey);
				if ( delegateServicePerformance != null )
				{
					delegateServicePerformance.incrementNumberOfCASesProcessed();
				}
			}
			
			String xmi = aMessageContext.getStringMessage();

			//	Fetch entry from the cache for a given Cas Id. The entry contains a CAS that will be used during deserialization 
			CacheEntry cacheEntry = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId);
			
      CasStateEntry casStateEntry = 
        ((AggregateAnalysisEngineController)getController()).
            getLocalCache().lookupEntry(casReferenceId);
      if ( casStateEntry != null ) {
        casStateEntry.setReplyReceived();
      }
			
			cas = cacheEntry.getCas();  
			int totalNumberOfParallelDelegatesProcessingCas = casStateEntry.getNumberOfParallelDelegates();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_number_parallel_delegates_FINE",
	                new Object[] { totalNumberOfParallelDelegatesProcessingCas});
      }
			if (cas == null)
			{
				throw new AsynchAEException(Thread.currentThread().getName()+"-Cache Does not contain a CAS. Cas Reference Id::"+casReferenceId);
			}
			if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
		                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_rcvd_reply_FINEST",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint(), casReferenceId, xmi });
			}
			long t1 = getController().getCpuTime();
      /* --------------------- */
      /** DESERIALIZE THE CAS. */
      /* --------------------- */

      // check if the CAS is part of the Parallel Step
      if (totalNumberOfParallelDelegatesProcessingCas > 1) {
        // Synchronized because replies are merged into the same CAS.
        synchronized (cas) {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAEE_delegate_responded_count_FINEST",
                    new Object[] { casStateEntry.howManyDelegatesResponded(), casReferenceId });
          }
          // If a delta CAS, merge it while checking that no pre-existing FSs are modified.
          if (aMessageContext.getMessageBooleanProperty(AsynchAEMessage.SentDeltaCas)) {
            int highWaterMark = cacheEntry.getHighWaterMark();
            deserialize(xmi, cas, casReferenceId, highWaterMark, AllowPreexistingFS.disallow);
          } else {
            // If not a delta CAS (old service), take all of first reply, and merge in the new
            // entries in the later replies. Ignoring pre-existing FS for 2.2.2 compatibility
            if (casStateEntry.howManyDelegatesResponded() == 0) {
              deserialize(xmi, cas, casReferenceId);
            } else { // process secondary reply from a parallel step
              int highWaterMark = cacheEntry.getHighWaterMark();
              deserialize(xmi, cas, casReferenceId, highWaterMark, AllowPreexistingFS.ignore);
            }
          }
          casStateEntry.incrementHowManyDelegatesResponded();
        }
      } else { // Processing a reply from a non-parallel delegate (binary or delta xmi or xmi)
        String serializationStrategy = endpointWithTimer.getSerializer();
        if (serializationStrategy.equals("binary")) {
          byte[] binaryData = aMessageContext.getByteMessage();
          uimaSerializer.deserializeCasFromBinary(binaryData, cas);
        } else {
          if (aMessageContext.getMessageBooleanProperty(AsynchAEMessage.SentDeltaCas)) {
            int highWaterMark = cacheEntry.getHighWaterMark();
            deserialize(xmi, cas, casReferenceId, highWaterMark, AllowPreexistingFS.allow);
          } else {
            deserialize(xmi, cas, casReferenceId);
          }
        }
      }

      long timeToDeserializeCAS = getController().getCpuTime() - t1;

      getController().getServicePerformance().incrementCasDeserializationTime(timeToDeserializeCAS);

      ServicePerformance casStats = getController().getCasStatistics(casReferenceId);
      casStats.incrementCasDeserializationTime(timeToDeserializeCAS);
      LongNumericStatistic statistic;
      if ((statistic = getController().getMonitor().getLongNumericStatistic("",
              Monitor.TotalDeserializeTime)) != null) {
        statistic.increment(timeToDeserializeCAS);
      }

      computeStats(aMessageContext, casReferenceId);

      cancelTimer(aMessageContext, casReferenceId, true);

      // Send CAS for processing when all delegates reply
      // totalNumberOfParallelDelegatesProcessingCas indicates how many delegates are processing CAS
      // in parallel. Default is 1, meaning only one delegate processes the CAS at the same.
      // Otherwise, check if all delegates responded before passing CAS on to the Flow Controller.
      // The idea is that all delegates processing one CAS concurrently must respond, before the CAS
      // is allowed to move on to the next step.
      // HowManyDelegatesResponded is incremented every time a parallel delegate sends response.
      if (totalNumberOfParallelDelegatesProcessingCas == 1
              || (casStateEntry.howManyDelegatesResponded() == totalNumberOfParallelDelegatesProcessingCas)) {
        casStateEntry.resetDelegateResponded();
        super.invokeProcess(cas, casReferenceId, null, aMessageContext, null);
      }

    } catch (Exception e) {
      e.printStackTrace();
      ErrorContext errorContext = new ErrorContext();
      errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
      errorContext.add(AsynchAEMessage.CasReference, casReferenceId);
      errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
      getController().getErrorHandlerChain().handle(e, errorContext, getController());
    } finally {
      incrementDelegateProcessCount(aMessageContext);
    }

	}
	
	private void deserialize( String xmi, CAS cas, String casReferenceId, int highWaterMark, AllowPreexistingFS allow ) throws Exception
	{
		XmiSerializationSharedData deserSharedData;
		deserSharedData = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId).getDeserSharedData();
//    UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, highWaterMark, allow);
    uimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, highWaterMark, allow);
	}
	/**
	private void deserialize( String xmi, CAS cas, String casReferenceId, int highWaterMark ) throws Exception
	{
		XmiSerializationSharedData deserSharedData;
		deserSharedData = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId).getDeserSharedData();
		UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, highWaterMark);
	}
	**/
	private void deserialize( String xmi, CAS cas, String casReferenceId ) throws Exception
	{
	  CacheEntry entry = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId);
		//	Processing the reply from a standard, non-parallel delegate
		XmiSerializationSharedData deserSharedData;
		deserSharedData = entry.getDeserSharedData();
		if (deserSharedData == null) {
			deserSharedData = new XmiSerializationSharedData();
			entry.setXmiSerializationData(deserSharedData);
		}
//    UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, -1);
    uimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, -1);
	}
	
	
	private synchronized void handleProcessResponseWithCASReference(MessageContext aMessageContext )
	{
		String casReferenceId = null;
		CacheEntry cacheEntry = null;

		try
		{
			casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			cacheEntry = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId);
      CasStateEntry casStateEntry = 
        ((AggregateAnalysisEngineController)getController()).
            getLocalCache().lookupEntry(casReferenceId);
      if ( casStateEntry != null ) {
        casStateEntry.setReplyReceived();
      }
			
			CAS cas = cacheEntry.getCas();
			String endpointName = aMessageContext.getEndpoint().getEndpoint();
			String delegateKey = ((AggregateAnalysisEngineController)getController()).
									lookUpDelegateKey(endpointName);
			ServicePerformance delegateServicePerformance = 
				((AggregateAnalysisEngineController)getController()).
					getServicePerformance(delegateKey);
			/*
			if ( delegateServicePerformance != null )
			{
				delegateServicePerformance.incrementNumberOfCASesProcessed();
			}
*/
			//CAS cas = getController().getInProcessCache().getCasByReference(casReferenceId);
			if (cas != null)
			{
				cancelTimerAndProcess(aMessageContext, casReferenceId, cas);
			}
			else
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleProcessResponseWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_not_in_cache__INFO",
		                new Object[] { getController().getName(), casReferenceId, aMessageContext.getEndpoint().getEndpoint() });
        }
				throw new AsynchAEException("CAS with Reference Id:" + casReferenceId + " Not Found in CasManager's CAS Cache");
			}
		}
		catch ( Exception e)
		{
			
			e.printStackTrace();
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
			errorContext.add(AsynchAEMessage.CasReference, casReferenceId );
			errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
			getController().getErrorHandlerChain().handle(e, errorContext, getController());
		}
		finally
		{
			incrementDelegateProcessCount(aMessageContext);
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				try
				{
					String endpointName = aMessageContext.getEndpoint().getEndpoint();
					String delegateKey = 
						((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(endpointName);
					if ( delegateKey != null )
					{
						Endpoint endpoint =
							((AggregateAnalysisEngineController)getController()).lookUpEndpoint(delegateKey, false);

						//	Check if the multiplier aborted during processing of this input CAS
						if ( endpoint != null && endpoint.isCasMultiplier() && cacheEntry.isAborted() )
						{
							if ( !getController().getInProcessCache().isEmpty() )
							{
								getController().getInProcessCache().registerCallbackWhenCacheEmpty(getController().getEventListener());
							}
							else
							{
								//	Callback to notify that the cache is empty
								getController().getEventListener().onCacheEmpty();
							}
						}
						
					}
				}
				catch( Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}
	private void incrementDelegateProcessCount(MessageContext aMessageContext)
	{
		Endpoint endpoint = aMessageContext.getEndpoint();
		if ( endpoint != null && getController() instanceof AggregateAnalysisEngineController)
		{
			try
			{
				String delegateKey = ((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(endpoint.getEndpoint());
				LongNumericStatistic stat = getController().getMonitor().getLongNumericStatistic(delegateKey, Monitor.ProcessCount);
				stat.increment();
			}
			catch( Exception e) {}
		}
		
	}
	
	private boolean isException( Object object )
	{
		return (object instanceof Exception || object instanceof Throwable );
	}
	
	private boolean isShutdownException( Object object )
	{
		return ( object instanceof Exception && 
				 object instanceof UimaEEServiceException && 
				 ((UimaEEServiceException)object).getCause() != null &&
				 ((UimaEEServiceException)object).getCause() instanceof ServiceShutdownException
		        );
	}
	private boolean ignoreException(Object object)
	{
		if ( object != null && isException(object) && !isShutdownException(object) )
		{
			return false;
		}
		return true;
	}
	private synchronized void handleProcessResponseWithException(MessageContext aMessageContext, String delegateKey)
	{
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "handleProcessResponseWithException", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_handling_exception_from_delegate_FINE",
                new Object[] { getController().getName(), aMessageContext.getEndpoint().getEndpoint() });
    }
		boolean isCpCError = false;
		String casReferenceId = null;


    try
		{
      //  If a Process Request, increment number of docs processed
      if ( aMessageContext.getMessageIntProperty(AsynchAEMessage.MessageType) == AsynchAEMessage.Response &&
         aMessageContext.getMessageIntProperty(AsynchAEMessage.Command)== AsynchAEMessage.Process )
      {
        //  Increment number of CASes processed by a delegate
        incrementDelegateProcessCount(aMessageContext);
      }

      Object object = aMessageContext.getObjectMessage();
			if ( object == null )
			{
				//	 Could be a C++ exception. In this case the exception is just a String in the message cargo
				if ( aMessageContext.getStringMessage() != null )
				{
					object = new UimaEEServiceException(aMessageContext.getStringMessage());
				}
			}
			if ( ignoreException(object) )
			{
				return;
			}
			
			if ( getController() instanceof AggregateAnalysisEngineController &&
				 aMessageContext.propertyExists(AsynchAEMessage.Command ) &&
				 aMessageContext.getMessageIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.CollectionProcessComplete
			   )
			{
				isCpCError = true;
				((AggregateAnalysisEngineController)getController()).
					processCollectionCompleteReplyFromDelegate(delegateKey, false);
			}
			else
			{
				casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
				cancelTimer(aMessageContext, casReferenceId, false);
			}

			if ( object != null && (object instanceof Exception || object instanceof Throwable ))
			{
				
				
				Exception remoteException = (Exception) object;
				ErrorContext errorContext = new ErrorContext();
				errorContext.add(AsynchAEMessage.Command, aMessageContext.getMessageIntProperty(AsynchAEMessage.Command));
				errorContext.add(AsynchAEMessage.MessageType, aMessageContext.getMessageIntProperty(AsynchAEMessage.MessageType));
				if ( !isCpCError )
				{
					errorContext.add(AsynchAEMessage.CasReference, casReferenceId);
				}
				errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
				getController().getErrorHandlerChain().handle(remoteException, errorContext, getController());
			}
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
			errorContext.add(AsynchAEMessage.CasReference, casReferenceId );
			errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
			getController().getErrorHandlerChain().handle(e, errorContext, getController());
		}

	}

	private void handleCollectionProcessCompleteReply(MessageContext aMessageContext, String delegateKey)
	{
		try
		{
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				((AggregateAnalysisEngineController) getController())
					.processCollectionCompleteReplyFromDelegate(delegateKey, true);
			}
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.CollectionProcessComplete);
			errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
			getController().getErrorHandlerChain().handle(e, errorContext, getController());
		}

	}
	private void resetErrorCounts(String aDelegate)
	{
		getController().getMonitor().resetCountingStatistic(aDelegate, Monitor.ProcessErrorCount);
		getController().getMonitor().resetCountingStatistic(aDelegate, Monitor.ProcessErrorRetryCount);
	}
	
	private void handlePingReply( MessageContext aMessageContext ) {
	  try {
	    
	  } catch ( Exception e) {
	    e.printStackTrace();
	  }
	}
	
	public  void handle(Object anObjectToHandle) throws AsynchAEException
	{
		super.validate(anObjectToHandle);
		MessageContext messageContext = (MessageContext) anObjectToHandle;
		
		if (isHandlerForMessage(messageContext, AsynchAEMessage.Response, AsynchAEMessage.Process) ||
			isHandlerForMessage(messageContext, AsynchAEMessage.Response, AsynchAEMessage.ACK ) ||
		    isHandlerForMessage(messageContext, AsynchAEMessage.Response, AsynchAEMessage.CollectionProcessComplete) )
		{
			int payload = messageContext.getMessageIntProperty(AsynchAEMessage.Payload);
			int command = messageContext.getMessageIntProperty(AsynchAEMessage.Command);
			String delegate = ((Endpoint)messageContext.getEndpoint()).getEndpoint();
			String key = null;
			String fromServer = null;
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				if ( ((Endpoint)messageContext.getEndpoint()).isRemote() )
				{
					if ( ((MessageContext)anObjectToHandle).propertyExists(AsynchAEMessage.EndpointServer))
					{						
						fromServer =((MessageContext)anObjectToHandle).getMessageStringProperty(AsynchAEMessage.EndpointServer); 
					}
					// If old service does not echo back the external broker name then the queue name must be unique. 
					// Can't use the ServerURI set by the service as it may be its local name for the broker, e.g. tcp://localhost:61616
				}

				key = ((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(delegate, fromServer);
			}
			if (AsynchAEMessage.CASRefID == payload)
			{
				
				handleProcessResponseWithCASReference(messageContext);
				if ( key != null )
				{
					resetErrorCounts(key);
				}
			}
			else if (AsynchAEMessage.XMIPayload == payload || AsynchAEMessage.BinaryPayload == payload)
			{
			  handleProcessResponseFromRemoteDelegate(messageContext, key);
				if ( key != null )
				{
					resetErrorCounts(key);
				}
			}
			else if (AsynchAEMessage.Exception == payload)
			{
				if ( key == null )
				{
					key = ((Endpoint)messageContext.getEndpoint()).getEndpoint();
				}
				handleProcessResponseWithException(messageContext, key);
			}
			else if (AsynchAEMessage.None == payload && AsynchAEMessage.CollectionProcessComplete == command)
			{
				if ( key == null )
				{
					key = ((Endpoint)messageContext.getEndpoint()).getEndpoint();
				}
				handleCollectionProcessCompleteReply(messageContext, key);
			}
			else if (AsynchAEMessage.None == payload && AsynchAEMessage.ACK == command)
			{
				handleACK(messageContext);
			}
      else if (AsynchAEMessage.None == payload && AsynchAEMessage.Ping == command)
      {
        handlePingReply(messageContext);
      }

			else
			{
				throw new AsynchAEException("Invalid Payload. Expected XMI or CasReferenceId Instead Got::" + payload);
			}
			
			
			// Handled Request to Process with A given Payload
			return;
		}
		// Not a Request nor Command. Delegate to the next handler in the chain
		super.delegate(messageContext);

	}
	private void handleACK(MessageContext aMessageContext)
	throws AsynchAEException
	{
		if ( getController() instanceof PrimitiveAnalysisEngineController )
		{
//			((PrimitiveAnalysisEngineController) getController()).throttleNext();
		}
	}

}
