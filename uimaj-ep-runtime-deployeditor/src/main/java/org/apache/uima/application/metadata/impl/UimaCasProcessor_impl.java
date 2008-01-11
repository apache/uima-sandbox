/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 4:25:16 PM
 * source:  UimaCasProcessor_impl.java
 */
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

package org.apache.uima.application.metadata.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.application.metadata.ConfigParamOverrides;
import org.apache.uima.application.metadata.DeploymentOverrides;
import org.apache.uima.application.metadata.OverrideSet;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.application.util.UimaXmlParsingUtil;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CasProcessorErrorHandling;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.cpe.model.CasProcessorSettings;
import org.apache.uima.cpe.model.ConfigParameterModel;
import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.cpe.model.CpeCasProcessorModel;
import org.apache.uima.resource.CustomResourceSpecifier;
import org.apache.uima.resource.Parameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.impl.PropertyXmlInfo;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.apache.uima.util.XMLizable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


public class UimaCasProcessor_impl extends AbstractUimaCasProcessor implements UimaCasProcessor                                      
{ 
    static final long serialVersionUID = -2248322904617280983L;
    
    protected Object                    cloneResourceSpecifier;
    private TypePriorities              typePriorities;
    private CpeCasProcessor             cpeCasProcessor;
    private transient CasProcessorSettings        casProcessorSettings = null;
    private CasProcessorErrorHandling   casProcessorErrorHandling = null;
    private int                         batchSize = -1;     // Undefined
    
    // List of ConfigParameterModel
    // private List        paramModelList = new ArrayList();
    // List of ConfigParameterModel Overrides specified in CPE Xml
    // private List        cpeParamModelList = new ArrayList();
    
    
    // Associated CpeCasProcessorModel
//    private CpeCasProcessorModel    mCpeCasProcessorModel = null;
    
    
    // Set of Overrides Set
    // private ConfigParamOverrides        mConfigParamOverrides;

//    private ConfigParametersModel       mConfigParamsModel = null;
    
    /*************************************************************************/

    public UimaCasProcessor_impl() {
        super(CASPROCESSOR_CAT_UNKNOWN, null);
    }

    public UimaCasProcessor_impl(UimaApplication app) {
        super(CASPROCESSOR_CAT_UNKNOWN, app);
    }
    
    /**
     *  Create UimaCasProcessor from Xml Descriptor
     * 
     * @param xmlDescriptor
     * @return
     * @return UimaCasProcessor
     * @throws IOException 
     */
    static public UimaCasProcessor createUimaCasProcessor(String xmlDescriptor,
                                                UimaCasProcessor uimaCasProcessor) 
                    throws InvalidXMLException, ResourceInitializationException, IOException
    {
        XMLizable xmlizable = UimaXmlParsingUtil.parseUimaXmlDescriptor (xmlDescriptor);
        if (xmlizable == null) { 
            return null;
        }
        UimaCasProcessor_impl u = (UimaCasProcessor_impl) uimaCasProcessor;
        if (uimaCasProcessor == null) {
            // Create NEW uimaCasProcessor
            u = new UimaCasProcessor_impl();
        }
        
        u.xmlizableDescriptor = xmlizable;
        u.xmlDescriptor       = xmlDescriptor; // Xml descriptor
        if (xmlizable instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            u.setInstanceName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
            u.typePriorities = getTypePriorities(xmlizable, u.getResourceManager());
            
        } else if (xmlizable instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) xmlizable).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            u.setInstanceName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings(); 
            
        } else if (xmlizable instanceof URISpecifier) {
            URISpecifier specifier = (URISpecifier) xmlizable;
            Trace.err("URISpecifier protocol=" + specifier.getProtocol()
                    + " resource type=" + specifier.getResourceType()
                    + " uri=" + specifier.getUri());
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_SERVICE);
            
        } else if (xmlizable instanceof CustomResourceSpecifier) {
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CUSTOM_RESOURCE_SPECIFIER);
            CustomResourceSpecifier specifier = (CustomResourceSpecifier) xmlizable;
            u.cloneResourceSpecifier = specifier.clone();
            Parameter[] params = specifier.getParameters();
            Trace.err("CustomResourceSpecifier ResourceClassname=" + specifier.getResourceClassName()
                    + " Parameter size=" + params.length);
            for (int i=0; i<params.length; ++i) {
                Trace.err("Param name: " + params[i].getName() +
                          " value: " + params[i].getValue());
            }
        } else {
            Trace.err("UNKNOWN Descriptor: " + xmlizable.getClass().getName());
        }
        
        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings, null);
        createConfigParamOverrides(u.configParamsModel);
        return u;
    }
    
    static public UimaCasProcessor createUimaCasProcessor(CpeCasProcessor cpeCasProcessor, 
                                            ResourceSpecifier specifier, UimaApplication app) 
                    throws InvalidXMLException, ResourceInitializationException
    {
        UimaCasProcessor_impl u = new UimaCasProcessor_impl(app);
        u.xmlDescriptor   = cpeCasProcessor.getDescriptor();
        u.cpeCasProcessor = cpeCasProcessor;
        u.setInstanceName(cpeCasProcessor.getName());
        u.xmlizableDescriptor = specifier;
        if (specifier instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) specifier).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            // u.setCasProcessorName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
            u.typePriorities = getTypePriorities(specifier, u.getResourceManager());
            
        } else if (specifier instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) specifier).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            // u.setCasProcessorName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings();            
        }
        // Trace.trace("Create UimaCasProcessor for "+ cpeCasProcessor.getName());
        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings,
                cpeCasProcessor.getConfigurationParameterSettings());
        createConfigParamOverrides(u.configParamsModel);
        return u;
    }
    
    static protected UimaCasProcessor createUimaCasProcessorFrom (UimaCasProcessor uimaCasProcessor, CpeCasProcessor cpeCasProcessor, 
                                        ResourceSpecifier specifier, UimaApplication app) 
                throws InvalidXMLException, ResourceInitializationException
    {
        UimaCasProcessor_impl u;
        if (uimaCasProcessor != null) {
            u = (UimaCasProcessor_impl) uimaCasProcessor;
        } else {
            u = new UimaCasProcessor_impl(app);
        }
        u.xmlDescriptor = cpeCasProcessor.getDescriptor();
        u.cpeCasProcessor = cpeCasProcessor;
        u.xmlizableDescriptor = specifier;
        if (specifier instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) specifier).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            u.setInstanceName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
            u.typePriorities = getTypePriorities(specifier, u.getResourceManager());
            
        } else if (specifier instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) specifier).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            u.setInstanceName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings();            
        }
        // Trace.trace("Create CpeCasProcessorModel for "+ cpeCasProcessor.getName());
        
        // Thist method is called from "addCpeDescription" when adding a CasProcessor.
        // Since UimaApplication has already the UimaCasProcessor (wchic is supposed
        // to be the same as the one from CPE), the following code is duplicate operation.
//        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings,
//                cpeCasProcessor.getConfigurationParameterSettings());
//        createConfigParamOverrides(u.configParamsModel);
        
        return u;
    }
    
    static public UimaCasProcessor createUimaCasProcessor(CpeCasProcessor cpeCasProcessor, 
                                        XMLizable xmlizable, UimaApplication app) 
                    throws InvalidXMLException, ResourceInitializationException
    {
        UimaCasProcessor_impl u = new UimaCasProcessor_impl(app);
        u.xmlDescriptor = cpeCasProcessor.getDescriptor(); // Xml descriptor
        u.cpeCasProcessor = cpeCasProcessor;
        u.xmlizableDescriptor = xmlizable;
        if (xmlizable instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            u.setInstanceName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
            u.typePriorities = getTypePriorities(xmlizable, u.getResourceManager());
            
        } else if (xmlizable instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) xmlizable).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            u.setInstanceName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings();            
        } else if (xmlizable instanceof URISpecifier) {
            URISpecifier specifier = (URISpecifier) xmlizable;
//            Trace.err("URISpecifier protocol=" + specifier.getProtocol()
//                    + " resource type=" + specifier.getResourceType()
//                    + " uri=" + specifier.getUri());
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_SERVICE);
            // Create a name from URI
            String name = "Unknow";
            try {
                name = new URI(specifier.getUri()).getPath();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            u.setInstanceName(name);            
            
        } else if (xmlizable instanceof CustomResourceSpecifier) {
            CustomResourceSpecifier specifier = (CustomResourceSpecifier) xmlizable;
//            Parameter[] params = specifier.getParameters();
//            Trace.err("CustomResourceSpecifier ResourceClassname=" + specifier.getResourceClassName()
//                    + " Parameter size=" + params.length);
//            for (int i=0; i<params.length; ++i) {
//                Trace.err("Param name: " + params[i].getName() +
//                          " value: " + params[i].getValue());
//            }
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CUSTOM_RESOURCE_SPECIFIER);
            
            // Create a name from ResourceClassName
            u.setInstanceName(specifier.getResourceClassName());            
        }
        
        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings,
                cpeCasProcessor.getConfigurationParameterSettings());
        createConfigParamOverrides(u.configParamsModel);
        return u;
    }
    
    /**
     *  Create UimaCasProcessor from Xml Descriptor
     * 
     * @param xmlDescriptor
     * @return
     * @return UimaCasProcessor
     */
    static public UimaCasProcessor createUimaCasProcessorFromString(String xmlDescriptorString,
                                        UimaApplication app) 
                        throws InvalidXMLException, ResourceInitializationException
    {
        XMLizable xmlizable = UimaXmlParsingUtil.parseUimaXmlDescriptorFromString (xmlDescriptorString);
        if (xmlizable == null) { 
            return null;
        }
        UimaCasProcessor_impl u;
        if ( xmlizable instanceof UimaCasProcessor ) {
            u = (UimaCasProcessor_impl) xmlizable;
        } else {
            u = new UimaCasProcessor_impl(app);
            u.xmlDescriptor = null; // Xml descriptor
        }
        u.xmlizableDescriptor = xmlizable;
        if (xmlizable instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            u.setInstanceName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
            u.typePriorities = getTypePriorities(xmlizable, u.getResourceManager());
        } else if (xmlizable instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) xmlizable).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            u.setInstanceName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings();   
            
        } else if (xmlizable instanceof URISpecifier) {
            URISpecifier specifier = (URISpecifier) xmlizable;
//            Trace.err("URISpecifier protocol=" + specifier.getProtocol()
//                    + " resource type=" + specifier.getResourceType()
//                    + " uri=" + specifier.getUri());
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_SERVICE);
            // Create a name from URI
            String name = "Unknow";
            try {
                name = new URI(specifier.getUri()).getPath();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            u.setInstanceName(name);   
            return u;
        }
        
        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings, null);
        createConfigParamOverrides(u.configParamsModel);
        return u;
    }    
            
    static public TypePriorities getTypePriorities (XMLizable xmlizable, ResourceManager rm) 
                    throws InvalidXMLException, ResourceInitializationException
    {
        TypePriorities tp = null;
//        if (xmlizable instanceof AnalysisEngineDescription) {
//            AnalysisEngineDescription ae = (AnalysisEngineDescription) xmlizable;
//            if ( ae.isPrimitive() ) {
//                AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData(); 
//                tp = ae.getAnalysisEngineMetaData().getTypePriorities();
//            } else {
//                // Aggregate AE
//                if (rm != null) {
//                    tp = CasCreationUtils.mergeDelegateAnalysisEngineTypePriorities(ae, rm);
//                } else {
//                    tp = CasCreationUtils.mergeDelegateAnalysisEngineTypePriorities(ae);
//                }
//            }
//            if (tp != null) {
//                if (rm != null) {
//                    tp.resolveImports(rm);
//                } else {
//                    tp.resolveImports();
//                }
//                Trace.err(ae.getAnalysisEngineMetaData().getName() + ": HAS Type priorities");
//            } else {
//                Trace.err(ae.getAnalysisEngineMetaData().getName() + ": NO Type priorities");
//            }
//        }
        return tp;
    }
    
    /*************************************************************************/
        
    public void initConfigurationParameters (CpeCasProcessorModel model)
    {
        configParamsModel    = model.getConfigParamsModel();
        
        // Attach ConfigParametersModel with each OverrideSet
        ConfigParamOverrides overrides = getConfigParamOverrides();
        if (overrides == null) return;
        OverrideSet[] sets = overrides.getOverrideSets();
        for (int i=0; i<sets.length; ++i) {
            sets[i].setConfigParametersModel(configParamsModel);
        }        
    }
    
    /**
     *  Set Config Param Definitions & Settings (from CR, AE, CC) 
     *  and Overrides (from CPE) which can be null.
     *  These 3 Config Params (Def, Settings, and Override) is encapsulated 
     *  into 1 ConfigParameterModel.
     *  
     * @param decls
     * @param settings
     * @param cpeSettings   Overrides from CPE which can be null
     * @return void
     */
/*    public void setConfParams(ConfigurationParameterDeclarations decls,
                              ConfigurationParameterSettings settings,
                              CasProcessorConfigurationParameterSettings cpeSettings)
    {
        configParamDecls = decls;
        configParamSettings = settings;
        // printNameValuePairs(settings.getParameterSettings());
        
        // Create Param Model List
        ConfigurationParameter[] params = configParamDecls.getConfigurationParameters();
        for (int i=0; i<params.length; ++i) {
            ConfigParameterModel paramModel = new ConfigParameterModel(params[i]);
            paramModel.setValue(configParamSettings.getParameterValue(paramModel.getName()));
            if (cpeSettings != null) {
                Trace.trace("Set cpeValue for " + paramModel.getName());
                if (cpeSettings.getParameterValue(paramModel.getName()) == null) {
                    Trace.trace("   NULL cpeValue");
                }
                paramModel.setCpeValue(cpeSettings.getParameterValue(paramModel.getName()));
                // cpeParamModelList.add(paramModel);
            }
            paramModelList.add(paramModel);
        }
    }
*/    
    
    /*************************************************************************/
    
    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
                                    throws InvalidXMLException
    {
        setInstanceName(aElement.getAttribute(UimaApplication_Impl.TAG_NAME));
        
        //read parameters, commonParameters, and configurationGroups
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element) {
                Element elem = (Element)curNode;
                if (UimaApplication.TAG_IMPORT.equals(elem.getTagName())) {
                    setXmlDescriptor(elem.getAttribute(UimaApplication_Impl.TAG_LOCATION));
                    try {
                        createUimaCasProcessor(UimaApplication_Impl.resolveUimaXmlDescriptor(getXmlDescriptor()), this);
                    } catch (ResourceInitializationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                } else if (UimaApplication.TAG_DEPLOYMENT_OVERRIDES.equals(elem.getTagName())) {
                    setDeploymentOverrides((DeploymentOverrides)(aParser.buildObject(elem, aOptions))); 
                    
                } else if (UimaApplication.TAG_DEPLOYMENT_SETTINGS.equals(elem.getTagName())) {
                    
                } else {
                    Trace.err("Unknown Tag: " + elem.getTagName());
//                    throw new InvalidXMLException(
//                            InvalidXMLException.UNKNOWN_ELEMENT,
//                            new Object[]{elem.getTagName()});
                }
            }
        }  
        
        // Set Override, if any
        if (getConfigParamsModel() != null) {
            ConfigParametersModel models = getConfigParamsModel();
            OverrideSet[] sets = getDeploymentOverrides().getConfigParamOverrides().getOverrideSets();
            if (sets != null && sets[0].getConfigurationParameterSettings() != null) {
                NameValuePair[] pairs = sets[0].getConfigurationParameterSettings().getParameterSettings();
                if (pairs != null && pairs.length > 0) {    
                    createConfigParamOverrides(models, sets[0].getConfigurationParameterSettings());
                } else {
                    Trace.err("EMPTY Overrides");
                }
            } else {
                Trace.err("NO Overrides");
            }
        }

    }
    
    /**
     * @see org.apache.uima.resource.impl.MetaDataObject_impl#getXmlizationInfo()
     */
    protected XmlizationInfo getXmlizationInfo()
    {
        //NOTE: custom XMLization is used for reading.  This information
        //is only used for writing.
        return new XmlizationInfo("configurationParameters",
                new PropertyXmlInfo[]{
                new PropertyXmlInfo("configurationParameters",null),
                new PropertyXmlInfo("commonParameters","commonParameters"),
                new PropertyXmlInfo("configurationGroups",null)
        });
    }
    
    /*************************************************************************/
    
    /**
     * @return Returns the configParamsModel.
     */
//    public ConfigParametersModel getConfigParamsModel() {
//        return configParamsModel;
//    }
    
    
    
    public ConfigParamOverrides getConfigParamOverrides() {
        if (mDeploymentOverrides == null) return null;
        return mDeploymentOverrides.getConfigParamOverrides();
    }
    
//    public void setConfigParamOverrides(ConfigParamOverrides aParam) {
//        mDeploymentOverrides.setConfigParamOverrides(aParam);
//    }

    /**
     * @param casprocCategory The casprocCategory to set.
     */
    public void setCasProcessorCategory(int casprocCategory) {
        this.casprocCategory = casprocCategory;
    }

    /**
     * @return Returns the cpeCasProcessor.
     */
    public CpeCasProcessor getCpeCasProcessor() {
        return cpeCasProcessor;
    }

    /**
     * @param cpeCasProcessor The cpeCasProcessor to set.
     */
    public void setCpeCasProcessor(CpeCasProcessor cpeCasProcessor) {
        this.cpeCasProcessor = cpeCasProcessor;
    }

    /**
     * @return Returns the casProcessorSettings.
     */
    public CasProcessorSettings getCasProcessorSettings() {
        return casProcessorSettings;
    }

    /**
     * @param casProcessorSettings The casProcessorSettings to set.
     */
    public void setCasProcessorSettings(CasProcessorSettings casProcessorSettings) {
        this.casProcessorSettings = casProcessorSettings;
    }

    /**
     * @return Returns the casProcessorErrorHandling.
     */
    public CasProcessorErrorHandling getCasProcessorErrorHandling() {
        return casProcessorErrorHandling;
    }

    /**
     * @param casProcessorErrorHandling The casProcessorErrorHandling to set.
     */
    public void setCasProcessorErrorHandling(
            CasProcessorErrorHandling casProcessorErrorHandling) {
        this.casProcessorErrorHandling = casProcessorErrorHandling;
    }

    /**
     * @return Returns the batchSize.
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * @param batchSize The batchSize to set.
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public Object getCloneResourceSpecifier() {
        return cloneResourceSpecifier;
    }
        
}
