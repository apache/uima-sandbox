/*
 * XML Type:  filterOperator
 * Namespace: http://uima.apache.org/simpleserver/config/xml
 * Java type: org.apache.uima.simpleserver.config.xml.FilterOperator
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.simpleserver.config.xml;


/**
 * An XML filterOperator(@http://uima.apache.org/simpleserver/config/xml).
 *
 * This is an atomic type that is a restriction of org.apache.uima.simpleserver.config.xml.FilterOperator.
 */
public interface FilterOperator extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FilterOperator.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sD8360DFF7E1263139EC462A6BFD0672D").resolveHandle("filteroperator18c8type");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum NULL = Enum.forString("null");
    static final Enum NULL_2 = Enum.forString("!null");
    static final Enum X = Enum.forString("=");
    static final Enum X_2 = Enum.forString("!=");
    static final Enum X_3 = Enum.forString(">");
    static final Enum X_4 = Enum.forString("<");
    static final Enum X_5 = Enum.forString(">=");
    static final Enum X_6 = Enum.forString("<=");
    
    static final int INT_NULL = Enum.INT_NULL;
    static final int INT_NULL_2 = Enum.INT_NULL_2;
    static final int INT_X = Enum.INT_X;
    static final int INT_X_2 = Enum.INT_X_2;
    static final int INT_X_3 = Enum.INT_X_3;
    static final int INT_X_4 = Enum.INT_X_4;
    static final int INT_X_5 = Enum.INT_X_5;
    static final int INT_X_6 = Enum.INT_X_6;
    
    /**
     * Enumeration value class for org.apache.uima.simpleserver.config.xml.FilterOperator.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NULL
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
    {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s)
            { return (Enum)table.forString(s); }
        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i)
            { return (Enum)table.forInt(i); }
        
        private Enum(java.lang.String s, int i)
            { super(s, i); }
        
        static final int INT_NULL = 1;
        static final int INT_NULL_2 = 2;
        static final int INT_X = 3;
        static final int INT_X_2 = 4;
        static final int INT_X_3 = 5;
        static final int INT_X_4 = 6;
        static final int INT_X_5 = 7;
        static final int INT_X_6 = 8;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("null", INT_NULL),
                new Enum("!null", INT_NULL_2),
                new Enum("=", INT_X),
                new Enum("!=", INT_X_2),
                new Enum(">", INT_X_3),
                new Enum("<", INT_X_4),
                new Enum(">=", INT_X_5),
                new Enum("<=", INT_X_6),
            }
        );
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() { return forInt(intValue()); } 
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.simpleserver.config.xml.FilterOperator newValue(java.lang.Object obj) {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) type.newValue( obj ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator newInstance() {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.simpleserver.config.xml.FilterOperator parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.simpleserver.config.xml.FilterOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
