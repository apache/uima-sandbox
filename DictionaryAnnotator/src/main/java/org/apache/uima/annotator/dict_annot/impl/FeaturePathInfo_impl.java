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
package org.apache.uima.annotator.dict_annot.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.uima.annotator.dict_annot.dictionary.impl.FeaturePathInfo;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.LowLevelCAS;
import org.apache.uima.cas.impl.TypeSystemUtils;
import org.apache.uima.cas.impl.TypeSystemUtils.PathValid;
import org.apache.uima.cas.text.AnnotationFS;

/**
 * FeaturePathInfo implementation that validates the given featurePath and
 * returns the featurePath value for a specified annotation.
 */
public class FeaturePathInfo_impl implements FeaturePathInfo{

   // featurePath string, separated by "/"
   private String featurePathString;

   // featurePath element names
   private ArrayList<String> featurePathElementNames;

   // featurePath element features
   private ArrayList<Feature> featurePathElements;

   /**
    * Constructor to create a new featurePath object 
    */
   public FeaturePathInfo_impl() {
      this.featurePathElementNames = new ArrayList<String>();
      this.featurePathElements = null;
   }

   /**
    * Initialize the object's featurePath for the given type. If the featurePath
    * is not valid an exception is thrown.
    * 
    * @param featurePath
    *           featurePath string separated by "/"
    */
   public void initialize(String featurePath) throws DictionaryAnnotatorConfigException {

      this.featurePathString = featurePath;
      
      // check featurePath for invalid character sequences
      if (this.featurePathString.indexOf("//") > -1) {
         // invalid featurePath syntax
         throw new DictionaryAnnotatorConfigException(
               "dictionary_annotator_error_invalid_feature_path_syntax",
               new Object[] { this.featurePathString, "//" });
      }

      // parse feature path into path elements
      StringTokenizer tokenizer = new StringTokenizer(
            this.featurePathString, "/");
      while (tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();
         this.featurePathElementNames.add(token);
      }
   }
   
   /**
    * checks the feature path for the given type and checks if it can be valid.
    * 
    * @throws DictionaryAnnotatorProcessException
    */
   public void typeSystemInit(Type featurePathType) throws DictionaryAnnotatorProcessException {
      // validate featurePath for given type
      PathValid pathValid = TypeSystemUtils.isPathValid(featurePathType,
            this.featurePathElementNames);
      if (PathValid.NEVER == pathValid) {
         // invalid featurePath - throw an configuration exception
         throw new DictionaryAnnotatorProcessException(
               "dictionary_annotator_error_validating_feature_path",
               new Object[] { this.featurePathString, featurePathType.getName() });
      } else if (PathValid.ALWAYS == pathValid) {
         // the featurePath is always valid, so we can resolve and cache the
         // path elements
         this.featurePathElements = new ArrayList<Feature>();
         Type currentType = featurePathType;
         // iterate over all featurePathNames and store the resolved CAS
         // feature in the featurePathElements list
         for (int i = 0; i < this.featurePathElementNames.size(); i++) {
            // get feature
            Feature feature = currentType
                  .getFeatureByBaseName(this.featurePathElementNames.get(i));
            // store feature
            this.featurePathElements.add(feature);

            // get current feature type to resolve the next feature name
            currentType = feature.getRange();
         }
      }
   }

   public String getValue(AnnotationFS annotFS) {

      // handle special case where no featurePath was specified
      // (featurePathString == null)
      if (this.featurePathElementNames.size() == 0) {
         // no featurePath was specified, return the coveredText of the annotFS
         // as matching text
         return annotFS.getCoveredText();
      } else {
         // we have a feature path that must be evaluated

         // featurePathValue
         String featurePathValue = null;
         // check if further featurePath elements are possible
         boolean noFurtherElementsPossible = false;

         // set current FS values
         FeatureStructure currentFS = annotFS;
         Type currentType = annotFS.getType();
         int currentFeatureTypeCode = LowLevelCAS.TYPE_CLASS_INVALID; 
         // resolve feature path value
         for (int i = 0; i < this.featurePathElementNames.size(); i++) {

            // if we had in the last iteration a primitive feature or a FS that
            // was
            // not set, the feature path is not valid for this annotation.
            if (noFurtherElementsPossible) {
               return null;
            }

            // get the Feature for the current featurePath element. If the
            // featurePath is always valid the featurePath Feature elements are
            // cached, otherwise the feature names must be resolved by name
            Feature feature;
            if (this.featurePathElements == null) {
               // resolve Feature by name
               feature = currentType
                     .getFeatureByBaseName(this.featurePathElementNames.get(i));
            } else {
               // use cached Feature element
               feature = this.featurePathElements.get(i);
            }

            // if feature is null the feature was not defined
            if (feature == null) {
               return null;
            }

            // get feature type and type code
            Type featureType = feature.getRange();
            currentFeatureTypeCode = TypeSystemUtils.classifyType(featureType);

            // switch feature type code
            switch (currentFeatureTypeCode) {
            case LowLevelCAS.TYPE_CLASS_STRING:
               featurePathValue = currentFS.getStringValue(feature);
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_INT:
               featurePathValue = Integer.toString(currentFS
                     .getIntValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_BOOLEAN:
               featurePathValue = Boolean.toString(currentFS
                     .getBooleanValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_BYTE:
               featurePathValue = Byte
                     .toString(currentFS.getByteValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_DOUBLE:
               featurePathValue = Double.toString(currentFS
                     .getDoubleValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_FLOAT:
               featurePathValue = Float.toString(currentFS
                     .getFloatValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_LONG:
               featurePathValue = Long
                     .toString(currentFS.getLongValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_INVALID:
               featurePathValue = null;
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_SHORT:
               featurePathValue = Short.toString(currentFS
                     .getShortValue(feature));
               noFurtherElementsPossible = true;
               break;
            case LowLevelCAS.TYPE_CLASS_FS:
               currentFS = currentFS.getFeatureValue(feature);
               if (currentFS == null) {
                  // FS value not set - feature path cannot return a valid value
                  noFurtherElementsPossible = true;
                  featurePathValue = null;
               } else {
                  currentType = currentFS.getType();
               }
               break;
            default:
               // create message for unsupported feature path type
               ResourceBundle bundle = ResourceBundle.getBundle(
                     DictionaryAnnotator.MESSAGE_DIGEST, Locale.getDefault(), this.getClass().getClassLoader());
               // retrieve the message from the resource bundle
               String rawMessage = bundle.getString("dictionary_annotator_error_feature_path_element_not_supported");
               MessageFormat messageFormat = new MessageFormat(rawMessage);
               messageFormat.setLocale(Locale.getDefault());
               String message = messageFormat.format( new Object[] { currentType.getName(),
                     this.featurePathElementNames.get(i),
                     this.featurePathString });
               // throw a RuntimeException with the unsupported feature path
               throw new RuntimeException(message);
            }
         }

         // the featurePath was processed correctly, check the featurePath value
         if (featurePathValue != null) {
            return featurePathValue;
         } else {
            // it seems that the last featurePath element was a FS
            // check now if the FS is of type AnnotationFS
            if (currentFS != null && currentFeatureTypeCode == LowLevelCAS.TYPE_CLASS_FS) {
               if (currentFS instanceof AnnotationFS) {
                  // the last FS was an Annotation, so return the covered Text
                  // of the annotation as featurePath value
                  return ((AnnotationFS) currentFS).getCoveredText();
               }
            }
            return null;
         }
      }
   }

   /* (non-Javadoc)
    * @see org.apache.uima.annotator.regex.FeaturePath#getFeaturePath()
    */
   public String getFeaturePath() {
      return this.featurePathString;
   }
   
}
