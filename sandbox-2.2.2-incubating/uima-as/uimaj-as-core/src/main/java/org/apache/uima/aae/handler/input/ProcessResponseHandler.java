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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
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
import org.apache.uima.aae.monitor.statistics.DelegateStats;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.TimerStats;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.util.Level;

public class ProcessResponseHandler extends HandlerBase
{
	private static final Class CLASS_NAME = ProcessResponseHandler.class;

	private Object monitor = new Object();

	public ProcessResponseHandler(String aName)
	{
		super(aName);
	}

	private void aggregateDelegateStats(MessageContext aMessageContext, String aCasReferenceId) throws AsynchAEException
	{
		String delegateKey = "";
		try
		{
			
			delegateKey = ((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(aMessageContext.getEndpoint().getEndpoint());
			CacheEntry entry = getController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
			if ( entry == null )
			{
				throw new AsynchAEException("CasReferenceId:"+aCasReferenceId+" Not Found in the Cache.");
			}
			CacheEntry inputCasEntry = null;
			String inputCasReferenceId = entry.getInputCasReferenceId();
			ServicePerformance casStats = 
				((AggregateAnalysisEngineController)getController()).getCasStatistics(aCasReferenceId);
			if ( inputCasReferenceId != null && 
				 getController().getInProcessCache().entryExists(inputCasReferenceId) )
			{
				String casProducerKey = entry.getCasProducerKey();
				if ( casProducerKey != null &&
					((AggregateAnalysisEngineController)getController()).
						isDelegateKeyValid(casProducerKey) )
				{
					//	Get entry for the input CAS
					inputCasEntry = getController().
								getInProcessCache().
									getCacheEntryForCAS(inputCasReferenceId);
				}
				
			}
			ServicePerformance delegateServicePerformance =
				((AggregateAnalysisEngineController)getController()).getServicePerformance(delegateKey);

			if (aMessageContext.propertyExists(AsynchAEMessage.TimeToSerializeCAS))
			{
				long timeToSerializeCAS = ((Long) aMessageContext.getMessageLongProperty(AsynchAEMessage.TimeToSerializeCAS)).longValue();
				if ( timeToSerializeCAS > 0)
				{
					casStats.incrementCasSerializationTime(timeToSerializeCAS);
					if ( delegateServicePerformance != null )
					{
						delegateServicePerformance.
						incrementCasSerializationTime(timeToSerializeCAS);
					}
					getController().getServicePerformance().
						incrementCasSerializationTime(timeToSerializeCAS);
				}
			}
			if (aMessageContext.propertyExists(AsynchAEMessage.TimeToDeserializeCAS))
			{
				long timeToDeserializeCAS = ((Long) aMessageContext.getMessageLongProperty(AsynchAEMessage.TimeToDeserializeCAS)).longValue();
				if ( timeToDeserializeCAS > 0 )
				{
					casStats.incrementCasDeserializationTime(timeToDeserializeCAS);

					if ( delegateServicePerformance != null )
					{
						delegateServicePerformance.
							incrementCasDeserializationTime(timeToDeserializeCAS);
					}
					getController().getServicePerformance().
						incrementCasDeserializationTime(timeToDeserializeCAS);
				}
			}

			if (aMessageContext.propertyExists(AsynchAEMessage.IdleTime))
			{
				long idleTime = ((Long) aMessageContext.getMessageLongProperty(AsynchAEMessage.IdleTime)).longValue();
				if ( idleTime > 0 )
				{
					casStats.incrementIdleTime(idleTime);
					if ( delegateServicePerformance != null )
					{
						delegateServicePerformance.
							incrementIdleTime(idleTime);
					}
				}
			}
			
			if (aMessageContext.propertyExists(AsynchAEMessage.TimeWaitingForCAS))
			{
				long timeWaitingForCAS = ((Long) aMessageContext.getMessageLongProperty(AsynchAEMessage.TimeWaitingForCAS)).longValue();
				if ( aMessageContext.getEndpoint().isRemote())
				{
					entry.incrementTimeWaitingForCAS(timeWaitingForCAS);
					if ( inputCasEntry != null )
					{
						inputCasEntry.incrementTimeWaitingForCAS(timeWaitingForCAS);
					}
				}
			}
			if (aMessageContext.propertyExists(AsynchAEMessage.TimeInProcessCAS))
			{
				long timeInProcessCAS = ((Long) aMessageContext.getMessageLongProperty(AsynchAEMessage.TimeInProcessCAS)).longValue();
				casStats.incrementAnalysisTime(timeInProcessCAS);
				if ( delegateServicePerformance != null )
				{
					delegateServicePerformance.
						incrementAnalysisTime(timeInProcessCAS);
				}
				//	Accumulate processing time
				getController().getServicePerformance().
					incrementAnalysisTime(timeInProcessCAS);
				if ( inputCasReferenceId != null )
				{
					ServicePerformance inputCasStats = 
						((AggregateAnalysisEngineController)getController()).
							getCasStatistics(inputCasReferenceId);
					// Update processing time for this CAS
					if ( inputCasStats != null )
					{
						inputCasStats.incrementAnalysisTime(timeInProcessCAS);
					}
				}
			}
		}
		catch( AsynchAEException e)
		{
			throw e;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	private void computeStats(MessageContext aMessageContext, String aCasReferenceId) throws AsynchAEException
	{
		if (aMessageContext.propertyExists(AsynchAEMessage.TimeInService))
		{
			long departureTime = getController().getTime(aCasReferenceId, aMessageContext.getEndpoint().getEndpoint());
			long currentTime = System.nanoTime();
			long roundTrip = currentTime - departureTime;
			long timeInService = aMessageContext.getMessageLongProperty(AsynchAEMessage.TimeInService);
			long totalTimeInComms = currentTime - (departureTime - timeInService);

			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "computeStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_roundtrip_time__FINE",
	                new Object[] { aCasReferenceId, aMessageContext.getEndpoint(),(double) roundTrip / (double) 1000000 });

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "computeStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_time_spent_in_delegate__FINE",
	                new Object[] { aCasReferenceId, (double) timeInService / (double) 1000000, aMessageContext.getEndpoint() });

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "computeStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_time_spent_in_comms__FINE",
	                new Object[] { aCasReferenceId, (double) totalTimeInComms / (double) 1000000, aMessageContext.getEndpoint() });
		}
		
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				aggregateDelegateStats( aMessageContext, aCasReferenceId );
			}			
	}

	private Endpoint lookupEndpoint(String anEndpointName, String aCasReferenceId)
	{
		return getController().getInProcessCache().getEndpoint(anEndpointName, aCasReferenceId);
	}

	private void cancelTimer(MessageContext aMessageContext, String aCasReferenceId, boolean removeEndpoint) throws AsynchAEException
	{
		if (aMessageContext != null && aMessageContext.getEndpoint() != null)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "cancelTimer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cancel_timer__FINE",
	                new Object[] { aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId });

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
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "cancelTimer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_endpoint_not_found__INFO",
			                new Object[] { aMessageContext.getEndpoint().getEndpoint(), aCasReferenceId });
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

	private void handleProcessResponseWithXMI(MessageContext aMessageContext, String aDelegateKey)
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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
		                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_invalid_endpoint__WARNING",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint(), casReferenceId});
				throw new AsynchAEException("Unable to lookup endpoint:"+aMessageContext.getEndpoint().getEndpoint()+ " Cas Reference Id:"+casReferenceId);
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
			cacheEntry.setReplyReceived();
			cas = cacheEntry.getCas();  
			int totalNumberOfParallelDelegatesProcessingCas = cacheEntry.getNumberOfParallelDelegates();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_number_parallel_delegates_FINE",
	                new Object[] { totalNumberOfParallelDelegatesProcessingCas});

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
			
			long t1 = System.nanoTime();
			
			synchronized (monitor)
			{
				XmiSerializationSharedData deserSharedData;
				if (totalNumberOfParallelDelegatesProcessingCas > 1 && cacheEntry.howManyDelegatesResponded() > 0)
				{
          // process secondary reply from a parallel step
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
			                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_delegate_responded_count_FINEST",
			                new Object[] { cacheEntry.howManyDelegatesResponded(), casReferenceId});
					
					int highWaterMark = cacheEntry.getHighWaterMark();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
			                "handleProcessResponseWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_high_water_mark_FINEST",
			                new Object[] { highWaterMark, casReferenceId });

          deserSharedData = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId).getDeserSharedData();
					UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, highWaterMark);
				}
        else // general case, or first reply from a parallel step
				{
					//	Processing the reply from a standard, non-parallel delegate
          deserSharedData = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId).getDeserSharedData();
          if (deserSharedData == null) {
            deserSharedData = new XmiSerializationSharedData();
            getController().getInProcessCache().getCacheEntryForCAS(casReferenceId).setXmiSerializationData(deserSharedData);
          }
          UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, -1);
				}
			}

			
			if ( cacheEntry != null && totalNumberOfParallelDelegatesProcessingCas > 1 )
			{
				synchronized( cacheEntry )
				{
					cacheEntry.incrementHowManyDelegatesResponded();
				}
			}
		
			long timeToDeserializeCAS = System.nanoTime() - t1;

            getController().
            	getServicePerformance().
            		incrementCasDeserializationTime(timeToDeserializeCAS);

            ServicePerformance casStats =
            	getController().getCasStatistics(casReferenceId);
			casStats.incrementCasDeserializationTime(timeToDeserializeCAS);
			LongNumericStatistic statistic;
			if ( (statistic = getController().getMonitor().getLongNumericStatistic("",Monitor.TotalDeserializeTime)) != null )
			{
				statistic.increment(timeToDeserializeCAS);
			}

			computeStats(aMessageContext, casReferenceId);

			cancelTimer(aMessageContext, casReferenceId, true);

			// Send CAS for processing when all delegates reply
			// totalNumberOfParallelDelegatesProcessingCas indicates how many delegates are processing CAS in parallel. Default is 1, meaning only
			// one delegate processes the CAS at the same. Otherwise, check if all delegates responded before passing CAS on to the Flow Controller.
			// The idea is that all delegates processing one CAS concurrently must respond, before the CAS is allowed to move on to the next step.
			// HowManyDelegatesResponded is incremented every time a parallel delegate sends response.
			if (totalNumberOfParallelDelegatesProcessingCas == 1 || ( cacheEntry.howManyDelegatesResponded() == totalNumberOfParallelDelegatesProcessingCas) )
			{
				cacheEntry.resetDelegateResponded();
				super.invokeProcess(cas, casReferenceId, null, aMessageContext, null);
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
		}

	}
	private void handleProcessResponseWithCASReference(MessageContext aMessageContext )
	{
		String casReferenceId = null;
		CacheEntry cacheEntry = null;

		try
		{
			casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			cacheEntry = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId);
			cacheEntry.setReplyReceived();
			CAS cas = cacheEntry.getCas();
			String endpointName = aMessageContext.getEndpoint().getEndpoint();
			String delegateKey = ((AggregateAnalysisEngineController)getController()).
									lookUpDelegateKey(endpointName);
			ServicePerformance delegateServicePerformance = 
				((AggregateAnalysisEngineController)getController()).
					getServicePerformance(delegateKey);
			if ( delegateServicePerformance != null )
			{
				delegateServicePerformance.incrementNumberOfCASesProcessed();
			}

			//CAS cas = getController().getInProcessCache().getCasByReference(casReferenceId);
			if (cas != null)
			{
				cancelTimerAndProcess(aMessageContext, casReferenceId, cas);
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleProcessResponseWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_not_in_cache__INFO",
		                new Object[] { getController().getName(), casReferenceId, aMessageContext.getEndpoint().getEndpoint() });
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
	private void handleProcessResponseWithException(MessageContext aMessageContext)
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "handleProcessResponseWithException", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_handling_exception_from_delegate_FINE",
                new Object[] { getController().getName(), aMessageContext.getEndpoint().getEndpoint() });

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
					processCollectionCompleteReplyFromDelegate(aMessageContext.getEndpoint().getEndpoint(), false);
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

	private void handleCollectionProcessCompleteReply(MessageContext aMessageContext)
	{
		try
		{
			String delegateKey = ((Endpoint)aMessageContext.getEndpoint()).getEndpoint();
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
	public synchronized void handle(Object anObjectToHandle) throws AsynchAEException
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
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				key = ((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(delegate);
			}
			if (AsynchAEMessage.CASRefID == payload)
			{
				
				handleProcessResponseWithCASReference(messageContext);
				if ( key != null )
				{
					resetErrorCounts(key);
				}
			}
			else if (AsynchAEMessage.XMIPayload == payload)
			{
				handleProcessResponseWithXMI(messageContext, key);
				if ( key != null )
				{
					resetErrorCounts(key);
				}
			}
			else if (AsynchAEMessage.Exception == payload)
			{
				handleProcessResponseWithException(messageContext);
			}
			else if (AsynchAEMessage.None == payload && AsynchAEMessage.CollectionProcessComplete == command)
			{
				handleCollectionProcessCompleteReply(messageContext);
			}
			else if (AsynchAEMessage.None == payload && AsynchAEMessage.ACK == command)
			{
				handleACK(messageContext);
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
