package org.apache.uima.aae.controller;

/**
 * Interface defining methods to enable stopping of Asynchronous Service. 
 */
public interface ControllerLifecycle
{
	/**
	 * Called to initiate shutdown of the Asynchronous Service. An implementation
	 * can close an input and output channels and do any necessary cleanup before
	 * terminating.
	 */
	public void terminate();

	
	/**
	   * Register one or more listeners through which the controller can send
	   * notification of events. 
	   *
	   * 
	   * @param aListener - application listener object to register
	   */
	  public void addControllerCallbackListener(ControllerCallbackListener aListener);

	  
	  /**
	   * Removes named application listener. 
	   * 
	   * @param aListener - application listener to remove
	   */
	  public void removeControllerCallbackListener(ControllerCallbackListener aListener);
}
