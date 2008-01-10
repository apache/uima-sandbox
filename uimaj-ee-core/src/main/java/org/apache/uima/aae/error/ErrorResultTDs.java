package org.apache.uima.aae.error;

import java.io.Serializable;
import java.util.Iterator;

public interface ErrorResultTDs extends Serializable
{
	public Iterator iterator();
	public void add( TD aTD);
	public void add( TD aTD, int anInsertPosition);
	public TD get(int aPosition);
	
	public interface TD extends Serializable
	{
		public String gatPath();
		public void setTerminated();
		public void setDisabled();
		public boolean wasTerminated();
		public boolean wasDisabled();
	}
	
}
