package org.apache.uima.adapter.jms;

public class JmsConstants
{
    public static final String JMS_LOG_RESOURCE_BUNDLE = "jms_adapter_messages";

    public static String threadName()
    {
    	return Thread.currentThread().getName();
    }


}
