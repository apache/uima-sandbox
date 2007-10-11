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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import noNamespace.ConceptSetDocument;
import noNamespace.ConceptSetDocument.ConceptSet;
import noNamespace.CreateAnnotationsDocument.CreateAnnotations;
import noNamespace.RulesDocument.Rules;
import noNamespace.SetFeatureDocument.SetFeature;

import org.apache.uima.annotator.regex.Annotation;
import org.apache.uima.annotator.regex.Concept;
import org.apache.uima.annotator.regex.ConceptFileParser;
import org.apache.uima.annotator.regex.Feature;
import org.apache.uima.annotator.regex.FilterFeature;
import org.apache.uima.annotator.regex.Position;
import org.apache.uima.annotator.regex.Rule;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.xmlbeans.XmlOptions;

/**
 * 
 */
public class ConceptFileParser_impl implements ConceptFileParser {

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.regex.RuleFileParser#parseRuleFile(java.io.File)
    */
   public Concept[] parseConceptFile(File conceptFile)
         throws ResourceInitializationException {
      ArrayList conceptList = new ArrayList();

      // parse regex concept file and extract content to local objects
      ConceptSetDocument conceptSetDoc;
      try {
         conceptSetDoc = ConceptSetDocument.Factory.parse(conceptFile);
      } catch (Exception ex) {
         throw new RegexAnnotatorConfigException(
               "regex_annotator_error_parsing_rule_set_file",
               new Object[] { conceptFile.getAbsolutePath() }, ex);
      }

      // validate input file
      ArrayList validationErrors = new ArrayList();
      XmlOptions validationOptions = new XmlOptions();
      validationOptions.setErrorListener(validationErrors);

      boolean isValid = conceptSetDoc.validate(validationOptions);

      // outout the errors if the XML is invalid.
      if (!isValid) {
         Iterator iter = validationErrors.iterator();
         StringBuffer errorMessages = new StringBuffer();
         while (iter.hasNext()) {
            errorMessages.append("\n>> ");
            errorMessages.append(iter.next());
         }
         throw new RegexAnnotatorConfigException(
               "regex_annotator_error_xml_validation", new Object[] {
                     conceptFile.getAbsolutePath(), errorMessages.toString() });
      }

      // ***************************************************
      // get the concepts from the concept file document
      // ***************************************************
      ConceptSet conceptSet = conceptSetDoc.getConceptSet();
      noNamespace.ConceptDocument.Concept[] concepts = conceptSet
            .getConceptArray();
      for (int i = 0; i < concepts.length; i++) {
         // get concept meta data
         String conceptName = concepts[i].getName();
         boolean processAllRules = concepts[i].getProcessAllRules();

         // create new concept object
         org.apache.uima.annotator.regex.Concept concept = new Concept_impl(
               conceptName, processAllRules);

         // ********************************
         // get all rules for this concept
         // ********************************
         Rules rules = concepts[i].getRules();
         noNamespace.RuleDocument.Rule[] ruleList = rules.getRuleArray();
         for (int r = 0; r < ruleList.length; r++) {
            // get rule meta data
            String regex = ruleList[r].getRegEx();
            String matchType = ruleList[r].getMatchType();
            int matchStrategy = ruleList[r].getMatchStrategy().intValue();
            String id = ruleList[r].getRuleId();
            float confidence = (float) 0.0;
            if (ruleList[r].getConfidence() != null) {
               confidence = ruleList[r].getConfidence().floatValue();
            }

            // create new rule
            Rule rule = new Rule_impl(regex, matchStrategy, matchType, id,
                  confidence);

            // ********************************
            // get match type filter features
            // ********************************
            if (ruleList[r].getMatchTypeFilter() != null) {
               // iterate over all filter features and add them to the rule
               noNamespace.FeatureDocument.Feature[] filterFeatures = ruleList[r]
                     .getMatchTypeFilter().getFeatureArray();
               for (int x = 0; x < filterFeatures.length; x++) {
                  String featureName = filterFeatures[x].getName();
                  String featureValue = filterFeatures[x].getStringValue();

                  // create new filter feature and add them to the rule
                  FilterFeature filterFeature = new FilterFeature_impl(
                        featureName, featureValue);
                  rule.addFilterFeature(filterFeature);
               }
            }
            // ***********************************************
            // get all update match type annotation features
            // ***********************************************
            if (ruleList[r].getUpdateMatchTypeAnnotation() != null) {
               // iterate over all match type annotation update features and add
               // them to the rule
               SetFeature[] updateFeatures = ruleList[r]
                     .getUpdateMatchTypeAnnotation().getSetFeatureArray();
               for (int x = 0; x < updateFeatures.length; x++) {
                  String featureName = updateFeatures[x].getName();
                  String featureValue = updateFeatures[x].getStringValue();
                  int featureType = updateFeatures[x].getType().intValue();
                  int normalization = 0;
                  if (updateFeatures[x].getNormalization() != null) {
                     normalization = updateFeatures[x].getNormalization()
                           .intValue();
                  }
                  String implClass = updateFeatures[x].getClass1();
                  // create new feature and add them to the rule
                  Feature updateFeature = new Feature_impl(featureType,
                        featureName, featureValue, normalization, implClass);
                  rule.addUpdateFeature(updateFeature);
               }
            }

            // **********************************
            // get all exceptions for this rule
            // **********************************
            if (ruleList[r].getRuleExceptions() != null) {
               // iterate over all match type annotation update features and add
               // them to the rule
               noNamespace.ExceptionDocument.Exception[] exceptions = ruleList[r]
                     .getRuleExceptions().getExceptionArray();
               for (int x = 0; x < exceptions.length; x++) {
                  String exceptionMatchType = exceptions[x].getMatchType();
                  String regexPattern = exceptions[x].getStringValue();

                  // create new Exception object and add them to the rule
                  org.apache.uima.annotator.regex.RuleException exception = new RuleException_impl(
                        exceptionMatchType, regexPattern);
                  rule.addException(exception);
               }
            }

            // add rule to the concept
            concept.addRule(rule);
         }

         // **************************************
         // get all annotations for this concept
         // **************************************
         CreateAnnotations annotations = concepts[i].getCreateAnnotations();
         noNamespace.AnnotationDocument.Annotation[] annotationList = annotations
               .getAnnotationArray();
         for (int a = 0; a < annotationList.length; a++) {

            // create annotation position objects
            int beginMatchGroup = annotationList[a].getBegin().getGroup();
            int beginLocation = annotationList[a].getBegin().getLocation()
                  .intValue();
            int endMatchGroup = annotationList[a].getEnd().getGroup();
            int endLocation = annotationList[a].getEnd().getLocation()
                  .intValue();

            Position begin = new Position_impl(beginMatchGroup, beginLocation);
            Position end = new Position_impl(endMatchGroup, endLocation);

            // create annotation object
            String id = annotationList[a].getId();
            String type = annotationList[a].getType();
            String validationClass = annotationList[a].getValidate();

            Annotation annotation = new Annotation_impl(id, type, begin, end, validationClass);

            // read out feature values and add it to the annotation
            SetFeature[] features = annotationList[a].getSetFeatureArray();
            for (int f = 0; f < features.length; f++) {
               String name = features[f].getName();
               int featureType = features[f].getType().intValue();
               String value = features[f].getStringValue();
               int normalization = 0;
               if (features[f].getNormalization() != null) {
                  normalization = features[f].getNormalization().intValue();
               }
               String implClass = features[f].getClass1();

               Feature feature = new Feature_impl(featureType, name, value,
                     normalization, implClass);
               annotation.addFeature(feature);
            }
            // add annotation to rule
            concept.addAnnotation(annotation);
         }
         conceptList.add(concept);
      }

      return (Concept[]) conceptList.toArray(new Concept[0]);
   }
}
