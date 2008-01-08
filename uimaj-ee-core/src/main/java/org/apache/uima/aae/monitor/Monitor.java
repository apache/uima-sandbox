package org.apache.uima.aae.monitor;

import java.util.Map;

import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.Statistic;
import org.apache.uima.aae.monitor.statistics.Statistics;

public interface Monitor
{
	public static final String MetadataRequestTimeoutCount = "MetadataRequestTimeoutCount";
	public static final String ProcessRequestTimeoutCount = "ProcessRequestTimeoutCount";
	public static final String ErrorCount = "ErrorCount";
	public static final String ProcessErrorCount = "ProcessErrorCount";
	public static final String TotalProcessErrorCount = "TotalProcessErrorCount";
	public static final String ProcessErrorRetryCount = "ProcessErrorRetryCount";
	public static final String GetMetaErrorCount = "GetMetaErrorCount";
	public static final String GetMetaErrorRetryCount = "GetMetaErrorRetryCount";
	public static final String CpCErrorCount = "CpcErrorCount";
	public static final String ProcessCount = "ProcessCount";
	public static final String IdleTime = "IdleTime";
	public static final String TotalDeserializeTime = "TotalDeserializeTime";
	public static final String TotalSerializeTime = "TotalSerializeTime";
	public static final String TotalAEProcessTime = "TotalAEProcessTime";
	
	public void addStatistic( String key, Statistic aStatistic);
//	public Statistic getStatistic( String key );

	public Statistic getStatistic( String aComponentName, String aStatisticName );
	public Statistics getStatistics( String aComponentName );

	public LongNumericStatistic getLongNumericStatistic( String aComponent, String aStatisticName );
	public void resetCountingStatistic( String aComponent, String aStatisticName );
	public void incrementCount(String aComponent, String aStatisticName );

	public void setThresholds( Map aThresholdMap);
	public Map getThresholds();
	public long componentMapSize();
	public long thresholdMapSize();
	
	
    
}
