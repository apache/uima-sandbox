package org.apache.uima.aae.monitor.statistics;

import java.io.Serializable;

public interface LongNumericStatisticMBean extends Serializable
{
	public long getValue();
	
	public String getName();
	
	public void reset();

}
