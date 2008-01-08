package org.apache.uima.ae.noop;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class NoOpAnnotator extends CasAnnotator_ImplBase
{
	private long counter = 1;
	int errorFrequency = 0;
	int cpcDelay = 1;
	int processDelay = 0;
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException
	{
		super.initialize(aContext);
		
		if ( getContext().getConfigParameterValue("ErrorFrequency") != null )
		{
			errorFrequency = ((Integer)getContext().getConfigParameterValue("ErrorFrequency")).intValue();
		}
		if ( getContext().getConfigParameterValue("CpCDelay") != null )
		{
			cpcDelay = ((Integer)getContext().getConfigParameterValue("CpCDelay")).intValue();
		}

		if ( getContext().getConfigParameterValue("ProcessDelay") != null )
		{
			processDelay = ((Integer)getContext().getConfigParameterValue("ProcessDelay")).intValue();
			System.out.println("NoOpAnnotator.initialize() Initializing With Process Delay of " +processDelay +" millis");

		}
		// write log messages
		Logger logger = getContext().getLogger();
		logger.log(Level.CONFIG, "NAnnotator initialized");
	}

	public void typeSystemInit(TypeSystem aTypeSystem) throws AnalysisEngineProcessException
	{

	}
	public void collectionProcessComplete() throws AnalysisEngineProcessException
	{
		System.out.println("NoOpAnnotator.collectionProcessComplete() Called -------------------------------------");
/*
		synchronized( this )
		{
			try
			{
				wait(cpcDelay);
			}
			catch( InterruptedException e) {}
		}
*/		
	//	System.out.println("NoOpAnnotator.collectionProcessComplete()Throwing Exception  -------------------------------------");
	//	throw new AnalysisEngineProcessException(new NullPointerException());
	}
	public void process(CAS aCAS) throws AnalysisEngineProcessException
	{
		try
		{
			//UimaClassFactory.someMethod();
      if ( processDelay == 0 ) {
			System.out.println("NoOpAnnotator.process() called for the " + counter + "th time.");
      }
			//System.out.println("NoOpAnnotator Received Cas with Text::"+aCAS.getDocumentText());
//			throw new IndexOutOfBoundsException();
      else {
				System.out.println("NoOpAnnotator.process() called for the " + counter + "th time, delaying Response For:" +processDelay +" millis");
				synchronized( this )
				{
					try
					{
						wait(processDelay);
					}
					catch( InterruptedException e) {}
				}
			}

			if ( errorFrequency > 0 )
			{
				if ( counter > 0 && counter % errorFrequency == 0)
				{
					System.out.println("Generating OutOfBoundsException");
					throw new IndexOutOfBoundsException();
				}
			}
		}
		catch ( Exception e)
		{
			throw new AnalysisEngineProcessException(e);
		}
		finally
		{
			counter++;
		}
	}

}
