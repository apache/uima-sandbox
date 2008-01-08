package org.apache.uima.aae;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.impl.CasManager_impl;

public class EECasManager_impl extends CasManager_impl
{
	Map casPoolMap = new HashMap();
	protected long initialCasHeapSize = 0;
	
	public EECasManager_impl(ResourceManager aResourceManager)
	{
		super(aResourceManager);
	}
	public void setInitialCasHeapSize( long anInitialCasHeapSize )
	{
		//	Heap size is defined in terms of bytes. Uima core expects number of cells.
		//	Each cell is 4 bytes. Divide heapSize expressed in bytes by 4.
		initialCasHeapSize = anInitialCasHeapSize/4;
	}
	public void defineCasPool(String aRequestorContextName, int aMinimumSize, Properties aPerformanceTuningSettings)
	throws ResourceInitializationException
	{
		if ( aPerformanceTuningSettings == null && initialCasHeapSize > 0 )
		{
			Properties performanceTuningSettings = new Properties();
			performanceTuningSettings.setProperty(UIMAFramework.CAS_INITIAL_HEAP_SIZE, 
					new Integer((int)initialCasHeapSize).toString() );
			super.defineCasPool(aRequestorContextName, aMinimumSize, performanceTuningSettings);
		}
		else
		{
			super.defineCasPool(aRequestorContextName, aMinimumSize, aPerformanceTuningSettings);
		}
	}
	public void setPoolSize(String aRequestorContextName, int aSize )
	{
		casPoolMap.put(aRequestorContextName, aSize);
		
	}
	public int getCasPoolSize(String aRequestorContextName, int aMinimumSize) 
	{
		int theSize = aMinimumSize;
    if ( casPoolMap.containsKey(aRequestorContextName) )
    {
       theSize += ((Integer)casPoolMap.get(aRequestorContextName)).intValue() - 1;
    }
    return theSize;
	}
protected void finalize() throws Throwable
{
	super.finalize();
	getCasToCasPoolMap().clear();
	casPoolMap.clear();
}


}
