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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.AsynchAECasManager;
import org.apache.uima.aae.InProcessCache;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaClassFactory;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.error.UnknownDestinationException;
import org.apache.uima.aae.jmx.AggregateServiceInfo;
import org.apache.uima.aae.jmx.JmxManagement;
import org.apache.uima.aae.jmx.PrimitiveServiceInfo;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.Statistic;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.asb.impl.FlowContainer;
import org.apache.uima.analysis_engine.asb.impl.FlowControllerContainer;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.flow.ParallelStep;
import org.apache.uima.flow.SimpleStep;
import org.apache.uima.flow.Step;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;

public class AggregateAnalysisEngineController_impl 
extends BaseAnalysisEngineController 
implements AggregateAnalysisEngineController, AggregateAnalysisEngineController_implMBean
{
	private static final long serialVersionUID = 2214140414061082168L;

	private static final Class CLASS_NAME = AggregateAnalysisEngineController_impl.class;

	private static final int SERVICE_INFO_INDX = 0;

    private static final int SERVICE_PERF_INDX = 1;

    private static final int SERVICE_ERROR_INDX = 2;

	private transient ControllerLatch latch = new ControllerLatch();

	private Map flowMap = new HashMap();

	protected ConcurrentHashMap destinationMap;

	private Map destinationToKeyMap;

	private boolean typeSystemsMerged = false;

	private AnalysisEngineMetaData aggregateMetadata;

	private HashMap analysisEngineMetaDataMap = new HashMap();

	private List disabledDelegateList = new ArrayList();

	private List remoteCasMultiplierList = new ArrayList();
	
	private String descriptor;

	private transient FlowControllerContainer flowControllerContainer;

	private String flowControllerDescriptor;

	private Map originMap = new HashMap();
	
	private boolean aborting = false;
	
	private String controllerBeanName = null;

	private String serviceEndpointName = null;

	private boolean initialized = false;
 
	private int cmCasPoolSizeDelta = 0;
	
	private int counter = 0;
	
	private Object counterMonitor = new Object();
	
	protected List childControllerList = new ArrayList();
	
	protected Map delegateStats = new HashMap();
	
	private AggregateServiceInfo serviceInfo = null;

	private int remoteIndex = 1;

	private boolean requestForMetaSentToRemotes = false;


	private ConcurrentHashMap<String, Object[]> delegateStatMap = 
		new ConcurrentHashMap();
	/**
	 * 
	 * @param anEndpointName
	 * @param aDescriptor
	 * @param aCasManager
	 * @param anInProcessCache
	 * @param aDestinationMap
	 * @throws Exception
	 */
	public AggregateAnalysisEngineController_impl(String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap) throws Exception
	{
		this(null, anEndpointName, aDescriptor, aCasManager, anInProcessCache, aDestinationMap);
	}

	/**
	 * 
	 * @param aParentController
	 * @param anEndpointName
	 * @param aDescriptor
	 * @param aCasManager
	 * @param anInProcessCache
	 * @param aDestinationMap
	 * @throws Exception
	 */
	public AggregateAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap) throws Exception
	{
		this(aParentController, anEndpointName, aDescriptor, aCasManager, anInProcessCache, aDestinationMap, null);
	}

	public AggregateAnalysisEngineController_impl(AnalysisEngineController aParentController, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap, JmxManagement aJmxManagement) throws Exception
	{
		super(aParentController, 0, anEndpointName, aDescriptor, aCasManager, anInProcessCache, aDestinationMap, aJmxManagement);
		this.initialize();
	}

	public void registerChildController( AnalysisEngineController aChildController, String aDelegateKey) throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "registerChildController", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_register_controller__FINE",
                new Object[] {getComponentName(), aChildController.getComponentName()});
		childControllerList.add(aChildController);
	}
	
	public void saveStatsFromService( String aServiceEndpointName, Map aServiceStats )
	{
		String delegateKey = lookUpDelegateKey(aServiceEndpointName);
		delegateStats.put(delegateKey, aServiceStats);
	}

	/**
	 * 
	 */
	public void addMessageOrigin(String aCasReferenceId, Endpoint anEndpoint)
	{
		originMap.put(aCasReferenceId, anEndpoint);
	}


	public boolean isDelegateDisabled( String aDelegateKey )
	{
		if ( aDelegateKey == null )
		{
			return false;
		}
		Iterator it = disabledDelegateList.iterator();
		while( it.hasNext() )
		{
			if ( aDelegateKey.equalsIgnoreCase((String)it.next()) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param anEndpointName
	 */
	public void setServiceEndpointName(String anEndpointName)
	{
		serviceEndpointName = anEndpointName;
		if (this.isTopLevelComponent())
		{
			//	This is done so that the collocated client application can determine where to send messages
			System.setProperty("ServiceInputQueueName", serviceEndpointName);
		}
	}

	/**
	 * 
	 */
	public String getServiceEndpointName()
	{
		return serviceEndpointName;
	}

	/**
	 * 
	 * @param aBeanName
	 */
	public void setControllerBeanName(String aBeanName)
	{
		controllerBeanName = aBeanName;
		if (this.isTopLevelComponent())
		{
			System.setProperty("Controller", controllerBeanName);
		}
	}

	/**
	 * 
	 */
	public Endpoint getMessageOrigin(String aCasReferenceId)
	{
		if (originMap.containsKey(aCasReferenceId))
		{
			return (Endpoint) originMap.get(aCasReferenceId);
		}
		return null;
	}
	
	public void removeMessageOrigin( String aCasReferenceId )
	{
		if (originMap.containsKey(aCasReferenceId))
		{
			originMap.remove(aCasReferenceId);
		}
	}
	/**
	 * 
	 */
	public void dropCAS(String aCasReferenceId, boolean dropCacheEntry)
	{
		
		
		FlowContainer flow = lookupFlow(aCasReferenceId);
			if ( flow != null )
			{
				flow.aborted();
				synchronized( flowMap )
				{
					flowMap.remove(aCasReferenceId);
				}
			}
		super.dropCAS(aCasReferenceId, dropCacheEntry);
	}
	
	public void dropFlow( String aCasReferenceId, boolean abortFlow)
	{
		FlowContainer flow = lookupFlow(aCasReferenceId);
		if ( flow != null )
		{
			if ( abortFlow )
			{
				flow.aborted();
			}
			
			synchronized( flowMap )
			{
				flowMap.remove(aCasReferenceId);
			}
		}
		
	}
	
	
	/**
	 * 
	 */
	public void mapEndpointsToKeys(ConcurrentHashMap aDestinationMap)
	{
		destinationMap = aDestinationMap;
		Set set = destinationMap.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			Endpoint endpoint = (Endpoint)entry.getValue();
			if (endpoint != null )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "mapEndpointsToKeys", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_endpoint_to_key_map__FINE",
		                new Object[] {getName(), (String)entry.getKey(),  endpoint.getEndpoint()  });
				if (destinationToKeyMap == null)
				{
					destinationToKeyMap = new HashMap();
				}
				destinationToKeyMap.put(endpoint.getEndpoint(), (String)entry.getKey());
			}
		}

	}
	/**
	 * 
	 * @return
	 */
	private boolean allDelegatesCompletedCollection()
	{
		Set set = destinationMap.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			Endpoint endpoint = (Endpoint)entry.getValue();
			if (endpoint != null && endpoint.completedProcessingCollection() == false)
			{
				return false;
			}
		}

		return true;
	}
	public Map getDelegateStats()
	{
		return delegateStats;
	}
	/**
	 * 
	 */
	public void processCollectionCompleteReplyFromDelegate(String aDelegateKey, boolean sendReply) throws AsynchAEException
	{
		
		try
		{
			Endpoint endpoint = (Endpoint) destinationMap.get(aDelegateKey);
			String key = lookUpDelegateKey( aDelegateKey);
			if ( endpoint == null )
			{
				
				endpoint = (Endpoint) destinationMap.get(key);
				if ( endpoint == null )
				{
					throw new AsynchAEException("Unable to find Endpoint Object Using:"+aDelegateKey);
				}
			}
			endpoint.cancelTimer();
			endpoint.setCompletedProcessingCollection(true);

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "processCollectionCompleteReplyFromDelegate", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_recvd_cpc_reply__FINE", new Object[] { key });
			
			
			if (sendReply && allDelegatesCompletedCollection() && getClientEndpoint() != null)
			{
				
				sendCpcReply();
			}
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	private void sendCpcReply() throws Exception
	{
		Iterator destIterator = destinationMap.keySet().iterator();
		while(destIterator.hasNext())
		{
			
			String key = (String)destIterator.next();
			Endpoint endpoint = (Endpoint) destinationMap.get(key);
			if ( endpoint != null )
			{
				endpoint.setCompletedProcessingCollection(false);  // reset for the next run
			}
			logStats( key,getDelegateServicePerformance(key) );
			
		}
		//	Log this controller's stats
		logStats(getComponentName(),servicePerformance);
		getOutputChannel().sendReply(AsynchAEMessage.CollectionProcessComplete, getClientEndpoint());
		clientEndpoint = null;
		clearStats();
	}
	/**
	 * 
	 * @param aFlowControllerDescriptor
	 */
	public void setFlowControllerDescriptor(String aFlowControllerDescriptor)
	{
		flowControllerDescriptor = aFlowControllerDescriptor;
	}
    /**
     * 
     * @param anEndpoint
     * @throws AsynchAEException
     */
	private void waitUntilAllCasesAreProcessed(Endpoint anEndpoint) throws AsynchAEException
	{
		try
		{
			boolean cacheNotEmpty = true;
			boolean shownOnce = false;
			while (cacheNotEmpty)
			{
				InProcessCache cache = getInProcessCache();
				if (!shownOnce)
				{
					shownOnce = true;
					cache.dumpContents();
				}

				if (cache.isEmpty())
				{
					cacheNotEmpty = false;
				}
				else
				{
					synchronized (cache)
					{
						cache.wait(10);
					}
				}
			}
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	/**
	 * 
	 */
	public void takeAction(String anAction, String anEndpointName, ErrorContext anErrorContext)
	{
		try
		{
			handleAction(anAction, anEndpointName, anErrorContext);
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
	}

	public void collectionProcessComplete(Endpoint anEndpoint) throws AsynchAEException
	{
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
				"collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc__FINEST", new Object[] { getName() });
		getInProcessCache().dumpContents();

		cacheClientEndpoint(anEndpoint);

		// Wait until the entire cache is empty. The cache stores in-process CASes.
		// When a CAS is processed completly it is removed from the cache.
		waitUntilAllCasesAreProcessed(anEndpoint);

		anEndpoint.setCommand( AsynchAEMessage.CollectionProcessComplete);

		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
				"collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cpc_all_cases_processed__FINEST", new Object[] { getName() });

		//	Special case. Check if ALL delegates have been disabled. If so, destinationMap
		//	will be empty.
		if (destinationMap.size() == 0)
		{
			try
			{
				sendCpcReply();
			}
			catch(Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "collectionProcessComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
			}
		}
		else
		{
			Set set = destinationMap.entrySet();
			for( Iterator it = set.iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry)it.next();
				Endpoint endpoint = (Endpoint)entry.getValue();
				if (endpoint != null )
				{
					getOutputChannel().sendRequest(AsynchAEMessage.CollectionProcessComplete, endpoint);
					endpoint.startCollectionProcessCompleteTimer();
				}
			}
		}
	}

	public String getDescriptor()
	{
		return descriptor;
	}

	public void setDescriptor(String descriptor)
	{
		this.descriptor = descriptor;
	}

	public boolean isPrimitive()
	{
		return false;
	}

	public Map getDestinations()
	{
		return destinationMap;
	}

	public void enableDelegates(List aDelegateList) throws AsynchAEException
	{
		try
		{
			// flowControllerContainer.addAnalysisEngines(aDelegateList);
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}

	}

	public void disableDelegates(List aDelegateList) throws AsynchAEException
	{
		try
		{
			Iterator it = aDelegateList.iterator();
			while (it.hasNext())
			{
				String key = (String) it.next();
				System.out.println("Controller:"+getName()+ " Disabling Delegate:"+key+" Due to Excessive Errors");
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "disableDelegates", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_removing_endpoint_from_map__INFO", new Object[] { getName(), key });
				//	Change state of the delegate
				ServiceInfo sf = getDelegateServiceInfo( key );
				if ( sf != null )
				{
					sf.setState("Disabled");
				}
				Endpoint endpoint = lookUpEndpoint(key, false);
				destinationMap.remove(key);
				disabledDelegateList.add(key);
				if ( endpoint != null && 
				     endpoint.isRemote() && 
				     getUimaEEAdminContext() != null && 
				     endpoint.getReplyToEndpoint() != null &&
				     endpoint.getReplyToEndpoint().trim().length() > 0)
				{
					System.out.println("Controller:"+getComponentName()+"-Stopping Listener Thread on Endpoint:"+endpoint.getReplyToEndpoint());					
					//	Stop the Listener on endpoint that has been disabled
					getUimaEEAdminContext().stopListener(endpoint.getReplyToEndpoint());
				}
			}
			if (flowControllerContainer != null)
			{
				try
				{
					flowControllerContainer.removeAnalysisEngines(aDelegateList);
				}
				catch( Exception ex)
				{
					ex.printStackTrace();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "disableDelegates", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { ex });
					handleAction(ErrorHandler.TERMINATE, null, null);
					return;
				}
			}
			if (!initialized && allTypeSystemsMerged() )
			{
				completeInitialization();
			}
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			throw new AsynchAEException(e);
		}

	}

	public boolean continueOnError(String aCasReferenceId, String aDelegateKey, Exception anException) throws AsynchAEException
	{
		if (aDelegateKey == null)
		{
			return false;
		}
		try
		{
			FlowContainer flow = lookupFlow(aCasReferenceId);
			if (flow == null)
			{
				return false;
			}
			return flow.continueOnFailure(aDelegateKey, anException);
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	private FlowContainer lookupFlow(String aCasReferenceId)
	{
		if (flowMap.containsKey(aCasReferenceId))
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "lookupFlow", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retrieve_flow_object__FINEST", new Object[] { aCasReferenceId });

			return (FlowContainer) flowMap.get(aCasReferenceId);
		}
		return null;
	}
	public String getLastDelegateKeyFromFlow(String anInputCasReferenceId)
	{
		return null;
	}
	/**
	 * This routine is called to handle CASes produced by a CAS Multiplier. A new CAS needs a flow object which is produced here from the 
	 * Flow associated with the input CAS. Once the subflow is computed, it is cached for future use.
	 * 
	 * @param aCAS - CAS to process
	 * @param anInputCasReferenceId - reference id of the input CAS
	 * @param aNewCasReferenceId - reference id of the CAS created by the CAS multiplier
	 * @param newCASProducedBy - name of the multiplier that created the CAS
	 * @throws AnalysisEngineProcessException
	 * @throws AsynchAEException
	 */
	public void process(CAS aCAS, String anInputCasReferenceId, String aNewCasReferenceId, String newCASProducedBy) //throws AnalysisEngineProcessException, AsynchAEException
	{
		FlowContainer flow = null;

		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_lookup_flow__FINE",
	                new Object[] {getName(), anInputCasReferenceId });
			
			try
			{
				//	Lookup a Flow object associated with an input CAS.  
				if (flowMap.containsKey(anInputCasReferenceId))
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retrieve_flow_object__FINEST", new Object[] { anInputCasReferenceId });
					// Retrieve an input CAS Flow object from the flow cache. This Flow object will be used to compute
					// subordinate Flow for the new CAS.
					flow = (FlowContainer) flowMap.get(anInputCasReferenceId);
				}

				if (flow != null)
				{

					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_lookup_flow_ok__FINE",
			                new Object[] {getName(), aNewCasReferenceId,  newCASProducedBy, anInputCasReferenceId, });
					
					// Compute subordinate Flow from the Flow associated with the
					// input CAS.
					flow = flow.newCasProduced(aCAS, newCASProducedBy);

					// Save the subordinate Flow Object in a cache. Flow exists in the
					// cache until the CAS is fully processed or it is
					// explicitely deleted when processing of this CAS cannot continue
					synchronized( flowMap )
					{
						flowMap.put(aNewCasReferenceId, flow);
					}
					// Register the fact that this is a new CAS and the fact that is was produced
					// by this aggregate. It is important to register this to determine how to
					// handle the CAS in delegate Aggregate services. When the CAS is processed
					// in the Delegate Aggregate, the CAS produced in the parent Aggregate cannot
					// be dropped in the delegate. Check Final Step logic.
					getInProcessCache().getCacheEntryForCAS(aNewCasReferenceId).setNewCas(true, getName());
				}
				else
				{
					throw new AsynchAEException("Flow Object Not In Flow Cache. Expected Flow Object in FlowCache for Cas Reference Id:" + anInputCasReferenceId);
				}

			}
			catch( Exception ex)
			{
				//	Any error here is automatic termination
				ex.printStackTrace();
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { ex });
				handleAction(ErrorHandler.TERMINATE, null, null);
				return;
			}

			// Continue with Steps. The CAS has been produced by the CAS Multiplier
			executeFlowStep(flow, aNewCasReferenceId, true);
			
		}
		catch ( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			handleError(map, e);
		}
		
	}

	/**
	 * This is a process method that is executed for CASes not created by a Multiplier in this aggregate.
	 * 
	 */
	public void process(CAS aCAS, String aCasReferenceId)// throws AnalysisEngineProcessException, AsynchAEException
	{
		FlowContainer flow = null;
		try
		{
			if (aCasReferenceId != null)
			{
				try
				{
					//	Check if a Flow object has been previously generated for the Cas.
					if (flowMap.containsKey(aCasReferenceId))
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retrieve_flow_object__FINEST", new Object[] { aCasReferenceId });

						flow = (FlowContainer) flowMap.get(aCasReferenceId);
					}
					else
					{
						//getMonitor().incrementCount("", Monitor.ProcessCount);
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_new_flow_object__FINEST", new Object[] { aCasReferenceId });
						flow = flowControllerContainer.computeFlow(aCAS);
						// Save the Flow Object in a cache. Flow exists in the cache
						// 	until the CAS is fully processed or it is
						// explicitly deleted when processing of this CAS cannot
						// continue
						synchronized( flowMap )
						{
							flowMap.put(aCasReferenceId, flow);
						}
					}
				}
				catch( Exception ex)
				{
					//	Any error here is automatic termination
					ex.printStackTrace();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { ex });
					handleAction(ErrorHandler.TERMINATE, null, null);
					return;
				}
				if ( !isStopped() )
				{
					// Execute a step in the flow. false means that this CAS has not
					// been produced by CAS Multiplier
					executeFlowStep(flow, aCasReferenceId, false);
				}
				else
				{
					flow.aborted();
				}
			}
		}
		catch ( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			handleError(map, e);
		}
	}

	private void simpleStep(SimpleStep aStep, String aCasReferenceId)// throws AsynchAEException
	{
		Endpoint endpoint = null;
		try
		{
			String analysisEngineKey = aStep.getAnalysisEngineKey();
			//	Find the endpoint for the delegate
			endpoint = lookUpEndpoint(analysisEngineKey, true);
			if ( endpoint != null )
			{
				endpoint.setController(this);
				if ( endpoint.isCasMultiplier() )
				{
					getInProcessCache().
						getCacheEntryForCAS(aCasReferenceId).setCasMultiplierKey(analysisEngineKey);
				}
				
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "simpleStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_next_step__FINEST", new Object[] { aCasReferenceId, analysisEngineKey });
				
				//	Reset number of parrallel delegates back to one. This is done only if the previous step was a parallel step.
				if (getInProcessCache().getCacheEntryForCAS(aCasReferenceId).getNumberOfParallelDelegates() > 1)
				{
					getInProcessCache().getCacheEntryForCAS(aCasReferenceId).setNumberOfParallelDelegates(1);
				}
				if ( !isStopped() )
				{
					// Start a timer for this request. The amount of time to wait
					// for response is provided in configuration for this endpoint
					dispatchProcessRequest(aCasReferenceId, endpoint, true);
				}
			}
		}
		catch ( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			if ( endpoint != null )
			{
				map.put(AsynchAEMessage.Endpoint, endpoint);
			}
			handleError(map, e);
		}

	}

	private void parallelStep(ParallelStep aStep, String aCasReferenceId) throws AsynchAEException
	{
		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "parallelStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_parallel_step__FINE",
	                new Object[] {getName(), aCasReferenceId });
			Collection keyList = aStep.getAnalysisEngineKeys();
			String[] analysisEngineKeys = new String[keyList.size()];
			keyList.toArray(analysisEngineKeys);

			CacheEntry cacheEntry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
			cacheEntry.resetDelegateResponded();
			cacheEntry.setNumberOfParallelDelegates(analysisEngineKeys.length);

			Endpoint[] endpoints = new Endpoint_impl[analysisEngineKeys.length];
			for (int i = 0; i < analysisEngineKeys.length; i++)
			{
				endpoints[i] = lookUpEndpoint(analysisEngineKeys[i], true);
				endpoints[i].setController(this);
			}
			dispatchProcessRequest(aCasReferenceId, endpoints, true);
		}
		catch ( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			handleError(map, e);
		}
			
	}

	public void sendRequestForMetadataToRemoteDelegates() throws AsynchAEException
	{
		for( int i=0; i < childControllerList.size(); i++ )
		{
			if (  childControllerList.get(i) instanceof AggregateAnalysisEngineController )
			{
				(( AggregateAnalysisEngineController)childControllerList.get(i)).sendRequestForMetadataToRemoteDelegates();
			}
		}
		Endpoint[] remoteEndpoints = new Endpoint[destinationMap.size()];

		//	First copy endpoints to an array so that we dont get Concurrent access problems
		//	in case an error handler needs to disable the endpoint.
		Set keySet = destinationMap.keySet();
		Iterator it = keySet.iterator();
		int indx=0;
		while (it.hasNext())
		{
			remoteEndpoints[indx++] = (Endpoint) destinationMap.get((String) it.next());
		}
		//	Now send GetMeta request to all remote delegates
		for( int i=0; i < remoteEndpoints.length; i++)
		{
			if ( remoteEndpoints[i].isRemote())
			{
				remoteEndpoints[i].initialize();
				remoteEndpoints[i].setController(this);
				String key = lookUpDelegateKey(remoteEndpoints[i].getEndpoint());
				Endpoint endpoint = ((Endpoint) destinationMap.get(key));
				if ( key != null && endpoint != null)
				{
					ServiceInfo serviceInfo = endpoint.getServiceInfo();
					PrimitiveServiceInfo pServiceInfo = new PrimitiveServiceInfo();
					pServiceInfo.setBrokerURL(serviceInfo.getBrokerURL());
					pServiceInfo.setInputQueueName(serviceInfo.getInputQueueName());
					pServiceInfo.setState(serviceInfo.getState());
					pServiceInfo.setAnalysisEngineInstanceCount(1);
					
					registerWithAgent(pServiceInfo, super.getManagementInterface().getJmxDomain()
							+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+serviceInfo.getLabel());

					ServicePerformance servicePerformance = new ServicePerformance();
					
					registerWithAgent(servicePerformance, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+servicePerformance.getLabel());

					ServiceErrors serviceErrors = new ServiceErrors();
					
					registerWithAgent(serviceErrors, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+serviceErrors.getLabel());
					remoteIndex++;

					serviceErrorMap.put(key, serviceErrors);
					Object[] delegateStatsArray = 
						new Object[] { pServiceInfo, servicePerformance, serviceErrors }; 

					delegateStatMap.put( key, delegateStatsArray);					
				}
				dispatchMetadataRequest(remoteEndpoints[i]);
			}
		}
	}

	private void finalStep(FinalStep aStep, String aCasReferenceId)// throws AsynchAEException
	{
		Endpoint endpoint=null;
    CacheEntry inputCASCacheEntry = null;
    boolean replyWithInputCAS = false;
    
    
		try
		{
			CacheEntry cacheEntry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
/*
			System.out.println("Aggregate.finalStep() - Cache Size:::"+getInProcessCache().getSize()+"-Input CAS RefId::::::::::::::::::::::"+cacheEntry.getInputCasReferenceId());
			
			
			if ( cacheEntry.getInputCasReferenceId() == null )  // This is an input CAS  
			{
			  // Check the cache to see if there are any subordinate CASes (those produced from the input CAS) still in-play
			  if ( getInProcessCache().producedCASesStillInPlay(aCasReferenceId, null))
			  {
		      System.out.println("Aggregate.finalStep() - Input CAS In Final Step - There are still subordinate CASes in-play");
			    cacheEntry.setPendingReply(true);
			    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			    // Leave input CAS in pending state. It will be returned to the client
			    // *only* if the last subordinate CAS is fully processed.
          // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			    return;
			  }
			}
			else 
      {
			  inputCASCacheEntry =
			    getInProcessCache().getCacheEntryForCAS(cacheEntry.getInputCasReferenceId());      
			  if ( !getInProcessCache().producedCASesStillInPlay(cacheEntry.getInputCasReferenceId(), aCasReferenceId) )
			  {
			    replyWithInputCAS = true;
			  }
      }
*/
			
			// Cas Processing has been completed. Check if the CAS should be sent to
			// the client.
			// Any of the two following conditions will prevent this aggregate from
			// sending the CAS to the client:
			// 1) FinalStep is configured to drop the CAS
			// 2) If the CAS has been produced by the Cas Multiplier and the
			// aggregate is not configured to output new CASes

			String casProducer = cacheEntry.getCasProducerAggregateName();
			boolean isNewCas = (cacheEntry.isNewCas() && casProducer != null && getName().equals(casProducer));

			//	If debug level=FINEST show the size of the cache
			getInProcessCache().dumpContents();
			if (aStep.getForceCasToBeDropped() || (isNewCas && aggregateMetadata.getOperationalProperties().getOutputsNewCASes() == false))
			{
				endpoint = getInProcessCache().getEndpoint(null, aCasReferenceId);
				dropCAS(aCasReferenceId, true);
			}
			else
			{
				// Get the endpoint that represents a client that send the request
				// to this analytic. If the first arg to getEndpoint() is null, the method
				// should return the origin.
				if (isTopLevelComponent())
				{
					endpoint = getInProcessCache().getEndpoint(null, aCasReferenceId);
				}
				else
				{
					endpoint = getMessageOrigin(aCasReferenceId);
				}
				if ( endpoint != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
							"finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step__FINEST", new Object[] { aCasReferenceId, (double) (System.nanoTime() - endpoint.getEntryTime()) / (double) 1000000 });
				}
	/*
				System.out.println(Thread.currentThread().getName() + "::" + getClass().getName() + "=========== Completed Flow For Cas with Reference Id::" + aCasReferenceId);
				System.out.println(Thread.currentThread().getName() + "::" + getClass().getName() + "=========== Origin Server::" + endpoint.getServerURI());
				System.out.println(Thread.currentThread().getName() + "::" + getClass().getName() + "=========== Origin Endpoint::" + endpoint.getEndpoint());
				System.out.println(Thread.currentThread().getName() + "::" + getClass().getName() + "=========== Entry Time::" + endpoint.getEntryTime());
				System.out.println(Thread.currentThread().getName() + "::" + getClass().getName() + "=========== Time Spent in Anotator::" + (double) (System.nanoTime() - endpoint.getEntryTime()) / (double) 1000000);
	*/
				if ( !isTopLevelComponent() )
				{
					dropFlow(aCasReferenceId, false);
				}

				if (endpoint != null && endpoint.getEndpoint() == null)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_reply_destination__INFO", new Object[] { aCasReferenceId });
					HashMap map = new HashMap();
					map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
					map.put(AsynchAEMessage.CasReference, aCasReferenceId);
					handleError(map, new UnknownDestinationException());

				}
				else if ( endpoint != null && !endpoint.isCasMultiplier() )
				{
					endpoint.setFinal(true);
					
					if ( !isStopped() )
					{
						// Send response to the given endpoint
						getOutputChannel().sendReply(aCasReferenceId, endpoint);
					}
					
				}
				// If the destination for the reply is in the same jvm dont remove
				// the entry from the cache. The client may need to retrive CAS by reference
				// to do some post-processing. The client will remove the entry when done
				// post-processing CAS.
				if (endpoint != null && !endpoint.getServerURI().startsWith("vm:"))
				{

					// Message was fully processed, remove state info related to the
					// previous CAS from the cache
					InProcessCache cache = getInProcessCache();
					
					synchronized (cache)
					{
						dropCAS(aCasReferenceId, true);
					}
				}
				
			}
/*
			if ( replyWithInputCAS )
			{
        if (isTopLevelComponent())
        {
          endpoint = getInProcessCache().getEndpoint(null, cacheEntry.getInputCasReferenceId());
        }
        else
        {
          endpoint = getMessageOrigin(cacheEntry.getInputCasReferenceId());
        }
        // Send response to the given endpoint
        getOutputChannel().sendReply(cacheEntry.getInputCasReferenceId(), endpoint);
        synchronized (getInProcessCache())
        {
          dropCAS(inputCASCacheEntry.getCasReferenceId(), true);
        }
			}
*/
			String casMultiplierKey = cacheEntry.getCasMultiplierKey();
			if ( isNewCas  && casMultiplierKey != null ) //&& cacheEntry.shouldSendRequestToFreeCas())
			{
				endpoint = lookUpEndpoint(casMultiplierKey, true);
				if ( endpoint != null && endpoint.isRemote() && endpoint.isCasMultiplier() ) //&& cacheEntry.shouldSendRequestToFreeCas() )
				{
					endpoint.setEndpoint(endpoint.getEndpoint()+"__CasSync");
					getOutputChannel().sendRequest(AsynchAEMessage.ReleaseCAS, endpoint );
				}
				endpoint = null;
			}
			


			
			
			
			removeMessageOrigin(aCasReferenceId);
			dropStats(aCasReferenceId, super.getName());
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
					"finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step_show_internal_stats__FINEST", new Object[] { getName(), flowMap.size(),getInProcessCache().getSize(),originMap.size(), super.statsMap.size()});
			
		}
		catch( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			if ( endpoint != null )
			{
				map.put(AsynchAEMessage.Endpoint, endpoint);
			}
			handleError(map, e);
		}
		finally
		{
			if ( aCasReferenceId != null && originMap.containsKey(aCasReferenceId))
			{
				originMap.remove(aCasReferenceId);
			}
			getServicePerformance().incrementNumberOfCASesProcessed();
		}
	}

	private void executeFlowStep(FlowContainer aFlow, String aCasReferenceId, boolean newCAS) throws AsynchAEException
	{
		Step step = null;
		try
		{
			step = aFlow.next();
		}
		catch( Exception e)
		{
			//	Any error here is automatic termination
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
			try
			{
				getInProcessCache().destroy();
				handleAction(ErrorHandler.TERMINATE, null, null);
			}
			catch( Exception ex){ex.printStackTrace();}
			return;
		}

		
		try
		{
			if (step instanceof SimpleStep)
			{
				simpleStep((SimpleStep) step, aCasReferenceId);
			}
			else if (step instanceof ParallelStep)
			{

				Collection keyList = ((ParallelStep) step).getAnalysisEngineKeys();
				String[] analysisEngineKeys = new String[keyList.size()];
				keyList.toArray(analysisEngineKeys);

				String aeKeys = "";
				Iterator it = keyList.iterator();
				while (it.hasNext())
				{
					aeKeys += "::" + (String) it.next();

				}

				parallelStep((ParallelStep) step, aCasReferenceId);
			}
			else if (step instanceof FinalStep)
			{
				finalStep((FinalStep) step, aCasReferenceId);
			}

		}
		catch ( Exception e)
		{
			e.printStackTrace();
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			handleError(map, e);
		}
	}

	private void dispatch(String aCasReferenceId, Endpoint anEndpoint) throws AsynchAEException
	{
		getOutputChannel().sendRequest(aCasReferenceId, anEndpoint);
	}

	private void dispatchProcessRequest(String aCasReferenceId, Endpoint anEndpoint, boolean addEndpointToCache) throws AsynchAEException
	{
		if (addEndpointToCache)
		{
			getInProcessCache().addEndpoint(anEndpoint, aCasReferenceId);
		}
		anEndpoint.setController(this);
		dispatch(aCasReferenceId, anEndpoint);

	}

	public void retryProcessCASRequest(String aCasReferenceId, Endpoint anEndpoint, boolean addEndpointToCache) throws AsynchAEException
	{
		Endpoint endpoint = null;
		if ( getInProcessCache().getEndpoint(anEndpoint.getEndpoint(), aCasReferenceId) != null)
		{
			endpoint = getInProcessCache().getEndpoint(anEndpoint.getEndpoint(), aCasReferenceId);
		}
		else
		{
			endpoint = anEndpoint;
			String key = lookUpDelegateKey(anEndpoint.getEndpoint());
			endpoint = lookUpEndpoint(key, true);
			getInProcessCache().addEndpoint(endpoint, aCasReferenceId);
		}
		dispatchProcessRequest(aCasReferenceId, endpoint, addEndpointToCache);
	}

	private void dispatchProcessRequest(String aCasReferenceId, Endpoint[] anEndpointList, boolean addEndpointToCache) throws AsynchAEException
	{
		for (int i = 0; i < anEndpointList.length; i++)
		{
			if (addEndpointToCache)
			{
				getInProcessCache().addEndpoint(anEndpointList[i], aCasReferenceId);
			}
		}

		getOutputChannel().sendRequest(aCasReferenceId, anEndpointList);
	}


	public boolean isDelegateKeyValid(String aDelegateKey)
	{
		if (destinationMap.containsKey(aDelegateKey))
		{
			return true;
		}

		return false;
	}	
	
	public String lookUpDelegateKey(String anEndpointName)
	{

		if (destinationToKeyMap.containsKey(anEndpointName))
		{
			return (String) destinationToKeyMap.get(anEndpointName);
		}

		return null;
	}

	public Endpoint lookUpEndpoint(String anAnalysisEngineKey, boolean clone) throws AsynchAEException
	{
		Endpoint endpoint = (Endpoint) destinationMap.get(anAnalysisEngineKey);
		if (endpoint != null && clone)
		{
			return (Endpoint) ((Endpoint_impl) endpoint).clone(); // (Endpoint)
		}
		return endpoint;
	}
    public PrimitiveServiceInfo getDelegateServiceInfo( String aDelegateKey )
    {
    	if ( delegateStatMap.containsKey(aDelegateKey ) == false )
    	{
    		return null;
    	}
    	Object[] delegateStats;
    	delegateStats = (Object[])delegateStatMap.get(aDelegateKey);
    	if ( delegateStats != null )
    	{
    		return (PrimitiveServiceInfo)delegateStats[SERVICE_INFO_INDX];
    	}
    	return null;
    }
    public ServicePerformance getDelegateServicePerformance( String aDelegateKey )
    {
    	Object[] delegateStats;
    	delegateStats = (Object[])delegateStatMap.get(aDelegateKey);
    	if ( delegateStats != null )
    	{
        	return (ServicePerformance)delegateStats[SERVICE_PERF_INDX];
    	}
    	
    	return null;
    }
    public ServiceErrors getDelegateServiceErrors( String aDelegateKey )
    {
    	Object[] delegateStats;
    	delegateStats = (Object[])delegateStatMap.get(aDelegateKey);
    	if ( delegateStats != null )
    	{
    		return (ServiceErrors)delegateStats[SERVICE_ERROR_INDX];
    	}
    	return null;
    }

    public void mergeTypeSystem(String aTypeSystem, String fromDestination) throws AsynchAEException
	{
		try
		{
			String key = findKeyForValue(fromDestination);
			Endpoint_impl endpoint = (Endpoint_impl) destinationMap.get(key);

			if (endpoint == null)
			{
				// Log invalid reply and move on
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "mergeTypeSystem", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_metadata_recvd_from_invalid_delegate__INFO", new Object[] {getName(), fromDestination });
			}
			else if ( endpoint.isWaitingForResponse())
			{
				endpoint.setWaitingForResponse(false);
				endpoint.cancelTimer();
				boolean collocatedAggregate = false;
				ResourceMetaData resource = null;
				if (aTypeSystem.trim().length() > 0)
				{
					if ( endpoint.isRemote() )
					{
						System.out.println(key+" Remote Service Registered Successfully");
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "mergeTypeSystem", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_remote_delegate_ready__CONFIG", new Object[] { getComponentName(), fromDestination });
					
					}
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "mergeTypeSystem", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_merge_ts_from_delegate__CONFIG", new Object[] { fromDestination });

					ByteArrayInputStream bis = new ByteArrayInputStream(aTypeSystem.getBytes());
					XMLInputSource in1 = new XMLInputSource(bis, null);

					resource = UIMAFramework.getXMLParser().parseResourceMetaData(in1);

					getCasManagerWrapper().addMetadata((ProcessingResourceMetaData) resource);
					analysisEngineMetaDataMap.put(key, (ProcessingResourceMetaData) resource);
					
					if ( ((ProcessingResourceMetaData) resource).getOperationalProperties().getOutputsNewCASes() )
					{
						endpoint.setIsCasMultiplier(true);
						remoteCasMultiplierList.add(key);
						if ( endpoint.isRemote())
						{
							int remoteCasPoolSize = 0;
							Object o = null;
							if ( ( o = ((ProcessingResourceMetaData) resource).getConfigurationParameterSettings().getParameterValue(AnalysisEngineController.CasPoolSize)) != null )
							{
								remoteCasPoolSize = ((Integer)o).intValue();
								System.out.println(">>>>>>>>>>>>>> Remote CAS Pool Size:::"+remoteCasPoolSize);
								if ( remoteCasPoolSize > endpoint.getShadowPoolSize() )
								{
									System.out.println(">>>>> Remote Cas Multiplier Cas Pool Size Exceeds the Size of the Local Cas Pool Size <<<<<<");
									UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "mergeTypeSystem", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_invalid_cp_size__CONFIG", new Object[] {getName(), fromDestination, remoteCasPoolSize, endpoint.getShadowPoolSize() });
									throw new ResourceConfigurationException(UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,"UIMAEE_invalid_cp_size__CONFIG", new Object[] {getName(), fromDestination, remoteCasPoolSize, endpoint.getShadowPoolSize()});
								}
								cmCasPoolSizeDelta = endpoint.getShadowPoolSize()-remoteCasPoolSize;
							}
						}
					}
					
					if ( endpoint.isRemote())
					{
						Object o = null;
						ServiceInfo remoteDelegateServiceInfo =
							getDelegateServiceInfo(key);
						if ( remoteDelegateServiceInfo != null && remoteDelegateServiceInfo instanceof PrimitiveServiceInfo &&
							 ( o = ((ProcessingResourceMetaData) resource).getConfigurationParameterSettings().getParameterValue(AnalysisEngineController.AEInstanceCount)) != null )
						{
							((PrimitiveServiceInfo)remoteDelegateServiceInfo).setAnalysisEngineInstanceCount(((Integer)o).intValue());
						}
					}
				}
				else
				{
					collocatedAggregate = true;
				}

				endpoint.setInitialized(true);

				//	If getMeta request not yet sent, send meta request to all remote delegate
				//	Special case when all delegates are remote is handled in the setInputChannel

				if ( requestForMetaSentToRemotes == false && !allDelegatesAreRemote )
				{
					String unregisteredDelegateKey = null;
					for ( int i=0; i < unregisteredDelegateList.size(); i++ )
					{
						unregisteredDelegateKey = (String) unregisteredDelegateList.get(i);
						if ( unregisteredDelegateKey.equals( key ))
						{
							unregisteredDelegateList.remove(i);
						}
					}
/*
					//	When all collocated delegates reply with metadata send request for meta to
					//	remote delegates.
					if ( unregisteredDelegateList.size() == 0 )
					{
						requestForMetaSentToRemotes = true;
						sendRequestForMetadataToRemoteDelegates();
					}
					*/
				}

				
				//  
				if (collocatedAggregate || resource instanceof ProcessingResourceMetaData)
				{
					if (allTypeSystemsMerged())
					{

						for(int i=0; i < remoteCasMultiplierList.size(); i++)
						{
							Endpoint endpt = (Endpoint) destinationMap.get((String)remoteCasMultiplierList.get(i));
							if( endpt != null && endpt.isCasMultiplier() && endpt.isRemote())
							{
								System.out.println("Setting Shadow Pool of Size:"+endpt.getShadowPoolSize()+" For Cas Multiplier:"+(String)remoteCasMultiplierList.get(i));						
								getCasManagerWrapper().initialize(endpt.getShadowPoolSize(),(String)remoteCasMultiplierList.get(i));
							}
						}
						if ( !isStopped() )
						{
							completeInitialization();
						}
					}
				}
			}
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	private void completeInitialization() throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(), "completeInitialization", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_all_ts_merged__CONFIG");

		if (errorHandlerChain == null)
		{
			plugInDefaultErrorHandlerChain();
		}

		// Create CAS Pool with a given Context.
		AnalysisEngineDescription specifier = (AnalysisEngineDescription) super.getResourceSpecifier();
		aggregateMetadata = specifier.getAnalysisEngineMetaData();
		if (isTopLevelComponent())
		{
			// Top level component is the outer most component in the containment hierarchy.
			getCasManagerWrapper().initialize("AggregateContext");
			aggregateMetadata.setTypeSystem(getCasManagerWrapper().getMetadata().getTypeSystem());
			aggregateMetadata.setTypePriorities(getCasManagerWrapper().getMetadata().getTypePriorities());
			aggregateMetadata.setFsIndexCollection(getCasManagerWrapper().getMetadata().getFsIndexCollection());

		}
		
        flowControllerContainer = 
					UimaClassFactory.produceAggregateFlowControllerContainer(specifier, 
					flowControllerDescriptor, 
					analysisEngineMetaDataMap,
					getUimaContextAdmin(), 
					((AnalysisEngineDescription)getResourceSpecifier()).getSofaMappings(),
					super.getManagementInterface());
		
		if (disabledDelegateList.size() > 0)
		{
			flowControllerContainer.removeAnalysisEngines(disabledDelegateList);
		}
		

		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "completeInitialization", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_initialized_controller__INFO",new Object[] { getComponentName() });
		// Open latch to allow messages to be processed. The
		// latch was closed to prevent messages from entering
		// the controller before it is initialized.
		latch.openLatch(getName(), isTopLevelComponent(), true);
		initialized = true;
		
		//  Notify client listener that the initialization of the controller was successfull
		notifyListenersWithInitializationStatus(null);
	}

	private String findKeyForValue(String fromDestination)
	{
	
		Set set = destinationMap.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			Endpoint endpoint = (Endpoint)entry.getValue();
			if (endpoint != null )
			{
				String value = endpoint.getEndpoint();
				if (value.equals(fromDestination))
				{
					return (String)entry.getKey();
				}
			}
		}

		return null;
	}

	private boolean allTypeSystemsMerged()
	{
		if (typeSystemsMerged)
		{
			return true;
		}
		Set set = destinationMap.entrySet();
		int delegateCount = 0;
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			Endpoint endpoint = (Endpoint)entry.getValue();
			if (endpoint != null && endpoint.isInitialized() == false)
			{
				break; //	At least one delegate has not replied to GetMeta Request
			}
			delegateCount++;
		}
		if ( delegateCount == destinationMap.size())
		{
			return true;    // 	All delegates responded to GetMeta request
		}
		return false;
	}


	public void initialize() throws AsynchAEException
	{
		Statistic statistic = null;
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessCount)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.ProcessCount);
			getMonitor().addStatistic("", statistic);
		}
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessErrorCount)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.ProcessErrorCount);
			getMonitor().addStatistic("", statistic);
		}
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessErrorRetryCount)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.ProcessErrorRetryCount);
			getMonitor().addStatistic("", statistic);
		}

	}

	public void dispatchMetadataRequest(Endpoint anEndpoint) throws AsynchAEException
	{
		anEndpoint.startMetadataRequestTimer();
		anEndpoint.setController(this);
		
		String key = lookUpDelegateKey(anEndpoint.getEndpoint());
		if ( !delegateStatMap.containsKey(key))
		{
			if ( key != null && anEndpoint != null)
			{
				ServiceInfo serviceInfo = anEndpoint.getServiceInfo();
				PrimitiveServiceInfo pServiceInfo = new PrimitiveServiceInfo();
				pServiceInfo.setBrokerURL(serviceInfo.getBrokerURL());
				pServiceInfo.setInputQueueName(serviceInfo.getInputQueueName());
				pServiceInfo.setState(serviceInfo.getState());
				pServiceInfo.setAnalysisEngineInstanceCount(1);
				
				registerWithAgent(pServiceInfo, super.getManagementInterface().getJmxDomain()
						+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+serviceInfo.getLabel());

				ServicePerformance servicePerformance = new ServicePerformance();
				
				registerWithAgent(servicePerformance, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+servicePerformance.getLabel());

				ServiceErrors serviceErrors = new ServiceErrors();
				
				registerWithAgent(serviceErrors, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+serviceErrors.getLabel());
				remoteIndex++;

				serviceErrorMap.put(key, serviceErrors);
				Object[] delegateStatsArray = 
					new Object[] { pServiceInfo, servicePerformance, serviceErrors }; 

				delegateStatMap.put( key, delegateStatsArray);					

			}
		}
		getOutputChannel().sendRequest(AsynchAEMessage.GetMeta, anEndpoint);
	}

	public void retryMetadataRequest(Endpoint anEndpoint) throws AsynchAEException
	{
		dispatchMetadataRequest(anEndpoint);
	}

	public void sendMetadata(Endpoint anEndpoint)// throws AsynchAEException
	{
		try
		{
			if ( aggregateMetadata != null )
			{
				getOutputChannel().sendReply(aggregateMetadata, anEndpoint, true);
			}
		}
		catch ( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Endpoint, anEndpoint);
			map.put(AsynchAEMessage.MessageType, Integer.valueOf(AsynchAEMessage.Request));
			map.put(AsynchAEMessage.Command, Integer.valueOf(AsynchAEMessage.Metadata));

			handleError(map, e);
		}
	}

	public ControllerLatch getControllerLatch()
	{
		return latch;
	}
	public Monitor getMonitor()
	{
		return super.monitor;
	}

	
	public void setMonitor(Monitor monitor)
	{
		this.monitor = monitor;
	}
	public void handleDelegateLifeCycleEvent(String anEndpoint, int aDelegateCount)
	{
		Endpoint endpoint = null;
		if (destinationMap.containsKey(anEndpoint))
		{
			endpoint =  (Endpoint) destinationMap.get(anEndpoint);
		}
		if ( aDelegateCount == 0 )
		{
			
			//	Set the state of the delegate endpoint
			endpoint.setNoConsumers(true);
			//	Get entries from the in-process cache that match the endpoint name
			CacheEntry[] cachedEntries = 
				getInProcessCache().getCacheEntriesForEndpoint(anEndpoint);
			
			for( int i=0; cachedEntries != null && i < cachedEntries.length; i++ )
			{
				String casReferenceId = cachedEntries[i].getCasReferenceId();
				getInProcessCache().getEndpoint(anEndpoint, casReferenceId).cancelTimer();
				Endpoint requestOrigin = cachedEntries[i].getMessageOrigin();
				try
				{
					getOutputChannel().sendReply(new UimaEEServiceException("Delegates Not Found To Process CAS on Endpoint:"+anEndpoint), casReferenceId, requestOrigin, AsynchAEMessage.Process);
				}
				catch( Exception e)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleDelegateLifeCycleEvent", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_consumers__INFO", new Object[] { casReferenceId, anEndpoint });
				}
				finally
				{
					getInProcessCache().remove(casReferenceId);
				}
			}
		}
		else if ( endpoint != null && endpoint.hasNoConsumers() )
		{
			//	At least one consumer is available 
			endpoint.setNoConsumers(false);
		}
			
	}

	public void retryLastCommand( int aCommand, Endpoint anEndpoint, String aCasReferenceId )
	{
		try
		{
			if ( AsynchAEMessage.Process == aCommand )
			{
				getOutputChannel().sendRequest(aCasReferenceId, anEndpoint);
			}
			else
			{
				getOutputChannel().sendRequest(aCommand, anEndpoint);
			}
		}
		catch( AsynchAEException e)
		{
			
		}
	}
	
	public boolean sendRequestToReleaseCas()
	{
		
		synchronized( counterMonitor )
		{
			if ( cmCasPoolSizeDelta > 0 && counter < cmCasPoolSizeDelta )
			{
				counter++;
				return true;
			}
			return false;
		}
	}
	public ServiceErrors getServiceErrors(String aDelegateKey)
	{
		if ( !serviceErrorMap.containsKey(aDelegateKey ))
		{
			return null;
		}
		return (ServiceErrors)serviceErrorMap.get(aDelegateKey);
	}
	public AggregateServiceInfo getServiceInfo()
	{
		if ( serviceInfo == null )
		{
			serviceInfo = new AggregateServiceInfo();
			if ( getInputChannel() != null )
			{
				serviceInfo.setInputQueueName(getInputChannel().getName());
				serviceInfo.setBrokerURL(getInputChannel().getServerUri());
				serviceInfo.setDeploymentDescriptor("");

			}
			serviceInfo.setState("Running");
		}
		return serviceInfo;
	}
	public ServicePerformance getServicePerformance(String aDelegateKey )
	{
		return getDelegateServicePerformance(aDelegateKey);
	}
    /**
     * Accumulate analysis time for the aggregate
     * 
     * @param anAnalysisTime
     */
    public synchronized void incrementAnalysisTime( long anAnalysisTime )
    {
		servicePerformance.incrementAnalysisTime(anAnalysisTime);
    }

    public void stopTimers()
	{
		Set set = destinationMap.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			Endpoint endpoint = (Endpoint)entry.getValue();
			if (endpoint != null )
			{
				endpoint.cancelTimer();
			}
		}
	
	}

	public boolean requestForMetaSentToRemotes()
	{
		return requestForMetaSentToRemotes;
	}
	
	public void setRequestForMetaSentToRemotes()
	{
		requestForMetaSentToRemotes	= true;
	}

	public void cleanUp()
	{
		if ( flowMap != null )
		{
			flowMap.clear();
		}
		if ( destinationMap != null )
		{
			destinationMap.clear();
		}

		if ( destinationToKeyMap != null )
		{
			destinationToKeyMap.clear();
		}
		if ( analysisEngineMetaDataMap != null )
		{
			analysisEngineMetaDataMap.clear();
		}
		if ( remoteCasMultiplierList != null )
		{
			remoteCasMultiplierList.clear();
		}
		if ( originMap != null )
		{
			originMap.clear();
		}
		if ( childControllerList != null )
		{
			childControllerList.clear();
		}
		if ( delegateStats != null )
		{
			delegateStats.clear();
		}
    if ( flowControllerContainer != null )
    {
      flowControllerContainer.destroy();
    }
	}
	
	
	public void stop()
	{
		super.stop();
		cleanUp();
	}
/*	
	public ServicePerformance getCasStatistics( String aCasReferenceId )
	{
		ServicePerformance casStats = null;
		if ( perCasStatistics.containsKey(aCasReferenceId) )
		{
			casStats = (ServicePerformance)perCasStatistics.get(aCasReferenceId);
		}
		else
		{
			casStats = new ServicePerformance();
			perCasStatistics.put( aCasReferenceId, casStats);
			System.out.println("########## AggregateController.getCasStatistics()-Controller:"+getComponentName()+" Creating New ServicePerformance Object for Cas:"+aCasReferenceId+" Map HashCode:"+perCasStatistics.hashCode()+" Map Size:"+perCasStatistics.size());
		}
		return casStats;
	}
*/

}
