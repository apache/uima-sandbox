/*
 * XML Type:  RangeFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.RangeFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML RangeFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class RangeFeatureValuesXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML
{
    
    public RangeFeatureValuesXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LOWERBOUNDARYINCLUSIVE$0 = 
        new javax.xml.namespace.QName("", "lowerBoundaryInclusive");
    private static final javax.xml.namespace.QName UPPERBOUNDARYINCLUSIVE$2 = 
        new javax.xml.namespace.QName("", "upperBoundaryInclusive");
    private static final javax.xml.namespace.QName LOWERBOUNDARY$4 = 
        new javax.xml.namespace.QName("", "lowerBoundary");
    private static final javax.xml.namespace.QName UPPERBOUNDARY$6 = 
        new javax.xml.namespace.QName("", "upperBoundary");
    
    
    /**
     * Gets the "lowerBoundaryInclusive" attribute
     */
    public boolean getLowerBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "lowerBoundaryInclusive" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetLowerBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            return target;
        }
    }
    
    /**
     * True if has "lowerBoundaryInclusive" attribute
     */
    public boolean isSetLowerBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(LOWERBOUNDARYINCLUSIVE$0) != null;
        }
    }
    
    /**
     * Sets the "lowerBoundaryInclusive" attribute
     */
    public void setLowerBoundaryInclusive(boolean lowerBoundaryInclusive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            }
            target.setBooleanValue(lowerBoundaryInclusive);
        }
    }
    
    /**
     * Sets (as xml) the "lowerBoundaryInclusive" attribute
     */
    public void xsetLowerBoundaryInclusive(org.apache.xmlbeans.XmlBoolean lowerBoundaryInclusive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(LOWERBOUNDARYINCLUSIVE$0);
            }
            target.set(lowerBoundaryInclusive);
        }
    }
    
    /**
     * Unsets the "lowerBoundaryInclusive" attribute
     */
    public void unsetLowerBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(LOWERBOUNDARYINCLUSIVE$0);
        }
    }
    
    /**
     * Gets the "upperBoundaryInclusive" attribute
     */
    public boolean getUpperBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "upperBoundaryInclusive" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetUpperBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            return target;
        }
    }
    
    /**
     * True if has "upperBoundaryInclusive" attribute
     */
    public boolean isSetUpperBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(UPPERBOUNDARYINCLUSIVE$2) != null;
        }
    }
    
    /**
     * Sets the "upperBoundaryInclusive" attribute
     */
    public void setUpperBoundaryInclusive(boolean upperBoundaryInclusive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            }
            target.setBooleanValue(upperBoundaryInclusive);
        }
    }
    
    /**
     * Sets (as xml) the "upperBoundaryInclusive" attribute
     */
    public void xsetUpperBoundaryInclusive(org.apache.xmlbeans.XmlBoolean upperBoundaryInclusive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(UPPERBOUNDARYINCLUSIVE$2);
            }
            target.set(upperBoundaryInclusive);
        }
    }
    
    /**
     * Unsets the "upperBoundaryInclusive" attribute
     */
    public void unsetUpperBoundaryInclusive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(UPPERBOUNDARYINCLUSIVE$2);
        }
    }
    
    /**
     * Gets the "lowerBoundary" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getLowerBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(LOWERBOUNDARY$4);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "lowerBoundary" attribute
     */
    public boolean isSetLowerBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(LOWERBOUNDARY$4) != null;
        }
    }
    
    /**
     * Sets the "lowerBoundary" attribute
     */
    public void setLowerBoundary(org.apache.xmlbeans.XmlAnySimpleType lowerBoundary)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(LOWERBOUNDARY$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(LOWERBOUNDARY$4);
            }
            target.set(lowerBoundary);
        }
    }
    
    /**
     * Appends and returns a new empty "lowerBoundary" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewLowerBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(LOWERBOUNDARY$4);
            return target;
        }
    }
    
    /**
     * Unsets the "lowerBoundary" attribute
     */
    public void unsetLowerBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(LOWERBOUNDARY$4);
        }
    }
    
    /**
     * Gets the "upperBoundary" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getUpperBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(UPPERBOUNDARY$6);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "upperBoundary" attribute
     */
    public boolean isSetUpperBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(UPPERBOUNDARY$6) != null;
        }
    }
    
    /**
     * Sets the "upperBoundary" attribute
     */
    public void setUpperBoundary(org.apache.xmlbeans.XmlAnySimpleType upperBoundary)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(UPPERBOUNDARY$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(UPPERBOUNDARY$6);
            }
            target.set(upperBoundary);
        }
    }
    
    /**
     * Appends and returns a new empty "upperBoundary" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewUpperBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(UPPERBOUNDARY$6);
            return target;
        }
    }
    
    /**
     * Unsets the "upperBoundary" attribute
     */
    public void unsetUpperBoundary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(UPPERBOUNDARY$6);
        }
    }
}
