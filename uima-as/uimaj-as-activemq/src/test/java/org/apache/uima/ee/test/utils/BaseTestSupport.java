/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.ee.test.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Message;
import org.apache.uima.aae.error.UimaASProcessCasTimeout;
import org.apache.uima.aae.client.UimaASProcessStatus;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.client.UimaASProcessStatusImpl;
import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaASPingTimeout;
import org.apache.uima.aae.error.UimaASProcessCasTimeout;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.ProcessTraceEvent;
import org.apache.uima.util.impl.ProcessTrace_impl;

public abstract class BaseTestSupport extends ActiveMQSupport 
//implements UimaASStatusCallbackListener
{
  private static final char FS = System.getProperty("file.separator").charAt(0);
	protected String text = "IBM today elevated five employees to the title of IBM Fellow\n -- its most prestigious technical honor.\n The company also presented more than $2.8 million in cash awards to employees whose technical innovation have yielded exceptional value to the company and its customers.\nIBM conferred the accolades and awards at its 2003 Corporate Technical Recognition Event (CTRE) in Scottsdale, Ariz. CTRE is a 40-year tradition at IBM, established to recognize exceptional technical employees and reward them for extraordinary achievements and contributions to the company's technology leadership.\n Our technical employees are among the best and brightest innovators in the world.\n They share a passion for excellence that defines their work and permeates the products and services IBM delivers to its customers, said Nick Donofrio, senior vice president, technology and manufacturing for IBM.\n CTRE provides the means for us to honor those who have distinguished themselves as exceptional leaders among their peers.\nAmong the special honorees at the 2003 CTRE are five employees who earned the coveted distinction of IBM Fellow:- David Ferrucci aka Dave, Grady Booch, chief scientist of Rational Software, IBM Software Group.\n Recognized internationally for his innovative work on software architecture, modeling, and software engineering process. \nMr. Booch is one of the original authors of the Unified Modeling Language (UML), the industry-standard language of blueprints for software-intensive systems.- Dr. Donald Chamberlin, researcher, IBM Almaden Research Center. An expert in relational database languages, Dr. Chamberlin is co- inventor of SQL, the language that energized the relational database market. He has also";
	protected String doubleByteText = null;
	protected volatile boolean unexpectedException = false;
	private static final boolean SEND_CAS_ASYNCHRONOUSLY = true;

	protected static final int CPC_LATCH = 1;
	protected static final int EXCEPTION_LATCH = 2;
	protected static final int PROCESS_LATCH = 3;
	protected CountDownLatch processCountLatch = null;
	protected CountDownLatch cpcLatch = null;
	protected CountDownLatch exceptionCountLatch = null;
	protected boolean initialized = false;
	protected Object initializeMonitor = new Object();
	protected boolean isStopped = false;
	protected long responseCounter = 0;
	protected boolean expectingServiceShutdownException = false;
	protected long expectedProcessTime = 0;
	boolean serviceShutdownException = false;
	List<Class> exceptionsToIgnore = new ArrayList<Class>();
	private int timeoutCounter = 0;
	private Object errorCounterMonitor = new Object(); 
	private BaseUIMAAsynchronousEngine_impl engine;
	protected UimaAsTestCallbackListener listener = new UimaAsTestCallbackListener();
	
	protected String deployService(BaseUIMAAsynchronousEngine_impl eeUimaEngine, String aDeploymentDescriptorPath) throws Exception
	{
		Map<String, Object> appCtx = new HashMap();
		appCtx.put(UimaAsynchronousEngine.DD2SpringXsltFilePath, "../uima-as-distr/src/main/scripts/dd2spring.xsl".replace('/', FS));
		appCtx.put(UimaAsynchronousEngine.SaxonClasspath, "file:../uima-as-distr/src/main/saxon/saxon8.jar".replace('/', FS)); 
//		appCtx.put(UimaAsynchronousEngine.UimaEeDebug, UimaAsynchronousEngine.UimaEeDebug);
		String containerId=null;
		try {
		  containerId = eeUimaEngine.deploy(aDeploymentDescriptorPath, appCtx);
		} catch( ResourceInitializationException e) {
		  if ( !ignoreException(ResourceInitializationException.class)) {
		    System.out.println(">>>>>>>>>>> Stopping Client API Due To Initialization Exception");
		    eeUimaEngine.stop();
		    throw e;
		  }
      System.out.println(">>>>>>>>>>> Exception ---:"+e.getClass().getName());
		} catch( Exception e) {
      System.out.println(">>>>>>>>>>> Exception:"+e.getClass().getName());
      throw e;
		}
		return containerId;
	}

	protected void addExceptionToignore( Class anExceptionToIgnore)
	{
	  exceptionsToIgnore.add( anExceptionToIgnore );
	}
	
	protected boolean ignoreException( Class<?> anException )
	{
	  if ( anException == null )
	  {
	    return true;
	  }
	  for( int i=0; i < exceptionsToIgnore.size(); i++)
	  {
	    String name = anException.getName();
	    if ( name.equals( exceptionsToIgnore.get(i).getName()) )
	    {
	      return true;
	    }
	  }
	  return false;
	}
	protected void initialize(BaseUIMAAsynchronousEngine_impl eeUimaEngine, Map<String, Object> appCtx) throws Exception
	{
		eeUimaEngine.addStatusCallbackListener(listener);
		eeUimaEngine.initialize(appCtx);
	}
	protected void setExpectingServiceShutdown()
	{
		expectingServiceShutdownException = true;
	}
	protected void setDoubleByteText( String aDoubleByteText )
	{
		doubleByteText = aDoubleByteText;
	}
	protected void setExpectedProcessTime( long expectedTimeToProcess )
	{
		expectedProcessTime = expectedTimeToProcess;
	}


	protected String getFilepathFromClassloader( String aFilename ) throws Exception
	{
  	  	URL url = this.getClass().getClassLoader().getResource(aFilename);
  	  	return (url == null ? null : url.getPath());
	}
	
	protected Map buildContext( String aTopLevelServiceBrokerURI, String aTopLevelServiceQueueName ) throws Exception
	{
		return buildContext(aTopLevelServiceBrokerURI, aTopLevelServiceQueueName, 0);
	}

	protected Map buildContext(String aTopLevelServiceBrokerURI, String aTopLevelServiceQueueName, int timeout) throws Exception
	{
		Map<String, Object> appCtx = new HashMap();
		appCtx.put(UimaAsynchronousEngine.ServerUri, aTopLevelServiceBrokerURI);
		appCtx.put(UimaAsynchronousEngine.Endpoint, aTopLevelServiceQueueName);
		appCtx.put(UimaAsynchronousEngine.CasPoolSize, Integer.valueOf(4));
		appCtx.put(UimaAsynchronousEngine.ReplyWindow, 15);
		appCtx.put(UimaAsynchronousEngine.Timeout, timeout);
		return appCtx;
	}
	protected boolean isMetaRequest( Message aMessage ) throws Exception
	{
		int messageType = aMessage.getIntProperty(AsynchAEMessage.MessageType);
		int command = aMessage.getIntProperty(AsynchAEMessage.Command);
		return ( AsynchAEMessage.Request == messageType && AsynchAEMessage.GetMeta == command );
	}

	protected Thread spinMonitorThread(final AtomicBoolean ctrlMonitor, int howMany, final int aLatchKind) throws Exception
	{

		switch (aLatchKind)
		{
		case CPC_LATCH:
			// Initialize latch to open after CPC reply comes in.
			cpcLatch = new CountDownLatch(howMany);
			break;

		case EXCEPTION_LATCH:
			// Initialize latch to open after CPC reply comes in.
			exceptionCountLatch = new CountDownLatch(howMany);
			break;

		case PROCESS_LATCH:
			// Initialize latch to open after CPC reply comes in.
			System.out.println("Initializing Process Latch. Number of CASes Expected:" + howMany);
			processCountLatch = new CountDownLatch(howMany);
			break;
		}

		// Spin a thread that waits for the latch to open. The latch will open
		// only when
		// a CPC reply comes in
		Thread t = new Thread()
		{
			public void run()
			{
				try
				{
					// Signal the parent thread that it is ok to send CASes.
					// This is needed
					// so that the CASes are send out when the count down latch
					// is ready.
					synchronized (ctrlMonitor)
					{
						ctrlMonitor.set(true);
						ctrlMonitor.notifyAll();
					}
					// Wait until the count down latch = 0
					// cpcLatch.await();
					switch (aLatchKind)
					{
					case CPC_LATCH:
						// Initialize latch to open after CPC reply comes in.
						cpcLatch.await();
						break;

					case EXCEPTION_LATCH:
						// Initialize latch to open after Exception reply comes in.
						exceptionCountLatch.await();
						break;

					case PROCESS_LATCH:
						// Initialize latch to open after Process reply comes in.
						processCountLatch.await();
						break;
					}
				}
				catch (InterruptedException e)
				{

				}
			}
		};
		t.start();
		return t;
	}

	protected void waitUntilInitialized() throws Exception
	{
		synchronized (initializeMonitor)
		{
			while (!initialized)
			{
				initializeMonitor.wait();
			}
		}
	}

	protected void waitOnMonitor(final AtomicBoolean aMonitor) throws Exception
	{
		// Wait until the count down latch thread is ready
		synchronized (aMonitor)
		{
			while (aMonitor.get() == false)
			{
				aMonitor.wait();
			}
		}

	}
  protected void runTestWithMultipleThreads(String serviceDeplyDescriptor, String queueName, int howManyCASesPerRunningThread, int howManyRunningThreads, int timeout, int aGetMetaTimeout) throws Exception {
    runTestWithMultipleThreads(serviceDeplyDescriptor, queueName, howManyCASesPerRunningThread, howManyRunningThreads, timeout, aGetMetaTimeout,false);
  }

	protected void runTestWithMultipleThreads(String serviceDeplyDescriptor, String queueName, int howManyCASesPerRunningThread, int howManyRunningThreads, int timeout, int aGetMetaTimeout, boolean failOnTimeout) throws Exception
	{
		// Instantiate Uima EE Client
		final BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		// Deploy Uima EE Primitive Service
		final String containerId = deployService(eeUimaEngine, serviceDeplyDescriptor);
		
		engine = eeUimaEngine;
		
		Thread t1 = null;
		Thread t2 = null;
    Map appCtx = buildContext(String.valueOf(broker.getMasterConnectorURI()), queueName, timeout);
    // Set an explicit getMeta (Ping)timeout 
    appCtx.put(UimaAsynchronousEngine.GetMetaTimeout, aGetMetaTimeout );

		initialize(eeUimaEngine, appCtx);

		// Wait until the top level service returns its metadata
		waitUntilInitialized();
		final AtomicBoolean ctrlMonitor = new AtomicBoolean();
		t2 = spinMonitorThread(ctrlMonitor, howManyCASesPerRunningThread * howManyRunningThreads, PROCESS_LATCH);
		// Wait until the CPC Thread is ready.
		waitOnMonitor(ctrlMonitor);

		if ( failOnTimeout ) {
	    //  Spin a thread and wait for awhile before killing the remote service.
	    //  This will cause the client to timeout waiting for a CAS reply and
	    //  to send a Ping message to test service availability. The Ping times
	    //  out and causes the client API to stop.
	    new Thread() {
	      public void run()
	      {
	        Object mux = new Object();
	        synchronized( mux ) {
	          try {
	            mux.wait(5000);
	            //  Undeploy service container
	            eeUimaEngine.undeploy(containerId);
	          } catch (Exception e) {}
	        }
	      }
	    }.start();
		  
		}
		
		
		
		// Spin runner threads and start sending CASes
		for (int i = 0; i < howManyRunningThreads; i++)
		{
			SynchRunner runner = new SynchRunner(eeUimaEngine, howManyCASesPerRunningThread);
			Thread runnerThread = new Thread(runner);
			runnerThread.start();
			System.out.println("Started Runner Thread::Id=" + runnerThread.getId());

		}
		// Wait until ALL CASes return from the service
		t2.join();
		t1 = spinMonitorThread(ctrlMonitor, 1, CPC_LATCH);

		if (!isStopped && !unexpectedException)
		{
			System.out.println("Sending CPC");
			// Send CPC
			eeUimaEngine.collectionProcessingComplete();
		}

		// If have skipped CPC trip the latch
		if (unexpectedException && cpcLatch != null)
		{
			cpcLatch.countDown();
		}
		t1.join();
		eeUimaEngine.stop();

	}

  protected void runCrTest(BaseUIMAAsynchronousEngine_impl aUimaEeEngine, int howMany) throws Exception
  {
    Thread t = null;
    engine = aUimaEeEngine;
    
    final AtomicBoolean ctrlMonitor = new AtomicBoolean();
    t = spinMonitorThread(ctrlMonitor, howMany, PROCESS_LATCH);
    aUimaEeEngine.process();
    waitOnMonitor(ctrlMonitor);
  }
  
	protected void runTest(Map appCtx, BaseUIMAAsynchronousEngine_impl aUimaEeEngine, String aBrokerURI, String aTopLevelServiceQueueName, int howMany, int aLatchKind) throws Exception
	{
		runTest(appCtx, aUimaEeEngine, aBrokerURI, aTopLevelServiceQueueName, howMany, aLatchKind, SEND_CAS_ASYNCHRONOUSLY);
	}
	
	protected void runTest2(Map appCtx, BaseUIMAAsynchronousEngine_impl aUimaEeEngine, String aBrokerURI, String aTopLevelServiceQueueName, int howMany, int aLatchKind) throws Exception
	{
		runTest2(appCtx, aUimaEeEngine, aBrokerURI, aTopLevelServiceQueueName, howMany, aLatchKind, SEND_CAS_ASYNCHRONOUSLY);
	}
	/**
	 * Initializes a given instance of the Uima EE client and executes a test. It uses synchronization to
	 * enforce correct sequence of calls and setups expected result.
	 *  
	 * @param appCtx
	 * @param aUimaEeEngine
	 * @param aBrokerURI
	 * @param aTopLevelServiceQueueName
	 * @param howMany
	 * @param aLatchKind
	 * @param sendCasAsynchronously
	 * @throws Exception
	 */
	protected void runTest(Map appCtx, BaseUIMAAsynchronousEngine_impl aUimaEeEngine, String aBrokerURI, String aTopLevelServiceQueueName, int howMany, int aLatchKind, boolean sendCasAsynchronously) throws Exception
	{
		Thread t1 = null;
		Thread t2 = null;
		serviceShutdownException = false;
		unexpectedException = false;
    engine = aUimaEeEngine;

		if (appCtx == null)
		{
			appCtx = buildContext(aBrokerURI, aTopLevelServiceQueueName, 0);
		}
		try {
	    initialize(aUimaEeEngine, appCtx);
		} catch ( ResourceInitializationException e) {
		  if ( ignoreException(ResourceInitializationException.class)) {
		    return;
		  } else {
		    throw e;
		  }
		} catch ( Exception e) {
      throw e;
		}

		// Wait until the top level service returns its metadata
		waitUntilInitialized();
		if (howMany > 0)
		{
			final AtomicBoolean ctrlMonitor = new AtomicBoolean();
			// Create a thread that will block until the CPC reply come back
			// from the top level service
			if (aLatchKind == EXCEPTION_LATCH)
			{
				t1 = spinMonitorThread(ctrlMonitor, 1, EXCEPTION_LATCH);
			}
			else
			{
				t1 = spinMonitorThread(ctrlMonitor, 1, CPC_LATCH);
				t2 = spinMonitorThread(ctrlMonitor, howMany, PROCESS_LATCH);
			}

			if (!isStopped)
			{
				// Wait until the CPC Thread is ready.
				waitOnMonitor(ctrlMonitor);
				if (!isStopped)
				{
					// Send an in CAS to the top level service
					sendCAS(aUimaEeEngine, howMany, sendCasAsynchronously);
				}
				// Wait until ALL CASes return from the service
				if (t2 != null)
				{
					t2.join();

					if (!serviceShutdownException && !isStopped && !unexpectedException)
					{
						System.out.println("Sending CPC");

						// Send CPC
						aUimaEeEngine.collectionProcessingComplete();
					}
					else
					{
            System.out.println(">>>>>>>>>>>>>>>> Not Sending CPC Due To Exception [serviceShutdownException="+serviceShutdownException+"] [isStopped="+isStopped+"] [unexpectedException="+unexpectedException+"]");
					}
				}

				// If have skipped CPC trip the latch
				if ( serviceShutdownException || (unexpectedException && cpcLatch != null) )
				{
					cpcLatch.countDown();
				}
				t1.join();
			}

		}

		if (unexpectedException)
		{
			fail("Unexpected exception returned");
		}
		aUimaEeEngine.stop();
	}

	/**
	 * Initializes a given instance of the Uima EE client and executes a test. It uses synchronization to
	 * enforce correct sequence of calls and setups expected result.
	 *  
	 * @param appCtx
	 * @param aUimaEeEngine
	 * @param aBrokerURI
	 * @param aTopLevelServiceQueueName
	 * @param howMany
	 * @param aLatchKind
	 * @param sendCasAsynchronously
	 * @throws Exception
	 */
	protected void runTest2(Map appCtx, BaseUIMAAsynchronousEngine_impl aUimaEeEngine, String aBrokerURI, String aTopLevelServiceQueueName, int howMany, int aLatchKind, boolean sendCasAsynchronously) throws Exception
	{
		Thread t1 = null;
		Thread t2 = null;
    engine = aUimaEeEngine;

		if (appCtx == null)
		{
			appCtx = buildContext(aBrokerURI, aTopLevelServiceQueueName, 0);
		}
		initialize(aUimaEeEngine, appCtx);

		// Wait until the top level service returns its metadata
		waitUntilInitialized();
		for (int i=0; i < howMany; i++)
		{
			final AtomicBoolean ctrlMonitor = new AtomicBoolean();
			// Create a thread that will block until the CPC reply come back
			// from the top level service
			if (aLatchKind == EXCEPTION_LATCH)
			{
				t1 = spinMonitorThread(ctrlMonitor, 1, EXCEPTION_LATCH);
			}
			else
			{
				t1 = spinMonitorThread(ctrlMonitor, 1, CPC_LATCH);
				t2 = spinMonitorThread(ctrlMonitor, 1, PROCESS_LATCH);
			}

			if (!isStopped)
			{
				// Wait until the CPC Thread is ready.
				waitOnMonitor(ctrlMonitor);
				if (!isStopped)
				{
					// Send an in CAS to the top level service
					sendCAS(aUimaEeEngine, 1, sendCasAsynchronously);
				}
				// Wait until ALL CASes return from the service
				if (t2 != null)
				{
					t2.join();

					if (!serviceShutdownException && !isStopped && !unexpectedException)
					{
						System.out.println("Sending CPC");

						// Send CPC
						aUimaEeEngine.collectionProcessingComplete();
					}
				}

				// If have skipped CPC trip the latch
				if ( serviceShutdownException || (unexpectedException && cpcLatch != null) )
				{
					cpcLatch.countDown();
				}
				t1.join();
			}

		}

		if (unexpectedException)
		{
			fail("Unexpected exception returned");
		}
		aUimaEeEngine.stop();
	}
	/**
	 * Sends a given number of CASs to Uima EE service. This method sends each CAS using either 
	 * synchronous or asynchronous API. 
	 * 
	 * @param eeUimaEngine - fully initialized instance of the Uima EE client
	 * @param howMany - how many CASes to send to the service
	 * @param sendCasAsynchronously - use either synchronous or asynchronous API
	 * @throws Exception
	 */
	protected void sendCAS(BaseUIMAAsynchronousEngine_impl eeUimaEngine, int howMany, boolean sendCasAsynchronously) throws Exception
	{
    engine = eeUimaEngine;
		for (int i = 0; i < howMany; i++)
		{
			CAS cas = eeUimaEngine.getCAS();
			if ( doubleByteText != null )
			{
				cas.setDocumentText(doubleByteText);
			}
			else
			{
				cas.setDocumentText(text);
			}
			if (sendCasAsynchronously)
			{
				eeUimaEngine.sendCAS(cas);
			}
			else
			{
				eeUimaEngine.sendAndReceiveCAS(cas);
			}
		}
	}

	/**
	 * Increments total number of CASes processed
	 */
	protected void incrementCASesProcessed()
	{
		responseCounter++;
		System.out.println("Client:::::::::::::: Received:" + responseCounter + " Reply");

	}
	
	protected class UimaAsTestCallbackListener extends UimaAsBaseCallbackListener {
	  
    public void onBeforeMessageSend(UimaASProcessStatus status) {
      System.out.println("Received onBeforeMessageSend() Notification With CAS:"+status.getCasReferenceId());
    }

	  /**
	   * Callback method which is called by Uima EE client when a reply to process CAS 
	   * is received. The reply contains either the CAS or an exception that occurred 
	   * while processing the CAS.
	   */
	  public synchronized void entityProcessComplete(CAS aCAS, EntityProcessStatus aProcessStatus)
	  {
	    String casReferenceId="";
	    String parentCasReferenceId="";
	    boolean expectedException = false;
	    if ( aProcessStatus instanceof UimaASProcessStatus )
	    {
	      casReferenceId = 
	        ((UimaASProcessStatus)aProcessStatus).getCasReferenceId();
	      parentCasReferenceId = 
	        ((UimaASProcessStatus)aProcessStatus).getParentCasReferenceId();
	    }
	    if (aProcessStatus.isException())
	    {
	      if ( !expectingServiceShutdownException )
	        System.out.println(" Process Received Reply Containing Exception.");
	      

	      List list = aProcessStatus.getExceptions();
	      for( int i=0; i < list.size(); i++)
	      {
	        Exception e = (Exception)list.get(i);
	        if ( e instanceof ServiceShutdownException || 
	           (e.getCause() != null && e.getCause() instanceof ServiceShutdownException ))
	        {
	          serviceShutdownException = true;
	        }
	        else if ( ignoreException( e.getClass()))
	        {
	          expectedException = true;
	        } else if ( e instanceof ResourceProcessException && isProcessTimeout(e) ) {
	          synchronized(errorCounterMonitor) {
	            System.out.println("Incrementing ProcessTimeout Counter");
	            timeoutCounter++;
	          }
	        } else if ( engine != null && e instanceof UimaASPingTimeout) {
	          System.out.println(".......... Listener Stopping Uima AS Due to Ping Timeout. Service Not Responding To Ping");
	          if ( cpcLatch != null)
	          {
	            cpcLatch.countDown();
	          }

	          engine.stop();
	        }
	        if ( !expectedException && !expectingServiceShutdownException )
	        {
	          e.printStackTrace();
	        }
	      }
	      if (exceptionCountLatch != null)
	      {
	        exceptionCountLatch.countDown();
	        if (processCountLatch != null)
	        {
	          processCountLatch.countDown();
	        }
	      }
	      else if (processCountLatch != null)
	      {
	        if ( !expectedException && !(serviceShutdownException && expectingServiceShutdownException) )
	        {
	        unexpectedException = true;
	        System.out.println(" ... when expecting normal completion!");
	        }
	        while (processCountLatch.getCount() > 0)
	        {
	          processCountLatch.countDown();
	        }
	      }
	    }
	    else if (processCountLatch != null && aCAS != null)
	    {
	      if ( parentCasReferenceId != null )
	      {
	        System.out.println("Client Received Reply Containing CAS:"+casReferenceId+" The Cas Was Generated From Parent Cas Id:"+parentCasReferenceId);
	      }
	      else
	      {
	        System.out.println("Client Received Reply Containing CAS:"+casReferenceId);
	      }
	      
	      if ( doubleByteText != null )
	      {
	        String returnedText = aCAS.getDocumentText();
	        if ( !doubleByteText.equals(returnedText))
	        {
	            System.out.println("!!! DocumentText in CAS reply different from that in CAS sent !!!");
	            System.out.println("This is expected using http connector with vanilla AMQ 5.0 release,");
	            System.out.println("and the test file DoubleByteText.txt contains double byte chars.");
	            System.out.println("To fix, use uima-as-distr/src/main/lib/optional/activemq-optional-5.0.0.jar");
	            unexpectedException = true;
	            processCountLatch.countDown();
	            return;
	        }
	      }
	      // test worked, reset use of this text
	      doubleByteText = null;
	      if ( parentCasReferenceId == null)
	      {
	        processCountLatch.countDown();
	      }
	      List eList = aProcessStatus.getProcessTrace().getEventsByComponentName("UimaEE", false);
	      for( int i=0; i < eList.size(); i++)
	      {
	        ProcessTraceEvent eEvent = (ProcessTraceEvent)eList.get(i);
	        System.out.println("Received Process Event - "+eEvent.getDescription()+" Duration::"+eEvent.getDuration()+" ms"); // / (float) 1000000);
	        //  Check if the running test wants to check how long the processing of CAS took
	        if (  expectedProcessTime > 0 &&
	            "Total Time In Process CAS".equals(eEvent.getDescription()))
	        {
	          //  Check if the expected duration exceeded actual duration for processing
	          //  a CAS. Allow 50ms difference.
	          if (eEvent.getDuration() > expectedProcessTime &&  (eEvent.getDuration() % expectedProcessTime ) > 50 ) 
	          {
	            System.out.println("!!!!!!!!!!!!! Expected Process CAS Duration of:"+expectedProcessTime+" ms. Instead Process CAS Took:"+eEvent.getDuration());
	            unexpectedException = true;
	          }
	        }

	      }
	      incrementCASesProcessed();
	    }
	  }
	  private boolean isProcessTimeout( Exception e) {
	    return (e.getCause() != null && (e.getCause() instanceof UimaASProcessCasTimeout )); 
	  }
	  /**
	   * Callback method which is called by Uima EE client when the initialization 
	   * of the client is completed successfully. 
	   */
	  public void initializationComplete(EntityProcessStatus aStatus)
	  {
	    boolean isPingException;
	    
	    if (aStatus != null && aStatus.isException())
	    {
	      System.out.println("Initialization Received Reply Containing Exception:");
	      List exceptions = aStatus.getExceptions();
	      for (int i = 0; i < exceptions.size(); i++)
	      {
	        if ( exceptions.get(i) instanceof UimaASPingTimeout ) {
	          System.out.println("Client Received PING Timeout. Service Not Available");
	          if (cpcLatch != null)
	          {
	            cpcLatch.countDown();
	          }
	          
	        }
	        if ( !expectingServiceShutdownException )
	        {
	          ((Throwable) exceptions.get(i)).printStackTrace();
	        }
	      }
	      if (exceptionCountLatch != null)
	        exceptionCountLatch.countDown();
	    }
	    synchronized (initializeMonitor)
	    {
	      initialized = true;
	      initializeMonitor.notifyAll();
	    }
	  }

	  /**
	   * Callback method which is called by Uima EE client when a CPC reply
	   * is received OR exception occured while processing CPC request.
	   */
	  public void collectionProcessComplete(EntityProcessStatus aStatus)
	  {
	    if (aStatus != null && aStatus.isException())
	    {
	      
	      List list = aStatus.getExceptions();
	      boolean expectedException = false;
	      for( int i=0; i < list.size(); i++)
	      {
	        Exception e = (Exception)list.get(i);
	        if ( e instanceof ServiceShutdownException || 
	           (e.getCause() != null && e.getCause() instanceof ServiceShutdownException ))
	        {
	          serviceShutdownException = true;
	        }
	        else if ( ignoreException( e.getClass()))
	        {
	          expectedException = true;
	        }
	        if ( !expectedException && !expectingServiceShutdownException )
	        {
	          e.printStackTrace();
	        }
	      }
	      if ( !expectedException && !(serviceShutdownException && expectingServiceShutdownException) )
	      {
	        System.out.println(" Received CPC Reply Containing Exception");
	        System.out.println(" ... when expecting normal CPC reply!");
	        unexpectedException = true;
	      }
	      if (exceptionCountLatch != null)
	      {
	        exceptionCountLatch.countDown();
	      }
	      if (cpcLatch != null)
	      {
	        cpcLatch.countDown();
	      }
	    }
	    else
	    {
	      System.out.println("Received CPC Reply");
	      if (cpcLatch != null)
	      {
	        cpcLatch.countDown();
	      }
	    }
	  }
	}
	/**
	 * A Runnable class used to test concurrency support in Uima EE client. Each instance of this
	 * class will start and send specified number of CASes to a service using synchronous sendAndReceive
	 * API. Each thread sends a CAS and waits for a reply.
	 *
	 */
	protected class SynchRunner implements Runnable
	{
		private BaseUIMAAsynchronousEngine_impl uimaClient = null;
		private long howManyCASes = 1;

		public SynchRunner(BaseUIMAAsynchronousEngine_impl aUimaClient, int howMany)
		{
			uimaClient = aUimaClient;
			howManyCASes = howMany;
		}
		//	Run until All CASes are sent
		public void run()
		{
			UimaASProcessStatusImpl status=null;
			try
			{
				while (howManyCASes-- > 0)
				{
					CAS cas = uimaClient.getCAS();
					cas.setDocumentText(text);
					ProcessTrace pt = new ProcessTrace_impl();

					try
					{
						// Send CAS and wait for a response
						String casReferenceId = uimaClient.sendAndReceiveCAS(cas, pt);
						status = new UimaASProcessStatusImpl(pt, casReferenceId);
					}
					catch( ResourceProcessException rpe)
					{
						status = new UimaASProcessStatusImpl(pt);
						status.addEventStatus("Process", "Failed", rpe);
					}
					finally
					{
						listener.entityProcessComplete(cas, status);
						cas.release();
					}
				}
				System.out.println(">>>>>>>>Thread::" + Thread.currentThread().getId() + " Is Exiting - Completed Full Run");
			}
			catch (Exception e)
			{
				//e.printStackTrace();
			}
		}
	}
	protected void spinShutdownThread( final BaseUIMAAsynchronousEngine_impl uimaEEEngine, long when)
	throws Exception
	{
		Date timeToRun = new Date(System.currentTimeMillis() + when);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run()
			{
				timer.cancel();
				timer.purge();
				System.out.println(">>>> Stopping UIMA EE Engine");
				uimaEEEngine.stop();
				isStopped = true;
				System.out.println(">>>> UIMA EE Engine Stopped");
				if (cpcLatch != null )
				cpcLatch.countDown();
				if ( processCountLatch != null)
				processCountLatch.countDown();
			}
		}, timeToRun);
		
	}
}
