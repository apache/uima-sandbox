/*
 * XML Type:  GroupFeatureMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.GroupFeatureMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML GroupFeatureMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class GroupFeatureMatcherXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML
{
    
    public GroupFeatureMatcherXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FEATUREMATCHERS$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "featureMatchers");
    private static final javax.xml.namespace.QName EXCLUDE$2 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "exclude");
    
    
    /**
     * Gets array of all "featureMatchers" elements
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML[] getFeatureMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FEATUREMATCHERS$0, targetList);
            org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML[] result = new org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "featureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML getFeatureMatchersArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML)get_store().find_element_user(FEATUREMATCHERS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "featureMatchers" element
     */
    public int sizeOfFeatureMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FEATUREMATCHERS$0);
        }
    }
    
    /**
     * Sets array of all "featureMatchers" element
     */
    public void setFeatureMatchersArray(org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML[] featureMatchersArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(featureMatchersArray, FEATUREMATCHERS$0);
        }
    }
    
    /**
     * Sets ith "featureMatchers" element
     */
    public void setFeatureMatchersArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML featureMatchers)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML)get_store().find_element_user(FEATUREMATCHERS$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(featureMatchers);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "featureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML insertNewFeatureMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML)get_store().insert_element_user(FEATUREMATCHERS$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "featureMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML addNewFeatureMatchers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML)get_store().add_element_user(FEATUREMATCHERS$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "featureMatchers" element
     */
    public void removeFeatureMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FEATUREMATCHERS$0, i);
        }
    }
    
    /**
     * Gets the "exclude" element
     */
    public boolean getExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXCLUDE$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "exclude" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(EXCLUDE$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "exclude" element
     */
    public boolean isSetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(EXCLUDE$2) != 0;
        }
    }
    
    /**
     * Sets the "exclude" element
     */
    public void setExclude(boolean exclude)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXCLUDE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EXCLUDE$2);
            }
            target.setBooleanValue(exclude);
        }
    }
    
    /**
     * Sets (as xml) the "exclude" element
     */
    public void xsetExclude(org.apache.xmlbeans.XmlBoolean exclude)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(EXCLUDE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(EXCLUDE$2);
            }
            target.set(exclude);
        }
    }
    
    /**
     * Unsets the "exclude" element
     */
    public void unsetExclude()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(EXCLUDE$2, 0);
        }
    }
}
