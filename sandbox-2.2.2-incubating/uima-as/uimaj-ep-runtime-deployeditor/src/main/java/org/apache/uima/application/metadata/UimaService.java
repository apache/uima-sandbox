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
public interface UimaService extends MetaDataObject {

    final public int    CASPROCESSOR_CAT_UNKNOWN            = 0;
    final public int    CASPROCESSOR_CAT_COLLECTION_READER  = 1;
    final public int    CASPROCESSOR_CAT_AE                 = 2;
    final public int    CASPROCESSOR_CAT_CAS_CONSUMER       = 4;
    final public int    CASPROCESSOR_CAT_CAS_INITIALIZER    = 5;
    final public int    CASPROCESSOR_CAT_SERVICE            = 6;
    
    public boolean isBuiltin();
    public void setBuiltin(boolean isBuiltin);
    
    public int getStatus();
    public void setStatus(int status);
    public int getStatusDetails();
    public void setStatusDetails(int statusDetails);
    
    public String generateComponentXmlDescriptor (String xmlDescriptorFileName, boolean resolve);
    public String generateXmlDescriptor (String xmlDescriptorFileName, boolean resolve);
    
    /**
     * 
     *  Get the category of this cas processor
     * 
     * @return int  Category of this cas processor
     */
    public int      getCasProcessorCategory();
    public void     setCasProcessorCategory(int casprocCategory);
    
    public String   getInstanceName();
    public void     setInstanceName(String casProcessorName);
    
    public String getCasProcessorDescription();
    
    public String getXmlDescriptor();
    public CpeCasProcessor getCpeCasProcessor();
    
    public ConfigParametersModel getConfigParamsModel();
    
    public DeploymentOverrides getDeploymentOverrides ();
    
    public void setDeploymentOverrides (DeploymentOverrides aParam);
    
    public ConfigParamOverrides getConfigParamOverrides();    
    public void setConfigParamOverrides (ConfigParamOverrides aParam);
    
    public CasProcessorErrorHandling getCasProcessorErrorHandling();
    public void setCasProcessorErrorHandling(CasProcessorErrorHandling casProcessorErrorHandling);
    public int getBatchSize();
    public void setBatchSize(int batchSize);
}
