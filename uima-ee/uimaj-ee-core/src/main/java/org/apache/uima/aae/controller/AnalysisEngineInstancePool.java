package org.apache.uima.aae.controller;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;

public interface AnalysisEngineInstancePool
{
	/**
	 * Creates and initializes the AE Pool with intances of AEs provided in the anAnalysisEngineInstanceList
	 * 
	 * @param anAnalysisEngineInstanceList - list of AnalysisEngine instances
	 * @throws Exception
	 */
	public void intialize( List anAnalysisEngineInstanceList ) throws Exception;
	
	/**
	 * Adds an instance of AnalysisEngine to the pool
	 * 
	 * @param anAnalysisEngine - AnalysisEngine instance to be added to the pool
	 * @throws Exception
	 */
	public void checkin( AnalysisEngine anAnalysisEngine )  throws Exception;
	
	/**
	 * Borrows an instance of AnalysisEngine from the pool
	 * 
	 * @return AnalysisEngine instance
	 * @throws Exception
	 */
	public AnalysisEngine checkout()  throws Exception;
	
	/**
	 * Destroys Analysis Engine instance pool. 
	 * 
	 * @throws Exception
	 */
	public void destroy() throws Exception;
}
