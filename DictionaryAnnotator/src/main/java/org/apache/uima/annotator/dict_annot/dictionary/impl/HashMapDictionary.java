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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.annotator.dict_annot.dictionary.Dictionary;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryMatch;
import org.apache.uima.annotator.dict_annot.dictionary.EntryMetaData;
import org.apache.uima.cas.text.AnnotationFS;

/**
 * HashMap dictionary implementation.
 * 
 * TODO: describe how the dictionary is implemented
 * 
 */
public class HashMapDictionary implements Dictionary {

   // main dictionary HashMap
   private HashMap dictionary = null;

   private int idCounter;

   private String language;

   private String typeName;

   private boolean caseNormalization = true;

   /**
    * Creates a new HashMapDictionary object with an initial capacity of 100
    * entries.
    */
   public HashMapDictionary(boolean caseNormalization) {
      this.dictionary = new HashMap(100);
      this.idCounter = 0;
      this.caseNormalization = caseNormalization;
      this.language = null;
      this.typeName = null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.Dictionary#contains(java.lang.String)
    */
   public boolean contains(String word) {
      // check if the given word is available in the dictionary
      DictionaryEntry entry = (DictionaryEntry) this.dictionary
            .get(normalizeString(word));
      if (entry != null) {
         return entry.isComplete();
      } else {
         return false;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.Dictionary#contains(java.lang.String[])
    */
   public boolean contains(String[] multiWord) {

      DictionaryEntry entry = containsMultiWord(multiWord);
      if (entry != null) {
         return entry.isComplete();
      } else {
         return false;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.Dictionary#getTypeName()
    */
   public String getTypeName() {
      return this.typeName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.Dictionary#getLanguage()
    */
   public String getLanguage() {
      return this.language;
   }

   /**
    * check if the given multi word is available in the dictionary. If it is
    * available the DictionaryEntry is returned. If it is not available, null is
    * returned
    * 
    * @param multiWord
    *           multi word to look for
    * 
    * @return DictionaryEntry for the given multi word, or null if the entry is
    *         not in the dictionary
    */
   private DictionaryEntry containsMultiWord(String[] multiWord) {
      HashMap currentMap = this.dictionary;
      DictionaryEntry entry = null;

      // iterate over all multi word tokens and check if the multi word is
      // available
      for (int i = 0; i < multiWord.length; i++) {
         // check token
         entry = (DictionaryEntry) currentMap
               .get(normalizeString(multiWord[i]));
         if (entry == null) {
            // multi word is not available
            return null;
         } else {
            // use sub branch to lookup the next token
            currentMap = entry.getSubBranch();
         }
      }
      return entry;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.Dictionary#matchEntry(int,
    *      java.lang.String[])
    */
   public DictionaryMatch matchEntry(int pos, AnnotationFS[] annotFSs) {

      // create a dictionary match object
      DictionaryMatchImpl match = new DictionaryMatchImpl();
      int offset = 0;

      // check dictionary matches
      checkMatches(pos, annotFSs, this.dictionary, match, offset);

      // check if a match was found that is valid
      if (match.isValidMatch()) {
         // valid match found, return the match
         return match;
      } else {
         // no valid match found
         return null;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.Dictionary#getEntryCount()
    */
   public int getEntryCount() {
      return this.idCounter;
   }

   /**
    * Adds a new word to the dictionary.
    * 
    * @param word
    *           word that should be added to the dictionary
    * 
    * @return ID that was generated for this entry
    */
   public int addWord(String word) {

      // check if the word is already available in the dictionary
      DictionaryEntry entry = (DictionaryEntry) this.dictionary
            .get(normalizeString(word));
      if (entry != null) {
         // check if entry is marked as complete
         if (entry.isComplete()) {
            return -1;
         } else {
            // set valid word
            entry.setComplete();

            // update entry meta data
            this.idCounter++;
            EntryMetaData metaData = new EntryMetaDataImpl(this.idCounter);
            entry.setEntryMetaData(metaData);
            return this.idCounter;
         }
      }

      // increase ID counter
      this.idCounter++;
      // create new entry meta data object and add ID
      EntryMetaData metaData = new EntryMetaDataImpl(this.idCounter);

      // add entry to the dictionary
      this.dictionary.put(normalizeString(word), new DictionaryEntry(true,
            metaData));
      return this.idCounter;
   }

   /**
    * Adds a new multi word to the dictionary
    * 
    * @param multiWord
    *           multi word that should be added to the dictionary
    * 
    * @return ID that was generated for this entry
    */
   public int addMultiWord(String[] multiWord) {

      DictionaryEntry entry = containsMultiWord(multiWord);
      if (entry != null) {
         // check if entry is marked as complete
         if (entry.isComplete()) {
            return -1;
         } else {
            // set valid word
            entry.setComplete();
            // update entry meta data
            this.idCounter++;
            EntryMetaData metaData = new EntryMetaDataImpl(this.idCounter);
            entry.setEntryMetaData(metaData);
            return this.idCounter;
         }
      }

      // increase ID counter
      this.idCounter++;
      // create new entry meta data object and add ID
      EntryMetaData metaData = new EntryMetaDataImpl(this.idCounter);

      HashMap currentMap = this.dictionary;

      // iterate over all multi word tokens and add them to the dictionary
      for (int i = 0; i < multiWord.length; i++) {
         // check if the current token is already in the dictionary
         if (currentMap.containsKey(normalizeString(multiWord[i]))) {
            // current multi word token is already in the dictionary get the map
            // of sub tokens for this entry
            currentMap = ((DictionaryEntry) currentMap
                  .get(normalizeString(multiWord[i]))).getSubBranch();
         } else { // current multi word token is not in the dictionary
            // check how we have to set the word end property
            if (i == (multiWord.length - 1)) {
               // if it is the last token of the multi word, the word end
               // property must be set to true
               entry = new DictionaryEntry(true, metaData);
            } else {
               entry = new DictionaryEntry(false, null);
            }
            // add token to the dictionary
            currentMap.put(normalizeString(multiWord[i]), entry);
            // set map for the next sub tokens
            currentMap = entry.getSubBranch();
         }
      }

      // return generated ID
      return this.idCounter;
   }

   /**
    * print the dictionary content either to an Writer object or if not output
    * is specified to the command line.
    * 
    * @param out
    *           Writer object to write the content to
    * 
    * @throws IOException
    */
   public void printDictionary(Writer out) throws IOException {

      // start printing with the main branch
      printBranch(this.dictionary, null, out);
   }

   /**
    * set the dictionary language
    * 
    * @param language
    *           dictionary language
    */
   public void setDictionaryLanguage(String language) {
      this.language = language;
   }

   /**
    * set the dictionary type name
    * 
    * @param typeName
    *           dictionary type name
    */
   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }

   /**
    * search the first longest matches for the given input data. This method is
    * called recursive to detect multi word matches. All valid matches that are
    * found are stored so that they can be retrieved later.
    * 
    * @param pos
    *           start position in the token array
    * @param tokens
    *           token input array
    * @param dict
    *           dictionary to use
    * @param match
    *           match result object where all match data is stored
    * @param offset
    *           offset in relation to the start position of the current token
    *           that is discovered
    */
   private void checkMatches(int pos, AnnotationFS[] annotFSs, HashMap dict,
         DictionaryMatchImpl match, int offset) {

      // check if the current token that is investigated is available in the
      // current map
      if (dict.containsKey(normalizeString(annotFSs[pos + offset].getCoveredText()))) {
         // token is available in the map, get the dictionary entry object
         DictionaryEntry currentEntry = (DictionaryEntry) dict
               .get(normalizeString(annotFSs[pos + offset].getCoveredText()));
         // add match to the match object
         match.storeMatch(currentEntry.getEntryMetaData(), currentEntry
               .isComplete());

         // increase offset to investigate the next token
         offset++;
         // if further tokens are available ....
         if (annotFSs.length > pos + offset) {
            // ... investigate them
            checkMatches(pos, annotFSs, currentEntry.getSubBranch(), match,
                  offset);
         }
      }
   }

   /**
    * print out the words of a dictionary branch
    * 
    * @param map
    *           current dictionary branch
    * 
    * @param previousTokens
    *           previous tokens for the current branch
    */
   private void printBranch(HashMap map, ArrayList previousTokens, Writer out)
         throws IOException {
      // get an iterator over the main entries of this dictionary branch
      Iterator mainEntries = map.keySet().iterator();
  
      // iterate over all entries of this branch
      while (mainEntries.hasNext()) {
         // get current token
         String currentToken = (String) mainEntries.next();

         // get dictionary entry for the current token
         DictionaryEntry dictEntry = (DictionaryEntry) map.get(currentToken);

         // check if the token is a valid word entry or a part of a multi word
         // entry
         if (dictEntry.isComplete()) {
            // word end detected, print word
            if (previousTokens != null) {
               // add previous tokens of this multi word
               String previous = getString(previousTokens);
               if (out == null) {
                  System.out.println(previous + currentToken);
               } else {
                  out.write(previous + currentToken);
                  out.write(System.getProperty("line.separator"));
               }
            } else {
               // just print the single word
               if (out == null) {
                  System.out.println(currentToken);
               } else {
                  out.write(currentToken);
                  out.write(System.getProperty("line.separator"));
               }
            }
         }

         // check for the current token if it is part of a multi word
         // get the sub branch of this entry
         HashMap subBranch = dictEntry.getSubBranch();
         // add current token to previousTokens list
         if (previousTokens == null) {
            previousTokens = new ArrayList();
         }
         previousTokens.add(currentToken);
         // call printBranch for the current sub branch
         printBranch(subBranch, previousTokens, out);
         // remove current token form list, since the sub branch is processed
         // completely
         previousTokens.remove(previousTokens.size() - 1);
      }
   }

   /**
    * Converts the array list of string to a concatenated String separated by a
    * whitespace character.
    * 
    * @param stringList
    *           array list of string
    * 
    * @return concatenated String separated by whitespace characters
    */
   private String getString(ArrayList stringList) {

      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < stringList.size(); i++) {
         buf.append(stringList.get(i));
         buf.append(" ");
      }

      return buf.toString();
   }

   /**
    * Normalize the input string to lower case and remove all spaces around if
    * the dictionary is configured to do a case normalization
    * 
    * @param input
    *           input string to normalize
    * 
    * @return returns the normalized string
    */
   private String normalizeString(String input) {
      if (this.caseNormalization) {
         return input.toLowerCase().trim();
      }

      return input;
   }

}
