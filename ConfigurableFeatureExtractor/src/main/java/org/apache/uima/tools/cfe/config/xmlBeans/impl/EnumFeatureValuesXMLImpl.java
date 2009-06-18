/*
 * XML Type:  EnumFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.EnumFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML EnumFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class EnumFeatureValuesXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML
{
    
    public EnumFeatureValuesXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUES$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "values");
    private static final javax.xml.namespace.QName CASESENSITIVE$2 = 
        new javax.xml.namespace.QName("", "caseSensitive");
    
    
    /**
     * Gets array of all "values" elements
     */
    public java.lang.String[] getValuesArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VALUES$0, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "values" element
     */
    public java.lang.String getValuesArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUES$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "values" elements
     */
    public org.apache.xmlbeans.XmlString[] xgetValuesArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VALUES$0, targetList);
            org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "values" element
     */
    public org.apache.xmlbeans.XmlString xgetValuesArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUES$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.apache.xmlbeans.XmlString)target;
        }
    }
    
    /**
     * Returns number of "values" element
     */
    public int sizeOfValuesArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VALUES$0);
        }
    }
    
    /**
     * Sets array of all "values" element
     */
    public void setValuesArray(java.lang.String[] valuesArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(valuesArray, VALUES$0);
        }
    }
    
    /**
     * Sets ith "values" element
     */
    public void setValuesArray(int i, java.lang.String values)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUES$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(values);
        }
    }
    
    /**
     * Sets (as xml) array of all "values" element
     */
    public void xsetValuesArray(org.apache.xmlbeans.XmlString[]valuesArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(valuesArray, VALUES$0);
        }
    }
    
    /**
     * Sets (as xml) ith "values" element
     */
    public void xsetValuesArray(int i, org.apache.xmlbeans.XmlString values)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUES$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(values);
        }
    }
    
    /**
     * Inserts the value as the ith "values" element
     */
    public void insertValues(int i, java.lang.String values)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(VALUES$0, i);
            target.setStringValue(values);
        }
    }
    
    /**
     * Appends the value as the last "values" element
     */
    public void addValues(java.lang.String values)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUES$0);
            target.setStringValue(values);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "values" element
     */
    public org.apache.xmlbeans.XmlString insertNewValues(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(VALUES$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "values" element
     */
    public org.apache.xmlbeans.XmlString addNewValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUES$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "values" element
     */
    public void removeValues(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VALUES$0, i);
        }
    }
    
    /**
     * Gets the "caseSensitive" attribute
     */
    public boolean getCaseSensitive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CASESENSITIVE$2);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "caseSensitive" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetCaseSensitive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(CASESENSITIVE$2);
            return target;
        }
    }
    
    /**
     * True if has "caseSensitive" attribute
     */
    public boolean isSetCaseSensitive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CASESENSITIVE$2) != null;
        }
    }
    
    /**
     * Sets the "caseSensitive" attribute
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CASESENSITIVE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CASESENSITIVE$2);
            }
            target.setBooleanValue(caseSensitive);
        }
    }
    
    /**
     * Sets (as xml) the "caseSensitive" attribute
     */
    public void xsetCaseSensitive(org.apache.xmlbeans.XmlBoolean caseSensitive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(CASESENSITIVE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(CASESENSITIVE$2);
            }
            target.set(caseSensitive);
        }
    }
    
    /**
     * Unsets the "caseSensitive" attribute
     */
    public void unsetCaseSensitive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CASESENSITIVE$2);
        }
    }
}
