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

package org.apache.uima.adapter.jms.client;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalArgumentException;
import org.apache.uima.aae.AsynchAECasManager;
import org.apache.uima.aae.AsynchAECasManager_impl;
import org.apache.uima.aae.UIDGenerator;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaASCollectionProcessCompleteTimeout;
import org.apache.uima.aae.error.UimaASMetaRequestTimeout;
import org.apache.uima.aae.error.UimaASProcessCasTimeout;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.jmx.UimaASClientInfoMBean;
import org.apache.uima.aae.jmx.UimaASClientInfo;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.CasDefinition;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.impl.ProcessTrace_impl;
import org.apache.uima.aae.client.UimaASProcessStatus;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.client.UimaASProcessStatusImpl;
import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.message.PendingMessage;
import org.apache.uima.aae.controller.Endpoint;

public abstract class BaseUIMAAsynchronousEngineCommon_impl 
implements UimaAsynchronousEngine, MessageListener
{
	private static final Class CLASS_NAME = BaseUIMAAsynchronousEngineCommon_impl.class;

	protected static final int MetadataTimeout = 1;

	protected static final int CpCTimeout = 2;

	protected static final int ProcessTimeout = 3;

	protected boolean initialized;

	protected List listeners = new ArrayList();


	protected AsynchAECasManager asynchManager;

	protected Object endOfCollectionMonitor = new Object();

	protected Object metadataReplyMonitor = new Object();


	protected boolean remoteService = false;






	protected Object gater = new Object();

	protected int howManyBeforeReplySeen = 0;

	protected int receiveWindow = 0;

	protected CollectionReader collectionReader = null;

	protected boolean running = false;

	protected final Object sendAndReceiveCasMonitor = new Object();

	protected ProcessingResourceMetaData resourceMetadata;

	protected CAS sendAndReceiveCAS = null;

	protected UIDGenerator idGenerator = new UIDGenerator();

	protected ConcurrentHashMap clientCache = new ConcurrentHashMap();
	
	protected ConcurrentHashMap threadMonitorMap = new ConcurrentHashMap();

	//	Default timeout for ProcessCas requests
	protected int processTimeout = 0;

	//	Default timeout for GetMeta requests 
	protected int metadataTimeout = 60000;
	
	//	Default timeout for CpC requests is no timeout  
	protected int cpcTimeout = 0;

	protected boolean abort = false;

	protected static final String uniqueIdentifier = String.valueOf(System.nanoTime());

//	protected boolean error;

	protected Exception exc;

	protected long howManySent = 0;

	protected long howManyRecvd = 0;

	protected Object cpcGate = new Object();

	protected ConcurrentHashMap springContainerRegistry = new ConcurrentHashMap();

	protected boolean receivedMetaReply;

	protected boolean receivedCpcReply;

	protected MessageConsumer consumer = null;

	protected UimaASClientInfoMBean clientSideJmxStats =
		new UimaASClientInfo();
	
	protected List pendingMessageList = new ArrayList();
	protected boolean producerInitialized;
	abstract public String getEndPointName() throws Exception;
	abstract protected TextMessage createTextMessage() throws Exception;
	abstract protected void setMetaRequestMessage(TextMessage msg) throws Exception;
	abstract protected void setCASMessage(String casReferenceId, CAS aCAS, TextMessage msg) throws Exception;
	abstract protected void setCASMessage(String casReferenceId, String aSerializedCAS, TextMessage msg) throws Exception;
	abstract public void setCPCMessage(TextMessage msg) throws Exception;
	abstract public void initialize(Map anApplicationContext) throws ResourceInitializationException;
	abstract protected void cleanup() throws Exception;
	abstract public String deploy(String[] aDeploymentDescriptorList, Map anApplicationContext) throws Exception;
	abstract protected String deploySpringContainer(String[] springContextFiles) throws ResourceInitializationException;
  
	public void addStatusCallbackListener(UimaASStatusCallbackListener aListener)
	{
	    listeners.add(aListener);
	}

	
	/**
	 * Serializes a given CAS. 
	 * 
	 * @param aCAS - CAS to serialize
	 * @return - serialized CAS
	 * 
	 * @throws Exception
	 */
	protected String serializeCAS(CAS aCAS) throws Exception
	{
		XmiSerializationSharedData serSharedData = new XmiSerializationSharedData();
		return UimaSerializer.serializeCasToXmi(aCAS, serSharedData);

	}

	public void removeStatusCallbackListener(UimaASStatusCallbackListener aListener)
	{
		listeners.remove(aListener);
	}

	public void setCollectionReader(CollectionReader aCollectionReader) throws ResourceInitializationException
	{
		if ( initialized )
		{
			//	Uima ee client has already been initialized. CR should be
			//	set before calling initialize()
			throw new ResourceInitializationException();
		}
		collectionReader = aCollectionReader;
	}
		
	public synchronized void collectionProcessingComplete() throws ResourceProcessException
	{
		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_app_cpc_request_FINEST", new Object[] {});

			if (howManySent > 0 && howManyRecvd < howManySent)
			{
				synchronized (cpcGate)
				{
					// This monitor is dedicated to single purpose event.
					cpcGate.wait();
				}
			}
			if (!running)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_request_not_done_INFO", new Object[] {});

				return;
			}

			ClientRequest requestToCache = new ClientRequest(uniqueIdentifier, this); //, timeout);
			requestToCache.setIsRemote(remoteService);
			requestToCache.setCPCRequest(true);
			requestToCache.setCpcTimeout(cpcTimeout);
			requestToCache.setEndpoint(getEndPointName());
			
			clientCache.put(uniqueIdentifier, requestToCache);

			PendingMessage msg = new PendingMessage(AsynchAEMessage.CollectionProcessComplete);
			if (cpcTimeout > 0)
			{
				
				requestToCache.startTimer();
				msg.put(UimaAsynchronousEngine.CpcTimeout, String.valueOf(cpcTimeout));
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_started_cpc_request_timer_FINEST", new Object[] {});

			synchronized( pendingMessageList )
			{
				pendingMessageList.add(msg);
				pendingMessageList.notifyAll();
			}

			// Wait for CPC Reply. This blocks!
			waitForCpcReply();

			cancelTimer(uniqueIdentifier);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cancelled_cpc_request_timer_FINEST", new Object[] {});
	
			if (running)
			{
				for (int i = 0; listeners != null && i < listeners.size(); i++)
				{
					((UimaASStatusCallbackListener) listeners.get(i)).collectionProcessComplete(null);
				}
			}
			
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}

	public synchronized void stop()
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});

		if (!running)
		{
			return;
		}

		running = false;
		
		try
		{
			//	Unblock threads
			if( threadMonitorMap.size() > 0 )
			{
				Iterator it = threadMonitorMap.keySet().iterator();
				while( it.hasNext() )
				{
					long key = ((Long)it.next()).longValue();
					ThreadMonitor threadMonitor = 
						(ThreadMonitor)threadMonitorMap.get(key);
					synchronized( threadMonitor.getMonitor())
					{
						threadMonitor.setWasSignaled();
						threadMonitor.getMonitor().notifyAll();
					}
				}
			}
			
			synchronized(endOfCollectionMonitor)
			{
				receivedCpcReply = true;
				endOfCollectionMonitor.notifyAll();
			}
			synchronized(metadataReplyMonitor)
			{
				receivedMetaReply = true;
				metadataReplyMonitor.notifyAll();
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_as_client_INFO", new Object[] {});
			for (Iterator i = springContainerRegistry.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entry = (Map.Entry) i.next();
				Object key = entry.getKey();
				undeploy((String) key);
			}
			asynchManager = null;
			springContainerRegistry.clear();
			listeners.clear();
			clientCache.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			synchronized(this)
			{
				try
				{
					wait(2000); // Let asynch shutdown threads to stop
				}
				catch( Exception e) {}
			}

		}
	}

	public CAS getCAS() throws Exception
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_request_for_cas_FINEST", new Object[] {});
		if (!initialized || !running)
		{
			throw new ResourceInitializationException();
		}
		CAS cas = null;
		if (remoteService)
		{
			cas = asynchManager.getNewCas("ApplicationCasPoolContext");
		}
		else
		{
			cas = asynchManager.getNewCas();
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_new_cas_FINEST", new Object[] {});

		return cas;
	}

	
	protected void reset()
	{
		receivedCpcReply = false;
		receivedMetaReply = false;
	}

	
	protected void sendMetaRequest() throws Exception
	{
		PendingMessage msg = new PendingMessage(AsynchAEMessage.GetMeta);
		ClientRequest requestToCache = new ClientRequest(uniqueIdentifier, this); //, metadataTimeout);
		requestToCache.setIsRemote(remoteService);
		requestToCache.setMetaRequest(true);
		requestToCache.setMetadataTimeout(metadataTimeout);

		requestToCache.setEndpoint(getEndPointName());

		clientCache.put(uniqueIdentifier, requestToCache);
		if (metadataTimeout > 0)
		{
			requestToCache.startTimer();
			msg.put(UimaAsynchronousEngine.GetMetaTimeout, String.valueOf(metadataTimeout));
		}
		synchronized( pendingMessageList )
		{
			pendingMessageList.add(msg);
			pendingMessageList.notifyAll();
		}
	}

	protected void waitForCpcReply()
	{
		synchronized (endOfCollectionMonitor)
		{
			while (!receivedCpcReply)
			{
				try
				{
					// This monitor is dedicated to single purpose event.
					endOfCollectionMonitor.wait();
				}
				catch (Exception e)
				{
				}
			}
		}

	}

	protected void waitForMetadataReply()
	{
		synchronized (metadataReplyMonitor)
		{
			while (!receivedMetaReply)
			{
				try
				{
					// This monitor is dedicated to single purpose event.
					metadataReplyMonitor.wait();
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	public String getPerformanceReport()
	{
		return null;
	}

	public synchronized void process() throws ResourceProcessException
	{
		if (!initialized)
		{
			throw new ResourceProcessException();
		}
		if (collectionReader == null)
		{
			throw new ResourceProcessException();
		}
		try
		{
			CAS cas = null;
			boolean hasNext = true;
			while ((hasNext = collectionReader.hasNext()) == true)
			{
				if (initialized && running)
				{
					cas = getCAS();
					collectionReader.getNext(cas);
					sendCAS(cas);
				}
				else
				{
					break;
				}
			}

			if (hasNext == false)
			{
				collectionProcessingComplete();
			}
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
	}

	protected void waitUntilReadyToSendMessage(int aCommand)
	{
		if (receiveWindow > 0)
		{
			if (howManyBeforeReplySeen > 0 && howManyBeforeReplySeen % receiveWindow == 0)
			{
				synchronized (gater)
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "waitUntilReadyToSendMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_blocking_on_semaphore_FINEST", new Object[] { "Gater" });
					try
					{
						// This monitor is dedicated to single purpose event.
						gater.wait();
					}
					catch (Exception e)
					{
					}
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "waitUntilReadyToSendMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_done_blocking_on_semaphore_FINEST", new Object[] { "Gater" });
			}
			howManyBeforeReplySeen++;
		}

	}
	protected ConcurrentHashMap getCache()
	{
		return clientCache;
	}
	/**
	 * Sends a given CAS for analysis to the UIMA EE Service.
	 * 
	 */
	private synchronized String sendCAS(CAS aCAS, ClientRequest requestToCache) throws ResourceProcessException
	{
		String casReferenceId = requestToCache.getCasReferenceId();
		try
		{
			waitUntilReadyToSendMessage(AsynchAEMessage.Process);

			if (!running)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "sendCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_not_sending_cas_INFO", new Object[] { "Asynchronous Client is Stopping" });
				return null;
			}

			PendingMessage msg = new PendingMessage(AsynchAEMessage.Process);
			long t1 = System.nanoTime();
			String serializedCAS = serializeCAS(aCAS);
			requestToCache.setSerializationTime(System.nanoTime()-t1);
			msg.put( AsynchAEMessage.CAS, serializedCAS);
			msg.put( AsynchAEMessage.CasReference, casReferenceId);
			requestToCache.setIsRemote(remoteService);
			requestToCache.setEndpoint(getEndPointName());
			requestToCache.setProcessTimeout(processTimeout);
			requestToCache.setThreadId(Thread.currentThread().getId());
            requestToCache.clearTimeoutException();

			if (remoteService)
			{
				requestToCache.setCAS(aCAS);
				//	Store the serialized CAS in case the timeout occurs and need to send the 
				//	the offending CAS to listeners for reporting
				requestToCache.setCAS(serializedCAS);
			}

			clientCache.put(casReferenceId, requestToCache);

			if (processTimeout > 0)
			{
				requestToCache.startTimer();
			}
			synchronized( pendingMessageList )
			{
				pendingMessageList.add(msg);
				pendingMessageList.notifyAll();
			}
			howManySent++;
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
		return casReferenceId;

	}
	private ClientRequest produceNewClientRequestObject()
	{
		String casReferenceId = idGenerator.nextId();
		return new ClientRequest(casReferenceId, this);
	}
	/**
	 * Sends a given CAS for analysis to the UIMA EE Service.
	 * 
	 */
	public synchronized String sendCAS(CAS aCAS) throws ResourceProcessException
	{
		return this.sendCAS(aCAS, produceNewClientRequestObject());
	}

	/**
	 * Handles response to CollectionProcessComplete request.
	 * 
	 * @throws Exception
	 */
	protected void handleCollectionProcessCompleteReply(Message message) throws Exception
	{
		int payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();

		if (AsynchAEMessage.Exception == payload)
		{
			ProcessTrace pt = new ProcessTrace_impl();
			UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt);
			Exception exception = retrieveExceptionFormMessage(message);

//			Exception exception = (Exception) ((ObjectMessage) message).getObject();
			status.addEventStatus("CpC", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleCollectionProcessCompleteReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
		}
		else
		{
			//Make the receiving thread to complete
			cleanup(); //Make the receiving thread to complete
			synchronized (endOfCollectionMonitor)
			{
				receivedCpcReply = true;
				endOfCollectionMonitor.notifyAll();
			}
			
		}
	}

	/**
	 * Handles response to GetMeta Request. Deserializes ResourceMetaData and initializes CasManager.
	 * 
	 * @param message -
	 *            jms message containing serialized ResourceMetaData
	 * 
	 * @throws Exception
	 */
	protected void handleMetadataReply(Message message) throws Exception
	{
		cancelTimer(uniqueIdentifier);
		int payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();

		if (AsynchAEMessage.Exception == payload)
		{
			ProcessTrace pt = new ProcessTrace_impl();
			UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt);
			Exception exception = retrieveExceptionFormMessage(message);
			clientSideJmxStats.incrementMetaErrorCount();
			status.addEventStatus("GetMeta", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
			synchronized( metadataReplyMonitor )
			{
				abort = true;
				receivedMetaReply = true; // not really but simulate receiving the meta so that we unblock the monitor
				initialized = false;
				metadataReplyMonitor.notifyAll();
			}
		}
		else
		{
			String meta = ((TextMessage) message).getText();
			ByteArrayInputStream bis = new ByteArrayInputStream(meta.getBytes());
			XMLInputSource in1 = new XMLInputSource(bis, null);
			// Adam - store ResouceMetaData in field so we can return it from getMetaData().
			resourceMetadata = (ProcessingResourceMetaData) UIMAFramework.getXMLParser().parseResourceMetaData(in1);

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_handling_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), meta });

			asynchManager.addMetadata(resourceMetadata);
			synchronized (metadataReplyMonitor)
			{
				receivedMetaReply = true;
				metadataReplyMonitor.notifyAll();
			}
		}
	}

	protected void notifyListeners(CAS aCAS, EntityProcessStatus aStatus, int aCommand)
	{
		for (int i = 0; listeners != null && i < listeners.size(); i++)
		{
			UimaASStatusCallbackListener statCL = (UimaASStatusCallbackListener) listeners.get(i);
			switch( aCommand )
			{
			case AsynchAEMessage.GetMeta:
				statCL.initializationComplete(aStatus);
				break;
				
			case AsynchAEMessage.CollectionProcessComplete:
				statCL.collectionProcessComplete(aStatus);
				break;
				
			case AsynchAEMessage.Process:
				statCL.entityProcessComplete(aCAS, aStatus);
				break;
			}

		}
	}

	protected void cancelTimer(String identifier)
	{
		ClientRequest request = null;
		if (clientCache.containsKey(identifier))
		{
			request = (ClientRequest) clientCache.get(identifier);
			if ( request != null )
			{
			request.cancelTimer();
			}
		}
	}

	private boolean isException( Message message ) throws Exception
	{
		int payload;
		if (message.propertyExists(AsynchAEMessage.Payload))
		{
			payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
		}
		else
		{
			throw new InvalidMessageException("Message Does not Contain Payload property");
		}

		return ( AsynchAEMessage.Exception == payload ? true : false);
	}
	private Exception retrieveExceptionFormMessage( Message message) throws Exception
	{
		Exception exception = null;
		if ( message instanceof ObjectMessage && ((ObjectMessage)message).getObject() instanceof Exception )
		{
			exception = (Exception) ((ObjectMessage) message).getObject();
		}
		else if ( message instanceof TextMessage )
		{
			exception = new UimaEEServiceException(((TextMessage)message).getText());
		}
		return exception;
	}
	/**
	 * Handles response to Process CAS request. If the message originated in a service that is running in a separate jvm (remote), deserialize the CAS and notify the application of the completed analysis via application listener.
	 * 
	 * @param message -
	 *            jms message containing serialized CAS
	 * 
	 * @throws Exception
	 */
	protected void handleProcessReply(Message message, boolean doNotify, ProcessTrace pt) throws Exception
	{
		int payload = -1;
		ClientRequest cachedRequest = null;
		String casReferenceId = null;

		try
		{
			howManyRecvd++;
			
			if ( !running )
			{
				return;
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_handling_process_reply_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });

			casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
			if (casReferenceId != null && !clientCache.containsKey(casReferenceId))
			{
				// Most likely expired message. Already handled as timeout. Discard the message and move on to the next
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
						new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });
				return;
			}
			if (message.propertyExists(AsynchAEMessage.Payload))
			{
				payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
			}
			cachedRequest = (ClientRequest)clientCache.get(casReferenceId);

			if (AsynchAEMessage.Exception == payload)
			{
				handleException(message, doNotify);
				if ( !isShutdownException(message))
				{
					clientSideJmxStats.incrementProcessErrorCount();
				}
				return;
			}
			completeProcessingReply( casReferenceId, payload, doNotify,  message, cachedRequest, pt);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			removeFromCache(casReferenceId);
			if (howManyRecvd == howManySent)
			{
				synchronized (cpcGate)
				{
					cpcGate.notifyAll();
				}
			}
			if (howManyRecvd == howManySent)
			{
				synchronized (cpcGate)
				{
					cpcGate.notifyAll();
				}
			}
		}
	}
	private boolean isShutdownException( Message message ) throws Exception
	{
		Exception exception = retrieveExceptionFormMessage(message);
		if ( exception != null )
		{
			if ( exception instanceof ServiceShutdownException || 
				 exception.getCause() != null && exception.getCause() 
				 instanceof ServiceShutdownException )
			{
				return true;
			}
		}
		return false;
	}
	private void handleException( Message message, boolean doNotify )
	throws Exception
	{
		Exception exception = retrieveExceptionFormMessage(message);
		receivedCpcReply = true; // change state as if the CPC reply came in. This is done to prevent a hang on CPC request 
		synchronized(endOfCollectionMonitor)
		{
			endOfCollectionMonitor.notifyAll();
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
				new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
		if ( doNotify )
		{
			ProcessTrace pt = new ProcessTrace_impl();
			UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt);
			String casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
			if ( casReferenceId != null && casReferenceId.trim().length() > 0)
			{
				//	Add Cas reference Id to enable matching replies with requests
				status = new UimaASProcessStatusImpl(pt, casReferenceId);
			}
			else
			{
				status = new UimaASProcessStatusImpl(pt);
			}
			status.addEventStatus("Process", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.Process);
			//	 Done here
			return;
		}
		else
		{
			throw new ResourceProcessException(exception);
		}

	}
	private void completeProcessingReply( String casReferenceId, int payload, boolean doNotify, Message message, ClientRequest cachedRequest, ProcessTrace pt  )
	throws Exception
	{
		if (AsynchAEMessage.XMIPayload == payload || AsynchAEMessage.CASRefID == payload)
		{
			//cancelTimer(casReferenceId);
			if ( pt == null )
			{
				pt = new ProcessTrace_impl();
			}
			CAS cas=null;
			try
			{
				// If the analysis service is remote deserialize the CAS
				if (remoteService)
				{
					long t1 = System.nanoTime();
					cas = deserializeCAS(((TextMessage) message).getText(), cachedRequest);
					cachedRequest.setDeserializationTime(System.nanoTime() - t1);
				}
				//	Log stats and populate ProcessTrace object
				logTimingInfo(message, pt, cachedRequest);
				if ( doNotify )
				{
					//	Add CAS identifier to enable matching replies with requests
					UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt, casReferenceId);
					notifyListeners(cas, status, AsynchAEMessage.Process);
				}
			}
			finally
			{
				//	Dont release the CAS if the application uses synchronous API
				if ( remoteService && !cachedRequest.isSynchronousInvocation() )
				{
					if (cas != null)
					{
						cas.release();
					}
				}

			}
		}

	}
	private void logTimingInfo(Message message, ProcessTrace pt, ClientRequest cachedRequest ) throws Exception
	{
		clientSideJmxStats.incrementTotalNumberOfCasesProcessed();

		if ( message.getStringProperty(AsynchAEMessage.CasReference) != null )
		{
			String casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
			if ( clientCache.containsKey(casReferenceId) )
			{
				ClientRequest cacheEntry = (ClientRequest) clientCache.get(casReferenceId);
				long timeWaitingForReply = cacheEntry.getTimeWaitingForReply()/ 1000000;

				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time Waiting For Reply", (float) timeWaitingForReply / (float) 1000000 });
				pt.addEvent("UimaEE", "process", "Total Time Waiting For Reply", (int)timeWaitingForReply, "");
			}
		}
		if (message.propertyExists(AsynchAEMessage.TimeToSerializeCAS))
		{
			long timeToSerializeCAS = message.getLongProperty(AsynchAEMessage.TimeToSerializeCAS);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Serialize Cas", (float) timeToSerializeCAS / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Time To Serialize Cas", (int)timeToSerializeCAS/1000000, "");
			//	Add the client serialization overhead to the value returned from a service
			timeToSerializeCAS += cachedRequest.getSerializationTime();
			clientSideJmxStats.incrementTotalSerializationTime(timeToSerializeCAS);
		}
		if (message.propertyExists(AsynchAEMessage.TimeToDeserializeCAS))
		{
			long timeToDeserializeCAS = message.getLongProperty(AsynchAEMessage.TimeToDeserializeCAS);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Deserialize Cas", (float) timeToDeserializeCAS / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Time To Deserialize Cas", (int)timeToDeserializeCAS/1000000, "");
			//	Add the client deserialization overhead to the value returned from a service
			timeToDeserializeCAS += cachedRequest.getDeserializationTime();
			clientSideJmxStats.incrementTotalDeserializationTime(timeToDeserializeCAS);
		}
		if (message.propertyExists(AsynchAEMessage.TimeWaitingForCAS))
		{
			long timeWaitingForCAS = message.getLongProperty(AsynchAEMessage.TimeWaitingForCAS);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time to Wait for CAS", (float) timeWaitingForCAS / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Time to Wait for CAS", (int)timeWaitingForCAS/1000000, "");
		}
		if (message.propertyExists(AsynchAEMessage.TimeInService))
		{
			long ttimeInService = message.getLongProperty(AsynchAEMessage.TimeInService);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time In Service", (float) ttimeInService / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Time In Service", (int)ttimeInService/1000000, "");

		}
		if (message.propertyExists(AsynchAEMessage.TotalTimeSpentInAnalytic))
		{
			long totaltimeInService = message.getLongProperty(AsynchAEMessage.TotalTimeSpentInAnalytic);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time In Service", (float) totaltimeInService / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Total Time In Service", (int)totaltimeInService/1000000, "");
		}
		if (message.propertyExists(AsynchAEMessage.TimeInProcessCAS))
		{
			long totaltimeInProcessCAS = message.getLongProperty(AsynchAEMessage.TimeInProcessCAS);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time In Process CAS", (float) totaltimeInProcessCAS / (float) 1000000 });
			float timeInMillis = (float)totaltimeInProcessCAS/(float)1000000;
			pt.addEvent("UimaEE", "process", "Total Time In Process CAS", (int)timeInMillis, "");
			clientSideJmxStats.incrementTotalTimeToProcess(totaltimeInProcessCAS);
		}
		if (message.propertyExists(AsynchAEMessage.IdleTime))
		{
			long totalIdletime = message.getLongProperty(AsynchAEMessage.IdleTime);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Idle Time Waiting For CAS", (float) totalIdletime / (float) 1000000 });
			pt.addEvent("UimaEE", "process", "Idle Time Waiting For CAS", (int)totalIdletime/1000000, "");
			clientSideJmxStats.incrementTotalIdleTime(totalIdletime);
		}
		

	}
	protected void removeFromCache( String aCasReferenceId )
	{
		if ( aCasReferenceId != null && clientCache.containsKey(aCasReferenceId ))
		{
			ClientRequest requestToCache = (ClientRequest)clientCache.get(aCasReferenceId);
			if ( requestToCache != null )
			{
			requestToCache.removeEntry(aCasReferenceId);
			}
			clientCache.remove(aCasReferenceId);
		}
	}
	protected CAS deserializeCAS(String aSerializedCAS, ClientRequest cachedRequest) throws Exception
	{
		CAS cas = cachedRequest.getCAS();
		XmiSerializationSharedData deserSharedData = new XmiSerializationSharedData();
		UimaSerializer.deserializeCasFromXmi(aSerializedCAS, cas, deserSharedData, true, -1);
		return cas;
	}
	
	protected CAS deserializeCAS(String aSerializedCAS) throws Exception
	{
		CAS cas;

		synchronized (sendAndReceiveCasMonitor)
		{
			if (sendAndReceiveCAS != null)
			{
				cas = sendAndReceiveCAS;
			}
			else
			{
				cas = getCAS();
			}
		}

		XmiSerializationSharedData deserSharedData = new XmiSerializationSharedData();
		UimaSerializer.deserializeCasFromXmi(aSerializedCAS, cas, deserSharedData, true, -1);
		return cas;
	}

	/**
	 * Listener method receiving JMS Messages from the response queue.
	 * 
	 */
	public void onMessage(Message message)
	{
		try
		{

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_msg_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
			if ( !message.propertyExists(AsynchAEMessage.Command) )
			{
			  return;
			}
			int command = message.getIntProperty(AsynchAEMessage.Command);
			if (AsynchAEMessage.CollectionProcessComplete == command)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_cpc_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
				handleCollectionProcessCompleteReply(message);
			}
			else if (AsynchAEMessage.GetMeta == command)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
				handleMetadataReply(message);
			}
			else if (AsynchAEMessage.Process == command)
			{
				if (receiveWindow > 0)
				{
					synchronized (gater)
					{
						howManyBeforeReplySeen--;
						gater.notifyAll();
					}

				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_process_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
				
				String casReferenceId = 
					message.getStringProperty(AsynchAEMessage.CasReference);
				
				if ( casReferenceId == null )
				{
					int payload;
					if (message.propertyExists(AsynchAEMessage.Payload))
					{
						payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
						if (AsynchAEMessage.Exception == payload)
						{
							if ( !isShutdownException(message))
							{
								clientSideJmxStats.incrementProcessErrorCount();
							}
							handleException(message, true);
						}
					}
	
					return;
				}
				
				try
				{
					cancelTimer(casReferenceId);
				}
				catch( Exception e) {}

				ClientRequest cachedRequest =
					(ClientRequest)clientCache.get(casReferenceId);

				if ( cachedRequest == null )
				{
					// Most likely expired message. Already handled as timeout. Discard the message and move on to the next
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });
					return;
				}
				//	Store the total latency for this CAS. The departure time is set right before the CAS
				//	is sent to a service.
				cachedRequest.setTimeWaitingForReply(System.nanoTime() - cachedRequest.getCASDepartureTime());
				if ( cachedRequest.isSynchronousInvocation() )
				{
					//	Save reply message in the cache
					cachedRequest.setMessage(message);
					//	Signal a thread that we received a reply
					if ( threadMonitorMap.containsKey(cachedRequest.getThreadId()))
					{
						ThreadMonitor threadMonitor = (ThreadMonitor) threadMonitorMap.get(cachedRequest.getThreadId());
						//	Unblock the sending thread so that it can complete processing
						//	of the reply. The message has been stored in the cache and 
						//	when the thread wakes up due to notification below, it will
						//	retrieve the reply and process it.
						synchronized( threadMonitor.getMonitor() )
						{
							threadMonitor.setWasSignaled();
							cachedRequest.setReceivedProcessCasReply();
							threadMonitor.getMonitor().notifyAll();
						}
					}
				}
				else
				{
					//	Asynchronous invocation - use notification
					handleProcessReply(message, true, null);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
/*
	private void sendRequestToReleaseCas( String aCasReferenceId, Endpoint anEndpoint ) throws Exception
	{
			MessageProducer msgProducer =
				lookupProducerForEndpoint( anEndpoint );
			TextMessage tm = producerSession.createTextMessage("");
			setReleaseCASMessage(tm, aCasReferenceId);
			msgProducer.send(tm);
	}
*/	
	/**
	 * Gets the ProcessingResourceMetadata for the asynchronous AnalysisEngine.
	 */
	public ProcessingResourceMetaData getMetaData() throws ResourceInitializationException
	{
		return resourceMetadata;
	}
	/**
	 * This is a synchronous method which sends a message to a destination and blocks waiting for
	 * reply. 
	 */
	public void sendAndReceiveCAS(CAS aCAS, ProcessTrace pt) throws ResourceProcessException
	{
		if ( !running )
		{
			throw new ResourceProcessException( new Exception("Uima EE Client Not In Running State"));
		}
		String casReferenceId = null;
			// keep handle to CAS, we'll deserialize into this same CAS later
			sendAndReceiveCAS = aCAS;
			
			ThreadMonitor threadMonitor = null;
			
			if ( threadMonitorMap.containsKey(Thread.currentThread().getId()))
			{
				threadMonitor = (ThreadMonitor) threadMonitorMap.get(Thread.currentThread().getId());
			}
			else
			{
				threadMonitor = new ThreadMonitor( Thread.currentThread().getId() );
				threadMonitorMap.put(Thread.currentThread().getId(), threadMonitor );
			}
			
			ClientRequest cachedRequest = produceNewClientRequestObject();
			cachedRequest.setSynchronousInvocation();
			// send CAS. This call does not block. Instead we will block the sending thread below.
			casReferenceId = sendCAS(aCAS, cachedRequest);

			//cachedRequest = (ClientRequest)clientCache.get(casReferenceId);
			//	Block here
			synchronized (threadMonitor.getMonitor())
			{
				//	Block sending thread until a reply is received
				while (!threadMonitor.wasSignaled && running)
				{
					try
					{
						threadMonitor.getMonitor().wait();
					}
					catch (InterruptedException e)
					{
					}
				}
			}
			try
			{
        // check if timeout exception
        if (cachedRequest.isTimeoutException()) {
          throw new ResourceProcessException(new UimaASProcessCasTimeout());
        }
				//	Process reply in the sending thread
				Message message = cachedRequest.getMessage();
				handleProcessReply(message, false, pt);
			}
			catch( ResourceProcessException rpe )
			{
				throw rpe;
			}
			catch( Exception e )
			{
				throw new ResourceProcessException(e);
			}
			finally
			{
				threadMonitor.reset();
			}
	}
	public void sendAndReceiveCAS(CAS aCAS) throws ResourceProcessException
	{
		sendAndReceiveCAS( aCAS, null );
	}

	protected void notifyOnTimout(CAS aCAS, String anEndpoint, int aTimeoutKind, String casReferenceId)
	{

		ProcessTrace pt = new ProcessTrace_impl();
		UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt,casReferenceId);

		switch (aTimeoutKind)
		{
		case (MetadataTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_meta_timeout_INFO", new Object[] { anEndpoint });
			status.addEventStatus("GetMeta", "Failed", new UimaASMetaRequestTimeout());
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
			synchronized (metadataReplyMonitor)
			{
				abort = true;
				metadataReplyMonitor.notifyAll();
			}
			break;
		case (CpCTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_timeout_INFO", new Object[] { anEndpoint });
			status.addEventStatus("CpC", "Failed", new UimaASCollectionProcessCompleteTimeout());
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
			receivedCpcReply = true;
			synchronized(endOfCollectionMonitor)
			{
				endOfCollectionMonitor.notifyAll();
			}
			break;

		case (ProcessTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_process_timeout_INFO", new Object[] { anEndpoint });
		  ClientRequest cachedRequest = (ClientRequest)clientCache.get(casReferenceId);

		  if ( cachedRequest == null )
		  {
		    // if missing for any reason ...
		    UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
		            new Object[] { anEndpoint, casReferenceId });
		    return;
		  }
		  //  Store the total latency for this CAS. The departure time is set right before the CAS
		  //  is sent to a service.
      //TODO set to process timeout value in nanos
		  cachedRequest.setTimeWaitingForReply(System.nanoTime() - cachedRequest.getCASDepartureTime());

      // mark timeout exception
      cachedRequest.setTimeoutException();

      if ( cachedRequest.isSynchronousInvocation() )
      {
        //  Signal a thread that we received a reply, if in the map
        if ( threadMonitorMap.containsKey(cachedRequest.getThreadId()))
        {
          ThreadMonitor threadMonitor = (ThreadMonitor) threadMonitorMap.get(cachedRequest.getThreadId());
          //  Unblock the sending thread so that it can complete processing with an error
          synchronized( threadMonitor.getMonitor() )
          {
            threadMonitor.setWasSignaled();
            cachedRequest.setReceivedProcessCasReply(); // should not be needed
            threadMonitor.getMonitor().notifyAll();
          }
        }
      }
      else {
        // notify the application listener with the error
        exc = new UimaASProcessCasTimeout();
        status.addEventStatus("Process", "Failed", exc);
        notifyListeners(aCAS, status, AsynchAEMessage.Process);
      }
      cachedRequest.removeEntry(casReferenceId);

//			if (sendAndReceiveCAS != null)
//			{
//				synchronized (sendAndReceiveCasMonitor)
//				{
//					error = true;
//					sendAndReceiveCasMonitor.notifyAll();
//				}
//				sendAndReceiveCAS = aCAS;
//			}

      synchronized (gater) {
        if (howManyBeforeReplySeen > 0) {
          howManyBeforeReplySeen--;
        }
        //TODO what is being notified???
        gater.notifyAll();
      }
      howManyRecvd++; // increment global counter to enable CPC request to be sent when howManySent = howManyRecvd
			break;
		}
    

	}

	public class ClientRequest
	{
		private Timer timer = null;

		private long processTimeout = 0L;

		private long metadataTimeout = 0L;

		private long cpcTimeout = 0L;

		private String casReferenceId = null;

		private BaseUIMAAsynchronousEngineCommon_impl uimaEEEngine = null;

		private boolean isSerializedCAS;

		private String serializedCAS;

		private CAS cas;

		private boolean isMetaRequest = false;

		private boolean isCPCRequest = false;

		private boolean isRemote = true;

		private String endpoint;

		private boolean receivedProcessCasReply = false;
		
		private long threadId=-1;
		
		private Message message;
		
		private boolean synchronousInvocation;  
		
    private boolean timeoutException;  
    
		private long casDepartureTime;
		
		private long timeWaitingForReply;
		
		private long serializationTime;
		
		private long deserializationTime;
		
		private long metaTimeoutErrorCount;
		
		private long processTimeoutErrorCount;
		
		private long processErrorCount;
		
		public long getMetaTimeoutErrorCount() {
			return metaTimeoutErrorCount;
		}
		public void setMetaTimeoutErrorCount(long timeoutErrorCount) {
			metaTimeoutErrorCount = timeoutErrorCount;
		}

		public long getProcessTimeoutErrorCount() {
			return processTimeoutErrorCount;
		}
		public void setProcessTimeoutErrorCount(long timeoutErrorCount) {
			processTimeoutErrorCount = timeoutErrorCount;
		}
		
		public long getProcessErrorCount() {
			return processErrorCount;
		}
		public void setProcessErrorCount(long processErrorCount) {
			this.processErrorCount = processErrorCount;
		}
		public long getSerializationTime() {
			return serializationTime;
		}
		public void setSerializationTime(long serializationTime) {
			this.serializationTime = serializationTime;
		}
		public long getDeserializationTime() {
			return deserializationTime;
		}
		public void setDeserializationTime(long deserializationTime) {
			this.deserializationTime = deserializationTime;
		}
		public boolean isSynchronousInvocation()
		{
			return synchronousInvocation;
		}
		public void setSynchronousInvocation()
		{
			synchronousInvocation = true;
		}
    public boolean isTimeoutException()
    {
      return timeoutException;
    }
    public void setTimeoutException()
    {
      timeoutException = true;
    }
    public void clearTimeoutException()
    {
      timeoutException = false;
    }
		public Message getMessage() 
		{
			return message;
		}
		public void setMessage(Message message) 
		{
			this.message = message;
		}
		public ClientRequest(String aCasReferenceId, BaseUIMAAsynchronousEngineCommon_impl aUimaEEEngine) //, long aTimeout)
		{
			uimaEEEngine = aUimaEEEngine;
			casReferenceId = aCasReferenceId;
		}
		public String getCasReferenceId()
		{
			return casReferenceId;
		}
		
		public void setThreadId( long aThreadId )
		{
			threadId = aThreadId;
		}
		
		public long getThreadId()
		{
			return threadId;
		}
		public void setReceivedProcessCasReply()
		{
			receivedProcessCasReply = true;
		}
		public void setMetadataTimeout( int aTimeout )
		{
			metadataTimeout = aTimeout;
		}
		
		public void setProcessTimeout( int aTimeout )
		{
			processTimeout = aTimeout;
		}
		public long getProcessTimeout()
		{
			return processTimeout;
		}
		public void setCpcTimeout( int aTimeout )
		{
			cpcTimeout = aTimeout;
		}
		
		public void setEndpoint(String anEndpoint)
		{
			endpoint = anEndpoint;
		}

		public void setIsRemote(boolean aRemote)
		{
			isRemote = aRemote;
		}

		public void setCAS(CAS aCAS)
		{
			cas = aCAS;
		}

		public CAS getCAS()
		{
			return cas;
		}
		public void setCAS(String aSerializedCAS)
		{
			serializedCAS = aSerializedCAS;
			isSerializedCAS = true;
		}

		public void startTimer()
		{
			Date timeToRun = null; 
			final ClientRequest _clientReqRef = this;
			if (isMetaRequest())
			{
				timeToRun = new Date(System.currentTimeMillis() + metadataTimeout);
			}
			else if (isCPCRequest())
			{
				timeToRun = new Date(System.currentTimeMillis() + cpcTimeout);
			}
			else
			{
				timeToRun = new Date(System.currentTimeMillis() + processTimeout);
			}

			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "startTimer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_starting_timer_FINEST", new Object[] { endpoint });
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run()
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "run", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_expired_INFO", new Object[] { endpoint, casReferenceId });
					CAS cas = null;
					if (isSerializedCAS)
					{
						try
						{
							if (isRemote)
							{
								cas = deserializeCAS(serializedCAS, _clientReqRef);
							}
							else
							{
								cas = null; // not supported for collocated
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				
          //  TODO: This needs to be done elsewhere
          //removeEntry(casReferenceId);
          
					int timeOutKind;
					if (isMetaRequest())
					{
						timeOutKind = MetadataTimeout;
						initialized = false;
						abort = true;
						metaTimeoutErrorCount++;
						clientSideJmxStats.incrementMetaTimeoutErrorCount();
						synchronized( metadataReplyMonitor )
						{
							receivedMetaReply = true; // not really but simulate receving the meta so that we unblock the monitor
							metadataReplyMonitor.notifyAll();
						}
					}
					else if (isCPCRequest())
					{
						timeOutKind = CpCTimeout;
						receivedCpcReply = true;// not really but simulate receving the meta so that we unblock the monitor
						synchronized( cpcGate )
						{
							cpcGate.notifyAll();
						}
					}
					else
					{
						timeOutKind = ProcessTimeout;
						processTimeoutErrorCount++;
						clientSideJmxStats.incrementProcessTimeoutErrorCount();
					}
					uimaEEEngine.notifyOnTimout(cas, endpoint, timeOutKind, getCasReferenceId());
					timer.cancel();
					if (cas != null)
					{
						cas.release();
					}
					return;
				}
			}, timeToRun);

		}

		public void removeEntry(String aCasReferenceId)
		{
			if (uimaEEEngine.clientCache.containsKey(casReferenceId))
			{
				uimaEEEngine.clientCache.remove(casReferenceId);
			}

		}

		public void cancelTimer()
		{
			if (timer != null)
			{
				timer.cancel();
			}
		}

		public boolean isCPCRequest()
		{
			return isCPCRequest;
		}

		public void setCPCRequest(boolean isCPCRequest)
		{
			this.isCPCRequest = isCPCRequest;
		}

		public boolean isMetaRequest()
		{
			return isMetaRequest;
		}

		public void setMetaRequest(boolean isMetaRequest)
		{
			this.isMetaRequest = isMetaRequest;
		}

		public void setCASDepartureTime( long aDepartureTime )
		{
			casDepartureTime = aDepartureTime;
		}
		public long getCASDepartureTime()
		{
			return casDepartureTime;
		}
		
		public void setTimeWaitingForReply( long aTimeWaitingForReply )
		{
			timeWaitingForReply = aTimeWaitingForReply;
		}
		public long getTimeWaitingForReply()
		{
			return timeWaitingForReply;
		}
	}

	protected class ThreadMonitor
	{
		private long threadId;
		private Object monitor = new Object();
		private boolean wasSignaled = false;
		public ThreadMonitor( long aThreadId )
		{
			threadId = aThreadId;
		}
		public void reset()
		{
			wasSignaled = false;
		}
		public long getThreadId()
		{
			return threadId;
		}
		public Object getMonitor()
		{
			return monitor;
		}
		public void setWasSignaled()
		{
			wasSignaled = true;
		}
		public boolean wasSignaled()
		{
			return wasSignaled;
		}
	}
	/**
	 * Called when the producer thread is fully initialized
	 */
	protected void onProducerInitialized()
	{
		producerInitialized = true;
	}
	
	public void onException(Exception aFailure, String aDestination)
	{
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "onException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_error_while_sending_msg__WARNING", new Object[] {  aDestination, aFailure });
		stop();
	}
}
