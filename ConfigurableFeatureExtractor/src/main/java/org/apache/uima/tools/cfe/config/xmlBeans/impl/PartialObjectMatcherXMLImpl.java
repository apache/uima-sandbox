/*
 * XML Type:  PartialObjectMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.PartialObjectMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML PartialObjectMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class PartialObjectMatcherXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML
{
    
    public PartialObjectMatcherXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GROUPFEATUREMATCHERS$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "groupFeatureMatchers");
    private static final javax.xml.namespace.QName ANNOTATIONTYPENAME$2 = 
        new javax.xml.namespace.QName("", "annotationTypeName");
    private static final javax.xml.namespace.QName FULLPATH$4 = 
        new javax.xml.namespace.QName("", "fullPath");
    
    
    /**
     * Gets array of all "groupFeatureMatchers" elements
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[] getGroupFeatureMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(GROUPFEATUREMATCHERS$0, targetList);
            org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[] result = new org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "groupFeatureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML getGroupFeatureMatchersArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML)get_store().find_element_user(GROUPFEATUREMATCHERS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "groupFeatureMatchers" element
     */
    public int sizeOfGroupFeatureMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(GROUPFEATUREMATCHERS$0);
        }
    }
    
    /**
     * Sets array of all "groupFeatureMatchers" element
     */
    public void setGroupFeatureMatchersArray(org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[] groupFeatureMatchersArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(groupFeatureMatchersArray, GROUPFEATUREMATCHERS$0);
        }
    }
    
    /**
     * Sets ith "groupFeatureMatchers" element
     */
    public void setGroupFeatureMatchersArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML groupFeatureMatchers)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML)get_store().find_element_user(GROUPFEATUREMATCHERS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(groupFeatureMatchers);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "groupFeatureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML insertNewGroupFeatureMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML)get_store().insert_element_user(GROUPFEATUREMATCHERS$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "groupFeatureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML addNewGroupFeatureMatchers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML)get_store().add_element_user(GROUPFEATUREMATCHERS$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "groupFeatureMatchers" element
     */
    public void removeGroupFeatureMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(GROUPFEATUREMATCHERS$0, i);
        }
    }
    
    /**
     * Gets the "annotationTypeName" attribute
     */
    public java.lang.String getAnnotationTypeName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ANNOTATIONTYPENAME$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "annotationTypeName" attribute
     */
    public org.apache.xmlbeans.XmlString xgetAnnotationTypeName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ANNOTATIONTYPENAME$2);
            return target;
        }
    }
    
    /**
     * Sets the "annotationTypeName" attribute
     */
    public void setAnnotationTypeName(java.lang.String annotationTypeName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ANNOTATIONTYPENAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ANNOTATIONTYPENAME$2);
            }
            target.setStringValue(annotationTypeName);
        }
    }
    
    /**
     * Sets (as xml) the "annotationTypeName" attribute
     */
    public void xsetAnnotationTypeName(org.apache.xmlbeans.XmlString annotationTypeName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ANNOTATIONTYPENAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ANNOTATIONTYPENAME$2);
            }
            target.set(annotationTypeName);
        }
    }
    
    /**
     * Gets the "fullPath" attribute
     */
    public java.lang.String getFullPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FULLPATH$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "fullPath" attribute
     */
    public org.apache.xmlbeans.XmlString xgetFullPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FULLPATH$4);
            return target;
        }
    }
    
    /**
     * True if has "fullPath" attribute
     */
    public boolean isSetFullPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FULLPATH$4) != null;
        }
    }
    
    /**
     * Sets the "fullPath" attribute
     */
    public void setFullPath(java.lang.String fullPath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FULLPATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FULLPATH$4);
            }
            target.setStringValue(fullPath);
        }
    }
    
    /**
     * Sets (as xml) the "fullPath" attribute
     */
    public void xsetFullPath(org.apache.xmlbeans.XmlString fullPath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FULLPATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FULLPATH$4);
            }
            target.set(fullPath);
        }
    }
    
    /**
     * Unsets the "fullPath" attribute
     */
    public void unsetFullPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FULLPATH$4);
        }
    }
}
