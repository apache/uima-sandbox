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

import java.util.ArrayList;

import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.application.metadata.ConfigParamOverrides;
import org.apache.uima.application.metadata.OverrideSet;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.PropertyXmlInfo;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
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
 * 
 */
public class ConfigParamOverrides_impl extends MetaDataObject_impl
                                            implements ConfigParamOverrides
{ 
    
    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
    throws InvalidXMLException
    {
        ArrayList params = new ArrayList();
        NodeList childNodes = aElement.getChildNodes();
        // Trace.trace("configParamSettingsSet count: " + childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element)
            {
                Element elem = (Element)curNode;
                if (UimaApplication.TAG_CONFIG_PARAM_OVERRIDE_SET.equals(elem.getTagName())) {
                    // Trace.err("Found ConfigParamOverrideSet");  
                    params.add(aParser.buildObject(elem, aOptions));
                    
                } else {
                    Trace.err("Unknown Tag: " + elem.getTagName());
//                    throw new InvalidXMLException(
//                            InvalidXMLException.UNKNOWN_ELEMENT,
//                            new Object[]{elem.getTagName()});
                }
            }
        }  
        OverrideSet[] paramArr = 
            new OverrideSet[params.size()];
        if (params.size() > 0) {
            params.toArray(paramArr);
        }
        setOverrideSets(paramArr);      
        
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
    
    
    static final long serialVersionUID = -2248322904617280983L;
    
    
    // Name of Cas Processor owning these sets of parameter settings
    private String casProcessorName = "";
    
    /** Set of Configuration Parameters that are not in any group */
    private OverrideSet[] mConfigParamSettingsSets =
        new OverrideSet[0];
    
    public OverrideSet[] getOverrideSets() {
        return mConfigParamSettingsSets;
    }
    
    public void setOverrideSets(OverrideSet[] aParams) {
        if (aParams == null)
        {
            throw new UIMA_IllegalArgumentException(
                    UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT,
                    new Object[]{"null", "aParams", "setConfigParamSettingsSets"});            
        }        
        mConfigParamSettingsSets = aParams;        
    }
    
    
    /**
     * @return Returns the casProcessorName.
     */
    public String getCasProcessorName() {
        return casProcessorName;
    }
    
    
    /**
     * @param casProcessorName The casProcessorName to set.
     */
    public void setCasProcessorName(String casProcessorName) {
        this.casProcessorName = casProcessorName;
    }  
    
}
