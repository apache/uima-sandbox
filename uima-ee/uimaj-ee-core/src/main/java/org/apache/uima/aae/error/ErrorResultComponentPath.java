package org.apache.uima.aae.error;

import java.io.Serializable;
import java.util.Iterator;

public interface ErrorResultComponentPath extends Serializable
{
	public Iterator iterator();
	public void add( String aPath );
	public void add( String aPath, int anInsertPosition);
	public String get( int aPosition );
	
}
