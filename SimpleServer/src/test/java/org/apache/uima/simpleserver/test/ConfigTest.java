package org.apache.uima.simpleserver.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.SimpleServerException;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.uima.simpleserver.config.impl.XmlConfigReader;
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
      System.out.println("Short description: " + spec.getShortDescription());
      List<TypeMap> types = spec.getTypeSpecs();
      for (int i = 0; i < types.size(); i++) {
        TypeMap type = types.get(i);
        System.out.println("Type: " + type.getTypeName() + ", " + type.getOutputTag());
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

}
