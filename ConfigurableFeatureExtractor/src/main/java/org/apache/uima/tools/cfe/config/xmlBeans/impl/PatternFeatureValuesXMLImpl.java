/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * XML Type:  PatternFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.PatternFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML PatternFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class PatternFeatureValuesXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML
{
    
    public PatternFeatureValuesXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PATTERN$0 = 
        new javax.xml.namespace.QName("", "pattern");
    
    
    /**
     * Gets the "pattern" attribute
     */
    public java.lang.String getPattern()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATTERN$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "pattern" attribute
     */
    public org.apache.xmlbeans.XmlString xgetPattern()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PATTERN$0);
            return target;
        }
    }
    
    /**
     * True if has "pattern" attribute
     */
    public boolean isSetPattern()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(PATTERN$0) != null;
        }
    }
    
    /**
     * Sets the "pattern" attribute
     */
    public void setPattern(java.lang.String pattern)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATTERN$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PATTERN$0);
            }
            target.setStringValue(pattern);
        }
    }
    
    /**
     * Sets (as xml) the "pattern" attribute
     */
    public void xsetPattern(org.apache.xmlbeans.XmlString pattern)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PATTERN$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PATTERN$0);
            }
            target.set(pattern);
        }
    }
    
    /**
     * Unsets the "pattern" attribute
     */
    public void unsetPattern()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(PATTERN$0);
        }
    }
}
