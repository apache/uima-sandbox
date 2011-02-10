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

package org.apache.uima.solrcas;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * SAX Reader for Solr mapping configuration
 */
public class FieldMappingReader {

  public SolrMappingConfiguration getConf(String path) throws Exception {

    InputStream input = this.getClass().getResourceAsStream(path);

    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

    FieldMappingHandler handler = new FieldMappingHandler();
    parser.parse(input, handler); //parse the stream

    Map<String, Map<String, String>> fieldMapping = handler.getFieldMapping();

    String documentText = handler.getDocumentText();

    String documentLanguage = handler.getDocumentLanguage();

    String cas = handler.getCas();

    input.close();

    SolrMappingConfiguration solrMappingConfiguration = new SolrMappingConfiguration(cas, documentText, documentLanguage, fieldMapping);

    return solrMappingConfiguration;
  }

  private static class FieldMappingHandler extends DefaultHandler {

    private Map<String, Map<String, String>> fieldMapping;
    private String type;
    private Map<String, String> mapping;
    private String documentText;
    private String documentLanguage;
    private String cas;

    private boolean inLang = false;
    private boolean inText = false;
    private boolean inCas = false;

    private static final String TYPE = "type";
    private static final String MAP = "map";
    private static final String TEXT = "documentText";
    private static final String LANGUAGE = "documentLanguage";
    private static final String CAS = "cas";


    public Map<String, Map<String, String>> getFieldMapping() {
      return fieldMapping;
    }

    public String getDocumentText() {
      return documentText;
    }

    public String getDocumentLanguage() {
      return documentLanguage;
    }

    public String getCas() {
      return cas;
    }

    @Override
    public void startDocument() throws SAXException {
      fieldMapping = new HashMap<String, Map<String, String>>();
    }

    @Override
    public void endDocument() throws SAXException {
//      super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
      if (TYPE.equals(name)) {
        type = attributes.getValue("name");
        mapping = new HashMap<String, String>();
      } else if (MAP.equals(name)) {
        String feature = attributes.getValue("feature");
        String field = attributes.getValue("field");
        mapping.put(feature, field);
      } else if (LANGUAGE.equals(name)) {
        inLang = true;
      } else if (TEXT.equals(name)) {
        inText = true;
      } else if (CAS.equals(name)) {
        inCas = true;
      }
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
      if (inText) {
        documentText = String.valueOf(chars, start, length);
      } else if (inLang) {
        documentLanguage = String.valueOf(chars, start, length);
      } else if (inCas) {
        cas = String.valueOf(chars, start, length);
      }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
      if (MAP.equals(name)) {
      } else if (TYPE.equals(name)) {
        fieldMapping.put(type, mapping);
      } else if (LANGUAGE.equals(name)) {
        inLang = false;
      } else if (TEXT.equals(name)) {
        inText = false;
      } else if (CAS.equals(name)) {
        inCas = false;
      }
    }


  }
}
