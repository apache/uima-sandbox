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

import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.application.metadata.impl.AbstractUimaCasProcessor;
import org.apache.uima.application.metadata.impl.UimaApplication_Impl;
import org.apache.uima.collection.CasInitializerDescription;
import org.apache.uima.collection.metadata.CpeCollectionReaderCasInitializer;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;


/**
 * 
 *
 */
public class UimaCasInitializer extends AbstractUimaCasProcessor
{

    private CpeCollectionReaderCasInitializer cpeCasInitializer = null;
    private ResourceSpecifier           specifier;
    
    
    /*************************************************************************/
    public UimaCasInitializer () {
        super(UimaCasProcessor.CASPROCESSOR_CAT_CAS_INITIALIZER, null);
    }

    public UimaCasInitializer (UimaApplication app) {
        super(UimaCasProcessor.CASPROCESSOR_CAT_CAS_INITIALIZER, app);
    }
    
    public UimaCasInitializer(CpeDescriptorModel parentModel,
            CpeCollectionReaderCasInitializer cpeCasInitializer,
            UimaApplication app) 
    {
        super(UimaCasProcessor.CASPROCESSOR_CAT_CAS_INITIALIZER, app);
//        this.mCpeDescriptorModel = parentModel;
        this.cpeCasInitializer = cpeCasInitializer;
        xmlDescriptor = cpeCasInitializer.getDescriptor().getInclude().get();
        try {
            specifier = UIMAFramework.getXMLParser().
                                            parseResourceSpecifier(new XMLInputSource(xmlDescriptor));
            if (specifier instanceof CasInitializerDescription) {
                ProcessingResourceMetaData m = ((CasInitializerDescription) specifier).getCasInitializerMetaData();
                configParamDecls = m.getConfigurationParameterDeclarations();
                configParamSettings = m.getConfigurationParameterSettings();
                configParamsModel = new ConfigParametersModel(configParamDecls, configParamSettings);               
                setName(m.getName());
                setDescription(m.getDescription());
            } else {
                specifier = null;
            }
        
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    
    static public UimaCasInitializer createUimaCasProcessor (CpeCollectionReaderCasInitializer cpeCasInitializer,
                                                String xmlDescriptor, UimaApplication app)
    {
        UimaCasInitializer u = new UimaCasInitializer(app);
        u.xmlDescriptor =  xmlDescriptor; // new String(cpeCollReader.getDescriptor().getInclude().get());
        u.instanceName = "Unknown";
        try {
            String resolvedFileName = UimaApplication_Impl.resolveUimaXmlDescriptor(xmlDescriptor);
            u.specifier = UIMAFramework.getXMLParser().
                                    parseResourceSpecifier(new XMLInputSource(resolvedFileName));
            if (u.specifier instanceof CasInitializerDescription) {
                ProcessingResourceMetaData m = ((CasInitializerDescription) u.specifier).getCasInitializerMetaData();
                u.configParamDecls = m.getConfigurationParameterDeclarations();
                u.configParamSettings = m.getConfigurationParameterSettings();
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings);               
                u.instanceName = m.getName();
                u.casProcessorDescription = m.getDescription();
                
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings,
                        cpeCasInitializer.getConfigurationParameterSettings());
                
                // Trace.trace(u.casProcessorName);
            } else {
                Trace.err("Cannot parse CI xml: " + xmlDescriptor);
                u.specifier = null;
            }
        
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
    
    static public UimaCasInitializer createUimaCasProcessor (CasInitializerDescription casInitializerDescription,
                                        String xmlDescriptor, UimaApplication app)
    {
        UimaCasInitializer u = new UimaCasInitializer(app);
        u.xmlDescriptor =  xmlDescriptor; // new String(cpeCollReader.getDescriptor().getInclude().get());
        u.instanceName = "Unknown";
        try {
            u.specifier = UIMAFramework.getXMLParser().
            parseResourceSpecifier(new XMLInputSource(UimaApplication_Impl.resolveUimaXmlDescriptor(xmlDescriptor)));
            if (u.specifier instanceof CasInitializerDescription) {
                ProcessingResourceMetaData m = ((CasInitializerDescription) u.specifier).getCasInitializerMetaData();
                u.configParamDecls = m.getConfigurationParameterDeclarations();
                u.configParamSettings = m.getConfigurationParameterSettings();
                u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings);               
                u.instanceName = m.getName();
                u.casProcessorDescription = m.getDescription();
                // Trace.trace(u.casProcessorName);
            } else {
                u.specifier = null;
            }
            
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

    /*************************************************************************/
        
    public ConfigurationParameterDeclarations getConfigurationParameterDeclarations () {
        return configParamDecls;
    }

    public ConfigurationParameterSettings getConfigurationParameterSettings () {
        return configParamSettings;
    }


    /*************************************************************************/
    
}
