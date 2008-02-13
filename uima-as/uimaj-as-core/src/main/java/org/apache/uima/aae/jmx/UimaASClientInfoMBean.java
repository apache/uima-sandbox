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

package org.apache.uima.aae.jmx;

public interface UimaASClientInfoMBean {

	public void setApplicationName( String anApplicationName );
	public String getApplicationName();

	public void incrementTotalNumberOfCasesProcessed();
	public long getTotalNumberOfCasesProcessed();
	
	public String getEndpointName();
	public void setEndpointName(String endpointName);

	public int getReplyWindowSize();
	public void setReplyWindowSize(int replyWindowSize);

	public int getCasPoolSize();
	public void setCasPoolSize(int casPoolSize);


	public void incrementTotalTimeToProcess( long aTotalTimeToProcess );
	public String getTotalTimeToProcess();
	public String getAverageTimeToProcessCas();
	
	public void incrementTotalSerializationTime( long aTotalSerializationTime );
	public String getTotalSerializationTime();
	public String getAverageSerializationTime();
	
	public void incrementTotalDeserializationTime( long aTotalDeserializationTime );
	public String getTotalDeserializationTime();
	public String getAverageDeserializationTime();
	
	public void incrementTotalIdleTime( long aTotalIdleTime );
	public String getTotalIdleTime();
	public String getAverageIdleTime();

    public String getMaxProcessTime();
    public String getMaxSerializationTime();
    public String getMaxDeserializationTime();
    public String getMaxIdleTime();
}
