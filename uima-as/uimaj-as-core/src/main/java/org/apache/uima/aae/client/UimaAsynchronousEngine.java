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

package org.apache.uima.aae.client;

import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
/**
 * A <code>UimaAsynchronousEngine</code> enables an application to send CASes for analysis
 * to remote UIMA AS service. It provides a high level API and hides the detail of the
 * transport implementation. The UIMA AS Client implementation uses JMS as its transport
 * mechanism. Both synchronous and asynchronous processing is supported. For synchronous
 * processing an application should call {@link #sendAndReceive(CAS)} method. For asynchronous
 * processing the application should call {@link #sendCAS(CAS)} method. Additionally, processing
 * with a client side <code>CollectionReader</code> is supported as well. The application first
 * instantiates and initializes the <code>CollectionReader</code> and registers it via
 * {@link #setCollectionReader(CollectionReads)} method. Once the <code>CollectionReader</code>
 * is registered, and {@link #initialize(Map)} method is called, the application may call
 * {@link #process()} method.
 * 
 * <p>
 * This API enables the application to dynamically deploy UIMA AS services that it intends 
 * to use for processing. These services are deployed in a container and are collocated in 
 * the same JVM as the application. The services are considered private and used exclusively 
 * by the application. To deploy "private" services the application calls either 
 * {@link #deploy(String, Map)} {@link #deploy(String[], Map)} 
 * method, where the first parameter is either a single AS deployment descriptor or an 
 * array thereof. The application must deploy its "private" services *before* calling 
 * {@link #initialize(Map)} method. 
 *  
 * <p>
 * The application may stop the UIMA AS client in the middle of processing by calling
 *  {@link #stop()} method.
 *    
 * <p>
 * Listeners can register with the <code>UimaAsynchronousEngine</code> by calling the
 * {@link #addStatusCallbackListener(UimaASStatusCallbackListener)} method. These listeners receive status
 * callbacks during the processing. An exception to that is the synchronous processing via
 * {@link #sendAndReceive(CAS)} method. This method returns either a CAS containing results of
 * analysis or an exception. No callbacks are made while processing CASes synchronously.
 * <p>
 * An application may choose to implement parallelization of the processing, calling either
 * {@link #sendAndReceive(CAS)} or {@link #sendCASCAS)} methods from multiple threads.
 * <p>
 * 
 * 
 */
public interface UimaAsynchronousEngine
{
	public final String ApplicationContext = "ApplicationContext";
	public final String ApplicationName = "ApplicationName";
	public final String ServerUri = "ServerURI";
	public final String Endpoint = "Endpoint";
	public final String CasPoolSize = "CasPoolSize";
	public final String ShadowCasPoolSize ="ShadowCasPoolSize";
	public static final String ReplyWindow = "ReplyWindow";
	public static final String Timeout = "Timeout";
	public static final String CpcTimeout = "CpcTimeout";
	public static final String GetMetaTimeout = "GetMetaTimeout";
	public static final String DD2SpringXsltFilePath = "DD2SpringXsltFilePath";
	public static final String SaxonClasspath = "SaxonClasspath";
  public static final String UimaEeDebug = "-uimaEeDebug";
  public static final String SerializationStrategy = "SerializationStrategy";
  public static final String SharedConnection = "SharedConnection";
		
	/**
	 * Initializes UIMA asynchronous client using configuration parameters provided in a Map object. It creates a
	 * connection to a service queue managed by a Broker as specified in the parameters. A temporary reply 
	 * queue is also created with a JMS listener attached to it. Once the connections are made and the
	 * listener is started the method sends getMeta request to the UIMA AS service and waits for a response.
	 * When the reply to getMeta is received the UIMA AS client is fully initialized and notifies an
	 * application by calling {@link #initializationComplete()} on the application listener.
	 * 
	 * @param anApplicationContext - configuration containing UIMA EE Service Broker URI, service
	 * queue name, timeout value, reply window size, and CAS Pool size.
	 * 
	 * @throws ResourceInitializationException 
	 */
	public void initialize( Map anApplicationContext ) throws ResourceInitializationException;

	/**
	 * Registers a <code>CollectionReader</code> instance to process a Collection. This method must be called first, before
	 * calling {@link #process()} method.
	 * 
	 * @param aCollectionReader - instance of a <code>CollectionReader</code>
	 * 
	 * @throws ResourceInitializationException 
	 */
	public void setCollectionReader( CollectionReader aCollectionReader ) throws ResourceInitializationException;
	
	/**
	 * Registers application specific listener. Via this listener the
	 * application receives call-backs. More than one listener can be added.
	 *
	 * 
	 * @param aListener - application listener object to add
	 */
  public void addStatusCallbackListener(UimaAsBaseCallbackListener aListener);

  /**
	 * Unregisters named application listener. 
	 * 
	 * @param aListener - application listener to remove
	 */
  public void removeStatusCallbackListener(UimaAsBaseCallbackListener aListener);
	
	
	/**
	 * Stops the asynchronous client. Cleans up resources, drops connection to UIMA AS
	 * service queue and stops listening on a response queue.
	 * 
	 * @throws Exception
	 */
	public void stop()  throws Exception;
	
	/**
	 * Not implemented
	 * 
	 * @return null
	 */
	public String getPerformanceReport();
	
	 /**
	   * Initiates processing of a collection. This method should be only called after initialize() 
	   * has been called and an instance of a <code>CollectionReader</code> is provided via {@link #setCollectionReader()}. 
	   * This method blocks until the <code>CollectionReader</code> finishes processing the entire collection.
	   * Status of the processing can be obtained by registering a listener with 
	   * the {@link #addStatusCallbackListener(UimaASStatusCallbackListener)} method.
	   * <p>
	   * The method is synchronized to allow processing of only one collection at a time. The application must
	   * wait with processing another collection until it receives notification via a listener
	   * {@link #org.apache.uima.aae.clientcollectionProcessComplete(EntityProcessStatus aStatus)}
	   * 
	   * @throws ResourceProcessException - if there is a problem processing the Collection
	   */
	public void process() throws ResourceProcessException;
	/**
	 * Sends a given CAS for analysis to UIMA AS Service. This method may block if 
	 * the client is configured to use a reply window which prevents sending too
	 * many CASes to the service. Assuming the window is large enough to send the CAS,
	 * this method returns as soon as the CAS is sent. Before sending the CAS, a timer 
	 * starts that will expire if a reply doesn't arrive in a given interval.
	 * 
	 * @param aCAS - a CAS to analyze.
	 * 
	 * @return - returns a unique identifier associated with the sent CAS
	 * @throws ResourceProcessException 
	 */
	public String sendCAS( CAS aCAS ) throws ResourceProcessException;
	
	/**
	 * Requests new CAS instance from a CAS pool. This method blocks
	 * until a free instance of CAS is available in a CAS pool.
	 * 
	 * @return - new CAS instance fetched from the CAS pool
	 * @throws Exception
	 */
	public CAS getCAS() throws Exception;
	
	/**
	 * Sends a Collection Processing Complete (CPC) request to a UIMA AS Service and
	 * blocks waiting for a reply. The method blocks until the service replies
	 * or a timeout occurs. Before returning, this method will notify an application
	 * of completing the Collection Processing Complete request using registered listener 
	 * 
	 * @throws ResourceProcessException
	 */
	public void collectionProcessingComplete() throws ResourceProcessException;
	
	 /**
	   * Returns a <code>ProcessingResourceMetadata</code> received from the UIMA AS Service. 
	   * The metadata is obtained from the service during initialization. 
	   * 	   
	   *  returns - an ProcessingResourceMetadata received from an asynchronous
	   *  Analysis Engine service, or null if initialize() has not yet been called.
	   *  
	   * @throws ResourceInitializationException
	   */
	  public ProcessingResourceMetaData getMetaData() throws ResourceInitializationException;
	  
	  /**
	   * This synchronous method sends a given CAS to a UIMA AS service and waits for response. 
	   * The method either returns a CAS with the result of analysis or throws an exception. 
	   * It doesn't use call-backs through a registered application listener.  
	   *  
	   * @param aCAS - a CAS to analyze.
	   * @return - a unique id assigned to the CAS
	   * @throws ResourceProcessException
	   */
	  public String sendAndReceiveCAS(CAS aCAS) throws ResourceProcessException;
	
	  /**
	   * Deploys a UIMA AS container and all services defined in provided deployment 
	   * descriptor. Each deployment descriptor contains an assembly of related UIMA AS services.
	   * This method is synchronous and will block until all UIMA AS services are completely
	   * deployed and initialized. If there is a problem deploying any of the UIMA AS services
	   * the container is destroyed and exception thrown. 
	   * 
	   * @param aDeploymentDescriptor- a deployment descriptor to deploy in a container. 
	   * @param anApplicationContext - initialization parameters needed to configure the 
	   * client and services
	   * 
	   * @return - the id of the container in which the UIMA AS services were deployed
	   * 
	   * @throws Exception - if there was a problem deploying the container or UIMA AS services.
	   */
	  public String deploy( String aDeploymentDescriptor, Map anApplicationContext) throws Exception;
	  
	  /**
	   * Deploys a single UIMA AS container and all services defined in provided deployment 
	   * descriptors. Each deployment descriptor contains an assembly of related UIMA AS services.
	   * This method is synchronous and will block until all UIMA AS services are completely
	   * deployed and initialized. If there is a problem deploying any of the UIMA AS services
	   * the container is destroyed and exception thrown. 
	   * 
	   * @param aDeploymentDescriptor- a deployment descriptor to deploy in a container. 
	   * @param anApplicationContext - initialization parameters needed to configure the 
	   * client and services
	   * 
	   * @return - the id of the container in which the UIMA AS services were deployed
	   * 
	   * @throws Exception - if there was a problem deploying the container or UIMA AS services.
	   */
	  public String deploy( String[] aDeploymentDescriptorList,  Map anApplicationContext ) throws Exception;
	  
	  /**
	   * Undeploys specified UIMA AS container and all services running within it. Each UIMA AS container
	   * has a unique id assigned to it during the deploy phase. This method is synchronous and
	   * will block until the container (and all services contained within it) is destroyed.
	   * 
	   * @param aSpringContainerId - an id of the container to be destroyed.
	   * 
	   * @throws Exception
	   */
	  public void undeploy( String aSpringContainerId ) throws Exception;

    /**
     * Undeploys specified UIMA AS container and all services running within it. Each UIMA AS container
     * has a unique id assigned to it during the deploy phase. This method is synchronous and
     * will block until the container (and all services contained within it) is destroyed.
     * 
     * @param aSpringContainerId - an id of the container to be destroyed.
     * 
     * @throws Exception
     */
	  public void undeploy(String aSpringContainerId, int stop_level) throws Exception;
	  
	  
	  /**
	   * Returns serialization strategy for the remote service. All CASes will
	   * be serialized according to the specified serialization. Either xmi or binary
	   * 
	   *   
	   * @return String type of serialization for CASes
	   */
	  public String getSerializationStrategy() ;
	  
	  /**
	   * Request Uima AS client to initiate sending Stop requests to a service for all outstanding
	   * CASes awaiting reply. 
	   * 
	   */
	  public void stopProducingCases();

    /**
     * Request Uima AS client to initiate sending Stop request to a service for a given CAS id
     * If the service is a Cas Multiplier, it will stop producing new CASes, will wait until all 
     * child CASes finish and finally returns the input CAS. 
     * 
     */
	  public void stopProducingCases(String aCasReferenceId);
}

