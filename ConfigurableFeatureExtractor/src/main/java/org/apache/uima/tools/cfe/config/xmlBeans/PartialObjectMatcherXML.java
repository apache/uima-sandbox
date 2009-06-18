/*
 * XML Type:  PartialObjectMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.PartialObjectMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML PartialObjectMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface PartialObjectMatcherXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PartialObjectMatcherXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("partialobjectmatcherxml860btype");
    
    /**
     * Gets array of all "groupFeatureMatchers" elements
     */
    org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[] getGroupFeatureMatchersArray();
    
    /**
     * Gets ith "groupFeatureMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML getGroupFeatureMatchersArray(int i);
    
    /**
     * Returns number of "groupFeatureMatchers" element
     */
    int sizeOfGroupFeatureMatchersArray();
    
    /**
     * Sets array of all "groupFeatureMatchers" element
     */
    void setGroupFeatureMatchersArray(org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML[] groupFeatureMatchersArray);
    
    /**
     * Sets ith "groupFeatureMatchers" element
     */
    void setGroupFeatureMatchersArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML groupFeatureMatchers);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "groupFeatureMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML insertNewGroupFeatureMatchers(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "groupFeatureMatchers" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.GroupFeatureMatcherXML addNewGroupFeatureMatchers();
    
    /**
     * Removes the ith "groupFeatureMatchers" element
     */
    void removeGroupFeatureMatchers(int i);
    
    /**
     * Gets the "annotationTypeName" attribute
     */
    java.lang.String getAnnotationTypeName();
    
    /**
     * Gets (as xml) the "annotationTypeName" attribute
     */
    org.apache.xmlbeans.XmlString xgetAnnotationTypeName();
    
    /**
     * Sets the "annotationTypeName" attribute
     */
    void setAnnotationTypeName(java.lang.String annotationTypeName);
    
    /**
     * Sets (as xml) the "annotationTypeName" attribute
     */
    void xsetAnnotationTypeName(org.apache.xmlbeans.XmlString annotationTypeName);
    
    /**
     * Gets the "fullPath" attribute
     */
    java.lang.String getFullPath();
    
    /**
     * Gets (as xml) the "fullPath" attribute
     */
    org.apache.xmlbeans.XmlString xgetFullPath();
    
    /**
     * True if has "fullPath" attribute
     */
    boolean isSetFullPath();
    
    /**
     * Sets the "fullPath" attribute
     */
    void setFullPath(java.lang.String fullPath);
    
    /**
     * Sets (as xml) the "fullPath" attribute
     */
    void xsetFullPath(org.apache.xmlbeans.XmlString fullPath);
    
    /**
     * Unsets the "fullPath" attribute
     */
    void unsetFullPath();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
