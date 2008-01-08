package org.apache.uima.aae.error;

import java.io.Serializable;

import org.apache.uima.aae.error.ErrorResultTDs.TD;

public interface ErrorResult extends Serializable
{
	public void setRootCause( Throwable aRootCause );
	
	public void addComponentKeyPath( String aComponentKeyPath );
	
	public void addComponentKeyPath(String aComponentKeyPath, boolean terminated, boolean disabled);

	public void setTerminated();
	
	public void setDisabled();

	/**
	 * Returns the underlying root cause first reported as an error
	 * @return Throwable
	 */
	public Throwable getRootCause();
	/**
	 * Returns a path consisting of a list of component key names
	 * @return
	 */
	public ErrorResultComponentPath getComponentKeyPath();
	/**
	 * Returns true is any termination occurred with this error
	 * @return
	 */
	public boolean wasTerminated();
	/**
	 * Returns true if any disabling occured with this error
	 * @return
	 */
	public boolean wasDisabled();
	/**
	 * Returns a collection of paths to the components that were terminated or disabled
	 * 
	 * @return
	 */
	public ErrorResultTDs getTDs();
	
	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
