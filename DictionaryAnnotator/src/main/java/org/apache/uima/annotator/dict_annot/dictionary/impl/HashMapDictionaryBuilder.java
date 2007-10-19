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
package org.apache.uima.annotator.dict_annot.dictionary.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.uima.annotator.dict_annot.dictionary.Dictionary;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder;

/**
 * Implementation of the DictionaryBuilder interface to build a new HashMap
 * dictionary.
 */
public class HashMapDictionaryBuilder implements DictionaryBuilder {

   private boolean caseNormalization;

   private boolean createMultiWordEntries;

   private HashMapDictionary dictionary;

   public HashMapDictionaryBuilder() {
      this(true, true);
   }

   public HashMapDictionaryBuilder(boolean caseNormalization,
         boolean createMultiWordEntries) {
      this.caseNormalization = caseNormalization;
      this.createMultiWordEntries = createMultiWordEntries;
      this.dictionary = new HashMapDictionary(this.caseNormalization);
   }

   /* (non-Javadoc)
    * @see org.apache.uima.annotator.listbased.dictionary.DictionaryBuilder#setDictionaryProperties(java.lang.String, java.lang.String)
    */
   public void setDictionaryProperties(String language, String typeName) {
      this.dictionary.setDictionaryLanguage(language);
      this.dictionary.setTypeName(typeName);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.DictionaryBuilder#createDictionary(java.io.File,
    *      java.lang.String)
    */
   public void createDictionary(File entriesFile, String encoding)
         throws DictionaryBuilderException {

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               new FileInputStream(entriesFile), encoding));
         String line = reader.readLine();

         while (line != null) {

            addWord(line);
            //get next line
            line = reader.readLine();
         }
         reader.close();
      } catch (Exception ex) {
         throw new DictionaryBuilderException(ex);
      }
   }

   /**
    * split up the given input in several tokens using the whitespace character 
    * as delimiter.
    *  
    * @param input word that should be tokenized
    * 
    * @return Tokens for the given input
    */
   private String[] whiteSpaceTokenizer(String input) {
      
      //create 
      StringTokenizer tokenizer = new StringTokenizer(input, " ");
      
      ArrayList tokens = new ArrayList();
      while (tokenizer.hasMoreTokens()) {
         tokens.add(tokenizer.nextToken());
      }
      String[] multiWord = (String[]) tokens.toArray(new String[] {});
      
      return multiWord;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.DictionaryBuilder#addWord(java.lang.String)
    */
   public void addWord(String word) {
      
      if (this.createMultiWordEntries) {
         //tokenize the entry
         String[] multiWord = whiteSpaceTokenizer(word);

         if (multiWord.length == 1) {
            this.dictionary.addWord(multiWord[0]);
         } else {
            this.dictionary.addMultiWord(multiWord);
         }
      } else {
         this.dictionary.addWord(word);  
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.DictionaryBuilder#getDictionary()
    */
   public Dictionary getDictionary() {
      return this.dictionary;
   }

}
