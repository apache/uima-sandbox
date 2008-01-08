package org.apache.uima.aae.jmx;

import java.util.ArrayList;
import java.util.List;

public class ServiceErrors implements ServiceErrorsMBean
{
	private static final long serialVersionUID = 1L;
	private static final String label="Service Errors";
	private static final int MAX_ERROR_COUNT = 5;
	
	private long processErrors = 0;
	private long metadataErrrors = 0;
	private long cpcErrors = 0;
	private String[] errors = new String[] {" ", " "," "," "," "};
    private List throwables = new ArrayList();
    
	public String getLabel()
	{
		return label;
	}

	public synchronized void addError( Throwable aThrowable)
	{
		
	}
	public String[] getErrors()
	{
		return errors;
	}
	
	public long getProcessErrors()
	{
		return processErrors;
	}
	
	public synchronized void incrementProcessErrors()
	{
		processErrors++;
	}
	
	public long getMetadataErrors()
	{
		return metadataErrrors;
	}
	public synchronized void incrementMetadataErrors()
	{
		metadataErrrors++;
	}
	
	public long getCpcErrors()
	{
		return cpcErrors;
	}
	
	public synchronized void incrementCpcErrors()
	{
		cpcErrors++;
	}
	
	public void reset()
	{
		cpcErrors = 0;
		metadataErrrors = 0;
		processErrors = 0;
	}

}
