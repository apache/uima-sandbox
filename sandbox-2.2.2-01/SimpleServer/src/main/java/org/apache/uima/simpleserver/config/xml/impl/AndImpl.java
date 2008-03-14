/*
 * XML Type:  and
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.And
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml.impl;
/**
 * An XML and(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public class AndImpl extends org.apache.uima.simpleserver.config.xml.impl.FilterTypeImpl implements org.apache.uima.simpleserver.config.xml.And
{
    
    public AndImpl(org.apache.xmlbeans.SchemaType sType)
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
     * Gets a List of "filter" elements
     */
    public java.util.List<org.apache.uima.simpleserver.config.xml.SimpleFilterType> getFilterList()
    {
        final class FilterList extends java.util.AbstractList<org.apache.uima.simpleserver.config.xml.SimpleFilterType>
        {
            public org.apache.uima.simpleserver.config.xml.SimpleFilterType get(int i)
                { return AndImpl.this.getFilterArray(i); }
            
            public org.apache.uima.simpleserver.config.xml.SimpleFilterType set(int i, org.apache.uima.simpleserver.config.xml.SimpleFilterType o)
            {
                org.apache.uima.simpleserver.config.xml.SimpleFilterType old = AndImpl.this.getFilterArray(i);
                AndImpl.this.setFilterArray(i, o);
                return old;
            }
            
            public void add(int i, org.apache.uima.simpleserver.config.xml.SimpleFilterType o)
                { AndImpl.this.insertNewFilter(i).set(o); }
            
            public org.apache.uima.simpleserver.config.xml.SimpleFilterType remove(int i)
            {
                org.apache.uima.simpleserver.config.xml.SimpleFilterType old = AndImpl.this.getFilterArray(i);
                AndImpl.this.removeFilter(i);
                return old;
            }
            
            public int size()
                { return AndImpl.this.sizeOfFilterArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new FilterList();
        }
    }
    
    /**
     * Gets array of all "filter" elements
     */
    public org.apache.uima.simpleserver.config.xml.SimpleFilterType[] getFilterArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FILTER$0, targetList);
            org.apache.uima.simpleserver.config.xml.SimpleFilterType[] result = new org.apache.uima.simpleserver.config.xml.SimpleFilterType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "filter" element
     */
    public org.apache.uima.simpleserver.config.xml.SimpleFilterType getFilterArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
            target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().find_element_user(FILTER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "filter" element
     */
    public int sizeOfFilterArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FILTER$0);
        }
    }
    
    /**
     * Sets array of all "filter" element
     */
    public void setFilterArray(org.apache.uima.simpleserver.config.xml.SimpleFilterType[] filterArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(filterArray, FILTER$0);
        }
    }
    
    /**
     * Sets ith "filter" element
     */
    public void setFilterArray(int i, org.apache.uima.simpleserver.config.xml.SimpleFilterType filter)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
            target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().find_element_user(FILTER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(filter);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "filter" element
     */
    public org.apache.uima.simpleserver.config.xml.SimpleFilterType insertNewFilter(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.SimpleFilterType target = null;
            target = (org.apache.uima.simpleserver.config.xml.SimpleFilterType)get_store().insert_element_user(FILTER$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "filter" element
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
     * Removes the ith "filter" element
     */
    public void removeFilter(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FILTER$0, i);
        }
    }
    
    /**
     * Gets a List of "or" elements
     */
    public java.util.List<org.apache.uima.simpleserver.config.xml.Or> getOrList()
    {
        final class OrList extends java.util.AbstractList<org.apache.uima.simpleserver.config.xml.Or>
        {
            public org.apache.uima.simpleserver.config.xml.Or get(int i)
                { return AndImpl.this.getOrArray(i); }
            
            public org.apache.uima.simpleserver.config.xml.Or set(int i, org.apache.uima.simpleserver.config.xml.Or o)
            {
                org.apache.uima.simpleserver.config.xml.Or old = AndImpl.this.getOrArray(i);
                AndImpl.this.setOrArray(i, o);
                return old;
            }
            
            public void add(int i, org.apache.uima.simpleserver.config.xml.Or o)
                { AndImpl.this.insertNewOr(i).set(o); }
            
            public org.apache.uima.simpleserver.config.xml.Or remove(int i)
            {
                org.apache.uima.simpleserver.config.xml.Or old = AndImpl.this.getOrArray(i);
                AndImpl.this.removeOr(i);
                return old;
            }
            
            public int size()
                { return AndImpl.this.sizeOfOrArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new OrList();
        }
    }
    
    /**
     * Gets array of all "or" elements
     */
    public org.apache.uima.simpleserver.config.xml.Or[] getOrArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(OR$2, targetList);
            org.apache.uima.simpleserver.config.xml.Or[] result = new org.apache.uima.simpleserver.config.xml.Or[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "or" element
     */
    public org.apache.uima.simpleserver.config.xml.Or getOrArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.Or target = null;
            target = (org.apache.uima.simpleserver.config.xml.Or)get_store().find_element_user(OR$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "or" element
     */
    public int sizeOfOrArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OR$2);
        }
    }
    
    /**
     * Sets array of all "or" element
     */
    public void setOrArray(org.apache.uima.simpleserver.config.xml.Or[] orArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(orArray, OR$2);
        }
    }
    
    /**
     * Sets ith "or" element
     */
    public void setOrArray(int i, org.apache.uima.simpleserver.config.xml.Or or)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.Or target = null;
            target = (org.apache.uima.simpleserver.config.xml.Or)get_store().find_element_user(OR$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(or);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "or" element
     */
    public org.apache.uima.simpleserver.config.xml.Or insertNewOr(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.Or target = null;
            target = (org.apache.uima.simpleserver.config.xml.Or)get_store().insert_element_user(OR$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "or" element
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
     * Removes the ith "or" element
     */
    public void removeOr(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OR$2, i);
        }
    }
    
    /**
     * Gets a List of "and" elements
     */
    public java.util.List<org.apache.uima.simpleserver.config.xml.And> getAndList()
    {
        final class AndList extends java.util.AbstractList<org.apache.uima.simpleserver.config.xml.And>
        {
            public org.apache.uima.simpleserver.config.xml.And get(int i)
                { return AndImpl.this.getAndArray(i); }
            
            public org.apache.uima.simpleserver.config.xml.And set(int i, org.apache.uima.simpleserver.config.xml.And o)
            {
                org.apache.uima.simpleserver.config.xml.And old = AndImpl.this.getAndArray(i);
                AndImpl.this.setAndArray(i, o);
                return old;
            }
            
            public void add(int i, org.apache.uima.simpleserver.config.xml.And o)
                { AndImpl.this.insertNewAnd(i).set(o); }
            
            public org.apache.uima.simpleserver.config.xml.And remove(int i)
            {
                org.apache.uima.simpleserver.config.xml.And old = AndImpl.this.getAndArray(i);
                AndImpl.this.removeAnd(i);
                return old;
            }
            
            public int size()
                { return AndImpl.this.sizeOfAndArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new AndList();
        }
    }
    
    /**
     * Gets array of all "and" elements
     */
    public org.apache.uima.simpleserver.config.xml.And[] getAndArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(AND$4, targetList);
            org.apache.uima.simpleserver.config.xml.And[] result = new org.apache.uima.simpleserver.config.xml.And[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "and" element
     */
    public org.apache.uima.simpleserver.config.xml.And getAndArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.And target = null;
            target = (org.apache.uima.simpleserver.config.xml.And)get_store().find_element_user(AND$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "and" element
     */
    public int sizeOfAndArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(AND$4);
        }
    }
    
    /**
     * Sets array of all "and" element
     */
    public void setAndArray(org.apache.uima.simpleserver.config.xml.And[] andArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(andArray, AND$4);
        }
    }
    
    /**
     * Sets ith "and" element
     */
    public void setAndArray(int i, org.apache.uima.simpleserver.config.xml.And and)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.And target = null;
            target = (org.apache.uima.simpleserver.config.xml.And)get_store().find_element_user(AND$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(and);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "and" element
     */
    public org.apache.uima.simpleserver.config.xml.And insertNewAnd(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.simpleserver.config.xml.And target = null;
            target = (org.apache.uima.simpleserver.config.xml.And)get_store().insert_element_user(AND$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "and" element
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
     * Removes the ith "and" element
     */
    public void removeAnd(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(AND$4, i);
        }
    }
}
