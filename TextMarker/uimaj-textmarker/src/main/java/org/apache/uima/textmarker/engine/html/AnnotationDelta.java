/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.textmarker.engine.html;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.cas.StringArray;

public class AnnotationDelta {
  private AnnotationFS annotation;

  private int delta;

  private StringArray attributeName;

  private StringArray attributeValue;

  private String name;

  private boolean stripHtml;

  public AnnotationDelta(AnnotationFS annotation, int delta, StringArray attributeName,
          StringArray attributeValue, String name, boolean stripHtml) {
    this.annotation = annotation;
    this.delta = delta;
    this.attributeName = attributeName;
    this.attributeValue = attributeValue;
    this.name = name;
    this.stripHtml = stripHtml;
  }

  public void changeDelta(int omega) {
    delta = omega;
  }

  public AnnotationFS getAnnotation() {
    return annotation;
  }

  public int getDelta() {
    if(stripHtml) {
      return delta;
    } else  {
      return 0;
    }
  }

  public StringArray getAttributeName() {
    return attributeName;
  }

  public StringArray getAttributeValue() {
    return attributeValue;
  }

  public String getName() {
    return name;
  }
}
