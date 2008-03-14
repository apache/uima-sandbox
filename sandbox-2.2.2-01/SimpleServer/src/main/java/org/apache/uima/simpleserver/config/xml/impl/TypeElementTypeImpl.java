/*
 * XML Type:  TypeElementType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.TypeElementType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml.impl;
/**
 * An XML TypeElementType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public class TypeElementTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.TypeElementType
{
    
    public TypeElementTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SHORTDESCRIPTION$0 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "shortDescription");
    private static final javax.xml.namespace.QName LONGDESCRIPTION$2 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "longDescription");
    private static final javax.xml.namespace.QName FILTERS$4 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "filters");
    private static final javax.xml.namespace.QName OUTPUTS$6 = 
        new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "outputs");
    private static final javax.xml.namespace.QName NAME$8 = 
        new javax.xml.namespace.QName("", "name");
    private static final javax.xml.namespace.QName OUTPUTTAG$10 = 
        new javax.xml.namespace.QName("", "outputTag");
    private static final javax.xml.namespace.QName OUTPUTCOVEREDTEXT$12 = 
        new javax.xml.namespace.QName("", "outputCoveredText");
    private static final javax.xml.namespace.QName OUTPUTALL$14 = 
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
     * Gets the "filters" element
     */
    public org.apache.uima.simpleserver.config.xml.TypeElementType.Filters getFilters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Filters target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters)get_store().find_element_user(FILTERS$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "filters" element
     */
    public boolean isSetFilters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FILTERS$4) != 0;
        }
    }
    
    /**
     * Sets the "filters" element
     */
    public void setFilters(org.apache.uima.simpleserver.config.xml.TypeElementType.Filters filters)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Filters target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters)get_store().find_element_user(FILTERS$4, 0);
            if (target == null)
            {
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters)get_store().add_element_user(FILTERS$4);
            }
            target.set(filters);
        }
    }
    
    /**
     * Appends and returns a new empty "filters" element
     */
    public org.apache.uima.simpleserver.config.xml.TypeElementType.Filters addNewFilters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Filters target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters)get_store().add_element_user(FILTERS$4);
            return target;
        }
    }
    
    /**
     * Unsets the "filters" element
     */
    public void unsetFilters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FILTERS$4, 0);
        }
    }
    
    /**
     * Gets the "outputs" element
     */
    public org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs getOutputs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs)get_store().find_element_user(OUTPUTS$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "outputs" element
     */
    public boolean isSetOutputs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OUTPUTS$6) != 0;
        }
    }
    
    /**
     * Sets the "outputs" element
     */
    public void setOutputs(org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs outputs)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs)get_store().find_element_user(OUTPUTS$6, 0);
            if (target == null)
            {
                target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs)get_store().add_element_user(OUTPUTS$6);
            }
            target.set(outputs);
        }
    }
    
    /**
     * Appends and returns a new empty "outputs" element
     */
    public org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs addNewOutputs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs target = null;
            target = (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs)get_store().add_element_user(OUTPUTS$6);
            return target;
        }
    }
    
    /**
     * Unsets the "outputs" element
     */
    public void unsetOutputs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OUTPUTS$6, 0);
        }
    }
    
    /**
     * Gets the "name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "name" attribute
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$8);
            return target;
        }
    }
    
    /**
     * Sets the "name" attribute
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$8);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "name" attribute
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$8);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "outputTag" attribute
     */
    public java.lang.String getOutputTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTTAG$10);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "outputTag" attribute
     */
    public org.apache.xmlbeans.XmlString xgetOutputTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OUTPUTTAG$10);
            return target;
        }
    }
    
    /**
     * Sets the "outputTag" attribute
     */
    public void setOutputTag(java.lang.String outputTag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTTAG$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTPUTTAG$10);
            }
            target.setStringValue(outputTag);
        }
    }
    
    /**
     * Sets (as xml) the "outputTag" attribute
     */
    public void xsetOutputTag(org.apache.xmlbeans.XmlString outputTag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OUTPUTTAG$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(OUTPUTTAG$10);
            }
            target.set(outputTag);
        }
    }
    
    /**
     * Gets the "outputCoveredText" attribute
     */
    public boolean getOutputCoveredText()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTCOVEREDTEXT$12);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "outputCoveredText" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetOutputCoveredText()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTCOVEREDTEXT$12);
            return target;
        }
    }
    
    /**
     * True if has "outputCoveredText" attribute
     */
    public boolean isSetOutputCoveredText()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(OUTPUTCOVEREDTEXT$12) != null;
        }
    }
    
    /**
     * Sets the "outputCoveredText" attribute
     */
    public void setOutputCoveredText(boolean outputCoveredText)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTCOVEREDTEXT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTPUTCOVEREDTEXT$12);
            }
            target.setBooleanValue(outputCoveredText);
        }
    }
    
    /**
     * Sets (as xml) the "outputCoveredText" attribute
     */
    public void xsetOutputCoveredText(org.apache.xmlbeans.XmlBoolean outputCoveredText)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTCOVEREDTEXT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(OUTPUTCOVEREDTEXT$12);
            }
            target.set(outputCoveredText);
        }
    }
    
    /**
     * Unsets the "outputCoveredText" attribute
     */
    public void unsetOutputCoveredText()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(OUTPUTCOVEREDTEXT$12);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTALL$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(OUTPUTALL$14);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTALL$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(OUTPUTALL$14);
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
            return get_store().find_attribute_user(OUTPUTALL$14) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OUTPUTALL$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OUTPUTALL$14);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(OUTPUTALL$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(OUTPUTALL$14);
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
            get_store().remove_attribute(OUTPUTALL$14);
        }
    }
    /**
     * An XML filters(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public static class FiltersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.TypeElementType.Filters
    {
        
        public FiltersImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName FILTER$0 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "filter");
        private static final javax.xml.namespace.QName OR$2 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "or");
        private static final javax.xml.namespace.QName AND$4 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "and");
        
        
        /**
         * Gets the "filter" element
         */
        public org.apache.uima.simpleserver.config.xml.SimpleFilterType getFilter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
                target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().find_element_user(FILTER$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "filter" element
         */
        public boolean isSetFilter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(FILTER$0) != 0;
            }
        }
        
        /**
         * Sets the "filter" element
         */
        public void setFilter(org.apache.uima.simpleserver.config.xml.SimpleFilterType filter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
                target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().find_element_user(FILTER$0, 0);
                if (target == null)
                {
                    target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().add_element_user(FILTER$0);
                }
                target.set(filter);
            }
        }
        
        /**
         * Appends and returns a new empty "filter" element
         */
        public org.apache.uima.simpleserver.config.xml.SimpleFilterType addNewFilter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
                target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().add_element_user(FILTER$0);
                return target;
            }
        }
        
        /**
         * Unsets the "filter" element
         */
        public void unsetFilter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(FILTER$0, 0);
            }
        }
        
        /**
         * Gets the "or" element
         */
        public org.apache.uima.simpleserver.config.xml.Or getOr()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.Or target = null;
                target = (org.apache.uima.simpleserver.config.xml.Or)get_store().find_element_user(OR$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "or" element
         */
        public boolean isSetOr()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(OR$2) != 0;
            }
        }
        
        /**
         * Sets the "or" element
         */
        public void setOr(org.apache.uima.simpleserver.config.xml.Or or)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.Or target = null;
                target = (org.apache.uima.simpleserver.config.xml.Or)get_store().find_element_user(OR$2, 0);
                if (target == null)
                {
                    target = (org.apache.uima.simpleserver.config.xml.Or)get_store().add_element_user(OR$2);
                }
                target.set(or);
            }
        }
        
        /**
         * Appends and returns a new empty "or" element
         */
        public org.apache.uima.simpleserver.config.xml.Or addNewOr()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.Or target = null;
                target = (org.apache.uima.simpleserver.config.xml.Or)get_store().add_element_user(OR$2);
                return target;
            }
        }
        
        /**
         * Unsets the "or" element
         */
        public void unsetOr()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(OR$2, 0);
            }
        }
        
        /**
         * Gets the "and" element
         */
        public org.apache.uima.simpleserver.config.xml.And getAnd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.And target = null;
                target = (org.apache.uima.simpleserver.config.xml.And)get_store().find_element_user(AND$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "and" element
         */
        public boolean isSetAnd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(AND$4) != 0;
            }
        }
        
        /**
         * Sets the "and" element
         */
        public void setAnd(org.apache.uima.simpleserver.config.xml.And and)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.And target = null;
                target = (org.apache.uima.simpleserver.config.xml.And)get_store().find_element_user(AND$4, 0);
                if (target == null)
                {
                    target = (org.apache.uima.simpleserver.config.xml.And)get_store().add_element_user(AND$4);
                }
                target.set(and);
            }
        }
        
        /**
         * Appends and returns a new empty "and" element
         */
        public org.apache.uima.simpleserver.config.xml.And addNewAnd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.And target = null;
                target = (org.apache.uima.simpleserver.config.xml.And)get_store().add_element_user(AND$4);
                return target;
            }
        }
        
        /**
         * Unsets the "and" element
         */
        public void unsetAnd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(AND$4, 0);
            }
        }
    }
    /**
     * An XML outputs(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public static class OutputsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs
    {
        
        public OutputsImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName OUTPUT$0 = 
            new javax.xml.namespace.QName("http://uima.apache.org/simpleserver/config/xml", "output");
        
        
        /**
         * Gets a List of "output" elements
         */
        public java.util.List<org.apache.uima.simpleserver.config.xml.OutputType> getOutputList()
        {
            final class OutputList extends java.util.AbstractList<org.apache.uima.simpleserver.config.xml.OutputType>
            {
                public org.apache.uima.simpleserver.config.xml.OutputType get(int i)
                    { return OutputsImpl.this.getOutputArray(i); }
                
                public org.apache.uima.simpleserver.config.xml.OutputType set(int i, org.apache.uima.simpleserver.config.xml.OutputType o)
                {
                    org.apache.uima.simpleserver.config.xml.OutputType old = OutputsImpl.this.getOutputArray(i);
                    OutputsImpl.this.setOutputArray(i, o);
                    return old;
                }
                
                public void add(int i, org.apache.uima.simpleserver.config.xml.OutputType o)
                    { OutputsImpl.this.insertNewOutput(i).set(o); }
                
                public org.apache.uima.simpleserver.config.xml.OutputType remove(int i)
                {
                    org.apache.uima.simpleserver.config.xml.OutputType old = OutputsImpl.this.getOutputArray(i);
                    OutputsImpl.this.removeOutput(i);
                    return old;
                }
                
                public int size()
                    { return OutputsImpl.this.sizeOfOutputArray(); }
                
            }
            
            synchronized (monitor())
            {
                check_orphaned();
                return new OutputList();
            }
        }
        
        /**
         * Gets array of all "output" elements
         */
        public org.apache.uima.simpleserver.config.xml.OutputType[] getOutputArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(OUTPUT$0, targetList);
                org.apache.uima.simpleserver.config.xml.OutputType[] result = new org.apache.uima.simpleserver.config.xml.OutputType[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "output" element
         */
        public org.apache.uima.simpleserver.config.xml.OutputType getOutputArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.OutputType target = null;
                target = (org.apache.uima.simpleserver.config.xml.OutputType)get_store().find_element_user(OUTPUT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "output" element
         */
        public int sizeOfOutputArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(OUTPUT$0);
            }
        }
        
        /**
         * Sets array of all "output" element
         */
        public void setOutputArray(org.apache.uima.simpleserver.config.xml.OutputType[] outputArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(outputArray, OUTPUT$0);
            }
        }
        
        /**
         * Sets ith "output" element
         */
        public void setOutputArray(int i, org.apache.uima.simpleserver.config.xml.OutputType output)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.OutputType target = null;
                target = (org.apache.uima.simpleserver.config.xml.OutputType)get_store().find_element_user(OUTPUT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(output);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "output" element
         */
        public org.apache.uima.simpleserver.config.xml.OutputType insertNewOutput(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.OutputType target = null;
                target = (org.apache.uima.simpleserver.config.xml.OutputType)get_store().insert_element_user(OUTPUT$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "output" element
         */
        public org.apache.uima.simpleserver.config.xml.OutputType addNewOutput()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.uima.simpleserver.config.xml.OutputType target = null;
                target = (org.apache.uima.simpleserver.config.xml.OutputType)get_store().add_element_user(OUTPUT$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "output" element
         */
        public void removeOutput(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(OUTPUT$0, i);
            }
        }
    }
}
