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

import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngineCommon_impl.ClientRequest;
import org.apache.uima.cas.CAS;

public class ClientServiceDelegate extends Delegate {

  private BaseUIMAAsynchronousEngineCommon_impl clientUimaAsEngine;
  private String applicationName = "UimaAsClient";
  public ClientServiceDelegate(String serviceName, String anApplicationName,  BaseUIMAAsynchronousEngineCommon_impl engine ) {
    super.delegateKey = serviceName;
    clientUimaAsEngine = engine;
    if ( anApplicationName != null && anApplicationName.trim().length() > 0 ) {
      applicationName = anApplicationName;
    }
  }
  public String getComponentName() {
    return applicationName;
  }

  public void handleError(Exception e, ErrorContext errorContext) {
    String casReferenceId = (String)errorContext.get(AsynchAEMessage.CasReference);
    ClientRequest cachedRequest = (ClientRequest)clientUimaAsEngine.clientCache.get(casReferenceId);
    CAS cas = null;
    try {
       if (cachedRequest.isRemote()) {
         cas = cachedRequest.getCAS();
       }
       if ( e instanceof MessageTimeoutException) {
         
         //  Notifies Listeners and removes ClientRequest instance from the client cache
         clientUimaAsEngine.notifyOnTimout(cas, clientUimaAsEngine.getEndPointName(), BaseUIMAAsynchronousEngineCommon_impl.ProcessTimeout, casReferenceId);
         clientUimaAsEngine.clientSideJmxStats.incrementProcessTimeoutErrorCount();
       }
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }
    //  Dont release the CAS if synchronous API was used
    if (cas != null && !cachedRequest.isSynchronousInvocation())
    {
      cas.release();
    }
  }

}
