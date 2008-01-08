/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jul 8, 2006, 10:56:39 AM
 * source:  IRuntimeContext.java
 */
package org.apache.uima.application.metadata;

/**
 *  Runtime context for UIMA application which includes:
 *      - classpath of the project
 *      - datapath
 */
public interface IRuntimeContext {

    public String   getProjectClasspath();
    public void     setProjectClasspath(String projectClasspath);
    
    public String   getProjectDatapath();
    public void     setProjectDatapath(String projectDatapath);
    
    public String   getProjectName();
    public void     setProjectName(String projectName);
    
}
