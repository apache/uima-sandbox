package org.apache.uima.aae.controller;

import java.io.Serializable;

public interface BaseAnalysisEngineControllerMBean extends Serializable
{
	public String getServiceName();
	public String getDeploymentMode();
	public String getBrokerURL();
	public String getInputQueue();
	public String getComponentName();
	public long getIdleTime();
	public long getTotalTimeSpentSerializingCAS();
	public long getTotalTimeSpendDeSerializingCAS();
	public long getTotalTimeSpentWaitingForFreeCASInstance();
	public long getTotalNumberOfCASesReceived();
	public long getTotalNumberOfCASesProcessed();
	public long getTotalNumberOfCASesDropped();
	public long getTotalNumberOfErrors();
	public String getDeploymentDescriptor();

}
