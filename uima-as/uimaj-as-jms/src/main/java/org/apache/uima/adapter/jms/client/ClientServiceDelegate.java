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
package org.apache.uima.adapter.jms.client;

import javax.jms.Destination;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngineCommon_impl.ClientRequest;
import org.apache.uima.cas.CAS;
import org.apache.uima.util.Level;

public class ClientServiceDelegate extends Delegate {
  private static final Class CLASS_NAME = ClientServiceDelegate.class;

  private BaseUIMAAsynchronousEngineCommon_impl clientUimaAsEngine;
  private String applicationName = "UimaAsClient";
  private volatile boolean usesSynchronousAPI;
  private Destination freeCasDestination = null;
  
  public ClientServiceDelegate(String serviceName, String anApplicationName,  BaseUIMAAsynchronousEngineCommon_impl engine ) {
    super.delegateKey = serviceName;
    clientUimaAsEngine = engine;
    if ( anApplicationName != null && anApplicationName.trim().length() > 0 ) {
      applicationName = anApplicationName;
    }
  }
  public boolean isSynchronousAPI() {
    return usesSynchronousAPI;
  }
  public void setSynchronousAPI() {
    this.usesSynchronousAPI = true;;
  }

  public Destination getFreeCasDestination() {
    return freeCasDestination;
  }
  public void setFreeCasDestination(Destination freeCasDestination) {
    this.freeCasDestination = freeCasDestination;
  }
  public String getComponentName() {
    return applicationName;
  }

  public synchronized void handleError(Exception e, ErrorContext errorContext) {
    String casReferenceId = null;
    CAS cas = null;
    ClientRequest cachedRequest = null;
    if ( !clientUimaAsEngine.running ) {
      cancelDelegateTimer();
      return;
    }
    int command = ((Integer)errorContext.get(AsynchAEMessage.Command)).intValue();
    try {
      if ( e instanceof MessageTimeoutException) {
        cancelDelegateTimer();
        switch( command ) {
          case AsynchAEMessage.Process:
            casReferenceId = (String)errorContext.get(AsynchAEMessage.CasReference);
            if ( casReferenceId != null ) {
              cachedRequest =(ClientRequest)clientUimaAsEngine.clientCache.get(casReferenceId);
              if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) &&
                      getEndpoint() != null )  {
                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_process_timeout_INFO", new Object[] { getEndpoint().getEndpoint() });
              }        
              if (cachedRequest != null && cachedRequest.isRemote()) {
                cas = cachedRequest.getCAS();
              }
              boolean isPingTimeout = false;
              if ( errorContext.containsKey(AsynchAEMessage.ErrorCause)) {
                isPingTimeout =  
                  AsynchAEMessage.PingTimeout == 
                    (Integer)errorContext.get(AsynchAEMessage.ErrorCause);
              }
              if ( isPingTimeout && isAwaitingPingReply() ) {
                System.out.println(">>>>> Client Ping Timedout");
                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "handleError", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_client_ping_timed_out__WARNING",
                        new Object[] { getKey() });
                
                clientUimaAsEngine.notifyOnTimout(cas, clientUimaAsEngine.getEndPointName(), BaseUIMAAsynchronousEngineCommon_impl.PingTimeout, casReferenceId);
              } else {
                if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
                  UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleError", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_client_process_timedout__INFO",
                          new Object[] { getGetMetaTimeout() });
                }
                System.out.println(">>>>> Client Process Timed out. Notifying Listeners");
                clientUimaAsEngine.notifyOnTimout(cas, clientUimaAsEngine.getEndPointName(), BaseUIMAAsynchronousEngineCommon_impl.ProcessTimeout, casReferenceId);
              }
            }
            clientUimaAsEngine.clientSideJmxStats.incrementProcessTimeoutErrorCount();
            break;
            
          case AsynchAEMessage.GetMeta:
               if ( isAwaitingPingReply() ) {
                 System.out.println(">>>>> Client Ping Timedout");
                 clientUimaAsEngine.notifyOnTimout(cas, clientUimaAsEngine.getEndPointName(), BaseUIMAAsynchronousEngineCommon_impl.PingTimeout, casReferenceId);
               } else {
                 //  Notifies Listeners and removes ClientRequest instance from the client cache
                 clientUimaAsEngine.notifyOnTimout(cas, clientUimaAsEngine.getEndPointName(), BaseUIMAAsynchronousEngineCommon_impl.MetadataTimeout, casReferenceId);
                 clientUimaAsEngine.clientSideJmxStats.incrementMetaTimeoutErrorCount();
               }
               if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
                 UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleError", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_meta_timeout_INFO",
                   new Object[] { getKey() });
               }
               System.out.println("Stopping Uima AS Client API. Service Not Responding To a Ping.");
               clientUimaAsEngine.stop();
            break;
              
          case AsynchAEMessage.CollectionProcessComplete:
            
            break;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { ex });
      }        
    }
    //  Dont release the CAS if synchronous API was used
    if (cas != null && !cachedRequest.isSynchronousInvocation())
    {
      cas.release();
    }
  }

}
