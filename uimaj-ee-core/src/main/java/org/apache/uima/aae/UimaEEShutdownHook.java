package org.apache.uima.aae;

import java.util.ArrayList;

import org.apache.uima.aae.jmx.JmxManager;

public class UimaEEShutdownHook extends Thread
{
	private JmxManager[] jmxMgrs;
	private String serviceName;
	
	public UimaEEShutdownHook( ArrayList jmxManagers, String aServiceName )
	{
		jmxMgrs = new JmxManager[jmxManagers.size()];
		jmxManagers.toArray(jmxMgrs);
		serviceName = aServiceName;
	}
	public void run() 
	{
//        try
//        {
//        	for( int i=0; jmxMgrs!= null && i < jmxMgrs.length; i++ )
//        	{
//            	jmxMgrs[i].removeQueue();
//        	}
//        }
//        catch( Exception e)
//        {
//        	e.printStackTrace();
//        }
	}



}
