/*
 * An XML document type.
 * Localname: uimaSimpleServerSpec
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * A document containing one uimaSimpleServerSpec(@http://uima.apache.org/simpleserver/config/xml) element.
 *
 * This is a complex type.
 */
public interface UimaSimpleServerSpecDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UimaSimpleServerSpecDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("uimasimpleserverspec9ffcdoctype");
    
    /**
     * Gets the "uimaSimpleServerSpec" element
     */
    org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec getUimaSimpleServerSpec();
    
    /**
     * Sets the "uimaSimpleServerSpec" element
     */
    void setUimaSimpleServerSpec(org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec uimaSimpleServerSpec);
    
    /**
     * Appends and returns a new empty "uimaSimpleServerSpec" element
     */
    org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec addNewUimaSimpleServerSpec();
    
    /**
     * An XML uimaSimpleServerSpec(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public interface UimaSimpleServerSpec extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UimaSimpleServerSpec.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("uimasimpleserverspecf1d0elemtype");
        
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
         * Gets a List of "type" elements
         */
        java.util.List<org.apache.uima.simpleserver.config.xml.TypeElementType> getTypeList();
        
        /**
         * Gets array of all "type" elements
         * @deprecated
         */
        org.apache.uima.simpleserver.config.xml.TypeElementType[] getTypeArray();
        
        /**
         * Gets ith "type" element
         */
        org.apache.uima.simpleserver.config.xml.TypeElementType getTypeArray(int i);
        
        /**
         * Returns number of "type" element
         */
        int sizeOfTypeArray();
        
        /**
         * Sets array of all "type" element
         */
        void setTypeArray(org.apache.uima.simpleserver.config.xml.TypeElementType[] typeArray);
        
        /**
         * Sets ith "type" element
         */
        void setTypeArray(int i, org.apache.uima.simpleserver.config.xml.TypeElementType type);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "type" element
         */
        org.apache.uima.simpleserver.config.xml.TypeElementType insertNewType(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "type" element
         */
        org.apache.uima.simpleserver.config.xml.TypeElementType addNewType();
        
        /**
         * Removes the ith "type" element
         */
        void removeType(int i);
        
        /**
         * Gets the "outputAll" attribute
         */
        boolean getOutputAll();
        
        /**
         * Gets (as xml) the "outputAll" attribute
         */
        org.apache.xmlbeans.XmlBoolean xgetOutputAll();
        
        /**
         * True if has "outputAll" attribute
         */
        boolean isSetOutputAll();
        
        /**
         * Sets the "outputAll" attribute
         */
        void setOutputAll(boolean outputAll);
        
        /**
         * Sets (as xml) the "outputAll" attribute
         */
        void xsetOutputAll(org.apache.xmlbeans.XmlBoolean outputAll);
        
        /**
         * Unsets the "outputAll" attribute
         */
        void unsetOutputAll();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec newInstance() {
              return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument newInstance() {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
