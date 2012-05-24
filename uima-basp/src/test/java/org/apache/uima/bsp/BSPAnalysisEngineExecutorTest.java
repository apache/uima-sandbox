package org.apache.uima.bsp;

import org.junit.Test;

/**
 * Testcase for {@link BSPAnalysisEngineExecutor}
 */
public class BSPAnalysisEngineExecutorTest {

  @Test
  public void simpleTest() throws Exception {
    BSPAnalysisEngineExecutor bspAnalysisEngineExecutor = new BSPAnalysisEngineExecutor();
    bspAnalysisEngineExecutor.executeAE("src/test/resources/uima/SampleAE.xml", "src/test/resources/data/dev", "target/analysis-results.txt", 3);
  }

}
