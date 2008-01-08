/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 4:25:16 PM
 * source:  ConfigParamSettingsSets.java
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
