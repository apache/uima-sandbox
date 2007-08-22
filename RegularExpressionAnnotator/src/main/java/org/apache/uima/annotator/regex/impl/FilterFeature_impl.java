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

import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.annotator.regex.FilterFeature;
import org.apache.uima.cas.Type;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * 
 * 
 */
public class FilterFeature_impl implements FilterFeature {

  private final String name;

  private final String patternStr;

  private org.apache.uima.cas.Feature feature;

  private Pattern pattern;

  /**
   * @param name
   * @param patternStr
   */
  public FilterFeature_impl(String name, String patternStr) {
    this.name = name;
    this.patternStr = patternStr;
    this.feature = null;
    this.pattern = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.FilterFeature#getName()
   */
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.FilterFeature#getPattern()
   */
  public Pattern getPattern() {
    return this.pattern;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.FilterFeature#getFeature()
   */
  public org.apache.uima.cas.Feature getFeature() {
    return this.feature;
  }

  public void initialize(Type annotationType) throws ResourceInitializationException {
    // compile pattern
    this.pattern = Pattern.compile(this.patternStr);

    // get feature by name from the specified annotation type
    this.feature = annotationType.getFeatureByBaseName(this.name);

    // throw exception if the feature does not exist
    if (feature == null) {
      throw new ResourceInitializationException(AnnotatorInitializationException.FEATURE_NOT_FOUND,
              new Object[] { FilterFeature_impl.class.getName(), this.name });
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Filter Feature: ");
    buffer.append("\n  Name: ");
    buffer.append(this.name);
    buffer.append("\n  Pattern: ");
    buffer.append(this.patternStr);
    buffer.append("\n");

    return buffer.toString();
  }

}
