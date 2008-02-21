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

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Formatter;


public class ServicePerformance implements ServicePerformanceMBean
{
	private static final long serialVersionUID = 1L;
	private static final String label="Service Performance";
	private long accumulatedIdleTime=0;
	private long numberOfCASesProcessed=0;
	private long casDeserializationTime=0;
	private long casSerializationTime=0;
	private long analysisTime=0;
	private long maxSerializationTime=0;
	private long maxDeserializationTime=0;
	private long maxAnalysisTime=0;
	
	private Object sem = new Object();

	public String getLabel()
	{
		return label;
	}

	public synchronized void reset()
	{
		accumulatedIdleTime = 0;
		numberOfCASesProcessed=0;
		casDeserializationTime=0;
		casSerializationTime=0;
		analysisTime=0;
	}
	public double getIdleTime()
	{
		if ( accumulatedIdleTime != 0)
			synchronized( sem )
			{
				return((double)accumulatedIdleTime/(double) 1000000);
			}
		else
			return 0;
	}

	public long getRawIdleTime()
	{
		return accumulatedIdleTime;
	}
	public void incrementIdleTime(long anIdleTime)
	{
		synchronized( sem )
		{
			accumulatedIdleTime += anIdleTime;
		}
	}

	public void incrementAnalysisTime( long anAnalysisTime )
	{
		synchronized(sem)
		{
			if ( maxAnalysisTime < anAnalysisTime )
			{
				maxAnalysisTime = anAnalysisTime;
			}
			analysisTime += anAnalysisTime;
		}
	}
	
	public double getAnalysisTime()
	{
		return (double)analysisTime/(double)1000000;
	}
	
	public long getRawAnalysisTime()
	{
		return analysisTime;
	}

	public long getNumberOfCASesProcessed()
	{
		return numberOfCASesProcessed;
	}

	public synchronized void incrementNumberOfCASesProcessed()
	{
		numberOfCASesProcessed++;
	}

	public synchronized double getCasDeserializationTime()
	{
		if ( casDeserializationTime > 0 )
		{
			return ((double)casDeserializationTime/(double) 1000000);
		}
		return 0.0;
	}
	public synchronized long getRawCasDeserializationTime()
	{
		return casDeserializationTime;
	}
	public synchronized void incrementCasDeserializationTime(long aCasDeserializationTime)
	{
		if ( maxDeserializationTime < aCasDeserializationTime )
		{
			maxDeserializationTime = aCasDeserializationTime;
		}
		casDeserializationTime += aCasDeserializationTime;
	}
	public double getCasSerializationTime()
	{
		return ((double)casSerializationTime/(double) 1000000);
	}
	public long getRawCasSerializationTime()
	{
		return casSerializationTime;
	}
	public synchronized void incrementCasSerializationTime(long casSerializationTime)
	{
		if ( maxSerializationTime < casSerializationTime )
		{
			maxSerializationTime = casSerializationTime;
		}
		this.casSerializationTime += casSerializationTime;
	}
	
	public double getMaxSerializationTime()
	{
		return (double)maxSerializationTime/(double)1000000;
	}
	public double getMaxDeserializationTime()
	{
		return (double)maxDeserializationTime/(double)1000000;
	}
	
	public double getMaxAnalysisTime()
	{
		return (double)maxAnalysisTime / (double)1000000;
	}
}
