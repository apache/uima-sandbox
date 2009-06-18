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
