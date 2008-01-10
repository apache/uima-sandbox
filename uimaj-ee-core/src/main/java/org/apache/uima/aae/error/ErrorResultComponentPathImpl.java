package org.apache.uima.aae.error;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorResultComponentPathImpl implements ErrorResultComponentPath
{
	private static final long serialVersionUID = 100262915217554060L;

	private List pathList = new ArrayList();
	
	public void add(String aPath)
	{
		this.add(aPath, pathList.size());
	}

	public void add(String aPath, int anInsertPosition)
	{
		pathList.add(anInsertPosition, aPath);
	}

	public String get(int aPosition)
	{
		if ( aPosition >= 0 && aPosition <= pathList.size())
		{
			return (String)pathList.get(aPosition);
		}
		return null;
	}

	public Iterator iterator()
	{
		return pathList.iterator();
	}
}
