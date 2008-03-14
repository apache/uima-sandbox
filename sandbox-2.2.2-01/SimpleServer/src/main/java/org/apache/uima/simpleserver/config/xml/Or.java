/*
 * XML Type:  or
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.Or
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * An XML or(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public interface Or extends org.apache.uima.simpleserver.config.xml.FilterType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Or.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("orbec1type");
    
    /**
     * Gets a List of "filter" elements
     */
    java.util.List<org.apache.uima.simpleserver.config.xml.SimpleFilterType> getFilterList();
    
    /**
     * Gets array of all "filter" elements
     * @deprecated
     */
    org.apache.uima.simpleserver.config.xml.SimpleFilterType[] getFilterArray();
    
    /**
     * Gets ith "filter" element
     */
    org.apache.uima.simpleserver.config.xml.SimpleFilterType getFilterArray(int i);
    
    /**
     * Returns number of "filter" element
     */
    int sizeOfFilterArray();
    
    /**
     * Sets array of all "filter" element
     */
    void setFilterArray(org.apache.uima.simpleserver.config.xml.SimpleFilterType[] filterArray);
    
    /**
     * Sets ith "filter" element
     */
    void setFilterArray(int i, org.apache.uima.simpleserver.config.xml.SimpleFilterType filter);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "filter" element
     */
    org.apache.uima.simpleserver.config.xml.SimpleFilterType insertNewFilter(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "filter" element
     */
    org.apache.uima.simpleserver.config.xml.SimpleFilterType addNewFilter();
    
    /**
     * Removes the ith "filter" element
     */
    void removeFilter(int i);
    
    /**
     * Gets a List of "or" elements
     */
    java.util.List<org.apache.uima.simpleserver.config.xml.Or> getOrList();
    
    /**
     * Gets array of all "or" elements
     * @deprecated
     */
    org.apache.uima.simpleserver.config.xml.Or[] getOrArray();
    
    /**
     * Gets ith "or" element
     */
    org.apache.uima.simpleserver.config.xml.Or getOrArray(int i);
    
    /**
     * Returns number of "or" element
     */
    int sizeOfOrArray();
    
    /**
     * Sets array of all "or" element
     */
    void setOrArray(org.apache.uima.simpleserver.config.xml.Or[] orArray);
    
    /**
     * Sets ith "or" element
     */
    void setOrArray(int i, org.apache.uima.simpleserver.config.xml.Or or);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "or" element
     */
    org.apache.uima.simpleserver.config.xml.Or insertNewOr(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "or" element
     */
    org.apache.uima.simpleserver.config.xml.Or addNewOr();
    
    /**
     * Removes the ith "or" element
     */
    void removeOr(int i);
    
    /**
     * Gets a List of "and" elements
     */
    java.util.List<org.apache.uima.simpleserver.config.xml.And> getAndList();
    
    /**
     * Gets array of all "and" elements
     * @deprecated
     */
    org.apache.uima.simpleserver.config.xml.And[] getAndArray();
    
    /**
     * Gets ith "and" element
     */
    org.apache.uima.simpleserver.config.xml.And getAndArray(int i);
    
    /**
     * Returns number of "and" element
     */
    int sizeOfAndArray();
    
    /**
     * Sets array of all "and" element
     */
    void setAndArray(org.apache.uima.simpleserver.config.xml.And[] andArray);
    
    /**
     * Sets ith "and" element
     */
    void setAndArray(int i, org.apache.uima.simpleserver.config.xml.And and);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "and" element
     */
    org.apache.uima.simpleserver.config.xml.And insertNewAnd(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "and" element
     */
    org.apache.uima.simpleserver.config.xml.And addNewAnd();
    
    /**
     * Removes the ith "and" element
     */
    void removeAnd(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.Or newInstance() {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.Or parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.Or parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.Or parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.Or) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
