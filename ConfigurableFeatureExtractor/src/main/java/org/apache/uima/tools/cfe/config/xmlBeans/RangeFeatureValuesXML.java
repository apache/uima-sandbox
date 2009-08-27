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
 * XML Type:  RangeFeatureValuesXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.RangeFeatureValuesXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML RangeFeatureValuesXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface RangeFeatureValuesXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(RangeFeatureValuesXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("rangefeaturevaluesxml1c94type");
    
    /**
     * Gets the "lowerBoundaryInclusive" attribute
     */
    boolean getLowerBoundaryInclusive();
    
    /**
     * Gets (as xml) the "lowerBoundaryInclusive" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLowerBoundaryInclusive();
    
    /**
     * True if has "lowerBoundaryInclusive" attribute
     */
    boolean isSetLowerBoundaryInclusive();
    
    /**
     * Sets the "lowerBoundaryInclusive" attribute
     */
    void setLowerBoundaryInclusive(boolean lowerBoundaryInclusive);
    
    /**
     * Sets (as xml) the "lowerBoundaryInclusive" attribute
     */
    void xsetLowerBoundaryInclusive(org.apache.xmlbeans.XmlBoolean lowerBoundaryInclusive);
    
    /**
     * Unsets the "lowerBoundaryInclusive" attribute
     */
    void unsetLowerBoundaryInclusive();
    
    /**
     * Gets the "upperBoundaryInclusive" attribute
     */
    boolean getUpperBoundaryInclusive();
    
    /**
     * Gets (as xml) the "upperBoundaryInclusive" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUpperBoundaryInclusive();
    
    /**
     * True if has "upperBoundaryInclusive" attribute
     */
    boolean isSetUpperBoundaryInclusive();
    
    /**
     * Sets the "upperBoundaryInclusive" attribute
     */
    void setUpperBoundaryInclusive(boolean upperBoundaryInclusive);
    
    /**
     * Sets (as xml) the "upperBoundaryInclusive" attribute
     */
    void xsetUpperBoundaryInclusive(org.apache.xmlbeans.XmlBoolean upperBoundaryInclusive);
    
    /**
     * Unsets the "upperBoundaryInclusive" attribute
     */
    void unsetUpperBoundaryInclusive();
    
    /**
     * Gets the "lowerBoundary" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getLowerBoundary();
    
    /**
     * True if has "lowerBoundary" attribute
     */
    boolean isSetLowerBoundary();
    
    /**
     * Sets the "lowerBoundary" attribute
     */
    void setLowerBoundary(org.apache.xmlbeans.XmlAnySimpleType lowerBoundary);
    
    /**
     * Appends and returns a new empty "lowerBoundary" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewLowerBoundary();
    
    /**
     * Unsets the "lowerBoundary" attribute
     */
    void unsetLowerBoundary();
    
    /**
     * Gets the "upperBoundary" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getUpperBoundary();
    
    /**
     * True if has "upperBoundary" attribute
     */
    boolean isSetUpperBoundary();
    
    /**
     * Sets the "upperBoundary" attribute
     */
    void setUpperBoundary(org.apache.xmlbeans.XmlAnySimpleType upperBoundary);
    
    /**
     * Appends and returns a new empty "upperBoundary" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewUpperBoundary();
    
    /**
     * Unsets the "upperBoundary" attribute
     */
    void unsetUpperBoundary();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
