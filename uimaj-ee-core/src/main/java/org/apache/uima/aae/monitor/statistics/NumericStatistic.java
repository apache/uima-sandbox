package org.apache.uima.aae.monitor.statistics;

public interface NumericStatistic extends Statistic
{
	public void increment();
	public void decrement();

}
