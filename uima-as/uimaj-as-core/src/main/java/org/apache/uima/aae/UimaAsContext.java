package org.apache.uima.aae;

import java.util.concurrent.ConcurrentHashMap;

public class UimaAsContext extends ConcurrentHashMap {
  
  private final static String CONSUMER_COUNT_KEY = "CONCURRENT_CONSUMER_COUNT";
 
  public void setConcurrentConsumerCount(int aCount)
  {
    put( CONSUMER_COUNT_KEY, aCount);
  }
  
  public int getConcurrentConsumerCount() {
    return new Integer( (Integer)get(CONSUMER_COUNT_KEY )).intValue();
  }
  

}
