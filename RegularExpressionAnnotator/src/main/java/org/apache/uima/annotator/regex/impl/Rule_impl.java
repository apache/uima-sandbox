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
import java.util.regex.Pattern;

import org.apache.uima.annotator.regex.Feature;
import org.apache.uima.annotator.regex.FilterFeature;
import org.apache.uima.annotator.regex.Rule;
import org.apache.uima.annotator.regex.RuleException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * 
 */
public class Rule_impl implements Rule {

   private final String regex;

   private final String id;

   private final float confidence;

   private final String matchTypeStr;

   private final int matchStrategy;

   private Pattern pattern;

   private Type matchType;

   private ArrayList filterFeatures;

   private ArrayList updateFeatures;

   private ArrayList exceptions;

   public Rule_impl(String regex, int matchStrategy, String matchType,
         String id, float confidence) {
      this.regex = regex;
      this.matchStrategy = matchStrategy;
      this.matchTypeStr = matchType;
      this.filterFeatures = new ArrayList();
      this.updateFeatures = new ArrayList();
      this.exceptions = new ArrayList();
      this.pattern = null;
      this.id = id;
      this.confidence = confidence;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#addFilterFeature(org.apache.uima.annotator.regex.Feature)
    */
   public void addFilterFeature(FilterFeature aFeature) {
      this.filterFeatures.add(aFeature);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getMatchTypeFilterFeatures()
    */
   public FilterFeature[] getMatchTypeFilterFeatures() {
      return (FilterFeature[]) this.filterFeatures
            .toArray(new FilterFeature[0]);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getMatchStrategy()
    */
   public int getMatchStrategy() {
      return this.matchStrategy;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getMatchType()
    */
   public Type getMatchType() {
      return this.matchType;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getRegex()
    */
   public Pattern getRegexPattern() {
      return this.pattern;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getConfidence()
    */
   public float getConfidence() {
      return this.confidence;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getId()
    */
   public String getId() {
      return this.id;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#addUpdateFeature(org.apache.uima.annotator.regex.Feature)
    */
   public void addUpdateFeature(Feature aFeature) {
      this.updateFeatures.add(aFeature);

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getMatchTypeUpdateFeatures()
    */
   public Feature[] getMatchTypeUpdateFeatures() {
      return (Feature[]) this.updateFeatures.toArray(new Feature[0]);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#addException(org.apache.uima.annotator.regex.Exception)
    */
   public void addException(RuleException aException) {
      this.exceptions.add(aException);

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.Rule#getExceptions()
    */
   public RuleException[] getExceptions() {
      return (RuleException[]) this.exceptions.toArray(new RuleException[0]);
   }

   /**
    * @param ts
    * @throws ResourceInitializationException
    */
   public void typeInit(TypeSystem ts) throws ResourceInitializationException {
      // initialize the match type
      if (this.matchTypeStr != null) {
         this.matchType = ts.getType(this.matchTypeStr);
         if (this.matchType == null) {
            throw new ResourceInitializationException(
                  RegExAnnotator.MESSAGE_DIGEST,
                  "regex_annotator_error_resolving_types",
                  new Object[] { this.matchTypeStr });
         }
      }

      // initialize match type filters
      FilterFeature[] filterFeats = getMatchTypeFilterFeatures();
      for (int i = 0; i < filterFeats.length; i++) {
         ((FilterFeature_impl) filterFeats[i]).typeInit(this.matchType);
      }

      // initialize match type update features
      Feature[] updateFeats = getMatchTypeUpdateFeatures();
      for (int i = 0; i < updateFeats.length; i++) {
         ((Feature_impl) updateFeats[i]).typeInit(this.matchType);
      }

      // initialize rule exceptions
      RuleException[] ruleExceptions = getExceptions();
      for (int i = 0; i < ruleExceptions.length; i++) {
         ((RuleException_impl) ruleExceptions[i]).typeInit(ts);
      }
   }

   /**
    * @throws RegexAnnotatorConfigException
    */
   public void initialize() throws RegexAnnotatorConfigException {
      // compile regex
      this.pattern = Pattern.compile(this.regex);

      // initialize match type filters
      FilterFeature[] filterFeats = getMatchTypeFilterFeatures();
      for (int i = 0; i < filterFeats.length; i++) {
         ((FilterFeature_impl) filterFeats[i]).initialize();
      }

      // initialize match type update features
      Feature[] updateFeats = getMatchTypeUpdateFeatures();
      for (int i = 0; i < updateFeats.length; i++) {
         ((Feature_impl) updateFeats[i]).initialize();
      }

      // initialize rule exceptions
      RuleException[] ruleExceptions = getExceptions();
      for (int i = 0; i < ruleExceptions.length; i++) {
         ((RuleException_impl) ruleExceptions[i]).initialize();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   public String toString() {

      StringBuffer buffer = new StringBuffer();
      buffer.append("Rule ");
      if (this.id != null) {
         buffer.append(this.id);
      }
      buffer.append("\n");
      buffer.append("Regex: ");
      buffer.append(this.regex);
      if (this.matchStrategy == Rule.MATCH_ALL) {
         buffer.append("\nMatch strategy: MATCH_ALL");
      } else if (this.matchStrategy == Rule.MATCH_COMPLETE) {
         buffer.append("\nMatch strategy: MATCH_COMPLETE");
      } else if (this.matchStrategy == Rule.MATCH_FIRST) {
         buffer.append("\nMatch strategy: MATCH_FIRST");
      }
      buffer.append("\nMatch type: ");
      buffer.append(this.matchTypeStr);

      if (this.confidence != 0.0) {
         buffer.append("\nConfidence: ");
         buffer.append(this.confidence);
      }

      FilterFeature[] filterFeats = getMatchTypeFilterFeatures();
      if (filterFeats.length > 0) {
         buffer.append("\nMatch type filter features: \n");
      }
      for (int i = 0; i < filterFeats.length; i++) {
         buffer.append(filterFeats[i].toString());
      }
      buffer.append("\n");
      return buffer.toString();
   }

}