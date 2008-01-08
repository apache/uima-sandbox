/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jul 8, 2006, 10:47:33 AM
 * source:  RuntimeContext.java
 */
package org.apache.uima.application.metadata.impl;

import org.apache.uima.application.metadata.IRuntimeContext;

/**
 *  Runtime context for UIMA application which includes:
 *      - classpath of the project
 *      - datapath
 *
 */
public class RuntimeContext implements IRuntimeContext {

    protected String                projectName;
    protected String                projectClasspath;
    protected String                projectDatapath;
    
    public RuntimeContext() {
        // NOT to be used
    }

    /**
     * @return the projectClasspath
     */
    public String getProjectClasspath() {
        return projectClasspath;
    }

    /**
     * @param projectClasspath the projectClasspath to set
     */
    public void setProjectClasspath(String projectClasspath) {
        this.projectClasspath = projectClasspath;
    }

    /**
     * @return the projectDatapath
     */
    public String getProjectDatapath() {
        return projectDatapath;
    }

    /**
     * @param projectDatapath the projectDatapath to set
     */
    public void setProjectDatapath(String projectDatapath) {
        this.projectDatapath = projectDatapath;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
