/*
 * XML Type:  CFEDescriptorXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.CFEDescriptorXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML CFEDescriptorXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class CFEDescriptorXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML
{
    
    public CFEDescriptorXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TARGETANNOTATIONS$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "targetAnnotations");
    private static final javax.xml.namespace.QName NULLVALUEIMAGE$2 = 
        new javax.xml.namespace.QName("", "nullValueImage");
    
    
    /**
     * Gets array of all "targetAnnotations" elements
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[] getTargetAnnotationsArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TARGETANNOTATIONS$0, targetList);
            org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[] result = new org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "targetAnnotations" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML getTargetAnnotationsArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML)get_store().find_element_user(TARGETANNOTATIONS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "targetAnnotations" element
     */
    public int sizeOfTargetAnnotationsArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TARGETANNOTATIONS$0);
        }
    }
    
    /**
     * Sets array of all "targetAnnotations" element
     */
    public void setTargetAnnotationsArray(org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[] targetAnnotationsArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(targetAnnotationsArray, TARGETANNOTATIONS$0);
        }
    }
    
    /**
     * Sets ith "targetAnnotations" element
     */
    public void setTargetAnnotationsArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML targetAnnotations)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML)get_store().find_element_user(TARGETANNOTATIONS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(targetAnnotations);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "targetAnnotations" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML insertNewTargetAnnotations(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML)get_store().insert_element_user(TARGETANNOTATIONS$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "targetAnnotations" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML addNewTargetAnnotations()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML)get_store().add_element_user(TARGETANNOTATIONS$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "targetAnnotations" element
     */
    public void removeTargetAnnotations(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TARGETANNOTATIONS$0, i);
        }
    }
    
    /**
     * Gets the "nullValueImage" attribute
     */
    public java.lang.String getNullValueImage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLVALUEIMAGE$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "nullValueImage" attribute
     */
    public org.apache.xmlbeans.XmlString xgetNullValueImage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NULLVALUEIMAGE$2);
            return target;
        }
    }
    
    /**
     * True if has "nullValueImage" attribute
     */
    public boolean isSetNullValueImage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NULLVALUEIMAGE$2) != null;
        }
    }
    
    /**
     * Sets the "nullValueImage" attribute
     */
    public void setNullValueImage(java.lang.String nullValueImage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLVALUEIMAGE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLVALUEIMAGE$2);
            }
            target.setStringValue(nullValueImage);
        }
    }
    
    /**
     * Sets (as xml) the "nullValueImage" attribute
     */
    public void xsetNullValueImage(org.apache.xmlbeans.XmlString nullValueImage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NULLVALUEIMAGE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NULLVALUEIMAGE$2);
            }
            target.set(nullValueImage);
        }
    }
    
    /**
     * Unsets the "nullValueImage" attribute
     */
    public void unsetNullValueImage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NULLVALUEIMAGE$2);
        }
    }
}
