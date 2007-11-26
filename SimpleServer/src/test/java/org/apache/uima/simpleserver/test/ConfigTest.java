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

package org.apache.uima.simpleserver.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.simpleserver.ResultExtractor;
import org.apache.uima.simpleserver.SimpleServerException;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.uima.simpleserver.config.impl.XmlConfigReader;
import org.apache.uima.simpleserver.output.Result;
import org.apache.uima.test.junit_extension.JUnitExtension;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.apache.xmlbeans.XmlException;
import org.junit.Test;

/**
 * TODO: Create type commment.
 */
public class ConfigTest {

  public static final String CONFIG_TEST_FILE = "stuff.xml";

  @Test
  public void readSampleConfig() {
    try {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_TEST_FILE);
      ServerSpec spec = XmlConfigReader.readServerSpec(is);
      List<TypeMap> types = spec.getTypeSpecs();
      for (int i = 0; i < types.size(); i++) {
        assertNotNull(types.get(i));
      }
    } catch (XmlException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (SimpleServerException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }

  @Test
  public void testResultGeneration1() {
    test("serverspec/spec1.xml", 289);    
  }
  
  @Test
  public void testResultGeneration2() {
    test("serverspec/spec2.xml", 3);
  }

  @Test
  public void testResultGeneration3() {
    test("serverspec/spec3.xml", 1);
  }

  @Test
  public void testResultGeneration4() {
    test("serverspec/spec4.xml", 0);
  }

  private static final void test(String configFile, int expectedResultNumber) {
    JCas cas = createTestCas();
    ServerSpec serverSpec = null;
    try {
      serverSpec = XmlConfigReader.readServerSpec(JUnitExtension.getFile(configFile));
    } catch (SimpleServerException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (XmlException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    List<SimpleServerException> exc = serverSpec.validate(cas.getTypeSystem());
    if (exc.size() > 0) {
      exc.get(0).printStackTrace();
      assertTrue(false);
    }
    Result result = ResultExtractor.getResult(cas.getCas(), serverSpec);
    final int resultSize = result.getResultEntries().size();
    assertTrue("Expected number of results was " + expectedResultNumber + ", actual number is "
        + resultSize, (resultSize == expectedResultNumber));
  }

  private static final JCas createTestCas() {
    XMLParser parser = UIMAFramework.getXMLParser();
    File descriptorFile = JUnitExtension.getFile("desc/simpleServerTestDescriptor.xml");
    File textFile = JUnitExtension.getFile("test.txt");
    String text = null;
    try {
      text = FileUtils.file2String(textFile, "utf-8");
    } catch (IOException e1) {
      e1.printStackTrace();
      assertTrue(false);
    }
    AnalysisEngineDescription aeDesc = null;
    try {
      aeDesc = (AnalysisEngineDescription) parser.parse(new XMLInputSource(descriptorFile));
    } catch (InvalidXMLException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    AnalysisEngine ae = null;
    try {
      ae = UIMAFramework.produceAnalysisEngine(aeDesc);
    } catch (ResourceInitializationException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    JCas cas = null;
    try {
      cas = ae.newJCas();
    } catch (ResourceInitializationException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    cas.setDocumentText(text);
    try {
      ae.process(cas);
    } catch (AnalysisEngineProcessException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    return cas;
  }

}
