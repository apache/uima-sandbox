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

import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.MetaDataObject;


/**
 * 
                    <overrideSet name="set# 1"  default >
                        <description>
                           Describe about this override set
                        </description>                      
                        <configurationParameterSettings> <!-- Same syntax as UIMA -->
                        </configurationParameterSettings>                   
                    </overrideSet>
 *
 */
public interface OverrideSet extends MetaDataObject {

    public String getName ();
    public void setName (String name);
    
    public String getDescription();
    public void setDescription(String description);
    
    public boolean isSelected ();
    public void    setSelected (boolean selected);  
    
    public ConfigParametersModel getConfigParametersModel ();
    
    public void setConfigParametersModel (ConfigParametersModel aParam);
    
    /**
     * Get a configuration parameter settings.
     * 
     * @return ConfigurationParameterSettings
     */
    public ConfigurationParameterSettings getConfigurationParameterSettings();
    
    public void setConfigurationParameterSettings(ConfigurationParameterSettings aParam);

    public void printMe();
}
