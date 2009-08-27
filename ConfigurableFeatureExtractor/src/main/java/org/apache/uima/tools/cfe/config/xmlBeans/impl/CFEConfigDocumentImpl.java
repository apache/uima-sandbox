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
 * An XML document type.
 * Localname: CFEConfig
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.CFEConfigDocument
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans.impl;
/**
 * A document containing one CFEConfig(@http://www.apache.org/uima/cfe/config/XMLBeans) element.
 *
 * This is a complex type.
 */
public class CFEConfigDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.uima.tools.cfe.config.xmlBeans.CFEConfigDocument
{
    
    public CFEConfigDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CFECONFIG$0 = 
        new javax.xml.namespace.QName("http://www.apache.org/uima/cfe/config/XMLBeans", "CFEConfig");
    
    
    /**
     * Gets the "CFEConfig" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML getCFEConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML)get_store().find_element_user(CFECONFIG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CFEConfig" element
     */
    public void setCFEConfig(org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML cfeConfig)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML)get_store().find_element_user(CFECONFIG$0, 0);
            if (target == null)
            {
                target = (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML)get_store().add_element_user(CFECONFIG$0);
            }
            target.set(cfeConfig);
        }
    }
    
    /**
     * Appends and returns a new empty "CFEConfig" element
     */
    public org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML addNewCFEConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML target = null;
            target = (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML)get_store().add_element_user(CFECONFIG$0);
            return target;
        }
    }
}
