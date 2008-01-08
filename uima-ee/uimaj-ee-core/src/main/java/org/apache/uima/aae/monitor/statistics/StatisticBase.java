package org.apache.uima.aae.monitor.statistics;

import java.util.HashMap;
import java.util.Map;

public class StatisticBase implements Statistic
{
	private Map statMap = new HashMap();

	public void add(Object key, Object value)
	{
		statMap.put( key, value);
	}
	
	public void reset()
	{
		
	}

	public Object get(Object key)
	{
		if ( statMap.containsKey(key))
		{
			return statMap.get(key);
		}
		return null;
	}

	public synchronized void increment(Object key)
	{
		if ( statMap.containsKey(key) )
		{
			Long count = (Long)statMap.get(key);
			count++;
			statMap.remove(key);
			add(key, count);
		}
		
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}
	

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
