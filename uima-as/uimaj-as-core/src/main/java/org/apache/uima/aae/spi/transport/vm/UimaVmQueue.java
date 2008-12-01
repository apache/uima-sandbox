package org.apache.uima.aae.spi.transport.vm;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a JMX wrapper around the {@link LinkedBlockingQueue}. It exposes
 * the following queue statistics:
 * <ul>
 *    <li>size - the number of items on the queue</li>
 *    <li>consumerCount - number of concurrent consuming threads taking items from this queue.</li>
 *    <li>dequeueCount - total number of items consumed so far </li>   
 * </ul>
 *
 */
public class UimaVmQueue extends LinkedBlockingQueue<Runnable> 
implements UimaVmQueueMBean {
  private static final long serialVersionUID = 1L;
  private int consumerCount = 0;
  private long dequeueCount = 0;
  
  public UimaVmQueue( int size ) {
   // super(size);
  }
 
  /**
   * Returns the current number of items in the queue.
   */  
  public int getQueueSize() {
    return super.size();
  }
  /**
   * Returns total number of items dequeued so far
   */
  public long getDequeueCount() {
    return dequeueCount;
  }
  /**
   * Override of the method in the super class to enable counting of items
   * taken (dequeued) off the queue.
   */
  public Runnable take() throws InterruptedException {
    Runnable work =  super.take();
    if ( work != null ) {
      dequeueCount++;
    }
    return work;
  }
  /**
   * Returns total number of concurrent threads consuming work from this
   * queue.  
   */
  public int getConsumerCount() {
    return consumerCount;
  }
  /**
   * Sets the number of concurrent threads consuming work from this queue
   * @param aConsumerCount - number of consuming threads
   */
  public void setConsumerCount(int aConsumerCount) {
    consumerCount = aConsumerCount;
  }
  /**
   * Resets both the queue size and dequeue count to zero
   */
  public void reset() {
    super.clear();
    dequeueCount = 0;
  }
  
}
