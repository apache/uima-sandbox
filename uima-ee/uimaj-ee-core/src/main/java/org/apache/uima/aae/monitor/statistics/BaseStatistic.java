package org.apache.uima.aae.monitor.statistics;

public abstract class BaseStatistic //implements Statistic
{
	private String name;

	public BaseStatistic( String aName )
	{
		name = aName;
	}
	public String getName()
	{
		return name;
	}


}
