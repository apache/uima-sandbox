package org.apache.uima.simpleserver.test;

import static org.junit.Assert.assertTrue;

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
        TypeMap type = types.get(i);
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
    JCas cas = createTestCas();
    ServerSpec serverSpec = null;
    try {
      serverSpec = XmlConfigReader.readServerSpec(JUnitExtension
          .getFile("serverspec/spec1.xml"));
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
    assertTrue((result.getResultEntries().size() + 1) == cas.getDocumentText().length());
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
