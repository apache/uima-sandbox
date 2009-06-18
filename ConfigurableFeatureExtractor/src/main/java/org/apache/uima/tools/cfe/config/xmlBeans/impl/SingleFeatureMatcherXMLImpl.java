/*
 * XML Type:  SingleFeatureMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.SingleFeatureMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML SingleFeatureMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class SingleFeatureMatcherXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML
{
    
    public SingleFeatureMatcherXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RANGEFEATUREVALUES$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "rangeFeatureValues");
    private static final javax.xml.namespace.QName ENUMFEATUREVALUES$2 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "enumFeatureValues");
    private static final javax.xml.namespace.QName BITSETFEATUREVALUES$4 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "bitsetFeatureValues");
    private static final javax.xml.namespace.QName OBJECTPATHFEATUREVALUES$6 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "objectPathFeatureValues");
    private static final javax.xml.namespace.QName PATTERNFEATUREVALUES$8 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "patternFeatureValues");
    private static final javax.xml.namespace.QName FEATURETYPENAME$10 = 
        new javax.xml.namespace.QName("", "featureTypeName");
    private static final javax.xml.namespace.QName FEATUREPATH$12 = 
        new javax.xml.namespace.QName("", "featurePath");
    private static final javax.xml.namespace.QName EXCLUDE$14 = 
        new javax.xml.namespace.QName("", "exclude");
    private static final javax.xml.namespace.QName QUIET$16 = 
        new javax.xml.namespace.QName("", "quiet");
    
    
    /**
     * Gets the "rangeFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML getRangeFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML)get_store().find_element_user(RANGEFEATUREVALUES$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "rangeFeatureValues" element
     */
    public boolean isSetRangeFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RANGEFEATUREVALUES$0) != 0;
        }
    }
    
    /**
     * Sets the "rangeFeatureValues" element
     */
    public void setRangeFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML rangeFeatureValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML)get_store().find_element_user(RANGEFEATUREVALUES$0, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML)get_store().add_element_user(RANGEFEATUREVALUES$0);
            }
            target.set(rangeFeatureValues);
        }
    }
    
    /**
     * Appends and returns a new empty "rangeFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML addNewRangeFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML)get_store().add_element_user(RANGEFEATUREVALUES$0);
            return target;
        }
    }
    
    /**
     * Unsets the "rangeFeatureValues" element
     */
    public void unsetRangeFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RANGEFEATUREVALUES$0, 0);
        }
    }
    
    /**
     * Gets the "enumFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML getEnumFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML)get_store().find_element_user(ENUMFEATUREVALUES$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "enumFeatureValues" element
     */
    public boolean isSetEnumFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENUMFEATUREVALUES$2) != 0;
        }
    }
    
    /**
     * Sets the "enumFeatureValues" element
     */
    public void setEnumFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML enumFeatureValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML)get_store().find_element_user(ENUMFEATUREVALUES$2, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML)get_store().add_element_user(ENUMFEATUREVALUES$2);
            }
            target.set(enumFeatureValues);
        }
    }
    
    /**
     * Appends and returns a new empty "enumFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML addNewEnumFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML)get_store().add_element_user(ENUMFEATUREVALUES$2);
            return target;
        }
    }
    
    /**
     * Unsets the "enumFeatureValues" element
     */
    public void unsetEnumFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENUMFEATUREVALUES$2, 0);
        }
    }
    
    /**
     * Gets the "bitsetFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML getBitsetFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML)get_store().find_element_user(BITSETFEATUREVALUES$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "bitsetFeatureValues" element
     */
    public boolean isSetBitsetFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(BITSETFEATUREVALUES$4) != 0;
        }
    }
    
    /**
     * Sets the "bitsetFeatureValues" element
     */
    public void setBitsetFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML bitsetFeatureValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML)get_store().find_element_user(BITSETFEATUREVALUES$4, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML)get_store().add_element_user(BITSETFEATUREVALUES$4);
            }
            target.set(bitsetFeatureValues);
        }
    }
    
    /**
     * Appends and returns a new empty "bitsetFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML addNewBitsetFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML)get_store().add_element_user(BITSETFEATUREVALUES$4);
            return target;
        }
    }
    
    /**
     * Unsets the "bitsetFeatureValues" element
     */
    public void unsetBitsetFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(BITSETFEATUREVALUES$4, 0);
        }
    }
    
    /**
     * Gets the "objectPathFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML getObjectPathFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML)get_store().find_element_user(OBJECTPATHFEATUREVALUES$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "objectPathFeatureValues" element
     */
    public boolean isSetObjectPathFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OBJECTPATHFEATUREVALUES$6) != 0;
        }
    }
    
    /**
     * Sets the "objectPathFeatureValues" element
     */
    public void setObjectPathFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML objectPathFeatureValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML)get_store().find_element_user(OBJECTPATHFEATUREVALUES$6, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML)get_store().add_element_user(OBJECTPATHFEATUREVALUES$6);
            }
            target.set(objectPathFeatureValues);
        }
    }
    
    /**
     * Appends and returns a new empty "objectPathFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML addNewObjectPathFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML)get_store().add_element_user(OBJECTPATHFEATUREVALUES$6);
            return target;
        }
    }
    
    /**
     * Unsets the "objectPathFeatureValues" element
     */
    public void unsetObjectPathFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OBJECTPATHFEATUREVALUES$6, 0);
        }
    }
    
    /**
     * Gets the "patternFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML getPatternFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML)get_store().find_element_user(PATTERNFEATUREVALUES$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "patternFeatureValues" element
     */
    public boolean isSetPatternFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PATTERNFEATUREVALUES$8) != 0;
        }
    }
    
    /**
     * Sets the "patternFeatureValues" element
     */
    public void setPatternFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML patternFeatureValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML)get_store().find_element_user(PATTERNFEATUREVALUES$8, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML)get_store().add_element_user(PATTERNFEATUREVALUES$8);
            }
            target.set(patternFeatureValues);
        }
    }
    
    /**
     * Appends and returns a new empty "patternFeatureValues" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML addNewPatternFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML)get_store().add_element_user(PATTERNFEATUREVALUES$8);
            return target;
        }
    }
    
    /**
     * Unsets the "patternFeatureValues" element
     */
    public void unsetPatternFeatureValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PATTERNFEATUREVALUES$8, 0);
        }
    }
    
    /**
     * Gets the "featureTypeName" attribute
     */
    public java.lang.String getFeatureTypeName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATURETYPENAME$10);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "featureTypeName" attribute
     */
    public org.apache.xmlbeans.XmlString xgetFeatureTypeName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATURETYPENAME$10);
            return target;
        }
    }
    
    /**
     * Sets the "featureTypeName" attribute
     */
    public void setFeatureTypeName(java.lang.String featureTypeName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATURETYPENAME$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FEATURETYPENAME$10);
            }
            target.setStringValue(featureTypeName);
        }
    }
    
    /**
     * Sets (as xml) the "featureTypeName" attribute
     */
    public void xsetFeatureTypeName(org.apache.xmlbeans.XmlString featureTypeName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATURETYPENAME$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FEATURETYPENAME$10);
            }
            target.set(featureTypeName);
        }
    }
    
    /**
     * Gets the "featurePath" attribute
     */
    public java.lang.String getFeaturePath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$12);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "featurePath" attribute
     */
    public org.apache.xmlbeans.XmlString xgetFeaturePath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$12);
            return target;
        }
    }
    
    /**
     * Sets the "featurePath" attribute
     */
    public void setFeaturePath(java.lang.String featurePath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FEATUREPATH$12);
            }
            target.setStringValue(featurePath);
        }
    }
    
    /**
     * Sets (as xml) the "featurePath" attribute
     */
    public void xsetFeaturePath(org.apache.xmlbeans.XmlString featurePath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FEATUREPATH$12);
            }
            target.set(featurePath);
        }
    }
    
    /**
     * Gets the "exclude" attribute
     */
    public boolean getExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXCLUDE$14);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "exclude" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(EXCLUDE$14);
            return target;
        }
    }
    
    /**
     * True if has "exclude" attribute
     */
    public boolean isSetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(EXCLUDE$14) != null;
        }
    }
    
    /**
     * Sets the "exclude" attribute
     */
    public void setExclude(boolean exclude)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXCLUDE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXCLUDE$14);
            }
            target.setBooleanValue(exclude);
        }
    }
    
    /**
     * Sets (as xml) the "exclude" attribute
     */
    public void xsetExclude(org.apache.xmlbeans.XmlBoolean exclude)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(EXCLUDE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(EXCLUDE$14);
            }
            target.set(exclude);
        }
    }
    
    /**
     * Unsets the "exclude" attribute
     */
    public void unsetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(EXCLUDE$14);
        }
    }
    
    /**
     * Gets the "quiet" attribute
     */
    public boolean getQuiet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUIET$16);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "quiet" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetQuiet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(QUIET$16);
            return target;
        }
    }
    
    /**
     * True if has "quiet" attribute
     */
    public boolean isSetQuiet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(QUIET$16) != null;
        }
    }
    
    /**
     * Sets the "quiet" attribute
     */
    public void setQuiet(boolean quiet)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUIET$16);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(QUIET$16);
            }
            target.setBooleanValue(quiet);
        }
    }
    
    /**
     * Sets (as xml) the "quiet" attribute
     */
    public void xsetQuiet(org.apache.xmlbeans.XmlBoolean quiet)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(QUIET$16);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(QUIET$16);
            }
            target.set(quiet);
        }
    }
    
    /**
     * Unsets the "quiet" attribute
     */
    public void unsetQuiet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(QUIET$16);
        }
    }
}
