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

import java.io.IOException;
import java.util.List;

import org.apache.uima.application.metadata.impl.AbstractUimaCasProcessor;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.cpe.model.DefaultCasProcessorSettings;
import org.apache.uima.cpe.model.UimaCollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLizable;
import org.xml.sax.SAXException;


/**
    Use " public XMLSerializer(boolean isFormattedOutput)" for formatting xml
    public String prettyPrintModel (XMLizable trueDescriptor) {

    UimaApplicationModel -> UimaApplication
        List of CasProcSettingsOverride (casProcessorName) 
          - getConfigParamSettingsSet(setName)
         CasProcessorSettings
         ConfigParamSettingsSets
             ConfigParamSettingsSet[]
                 ConfigParamSettingsSet (need a link to ConfigParametersModel)
                     
                     
        CpeDescriptorModel -> CpeDescription
         - UimaApplicationModel
         List of CpeCasProcessorModel
              - getConfigParametersModel ()
             ConfigParametersModel
                 List of ConfigParameterModel



    <?xml version="1.0" encoding="UTF-8" ?>
    <uimaApplication uima="1.3">
        <!-- Link to legacy CPE descriptor -->
        <cpeDescription href="..."   />
        
        <!-- Common Deployment Settings for ALL cas processors -->      
        <casProcessorCommonSettings>
            <casProcessors casPoolSize="3" processingUnitThreadCount="1"/> <!-- get from CPE Xml -->
        </casProcessorCommonSettings>
     
        <deploymentDefaultSettings>
          <errorHandling>
              <errorRateThreshold action="terminate" count="100" sample_size="1000"/>
              <maxConsecutiveRestarts action="terminate" value="30"/>
              <timeout max="10000"/>
          </errorHandling>
          <checkpoint batch="10000" frequency="" />
        </deploymentDefaultSettings>
     
        <!-- BEGIN Per CAS Processor -->
        <uimaCasProcessor name="Meeting Detector TAE">
        
            <import location="xml_descriptor.xml" />
           
            <deploymentSettings>
                <!-- get from CPE Xml -->
            </deploymentSettings>
         
            <deploymentOverrides>
                <!-- may have other overrides than Configuration Parameters -->
            
                <!-- Overrides for UIMA Configuration Parameters -->
                <configParamOverrides>
     
                    <configParamOverrideSet name="setName 1" >
                        <description>
                            Description of ConfigParamSettingsSet
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
             
                    </configParamOverrideSet>
         
                    <configParamOverrideSet name="setName 2" >
                    </configParamOverrideSet>
         
                </configParamOverrides>
            </deploymentOverrides>
        </uimaCasProcessor>
        <!-- END Per CAS Processor -->
     
    </uimaApplication>

 */
public interface UimaApplication extends MetaDataObject 
{
    final public static String TAG_COLLECTION_READER            = "uimaCollectionReader";
    final public static String TAG_CAS_PROCESSOR                = "uimaCasProcessor";
    final public static String TAG_NAME                         = "name";
    final public static String TAG_DESCRIPTION                  = "description";
    final public static String TAG_IMPORT                       = "import";
    final public static String TAG_LOCATION                     = "location";
    
    final public static String TAG_DEPLOYMENT_DEFAULT_SETTINGS  = "deploymentDefaultSettings";
    final public static String TAG_DEPLOYMENT_SETTINGS          = "deploymentSettings";
    final public static String TAG_DEPLOYMENT_OVERRIDES         = "deploymentOverrides";

    final public static String TAG_CONFIG_PARAM_OVERRIDES       = "configParamOverrides";
    final public static String TAG_CONFIG_PARAM_OVERRIDE_SET    = "configParamOverrideSet";

    static final public int STATUS_DEPLOYMENT_NODE_OK           = 0;
    static final public int STATUS_DEPLOYMENT_NODE_WARNING      = 1;
    static final public int STATUS_DEPLOYMENT_NODE_ERROR        = 2;
    
    static final public int STATUS_DETAILS_FILE_NOT_FOUND       = 1;

    public IRuntimeContext getRuntimeContext();
    public void setRuntimeContext(IRuntimeContext runtimeContext);
    
    /**
     * Get UIMA CPE descriptor.
     * 
     * @return String
     */
    public String getCpeDescriptor ();
    
    /**
     * Set UIMA CPE descriptor.
     * 
     * @param descriptor
     * @return void
     */
    // public void setCpeDescriptor (String descriptor);
    
    // public CpeDescriptorModel  getCpeDescriptorModel ();
    
    /**
     * Get all UIMA Collection Readers.
     * 
     * @return An arry of UimaCollectionReader
     */
    public List getUimaCollectionReaders();
    public UimaCollectionReader getUimaCollectionReader ();
    public UimaCollectionReader getUimaCollectionReader (String name);
    
    public List getUimaCasInitializers();
    
    /**
     * Get all UIMA cas processors.
     * 
     * @return An arry of UimaCasProcessor
     */
    // public UimaCasProcessor[] getUimaCasProcessors();
    public List getUimaCasProcessors();
    public UimaCasProcessor getUimaCasProcessor (String name);
    
    // public void setUimaCasProcessors(UimaCasProcessor[] aParams);
    
    /**
     *  Get UIMA cas processor by name.
     * 
     * @param casProcessorName
     * @return UimaCasProcessor
     */
//    public UimaCasProcessor getUimaCasProcessor(String casProcessorName);
    
    public Object addXMLizable (String xmlizableXmlFile, XMLizable xmlizable) throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException;
    public void addCpeDescription (String cpeXml, CpeDescription cpeDescription)
                    throws InvalidXMLException, ResourceInitializationException, 
                           CpeDescriptorException;
    // public void addCasProcessor (String cpeXml, CpeDescription cpeDescription) throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException;
    // public void addCasProcessorsFromCpeDescription (String cpeXml, CpeDescription cpeDescription) throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException;
    
    public void removeAll ();
    public boolean deleteUimaCasProcessor (AbstractUimaCasProcessor u);
    public boolean moveUimaCasProcessor (AbstractUimaCasProcessor u, boolean moveUp);
    
    public String generateCpeXmlDescriptor (String xmlDescriptorFileName, boolean resolve)
                    throws CpeDescriptorException, SAXException, IOException;
    
    public TypePriorities getMergedTypePriorities () 
                throws InvalidXMLException, ResourceInitializationException;
    
    /**
     * Check if AbstractUimaCasProcessor is movable
     * 
     * @param u
     * @return int  0: non-movable; 0x01: up; 0x02: down; 0x003; both dir
     */
    public int isMovable (AbstractUimaCasProcessor u);
    
    public void setCurrentDirectory (String currDir);
    
    public ResourceManager getResourceManager ();
    public void setResourceManager (ResourceManager rm);
    
    /**
     * @return Returns the cpeConfiguration.
     */
    public CpeConfiguration getCpeConfiguration();
    
    /**
     * @param cpeConfiguration The cpeConfiguration to set.
     */
    public void setCpeConfiguration(CpeConfiguration cpeConfiguration);
    
    /**
     * @return Returns the defaultCasProcessorSettings.
     */
    public DefaultCasProcessorSettings getDefaultCasProcessorSettings();

    /**
     * @param defaultCasProcessorSettings The defaultCasProcessorSettings to set.
     */
    public void setDefaultCasProcessorSettings (DefaultCasProcessorSettings defaultCasProcessorSettings);
    
}
