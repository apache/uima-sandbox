package org.apache.uima.aae.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.uima.UimaContext;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.jmx.AggregateServiceInfo;
import org.apache.uima.aae.jmx.PrimitiveServiceInfo;
import org.apache.uima.aae.jmx.ServiceErrors;
import org.apache.uima.aae.jmx.ServiceInfo;
import org.apache.uima.aae.jmx.ServicePerformance;

public interface AggregateAnalysisEngineController extends AnalysisEngineController
{
	public void mergeTypeSystem(String aTypeSystem, String fromDestination) throws AsynchAEException;

	public void sendRequestForMetadataToRemoteDelegates() throws AsynchAEException;
	
	public void addMessageOrigin( String aCasReferenceId, Endpoint anEndpoint );

	public Endpoint getMessageOrigin( String aCasReferenceId );
	
	public void removeMessageOrigin( String aCasReferenceId );
	
	public void processCollectionCompleteReplyFromDelegate( String aDelegateKey, boolean sendReply ) throws AsynchAEException;
	
	public boolean isDelegateKeyValid(String anEndpointName);
	
	public void mapEndpointsToKeys(ConcurrentHashMap aDestinationMap);
	
	public Endpoint lookUpEndpoint(String anAnalysisEngineKey, boolean clone) throws AsynchAEException;

	public void dispatchMetadataRequest(Endpoint anEndpoint) throws AsynchAEException;

	public void retryLastCommand( int aCommand, Endpoint anEndpoint, String aCasReferenceId );

	public void retryMetadataRequest( Endpoint anEndpoint) throws AsynchAEException;
	
	public String lookUpDelegateKey( String aDelegateEndpointName );
	
	public UimaContext getChildUimaContext( String aDelegateEndpointName ) throws Exception;
	
//	public void retryCAS( String aCasReferenceId, Endpoint anEndpoint )throws AsynchAEException;

	public void retryProcessCASRequest( String aCasReferenceId, Endpoint anEndpoint, boolean addEndpointToCache ) throws AsynchAEException;
	
	public void enableDelegates( List aDelegateList ) throws AsynchAEException;
	
	public void disableDelegates( List aDelegateList ) throws AsynchAEException;

	public boolean continueOnError(String aCasReferenceId, String aDelegateKey, Exception anException )throws AsynchAEException;

	public void dropFlow( String aCasReferenceId, boolean dropFlow );
	
	public boolean isDelegateDisabled( String aDelegateKey );

	
	public String getLastDelegateKeyFromFlow(String anInputCasReferenceId);

	public boolean sendRequestToReleaseCas();
	
	public void registerChildController( AnalysisEngineController aChildController, String aDelegateKey) throws Exception;

	public void saveStatsFromService( String aServiceEndpointName, Map aServiceStats);

	public Map getDelegateStats();
	
	public AggregateServiceInfo getServiceInfo();

	public ServicePerformance getDelegateServicePerformance( String aDelegateKey );

    public PrimitiveServiceInfo getDelegateServiceInfo( String aDelegateKey );

    public ServiceErrors getDelegateServiceErrors( String aDelegateKey );

    public void incrementCasProcessedByDelegate(String aDelegateKey);
    
    public void incrementCasSerializationTime( String aDelegateKey,long aSerializationTime );
    
    public void incrementCasDeserializationTime( String aDelegateKey, long aDeserializationTime );
    
    public void incrementDelegateIdleTime(String aDelegateKey, long anIdleTime);

	public void stopTimers();

	
	public boolean requestForMetaSentToRemotes();
	
	public void setRequestForMetaSentToRemotes();
	
	public Map getDestinations();

	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
