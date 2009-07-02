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

package org.apache.uima.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.uima.aae.client.UimaASProcessStatusImpl;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * The <code>UimaAsProducer</code> sends the body of a message to a UIMA AS processing
 * pipeline.
 * 
 * Configuration URI:
 * uimadriver:authority?queue="nameofqueue"
 * 
 * For example:
 * uimadriver:tcp://localhost:61616?queue=TextAnalysisQueue
 */
public class UimaAsProducer extends DefaultProducer<Exchange> implements AsyncProcessor {

  private static class ExchangeAsyncCallbackPair {
    Exchange exchange;

    AsyncCallback callback;
  }

  private static class UimaStatusCallbackListener extends UimaAsBaseCallbackListener {

    /**
     * Shared intermediate map instance. Its used to map the cas reference id
     * to the input <code>Exchange</code>.
     */
    private final Map<String, ExchangeAsyncCallbackPair> intermediateMap;
    
    private UimaStatusCallbackListener(Map<String, ExchangeAsyncCallbackPair> intermediateMap) {
      this.intermediateMap = intermediateMap;
    }
    
    public void initializationComplete(EntityProcessStatus aStatus) {

    }

    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
      UimaASProcessStatusImpl statusImpl = (UimaASProcessStatusImpl) aStatus;

      String referenceId = statusImpl.getCasReferenceId();
      
      ExchangeAsyncCallbackPair exchangeCallbackPair;
      
      synchronized (intermediateMap) {
        exchangeCallbackPair = intermediateMap.remove(referenceId);
      }
      
      if (exchangeCallbackPair != null) {
        if (aStatus.isException()) {

          // TODO: Retrieve exceptions from status object

          exchangeCallbackPair.exchange.setException(new Exception(aStatus.getStatusMessage()));
        }

        exchangeCallbackPair.callback.done(false);
      } else {
        // TODO: log error
      }
    }

    public void collectionProcessComplete(EntityProcessStatus aStatus) {
      // TODO: log error status
    }
  }

  private UimaAsynchronousEngine uimaAsEngine;

  /**
   * The intermediate map keeps all {@link Exchange}s and their callbacks until asynchronous
   * processing is finished.
   */
  private final Map<String, ExchangeAsyncCallbackPair> intermediateMap;

  public UimaAsProducer(String brokerAddress, String queue, Endpoint<Exchange> endpoint)
          throws Exception {
    super(endpoint);

    intermediateMap = new HashMap<String, ExchangeAsyncCallbackPair>();

    uimaAsEngine = new BaseUIMAAsynchronousEngine_impl();

    uimaAsEngine.addStatusCallbackListener(new UimaStatusCallbackListener(intermediateMap));

    Map<String, Object> appCtx = new HashMap<String, Object>();
    appCtx.put(UimaAsynchronousEngine.ServerUri, brokerAddress);
    appCtx.put(UimaAsynchronousEngine.Endpoint, queue);
    appCtx.put(UimaAsynchronousEngine.CasPoolSize, 15);

    try {
      uimaAsEngine.initialize(appCtx);
    } catch (ResourceInitializationException e) {
      throw e;
    }
  }

  /**
   * Not implemented, since the producer implements the AsyncProcessor interface.
   */
  public void process(Exchange exchange) throws Exception {
  }

  public boolean process(Exchange exchange, AsyncCallback callback) {

    String rowId = exchange.getIn().getBody(String.class);

    String refernceId;

    try {
      CAS cas = uimaAsEngine.getCAS();

      cas.setDocumentText(rowId);
      
      // The intermediate map must be locked for sending
      // a CAS to the service to guarantee that the reference
      // id for the CAS is inserted into the intermediate map
      // before the call back listener tries to map
      // the just returned reference id to an exchange
      
      synchronized (intermediateMap) {
        
        refernceId = uimaAsEngine.sendCAS(cas);
      
        ExchangeAsyncCallbackPair exchangeCallback = new ExchangeAsyncCallbackPair();
        exchangeCallback.exchange = exchange;
        exchangeCallback.callback = callback;

        intermediateMap.put(refernceId, exchangeCallback);
      }

    } catch (Exception e) {
      // Processing of the exchange failed

      // The error message is set on the exchange
      exchange.setException(e);

      // and the method returns synchronously with true
      callback.done(true);
      return true;
    }
    
    return false;
  }
}
