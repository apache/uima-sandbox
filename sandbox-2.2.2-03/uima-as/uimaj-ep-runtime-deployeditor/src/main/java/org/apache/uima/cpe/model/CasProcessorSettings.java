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

package org.apache.uima.cpe.model;

import org.apache.uima.collection.metadata.CasProcessorErrorHandling;
import org.apache.uima.collection.metadata.CpeCheckpoint;




/**
 * 
 *
 */
public class CasProcessorSettings {

    protected DefaultCasProcessorSettings   defaultSettings;    // Shared copy
    protected CasProcessorErrorHandling     errorHandling = null;
    protected CpeCheckpoint                 cpeCheckpoint = null;
    
    /**
     * 
     */
    private CasProcessorSettings() {        
    }
    
    public CasProcessorSettings(DefaultCasProcessorSettings defaultSettings) {
        super();
        this.defaultSettings = defaultSettings;
    }

    /**
     * @return Returns the cpeCheckpoint.
     */
    public CpeCheckpoint getCpeCheckpoint() {
        if (cpeCheckpoint != null) {
            return cpeCheckpoint;
        }
        return defaultSettings.cpeCheckpoint;
    }

    /**
     * @param cpeCheckpoint The cpeCheckpoint to set.
     */
    public void setCpeCheckpoint(CpeCheckpoint cpeCheckpoint) {
        this.cpeCheckpoint = cpeCheckpoint;
    }

    /**
     * @return Returns the defaultSettings.
     */
    public DefaultCasProcessorSettings getDefaultSettings() {
        return defaultSettings;
    }

    /**
     * @param defaultSettings The defaultSettings to set.
     */
    public void setDefaultSettings(DefaultCasProcessorSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    /**
     * @return Returns the errorHandling.
     */
    public CasProcessorErrorHandling getErrorHandling() {
        if (errorHandling != null) {
            return errorHandling;
        }
        return defaultSettings.getErrorHandling();
    }

    /**
     * @param errorHandling The errorHandling to set.
     */
    public void setErrorHandling(CasProcessorErrorHandling errorHandling) {
        this.errorHandling = errorHandling;
    }




}
