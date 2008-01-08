package org.apache.uima.aae;

import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;

public interface OutputChannel extends Channel
{
	public void setController( AnalysisEngineController aContainer );
	
	public void initialize() throws AsynchAEException;

	public void sendRequest( String aCasReferenceId, Endpoint anEndpoint ) throws AsynchAEException;

	public void sendRequest( String aCasReferenceId, Endpoint[] anEndpoint ) throws AsynchAEException;

	public void sendRequest( int aCommand, Endpoint anEndpoint ) throws AsynchAEException;

	public void sendRequest( int aCommand, String aCasReferenceId, Endpoint anEndpoint ) throws AsynchAEException;

	public void sendReply( int aCommand, Endpoint anEndpoint ) throws AsynchAEException;

	public void sendReply( CAS aCas, String anInputCasReferenceId, String aNewCasReferenceId,  Endpoint anEndpoint, long sequence ) throws AsynchAEException;
	
	public void sendReply( String aCasReferenceId, Endpoint anEndpoint ) throws AsynchAEException;

//	public void sendReply( AnalysisEngineMetaData anAnalysisEngineMetadata, Endpoint anEndpoint, boolean serialize ) throws AsynchAEException;
	public void sendReply( ProcessingResourceMetaData aProcessingResourceMetadata, Endpoint anEndpoint, boolean serialize ) throws AsynchAEException;

	public void sendReply(Throwable t, String aCasReferenceId, Endpoint anEndpoint, int aCommand) throws AsynchAEException;
	
	
	
	public void setServerURI( String aServerURI );
	
	public void stop();
	
    static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
