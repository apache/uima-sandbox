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

import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.jmx.ServiceInfo;

public interface Endpoint
{
	public int getMetadataRequestTimeout();
	public void setController( AnalysisEngineController aController);
	public void startCheckpointTimer();
	public long getCheckpointTimer();
	
	public boolean isRetryEnabled();
	public void setMetadataRequestTimeout(int metadataRequestTimeout);

	public int getProcessRequestTimeout();

	public void setReplyEndpoint(boolean tORf );

	public boolean isReplyEndpoint();

	public void setProcessRequestTimeout(int processRequestTimeout);

	public boolean completedProcessingCollection();
	
	public void setCompletedProcessingCollection(boolean completed);
	
	public void setNoConsumers(boolean trueOrFalse);
	
	public boolean hasNoConsumers();
	
	public boolean isInitialized();

	public void setInitialized(boolean initialized);

	public String getEndpoint();
	
	public boolean isFinal();
	public void setFinal(boolean isFinal);
	
	public long getEntryTime();

	public void setEndpoint(String endpoint);

	public String getServerURI();

	public void setServerURI(String serverURI);
	
	public void startMetadataRequestTimer();
	
	public void startCollectionProcessCompleteTimer();

	public void startProcessRequestTimer(String aCasReferenceId);
	
	public void cancelTimer();

	public boolean isWaitingForResponse();
	
	public void setWaitingForResponse(boolean isWaiting);
	
	public void initialize() throws AsynchAEException;
	
	public void setDescriptor( String aDescriptor );
	
	public String getDescriptor();
	
	public void setRemote( boolean aRemote) ;

	public String getReplyToEndpoint();
	
	public boolean isRemote();
	
	public String getSerializer();
	
	public void close();
	
	public boolean isOpen();
	
	public void setHighWaterMark( String aHighWaterMark );
	
	public String getHighWaterMark();
	
	public boolean remove();
	
	public void setRemove(boolean removeIt);

	public boolean isCasMultiplier();
	
	public void setIsCasMultiplier(boolean trueORfalse);
	
	public void setShadowCasPoolSize( int aPoolSize );
	
	public int getShadowPoolSize();
	
	public ServiceInfo getServiceInfo();

	public Object getDestination();
	
	public void setDestination( Object aDestination);

	public void setCommand( int aCommand );
	
	public int getCommand();
	
	public void setRegisteredWithParent();
	
	public boolean isRegisteredWithParent();
	
	public void setInitialFsHeapSize(int aHeapSize);
	
	
}
