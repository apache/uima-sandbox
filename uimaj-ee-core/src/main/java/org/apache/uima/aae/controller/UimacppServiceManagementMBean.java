package org.apache.uima.aae.controller;

import java.io.IOException;
import java.io.Serializable;

import org.apache.uima.resource.ManagementObject;
/**
 * Defines the JMX management interface for the C++ service.
 * 
 *
 */
public interface UimacppServiceManagementMBean extends ManagementObject, 
											java.io.Serializable {


  public String getQueueBrokerURL() throws IOException;
  public String getQueueName()  throws IOException;
  public String getAEDescriptor() throws IOException;
  public int getAEInstances() throws IOException;
  
	public long   getErrorsGetMeta() throws IOException;
	public long   getErrorsProcessCas() throws IOException;
	public long   getErrorsCPC() throws IOException;
  
	public long   getTotalNumCasProcessed() throws IOException;
	public long   getTimingGetMeta() throws IOException;
	public long   getTimingCPC() throws IOException;
  public long   getTimingSerialization() throws IOException;
  public long   getTimingAnnotatorProcess() throws IOException;
  public long   getTimingDeserialization() throws IOException;
  public long   getTimingMessageProcessing() throws IOException;
  public long   getTimingIdle() throws IOException;
  
  public void   resetStats() throws IOException;
  public void   increaseAEInstances(int num);
  public void   decreaseAEInstances(int num);
  public void   shutdown()throws IOException;
    
}
