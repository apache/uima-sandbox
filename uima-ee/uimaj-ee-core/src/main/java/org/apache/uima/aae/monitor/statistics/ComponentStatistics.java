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
