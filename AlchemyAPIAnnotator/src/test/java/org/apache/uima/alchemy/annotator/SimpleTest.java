package org.apache.uima.alchemy.annotator;

import static org.junit.Assert.fail;

import org.apache.uima.alchemy.utils.TestUtils;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.junit.Test;

/**
 * 
 * Test case for simple actions
 *
 */
public class SimpleTest {

  /**
   * test reconfiguration of an existing AE
   */
  @Test
  public void reconfigureTest() {
    try {
      AnalysisEngine ae = TestUtils.getAE("desc/TextCategorizationAEDescriptor.xml");
      ae.reconfigure();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getLocalizedMessage());
    }
  }

}
