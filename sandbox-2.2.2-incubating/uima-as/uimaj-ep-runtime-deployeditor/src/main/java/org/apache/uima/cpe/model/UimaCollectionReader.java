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

package org.apache.uima.cpe.model;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.application.metadata.DeploymentOverrides;
import org.apache.uima.application.metadata.OverrideSet;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.application.metadata.impl.AbstractUimaCasProcessor;
import org.apache.uima.application.metadata.impl.UimaApplication_Impl;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 *
 */
public class UimaCollectionReader extends AbstractUimaCasProcessor
{

    private CpeCollectionReader         cpeCollectionReader = null;
    private ResourceSpecifier           specifier;
    
    /*************************************************************************/
    
    public UimaCollectionReader () {
        super(UimaCasProcessor.CASPROCESSOR_CAT_COLLECTION_READER, null);
    }
    
    public UimaCollectionReader (UimaApplication app) {
        super(UimaCasProcessor.CASPROCESSOR_CAT_COLLECTION_READER, app);
    }

    public UimaCollectionReader(CpeDescriptorModel parentModel,
                                    CpeCollectionReader cpeCollReader, UimaApplication app) 
    {
        super(UimaCasProcessor.CASPROCESSOR_CAT_COLLECTION_READER, app);
//        this.mCpeDescriptorModel = parentModel;
        this.cpeCollectionReader = cpeCollReader;
        xmlDescriptor = cpeCollReader.getDescriptor().getInclude().get();
        try {
            specifier = UIMAFramework.getXMLParser().
                                            parseResourceSpecifier(new XMLInputSource(xmlDescriptor));
            if (specifier instanceof CollectionReaderDescription) {
                ProcessingResourceMetaData m = ((CollectionReaderDescription) specifier).getCollectionReaderMetaData();
                configParamDecls = m.getConfigurationParameterDeclarations();
                configParamSettings = m.getConfigurationParameterSettings();
                configParamsModel = new ConfigParametersModel(configParamDecls, configParamSettings);               
                setName(m.getName());
                setInstanceName(m.getName());
                setDescription(m.getDescription());
                createConfigParamOverrides(configParamsModel);
                // Trace.trace(name);
            } else {
                specifier = null;
            }
            xmlizableDescriptor = specifier;
        
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    
    static public UimaCollectionReader createUimaCasProcessor (CpeCollectionReader cpeCollReader_NOT_USED,
                                                String xmlDescriptor, UimaApplication app)
    {
        UimaCollectionReader u = new UimaCollectionReader(app);
        // u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
        u.xmlDescriptor =  xmlDescriptor; // new String(cpeCollReader.getDescriptor().getInclude().get());
        u.instanceName = "Unknown Collection Reader";
        try {
            String resolvedFileName = UimaApplication_Impl.resolveUimaXmlDescriptor(xmlDescriptor);
            if (resolvedFileName == null) {
                // Trace.err("cannot resolve file: " + resolvedFileName);
                u.status = UimaApplication.STATUS_DEPLOYMENT_NODE_ERROR;
                u.statusDetails = UimaApplication.STATUS_DETAILS_FILE_NOT_FOUND;
                return u;
            }
            File f = new File (resolvedFileName);
            if ( ! f.exists() ) {
                Trace.err("cannot find file: " + resolvedFileName);
                u.status = UimaApplication.STATUS_DEPLOYMENT_NODE_ERROR;
                u.statusDetails = UimaApplication.STATUS_DETAILS_FILE_NOT_FOUND;
                return u;
            }
            u.specifier = UIMAFramework.getXMLParser().
                                    parseResourceSpecifier(new XMLInputSource(resolvedFileName));
            if (u.specifier instanceof CollectionReaderDescription) {
                ProcessingResourceMetaData m = ((CollectionReaderDescription) u.specifier).getCollectionReaderMetaData();
                u.configParamDecls = m.getConfigurationParameterDeclarations();
                u.configParamSettings = m.getConfigurationParameterSettings();
                // u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings);               
                u.instanceName = m.getName();
                u.casProcessorDescription = m.getDescription();
                
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings,
                        cpeCollReader_NOT_USED.getConfigurationParameterSettings());
                createConfigParamOverrides(u.configParamsModel);
                // Trace.trace(u.casProcessorName);
            } else {
                Trace.err("Cannot parse CR xml: " + xmlDescriptor);
                u.specifier = null;
            }
            u.xmlizableDescriptor = u.specifier;
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            u.specifier = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            u.specifier = null;
        }       
        return u;
    }
    
    static public UimaCollectionReader createUimaCasProcessor (CollectionReaderDescription collReaderDescription_NOT_USED,
                                            String xmlDescriptor, UimaApplication app)
    {
//        Trace.trace("UimaCollectionReader XML: " + xmlDescriptor);
        UimaCollectionReader u = new UimaCollectionReader(app);
//      u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
        u.xmlDescriptor =  xmlDescriptor; // new String(cpeCollReader.getDescriptor().getInclude().get());
        u.instanceName = "Unknown";
        try {
            u.specifier = UIMAFramework.getXMLParser().
            parseResourceSpecifier(new XMLInputSource(UimaApplication_Impl.resolveUimaXmlDescriptor(xmlDescriptor)));
            if (u.specifier instanceof CollectionReaderDescription) {
                ProcessingResourceMetaData m = ((CollectionReaderDescription) u.specifier).getCollectionReaderMetaData();
                u.configParamDecls = m.getConfigurationParameterDeclarations();
                u.configParamSettings = m.getConfigurationParameterSettings();
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings);               
                u.instanceName = m.getName();
                u.casProcessorDescription = m.getDescription();
                createConfigParamOverrides(u.configParamsModel);
                // Trace.trace(u.casProcessorName);
            } else {
                u.specifier = null;
            }
            u.xmlizableDescriptor = u.specifier;
            
        } catch (InvalidXMLException e) {
//          TODO Auto-generated catch block
            e.printStackTrace();
            u.specifier = null;
        } catch (IOException e) {
//          TODO Auto-generated catch block
            e.printStackTrace();
            u.specifier = null;
        }       
        return u;
    }
    
    static protected CollectionReaderDescription createCollectionReaderDescription (UimaCollectionReader u, String xmlDescriptor)
    {        
        String resolvedFileName = UimaApplication_Impl.resolveUimaXmlDescriptor(xmlDescriptor);
        if (resolvedFileName == null) {
            // Trace.err("cannot resolve file: " + resolvedFileName);
            u.status = UimaApplication.STATUS_DEPLOYMENT_NODE_ERROR;
            u.statusDetails = UimaApplication.STATUS_DETAILS_FILE_NOT_FOUND;
            return null;
        }
        File f = new File (resolvedFileName);
        if ( ! f.exists() ) {
            Trace.err("cannot find file: " + resolvedFileName);
            u.status = UimaApplication.STATUS_DEPLOYMENT_NODE_ERROR;
            u.statusDetails = UimaApplication.STATUS_DETAILS_FILE_NOT_FOUND;
            return null;
        }
        try {
            u.specifier = UIMAFramework.getXMLParser().
                                    parseResourceSpecifier(new XMLInputSource(resolvedFileName));
            if (u.specifier instanceof CollectionReaderDescription) {
                ProcessingResourceMetaData m = ((CollectionReaderDescription) u.specifier).getCollectionReaderMetaData();
                u.configParamDecls = m.getConfigurationParameterDeclarations();
                u.configParamSettings = m.getConfigurationParameterSettings();
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings);               
                u.instanceName = m.getName();
                u.casProcessorDescription = m.getDescription();
                // createConfigParamOverrides(u.configParamsModel);
                // Trace.trace(u.casProcessorName);
            } else {
                u.specifier = null;
            }
            u.xmlizableDescriptor = u.specifier;
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            u.specifier = null;
        } catch (IOException e) {
            e.printStackTrace();
            u.specifier = null;
        }

        return (CollectionReaderDescription) u.specifier;
    }

    /*************************************************************************/
        
    public ConfigurationParameterDeclarations getConfigurationParameterDeclarations () {
        return configParamDecls;
    }

    public ConfigurationParameterSettings getConfigurationParameterSettings () {
        return configParamSettings;
    }


    /*************************************************************************/
    
    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
                                    throws InvalidXMLException
    {
        // Trace.trace();
        setInstanceName(aElement.getAttribute(UimaApplication_Impl.TAG_NAME));
        
        //read parameters, commonParameters, and configurationGroups
//        ArrayList params = new ArrayList();
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element) {
                Element elem = (Element)curNode;
                if (UimaApplication.TAG_IMPORT.equals(elem.getTagName())) {
                    setXmlDescriptor(elem.getAttribute(UimaApplication_Impl.TAG_LOCATION));
                    createCollectionReaderDescription(this, getXmlDescriptor());
                    
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
        // return XMLIZATION_INFO;
        return new XmlizationInfo(null, null);
        //this object has custom XMLization routines
    }
    
}
