package org.apache.uima.adapter.jms.client;

import java.net.URI;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.springframework.context.ApplicationContext;

public interface UIMAAsynchronousEngine
{
	public void initialize( String[] configFiles ) throws ResourceInitializationException;
	public void addListener( StatusCallbackListener aListener);
	public void sendCAS( CAS aCAS ) throws ResourceProcessException;
	public CAS getCAS() throws Exception;
	public void process( URI aCollection ) throws ResourceProcessException;
}
