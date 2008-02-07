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

import org.apache.activemq.memory.UsageListener;
import org.apache.activemq.memory.UsageManager;
import org.apache.uima.UIMAFramework;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;

public class UimaEEBrokerMemoryUsageListener implements UsageListener
{
	private static final Class CLASS_NAME = UimaEEBrokerMemoryUsageListener.class;

	public void onMemoryUseChanged(UsageManager aUsageManager, int oldPercentageUsage, int newPercentageUsage)
	{
		if ( aUsageManager.isFull() )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "onMemoryUseChanged", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_no_memory__WARNING",
                    new Object[] { aUsageManager.getLimit(), aUsageManager.getPercentUsage()});
		}
		else if ( UIMAFramework.getLogger().isLoggable(Level.FINEST))
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "onMemoryUseChanged", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_broker_memory__FINEST",
                    new Object[] { aUsageManager.getLimit(), oldPercentageUsage, newPercentageUsage});
		}
	}

}
