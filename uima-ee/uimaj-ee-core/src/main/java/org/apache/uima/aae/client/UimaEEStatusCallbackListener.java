package org.apache.uima.aae.client;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;

/**
 * 
 * Interface for a Listener that receives notification from the {@link UimaAsynchronousEngine} as 
 * various events occur. 
 * 
 */
public interface UimaEEStatusCallbackListener
{
	 /**
	   * The callback used to inform the application that the initialization request has completed. 
	   * On success aStatus will be null; on failure use the EntityProcessStatus class to get the details.
	   * 
	   * @param aStatus
	   *          the status of the processing. This object contains a record of any Exception that
	   *          occurred, as well as timing information.
	   */
	public void initializationComplete(EntityProcessStatus aStatus);
	 /**
	   * Called when the processing of each entity has completed.
	   * 
	   * @param aCas
	   *          the CAS containing the processed entity and the analysis results
	   * @param aStatus
	   *          the status of the processing. This object contains a record of any Exception that
	   *          occurred, as well as timing information.
	   */
	public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus);
	  /**
	   * The callback used to inform the application that the CPC request has completed. 
	   * On success aStatus will be null; on failure use the EntityProcessStatus class to get the details.
	   * 
	   * @param aStatus
	   *          the status of the processing. This object contains a record of any Exception that
	   *          occurred, as well as timing information.
	   */
	public void collectionProcessComplete(EntityProcessStatus aStatus);
}
