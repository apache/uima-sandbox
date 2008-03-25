/*
 * XML Type:  outputType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.OutputType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * An XML outputType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public interface OutputType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OutputType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("outputtypea2c9type");
    
    /**
     * Gets the "shortDescription" element
     */
    java.lang.String getShortDescription();
    
    /**
     * Gets (as xml) the "shortDescription" element
     */
    org.apache.xmlbeans.XmlString xgetShortDescription();
    
    /**
     * True if has "shortDescription" element
     */
    boolean isSetShortDescription();
    
    /**
     * Sets the "shortDescription" element
     */
    void setShortDescription(java.lang.String shortDescription);
    
    /**
     * Sets (as xml) the "shortDescription" element
     */
    void xsetShortDescription(org.apache.xmlbeans.XmlString shortDescription);
    
    /**
     * Unsets the "shortDescription" element
     */
    void unsetShortDescription();
    
    /**
     * Gets the "longDescription" element
     */
    java.lang.String getLongDescription();
    
    /**
     * Gets (as xml) the "longDescription" element
     */
    org.apache.xmlbeans.XmlString xgetLongDescription();
    
    /**
     * True if has "longDescription" element
     */
    boolean isSetLongDescription();
    
    /**
     * Sets the "longDescription" element
     */
    void setLongDescription(java.lang.String longDescription);
    
    /**
     * Sets (as xml) the "longDescription" element
     */
    void xsetLongDescription(org.apache.xmlbeans.XmlString longDescription);
    
    /**
     * Unsets the "longDescription" element
     */
    void unsetLongDescription();
    
    /**
     * Gets the "featurePath" attribute
     */
    java.lang.String getFeaturePath();
    
    /**
     * Gets (as xml) the "featurePath" attribute
     */
    org.apache.xmlbeans.XmlString xgetFeaturePath();
    
    /**
     * Sets the "featurePath" attribute
     */
    void setFeaturePath(java.lang.String featurePath);
    
    /**
     * Sets (as xml) the "featurePath" attribute
     */
    void xsetFeaturePath(org.apache.xmlbeans.XmlString featurePath);
    
    /**
     * Gets the "outputAttribute" attribute
     */
    java.lang.String getOutputAttribute();
    
    /**
     * Gets (as xml) the "outputAttribute" attribute
     */
    org.apache.xmlbeans.XmlString xgetOutputAttribute();
    
    /**
     * Sets the "outputAttribute" attribute
     */
    void setOutputAttribute(java.lang.String outputAttribute);
    
    /**
     * Sets (as xml) the "outputAttribute" attribute
     */
    void xsetOutputAttribute(org.apache.xmlbeans.XmlString outputAttribute);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.OutputType newInstance() {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.OutputType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.OutputType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
