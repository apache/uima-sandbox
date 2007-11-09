package org.apache.uima.simpleserver.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.incubator.uima.simpleserver.config.xml.ResultSpecificationDocument;
import org.apache.incubator.uima.simpleserver.config.xml.TypeElementType;
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
      ResultSpecificationDocument.ResultSpecification spec = ResultSpecificationDocument.Factory
          .parse(is).getResultSpecification();
      System.out
          .println("Short description: " + spec.getShortDescription());
      TypeElementType[] types = spec.getTypeArray();
      for (int i = 0; i < types.length; i++) {
        TypeElementType type = types[i];
        System.out.println("Type: " + type.getName() + ", " + type.getOutputTag());
      }
    } catch (XmlException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }

}
