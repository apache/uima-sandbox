package org.apache.uima.aae.monitor.statistics;

import java.io.Serializable;

public interface ComponentStatisticsMBean extends Serializable
{
	public void reset();
	
	public long getNumberOfCASesProcessed();
	
	public long getNumberOfProcessErrors();
}
