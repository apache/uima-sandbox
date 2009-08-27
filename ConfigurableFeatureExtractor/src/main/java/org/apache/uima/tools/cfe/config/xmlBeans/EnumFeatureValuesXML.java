/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * XML Type:  EnumFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.EnumFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML EnumFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface EnumFeatureValuesXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(EnumFeatureValuesXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("enumfeaturevaluesxmlbf1etype");
    
    /**
     * Gets array of all "values" elements
     */
    java.lang.String[] getValuesArray();
    
    /**
     * Gets ith "values" element
     */
    java.lang.String getValuesArray(int i);
    
    /**
     * Gets (as xml) array of all "values" elements
     */
    org.apache.xmlbeans.XmlString[] xgetValuesArray();
    
    /**
     * Gets (as xml) ith "values" element
     */
    org.apache.xmlbeans.XmlString xgetValuesArray(int i);
    
    /**
     * Returns number of "values" element
     */
    int sizeOfValuesArray();
    
    /**
     * Sets array of all "values" element
     */
    void setValuesArray(java.lang.String[] valuesArray);
    
    /**
     * Sets ith "values" element
     */
    void setValuesArray(int i, java.lang.String values);
    
    /**
     * Sets (as xml) array of all "values" element
     */
    void xsetValuesArray(org.apache.xmlbeans.XmlString[] valuesArray);
    
    /**
     * Sets (as xml) ith "values" element
     */
    void xsetValuesArray(int i, org.apache.xmlbeans.XmlString values);
    
    /**
     * Inserts the value as the ith "values" element
     */
    void insertValues(int i, java.lang.String values);
    
    /**
     * Appends the value as the last "values" element
     */
    void addValues(java.lang.String values);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "values" element
     */
    org.apache.xmlbeans.XmlString insertNewValues(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "values" element
     */
    org.apache.xmlbeans.XmlString addNewValues();
    
    /**
     * Removes the ith "values" element
     */
    void removeValues(int i);
    
    /**
     * Gets the "caseSensitive" attribute
     */
    boolean getCaseSensitive();
    
    /**
     * Gets (as xml) the "caseSensitive" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetCaseSensitive();
    
    /**
     * True if has "caseSensitive" attribute
     */
    boolean isSetCaseSensitive();
    
    /**
     * Sets the "caseSensitive" attribute
     */
    void setCaseSensitive(boolean caseSensitive);
    
    /**
     * Sets (as xml) the "caseSensitive" attribute
     */
    void xsetCaseSensitive(org.apache.xmlbeans.XmlBoolean caseSensitive);
    
    /**
     * Unsets the "caseSensitive" attribute
     */
    void unsetCaseSensitive();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
