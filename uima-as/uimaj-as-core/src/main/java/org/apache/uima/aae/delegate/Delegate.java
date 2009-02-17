/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.aae.delegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.error.UimaASMetaRequestTimeout;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.util.Level;

public abstract class Delegate {
  public static final int OK_STATE = 1;

  public static final int TIMEOUT_STATE = 2;

  public static final int DISABLED_STATE = 3;

  private static final Class CLASS_NAME = Delegate.class;

  // stores unique key assigned to the delegate
  protected String delegateKey;

  // stores delegate state
  private int state = OK_STATE;

  // List holding CASes already sent to the delegate
  private List<DelegateEntry> outstandingCasList = new ArrayList<DelegateEntry>();

  // stores the endpoint info
  private Endpoint endpoint;

  // synchronizes access to the pending CAS list
  private Object outstandingCasListMux = new Object();

  // Timer object to time replies
  private Timer timer;

  // Process Timeout value for this delegate
  private long casProcessTimeout = 0;

  // getMeta Timeout value for this delegate
  private long getMetaTimeout = 0;

  // CPC Timeout value for this delegate
  private long cpcTimeout = 0;

  // synchronizes access to the delegate's state
  private Object stateMux = new Object();

  // List holding CASes that have been delayed due to a delegate timeout. These
  // CASes should be send to the delegate as soon as the getMeta (Ping) is received.
  private List<DelegateEntry> pendingDispatchList = new ArrayList<DelegateEntry>();

  // synchronizes access to the list of CASes pending dispatch
  private Object pendingDispatchListMux = new Object();
  
  //  Flag that is set when getMeta reply is received
  private volatile boolean awaitingPingReply;

  public boolean isAwaitingPingReply() {
    return awaitingPingReply;
  }

  public void setAwaitingPingReply() {
    this.awaitingPingReply = true;
  }

  public void resetAwaitingPingReply() {
    this.awaitingPingReply = false;
  }
  /**
   * Returns delegate key
   * 
   * @return
   */
  public String getKey() {
    return delegateKey;
  }

  /**
   * Sets an {@link Endpoint} object
   * 
   * @param anEndpoint
   *          - an endpoint object
   */
  public void setEndpoint(Endpoint anEndpoint) {
    endpoint = anEndpoint;
  }

  /**
   * Returns an {@link Endpoint} object
   * 
   * @return
   */
  public Endpoint getEndpoint() {
    return endpoint;
  }
  /**
   * Forces Timer restart for the oldest CAS sitting in the list
   * of CASes pending reply.  
   */
  public void restartTimerForOldestCasInOutstandingList() {
    DelegateEntry entry = null;
    synchronized (outstandingCasListMux) {
      if (!outstandingCasList.isEmpty()) {
        //  Get the oldest entry
        entry = outstandingCasList.get(0);
        if ( entry != null ) {
          restartTimerForCas(entry);
        }
      }
    }
  }
  /**
   * Restarts timer for a given CAS
   * 
   * @param entry
   */
  private void restartTimerForCas( DelegateEntry entry ) {
    if (getCasProcessTimeout() > 0) {
      entry.incrementRetryCount();
      // restart timer for retry
      startDelegateTimer(entry.getCasReferenceId(), AsynchAEMessage.Process);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                this.getClass().getName(),
                "restartTimerForCas",
                UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAEE_restart_timer_FINE",
                new Object[] { getComponentName(), delegateKey, entry.getCasReferenceId(),
                    getCasProcessTimeout() });
      }
    }    
  }

  
  /**
   * Adds a given Cas ID to the list of CASes pending reply. A new timer will be started to handle
   * delegate's timeout if either: 1) the list of CASes pending reply is empty AND delegate timeout
   * > 0 2) the list already contains the CAS ID AND delegate timeout > 0. This is a retry logic.
   * 
   * @param aCasReferenceId
   *          - CAS ID to add to pending list if not already there
   * 
   */
  public void addCasToOutstandingList(String aCasReferenceId) {
    synchronized (outstandingCasListMux) {
      DelegateEntry entry = null;
      // Check if the outstanding list already contains entry for the Cas Id. If it does, retry
      // logic
      // is calling this method. Increment number of retries and restart the timer.
      if (!outstandingCasList.isEmpty() && (entry = lookupEntry(aCasReferenceId, outstandingCasList)) != null) {
        restartTimerForCas(entry);
      } else {
        // Create a new entry to be stored in the list of CASes pending reply
        entry = new DelegateEntry(aCasReferenceId);
        // Remember the command
        entry.setCommand(AsynchAEMessage.Process);
        // Start delegate timer if the pending list is empty
        if (outstandingCasList.isEmpty() && getCasProcessTimeout() > 0) {
          startDelegateTimer(aCasReferenceId, AsynchAEMessage.Process);
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(
                    Level.FINE,
                    this.getClass().getName(),
                    "addCasToOutstandingList",
                    UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAEE_start_timer_FINE",
                    new Object[] { getComponentName(), delegateKey, aCasReferenceId,
                        getCasProcessTimeout() });
          }
        }
        // Append Cas Entry to the end of the list
        outstandingCasList.add(entry);
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  this.getClass().getName(),
                  "addCasToOutstandingList",
                  UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAEE_add_cas_to_delegate_pending_reply_FINE",
                  new Object[] { getComponentName(), delegateKey, aCasReferenceId,
                      outstandingCasList.size() });
        }
      }
    }
  }

  /**
   * Adds given CAS ID to the list of CASes pending dispatch. These CASes are delayed due to a
   * questionable state of the delegate that most likely timed out on a previous CAS. When the
   * timeout occurs, the subsequent CASes are queued (delayed) and a GetMeta request is sent to the
   * delegate. When the delegate responds to GetMeta request, the state of the delegate is reset
   * back to normal and the CASes queued (delayed) are immediately send to the delegate.
   * 
   * @param aCasReferenceId
   *          - CAS ID to add to the delayed list
   */
  public int addCasToPendingDispatchList(String aCasReferenceId) {
    synchronized (pendingDispatchListMux) {
      DelegateEntry entry = null;
      // Create a new entry to be stored in the list of CASes pending
      // dispatch
      entry = new DelegateEntry(aCasReferenceId);
      // Remember the command
      entry.setCommand(AsynchAEMessage.Process);
      // Append Cas Entry to the end of the list
      pendingDispatchList.add(entry);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        dumpDelayedList();
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                this.getClass().getName(),
                "addCasToPendingDispatchList",
                UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAEE_add_cas_to_delegate_pending_dispatch_FINE",
                new Object[] { getComponentName(), delegateKey, aCasReferenceId,
                    pendingDispatchList.size() });
      }
      return pendingDispatchList.size();
    }
  }
  /**
   * Logs CASes sitting in the list of CASes pending dispatch. These CASes
   * were delayed due to a bad state of the delegate. 
   */
  private void dumpDelayedList() {
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      for( DelegateEntry entry: pendingDispatchList) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                this.getClass().getName(),
                "dumpDelayedList",
                UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAEE_dump_cas_pending_dispatch__FINE",
                new Object[] { getComponentName(),  entry.getCasReferenceId(), delegateKey });
      }
    }
  }
  /**
   * Logs CASes sitting in the list of CASes pending reply. 
   */
  private void dumpPendingReplyList() {
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      for( DelegateEntry entry: outstandingCasList) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                this.getClass().getName(),
                "dumpPendingReplyList",
                UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAEE_dump_cas_pending_reply__FINE",
                new Object[] { getComponentName(),  entry.getCasReferenceId(), delegateKey });
      }
    }
  }
  /**
   * Increments retry count
   * 
   * @param aCasReferenceId
   */
  public void incrementRetryCount(String aCasReferenceId) {
    synchronized (outstandingCasListMux) {
      DelegateEntry entry = lookupEntry(aCasReferenceId, outstandingCasList);
      if (entry != null) {
        entry.incrementRetryCount();
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  this.getClass().getName(),
                  "incrementRetryCount",
                  UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAEE_increment_retry_count_FINE",
                  new Object[] { getComponentName(), delegateKey, aCasReferenceId,
                      entry.getRetryCount() });
        }
      }
    }
  }

  /**
   * Returns {@link DelegateEntry} instance that matches given CAS ID pending reply.
   * 
   * @param aCasReferenceId
   *          - unique id of a CAS to be searched for
   * @return
   */
  private DelegateEntry lookupEntry(String aCasReferenceId, List<DelegateEntry> list) {
    for (DelegateEntry entry : list) {
      if (entry.getCasReferenceId().equals(aCasReferenceId)) {
        return entry;
      }
    }
    return null;
  }
  /** 
   * Removes the oldest entry from the list of CASes pending dispatch.
   * A CAS is delayed and placed on this list when the delegate status
   * changes to TIMED_OUT and a PING message is sent to test delegate 
   * availability. Until the PING message is acked by the delegate OR
   * the PING times out, all CASes are delayed. When the PING is acked
   * by the delegate ALL delayed CASes are sent to the delegate one at
   * a time.
   * 
   * @return - ID of the oldest CAS in the list
   */
  public String removeOldestFromPendingDispatchList() {
    synchronized (pendingDispatchListMux) {
      if ( pendingDispatchList.size() > 0 ) {
        String casReferenceId = pendingDispatchList.remove(0).getCasReferenceId();
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  this.getClass().getName(),
                  "removeOldestFromPendingDispatchList",
                  UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAEE_removed_cas_from_delegate_pending_dispatch_list__FINE",
                  new Object[] { getComponentName(), delegateKey, casReferenceId,
                    pendingDispatchList.size() });
        }
        return casReferenceId;
      }
    }
    return null;
  }

  
  /** 
   * Removes an entry from the list of CASes pending dispatch that
   * matches a given CAS Id.A CAS is delayed and placed on this list when the delegate status
   * changes to TIMED_OUT and a PING message is sent to test delegate 
   * availability. Until the PING message is acked by the delegate OR
   * the PING times out, all CASes are delayed. When the PING is acked
   * by the delegate ALL delayed CASes are sent to the delegate one at
   * a time.
   * 
   * @return - ID of the oldest CAS in the list
   */
  public boolean removeCasFromPendingDispatchList(String aCasReferenceId) {
    synchronized (pendingDispatchListMux) {
      DelegateEntry entry = lookupEntry(aCasReferenceId, pendingDispatchList);
      if (entry != null) {
        pendingDispatchList.remove(entry);
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  this.getClass().getName(),
                  "removeCasFromPendingDispatchList",
                  UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAEE_removed_cas_from_delegate_pending_dispatch_list__FINE",
                  new Object[] { getComponentName(), delegateKey, entry.getCasReferenceId(),
                    pendingDispatchList.size() });
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Removes {@link DelegateEntry} from the list of CASes pending reply. The entry is removed when
   * either: 1) reply is received from the delegate before the timeout 2) the timeout occurs with no
   * retry 3) an error occurs and the CAS is dropped as part of Error Handling
   * 
   * @param aCasReferenceId
   *          - id of the CAS to remove from the list
   */
  public boolean removeCasFromOutstandingList(String aCasReferenceId) {
    synchronized (outstandingCasListMux) {
      DelegateEntry entry = lookupEntry(aCasReferenceId, outstandingCasList);
      if (entry != null) {
        this.removeCasFromOutstandingList(entry);
        return true;
      }
    }
    return false;
  }

  /**
   * Removes {@link DelegateEntry} from the list of CASes pending reply. The entry is removed when
   * either: 1) reply is received from the delegate before the timeout 2) the timeout occurs with no
   * retry 3) an error occurs and the CAS is dropped as part of Error Handling
   * 
   * @param aCasReferenceId
   *          - id of the CAS to remove from the list
   */
  public String removeOldestCasFromOutstandingList() {
    synchronized (outstandingCasListMux) {
      dumpPendingReplyList();
      return outstandingCasList.remove(0).getCasReferenceId();
    }
  }
  
  public String getOldestCasIdFromOutstandingList() {
    synchronized (outstandingCasListMux) {
      return outstandingCasList.get(0).getCasReferenceId();
    }
  }
  /**
   * Removes {@link DelegateEntry} from the list of CASes pending reply. If the CAS removed was the
   * oldest in the list (first in the list) AND there are other CASes in the list pending reply AND
   * the delegate timeout is configured ( timeout > 0) , restart the timer for the next oldest CAS
   * in the list.
   * 
   * @param aDelegateEntry
   */
  private void removeCasFromOutstandingList(DelegateEntry aDelegateEntry) {
    // Before removing the entry check if this is the oldest in the list. This will be
    // used to determine if we need to restart the delegate timer
    DelegateEntry oldestEntry = outstandingCasList.get(0);
    boolean doStartDelegateTimer = oldestEntry.equals(aDelegateEntry) && getCasProcessTimeout() > 0;
    outstandingCasList.remove(aDelegateEntry);

    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(
              Level.FINE,
              this.getClass().getName(),
              "removeCasFromOutstandingList",
              UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
              "UIMAEE_removed_cas_from_delegate_list__FINE",
              new Object[] { getComponentName(), delegateKey, aDelegateEntry.getCasReferenceId(),
                  outstandingCasList.size() });
    }
    // Restart delegate Timer if the CAS removed was the oldest and the list is not empty
    if (doStartDelegateTimer) {
      // Cancel previous timer and restart it if there are still CASes in the outstanding list
      cancelDelegateTimer();
      if (!outstandingCasList.isEmpty()) {
        // get the oldest entry from the list of CASes pending reply
        DelegateEntry delegateEntry = outstandingCasList.get(0);

        // Restart the timer for the oldest CAS in the list
        startDelegateTimer(delegateEntry.getCasReferenceId(), delegateEntry.getCommand());
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  this.getClass().getName(),
                  "removeCasFromOutstandingList",
                  UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAEE_restart_timer_FINE",
                  new Object[] { getComponentName(), delegateKey,
                      delegateEntry.getCasReferenceId(), getCasProcessTimeout() });
        }
      }
    }

  }

  /**
   * Cancels timer and clears a list of CASes pending reply
   */
  public void cleanup() {
    cancelDelegateTimer();
    synchronized (outstandingCasListMux) {
      outstandingCasList.clear();
    }
    synchronized( pendingDispatchListMux) {
      pendingDispatchList.clear();
    }
  }

  public int getCasPendingReplyListSize() {
    synchronized (outstandingCasListMux) {
      return outstandingCasList.size();
    }
  }

  public int getCasPendingDispatchListSize() {
    synchronized (pendingDispatchListMux) {
      return pendingDispatchList.size();
    }
  }
  
  /**
   * Cancels current timer
   */
  public synchronized void cancelDelegateTimer() {
    if (timer != null) {
      timer.cancel();
      timer.purge();
    }
  }

  /**
   * Returns a timeout value for a given command type. The values are defined in the deployment
   * descriptor
   * 
   * @param aCommand
   *          - command for which a timeout value is saught
   * 
   * @return - long time out value
   */
  private long getTimeoutValueForCommand(int aCommand) {
    switch (aCommand) {
      case AsynchAEMessage.Process:
        return getCasProcessTimeout();
      case AsynchAEMessage.GetMeta:
        return getGetMetaTimeout();
      case AsynchAEMessage.CollectionProcessComplete:
        return getCpcTimeout();
      default:
        return -1;
    }
  }

  /**
   * Starts GetMeta Request timer
   */
  public void startGetMetaRequestTimer() {
    startDelegateTimer(null, AsynchAEMessage.GetMeta);
  }

  /**
   * Starts a timer for a given command
   * 
   * @param aCasReferenceId
   *          - id of a CAS if command = Process, null otherwise
   * @param aCommand
   *          - command for which the timer is started
   */
  private void startDelegateTimer(final String aCasReferenceId, final int aCommand) {
    //  Check if we are awaiting a Ping reply. While awaiting ping reply dont start
    //  a new timer.
    if ( isAwaitingPingReply() ) {
      //  Ping is actually a GetMeta request
      if ( aCommand == AsynchAEMessage.GetMeta ) {
        //  Cancel any outstanding timers. A timer for a Ping message is about to
        //  be started. Another thread may have started a Process timer.
        cancelDelegateTimer();
      } else {
        //  We are waiting for a ping reply, don't start a new timer
        return;
      }
    }
    final long timeToWait = getTimeoutValueForCommand(aCommand);
    Date timeToRun = new Date(System.currentTimeMillis() + timeToWait);
    timer = new Timer("Controller:" + getComponentName() + ":TimerThread-Endpoint_impl:" + endpoint
            + ":" + System.nanoTime() + ":Cmd:" + aCommand);
    final Delegate delegate = this;
    timer.schedule(new TimerTask() {
      public void run() {
        cancelDelegateTimer();
        delegate.setState(TIMEOUT_STATE);
        ErrorContext errorContext = new ErrorContext();
        errorContext.add(AsynchAEMessage.Command, aCommand);

        if (AsynchAEMessage.Process == aCommand) {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                    "Delegate.TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAEE_cas_timeout_no_reply__INFO",
                    new Object[] { delegate.getKey(), timeToWait, aCasReferenceId });
          }
          errorContext.add(AsynchAEMessage.CasReference, aCasReferenceId);
        } else if (AsynchAEMessage.GetMeta == aCommand) {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                    "Delegate.TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAEE_meta_timeout_no_reply__INFO",
                    new Object[] { delegate.getKey(), timeToWait });
          }
          //  Check if this is a Ping timeout. If it is and there are CASes on
          //  the list of CASes pending reply, treat this timeout as Process
          //  Timeout
          if ( isAwaitingPingReply() && getCasPendingReplyListSize() > 0) {
            String casReferenceId = getOldestCasIdFromOutstandingList();
            errorContext.add(AsynchAEMessage.CasReference, casReferenceId);
            //  Override the command to make sure this timeout is handled
            //  by the ProcessCasErrorHandler.
            errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
            errorContext.add(AsynchAEMessage.ErrorCause, AsynchAEMessage.PingTimeout);
          }
        } else if (AsynchAEMessage.CollectionProcessComplete == aCommand) {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                    "Delegate.TimerTask.run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAEE_cpc_timeout_no_reply__INFO",
                    new Object[] { delegate.getKey(), timeToWait });
          }

        }
        errorContext.add(AsynchAEMessage.Endpoint, getEndpoint());
        handleError(new MessageTimeoutException(), errorContext);
      }
    }, timeToRun);
  }

  public long getCasProcessTimeout() {
    return casProcessTimeout;
  }

  public void setCasProcessTimeout(long casProcessTimeout) {
    this.casProcessTimeout = casProcessTimeout;
  }

  public long getGetMetaTimeout() {
    return getMetaTimeout;
  }

  public void setGetMetaTimeout(long getMetaTimeout) {
    this.getMetaTimeout = getMetaTimeout;
  }

  public long getCpcTimeout() {
    return cpcTimeout;
  }

  public void setCpcTimeout(long cpcTimeout) {
    this.cpcTimeout = cpcTimeout;
  }

  public int getState() {
    synchronized (stateMux) {
      return state;
    }
  }

  public void setState(int aState) {
    synchronized (stateMux) {
      //  Change the state to timout, only if the current state = OK_STATE
      //  This prevents overriding DISABLED state.
      if ( aState == TIMEOUT_STATE && this.state != OK_STATE ) {
        return;
      } 
      state = aState;
    }
  }

  public abstract void handleError(Exception e, ErrorContext errorContext);

  public abstract String getComponentName();

  /**
   * Entry in the list of CASes pending reply. It stores the {@link CacheEntry} containing
   * information about a CAS that was sent to the delegate.
   * 
   * 
   */
  public class DelegateEntry {
    private String casReferenceId;

    private int command;

    private int retryCount = 0;

    public DelegateEntry(String aCasReferenceId) {
      casReferenceId = aCasReferenceId;
    }

    public int getCommand() {
      return command;
    }

    public void setCommand(int command) {
      this.command = command;
    }

    public int getRetryCount() {
      return retryCount;
    }

    public void incrementRetryCount() {
      this.retryCount++;
    }

    public void resetRetryCount() {
      this.retryCount = 0;
    }

    public String getCasReferenceId() {
      return casReferenceId;
    }
  }

}