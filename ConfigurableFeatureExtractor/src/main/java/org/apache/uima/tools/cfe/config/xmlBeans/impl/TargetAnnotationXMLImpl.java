/*
 * XML Type:  TargetAnnotationXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.TargetAnnotationXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML TargetAnnotationXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class TargetAnnotationXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML
{
    
    public TargetAnnotationXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TARGETANNOTATIONMATCHER$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "targetAnnotationMatcher");
    private static final javax.xml.namespace.QName FEATUREANNOTATIONMATCHERS$2 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "featureAnnotationMatchers");
    private static final javax.xml.namespace.QName CLASSNAME$4 = 
        new javax.xml.namespace.QName("", "className");
    private static final javax.xml.namespace.QName ENCLOSINGANNOTATION$6 = 
        new javax.xml.namespace.QName("", "enclosingAnnotation");
    
    
    /**
     * Gets the "targetAnnotationMatcher" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML getTargetAnnotationMatcher()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML)get_store().find_element_user(TARGETANNOTATIONMATCHER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "targetAnnotationMatcher" element
     */
    public void setTargetAnnotationMatcher(org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML targetAnnotationMatcher)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML)get_store().find_element_user(TARGETANNOTATIONMATCHER$0, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML)get_store().add_element_user(TARGETANNOTATIONMATCHER$0);
            }
            target.set(targetAnnotationMatcher);
        }
    }
    
    /**
     * Appends and returns a new empty "targetAnnotationMatcher" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML addNewTargetAnnotationMatcher()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML)get_store().add_element_user(TARGETANNOTATIONMATCHER$0);
            return target;
        }
    }
    
    /**
     * Gets array of all "featureAnnotationMatchers" elements
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[] getFeatureAnnotationMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FEATUREANNOTATIONMATCHERS$2, targetList);
            org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[] result = new org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "featureAnnotationMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML getFeatureAnnotationMatchersArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML)get_store().find_element_user(FEATUREANNOTATIONMATCHERS$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "featureAnnotationMatchers" element
     */
    public int sizeOfFeatureAnnotationMatchersArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FEATUREANNOTATIONMATCHERS$2);
        }
    }
    
    /**
     * Sets array of all "featureAnnotationMatchers" element
     */
    public void setFeatureAnnotationMatchersArray(org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[] featureAnnotationMatchersArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(featureAnnotationMatchersArray, FEATUREANNOTATIONMATCHERS$2);
        }
    }
    
    /**
     * Sets ith "featureAnnotationMatchers" element
     */
    public void setFeatureAnnotationMatchersArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML featureAnnotationMatchers)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML)get_store().find_element_user(FEATUREANNOTATIONMATCHERS$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(featureAnnotationMatchers);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "featureAnnotationMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML insertNewFeatureAnnotationMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML)get_store().insert_element_user(FEATUREANNOTATIONMATCHERS$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "featureAnnotationMatchers" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML addNewFeatureAnnotationMatchers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML)get_store().add_element_user(FEATUREANNOTATIONMATCHERS$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "featureAnnotationMatchers" element
     */
    public void removeFeatureAnnotationMatchers(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FEATUREANNOTATIONMATCHERS$2, i);
        }
    }
    
    /**
     * Gets the "className" attribute
     */
    public java.lang.String getClassName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CLASSNAME$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "className" attribute
     */
    public org.apache.xmlbeans.XmlString xgetClassName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CLASSNAME$4);
            return target;
        }
    }
    
    /**
     * Sets the "className" attribute
     */
    public void setClassName(java.lang.String className)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CLASSNAME$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CLASSNAME$4);
            }
            target.setStringValue(className);
        }
    }
    
    /**
     * Sets (as xml) the "className" attribute
     */
    public void xsetClassName(org.apache.xmlbeans.XmlString className)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CLASSNAME$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(CLASSNAME$4);
            }
            target.set(className);
        }
    }
    
    /**
     * Gets the "enclosingAnnotation" attribute
     */
    public java.lang.String getEnclosingAnnotation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ENCLOSINGANNOTATION$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "enclosingAnnotation" attribute
     */
    public org.apache.xmlbeans.XmlString xgetEnclosingAnnotation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ENCLOSINGANNOTATION$6);
            return target;
        }
    }
    
    /**
     * Sets the "enclosingAnnotation" attribute
     */
    public void setEnclosingAnnotation(java.lang.String enclosingAnnotation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ENCLOSINGANNOTATION$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ENCLOSINGANNOTATION$6);
            }
            target.setStringValue(enclosingAnnotation);
        }
    }
    
    /**
     * Sets (as xml) the "enclosingAnnotation" attribute
     */
    public void xsetEnclosingAnnotation(org.apache.xmlbeans.XmlString enclosingAnnotation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ENCLOSINGANNOTATION$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ENCLOSINGANNOTATION$6);
            }
            target.set(enclosingAnnotation);
        }
    }
}
