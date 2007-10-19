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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import noNamespace.DictionaryDocument;
import noNamespace.EntriesDocument.Entries;
import noNamespace.EntryDocument.Entry;
import noNamespace.TypeCollectionDocument.TypeCollection;

import org.apache.uima.annotator.dict_annot.dictionary.Dictionary;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder;
import org.apache.uima.annotator.dict_annot.dictionary.DictionaryFileParser;
import org.apache.uima.annotator.dict_annot.impl.DictionaryAnnotatorConfigException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.xmlbeans.XmlOptions;

/**
 * The DictionaryFileParser implementation parse the dictionary XML file and
 * creates a dictionary with the given dictionary builder.
 */
public class DictionaryFileParserImpl implements DictionaryFileParser {

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.uima.annotator.listbased.dictionary.DictionaryFileParser#parseDictionaryFile(java.io.File,
    *      org.apache.uima.annotator.listbased.dictionary.DictionaryBuilder)
    */
   public Dictionary parseDictionaryFile(File dictionaryFile,
         DictionaryBuilder dictBuilder) throws ResourceInitializationException {

      // parse the dictionary file and extract the content
      DictionaryDocument dictionaryDoc;
      try {
         dictionaryDoc = DictionaryDocument.Factory.parse(dictionaryFile);
      } catch (Exception ex) {
         throw new DictionaryAnnotatorConfigException(
               "listbased_annotator_error_parsing_dictionary_file",
               new Object[] { dictionaryFile.getAbsolutePath() }, ex);
      }

      // validate input file
      ArrayList validationErrors = new ArrayList();
      XmlOptions validationOptions = new XmlOptions();
      validationOptions.setErrorListener(validationErrors);

      boolean isValid = dictionaryDoc.validate(validationOptions);

      // output the errors if the XML is invalid.
      if (!isValid) {
         Iterator iter = validationErrors.iterator();
         StringBuffer errorMessages = new StringBuffer();
         while (iter.hasNext()) {
            errorMessages.append("\n>> ");
            errorMessages.append(iter.next());
         }
         throw new DictionaryAnnotatorConfigException(
               "listbased_annotator_error_xml_validation",
               new Object[] { dictionaryFile.getAbsolutePath(),
                     errorMessages.toString() });
      }

      // ***************************************************
      // get the concepts from the concept file document
      // ***************************************************
      noNamespace.DictionaryDocument.Dictionary dictionary = dictionaryDoc
            .getDictionary();

      TypeCollection typeCollection = dictionary.getTypeCollection();

      // get the dictionary language and the type name
      String language = typeCollection.getLanguageId();
      String typeName = typeCollection.getTypeDescription().getTypeName();
      // set dictionary properties
      dictBuilder.setDictionaryProperties(language, typeName);

      // get dictionary entries and add process them with the dictionary builder
      Entries entries = typeCollection.getEntries();
      Entry[] entryArray = entries.getEntryArray();
      for (int i = 0; i < entryArray.length; i++) {
         dictBuilder.addWord(entryArray[i].getKey().getStringValue());
      }

      // get dictionary and return it
      return dictBuilder.getDictionary();
   }

}
