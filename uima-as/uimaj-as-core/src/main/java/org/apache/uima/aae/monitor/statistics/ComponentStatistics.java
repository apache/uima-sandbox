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

package org.apache.uima.aae.monitor.statistics;

import org.apache.uima.aae.monitor.Monitor;

public class ComponentStatistics implements ComponentStatisticsMBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Statistics statistics;
	
	public ComponentStatistics()
	{
	}

	public ComponentStatistics( Statistics aStatistics )
	{
		statistics = aStatistics;
	}
	private Statistic getStatistic( String aName)
	{
		if ( statistics == null )
		{
			return null;
		}
		return (Statistic)statistics.get(aName);
		
		
	}
	public long getNumberOfCASesProcessed()
	{
		Statistic statistic = getStatistic(Monitor.ProcessCount);
		if (statistic != null && statistic instanceof LongNumericStatistic)
		{
			return ((LongNumericStatistic)statistic).getValue();
		}
		return 0;
	}

	public long getNumberOfProcessErrors()
	{
		Statistic statistic = getStatistic(Monitor.TotalProcessErrorCount);
		if (statistic != null && statistic instanceof LongNumericStatistic)
		{
			return ((LongNumericStatistic)statistic).getValue();
		}
		return 0;
	}

	public void reset()
	{
		reset( getStatistic(Monitor.ProcessCount) );
		reset( getStatistic(Monitor.TotalProcessErrorCount) );
	}
	
	private void reset( Statistic aStatistic )
	{
		if (aStatistic != null && aStatistic instanceof LongNumericStatistic)
		{
			((LongNumericStatistic)aStatistic).reset();
		}
		
	}
}
