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
package org.apache.uima.annotator.regex;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.test.junit_extension.AnnotatorTester;
import org.apache.uima.test.junit_extension.JUnitExtension;

/**
 * Test class to test the concept file syntax.
 */
public class TestConceptFileSyntax extends TestCase {

   public void testSyntaxDuplicateNames() throws Exception {
      // test annotator configuration with
      // - duplicate concept names
      // - duplicate rule IDs within the same concept
      // - duplicate annotation IDs within the concept file document but not
      // within the same concept
      new AnnotatorTester(JUnitExtension
            .getFile("conceptSyntax/RegExAnnotDuplicateNames.xml"));
   }

   public void testSyntaxDuplicateAnnotationIds() throws Exception {
      // test annotator configuration with
      // - duplicate annotation IDs within the same concept
      try {
         new AnnotatorTester(JUnitExtension
               .getFile("conceptSyntax/RegExAnnotDuplicateAnnotId.xml"));
      } catch (ResourceInitializationException ex) {
         String message = ex.getCause().getLocalizedMessage();
         Assert
               .assertTrue(
                     "exception does not contain the duplicate annotation id: testannotation",
                     message.indexOf("testannotation") > 0);
      }
   }

   public void testSyntaxReferenceAnnotationIdNotAvaliable() throws Exception {
      // test annotator configuration with
      // - duplicate annotation IDs within the same concept
      try {
         new AnnotatorTester(JUnitExtension
               .getFile("conceptSyntax/RegExAnnotReferenceFeature.xml"));
      } catch (ResourceInitializationException ex) {
         String message = ex.getCause().getLocalizedMessage();
         Assert.assertTrue(
               "exception does not contain missing annotation id: refAnnotID",
               message.indexOf("refAnnotID") > 0);
      }
   }

   public void testSyntaxReferenceAnnotationIdError() throws Exception {
      // test annotator configuration with
      // a missing annotation id for the annotation that has - duplicate
      // annotation IDs within the same concept
      try {
         new AnnotatorTester(JUnitExtension
               .getFile("conceptSyntax/RegExAnnotReferenceFeatureError.xml"));
      } catch (ResourceInitializationException ex) {
         String message = ex.getCause().getLocalizedMessage();
         Assert.assertTrue(
                "exception does not contain the concept name: referenceFeatureValueError",
                message.indexOf("referenceFeatureValueError") > 0);
      }
   }

}
