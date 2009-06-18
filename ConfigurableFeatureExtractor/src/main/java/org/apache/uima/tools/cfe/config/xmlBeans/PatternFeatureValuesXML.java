/*
 * XML Type:  PatternFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.PatternFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML PatternFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface PatternFeatureValuesXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PatternFeatureValuesXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("patternfeaturevaluesxml0da1type");
    
    /**
     * Gets the "pattern" attribute
     */
    java.lang.String getPattern();
    
    /**
     * Gets (as xml) the "pattern" attribute
     */
    org.apache.xmlbeans.XmlString xgetPattern();
    
    /**
     * True if has "pattern" attribute
     */
    boolean isSetPattern();
    
    /**
     * Sets the "pattern" attribute
     */
    void setPattern(java.lang.String pattern);
    
    /**
     * Sets (as xml) the "pattern" attribute
     */
    void xsetPattern(org.apache.xmlbeans.XmlString pattern);
    
    /**
     * Unsets the "pattern" attribute
     */
    void unsetPattern();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
