package org.apache.uima.aae;

import java.io.Serializable;

public interface InProcessCacheMBean extends Serializable
{
	public String getName();
	
	public void setName( String aName );
	
	public void reset();
	
	public int getSize();
	
	public void setSize( int i);
}
