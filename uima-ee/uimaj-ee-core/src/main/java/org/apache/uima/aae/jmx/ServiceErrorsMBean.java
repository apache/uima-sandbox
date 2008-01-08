package org.apache.uima.aae.jmx;

import java.io.Serializable;

public interface ServiceErrorsMBean extends Serializable
{
	public String[] getErrors();
	public long getProcessErrors();
	public long getMetadataErrors();
	public long getCpcErrors();
	
	public void reset();

}
