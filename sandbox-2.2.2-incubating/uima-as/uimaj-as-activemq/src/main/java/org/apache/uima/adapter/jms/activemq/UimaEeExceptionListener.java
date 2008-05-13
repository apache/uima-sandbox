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

package org.apache.uima.adapter.jms.activemq;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InProcessCache;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.message.UimaEEShutdownTriggerEvent;
import org.apache.uima.util.Level;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class UimaEeExceptionListener implements ExceptionListener, ApplicationListener
{

	public void onException(JMSException arg0)
	{
		System.out.println("Exception");
		arg0.printStackTrace();

	}
	public void onApplicationEvent(ApplicationEvent anEvent)
	{
		if ( anEvent instanceof UimaEEShutdownTriggerEvent)
		{
System.out.println("Reaper Received Shutdown Request");
/*
	AnalysisEngineController targetController =
				((UimaEEShutdownTriggerEvent)anEvent).getTargetController();
			
			if ( targetController != null && targetController.isTopLevelComponent() )
			{
				targetController.terminate();
				InProcessCache inProcessCache = targetController.getInProcessCache();
				if ( !inProcessCache.isEmpty() )
				{
					inProcessCache.registerCallbackWhenCacheEmpty(targetController.getEventListener());
				}
				else
				{
					//	Callback to notify that the cache is empty
					targetController.getEventListener().onCacheEmpty();
				}
			}
*/			
		}
		else
		{
			System.out.println("=====> Initialize Event:"+anEvent.getClass().getName());
		}
	}

}
