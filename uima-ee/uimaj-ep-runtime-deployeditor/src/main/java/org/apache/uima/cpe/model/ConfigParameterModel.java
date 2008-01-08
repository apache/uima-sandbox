/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 12, 2006, 1:58:21 PM
 * source:  ConfigParameterModel.java
 */
package org.apache.uima.cpe.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLParser;
import org.apache.uima.util.XMLParser.ParsingOptions;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 *  Encapsulate the following 3 UIMA classes into one class:
 *      Config Parameter Declaration
 *      Config Parameter Settings (value)
 *       - for Cas Processor
 *       - for CPE
 *
 */
public class ConfigParameterModel {

    /**
     * MultiValues[] is used to represent the multi-values of the Configuration Parameter.
     * MultiValues[i] is the value of the i-th index of multivalues.
     * Example:
     *      <nameValuePair>
     *          <name>Locations</name>
     *          <value>
     *              <array>                     multiValues[]
     *                  <string>a</string>      multiValues[0]= {Locations, "a", 0}
     *                  <string>b</string>      multiValues[1]= {Locations, "b", 0}
     *                  <string>c</string>      multiValues[2]= {Locations, "b", 0}
     *              <array>
     *          </value>
     *      </nameValuePair>
     *      
     */
    public static class MultiValues {
        public ConfigParameterModel    parameterModel;
        
        // The "values" cannot be used since this MultiValues can be DYNAMICALLY created !
        public Object[]                values;
        public int                     index;      // index within values[] (0, 1, ...)
//        private boolean                forCpe = false;
        
         
        public MultiValues(ConfigParameterModel paramModel, boolean forCpe, int index)
        {
            super();
            this.parameterModel = paramModel;
//            this.forCpe = forCpe;
            this.index = index;
        }
        public MultiValues(ConfigParameterModel paramModel, Object[] values, int index)
        {
            super();
            this.parameterModel = paramModel;
            this.values = values;
            this.index = index;
        }
        
        public String getType() {
            return parameterModel.getType();
        }
        public String getName() {
            return parameterModel.getName();
        }
        
        public Object getDefValue() {
            return values[index];
        }
        
        public void setDefValue(Object value) {
            values[index] = value;
        }        
        public Object getCpeValue() {
            Object[] cpeValues = (Object[]) parameterModel.getCpeValue();
            if (cpeValues == null) {
                return null;
            }
            return cpeValues[index];
        }
        
        public void setCpeValue(Object value) {
            Object[] cpeValues = (Object[]) parameterModel.getCpeValue();
            // Trace.trace("setValue: index=" + index + " ; value:" + value.toString());
            cpeValues[index] = value;
        }

        /**
         * @return Returns the index.
         */
        public int getIndex() {
            return index;
        }
        
        /**
         * Get the total number of multi-values
         * 
         * @return int
         */
        public int getMultiValuesSize () {
            Object[] cpeValues = (Object[]) parameterModel.getCpeValue();
            if (cpeValues == null) {
                Trace.err(getName() + ": getMultiValuesSize = 0");
                return 0;
            }
            return cpeValues.length;
        }

        /**
         * @return Returns the parameterModel.
         */
        public ConfigParameterModel getParameterModel() {
            return parameterModel;
        }

        /**
         * @param parameterModel The parameterModel to set.
         */
        public void setParameterModel(ConfigParameterModel parameterModel) {
            this.parameterModel = parameterModel;
        }
        /**
         *  Use to moved value up/down
         * @param index the index to set
         */
        public void setIndex(int index) {
            this.index = index;
        }
    } // MultiValues
    
    private ConfigurationParameter  configParam;
    private Object                  value;              // for Cas Processor
    private Object                  cpeValue    = null; // for CPE

    public ConfigParameterModel(ConfigurationParameter configParam) {
        super();
        this.configParam = configParam;
    }

    public void toXML(ContentHandler aContentHandler, 
            boolean aWriteDefaultNamespaceAttribute)
            throws SAXException
    {
        if (getCpeValue() == null) {
            Trace.trace("No value");
            return;
        }
        AttributesImpl attrs = new AttributesImpl();
        // <nameValuePair>
        aContentHandler.startElement(getXmlizationInfo().namespace,
                    "","nameValuePair",attrs);
        
        // <name>  </name>
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "","name",attrs);
        String valStr = getName();
        aContentHandler.characters(valStr.toCharArray(),0,valStr.length());
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "","name");
        
        // <value>
        aContentHandler.startElement(getXmlizationInfo().namespace, "","value",attrs);
        
        String lowerCaseType = getType().toLowerCase();
        if (isMultiValued()) {
            aContentHandler.startElement(getXmlizationInfo().namespace, "","array",attrs);            
            Object[] v = (Object[]) getCpeValue();
            for (int i=0; i<v.length; ++i) {
                aContentHandler.startElement(getXmlizationInfo().namespace, "", lowerCaseType, attrs);            
                valStr ="" + v[i];
                aContentHandler.characters(valStr.toCharArray(),0,valStr.length());                
                aContentHandler.endElement(getXmlizationInfo().namespace, "", lowerCaseType);
            }        
        } else {
            aContentHandler.startElement(getXmlizationInfo().namespace, "", lowerCaseType, attrs);            
            valStr = "" + getCpeValue();
            aContentHandler.characters(valStr.toCharArray(),0,valStr.length());                
            aContentHandler.endElement(getXmlizationInfo().namespace, "", lowerCaseType);
        }
        
        if (isMultiValued()) {
            aContentHandler.endElement(getXmlizationInfo().namespace, "","array");
        }
        
        // </value>
        aContentHandler.endElement(getXmlizationInfo().namespace, "","value");
        // </nameValuePair>
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "","nameValuePair");
    }    
    
    protected XmlizationInfo getXmlizationInfo() {
        // return XMLIZATION_INFO;
        return new XmlizationInfo(null, null);
        //this object has custom XMLization routines
    }
    
    /**
     * @return Returns the value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    public void cloneValue ()
    {
        // cpeValue = value.clone();
    }

    /**
     * Get total number of multi-values
     * 
     * @return int
     */
    public int getMultivalueSize ()
    {
        if (value == null) return 0;
        if (!isMultiValued()) return 1;
        return ((Object[]) value).length;
    }
    
    /**
     * Get total number of CPE values
     * 
     * @return int
     */
    public int getCpeValueSize ()
    {
        if (cpeValue == null) return 0;
        if (!isMultiValued()) return 1;
        return ((Object[]) cpeValue).length;
    }
    
    /**
     * @return Returns the cpeValue.
     */
    public Object getCpeValue() {
        if (cpeValue != null && getCpeValueSize() > 0) {
            return cpeValue;
        }
        return null;
    }

    /**
     * @param cpeValue The cpeValue to set.
     */
    public void setCpeValue(Object cpeValue) {
        this.cpeValue = cpeValue;
    }    
    
    public void setCpeValue (String aValueString)
    {
        if (!isMultiValued()) {
            // Single value
            setCpeValue(getValueObjectFromString(aValueString));
        } else {
            addValueToCpeMultivalue(aValueString);
        }
    }
    
    public Object getValueObjectFromString (String aValueString)
    {
        if (aValueString == null || aValueString.trim().length() == 0) {
            return null;
        }
        Object aValue = null;
        String paramType = getType();
        
        try {
            if (ConfigurationParameter.TYPE_STRING.equals(paramType)) {
                aValue = aValueString;
                
            } else if (ConfigurationParameter.TYPE_INTEGER.equals(paramType)) {
                aValue = Integer.valueOf(aValueString);
                
            } else if (ConfigurationParameter.TYPE_FLOAT.equals(paramType)) {
                aValue = Float.valueOf(aValueString);
                
            } else if (ConfigurationParameter.TYPE_BOOLEAN.equals(paramType)) {
                aValue = Boolean.valueOf(aValueString);
            } else {
                Trace.bug("Wrong param type: " + paramType);
            }
        } catch (NumberFormatException e) {
            Trace.err(e.toString());
        }
        return aValue;
    }
    
    private Object _insertValueToMultivalue (Object[] v, Object aValue, int atIndex)
    {
        Object[] newValues;
        if (v != null) {
            // Copy OLD values
            newValues = new Object[v.length+1];
            for (int i=0; i<atIndex; ++i) {
                newValues[i] = v[i];
            }
            newValues[atIndex] = aValue;
            for (int i=atIndex; i<v.length; ++i) {
                newValues[i+1] = v[i];
            }
        } else {
            newValues = new Object[1];
            newValues[0] = aValue;
        }
        
        return newValues;
    }        
    
    public void addValueToMultivalue (String aValueString, int atIndex)
    {
        if (!isMultiValued()) return;
        value =_insertValueToMultivalue ((Object[]) value, 
                        getValueObjectFromString(aValueString), atIndex);
    }    
    
    public void addValueToCpeMultivalue (String aValueString, int atIndex)
    {
        if (!isMultiValued()) return;
        cpeValue =_insertValueToMultivalue ((Object[]) cpeValue, 
                        getValueObjectFromString(aValueString), atIndex);
    }    
    
    /**
     * Add a value to multi-value parameter override (for AE) 
     * 
     * @param value
     * @return void
     */
    public void addValueToMultivalue (String aValueString)
    {
        if (!isMultiValued()) return;
        
        Object aValue = getValueObjectFromString(aValueString);        
        Object[] newValues;
        Object[] v = (Object[]) getValue();
        if (v != null) {
            // Copy OLD values
            newValues = new Object[v.length+1];
            for (int i=0; i<v.length; ++i) {
                newValues[i] = v[i];
            }
            newValues[v.length] = aValue;
        } else {
            newValues = new Object[1];
            newValues[0] = aValue;
        }
        setValue(newValues);
    }
    
    /**
     * Add a value to multi-value parameter override (for CPE) 
     * 
     * @param value
     * @return void
     */
    public void addValueToCpeMultivalue (String aValueString)
    {
        if (!isMultiValued()) return;
        
        Object aValue = getValueObjectFromString(aValueString);        
        Object[] newValues;
        Object[] v = (Object[]) cpeValue;
        if (v != null) {
            // Copy OLD values
            newValues = new Object[v.length+1];
            for (int i=0; i<v.length; ++i) {
                newValues[i] = v[i];
            }
            newValues[v.length] = aValue;
        } else {
            newValues = new Object[1];
            newValues[0] = aValue;
        }
        setCpeValue(newValues);
    }
    
    private Object _removeMultivalue (Object[] v, MultiValues mv)
    {
        if (v.length == 1) {
            // Has only 1 element to be removed
            return null;
        }
        
        int index = mv.getIndex();
        Object[] newValues = new Object[v.length-1];
        for (int i=0; i<index; ++i) {
            newValues[i] = v[i];
        }
        for (int i=index; i<(v.length-1); ++i) {
            newValues[i] = v[i+1];            
        }
        return newValues;
    }
    
    public void removeMultivalue (MultiValues mv)
    {
        if (!isMultiValued()) return;
        value = _removeMultivalue ((Object[]) value, mv);
    }    
    
    public void removeCpeMultivalue (MultiValues mv)
    {
        if (!isMultiValued()) return;
        cpeValue = _removeMultivalue ((Object[]) cpeValue, mv);
    }

    private boolean moveMultivalue (boolean moveUp, Object[] v, int index)
    {
        if (!isMultiValued()) return false;
        
        Object aValue;
        if (v != null && index < v.length) {
            if (moveUp) {
                // Move Up
                if (index > 0) {
                    aValue   = v[index];
                    v[index] = v[index-1];
                    v[index-1] = aValue;
                    return true;    // is moved
                }
            } else {
                // Move Down
                if (index < (v.length-1)) {
                    aValue   = v[index];
                    v[index] = v[index+1];
                    v[index+1] = aValue;
                    return true;    // is moved
                }
            }
        }        
        return false; // NOT moved
    } // moveMultivalue
    
    public boolean moveValue (boolean moveUp, int index)
    {
        return moveMultivalue (moveUp, (Object[]) value, index);
    } // moveValue
    
    public boolean moveCpeValue (boolean moveUp, int index)
    {
        return moveMultivalue (moveUp, (Object[]) cpeValue, index);
    } // moveCpeValue

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#addOverride(java.lang.String)
     */
    public void addOverride(String arg0) {
        configParam.addOverride(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser, org.apache.uima.util.XMLParser.ParsingOptions)
     */
    public void buildFromXMLElement(Element arg0, XMLParser arg1, ParsingOptions arg2) throws InvalidXMLException {
        configParam.buildFromXMLElement(arg0, arg1, arg2);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement(Element arg0, XMLParser arg1) throws InvalidXMLException {
        configParam.buildFromXMLElement(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#clone()
     */
    public Object clone() {
        return configParam.clone();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        return configParam.equals(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#getAttributeValue(java.lang.String)
     */
    public Object getAttributeValue(String arg0) {
        return configParam.getAttributeValue(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#getDescription()
     */
    public String getDescription() {
        return configParam.getDescription();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#getName()
     */
    public String getName() {
        return configParam.getName();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#getOverrides()
     */
    public String[] getOverrides() {
        return configParam.getOverrides();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#getType()
     */
    public String getType() {
        return configParam.getType();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#isMandatory()
     */
    public boolean isMandatory() {
        return configParam.isMandatory();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#isModifiable()
     */
    public boolean isModifiable() {
        return configParam.isModifiable();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#isMultiValued()
     */
    public boolean isMultiValued() {
        return configParam.isMultiValued();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#listAttributes()
     */
    public List listAttributes() {
        return configParam.listAttributes();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#removeOverride(java.lang.String)
     */
    public void removeOverride(String arg0) {
        configParam.removeOverride(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.MetaDataObject#setAttributeValue(java.lang.String, java.lang.Object)
     */
    public void setAttributeValue(String arg0, Object arg1) {
        configParam.setAttributeValue(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setDescription(java.lang.String)
     */
    public void setDescription(String arg0) {
        configParam.setDescription(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setMandatory(boolean)
     */
    public void setMandatory(boolean arg0) {
        configParam.setMandatory(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setMultiValued(boolean)
     */
    public void setMultiValued(boolean arg0) {
        configParam.setMultiValued(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setName(java.lang.String)
     */
    public void setName(String arg0) {
        configParam.setName(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setOverrides(java.lang.String[])
     */
    public void setOverrides(String[] arg0) {
        configParam.setOverrides(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.resource.metadata.ConfigurationParameter#setType(java.lang.String)
     */
    public void setType(String arg0) {
        configParam.setType(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#toXML(org.xml.sax.ContentHandler, boolean)
     */
//    public void toXML(ContentHandler arg0, boolean arg1) throws SAXException {
//        configParam.toXML(arg0, arg1);
//    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#toXML(org.xml.sax.ContentHandler)
     */
    public void toXML(ContentHandler arg0) throws SAXException {
        configParam.toXML(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#toXML(java.io.OutputStream)
     */
    public void toXML(OutputStream arg0) throws SAXException, IOException {
        configParam.toXML(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.util.XMLizable#toXML(java.io.Writer)
     */
    public void toXML(Writer arg0) throws SAXException, IOException {
        configParam.toXML(arg0);
    }

}
