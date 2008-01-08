/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Feb 24, 2006, 10:34:20 PM
 * source:  CasProcessorSettings.java
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
