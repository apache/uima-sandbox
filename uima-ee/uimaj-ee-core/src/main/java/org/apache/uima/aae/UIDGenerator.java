package org.apache.uima.aae;

import java.rmi.server.UID;

public class UIDGenerator implements UniqueIdGenerator
{
	public String nextId()
	{
		return new UID().toString();
	}



}
