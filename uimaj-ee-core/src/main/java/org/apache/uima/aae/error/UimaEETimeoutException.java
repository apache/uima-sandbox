package org.apache.uima.aae.error;

public class UimaEETimeoutException extends Exception
{
	public UimaEETimeoutException()
	{
	}

	public UimaEETimeoutException(String message)
	{
		super(message);
	}

	public UimaEETimeoutException(Throwable cause)
	{
		super(cause);
	}

	public UimaEETimeoutException(String message, Throwable cause)
	{
		super(message, cause);
	}


}
