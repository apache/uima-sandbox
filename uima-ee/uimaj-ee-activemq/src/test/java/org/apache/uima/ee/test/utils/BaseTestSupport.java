package org.apache.uima.ee.test.utils;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Message;

import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.client.UimaEEProcessStatusImpl;
import org.apache.uima.aae.client.UimaEEStatusCallbackListener;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.impl.ProcessTrace_impl;

public abstract class BaseTestSupport extends ActiveMQSupport implements UimaEEStatusCallbackListener
{
	protected String text = "IBM today elevated five employees to the title of IBM Fellow\n -- its most prestigious technical honor.\n The company also presented more than $2.8 million in cash awards to employees whose technical innovation have yielded exceptional value to the company and its customers.\nIBM conferred the accolades and awards at its 2003 Corporate Technical Recognition Event (CTRE) in Scottsdale, Ariz. CTRE is a 40-year tradition at IBM, established to recognize exceptional technical employees and reward them for extraordinary achievements and contributions to the company's technology leadership.\n Our technical employees are among the best and brightest innovators in the world.\n They share a passion for excellence that defines their work and permeates the products and services IBM delivers to its customers, said Nick Donofrio, senior vice president, technology and manufacturing for IBM.\n CTRE provides the means for us to honor those who have distinguished themselves as exceptional leaders among their peers.\nAmong the special honorees at the 2003 CTRE are five employees who earned the coveted distinction of IBM Fellow:- David Ferrucci aka Dave, Grady Booch, chief scientist of Rational Software, IBM Software Group.\n Recognized internationally for his innovative work on software architecture, modeling, and software engineering process. \nMr. Booch is one of the original authors of the Unified Modeling Language (UML), the industry-standard language of blueprints for software-intensive systems.- Dr. Donald Chamberlin, researcher, IBM Almaden Research Center. An expert in relational database languages, Dr. Chamberlin is co- inventor of SQL, the language that energized the relational database market. He has also";
	protected boolean unexpectedException = false;
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

	protected String deployService(BaseUIMAAsynchronousEngine_impl eeUimaEngine, String aDeploymentDescriptorPath) throws Exception
	{
		Map<String, Object> appCtx = new HashMap();
		appCtx.put(UimaAsynchronousEngine.DD2SpringXsltFilePath, System.getenv("UIMA_HOME") + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator") + "dd2spring.xsl"); // C:/uima-ee-rc8/bin/dd2spring.xsl");
		appCtx.put(UimaAsynchronousEngine.SaxonClasspath, "file:" + System.getenv("UIMA_HOME") + System.getProperty("file.separator") + "lib" + System.getProperty("file.separator") + "saxon8.jar");
		appCtx.put(UimaAsynchronousEngine.UimaEeDebug, UimaAsynchronousEngine.UimaEeDebug);
		String containerId = eeUimaEngine.deploy(aDeploymentDescriptorPath, appCtx);
		return containerId;
	}

	protected void initialize(BaseUIMAAsynchronousEngine_impl eeUimaEngine, Map<String, Object> appCtx) throws Exception
	{
		eeUimaEngine.addStatusCallbackListener(this);
		eeUimaEngine.initialize(appCtx);
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
						// Initialize latch to open after CPC reply comes in.
						exceptionCountLatch.await();
						break;

					case PROCESS_LATCH:
						// Initialize latch to open after CPC reply comes in.
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
			if (!initialized)
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
			if (aMonitor.get() == false)
			{
				aMonitor.wait();
			}
		}

	}

	protected void runTestWithMultipleThreads(String serviceDeplyDescriptor, String queueName, int howManyCASesPerRunningThread, int howManyRunningThreads, int timeout) throws Exception
	{
		String[] containers = new String[1];
		// Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		// Deploy Uima EE Primitive Service
		containers[0] = deployService(eeUimaEngine, serviceDeplyDescriptor);// "resources/deployment/Deploy_PersonTitleAnnotator.xml");
		Thread t1 = null;
		Thread t2 = null;
		Map appCtx = buildContext(String.valueOf(broker.getMasterConnectorURI()), queueName, timeout); // "PersonTitleAnnotatorQueue"
																										// );

		initialize(eeUimaEngine, appCtx);

		// Wait until the top level service returns its metadata
		waitUntilInitialized();
		final AtomicBoolean ctrlMonitor = new AtomicBoolean();
		t2 = spinMonitorThread(ctrlMonitor, howManyCASesPerRunningThread * howManyRunningThreads, PROCESS_LATCH);
		// Wait until the CPC Thread is ready.
		waitOnMonitor(ctrlMonitor);

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
    
    final AtomicBoolean ctrlMonitor = new AtomicBoolean();
    t = spinMonitorThread(ctrlMonitor, howMany, PROCESS_LATCH);
    aUimaEeEngine.process();
    waitOnMonitor(ctrlMonitor);
  }
  
	protected void runTest(Map appCtx, BaseUIMAAsynchronousEngine_impl aUimaEeEngine, String aBrokerURI, String aTopLevelServiceQueueName, int howMany, int aLatchKind) throws Exception
	{
		runTest(appCtx, aUimaEeEngine, aBrokerURI, aTopLevelServiceQueueName, howMany, aLatchKind, SEND_CAS_ASYNCHRONOUSLY);
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

		if (appCtx == null)
		{
			appCtx = buildContext(aBrokerURI, aTopLevelServiceQueueName, 0);
		}
		initialize(aUimaEeEngine, appCtx);

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

					if (!isStopped && !unexpectedException)
					{
						System.out.println("Sending CPC");

						// Send CPC
						aUimaEeEngine.collectionProcessingComplete();
					}
				}

				// If have skipped CPC trip the latch
				if (unexpectedException && cpcLatch != null)
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
		for (int i = 0; i < howMany; i++)
		{
			CAS cas = eeUimaEngine.getCAS();
			cas.setDocumentText(text);
			System.out.println(" Sending CAS");
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
		System.out.println(":::::::::::::: Received:" + responseCounter + " Reply");

	}
	/**
	 * Callback method which is called by Uima EE client when a reply to process CAS 
	 * is received. The reply contains either the CAS or an exception that occurred 
	 * while processing the CAS.
	 */
	public void entityProcessComplete(CAS aCAS, EntityProcessStatus aProcessStatus)
	{
		if (aProcessStatus.isException())
		{
			System.out.println(" Process Received Reply Containing Exception");
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
				unexpectedException = true;
				System.out.println(" ... when expecting normal completion!");
				while (processCountLatch.getCount() > 0)
				{
					processCountLatch.countDown();
				}
			}
		}
		else if (processCountLatch != null && aCAS != null)
		{
			System.out.println(" Received Reply Containing CAS");
			processCountLatch.countDown();
			incrementCASesProcessed();
		}
	}
	/**
	 * Callback method which is called by Uima EE client when the initialization 
	 * of the client is completed successfully. 
	 */
	public void initializationComplete(EntityProcessStatus aStatus)
	{
		if (aStatus != null && aStatus.isException())
		{
			System.out.println("Initialization Received Reply Containing Exception:");
			List exceptions = aStatus.getExceptions();
			for (int i = 0; i < exceptions.size(); i++)
			{
				((Throwable) exceptions.get(i)).printStackTrace();
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
			System.out.println(" Received CPC Reply Containing Exception");
			if (exceptionCountLatch != null)
			{
				exceptionCountLatch.countDown();
			}
			else
			{
				if (cpcLatch != null)
				{
					System.out.println(" ... when expecting normal CPC reply!");
					unexpectedException = true;
					cpcLatch.countDown();
				}
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
			try
			{
				while (howManyCASes-- > 0)
				{
					CAS cas = uimaClient.getCAS();
					cas.setDocumentText(text);
					ProcessTrace pt = new ProcessTrace_impl();
					UimaEEProcessStatusImpl status = new UimaEEProcessStatusImpl(pt);

					try
					{
						// Send CAS and wait for a response
						uimaClient.sendAndReceiveCAS(cas);
					}
					catch( ResourceProcessException rpe)
					{
						status.addEventStatus("Process", "Failed", rpe);
					}
					finally
					{
						entityProcessComplete(cas, status);
						cas.release();
					}
				}
				System.out.println(">>>>>>>>Thread::" + Thread.currentThread().getId() + " Is Exiting - Completed Full Run");
			}
			catch (Exception e)
			{
				e.printStackTrace();
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
				cpcLatch.countDown();
				processCountLatch.countDown();
			}
		}, timeToRun);
		
	}
}
