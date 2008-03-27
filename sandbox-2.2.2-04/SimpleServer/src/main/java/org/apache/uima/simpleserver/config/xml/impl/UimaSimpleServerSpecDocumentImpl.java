/*
 * An XML document type.
 * Localname: uimaSimpleServerSpec
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml.impl;
/**
 * A document containing one uimaSimpleServerSpec(@http://uima.apache.org/simpleserver/config/xml) element.
 *
 * This is a complex type.
 */
public class UimaSimpleServerSpecDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument
{
    
    public UimaSimpleServerSpecDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UIMASIMPLESERVERSPEC$0 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "uimaSimpleServerSpec");
    
    
    /**
     * Gets the "uimaSimpleServerSpec" element
     */
    public org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec getUimaSimpleServerSpec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec target = null;
            target = (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec)get_store().find_element_user(UIMASIMPLESERVERSPEC$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "uimaSimpleServerSpec" element
     */
    public void setUimaSimpleServerSpec(org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec uimaSimpleServerSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec target = null;
            target = (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec)get_store().find_element_user(UIMASIMPLESERVERSPEC$0, 0);
            if (target == null)
            {
                target = (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec)get_store().add_element_user(UIMASIMPLESERVERSPEC$0);
            }
            target.set(uimaSimpleServerSpec);
        }
    }
    
    /**
     * Appends and returns a new empty "uimaSimpleServerSpec" element
     */
    public org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec addNewUimaSimpleServerSpec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec target = null;
            target = (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec)get_store().add_element_user(UIMASIMPLESERVERSPEC$0);
            return target;
        }
    }
    /**
     * An XML uimaSimpleServerSpec(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public static class UimaSimpleServerSpecImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec
    {
        
        public UimaSimpleServerSpecImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SHORTDESCRIPTION$0 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "shortDescription");
        private static final javax.xml.namespace.QName LONGDESCRIPTION$2 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "longDescription");
        private static final javax.xml.namespace.QName TYPE$4 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "type");
        private static final javax.xml.namespace.QName OUTPUTALL$6 = 
            new javax.xml.namespace.QName("", "outputAll");
        
        
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
         * Gets a List of "type" elements
         */
        public java.util.List<org.apache.uima.simpleserver.config.xml.TypeElementType> getTypeList()
        {
            final class TypeList extends java.util.AbstractList<org.apache.uima.simpleserver.config.xml.TypeElementType>
            {
                public org.apache.uima.simpleserver.config.xml.TypeElementType get(int i)
                    { return UimaSimpleServerSpecImpl.this.getTypeArray(i); }
                
                public org.apache.uima.simpleserver.config.xml.TypeElementType set(int i, org.apache.uima.simpleserver.config.xml.TypeElementType o)
                {
                    org.apache.uima.simpleserver.config.xml.TypeElementType old = UimaSimpleServerSpecImpl.this.getTypeArray(i);
                    UimaSimpleServerSpecImpl.this.setTypeArray(i, o);
                    return old;
                }
                
                public void add(int i, org.apache.uima.simpleserver.config.xml.TypeElementType o)
                    { UimaSimpleServerSpecImpl.this.insertNewType(i).set(o); }
                
                public org.apache.uima.simpleserver.config.xml.TypeElementType remove(int i)
                {
                    org.apache.uima.simpleserver.config.xml.TypeElementType old = UimaSimpleServerSpecImpl.this.getTypeArray(i);
                    UimaSimpleServerSpecImpl.this.removeType(i);
                    return old;
                }
                
                public int size()
                    { return UimaSimpleServerSpecImpl.this.sizeOfTypeArray(); }
                
            }
            
            synchronized (monitor())
            {
                check_orphaned();
                return new TypeList();
            }
        }
        
        /**
         * Gets array of all "type" elements
         */
        public org.apache.uima.simpleserver.config.xml.TypeElementType[] getTypeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(TYPE$4, targetList);
                org.apache.uima.simpleserver.config.xml.TypeElementType[] result = new org.apache.uima.simpleserver.config.xml.TypeElementType[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "type" element
         */
        public org.apache.uima.simpleserver.config.xml.TypeElementType getTypeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.TypeElementType target = null;
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType)get_store().find_element_user(TYPE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "type" element
         */
        public int sizeOfTypeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TYPE$4);
            }
        }
        
        /**
         * Sets array of all "type" element
         */
        public void setTypeArray(org.apache.uima.simpleserver.config.xml.TypeElementType[] typeArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(typeArray, TYPE$4);
            }
        }
        
        /**
         * Sets ith "type" element
         */
        public void setTypeArray(int i, org.apache.uima.simpleserver.config.xml.TypeElementType type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.TypeElementType target = null;
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType)get_store().find_element_user(TYPE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(type);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "type" element
         */
        public org.apache.uima.simpleserver.config.xml.TypeElementType insertNewType(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.TypeElementType target = null;
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType)get_store().insert_element_user(TYPE$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "type" element
         */
        public org.apache.uima.simpleserver.config.xml.TypeElementType addNewType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.TypeElementType target = null;
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType)get_store().add_element_user(TYPE$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "type" element
         */
        public void removeType(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TYPE$4, i);
            }
        }
        
        /**
         * Gets the "outputAll" attribute
         */
        public boolean getOutputAll()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTALL$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(OUTPUTALL$6);
                }
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "outputAll" attribute
         */
        public org.apache.xmlbeans.XmlBoolean xgetOutputAll()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTALL$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(OUTPUTALL$6);
                }
                return target;
            }
        }
        
        /**
         * True if has "outputAll" attribute
         */
        public boolean isSetOutputAll()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(OUTPUTALL$6) != null;
            }
        }
        
        /**
         * Sets the "outputAll" attribute
         */
        public void setOutputAll(boolean outputAll)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTALL$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTPUTALL$6);
                }
                target.setBooleanValue(outputAll);
            }
        }
        
        /**
         * Sets (as xml) the "outputAll" attribute
         */
        public void xsetOutputAll(org.apache.xmlbeans.XmlBoolean outputAll)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTALL$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(OUTPUTALL$6);
                }
                target.set(outputAll);
            }
        }
        
        /**
         * Unsets the "outputAll" attribute
         */
        public void unsetOutputAll()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(OUTPUTALL$6);
            }
        }
    }
}
