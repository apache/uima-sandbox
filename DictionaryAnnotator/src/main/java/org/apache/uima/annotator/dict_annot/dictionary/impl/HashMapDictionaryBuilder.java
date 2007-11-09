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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.uima.annotator.dict_annot.dictionary.Dictionary;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder;

/**
 * Implementation of the DictionaryBuilder interface to build a new HashMap
 * dictionary.
 */
public class HashMapDictionaryBuilder implements DictionaryBuilder {

   // multi-word entry dictionary
   private boolean createMultiWordEntries;

   // HashMap dictionary
   private HashMapDictionary dictionary;

   /**
    * Default constructor. Creates a new HashMap dictionary with case normalization.
    */
   public HashMapDictionaryBuilder() {
      this.dictionary = new HashMapDictionary(true);
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder#setDictionaryProperties(java.lang.String,
    *      java.lang.String, boolean, boolean)
    */
   public void setDictionaryProperties(String language, String typeName,
         boolean caseNormalization, boolean multiWordEntries) {
      this.dictionary.setDictionaryLanguage(language);
      this.dictionary.setTypeName(typeName);
      this.createMultiWordEntries = multiWordEntries;
      
      //create a new dictionary if the settings changed.
      if(!caseNormalization) {
         this.dictionary = new HashMapDictionary(caseNormalization);
      }
   }

   /**
    * split up the given input in several tokens using the whitespace character
    * as delimiter.
    * 
    * @param input
    *           word that should be tokenized
    * 
    * @return Tokens for the given input
    */
   private String[] whiteSpaceTokenizer(String input) {

      // create
      StringTokenizer tokenizer = new StringTokenizer(input, " ");

      ArrayList<String> tokens = new ArrayList<String>();
      while (tokenizer.hasMoreTokens()) {
         tokens.add(tokenizer.nextToken());
      }
      String[] multiWord = tokens.toArray(new String[] {});

      return multiWord;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder#addWord(java.lang.String)
    */
   public void addWord(String word) {

      if (this.createMultiWordEntries) {
         // tokenize the entry
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
    * @see org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder#getDictionary()
    */
   public Dictionary getDictionary() {
      return this.dictionary;
   }

}
