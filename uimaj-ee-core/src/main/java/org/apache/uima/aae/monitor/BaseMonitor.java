package org.apache.uima.aae.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.aae.monitor.statistics.Statistic;
import org.apache.uima.aae.monitor.statistics.Statistics;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import com.sun.jmx.remote.security.MBeanServerAccessController;

public class BaseMonitor implements Monitor
{
	private Map monitorMap = new HashMap();
	private MBeanServerConnection server;
	private Map thresholds = null;
	

	public BaseMonitor( String jmxServerURI ) throws Exception
	{
		JMXServiceURL url = new JMXServiceURL (jmxServerURI) ;
		JMXConnector connector = JMXConnectorFactory.connect(url);
		server =connector.getMBeanServerConnection();
		
	}

	public void setThresholds( Map aThresholdMap)
	{
		thresholds = aThresholdMap;
	}
	public Map getThresholds()
	{
		return thresholds;
	}
	
	public void addStatistic(String aName, Statistic aStatistic, Endpoint anEndpoint)
	{
		
//		if ( aStatistic.isJMXEnabled())
//		{
//			//server.
//		}
		
	}

	public Statistic getStatistic(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public void addStatistic(String key, Statistic aStatistic)
	{
		// TODO Auto-generated method stub
		
	}
	public LongNumericStatistic getLongNumericStatistic(String aComponent, String aStatisticName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Statistic getStatistic(String aComponentName, String aStatisticName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Statistics getStatistics(String aComponentName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public void incrementCount(String aComponent, String aStatisticName)
	{
		// TODO Auto-generated method stub
		
	}
	public void resetCountingStatistic(String aComponent, String aStatisticName)
	{
		// TODO Auto-generated method stub
		
	}
	public long componentMapSize()
	{
		return 0;
	}
	
	public long thresholdMapSize()
	{
		return 0;
	}


}
