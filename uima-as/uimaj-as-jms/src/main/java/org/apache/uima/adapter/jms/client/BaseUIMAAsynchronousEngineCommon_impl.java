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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
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
import org.apache.uima.aae.client.UimaASProcessStatusImpl;
import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.InvalidMessageException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaASCollectionProcessCompleteTimeout;
import org.apache.uima.aae.error.UimaASMetaRequestTimeout;
import org.apache.uima.aae.error.UimaASPingTimeout;
import org.apache.uima.aae.error.UimaASProcessCasTimeout;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.jmx.UimaASClientInfo;
import org.apache.uima.aae.jmx.UimaASClientInfoMBean;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.message.PendingMessage;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.AllowPreexistingFS;
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
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.delegate.Delegate;

public abstract class BaseUIMAAsynchronousEngineCommon_impl 
implements UimaAsynchronousEngine, MessageListener
{
	private static final Class CLASS_NAME = BaseUIMAAsynchronousEngineCommon_impl.class;
	protected static final String SHADOW_CAS_POOL = "ShadowCasPool";
	protected static final int MetadataTimeout = 1;

	protected static final int CpCTimeout = 2;

	protected static final int ProcessTimeout = 3;
	
	protected static final int PingTimeout = 4;

	protected volatile boolean initialized;

	protected List listeners = new ArrayList();

	protected AsynchAECasManager asynchManager;

	protected Object endOfCollectionMonitor = new Object();

	protected Object metadataReplyMonitor = new Object();

	protected boolean remoteService = false;

	protected Object gater = new Object();

	protected int howManyBeforeReplySeen = 0;

	protected CollectionReader collectionReader = null;

	protected volatile boolean running = false;

	protected final Object sendAndReceiveCasMonitor = new Object();

	protected ProcessingResourceMetaData resourceMetadata;

	protected CAS sendAndReceiveCAS = null;

	protected UIDGenerator idGenerator = new UIDGenerator();

	protected ConcurrentHashMap<String,ClientRequest> clientCache = 
	  new ConcurrentHashMap<String,ClientRequest>();
	
	protected ConcurrentHashMap<Long, ThreadMonitor> threadMonitorMap = 
	  new ConcurrentHashMap<Long, ThreadMonitor>();

	//	Default timeout for ProcessCas requests
	protected int processTimeout = 0;

	//	Default timeout for GetMeta requests 
	protected int metadataTimeout = 60000;
	
	//	Default timeout for CpC requests is no timeout  
	protected int cpcTimeout = 0;

	protected volatile boolean abort = false;

	protected static final String uniqueIdentifier = String.valueOf(System.nanoTime());

	protected Exception exc;

	protected long howManySent = 0;

	protected long howManyRecvd = 0;

	protected Object cpcGate = new Object();

	protected ConcurrentHashMap springContainerRegistry = new ConcurrentHashMap();

	protected volatile boolean receivedMetaReply;

	protected volatile boolean receivedCpcReply;

	protected MessageConsumer consumer = null;

	protected String serializationStrategy = "xmi";
	
	protected UimaASClientInfoMBean clientSideJmxStats =
		new UimaASClientInfo();
	
  private UimaSerializer uimaSerializer = new UimaSerializer();

  protected ClientServiceDelegate serviceDelegate = null;
  
  private Object stopMux = new Object();
  
	protected List pendingMessageList = new ArrayList();
	protected volatile boolean producerInitialized;
	abstract public String getEndPointName() throws Exception;
  abstract protected TextMessage createTextMessage() throws Exception;
  abstract protected BytesMessage createBytesMessage() throws Exception;
	abstract protected void setMetaRequestMessage(Message msg) throws Exception;
	abstract protected void setCASMessage(String casReferenceId, CAS aCAS,Message msg) throws Exception;
  abstract protected void setCASMessage(String casReferenceId, String aSerializedCAS, Message msg) throws Exception;
  abstract protected void setCASMessage(String casReferenceId, byte[] aSerializedCAS, Message msg) throws Exception;
	abstract public void setCPCMessage(Message msg) throws Exception;
	abstract public void initialize(Map anApplicationContext) throws ResourceInitializationException;
	abstract protected void cleanup() throws Exception;
	abstract public String deploy(String[] aDeploymentDescriptorList, Map anApplicationContext) throws Exception;
	abstract protected String deploySpringContainer(String[] springContextFiles) throws ResourceInitializationException;
  
	public void addStatusCallbackListener(UimaASStatusCallbackListener aListener)
	{
	    listeners.add(aListener);
	}

	public String getSerializationStrategy()
	{
	  return serializationStrategy;
	}
	
  protected void setSerializationStrategy(String aSerializationStrategy) {
    serializationStrategy = aSerializationStrategy;
  }
	/**
	 * Serializes a given CAS. 
	 * 
	 * @param aCAS - CAS to serialize
	 * @return - serialized CAS
	 * 
	 * @throws Exception
	 */
	protected String serializeCAS(CAS aCAS,  XmiSerializationSharedData serSharedData) throws Exception
	{
		return uimaSerializer.serializeCasToXmi(aCAS, serSharedData);
	}
	
	protected String serializeCAS(CAS aCAS) throws Exception
	{
		XmiSerializationSharedData serSharedData  = new XmiSerializationSharedData();
		return uimaSerializer.serializeCasToXmi(aCAS, serSharedData);
	}

	public void removeStatusCallbackListener(UimaASStatusCallbackListener aListener)
	{
		listeners.remove(aListener);
	}

	public synchronized void setCollectionReader(CollectionReader aCollectionReader) throws ResourceInitializationException
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
		  if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_app_cpc_request_FINEST", new Object[] {});
		  }

				synchronized (cpcGate)
				{
					while (howManySent > 0 && howManyRecvd < howManySent)
					{
						// This monitor is dedicated to single purpose event.
						cpcGate.wait();
					}
				}
			if (!running)
			{
		     if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
		       UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_request_not_done_INFO", new Object[] {});
		     }
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_started_cpc_request_timer_FINEST", new Object[] {});
      }
			synchronized( pendingMessageList )
			{
			  receivedCpcReply = false;
				pendingMessageList.add(msg);
				pendingMessageList.notifyAll();
			}

			// Wait for CPC Reply. This blocks!
			waitForCpcReply();

			cancelTimer(uniqueIdentifier);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cancelled_cpc_request_timer_FINEST", new Object[] {});
      }
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

	public void stop()
	{
	  synchronized( stopMux ) {
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});
	    }
	    if (!running)
	    {
	      return;
	    }

	    running = false;
	    uimaSerializer.reset();
	    try
	    {
	      //  Unblock threads
	      if( threadMonitorMap.size() > 0 )
	      {
	        Iterator it = threadMonitorMap.keySet().iterator();
	        while( it.hasNext() )
	        {
	          long key = ((Long)it.next()).longValue();
	          ThreadMonitor threadMonitor = 
	            (ThreadMonitor)threadMonitorMap.get(key);
	          if ( threadMonitor == null || threadMonitor.getMonitor() == null)
	          {
	            continue;
	          }
	          synchronized( threadMonitor.getMonitor())
	          {
	            threadMonitor.setWasSignaled();
	            threadMonitor.getMonitor().notifyAll();
	          }
	        }
	      }
        synchronized (cpcGate)
        {
           howManySent = 0;
           cpcGate.notifyAll();
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
	      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_as_client_INFO", new Object[] {});
	      }
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
	}

	public CAS getCAS() throws Exception
	{
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_request_for_cas_FINEST", new Object[] {});
    }
    if ( !running ) {
      throw new RuntimeException("Uima AS Client Is Stopping");
    }
    if (!initialized )
		{
			throw new ResourceInitializationException();
		}
		CAS cas = null;
		long startTime = System.nanoTime();
		if (remoteService)
		{
			cas = asynchManager.getNewCas("ApplicationCasPoolContext");
		}
		else
		{
			cas = asynchManager.getNewCas();
		}
		long waitingTime = System.nanoTime() - startTime;
		clientSideJmxStats.incrementTotalTimeWaitingForCas( waitingTime );
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_new_cas_FINEST", 
    		  new Object[] {"Time Waiting for CAS", (double)waitingTime / (double)1000000});
    }
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
		  serviceDelegate.startGetMetaRequestTimer();
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
			if (!running)
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "sendCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_not_sending_cas_INFO", new Object[] { "Asynchronous Client is Stopping" });
        }
        return null;
			}
			PendingMessage msg = new PendingMessage(AsynchAEMessage.Process);
			long t1 = System.nanoTime();
			if ( serializationStrategy.equals("xmi")) {
	      XmiSerializationSharedData serSharedData = new XmiSerializationSharedData();
	      String serializedCAS = serializeCAS(aCAS, serSharedData);
	      msg.put( AsynchAEMessage.CAS, serializedCAS);
	      if (remoteService)
	      {
	        requestToCache.setCAS(aCAS);
	        //  Store the serialized CAS in case the timeout occurs and need to send the 
	        //  the offending CAS to listeners for reporting
	        requestToCache.setCAS(serializedCAS);
	        requestToCache.setXmiSerializationSharedData(serSharedData);
	      }
			} else {
        byte[] serializedCAS = uimaSerializer.serializeCasToBinary(aCAS);
        msg.put( AsynchAEMessage.CAS, serializedCAS);
        if (remoteService)
        {
          requestToCache.setCAS(aCAS);
        }
			}
			  
      requestToCache.setSerializationTime(System.nanoTime()-t1);
			msg.put( AsynchAEMessage.CasReference, casReferenceId);
			requestToCache.setIsRemote(remoteService);
			requestToCache.setEndpoint(getEndPointName());
			requestToCache.setProcessTimeout(processTimeout);
			requestToCache.setThreadId(Thread.currentThread().getId());
      requestToCache.clearTimeoutException();

			clientCache.put(casReferenceId, requestToCache);

      //  Check delegate's state before sending it a CAS. The delegate
      //  may have previously timed out and the client is in a process of pinging
      //  the delegate to check its availability. While the delegate
      //  is in this state, delay CASes by placing them on a list of
      //  CASes pending dispatch. Once the ping reply is received all
      //  delayed CASes will be dispatched to the delegate.
      if ( !delayCasIfDelegateInTimedOutState( casReferenceId) ) {
        //  The delegate state is normal, add the CAS Id to the list
        //  of CASes sent to the delegate.
        serviceDelegate.addCasToOutstandingList(casReferenceId);
      } else {
        //  CAS was added to the list of CASes pending dispatch. The service
        //  has previously timed out. A Ping message was dispatched to test
        //  service availability. When the Ping reply is received ALL CASes
        //  from the list of CASes pending dispatch will be sent to the 
        //  delegate.
        return casReferenceId;
      }
      synchronized( pendingMessageList )
			{
				pendingMessageList.add(msg);
				pendingMessageList.notifyAll();
			}
			
			synchronized (cpcGate)
			{
				howManySent++;
			}
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
		return casReferenceId;

	}
	
  /**
   * Checks the state of a delegate to see if it is in TIMEOUT State.
   * If it is, push the CAS id onto a list of CASes pending dispatch.
   * The delegate is in a questionable state and the aggregate sends
   * a ping message to check delegate's availability. If the delegate
   * responds to the ping, all CASes in the pending dispatch list will
   * be immediately dispatched.
  **/
  public boolean delayCasIfDelegateInTimedOutState( String aCasReferenceId ) throws AsynchAEException {
    if (serviceDelegate != null && serviceDelegate.getState() == Delegate.TIMEOUT_STATE ) {
      // Add CAS id to the list of delayed CASes.
      serviceDelegate.addCasToPendingDispatchList(aCasReferenceId);
      return true;
    }
    return false;  // Cas Not Delayed
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
			Exception exception = retrieveExceptionFromMessage(message);

			status.addEventStatus("CpC", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleCollectionProcessCompleteReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
      }
		}
		else
		{
		  //  After receiving CPC reply there may be cleanup to do. Delegate this
		  //  to platform specific implementation (ActiveMQ or WAS)
			cleanup(); //Make the receiving thread to complete
			synchronized (endOfCollectionMonitor)
			{
			  // Notify sleeping thread that the CPC reply was received
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
		serviceDelegate.cancelDelegateTimer();
		serviceDelegate.setState(Delegate.OK_STATE);

		//  Check if this is a reply for Ping sent in response to a timeout
		if ( serviceDelegate.isAwaitingPingReply() ) {
      serviceDelegate.resetAwaitingPingReply();
      String casReferenceId = null;
      //  
      if ( serviceDelegate.isSynchronousAPI() ) {
        //  Synchronous API used for sending outgoing messages.
        //  Notify ALL sending threads. A send thread may have
        //  added a CAS to the list of pending CASes due to
        //  a timeout and subsequently entered a wait state
        //  waiting for this notification. The CAS this thread
        //  was trying to deliver will be taken off the pending
        //  dispatch list and send to the service.
        ThreadMonitor threadMonitor = null;
        Iterator it = threadMonitorMap.entrySet().iterator();
        while( it.hasNext() ) {
          threadMonitor = ((Entry<Long, ThreadMonitor>)it.next()).getValue();
          synchronized(threadMonitor.getMonitor()) {
            //  Awake the send thread
            threadMonitor.getMonitor().notifyAll();
          }
        }
      } else {
        //  Asynch API used for sending outgoing messages.
        //  If there are delayed CASes in the delegate's list of CASes
        //  pending dispatch, send them all to the delegate now.
        while( serviceDelegate.getState() == Delegate.OK_STATE && 
                ( casReferenceId = serviceDelegate.removeOldestFromPendingDispatchList()) != null ) {
          ClientRequest cachedRequest = (ClientRequest)clientCache.get(casReferenceId);
          sendCAS(cachedRequest.getCAS(), cachedRequest);
        }
      }
      if ( serviceDelegate.getCasPendingReplyListSize() > 0) {
        serviceDelegate.restartTimerForOldestCasInOutstandingList();
      }
      //  Handled Ping reply
      return;
		}
    //cancelTimer(uniqueIdentifier);
		int payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();

		if (AsynchAEMessage.Exception == payload)
		{
			ProcessTrace pt = new ProcessTrace_impl();
			UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt);
			Exception exception = retrieveExceptionFromMessage(message);
			clientSideJmxStats.incrementMetaErrorCount();
			status.addEventStatus("GetMeta", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
      }
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
		  //  Check serialization supported by the service against client configuration.
		  //  If the client is configured to use Binary serialization *but* the service
		  //  doesnt support it, change the client serialization to xmi. Old services will
		  //  not return in a reply the type of serialization supported which implies "xmi".
		  //  New services *always* return "binary" as a default serialization. The client
		  //  however may still want to serialize messages using xmi though. 
		  if ( !message.propertyExists(AsynchAEMessage.Serialization)) {
        //  Dealing with an old service here, check if there is a mismatch with the 
		    //  client configuration. If the client is configured with binary serialization
		    //  override this and change serialization to "xmi".
		    if ( getSerializationStrategy().equalsIgnoreCase("binary")) {
          System.out.println("\n\t***** WARNING: Service Doesn't Support Binary Serialization. Client Defaulting to XMI Serialization\n");
          //  Override configured serialization
          setSerializationStrategy("xmi");
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "handleMetadataReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_client_serialization_ovveride__WARNING", new Object[] { });
        }
		  }
			String meta = ((TextMessage) message).getText();
			ByteArrayInputStream bis = new ByteArrayInputStream(meta.getBytes());
			XMLInputSource in1 = new XMLInputSource(bis, null);
			// Adam - store ResouceMetaData in field so we can return it from getMetaData().
			resourceMetadata = (ProcessingResourceMetaData) UIMAFramework.getXMLParser().parseResourceMetaData(in1);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_handling_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), meta });
      }
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
      case AsynchAEMessage.Ping:
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
	private Exception retrieveExceptionFromMessage( Message message) throws Exception
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
	private void handleProcessReplyFromSynchronousCall(ClientRequest cachedRequest, Message message) throws Exception
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

	
	/**
	 * Handles response to Process CAS request. If the message originated in a service that is running in a separate jvm (remote), 
	 * deserialize the CAS and notify the application of the completed analysis via application listener.
	 * 
	 * @param message -
	 *            jms message containing serialized CAS
	 * 
	 * @throws Exception
	 */
	protected void handleProcessReply(Message message, boolean doNotify, ProcessTrace pt) throws Exception
	{
		if ( !running )
		{
			return;
		}
		int payload = -1;
		String casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
		
		
		
		//	Determine the type of payload in the message (XMI,Cas Reference,Exception,etc)
		if (message.propertyExists(AsynchAEMessage.Payload))
		{
			payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
		}
    //  Fetch entry from the client cache for a cas id returned from the service
    //  The client cache maintains an entry for every outstanding CAS sent to the
    //  service.
    ClientRequest cachedRequest = null;
    
    if ( casReferenceId != null ) {
      cachedRequest = (ClientRequest)clientCache.get(casReferenceId);
    }

    if (AsynchAEMessage.Exception == payload)
		{
			handleException(message, cachedRequest, true);
			return;
		}
		//	If the Cas Reference id not in the message check if the message contains an
		//	exception and if so, handle the exception and return.
		if ( casReferenceId == null )
		{
			return;
		}
    if ( serviceDelegate.getCasProcessTimeout() > 0) {
      serviceDelegate.cancelDelegateTimer();
    }
		
		serviceDelegate.removeCasFromOutstandingList(casReferenceId);
		if ( message instanceof TextMessage && UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	    UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_handling_process_reply_FINEST",
	            new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), message.toString()+((TextMessage) message).getText() });
		}
		  

		if ( cachedRequest != null )
		{
			//	Store the total latency for this CAS. The departure time is set right before the CAS
			//	is sent to a service.
			cachedRequest.setTimeWaitingForReply(System.nanoTime() - cachedRequest.getCASDepartureTime());
			
			//	If the CAS was sent from a synchronous API sendAndReceive(), wake up the thread that
			//	sent the CAS and process the reply
			if ( cachedRequest.isSynchronousInvocation() )
			{
				handleProcessReplyFromSynchronousCall(cachedRequest, message);
			}
			else
			{
				deserializeAndCompleteProcessingReply( casReferenceId, message, cachedRequest, pt, doNotify );
			}
		}
		else if ( message.propertyExists(AsynchAEMessage.InputCasReference) )
		{
			handleProcessReplyFromCasMultiplier(message, casReferenceId, payload);
		}
		else
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        // Most likely expired message. Already handled as timeout. Discard the message and move on to the next
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });
      }
		}
	}

	private void handleProcessReplyFromCasMultiplier( Message message, String casReferenceId, int payload /*, ClientRequest inputCasCachedRequest*/) throws Exception
	{
		//	Check if the message contains a CAS that was generated by a Cas Multiplier. If so, 
		//  verify that the message also includes an input CAS id and that such input CAS id
		//	exists in the client's cache.
		//	Fetch the input CAS Reference Id from which the CAS being processed was generated from
		String inputCasReferenceId = 
			message.getStringProperty(AsynchAEMessage.InputCasReference);
		//	Fetch an entry from the client cache for a given input CAS id. This would be an id
		//	of the CAS that the client sent out to the service.
		ClientRequest inputCasCachedRequest =
			(ClientRequest)clientCache.get(inputCasReferenceId);
		if ( inputCasCachedRequest == null )
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        // Most likely expired message. Already handled as timeout. Discard the message and move on to the next
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleProcessReplyFromCasMultiplier", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });
      }
			return;
		}
		if (inputCasCachedRequest.isSynchronousInvocation()) {
			handleProcessReplyFromSynchronousCall(inputCasCachedRequest, message);
		}
		//	Fetch the destination for Free CAS notification 
		Destination freeCASNotificationDestination = message.getJMSReplyTo();
		if ( freeCASNotificationDestination != null ) 
		{
			TextMessage msg = createTextMessage();
			setReleaseCASMessage(msg, casReferenceId);
			//	Create Message Producer for the Destination 
			MessageProducer msgProducer = 
				getMessageProducer(freeCASNotificationDestination);
			if ( msgProducer != null )
			{
				try
				{
					//	Send FreeCAS message to a Cas Multiplier
					msgProducer.send(msg);
	         if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	           UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReplyFromCasMultiplier", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_client_sending_release_cas_FINEST",
							new Object[] { freeCASNotificationDestination, message.getStringProperty(AsynchAEMessage.CasReference) });
	         }
				}
				catch( Exception e) 
				{
					e.printStackTrace();
	        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
	          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "handleProcessReplyFromCasMultiplier", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_error_while_sending_msg__WARNING", new Object[] {  "Free Cas Temp Destination", e });
	        }
				} 
			}
		}
		CAS cas =  null;
		if ( message instanceof TextMessage )
		{
	    cas = deserializeCAS(((TextMessage) message).getText(), SHADOW_CAS_POOL );
		} 
		else
		{
      long bodyLength = ((BytesMessage) message).getBodyLength();
      byte[] serializedCas = new byte[(int)bodyLength];
      ((BytesMessage) message).readBytes(serializedCas);
      cas = deserializeCAS(serializedCas, SHADOW_CAS_POOL);

		}
		completeProcessingReply(cas, casReferenceId, payload, true, message, inputCasCachedRequest, null);
	}

	private boolean isShutdownException( Message message ) throws Exception
	{
		Exception exception = retrieveExceptionFromMessage(message);
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
	private void handleException( Message message, ClientRequest cachedRequest, boolean doNotify )
	throws Exception
	{
		if ( !isShutdownException(message))
		{
			clientSideJmxStats.incrementProcessErrorCount();
		}
		Exception exception = retrieveExceptionFromMessage(message);
		
		
		exception.printStackTrace();
		
		receivedCpcReply = true; // change state as if the CPC reply came in. This is done to prevent a hang on CPC request 
		synchronized(endOfCollectionMonitor)
		{
			endOfCollectionMonitor.notifyAll();
		}
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
				new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
    }
    String casReferenceId = null;
    try {
      if ( doNotify )
      {
        ProcessTrace pt = new ProcessTrace_impl();
        UimaASProcessStatusImpl status = null; //  new UimaASProcessStatusImpl(pt);
        casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
        if ( casReferenceId != null && casReferenceId.trim().length() > 0)
        {
          //  Add Cas reference Id to enable matching replies with requests
          status = new UimaASProcessStatusImpl(pt, casReferenceId);
        }
        else
        {
          status = new UimaASProcessStatusImpl(pt);
        }
        status.addEventStatus("Process", "Failed", exception);
        if ( cachedRequest != null && 
                !cachedRequest.isSynchronousInvocation() && 
                cachedRequest.getCAS() != null ) {
          notifyListeners(cachedRequest.getCAS(), status, AsynchAEMessage.Process);
        } else {
          notifyListeners(null, status, AsynchAEMessage.Process);
        }
        //   Done here
        return;
      }
      else
      {
        throw new ResourceProcessException(exception);
      }
    } catch ( Exception e) {
      throw e;
    }
    finally {
      //  Dont release the CAS if the application uses synchronous API
      if ( cachedRequest != null && 
           !cachedRequest.isSynchronousInvocation() && 
           cachedRequest.getCAS() != null )
      {
         cachedRequest.getCAS().release();
      }
      removeFromCache(casReferenceId);
      serviceDelegate.removeCasFromOutstandingList(casReferenceId);
      if (howManyRecvd == howManySent)
      {
        synchronized (cpcGate)
        {
          cpcGate.notifyAll();
        }
      }
    }
	}
	private void completeProcessingReply( CAS cas, String casReferenceId, int payload, boolean doNotify, Message message, ClientRequest cachedRequest, ProcessTrace pt  )
	throws Exception
	{
		if (AsynchAEMessage.XMIPayload == payload || AsynchAEMessage.BinaryPayload == payload || AsynchAEMessage.CASRefID == payload)
		{
			if ( pt == null )
			{
				pt = new ProcessTrace_impl();
			}
			//	Incremente number of replies
			if ( casReferenceId.equals(cachedRequest.getCasReferenceId()) )
			{
				synchronized(cpcGate)
				{
					//	increment number of replies received
					howManyRecvd++;
					cpcGate.notifyAll();
				}
			}

			try
			{
				//	Log stats and populate ProcessTrace object
				logTimingInfo(message, pt, cachedRequest);
				if ( doNotify )
				{
					UimaASProcessStatusImpl status;
					String inputCasReferenceId = 
						message.getStringProperty(AsynchAEMessage.InputCasReference);
					if ( inputCasReferenceId != null && inputCasReferenceId.equals( cachedRequest.getCasReferenceId()))
					{
						status = new UimaASProcessStatusImpl(pt, casReferenceId, inputCasReferenceId);
					}
					else
					{
						status = new UimaASProcessStatusImpl(pt, casReferenceId);
					}
					//	Add CAS identifier to enable matching replies with requests
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
				removeFromCache(casReferenceId);
				if (howManyRecvd == howManySent)
				{
					synchronized (cpcGate)
					{
						cpcGate.notifyAll();
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
				if ( cacheEntry == null )
				{
					return;
				}
				// Add time waiting for reply to the client JMX stats
				long timeWaitingForReply = cacheEntry.getTimeWaitingForReply();
				clientSideJmxStats.incrementTotalTimeWaitingForReply(timeWaitingForReply);
				// Add CAS response latency time to the client JMX stats
				long responseLatencyTime = cacheEntry.getSerializationTime() + timeWaitingForReply + cacheEntry.getDeserializationTime();
				clientSideJmxStats.incrementTotalResponseLatencyTime(responseLatencyTime);
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time Waiting For Reply", (float) timeWaitingForReply / (float) 1000000 });
        }
				pt.addEvent("UimaEE", "process", "Total Time Waiting For Reply", (int)(timeWaitingForReply/1000000), "");
			}
		}
		if (message.propertyExists(AsynchAEMessage.TimeToSerializeCAS))
		{
			long timeToSerializeCAS = message.getLongProperty(AsynchAEMessage.TimeToSerializeCAS);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Serialize Cas", (float) timeToSerializeCAS / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Time To Serialize Cas", (int)(timeToSerializeCAS/1000000), "");
			//	Add the client serialization overhead to the value returned from a service
			timeToSerializeCAS += cachedRequest.getSerializationTime();
			clientSideJmxStats.incrementTotalSerializationTime(timeToSerializeCAS);
		}
		if (message.propertyExists(AsynchAEMessage.TimeToDeserializeCAS))
		{
			long timeToDeserializeCAS = message.getLongProperty(AsynchAEMessage.TimeToDeserializeCAS);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Deserialize Cas", (float) timeToDeserializeCAS / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Time To Deserialize Cas", (int)(timeToDeserializeCAS/1000000), "");
			//	Add the client deserialization overhead to the value returned from a service
			timeToDeserializeCAS += cachedRequest.getDeserializationTime();
			clientSideJmxStats.incrementTotalDeserializationTime(timeToDeserializeCAS);
		}
		if (message.propertyExists(AsynchAEMessage.TimeWaitingForCAS))
		{
			long timeWaitingForCAS = message.getLongProperty(AsynchAEMessage.TimeWaitingForCAS);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time to Wait for CAS", (float) timeWaitingForCAS / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Time to Wait for CAS", (int)(timeWaitingForCAS/1000000), "");
		}
		if (message.propertyExists(AsynchAEMessage.TimeInService))
		{
			long ttimeInService = message.getLongProperty(AsynchAEMessage.TimeInService);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time In Service", (float) ttimeInService / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Time In Service", (int)(ttimeInService/1000000), "");

		}
		if (message.propertyExists(AsynchAEMessage.TotalTimeSpentInAnalytic))
		{
			long totaltimeInService = message.getLongProperty(AsynchAEMessage.TotalTimeSpentInAnalytic);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time In Service", (float) totaltimeInService / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Total Time In Service", (int)(totaltimeInService/1000000), "");
		}
		if (message.propertyExists(AsynchAEMessage.TimeInProcessCAS))
		{
			long totaltimeInProcessCAS = message.getLongProperty(AsynchAEMessage.TimeInProcessCAS);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time In Process CAS", (float) totaltimeInProcessCAS / (float) 1000000 });
      }
			float timeInMillis = (float)totaltimeInProcessCAS/(float)1000000;
			pt.addEvent("UimaEE", "process", "Total Time In Process CAS", (int)timeInMillis, "");
			clientSideJmxStats.incrementTotalTimeToProcess(totaltimeInProcessCAS);
		}
		if (message.propertyExists(AsynchAEMessage.IdleTime))
		{
			long totalIdletime = message.getLongProperty(AsynchAEMessage.IdleTime);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Idle Time Waiting For CAS", (float) totalIdletime / (float) 1000000 });
      }
			pt.addEvent("UimaEE", "process", "Idle Time Waiting For CAS", (int)(totalIdletime/1000000), "");
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
	protected CAS deserialize(String aSerializedCAS, CAS aCAS ) throws Exception
	{
		XmiSerializationSharedData deserSharedData = new XmiSerializationSharedData();
    uimaSerializer.deserializeCasFromXmi(aSerializedCAS, aCAS, deserSharedData, true, -1);
		return aCAS;
	}
	
	protected CAS deserialize(String aSerializedCAS, CAS aCAS, XmiSerializationSharedData deserSharedData, boolean deltaCas ) throws Exception
	{
		if (deltaCas) {
      uimaSerializer.deserializeCasFromXmi(aSerializedCAS, aCAS, deserSharedData, true, deserSharedData.getMaxXmiId(), AllowPreexistingFS.allow);
		} else {
      uimaSerializer.deserializeCasFromXmi(aSerializedCAS, aCAS, deserSharedData, true, -1);
		}
		return aCAS;
	}

  protected CAS deserialize(byte[] binaryData, ClientRequest cachedRequest) throws Exception
  {
    CAS cas = cachedRequest.getCAS();
    uimaSerializer.deserializeCasFromBinary(binaryData, cas);
    return cas;
  }
	
	protected CAS deserializeCAS(String aSerializedCAS, ClientRequest cachedRequest) throws Exception
	{
		CAS cas = cachedRequest.getCAS();
		return deserialize(aSerializedCAS, cas);
	}

  protected CAS deserializeCAS(byte[] aSerializedCAS, ClientRequest cachedRequest) throws Exception
  {
    CAS cas = cachedRequest.getCAS();
    uimaSerializer.deserializeCasFromBinary(aSerializedCAS, cas);
    return cas;
  }

  protected CAS deserializeCAS(byte[] aSerializedCAS, CAS aCas) throws Exception
  {
    uimaSerializer.deserializeCasFromBinary(aSerializedCAS, aCas);
    return aCas;
  }

  protected CAS deserializeCAS(String aSerializedCAS, ClientRequest cachedRequest, boolean deltaCas) throws Exception
	{
		CAS cas = cachedRequest.getCAS();
		return deserialize(aSerializedCAS, cas, cachedRequest.getXmiSerializationSharedData(), deltaCas);
	}
	protected CAS deserializeCAS(String aSerializedCAS, String aCasPoolName) throws Exception
	{
		CAS cas = asynchManager.getNewCas(aCasPoolName);
		return deserialize(aSerializedCAS, cas);
	}
  protected CAS deserializeCAS(byte[] aSerializedCAS, String aCasPoolName ) throws Exception
  {
    CAS cas = asynchManager.getNewCas(aCasPoolName);
    uimaSerializer.deserializeCasFromBinary(aSerializedCAS, cas);
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

      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_msg_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
      }
			if ( !message.propertyExists(AsynchAEMessage.Command) )
			{
			  return;
			}
			
			
			int command = message.getIntProperty(AsynchAEMessage.Command);
			if (AsynchAEMessage.CollectionProcessComplete == command)
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_cpc_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
        }
				handleCollectionProcessCompleteReply(message);
			}
			else if (AsynchAEMessage.GetMeta == command)
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
        }
				handleMetadataReply(message);
			}
			else if (AsynchAEMessage.Process == command)
			{
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_process_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
        }
				handleProcessReply(message, true, null);
			}
//			System.out.println("#### Client Completed Processing Of the Message. Waiting For Next Message...");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the ProcessingResourceMetadata for the asynchronous AnalysisEngine.
	 */
	public ProcessingResourceMetaData getMetaData() throws ResourceInitializationException
	{
		return resourceMetadata;
	}
	/**
	 * This is a synchronous method which sends a message to a destination and blocks waiting for
	 * a reply. 
	 */
	public String sendAndReceiveCAS(CAS aCAS, ProcessTrace pt) throws ResourceProcessException {
    if (!running) {
      throw new ResourceProcessException(new Exception("Uima EE Client Not In Running State"));
    }
    if ( !serviceDelegate.isSynchronousAPI() ) {
      //  Change the flag to indicate synchronous invocation.
      //  This info will be needed to handle Ping replies.
      //  Different code is used for handling PING replies for 
      //  sync and async API.
      serviceDelegate.setSynchronousAPI();
    }
    String casReferenceId = null;
    // keep handle to CAS, we'll deserialize into this same CAS later
    sendAndReceiveCAS = aCAS;

    ThreadMonitor threadMonitor = null;

    if (threadMonitorMap.containsKey(Thread.currentThread().getId())) {
      threadMonitor = (ThreadMonitor) threadMonitorMap.get(Thread.currentThread().getId());
    } else {
      threadMonitor = new ThreadMonitor(Thread.currentThread().getId());
      threadMonitorMap.put(Thread.currentThread().getId(), threadMonitor);
    }

    ClientRequest cachedRequest = produceNewClientRequestObject();
    cachedRequest.setSynchronousInvocation();
    // send CAS. This call does not block. Instead we will block the sending thread below.
    casReferenceId = sendCAS(aCAS, cachedRequest);
    if (threadMonitor != null && threadMonitor.getMonitor() != null) {
      // Block here waiting for reply
      synchronized (threadMonitor.getMonitor()) {
        //  Block sending thread until a reply is received. The thread
        //  will be signaled either when a reply to the request just
        //  sent is received OR a Ping reply was received. The latter
        //  is necessary to allow handling of CASes delayed due to
        //  a timeout. A previous request timed out and the service
        //  state was changed to TIMEDOUT. While the service is in this
        //  state all sending threads add outstanding CASes to the list
        //  of CASes pending dispatch and each waits until the state
        //  of the service changes to OK. The state is changed to OK
        //  when the client receives a reply to a PING request. When
        //  the Ping reply comes, the client will signal this thread.
        //  The thread checks the list of CASes pending dispatch trying
        //  to find an entry that matches ID of the CAS previously 
        //  delayed. If the CAS is found in the delayed list, it will 
        //  be removed from the list and send to the service for 
        //  processing. The 'wasSignaled' flag is only set when the  
        //  CAS reply is received. Ping reply logic does not change
        //  this flag.
        while (!threadMonitor.wasSignaled && running) {
          try {
            threadMonitor.getMonitor().wait();
            //  Send thread was awoken by either process reply or ping reply 
            //  If there service is in the ok state and the CAS is in the
            //  list of CASes pending dispatch, remove the CAS from the list
            //  and send it to the service.
            if ( running && serviceDelegate.getState() == Delegate.OK_STATE && 
                 serviceDelegate.removeCasFromPendingDispatchList(casReferenceId)) {
              sendCAS(aCAS, cachedRequest);
            }
          } catch (InterruptedException e) {
          }
        }
      }
    }
    if ( abort ) {
      throw new ResourceProcessException(new RuntimeException("Uima AS Client API Stopping"));
    }
    try {
      // check if timeout exception
      if (cachedRequest.isTimeoutException()) {
        throw new ResourceProcessException(new UimaASProcessCasTimeout());
      }
      // Process reply in the send thread
      Message message = cachedRequest.getMessage();
      deserializeAndCompleteProcessingReply(casReferenceId, message, cachedRequest, pt, false);
    } catch (ResourceProcessException rpe) {
      throw rpe;
    } catch (Exception e) {
      throw new ResourceProcessException(e);
    } finally {
      //  reset 'wasSignaled' flag
      threadMonitor.reset();
    }
    return casReferenceId;
  }
	private void deserializeAndCompleteProcessingReply( String casReferenceId, Message message, ClientRequest cachedRequest, ProcessTrace pt, boolean doNotify ) throws Exception
	{
		int payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
		if ( message.propertyExists(AsynchAEMessage.CasSequence) )
		{
			handleProcessReplyFromCasMultiplier( message, casReferenceId, payload);//, cachedRequest);
		}
		else
		{
			long t1 = System.nanoTime();
			boolean deltaCas = false;
			if (message.propertyExists(AsynchAEMessage.SentDeltaCas)) {
				deltaCas = message.getBooleanProperty(AsynchAEMessage.SentDeltaCas);
			}
			CAS cas = null;
			if ( message instanceof TextMessage ) {
	      cas = deserializeCAS(((TextMessage) message).getText(), cachedRequest, deltaCas);
			} else {
			  long bodyLength = ((BytesMessage) message).getBodyLength();
			  byte[] serializedCas = new byte[(int)bodyLength];
			  ((BytesMessage) message).readBytes(serializedCas);
		    cas = deserializeCAS(serializedCas, cachedRequest);
			}
			cachedRequest.setDeserializationTime(System.nanoTime() - t1);
			completeProcessingReply( cas, casReferenceId, payload, doNotify,  message, cachedRequest, pt);
		}
	}

	public String sendAndReceiveCAS(CAS aCAS) throws ResourceProcessException
	{
		return sendAndReceiveCAS( aCAS, null );
	}

	protected void notifyOnTimout(CAS aCAS, String anEndpoint, int aTimeoutKind, String casReferenceId)
	{

		ProcessTrace pt = new ProcessTrace_impl();
		UimaASProcessStatusImpl status = new UimaASProcessStatusImpl(pt,casReferenceId);

		switch (aTimeoutKind)
		{
		case (MetadataTimeout):
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_meta_timeout_INFO", new Object[] { anEndpoint });
      }
			status.addEventStatus("GetMeta", "Failed", new UimaASMetaRequestTimeout());
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
			synchronized (metadataReplyMonitor)
			{
				abort = true;
				metadataReplyMonitor.notifyAll();
			}
			break;
    case (PingTimeout):
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_meta_timeout_INFO", new Object[] { anEndpoint });
      }
      status.addEventStatus("Ping", "Failed", new UimaASPingTimeout());
      notifyListeners(null, status, AsynchAEMessage.Ping);
      //  The main thread could be stuck waiting for a CAS. Grab any CAS in the
      //  client cache and release it so that we can shutdown.
      if ( !clientCache.isEmpty()) {
        ClientRequest anyCasRequest = clientCache.elements().nextElement();
        if ( anyCasRequest.getCAS() != null ) {
          anyCasRequest.getCAS().release();
        }
      }
      synchronized (metadataReplyMonitor)
      {
        abort = true;
        metadataReplyMonitor.notifyAll();
      }
      break;
		case (CpCTimeout):
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_timeout_INFO", new Object[] { anEndpoint });
      }
			status.addEventStatus("CpC", "Failed", new UimaASCollectionProcessCompleteTimeout());
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
			receivedCpcReply = true;
			synchronized(endOfCollectionMonitor)
			{
				endOfCollectionMonitor.notifyAll();
			}
			break;

		case (ProcessTimeout):
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_process_timeout_INFO", new Object[] { anEndpoint });
      }
		  ClientRequest cachedRequest = (ClientRequest)clientCache.get(casReferenceId);

		  if ( cachedRequest == null )
		  {
		    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	        // if missing for any reason ...
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(), "handleProcessReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
	                new Object[] { anEndpoint, casReferenceId });
		    }
		    return;
		  }
		  //  Store the total latency for this CAS. The departure time is set right before the CAS
		  //  is sent to a service.
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
		  else 
		  {
			  // notify the application listener with the error
			  exc = new UimaASProcessCasTimeout();
			  status.addEventStatus("Process", "Failed", exc);
			  notifyListeners(aCAS, status, AsynchAEMessage.Process);
		  }
		  cachedRequest.removeEntry(casReferenceId);
		  serviceDelegate.removeCasFromOutstandingList(casReferenceId);
		  synchronized (gater) 
		  {
			  if (howManyBeforeReplySeen > 0) 
			  {
				  howManyBeforeReplySeen--;
			  }
			  gater.notifyAll();
			  howManyRecvd++; // increment global counter to enable CPC request to be sent when howManySent = howManyRecvd
	      if (howManyRecvd == howManySent)
	      {
	        synchronized (cpcGate)
	        {
	          cpcGate.notifyAll();
	        }
	      }
		  }
		  break;
		}  // case
 	}
	/**
	 * @override
	 */
	protected MessageProducer getMessageProducer( Destination destination ) throws Exception
	{
		return null;
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
		
		private XmiSerializationSharedData sharedData;

    private byte[] binaryCas = null;
    
    private volatile boolean isBinaryCas = false;

		
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
			sharedData = null;
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
		public boolean isRemote() {
		  return isRemote;
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
		
		public void setBinaryCAS(byte[] aBinaryCas) {
		  binaryCas = aBinaryCas;
		  isBinaryCas = true;
		}
		public boolean isBinaryCAS() {
		  return isBinaryCas;
		}
		public byte[] getBinaryCAS() {
		  return binaryCas;
		}
		public String getXmiCAS() {
		  return serializedCAS;
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

			if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "startTimer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_starting_timer_FINEST", new Object[] { endpoint });
			}
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run()
				{
		      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
		        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "run", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_expired_INFO", new Object[] { endpoint, casReferenceId });
		      }
					CAS cas = null;
					if (isSerializedCAS)
					{
						try
						{
							if (isRemote)
							{
							  if ( isBinaryCas ) {
							    cas = deserialize(binaryCas, _clientReqRef);
							  } else {
	                cas = deserializeCAS(serializedCAS, _clientReqRef);
							  }
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
		
		public XmiSerializationSharedData getXmiSerializationSharedData() {
			return sharedData;
		}
		
		public void setXmiSerializationSharedData(XmiSerializationSharedData data) {
			this.sharedData = data;
		}
	}

	protected class ThreadMonitor
	{
		private long threadId;
		private Object monitor = new Object();
		private volatile boolean wasSignaled = false;
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
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "onException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_error_while_sending_msg__WARNING", new Object[] {  aDestination, aFailure });
    }
		stop();
	}
	/**
	 * @override
	 */
	protected void setReleaseCASMessage(TextMessage msg, String aCasReferenceId)
	throws Exception 
	{
	}

}
