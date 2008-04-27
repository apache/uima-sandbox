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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

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
import org.apache.uima.aae.jmx.JmxManager;
import org.apache.uima.aae.jmx.PrimitiveServiceInfo;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.statistics.ComponentStatistics;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.Statistic;
import org.apache.uima.aae.monitor.statistics.Statistics;
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

	private AnalysisEngineMetaData analysisEngineMetadata;

	private ControllerLatch latch = new ControllerLatch();

	private int analysisEnginePoolSize;

	protected Object notifyObj = new Object();

	private List aeList = new ArrayList();
	
	private List cmOutstandingCASes = new ArrayList();
	
	private int throttleWindow = 0;
	
	private Object gater = new Object();
	
	private long howManyBeforeReplySeen = 0;
	
	private boolean isMultiplier = false;
	
	private Object syncObject = new Object();
	
	private PrimitiveServiceInfo serviceInfo = null;

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

			for (int i = 0; i < analysisEnginePoolSize; i++)
			{
				AnalysisEngine ae =  UIMAFramework.produceAnalysisEngine(resourceSpecifier, paramsMap);
				aeList.add(ae);
				
				//	Cache metadata once
				if (i == 0)
				{
					analysisEngineMetadata = ae.getAnalysisEngineMetaData();
					if ( analysisEngineMetadata.getOperationalProperties().getOutputsNewCASes())
					{
						isMultiplier = true;
					}
				}
			}
			
			if ( serviceInfo == null )
			{
				serviceInfo = new PrimitiveServiceInfo();
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
			throw e;
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "initialize", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_initialized_controller__INFO", new Object[] { getComponentName() });
		super.serviceInitialized = true;
	}
	public boolean isMultiplier()
	{
		return isMultiplier;
	}

	/**
	 * 
	 */
	public void collectionProcessComplete(Endpoint anEndpoint)// throws AsynchAEException
	{
		AnalysisEngine ae = null;
		
		try
		{
			ae = aeInstancePool.checkout();
			if ( ae != null)
			{
				ae.collectionProcessComplete();
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc_all_cases_processed__FINEST", new Object[] { getComponentName() });
			getOutputChannel().sendReply(AsynchAEMessage.CollectionProcessComplete, anEndpoint);
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

	private void rejectAndReturnInputCAS(String aCasReferenceId, Endpoint anEndpoint)
	{
		if ( getInProcessCache() != null )
		{
			try
			{
				//	Set a flag on the input CAS to indicate that the processing was aborted
				getInProcessCache().getCacheEntryForCAS(aCasReferenceId).setAborted(true);
				getOutputChannel().sendReply(aCasReferenceId, anEndpoint);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "rejectAndReturnInputCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
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
/*		
		try
		{
		  //  Test to see if the connection to the reply endpoint can be created 
		  //  If the client has died, dont waste time analyzing the CAS.
		  getOutputChannel().bindWithClientEndpoint(anEndpoint);
		}
		catch( Exception e)
		{
		  if ( isTopLevelComponent() )
		  {
		    
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_client_drop_cas__INFO", new Object[] { getComponentName(), aCasReferenceId, anEndpoint.getEndpoint()});
		    dropCAS(aCasReferenceId, true);
		  }
		  return;
		}
*/	
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
			long time = System.nanoTime();
			long totalProcessTime = 0;  // stored total time spent producing ALL CASes
			CasIterator casIterator = ae.processAndOutputNewCASes(aCAS);
			//	Store how long it took to call processAndOutputNewCASes()
			totalProcessTime = ( System.nanoTime() - time);
			long sequence = 1;
			String newCasReferenceId = null;
			long hasNextTime = 0;         // stores time in hasNext()
			long getNextTime = 0;         // stores time in next();   
			long timeToProcessCAS = 0;    // stores time in hasNext() and next() for each CAS
			boolean moreCASesToProcess = true;
			
			
			String parentCasReferenceId = null;
			CacheEntry inputCasCacheEntry = null;
			try
			{
				//	Fetch cache entry for the input CAS
				inputCasCacheEntry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				parentCasReferenceId = inputCasCacheEntry.getInputCasReferenceId();
			}
			catch( Exception e )
			{
				//	An exception be be thrown here if the service is being stopped.
				//	The top level controller may have already cleaned up the cache
				//	and the getCacheEntryForCAS() will throw an exception. Ignore it
				//	here, we are shutting down.
			}

			while (moreCASesToProcess)
			{
				hasNextTime = System.nanoTime();
				if ( !casIterator.hasNext() )
				{
					moreCASesToProcess = false;
					//	Measure how long it took to call hasNext()
					timeToProcessCAS = (System.nanoTime()-hasNextTime);
					totalProcessTime += timeToProcessCAS;
					break;
				}
				//	Measure how long it took to call hasNext()
				timeToProcessCAS = (System.nanoTime()-hasNextTime);
				getNextTime = System.nanoTime();
				CAS casProduced = casIterator.next();
				//	Add how long it took to call next()
				timeToProcessCAS += (System.nanoTime()- getNextTime);
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
				sequence++;
				newCasReferenceId = getInProcessCache().register( casProduced, mContext, otsd);
				CacheEntry newEntry = getInProcessCache().getCacheEntryForCAS(newCasReferenceId);
				if ( parentCasReferenceId == null )
				{
					newEntry.setInputCasReferenceId(aCasReferenceId);
				}
				else
				{
					newEntry.setInputCasReferenceId(parentCasReferenceId);
				}
				//	Add to the cache how long it took to process the generated (subordinate) CAS
				getCasStatistics(newCasReferenceId).incrementAnalysisTime(timeToProcessCAS);
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_produced_new_cas__FINE", new Object[] { Thread.currentThread().getName(),getComponentName(),newCasReferenceId, aCasReferenceId });
				synchronized(syncObject)
				{
					cmOutstandingCASes.add(newCasReferenceId);
					getOutputChannel().sendReply(casProduced, aCasReferenceId, newCasReferenceId, anEndpoint, sequence);
				}
				//	Remove Stats from the global Map associated with the new CAS
				//	This stats for this CAS were added to the response message
				//	and are no longer needed
				dropCasStatistics(newCasReferenceId);
				//	Increment number of CASes processed by this service
				getServicePerformance().incrementNumberOfCASesProcessed();
				getServicePerformance().incrementAnalysisTime(timeToProcessCAS);
			}

			LongNumericStatistic statistic = null;
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalAEProcessTime)) != null )
			{
				//	Increment how long it took to process the input CAS. This timer is exposed via JMX
				statistic.increment(totalProcessTime);
			}
			
			if (newCasReferenceId != null)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_completed_analysis__FINEST", new Object[] { Thread.currentThread().getName(), getComponentName(), newCasReferenceId, (double) (System.nanoTime() - time) / (double) 1000000 });
			}
			else
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, getClass().getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_completed_analysis__FINEST", new Object[] { Thread.currentThread().getName(), getComponentName(), aCasReferenceId, (double) (System.nanoTime() - time) / (double) 1000000 });

			}
			getMonitor().resetCountingStatistic("", Monitor.ProcessErrorCount);
			getCasStatistics(aCasReferenceId).incrementAnalysisTime(totalProcessTime);
			//	Aggregate total time spent processing the input CAS
			getServicePerformance().incrementAnalysisTime(totalProcessTime);
			getOutputChannel().sendReply(aCasReferenceId, anEndpoint);
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
				else
				{
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
		getOutputChannel().sendReply(getAnalysisEngineMetadata(), anEndpoint, true);
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

	public ControllerLatch getControllerLatch()
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

	public int getThrottleWindowSize()
	{
		return throttleWindow;
	}
	

	public void throttleNext()
	{
		if (throttleWindow > 0)
		{
			synchronized (gater)
			{
				howManyBeforeReplySeen--;
				gater.notifyAll();
			}
		}
	}
	

	
	
	protected String getNameFromMetadata()
	{
		return super.getMetaData().getName();
	}
	public void releaseNextCas()
	{
		synchronized(syncObject)
		{
			if ( cmOutstandingCASes.size() > 0 )
			{
				try
				{
					String casReferenceId = (String)cmOutstandingCASes.remove(0);
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                "releaseNextCas", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_release_cas_req__FINE",
			                new Object[] { getName(), casReferenceId });
					if ( casReferenceId != null && getInProcessCache().entryExists(casReferenceId))
					{
						dropCAS(casReferenceId, true);
					}
				}
				catch( Exception e)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
			                "releaseNextCas", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
			                new Object[] { e});
				}
			}
		}
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
			cmOutstandingCASes = null;
		}
		if ( aeList != null )
		{
			aeList.clear();
			aeList = null;
		}
	}
}
