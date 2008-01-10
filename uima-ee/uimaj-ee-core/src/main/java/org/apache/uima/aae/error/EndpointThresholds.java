package org.apache.uima.aae.error;

import java.util.Iterator;
import java.util.Map;

public class EndpointThresholds
{
	private Map thresholdMap;
	
	public EndpointThresholds( Map aThresholdMap )
	{
		thresholdMap = aThresholdMap;
	}
	
	public Iterator getIterator()
	{
		if ( thresholdMap != null )
		{
			return thresholdMap.keySet().iterator();
		}
		return null;
	}
	
	public Threshold getThreshold( Object key )
	{
		if ( thresholdMap.containsKey(key))
		{
			return (Threshold)thresholdMap.get(key);
		}
		return null;
	}
	public void addThreshold( Object key, Threshold aThreshold )
	{
		if ( !thresholdMap.containsKey(key) )
		{
			thresholdMap.put(key, aThreshold);
		}
	}

}
