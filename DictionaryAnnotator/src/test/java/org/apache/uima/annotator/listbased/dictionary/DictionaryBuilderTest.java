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
package org.apache.uima.annotator.listbased.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.uima.annotator.dict_annot.dictionary.DictionaryBuilder;
import org.apache.uima.annotator.dict_annot.dictionary.impl.HashMapDictionary;
import org.apache.uima.annotator.dict_annot.dictionary.impl.HashMapDictionaryBuilder;
import org.apache.uima.test.junit_extension.JUnitExtension;

/**
 * The DictionaryBuilderTest class tests the dictionary creation for single and
 * multi word entries.
 */
public class DictionaryBuilderTest extends TestCase {

   /**
    * test single word dictionary creation with case normalization
    * 
    * @throws Exception
    */
   public void testSingleWordDictionaryBuildingCaseNormalization()
         throws Exception {

      // read input file
      File dictFile = JUnitExtension
            .getFile("DictionaryBuilderTests/SingleWords.txt");
      // create dictionary
      DictionaryBuilder dictBuilder = new HashMapDictionaryBuilder(true, false);
      dictBuilder.createDictionary(dictFile, "UTF-8");
      HashMapDictionary dict = (HashMapDictionary) dictBuilder.getDictionary();

      BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(dictFile), "UTF-8"));
      String line = reader.readLine();

      while (line != null) {
         Assert.assertTrue("Missing in dictionary: " + line, dict
               .contains(line));
         line = reader.readLine();
      }

      reader.close();
      
      Assert.assertTrue(dict.contains("CpE"));
      Assert.assertTrue(dict.contains("CPE"));
      Assert.assertTrue(dict.contains("cpe"));
      Assert.assertEquals(2761, dict.getEntryCount());
   }

   /**
    * test single word dictionary creation without case normalization
    * 
    * @throws Exception
    */
   public void testSingleWordDictionaryBuildingNoCaseNormalization()
         throws Exception {

      // read input file
      File dictFile = JUnitExtension
            .getFile("DictionaryBuilderTests/SingleWords.txt");
      // create dictionary
      DictionaryBuilder dictBuilder = new HashMapDictionaryBuilder(false, false);
      dictBuilder.createDictionary(dictFile, "UTF-8");
      HashMapDictionary dict = (HashMapDictionary) dictBuilder.getDictionary();

      BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(dictFile), "UTF-8"));
      String line = reader.readLine();

      while (line != null) {
         Assert.assertTrue("Missing in dictionary: " + line, dict
               .contains(line));
         line = reader.readLine();
      }
      
      reader.close();

      Assert.assertFalse(dict.contains("CpE"));
      Assert.assertTrue(dict.contains("CPE"));
      Assert.assertTrue(dict.contains("cpe"));
      Assert.assertEquals(3337, dict.getEntryCount());
   }

   /**
    * test multi word dictionary creation without case normalization
    * 
    * @throws Exception
    */
   public void testMultiWordDictionaryBuildingNoCaseNormalization()
         throws Exception {

      // read input file
      File dictFile = JUnitExtension
            .getFile("DictionaryBuilderTests/MultiWords.txt");
      // create dictionary
      DictionaryBuilder dictBuilder = new HashMapDictionaryBuilder(false, true);
      dictBuilder.createDictionary(dictFile, "UTF-8");
      HashMapDictionary dict = (HashMapDictionary) dictBuilder.getDictionary();

      BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(dictFile), "UTF-8"));
      String line = reader.readLine();

      while (line != null) {

         StringTokenizer tokenizer = new StringTokenizer(line, " ");

         ArrayList list = new ArrayList();
         while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
         }
         String[] multiWord = (String[]) list.toArray(new String[] {});

         Assert.assertTrue("Missing in dictionary: " + line, dict
               .contains(multiWord));
         line = reader.readLine();
      }

      reader.close();
      
      Assert.assertEquals(5, dict.getEntryCount());
      Assert.assertFalse(dict.contains(new String[] { "Unstructured",
            "Information", "Management", "Architecture" }));
      Assert.assertTrue(dict.contains(new String[] { "new", "York" }));
      Assert.assertTrue(dict.contains(new String[] { "new", "York", "City" }));
   }

   /**
    * test multi word dictionary creation with case normalization
    * 
    * @throws Exception
    */
   public void testMultiWordDictionaryBuildingCaseNormalization()
         throws Exception {

      // read input file
      File dictFile = JUnitExtension
            .getFile("DictionaryBuilderTests/MultiWords.txt");
      // create dictionary
      DictionaryBuilder dictBuilder = new HashMapDictionaryBuilder(true, true);
      dictBuilder.createDictionary(dictFile, "UTF-8");
      HashMapDictionary dict = (HashMapDictionary) dictBuilder.getDictionary();

      BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(dictFile), "UTF-8"));
      String line = reader.readLine();

      while (line != null) {

         StringTokenizer tokenizer = new StringTokenizer(line, " ");

         ArrayList list = new ArrayList();
         while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
         }
         String[] multiWord = (String[]) list.toArray(new String[] {});

         Assert.assertTrue("Missing in dictionary: " + line, dict
               .contains(multiWord));
         line = reader.readLine();
      }

      reader.close();
      Assert.assertEquals(4, dict.getEntryCount());
      Assert.assertFalse(dict.contains(new String[] { "Unstructured",
            "Information", "Management", "Architecture" }));
      Assert.assertTrue(dict.contains(new String[] { "new", "yORk" }));
      Assert.assertTrue(dict.contains(new String[] { "new", "york", "city" }));
      Assert.assertFalse(dict.contains("new"));
   }

}
