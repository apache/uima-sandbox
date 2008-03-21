/*
 * XML Type:  simpleFilterType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.SimpleFilterType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml.impl;
/**
 * An XML simpleFilterType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public class SimpleFilterTypeImpl extends org.apache.uima.simpleserver.config.xml.impl.FilterTypeImpl implements org.apache.uima.simpleserver.config.xml.SimpleFilterType
{
    
    public SimpleFilterTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FEATUREPATH$0 = 
        new javax.xml.namespace.QName("", "featurePath");
    private static final javax.xml.namespace.QName OPERATOR$2 = 
        new javax.xml.namespace.QName("", "operator");
    private static final javax.xml.namespace.QName VALUE$4 = 
        new javax.xml.namespace.QName("", "value");
    
    
    /**
     * Gets the "featurePath" attribute
     */
    public java.lang.String getFeaturePath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FEATUREPATH$0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FEATUREPATH$0);
            }
            target.set(featurePath);
        }
    }
    
    /**
     * Gets the "operator" attribute
     */
    public org.apache.uima.simpleserver.config.xml.FilterOperator.Enum getOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPERATOR$2);
            if (target == null)
            {
                return null;
            }
            return (org.apache.uima.simpleserver.config.xml.FilterOperator.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "operator" attribute
     */
    public org.apache.uima.simpleserver.config.xml.FilterOperator xgetOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.FilterOperator target = null;
            target = (org.apache.uima.simpleserver.config.xml.FilterOperator)get_store().find_attribute_user(OPERATOR$2);
            return target;
        }
    }
    
    /**
     * Sets the "operator" attribute
     */
    public void setOperator(org.apache.uima.simpleserver.config.xml.FilterOperator.Enum operator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPERATOR$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OPERATOR$2);
            }
            target.setEnumValue(operator);
        }
    }
    
    /**
     * Sets (as xml) the "operator" attribute
     */
    public void xsetOperator(org.apache.uima.simpleserver.config.xml.FilterOperator operator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.FilterOperator target = null;
            target = (org.apache.uima.simpleserver.config.xml.FilterOperator)get_store().find_attribute_user(OPERATOR$2);
            if (target == null)
            {
                target = (org.apache.uima.simpleserver.config.xml.FilterOperator)get_store().add_attribute_user(OPERATOR$2);
            }
            target.set(operator);
        }
    }
    
    /**
     * Gets the "value" attribute
     */
    public java.lang.String getValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "value" attribute
     */
    public org.apache.xmlbeans.XmlString xgetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$4);
            return target;
        }
    }
    
    /**
     * True if has "value" attribute
     */
    public boolean isSetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(VALUE$4) != null;
        }
    }
    
    /**
     * Sets the "value" attribute
     */
    public void setValue(java.lang.String value)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VALUE$4);
            }
            target.setStringValue(value);
        }
    }
    
    /**
     * Sets (as xml) the "value" attribute
     */
    public void xsetValue(org.apache.xmlbeans.XmlString value)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VALUE$4);
            }
            target.set(value);
        }
    }
    
    /**
     * Unsets the "value" attribute
     */
    public void unsetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(VALUE$4);
        }
    }
}
