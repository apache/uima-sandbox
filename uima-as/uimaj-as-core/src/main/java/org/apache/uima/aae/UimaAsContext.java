package org.apache.uima.aae;

import java.util.concurrent.ConcurrentHashMap;

public class UimaAsContext extends ConcurrentHashMap {
  
  private final static String CONSUMER_COUNT_KEY = "CONCURRENT_CONSUMER_COUNT";
 
  public void setConcurrentConsumerCount(int aCount)
  {
    put( CONSUMER_COUNT_KEY, aCount);
  }
  
  public int getConcurrentConsumerCount() {
    Object value = get(CONSUMER_COUNT_KEY);
    if ( value != null ) {
      return Integer.valueOf((Integer) value);
    }
    return 0;
  }
  

}
