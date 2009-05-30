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

package org.apache.uima.lucas.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * Build Object out of the Index Mapping File (XML)
 */
public class IndexMappingFileReader {

  // Variables for XML-elements
  private final String ROOT = "fields";

  private final String FIELD = "field"; // the tag name for field definitions

  private final String FIELD_NAME = "name"; // defines the name of the field the data has to be

  // stored

  private final String FIELD_INDEX = "index"; // defines if the field should be tokenized or not

  private final String FIELD_TERM_VECTOR = "termVector";

  private final String FIELD_STORED = "stored"; // yes if the field should be stored (returnable);

  // no: false

  private final String FIELD_DELIMITER = "delimiter"; // use special characters to limit the tokens

  // and build one string

  private final String FIELD_MERGE = "merge"; // use the tokenstream merger

  private final String ANNOTATION = "annotation"; // the tag name for annotation definitions

  private final String ANNOTATION_TYPE = "type"; // defines the CAS annotation type to be indexed

  private final String ANNOTATION_CONCAT_STRING = "concatString"; // use special characters to

  // concatenate the feature values

  private final String ANNOTATION_SPLIT_STRING = "splitString"; // use special characters to split

  // the feature values

  private final String ANNOTATION_LOWERCASE = "lowercase"; // true if the feature values have to be

  // converted to lower case; else: false

  private final String ANNOTATION_UPPERCASE = "uppercase"; // true if the feature values have to be

  // converted to upper case; else: false

  private final String ANNOTATION_STOPWORD_REMOVE = "stopwordRemove"; // true if the feature values

  // have to be
  // stopword-filtered; else:
  // false

  private final String ANNOTATION_HYPERNYM_ADD = "addHypernyms"; // true if hypernyms from a given

  // hypernym list should be added

  private final String ANNOTATION_MAPPING = "mappingFile"; // name of mapping file (without path)

  // used for token text replacement

  private final String ANNOTATION_SNOWBALL_FILTER = "snowballFilter"; // use a certain snowball

  // filter @see
  // org.apache.lucene.analysis.snowball.SnowballFilter

  private final String ANNOTATION_FEATURE_PATH = "featurePath"; // path to the featurestructure

  // which contains the features

  private final String ANNOTATION_SOFA = "sofa"; // source sofa of annotations

  private final String ANNOTATION_POSITION = "position"; // position of feature structures in an

  // array, allowed values; first or last

  private final String ANNOTATION_PREFIX = "prefix"; // adds a prefix to each token

  private final String ANNOTATION_POSTFIX = "postfix"; // adds a postfix to each token

  private final String ANNOTATION_UNIQUE = "unique"; // make the token stream unique (only one token

  // with the same content and same offset)

  private final String ANNOTATION_TOKENIZER = "tokenizer"; // defines the tokenizer to be used

  private final String FEATURE = "feature"; // the tag name for feature definitions

  private final String FEATURE_NAME = "name";

  private final String FEATURE_NUMBER_FORMAT = "numberFormat";

  private DocumentBuilderFactory factory;

  public IndexMappingFileReader() {
    super();
    factory = DocumentBuilderFactory.newInstance();
  }

  /**
   * method to read out the mapping file
   */
  private Document readMappingFile(String mappingFileName) throws IOException {
    Document doc = null;
    File mappingFile = new File(mappingFileName);
    if (!mappingFile.exists()) {
      throw new IOException("no such Filename for Mappingfile");
    }

    FileInputStream fis = new FileInputStream(mappingFile);

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.parse(mappingFile);

    } catch (SAXException e) {
      throw new IOException(e);
    } catch (ParserConfigurationException e) {
      throw new IOException(e);
    }

    fis.close();
    return doc;
  }

  public Collection<FieldDescription> readFieldDescriptionsFromFile(String filePath)
          throws IOException {

    Document doc = readMappingFile(filePath);
    Collection<FieldDescription> fieldDescriptions = new ArrayList<FieldDescription>();
    ArrayList<Node> fieldNodes = new ArrayList<Node>();
    Node rootNode = doc.getElementsByTagName(ROOT).item(0);
    fieldNodes = getChildNodesByName(rootNode, FIELD);
    for (Node fieldNode : fieldNodes) {

      String fieldName = getFieldName(fieldNode);
      FieldDescription fieldDef = new FieldDescription(fieldName);

      String delimiter = getFieldDelimiter(fieldNode);
      if (delimiter != null)
        fieldDef.setDelimiter(delimiter);

      String termVector = getTermVectorTokenizer(fieldNode);
      if (termVector != null)
        fieldDef.setTermVector(termVector);

      String index = getFieldIndex(fieldNode);
      if (index != null)
        fieldDef.setIndex(index);

      String stored = getFieldStored(fieldNode);
      if (stored != null)
        fieldDef.setStored(stored);

      Boolean merge = getFieldMerge(fieldNode);
      if (merge != null)
        fieldDef.setMerge(merge);

      ArrayList<AnnotationDescription> annotationDefAL = new ArrayList<AnnotationDescription>();
      annotationDefAL = createAnnotationDefinitionAL(fieldNode);
      fieldDef.setAnnotationDescriptions(annotationDefAL);

      fieldDescriptions.add(fieldDef);
    }
    return fieldDescriptions;
  }

  private ArrayList<AnnotationDescription> createAnnotationDefinitionAL(Node fieldNode) {
    ArrayList<Node> annotationNodes = new ArrayList<Node>();
    annotationNodes = getChildNodesByName(fieldNode, ANNOTATION);
    ArrayList<AnnotationDescription> annotationDefAL = new ArrayList<AnnotationDescription>();
    for (Node annotationNode : annotationNodes) {

      String annotationType = getAnnotationType(annotationNode);
      AnnotationDescription annotationDef = new AnnotationDescription(annotationType);

      String concatString = getAnnotationConcatString(annotationNode);
      if (concatString != null)
        annotationDef.setConcatString(concatString);

      String splitString = getAnnotationSplitString(annotationNode);
      if (splitString != null)
        annotationDef.setSplitString(splitString);

      Boolean lowercase = getAnnotationLowercase(annotationNode);
      if (lowercase != null)
        annotationDef.setLowercase(lowercase);

      Boolean uppercase = getAnnotationUppercase(annotationNode);
      if (uppercase != null)
        annotationDef.setUppercase(uppercase);

      Boolean stopwordRemove = getAnnotationStopwordRemove(annotationNode);
      if (stopwordRemove != null)
        annotationDef.setStopwordRemove(stopwordRemove);

      Boolean unique = getAnnotationUnique(fieldNode);
      if (unique != null)
        annotationDef.setUnique(unique);

      String snowballFilter = getAnnotationSnowballFilter(annotationNode);
      if (snowballFilter != null)
        annotationDef.setSnowballFilter(snowballFilter);

      String mappingFile = getAnnotationMappingFile(annotationNode);
      if (mappingFile != null)
        annotationDef.setMappingFile(mappingFile);

      String featurePath = getAnnotationFeaturePath(annotationNode);
      if (featurePath != null)
        annotationDef.setFeaturePath(featurePath);

      String sofa = getAnnotationSofa(annotationNode);
      if (sofa != null)
        annotationDef.setSofa(sofa);

      String position = getAnnotationPosition(annotationNode);
      if (position != null)
        annotationDef.setPosition(position);

      String tokenizer = getAnnotationTokenizer(fieldNode);
      if (tokenizer != null)
        annotationDef.setTokenizer(tokenizer);

      String prefix = getAnnotationPrefix(annotationNode);
      if (prefix != null)
        annotationDef.setPrefix(prefix);

      String postfix = getAnnotationPostfix(annotationNode);
      if (postfix != null)
        annotationDef.setPostfix(postfix);

      Boolean addHypernyms = getAddHypernyms(annotationNode);
      if (addHypernyms != null)
        annotationDef.setAddHypernyms(addHypernyms);

      ArrayList<FeatureDescription> featureDefAL = new ArrayList<FeatureDescription>();
      featureDefAL = createFeatureDefinitionAL(annotationNode);
      annotationDef.setFeatureDescriptions(featureDefAL);

      annotationDefAL.add(annotationDef);
    }
    return annotationDefAL;
  }

  private Boolean getAnnotationStopwordRemove(Node annotationNode) {
    Boolean stopwordRemove = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_STOPWORD_REMOVE) != null
            && annotationNode.getAttributes().getNamedItem(ANNOTATION_STOPWORD_REMOVE)
                    .getNodeValue().equals("true")) {
      stopwordRemove = true;
    } else {
      stopwordRemove = false;
    }
    return stopwordRemove;
  }

  private Boolean getAnnotationLowercase(Node annotationNode) {
    Boolean lowercase = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_LOWERCASE) != null
            && annotationNode.getAttributes().getNamedItem(ANNOTATION_LOWERCASE).getNodeValue()
                    .equals("true")) {
      lowercase = true;
    } else {
      lowercase = false;
    }
    return lowercase;
  }

  private Boolean getAnnotationUppercase(Node annotationNode) {
    Boolean uppercase = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_UPPERCASE) != null
            && annotationNode.getAttributes().getNamedItem(ANNOTATION_UPPERCASE).getNodeValue()
                    .equals("true")) {
      uppercase = true;
    } else {
      uppercase = false;
    }
    return uppercase;
  }

  private String getAnnotationConcatString(Node annotationNode) {
    String concatString = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_CONCAT_STRING) != null) {
      concatString =
              annotationNode.getAttributes().getNamedItem(ANNOTATION_CONCAT_STRING).getNodeValue();
    }
    return concatString;
  }

  private String getAnnotationSplitString(Node annotationNode) {
    String splitString = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_SPLIT_STRING) != null) {
      splitString =
              annotationNode.getAttributes().getNamedItem(ANNOTATION_SPLIT_STRING).getNodeValue();
    }
    return splitString;
  }

  private ArrayList<FeatureDescription> createFeatureDefinitionAL(Node annotationNode) {
    ArrayList<Node> featureNodes = new ArrayList<Node>();
    featureNodes = getChildNodesByName(annotationNode, FEATURE);
    ArrayList<FeatureDescription> featureDefAL = new ArrayList<FeatureDescription>();
    for (Node featureNode : featureNodes) {
      String featureName = getFeatureName(featureNode);
      FeatureDescription featureDef = new FeatureDescription(featureName);
      String numberFormat = getFeatureNumberFormat(featureNode);
      featureDef.setNumberFormat(numberFormat);
      featureDefAL.add(featureDef);
    }

    return featureDefAL;
  }

  private String getFeatureName(Node featureNode) {
    String featureName = null;
    if (featureNode.getAttributes().getNamedItem(FEATURE_NAME) != null) {
      featureName = featureNode.getAttributes().getNamedItem(FEATURE_NAME).getNodeValue();
    }
    return featureName;
  }

  private String getFeatureNumberFormat(Node featureNode) {
    String numberFormat = null;
    if (featureNode.getAttributes().getNamedItem(FEATURE_NUMBER_FORMAT) != null) {
      numberFormat = featureNode.getAttributes().getNamedItem(FEATURE_NUMBER_FORMAT).getNodeValue();
    }
    return numberFormat;
  }

  private String getAnnotationType(Node annotationNode) {
    String name = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_TYPE) != null) {
      name = annotationNode.getAttributes().getNamedItem(ANNOTATION_TYPE).getNodeValue();
    }
    return name;
  }

  private String getAnnotationSnowballFilter(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_SNOWBALL_FILTER) != null) {
      value =
              annotationNode.getAttributes().getNamedItem(ANNOTATION_SNOWBALL_FILTER)
                      .getNodeValue();
    }
    return value;
  }

  private String getAnnotationMappingFile(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_MAPPING) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_MAPPING).getNodeValue();
    }
    return value;
  }

  private String getAnnotationFeaturePath(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_FEATURE_PATH) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_FEATURE_PATH).getNodeValue();
    }
    return value;
  }

  private String getAnnotationSofa(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_SOFA) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_SOFA).getNodeValue();
    }
    return value;
  }

  private String getAnnotationPosition(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_POSITION) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_POSITION).getNodeValue();
    }
    return value;
  }

  private String getAnnotationPrefix(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_PREFIX) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_PREFIX).getNodeValue();
    }
    return value;
  }

  private String getAnnotationPostfix(Node annotationNode) {
    String value = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_POSTFIX) != null) {
      value = annotationNode.getAttributes().getNamedItem(ANNOTATION_POSTFIX).getNodeValue();
    }
    return value;
  }

  private Boolean getAddHypernyms(Node annotationNode) {
    Boolean addHypernyms = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_HYPERNYM_ADD) != null
            && annotationNode.getAttributes().getNamedItem(ANNOTATION_HYPERNYM_ADD).getNodeValue()
                    .equals("true")) {
      addHypernyms = true;
    } else {
      addHypernyms = false;
    }
    return addHypernyms;
  }

  private Boolean getFieldMerge(Node fieldNode) {
    Boolean merge = null;
    if (fieldNode.getAttributes().getNamedItem(FIELD_MERGE) != null
            && fieldNode.getAttributes().getNamedItem(FIELD_MERGE).getNodeValue().equals("true")) {
      merge = true;
    } else {
      merge = false;
    }
    return merge;
  }

  private Boolean getAnnotationUnique(Node annotationNode) {
    Boolean unique = null;
    if (annotationNode.getAttributes().getNamedItem(ANNOTATION_UNIQUE) != null
            && annotationNode.getAttributes().getNamedItem(ANNOTATION_UNIQUE).getNodeValue()
                    .equals("true")) {
      unique = true;
    } else {
      unique = false;
    }
    return unique;
  }

  private String getFieldStored(Node fieldNode) {

    Node namedItem = fieldNode.getAttributes().getNamedItem(FIELD_STORED);
    if (namedItem != null)
      return namedItem.getNodeValue();
    else
      return null;
  }

  private String getFieldIndex(Node fieldNode) {
    String index = null;
    if (fieldNode.getAttributes().getNamedItem(FIELD_INDEX) != null) {
      index = fieldNode.getAttributes().getNamedItem(FIELD_INDEX).getNodeValue();
    }
    return index;
  }

  private String getAnnotationTokenizer(Node fieldNode) {
    String tokenizer = null;
    if (fieldNode.getAttributes().getNamedItem(ANNOTATION_TOKENIZER) != null) {
      tokenizer = fieldNode.getAttributes().getNamedItem(ANNOTATION_TOKENIZER).getNodeValue();
    }
    return tokenizer;
  }

  private String getTermVectorTokenizer(Node fieldNode) {
    String termVector = null;
    if (fieldNode.getAttributes().getNamedItem(FIELD_TERM_VECTOR) != null) {
      termVector = fieldNode.getAttributes().getNamedItem(FIELD_TERM_VECTOR).getNodeValue();
    }
    return termVector;
  }

  private String getFieldDelimiter(Node fieldNode) {
    String delimiter = null;
    if (fieldNode.getAttributes().getNamedItem(FIELD_DELIMITER) != null) {
      delimiter = fieldNode.getAttributes().getNamedItem(FIELD_DELIMITER).getNodeValue();
    }
    return delimiter;
  }

  private String getFieldName(Node fieldNode) {
    String name = null;
    if (fieldNode.getAttributes().getNamedItem(FIELD_NAME) != null) {
      name = fieldNode.getAttributes().getNamedItem(FIELD_NAME).getNodeValue();
    }
    return name;
  }

  /**
   * given a rootNode and a nodeName, this mehtod returns the correponding child nodes calles
   * nodeName
   * 
   * @param rootNode
   * @param nodeName
   * @return a node array list
   */
  private ArrayList<Node> getChildNodesByName(Node rootNode, String nodeName) {
    ArrayList<Node> al = getChildrenNodes(rootNode, nodeName, new ArrayList<Node>());
    return al;
  }

  /**
   * returns an Arraylist of all childnodes given a node n with a particular name
   * 
   * @param n
   * @param nodeName
   * @param al
   * @return
   */
  private ArrayList<Node> getChildrenNodes(Node n, String nodeName, ArrayList<Node> al) {
    if (n.getNodeName().equals(nodeName)) {
      al.add(n);
    } else {
      if (n.hasChildNodes()) {
        NodeList nl = n.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
          getChildrenNodes(nl.item(i), nodeName, al);
        }
      }
    }
    return al;
  }
}
