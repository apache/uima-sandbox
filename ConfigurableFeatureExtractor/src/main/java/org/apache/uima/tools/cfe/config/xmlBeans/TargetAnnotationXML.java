/*
 * XML Type:  TargetAnnotationXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.TargetAnnotationXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML TargetAnnotationXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface TargetAnnotationXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TargetAnnotationXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("targetannotationxmlf9b9type");
    
    /**
     * Gets the "targetAnnotationMatcher" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML getTargetAnnotationMatcher();
    
    /**
     * Sets the "targetAnnotationMatcher" element
     */
    void setTargetAnnotationMatcher(org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML targetAnnotationMatcher);
    
    /**
     * Appends and returns a new empty "targetAnnotationMatcher" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML addNewTargetAnnotationMatcher();
    
    /**
     * Gets array of all "featureAnnotationMatchers" elements
     */
    org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[] getFeatureAnnotationMatchersArray();
    
    /**
     * Gets ith "featureAnnotationMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML getFeatureAnnotationMatchersArray(int i);
    
    /**
     * Returns number of "featureAnnotationMatchers" element
     */
    int sizeOfFeatureAnnotationMatchersArray();
    
    /**
     * Sets array of all "featureAnnotationMatchers" element
     */
    void setFeatureAnnotationMatchersArray(org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML[] featureAnnotationMatchersArray);
    
    /**
     * Sets ith "featureAnnotationMatchers" element
     */
    void setFeatureAnnotationMatchersArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML featureAnnotationMatchers);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "featureAnnotationMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML insertNewFeatureAnnotationMatchers(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "featureAnnotationMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML addNewFeatureAnnotationMatchers();
    
    /**
     * Removes the ith "featureAnnotationMatchers" element
     */
    void removeFeatureAnnotationMatchers(int i);
    
    /**
     * Gets the "className" attribute
     */
    java.lang.String getClassName();
    
    /**
     * Gets (as xml) the "className" attribute
     */
    org.apache.xmlbeans.XmlString xgetClassName();
    
    /**
     * Sets the "className" attribute
     */
    void setClassName(java.lang.String className);
    
    /**
     * Sets (as xml) the "className" attribute
     */
    void xsetClassName(org.apache.xmlbeans.XmlString className);
    
    /**
     * Gets the "enclosingAnnotation" attribute
     */
    java.lang.String getEnclosingAnnotation();
    
    /**
     * Gets (as xml) the "enclosingAnnotation" attribute
     */
    org.apache.xmlbeans.XmlString xgetEnclosingAnnotation();
    
    /**
     * Sets the "enclosingAnnotation" attribute
     */
    void setEnclosingAnnotation(java.lang.String enclosingAnnotation);
    
    /**
     * Sets (as xml) the "enclosingAnnotation" attribute
     */
    void xsetEnclosingAnnotation(org.apache.xmlbeans.XmlString enclosingAnnotation);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
