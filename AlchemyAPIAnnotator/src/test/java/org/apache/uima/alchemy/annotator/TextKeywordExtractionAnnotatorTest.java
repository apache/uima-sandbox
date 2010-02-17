/**
 * 	Licensed to the Apache Software Foundation (ASF) under one
 * 	or more contributor license agreements.  See the NOTICE file
 * 	distributed with this work for additional information
 * 	regarding copyright ownership.  The ASF licenses this file
 * 	to you under the Apache License, Version 2.0 (the
 * 	"License"); you may not use this file except in compliance
 * 	with the License.  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing,
 * 	software distributed under the License is distributed on an
 * 	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * 	KIND, either express or implied.  See the License for the
 * 	specific language governing permissions and limitations
 * 	under the License.
 */
package org.apache.uima.alchemy.annotator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.uima.alchemy.ts.keywords.KeywordFS;
import org.apache.uima.alchemy.utils.TestUtils;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

public class TextKeywordExtractionAnnotatorTest {

  @Test
  public void testAnnotator() {
    String doc = "In an effort to centralize developer resources and help newcomers to open source, the Apache Software Foundation recently launched the Community Developer project.  So far, the project includes plans for a mentoring program, a \"Newbie FAQ\", and resources for educators.  To find out how the project started and where it's going, DZone conducted an exclusive interview with Bertrand Delacretaz, an Apache member who has been with the project since the beginning.  He says some of the major goals for the project include mentoring for student developers and an entry point to the foundation at-large.";
    String xmlPath = "desc/TextKeywordExtractionAEDescriptor.xml";
    try {
      JCas resultingCAS = TestUtils.executeAE(TestUtils.getAE(xmlPath), doc);
      List<KeywordFS> keywords = (List<KeywordFS>) TestUtils.getAllFSofType(KeywordFS.type, resultingCAS);
      assertTrue(keywords.size()>0);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

}
