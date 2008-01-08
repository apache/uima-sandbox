package org.apache.uima.aae;

import java.util.Map;
import java.util.Properties;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.CasManager;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;

public interface AsynchAECasManager 
{
	public void addMetadata(ProcessingResourceMetaData meta);
	public ProcessingResourceMetaData getMetadata() throws ResourceInitializationException;
	public Map getMetadataAsMap() throws ResourceInitializationException;
	public void setMetadata(ProcessingResourceMetaData meta);
	public void initialize(  int aCasPoolSize, String aContext) throws Exception;
	public void initialize( String aContext) throws Exception;
	public void initialize(int aCasPoolSize, String aContextName, Properties aPerformanceTuningSettings) throws Exception;
	public CAS getNewCas();
	public CAS getNewCas(String aContext);
	public CasManager getInternalCasManager();
	public boolean isInitialized();
	public void setInitialized(boolean initialized);
	public String getCasManagerContext();
	public ResourceManager getResourceManager();
	public void setInitialFsHeapSize( long aSizeInBytes);
	public long getInitialFsHeapSize();
	
	static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
