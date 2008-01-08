/**
 * 
 */
package org.apache.uima.aae.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.util.Level;


public class AnalysisEngineInstancePoolWithThreadAffinity 
implements AnalysisEngineInstancePool
{
	private static final Class CLASS_NAME = AnalysisEngineInstancePoolWithThreadAffinity.class;

	private boolean allThreadsAlreadyAssigned = false;
	private Map aeInstanceMap = new HashMap();
	private List aeList = new ArrayList();
	private int analysisEnginePoolSize = 0;
	
	public AnalysisEngineInstancePoolWithThreadAffinity( int aePoolSize )
	{
		analysisEnginePoolSize = aePoolSize;
	}
	/* (non-Javadoc)
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#intialize(java.util.List)
	 */
	public void intialize(List anAnalysisEngineInstanceList) throws Exception
	{
		aeList = anAnalysisEngineInstanceList;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#checkin(org.apache.uima.analysis_engine.AnalysisEngine)
	 */
	public void checkin(AnalysisEngine anAnalysisEngine) throws Exception
	{
		// 
	}

	/**
	 * Pins each process thread to a specific and dedicated AE instance.
	 * All AE instances are managed in a HashMap with thread name as a key.
	 * AE instance is not removed from the HashMap before it is returned to
	 * the client. 
	 *  
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#checkout()
	 **/
	public AnalysisEngine checkout() throws Exception
	{
		AnalysisEngine ae = null;
		
		//	AEs are instantiated and initialized in the the main thread and placed in the temporary list.
		//	First time in the process() method, each thread will remove AE instance from the temporary list
		//	and place it in the permanent instanceMap. The key to the instanceMap is the thread name. Each
		//	thread will always process a CAS using its own and dedicated AE instance.
		if ( !allThreadsAlreadyAssigned )
		{
			if ( !aeInstanceMap.containsKey(Thread.currentThread().getName()) )
			{
				//	Remove an instance of the AE from the temporary list
				ae = (AnalysisEngine)aeList.remove(0);
				//	Associate thread name with AE instance
				aeInstanceMap.put(Thread.currentThread().getName(), ae);
				if ( aeInstanceMap.size() == analysisEnginePoolSize)
				{
					allThreadsAlreadyAssigned = true;
				}
			}
		}
		//	ae may have been assigned above already, no need to fetch it again
		if ( ae == null )
		{
			//	Fetch ae instance from the map using thread name as key. This mechanism assures that a thread
			//	uses the same ae instance every time.
			ae = (AnalysisEngine)aeInstanceMap.get(Thread.currentThread().getName()) ;
		}
		return ae;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#destroy()
	 */
	public void destroy() throws Exception
	{

		Iterator aeInstanceIterator = aeInstanceMap.keySet().iterator();
		int i=0;
		while ( aeInstanceIterator.hasNext() )
		{
			AnalysisEngine ae = (AnalysisEngine) aeInstanceMap.get((String)aeInstanceIterator.next());
			ae.destroy();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "abort", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_destroying_ae__INFO", new Object[] { ae.getAnalysisEngineMetaData().getName(), i });
			i++;
		}
		aeInstanceMap.clear();
	}


}
