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
 * XML Type:  SingleFeatureMatcherXML
 * Namespace: http://www.apache.org/uima/cfe/config/XMLBeans
 * Java type: org.apache.uima.cfe.config.xmlBeans.SingleFeatureMatcherXML
 *
 * Automatically generated - do not modify.
 */
package org.apache.uima.tools.cfe.config.xmlBeans;


/**
 * An XML SingleFeatureMatcherXML(@http://www.apache.org/uima/cfe/config/XMLBeans).
 *
 * This is a complex type.
 */
public interface SingleFeatureMatcherXML extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SingleFeatureMatcherXML.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s6153DD1BF87A9430F8A6FA57AFB2FDD7").resolveHandle("singlefeaturematcherxml5c5dtype");
    
    /**
     * Gets the "rangeFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML getRangeFeatureValues();
    
    /**
     * True if has "rangeFeatureValues" element
     */
    boolean isSetRangeFeatureValues();
    
    /**
     * Sets the "rangeFeatureValues" element
     */
    void setRangeFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML rangeFeatureValues);
    
    /**
     * Appends and returns a new empty "rangeFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.RangeFeatureValuesXML addNewRangeFeatureValues();
    
    /**
     * Unsets the "rangeFeatureValues" element
     */
    void unsetRangeFeatureValues();
    
    /**
     * Gets the "enumFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML getEnumFeatureValues();
    
    /**
     * True if has "enumFeatureValues" element
     */
    boolean isSetEnumFeatureValues();
    
    /**
     * Sets the "enumFeatureValues" element
     */
    void setEnumFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML enumFeatureValues);
    
    /**
     * Appends and returns a new empty "enumFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.EnumFeatureValuesXML addNewEnumFeatureValues();
    
    /**
     * Unsets the "enumFeatureValues" element
     */
    void unsetEnumFeatureValues();
    
    /**
     * Gets the "bitsetFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML getBitsetFeatureValues();
    
    /**
     * True if has "bitsetFeatureValues" element
     */
    boolean isSetBitsetFeatureValues();
    
    /**
     * Sets the "bitsetFeatureValues" element
     */
    void setBitsetFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML bitsetFeatureValues);
    
    /**
     * Appends and returns a new empty "bitsetFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.BitsetFeatureValuesXML addNewBitsetFeatureValues();
    
    /**
     * Unsets the "bitsetFeatureValues" element
     */
    void unsetBitsetFeatureValues();
    
    /**
     * Gets the "objectPathFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML getObjectPathFeatureValues();
    
    /**
     * True if has "objectPathFeatureValues" element
     */
    boolean isSetObjectPathFeatureValues();
    
    /**
     * Sets the "objectPathFeatureValues" element
     */
    void setObjectPathFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML objectPathFeatureValues);
    
    /**
     * Appends and returns a new empty "objectPathFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.ObjectPathFeatureValuesXML addNewObjectPathFeatureValues();
    
    /**
     * Unsets the "objectPathFeatureValues" element
     */
    void unsetObjectPathFeatureValues();
    
    /**
     * Gets the "patternFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML getPatternFeatureValues();
    
    /**
     * True if has "patternFeatureValues" element
     */
    boolean isSetPatternFeatureValues();
    
    /**
     * Sets the "patternFeatureValues" element
     */
    void setPatternFeatureValues(org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML patternFeatureValues);
    
    /**
     * Appends and returns a new empty "patternFeatureValues" element
     */
    org.apache.uima.tools.cfe.config.xmlBeans.PatternFeatureValuesXML addNewPatternFeatureValues();
    
    /**
     * Unsets the "patternFeatureValues" element
     */
    void unsetPatternFeatureValues();
    
    /**
     * Gets the "featureTypeName" attribute
     */
    java.lang.String getFeatureTypeName();
    
    /**
     * Gets (as xml) the "featureTypeName" attribute
     */
    org.apache.xmlbeans.XmlString xgetFeatureTypeName();
    
    /**
     * Sets the "featureTypeName" attribute
     */
    void setFeatureTypeName(java.lang.String featureTypeName);
    
    /**
     * Sets (as xml) the "featureTypeName" attribute
     */
    void xsetFeatureTypeName(org.apache.xmlbeans.XmlString featureTypeName);
    
    /**
     * Gets the "featurePath" attribute
     */
    java.lang.String getFeaturePath();
    
    /**
     * Gets (as xml) the "featurePath" attribute
     */
    org.apache.xmlbeans.XmlString xgetFeaturePath();
    
    /**
     * Sets the "featurePath" attribute
     */
    void setFeaturePath(java.lang.String featurePath);
    
    /**
     * Sets (as xml) the "featurePath" attribute
     */
    void xsetFeaturePath(org.apache.xmlbeans.XmlString featurePath);
    
    /**
     * Gets the "exclude" attribute
     */
    boolean getExclude();
    
    /**
     * Gets (as xml) the "exclude" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetExclude();
    
    /**
     * True if has "exclude" attribute
     */
    boolean isSetExclude();
    
    /**
     * Sets the "exclude" attribute
     */
    void setExclude(boolean exclude);
    
    /**
     * Sets (as xml) the "exclude" attribute
     */
    void xsetExclude(org.apache.xmlbeans.XmlBoolean exclude);
    
    /**
     * Unsets the "exclude" attribute
     */
    void unsetExclude();
    
    /**
     * Gets the "quiet" attribute
     */
    boolean getQuiet();
    
    /**
     * Gets (as xml) the "quiet" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetQuiet();
    
    /**
     * True if has "quiet" attribute
     */
    boolean isSetQuiet();
    
    /**
     * Sets the "quiet" attribute
     */
    void setQuiet(boolean quiet);
    
    /**
     * Sets (as xml) the "quiet" attribute
     */
    void xsetQuiet(org.apache.xmlbeans.XmlBoolean quiet);
    
    /**
     * Unsets the "quiet" attribute
     */
    void unsetQuiet();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML newInstance() {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.apache.uima.tools.cfe.config.xmlBeans.SingleFeatureMatcherXML) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
