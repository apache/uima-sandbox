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

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.uima.annotator.regex.Annotation;
import org.apache.uima.annotator.regex.Concept;
import org.apache.uima.annotator.regex.Rule;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * 
 */
public class Concept_impl implements Concept {

  private ArrayList annotations;

  private String name;
    
  private ArrayList rules;

  /**
   * @param name
   */
  public Concept_impl(String name) {
    this.name = name;
    this.annotations = new ArrayList();
    this.rules = new ArrayList();
  }

  /* (non-Javadoc)
   * @see org.apache.uima.annotator.regex.Rule#addAnnotation(org.apache.uima.annotator.regex.Annotation)
   */
  public void addAnnotation(Annotation aAnnotation) {
    this.annotations.add(aAnnotation);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.annotator.regex.Rule#getInstructions()
   */
  public Annotation[] getAnnotations() {
    return (Annotation[]) this.annotations.toArray(new Annotation[0]);
  }


  /* (non-Javadoc)
   * @see org.apache.uima.annotator.regex.Concept#addRule(org.apache.uima.annotator.regex.Rule)
   */
  public void addRule(Rule aRule) {
    this.rules.add(aRule);
  }

  /* (non-Javadoc)
   * @see org.apache.uima.annotator.regex.Concept#getName()
   */
  public String getName() {
    return this.name;
  }

  /* (non-Javadoc)
   * @see org.apache.uima.annotator.regex.Concept#getRules()
   */
  public Rule[] getRules() {
    //get rules array
    Rule[] ruleList = (Rule[]) this.rules.toArray(new Rule[0]);
    //sort rules array by confidence
    Arrays.sort(ruleList, new RuleComparator());
    return ruleList;
  }

  /**
   * @param ts
   * @throws ResourceInitializationException
   */
  public void initialize(TypeSystem ts) throws ResourceInitializationException {

    // initialize all rules for this concept
    Rule[] ruleList = getRules();
    for (int i = 0; i < ruleList.length; i++) {
      ((Rule_impl) ruleList[i]).initialize(ts);
    }
    
    // initialize all annotations for this concept
    Annotation[] annots = getAnnotations();
    for (int i = 0; i < annots.length; i++) {
      ((Annotation_impl) annots[i]).initialize(ts);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {

    StringBuffer buffer = new StringBuffer();
    buffer.append("Concept ");
    if(this.name != null) {
      buffer.append(this.name);
    }
 
    Rule[] ruleList = getRules();
    if(ruleList.length > 0) {
      buffer.append("\nConcept rules: \n");
    }
    for (int i = 0; i < ruleList.length; i++) {
        buffer.append(ruleList[i].toString());
    }

    Annotation[] annots = getAnnotations();
    if(annots.length > 0) {
      buffer.append("Annotations: \n");
    }
    // print all annotations for this rule
    for (int i = 0; i < annots.length; i++) {
      buffer.append(annots[i].toString());
    }
    buffer.append("\n");
    return buffer.toString();
  }
}