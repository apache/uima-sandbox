package org.apache.uima.aae.handler.input;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.handler.HandlerBase;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.statistics.DelegateStats;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.TimerStats;
import org.apache.uima.analysis_engine.asb.impl.FlowContainer;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.util.Level;

public class ProcessRequestHandler_impl extends HandlerBase
{
	private static final Class CLASS_NAME = ProcessRequestHandler_impl.class;

	public ProcessRequestHandler_impl(String aName)
	{
		super(aName);
	}

	private synchronized void cacheStats(String aCasReferenceId, long aTimeWaitingForCAS, long aTimeToDeserializeCAS )
	throws Exception
	{
		CacheEntry entry = getController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
		entry.incrementTimeWaitingForCAS( aTimeWaitingForCAS);
		entry.incrementTimeToDeserializeCAS(aTimeToDeserializeCAS);
	}
	
	private void handleProcessRequestWithXMI(MessageContext aMessageContext) throws AsynchAEException
	{
		CAS cas = null;
		String casReferenceId = null;
		long inTime = System.nanoTime();
		boolean casRegistered = false;
		boolean requestToFreeCasSent = false;
		try
		{
			boolean isNewCAS = false;
			String newCASProducedBy = null;
			String remoteCasReferenceId = null;
			//	This is only used when handling CASes produced by CAS Multiplier
			
			//	Get the CAS Reference Id of the input CAS
			casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			String inputCasReferenceId = casReferenceId;
			if ( aMessageContext.propertyExists(AsynchAEMessage.CasSequence) )
			{
				if ( aMessageContext.getEndpoint().isRemote())
				{
					remoteCasReferenceId = casReferenceId;
				}
				isNewCAS = true;
				inputCasReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.InputCasReference);
				//	This CAS came in from the CAS Multiplier. Treat it differently than the
				//	input CAS. First, in case the Aggregate needs to send this CAS to the
				//	client, retrieve the client destination by looking up the client endpoint
				//	using input CAS reference id. CASes generated by the CAS multiplier will have 
				//	the same Cas Reference id.
				Endpoint replyToEndpoint = 
					getController().
						getInProcessCache().
							getCacheEntryForCAS(inputCasReferenceId).getMessageOrigin();
				//	
				if ( getController() instanceof AggregateAnalysisEngineController )
				{

					//String key = ((AggregateAnalysisEngineController) getController()).getLastDelegateKeyFromFlow(inputCasReferenceId);
					newCASProducedBy = getController().
						getInProcessCache().
							getCacheEntryForCAS(inputCasReferenceId).getCasMultiplierKey();

					//newCASProducedBy = key;
//						((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(key); //replyToEndpoint.getEndpoint());
				
					if ( ((AggregateAnalysisEngineController)getController()).sendRequestToReleaseCas() )
					{
						try
						{
							//	Change the name of the queue where the request to free a CAS will be sent.
							aMessageContext.getEndpoint().setEndpoint(aMessageContext.getEndpoint().getEndpoint()+"CasSync");

							getController().getOutputChannel().sendRequest(AsynchAEMessage.ReleaseCAS, casReferenceId, aMessageContext.getEndpoint());
							requestToFreeCasSent = true;
						}
						catch( Exception e){}
					}
				}
				//	MessageContext contains endpoint set by the CAS Multiplier service. Overwrite
				//	this with the endpoint of the client who sent the input CAS. In case this 
				//	aggregate is configured to send new CASes to the client we know where to send them.
				aMessageContext.getEndpoint().setEndpoint(replyToEndpoint.getEndpoint());
				aMessageContext.getEndpoint().setServerURI(replyToEndpoint.getServerURI());
				//	Set this to null so that the new CAS gets its own Cas Reference Id below
				casReferenceId = null;
			}
			else if ( getController().isTopLevelComponent())
			{
				Endpoint replyToEndpoint = aMessageContext.getEndpoint(); 

				if ( getController() instanceof AggregateAnalysisEngineController )
				{
					((AggregateAnalysisEngineController)getController()).addMessageOrigin(casReferenceId, replyToEndpoint);
				}

			}
			//long arrivalTime = System.nanoTime();
			
			//	To prevent processing multiple messages with the same CasReferenceId, check the CAS cache
			//	to see if the message with a given CasReferenceId is already being processed. It is, the
			//	message contains the same request possibly issued by the caller due to timeout. Also this
			//	mechanism helps with dealing with scenario when this service is not up when the client sends
			//	request. The client can keep re-sending the same request until its timeout thresholds are
			//	exceeded. By that time, there may be multiple messages in this service queue with the same
			//	CasReferenceId. When the service finally comes back up, it will have multiple messages in
			//	its queue possibly from the same client. Only the first message for any given CasReferenceId
			//	should be processed. 
			if ( casReferenceId == null || !getController().getInProcessCache().entryExists(casReferenceId) )
			{
				String xmi = aMessageContext.getStringMessage();
				
				//	*****************************************************************
				// ***** NO XMI In Message. Kick this back to sender with exception
				//	*****************************************************************
				if ( xmi == null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_message_has_no_cargo__INFO",
			                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
					getController().
						getOutputChannel().
							sendReply(new InvalidMessageException("No XMI data in message"), casReferenceId, aMessageContext.getEndpoint(),AsynchAEMessage.Process);
					//	Dont process this empty message
					return;
				}
				
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
		                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_request_cas__FINEST",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
			    long t1 = System.nanoTime();
			    if ( isNewCAS )
			    {
					cas = getController().getCasManagerWrapper().getNewCas(newCASProducedBy);
			    }
			    else
			    {
				    cas = getController().getCasManagerWrapper().getNewCas();
			    }
				long timeWaitingForCAS = System.nanoTime() - t1;
	
				if ( getController().isStopped() )
				{
					//	The Controller is in shutdown state. 
					getController().dropCAS(cas);
					return;
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_request_cas_granted__FINE",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
			
//				if ( UIMAFramework.getLogger().isLoggable(Level.FINEST))
//				{
//					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
//			                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_got_cas__FINEST",
//			                new Object[] { aMessageContext.getEndpoint().getEndpoint(), xmi });
//				}
				
			    t1 = System.nanoTime();
				XmiSerializationSharedData deserSharedData = new XmiSerializationSharedData();
				
//if ( 1 == 1 && getController() instanceof PrimitiveAnalysisEngineController && getController().isTopLevelComponent())
//{
//	System.out.println("Simulating Exception in Deserializer");
//	throw new IndexOutOfBoundsException();
//}
				
				
				UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, -1);
				
				long timeToDeserializeCAS = System.nanoTime() - t1;
				LongNumericStatistic statistic;
				if ( (statistic = getController().getMonitor().getLongNumericStatistic("",Monitor.TotalDeserializeTime)) != null )
				{
					statistic.increment(timeToDeserializeCAS);
				}

        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_deserialize_cas_time_FINE",
                new Object[] { timeToDeserializeCAS / 1000 });
        
        
				if (casReferenceId == null)
				{
					if (getController() instanceof PrimitiveAnalysisEngineController)
					{
						inputCasReferenceId = getController().getInProcessCache().register(cas, aMessageContext, deserSharedData);
					}
					else
					{
						casReferenceId = getController().getInProcessCache().register(cas, aMessageContext, deserSharedData);
						if ( inputCasReferenceId == null )
						{
							inputCasReferenceId = casReferenceId;
						}
					}
				}
				else
				{
					getController().getInProcessCache().register(cas, aMessageContext, deserSharedData, casReferenceId);
				}
				//	Set a local flag to indicate that the CAS has been added to the cache. This will be usefull when handling an exception
				//	If an exception happens before the CAS is added to the cache, the CAS needs to be dropped immediately.
				casRegistered = true;
				if ( casReferenceId == null )
				{
					getController().saveTime(inTime, inputCasReferenceId,  getController().getName()); //aMessageContext.getEndpointName());
				}
				else
				{
					getController().saveTime(inTime, casReferenceId,  getController().getName()); //aMessageContext.getEndpointName());
				}
				
				CacheEntry entry = null;
				if ( getController() instanceof AggregateAnalysisEngineController )
				{
					entry = getController().getInProcessCache().getCacheEntryForCAS(casReferenceId);
					if ( isNewCAS )
					{
						if ( newCASProducedBy != null )
						{
							entry.setCasMultiplierKey(newCASProducedBy);
						}
						if ( remoteCasReferenceId != null )
						{
							entry.setRemoteCMCasReferenceId(remoteCasReferenceId);
						}
						if ( requestToFreeCasSent )
						{
							entry.setSendRequestToFreeCas(false);
						}
					}
					DelegateStats stats = new DelegateStats();
					if ( entry.getStat() == null )
					{
						entry.setStat(stats);
						//	Add entry for self (this aggregate). MessageContext.getEndpointName()
						//	returns the name of the queue receiving the message.
						stats.put(getController().getServiceEndpointName(), new TimerStats());
					}
					else
					{
						if (!stats.containsKey(getController().getServiceEndpointName()))
						{
							stats.put(getController().getServiceEndpointName(), new DelegateStats());
						}
					}
				}
				
				cacheStats( inputCasReferenceId, timeWaitingForCAS, timeToDeserializeCAS);
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_deserialized_cas_ready_to_process_FINE",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });

				
				cacheProcessCommandInClientEndpoint();
				
				if ( getController().isStopped() )
				{
					if ( entry != null )
					{
						//	The Controller is in shutdown state. 
						getController().dropCAS( entry.getCasReferenceId(), true);
						return;
					}
				}
				invokeProcess(cas, inputCasReferenceId, casReferenceId, aMessageContext, newCASProducedBy);
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleProcessRequestWithXMI", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_duplicate_request__INFO",
		                new Object[] { casReferenceId});
			}
        	
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleProcessRequestWithXMI", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", e);

			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Endpoint, aMessageContext.getEndpoint());
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
			errorContext.add(AsynchAEMessage.CasReference, casReferenceId );
			if ( casRegistered == false )
			{
				getController().dropCAS(cas);
			}
			getController().getErrorHandlerChain().handle(e, errorContext, getController());
		}

	}
	private void handleProcessRequestWithXCAS(MessageContext aMessageContext) throws AsynchAEException
	{
		
		try
		{
			boolean isNewCAS = false;
			String newCASProducedBy = null;
			
			//	This is only used when handling CASes produced by CAS Multiplier
			
			//	Get the CAS Reference Id of the input CAS
			String casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			String inputCasReferenceId = casReferenceId;

			if ( aMessageContext.propertyExists(AsynchAEMessage.CasSequence) )
			{
				isNewCAS = true;
				//	This CAS came in from the CAS Multiplier. Treat it differently than the
				//	input CAS. First, in case the Aggregate needs to send this CAS to the
				//	client, retrieve the client destination by looking up the client endpoint
				//	using input CAS reference id. CASes generated by the CAS multiplier will have 
				//	the same Cas Reference id.
				Endpoint replyToEndpoint = 
					getController().
						getInProcessCache().
							getCacheEntryForCAS(casReferenceId).getMessageOrigin();
				
				//	
				if ( getController() instanceof AggregateAnalysisEngineController )
				{
					newCASProducedBy = 
						((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(replyToEndpoint.getEndpoint());
				}
				//	MessageContext contains endpoint set by the CAS Multiplier service. Overwrite
				//	this with the endpoint of the client who sent the input CAS. In case this 
				//	aggregate is configured to send new CASes to the client we know where to send them.
				aMessageContext.getEndpoint().setEndpoint(replyToEndpoint.getEndpoint());
				aMessageContext.getEndpoint().setServerURI(replyToEndpoint.getServerURI());
				inputCasReferenceId = String.valueOf(casReferenceId);
				//	Set this to null so that the new CAS gets its own Cas Reference Id below
				casReferenceId = null;
			}
			
			long arrivalTime = System.nanoTime();
			getController().saveTime(arrivalTime, casReferenceId,  getController().getName());//aMessageContext.getEndpointName());
			
			//	To prevent processing multiple messages with the same CasReferenceId, check the CAS cache
			//	to see if the message with a given CasReferenceId is already being processed. It is, the
			//	message contains the same request possibly issued by the caller due to timeout. Also this
			//	mechanism helps with dealing with scenario when this service is not up when the client sends
			//	request. The client can keep re-sending the same request until its timeout thresholds are
			//	exceeded. By that time, there may be multiple messages in this service queue with the same
			//	CasReferenceId. When the service finally comes back up, it will have multiple messages in
			//	its queue possibly from the same client. Only the first message for any given CasReferenceId
			//	should be processed. 
			if ( casReferenceId == null || !getController().getInProcessCache().entryExists(casReferenceId) )
			{
				String xmi = aMessageContext.getStringMessage();
				
				//	*****************************************************************
				// ***** NO XMI In Message. Kick this back to sender with exception
				//	*****************************************************************
				if ( xmi == null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "handleProcessRequestWithXCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_message_has_no_cargo__INFO",
			                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
					getController().
						getOutputChannel().
							sendReply(new InvalidMessageException("No XMI data in message"), casReferenceId, aMessageContext.getEndpoint(),AsynchAEMessage.Process);
					//	Dont process this empty message
					return;
				}
				
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithXCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_request_cas__FINE",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
				
				CAS cas = getController().getCasManagerWrapper().getNewCas();

				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithXCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_request_cas_granted__FINE",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });

				XmiSerializationSharedData deserSharedData = new XmiSerializationSharedData();
				UimaSerializer.deserializeCasFromXmi(xmi, cas, deserSharedData, true, -1);
				
				if (casReferenceId == null)
				{
					casReferenceId = getController().getInProcessCache().register(cas, aMessageContext, deserSharedData);
				}
				else
				{
					if (getController() instanceof PrimitiveAnalysisEngineController)
					{
						getController().getInProcessCache().register(cas, aMessageContext, deserSharedData, casReferenceId);
					}
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithXCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_deserialized_cas_ready_to_process_FINE",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });

				cacheProcessCommandInClientEndpoint();

				invokeProcess(cas, inputCasReferenceId, casReferenceId, aMessageContext, newCASProducedBy);
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleProcessRequestWithXCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_duplicate_request__INFO",
		                new Object[] { casReferenceId});
			}
        	
		}
		catch ( AsynchAEException e)
		{
			throw e;
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}

	}

	private void handleProcessRequestWithCASReference(MessageContext aMessageContext) throws AsynchAEException
	{
		boolean isNewCAS = false;
		String newCASProducedBy = null;

		if ( getController().isStopped() )
		{
			return;
		}

		try
		{
			String casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);

			if ( casReferenceId == null )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleProcessRequestWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_message_has_cas_refid__INFO",
		                new Object[] { aMessageContext.getEndpoint().getEndpoint() });
				
				getController().
					getOutputChannel().
						sendReply(new InvalidMessageException("No Cas Reference Id Received From Delegate In message"), null, aMessageContext.getEndpoint(),AsynchAEMessage.Process);
				//	Dont process this empty message
				return;

			}
			Endpoint replyToEndpoint = aMessageContext.getEndpoint(); 
			if ( getController() instanceof AggregateAnalysisEngineController )
			{
				((AggregateAnalysisEngineController)getController()).addMessageOrigin(casReferenceId, replyToEndpoint);
			}
			
			//	This is only used when handling CASes produced by CAS Multiplier
			String inputCasReferenceId = null;
			CAS cas = null;
			
			if ( aMessageContext.propertyExists(AsynchAEMessage.CasSequence) )
			{
				isNewCAS = true;
				if ( replyToEndpoint == null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "handleProcessRequestWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint_for_reply__INFO",
			                new Object[] { casReferenceId });
				}
				//	
				if ( getController() instanceof AggregateAnalysisEngineController )
				{
					getController().getInProcessCache().setCasProducer(casReferenceId, replyToEndpoint.getEndpoint());
					newCASProducedBy = 
						((AggregateAnalysisEngineController)getController()).lookUpDelegateKey(replyToEndpoint.getEndpoint());
					replyToEndpoint.setIsCasMultiplier(true);
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_new_cas__FINE",
		                new Object[] { casReferenceId, newCASProducedBy });
				//	MessageContext contains endpoint set by the CAS Multiplier service. Overwrite
				//	this with the endpoint of the client who sent the input CAS. In case this 
				//	aggregate is configured to send new CASes to the client we know where to send them.
				if ( aMessageContext.getEndpoint() != null )
				{
					aMessageContext.getEndpoint().setEndpoint(replyToEndpoint.getEndpoint());
					aMessageContext.getEndpoint().setServerURI(replyToEndpoint.getServerURI());
				}

				inputCasReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.InputCasReference);
			}
			
			cas = getController().getInProcessCache().getCasByReference(casReferenceId);
			
			long arrivalTime = System.nanoTime();
			getController().saveTime(arrivalTime, casReferenceId, getController().getName());//aMessageContext.getEndpointName());

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "handleProcessRequestWithCASReference", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_analyzing_cas__FINE",
		                new Object[] { casReferenceId});

			//	Save Process command in the client endpoint.
			cacheProcessCommandInClientEndpoint();

			if ( getController().isStopped() )
			{
				return;
			}
	
			if ( isNewCAS )
			{
				invokeProcess(cas, inputCasReferenceId, casReferenceId, aMessageContext, newCASProducedBy);
			}
			else
			{
				invokeProcess(cas, casReferenceId, null,  aMessageContext, newCASProducedBy);
			}
		}
		catch ( AsynchAEException e)
		{
			throw e;
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}

	}
	
	
	
	private void cacheProcessCommandInClientEndpoint()
	{
		Endpoint clientEndpoint = getController().getClientEndpoint();
		if ( clientEndpoint != null )
		{
			clientEndpoint.setCommand(AsynchAEMessage.Process);
		}
	}
	
	private void handleCollectionProcessCompleteRequest(MessageContext aMessageContext)
	throws AsynchAEException
	{
		Endpoint replyToEndpoint = aMessageContext.getEndpoint(); 
		getController().collectionProcessComplete(replyToEndpoint);
	}

	private void handleReleaseCASRequest(MessageContext aMessageContext)
	{
		
		if ( getController() instanceof PrimitiveAnalysisEngineController )
		{
			( (PrimitiveAnalysisEngineController)getController()).releaseNextCas(); 
		}
/*
		try
		{
			String casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "handleReleaseCASRequest", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_release_cas_req__FINE",
	                new Object[] { getController().getName(), casReferenceId });
System.out.println(getController().getName()+" ::::::: Processing Release CAS Request:"+casReferenceId);	

			if ( casReferenceId != null && getController().getInProcessCache().entryExists(casReferenceId))
			{
				getController().dropCAS(casReferenceId, true);
			}
		}
		catch( Exception e)
		{
			getController().getErrorHandlerChain().handle(e, HandlerBase.populateErrorContext( (MessageContext)aMessageContext ), getController());			
		}
*/		
	}
	
	private void handleStopRequest(MessageContext aMessageContext)
	{
		System.out.println("###################Controller::"+getController().getComponentName()+" Received <<<STOP>>> Request");
		if ( getController() instanceof PrimitiveAnalysisEngineController )
		{
			try
			{
				String casReferenceId = aMessageContext.getMessageStringProperty(AsynchAEMessage.CasReference);
				( (PrimitiveAnalysisEngineController)getController()).addAbortedCasReferenceId(casReferenceId); 
			}
			catch( Exception e){}
		}
	}
	/**
	 * Main method called by the predecessor handler.  
	 *  
	 * 
	 */
	public void handle(Object anObjectToHandle) //throws AsynchAEException
	{
		try
		{
			super.validate(anObjectToHandle);

			MessageContext messageContext = (MessageContext) anObjectToHandle;
			if ( isHandlerForMessage(messageContext, AsynchAEMessage.Request, AsynchAEMessage.Process) ||
	 			 isHandlerForMessage(messageContext, AsynchAEMessage.Request, AsynchAEMessage.CollectionProcessComplete) ||
	 			 isHandlerForMessage(messageContext, AsynchAEMessage.Request, AsynchAEMessage.ReleaseCAS ) ||
	 			 isHandlerForMessage(messageContext, AsynchAEMessage.Request, AsynchAEMessage.Stop ) 
				)
			{
				int payload = ((MessageContext) anObjectToHandle).getMessageIntProperty(AsynchAEMessage.Payload);
				int command = messageContext.getMessageIntProperty(AsynchAEMessage.Command);

				getController().getControllerLatch().waitUntilInitialized();
				
				
				if (AsynchAEMessage.CASRefID == payload)
				{
					handleProcessRequestWithCASReference(messageContext);
				}
				else if (AsynchAEMessage.XMIPayload == payload)
				{
					handleProcessRequestWithXMI(messageContext);
				}
				else if (AsynchAEMessage.XCASPayload == payload)
				{
					handleProcessRequestWithXCAS(messageContext);
				}
				else if ( AsynchAEMessage.None == payload && AsynchAEMessage.CollectionProcessComplete == command)
				{
					handleCollectionProcessCompleteRequest(messageContext);
				}
				else if ( AsynchAEMessage.None == payload && AsynchAEMessage.ReleaseCAS == command)
				{
					handleReleaseCASRequest(messageContext);
				}
				else if ( AsynchAEMessage.None == payload && AsynchAEMessage.Stop == command)
				{
					handleStopRequest(messageContext);
				}
				// Handled Request
				return;
			}
			// Not a Request nor Command. Delegate to the next handler in the chain
			super.delegate(messageContext);
		}
		catch( Exception e)
		{
			getController().getErrorHandlerChain().handle(e, HandlerBase.populateErrorContext( (MessageContext)anObjectToHandle ), getController());			
		}
	}


}
