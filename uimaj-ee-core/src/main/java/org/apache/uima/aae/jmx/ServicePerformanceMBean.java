package org.apache.uima.aae.jmx;

import java.io.Serializable;


public interface ServicePerformanceMBean extends Serializable
{
	public double getIdleTime();
	
	public void reset();
	
	public long getNumberOfCASesProcessed();
	public double getCasDeserializationTime();
	public double getCasSerializationTime();
	
}
