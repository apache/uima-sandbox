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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.management.ObjectName;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.UimaContextAdmin;
import org.apache.uima.aae.AsynchAECasManager;
import org.apache.uima.aae.EECasManager_impl;
import org.apache.uima.aae.InProcessCache;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.OutputChannel;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaClassFactory;
import org.apache.uima.aae.UimaEEAdminContext;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerChain;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.handler.ProcessCasErrorHandler;
import org.apache.uima.aae.jmx.JmxManagement;
import org.apache.uima.aae.jmx.JmxManager;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.MonitorBaseImpl;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.Statistic;
import org.apache.uima.aae.monitor.statistics.Statistics;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.impl.AnalysisEngineManagementImpl;
import org.apache.uima.analysis_engine.metadata.SofaMapping;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.Resource_ImplBase;
import org.apache.uima.util.Level;

public abstract class BaseAnalysisEngineController extends Resource_ImplBase 
implements AnalysisEngineController, EventSubscriber
{
	private static final Class CLASS_NAME = BaseAnalysisEngineController.class;

	protected HashMap statsMap = new HashMap();

	protected Monitor monitor = new MonitorBaseImpl();

	protected Endpoint clientEndpoint;

	private CountDownLatch inputChannelLatch = new CountDownLatch(1);
	
	private OutputChannel outputChannel;

	private AsynchAECasManager casManager;

	private InProcessCache inProcessCache;

	private AnalysisEngineController parentController;

	private String endpointName;

	protected ResourceSpecifier resourceSpecifier;

	protected HashMap paramsMap;

	protected InputChannel inputChannel;

	protected ErrorHandlerChain errorHandlerChain;
	
	protected long errorCount = 0;

	protected List inputChannelList = new ArrayList();
	
	protected ConcurrentHashMap inputChannelMap = new ConcurrentHashMap();

	protected Map idleTimeMap = new HashMap();

	private UimaEEAdminContext adminContext;

	protected int componentCasPoolSize = 0;

	protected long replyTime = 0;
	
	protected long idleTime = 0;

	protected HashMap serviceErrorMap = new HashMap();

	private boolean registeredWithJMXServer = false; 
	protected String jmxContext = "";
	
	protected HashMap mBeanMap = new HashMap();
	
	protected ServicePerformance servicePerformance = null;
	
	protected ServiceErrors serviceErrors = null;
	
	protected Map timeSnapshotMap = new HashMap();

	private String deploymentDescriptor = "";
	
	private JmxManagement jmxManagement = null;
	
	protected boolean stopped = false;
	
	protected String delegateKey = null;
	
	protected List unregisteredDelegateList = new ArrayList();
	
	protected boolean allDelegatesAreRemote = false;
	
	protected List controllerListeners = new ArrayList();
	
	protected boolean serviceInitialized = false;
	
	protected ConcurrentHashMap perCasStatistics = new ConcurrentHashMap();

	public BaseAnalysisEngineController(AnalysisEngineController aParentController, int aComponentCasPoolSize, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache) throws Exception
	{
		this(aParentController, aComponentCasPoolSize, 0, anEndpointName, aDescriptor, aCasManager, anInProcessCache, null, null);
	}
	public BaseAnalysisEngineController(AnalysisEngineController aParentController, int aComponentCasPoolSize, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap) throws Exception
	{
		this(aParentController, aComponentCasPoolSize, 0, anEndpointName, aDescriptor, aCasManager, anInProcessCache, aDestinationMap, null);
	}	
	public BaseAnalysisEngineController(AnalysisEngineController aParentController, int aComponentCasPoolSize, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap, JmxManagement aJmxManagement) throws Exception
	{
		this(aParentController, aComponentCasPoolSize, 0, anEndpointName, aDescriptor, aCasManager, anInProcessCache, aDestinationMap, aJmxManagement);
	}
	public BaseAnalysisEngineController(AnalysisEngineController aParentController, int aComponentCasPoolSize, long anInitialCasHeapSize, String anEndpointName, String aDescriptor, AsynchAECasManager aCasManager, InProcessCache anInProcessCache, Map aDestinationMap, JmxManagement aJmxManagement) throws Exception
	{
		casManager = aCasManager;
		inProcessCache = anInProcessCache;
		
		parentController = aParentController;
		componentCasPoolSize = aComponentCasPoolSize;
		
		if ( this instanceof AggregateAnalysisEngineController )
		{
			//	Populate a list of un-registered co-located delegates. A delegate will be taken off the un-registered list
			//	when it calls its parent registerChildController() method.
			Set set = aDestinationMap.entrySet();
			for( Iterator it = set.iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry)it.next();
				Endpoint endpoint = (Endpoint)entry.getValue();
				if ( endpoint != null && !endpoint.isRemote() )
				{
					unregisteredDelegateList.add(entry.getKey());
				}
			}
			if ( unregisteredDelegateList.size() == 0 ) // All delegates are remote
			{
				allDelegatesAreRemote = true;
			}
		}

		
		endpointName = anEndpointName;
		delegateKey = anEndpointName;
		
		if (this instanceof AggregateAnalysisEngineController)
		{
			ConcurrentHashMap endpoints = new ConcurrentHashMap();
			endpoints.putAll(aDestinationMap);
			//	Create a map containing: Endpoint-DelegateKey pairs, to enable look-up
			//	of a delegate key based on delegate's endpoint
			((AggregateAnalysisEngineController) this).mapEndpointsToKeys(endpoints);
		
		}
		//	If not the top level, retrieve the name of the endpoint from the parent 
		if ( !isTopLevelComponent() )
		{
			Endpoint endpoint = ((AggregateAnalysisEngineController)parentController).lookUpEndpoint(endpointName, false);
			endpointName = endpoint.getEndpoint();
		}

		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "BaseAnalysisEngineController", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_service_id_INFO",
                new Object[] { endpointName });
		
		resourceSpecifier = UimaClassFactory.produceResourceSpecifier(aDescriptor);

		paramsMap = new HashMap();
		if ( aJmxManagement == null )
		{
			jmxManagement = new JmxManager(getJMXDomain());
		}
		else
		{
			jmxManagement = aJmxManagement;
			if ( jmxManagement.getMBeanServer() != null )
			{
				paramsMap.put(AnalysisEngine.PARAM_MBEAN_SERVER, jmxManagement.getMBeanServer()); 
			}
		}
		paramsMap.put(AnalysisEngine.PARAM_MBEAN_NAME_PREFIX, jmxManagement.getJmxDomain()); 


		//	Top level component?
		if (parentController == null)
		{
		    paramsMap.put(Resource.PARAM_RESOURCE_MANAGER, casManager.getResourceManager());
			initialize(resourceSpecifier, paramsMap);
			AnalysisEngineManagementImpl mbean = (AnalysisEngineManagementImpl)
				getUimaContextAdmin().getManagementInterface();
			//	Override uima core jmx domain setting
			mbean.setName(getComponentName(), getUimaContextAdmin(),jmxManagement.getJmxDomain());
			if ( this instanceof PrimitiveAnalysisEngineController && resourceSpecifier instanceof AnalysisEngineDescription )
			{
				if ( ((AnalysisEngineDescription) resourceSpecifier).getAnalysisEngineMetaData().getOperationalProperties().getOutputsNewCASes() )
				{
					System.out.println(getName()+"-Initializing CAS Pool for Context:"+getUimaContextAdmin().getQualifiedContextName());
					System.out.println(getComponentName()+"-CasMultiplier Cas Pool Size="+aComponentCasPoolSize+" Cas Initialial Heap Size:"+anInitialCasHeapSize);
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "C'tor", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_multiplier_cas_pool_config_INFO",
		                    new Object[] { getComponentName(), aComponentCasPoolSize, anInitialCasHeapSize });
					initializeComponentCasPool(aComponentCasPoolSize, anInitialCasHeapSize);
				}
			}
		}
		else
		{
			UimaContext childContext = parentController.getChildUimaContext(endpointName);
			paramsMap.put(Resource.PARAM_UIMA_CONTEXT, childContext);
			initialize(resourceSpecifier, paramsMap);
			initializeComponentCasPool(aComponentCasPoolSize, anInitialCasHeapSize );
			if (aParentController instanceof AggregateAnalysisEngineController )
			{
				//	Register self with the parent controller 
				((AggregateAnalysisEngineController) aParentController).registerChildController(this,delegateKey);
			}
		}

		//	Each component in the service hierarchy has its own index. The index is used
		//	to construct jmx context path to which every object belongs. 
		int index = getIndex();
		
		//	Get uima ee jmx base context path 
		jmxContext = getJmxContext();
		if ( !isTopLevelComponent() && this instanceof PrimitiveAnalysisEngineController )
		{
			String thisComponentName = ((AggregateAnalysisEngineController)parentController).lookUpDelegateKey(endpointName);
			jmxContext += ( thisComponentName + " Uima EE Service");
		}
		
		//	Register InProcessCache with JMX under the top level component
		if ( inProcessCache != null && isTopLevelComponent() )
		{
			inProcessCache.setName(jmxManagement.getJmxDomain()+jmxContext+",name="+inProcessCache.getName());
			ObjectName on = new ObjectName(inProcessCache.getName() );
			jmxManagement.registerMBean(inProcessCache, on);
		}
		initializeServiceStats();

	}
	/**
	 * Get the domain for Uima JMX. The domain includes a fixed string plus the name of the 
	 * top level component. All uima ee objects are rooted at this domain. 
	 */

	public String getJMXDomain()
	{
		//	Keep calling controllers until the top level component is reached
		if ( !isTopLevelComponent() )
		{
			return parentController.getJMXDomain();
		}
		else
		{
			//	The domain includes the name of the top level component
			return "org.apache.uima:type=ee.jms.services,s="+getComponentName()+" Uima EE Service,";
		}
	}
	public JmxManagement getManagementInterface()
	{
		return jmxManagement;
	}

	/**
	 * Returns a unique id for each component in the service hierarchy.
	 * The top level component's id is always = 0
	 * 
	 */
	public int getIndex()
	{
		if ( isTopLevelComponent() )
		{
			return 0;
		}
		return parentController.getIndex()+1;
	}
	
	
	private void initializeServiceStats()
	{
		Statistic statistic = null;
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalDeserializeTime)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.TotalDeserializeTime);
			getMonitor().addStatistic("", statistic);
		}
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalSerializeTime)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.TotalSerializeTime);
			getMonitor().addStatistic("", statistic);
		}
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.IdleTime)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.IdleTime);
			getMonitor().addStatistic("", statistic);
		}
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessCount)) == null )
		{
			statistic = new LongNumericStatistic(Monitor.ProcessCount);
			getMonitor().addStatistic("", statistic);
		}
		if ( this instanceof PrimitiveAnalysisEngineController )
		{
//			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessCount)) == null )
//			{
//				statistic = new LongNumericStatistic(Monitor.ProcessCount);
//				getMonitor().addStatistic("", statistic);
//			}
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessErrorCount)) == null )
			{
				statistic = new LongNumericStatistic(Monitor.ProcessErrorCount);
				getMonitor().addStatistic("", statistic);
			}
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalProcessErrorCount)) == null )
			{
				statistic = new LongNumericStatistic(Monitor.TotalProcessErrorCount);
				getMonitor().addStatistic("", statistic);
			}
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalAEProcessTime)) == null )
			{
				statistic = new LongNumericStatistic(Monitor.TotalAEProcessTime);
				getMonitor().addStatistic("", statistic);
			}
		}
	}
	private void removeFromJmxServer( ObjectName anMBean) throws Exception
	{
		jmxManagement.unregisterMBean(anMBean);
	}
	
	/**
	 * This is called once during initialization to compute the position of the 
	 * component in the JMX hierarchy and create a context path that will be used
	 * to register the component in the JMX registry.
	 */
	public String getJmxContext()
	{
		if ( isTopLevelComponent() )
		{
			if ( this instanceof AggregateAnalysisEngineController )
			{
				return "p0="+getComponentName()+" Components";
			}
			else if ( this instanceof PrimitiveAnalysisEngineController )
			{
				return "p0="+getComponentName()+" Uima EE";
			}
				
		}
		//	Get the position of the component in the hierarchy. Each component
		//	is registered with a unique context string that is composed of
		//	the domain+<key,value> pair, where the key=p+<index>. The index is
		//	incremented for every component. An example of a hierarchy would be
		//	something like:
		//	<domain>,s=<service name>,p0=<service name>,p1=<aggregate service>,p2=<delegate service>
		
		int index = getIndex();
		String parentContext = parentController.getJmxContext();
		if ( parentController.isTopLevelComponent())
		{
			index=1;
		}
		if ( this instanceof AggregateAnalysisEngineController )
		{
			String thisComponentName = getComponentName();
			if ( !isTopLevelComponent() && endpointName != null)
			{
				thisComponentName = ((AggregateAnalysisEngineController)parentController).lookUpDelegateKey(endpointName);
			}
			return parentContext+",p"+index+"="+thisComponentName+" Components";
		}
		else
		{
			return parentContext+",p"+index+"=";
		}
	}
	/**
	 * Register a component with a given name with JMX MBeanServer
	 * 
	 * @param o - component to register with JMX
	 * @param aName - full jmx context name for the component 
	 */
	protected void registerWithAgent( Object o, String aName)
	{
		try
		{
			ObjectName on = new ObjectName( aName );
			jmxManagement.registerMBean(o, on);
		}
		catch( Exception e)
		{
			//	Log and move on
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "setListenerContainer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
                    new Object[] { e });
		}
	}
	protected void registerServiceWithJMX(String key_value_list, boolean remote) 
	{
		String thisComponentName = getComponentName();
		
		String name = "";
		int index = getIndex(); 
		servicePerformance = new ServicePerformance();
//		name = getJMXDomain()+key_value_list+",name="+thisComponentName+"_"+servicePerformance.getLabel();
		name = jmxManagement.getJmxDomain()+key_value_list+",name="+thisComponentName+"_"+servicePerformance.getLabel();
		
		
		registerWithAgent(servicePerformance, name );

		ServiceInfo serviceInfo = getInputChannel().getServiceInfo();
		ServiceInfo pServiceInfo = null;

		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			pServiceInfo = ((PrimitiveAnalysisEngineController)this).getServiceInfo();
		}
		else
		{
			pServiceInfo = 
				((AggregateAnalysisEngineController)this).getServiceInfo();
		}
		if ( pServiceInfo != null )
		{
//			name = getJMXDomain()+key_value_list+",name="+thisComponentName+"_"+serviceInfo.getLabel();
			name = jmxManagement.getJmxDomain()+key_value_list+",name="+thisComponentName+"_"+serviceInfo.getLabel();
			
			
			if ( !isTopLevelComponent() )
			{
				pServiceInfo.setBrokerURL("Embedded Broker");
			}
			registerWithAgent(pServiceInfo, name );
		}

		serviceErrors = new ServiceErrors();
//		name = getJMXDomain()+key_value_list+",name="+thisComponentName+"_"+serviceErrors.getLabel();
		name = jmxManagement.getJmxDomain()+key_value_list+",name="+thisComponentName+"_"+serviceErrors.getLabel();
		
		
		registerWithAgent(serviceErrors, name );
	}

	protected void cleanUp() throws Exception
	{
		if ( inProcessCache != null && isTopLevelComponent() )
		{
			ObjectName on = new ObjectName(inProcessCache.getName() );
			removeFromJmxServer(on);
		}
	}

	/**
	 * Override the default JmxManager
	 */
	public void setJmxManagement(JmxManagement aJmxManagement)
	{
		jmxManagement = aJmxManagement;
	}
	
	
	private void initializeComponentCasPool(int aComponentCasPoolSize, long anInitialCasHeapSize )
	{
		if (aComponentCasPoolSize > 0)
		{
			EECasManager_impl cm = (EECasManager_impl) getResourceManager().getCasManager();
			cm.setInitialCasHeapSize(anInitialCasHeapSize);
			cm.setPoolSize(getUimaContextAdmin().getQualifiedContextName(), aComponentCasPoolSize);
			System.out.println("Component:"+getComponentName()+" Cas Pool:"+getUimaContextAdmin().getQualifiedContextName()+" Size:"+aComponentCasPoolSize+" Cas Heap Size:"+anInitialCasHeapSize/4 +" cells");
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "initializeComponentCasPool", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_pool_config_INFO",
	                new Object[] { getComponentName(), getUimaContextAdmin().getQualifiedContextName(), aComponentCasPoolSize, anInitialCasHeapSize/4});
		}

	}

	public boolean isTopLevelComponent()
	{
		return (parentController == null);
	}
	
	/**
	 * Returns the name of the component. The name comes from the analysis engine descriptor
	 */
	public String getComponentName()
	{
		return ((ResourceCreationSpecifier)resourceSpecifier).getMetaData().getName();
	}
	
	/**
	 * Print the component name rather than the class name
	 */
	public String toString()
	{
		return getComponentName();
	}
	
	public void addTimeSnapshot( long snapshot, String aKey )
	{
		if ( timeSnapshotMap.containsKey(aKey) )
		{
			timeSnapshotMap.remove(aKey);
		}
		timeSnapshotMap.put(aKey, snapshot);
		
	}
	public void addServiceInfo( ServiceInfo aServiceInfo )
	{
		ServiceInfo sInfo = null;

		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			sInfo = 
				((PrimitiveAnalysisEngineController)this).getServiceInfo();
		}
		else if ( this instanceof AggregateAnalysisEngineController )
		{
			sInfo = 
				((AggregateAnalysisEngineController)this).getServiceInfo();
		}
		if ( sInfo != null )
		{
			sInfo.setBrokerURL(aServiceInfo.getBrokerURL());
			sInfo.setInputQueueName(aServiceInfo.getInputQueueName());
			sInfo.setState(aServiceInfo.getState());
			sInfo.setDeploymentDescriptor(deploymentDescriptor);
		}
		else
		{
			System.out.println("!!!!!!!!!!!!!!! ServiceInfo instance is NULL");
		}

	}
	public long getTimeSnapshot( String aKey )
	{
		if ( timeSnapshotMap.containsKey(aKey) )
		{
			return ((Long)timeSnapshotMap.get(aKey)).longValue();
		}
		return 0;
		
	}
	public ServicePerformance getServicePerformance()
	{
		return servicePerformance;
	}
	public ServiceErrors getServiceErrors()
	{
		return serviceErrors;
	}

	public UimaContext getChildUimaContext(String aDelegateEndpointName) throws Exception
	{
		if (this instanceof AggregateAnalysisEngineController)
		{
			String key = ((AggregateAnalysisEngineController) this).lookUpDelegateKey(aDelegateEndpointName);
			if (key == null )
			{
				if ( ((AggregateAnalysisEngineController) this).isDelegateKeyValid(aDelegateEndpointName) )
				{
					key = aDelegateEndpointName;
				}
			}
			
			if ( key == null )
			{
				throw new AsynchAEException(getName()+"-Unable to look up delegate "+aDelegateEndpointName+" in internal map");
			}
			UimaContextAdmin uctx = getUimaContextAdmin();

			// retrieve the sofa mappings for input/output sofas of this analysis engine
			HashMap sofamap = new HashMap();
			if (resourceSpecifier instanceof AnalysisEngineDescription)
			{
				AnalysisEngineDescription desc = (AnalysisEngineDescription) resourceSpecifier;
				SofaMapping[] sofaMappings = desc.getSofaMappings();
				if (sofaMappings != null && sofaMappings.length > 0)
				{
					for (int s = 0; s < sofaMappings.length; s++)
					{
						// the mapping is for this analysis engine
						if (sofaMappings[s].getComponentKey().equals(key))
						{
							// if component sofa name is null, replace it with
							// the default for TCAS sofa name
							// This is to support old style TCAS
							if (sofaMappings[s].getComponentSofaName() == null)
								sofaMappings[s].setComponentSofaName(CAS.NAME_DEFAULT_SOFA);
							sofamap.put(sofaMappings[s].getComponentSofaName(), sofaMappings[s].getAggregateSofaName());
						}
					}
				}
			}
			Endpoint endpoint = ((AggregateAnalysisEngineController) this).lookUpEndpoint(key, false);
			endpoint.initialize();
			endpoint.setController(this);
			((AggregateAnalysisEngineController) this).dispatchMetadataRequest(endpoint);
			// create child UimaContext and insert into mInitParams map
			return uctx.createChild(key, sofamap);
		}
		return null;
	}

	public void setInputChannel(InputChannel anInputChannel) throws Exception
	{
		inputChannel = anInputChannel;
		inputChannelList.add(anInputChannel);

		inputChannelLatch.countDown();
		if ( !registeredWithJMXServer )
		{
			registeredWithJMXServer = true;
			registerServiceWithJMX(jmxContext, false);
		}
/*		
		if ( this instanceof AggregateAnalysisEngineController )
		{
			AggregateAnalysisEngineController aC = (AggregateAnalysisEngineController)this;
			 if ( aC.requestForMetaSentToRemotes() == false && allDelegatesAreRemote )
			 {
				 aC.setRequestForMetaSentToRemotes();
				 aC.sendRequestForMetadataToRemoteDelegates();
			 }
*/
	}
	public void addInputChannel( InputChannel anInputChannel )
	{
		if ( !inputChannelMap.containsKey(anInputChannel.getInputQueueName()))
		{
			inputChannelMap.put(anInputChannel.getInputQueueName(), anInputChannel);
			
		}
	}
	public InputChannel getInputChannel()
	{
		try
		{
			inputChannelLatch.await();
			
		}
		catch( Exception e){}
		
		return inputChannel;
	}

	public void dropCAS(CAS aCAS)
	{
		if (aCAS != null)
		{
			aCAS.release();
		}

	}
	
	
	public synchronized void saveReplyTime( long snapshot, String aKey )
	{
		replyTime = snapshot;
	}
	
	public synchronized long getReplyTime()
	{
		return replyTime;
	}
	
	public long getIdleTime( String aKey )
	{
		return idleTime;
	}
	
	public synchronized void saveIdleTime( long snapshot, String aKey, boolean accumulate )
	{
		if ( accumulate )
		{
			LongNumericStatistic statistic;
			//	Accumulate idle time across all processing threads
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.IdleTime)) != null )
			{
				statistic.increment(snapshot);
			}
		}
		getServicePerformance().incrementIdleTime(snapshot);
		idleTime += snapshot;
	}
	protected void handleAction( String anAction, String anEndpoint, ErrorContext anErrorContext )
	throws Exception
	{
		
		
		String casReferenceId = null;
		if ( anErrorContext != null )
		{
			casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);
		}
		
		if ( ErrorHandler.TERMINATE.equalsIgnoreCase(anAction))
		{
			//	Propagate terminate event to the top controller and begin shutdown of this service along
			//	with all collocated delegates (if any)
			terminate();
		}
		else if ( ErrorHandler.DISABLE.equalsIgnoreCase(anAction)  )
		{

				if ( anEndpoint != null )
				{
					List list = new ArrayList();
					String key = "";
					if ( ((AggregateAnalysisEngineController)this).lookUpEndpoint(anEndpoint, false) == null )
					{
						key = ((AggregateAnalysisEngineController)this).lookUpDelegateKey(anEndpoint);
						list.add(key);
					}
					else
					{
						key = anEndpoint;
						list.add(anEndpoint);
					}
					
					((AggregateAnalysisEngineController)this).disableDelegates(list);
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleAction", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_disabled_delegate_INFO",
		                new Object[] { getComponentName(), key });
				}
		}
		else if ( ErrorHandler.CONTINUE.equalsIgnoreCase(anAction) )
		{
			if ( anEndpoint != null )
			{
				String key = ((AggregateAnalysisEngineController)this).lookUpDelegateKey(anEndpoint);
				Exception ex = (Exception)anErrorContext.get(ErrorContext.THROWABLE_ERROR);
				boolean continueOnError = 
					((AggregateAnalysisEngineController)this).continueOnError(casReferenceId, key, ex);
				if ( continueOnError )
				{
					CacheEntry entry = null;
					try
					{
						entry = getInProcessCache().getCacheEntryForCAS(casReferenceId);
					}
					catch( AsynchAEException e) {}
                    CAS cas = null;
					//	Make sure that the ErrorHandler did not drop the cache entry and the CAS
					if ( entry != null && (( cas = entry.getCas()) != null ) )
					{
						((AggregateAnalysisEngineController)this).process(cas, casReferenceId);
					}
				}
			}
		}
		else if(ErrorHandler.DROPCAS.equalsIgnoreCase( anAction )) 
		{
			if ( casReferenceId != null)
			{
				dropCAS(casReferenceId, true);
			}
		}

	}
	protected void plugInDefaultErrorHandlerChain()
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
                "plugInDefaultErrorHandlerChain", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_add_default_eh__CONFIG",
                new Object[] { getComponentName() });

		List errorHandlerList = new ArrayList();
		errorHandlerList.add(new ProcessCasErrorHandler());
		errorHandlerChain = new ErrorHandlerChain(errorHandlerList);
	}
	public void setErrorHandlerChain(ErrorHandlerChain errorHandlerChain)
	{
		this.errorHandlerChain = errorHandlerChain;
	}
	
	public ErrorHandlerChain getErrorHandlerChain()
	{
		return errorHandlerChain;
	}	
	protected void handleError( HashMap aMap, Throwable e)
	{
		ErrorContext errorContext = new ErrorContext();
		errorContext.add(aMap);
		getErrorHandlerChain().handle(e, errorContext, this);
		
	}
	public void dropCAS(String aCasReferenceId, boolean deleteCacheEntry)
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dropping_cas__FINE",
                new Object[] {aCasReferenceId, getName() });
			synchronized (inProcessCache)
			{
				if ( inProcessCache.entryExists(aCasReferenceId))
				{
					CAS cas = inProcessCache.getCasByReference(aCasReferenceId);
					if ( cas != null )
					{
						int casHashCode = cas.hashCode();
						dropCAS(cas);
						
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_released_cas__FINE",
				                new Object[] {aCasReferenceId, getComponentName(), casHashCode });
					}
					else
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_release_cas__WARNING",
				                new Object[] {aCasReferenceId, getComponentName() });
						
					}
					if (deleteCacheEntry)
					{
						inProcessCache.remove(aCasReferenceId);
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_removed_cache_entry__FINE",
				                new Object[] {aCasReferenceId, getComponentName() });
					}
					inProcessCache.dumpContents();
				}	
			}
			//	Remove stats from the map maintaining CAS specific stats
			if ( perCasStatistics.containsKey(aCasReferenceId))
			{
				perCasStatistics.remove(aCasReferenceId);
			}
	}

	public synchronized void saveTime(long aTime, String aCasReferenceId, String anEndpointName)
	{
		String key = aCasReferenceId + anEndpointName;
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "saveTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_save_time__FINE",
                new Object[] {aTime, aCasReferenceId, getComponentName(), anEndpointName, key  });
		statsMap.put(key, Long.valueOf(aTime));
	}

	public long getTime(String aCasReferenceId, String anEndpointName)
	{
		String key = aCasReferenceId + anEndpointName;
		
		if (statsMap.containsKey(key))
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "getTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_get_time__FINE",
	                new Object[] {aCasReferenceId, getComponentName(), anEndpointName, key  });
			long time = ((Long) statsMap.get(key)).longValue();
			
			
			synchronized(statsMap)
			{
				statsMap.remove(key);
			}
			return time;
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "getTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_time_not_found__FINE",
	                new Object[] {aCasReferenceId, getName(), anEndpointName, key  });
		}
		return 0;
	}
	protected void resetErrorCounter()
	{
		errorCount = 0;
	}
	
	protected void incrementErrorCounter()
	{
		errorCount++;
	}
	
	protected boolean exceedsThresholdWithinWindow( int threshold, long docCount, int windowSize )
	{
		//	Check if the errorCount reached the threshold
		if ( errorCount > 0 && errorCount % threshold == 0 )
		{
			return true;
		}

		//	Threshold not reached. Now, check if reached max window size. If so, we've processed as many documents
		//	as the window size. Did not exceed error threshold defined for this window, so clear error counter to
		//	count against a new window.
		if (docCount % windowSize == 0 ) 
		{
			resetErrorCounter();
		}

		return false;
	}

	public OutputChannel getOutputChannel()
	{
		return outputChannel;
	}

	public void setOutputChannel(OutputChannel outputChannel) throws Exception
	{
		this.outputChannel = outputChannel;
	}

	public AsynchAECasManager getCasManagerWrapper()
	{
		return casManager;
	}

	public void setCasManager(AsynchAECasManager casManager)
	{
		this.casManager = casManager;
	}

	public InProcessCache getInProcessCache()
	{
		return inProcessCache;
	}

	protected ResourceSpecifier getResourceSpecifier()
	{
		return resourceSpecifier;
	}

	public String getName()
	{
		return endpointName;
	}
	public void process(CAS aCas, String aCasId) 
	{
		// to be overriden
	}

	public void process(CAS aCAS, String anInputCasReferenceId, String aNewCasReferenceId, String newCASProducedBy) //throws AnalysisEngineProcessException, AsynchAEException
	{
		// to be overriden

	}

	public void process(CAS aCAS, String aCasReferenceId, Endpoint anEndpoint) 
	{
		// to be overriden

	}
	public void setUimaEEAdminContext( UimaEEAdminContext anAdminContext )
	{
		adminContext = anAdminContext;
	}
	
	public UimaEEAdminContext getUimaEEAdminContext()
	{
		return adminContext;
	}

	protected void dropIdleTime( String aKey )
	{
		if ( idleTimeMap.containsKey(aKey ) )
		{
			idleTimeMap.remove(aKey);
		}
	}	
	
	private void dropStats( String aKey )
	{
		if ( aKey != null && statsMap.containsKey(aKey ))
		{
			synchronized( statsMap)
			{
				statsMap.remove(aKey);
			}
		}
	}
	/**
	 * Removes statistics from the global Map
	 */
	public void dropStats( String aCasReferenceId, String anEndpointName )
	{
		String key = aCasReferenceId+anEndpointName;
		//	Remove stats associated with this service
		dropStats(key);
		
		if ( this instanceof AggregateAnalysisEngineController )
		{
			//	remove stats for delegates
			Set set = ((AggregateAnalysisEngineController)this).getDestinations().entrySet();
			for( Iterator it = set.iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry)it.next();
				key = aCasReferenceId + ((Endpoint)entry.getValue()).getEndpoint();
				dropStats(key);
			}

		}
		dropCasStatistics(aCasReferenceId);
	}
	protected void logStats()
	{
		if ( this instanceof AggregateAnalysisEngineController )
		{
			Map delegates = ((AggregateAnalysisEngineController)this).getDestinations();
			Set set = delegates.entrySet();
			for( Iterator it = set.iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry)it.next();
				Endpoint endpoint = (Endpoint)entry.getValue();
				if ( endpoint != null )
				{
					//	Fetch stats for the delegate
					ServicePerformance delegatePerformanceStats =
						((AggregateAnalysisEngineController)this).
							getDelegateServicePerformance((String)entry.getKey());
					//	Log this delegate's statistics
					logStats((String)entry.getKey(), delegatePerformanceStats);
				}
			}
		}
		//	log stats for this service
		logStats(getComponentName(),servicePerformance);
	}
	
	/**
	 * Returns stats associated with a given CAS. A service uses a global
	 * map to store CAS level statistics. A key to the map is the CAS id.
	 * This method creates a new instance of ServicePerformance object
	 * if one doesnt exist in the map for a given CAS id.
	 *  
	 */
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
		}
		return casStats;
	}

	/**
	 * Logs statistics  
	 * 
	 * @param aDelegateKey
	 * @param aDelegateServicePerformance
	 */
	protected void logStats( String aDelegateKey, ServicePerformance aServicePerformance )
	{
		if ( aServicePerformance != null )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "logStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dump_primitive_stats__INFO", 
						new Object[] {getComponentName(), aDelegateKey, 
									   aServicePerformance.getNumberOfCASesProcessed(), 
									   aServicePerformance.getCasDeserializationTime(), 
									   aServicePerformance.getCasSerializationTime(), 
									   aServicePerformance.getAnalysisTime(),
				   	   				   aServicePerformance.getMaxSerializationTime(), 
				   	   				   aServicePerformance.getMaxDeserializationTime(), 
				   	   				   aServicePerformance.getMaxAnalysisTime(),
									   aServicePerformance.getIdleTime() });
		}
	}
	
	/**
	 * Logs controller statistics in a uima log.
	 * 
	 * @param aComponentName 
	 * @param aStatsMap
	 */
/*	
	protected void logStats(String aComponentName, Map aStatsMap)
	{
		float totalIdleTime = 0;
		long numberCASesProcessed = 0;
		float totalDeserializeTime = 0;
		float totalSerializeTime = 0;
		
		if ( aStatsMap.containsKey(Monitor.IdleTime))
		{
			totalIdleTime = ((Float)aStatsMap.get(Monitor.IdleTime)).floatValue();
		}
		if ( aStatsMap.containsKey(Monitor.ProcessCount))
		{
			numberCASesProcessed = ((Long)aStatsMap.get(Monitor.ProcessCount)).longValue();
		}
		if ( aStatsMap.containsKey(Monitor.TotalDeserializeTime))
		{
			totalDeserializeTime = ((Float)aStatsMap.get(Monitor.TotalDeserializeTime)).floatValue();
		}
		if ( aStatsMap.containsKey(Monitor.TotalDeserializeTime))
		{
			totalSerializeTime = ((Float)aStatsMap.get(Monitor.TotalSerializeTime)).floatValue();
		}
		float totalAEProcessTime=0;
		if ( aStatsMap.containsKey(Monitor.TotalAEProcessTime))
		{
			totalAEProcessTime = ((Float)aStatsMap.get(Monitor.TotalAEProcessTime)).floatValue();
		}

		if ( totalAEProcessTime > 0 )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "logStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dump_primitive_stats__INFO", new Object[] { aComponentName, totalIdleTime, numberCASesProcessed, totalDeserializeTime, totalSerializeTime, totalAEProcessTime });
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "logStats", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dump_aggregate_stats__INFO", new Object[] { aComponentName, totalIdleTime, numberCASesProcessed, totalDeserializeTime, totalSerializeTime });
		}
		
	}
*/	
	/**
	 * Clears controller statistics.
	 * 
	 */
	protected void clearStats()
	{
		LongNumericStatistic statistic;
		Statistics stats = getMonitor().getStatistics("");
		Set set = stats.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			if ( entry != null && entry.getValue() != null && entry.getValue() instanceof LongNumericStatistic )
			{
				((LongNumericStatistic)entry.getValue()).reset();
			}
		}
		//	Clear CAS statistics
		perCasStatistics.clear();
	}
	/**
	 * Returns a copy of the controller statistics.
	 * 
	 */
	public Map getStats()
	{
		LongNumericStatistic statistic;
		float totalIdleTime = 0;
		long numberCASesProcessed = 0;
		float totalDeserializeTime = 0;
		float totalSerializeTime = 0;
		HashMap map = new HashMap();
		
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.IdleTime)) != null )
		{
			if (statistic.getValue() > 0 )
			{
				totalIdleTime = (float) statistic.getValue()/ (float)1000000;   // get millis
			}
		}
		map.put(Monitor.IdleTime, totalIdleTime);
		
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.ProcessCount)) != null )
		{
			numberCASesProcessed = statistic.getValue();
		}
		map.put(Monitor.ProcessCount, numberCASesProcessed);
		
		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalDeserializeTime)) != null )
		{
			if (statistic.getValue() > 0 )
			{
				totalDeserializeTime = (float) statistic.getValue()/ (float)1000000;   // get millis
			}
		}
		map.put(Monitor.TotalDeserializeTime, totalDeserializeTime);

		if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalSerializeTime)) != null )
		{
			if (statistic.getValue() > 0 )
			{
				totalSerializeTime = (float) statistic.getValue()/ (float)1000000;   // get millis
			}
		}
		map.put(Monitor.TotalSerializeTime, totalSerializeTime);
		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			float totalAEProcessTime=0;
			if ( (statistic = getMonitor().getLongNumericStatistic("",Monitor.TotalAEProcessTime)) != null )
			{
				if (statistic.getValue() > 0 )
				{
					totalAEProcessTime = (float) statistic.getValue()/ (float)1000000;   // get millis
				}
			}
			map.put(Monitor.TotalAEProcessTime, totalAEProcessTime);
		}
		return map;
	}
	
	public void setDeployDescriptor( String aDeployDescriptor )
	{
		deploymentDescriptor = aDeployDescriptor;
		ServiceInfo serviceInfo = null;
		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			serviceInfo = ((PrimitiveAnalysisEngineController)this).getServiceInfo();
		}
		else
		{
			serviceInfo =((AggregateAnalysisEngineController)this).getServiceInfo();
		}
		if ( serviceInfo != null)
		{
			serviceInfo.setDeploymentDescriptor(deploymentDescriptor);
		}

	}
	
	//	JMX 
	public String getServiceName()
	{
		return getInputChannel().getName();
	}
	
	public String getDeploymentDescriptor()
	{
		return deploymentDescriptor;
	}
	public String getDeploymentMode()
	{
		return isTopLevelComponent() ? "remote" : "collocated";
	}
	public String getBrokerURL()
	{
		return System.getProperty("BrokerURI");
	}
	public String getInputQueue()
	{
		return getInputChannel().getInputQueueName();
	}
	public long getIdleTime()
	{
		return 0;
	}
	public long getTotalTimeSpentSerializingCAS()
	{
		return 0;
	}
	public long getTotalTimeSpendDeSerializingCAS()
	{
		return 0;
		
	}
	public long getTotalTimeSpentWaitingForFreeCASInstance()
	{
		return 0;
		
	}
	public long getTotalNumberOfCASesReceived()
	{
		return 0;
		
	}
	public long getTotalNumberOfCASesProcessed()
	{
		return 0;
		
	}
	public long getTotalNumberOfCASesDropped()
	{
		return 0;
		
	}
	public long getTotalNumberOfErrors()
	{
		return 0;
		
	}
	
	public Endpoint getClientEndpoint()
	{
		return clientEndpoint;
	}

	/**
	 * 
	 * @param anEndpoint
	 */
	public void cacheClientEndpoint(Endpoint anEndpoint)
	{
		clientEndpoint = anEndpoint;
	}
	
	/**
	 * Return true if this service is in the shutdown state
	 * 
	 */
	public boolean isStopped()
	{
		return stopped;
	}
	
	public void setStopped()
	{
		stopped = true;
	}
	/**
	 * Stops input channel(s) and initiates a shutdown of all delegates ( if this is an aggregate ). At the end
	 * sends an Exception to the client and closes an output channel.
	 */
	public void stop()
	{
		if ( !isStopped() )
		{
			setStopped();
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stop__INFO", new Object[] { getComponentName() });
		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			getControllerLatch().release();
			//	Stops the input channel of this service
			stopInputChannels();
		}
		else
		{
			((AggregateAnalysisEngineController_impl)this).stopTimers();
			//	Stops ALL input channels of this service including the reply channels
			stopInputChannels();
			int childControllerListSize = ((AggregateAnalysisEngineController_impl)this).childControllerList.size();
			//	send terminate event to all collocated child controllers
			if ( childControllerListSize > 0 )
			{
				for( int i=0; i < childControllerListSize; i++ )
				{
					AnalysisEngineController childController = 
						(AnalysisEngineController)((AggregateAnalysisEngineController_impl)this).childControllerList.get(i);
					
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stop_delegate__INFO", new Object[] { getComponentName(), childController.getComponentName() });
					childController.stop();
					childController.getControllerLatch().release();
				}
			}
		}
		//	Send an exception to the client if this is a top level service
		if (getOutputChannel() != null && isTopLevelComponent() )
		{
			Endpoint clientEndpoint = null;
			if ( ( clientEndpoint = getClientEndpoint() ) != null )
			{
				try
				{
					getOutputChannel().sendReply( new ServiceShutdownException(), null, clientEndpoint, clientEndpoint.getCommand());
				}
				catch( Exception e)
				{
					e.printStackTrace();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
		                    "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
		                    new Object[] { e });
				}
			}
		}
		//	Stop output channel
		getOutputChannel().stop();
		
		adminContext = null;
		if ( !isTopLevelComponent() )
		{
			adminContext = null;
		}
		else
		{
			try
			{
				//	Remove all MBeans registered by this service
				jmxManagement.destroy();
			}
			catch( Exception e){}
			try
			{
				getInProcessCache().destroy();
			}
			catch( Exception e){}
		}
		if (  statsMap != null )
		{
			statsMap.clear();
		}
		if ( inputChannelList != null )
		{
			inputChannelList.clear();
		}
		inputChannel = null;
		if ( idleTimeMap != null )
		{
			idleTimeMap.clear();
		}
		if ( serviceErrorMap != null )
		{
			serviceErrorMap.clear();
		}
		if ( mBeanMap != null )
		{
			mBeanMap.clear();
		}
		if ( timeSnapshotMap != null )
		{
			timeSnapshotMap.clear();
		}
		if ( unregisteredDelegateList != null )
		{
			unregisteredDelegateList.clear();
		}
		if ( casManager != null )
		{
			casManager = null;
		}
		super.destroy();
	
	}
	
	
	/**
	 *  Using a reference to its parent, propagates the terminate event to the top level controller. 
	 *  Typically invoked, when the error handling detects excessive errors and action=terminate. 
	 *  The top level controller, stops its input channel and instructs a colocated Cas Multiplier (it it has one)
	 *  to stop generating new CASes. It then registers self as a listener with the InProcessCache. The callback
	 *  will be called when the InProcessCache becomes empty. Only then, the top level controller will 
	 *  call stop() on each of a delegates (if the top level is an aggregate). 
	 */
	public void terminate()
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "terminate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_process_termiate_event__INFO", new Object[] { getComponentName() });
		if ( !isTopLevelComponent() )
		{
			((BaseAnalysisEngineController)parentController).terminate();
		}
		else if ( !isStopped() )
		{
			setStopped();
			//	Stop the inflow of new input CASes
			stopInputChannel();
			System.out.println("Controller:"+getComponentName()+" Done Stopping Main Input Channel");			
			//	If this is an aggregate that uses a collocated CAS Multiplier, we need to stop generating new CASes.
			//	Call stop() on the CM, and await an input CAS. When the CM is stopped() it will set 'Aborted' flag
			//	in the InProcessCache for the input CAS. When this aggregate receives the input CAS, it will detect
			//	the aborted CAS and will set a callback on the InProcessCache to await an event when all CASes are
			//	fully processed.
			stopCasMultiplier();
			stop();
/*
			//	If the InProcessCache is not empty ( CASes are still in play), register self
			//	(the top level controller) to receive a callback when all CASes are fully
			//	processed and the cache becomes empty. 
			if ( !getInProcessCache().isEmpty() )
			{
				System.out.println("Controller:"+getComponentName()+" Cache Not Empty. Registering Self For Callback");			
				getInProcessCache().dumpContents();
				getInProcessCache().registerCallbackWhenCacheEmpty(this.getEventListener());
			}
			else  
			{
				// Cache is already empty - trigger shutdown. If this controller is an 
			    // aggregate, it will propagate stop() down the delegate hierarchy
				getEventListener().onCacheEmpty();
			}
*/		
		}
	}

	private void stopCasMultiplier()
	{
		if (this instanceof AggregateAnalysisEngineController )
		{
			AnalysisEngineController casMultiplierController = null;
			if  ( (casMultiplierController = getCasMultiplierController()) != null )
			{
				//	If configured, stop a collocated Cas Multiplier
				if ( casMultiplierController != null )
				{
					casMultiplierController.setStopped();
				}
			}
			Map endpoints = ((AggregateAnalysisEngineController)this).getDestinations();
			Iterator it = endpoints.keySet().iterator();
			while( it.hasNext() )
			{
				String key = (String) it.next();
				Endpoint endpoint = (Endpoint) endpoints.get(key);
				if ( endpoint != null && endpoint.isCasMultiplier() && endpoint.isRemote() )
				{
					CacheEntry[] entries = getInProcessCache().getCacheEntriesForEndpoint(endpoint.getEndpoint());
					if ( entries != null )
					{
						try
						{
							Endpoint clonedEndpoint = ((AggregateAnalysisEngineController)this).lookUpEndpoint(key, true);
							//	Modify the name of the queue by appending CasSync. 
							clonedEndpoint.setEndpoint(clonedEndpoint.getEndpoint()+"__CasSync");
							for( int i=0; i < entries.length; i++ )
							{
								getOutputChannel().sendRequest(AsynchAEMessage.Stop, entries[i].getCasReferenceId(), clonedEndpoint);
							}
						}
						catch( Exception e) { e.printStackTrace();}
					}
					
				}
			}
		}
		
	}
	/**
	 * Stops a listener on the main input channel
	 * 
	 */
	protected void stopInputChannel()
	{
		InputChannel iC = getInputChannel(endpointName);
		if( iC != null && !iC.isStopped())
		{
			try
			{
				System.out.println("Controller:"+getComponentName()+" Stopping Input Channel:"+iC.getInputQueueName());
				iC.stop();
			}
			catch( Exception e)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "terminate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_stop_inputchannel__INFO", new Object[] { getComponentName(), endpointName });
			}
		}
	}
	/**
	 * Aggregates have more than one Listener channel. This method stops all configured input channels this service
	 * is configured with.
	 * 
	 */
	protected void stopInputChannels()
	{
		InputChannel iC = null;
		try
		{
			if ( inputChannel != null )
			{
				inputChannel.stop();
			}
		}
		catch( Exception e) { e.printStackTrace();}
		
		Iterator it = inputChannelMap.keySet().iterator();
		int i=1;
		while( it.hasNext() )
		{
			try
			{		
				String key = (String)it.next();
				if ( key != null && key.trim().length() > 0)
				{
					iC = (InputChannel)inputChannelMap.get(key);
					if ( iC != null )
					{
						iC.stop();
					}
				}
				i++;
			}
			catch( Exception e)
			{
				if ( iC != null )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stopInputChannels", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_stop_inputchannel__INFO", new Object[] { getComponentName(), iC.getInputQueueName() });
				}
				else
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "stopInputChannels", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
				}
			}
		}
	}

	
	public AnalysisEngineController getCasMultiplierController()
	{
		int childControllerListSize = ((AggregateAnalysisEngineController_impl)this).childControllerList.size();
		if ( childControllerListSize > 0 )
		{
			for( int i=0; i < childControllerListSize; i++ )
			{
				AnalysisEngineController childController = 
					(AnalysisEngineController)((AggregateAnalysisEngineController_impl)this).childControllerList.get(i);
				if ( childController instanceof PrimitiveAnalysisEngineController  &&
				    ((PrimitiveAnalysisEngineController)childController).isMultiplier()
			       )
				{
					return childController;
				}
			}
		}
		return null;
	}

	public InputChannel getInputChannel( String anEndpointName )
	{
		
		for( int i=0; inputChannelList != null && i < inputChannelList.size(); i++ )
		{
			if ( ((InputChannel)inputChannelList.get(i)).getInputQueueName().equals( anEndpointName) )
			{
				return (InputChannel)inputChannelList.get(i);
			}
		}
		return null;
	}
	
	 
	/**
	 * Callback method called the InProcessCache becomes empty meaning ALL CASes are processed.
	 * The callback is only active when the the top level component is in the process of shutting 
	 * down.
	 */
	public void onCacheEmpty()
	{
		
		if ( stopped )
		{
			getInProcessCache().cancelTimers();
		}
		if ( isTopLevelComponent())
		{
			//	Stop all collocated services
			stop();
			if ( getUimaEEAdminContext() != null)
			{
				//	Stop the container
				getUimaEEAdminContext().shutdown();
			}
			adminContext = null;
		}
	}
	
	/**
	 * Returns interface via which this instance receives callbacks
	 * 
	 */
	public EventSubscriber getEventListener()
	{
		return this;
	}
	/**
	   * Register one or more listeners through which the controller can send
	   * notification of events. 
	   *
	   * 
	   * @param aListener - application listener object to register
	   */
	  public void addControllerCallbackListener(ControllerCallbackListener aListener)
	  {
		    controllerListeners.add(aListener); 
		    if ( serviceInitialized )
		    {
          notifyListenersWithInitializationStatus(null);
		    }
	  }

	  
	  /**
	   * Removes named application listener. 
	   * 
	   * @param aListener - application listener to remove
	   */
	  public void removeControllerCallbackListener(ControllerCallbackListener aListener)
	  {
		    controllerListeners.remove(aListener);
	  }

	  public void notifyListenersWithInitializationStatus(Exception e)
	  {
      if ( controllerListeners.isEmpty())
      {
        return;
      }
      for( int i=0; i < controllerListeners.size(); i++ )
      {
        //  If there is an exception, notify listener with failure
        if ( e != null )
        {
          ((ControllerCallbackListener)controllerListeners.get(i)).
              notifyOnInitializationFailure(e);
        }
        else
        {
          ((ControllerCallbackListener)controllerListeners.get(i)).
              notifyOnInitializationSuccess();
        }
      }
	  }
	  
	  protected void dropCasStatistics( String aCasReferenceId )
	  {
		if ( isTopLevelComponent() && perCasStatistics.containsKey(aCasReferenceId))
		{
				perCasStatistics.remove(aCasReferenceId);
		}
	  }
}
