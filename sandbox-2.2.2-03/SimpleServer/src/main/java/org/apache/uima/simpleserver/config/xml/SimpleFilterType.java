/*
 * XML Type:  simpleFilterType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.SimpleFilterType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * An XML simpleFilterType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public interface SimpleFilterType extends org.apache.uima.simpleserver.config.xml.FilterType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SimpleFilterType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("simplefiltertypef020type");
    
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
     * Gets the "operator" attribute
     */
    org.apache.uima.simpleserver.config.xml.FilterOperator.Enum getOperator();
    
    /**
     * Gets (as xml) the "operator" attribute
     */
    org.apache.uima.simpleserver.config.xml.FilterOperator xgetOperator();
    
    /**
     * Sets the "operator" attribute
     */
    void setOperator(org.apache.uima.simpleserver.config.xml.FilterOperator.Enum operator);
    
    /**
     * Sets (as xml) the "operator" attribute
     */
    void xsetOperator(org.apache.uima.simpleserver.config.xml.FilterOperator operator);
    
    /**
     * Gets the "value" attribute
     */
    java.lang.String getValue();
    
    /**
     * Gets (as xml) the "value" attribute
     */
    org.apache.xmlbeans.XmlString xgetValue();
    
    /**
     * True if has "value" attribute
     */
    boolean isSetValue();
    
    /**
     * Sets the "value" attribute
     */
    void setValue(java.lang.String value);
    
    /**
     * Sets (as xml) the "value" attribute
     */
    void xsetValue(org.apache.xmlbeans.XmlString value);
    
    /**
     * Unsets the "value" attribute
     */
    void unsetValue();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType newInstance() {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.SimpleFilterType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.SimpleFilterType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
