/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2006, 4:25:16 PM
 * source:  ConfigParamSettingsSets.java
 */
package org.apache.uima.application.metadata;

import org.apache.uima.resource.metadata.MetaDataObject;


/**
                <!-- Overrides for UIMA Configuration Parameters -->
                <configParamOverrides>
                
                    <overrideSet name="set# 1"  default >
                        <description>
                           Describe about this override set
                        </description>                      
                        <configurationParameterSettings> <!-- Same syntax as UIMA -->
                        </configurationParameterSettings>                   
                    </overrideSet>
                    
                    <overrideSet name="set# 2" >
                    </overrideSet>
                    
                </configParamOverrides>             

 * 
 */
public interface ConfigParamOverrides extends MetaDataObject {

    public OverrideSet[] getOverrideSets();
    
    public void setOverrideSets(OverrideSet[] aParams);
    
}
