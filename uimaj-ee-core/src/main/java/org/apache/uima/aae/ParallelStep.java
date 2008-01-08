package org.apache.uima.aae;

import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.Step;

public class ParallelStep extends Step 
{
	  public ParallelStep(String[] aCasProcessorKeys) {
		    mKeys = aCasProcessorKeys;
		  }

		  /**
		   * Gets the key of the Analysis Engine to which the CAS should be routed.
		   * 
		   * @return an AnalysisEngine key
		   */
		  public String[] getAnalysisEngineKeys() {
		    return mKeys;
		  }

		  /**
		   * Sets the key of the Analysis Engine to which the CAS should be routed. By using this method, a
		   * user's Flow implementation can (but is not required to) reuse the same SimpleStep object
		   * multiple times.
		   * 
		   * @return an Analysis Engine key. This must be one of the keys in the FlowController's
		   *         {@link FlowControllerContext#getAnalysisEngineMetaDataMap()}.
		   */
		  public void setAnalysisEngineKeys(String[] aKeys) {
		    mKeys = aKeys;
		  }

		  private String[] mKeys;



}
