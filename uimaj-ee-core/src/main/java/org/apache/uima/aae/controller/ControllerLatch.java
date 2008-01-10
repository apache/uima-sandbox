package org.apache.uima.aae.controller;

import java.util.concurrent.CountDownLatch;

import org.apache.uima.aae.error.AsynchAEException;

public class ControllerLatch
{
	private final CountDownLatch latch = new CountDownLatch(1);
	
	public void waitUntilInitialized() throws AsynchAEException
	{
		try
		{
			latch.await();
		}
		catch( InterruptedException e)
		{
			throw new AsynchAEException(e);
		}
	}
	public void release()
	{
		latch.countDown();
		
	}
	public void openLatch(String aName, boolean isTopLevelAggregate, boolean showMsg)
	{
		release();
		if ( !showMsg )
		{
			return;
		}
		if ( isTopLevelAggregate )
		{
			System.out.println(aName+" Controller Initialized. Ready To Process");
		}
		else
		{
			System.out.println(aName+" Controller Initialized.");

		}
	}

}
