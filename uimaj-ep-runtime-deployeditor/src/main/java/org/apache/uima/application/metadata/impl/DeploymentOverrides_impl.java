package org.apache.uima.application.metadata.impl;

import java.util.ArrayList;

import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.application.metadata.ConfigParamOverrides;
import org.apache.uima.application.metadata.DeploymentOverrides;
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
            <deploymentOverrides>
                <!-- Overrides for UIMA Configuration Parameters -->
                <configParamOverrides>                
                    <overrideSet name="set# 1"  default >
                    </overrideSet>
                    
                    <overrideSet name="set# 2" >
                    </overrideSet>                    
                </configParamOverrides>             
            </deploymentOverrides>
 * 
 */
public class DeploymentOverrides_impl extends MetaDataObject_impl
                                            implements DeploymentOverrides
{ 
    
    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
    throws InvalidXMLException
    {
        // Trace.trace();
        
//        ArrayList params = new ArrayList();
        NodeList childNodes = aElement.getChildNodes();
        // Trace.trace("configParamSettingsSet count: " + childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element)
            {
                Element elem = (Element)curNode;
                if (UimaApplication.TAG_CONFIG_PARAM_OVERRIDES.equals(elem.getTagName())) {
                    // Trace.trace("Found ConfigParamSettingsSet");  
                    setConfigParamOverrides((ConfigParamOverrides) aParser.buildObject(elem, aOptions));
                    
                } else if (UimaApplication.TAG_DESCRIPTION.equals(elem.getTagName())) {
                    Trace.trace("Found description");  
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
       
    static final long serialVersionUID = -2248322904617280983L;
    private ConfigParamOverrides        configParamOverrides = null;
    
    public ConfigParamOverrides getConfigParamOverrides() {
        return configParamOverrides;
    }

    public void setConfigParamOverrides(ConfigParamOverrides aParam) {
        if (aParam == null) {
            throw new UIMA_IllegalArgumentException(
                    UIMA_IllegalArgumentException.ILLEGAL_ARGUMENT,
                    new Object[]{"null", "aParams", "setConfigParamSettingsSets"});            
        }
        configParamOverrides = aParam;
    }  
    
}
