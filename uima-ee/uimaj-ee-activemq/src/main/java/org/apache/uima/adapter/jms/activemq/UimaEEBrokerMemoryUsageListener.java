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
