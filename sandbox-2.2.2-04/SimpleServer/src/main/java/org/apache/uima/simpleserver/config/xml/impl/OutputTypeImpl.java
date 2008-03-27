/*
 * XML Type:  outputType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.OutputType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml.impl;
/**
 * An XML outputType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public class OutputTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.OutputType
{
    
    public OutputTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SHORTDESCRIPTION$0 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "shortDescription");
    private static final javax.xml.namespace.QName LONGDESCRIPTION$2 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "longDescription");
    private static final javax.xml.namespace.QName FEATUREPATH$4 = 
        new javax.xml.namespace.QName("", "featurePath");
    private static final javax.xml.namespace.QName OUTPUTATTRIBUTE$6 = 
        new javax.xml.namespace.QName("", "outputAttribute");
    
    
    /**
     * Gets the "shortDescription" element
     */
    public java.lang.String getShortDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SHORTDESCRIPTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "shortDescription" element
     */
    public org.apache.xmlbeans.XmlString xgetShortDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SHORTDESCRIPTION$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "shortDescription" element
     */
    public boolean isSetShortDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SHORTDESCRIPTION$0) != 0;
        }
    }
    
    /**
     * Sets the "shortDescription" element
     */
    public void setShortDescription(java.lang.String shortDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SHORTDESCRIPTION$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SHORTDESCRIPTION$0);
            }
            target.setStringValue(shortDescription);
        }
    }
    
    /**
     * Sets (as xml) the "shortDescription" element
     */
    public void xsetShortDescription(org.apache.xmlbeans.XmlString shortDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SHORTDESCRIPTION$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SHORTDESCRIPTION$0);
            }
            target.set(shortDescription);
        }
    }
    
    /**
     * Unsets the "shortDescription" element
     */
    public void unsetShortDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SHORTDESCRIPTION$0, 0);
        }
    }
    
    /**
     * Gets the "longDescription" element
     */
    public java.lang.String getLongDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LONGDESCRIPTION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "longDescription" element
     */
    public org.apache.xmlbeans.XmlString xgetLongDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LONGDESCRIPTION$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "longDescription" element
     */
    public boolean isSetLongDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LONGDESCRIPTION$2) != 0;
        }
    }
    
    /**
     * Sets the "longDescription" element
     */
    public void setLongDescription(java.lang.String longDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LONGDESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(LONGDESCRIPTION$2);
            }
            target.setStringValue(longDescription);
        }
    }
    
    /**
     * Sets (as xml) the "longDescription" element
     */
    public void xsetLongDescription(org.apache.xmlbeans.XmlString longDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LONGDESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(LONGDESCRIPTION$2);
            }
            target.set(longDescription);
        }
    }
    
    /**
     * Unsets the "longDescription" element
     */
    public void unsetLongDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LONGDESCRIPTION$2, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$4);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FEATUREPATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FEATUREPATH$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FEATUREPATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FEATUREPATH$4);
            }
            target.set(featurePath);
        }
    }
    
    /**
     * Gets the "outputAttribute" attribute
     */
    public java.lang.String getOutputAttribute()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTATTRIBUTE$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "outputAttribute" attribute
     */
    public org.apache.xmlbeans.XmlString xgetOutputAttribute()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OUTPUTATTRIBUTE$6);
            return target;
        }
    }
    
    /**
     * Sets the "outputAttribute" attribute
     */
    public void setOutputAttribute(java.lang.String outputAttribute)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTATTRIBUTE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTPUTATTRIBUTE$6);
            }
            target.setStringValue(outputAttribute);
        }
    }
    
    /**
     * Sets (as xml) the "outputAttribute" attribute
     */
    public void xsetOutputAttribute(org.apache.xmlbeans.XmlString outputAttribute)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OUTPUTATTRIBUTE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(OUTPUTATTRIBUTE$6);
            }
            target.set(outputAttribute);
        }
    }
}
