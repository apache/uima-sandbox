package org.apache.uima.aae.jmx;

import java.io.Serializable;

public interface ServiceInfoMBean extends Serializable
{
	public String getState();
	public String getInputQueueName();
	public String getBrokerURL();
	public String[] getDeploymentDescriptor();
}
