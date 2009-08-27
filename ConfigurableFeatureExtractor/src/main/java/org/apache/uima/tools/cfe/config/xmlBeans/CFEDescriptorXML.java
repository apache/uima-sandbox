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
 * XML Type:  CFEDescriptorXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.CFEDescriptorXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML CFEDescriptorXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface CFEDescriptorXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CFEDescriptorXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("cfedescriptorxmle158type");
    
    /**
     * Gets array of all "targetAnnotations" elements
     */
    org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[] getTargetAnnotationsArray();
    
    /**
     * Gets ith "targetAnnotations" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML getTargetAnnotationsArray(int i);
    
    /**
     * Returns number of "targetAnnotations" element
     */
    int sizeOfTargetAnnotationsArray();
    
    /**
     * Sets array of all "targetAnnotations" element
     */
    void setTargetAnnotationsArray(org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML[] targetAnnotationsArray);
    
    /**
     * Sets ith "targetAnnotations" element
     */
    void setTargetAnnotationsArray(int i, org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML targetAnnotations);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "targetAnnotations" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML insertNewTargetAnnotations(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "targetAnnotations" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.TargetAnnotationXML addNewTargetAnnotations();
    
    /**
     * Removes the ith "targetAnnotations" element
     */
    void removeTargetAnnotations(int i);
    
    /**
     * Gets the "nullValueImage" attribute
     */
    java.lang.String getNullValueImage();
    
    /**
     * Gets (as xml) the "nullValueImage" attribute
     */
    org.apache.xmlbeans.XmlString xgetNullValueImage();
    
    /**
     * True if has "nullValueImage" attribute
     */
    boolean isSetNullValueImage();
    
    /**
     * Sets the "nullValueImage" attribute
     */
    void setNullValueImage(java.lang.String nullValueImage);
    
    /**
     * Sets (as xml) the "nullValueImage" attribute
     */
    void xsetNullValueImage(org.apache.xmlbeans.XmlString nullValueImage);
    
    /**
     * Unsets the "nullValueImage" attribute
     */
    void unsetNullValueImage();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.CFEDescriptorXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
