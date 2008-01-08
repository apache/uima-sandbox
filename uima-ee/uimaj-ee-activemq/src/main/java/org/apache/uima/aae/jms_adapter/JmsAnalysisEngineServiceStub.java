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

package org.apache.uima.aae.jms_adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.analysis_engine.AnalysisEngineServiceStub;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.Parameter;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.ResourceServiceException;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.Level;

public class JmsAnalysisEngineServiceStub implements AnalysisEngineServiceStub {
  public static final String PARAM_BROKER_URL = "brokerUrl";
  public static final String PARAM_ENDPOINT = "endpoint";
  public static final String PARAM_TIMEOUT = "timeout";
  private UimaAsynchronousEngine uimaEEEngine;
  
  public JmsAnalysisEngineServiceStub(Resource owner,
          Parameter[] parameters) throws ResourceInitializationException {
    // read parameters
    String brokerUrl = null;
    String endpoint = null;
    int timeout = 0;
    for (int i = 0; i < parameters.length; i++) {
      if (PARAM_BROKER_URL.equalsIgnoreCase(parameters[i].getName())) {
        brokerUrl = parameters[i].getValue();
      }
      else if (PARAM_ENDPOINT.equalsIgnoreCase(parameters[i].getName())) {
        endpoint = parameters[i].getValue();
      }
      else if (PARAM_TIMEOUT.equalsIgnoreCase(parameters[i].getName())) {
        timeout = Integer.parseInt(parameters[i].getValue());
      }
    }
    
    // initialize UIMA EE Engine
    Map appCtxt = new HashMap();
    appCtxt.put(UimaAsynchronousEngine.ServerUri, brokerUrl);
    appCtxt.put(UimaAsynchronousEngine.Endpoint, endpoint);
    appCtxt.put(UimaAsynchronousEngine.CasPoolSize, 0);
    if (timeout > 0) {
      System.out.println("setting timeout: " + timeout);
      appCtxt.put(UimaAsynchronousEngine.Timeout, timeout);
    }
    uimaEEEngine = new BaseUIMAAsynchronousEngine_impl();
    uimaEEEngine.initialize(appCtxt);
    System.out.println("adapter init complete");
  }


  /**
   * @see org.apache.uima.resource.service.ResourceServiceStub#callGetMetaData()
   */
  public ResourceMetaData callGetMetaData() throws ResourceServiceException {
    //metadata already retrieved during initialization
    try {
      return uimaEEEngine.getMetaData();
    } catch (ResourceInitializationException e) {
      throw new ResourceServiceException(e);
    }
  }

  /**
   * @see org.apache.uima.analysis_engine.service.AnalysisEngineServiceStub#callGetAnalysisEngineMetaData()
   */
  public AnalysisEngineMetaData callGetAnalysisEngineMetaData() throws ResourceServiceException {
    return (AnalysisEngineMetaData) callGetMetaData();
  }

  /**
   * @see org.apache.uima.analysis_engine.service.AnalysisEngineServiceStub#callProcess(CAS)
   */
  public void callProcess(CAS aCAS) throws ResourceServiceException {
    try {
      uimaEEEngine.sendAndReceiveCAS(aCAS);
    } catch (ResourceProcessException e) {
      throw new ResourceServiceException(e);
    }
  }

  /**
   * @see CasObjectProcessorServiceStub#callProcessCas(CAS)
   */
  public void callProcessCas(CAS aCAS) throws ResourceServiceException {
    callProcess(aCAS);
  }


  /**
   * @see org.apache.uima.resource.service.impl.ResourceServiceStub#destroy()
   */
  public void destroy() {
    try {
      uimaEEEngine.stop();
    } catch (Exception e) {
      UIMAFramework.getLogger().log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
   * @see org.apache.uima.collection.impl.service.CasObjectProcessorServiceStub#callBatchProcessComplete()
   */
  public void callBatchProcessComplete() throws ResourceServiceException {
    //Not supported.  Do nothing, rather than throw an exception, since this is called
    //in the normal course of CPE processing.
  }

  /**
   * @see org.apache.uima.collection.impl.service.CasObjectProcessorServiceStub#callCollectionProcessComplete()
   */
  public void callCollectionProcessComplete() throws ResourceServiceException {
    try {
      uimaEEEngine.collectionProcessingComplete();
    } catch (ResourceProcessException e) {
      throw new ResourceServiceException(e);
    }
  }


}
