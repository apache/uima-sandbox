package org.apache.uima.adapter.jms.client;

public class InvalidContainerException extends Exception
{

	public InvalidContainerException()
	{
	}

	public InvalidContainerException(String message)
	{
		super(message);
	}

	public InvalidContainerException(Throwable cause)
	{
		super(cause);
	}

	public InvalidContainerException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
