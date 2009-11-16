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
package org.apache.uima.simpleserver.as.servlet;

import java.io.File;
import java.util.logging.Level;

import javax.servlet.ServletException;

import org.apache.uima.simpleserver.as.UimaAsSerive;
import org.apache.uima.simpleserver.servlet.SimpleServerServlet;

public class UimaAsSimpleServiceServlet extends SimpleServerServlet {

    static final String UIMA_AS_BROKER_URL = "brokerUrl";
    static final String UIMA_AS_SERVICE_INPUT_QUEUE_NAME = "serviceInputQueueName";
    protected String uimaASBrokerUrl = null;
    protected String uimaASServiceQueueName = null;
    
    // protected UimaAsSerive server;
    
    public UimaAsSimpleServiceServlet() {
        super();
    }

    public UimaAsSimpleServiceServlet(boolean localInit) {
        super(localInit);
    }
    
    @Override
    public void init() throws ServletException {
      System.out.println("Starting UIMA servlet initialization");
      // this.super.init();
      System.out.println("Servlet " + this.getClass().getCanonicalName()
              + " -- initialisation begins");
      this.baseWebappDirectory = new File(getServletContext().getRealPath(""));

      this.server = new UimaAsSerive();
      this.initializationSuccessful = this.initServer();
      declareServletParameters();
    }

    
    /**
     * Check if this is a UIMA AS Service
     * 
     * @return boolean
     */
    protected boolean isUimaASService() {
        uimaASBrokerUrl = getInitParameter(UIMA_AS_BROKER_URL);
        uimaASServiceQueueName = getInitParameter(UIMA_AS_SERVICE_INPUT_QUEUE_NAME);
        if (uimaASBrokerUrl != null && uimaASServiceQueueName != null) {
            return true;
        }
        return false;
    }


    @Override
    protected boolean initServer() {
        File resultSpec = null;
        String resultSpecParamValue = getInitParameter("ResultSpecFile");
        if (resultSpecParamValue != null) {
          resultSpec = new File(this.baseWebappDirectory.getAbsoluteFile(), resultSpecParamValue);
        }
        isUimaASService();
        try {
            ((UimaAsSerive) this.server).configureUimaASService(uimaASBrokerUrl, uimaASServiceQueueName, resultSpec);
            

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "UIMA Simple Service configuaration failed", e);
            return false;
          
        } 
        return true;
    }    
}
