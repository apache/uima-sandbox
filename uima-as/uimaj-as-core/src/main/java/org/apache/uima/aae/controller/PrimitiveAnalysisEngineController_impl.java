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

package org.apache.uima.aae.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.AsynchAECasManager;
import org.apache.uima.aae.InProcessCache;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.jmx.JmxManagement;
import org.apache.uima.aae.jmx.PrimitiveServiceInfo;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.spi.transport.UimaMessage;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.CasIterator;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.OutOfTypeSystemData;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.impl.ConfigurationParameter_impl;
import org.apache.uima.util.Level;

public class PrimitiveAnalysisEngineController_impl 
extends BaseAnalysisEngineController implements PrimitiveAnalysisEngineController
{
	private static final Class CLASS_NAME = PrimitiveAnalysisEngineController_impl.class;
	//	Stores AE metadata
	private AnalysisEngineMetaData analysisEngineMetadata;

	//	Number of AE instances
	private int analysisEnginePoolSize;
	//	Mutex
	protected Object notifyObj = new Object();
	//	Temp list holding instances of AE 
	private List aeList = new ArrayList();
	//	Stores service info for JMX
	private PrimitiveServiceInfo serviceInfo = null;
	//	Pool containing instances of AE. The default implementation provides Thread affinity
	//	meaning each thread executes the same AE instance.
	private AnalysisEngineInstancePool aeInstancePool = null;
	
	private String abortedCASReferenceId = null;
	
	
	public PrimitiveAnalysisEngineController_impl(String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize) throws Exception
	{
		this(null, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, 0);
	}

	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize) throws Exception
	{
		this(aParentController, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, 0);
	}

	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize, int aComponentCasPoolSize) throws Exception
	{
		this(aParentController, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, aComponentCasPoolSize, null);
	}

	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize, int aComponentCasPoolSize, long anInitialCasHeapSize) throws Exception
	{
		this(aParentController, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, aComponentCasPoolSize, anInitialCasHeapSize, null);
	}

	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize, int aComponentCasPoolSize, JmxManagement aJmxManagement) throws Exception
	{
		this(aParentController, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, aComponentCasPoolSize,0, aJmxManagement);
	}
	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize, int aComponentCasPoolSize, long anInitialCasHeapSize, JmxManagement aJmxManagement) throws Exception
	{
		super(aParentController, aComponentCasPoolSize, anInitialCasHeapSize, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, null, aJmxManagement);
		analysisEnginePoolSize = anAnalysisEnginePoolSize;
	}
	public PrimitiveAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String anAnalysisEngineDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, int aWorkQueueSize, int anAnalysisEnginePoolSize, JmxManagement aJmxManagement) throws Exception
	{
		this(aParentController, anEndpointName, anAnalysisEngineDescriptor, aCasManager, anInProcessCache, aWorkQueueSize, anAnalysisEnginePoolSize, 0, aJmxManagement);
	}

  
	public int getAEInstanceCount()
	{
		return analysisEnginePoolSize;
	}
	public void initialize() throws AsynchAEException
	{
		try
		{

			if (errorHandlerChain == null)
			{
				super.plugInDefaultErrorHandlerChain();
			}

			if ( aeInstancePool == null )
			{
				aeInstancePool = new AnalysisEngineInstancePoolWithThreadAffinity(analysisEnginePoolSize);
			}
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, getClass().getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_primitive_ctrl_init_info__CONFIG", new Object[] { analysisEnginePoolSize });
			//	Instantiate and initialize UIMA analytics
			for (int i = 0; i < analysisEnginePoolSize; i++)
			{
				AnalysisEngine ae =  UIMAFramework.produceAnalysisEngine(resourceSpecifier, paramsMap);
				aeList.add(ae);
				
				//	Cache metadata once
				if (i == 0)
				{
					analysisEngineMetadata = ae.getAnalysisEngineMetaData();
				}
			}
			if ( serviceInfo == null )
			{
				serviceInfo = new PrimitiveServiceInfo(isCasMultiplier());
			}

			serviceInfo.setAnalysisEngineInstanceCount(analysisEnginePoolSize);
			aeInstancePool.intialize(aeList);

			
			getMonitor().setThresholds(getErrorHandlerChain().getThresholds());
			// Initialize Cas Manager
			if (getCasManagerWrapper() != null)
			{
				try
				{
					if (getCasManagerWrapper().isInitialized())
					{
						getCasManagerWrapper().addMetadata(getAnalysisEngineMetadata());
						if (isTopLevelComponent())
						{
							getCasManagerWrapper().initialize("PrimitiveAEService");
							CAS cas = getCasManagerWrapper().getNewCas("PrimitiveAEService");
							cas.release();
						}
					}

					// All internal components of this Primitive have been initialized. Open the latch
					// so that this service can start processing requests.
					latch.openLatch(getName(), isTopLevelComponent(), true);
					
				}
				catch ( Exception e)
				{
					e.printStackTrace();
					throw new AsynchAEException(e);
				}
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, getClass().getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_manager_wrapper_notdefined__CONFIG", new Object[] {});
			}

		}
		catch ( AsynchAEException e)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
			e.printStackTrace();
			throw e;
		}
		catch ( Exception e)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
			e.printStackTrace();
			throw new AsynchAEException(e);
		}
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_initialized_controller__INFO", new Object[] { getComponentName() });
		super.serviceInitialized = true;
	}

  	/**
  	 * Forces initialization of a Cas Pool if this is a Cas Multiplier delegate collocated with
  	 * an aggregate. The parent aggregate calls this method when all type systems have been 
  	 * merged.
  	 */
    public void onInitialize()
    {
		//	Component's Cas Pool is registered lazily, when the process() is called for
		//	the first time. For monitoring purposes, we need the comoponent's Cas Pool 
		//	MBeans to register during initialization of the service. For a Cas Multiplier 
    	//  force creation of the Cas Pool and registration of a Cas Pool with the JMX Server. 
    	//  Just get the CAS and release it back to the component's Cas Pool.
		if ( isCasMultiplier() && !isTopLevelComponent() )
		{
			System.out.println(Thread.currentThread().getId()+" >>>>>> CAS Multiplier::"+getComponentName()+" Initializing its Cas Pool");
			CAS cas = (CAS)getUimaContext().getEmptyCas(CAS.class);
			cas.release();
		}
    }
	/**
	 * 
	 */
	public void collectionProcessComplete(Endpoint anEndpoint)// throws AsynchAEException
	{
		AnalysisEngine ae = null;
		long start = super.getCpuTime();
		try
		{
			ae = aeInstancePool.checkout();
			if ( ae != null)
			{
				ae.collectionProcessComplete();
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc_all_cases_processed__FINEST", new Object[] { getComponentName() });
			getServicePerformance().incrementAnalysisTime(super.getCpuTime()-start);

			
	    if ( !anEndpoint.isRemote() && System.getProperty("UseVmTransport") != null)
	    {
	        UimaMessage message = 
	          getTransport(anEndpoint.getEndpoint()).produceMessage(AsynchAEMessage.CollectionProcessComplete,AsynchAEMessage.Response,getName());
	        //  Send CPC completion reply back to the client. Use internal (non-jms) transport
	        getTransport(getName()).getUimaMessageDispatcher().dispatch(message);
	    }
	    else
	    {
	      getOutputChannel().sendReply(AsynchAEMessage.CollectionProcessComplete, anEndpoint);
	    }
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc_completed__FINE", new Object[] { getComponentName()});
	
		}
		catch ( Exception e)
		{
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.CollectionProcessComplete);
			errorContext.add(AsynchAEMessage.Endpoint, anEndpoint);
			getErrorHandlerChain().handle(e, errorContext, this);
		}
		finally
		{
			clearStats();
			if ( ae != null )
			{
				try
				{
					aeInstancePool.checkin(ae);
				}
				catch( Exception ex) 
				{
					ex.printStackTrace();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_check_ae_back_to_pool__WARNING", new Object[] { getComponentName(), ex});
				}
			}
		}
	}

	public void addAbortedCasReferenceId( String aCasReferenceId )
	{
		abortedCASReferenceId = aCasReferenceId;
	}
	private boolean abortGeneratingCASes( String aCasReferenceId )
	{
		return ( aCasReferenceId.equals(abortedCASReferenceId));
	}
	
	public void process(CAS aCAS, String aCasReferenceId, Endpoint anEndpoint)// throws AnalysisEngineProcessException, AsynchAEException
	{
		
		if ( stopped )
		{
			return;
		}
		
		boolean inputCASReturned = false;
		boolean processingFailed = false;
		// This is a primitive controller. No more processing is to be done on the Cas. Mark the destination as final and return CAS in reply.
		anEndpoint.setFinal(true);
		AnalysisEngine ae = null;
		try
		{
			// Checkout an instance of AE from the pool
			ae = aeInstancePool.checkout();
			
			if ( ae == null )
			{
				//	This block just logs the CAS that cannot be processed 
				try
				{
				  XmiSerializationSharedData serSharedData = new XmiSerializationSharedData();
				  String  serializedCas = UimaSerializer.serializeCasToXmi(aCAS, serSharedData);
				  UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dump_cas__FINEST", new Object[] { serializedCas });
		        }
				catch( Exception e )
				{
					//	ignore exceptions. This block just logs the CAS
				}
				  UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dropping_cas_dueto_no_ae__WARNING", new Object[] { getComponentName(), aCasReferenceId});
				
				return;
			}
			//	Get input CAS entry from the InProcess cache
			CacheEntry inputCASEntry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);

			long time = super.getCpuTime();
			long totalProcessTime = 0;  // stored total time spent producing ALL CASes
			
			CasIterator casIterator = ae.processAndOutputNewCASes(aCAS);
			
			//	Store how long it took to call processAndOutputNewCASes()
			totalProcessTime = ( super.getCpuTime() - time);
			long sequence = 1;
			long hasNextTime = 0;         // stores time in hasNext()
			long getNextTime = 0;         // stores time in next();   
			boolean moreCASesToProcess = true;
			while (moreCASesToProcess)
			{
				long timeToProcessCAS = 0;    // stores time in hasNext() and next() for each CAS
				hasNextTime = super.getCpuTime();
				if ( !casIterator.hasNext() )
				{
					moreCASesToProcess = false;
					//	Measure how long it took to call hasNext()
					timeToProcessCAS = (super.getCpuTime()-hasNextTime);
					totalProcessTime += timeToProcessCAS;
					break;   // from while
				}
				//	Measure how long it took to call hasNext()
				timeToProcessCAS = (super.getCpuTime()-hasNextTime);
				getNextTime = super.getCpuTime();
				CAS casProduced = casIterator.next();
				//	Add how long it took to call next()
				timeToProcessCAS += (super.getCpuTime()- getNextTime);
                //	Add time to call hasNext() and next() to the running total
				totalProcessTime += timeToProcessCAS;
				
				//	If the service is stopped or aborted, stop generating new CASes and just return the input CAS
				if ( stopped || abortGeneratingCASes(aCasReferenceId))
				{
					if ( getInProcessCache() != null && getInProcessCache().getSize() > 0 && getInProcessCache().entryExists(aCasReferenceId))
					{
						try
						{
							//	Set a flag on the input CAS to indicate that the processing was aborted
							getInProcessCache().getCacheEntryForCAS(aCasReferenceId).setAborted(true);
						}
						catch( Exception e )
						{
							//	An exception be be thrown here if the service is being stopped.
							//	The top level controller may have already cleaned up the cache
							//	and the getCacheEntryForCAS() will throw an exception. Ignore it
							//	here, we are shutting down.
						}
						finally
						{
							//	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							//	We are terminating the iterator here, release the internal CAS lock
							//	so that we can release the CAS. This approach may need to be changed
							//	as there may potentially be a problem with a Class Loader.
							//	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							((CASImpl)aCAS).enableReset(true);
						}
					}
					return;
				}
				OutOfTypeSystemData otsd = getInProcessCache().getOutOfTypeSystemData(aCasReferenceId);
				MessageContext mContext = getInProcessCache().getMessageAccessorByReference(aCasReferenceId);
				CacheEntry newEntry = getInProcessCache().register( casProduced, mContext, otsd);
				//	Associate input CAS with the new CAS
				newEntry.setInputCasReferenceId(aCasReferenceId);
				newEntry.setCasSequence(sequence);
				//	Add to the cache how long it took to process the generated (subordinate) CAS
				getCasStatistics(newEntry.getCasReferenceId()).incrementAnalysisTime(timeToProcessCAS);
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_produced_new_cas__FINE", new Object[] { Thread.currentThread().getName(),getComponentName(),newEntry.getCasReferenceId(), aCasReferenceId });
				//	Add the generated CAS to the outstanding CAS Map. Client notification will release
				//	this CAS back to its pool
				synchronized(syncObject)
				{
          inputCASEntry.incrementSubordinateCasInPlayCount();
					if ( isTopLevelComponent() )
					{
//						inputCASEntry.incrementSubordinateCasInPlayCount();
						//	Add the id of the generated CAS to the map holding outstanding CASes. This
						//	map will be referenced when a client sends Free CAS Notification. The map
						//	stores the id of the CAS both as a key and a value. Map is used to facilitate
						//	quick lookup
						cmOutstandingCASes.put(newEntry.getCasReferenceId(),newEntry.getCasReferenceId());
					}
	        //  Increment number of CASes processed by this service
	        sequence++;

		      if ( !anEndpoint.isRemote() && System.getProperty("UseVmTransport") != null)
		      {
		          UimaMessage message = 
		            getTransport(anEndpoint.getEndpoint()).produceMessage(AsynchAEMessage.Process,AsynchAEMessage.Request,getName());
              message.addStringProperty(AsynchAEMessage.CasReference, newEntry.getCasReferenceId());
              message.addStringProperty(AsynchAEMessage.InputCasReference, aCasReferenceId);
              message.addLongProperty(AsynchAEMessage.CasSequence, sequence);
              ServicePerformance casStats =
                getCasStatistics(aCasReferenceId);
              
              message.addLongProperty(AsynchAEMessage.TimeToSerializeCAS, casStats.getRawCasSerializationTime());
              message.addLongProperty(AsynchAEMessage.TimeToDeserializeCAS, casStats.getRawCasDeserializationTime());
              message.addLongProperty(AsynchAEMessage.TimeInProcessCAS, casStats.getRawAnalysisTime());
              long iT = getIdleTimeBetweenProcessCalls(AsynchAEMessage.Process); 
              message.addLongProperty(AsynchAEMessage.IdleTime, iT );
              
		          getTransport(getName()).getUimaMessageDispatcher().dispatch(message);
		      }
		      else
		      {
	          //  Send generated CAS to the client
	          getOutputChannel().sendReply(newEntry, anEndpoint);
		      }
				}
				//	Remove Stats from the global Map associated with the new CAS
				//	These stats for this CAS were added to the response message
				//	and are no longer needed
				dropCasStatistics(newEntry.getCasReferenceId());
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_completed_analysis__FINEST", new Object[] { Thread.currentThread().getName(), getComponentName(), aCasReferenceId, (double) (super.getCpuTime() - time) / (double) 1000000 });
			getMonitor().resetCountingStatistic("", Monitor.ProcessErrorCount);
			
			// Store total time spent processing this input CAS
			getCasStatistics(aCasReferenceId).incrementAnalysisTime(totalProcessTime);
			synchronized( cmOutstandingCASes )
			{
				if ( cmOutstandingCASes.size() == 0)
				{
					inputCASReturned = true;
					
          if ( !anEndpoint.isRemote() && System.getProperty("UseVmTransport") != null)
          {
              UimaMessage message = 
                getTransport(anEndpoint.getEndpoint()).produceMessage(AsynchAEMessage.Process,AsynchAEMessage.Response,getName());
              message.addStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
              ServicePerformance casStats =
                getCasStatistics(aCasReferenceId);
              
              message.addLongProperty(AsynchAEMessage.TimeToSerializeCAS, casStats.getRawCasSerializationTime());
              message.addLongProperty(AsynchAEMessage.TimeToDeserializeCAS, casStats.getRawCasDeserializationTime());
              message.addLongProperty(AsynchAEMessage.TimeInProcessCAS, casStats.getRawAnalysisTime());
              long iT = getIdleTimeBetweenProcessCalls(AsynchAEMessage.Process); 
              message.addLongProperty(AsynchAEMessage.IdleTime, iT );
              //  Send reply back to the client. Use internal (non-jms) transport
              getTransport(getName()).getUimaMessageDispatcher().dispatch(message);
          }
          else
          {
            //	Return an input CAS to the client if there are no outstanding child CASes in play
            getOutputChannel().sendReply(aCasReferenceId, anEndpoint);
          }
				}
				else
				{
					//	Change the state of the input CAS. Since the input CAS is not returned to the client
					//	until all children of this CAS has been fully processed we keep the input in the cache.
					//	The client will send Free CAS Notifications to release CASes produced here. When the
					//	last child CAS is freed, the input CAS is allowed to be returned to the client.
					inputCASEntry.setPendingReply(true);
				}
			}
		}
		catch ( Throwable e)
		{
			processingFailed = true;
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.CasReference, aCasReferenceId);
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
			errorContext.add(AsynchAEMessage.MessageType, AsynchAEMessage.Request);
			errorContext.add(AsynchAEMessage.Endpoint, anEndpoint);
			// Handle the exception. Pass reference to the PrimitiveController instance
			getErrorHandlerChain().handle(e, errorContext, this);
		}
		finally
		{
			dropCasStatistics(aCasReferenceId);

			if ( ae != null )
			{
				try
				{
					aeInstancePool.checkin(ae);
				}
				catch( Exception e)
				{
					e.printStackTrace();
				}
			}
			//	drop the CAS if it has been successfully processed. If there was a failure, the Error Handler
			//	will drop the CAS
			if ( isTopLevelComponent() && !processingFailed) 
			{	
				//	Release CASes produced from the input CAS if the input CAS has been aborted
				if ( abortGeneratingCASes(aCasReferenceId) )
				{
					
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
			                "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_remove_cache_entry__INFO",
			                new Object[] { getComponentName(), aCasReferenceId });

					getInProcessCache().releaseCASesProducedFromInputCAS(aCasReferenceId);
				}
				else if ( inputCASReturned )
				{
					//	Remove input CAS cache entry if the CAS has been sent to the client
					dropCAS(aCasReferenceId, true);
				}
			}
		}
	}
	private void addConfigIntParameter( String aParamName, int aParamValue)
	{
		ConfigurationParameter cp = new ConfigurationParameter_impl();
		cp.setMandatory(false);
		cp.setMultiValued(false);
		cp.setName(aParamName);
		cp.setType("Integer");
		getAnalysisEngineMetadata().getConfigurationParameterDeclarations().addConfigurationParameter(cp);
		getAnalysisEngineMetadata().getConfigurationParameterSettings().setParameterValue(aParamName, aParamValue);
		
	}
	// Return metadata
	public void sendMetadata(Endpoint anEndpoint) throws AsynchAEException
	{
		addConfigIntParameter(AnalysisEngineController.AEInstanceCount, analysisEnginePoolSize);

		if ( getAnalysisEngineMetadata().getOperationalProperties().getOutputsNewCASes() )
		{
			addConfigIntParameter(AnalysisEngineController.CasPoolSize, super.componentCasPoolSize);
		}
		super.sendMetadata(anEndpoint, getAnalysisEngineMetadata());
	}

	private AnalysisEngineMetaData getAnalysisEngineMetadata()
	{
		return analysisEngineMetadata;
	}

	/**
	 * Executes action on error. Primitive Controller allows two types of actions TERMINATE and DROPCAS.
	 */
	public void takeAction(String anAction, String anEndpointName, ErrorContext anErrorContext)
	{
		try
		{
			if (ErrorHandler.TERMINATE.equalsIgnoreCase(anAction) || ErrorHandler.DROPCAS.equalsIgnoreCase(anAction))
			{
				super.handleAction(anAction, anEndpointName, anErrorContext);
			}
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "takeAction", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
		}
	}

	public String getServiceEndpointName()
	{
		return getName();
	}

	public synchronized ControllerLatch getControllerLatch()
	{
		return latch;
	}

	public boolean isPrimitive()
	{
		return true;
	}

	public Monitor getMonitor()
	{
		return super.monitor;
	}

	public void setMonitor(Monitor monitor)
	{
		this.monitor = monitor;
	}

	public void handleDelegateLifeCycleEvent( String anEndpoint, int aDelegateCount)
	{
		if ( aDelegateCount == 0 )
		{
			// tbi
		}
	}
	protected String getNameFromMetadata()
	{
		return super.getMetaData().getName();
	}
	
	public void setAnalysisEngineInstancePool( AnalysisEngineInstancePool aPool)
	{
		aeInstancePool = aPool;
	}
	
	public PrimitiveServiceInfo getServiceInfo()
	{ 
		if ( serviceInfo == null )
		{
			serviceInfo = new PrimitiveServiceInfo();
		}
		if ( getInputChannel() != null )
		{
			serviceInfo.setInputQueueName(getInputChannel().getServiceInfo().getInputQueueName());
			serviceInfo.setBrokerURL(getInputChannel().getServiceInfo().getBrokerURL());
		}
		
		serviceInfo.setState("Running");
		if ( isCasMultiplier() )
		{
			serviceInfo.setCASMultiplier();
		}
		return serviceInfo;
	}
	
	public void stop()
	{
		super.stop();
		stopInputChannel();
		if ( aeInstancePool != null )
		{
			try
			{
				aeInstancePool.destroy();
			}
			catch( Exception e){ e.printStackTrace();}
		}
		if ( cmOutstandingCASes != null )
		{
			cmOutstandingCASes.clear();
		}
		if ( aeList != null )
		{
			aeList.clear();
			aeList = null;
		}
	}
	
}
