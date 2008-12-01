package org.apache.uima.aae.spi.transport.vm;

import java.io.Serializable;

public interface UimaVmQueueMBean extends Serializable{
  
  public int getQueueSize();
  public int getConsumerCount();
  public long getDequeueCount();
  public void reset();
  
}
