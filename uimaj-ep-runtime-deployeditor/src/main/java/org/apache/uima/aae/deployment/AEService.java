package org.apache.uima.aae.deployment;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.InvalidXMLException;

public interface AEService extends MetaDataObject {

    public ResourceSpecifier resolveTopAnalysisEngineDescription(boolean recursive) throws InvalidXMLException;
    public ResourceSpecifier resolveTopAnalysisEngineDescription(ResourceManager aResourceManager, boolean recursive) throws InvalidXMLException;

    public ResourceSpecifier getTopAnalysisEngineDescription() throws InvalidXMLException;
    public ResourceSpecifier getTopAnalysisEngineDescription(ResourceManager aResourceManager) throws InvalidXMLException;
    
    /**
     * @return the analysisEngineDeploymentMetaData
     * @throws InvalidXMLException 
     */
    public AEDeploymentMetaData getAnalysisEngineDeploymentMetaData() throws InvalidXMLException;
    public AEDeploymentMetaData getAnalysisEngineDeploymentMetaData(ResourceManager aResourceManager) throws InvalidXMLException;

    /**
     * @param analysisEngineDeploymentMetaData the analysisEngineDeploymentMetaData to set
     */
    public void setAnalysisEngineDeploymentMetaData(
            AEDeploymentMetaData analysisEngineDeploymentMetaData);

    /**
     * @return the brokerURL
     */
    public String getBrokerURL();

    /**
     * @param brokerURL the brokerURL to set
     */
    public void setBrokerURL(String brokerURL);

    /**
     * @return the endPoint
     */
    public String getEndPoint();

    /**
     * @param endPoint the endPoint to set
     */
    public void setEndPoint(String endPoint);
    
    /**
     * @return the prefetch
     */
    public int getPrefetch();

    /**
     * @param prefetch the prefetch to set
     */
    public void setPrefetch(int prefetch);

    public Import getImportDescriptor();
    public void setImportDescriptor(Import importDescriptor);
    
//    /**
//     * @return the importByLocation
//     */
//    public String getImportByLocation();

//    /**
//     * @param importByLocation the importByLocation to set
//     */
//    public void setImportByLocation(String importByLocation);

//    /**
//     * @return the topDescriptor
//     */
//    public String getTopDescriptor();
//
//    /**
//     * @param topDescriptor the topDescriptor to set
//     */
//    public void setTopDescriptor(String topDescriptor);

}