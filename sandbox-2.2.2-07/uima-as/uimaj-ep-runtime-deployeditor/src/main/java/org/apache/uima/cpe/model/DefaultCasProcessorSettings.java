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
import org.apache.uima.collection.metadata.CasProcessorErrorRateThreshold;
import org.apache.uima.collection.metadata.CasProcessorMaxRestarts;
import org.apache.uima.collection.metadata.CasProcessorTimeout;
import org.apache.uima.collection.metadata.CpeCheckpoint;
import org.apache.uima.collection.metadata.CpeDescriptorException;


/**
 * Default settings for each Cas Processor in CPE descriptor.
 *
 */
public class DefaultCasProcessorSettings {
    
    protected CasProcessorErrorHandling errorHandling       = null;
    protected CpeCheckpoint             cpeCheckpoint       = null;
    protected int                       casProcBatchSize    = -1;
    /**
     * 
     */
    public DefaultCasProcessorSettings() {
        super();
    }

    public void removeAll()
    {
        errorHandling = null;
        cpeCheckpoint = null;
        casProcBatchSize = -1;
    }
    /**
     * @return Returns the cpeCheckpoint.
     */
    public CpeCheckpoint getCpeCheckpoint() {
        return cpeCheckpoint;
    }

    /**
     * @param cpeCheckpoint The cpeCheckpoint to set.
     */
    public void setCpeCheckpoint(CpeCheckpoint cpeCheckpoint) {
        this.cpeCheckpoint = cpeCheckpoint;
    }

    /**
     * @return Returns the errorHandling.
     */
    public CasProcessorErrorHandling getErrorHandling() {
        return errorHandling;
    }
    
    public boolean copyErrorHandlingValue (CasProcessorErrorHandling toErrorhandling)
    {
        if (errorHandling == null) {
            // No value to be copied
            return false;
        }
        toErrorhandling.getErrorRateThreshold().setAction(
                errorHandling.getErrorRateThreshold().getAction());        
        toErrorhandling.getErrorRateThreshold().setMaxErrorCount(
                errorHandling.getErrorRateThreshold().getMaxErrorCount());
        toErrorhandling.getErrorRateThreshold().setMaxErrorSampleSize(
                errorHandling.getErrorRateThreshold().getMaxErrorSampleSize());
        
        return true;
    }

    /**
     * @param errorHandling The errorHandling to set.
     */
    public void setErrorHandling(CasProcessorErrorHandling errorHandling) {
        this.errorHandling = errorHandling;
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#getBatchSize()
     */
//    public int getBatchSize() {
//        return cpeCheckpoint.getBatchSize();
//    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#getFilePath()
     */
    public String getFilePath() {
        return cpeCheckpoint.getFilePath();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#getFrequency()
     */
    public int getFrequency() {
        return cpeCheckpoint.getFrequency();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#isTimeBased()
     */
    public boolean isTimeBased() {
        return cpeCheckpoint.isTimeBased();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#setBatchSize(int)
     */
//    public void setBatchSize(int arg0) {
//        cpeCheckpoint.setBatchSize(arg0);
//    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#setFilePath(java.lang.String)
     */
    public void setFilePath(String arg0) throws CpeDescriptorException {
        cpeCheckpoint.setFilePath(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCheckpoint#setFrequency(int, boolean)
     */
    public void setFrequency(int arg0, boolean arg1) {
        cpeCheckpoint.setFrequency(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#getErrorRateThreshold()
     */
    public CasProcessorErrorRateThreshold getErrorRateThreshold() {
        return errorHandling.getErrorRateThreshold();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#getMaxConsecutiveRestarts()
     */
    public CasProcessorMaxRestarts getMaxConsecutiveRestarts() {
        return errorHandling.getMaxConsecutiveRestarts();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#getTimeout()
     */
    public CasProcessorTimeout getTimeout() {
        return errorHandling.getTimeout();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#setErrorRateThreshold(org.apache.uima.collection.metadata.CasProcessorErrorRateThreshold)
     */
    public void setErrorRateThreshold(CasProcessorErrorRateThreshold arg0) {
        errorHandling.setErrorRateThreshold(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#setMaxConsecutiveRestarts(org.apache.uima.collection.metadata.CasProcessorMaxRestarts)
     */
    public void setMaxConsecutiveRestarts(CasProcessorMaxRestarts arg0) {
        errorHandling.setMaxConsecutiveRestarts(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CasProcessorErrorHandling#setTimeout(org.apache.uima.collection.metadata.CasProcessorTimeout)
     */
    public void setTimeout(CasProcessorTimeout arg0) {
        errorHandling.setTimeout(arg0);
    }

    /**
     * @return Returns the casProcBatchSize.
     */
    public int getCasProcBatchSize() {
        return casProcBatchSize;
    }

    /**
     * @param casProcBatchSize The casProcBatchSize to set.
     */
    public void setCasProcBatchSize(int casProcBatchSize) {
        this.casProcBatchSize = casProcBatchSize;
    }
    public void removeCasProcBatchSize() {
        this.casProcBatchSize = -1;
    }
}
