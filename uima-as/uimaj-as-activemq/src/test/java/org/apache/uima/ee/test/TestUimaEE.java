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

package org.apache.uima.ee.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.aae.UimaClassFactory;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.JmsOutputChannel;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.adapter.jms.message.JmsMessageContext;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.ee.test.utils.BaseTestSupport;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;

import com.thoughtworks.xstream.XStream.InitializationException;

public class TestUimaEE extends BaseTestSupport
{
	private static final int DEFAULT_HTTP_PORT = 8888;
	private CountDownLatch getMetaCountLatch = null;
	private static final int MaxGetMetaRetryCount = 2;
    private static final String primitiveServiceQueue1 = "NoOpAnnotatorQueue";
	private static final String PrimitiveDescriptor1 = "resources/descriptors/analysis_engine/NoOpAnnotator.xml";
	private int getMetaRequestCount = 0;
	/**
	 * Tests Broker startup and shutdown
	 */
	public void testBrokerLifecycle() 
	{
		System.out.println("-------------- testBrokerLifecycle -------------");
		System.out.println("UIMA_HOME="+System.getenv("UIMA_HOME")+System.getProperty("file.separator")+"bin"+System.getProperty("file.separator")+"dd2spring.xsl");
	}
	/**
	 * Tests handling of multiple calls to initialize(). A subsequent call to
	 * initialize should result in ResourceInitializationException.
	 * 
	 * @throws Exception
	 */
	public void testInvalidInitializeCall() throws Exception
	{
		System.out.println("-------------- testInvalidInitializeCall -------------");
		//	Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		super.setExpectingServiceShutdown();

		deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
		Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue" );

		try
		{
			initialize(eeUimaEngine, appCtx);
			waitUntilInitialized();
			System.out.println("First Initialize Call Completed");
			eeUimaEngine.initialize(appCtx);
			fail("Subsequent call to initialize() did not return expected exception:"+ UIMA_IllegalStateException.class+" Subsequent call to initialize succeeded with no error");
		}
		catch( ResourceInitializationException e)
		{
			if ( e.getCause() != null && !(e.getCause() instanceof UIMA_IllegalStateException ) )
			{
				fail("Invalid Exception Thrown. Expected:"+ UIMA_IllegalStateException.class+" Received:"+ e.getClass());
			}
			else
			{
				System.out.println("Received Expected Exception:"+ UIMA_IllegalStateException.class);
			}
		}
		catch( ServiceShutdownException e)
		{
			//	expected
		}
		finally
		{
			eeUimaEngine.stop();
		}
	}

	
	/**
	 * Tests deployment of a primitive Uima EE Service (PersontTitleAnnotator). Deploys the primitive
	 * in the same jvm using Uima EE Client API and blocks on a monitor until the Uima Client calls initializationComplete()
	 * method. Once the primitive service starts it is expected to send its metadata to the Uima client
	 * which in turn notifies this object with a call to initializationComplete() where the monitor 
	 * is signaled to unblock the thread. This code will block if the Uima Client does not call 
	 * initializationComplete()
	 * 
	 * @throws Exception
	 */
	public void testDeployPrimitiveService() throws Exception
	{
		System.out.println("-------------- testDeployPrimitiveService -------------");
		//	Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy Uima EE Primitive Service 
		deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
		super.setExpectingServiceShutdown();
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue", 0, EXCEPTION_LATCH);
	}
	/**
	 * Tests a simple Aggregate with one remote Delegate and collocated Cas Multiplier
	 * 
	 * @throws Exception
	 */
	public void testDeployAggregateService() throws Exception
	{
		System.out.println("-------------- testDeployAggregateService -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		System.setProperty(JmsConstants.SessionTimeoutOverride, "2500000");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotator.xml");
		super.setExpectingServiceShutdown();
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 0, EXCEPTION_LATCH);
	}
	/**
	 * Tests a simple Aggregate with one remote Delegate and collocated Cas Multiplier
	 * 
	 * @throws Exception
	 */
	public void testDeployAggregateServiceWithTempReplyQueue() throws Exception
	{
		System.out.println("-------------- testDeployAggregateServiceWithTempReplyQueue -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateUsingRemoteTempQueue.xml");
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	}

  /**
   * Tests exception thrown in the Uima EE Client when the Collection Reader is added after
   * the uima ee client is initialized
   * 
   * @throws Exception
   */
/*	

  public void testCollectionReader() throws Exception
  {
    System.out.println("-------------- testCollectionReader -------------");
    //  Instantiate Uima EE Client
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
    super.setExpectingServiceShutdown();
    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue" );
    // reduce the cas pool size and reply window
    appCtx.remove(UimaAsynchronousEngine.CasPoolSize);
    appCtx.put(UimaAsynchronousEngine.CasPoolSize, Integer.valueOf(2));
    appCtx.remove(UimaAsynchronousEngine.ReplyWindow);
    appCtx.put(UimaAsynchronousEngine.ReplyWindow, 1);
    // set the collection reader
    File collectionReaderDescriptor = new File("resources/descriptors/collection_reader/FileSystemCollectionReader.xml");
    CollectionReaderDescription collectionReaderDescription = UIMAFramework.getXMLParser()
      .parseCollectionReaderDescription(new XMLInputSource(collectionReaderDescriptor));
    CollectionReader collectionReader = UIMAFramework
      .produceCollectionReader(collectionReaderDescription);
    eeUimaEngine.setCollectionReader(collectionReader);
    initialize(eeUimaEngine, appCtx);
    waitUntilInitialized();
    runCrTest(eeUimaEngine, 7);
    eeUimaEngine.stop();
  }
 */ 
  /**
	 * Tests exception thrown in the Uima EE Client when the Collection Reader is added after
	 * the uima ee client is initialized
	 * 
	 * @throws Exception
	 */
	public void testExceptionOnPostInitializeCollectionReaderInjection() throws Exception
	{
		System.out.println("-------------- testExceptionOnPostInitializeCollectionReaderInjection -------------");
		//	Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
		Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue" );
		initialize(eeUimaEngine, appCtx);
		waitUntilInitialized();
		try
		{
			//	Simulate plugging in a Collection Reader. This should throw
			//	ResourceInitializationException since the client code has
			//	been already initialized.
			eeUimaEngine.setCollectionReader(null);
		}
		catch( ResourceInitializationException e)
		{
			System.out.println("Received Expected Exception:"+ResourceInitializationException.class);
			//	Expected
			return;
		}
		catch( Exception e)
		{
			fail("Invalid Exception Thrown. Expected:"+ ResourceInitializationException.class+" Received:"+ e.getClass());
		}
		finally
		{
			eeUimaEngine.stop();
		}
		fail("Expected" + ResourceInitializationException.class);
	}

	/**
	 * Tests the shutdown due to a failure in the Flow Controller while diabling a delegate 
	 * 
	 * @throws Exception
	 */
	public void testTerminateOnFlowControllerExceptionOnDisable() throws Exception
	  {
	    System.out.println("-------------- testTerminateOnFlowControllerExceptionOnNextStep -------------");
	    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
	    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithException.xml");
	    deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithFlowControllerExceptionOnDisable.xml");
	    runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, EXCEPTION_LATCH); //PC_LATCH);
	  }
  
  /**
   * Tests the shutdown due to a failure in the Flow Controller when initializing
   * 
   * @throws Exception
   */
  public void testTerminateOnFlowControllerExceptionOnInitialization() throws Exception {
    System.out.println("-------------- testTerminateOnFlowControllerExceptionOnInitialization -------------");
    
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    String[] containerIds = new String[2];
    try {
        containerIds[0] = deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
    	containerIds[1] = deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithFlowControllerExceptionOnInitialization.xml");
      fail("Expected ResourceInitializationException. Instead, the Aggregate Deployed Successfully");
    } catch (ResourceInitializationException e) {
      System.out.println("\nExpected Initialization Exception was received - cause: "+e.getCause());
    } catch (Exception e) {
      fail("Expected ResourceInitializationException. Instead Got:" + e.getClass());
    }
    finally
    {
    	eeUimaEngine.undeploy(containerIds[0]);
    	eeUimaEngine.undeploy(containerIds[1]);
    }
    
    
  }
    
	/**
	 * Deploys a Primitive Uima EE service and sends 5 CASes to it.
	 * 
	 * @throws Exception
	 */
	
	public void testPrimitiveServiceProcess() throws Exception
	{
		System.out.println("-------------- testPrimitiveServiceProcess -------------");
		//	Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy Uima EE Primitive Service 
		deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
		super.setExpectingServiceShutdown();
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue", 5, PROCESS_LATCH);
	}
	/**
	 * Tests Uima EE client ability to test sendAndReceive in multiple/concurrent threads
	 * It spawns 4 thread each sending 100 CASes to a Primitive Uima EE service
	 * @throws Exception
	 */
	public void testSynchCallProcessWithMultipleThreads() throws Exception
	{
		System.out.println("-------------- testSynchCallProcessWithMultipleThreads -------------");
		int howManyCASesPerRunningThread = 100;
		int howManyRunningThreads = 4;
		super.setExpectingServiceShutdown();
		runTestWithMultipleThreads(relativePath+"/Deploy_PersonTitleAnnotator.xml", "PersonTitleAnnotatorQueue", howManyCASesPerRunningThread, howManyRunningThreads, 0 );
	}
	/**
	 * 
	 * @throws Exception
	 */
	public void testPrimitiveProcessCallWithLongDelay() throws Exception
	{
	    System.out.println("-------------- testPrimitiveProcessCallWithLongDelay -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy Uima EE Primitive Service 
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithLongDelay.xml");
		super.setExpectingServiceShutdown();
		//	We expect 18000ms to be spent in process method
		super.setExpectedProcessTime(6000);

	    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"NoOpAnnotatorQueueLongDelay" );
	    appCtx.remove(UimaAsynchronousEngine.ReplyWindow);
	    appCtx.put(UimaAsynchronousEngine.ReplyWindow, 1);
	    runTest(appCtx,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"NoOpAnnotatorQueueLongDelay", 4, PROCESS_LATCH, true);
	}
	/**
	 * Tests time spent in process CAS. The CAS is sent to three remote delegates each
	 * with a delay of 6000ms in the process method. The aggregate is expected to sum
	 * up the time spent in each annotator process method. The final sum is returned
	 * to the client (the test) and compared against expected 18000ms. The test actually
	 * allows for 20ms margin to account for any overhead (garbage collecting, slow cpu, etc)
	 *  
	 * @throws Exception
	 */
	public void testAggregateProcessCallWithLongDelay() throws Exception
	{
		
	    System.out.println("-------------- testAggregateProcessCallWithLongDelay -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy Uima EE Primitive Services each with 6000ms delay in process()
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorAWithLongDelay.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorBWithLongDelay.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorCWithLongDelay.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotatorWithLongDelay.xml");
		super.setExpectingServiceShutdown();
		//	We expect 18000ms to be spent in process method
		super.setExpectedProcessTime(18000);
	    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue" );
	    appCtx.remove(UimaAsynchronousEngine.ReplyWindow);
	    //	make sure we only send 1 CAS at a time
	    appCtx.put(UimaAsynchronousEngine.ReplyWindow, 1);
	    runTest(appCtx,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH, true);
	}
	/**
	 * Tests shutdown while running with multiple/concurrent threads
	 * The Annotator throws an exception and the Aggregate error handling is setup to terminate
	 * on the first error.
	 * 
	 * @throws Exception
	 */
	public void testTimeoutInSynchCallProcessWithMultipleThreads() throws Exception
	{
	    System.out.println("-------------- testTimeoutInSynchCallProcessWithMultipleThreads -------------");
		int howManyCASesPerRunningThread = 100;
		int howManyRunningThreads = 4;
		super.setExpectingServiceShutdown();
		runTestWithMultipleThreads(relativePath+"/Deploy_NoOpAnnotatorWithLongDelay.xml", "NoOpAnnotatorQueueLongDelay", howManyCASesPerRunningThread, howManyRunningThreads, 2000 );
		
	}
	/**
	 * Tests a parallel flow in the Uima EE aggregate.
	 * 
	 * @throws Exception
	 */
	public void testProcessWithParallelFlow() throws Exception
	{
		System.out.println("-------------- testProcessWithParallelFlow -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlow.xml");
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	}
	/**
	 * Tests ability to disable one delegate in parallel flow and continue
	 * 
	 * @throws Exception
	 */
	public void testDisableDelegateInParallelFlow() throws Exception
	{
		System.out.println("-------------- testDisableDelegateInParallelFlow -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlow.xml");
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	}
	/**
	 * 
	 * @throws Exception
	 */
  public void testTimeoutDelegateInParallelFlows() throws Exception
  {
    System.out.println("-------------- testTimeoutDelegateInParallelFlows -------------");
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithDelay.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithLongDelay.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator3.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlows.xml");
    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue" );
    // Set an explicit process timeout so one of the 1st parallels is disabled but 2nd parallel flow continues.
    appCtx.put(UimaAsynchronousEngine.Timeout, 20000 );
    runTest(appCtx, eeUimaEngine, null, null, 1, PROCESS_LATCH);
  }

  public void testDeployAggregateWithCollocatedAggregateService() throws Exception
	{
    System.out.println("-------------- testDeployAggregateWithCollocatedAggregateService -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_ComplexAggregate.xml");
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 0, PROCESS_LATCH);
	}

	public void testProcessWithAggregateUsingCollocatedMultiplier() throws Exception
	{
    System.out.println("-------------- testProcessWithAggregateUsingCollocatedMultiplier -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotator.xml");
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	}

	public void testProcessWithAggregateUsingRemoteMultiplier() throws Exception
	{
    System.out.println("-------------- testProcessWithAggregateUsingRemoteMultiplier -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_RemoteCasMultiplier.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithRemoteMultiplier.xml");
		runTest(null, eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	}

	public void testStopAggregateWithRemoteMultiplier() throws Exception
	{
    System.out.println("-------------- testStopAggregateWithRemoteMultiplier -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_RemoteCasMultiplier.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithExceptionOn5thCAS.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithRemoteMultiplier.xml");
		runTest(null, eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, EXCEPTION_LATCH);
	}

	public void testCancelProcessAggregateWithCollocatedMultiplier() throws Exception
	{
    System.out.println("-------------- testCancelProcessAggregateWithCollocatedMultiplier -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_ComplexAggregateWith1MillionDocs.xml");
		super.setExpectingServiceShutdown();
		//	Spin a thread to cancel Process after 20 seconds
		spinShutdownThread( eeUimaEngine, 20000 );
		runTest(null, eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1,PROCESS_LATCH);
	}
	public void testCancelProcessAggregateWithRemoteMultiplier() throws Exception
	{
    System.out.println("-------------- testStopAggregateWithRemoteMultiplier -------------");
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		deployService(eeUimaEngine, relativePath+"/Deploy_RemoteCasMultiplierWith1MillionDocs.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithRemoteMultiplier.xml");
		//	Spin a thread to cancel Process after 20 seconds
		spinShutdownThread( eeUimaEngine, 20000 );
		runTest(null, eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1,EXCEPTION_LATCH);
	}
	/**
	 * Test correct reply from the service when its process method fails. Deploys the Primitive
	 * Service ( NoOp Annotator) that is configured to throw an exception on every CAS. The expected
	 * behavior is for the Primitive Service to return a reply with an Exception. This code blocks
	 * on a Count Down Latch, until the exception is returned from the service. When the exception is
	 * received the latch is opened indicating success.  
	 *  
	 * @throws Exception
	 */
	public void testPrimitiveServiceResponseOnException() throws Exception
	{
    System.out.println("-------------- testPrimitiveServiceResponseOnException -------------");
		//	Instantiate Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithException.xml");
		//	Deploy Uima EE Primitive Service 
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"NoOpAnnotatorQueue", 1, EXCEPTION_LATCH);
	}

  public void testProcessParallelFlowWithDelegateFailure() throws Exception
	{
    System.out.println("-------------- testProcessParallelFlowWithDelegateFailure -------------");
		//	Create Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		UIMAFramework.getLogger().setLevel(Level.FINE);
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithException.xml");
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
		//	Deploy top level aggregate service
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlowTerminateOnDelegateFailure.xml");
		
		
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, EXCEPTION_LATCH); //PC_LATCH);
	}

  public void testProcessParallelFlowWithDelegateDisable() throws Exception
  {
    System.out.println("-------------- testProcessParallelFlowWithDelegateDisable -------------");
    //  Create Uima EE Client
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    UIMAFramework.getLogger().setLevel(Level.FINE);
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithException.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlowDisableOnDelegateFailure.xml");
    runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH); //PC_LATCH);
  }

	public void testPrimitiveShutdownOnTooManyErrors() throws Exception
	{
    System.out.println("-------------- testPrimitiveShutdownOnTooManyErrors -------------");
		//	Create Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		//	Deploy top level aggregate service
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotator.xml");
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, CPC_LATCH );
	}
	
	public void testClientHttpTunnelling() throws Exception
	{
    System.out.println("-------------- testClientHttpTunnelling -------------");
		// Add HTTP Connector to the broker. The connector will use port 8888. If this port is not available the test fails 
		String httpURI = addHttpConnector(DEFAULT_HTTP_PORT);
		//	Create Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		runTest(null,eeUimaEngine,httpURI,"NoOpAnnotatorQueue", 1, CPC_LATCH );
	}
	
	public void testAggregateHttpTunnelling() throws Exception
	{
    System.out.println("-------------- testAggregateHttpTunnelling -------------");
		// Add HTTP Connector to the broker. The connector will use port 8888. If this port is not available the test fails 
		addHttpConnector(DEFAULT_HTTP_PORT);
		//	Create Uima EE Client
		BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		//	Deploy top level aggregate that communicates with the remote via Http Tunnelling
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotatorWithHttpDelegate.xml");
		
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		runTest(null,eeUimaEngine, String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue", 10, CPC_LATCH );
	}
	/**
	   * Tests exception thrown in the Uima EE Client when the Collection Reader is added after
	   * the uima ee client is initialized
	   * 
	   * @throws Exception
	   */
		

	  public void testCollectionReader() throws Exception
	  {
	    System.out.println("-------------- testCollectionReader -------------");
	    //  Instantiate Uima EE Client
	    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
	    deployService(eeUimaEngine, relativePath+"/Deploy_PersonTitleAnnotator.xml");
	    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()),"PersonTitleAnnotatorQueue" );
	    // reduce the cas pool size and reply window
	    appCtx.remove(UimaAsynchronousEngine.CasPoolSize);
	    appCtx.put(UimaAsynchronousEngine.CasPoolSize, Integer.valueOf(2));
	    appCtx.remove(UimaAsynchronousEngine.ReplyWindow);
	    appCtx.put(UimaAsynchronousEngine.ReplyWindow, 1);
	    super.setExpectingServiceShutdown();

	    // set the collection reader
	    String filename = super.getFilepathFromClassloader("descriptors/collection_reader/FileSystemCollectionReader.xml");
	    if ( filename == null )
	    {
	    	fail("Unable to find file:"+"descriptors/collection_reader/FileSystemCollectionReader.xml"+ "in classloader");
	    }
	    File collectionReaderDescriptor = new File(filename);
	    CollectionReaderDescription collectionReaderDescription = UIMAFramework.getXMLParser()
	      .parseCollectionReaderDescription(new XMLInputSource(collectionReaderDescriptor));
	    CollectionReader collectionReader = UIMAFramework
	      .produceCollectionReader(collectionReaderDescription);
	    eeUimaEngine.setCollectionReader(collectionReader);
	    initialize(eeUimaEngine, appCtx);
	    waitUntilInitialized();
	    runCrTest(eeUimaEngine, 7);
	    synchronized(this)
	    {
	    	wait(50);
	    }
	    eeUimaEngine.stop();
	  }

  
  public void testAsynchronousTerminate() throws Exception
  {
    System.out.println("-------------- testAsynchronousTerminate -------------");
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotatorWithDelay.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlow.xml");
    
    Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue" );
    initialize(eeUimaEngine, appCtx);
    //  Wait until the top level service returns its metadata
    waitUntilInitialized();

    CAS cas = eeUimaEngine.getCAS();
    System.out.println(" Sending CAS to kick off aggregate w/colocated CasMultiplier");
    eeUimaEngine.sendCAS(cas);

    System.out.println(" Waiting 1 seconds");
    Thread.sleep(1000);

    System.out.println(" Trying to stop service");
    eeUimaEngine.stop();
    System.out.println(" stop() returned!");
  }

  public void testTerminateOnInitializationFailure() throws Exception
  {
    System.out.println("-------------- testTerminateOnInitializationFailure -------------");
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    try
    {
        deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
        deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlow.xml");
        Map<String, Object> appCtx = buildContext( String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue" );
        exceptionCountLatch = new CountDownLatch(1);
        initialize(eeUimaEngine, appCtx);
		fail("Expected ResourceInitializationException. Instead, the Aggregate Reports Successfull Initialization");
    }
    catch( ResourceInitializationException e)
    {
        System.out.println("Expected Initialization Exception was received");
        eeUimaEngine.stop();
    }
	catch( Exception e)
	{
		fail("Expected ResourceInitializationException. Instead Got:"+e.getClass());
	}
  }

  /**
   * Tests shutdown due to delegate broker missing. The Aggregate is configured to
   * retry getMeta 3 times and continue. The client times out after 20 seconds and forces the
   * shutdown. NOTE: The Spring listener tries to recover JMS connection on failure. In this
   * test a Listener to remote delegate cannot be established due to a missing broker. The 
   * Listener is setup to retry every 60 seconds. After failure, the listener goes to sleep
   * for 60 seconds and tries again. This results in a 60 second delay at the end of this test.  
   * 
   * @throws Exception
   */
  public void testTerminateOnInitializationFailureWithDelegateBrokerMissing() throws Exception
  {
    System.out.println("-------------- testTerminateOnInitializationFailureWithDelegateBrokerMissing -------------");
    System.out.println("---------------------- The Uima Client Times Out After 20 seconds --------------------------");
    System.out.println("-- The test requires 1 minute to complete due to 60 second delay in Spring Listener ----");
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
	try
	{
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		//	Deploy top level aggregate that communicates with the remote via Http Tunnelling
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotatorWithHttpDelegate.xml");
		//	Initialize and run the Test. Wait for a completion and cleanup resources.
		Map<String, Object> appCtx = new HashMap();
		appCtx.put(UimaAsynchronousEngine.ServerUri, String.valueOf(broker.getMasterConnectorURI()));
		appCtx.put(UimaAsynchronousEngine.Endpoint, "TopLevelTaeQueue");
		appCtx.put(UimaAsynchronousEngine.GetMetaTimeout, 20000);
		runTest(appCtx,eeUimaEngine, String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue", 1, EXCEPTION_LATCH );
		fail("Expected ResourceInitializationException. Instead, the Aggregate Reports Successfull Initialization");
	}
	catch( ResourceInitializationException e)
	{
	    System.out.println("Expected Initialization Exception was received");
	}
	catch( Exception e)
	{
		fail("Expected ResourceInitializationException. Instead Got:"+e.getClass());
	}
	eeUimaEngine.stop();
  }
  /**
   * Tests shutdown due to delegate broker missing. The Aggregate is configured to
   * terminate on getMeta timeout.
   * 
   * @throws Exception
   */
  public void testTerminateOnInitializationFailureWithAggregateForcedShutdown() throws Exception
  {
    System.out.println("-------------- testTerminateOnInitializationFailureWithAggregateForcedShutdown -------------");
	BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
	//	Initialize and run the Test. Wait for a completion and cleanup resources.
	try
	{
		//	Deploy remote service
		deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
		//	Deploy top level aggregate that communicates with the remote via Http Tunnelling
		deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotatorWithHttpDelegateNoRetries.xml");
		runTest(null,eeUimaEngine, String.valueOf(broker.getMasterConnectorURI()), "TopLevelTaeQueue", 10, EXCEPTION_LATCH );
		fail("Expected ResourceInitializationException. Instead, the Aggregate Reports Successfull Initialization");
	}
	catch( ResourceInitializationException e)
	{
	    System.out.println("Expected Initialization Exception was received");
	}
	catch( Exception e)
	{
		fail("Expected ResourceInitializationException. Instead Got:"+e.getClass());
	}
	eeUimaEngine.stop();
	
  }
  
  /**
   * This tests some of the error handling.  Each annotator writes a file and throws an  exception.
   * After the CAS is processed the presence/absence of certain files indicates success or failure.
   * The first annotator fails and lets the CAS proceed, so should write only one file.
   * The second annotator fails and is retried 2 times, and doesn't let the CAS proceed, so should write 3 files.
   * The third annotator should not see the CAS, so should not write any files
   *  
   * @throws Exception
   */
  public void testContinueOnRetryFailure() throws Exception
  {
    System.out.println("-------------- testContinueOnRetryFailure -------------");
    File tempDir = new File("temp");
    deleteAllFiles(tempDir);
    tempDir.mkdir();
    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
    deployService(eeUimaEngine, relativePath+"/Deploy_WriterAnnotatorA.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_WriterAnnotatorB.xml");
    deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithContinueOnRetryFailures.xml");
    runTest(null, eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
    if ( !(new File(tempDir, "WriterAnnotatorB.3")).exists()
            || (new File(tempDir, "WriterAnnotatorB.4")).exists()) {
      fail("Second annotator should have run 3 times");
    }
    if ((new File(tempDir, "WriterAnnotatorC.1")).exists()) {
      fail("Third annotator should not have seen CAS");
    }
  }

  public void testDeployAgainAndAgain() throws Exception
	  {
	    System.out.println("-------------- testDeployAgainAndAgain -------------");
	    BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl(); // here or in the loop, no change.
	    
	    for (int num=1; num<=50; num++) {
	      System.out.println("\nRunning iteration " + num );
	      deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator.xml");
	      deployService(eeUimaEngine, relativePath+"/Deploy_NoOpAnnotator2.xml");
	      deployService(eeUimaEngine, relativePath+"/Deploy_AggregateWithParallelFlow.xml");
	      runTest(null,eeUimaEngine,String.valueOf(broker.getMasterConnectorURI()),"TopLevelTaeQueue", 1, PROCESS_LATCH);
	    }
	  }	


	/**
	 * This tests GetMeta retries. It deploys a simple Aggregate service that contains a collocated 
	 * Primitive service and a Primitive remote. The Primitive remote is simulated in this code. The
	 * code starts a listener where the Aggregate sends GetMeta requests. The listener responds to
	 * the Aggregate with its metadata only when an expected number of GetMeta retries is met. If 
	 * the Aggregate fails to send expected number of GetMeta requests, the listener will not adjust
	 * its CountDownLatch and will cause this test to hang.
	 *   
	 * @throws Exception
	 */
	
	public void GetMetaRetry() throws Exception
	{
		getMetaCountLatch = new CountDownLatch(MaxGetMetaRetryCount);
        Connection connection = getConnection();
        connection.start();
        
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		ActiveMQDestination destination = (ActiveMQDestination)session.createQueue(primitiveServiceQueue1);
        ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer)session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
        public void onMessage(Message aMessage) 
        {
            	try
            	{
    				if ( isMetaRequest(aMessage))
    				{
    					//	Reply with metadata when retry count reaches defined threshold
    					if ( getMetaRequestCount > 0 && getMetaRequestCount % MaxGetMetaRetryCount == 0 )
    					{
    						JmsMessageContext msgContext = new JmsMessageContext(aMessage, primitiveServiceQueue1);
    						JmsOutputChannel outputChannel = new JmsOutputChannel();
    						outputChannel.setServiceInputEndpoint(primitiveServiceQueue1);
    						outputChannel.setServerURI(getBrokerUri());
    						Endpoint endpoint = msgContext.getEndpoint();
    						outputChannel.sendReply(getPrimitiveMetadata1(PrimitiveDescriptor1),endpoint, true);
    					}
   						getMetaRequestCount++;
   						getMetaCountLatch.countDown();	//	Count down to unblock the thread
    				}
            	}
            	catch( Exception e)
            	{
            		e.printStackTrace();
            	}
            }
        });

        consumer.start();
        BaseUIMAAsynchronousEngine_impl eeUimaEngine = new BaseUIMAAsynchronousEngine_impl();
		String containerId = 
			deployService(eeUimaEngine, relativePath+"/Deploy_AggregateAnnotator.xml");

		Map<String,Object> appCtx = new HashMap();
		appCtx.put(UimaAsynchronousEngine.ServerUri, String.valueOf(broker.getMasterConnectorURI()));
		appCtx.put(UimaAsynchronousEngine.Endpoint, "TopLevelTaeQueue"); 
		appCtx.put(UimaAsynchronousEngine.CasPoolSize, Integer.valueOf(4) );
		appCtx.put(UimaAsynchronousEngine.ReplyWindow, 15 );
		appCtx.put(UimaAsynchronousEngine.Timeout, 0 );
		initialize(eeUimaEngine, appCtx);
		
		System.out.println("TestBroker.testGetMetaRetry()-Blocking On GetMeta Latch. Awaiting GetMeta Requests");
	
		/*********************************************************************************/
		/**** This Code Will Block Until Expected Number Of GetMeta Requests Arrive ******/
		getMetaCountLatch.await();
		/*********************************************************************************/

		
		consumer.stop();
		connection.stop();
		eeUimaEngine.undeploy(containerId);
		eeUimaEngine.stop();
	}

	public ProcessingResourceMetaData getPrimitiveMetadata1(String aDescriptor) throws Exception
	{
		ResourceSpecifier resourceSpecifier = UimaClassFactory.produceResourceSpecifier(aDescriptor);
		return ((AnalysisEngineDescription) resourceSpecifier).getAnalysisEngineMetaData();
	}

	private static boolean deleteAllFiles(File directory) {
		if (directory.isDirectory()) {
			String[] files = directory.list();
			for (int i=0; i<files.length; i++) {
				deleteAllFiles(new File(directory, files[i]));
			}
		}
		// Now have an empty directory or simple file
		return directory.delete();
	}
}
