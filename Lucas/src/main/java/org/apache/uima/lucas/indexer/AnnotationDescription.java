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

import java.util.ArrayList;

/**
 * Instances of this class represent annotation descriptions in the mapping file.
 */
public class AnnotationDescription {

  private String type;

  private String concatString = null;

  private String splitString = null;

  private String featurePath;

  private String position;

  private Boolean lowercase = false;

  private Boolean uppercase = false;

  private Boolean stopwordRemove = false;

  private Boolean addHypernyms = false;

  private Boolean unique = false;

  private String mappingFile;

  private String prefix;

  private String postfix;

  private String tokenizer;

  private ArrayList<FeatureDescription> featureDescriptions;

  private String snowballFilter;

  private String sofa;

  public final static String FIRST_POSITION = "first";

  public final static String LAST_POSITION = "last";

  public AnnotationDescription(String type) {
    this.type = type;
    featureDescriptions = new ArrayList<FeatureDescription>();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ArrayList<FeatureDescription> getFeatureDescriptions() {
    return featureDescriptions;
  }

  public void setFeatureDescriptions(ArrayList<FeatureDescription> featureDefinitionAL) {
    this.featureDescriptions = featureDefinitionAL;
  }

  public String getConcatString() {
    return concatString;
  }

  public void setConcatString(String concatString) {
    this.concatString = concatString;
  }

  public Boolean getLowercase() {
    return lowercase;
  }

  public void setLowercase(Boolean lowercase) {
    this.lowercase = lowercase;
  }

  public Boolean getStopwordRemove() {
    return stopwordRemove;
  }

  public void setStopwordRemove(Boolean stopwordRemove) {
    this.stopwordRemove = stopwordRemove;
  }

  public String getSnowballFilter() {
    return snowballFilter;
  }

  public void setSnowballFilter(String snowballFilter) {
    this.snowballFilter = snowballFilter;
  }

  public Boolean getAddHypernyms() {
    return addHypernyms;
  }

  public void setAddHypernyms(Boolean addHypernyms) {
    this.addHypernyms = addHypernyms;
  }

  public String getFeaturePath() {
    return featurePath;
  }

  public void setFeaturePath(String featurePath) {
    this.featurePath = featurePath;
  }

  public String getSofa() {
    return sofa;
  }

  public void setSofa(String sofa) {
    this.sofa = sofa;
  }

  @Override
  public String toString() {
    String string = "{";
    string += "type: " + type + "; ";
    string += "concatString: " + concatString + "; ";
    string += "featurePath: " + featurePath + "; ";
    string += "lowercase: " + lowercase + "; ";
    string += "stopwordRemove: " + stopwordRemove + "; ";
    string += "addHypernyms: " + addHypernyms + "; ";
    string += "featureDescriptions" + featureDescriptions + "; ";
    string += "snowballFilter: " + snowballFilter + "; ";
    string += "sofa: " + sofa + "; ";
    string += "}";
    return string;
  }

  public Boolean getUppercase() {
    return uppercase;
  }

  public void setUppercase(Boolean uppercase) {
    this.uppercase = uppercase;
  }

  public String getMappingFile() {
    return mappingFile;
  }

  public void setMappingFile(String mappingFile) {
    this.mappingFile = mappingFile;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getSplitString() {
    return splitString;
  }

  public void setSplitString(String splitterString) {
    this.splitString = splitterString;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getPostfix() {
    return postfix;
  }

  public void setPostfix(String postfix) {
    this.postfix = postfix;
  }

  public Boolean getUnique() {
    return unique;
  }

  public void setUnique(Boolean unique) {
    this.unique = unique;
  }

  public String getTokenizer() {
    return tokenizer;
  }

  public void setTokenizer(String tokenizer) {
    this.tokenizer = tokenizer;
  }
}
