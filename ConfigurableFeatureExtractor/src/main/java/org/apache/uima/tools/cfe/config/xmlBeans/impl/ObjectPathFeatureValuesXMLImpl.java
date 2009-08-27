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
 * XML Type:  ObjectPathFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.ObjectPathFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * An XML ObjectPathFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public class ObjectPathFeatureValuesXMLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML
{
    
    public ObjectPathFeatureValuesXMLImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OBJECTPATH$0 = 
        new javax.xml.namespace.QName("", "objectPath");
    
    
    /**
     * Gets the "objectPath" attribute
     */
    public java.lang.String getObjectPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OBJECTPATH$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "objectPath" attribute
     */
    public org.apache.xmlbeans.XmlString xgetObjectPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OBJECTPATH$0);
            return target;
        }
    }
    
    /**
     * Sets the "objectPath" attribute
     */
    public void setObjectPath(java.lang.String objectPath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OBJECTPATH$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OBJECTPATH$0);
            }
            target.setStringValue(objectPath);
        }
    }
    
    /**
     * Sets (as xml) the "objectPath" attribute
     */
    public void xsetObjectPath(org.apache.xmlbeans.XmlString objectPath)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OBJECTPATH$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(OBJECTPATH$0);
            }
            target.set(objectPath);
        }
    }
}
