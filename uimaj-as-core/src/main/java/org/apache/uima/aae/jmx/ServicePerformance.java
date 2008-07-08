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

import org.apache.uima.aae.controller.AnalysisEngineController;


public class ServicePerformance implements ServicePerformanceMBean
{
	private static final long serialVersionUID = 1L;
	private static final String label="Service Performance";
	private long idleTime=0;
	private long numberOfCASesProcessed=0;
	private long casDeserializationTime=0;
	private long casSerializationTime=0;
	private long analysisTime=0;
	private long maxSerializationTime=0;
	private long maxDeserializationTime=0;
	private long maxAnalysisTime=0;
	private long casPoolWaitTime=0;
	private long shadowCasPoolWaitTime=0;
	private long timeSpentInCMGetNext = 0;
	private Object sem = new Object();
	private AnalysisEngineController controller;
	private boolean isRemoteDelegate = false;
	private long uptime = System.nanoTime();
	long lastUpdate=System.nanoTime();
	public ServicePerformance()
	{
	}

	public ServicePerformance(AnalysisEngineController aController)
	{
		controller = aController;
	}
	
	public void setRemoteDelegate()
	{
		isRemoteDelegate = true;
	}
	public String getLabel()
	{
		return label;
	}

	public synchronized void reset()
	{
		idleTime = 0;
		numberOfCASesProcessed=0;
		casDeserializationTime=0;
		casSerializationTime=0;
		casPoolWaitTime = 0;
		shadowCasPoolWaitTime=0;
		analysisTime=0;
		maxSerializationTime=0;
		maxDeserializationTime=0;
		maxAnalysisTime=0;
		timeSpentInCMGetNext = 0;
		uptime = System.nanoTime();
	}
	
	
	public void setIdleTime( long anIdleTime )
	{
		synchronized( sem )
		{
			idleTime = anIdleTime;
		}
	}
	public double getIdleTime()
	{
		
		if ( controller != null )
		{
			//	Force update of the idle time
			return ((double)controller.getIdleTime()/(double) 1000000);
		}
		else
		{
			synchronized( sem )
			{
				return ((double)idleTime/(double) 1000000);
			}
			
		}
	}

	public long getRawIdleTime()
	{
		return idleTime;
	}
	public void incrementIdleTime(long anIdleTime)
	{
		synchronized( sem )
		{
			idleTime += anIdleTime;
			lastUpdate = System.nanoTime();
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
	public void incrementCasPoolWaitTime(long aCasPoolsWaitTime )
	{
		synchronized (sem ) 
		{
			casPoolWaitTime += aCasPoolsWaitTime;
		}
	}
	public double getCasPoolWaitTime()
	{
		return (double)casPoolWaitTime/(double)1000000;
	}
	public void incrementShadowCasPoolWaitTime(long aShadowCasPoolWaitTime)
	{
		shadowCasPoolWaitTime += aShadowCasPoolWaitTime;
	}
	public double getShadowCasPoolWaitTime()
	{
		return (double)shadowCasPoolWaitTime/(double)1000000;
	}
	
	public void incrementTimeSpentInCMGetNext(long aTimeSpentInCMGetNext )
	{
		timeSpentInCMGetNext += aTimeSpentInCMGetNext;
	}
	public double getTimeSpentInCMGetNext()
	{
		return (double)timeSpentInCMGetNext/(double)1000000;
	}

}
