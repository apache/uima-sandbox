package org.apache.uima.aae.monitor.statistics;

public abstract class NumericStatisticBaseImpl 
extends BaseStatistic implements NumericStatistic
{
	public NumericStatisticBaseImpl(String aName )
	{
		super(aName);
	}
	
	public abstract void increment();
	
	public abstract void reset();
	
	public abstract void decrement();

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
