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
 * 
 * Instances of this class represent field descriptions in the mapping file.
 */
public class FieldDescription {

  private String name;

  private String index = "no"; // limited to no, no_norms, no_tf, no_norms_tf, and yes

  private String stored = "no";

  private String delimiter;

  private Boolean merge = false;

  private String termVector = "no";// limited to no, offsets, positions, positions_offset and yes

  private ArrayList<AnnotationDescription> annotationDescriptions;

  public FieldDescription(String name) {
    this.name = name;
    annotationDescriptions = new ArrayList<AnnotationDescription>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<AnnotationDescription> getAnnotationDescriptions() {
    return annotationDescriptions;
  }

  public void setAnnotationDescriptions(ArrayList<AnnotationDescription> annotationDefinitionAL) {
    this.annotationDescriptions = annotationDefinitionAL;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public Boolean getMerge() {
    return merge;
  }

  public void setMerge(Boolean merge) {
    this.merge = merge;
  }

  public String getTermVector() {
    return termVector;
  }

  public void setTermVector(String termVector) {
    this.termVector = termVector;
  }

  public String getStored() {
    return stored;
  }

  public void setStored(String stored) {
    this.stored = stored;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }
}
