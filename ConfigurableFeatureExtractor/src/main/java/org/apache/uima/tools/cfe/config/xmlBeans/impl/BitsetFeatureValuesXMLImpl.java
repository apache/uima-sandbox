/*
 * XML Type:  BitsetFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.BitsetFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML BitsetFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class BitsetFeatureValuesXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML
{
    
    public BitsetFeatureValuesXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BITMASK$0 = 
        new javax.xml.namespace.QName("", "bitmask");
    private static final javax.xml.namespace.QName EXACTMATCH$2 = 
        new javax.xml.namespace.QName("", "exact_match");
    
    
    /**
     * Gets the "bitmask" attribute
     */
    public java.lang.String getBitmask()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BITMASK$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "bitmask" attribute
     */
    public org.apache.xmlbeans.XmlString xgetBitmask()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(BITMASK$0);
            return target;
        }
    }
    
    /**
     * Sets the "bitmask" attribute
     */
    public void setBitmask(java.lang.String bitmask)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BITMASK$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(BITMASK$0);
            }
            target.setStringValue(bitmask);
        }
    }
    
    /**
     * Sets (as xml) the "bitmask" attribute
     */
    public void xsetBitmask(org.apache.xmlbeans.XmlString bitmask)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(BITMASK$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(BITMASK$0);
            }
            target.set(bitmask);
        }
    }
    
    /**
     * Gets the "exact_match" attribute
     */
    public boolean getExactMatch()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXACTMATCH$2);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "exact_match" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetExactMatch()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(EXACTMATCH$2);
            return target;
        }
    }
    
    /**
     * True if has "exact_match" attribute
     */
    public boolean isSetExactMatch()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(EXACTMATCH$2) != null;
        }
    }
    
    /**
     * Sets the "exact_match" attribute
     */
    public void setExactMatch(boolean exactMatch)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXACTMATCH$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXACTMATCH$2);
            }
            target.setBooleanValue(exactMatch);
        }
    }
    
    /**
     * Sets (as xml) the "exact_match" attribute
     */
    public void xsetExactMatch(org.apache.xmlbeans.XmlBoolean exactMatch)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(EXACTMATCH$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(EXACTMATCH$2);
            }
            target.set(exactMatch);
        }
    }
    
    /**
     * Unsets the "exact_match" attribute
     */
    public void unsetExactMatch()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(EXACTMATCH$2);
        }
    }
}
