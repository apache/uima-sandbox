/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Feb 4, 2006, 9:25:56 PM
 * source:  AbstractUimaCasProcessor.java
 */
package org.apache.uima.application.metadata.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.application.metadata.DeploymentOverrides;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.cpe.model.ConfigParameterModel;
import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.cpe.model.UimaCasInitializer;
import org.apache.uima.cpe.model.UimaCollectionReader;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * 
 *
 */
public abstract class AbstractUimaCasProcessor extends MetaDataObject_impl 
{

    // Status of this AbstractUimaCasProcessor
    protected int                       status = 0;
    protected int                       statusDetails = 0;
    
    private String                      name = "";
    private String                      description = "";
    
    // TRUE if built-in
    protected boolean                   isBuiltin = false;

    // Category of this cas processor
    protected int                     casprocCategory = UimaCasProcessor.CASPROCESSOR_CAT_UNKNOWN;

    // UimaApplication where this object belongs to
    protected UimaApplication           uimaApplication;        
    protected ResourceManager           resourceManager;
    
    // Name of Cas Processor owning these sets of parameter settings
    protected String                  instanceName = "";    
    protected String                  casProcessorDescription = "";    
    protected String                  xmlDescriptor = ""; // original Xml descriptor
    protected XMLizable               xmlizableDescriptor = null; // Object created from xmlDescriptor
    
    protected DeploymentOverrides         mDeploymentOverrides;

    protected ConfigParametersModel               configParamsModel;
    protected ConfigurationParameterDeclarations  configParamDecls = null;
    // Param settings in Cas Processor  (NOT in Cpe)
    protected ConfigurationParameterSettings      configParamSettings = null; 
             
    /*************************************************************************/

    public AbstractUimaCasProcessor(int category, UimaApplication app) {
        super();
        casprocCategory = category;
        this.uimaApplication = app;
    }
    
    
    static public void createConfigParamOverrides (ConfigParametersModel model) 
    {
//        // Get Declaration Configuration Parameter from UimaCasProcessor
//        List paramList = model.getParamModelList();
//        if (paramList != null && paramList.size() > 0) {
//            List        cpeParamModelList = new ArrayList();
//            
//            // For each override, set its name with "empty" value
//            for (int k=0; k<paramList.size(); ++k) {
//                ConfigParameterModel m = (ConfigParameterModel) paramList.get(k);
//                cpeParamModelList.add(m);
//            }
//            model.setCpeParamModelList(cpeParamModelList);
//        }
        createConfigParamOverrides(model, null);
    }

    // see  ConfigParametersModel.setCpeConfigParamSettings
    static public void createConfigParamOverrides (ConfigParametersModel model,
                ConfigurationParameterSettings cpeSettings) 
    {
        // Get Declaration Configuration Parameter from ConfigParametersModel
        List paramList = model.getParamModelList();
        if (paramList != null && paramList.size() > 0) {
            List        cpeParamModelList = new ArrayList();
            
            // For each override, set its name with "Default" value
            for (int k=0; k<paramList.size(); ++k) {
                ConfigParameterModel m = (ConfigParameterModel) paramList.get(k);
                
                Object cpeValue = null;
                if (cpeSettings != null) {
                    cpeValue = cpeSettings.getParameterValue(m.getName());
                    if (cpeValue == null) {
                        // Use Default Value
                        cpeValue = model.getParamSettings().getParameterValue(m.getName());               
                    }                    
                    m.setCpeValue(cpeValue);
                }
                cpeParamModelList.add(m);
            }
            model.setCpeParamModelList(cpeParamModelList.size() > 0 ? cpeParamModelList:null);
        }
    }

    /**
     * @return the uimaApplication
     */
    public UimaApplication getUimaApplication() {
        return uimaApplication;
    }

    /**
     * @param uimaApplication the uimaApplication to set
     */
    public void setUimaApplication(UimaApplication uimaApplication) {
        this.uimaApplication = uimaApplication;
    }

    /**
     * @return the resourceManager
     */
    public ResourceManager getResourceManager() {
        if (resourceManager == null && uimaApplication != null) {
            return uimaApplication.getResourceManager();
        }
        return resourceManager;
    }

    /**
     * @param resourceManager the resourceManager to set
     */
    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
    
    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.impl.MetaDataObject_impl#getXmlizationInfo()
     */
    protected XmlizationInfo getXmlizationInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
//    public String getName() {
//        return name;
//    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /*************************************************************************/
    
    public DeploymentOverrides getDeploymentOverrides() {
        return mDeploymentOverrides;
    }

    protected void setDeploymentOverrides(DeploymentOverrides aParam) {
        if (aParam == null) {
            throw new UIMA_IllegalArgumentException(
                    UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT,
                    new Object[]{"null", "aParams", "setConfigParamSettingsSets"});            
        }        
        mDeploymentOverrides = aParam;
    }

    /**
     * @return Returns the configParamsModel.
     */
    public ConfigParametersModel getConfigParamsModel() {
        return configParamsModel;
    }

    /**
     * @param configParamsModel The configParamsModel to set.
     */
    public void setConfigParamsModel(ConfigParametersModel configParamsModel) {
        this.configParamsModel = configParamsModel;
    }

    /*************************************************************************/
    
    public boolean isUimaCasProcessor () {
        return (this instanceof UimaCasProcessor);
    }
    
    public boolean isUimaCollectionReader () {
        return (this instanceof UimaCollectionReader);
    }
    
    public boolean isUimaCasInitializer () {
        return (this instanceof UimaCasInitializer);
    }
    
    /*************************************************************************/
    
    public int    getCasProcessorCategory()
    {
        return casprocCategory;
    }
    
    /**
     * @return Returns the casProcessorName.
     */
    public String getInstanceName() {
        return instanceName;
    }
    
    
    /**
     * @param casProcessorName The casProcessorName to set.
     */
    public void setInstanceName(String casProcessorName) {
        this.instanceName = casProcessorName;
    }
    
    public String getCasProcessorDescription() {
        return casProcessorDescription;
    }
    
    public String getXmlDescriptor() {
        return xmlDescriptor;
    }

    /**
     * @param xmlDescriptor The xmlDescriptor to set.
     */
    public void setXmlDescriptor(String xmlDescriptor) {
        this.xmlDescriptor = xmlDescriptor;
    }

    /**
     * @return the xmlizableDescriptor
     */
    public XMLizable getXmlizable() {
        return xmlizableDescriptor;
    }

    /**
     * @param xmlizableDescriptor the xmlizableDescriptor to set
     */
    public void setXmlizable(XMLizable xmlizableDescriptor) {
        this.xmlizableDescriptor = xmlizableDescriptor;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the statusDetails
     */
    public int getStatusDetails() {
        return statusDetails;
    }

    /**
     * @param statusDetails the statusDetails to set
     */
    public void setStatusDetails(int statusDetails) {
        this.statusDetails = statusDetails;
    }

    /**
     * @return the isBuiltin
     */
    public boolean isBuiltin() {
        return isBuiltin;
    }

    /**
     * @param isBuiltin the isBuiltin to set
     */
    public void setBuiltin(boolean isBuiltin) {
        this.isBuiltin = isBuiltin;
    }

    /**
     * Overridden to provide custom XML representation.
     * @see org.apache.uima.util.XMLizable#toXML(ContentHandler)
     */
    public void toXML (ContentHandler aContentHandler, 
        boolean aWriteDefaultNamespaceAttribute)
        throws SAXException
    {
        AttributesImpl attrs = new AttributesImpl();
        
        // <casProcessor>
        String  tagComponent = "";
        if (this instanceof UimaCollectionReader) {
            tagComponent = UimaApplication_Impl.TAG_COLLECTION_READER;
        } else {
            tagComponent = UimaApplication_Impl.TAG_CAS_PROCESSOR;            
        }
        attrs.addAttribute("", UimaApplication_Impl.TAG_NAME, UimaApplication_Impl.TAG_NAME,
                           null, "" + getInstanceName());
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", tagComponent, attrs);        
        attrs.clear();

        // <import>
        attrs.addAttribute("", UimaApplication_Impl.TAG_LOCATION, UimaApplication_Impl.TAG_LOCATION,
                null, "" + getXmlDescriptor());
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_IMPORT, attrs);
        // </import>
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_IMPORT);        
        attrs.clear();
        
        // <deploymentSettings>
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_DEPLOYMENT_SETTINGS, attrs);
        // </deploymentSettings>
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_DEPLOYMENT_SETTINGS);        
        
        // <deploymentOverrides>
        aContentHandler.startElement(getXmlizationInfo().namespace,
             "", UimaApplication_Impl.TAG_DEPLOYMENT_OVERRIDES,attrs);        
        
        //
        // Add Configuration Parameter Overrides for CPE
        //
                
        // <configParamOverrides>
        aContentHandler.startElement(getXmlizationInfo().namespace,
             "", UimaApplication_Impl.TAG_CONFIG_PARAM_OVERRIDES, attrs);        
        {
            // <configParamOverrideSet>
            attrs.addAttribute("", UimaApplication_Impl.TAG_NAME, UimaApplication_Impl.TAG_NAME,
                               null, "");
            aContentHandler.startElement(getXmlizationInfo().namespace,
                    "", UimaApplication_Impl.TAG_CONFIG_PARAM_OVERRIDE_SET, attrs);        
            attrs.clear();

            // <description>
            aContentHandler.startElement(getXmlizationInfo().namespace,
                    "", UimaApplication_Impl.TAG_DESCRIPTION, attrs);
            // </description>
            aContentHandler.endElement(getXmlizationInfo().namespace,
                    "", UimaApplication_Impl.TAG_DESCRIPTION);        

            if (getConfigParamsModel() != null) {
                // List cpeOverrideList = getConfigParamsModel().getParamModelList();
                List cpeOverrideList = getConfigParamsModel().getCpeParamModelList();
                if (cpeOverrideList != null && cpeOverrideList.size() > 0) {
                    // <configurationParameterSettings>
                    aContentHandler.startElement(getXmlizationInfo().namespace,
                            "", "configurationParameterSettings", attrs);        
                    
                    CasProcessorConfigurationParameterSettings settings =
                        CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
                    // casProc.setConfigurationParameterSettings(settings);
                    for (int k=0; k<cpeOverrideList.size(); ++k) {
                        ConfigParameterModel m = (ConfigParameterModel) cpeOverrideList.get(k);
                        if (m.getCpeValue() != null) {
                            settings.setParameterValue (m.getName(), m.getCpeValue());
                            m.toXML(aContentHandler, aWriteDefaultNamespaceAttribute);
                        }
                    }
                    
                    // </configurationParameterSettings>
                    aContentHandler.endElement(getXmlizationInfo().namespace,
                            "","configurationParameterSettings");        
                }
            }
            aContentHandler.endElement(getXmlizationInfo().namespace,
                    "", UimaApplication_Impl.TAG_CONFIG_PARAM_OVERRIDE_SET);            
        }
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_CONFIG_PARAM_OVERRIDES);            
        
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", UimaApplication_Impl.TAG_DEPLOYMENT_OVERRIDES);            
             
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", tagComponent);            
    }
       
    /**
     *  Generate "uimaCasProcessor" or "uimaCollectionReader" descriptor
     * 
     * @param xmlDescriptorFileName
     * @param resolve
     * @return String
     */
    public String generateComponentXmlDescriptor (String xmlDescriptorFileName, boolean resolve)
    {
        StringWriter w = new StringWriter();
        try {
            toXML(w);
            return w.toString();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public String generateXmlDescriptor (String xmlDescriptorFileName, boolean resolve, boolean updateOverrides)
    {
        // Update Config. Param Overrides, if any
        if (updateOverrides) {
            // Add Configuration Parameter Overrides for CPE
            List overrideList = getConfigParamsModel().getParamModelList();
            if (overrideList != null && overrideList.size() > 0) {
                for (int k=0; k<overrideList.size(); ++k) {
                    ConfigParameterModel m = (ConfigParameterModel) overrideList.get(k);
                    configParamSettings.setParameterValue (m.getName(), m.getValue());
                    if (m.getCpeValue() != null) {
                        configParamSettings.setParameterValue (m.getName(), m.getCpeValue());
//                      Trace.trace("Set value of " + m.getName() + " to " + m.getCpeValue());
//                      } else {
//                      Trace.trace("Value of " + m.getName() + " is " + m.getValue());                  
                    }
                }
            }
        }
        
        StringWriter w = new StringWriter();
        FileOutputStream out = null;
        try {
//            if (resolve) {
//                try {
//                    ((CollectionReaderDescription) xmlizableDescriptor).getCollectionReaderMetaData().resolveImports();
//                    Trace.err("*** Resolve Imports ***");
//                } catch (InvalidXMLException e) {
//                    // TODO Auto-generated catch block
//                    Trace.err("***FAILED TO  Resolve Imports ***");
//                    e.printStackTrace();
//                }
//            }
            // Save to file
            if (xmlDescriptorFileName != null && xmlDescriptorFileName.trim().length() > 0) {
                out = new FileOutputStream (xmlDescriptorFileName);
                xmlizableDescriptor.toXML(out);
                // Trace.trace("*** write to file: " + xmlDescriptorFileName);
            }
            
            xmlizableDescriptor.toXML(w);
            return w.toString();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }        
            }
        }
        
        return null;
    } // generateXmlDescriptor
    
   

}
