package org.apache.uima.aae.controller;

public interface DelegateManager
{
	public void disableDelegate( String aDelegateName );
	public void enableDelegate( String aDelegateName );
	public void skipDelegate( String aDelegateName, String aCasReferenceId );

	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
