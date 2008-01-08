/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 12, 2006, 1:57:56 PM
 * source:  ConfigParametersModel.java
 */
package org.apache.uima.cpe.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.tools.debug.util.Trace;


/**
 * 
 *
 */
public class ConfigParametersModel {
    
    // List of ConfigParameterModel
    private List        paramModelList = new ArrayList();

    // List of ConfigParameterModel Overrides specified in CPE Xml
    private List        cpeParamModelList = null; // new ArrayList();
    
    private ConfigurationParameterDeclarations  paramDeclarations;
    private ConfigurationParameterSettings      paramSettings;

    public ConfigParametersModel(ConfigurationParameterDeclarations decls,
            ConfigurationParameterSettings settings)             
    {
        this(decls, settings, null);
    }
    
    /**
     *  Create config. param model by setting CPE's overrides to the
     *  sepecified values or default values from the descriptor
     *  
     * @param decls         Param definitions from Xml descriptor
     * @param settings      Param values from Xml descriptor
     * @param cpeSettings   Settings for CPE's overrides. If null, the default values
     *                      from Xml descriptor (from "settings") will be used
     */
    public ConfigParametersModel(ConfigurationParameterDeclarations decls,
                                 ConfigurationParameterSettings settings,
                                 CasProcessorConfigurationParameterSettings cpeSettings)
    {
        super();
        paramDeclarations = decls;
        paramSettings = settings;
        if (paramDeclarations == null) return;
        // printNameValuePairs(paramSettings.getParameterSettings());
        
        // Create Param Model List
        ConfigurationParameter[] params = paramDeclarations.getConfigurationParameters();
        for (int i=0; i<params.length; ++i) {
            ConfigParameterModel paramModel = new ConfigParameterModel(params[i]);
            // Set "default" param values as defined in XML descriptor 
            paramModel.setValue(paramSettings.getParameterValue(paramModel.getName()));
            
            // Set param values for CPE
            if (cpeSettings != null) {
                // Trace.trace("Set cpeValue for " + paramModel.getName());
                Object cpeValue = cpeSettings.getParameterValue(paramModel.getName());
                if (cpeValue == null) {
                    // Clone Value
                    // Trace.trace("   Clone cpeValue");
                    cpeValue = paramSettings.getParameterValue(paramModel.getName());
                } else {
                    
                }
                paramModel.setCpeValue(cpeValue);
                if (cpeParamModelList == null) {
                    cpeParamModelList = new ArrayList();
                }
                cpeParamModelList.add(paramModel);
            } else {
                // Use default value from descriptor
                paramModel.setCpeValue(paramSettings.getParameterValue(paramModel.getName()));
            }
            paramModelList.add(paramModel);
        }
    }
    
    // see AbstractUimaCasProcessor.createConfigParamOverrides
    protected void setCpeConfigParamSettings (ConfigurationParameterSettings cpeSettings)
    {
        if (cpeParamModelList == null) {
            cpeParamModelList = new ArrayList();
        }
        for (int i=0; i<paramModelList.size(); ++i) {
            ConfigParameterModel paramModel = (ConfigParameterModel) paramModelList.get(i);

            // Set param values for CPE
            // Trace.trace("Set cpeValue for " + paramModel.getName());
            Object cpeValue = cpeSettings.getParameterValue(paramModel.getName());
            if (cpeValue == null) {
                // Use Default Value
                cpeValue = paramSettings.getParameterValue(paramModel.getName());
            }
            paramModel.setCpeValue(cpeValue);
            cpeParamModelList.add(paramModel);
        }
    }
    
    public ConfigParameterModel getConfigParameterModel (String paramName)
    {
        for (int i=0; i<paramModelList.size(); ++i) {
            if (paramName.equals(((ConfigParameterModel) paramModelList.get(i)).getName())) {
                return (ConfigParameterModel) paramModelList.get(i);
            }
        }
        Trace.trace("Cannot find ConfigParameterModel:" + paramName);
        return null;
    }
    
    /**
     * @return Returns the paramDeclarations.
     */
    public ConfigurationParameterDeclarations getParamDeclarations() {
        return paramDeclarations;
    }

    /**
     * @param paramDeclarations The paramDeclarations to set.
     */
    public void setParamDeclarations(
            ConfigurationParameterDeclarations paramDeclarations) {
        this.paramDeclarations = paramDeclarations;
    }

    /**
     * @return Returns the paramSettings.
     */
    public ConfigurationParameterSettings getParamSettings() {
        return paramSettings;
    }

    /**
     * @param paramSettings The paramSettings to set.
     */
    public void setParamSettings(ConfigurationParameterSettings paramSettings) {
        this.paramSettings = paramSettings;
    }

    /**
     * @return Returns the paramModelList.
     */
    public List getParamModelList() {
        return paramModelList;
    }

    /**
     * @param paramModelList The paramModelList to set.
     */
    public void setParamModelList(List paramModelList) {
        this.paramModelList = paramModelList;
    }

    /**
     * @return Returns the cpeParamModelList.
     */
    public List getCpeParamModelList() {
        return cpeParamModelList;
    }

    /**
     * @param cpeParamModelList The cpeParamModelList to set.
     */
    public void setCpeParamModelList(List cpeParamModelList) {
        this.cpeParamModelList = cpeParamModelList;
    }

    static public void printNameValuePairs (NameValuePair[] nvs)
    {
        for (int i=0; i<nvs.length; ++i) {
            String name = nvs[i].getName();
            Trace.trace("Param name:" + nvs[i].getName());
            Object aValue = nvs[i].getValue();
            if (aValue == null) {
                System.out.println("    no value");
            }
            if (aValue instanceof String) {
                
            } else if (aValue instanceof Integer) {
                System.out.println("    Integer" + ((Integer) aValue).intValue());
            } else if (aValue instanceof Float) {
                
            } else if (aValue instanceof Boolean) {
                
            } else if (aValue instanceof Object[]) {
                Object[] obj = (Object[])aValue;
                System.out.println("    Object[]:" + obj.length);
                for (int k = 0; k < obj.length; k++) {
                  if (obj[k] instanceof String) {
                      System.out.println("    [" + k + "]:" + obj[k].toString());
                  }
                  else if (obj[k] instanceof Integer) {
                      System.out.println("    [" + k + "]:" + ((Integer)obj[k]).intValue());      
                  }
                  else if (obj[k] instanceof Float) {
                      System.out.println("    [" + k + "]:" + ((Float)obj[k]).floatValue());
                  } else if (obj[k] instanceof Boolean) {
                      System.out.println("    [" + k + "]:" + ((Boolean)obj[k]).booleanValue());
                  }        
                }            
            } else {
                
            }
        }
        
    }
    
}
