package org.apache.uima.application.metadata.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.application.metadata.OverrideSet;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.cpe.model.ConfigParameterModel;
import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.internal.util.XMLUtils;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.PropertyXmlInfo;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * 
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
 * 
 */
public class OverrideSet_impl extends MetaDataObject_impl
                              implements OverrideSet
{
    static final long serialVersionUID = -2248322904617280983L;
    
    private String  name = "";
    private String  description = "";
    private boolean isSelected = false;
    
    private transient ConfigParametersModel       mConfigParametersModel = null;
    
    /** Settings for Configuration Parameter that are not in any group */
    private ConfigurationParameterSettings mConfigurationParameterSettings = null;
    
    public String getName ()
    {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isSelected () {
        return isSelected;
    }
    
    public void    setSelected (boolean selected) {
        isSelected = selected;
    }
    
    
    public ConfigParametersModel getConfigParametersModel () {
        return mConfigParametersModel;
    }
    
    public void setConfigParametersModel (ConfigParametersModel aParam) {
        mConfigParametersModel = aParam; // save
        
        // Set paramater value from CasProcessorConfigurationParameterSettings
        NameValuePair[] nvps = mConfigurationParameterSettings.getParameterSettings();
        for (int i=0; i<nvps.length; ++i) {
            ConfigParameterModel paramModel = aParam.getConfigParameterModel (nvps[i].getName());
            Trace.trace("Set cpeValue for:" + nvps[i].getName());
            paramModel.setCpeValue(nvps[i].getValue());
        }
        
    }
    
    public ConfigurationParameterSettings getConfigurationParameterSettings() {
        return mConfigurationParameterSettings;
    }  
    
    /**
     * @see org.apache.uima.resource.ConfigurationParameterDeclarations#setConfigurationParameters(ConfigurationParameter[])
     */
    public void setConfigurationParameterSettings(ConfigurationParameterSettings aParam)
    {
        if (aParam == null)
        {
            throw new UIMA_IllegalArgumentException(
                    UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT,
                    new Object[]{"null", "aParams", "setConfigurationParameterSettings"});            
        }        
        mConfigurationParameterSettings = aParam;
    }
    
    
    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
                                            throws InvalidXMLException
    {
        // Trace.trace();
        setName(aElement.getAttribute("name"));
        if (aElement.getAttribute("selected") != null ) {
            setSelected(aElement.getAttribute("selected").trim().equals("true"));
        }
        
        // read parameter settings
//        ArrayList paramSettingsList = new ArrayList();
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element) {
                Element elem = (Element)curNode;
                if ("configurationParameterSettings".equals(elem.getTagName())) {
                    // Trace.trace("BEGIN Found configurationParameterSettings");  
                    setConfigurationParameterSettings((ConfigurationParameterSettings)aParser.buildObject(elem,aOptions));  
                    // Trace.trace("END Found configurationParameterSettings");
//                    if (mConfigurationParameterSettings != null) {
//                        try {
//                            mConfigurationParameterSettings.toXML(System.out);
//                        } catch (SAXException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
                    
                } else if (UimaApplication.TAG_DESCRIPTION.equals(elem.getTagName())) {
                    setDescription(XMLUtils.getText(elem));
                    // Trace.trace("Found description:" + text);
                    
                } else {
                    throw new InvalidXMLException(
                            InvalidXMLException.UNKNOWN_ELEMENT,
                            new Object[]{elem.getTagName()});
                }
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
    
    public void printMe() {
        System.out.println("ConfigParamSettingsSet name=" + name);
        
    }
}
