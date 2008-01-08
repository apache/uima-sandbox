package org.apache.uima.aae.error;

import java.util.HashMap;
import java.util.Iterator;

public class ErrorContext
{
	public static final String THROWABLE_ERROR="ThrowableError";
	private HashMap contextMap = new HashMap();
	
	public void add( HashMap aCtx)
	{
		contextMap.putAll(aCtx);
	}
	public void add( Object key, Object value )
	{
		contextMap.put(key, value);
	}
	
	public Object get( String key )
	{
		if ( contextMap.containsKey(key))
		{
			return contextMap.get(key);
		}
		return null;
	}
	public boolean containsKey( String key )
	{
		return contextMap.containsKey(key);
	}
	public Iterator getIterator()
	{
		return contextMap.keySet().iterator();
	}


}
