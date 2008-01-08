package org.apache.uima.aae.controller;

import org.apache.uima.aae.jmx.PrimitiveServiceInfo;
import org.apache.uima.aae.jmx.ServiceInfo;


public interface PrimitiveAnalysisEngineController extends AnalysisEngineController
{
	public boolean isMultiplier();
	public void releaseNextCas();
	public void setAnalysisEngineInstancePool( AnalysisEngineInstancePool aPool);
	public PrimitiveServiceInfo getServiceInfo();
	public void addAbortedCasReferenceId( String aCasReferenceId );
	
  
}
