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
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
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
import org.apache.uima.aae.UimaAsContext;
import org.apache.uima.aae.UimaClassFactory;
import org.apache.uima.aae.UimaEEAdminContext;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.LocalCache.CasStateEntry;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.delegate.Delegate.DelegateEntry;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerChain;
import org.apache.uima.aae.error.MessageTimeoutException;
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
import org.apache.uima.aae.spi.transport.UimaMessage;
import org.apache.uima.aae.spi.transport.UimaMessageListener;
import org.apache.uima.aae.spi.transport.UimaTransport;
import org.apache.uima.aae.spi.transport.vm.VmTransport;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.impl.AnalysisEngineManagementImpl;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.SofaMapping;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.Resource_ImplBase;
import org.apache.uima.util.Level;

public abstract class BaseAnalysisEngineController extends Resource_ImplBase 
implements AnalysisEngineController, EventSubscriber
{
	private static final Class CLASS_NAME = BaseAnalysisEngineController.class;

	protected volatile ControllerLatch latch = new ControllerLatch();

	protected ConcurrentHashMap statsMap = new ConcurrentHashMap();

	protected Monitor monitor = new MonitorBaseImpl();

	protected Endpoint clientEndpoint;

	private CountDownLatch inputChannelLatch = new CountDownLatch(1);
	
	private OutputChannel outputChannel;

	private AsynchAECasManager casManager;

	private InProcessCache inProcessCache;

	protected AnalysisEngineController parentController;

	private String endpointName;

	protected ResourceSpecifier resourceSpecifier;

	protected HashMap paramsMap;

	protected InputChannel inputChannel;

	protected ErrorHandlerChain errorHandlerChain;
	
	protected long errorCount = 0;

	protected List inputChannelList = new ArrayList();
	
	protected ConcurrentHashMap inputChannelMap = new ConcurrentHashMap();

	protected ConcurrentHashMap idleTimeMap = new ConcurrentHashMap();

	private UimaEEAdminContext adminContext;

	protected int componentCasPoolSize = 0;

	protected long replyTime = 0;
	
	protected long idleTime = 0;

	protected ConcurrentHashMap serviceErrorMap = new ConcurrentHashMap();

	private boolean registeredWithJMXServer = false; 
	protected String jmxContext = "";
	
	protected ConcurrentHashMap mBeanMap = new ConcurrentHashMap();
	
	protected ServicePerformance servicePerformance = null;
	
	protected ServiceErrors serviceErrors = null;
	
	protected ConcurrentHashMap timeSnapshotMap = new ConcurrentHashMap();

	private String deploymentDescriptor = "";
	
	private JmxManagement jmxManagement = null;
	
	protected volatile boolean stopped = false;
	
	protected String delegateKey = null;
	
	protected List unregisteredDelegateList = new ArrayList();
	
	protected volatile boolean allDelegatesAreRemote = false;
	
	protected List controllerListeners = new ArrayList();
	
	protected volatile boolean serviceInitialized = false;
	
	protected ConcurrentHashMap perCasStatistics = new ConcurrentHashMap();

	private volatile boolean casMultiplier = false;
	
	protected Object syncObject = new Object();
	
	//	Map holding outstanding CASes produced by Cas Multiplier that have to be acked
	protected ConcurrentHashMap cmOutstandingCASes = new ConcurrentHashMap();
	
	private Object mux = new Object();
	
	private Object waitmux = new Object();
	
	private volatile boolean waitingForCAS = false;
	
	private long startTime = System.nanoTime();
	
	private long totalWaitTimeForCAS = 0;
	
	private long lastCASWaitTimeUpdate = 0;

	private Map<Long, AnalysisThreadState> threadStateMap =
		new HashMap<Long,AnalysisThreadState>();
	
	protected final Object finalStepMux = new Object();
	
	protected ConcurrentHashMap<String, UimaTransport> transports
	  = new ConcurrentHashMap<String, UimaTransport>();
	
  protected ConcurrentHashMap<String, UimaMessageListener> messageListeners
  = new ConcurrentHashMap<String, UimaMessageListener>();
  
  private Exception initException = null;
	
  // Local cache for this controller only. This cache stores state of 
  // each CAS. The actual CAS is still stored in the global cache. The
  // local cache is used to determine when each CAS can be removed as
  // it reaches the final step. Global cache is not a good place to
  // store this information if there are collocated (delegate) controllers
  // A CAS state change made by one controller may effect another controller. 
  protected LocalCache localCache;

  protected String aeDescriptor; 
  //	List of Delegates
  protected List<Delegate> delegates = new ArrayList<Delegate>();
  //  indicates whether or not we received a callback from the InProcessCache when
  //  it becomes empty
  protected volatile boolean  callbackReceived = false;
  //  Monitor used in stop() to await a callback from InProcessCache
  protected Object callbackMonitor = new Object();
  
  protected volatile boolean awaitingCacheCallbackNotification = false;
  protected ConcurrentHashMap<String, String> abortedCasesMap = 
    new ConcurrentHashMap<String, String>();

  protected String processPid = "";
  private CountDownLatch stopLatch = new CountDownLatch(1);
  
  //  Set to true when stopping the service
  private volatile boolean releasedAllCASes;
  
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
    localCache = new LocalCache(this);
    aeDescriptor = aDescriptor;
		parentController = aParentController;
		componentCasPoolSize = aComponentCasPoolSize;
		
		if ( this instanceof AggregateAnalysisEngineController )
		{
			//	Populate a list of un-registered co-located delegates. A delegate will be taken off the un-registered list
			//	when it calls its parent registerChildController() method.
			Set set = aDestinationMap.entrySet();
      synchronized (unregisteredDelegateList) {
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

		resourceSpecifier = UimaClassFactory.produceResourceSpecifier(aDescriptor);

		
		if ( isTopLevelComponent())
		{
	    logPlatformInfo(getComponentName());
//			System.out.println("************** Initializing Component:"+getComponentName());		
		}
		else
		{
			System.out.println("************** Initializing Collocated Component:"+getComponentName()+" Parent Controller:"+parentController.getComponentName());		
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "BaseAnalysisEngineController", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_service_id_INFO",
	                new Object[] { endpointName });
	    }
		}
		
		//	Is this service a CAS Multiplier?
		if ( (resourceSpecifier instanceof AnalysisEngineDescription &&
				((AnalysisEngineDescription) resourceSpecifier).getAnalysisEngineMetaData().getOperationalProperties().getOutputsNewCASes()) 
				|| resourceSpecifier instanceof CollectionReaderDescription)
		{
			casMultiplier = true;
		}
		
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
		if ( isTopLevelComponent())
		{
		  System.out.println("Top Level Service:"+getComponentName()+ " Configured to Use Java VM Transport For Internal Messaging");
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
              "C'tor", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_using_vm_transport_INFO",
              new Object[] { getComponentName() });
      }
		}

		//	Top level component?
		if (parentController == null)
		{
		    paramsMap.put(Resource.PARAM_RESOURCE_MANAGER, casManager.getResourceManager());
			initialize(resourceSpecifier, paramsMap);
			AnalysisEngineManagementImpl mbean = (AnalysisEngineManagementImpl)
				getUimaContextAdmin().getManagementInterface();
			//	Override uima core jmx domain setting
			mbean.setName(getComponentName(), getUimaContextAdmin(),jmxManagement.getJmxDomain());
			if ( resourceSpecifier instanceof AnalysisEngineDescription )
			{
				//	Is this service a CAS Multiplier?
				if ( ((AnalysisEngineDescription) resourceSpecifier).getAnalysisEngineMetaData().getOperationalProperties().getOutputsNewCASes() )
				{
					System.out.println(getName()+"-Initializing CAS Pool for Context:"+getUimaContextAdmin().getQualifiedContextName());
					System.out.println(getComponentName()+"-CasMultiplier Cas Pool Size="+aComponentCasPoolSize+" Cas Initialial Heap Size:"+anInitialCasHeapSize);
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "C'tor", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_multiplier_cas_pool_config_INFO",
		                    new Object[] { getComponentName(), aComponentCasPoolSize, anInitialCasHeapSize });
	         }
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
			if (parentController instanceof AggregateAnalysisEngineController )
			{

			  //	Register self with the parent controller 
				((AggregateAnalysisEngineController) parentController).registerChildController(this,delegateKey);
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

		
		//  Show Serialization Strategy of each remote delegate
    if ( this instanceof AggregateAnalysisEngineController )
    {
      Set set = aDestinationMap.entrySet();
      for( Iterator it = set.iterator(); it.hasNext();)
      {
        Map.Entry entry = (Map.Entry)it.next();
        Endpoint endpoint = (Endpoint)entry.getValue();
        if ( endpoint != null && endpoint.isRemote() )
        {
          String key = ((AggregateAnalysisEngineController)this).lookUpDelegateKey(endpoint.getEndpoint());
          System.out.println(">>> Controller:"+getComponentName()+" Configured To Serialize CASes To Remote Delegate:"+key+" Using "+endpoint.getSerializer()+" Serialization");
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "C'tor", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_remote_delegate_serialization_INFO",
                    new Object[] { getComponentName(), key, endpoint.getSerializer()});
          }
        }
      }
    }
    
    //  Create an instance of ControllerMBean and register it with JMX Server.
    //  This bean exposes service lifecycle APIs to enable remote stop
    if ( isTopLevelComponent() ) {
      Controller controller = new Controller(this);
      String jmxName = getManagementInterface().getJmxDomain()+"name="+"Controller";
      registerWithAgent( controller, jmxName);
    }
	}
	
	private void logPlatformInfo(String serviceName ) {
    if ( ManagementFactory.getPlatformMBeanServer() != null )
    {
      RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
      MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
      OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
      //  bean.getName() should return a string that looks like this: PID@HOSTNAME
      //  where PID is the process id and the HOSTNAME is the name of the machine
      processPid = bean.getName();
      if ( processPid != null && processPid.trim().length() > 0 ) {
        int endPos = processPid.indexOf("@"); // find the position where the PID ends
        if ( endPos > -1 ) {
          processPid = processPid.substring(0, endPos);  
       }
      }
      
      DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss ");
      
      StringBuffer platformInfo = new StringBuffer();
      platformInfo.append("\n+------------------------------------------------------------------");
      platformInfo.append("\n                   Starting UIMA AS Service - PID:"+processPid);
      platformInfo.append("\n+------------------------------------------------------------------");
      platformInfo.append("\n+ Service Name:"+serviceName);
      platformInfo.append("\n+ Service Queue Name:"+endpointName);
      platformInfo.append("\n+ Service Start Time:"+df.format(bean.getStartTime()));
      platformInfo.append("\n+ OS Name:"+osBean.getName());
      platformInfo.append("\n+ OS Version:"+osBean.getVersion());
      platformInfo.append("\n+ OS Architecture:"+osBean.getArch());
      platformInfo.append("\n+ OS CPU Count:"+osBean.getAvailableProcessors());
      platformInfo.append("\n+ JVM Vendor:"+bean.getVmVendor());
      platformInfo.append("\n+ JVM Name:"+bean.getVmName());
      platformInfo.append("\n+ JVM Version:"+bean.getVmVersion());
      platformInfo.append("\n+ JVM Input Args:"+bean.getInputArguments());
      platformInfo.append("\n+ JVM Classpath:"+bean.getClassPath());
      platformInfo.append("\n+ JVM LIB_PATH:"+bean.getLibraryPath());
      platformInfo.append("\n+------------------------------------------------------------------");
      System.out.println(platformInfo.toString());
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
              "logPlatformInfo", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_platform_info__INFO",
              new Object[] { platformInfo.toString() });
    }
	}
	public AnalysisEngineController getParentController() {
    return parentController;
	}
  public UimaTransport getTransport(String aKey) throws Exception
  {
    return getTransport( null, aKey);
  }

	 public UimaTransport getTransport(UimaAsContext asContext)
	 throws Exception
	 {
	   String endpointName = getName();
	   if ( !isTopLevelComponent() ) {
	     endpointName = parentController.getName();
	   }
     return getTransport(asContext, endpointName);
	 }

	 public UimaTransport getTransport(UimaAsContext asContext, String aKey)
   throws Exception
   {
     UimaTransport transport = null;
     if ( !transports.containsKey(aKey)) {
       transport = new VmTransport(asContext, this);
       transports.put( aKey, transport);
     }
     else {
       transport = (UimaTransport) transports.get(aKey);
     }
       
     return transport;
   }

	 /**
	  * Initializes transport used for internal messaging between collocated Uima AS services.
	  */
	 public void initializeVMTransport(int parentControllerReplyConsumerCount) throws Exception
	 {
	   //  If this controller is an Aggregate Controller, force delegates to initialize
	   //  their internal transports.
	   if ( this instanceof AggregateAnalysisEngineController )
	   {
	     //  Get a list of all colocated delegate controllers.
	     List childControllers = ((AggregateAnalysisEngineController_impl)this).childControllerList;
	     
	     for( int i=0; i < childControllers.size(); i++) 
	     {
	       AnalysisEngineController ctrl = (AnalysisEngineController)childControllers.get(i);
	       //  Force initialization
	       ctrl.initializeVMTransport(parentControllerReplyConsumerCount);
	     }
	   }
	   
	   //  Only delegate controllers execute the logic below
	   if ( parentController != null )
     {
       UimaAsContext uimaAsContext = new UimaAsContext();
       if ( !registeredWithJMXServer )
       {
         registeredWithJMXServer = true;
         registerServiceWithJMX(jmxContext, false);
       }
       // Determine how many consumer threads to create. First though use the parent Aggregate Controller
       // to lookup this delegate key. Next fetch the delegate endpoint which contains 
       // concurrentConsumers property.
       String key = ((AggregateAnalysisEngineController)parentController).lookUpDelegateKey(getName());
       int concurrentRequestConsumers = 1;
       int concurrentReplyConsumers = 1;
       if ( key != null )
       {
         Endpoint e = ((AggregateAnalysisEngineController)parentController).lookUpEndpoint(key, false);
         concurrentRequestConsumers = e.getConcurrentRequestConsumers();
         concurrentReplyConsumers = e.getConcurrentReplyConsumers();
       }
       
       System.out.println("Controller:"+getComponentName()+" Starting Request Listener With "+concurrentRequestConsumers+" Concurrent Consumers. Reply Listener Configured With "+concurrentReplyConsumers+" Concurrent Consumers");
       
       uimaAsContext.setConcurrentConsumerCount(concurrentRequestConsumers);
       uimaAsContext.put("EndpointName", endpointName);
       
       UimaTransport vmTransport = getTransport(uimaAsContext);
       // Creates delegate Listener for receiving requests from the parent
       UimaMessageListener messageListener = vmTransport.produceUimaMessageListener();
       // Plug in message handlers
       messageListener.initialize(uimaAsContext);
       // Store the listener
       messageListeners.put(getName(), messageListener);
       // Creates parent controller dispatcher for this delegate. The dispatcher is wired
       // with this delegate's listener.
       UimaAsContext uimaAsContext2 = new UimaAsContext();
       // Set up as many reply threads as there are threads to process requests
       uimaAsContext2.setConcurrentConsumerCount(concurrentReplyConsumers);
       uimaAsContext2.put("EndpointName", endpointName);
       UimaTransport parentVmTransport = parentController.getTransport(uimaAsContext2, endpointName);

       parentVmTransport.produceUimaMessageDispatcher(vmTransport);
       // Creates parent listener for receiving replies from this delegate. 
       UimaMessageListener parentListener = parentVmTransport.produceUimaMessageListener();
       // Plug in message handlers
       parentListener.initialize(uimaAsContext2);
       // Creates delegate's dispatcher. It is wired to send replies to the parent's listener.
       vmTransport.produceUimaMessageDispatcher(parentVmTransport);
       // Register input queue with JMX. This is an internal (non-jms) queue where clients
       // send requests to this service.
       vmTransport.registerWithJMX(this, "VmInputQueue");
       parentVmTransport.registerWithJMX( this, "VmReplyQueue");
     }

	 }
	 
	 public synchronized UimaMessageListener getUimaMessageListener(String aDelegateKey)
	 {
	   return messageListeners.get(aDelegateKey);
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "setListenerContainer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
                    new Object[] { e });
      }
		}
	}
	public void registerVmQueueWithJMX( Object o, String aName ) throws Exception {
	  String jmxName = getManagementInterface().getJmxDomain()
            +jmxContext+",name="+getComponentName()+"_"+aName;
	  registerWithAgent( o, jmxName);
	  
	  if ( "VmReplyQueue".equals( aName )) {
	    getServiceInfo().setReplyQueueName(jmxName);
	  } else {
	    getServiceInfo().setInputQueueName(jmxName);
	  }
	}
	protected void registerServiceWithJMX(String key_value_list, boolean remote) 
	{
		String thisComponentName = getComponentName();
		
		String name = "";
		int index = getIndex(); 
		servicePerformance = new ServicePerformance(this);
		name = jmxManagement.getJmxDomain()+key_value_list+",name="+thisComponentName+"_"+servicePerformance.getLabel();
		
		registerWithAgent(servicePerformance, name );
		servicePerformance.setIdleTime(System.nanoTime());
    ServiceInfo serviceInfo = null;
		if ( remote )
		{
	    serviceInfo = getInputChannel().getServiceInfo();
		}
		else
		{
      serviceInfo = new ServiceInfo();
      serviceInfo.setBrokerURL(getBrokerURL());
      serviceInfo.setInputQueueName(getName());
      serviceInfo.setState("Active");
		}
		ServiceInfo pServiceInfo = null;

		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			pServiceInfo = ((PrimitiveAnalysisEngineController)this).getServiceInfo();
			servicePerformance.setProcessThreadCount(((PrimitiveAnalysisEngineController)this).getServiceInfo().getAnalysisEngineInstanceCount());
		}
		else
		{
			pServiceInfo = 
				((AggregateAnalysisEngineController)this).getServiceInfo();
			pServiceInfo.setAggregate(true);
		}
    //  If this is a Cas Multiplier, add the key to the JMX MBean.
    //  This will help the JMX Monitor to fetch the CM Cas Pool MBean
    if ( isCasMultiplier() )
    {
      pServiceInfo.setServiceKey(getUimaContextAdmin().getQualifiedContextName());
    }

		
		if ( pServiceInfo != null )
		{
			name = jmxManagement.getJmxDomain()+key_value_list+",name="+thisComponentName+"_"+serviceInfo.getLabel();
			if ( !isTopLevelComponent() )
			{
				pServiceInfo.setBrokerURL("Embedded Broker");
			}
			else
			{
				pServiceInfo.setTopLevel();
			}
			if ( isCasMultiplier())
			{
				pServiceInfo.setCASMultiplier();
			}
			registerWithAgent(pServiceInfo, name );
		}

		serviceErrors = new ServiceErrors();
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "initializeComponentCasPool", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_pool_config_INFO",
	                new Object[] { getComponentName(), getUimaContextAdmin().getQualifiedContextName(), aComponentCasPoolSize, anInitialCasHeapSize/4});
      }
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
			if ( isCasMultiplier())
			{
				sInfo.setCASMultiplier();
			}
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
	}
	public void addInputChannel( InputChannel anInputChannel )
	{
		if ( !inputChannelMap.containsKey(anInputChannel.getInputQueueName()))
		{
      inputChannelMap.put(anInputChannel.getInputQueueName(), anInputChannel);
      if ( inputChannelList.contains(anInputChannel)) {
        inputChannelList.add(anInputChannel);
      }
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
		  //  Check if this method was called while another thread is stopping the service.
		  //  This is a special case. Normally the releasedAllCASes is false. It is only
		  //  true if another thread forcefully released CASes. 
		  if ( releasedAllCASes ) {
		    //  All CASes could have been forcefully released in the stop() method. If another
		    //  thread was operating on a CAS while the stop() was releasing the CAS we may
		    //  get an exception which will be ignored. We are shutting down. The forceful 
		    //  CAS release is not part of the graceful. In that case, stop() is only called
		    //  when ALL CASes are fully processed. Only than stop() is called and since ALL
		    //  CASes are released at that point we would not see any exceptions.
		    try {
		      aCAS.release();
		    } catch( Exception e) {}
		  } else {
	      aCAS.release();
		  }
		  
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
		  if ( anErrorContext.containsKey(ErrorContext.THROWABLE_ERROR) && anErrorContext.containsKey(AsynchAEMessage.CasReference)) {
        terminate((Throwable) anErrorContext.get(ErrorContext.THROWABLE_ERROR), (String) anErrorContext.get(AsynchAEMessage.CasReference));
		  } else {
	      terminate();
		  }
		}
		else if ( ErrorHandler.DISABLE.equalsIgnoreCase(anAction)  )
		{

				if ( anEndpoint != null )
				{
				  Endpoint endpoint = null;
					List list = new ArrayList();
					String key = "";
					if ( (endpoint = ((AggregateAnalysisEngineController)this).lookUpEndpoint(anEndpoint, false)) == null )
					{
						key = ((AggregateAnalysisEngineController)this).lookUpDelegateKey(anEndpoint);
						endpoint = ((AggregateAnalysisEngineController)this).lookUpEndpoint(key, false);
						list.add(key);
					}
					else
					{
						key = anEndpoint;
						list.add(anEndpoint);
					}
          ((AggregateAnalysisEngineController_impl)this).disableDelegates(list,casReferenceId);

          if ( key != null && key.trim().length() > 0) {
            //  Delegate has been disabled. Cleanup Delegate's lists. Each Delegate
            //  maintains a list of CASes pending reply and a different list of CASes
            //  pending dispatch. The first list contains CASes sent to the delegate.
            //  When a reply is received from the delegate, the CAS is removed from
            //  the list. The second list contains CASes that have been delayed
            //  while the service was in the TIMEDOUT state. These CASes were to 
            //  be dispatched to the delegate once its state is reset to OK. It is
            //  reset to OK state when the delegate responds to the client PING
            //  request. Since we have disabled the delegate, remove ALL CASes from
            //  both lists and send them through the ErrorHandler one at a time 
            //  as if these CASes timed out.
            
            Delegate delegate = ((AggregateAnalysisEngineController)this).lookupDelegate(key);
            //  Cancel the delegate timer. No more responses are expected 
            delegate.cancelDelegateTimer();
            //  Check if we should force timeout on all CASes in a pending state. If this
            //  method is called from ProcessCasErrorHandler we will skip this since we
            //  want to first completely handle the CAS exception. Once that CAS exception 
            //  is handled, the ProcessCasErrorHandler will call forceTimeoutOnPendingCases
            //  to time out CASes in pending lists
            if ( anErrorContext.containsKey( AsynchAEMessage.SkipPendingLists) == false ) {
              //  If the delegate has CASes pending reply still, send each CAS
              //  from the pending list through the error handler with 
              //  MessageTimeoutException as a cause of error
              forceTimeoutOnPendingCases(key);
            }
          }

          if ( endpoint != null ) {
	          try {
	            // Fetch all Cas entries currently awaiting reply from the delegate that was just
	            // disabled. In case the delegate was in a parallel step, we need to adjust the
	            // the number of responses received to account for the failed delegate. This 
	            // enables the processing to continue.
	            CacheEntry[] entries = getInProcessCache().getCacheEntriesForEndpoint(endpoint.getEndpoint());
	            if ( entries != null ) {
	              for ( int i=0; i < entries.length; i++ ) {
	                CasStateEntry cse = localCache.lookupEntry(entries[i].getCasReferenceId());
	                // Check if this is a parallel step
                  int parallelDelegateCount = cse.getNumberOfParallelDelegates();
	                // Check if all delegated responded
	                if ( parallelDelegateCount > 1 && cse.howManyDelegatesResponded() < parallelDelegateCount) {
	                  // increment responders 
	                  cse.incrementHowManyDelegatesResponded();
	                }
	              }
	            }
	          } catch( Exception e) {
	          }
					}
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                "handleAction", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_disabled_delegate_INFO",
		                new Object[] { getComponentName(), key });
	         }
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

	public void forceTimeoutOnPendingCases(String key ) {
    Delegate delegate = ((AggregateAnalysisEngineController)this).lookupDelegate(key);
    //  Cancel the delegate timer. No more responses are expected 
    delegate.cancelDelegateTimer();
    Endpoint endpoint = delegate.getEndpoint();
    //  If the delegate has CASes pending reply still, send each CAS
    //  from the pending list through the error handler with 
    //  MessageTimeoutException as a cause of error
    while ( delegate.getCasPendingReplyListSize() > 0 ) {
      String timedOutCasId = delegate.removeOldestCasFromOutstandingList();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
               "handleAction", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_force_cas_timeout__INFO",
               new Object[] { getComponentName(), key, timedOutCasId, " Pending Reply List" });
      }
      
      ErrorContext errorContext = new ErrorContext();
      errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
      errorContext.add(AsynchAEMessage.CasReference, timedOutCasId);
      errorContext.add(AsynchAEMessage.Endpoint, endpoint);
      getErrorHandlerChain().handle(new MessageTimeoutException(), errorContext, this);
    }
    //  If the delegate has CASes pending dispatch, send each CAS
    //  from the pending dispatch list through the error handler with 
    //  MessageTimeoutException as a cause of error
    while ( delegate.getCasPendingDispatchListSize() > 0 ) {
      String timedOutCasId = delegate.removeOldestFromPendingDispatchList();

      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
               "handleAction", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_force_cas_timeout__INFO",
               new Object[] { getComponentName(), key, timedOutCasId, " Pending Dispatch List" });
      }
      
      ErrorContext errorContext = new ErrorContext();
      errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
      errorContext.add(AsynchAEMessage.CasReference, timedOutCasId);
      errorContext.add(AsynchAEMessage.Endpoint, endpoint);
      getErrorHandlerChain().handle(new MessageTimeoutException(), errorContext, this);
    }
  }
	
	protected void plugInDefaultErrorHandlerChain()
	{
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.CONFIG)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
                "plugInDefaultErrorHandlerChain", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_add_default_eh__CONFIG",
                new Object[] { getComponentName() });
    }
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
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_dropping_cas__FINE",
                new Object[] {aCasReferenceId, getComponentName() });
    }
    if ( inProcessCache.entryExists(aCasReferenceId))
		{
			CAS cas = inProcessCache.getCasByReference(aCasReferenceId);
			if (deleteCacheEntry)
			{
				inProcessCache.remove(aCasReferenceId);
		    if ( localCache.containsKey(aCasReferenceId)) {
		      try {
		        localCache.lookupEntry(aCasReferenceId).setDropped(true);
		      } catch( Exception e) {}
	        localCache.remove(aCasReferenceId);
		    }
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_removed_cache_entry__FINE",
				                new Object[] {aCasReferenceId, getComponentName() });
        }
			}
			if ( cas != null )
			{
				int casHashCode = cas.hashCode();
				dropCAS(cas);
						
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_released_cas__FINE",
				                new Object[] {aCasReferenceId, getComponentName(), casHashCode });
        }
			}
			else
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
				                "dropCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_release_cas__WARNING",
				                new Object[] {aCasReferenceId, getComponentName() });
        }
			}
			inProcessCache.dumpContents(getComponentName());
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
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "saveTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_save_time__FINE",
                new Object[] {aTime, aCasReferenceId, getComponentName(), anEndpointName, key  });
    }
		statsMap.put(key, Long.valueOf(aTime));
	}

	public long getTime(String aCasReferenceId, String anEndpointName)
	{
		String key = aCasReferenceId + anEndpointName;
		
		if (statsMap.containsKey(key))
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "getTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_get_time__FINE",
	                new Object[] {aCasReferenceId, getComponentName(), anEndpointName, key  });
      }
			long time = ((Long) statsMap.get(key)).longValue();
			
			
			synchronized(statsMap)
			{
				statsMap.remove(key);
			}
			return time;
		}
		else
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "getTime", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_time_not_found__FINE",
	                new Object[] {aCasReferenceId, getName(), anEndpointName, key  });
      }
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
			casStats = new ServicePerformance(this);
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
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
	}
	
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
      // Wait until the connection factory is injected by Spring
      while ( System.getProperty("BrokerURI") == null) {
        try {
          Thread.sleep(50);
        } catch ( InterruptedException ex) {}
      }
		return System.getProperty("BrokerURI");
	}
	public String getInputQueue()
	{
		return getInputChannel().getInputQueueName();
	}
/*
	public long getIdleTime()
	{
		return 0;
	}
*/	
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
	
	protected void stopTransportLayer() {
	  if ( transports.size() > 0 ) {
	    Set<Entry<String, UimaTransport>> set = transports.entrySet();
	    for( Entry<String, UimaTransport> entry: set ) {
	      UimaTransport transport = entry.getValue();
	      try {
	        transport.stopIt();
	        System.out.println(">>> Controller:"+getComponentName()+" Stopped Transport For:"+entry.getKey());
	      } catch ( Exception e) {e.printStackTrace();}
	    }
	  }
	}
	/**
	 * Stops input channel(s) and initiates a shutdown of all delegates ( if this is an aggregate ). At the end
	 * sends an Exception to the client and closes an output channel.
	 */
	public void stop() {
	  this.stop(null, null);
	}
  public void stop(Throwable cause, String aCasReferenceId)
	{
		if ( !isStopped() )
		{
			setStopped();
		}
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stop__INFO", new Object[] { getComponentName() });
    }
		if ( this instanceof PrimitiveAnalysisEngineController )
		{
			getControllerLatch().release();
			//	Stops the input channel of this service
      stopInputChannels(InputChannel.CloseAllChannels);
		}
		else
		{
			((AggregateAnalysisEngineController_impl)this).stopTimers();
			//	Stops ALL input channels of this service including the reply channels
      stopInputChannels(InputChannel.CloseAllChannels);
			int childControllerListSize = ((AggregateAnalysisEngineController_impl)this).getChildControllerList().size();
			//	send terminate event to all collocated child controllers
			if ( childControllerListSize > 0 )
			{
				for( int i=0; i < childControllerListSize; i++ )
				{
					AnalysisEngineController childController = 
						(AnalysisEngineController)((AggregateAnalysisEngineController_impl)this).getChildControllerList().get(i);
					
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stop_delegate__INFO", new Object[] { getComponentName(), childController.getComponentName() });
	         }
					childController.stop();
					childController.getControllerLatch().release();
				}
			}
		}
		//	Stops internal transport used to communicate with colocated services
    stopTransportLayer();
		/*
		 * Commented this block. It generates ShutdownException which causes problems
		 * The shutdown of services happens ad hoc and not orderly. This whole logic
		 * needs to be revisited.
		 * 
		//	Send an exception to the client if this is a top level service
		 */ 
		if (cause != null && aCasReferenceId != null && getOutputChannel() != null && isTopLevelComponent() )
		{

		  Endpoint clientEndpoint = null;
			if ( ( clientEndpoint = getClientEndpoint() ) != null )
			{
				try
				{
					getOutputChannel().sendReply( cause, aCasReferenceId, null, clientEndpoint, clientEndpoint.getCommand());
				}
				catch( Exception e)
				{
					e.printStackTrace();
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
		                    "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
		                    new Object[] { e });
	         }
				}
			}
		}
		
    getInProcessCache().releaseAllCASes();
    
    releasedAllCASes = true;
		if ( !isTopLevelComponent() )
		{
			adminContext = null;
		}
		else
		{
	    //  Stop output channel
	    getOutputChannel().stop();
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
    if ( this instanceof AggregateAnalysisEngineController_impl ) {
      ((AggregateAnalysisEngineController_impl)this).cleanUp();
      if ( !((AggregateAnalysisEngineController_impl)this).initialized ) {
        notifyListenersWithInitializationStatus(new ResourceInitializationException());
      }
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
    synchronized (unregisteredDelegateList) {
      //TODO any reason this list needs to be cleared on Stop???
      if ( unregisteredDelegateList != null )
      {
        unregisteredDelegateList.clear();
      }
    }
    if ( casManager != null )
    {
      casManager = null;
    }
    if ( transports != null ) {
      transports.clear();
    }
    if ( threadStateMap != null ) {
      threadStateMap.clear();
    }
    if (inputChannelMap != null) {
      inputChannelMap.clear();
    }
    if ( controllerListeners != null) {
      controllerListeners.clear();
    }
    if ( perCasStatistics != null)  {
      perCasStatistics.clear();
    }
    if ( cmOutstandingCASes != null ) {
      cmOutstandingCASes.clear();
    }
    if ( messageListeners != null) {
      messageListeners.clear();
    }
    
    EECasManager_impl cm = (EECasManager_impl) getResourceManager().getCasManager();
    if ( cm != null ) {
      cm.cleanUp();
    }
    super.destroy();
  
  }
	
	/**
	 * Stops input channel(s) and waits for CASes still in play to complete processing.
	 * When the InProcessCache becomes empty, initiate the service shutdown.
	 */
	public  void quiesceAndStop()
	{
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "quiesceAndStop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stop__INFO", new Object[] { getComponentName() });
    }
    if ( !isStopped() && !callbackReceived ) {
      getControllerLatch().release();
      //  To support orderly shutdown, the service must first stop its input channel and 
      //  then wait until all CASes still in play are processed. When all CASes are processed
      //  we proceed with the shutdown of delegates and finally of the top level service.
      if ( isTopLevelComponent()) {
        //  Stops all input channels of this service, but keep temp reply queue input channels open
        //  to process replies.
        stopInputChannels(InputChannel.InputChannels);
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "quiesceAndStop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_register_onEmpty_callback__INFO", new Object[] { getComponentName() });
        }
        //  Register a callback with the cache. The callback will be made when the cache becomes empty
        getInProcessCache().registerCallbackWhenCacheEmpty(this, InProcessCache.NotifyWhenRegistering);
        //  Now wait until the InProcessCache becomes empty
        while( !callbackReceived ) {
          synchronized(callbackMonitor) {
            //  set global flag to indicate that the controller awaits notification from
            //  the cache. This mechanism is used in the Listener code to determine whether
            //  or not to call stop() on the controller in the event there is an exception.
            awaitingCacheCallbackNotification = true;
            try {
              System.out.println("Controller:"+getComponentName()+" Waiting For InProcessCache Callback. Cache Size:"+getInProcessCache().getSize());
              if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "quiesceAndStop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_waiting_for_onEmpty_callback__INFO", new Object[] { getComponentName() });
              }
              callbackMonitor.wait();
            } catch ( Exception e) {}
          }
        }
        System.out.println("Controller:"+getComponentName()+" Received Callback From the InProcessCache. The Cache Is Empty.");
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "quiesceAndStop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_onEmpty_callback_received__INFO", new Object[] { getComponentName() });
        }
        stop();
      }
    }
	}
	
	
	/**
	 *  Using a reference to its parent, propagates the terminate event to the top level controller. 
	 *  Typically invoked, when the error handling detects excessive errors and action=terminate. 
	 *  The top level controller, stops its input channel and instructs a colocated Cas Multiplier (it it has one)
	 *  to stop generating new CASes. It then registers self as a listener with the InProcessCache. The callback
	 *  will be called when the InProcessCache becomes empty. Only then, the top level controller will 
	 *  call stop() on each of a delegates (if the top level is an aggregate). 
	 */
	public void terminate() {
	  terminate(null, null );
	}
	
  public void terminate( Throwable cause, String aCasReferenceId) {

    synchronized(stopLatch) {
      if ( stopLatch.getCount() > 0 ) {
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "terminate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_process_termiate_event__INFO", new Object[] { getComponentName() });
        }
        stopLatch.countDown();
      } else {
        return;
      }
    }
    if ( !isTopLevelComponent() ) {
      ((BaseAnalysisEngineController)parentController).stop();
    }
    else if ( !isStopped() )
    {
      //  Stop the inflow of new input CASes
      stopInputChannel();
      System.out.println("Controller:"+getComponentName()+" Done Stopping Main Input Channel");     
      stopCasMultipliers();
      if ( cause != null && aCasReferenceId != null ) {
        this.stop(cause, aCasReferenceId);
      } else {
        this.stop();
      }
    }
	}

	private AnalysisEngineController lookupDelegateController( String aName ) {
    List delegateControllers = ((AggregateAnalysisEngineController)this).getChildControllerList();
    for( int i=0; i < delegateControllers.size(); i++ )
    {
      AnalysisEngineController delegateController = 
        (AnalysisEngineController)((AggregateAnalysisEngineController_impl)this).getChildControllerList().get(i);
      if ( delegateController.getName().equals(aName)) {
        return delegateController;
      }
    }
    return null; // no match
	}

	public void stopCasMultipliers() {

    if (this instanceof AggregateAnalysisEngineController )
    {
      Map endpoints = ((AggregateAnalysisEngineController)this).getDestinations();
      Iterator it = endpoints.keySet().iterator();
      // Loop through all delegates and send Stop to Cas Multipliers
      while( it.hasNext() )
      {
        String key = (String) it.next();
        //  Fetch an Endpoint for the corresponding delegate key
        Endpoint endpoint = (Endpoint) endpoints.get(key);
        //  Check if the delegate is a Cas Multiplier
        if ( endpoint != null && endpoint.isCasMultiplier()  )
        {
          //  Fetch the Delegate object corresponding to the current key
          Delegate delegate = ((AggregateAnalysisEngineController)this).lookupDelegate(key);
          if ( delegate != null ) {
            // Get a list of all CASes this aggregate has dispatched to the Cas Multiplier
            List<DelegateEntry> pendingList = delegate.getDelegateCasesPendingRepy();
            try {
              // For each CAS pending reply send a Stop message to the CM
              for( DelegateEntry delegateEntry : pendingList ) {
                if ( endpoint.isRemote() ) {
                  stopCasMultiplier(delegate, delegateEntry.getCasReferenceId());
                } else {
                  AnalysisEngineController delegateCasMultiplier = lookupDelegateController(endpoint.getEndpoint());
                  delegateCasMultiplier.addAbortedCasReferenceId(delegateEntry.getCasReferenceId());
                }
              }
            } catch ( Exception exx) {
              exx.printStackTrace();
            }
          }
        }
      }
    }
    System.out.println(">>> Controller:"+getComponentName()+" Sent Stop Request To Its Cas Multipliers");
  }
	public void stopCasMultiplier(Delegate casMultiplier, String aCasReferenceId)
	{
    // Lookup CAS entry in the local cache
    CasStateEntry casEntry = getLocalCache().lookupEntry(aCasReferenceId);
    if ( casEntry != null ) {
      try {
        if ( casMultiplier != null ) {
          if ( casMultiplier.getEndpoint().isRemote()) {
            // Fetch the endpoint where the Free CAS notification need to go. We use this
            // queue to send Stop messages.
            Endpoint freeCasNotificationEndpoint = casEntry.getFreeCasNotificationEndpoint(); 
            if (freeCasNotificationEndpoint != null ) {
              freeCasNotificationEndpoint.setCommand(AsynchAEMessage.Stop);
              getOutputChannel().sendRequest(AsynchAEMessage.Stop, aCasReferenceId, freeCasNotificationEndpoint);
            }
            System.out.println(">>> Instance Hashcode:"+hashCode()+" Controller:"+getComponentName()+" Stopping Remote Delegate Cas Multiplier:"+casMultiplier.getKey()+" Stopping CM From Generating More CASes from CAS: "+aCasReferenceId);
            if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
              UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stopCasMultiplier", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_stopping_remote_cm_INFO", new Object[] { getComponentName(), casMultiplier.getComponentName(),aCasReferenceId });
            }
          } else {
            System.out.println(">>> Instance Hashcode:"+hashCode()+" Controller:"+getComponentName()+" Stopping Collocated Delegate Cas Multiplier:"+casMultiplier.getKey());
            AnalysisEngineController cm = getCasMultiplierController(casMultiplier.getKey());
            cm.addAbortedCasReferenceId(aCasReferenceId);
          }
        }
      }
      catch( Exception e) { 
        e.printStackTrace();
      } finally {
        casMultiplier.setGeneratingChildrenFrom(aCasReferenceId, false);
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
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "terminate", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_stop_inputchannel__INFO", new Object[] { getComponentName(), endpointName });
        }
			}
		}
	}
	/**
	 * Aggregates have more than one Listener channel. This method stops all configured input channels this service
	 * is configured with.
	 * 
	 */
	protected void stopInputChannels(int channelsToStop)
	{
		InputChannel iC = null;
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
				    if ( channelsToStop == InputChannel.InputChannels && 
				            iC.getServiceInfo() != null && iC.getServiceInfo().getInputQueueName().startsWith("top_level_input_queue") ) {
				      //  This closes both listeners on the input queue: Process Listener and GetMeta Listener 
				      iC.stop(channelsToStop);
				      return; // Just closed input channels. Keep the others open
				    }
						iC.stop(channelsToStop);
					}
				}
				i++;
			}
			catch( Exception e)
			{
				if ( iC != null )
				{
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "stopInputChannels", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_unable_to_stop_inputchannel__INFO", new Object[] { getComponentName(), iC.getInputQueueName() });
	         }
				}
				else
				{
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "stopInputChannels", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { e });
	         }
				}
			}
		}
	}

	
	public AnalysisEngineController getCasMultiplierController(String cmKey)
	{
		int childControllerListSize = ((AggregateAnalysisEngineController_impl)this).getChildControllerList().size();
		if ( childControllerListSize > 0 )
		{
			for( int i=0; i < childControllerListSize; i++ )
			{
				AnalysisEngineController childController = 
					(AnalysisEngineController)((AggregateAnalysisEngineController_impl)this).getChildControllerList().get(i);
				if ( childController.isCasMultiplier() && ((BaseAnalysisEngineController)childController).delegateKey.equals(cmKey)) {
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
		  InputChannel iC = (InputChannel)inputChannelList.get(i);
		  if ( iC.isListenerForDestination( anEndpointName)) {
        return (InputChannel)inputChannelList.get(i);
		  }
		}
		return null;
	}
	 
	public InputChannel getReplyInputChannel( String aDelegateKey ) {
    for( int i=0; inputChannelList != null && i < inputChannelList.size(); i++ )
    {
      if ( ((InputChannel)inputChannelList.get(i)).isFailed(aDelegateKey) )
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
    callbackReceived = true;
	  if ( !stopped ) {
      quiesceAndStop();
    } 
    getInProcessCache().cancelTimers();
    synchronized(callbackMonitor) {
      try {
          callbackMonitor.notifyAll();
      } catch ( Exception e) {}
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
		    if ( initException != null ) {
		      notifyListenersWithInitializationStatus(initException);
		    } else if ( serviceInitialized )  {
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
	    initException = e;
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
              notifyOnInitializationFailure(this, e);
        }
        else
        {
          ((ControllerCallbackListener)controllerListeners.get(i)).
              notifyOnInitializationSuccess(this);
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
	  
	  public boolean isCasMultiplier()
	  {
		  return casMultiplier;
	  }
	  
		public void releaseNextCas(String casReferenceId)
		{
			//	Check if the CAS is in the list of outstanding CASes and also exists in the cache
			if ( cmOutstandingCASes.size() > 0 && cmOutstandingCASes.containsKey(casReferenceId) && getInProcessCache().entryExists(casReferenceId))
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                "releaseNextCas", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_release_cas_req__FINE",
		                new Object[] { getComponentName(), casReferenceId });
        }
				
				try
				{
					//	Release the CAS and remove a corresponding entry from the InProcess cache.
					dropCAS(casReferenceId, true);
					//  Remove the Cas from the outstanding CAS list. The id of the Cas was
		  		//	added to this list by the Cas Multiplier before the Cas was sent to 
	  			//	to the client. 
  				cmOutstandingCASes.remove(casReferenceId);
					//	If debug level=FINEST dump the entire cache
					getInProcessCache().dumpContents(getComponentName());
				}
				catch( Exception e)
				{
					e.printStackTrace();
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
			                "releaseNextCas", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
			                new Object[] { e});
	         }
				}
			}
		}
		
		private boolean validMessageForSnapshot( int msgType )
		{
			return ( AsynchAEMessage.Process == msgType || AsynchAEMessage.CollectionProcessComplete == msgType);
		}
		
		//	Called by ServicePerformance MBean on separate thread 
		
		//	This is called every time a request comes
		public void beginProcess(int msgType )
		{
			//	Disregard GetMeta as it comes on a non-process thread
			if ( validMessageForSnapshot( msgType ) )
			{
				synchronized( mux )
				{
					AnalysisThreadState threadState = null;
					if ( threadStateMap.containsKey(Thread.currentThread().getId()))
					{
						threadState = threadStateMap.get(Thread.currentThread().getId());
						if (threadState.isIdle) {
							threadState.setIdle(false);
							threadState.incrementIdleTime(System.nanoTime()-threadState.getLastUpdate());
							threadState.computeIdleTimeBetweenProcessCalls();
						}
					}
					else
					{
						threadStateMap.put(Thread.currentThread().getId(), new AnalysisThreadState(Thread.currentThread().getId()));
						
						threadState = threadStateMap.get(Thread.currentThread().getId());
						threadState.setIdle(false);
						threadState.incrementIdleTime(System.nanoTime()-startTime);
						threadState.setLastMessageDispatchTime(startTime);
						threadState.computeIdleTimeBetweenProcessCalls();
					}
				}
			}
		}
		//	This is called every time a request is completed
		public void endProcess( int msgType )
		{
			//	Disregard GetMeta as it comes on a non-process thread
			if ( validMessageForSnapshot( msgType ) )
			{
				synchronized( mux )
				{
					AnalysisThreadState threadState = getThreadState();
					if ( threadState != null ) {
	          threadState.setLastUpdate(System.nanoTime());
	          threadState.setIdle(true);
	          threadState.setLastMessageDispatchTime();
					}
				}
			}
		}
		public long getIdleTimeBetweenProcessCalls(int msgType)
		{
			if ( validMessageForSnapshot( msgType ) )
			{
				synchronized( mux )
				{
					AnalysisThreadState threadState = getThreadState();		
					if ( threadState != null ) {
	          return threadState.getIdleTimeBetweenProcessCalls();
					}
				}
			}
			return 0;
		}
		public long getIdleTime()
		{
			synchronized( mux )
			{
				long now = System.nanoTime();
				long serviceIdleTime = 0;
				Set<Long> set = threadStateMap.keySet();
				int howManyThreads = threadStateMap.size();
				//	Iterate over all processing threads to calculate the total amount of idle time 
				for(Long key: set )
				{	
					//	Retrieve the current thread state information from the global map. The key is
					//	the thread id.
					 AnalysisThreadState threadState = threadStateMap.get(key);
					 //	add this thread idle time
					 serviceIdleTime += threadState.getIdleTime() ;
					 
					 //	If this thread is currently idle, compute amount of time elapsed since the last
					 //	update. The last update has been done at the last startProcess() or endProcess() call.
					 if ( threadState.isIdle())
					 {
						 //	compute idle time since the last update
						 long delta = now - threadState.getLastUpdate();

						 threadState.setLastUpdate(System.nanoTime());

						 //	increment total idle time
						 threadState.incrementIdleTime(delta);
						 //	add the elapsed time since the last update to the total idle time
						 serviceIdleTime += delta;
					 }
				}
				//	If process CAS request has not yet been received, there are not process threads 
				//	created yet. Simply return the delta since the service started. This is a special
				//	case which is only executing if the client has not sent any CASes for processing.
				if ( howManyThreads == 0)
				{
					return System.nanoTime()-startTime;
				}
				else
				{
					//	Return accumulated idle time from all processing threads. Divide the total idle by the 
					//	number of process threads.
					
					if ( this instanceof PrimitiveAnalysisEngineController )
					{
						int aeInstanceCount = ((PrimitiveAnalysisEngineController)this).getAEInstanceCount();
						serviceIdleTime += (aeInstanceCount - howManyThreads)*(System.nanoTime()-startTime);
						return serviceIdleTime/aeInstanceCount;
					}
					else
					{
						return serviceIdleTime;
					}
				}
			}
		}

		/**
		 * Returns CPU Time with nanosecond precision (not nanosecond accuracy). If the OS/JVM
		 * does not support reporting the CPU Time, returns the wall clock time. 
		 */
		public synchronized long getCpuTime() 
		{
			if ( ManagementFactory.getPlatformMBeanServer() != null )
			{
				ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
			    return bean.isCurrentThreadCpuTimeSupported( ) ? bean.getCurrentThreadCpuTime( ) : System.nanoTime();
			}
			return System.nanoTime();
		}
		private synchronized long getCpuTime(long threadId) 
		{
			if ( ManagementFactory.getPlatformMBeanServer() != null )
			{
				ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
			    return bean.isCurrentThreadCpuTimeSupported( ) ? bean.getThreadCpuTime(threadId): System.nanoTime();
			}
			return System.nanoTime();
		}
		
		private AnalysisThreadState getFirstThreadState()
		{
			Set<Long> set = threadStateMap.keySet();
			Iterator<Long> it = set.iterator();
			if ( it.hasNext() ) {
	      return threadStateMap.get(it.next());
			}
			return null;
		}
		/**
		 * Returns the {@link AnalysisThreadState} object associated with the current thread.
		 * 
		 * @return
		 */
		private AnalysisThreadState getThreadState()
		{
			AnalysisThreadState threadState;
			if ( this instanceof AggregateAnalysisEngineController )
			{
				threadState = getFirstThreadState();
			}
			else
			{
				threadState = threadStateMap.get(Thread.currentThread().getId());
				if ( threadState == null )
				{
					//	This may be the case if the thread processing
					//	FreeCASRequest is returning an input CAS to the client.
					//	This thread is different from the process thread, thus
					//	we just return the first thread's state.
					threadState = getFirstThreadState();
				}
			}
			return threadState;
		}
		
		/**
		 * Returns the total CPU time all processing threads spent in analysis.
		 * This method subtracts the serialization and de-serialization time from
		 * the total. If this service is an aggregate, the return time is a sum
		 * of CPU utilization in each colocated delegate.
		 */
		public long getAnalysisTime()
		{
      long totalCpuProcessTime = 0;
      synchronized( mux )
      {
        Set<Long> set = threadStateMap.keySet();
        Iterator<Long> it = set.iterator();
        //  Iterate over all processing threads
        while( it.hasNext())
        {
          long threadId = it.next();
            //  Fetch the next thread's stats
            AnalysisThreadState threadState = threadStateMap.get(threadId);
            //  If an Aggregate service, sum up the CPU times of all collocated
            //  delegates.
            if ( this instanceof AggregateAnalysisEngineController_impl )
            {
              //  Get a list of all colocated delegate controllers from the Aggregate
              List<AnalysisEngineController> delegateControllerList = 
                ((AggregateAnalysisEngineController_impl)this).childControllerList;               
              //  Iterate over all colocated delegates
              for( int i=0; i < delegateControllerList.size(); i++)
              { 
                //  Get the next delegate's controller
                AnalysisEngineController delegateController =
                  (AnalysisEngineController)delegateControllerList.get(i);
                if ( delegateController != null && !delegateController.isStopped())
                {
                  //  get the CPU time for all processing threads in the current controller
                  totalCpuProcessTime += delegateController.getAnalysisTime();
                }
              }
            }
            else  // Primitive Controller
            {
              //  Get the CPU time of a thread with a given ID
              totalCpuProcessTime += getCpuTime(threadId);
              //  Subtract serialization and deserialization times from the total CPU used
              if ( totalCpuProcessTime > 0 )
              {
                totalCpuProcessTime -= threadState.getDeserializationTime();
                totalCpuProcessTime -= threadState.getSerializationTime();
              }
            }
        }
      }
			return totalCpuProcessTime;
		}
		/**
		 * Increments the time this thread spent in serialization of a CAS
		 */
		public void incrementSerializationTime(long cpuTime)
		{
			synchronized( mux )
			{
				AnalysisThreadState threadState = getThreadState();
				if ( threadState != null ) {
	        threadState.incrementSerializationTime(cpuTime);
				}
			}
		}
		/**
		 * Increments the time this thread spent in deserialization of a CAS
		 */
		public void incrementDeserializationTime(long cpuTime)
		{
			synchronized( mux )
			{
				AnalysisThreadState threadState = getThreadState();
				if ( threadState != null ) {
	        threadState.incrementDeserializationTime(cpuTime);
				}
			}
		}
		private class AnalysisThreadState
		{
			private long threadId;
			
			private boolean isIdle = false;
			private long lastUpdate = 0;
			private long totalIdleTime = 0;
			//	Measures idle time between process CAS calls
			private long idleTimeSinceLastProcess = 0;
			private long lastMessageDispatchTime = 0;
			
			private long serializationTime = 0;
			private long deserializationTime = 0;
			
			public AnalysisThreadState( long aThreadId )
			{
				threadId = aThreadId;
			}
			
			public long getThreadId()
			{
				return threadId;
			}
			public long getSerializationTime() {
				return serializationTime;
			}
			public void incrementSerializationTime(long serializationTime) {
				this.serializationTime += serializationTime;
			}
			public long getDeserializationTime() {
				return deserializationTime;
			}
			public void incrementDeserializationTime(long deserializationTime) {
				this.deserializationTime += deserializationTime;
			}
			public boolean isIdle() {
				return isIdle;
			}
			public void computeIdleTimeBetweenProcessCalls()
			{
				idleTimeSinceLastProcess = System.nanoTime() - lastMessageDispatchTime;
			}
			public void setLastMessageDispatchTime( long aTime )
			{
				lastMessageDispatchTime = aTime;
			}
			public void incrementIdleTime( long idleTime )
			{
				totalIdleTime += idleTime;
			}
			public void setIdle(boolean isIdle) {
				this.isIdle = isIdle;
			}
			public long getIdleTime()
			{
				return totalIdleTime;
			}
			public void setLastMessageDispatchTime()
			{
				lastMessageDispatchTime = System.nanoTime();
			}
			public long getIdleTimeBetweenProcessCalls()
			{
				long val = idleTimeSinceLastProcess;
				//	Reset so that only one reply contains a non-zero value
				idleTimeSinceLastProcess = 0;
				return val;
			}
			public long getLastUpdate() {
				return lastUpdate;
			}
			public void setLastUpdate(long lastUpdate) {
				this.lastUpdate = lastUpdate;
			}
			
		}
	  public void sendMetadata(Endpoint anEndpoint, AnalysisEngineMetaData metadata)
	  {
	    try
	    {
	      if ( metadata != null )
	      {
	        
	        if ( !anEndpoint.isRemote())
	        {
	          ByteArrayOutputStream bos = new ByteArrayOutputStream();
	          try
	          {
	            
	            UimaTransport transport = null;
              transport = getTransport(anEndpoint.getEndpoint());
	            UimaMessage message = 
	              transport.produceMessage(AsynchAEMessage.GetMeta,AsynchAEMessage.Response,getName());
	            metadata.toXML(bos);
	            message.addStringCargo(bos.toString());
              transport.getUimaMessageDispatcher(anEndpoint.getEndpoint()).dispatch(message);
	          }
	          catch(Exception e)
	          {
	            e.printStackTrace();
	          }
	          finally
	          {
	            try
	            {
	              bos.close();
	            }
	            catch( Exception e) {}
	          }
	        }
	        else
	        {
	          getOutputChannel().sendReply(metadata, anEndpoint, true);
	        }
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

	  public LocalCache getLocalCache() {
	    return localCache;
	  }
	  public void addAbortedCasReferenceId( String aCasReferenceId )
	  {
	    abortedCasesMap.put(aCasReferenceId, aCasReferenceId);
	  }
	  /**
	   * Returns true if a given CAS id is in the list of aborted CASes.
	   * 
	   * @param aCasReferenceId - id of the current input CAS being processed
	   * 
	   * @return - true if the CAS is in the list of aborted CASes, false otherwise
	   */
	  protected boolean abortGeneratingCASes( String aCasReferenceId )
	  {
	    if ( abortedCasesMap.containsKey(aCasReferenceId)) {
	      abortedCasesMap.remove(aCasReferenceId);
	      return true;
	    } else {
	      return false;
	    }
	  }
	  public boolean isAwaitingCacheCallbackNotification() {
	    return awaitingCacheCallbackNotification;
	  }
	  
}
