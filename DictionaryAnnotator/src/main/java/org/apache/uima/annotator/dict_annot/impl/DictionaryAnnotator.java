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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContextException;
import org.apache.uima.annotator.dict_annot.dictionary.Dictionary;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryFileParser;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryMatch;
import org.apache.uima.annotator.dict_annot.dictionary.impl.DictionaryFileParserImpl;
import org.apache.uima.annotator.dict_annot.dictionary.impl.HashMapDictionaryBuilder;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

/**
 * Dictionary annotator implementation that use HashMap dictionaries
 */
public class DictionaryAnnotator extends CasAnnotator_ImplBase {

   /**
    * Message catalog
    */
   public static final String MESSAGE_DIGEST = "org.apache.uima.annotator.dict_annot.dictionaryAnnotatorMessages";

   /**
    * System path separator
    */
   public static final String PATH_SEPARATOR = System
         .getProperty("path.separator");

   // DictionaryFiles configuration parameter name
   private static final String DICTIONARY_FILES = "DictionaryFiles";

   // InputMatchType configuration parameter name
   private static final String INPUT_MATCH_TYPE = "InputMatchType";

   // annotator logger
   private Logger logger;

   // input match type name
   private String inputMatchTypeStr;

   // input match type
   private Type inputMatchType;

   // dictionaries used with this annotator
   private Dictionary[] dictionaries;

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.analysis_component.CasAnnotator_ImplBase#process(org.apache.uima.cas.CAS)
    */
   public void process(CAS cas) throws AnalysisEngineProcessException {

      // copy input match type annotations to an array
      FSIterator it = cas.getAnnotationIndex(this.inputMatchType).iterator();
      ArrayList<AnnotationFS> inputTypeAnnots = new ArrayList<AnnotationFS>();
      while (it.hasNext()) {
         inputTypeAnnots.add((AnnotationFS) it.next());
      }
      AnnotationFS[] annotFSs = inputTypeAnnots.toArray(new AnnotationFS[] {});

      // -- use the array of annotations to detect matches --

      for (int i = 0; i < this.dictionaries.length; i++) {
         // get current dictionary output type
         Type currentDictOutputType = cas.getTypeSystem().getType(
               this.dictionaries[i].getTypeName());
         // check output type and throw an exception in case of errors
         if (currentDictOutputType == null) {
            Exception ex = new DictionaryAnnotatorConfigException(
                  "listbased_annotator_error_resolving_types",
                  new Object[] { this.inputMatchTypeStr });
            throw new AnalysisEngineProcessException(ex);
         }

         // iterate over the annotation array and detect matches
         int currentPos = 0;
         while (currentPos < annotFSs.length) {

            // check for dictionary matches at the current token position
            DictionaryMatch dictMatch = this.dictionaries[i].matchEntry(
                  currentPos, annotFSs);

            // check if we have a dictionary match
            if (dictMatch != null) {
               // -- we have found a match starting at the current position --

               // get match length of the match
               int matchLength = dictMatch.getMatchLength();

               // create annotation for the match we found
               int start = annotFSs[currentPos].getBegin();
               int end = annotFSs[currentPos + matchLength - 1].getEnd();
               FeatureStructure fs = cas.createAnnotation(
                     currentDictOutputType, start, end);
               // add annotation to the CAS
               cas.getIndexRepository().addFS(fs);
               // adjust current array position, add match length
               currentPos = currentPos + matchLength;
            } else {
               // -- no match was found, go on with the next token --
               currentPos++;
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#initialize(org.apache.uima.UimaContext)
    */
   public void initialize(UimaContext context)
         throws ResourceInitializationException {
      super.initialize(context);

      // initialize annotator logger
      this.logger = this.getContext().getLogger();

      // get configuration parameter settings
      // get parameter ConceptFiles, default is an empty array
      String[] dictionaryFileNames = safeGetConfigParameterStringArrayValue(
            getContext(), DICTIONARY_FILES, new String[] {});

      // get input match type
      this.inputMatchTypeStr = (String) this.getContext()
            .getConfigParameterValue(INPUT_MATCH_TYPE);

      // create dictionary builder
      DictionaryBuilder dictBuilder = new HashMapDictionaryBuilder();

      // create dictionary file parser
      DictionaryFileParser fileParser = new DictionaryFileParserImpl();

      // get UIMA datapath and tokenize it into its elements
      StringTokenizer tokenizer = new StringTokenizer(getContext()
            .getDataPath(), PATH_SEPARATOR);
      ArrayList<File> datapathElements = new ArrayList<File>();
      while (tokenizer.hasMoreTokens()) {
         // add datapath elements to the 'datapathElements' array list
         datapathElements.add(new File(tokenizer.nextToken()));
      }

      // parse dictionary files
      ArrayList<Dictionary> dicts = new ArrayList<Dictionary>();
      for (int i = 0; i < dictionaryFileNames.length; i++) {
         // try to resolve the relative file name with classpath or datapath
         File file = resolveRelativeFilePath(dictionaryFileNames[i],
               datapathElements);

         // if the current dictionary file wasn't found, throw an exception
         if (file == null) {
            throw new DictionaryAnnotatorConfigException(
                  "listbased_annotator_resource_not_found",
                  new Object[] { dictionaryFileNames[i] });
         } else {
            // log concept file path
            this.logger.logrb(Level.CONFIG, "ListBasedAnnotator", "initialize",
                  MESSAGE_DIGEST, "listbased_annotator_dictionary_file",
                  new Object[] { file.getAbsolutePath() });

            // parse dictionary file
            Dictionary dict = fileParser.parseDictionaryFile(file, dictBuilder);
            // add dictionary to the dictionary list
            dicts.add(dict);
         }
      }

      // store all dictionaries in the dictionary array
      this.dictionaries = dicts.toArray(new Dictionary[] {});
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.analysis_component.CasAnnotator_ImplBase#typeSystemInit(org.apache.uima.cas.TypeSystem)
    */
   public void typeSystemInit(TypeSystem typeSystem)
         throws AnalysisEngineProcessException {

      // initialize the input match type that is used to match the dictionary
      // entries
      this.inputMatchType = typeSystem.getType(this.inputMatchTypeStr);
      if (this.inputMatchType == null) {
         Exception ex = new DictionaryAnnotatorConfigException(
               "listbased_annotator_error_resolving_types",
               new Object[] { this.inputMatchTypeStr });
         throw new AnalysisEngineProcessException(ex);
      }
   }

   /**
    * Reads the given configuration parameters and returns the parameter value.
    * If the parameter is not available or the parameter type is not a String[],
    * the given default value is returned.
    * 
    * @param context
    *           Annotator context
    * @param param
    *           configuration parameter to read
    * @param defaultValue
    *           default parameter value in case of errors
    * @return returns the boolean parameter value
    * @throws AnnotatorContextException
    *            if an unrecoverable error occurs
    */
   private static String[] safeGetConfigParameterStringArrayValue(
         UimaContext context, String param, String[] defaultValue) {
      String[] array = (String[]) context.getConfigParameterValue(param);
      if (array != null && array.length > 0) {
         return array;
      }
      return defaultValue;
   }

   /**
    * Resolves the absolute file name of the given relative file name using the
    * given datapath path elements. If the resolution was successful the File
    * object is returned, if not null.
    * 
    * @param fileName
    *           relative file name to resolve
    * 
    * @param datapathElements
    *           datapath path elements
    * 
    * @return returns the File object of the resolved file, otherwise null.
    */
   private File resolveRelativeFilePath(String fileName,
         ArrayList<File> datapathElements) {

      URL url;
      // try to use the class loader to load the file resource
      if ((url = this.getClass().getClassLoader().getResource(fileName)) != null) {
         return new File(url.getFile());
      } else {
         if (datapathElements == null || datapathElements.size() == 0) {
            return null;
         }
         // try to use the datapath to load the file resource
         for (int i = 0; i < datapathElements.size(); i++) {
            File testFile = new File(datapathElements.get(i), fileName);
            if (testFile.exists()) {
               return testFile;
            }
         }
      }
      return null;
   }
}
