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
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;

public interface UimaAsynchronousEngine
{
	public final String ApplicationContext = "ApplicationContext";
	public final String ApplicationName = "ApplicationName";
	public final String ServerUri = "ServerURI";
	public final String Endpoint = "Endpoint";
	public final String CasPoolSize = "CasPoolSize";
	public static final String ReplyWindow = "ReplyWindow";
	public static final String Timeout = "Timeout";
	public static final String CpcTimeout = "CpcTimeout";
	public static final String GetMetaTimeout = "GetMetaTimeout";
	public static final String DD2SpringXsltFilePath = "DD2SpringXsltFilePath";
	public static final String SaxonClasspath = "SaxonClasspath";
	public static final String UimaEeDebug = "-uimaEeDebug";
	/**
	 * Initializes and instantiates UIMA EE Client component from
	 * provided spring xml context configuration file(s). 
	 * 
	 * @param configFiles - spring xml context files
	 * @param anApplicationContext
	 * 
	 * @throws ResourceInitializationException
	 */
	public void initialize( String[] configFiles, Map anApplicationContext ) throws ResourceInitializationException;

	/**
	 * Initializes asynchronous client using configuration parameters provided in a Map object. It creates a
	 * connection to a service queue managed by a Broker as specified in the parameters. A temporary reply 
	 * queue is also created here with a jms listener attached to it. Once the connections are made and the
	 * listener is started the method sends getMeta request to the uima ee service and waits for a response.
	 * When the reply to getMeta is receives the uima ee client is fully initialized and notifies an
	 * application by calling initializationComplete() on the application listener.
	 * 
	 * @param anApplicationContext - configuration containing UIMA EE Service Broker URI, service
	 * queue name, timeout value, reply window size, and Cas Pool size.
	 * 
	 * @throws ResourceInitializationException 
	 */
	public void initialize( Map anApplicationContext ) throws ResourceInitializationException;

	/**
	 * Plugs in a CollectionReader instance to use. The application calls process() method to
	 * begin analyzing the collection.
	 * 
	 * @param aCollectionReader - instnace of a CollectionReader
	 */
	public void setCollectionReader( CollectionReader aCollectionReader ) throws ResourceInitializationException;
	
	/**
	 * Plugs-in application specific listener. Via this listener the
	 * application receives callbacks. More than one listener can be added.
	 *
	 * 
	 * @param aListener - application listener object to add
	 */
	public void addStatusCallbackListener(UimaEEStatusCallbackListener aListener);
	
	/**
	 * Removes named application listener. 
	 * 
	 * @param aListener - application listener to remove
	 */
	public void removeStatusCallbackListener(UimaEEStatusCallbackListener aListener);
	
	/**
	 * Stops the asynchronous client. Cleans up resources, drops connection to UIMA EE
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
	 * Starts processing a collection. This method should be only called after an instance
	 * of a CollectionReader is provided via setCollectionReader(). The method will block
	 * until the CollectionReader finishes processing of the entire collection.  
	 * 
	 * @throws ResourceProcessException - when CollectionReader is not provided, or initialize()
	 *  has not been called.
	 */
	public void process() throws ResourceProcessException;
	/**
	 * Sends a given CAS for analysis to UIMA EE Service. This method may block if 
	 * the client is configured to use a reply window which prevents from sending too
	 * many CASes to the service. Assuming the window is large enough to send the CAS
	 * this method returns as soon as the CAS is sent. Before sending the CAS, a timer 
	 * starts that will expire if a reply doesnt arrive in a given interval.
	 * 
	 * @param aCAS - a CAS to analyze.
	 * 
	 * @throws ResourceProcessException 
	 */
	public String sendCAS( CAS aCAS ) throws ResourceProcessException;
	
	/**
	 * Requests a new CAS instance from a CAS pool. This method blocks
	 * until a free instance of CAS is available in a CAS pool.
	 * 
	 * @return - CAS instance
	 * @throws Exception
	 */
	public CAS getCAS() throws Exception;
	
	/**
	 * Sends a Collection Processing Complete request to UIMA EE Analysis Service and
	 * blocks waiting for a reply. The method blocks until the service replies
	 * or a timeout occurs. Before returning, this method will notify the application
	 * of completing Collection Processing Complete request via a listener 
	 * 
	 * @throws ResourceProcessException
	 */
	public void collectionProcessingComplete() throws ResourceProcessException;
	
	 /**
	   * Returns ProcessingResourceMetadata of the UIMA EE Analysis Service. The metadata is 
	   * obtained from the service during initialization and cached . 
	   * 	   
	   *  returns - instance of ProcessingResourceMetadata received from an asynchronous
	   *  Analysis Engine service, or null if initialize() has not yet been called.
	   */
	  public ProcessingResourceMetaData getMetaData() throws ResourceInitializationException;
	  
	  /**
	   * Send a CAS, wait for response, and deserialize into same CAS. This is a synchronous call
	   * to the Uima EE service. 
	   */
	  public void sendAndReceiveCAS(CAS aCAS) throws ResourceProcessException;
	
	  /**
	   * Deploys Uima EE service using provided deployment descriptor. The service is deployed
	   * in the same jvm as the client.
	   * 
	   * @param aDeploymentDescriptor - deployment descriptor for the uima ee service
	   * @param anApplicationContext - initialization parameters needed to configure the client and service
	   * 
	   * @return - the id of the container in which the uima service was deployed
	   * 
	   * @throws Exception
	   */
	  public String deploy( String aDeploymentDescriptor, Map anApplicationContext) throws Exception;
	  
	  /**
	   * Deploys Uima EE services using provided deployment descriptors. Each descriptor contains an
	   * assembly of related uima ee services. Uima EE services specified in each deployment descriptor
	   * will be deployed in a separate container.
	   * 
	   * @param aDeploymentDescriptorList - a list of deployment descriptors 
	   * @param anApplicationContext - initialization parameters needed to configure the client and services
	   * 
	   * @return - the ids of the container in which the uima service was deployed
	   * 
	   * @throws Exception
	   */
	  public String deploy( String[] aDeploymentDescriptorList,  Map anApplicationContext ) throws Exception;
	  
	  /**
	   * Un-deploys specified Uima EE service. The service is running in a container with a unique id. The 
	   * container with a given id will be stopped and all components running therein will be also stopped.
	   * 
	   * @param aSpringContainerId - an id of the container which hosts a service to be stopped
	   * 
	   * @throws Exception
	   */
	  public void undeploy( String aSpringContainerId ) throws Exception;

}
