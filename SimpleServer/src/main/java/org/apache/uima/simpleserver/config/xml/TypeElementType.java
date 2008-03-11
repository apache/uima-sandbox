/*
 * XML Type:  TypeElementType
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.TypeElementType
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * An XML TypeElementType(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is a complex type.
 */
public interface TypeElementType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TypeElementType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("typeelementtype4adatype");
    
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
     * Gets the "filters" element
     */
    org.apache.uima.simpleserver.config.xml.TypeElementType.Filters getFilters();
    
    /**
     * True if has "filters" element
     */
    boolean isSetFilters();
    
    /**
     * Sets the "filters" element
     */
    void setFilters(org.apache.uima.simpleserver.config.xml.TypeElementType.Filters filters);
    
    /**
     * Appends and returns a new empty "filters" element
     */
    org.apache.uima.simpleserver.config.xml.TypeElementType.Filters addNewFilters();
    
    /**
     * Unsets the "filters" element
     */
    void unsetFilters();
    
    /**
     * Gets the "outputs" element
     */
    org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs getOutputs();
    
    /**
     * True if has "outputs" element
     */
    boolean isSetOutputs();
    
    /**
     * Sets the "outputs" element
     */
    void setOutputs(org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs outputs);
    
    /**
     * Appends and returns a new empty "outputs" element
     */
    org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs addNewOutputs();
    
    /**
     * Unsets the "outputs" element
     */
    void unsetOutputs();
    
    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets the "outputTag" attribute
     */
    java.lang.String getOutputTag();
    
    /**
     * Gets (as xml) the "outputTag" attribute
     */
    org.apache.xmlbeans.XmlString xgetOutputTag();
    
    /**
     * Sets the "outputTag" attribute
     */
    void setOutputTag(java.lang.String outputTag);
    
    /**
     * Sets (as xml) the "outputTag" attribute
     */
    void xsetOutputTag(org.apache.xmlbeans.XmlString outputTag);
    
    /**
     * Gets the "outputCoveredText" attribute
     */
    boolean getOutputCoveredText();
    
    /**
     * Gets (as xml) the "outputCoveredText" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetOutputCoveredText();
    
    /**
     * True if has "outputCoveredText" attribute
     */
    boolean isSetOutputCoveredText();
    
    /**
     * Sets the "outputCoveredText" attribute
     */
    void setOutputCoveredText(boolean outputCoveredText);
    
    /**
     * Sets (as xml) the "outputCoveredText" attribute
     */
    void xsetOutputCoveredText(org.apache.xmlbeans.XmlBoolean outputCoveredText);
    
    /**
     * Unsets the "outputCoveredText" attribute
     */
    void unsetOutputCoveredText();
    
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
     * An XML filters(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public interface Filters extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Filters.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("filterscf53elemtype");
        
        /**
         * Gets the "filter" element
         */
        org.apache.uima.simpleserver.config.xml.SimpleFilterType getFilter();
        
        /**
         * True if has "filter" element
         */
        boolean isSetFilter();
        
        /**
         * Sets the "filter" element
         */
        void setFilter(org.apache.uima.simpleserver.config.xml.SimpleFilterType filter);
        
        /**
         * Appends and returns a new empty "filter" element
         */
        org.apache.uima.simpleserver.config.xml.SimpleFilterType addNewFilter();
        
        /**
         * Unsets the "filter" element
         */
        void unsetFilter();
        
        /**
         * Gets the "or" element
         */
        org.apache.uima.simpleserver.config.xml.Or getOr();
        
        /**
         * True if has "or" element
         */
        boolean isSetOr();
        
        /**
         * Sets the "or" element
         */
        void setOr(org.apache.uima.simpleserver.config.xml.Or or);
        
        /**
         * Appends and returns a new empty "or" element
         */
        org.apache.uima.simpleserver.config.xml.Or addNewOr();
        
        /**
         * Unsets the "or" element
         */
        void unsetOr();
        
        /**
         * Gets the "and" element
         */
        org.apache.uima.simpleserver.config.xml.And getAnd();
        
        /**
         * True if has "and" element
         */
        boolean isSetAnd();
        
        /**
         * Sets the "and" element
         */
        void setAnd(org.apache.uima.simpleserver.config.xml.And and);
        
        /**
         * Appends and returns a new empty "and" element
         */
        org.apache.uima.simpleserver.config.xml.And addNewAnd();
        
        /**
         * Unsets the "and" element
         */
        void unsetAnd();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.apache.uima.simpleserver.config.xml.TypeElementType.Filters newInstance() {
              return (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.apache.uima.simpleserver.config.xml.TypeElementType.Filters newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.apache.uima.simpleserver.config.xml.TypeElementType.Filters) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML outputs(@http://uima.apache.org/simpleserver/config/xml).
     *
     * This is a complex type.
     */
    public interface Outputs extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Outputs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("outputs169celemtype");
        
        /**
         * Gets a List of "output" elements
         */
        java.util.List<org.apache.uima.simpleserver.config.xml.OutputType> getOutputList();
        
        /**
         * Gets array of all "output" elements
         * @deprecated
         */
        org.apache.uima.simpleserver.config.xml.OutputType[] getOutputArray();
        
        /**
         * Gets ith "output" element
         */
        org.apache.uima.simpleserver.config.xml.OutputType getOutputArray(int i);
        
        /**
         * Returns number of "output" element
         */
        int sizeOfOutputArray();
        
        /**
         * Sets array of all "output" element
         */
        void setOutputArray(org.apache.uima.simpleserver.config.xml.OutputType[] outputArray);
        
        /**
         * Sets ith "output" element
         */
        void setOutputArray(int i, org.apache.uima.simpleserver.config.xml.OutputType output);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "output" element
         */
        org.apache.uima.simpleserver.config.xml.OutputType insertNewOutput(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "output" element
         */
        org.apache.uima.simpleserver.config.xml.OutputType addNewOutput();
        
        /**
         * Removes the ith "output" element
         */
        void removeOutput(int i);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs newInstance() {
              return (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.apache.uima.simpleserver.config.xml.TypeElementType.Outputs) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.TypeElementType newInstance() {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.TypeElementType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.TypeElementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
