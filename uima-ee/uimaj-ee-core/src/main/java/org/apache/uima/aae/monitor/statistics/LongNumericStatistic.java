package org.apache.uima.aae.monitor.statistics;

public class LongNumericStatistic extends NumericStatisticBaseImpl
implements LongNumericStatisticMBean
//implements NumericStatisticBean
{
	private long value=0;
	
	public LongNumericStatistic( String aName)
	{
		super( aName);
	}

	public void decrement()
	{
		if ( value > 0 )
		{
			value--;
		}
	}

	public void increment()
	{
		value++;
	}

	public void increment(long anIncrementBy)
	{
		value += anIncrementBy;
	}

	public void reset()
	{
		value = 0;
	}

	public long getValue()
	{
		return value;
	}

private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
