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

import java.util.Formatter;

/**
 * This class exposes client side statistics via JMX
 *
 */
public class UimaASClientInfo implements
		UimaASClientInfoMBean {

	
	private String applicationName;
	private long idleTime;
	private long serializationTime;
	private long deserializationTime;
	private long totalCasesProcessed;
	private long totalProcessTime;
	private long maxSerializationTime;
	private long maxDeserializationTime;
	private long maxIdleTime;
	private long maxProcessTime;
	private String endpointName;
	private int replyWindowSize;
	private int casPoolSize;
	private long _metaTimeoutErrorCount;
	private long _processTimeoutErrorCount;
	private long _processErrorCount;
	private long _metaErrorCount;
	
	public void reset()
	{
		applicationName="";
		idleTime = 0;
		serializationTime=0;
		deserializationTime=0;
		totalCasesProcessed=0;
		totalProcessTime=0;
		maxSerializationTime=0;
		maxDeserializationTime=0;
		maxIdleTime=0;
		maxProcessTime=0;
		endpointName="";
		replyWindowSize=0;
		casPoolSize=0;
		_metaTimeoutErrorCount=0;
		_processTimeoutErrorCount=0;
		_processErrorCount=0;
		_metaErrorCount=0;
	}
    public long getMetaErrorCount()
    {
    	return _metaErrorCount;
    }
    public void incrementMetaErrorCount()
    {
    	_metaErrorCount++;
    }

	public long getMetaTimeoutErrorCount() {
		return _metaTimeoutErrorCount;
	}

	public void incrementMetaTimeoutErrorCount() {
		_metaTimeoutErrorCount++;
	}

	public long getProcessTimeoutErrorCount() {
		return _processTimeoutErrorCount;
	}

	public void incrementProcessTimeoutErrorCount() {
		_processTimeoutErrorCount++;
	}

	public long getProcessErrorCount() {
		return _processErrorCount;
	}

	public void incrementProcessErrorCount() {
		_processErrorCount++;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public int getReplyWindowSize() {
		return replyWindowSize;
	}

	public void setReplyWindowSize(int replyWindowSize) {
		this.replyWindowSize = replyWindowSize;
	}

	public int getCasPoolSize() {
		return casPoolSize;
	}

	public void setCasPoolSize(int casPoolSize) {
		this.casPoolSize = casPoolSize;
	}

	public UimaASClientInfo()
	{
		
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	private String format(float aValue )
	{
		Formatter formatter  = new Formatter();
		formatter.format ("%,.2f ms", aValue);
		return formatter.out().toString();
	}
	public String getAverageDeserializationTime() {
		if ( totalCasesProcessed > 0 )
		{
			float floatValue = (float)((float)deserializationTime/(float)totalCasesProcessed)/(float)1000000;
			return format(floatValue);
		}
		else
			return "0.0";
	}

	public String getAverageIdleTime() {
		if ( totalCasesProcessed > 0 )
		{
			float floatValue =  (float)((float)idleTime/(float)totalCasesProcessed)/(float)1000000;
			return format(floatValue);
		}
		else
			return "0.0";
	}

	public String getAverageSerializationTime() {
		if ( totalCasesProcessed > 0 )
		{
			float floatValue = (float)((float)serializationTime/(float)totalCasesProcessed)/(float)1000000;
			return format(floatValue);
		}
		else
			return "0.0";
	}

	public String getAverageTimeToProcessCas() {
		if ( totalCasesProcessed > 0 )
		{
			float floatValue = (float)((float)totalProcessTime/(float)totalCasesProcessed)/(float)1000000;
			return format(floatValue);
		}
		else
			return "0.0";
	}

	
	public String getMaxDeserializationTime() {
		return format((float)maxDeserializationTime/(float)1000000);
	}

	public String getMaxIdleTime() {
		return format((float)maxIdleTime/(float)1000000);
	}

	public String getMaxProcessTime() {
		return format((float)maxProcessTime/(float)1000000);
	}

	public String getMaxSerializationTime() {
		return format((float)maxSerializationTime/(float)1000000);
	}

	public String getTotalDeserializationTime() {
		return format((float)deserializationTime/(float)1000000);
	}

	public String getTotalIdleTime() {
		return format((float)idleTime/(float)1000000);
	}

	public long getTotalNumberOfCasesProcessed() {
		return totalCasesProcessed;
	}

	public String getTotalSerializationTime() {
		return format((float)serializationTime/(float)1000000);
	}

	public String getTotalTimeToProcess() {
		return format((float)totalProcessTime/(float)1000000);
	}

	public void setApplicationName(String anApplicationName) {
		applicationName = anApplicationName;
	}

	public void incrementTotalIdleTime(long anIdleTime) {
		if ( maxIdleTime < anIdleTime )
		{
			maxIdleTime = anIdleTime;
		}
		idleTime += anIdleTime;
	}

	public void incrementTotalNumberOfCasesProcessed() {
		totalCasesProcessed++;
	}

	public void incrementTotalSerializationTime(long aSerializationTime) {
		
		if ( maxSerializationTime < aSerializationTime )
		{
			maxSerializationTime = aSerializationTime;
		}
		serializationTime += aSerializationTime;
	}

	public void incrementTotalTimeToProcess(long aTimeToProcess) {
		if ( maxProcessTime < aTimeToProcess )
		{
			maxProcessTime = aTimeToProcess;
		}
		totalProcessTime += aTimeToProcess;
	}

	public void incrementTotalDeserializationTime(long aDeserializationTime) {
		if ( maxDeserializationTime < aDeserializationTime )
		{
			maxDeserializationTime = aDeserializationTime;
		}
		deserializationTime += aDeserializationTime;
	}
}
