/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Mar 26, 2006, 4:07:18 PM
 * source:  CpeCasProcessorsSettings.java
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
