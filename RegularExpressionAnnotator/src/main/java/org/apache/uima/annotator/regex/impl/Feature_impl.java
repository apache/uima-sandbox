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
package org.apache.uima.annotator.regex.impl;

import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.annotator.regex.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * 
 * 
 */
public class Feature_impl implements Feature {

  private final int type;

  private final String name;

  private final String value;

  private org.apache.uima.cas.Feature feature;

  public Feature_impl(int type, String name, String value) {
    this.type = type;
    this.name = name;
    this.value = value;
    this.feature = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.Feature#getType()
   */
  public int getType() {
    return this.type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.Feature#getName()
   */
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.Feature#getValue()
   */
  public String getValue() {
    return this.value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.Feature#getFeature()
   */
  public org.apache.uima.cas.Feature getFeature() {
    return this.feature;
  }

  /**
   * @param annotationType
   * @throws ResourceInitializationException
   */
  public void initialize(Type annotationType) throws ResourceInitializationException {
    // get feature by name from the specified annotation type
    this.feature = annotationType.getFeatureByBaseName(this.name);

    // throw exception if the feature does not exist
    if (this.feature == null) {
      throw new ResourceInitializationException(AnnotatorInitializationException.FEATURE_NOT_FOUND,
              new Object[] { Feature_impl.class.getName(), this.name });
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Feature: ");
    buffer.append("\n  Name: ");
    buffer.append(this.name);
    if (this.type == Feature.FLOAT_FEATURE) {
      buffer.append("\n  Type: float");
    } else if (this.type == Feature.INTEGER_FEATURE) {
      buffer.append("\n  Type: integer");
    } else if (this.type == Feature.STRING_FEATURE) {
      buffer.append("\n  Type: string");
    } else if (this.type == Feature.REFERENCE_FEATURE) {
      buffer.append("\n  Type: reference");
    } else if (this.type == Feature.CONFIDENCE_FEATURE) {
      buffer.append("\n  Type: confidence");
    } else if (this.type == Feature.RULEID_FEATURE) {
      buffer.append("\n  Type: ruleId");
    }
    buffer.append("\n  Value: ");
    buffer.append(this.value);
    buffer.append("\n");

    return buffer.toString();
  }

}
