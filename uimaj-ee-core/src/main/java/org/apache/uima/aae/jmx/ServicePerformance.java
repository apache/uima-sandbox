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
	
	private Object sem = new Object();

	public String getLabel()
	{
		return label;
	}

	public void reset()
	{
		accumulatedIdleTime = 0;
		numberOfCASesProcessed=0;
		casDeserializationTime=0;
		casSerializationTime=0;
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

	public void incrementIdleTime(long anIdleTime)
	{
		synchronized( sem )
		{
			accumulatedIdleTime += anIdleTime;
		}
	}

	public long getNumberOfCASesProcessed()
	{
		return numberOfCASesProcessed;
	}

	public synchronized void incrementNumberOfCASesProcessed()
	{
		numberOfCASesProcessed++;
	}

	public double getCasDeserializationTime()
	{
		if ( casDeserializationTime > 0 )
		{
			return ((double)casDeserializationTime/(double) 1000000);
		}
		return 0.0;
	}

	public synchronized void incrementCasDeserializationTime(long aCasDeserializationTime)
	{
		casDeserializationTime += aCasDeserializationTime;
	}
	public double getCasSerializationTime()
	{
		return casSerializationTime;
	}

	public synchronized void incrementCasSerializationTime(long casSerializationTime)
	{
		this.casSerializationTime += casSerializationTime;
	}
	
}
