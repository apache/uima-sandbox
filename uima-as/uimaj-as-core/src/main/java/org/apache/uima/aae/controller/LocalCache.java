package org.apache.uima.aae.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.util.Level;

public class LocalCache extends ConcurrentHashMap<String, LocalCache.CasStateEntry> {
  private static final long serialVersionUID = 1L;
  private static final Class CLASS_NAME = LocalCache.class;

  private AnalysisEngineController controller;
  
  public LocalCache(AnalysisEngineController aController) {
    controller = aController;
  }
  public CasStateEntry createCasStateEntry( String aCasReferenceId ) {
    CasStateEntry entry = new CasStateEntry( aCasReferenceId );
    super.put( aCasReferenceId, entry);
    return entry;
  }
  public CasStateEntry lookupEntry( String aCasReferenceId ) {
    if ( super.containsKey(aCasReferenceId)) {
      return super.get(aCasReferenceId);
    }
    return null;
  }
  
  public String lookupInputCasReferenceId(String aCasReferenceId) {
    String parentCasReferenceId=null;
    if ( this.containsKey(aCasReferenceId)) {
      CasStateEntry entry = (CasStateEntry)get(aCasReferenceId);
      if ( entry != null && entry.isSubordinate()) {
        //  recursively call each parent until we get to the top of the 
        //  Cas hierarchy
        parentCasReferenceId = lookupInputCasReferenceId(entry.getInputCasReferenceId());
      } else {
        return aCasReferenceId;
      }
    }
    return parentCasReferenceId;
  }

  public String lookupInputCasReferenceId(CasStateEntry entry) {
    String parentCasReferenceId=null;
    if ( entry.isSubordinate()) {
      //  recursively call each parent until we get to the top of the 
      //  Cas hierarchy
      parentCasReferenceId = 
        lookupInputCasReferenceId((CasStateEntry)get(entry.getInputCasReferenceId()));
    } else {
      return entry.getCasReferenceId();
    }
    return parentCasReferenceId;
  }
  
  public synchronized void dumpContents() {
    int count=0;
    if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
    {
      StringBuffer sb = new StringBuffer("\n");

      
      for( Iterator it = entrySet().iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry)it.next();
        CasStateEntry casStateEntry = (CasStateEntry)entry.getValue();
        if ( casStateEntry == null ) {
          continue;
        }
        count++;
        if ( casStateEntry.isSubordinate())
        {
          sb.append(entry.getKey()+ " Number Of Child CASes In Play:"+casStateEntry.getSubordinateCasInPlayCount()+" Parent CAS id:"+casStateEntry.getInputCasReferenceId());
        }
        else
        {
          sb.append(entry.getKey()+ " *** Input CAS. Number Of Child CASes In Play:"+casStateEntry.getSubordinateCasInPlayCount());
        }
        if ( casStateEntry.isWaitingForRelease() )
        {
          sb.append(" <<< Reached Final State in Controller:"+controller.getComponentName());
        }
        sb.append("\n");
      }
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                  "dumpContents", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_cache_entry_key__FINEST",
                  new Object[] { controller.getComponentName(), count, sb.toString() });
      sb.setLength(0);
	  }
    else if ( UIMAFramework.getLogger().isLoggable(Level.FINE) )
    {
      int inFinalState=0;
      
      for( Iterator it = entrySet().iterator(); it.hasNext(); ) {
        Map.Entry entry = (Map.Entry)it.next();
        CasStateEntry casStateEntry = (CasStateEntry)entry.getValue();

        count++;
        if ( casStateEntry != null && casStateEntry.isWaitingForRelease() )
        {
          inFinalState++;
        }
      }
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                  "dumpContents", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_abbrev_cache_stats___FINE",
                  new Object[] { controller.getComponentName(), count, inFinalState });
    
      
    }
    
  }
  public synchronized void remove(String aCasReferenceId)
  {
    if (aCasReferenceId != null && containsKey(aCasReferenceId))
    {
      
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_remove_cache_entry_for_cas__FINE", new Object[] { aCasReferenceId });
      }
      super.remove(aCasReferenceId);
      this.notifyAll();
    }
    else if ( aCasReferenceId == null )
    {
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_is_null_remove_from_cache_failed__FINE");
      }
    }
    else
    {
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_is_invalid_remove_from_cache_failed__FINE", new Object[] { aCasReferenceId });
      }
    }
  }
  public CasStateEntry getTopCasAncestor( String casReferenceId ) throws Exception
  {
    if ( !containsKey(casReferenceId) ) {
      return null;
    }
    CasStateEntry casStateEntry = lookupEntry(casReferenceId);
    if ( casStateEntry.isSubordinate() )
    {
      //  Recurse until the top CAS reference Id is found
      return getTopCasAncestor(casStateEntry.getInputCasReferenceId());
    }
    //  Return the top ancestor CAS id
    return casStateEntry;
  }

  public static class CasStateEntry {
    
    private String casReferenceId;
    private volatile boolean waitingForRealease;
    private volatile boolean  pendingReply;
    private volatile boolean subordinateCAS;
    private volatile boolean replyReceived;
    private volatile boolean failed;
    private volatile boolean dropped;
    
    private List<Throwable> exceptionList = new ArrayList<Throwable>(); 
    private FinalStep step;
    private int state;
    private int subordinateCasInPlayCount;
    private Object childCountMux = new Object();
    private String inputCasReferenceId;
    private int numberOfParallelDelegates = 1;
    private Delegate lastDelegate = null; 
    private int howManyDelegatesResponded = 0;
    private Endpoint freeCasNotificationEndpoint;

    public boolean isDropped() {
      return dropped;
    }
    public void setDropped(boolean dropped) {
      this.dropped = dropped;
    }
    
    public Endpoint getFreeCasNotificationEndpoint() {
      return freeCasNotificationEndpoint;
    }
    public void setFreeCasNotificationEndpoint(Endpoint freeCasNotificationEndpoint) {
      this.freeCasNotificationEndpoint = freeCasNotificationEndpoint;
    }
    public CasStateEntry( String aCasReferenceId ) {
      casReferenceId = aCasReferenceId;
    }
    public void setLastDelegate( Delegate aDelegate) {
      lastDelegate = aDelegate;
    }
    public Delegate getLastDelegate() {
      return lastDelegate;
    }
    public String getCasReferenceId() {
      return casReferenceId;
    }
    public String getInputCasReferenceId() {
      return inputCasReferenceId;
    }
    public void setInputCasReferenceId(String anInputCasReferenceId) {
      inputCasReferenceId = anInputCasReferenceId;
      subordinateCAS = true;
    }
    public void setWaitingForRelease(boolean flag) {
      waitingForRealease = flag;
    }
    public boolean isWaitingForRelease() {
      return waitingForRealease;
    }
    public void setFinalStep( FinalStep step ) {
      this.step = step;
    }
    public FinalStep getFinalStep() {
      return step;
    }
    public int getState() {
      return state;
    }
    public void setState( int aState ) {
      state = aState;
    }
    public boolean isSubordinate() {
      return subordinateCAS;
    }
    public int getSubordinateCasInPlayCount() {
      synchronized( childCountMux ) {
        return subordinateCasInPlayCount;
      }
    }
    public void incrementSubordinateCasInPlayCount() {
      synchronized( childCountMux ) {
        subordinateCasInPlayCount++;
      }
    }
    public int decrementSubordinateCasInPlayCount() {
      synchronized( childCountMux ) {
        if ( subordinateCasInPlayCount > 0) {
          subordinateCasInPlayCount--;
        }
        return subordinateCasInPlayCount;
      }
    }
    public boolean isPendingReply() {
      return pendingReply;
    }
    public void setPendingReply(boolean pendingReply) {
      this.pendingReply = pendingReply;
    }
    public void setReplyReceived() {
      replyReceived = true;
    }
    public boolean isReplyReceived() {
      return replyReceived;
    }
    public synchronized void incrementHowManyDelegatesResponded(){
      if ( howManyDelegatesResponded < numberOfParallelDelegates) {
        howManyDelegatesResponded++;
      }
    }
    public synchronized int howManyDelegatesResponded(){
      return howManyDelegatesResponded;
    }
    
    public synchronized void resetDelegateResponded(){
      howManyDelegatesResponded = 0;
    }
    public void setNumberOfParallelDelegates( int aNumberOfParallelDelegates ) {
      numberOfParallelDelegates = aNumberOfParallelDelegates;
    }
    
    public int getNumberOfParallelDelegates() {
      return numberOfParallelDelegates;
    }
    public boolean isFailed() {
      return failed;
    }
    public void setFailed() {
      this.failed = true;
    }
    public void addThrowable( Throwable t ) {
      exceptionList.add(t);
    }
    public List<Throwable> getErrors() {
      return exceptionList;
    }
  }
}
