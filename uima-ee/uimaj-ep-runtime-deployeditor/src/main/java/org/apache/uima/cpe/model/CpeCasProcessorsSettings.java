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

/**
 * 
 *
 */
public class CpeCasProcessorsSettings {

    private int             casPoolSize               = 1;
    private int             processingUnitThreadCount = 1;
    private boolean         dropCasOnException        = false; // def value of CPE

    public CpeCasProcessorsSettings() {
        super();
    }

    /**
     * @return Returns the casPoolSize.
     */
    public int getCasPoolSize() {
        return casPoolSize;
    }

    /**
     * @param casPoolSize The casPoolSize to set.
     */
    public void setCasPoolSize(int casPoolSize) {
        this.casPoolSize = casPoolSize;
    }

    /**
     * @return Returns the dropCasOnException.
     */
    public boolean isDropCasOnException() {
        return dropCasOnException;
    }

    /**
     * @param dropCasOnException The dropCasOnException to set.
     */
    public void setDropCasOnException(boolean dropCasOnException) {
        this.dropCasOnException = dropCasOnException;
    }

    /**
     * @return Returns the processingUnitThreadCount.
     */
    public int getProcessingUnitThreadCount() {
        return processingUnitThreadCount;
    }

    /**
     * @param processingUnitThreadCount The processingUnitThreadCount to set.
     */
    public void setProcessingUnitThreadCount(int processingUnitThreadCount) {
        this.processingUnitThreadCount = processingUnitThreadCount;
    }

}
