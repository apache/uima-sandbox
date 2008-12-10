/**
 * 
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
	public int size() {
	  return aeInstanceMap.size();
	}
	/* (non-Javadoc)
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#checkin(org.apache.uima.analysis_engine.AnalysisEngine)
	 */
	public synchronized void checkin(AnalysisEngine anAnalysisEngine) throws Exception
	{
		aeInstanceMap.put(Thread.currentThread().getId(), anAnalysisEngine);
	}

	public boolean exists() {
	  return aeInstanceMap.containsKey(Thread.currentThread().getId());
	}
	/**
	 * Pins each process thread to a specific and dedicated AE instance.
	 * All AE instances are managed in a HashMap with thread name as a key.
	 * AE instance is not removed from the HashMap before it is returned to
	 * the client. 
	 *  
	 * @see org.apache.uima.aae.controller.AnalysisEngineInstancePool#checkout()
	 **/
	public synchronized AnalysisEngine checkout() throws Exception
	{
		AnalysisEngine ae = null;
		
		//	AEs are instantiated and initialized in the the main thread and placed in the temporary list.
		//	First time in the process() method, each thread will remove AE instance from the temporary list
		//	and place it in the permanent instanceMap. The key to the instanceMap is the thread name. Each
		//	thread will always process a CAS using its own and dedicated AE instance.
    return (AnalysisEngine)aeInstanceMap.remove(Thread.currentThread().getId()) ;
		
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
			AnalysisEngine ae = (AnalysisEngine) aeInstanceMap.get((Long)aeInstanceIterator.next());
			ae.destroy();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "abort", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_destroying_ae__INFO", new Object[] { ae.getAnalysisEngineMetaData().getName(), i });
      }
			i++;
		}
		aeInstanceMap.clear();
	}


}
