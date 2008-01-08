/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 21, 2006, 12:24:13 PM
 * source:  DeploymentOverrides.java
 */
package org.apache.uima.application.metadata;

import org.apache.uima.resource.metadata.MetaDataObject;


/**
 * 
 *
 */
public interface DeploymentOverrides extends MetaDataObject {

    public ConfigParamOverrides getConfigParamOverrides ();
    public void setConfigParamOverrides (ConfigParamOverrides aParam);
    
}
