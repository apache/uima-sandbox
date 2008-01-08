package org.apache.uima.aae.monitor.statistics;

public class StringStatisticImpl extends BaseStatistic implements StringStatistic
{
	private String value;
	
	public StringStatisticImpl( String aName, String anInitialValue)
	{
		super(aName);
		value = anInitialValue;
	}
	public String getValue()
	{
		return value;
	}

	public void setValue( String aValue)
	{
		value = aValue;
	}
	public void reset()
	{
		value = "";
	}


private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
