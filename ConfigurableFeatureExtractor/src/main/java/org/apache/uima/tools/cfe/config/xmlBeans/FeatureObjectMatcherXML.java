/*
 * XML Type:  FeatureObjectMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.FeatureObjectMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML FeatureObjectMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface FeatureObjectMatcherXML extends org.apache.uima.tools.cfe.config.xmlBeans.PartialObjectMatcherXML
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FeatureObjectMatcherXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("featureobjectmatcherxml79f6type");
    
    /**
     * Gets the "windowsizeLeft" attribute
     */
    int getWindowsizeLeft();
    
    /**
     * Gets (as xml) the "windowsizeLeft" attribute
     */
    org.apache.xmlbeans.XmlInt xgetWindowsizeLeft();
    
    /**
     * True if has "windowsizeLeft" attribute
     */
    boolean isSetWindowsizeLeft();
    
    /**
     * Sets the "windowsizeLeft" attribute
     */
    void setWindowsizeLeft(int windowsizeLeft);
    
    /**
     * Sets (as xml) the "windowsizeLeft" attribute
     */
    void xsetWindowsizeLeft(org.apache.xmlbeans.XmlInt windowsizeLeft);
    
    /**
     * Unsets the "windowsizeLeft" attribute
     */
    void unsetWindowsizeLeft();
    
    /**
     * Gets the "windowsizeInside" attribute
     */
    int getWindowsizeInside();
    
    /**
     * Gets (as xml) the "windowsizeInside" attribute
     */
    org.apache.xmlbeans.XmlInt xgetWindowsizeInside();
    
    /**
     * True if has "windowsizeInside" attribute
     */
    boolean isSetWindowsizeInside();
    
    /**
     * Sets the "windowsizeInside" attribute
     */
    void setWindowsizeInside(int windowsizeInside);
    
    /**
     * Sets (as xml) the "windowsizeInside" attribute
     */
    void xsetWindowsizeInside(org.apache.xmlbeans.XmlInt windowsizeInside);
    
    /**
     * Unsets the "windowsizeInside" attribute
     */
    void unsetWindowsizeInside();
    
    /**
     * Gets the "windowsizeRight" attribute
     */
    int getWindowsizeRight();
    
    /**
     * Gets (as xml) the "windowsizeRight" attribute
     */
    org.apache.xmlbeans.XmlInt xgetWindowsizeRight();
    
    /**
     * True if has "windowsizeRight" attribute
     */
    boolean isSetWindowsizeRight();
    
    /**
     * Sets the "windowsizeRight" attribute
     */
    void setWindowsizeRight(int windowsizeRight);
    
    /**
     * Sets (as xml) the "windowsizeRight" attribute
     */
    void xsetWindowsizeRight(org.apache.xmlbeans.XmlInt windowsizeRight);
    
    /**
     * Unsets the "windowsizeRight" attribute
     */
    void unsetWindowsizeRight();
    
    /**
     * Gets the "windowsizeEnclosed" attribute
     */
    int getWindowsizeEnclosed();
    
    /**
     * Gets (as xml) the "windowsizeEnclosed" attribute
     */
    org.apache.xmlbeans.XmlInt xgetWindowsizeEnclosed();
    
    /**
     * True if has "windowsizeEnclosed" attribute
     */
    boolean isSetWindowsizeEnclosed();
    
    /**
     * Sets the "windowsizeEnclosed" attribute
     */
    void setWindowsizeEnclosed(int windowsizeEnclosed);
    
    /**
     * Sets (as xml) the "windowsizeEnclosed" attribute
     */
    void xsetWindowsizeEnclosed(org.apache.xmlbeans.XmlInt windowsizeEnclosed);
    
    /**
     * Unsets the "windowsizeEnclosed" attribute
     */
    void unsetWindowsizeEnclosed();
    
    /**
     * Gets the "windowFlags" attribute
     */
    int getWindowFlags();
    
    /**
     * Gets (as xml) the "windowFlags" attribute
     */
    org.apache.xmlbeans.XmlInt xgetWindowFlags();
    
    /**
     * True if has "windowFlags" attribute
     */
    boolean isSetWindowFlags();
    
    /**
     * Sets the "windowFlags" attribute
     */
    void setWindowFlags(int windowFlags);
    
    /**
     * Sets (as xml) the "windowFlags" attribute
     */
    void xsetWindowFlags(org.apache.xmlbeans.XmlInt windowFlags);
    
    /**
     * Unsets the "windowFlags" attribute
     */
    void unsetWindowFlags();
    
    /**
     * Gets the "orientation" attribute
     */
    boolean getOrientation();
    
    /**
     * Gets (as xml) the "orientation" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetOrientation();
    
    /**
     * True if has "orientation" attribute
     */
    boolean isSetOrientation();
    
    /**
     * Sets the "orientation" attribute
     */
    void setOrientation(boolean orientation);
    
    /**
     * Sets (as xml) the "orientation" attribute
     */
    void xsetOrientation(org.apache.xmlbeans.XmlBoolean orientation);
    
    /**
     * Unsets the "orientation" attribute
     */
    void unsetOrientation();
    
    /**
     * Gets the "distance" attribute
     */
    boolean getDistance();
    
    /**
     * Gets (as xml) the "distance" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDistance();
    
    /**
     * True if has "distance" attribute
     */
    boolean isSetDistance();
    
    /**
     * Sets the "distance" attribute
     */
    void setDistance(boolean distance);
    
    /**
     * Sets (as xml) the "distance" attribute
     */
    void xsetDistance(org.apache.xmlbeans.XmlBoolean distance);
    
    /**
     * Unsets the "distance" attribute
     */
    void unsetDistance();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.FeatureObjectMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
