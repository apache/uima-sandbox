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
package org.apache.uima.annotator.dict_annot;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.uima.annotator.dict_annot.impl.DictionaryAnnotatorProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.test.junit_extension.AnnotatorTester;
import org.apache.uima.test.junit_extension.JUnitExtension;

/**
 * Dictionary annotator test. This test class tests the dictionary annotator
 * code and does not focus on dictionary matching.
 */
public class DictionaryAnnotatorTest extends TestCase {

   /**
    * Test the dictionary annotator processing.
    * 
    * @throws Exception
    */
   public void testDictionaryAnnotator() throws Exception {

      // create annotation tester with the dictionary annotator specifier
      AnnotatorTester annotTester = new AnnotatorTester(JUnitExtension
            .getFile("DictionaryAnnotatorTests/DictionaryAnnotator.xml"));
      CAS cas = annotTester.performTest("nEw yOrk City", "en");

      // define result interested in
      String[] tofs = { "org.apache.uima.DictionaryEntry" };

      // compare results
      File outputFile = new File(JUnitExtension
            .getFile("DictionaryAnnotatorTests"),
            "dictionaryAnnotTest_testoutput.txt");
      AnnotatorTester.checkResult(cas, tofs, JUnitExtension
            .getFile("DictionaryAnnotatorTests/dictionaryAnnotTestRef.txt"),
            outputFile);
   }

   /**
    * The test dictionary annotator processing with an invalid input type
    * 
    * @throws Exception
    */
   public void testDictionaryAnnotatorInvalidInputType() throws Exception {

      // create annotation tester with the dictionary annotator specifier
      AnnotatorTester annotTester = new AnnotatorTester(
            JUnitExtension
                  .getFile("DictionaryAnnotatorTests/DictionaryAnnotatorInvalidInputType.xml"));

      boolean foundMessage = false;
      try {
         annotTester.performTest("Dummy text", "en");
      } catch (DictionaryAnnotatorProcessException ex) {
         String message = ex.getMessage();
         if (message.indexOf("org.apache.uima.InvalidInputType") > -1) {
            foundMessage = true;
         }
      }
      Assert.assertTrue(foundMessage);
   }

   /**
    * The test dictionary annotator processing with an invalid output type
    * 
    * @throws Exception
    */
   public void testDictionaryAnnotatorInvalidOutputType() throws Exception {

      // create annotation tester with the dictionary annotator specifier
      AnnotatorTester annotTester = new AnnotatorTester(
            JUnitExtension
                  .getFile("DictionaryAnnotatorTests/DictionaryAnnotatorInvalidOutputType.xml"));

      boolean foundMessage = false;
      try {
         annotTester.performTest("Dummy text", "en");
      } catch (DictionaryAnnotatorProcessException ex) {
         String message = ex.getMessage();
         if (message.indexOf("org.apache.uima.DictionaryEntry") > -1) {
            foundMessage = true;
         }
      }
      Assert.assertTrue(foundMessage);
   }

   /**
    * The test dictionary annotator processing - dictionary file not found
    * 
    * @throws Exception
    */
   public void testDictionaryAnnotatorDictFileNotFound() throws Exception {

      boolean foundMessage = false;
      try {
         // create annotation tester with the dictionary annotator specifier
         new AnnotatorTester(
               JUnitExtension
                     .getFile("DictionaryAnnotatorTests/DictionaryAnnotatorDictFileNotFound.xml"));
      } catch (ResourceInitializationException ex) {
         String message = ex.getCause().getMessage();
         if (message.indexOf("fileNotFound.xml") > -1) {
            foundMessage = true;
         }
      }
      Assert.assertTrue(foundMessage);

   }

}
