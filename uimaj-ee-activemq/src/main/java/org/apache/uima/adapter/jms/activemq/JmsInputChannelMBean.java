package org.apache.uima.adapter.jms.activemq;

import java.io.Serializable;

public interface JmsInputChannelMBean extends Serializable
{
	public String getBrokerURL();
	
	public String getInputQueueName();
}
