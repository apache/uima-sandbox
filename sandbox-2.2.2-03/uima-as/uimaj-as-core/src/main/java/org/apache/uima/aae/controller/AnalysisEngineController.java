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

import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.aae.AsynchAECasManager;
import org.apache.uima.aae.InProcessCache;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.OutputChannel;
import org.apache.uima.aae.UimaEEAdminContext;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandlerChain;
import org.apache.uima.aae.jmx.JmxManagement;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;


public interface AnalysisEngineController extends ControllerLifecycle
{
	public static final String CasPoolSize = "CasPoolSize";
	public static final String AEInstanceCount = "AEInstanceCount";
	public void sendMetadata(Endpoint anEndpoint) throws AsynchAEException;
	public ControllerLatch getControllerLatch();
	public void setInputChannel(InputChannel anInputChannel ) throws Exception;

	public void addInputChannel(InputChannel anInputChannel ) throws Exception;

	public String getServiceEndpointName();
	
	public void handleDelegateLifeCycleEvent( String anEndpoint, int aDelegateCount);
	
	public void takeAction( String anAction, String anEndpointName, ErrorContext anErrorContext);  

	public InputChannel getInputChannel();
	
	public long getIdleTime( String aKey );
	
	public void saveIdleTime( long snapshot, String aKey, boolean accumulate );

	public void saveReplyTime( long snapshot, String aKey );
	
	public long getReplyTime();
	
	public Map getStats();
	
	public UimaContext getChildUimaContext( String aDelegateEndpointName ) throws Exception;
	
	public void dropCAS( String aCasReferenceId, boolean dropCacheEntry );
	
	public void dropCAS( CAS aCAS );
	
	public InProcessCache getInProcessCache();

	public boolean isPrimitive();
	
	public Monitor getMonitor();
	
	public String getName();
	
	public String getComponentName();
	
	public void collectionProcessComplete(Endpoint anEndpoint) throws AsynchAEException;

	public boolean isTopLevelComponent();
	
	public void initialize() throws AsynchAEException;
	
	public void process( CAS aCas, String aCasId );// throws AnalysisEngineProcessException, AsynchAEException;
	
	public void process(CAS aCAS, String anInputCasReferenceId, String aNewCasReferenceId, String newCASProducedBy); //throws AnalysisEngineProcessException, AsynchAEException;

	public void process( CAS aCAS, String aCasReferenceId, Endpoint anEndpoint );// throws AnalysisEngineProcessException, AsynchAEException;
	
	public void saveTime(long anArrivalTime, String aCasReferenceId, String anEndpointName );
	
	public long getTime(String aCasReferenceId, String anEndpointName );
	
	public ErrorHandlerChain getErrorHandlerChain();
	
	public void setOutputChannel( OutputChannel anOutputChannel ) throws Exception;
	
	public OutputChannel getOutputChannel();
	
	public void setCasManager( AsynchAECasManager aCasManager);
	
	public AsynchAECasManager getCasManagerWrapper();
	
	public void stop();
	
	/**
	 * Returns true if the AnalysisEngineController has been (or is in the process of)
	 * shutdown.
	 * 
	 * @return - true if stopped
	 */
	public boolean isStopped();
	/**
	 * Called to set the state of the AnalysisEngineController to STOPPED. 
	 * This method does not stop input or output channels. 
	 */
	public void setStopped();	
	
	
	public void dropStats( String aCasReferenceId, String anEndpointName );

	public void setUimaEEAdminContext( UimaEEAdminContext anAdminContext );
	
	public UimaEEAdminContext getUimaEEAdminContext();

	public String getJMXDomain();

	public int getIndex();
	
	public String getJmxContext();
	
	public void addTimeSnapshot( long snapshot, String aKey );
	
	public ServicePerformance getServicePerformance();
	
	public long getTimeSnapshot( String aKey );

	public void addServiceInfo( ServiceInfo aServiceInfo );

	public ServiceErrors getServiceErrors();

	public void setDeployDescriptor( String aDeployDescriptor );
	
	public void cacheClientEndpoint(Endpoint anEndpoint);

	public Endpoint getClientEndpoint();
	
	public EventSubscriber getEventListener();

	public JmxManagement getManagementInterface();
	
    public void notifyListenersWithInitializationStatus(Exception e);
  
	public ServicePerformance getCasStatistics( String aCasReferenceId );

}
