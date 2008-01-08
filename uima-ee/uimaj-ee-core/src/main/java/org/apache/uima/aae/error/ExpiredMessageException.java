package org.apache.uima.aae.error;

public class ExpiredMessageException extends Exception
{
	public ExpiredMessageException()
	{
		super();
	}

	public ExpiredMessageException(String message)
	{
		super(message);
	}

	public ExpiredMessageException(Throwable cause)
	{
		super(cause);
	}

	public ExpiredMessageException(String message, Throwable cause)
	{
		super(message, cause);
	}



}
