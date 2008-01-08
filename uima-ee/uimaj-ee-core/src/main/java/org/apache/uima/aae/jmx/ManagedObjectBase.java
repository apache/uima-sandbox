package org.apache.uima.aae.jmx;

import javax.management.ObjectName;

public class ManagedObjectBase
{
	private ObjectName objectName;

	public ObjectName getObjectName()
	{
		return objectName;
	}

	public void setObjectName(ObjectName objectName)
	{
		this.objectName = objectName;
	}
}
