/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Aug 28, 2007, 10:40:13 AM
 * source:  DeploymentMetaData_Impl.java
 */
package org.apache.uima.aae.deployment.impl;

import org.apache.uima.aae.deployment.AEDeploymentMetaData;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;


/**
 *  Common abstract class for AEDeploymentMetaData_Impl and RemoteAEDeploymentMetaData_Impl
 *
 */
public abstract class DeploymentMetaData_Impl extends MetaDataObject_impl {

    protected AEDeploymentMetaData  parent;
    protected String key;

    /* (non-Javadoc)
     * @see com.ibm.uima.aae.deployment.AEDeploymentMetaData#getParent()
     */
    public AEDeploymentMetaData getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(AEDeploymentMetaData parent) {
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#getKey()
     */
    public String getKey() {
      return key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.uima.aae.deployment.impl.AEDeploymentMetaData#setKey(java.lang.String)
     */
    public void setKey(String key) {
      this.key = key;
    }

}
