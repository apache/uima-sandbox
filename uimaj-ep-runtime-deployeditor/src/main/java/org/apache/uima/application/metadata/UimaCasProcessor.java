/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 4:25:16 PM
 * source:  UimaCasProcessor.java
 */
package org.apache.uima.application.metadata;

import org.apache.uima.collection.metadata.CasProcessorErrorHandling;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.resource.metadata.MetaDataObject;


/**
 *  Class that encaptulates the concept of Cas Processor from multiple UIMA classes:
 *          - CasProcessor
 *          - CpeCasProcessor
 *          - CollectionReader
 *          - ...
 * 
        <uimaCasProcessor casProcessorName="Meeting Detector TAE">
            <!-- get Cas Processor Definition from CPE Xml -->
            
            <deploymentSettings>
                <!-- get from CPE Xml -->
            </deploymentSettings>
           
            <deploymentOverrides>
                <!-- may have other overrides than Configuration Parameters -->
                
                <!-- Overrides for UIMA Configuration Parameters -->
                <configParamOverrides>
                
                    <overrideSet name="set# 1"  default >
                        <description>
                           Describe about this override set
                        </description>                      
                        <configurationParameterSettings> <!-- Same syntax as UIMA -->
                          <nameValuePair>
                            <name>Locations</name>
                            <value>
                              <array>
                                <string>a</string>
                                <string>b</string>
                                <string>c</string>
                                <string>d</string>
                              </array>
                            </value>
                          </nameValuePair> 
                        </configurationParameterSettings>                   
                    </overrideSet>
                    
                    <overrideSet name="set# 2" >
                    </overrideSet>
                    
                </configParamOverrides>             
            </deploymentOverrides>
        </uimaCasProcessor>           
        <!-- END Per CAS Processor -->
 *
 */
public interface UimaCasProcessor extends MetaDataObject {

    final public int    CASPROCESSOR_CAT_UNKNOWN            = 0;
    final public int    CASPROCESSOR_CAT_COLLECTION_READER  = 1;
    final public int    CASPROCESSOR_CAT_AE                 = 2;
    final public int    CASPROCESSOR_CAT_CAS_CONSUMER       = 4;
    final public int    CASPROCESSOR_CAT_CAS_INITIALIZER    = 5;
    final public int    CASPROCESSOR_CAT_SERVICE            = 6;
    final public int    CASPROCESSOR_CAT_CUSTOM_RESOURCE_SPECIFIER = 7;
    
    public boolean isBuiltin();
    public void setBuiltin(boolean isBuiltin);
    
    public int getStatus();
    public void setStatus(int status);
    public int getStatusDetails();
    public void setStatusDetails(int statusDetails);
    
    public String generateComponentXmlDescriptor (String xmlDescriptorFileName, boolean resolve);
    public String generateXmlDescriptor (String xmlDescriptorFileName, boolean resolve, boolean updateOverrides);
    
    /**
     * 
     *  Get the category of this cas processor
     * 
     * @return int  Category of this cas processor
     */
    public int      getCasProcessorCategory();
    public void     setCasProcessorCategory(int casprocCategory);
    
    public Object getCloneResourceSpecifier();
    
    public String   getInstanceName();
    public void     setInstanceName(String casProcessorName);
    
    public String getCasProcessorDescription();
    
    public String getXmlDescriptor();
    public CpeCasProcessor getCpeCasProcessor();
    public void setCpeCasProcessor(CpeCasProcessor cpeCasProcessor);
    
    public ConfigParametersModel getConfigParamsModel();
    
    public DeploymentOverrides getDeploymentOverrides ();
    
//    public void setDeploymentOverrides (DeploymentOverrides aParam);
    
    public ConfigParamOverrides getConfigParamOverrides();    
//    public void setConfigParamOverrides (ConfigParamOverrides aParam);
    
    public CasProcessorErrorHandling getCasProcessorErrorHandling();
    public void setCasProcessorErrorHandling(CasProcessorErrorHandling casProcessorErrorHandling);
    public int getBatchSize();
    public void setBatchSize(int batchSize);
}
