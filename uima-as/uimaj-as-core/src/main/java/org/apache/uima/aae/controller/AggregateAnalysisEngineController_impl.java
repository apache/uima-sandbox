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
import java.io.ByteArrayOutputStream;
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
import org.apache.uima.aae.spi.transport.UimaMessage;
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
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;


public class AggregateAnalysisEngineController_impl 
extends BaseAnalysisEngineController 
implements AggregateAnalysisEngineController, AggregateAnalysisEngineController_implMBean
{

	private static final Class CLASS_NAME = AggregateAnalysisEngineController_impl.class;

	private static final int SERVICE_INFO_INDX = 0;

    private static final int SERVICE_PERF_INDX = 1;

    private static final int SERVICE_ERROR_INDX = 2;

	private ConcurrentHashMap flowMap = new ConcurrentHashMap();

	private ConcurrentHashMap destinationMap;

	private Map destinationToKeyMap;

	private volatile boolean typeSystemsMerged = false;

	private AnalysisEngineMetaData aggregateMetadata;

	private HashMap analysisEngineMetaDataMap = new HashMap();

	private List disabledDelegateList = new ArrayList();

	private List remoteCasMultiplierList = new ArrayList();
	
	private String descriptor;

	private transient FlowControllerContainer flowControllerContainer;

	private String flowControllerDescriptor;

	private ConcurrentHashMap originMap = new ConcurrentHashMap();
	
	private String controllerBeanName = null;

	private String serviceEndpointName = null;

	private volatile boolean initialized = false;
 
	protected List childControllerList = new ArrayList();
	
	private ConcurrentHashMap delegateStats = new ConcurrentHashMap();
	
	private AggregateServiceInfo serviceInfo = null;

	private int remoteIndex = 1;

	private volatile boolean requestForMetaSentToRemotes = false;

	private ConcurrentHashMap<String, Object[]> delegateStatMap = 
		new ConcurrentHashMap();
	
	public final Object parallelStepMux = new Object();
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
	  if ( anEndpoint == null )
	  {
	    System.out.println("Controller:"+getComponentName()+" Endpoint is NULL. Cas Reference Id:"+aCasReferenceId);
	  }
		originMap.put(aCasReferenceId, anEndpoint);
		if ( UIMAFramework.getLogger().isLoggable(Level.FINE))
		{
			Iterator it = originMap.keySet().iterator();
			StringBuffer sb = new StringBuffer();
			while( it.hasNext())
			{
				String key = (String) it.next();
				Endpoint e = (Endpoint) originMap.get(key);
				if ( e != null )
				{
					sb.append("\t\nCAS:"+key+" Origin:"+e.getEndpoint());
				}
			}
/*			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "addMessageOrigin", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dump_msg_origin__FINE",
	                new Object[] {getComponentName(), sb.toString()});
*/
		}
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
		synchronized( originMap )
		{
			if (originMap.containsKey(aCasReferenceId))
			{
				return (Endpoint) originMap.get(aCasReferenceId);
			}
		}
		return null;
	}
	
	public void removeMessageOrigin( String aCasReferenceId )
	{
		synchronized( originMap )
		{
		  
			if (originMap.containsKey(aCasReferenceId))
			{
				originMap.remove(aCasReferenceId);
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                "removeMessageOrigin", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_remove_msg_origin_entry__FINEST",
                new Object[] {getComponentName(), aCasReferenceId });
			}
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
				//flow.aborted();
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
				synchronized ( flowControllerContainer )
				{
					flow.aborted();
				}
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
	private synchronized boolean allDelegatesCompletedCollection()
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
		
		endProcess(AsynchAEMessage.Process);

    if ( !getClientEndpoint().isRemote() && System.getProperty("UseVmTransport") != null)
    {
        UimaMessage message = 
          getTransport(getClientEndpoint().getEndpoint()).produceMessage(AsynchAEMessage.CollectionProcessComplete,AsynchAEMessage.Response,getName());
        //  Send reply back to the client. Use internal (non-jms) transport
        getTransport(getName()).getUimaMessageDispatcher().dispatch(message);
    }
    else
    {
      getOutputChannel().sendReply(AsynchAEMessage.CollectionProcessComplete, getClientEndpoint());
    }
		
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
			final Object localMux = new Object();
			while (cacheNotEmpty)
			{
				InProcessCache cache = getInProcessCache();
				if (!shownOnce)
				{
					shownOnce = true;
					cache.dumpContents(getComponentName());
				}

				if (cache.isEmpty())
				{
					cacheNotEmpty = false;
				}
				else
				{
					synchronized (localMux)
					{
						localMux.wait(10);
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
		getInProcessCache().dumpContents(getComponentName());

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
				  
			    if ( !endpoint.isRemote() && System.getProperty("UseVmTransport") != null)
			    {
			      try
			      {
			        UimaMessage message = 
			          getTransport(endpoint.getEndpoint()).produceMessage(AsynchAEMessage.CollectionProcessComplete,AsynchAEMessage.Request,getName());
			          //  Send reply back to the client. Use internal (non-jms) transport
			         getTransport(endpoint.getEndpoint()).getUimaMessageDispatcher().dispatch(message);
			      }
			      catch( Exception e)
			      {
			        e.printStackTrace();
			      }
			    }
			    else
			    {
			      getOutputChannel().sendRequest(AsynchAEMessage.CollectionProcessComplete, endpoint);
					  endpoint.startCollectionProcessCompleteTimer();
			    }
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
				synchronized( destinationMap )
				{
					destinationMap.remove(key);
				}
				synchronized( disabledDelegateList )
				{
					disabledDelegateList.add(key);
				}
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
					synchronized( flowControllerContainer )
					{
						flowControllerContainer.removeAnalysisEngines(aDelegateList);
					}
				}
				catch( Exception ex)
				{
					//ex.printStackTrace();
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
			synchronized( flowControllerContainer )
			{
				return flow.continueOnFailure(aDelegateKey, anException);
			}
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	private FlowContainer lookupFlow(String aCasReferenceId)
	{
		synchronized( flowMap )
		{
			if (flowMap.containsKey(aCasReferenceId))
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "lookupFlow", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retrieve_flow_object__FINEST", new Object[] { aCasReferenceId });

				return (FlowContainer) flowMap.get(aCasReferenceId);
			}
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
				synchronized( flowMap )
				{
					//	Lookup a Flow object associated with an input CAS.  
					if (flowMap.containsKey(anInputCasReferenceId))
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_retrieve_flow_object__FINEST", new Object[] { anInputCasReferenceId });
						// Retrieve an input CAS Flow object from the flow cache. This Flow object will be used to compute
						// subordinate Flow for the new CAS.
						flow = (FlowContainer) flowMap.get(anInputCasReferenceId);
					}
				}

				if (flow != null)
				{

					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_lookup_flow_ok__FINE",
			                new Object[] {getName(), aNewCasReferenceId,  newCASProducedBy, anInputCasReferenceId, });
					
					// Compute subordinate Flow from the Flow associated with the
					// input CAS.
					synchronized( flowControllerContainer )
					{
						flow = flow.newCasProduced(aCAS, newCASProducedBy);
					}

					// Save the subordinate Flow Object in a cache. Flow exists in the
					// cache until the CAS is fully processed or it is
					// explicitly deleted when processing of this CAS cannot continue
					synchronized( flowMap )
					{
						flowMap.put(aNewCasReferenceId, flow);
					}
					// Register the fact that this is a new CAS and the fact that is was produced
					// by this aggregate. It is important to register this to determine how to
					// handle the CAS in delegate Aggregate services. When the CAS is processed
					// in the Delegate Aggregate, the CAS produced in the parent Aggregate cannot
					// be dropped in the delegate. Check Final Step logic.
					getInProcessCache().getCacheEntryForCAS(aNewCasReferenceId).setNewCas(true, getComponentName());
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
						synchronized( flowMap)
						{
							flow = (FlowContainer) flowMap.get(aCasReferenceId);
						}
					}
					else
					{
						//getMonitor().incrementCount("", Monitor.ProcessCount);
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "process", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_new_flow_object__FINEST", new Object[] { aCasReferenceId });
						synchronized( flowControllerContainer )
						{
							flow = flowControllerContainer.computeFlow(aCAS);
						}
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
					synchronized( flowControllerContainer )
					{
						flow.aborted();
					}
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
				CacheEntry entry = 
					getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				
				if ( endpoint.isCasMultiplier() )
				{
					entry.setCasMultiplierKey(analysisEngineKey);
				}
				
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "simpleStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_next_step__FINEST", new Object[] { aCasReferenceId, analysisEngineKey });				

				//	Reset number of parallel delegates back to one. This is done only if the previous step was a parallel step.
				synchronized(parallelStepMux)
				{
					if ( entry.getNumberOfParallelDelegates() > 1)
					{
						entry.setNumberOfParallelDelegates(1);
					}
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
			synchronized(parallelStepMux)
			{
				cacheEntry.resetDelegateResponded();
				cacheEntry.setNumberOfParallelDelegates(analysisEngineKeys.length);
			}

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
		Endpoint[] delegateEndpoints = new Endpoint[destinationMap.size()];

		//	First copy endpoints to an array so that we dont get Concurrent access problems
		//	in case an error handler needs to disable the endpoint.
		Set keySet = destinationMap.keySet();
		Iterator it = keySet.iterator();
		int indx=0;
		while (it.hasNext())
		{
		  delegateEndpoints[indx++] = (Endpoint) destinationMap.get((String) it.next());
		}
		//	Now send GetMeta request to all remote delegates
		for( int i=0; i < delegateEndpoints.length; i++)
		{
			if ( delegateEndpoints[i].isRemote())
			{
			  delegateEndpoints[i].initialize();
			  delegateEndpoints[i].setController(this);
				String key = lookUpDelegateKey(delegateEndpoints[i].getEndpoint());
				if ( key != null && destinationMap.containsKey(key))
				{
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
						//servicePerformance.setIdleTime(System.nanoTime());
						servicePerformance.setRemoteDelegate();

						registerWithAgent(servicePerformance, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+servicePerformance.getLabel());

						ServiceErrors serviceErrors = new ServiceErrors();
						
						registerWithAgent(serviceErrors, super.getManagementInterface().getJmxDomain()+super.jmxContext+",r"+remoteIndex+"="+key+" [Remote Uima EE Service],name="+key+"_"+serviceErrors.getLabel());
						remoteIndex++;

						serviceErrorMap.put(key, serviceErrors);
						Object[] delegateStatsArray = 
							new Object[] { pServiceInfo, servicePerformance, serviceErrors }; 

						delegateStatMap.put( key, delegateStatsArray);					
					}
					dispatchMetadataRequest(delegateEndpoints[i]);
				}
			}
			
			else
			{
			  // collocated delegate
	      delegateEndpoints[i].initialize();
	      delegateEndpoints[i].setController(this);
	      
	      // Dispatch GetMeta to the collocated component
	      if ( System.getProperty("UseVmTransport") != null)
	      {
	        delegateEndpoints[i].setWaitingForResponse(true);
	        try
	        {
	          UimaMessage message = 
	            getTransport(delegateEndpoints[i].getEndpoint()).produceMessage(AsynchAEMessage.GetMeta,AsynchAEMessage.Request,getName());

	          getTransport(delegateEndpoints[i].getEndpoint()).getUimaMessageDispatcher().dispatch(message);
	        }
	        catch( Exception e)
	        {
	          throw new AsynchAEException(e);
	        }
	      }
	      else
	      {
	        ((AggregateAnalysisEngineController) this).dispatchMetadataRequest(delegateEndpoints[i]);
	      }
	      
			}
		}
	}

	public void finalStep(FinalStep aStep,  String aCasReferenceId)
	{
		Endpoint endpoint=null;
		boolean casDropped = false;
		boolean replySentToClient = false;
		boolean subordinateCasInPlayCountDecremented=false;
		boolean isSubordinate = false;
		CacheEntry cacheEntry = null;
		Endpoint freeCasEndpoint = null;
		CacheEntry parentCASCacheEntry = null;

		getInProcessCache().dumpContents(getComponentName());
		
		try
		{
			//	Get entry from the cache for a given CAS Id. This throws an exception if
			//	an entry doesnt exist in the cache
			cacheEntry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
			//	Mark the entry to indicate that the CAS reached a final step. This CAS
			//	may still have children and will not be returned to the client until
			//	all of them are fully processed. This state info will aid in the
			//	internal bookkeeping when the final child is processed.
			cacheEntry.setState(CacheEntry.FINAL_STATE);

		}
		catch(Exception e)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
			return;
		}
		//	Found the entry in the cache for a given CAS id
		try
		{
			endpoint = getInProcessCache().getEndpoint(null, aCasReferenceId);
			
			boolean doReleaseParent = false;
			
			synchronized( super.finalStepMux)
			{
				//	Check if this CAS has children (subordinates)
				if ( getInProcessCache().hasNoSubordinates(aCasReferenceId) == false)
				{
					//	This CAS has child CASes still in play. This CAS will remain in the cache
					//	until all its children are fully processed. 
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
							"finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step_parent_cas_child_count__FINEST", new Object[] { getComponentName(),aCasReferenceId,cacheEntry.getSubordinateCasInPlayCount()});
					// Leave input CAS in pending state. It will be returned to the client
		    		// *only* if the last subordinate CAS is fully processed.
		    		cacheEntry.setPendingReply(true);
		    		cacheEntry.setFinalStep(aStep);
		    		//	Done here. There are subordinate CASes still being processed.
					return;
				}
			
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
						"finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step_parent_cas_no_children__FINEST", new Object[] { getComponentName(),aCasReferenceId});

				isSubordinate = cacheEntry.isSubordinate();
				//	If this CAS has a parent, save the destination of a CM that produced it and where we may need to send Free Cas Notification
				if ( isSubordinate )
				{
					freeCasEndpoint = cacheEntry.getFreeCasEndpoint();
					// Decrement this Cas parent child count if this is a top controller
					if ( isTopLevelComponent())
					{
						//	 Fetch the parent Cas cache entry
						parentCASCacheEntry = getInProcessCache().getCacheEntryForCAS(cacheEntry.getInputCasReferenceId());
						//	Decrement number of child Cas'es in play
						parentCASCacheEntry.decrementSubordinateCasInPlayCount();
						//	Save this state in case an exception happens below, the error handler will not decrement children again
						subordinateCasInPlayCountDecremented = true;
					}
				}
				
	      Endpoint clientEndpoint = null;
	      //  If the CAS was generated by this component but the Flow Controller wants to drop it OR this component
	      //  is not a Cas Multiplier
	      if ( forceToDropTheCas( cacheEntry, aStep ) )
	      {
	        if ( cacheEntry.isReplyReceived() ) 
	        {
	          //  Drop the CAS and remove cache entry for it
	          dropCAS(aCasReferenceId, true);
	          casDropped = true;
	          //  If debug level=FINEST dump the entire cache
	          getInProcessCache().dumpContents(getComponentName());
	          // Set this state as if we sent the reply to the client. This triggers a cleanup of origin map and stats
	          // for the current cas
	          if ( isTopLevelComponent())
	          {
	            replySentToClient = true;
	          }
	        }
	      } 
	      else 
	      {
	        
	        synchronized( super.finalStepMux)
	        {
	          if ( cacheEntry.isSubordinate())
	          {
	            cacheEntry.setWaitingForRelease(true);
	          }
	        }
	        
	        //  Send a reply to the Client. If the CAS is an input CAS it will be dropped
	        clientEndpoint = replyToClient( cacheEntry );
	        if ( clientEndpoint != null )
	        {
	          replySentToClient = true;
	        }
	      }
	      if ( releaseParentCas(casDropped, clientEndpoint, parentCASCacheEntry) )
	      {
	        doReleaseParent = true;
	      }
				
			}
			
			//	Now check if the parent CAS is ready for a finalStep. The parent CAS may 
			//	have been processed already but	it is cached since its children are still 
			//	in play.
			if ( doReleaseParent )
			{
				//	All subordinate CASes have been processed. Process the parent CAS recursively.
				finalStep(parentCASCacheEntry.getFinalStep(), parentCASCacheEntry.getCasReferenceId());
			}
			if ( endpoint != null )
			{
				//	remove stats associated with this Cas and a given endpoint
				dropStats(aCasReferenceId, endpoint.getEndpoint());
			}
		}
		catch( Exception e)
		{
			HashMap map = new HashMap();
			map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
			map.put(AsynchAEMessage.CasReference, aCasReferenceId);
			//	If the subordinate count has been decremented, let the error handler know
			//	so that it doesn't decrement the count again. The default action in the
			//	error handler is to decrement number of subordinates responding. An exception
			//	that is no subject to retry will be counted as a response.
			if (subordinateCasInPlayCountDecremented)
			{
				map.put(AsynchAEMessage.SkipSubordinateCountUpdate, true);
			}
			if ( endpoint != null )
			{
				map.put(AsynchAEMessage.Endpoint, endpoint);
			}
			handleError(map, e);
		}
		finally
		{
		  if ( replySentToClient )
		  {
	      removeMessageOrigin(aCasReferenceId);
	      dropStats(aCasReferenceId, super.getName());
		  }
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
					"finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step_show_internal_stats__FINEST", new Object[] { getName(), flowMap.size(),getInProcessCache().getSize(),originMap.size(), super.statsMap.size()});
			//	freeCasEndpoint is a special endpoint for sending Free CAS Notification.
			if (  casDropped && freeCasEndpoint != null )
			{
				freeCasEndpoint.setReplyEndpoint(true);
				try
				{
					//	send Free CAS Notification to a Cas Multiplier
					getOutputChannel().sendRequest(AsynchAEMessage.ReleaseCAS, aCasReferenceId, freeCasEndpoint);
				}
				catch( Exception e) 
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "finalStep", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
				}
			}
		}
	}
	private boolean releaseParentCas(boolean casDropped, Endpoint clientEndpoint, CacheEntry parentCASCacheEntry) 
	{
		return (
				(casDropped || (clientEndpoint != null && !clientEndpoint.isRemote() )) 
				&& parentCASCacheEntry != null  
			    && parentCASCacheEntry.isReplyReceived()
			    && parentCASCacheEntry.isPendingReply()
			    && parentCASCacheEntry.getState() == CacheEntry.FINAL_STATE
				&& parentCASCacheEntry.getSubordinateCasInPlayCount() == 0
			);
	}

	private boolean forceToDropTheCas( CacheEntry cacheEntry, FinalStep aStep)
	{
		//	Get the key of the Cas Producer
		String casProducer = cacheEntry.getCasProducerAggregateName();
		//	CAS is considered new from the point of view of this service IF it was produced by it
		boolean isNewCas = (cacheEntry.isNewCas() && casProducer != null && getComponentName().equals(casProducer));
		//	If the CAS was generated by this component but the Flow Controller wants to drop the CAS OR this component
		//	is not a Cas Multiplier
		if (  isNewCas && ( aStep.getForceCasToBeDropped() || !isCasMultiplier()) )
		{
			return true;
		}
		return false;
	}
	
	private Endpoint replyToClient(CacheEntry cacheEntry ) throws Exception
	{
		Endpoint endpoint = null;
		
		// Get the endpoint that represents a client that send the request
		// to this service. If the first arg to getEndpoint() is null, the method
		// should return the origin.
		if (isTopLevelComponent())
		{
			if ( cacheEntry.isSubordinate()) 
			{
				endpoint = getInProcessCache().getTopAncestorEndpoint(cacheEntry);	
			}
			else
			{
				endpoint = getInProcessCache().getEndpoint(null, cacheEntry.getCasReferenceId());
			}
		}
		else
		{
		  endpoint = getReplyEndpoint( cacheEntry );
/*
		  endpoint = getMessageOrigin(cacheEntry.getCasReferenceId());
			if ( endpoint == null && cacheEntry.getInputCasReferenceId() != null)
			{
			  endpoint = getMessageOrigin(cacheEntry.getInputCasReferenceId());
			}
*/	
			dropFlow(cacheEntry.getCasReferenceId(), false);
		}
		if ( endpoint != null )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), 
					"replyToClient", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_final_step__FINEST", new Object[] { cacheEntry.getCasReferenceId(), (double) (System.nanoTime() - endpoint.getEntryTime()) / (double) 1000000 });

			if (endpoint.getEndpoint() == null)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "replyToClient", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_reply_destination__INFO", new Object[] { cacheEntry.getCasReferenceId() });
				HashMap map = new HashMap();
				map.put(AsynchAEMessage.Command, AsynchAEMessage.Process);
				map.put(AsynchAEMessage.CasReference, cacheEntry.getCasReferenceId());
				handleError(map, new UnknownDestinationException());

			}
			else if ( !endpoint.isCasMultiplier() )
			{
				endpoint.setFinal(true);
				
				if ( !isStopped() )
				{
					//	Check if this CAS is new, meaning it has a parent and this component is a Cas Multiplier
					if ( cacheEntry.isSubordinate() && isCasMultiplier())
					{
						//	Add the generated CAS to the outstanding CAS Map. Client notification will release
						//	this CAS back to its pool
						synchronized(syncObject)
						{
							cmOutstandingCASes.put(cacheEntry.getCasReferenceId(),cacheEntry.getCasReferenceId());
						}
				    if ( !endpoint.isRemote() && System.getProperty("UseVmTransport") != null)
				    {
				      sendVMMessage(AsynchAEMessage.Request, endpoint, cacheEntry);
				    }
				    else
				    {
	            // Send response to a given endpoint
	            //getOutputChannel().sendReply(cacheEntry.getCas(), cacheEntry.getInputCasReferenceId(), aCasReferenceId, endpoint, cacheEntry.getCasSequence());
	            getOutputChannel().sendReply(cacheEntry, endpoint);

				    }
					}
					else
					{
            if ( !endpoint.isRemote() && System.getProperty("UseVmTransport") != null)
            {
              sendVMMessage(AsynchAEMessage.Response, endpoint, cacheEntry);
            }
            else
            {
              // Send response to a given endpoint
              getOutputChannel().sendReply(cacheEntry.getCasReferenceId(), endpoint);
            }
					}
				}
			}
			//	Drop the CAS only if the client is remote and the CAS is an input CAS. 
			//  If this CAS has a parent the client will send Realease CAS notification to release the CAS.
			if ( endpoint.isRemote() && !cacheEntry.isSubordinate())
			{
				dropCAS(cacheEntry.getCasReferenceId(), true);
				//	If the cache is empty change the state of the Aggregate to idle
				if ( getInProcessCache().isEmpty() )
				{
					endProcess(AsynchAEMessage.Process);
				}
			}
		}
		else
		{
		  System.out.println("!!!!!!!!!!!!!!! Controller:"+getComponentName()+" Origin Endpoint Not Found For Cas:"+cacheEntry.getCasReferenceId()+" Or Its Parent Cas:"+cacheEntry.getInputCasReferenceId());
		}
		return endpoint;
	}
  private synchronized void sendVMMessage( int messageType, Endpoint endpoint, CacheEntry cacheEntry) throws Exception 
  {
    //  If the CAS was produced by this aggregate send the request message to the client
    //  Otherwise send the response message.
    UimaMessage message = getTransport(endpoint.getEndpoint()).produceMessage(AsynchAEMessage.Process,messageType,getName());
    if ( cacheEntry.getCasProducerAggregateName() != null && cacheEntry.getCasProducerAggregateName().equals(getComponentName()))
    {
      message.addLongProperty(AsynchAEMessage.CasSequence, cacheEntry.getCasSequence());
    }
    message.addStringProperty(AsynchAEMessage.CasReference, cacheEntry.getCasReferenceId());
    message.addStringProperty(AsynchAEMessage.InputCasReference, cacheEntry.getInputCasReferenceId());
    ServicePerformance casStats = getCasStatistics(cacheEntry.getCasReferenceId());
    
    message.addLongProperty(AsynchAEMessage.TimeToSerializeCAS, casStats.getRawCasSerializationTime());
    message.addLongProperty(AsynchAEMessage.TimeToDeserializeCAS, casStats.getRawCasDeserializationTime());
    message.addLongProperty(AsynchAEMessage.TimeInProcessCAS, casStats.getRawAnalysisTime());
    long iT = getIdleTimeBetweenProcessCalls(AsynchAEMessage.Process); 
    message.addLongProperty(AsynchAEMessage.IdleTime, iT );
    //  Send reply back to the client. Use internal (non-jms) transport
    getTransport(getName()).getUimaMessageDispatcher().dispatch(message);
  }
	private Endpoint getReplyEndpoint(CacheEntry cacheEntry) throws Exception
	{
	  if ( cacheEntry == null )
	  {
	    return null;
	  }
    Endpoint endpoint = getMessageOrigin(cacheEntry.getCasReferenceId());
    if ( endpoint == null && cacheEntry.getInputCasReferenceId() != null)
    {
      //  Recursively call self until an endpoint is found
      endpoint = getReplyEndpoint(getInProcessCache().getCacheEntryForCAS(cacheEntry.getInputCasReferenceId()));
    }
    return endpoint;
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
				//	Special case: check if this CAS has just been produced by a Cas Multiplier.
				//	If so, we received a new CAS but there are no delegates in the pipeline. 
				//	The CM was the last in the flow. In this case, set a property in the cache
				//	to simulate receipt of the reply to this CAS. This is so that the CAS is
				//	released in the finalStep() when the Aggregate is not a Cas Multiplier.
				if ( newCAS)
				{
					CacheEntry entry = getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
					if ( entry != null )
					{
						entry.setReplyReceived();
					}
				}
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
    if ( !anEndpoint.isRemote() && System.getProperty("UseVmTransport") != null)
    {
      try
      {
        UimaMessage message = 
          getTransport(anEndpoint.getEndpoint()).produceMessage(AsynchAEMessage.Process,AsynchAEMessage.Request,getName());
        message.addStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
          //  Send reply back to the client. Use internal (non-jms) transport
         getTransport(anEndpoint.getEndpoint()).getUimaMessageDispatcher().dispatch(message);
      }
      catch( Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      getOutputChannel().sendRequest(aCasReferenceId, anEndpoint);
    }
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
		return lookUpDelegateKey(anEndpointName, null);
	}
	/**
	 * Returns a delegate key given an endpoint (queue) name and a server uri.
	 * If a server is null, only the endpoint name will be used for matching.
	 */
	public String lookUpDelegateKey(String anEndpointName, String server)
	{
		String key = null;
		if (destinationToKeyMap.containsKey(anEndpointName))
		{
			Set keys = destinationMap.keySet();
			Iterator it = keys.iterator();
			//	Find an endpoint for the GetMeta reply. To succeed, match the endpoint (queue) name
			//	as well as the server URI. We allow endpoints managed by different servers to have
			//	the same queue name.
			//	iterate over all endpoints until a match [queue,server] is found.
			while( it.hasNext())
			{
				key = (String)it.next();
				Endpoint_impl endp = (Endpoint_impl) destinationMap.get(key);

				//	Check if a queue name matches
				if ( endp != null &&  endp.getEndpoint().equalsIgnoreCase(anEndpointName))
				{
					//	Check if server match is requested as well
					if ( server != null )
					{
						//	server URIs must match
						if (  endp.getServerURI() != null && endp.getServerURI().equalsIgnoreCase(server) )
						{
							//	found a match for [queue,server]
							break;
						}
						//	Not found yet. Reset the key
						key = null;
						continue;
					}
					//	found a match for [queue]
					break;
				}
				//	Not found yet. Reset the key
				key = null;
			}
		}

//		if (destinationToKeyMap.containsKey(anEndpointName))
//		{
//			return (String) destinationToKeyMap.get(anEndpointName);
//		}

		return key;
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
    	mergeTypeSystem(aTypeSystem, fromDestination, null);
    }
    public void mergeTypeSystem(String aTypeSystem, String fromDestination, String fromServer) throws AsynchAEException
	{
		try
		{
			Set keys = destinationMap.keySet();
			Iterator it = keys.iterator();
			Endpoint_impl endpoint = null;
			String key = null;
			//	Find an endpoint for the GetMeta reply. To succeed, match the endpoint (queue) name
			//	as well as the server URI. We allow endpoints managed by different servers to have
			//	the same queue name.
			//	iterate over all endpoints until a match [queue,server] is found.
			while( it.hasNext())
			{
				key = (String)it.next();
				Endpoint_impl endp = (Endpoint_impl) destinationMap.get(key);

				if ( endp.getServerURI() != null && endp.getServerURI().equalsIgnoreCase(fromServer) && endp.getEndpoint().equalsIgnoreCase(fromDestination))
				{
					endpoint = endp;
					break;
				}
				
			}

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
				ServiceInfo remoteDelegateServiceInfo = null;
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
					}
					if ( endpoint.isRemote())
					{
						Object o = null;
						remoteDelegateServiceInfo =
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
								if ( remoteDelegateServiceInfo != null )
								{
									remoteDelegateServiceInfo.setCASMultiplier();
								}
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
			synchronized( flowControllerContainer )
			{
				flowControllerContainer.removeAnalysisEngines(disabledDelegateList);
			}
		}
		//	Before processing CASes, send notifications to all collocated delegates to
		//	complete initialization. Currently this call forces all collocated Cas Multiplier delegates
		//	to initialize their internal Cas Pools. CM Cas Pool is lazily initialized on 
		//	the first process call. The JMX Monitor needs all the Cas Pools to initialize 
		//	before the process call.
	    onInitialize();

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
				ServicePerformance servicePerformance = new ServicePerformance();
				if ( anEndpoint.isRemote() )
				{
					servicePerformance.setRemoteDelegate();
				}
				ServiceErrors serviceErrors = new ServiceErrors();

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

	public void sendMetadata(Endpoint anEndpoint)
	{
    super.sendMetadata(anEndpoint, aggregateMetadata);
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
			serviceInfo = new AggregateServiceInfo(isCasMultiplier());
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
    	synchronized( flowControllerContainer )
    	{
        	flowControllerContainer.destroy();
    	}
    }
    	perCasStatistics.clear();
	}
	
	
	public void stop()
	{
		super.stop();
		cleanUp();
	}
	protected List getChildControllerList()
	{
		return childControllerList;
	}
	
	/**
	 * Force all collocated delegates to perform any post-initialization steps.
	 */
	public void onInitialize()
    {
		//	For each collocated delegate
		for( int i=0; i < childControllerList.size(); i++ )
		{
			AnalysisEngineController delegateController = 
				(AnalysisEngineController)childControllerList.get(i);
			//	notify the delegate 
			delegateController.onInitialize();
		}
    }
	
}
