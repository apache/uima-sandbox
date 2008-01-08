package org.apache.uima.aae.monitor.statistics;

public class IntNumericStatistic extends BaseStatistic 
implements NumericStatistic
{
	private int value;
	
	public IntNumericStatistic( String aName)
	{
		super( aName);
	}

	public IntNumericStatistic( String aName, int anInitialValue)
	{
		super( aName);
		value = anInitialValue;
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

	public void reset()
	{
		value = 0;
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
