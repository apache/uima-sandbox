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
import org.apache.uima.aae.error.UimaEECollectionProcessCompleteTimeout;
import org.apache.uima.aae.error.UimaEEMetaRequestTimeout;
import org.apache.uima.aae.error.UimaEEProcessCasTimeout;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.EntityProcessStatus;
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
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.client.UimaEEProcessStatusImpl;
import org.apache.uima.aae.client.UimaEEStatusCallbackListener;

public abstract class BaseUIMAAsynchronousEngineCommon_impl 
implements UimaAsynchronousEngine, MessageListener
{
	private static final Class CLASS_NAME = BaseUIMAAsynchronousEngineCommon_impl.class;

	protected static final int MetadataTimeout = 1;

	protected static final int CpCTimeout = 2;

	protected static final int ProcessTimeout = 3;

	protected boolean initialized;

	protected List listeners = new ArrayList();

	protected MessageProducer producer;

	protected AsynchAECasManager asynchManager;

	protected Object endOfCollectionMonitor = new Object();

	protected Object metadataReplyMonitor = new Object();

	protected String brokerURI = null;

	protected boolean remoteService = false;

	protected Session session = null;

	protected Connection connection = null;

	protected Session consumerSession = null;

	protected Queue consumerDestination = null;

	protected Session producerSession = null;

	protected Object gater = new Object();

	protected int howManyBeforeReplySeen = 0;

	protected int receiveWindow = 5;

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

	protected boolean error;

	protected Exception exc;

	protected long howManySent = 0;

	protected long howManyRecvd = 0;

	protected Object cpcGate = new Object();

	protected ConcurrentHashMap springContainerRegistry = new ConcurrentHashMap();

	protected boolean receivedMetaReply;

	protected boolean receivedCpcReply;

	protected MessageConsumer consumer = null;
	
	abstract public String getEndPointName() throws Exception;
	abstract protected TextMessage createTextMessage() throws Exception;
	abstract protected void setMetaRequestMessage(TextMessage msg) throws Exception;
	abstract protected void setCASMessage(String casReferenceId, CAS aCAS, TextMessage msg) throws Exception;
	abstract public void setCPCMessage(TextMessage msg) throws Exception;
	abstract public void initialize(Map anApplicationContext) throws ResourceInitializationException;
	abstract public void initialize(String[] configFiles, Map anApplicationContext) throws ResourceInitializationException;
	abstract protected void cleanup() throws Exception;
	abstract public String deploy(String[] aDeploymentDescriptorList, Map anApplicationContext) throws Exception;
	abstract protected String deploySpringContainer(String[] springContextFiles) throws ResourceInitializationException;


	public void addStatusCallbackListener(UimaEEStatusCallbackListener aListener)
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

	public void removeStatusCallbackListener(UimaEEStatusCallbackListener aListener)
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
		
	public void collectionProcessingComplete() throws ResourceProcessException
	{
		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_app_cpc_request_FINEST", new Object[] {});

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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_request_not_done_INFO", new Object[] {});

				return;
			}

			ClientRequest requestToCache = new ClientRequest(uniqueIdentifier, this); //, timeout);
			requestToCache.setIsRemote(remoteService);
			requestToCache.setCPCRequest(true);
			requestToCache.setCpcTimeout(cpcTimeout);
			
//			requestToCache.setEndpoint(((ActiveMQDestination) producer.getDestination()).getPhysicalName());
			requestToCache.setEndpoint(getEndPointName());
			
			clientCache.put(uniqueIdentifier, requestToCache);

			if (cpcTimeout > 0)
			{
				requestToCache.startTimer();
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_started_cpc_request_timer_FINEST", new Object[] {});

			TextMessage msg = createTextMessage();
			setCPCMessage(msg);
		    producer.send(msg);
		    			
			// Wait for CPC Reply. This blocks!
			waitForCpcReply();

			cancelTimer(uniqueIdentifier);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "collectionProcessingComplete", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cancelled_cpc_request_timer_FINEST", new Object[] {});
	
			if (running)
			{
				for (int i = 0; listeners != null && i < listeners.size(); i++)
				{
					((UimaEEStatusCallbackListener) listeners.get(i)).collectionProcessComplete(null);
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
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_as_client_INFO", new Object[] {});

		if (!running)
		{
			return;
		}

		running = false;
		
		try
		{
			producerSession.close();
			consumerSession.close();
			consumer.close();
			session.close();
			connection.close();
			connection = null;

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
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "stop", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_as_client_INFO", new Object[] {});
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
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_request_for_cas_FINEST", new Object[] {});
		if (!initialized)
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
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "getCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_new_cas_FINEST", new Object[] {});

		return cas;
	}

	
	protected void reset()
	{
		receivedCpcReply = false;
		receivedMetaReply = false;
	}

	
	protected void sendMetaRequest() throws Exception
	{
		TextMessage msg = createTextMessage();
		setMetaRequestMessage(msg);		

		ClientRequest requestToCache = new ClientRequest(uniqueIdentifier, this); //, metadataTimeout);
		requestToCache.setIsRemote(remoteService);
		requestToCache.setMetaRequest(true);
		requestToCache.setMetadataTimeout(metadataTimeout);

		requestToCache.setEndpoint(getEndPointName());

		clientCache.put(uniqueIdentifier, requestToCache);
		if (metadataTimeout > 0)
		{
			requestToCache.startTimer();
		}

		producer.send(msg);		

//		UIMAFramework.getLogger(CLASS_NAME)
//				.logrb(Level.FINEST, CLASS_NAME.getName(), "sendMetaRequest", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_as_meta_request_sent_FINEST", new Object[] { ((ActiveMQDestination) producer.getDestination()).getPhysicalName(), "brokerURI" });
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

	public void process() throws ResourceProcessException
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
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "waitUntilReadyToSendMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_blocking_on_semaphore_FINEST", new Object[] { "Gater" });
					try
					{
						// This monitor is dedicated to single purpose event.
						gater.wait();
					}
					catch (Exception e)
					{
					}
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "waitUntilReadyToSendMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_done_blocking_on_semaphore_FINEST", new Object[] { "Gater" });
			}
			howManyBeforeReplySeen++;
		}

	}


	/**
	 * Sends a given CAS for analysis to the UIMA EE Service.
	 * 
	 */
	public synchronized String sendCAS(CAS aCAS) throws ResourceProcessException
	{
		String casReferenceId = null;
		try
		{
			waitUntilReadyToSendMessage(AsynchAEMessage.Process);

			if (!running)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "sendCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_not_sending_cas_INFO", new Object[] { "Asynchronous Client is Stopping" });
				return null;
			}

			TextMessage msg = createTextMessage();
			casReferenceId = idGenerator.nextId();
			setCASMessage(casReferenceId, aCAS, msg);
			
			ClientRequest requestToCache = new ClientRequest(casReferenceId, this); //, timeout);
			requestToCache.setIsRemote(remoteService);
			requestToCache.setEndpoint(getEndPointName());
			requestToCache.setProcessTimeout(processTimeout);
			requestToCache.setThreadId(Thread.currentThread().getId());

			if (remoteService)
			{
				requestToCache.setCAS(aCAS);
				//	Store the serialized CAS in case the timeout occurs and need to send the 
				//	the offending CAS to listeners for reporting
				requestToCache.setCAS(msg.getText());
			}

			clientCache.put(casReferenceId, requestToCache);

			if (processTimeout > 0)
			{
				requestToCache.startTimer();
			}

//			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "sendCAS", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_cas_FINEST",
//					new Object[] { msg.getStringProperty(AsynchAEMessage.CasReference), brokerURI, ((ActiveMQDestination) producer.getDestination()).getPhysicalName() });

			producer.send(msg);
			howManySent++;
		}
		catch (Exception e)
		{
			throw new ResourceProcessException(e);
		}
//		finally
//		{
//			if (remoteService && aCAS != null && sendAndReceiveCAS == null)
//			{
//				aCAS.release();
//			}
//		}
		return casReferenceId;
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
			UimaEEProcessStatusImpl status = new UimaEEProcessStatusImpl(pt);

			Exception exception = (Exception) ((ObjectMessage) message).getObject();
			status.addEventStatus("CpC", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleCollectionProcessCompleteReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
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
			UimaEEProcessStatusImpl status = new UimaEEProcessStatusImpl(pt);

			Exception exception = (Exception) ((ObjectMessage) message).getObject();
			status.addEventStatus("GetMeta", "Failed", exception);
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
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

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleMetadataReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), meta });

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
			UimaEEStatusCallbackListener statCL = (UimaEEStatusCallbackListener) listeners.get(i);
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
			request.cancelTimer();
		}
	}

	/**
	 * Handles response to Process CAS request. If the message originated in a service that is running in a separate jvm (remote), deserialize the CAS and notify the application of the completed analysis via application listener.
	 * 
	 * @param message -
	 *            jms message containing serialized CAS
	 * 
	 * @throws Exception
	 */
	protected void handleProcessReply(Message message) throws Exception
	{
		CAS cas = null;
		int payload = -1;
		ClientRequest cachedRequest = null;
		String casReferenceId = null;
		
		try
		{
			howManyRecvd++;
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_process_reply_FINEST",
					new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });

			casReferenceId = message.getStringProperty(AsynchAEMessage.CasReference);
			ProcessTrace pt = new ProcessTrace_impl();

			if (message.propertyExists(AsynchAEMessage.Payload))
			{
				payload = ((Integer) message.getIntProperty(AsynchAEMessage.Payload)).intValue();
			}
			

			UimaEEProcessStatusImpl status = new UimaEEProcessStatusImpl(pt);
			if (AsynchAEMessage.Exception == payload)
			{
				Exception exception = (Exception) ((ObjectMessage) message).getObject();
				status.addEventStatus("Process", "Failed", exception);
				
				receivedCpcReply = true; // change state as if the CPC reply came in. This is done to prevent a hang on CPC request 
				synchronized(endOfCollectionMonitor)
				{
					endOfCollectionMonitor.notifyAll();
				}
				
				notifyListeners(null, status, AsynchAEMessage.Process);
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_exception_msg_INFO",
						new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference), exception });
				return;
			}
			if (casReferenceId != null && !clientCache.containsKey(casReferenceId))
			{
				// Most likely expire message. Already handled as timeout. Discard the message and move on to the next
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_expired_msg_INFO",
						new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), message.getStringProperty(AsynchAEMessage.CasReference) });
				return;
			}

			cachedRequest = (ClientRequest)clientCache.get(casReferenceId);
			
			if (AsynchAEMessage.XMIPayload == payload || AsynchAEMessage.CASRefID == payload)
			{
				cancelTimer(casReferenceId);
				if (message.propertyExists(AsynchAEMessage.TimeToSerializeCAS))
				{
					long timeToSerializeCAS = ((Long) message.getLongProperty(AsynchAEMessage.TimeToSerializeCAS)).longValue();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Serialize Cas", (float) timeToSerializeCAS / (float) 1000000 });

				}
				if (message.propertyExists(AsynchAEMessage.TimeToDeserializeCAS))
				{
					long timeToDeserializeCAS = ((Long) message.getLongProperty(AsynchAEMessage.TimeToDeserializeCAS)).longValue();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time To Deserialize Cas", (float) timeToDeserializeCAS / (float) 1000000 });
				}
				if (message.propertyExists(AsynchAEMessage.TimeWaitingForCAS))
				{
					long timeWaitingForCAS = ((Long) message.getLongProperty(AsynchAEMessage.TimeWaitingForCAS)).longValue();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time to Wait for CAS", (float) timeWaitingForCAS / (float) 1000000 });
				}
				if (message.propertyExists(AsynchAEMessage.TimeInService))
				{
					long ttimeInService = ((Long) message.getLongProperty(AsynchAEMessage.TimeInService)).longValue();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Time In Service", (float) ttimeInService / (float) 1000000 });
				}
				if (message.propertyExists(AsynchAEMessage.TotalTimeSpentInAnalytic))
				{
					long totaltimeInService = ((Long) message.getLongProperty(AsynchAEMessage.TotalTimeSpentInAnalytic)).longValue();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "handleProcessReply", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_detail_FINEST",
							new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom), "Total Time In Service", (float) totaltimeInService / (float) 1000000 });
				}

				// If the analysis service is remote deserialize the CAS
				if (remoteService)
				{
					cas = deserializeCAS(((TextMessage) message).getText(), cachedRequest);
				}
				notifyListeners(cas, status, AsynchAEMessage.Process);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			
/*			
			if ( cachedRequest != null )
			{
				
				//	Signal a thread that we received a reply
				if ( threadMonitorMap.containsKey(cachedRequest.getThreadId()))
				{
					ThreadMonitor threadMonitor = (ThreadMonitor) threadMonitorMap.get(cachedRequest.getThreadId());
					
					synchronized( threadMonitor.getMonitor() )
					{
						threadMonitor.setWasSignaled();
						cachedRequest.setReceivedProcessCasReply();
						threadMonitor.getMonitor().notifyAll();
						if (cas != null)
						{
							cas.release();
						}
					}
					
				}
*/
			if ( cachedRequest != null )
			{
				//	Signal a thread that we received a reply
				if ( threadMonitorMap.containsKey(cachedRequest.getThreadId()))
				{
					ThreadMonitor threadMonitor = (ThreadMonitor) threadMonitorMap.get(cachedRequest.getThreadId());
					
					synchronized( threadMonitor.getMonitor() )
					{
						threadMonitor.setWasSignaled();
						cachedRequest.setReceivedProcessCasReply();
						threadMonitor.getMonitor().notifyAll();
					}
					
				}
			}
			if (cas != null)
			{
				cas.release();
			}
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

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_msg_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
			int command = message.getIntProperty(AsynchAEMessage.Command);
			if (AsynchAEMessage.CollectionProcessComplete == command)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_cpc_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
				handleCollectionProcessCompleteReply(message);
			}
			else if (AsynchAEMessage.GetMeta == command)
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_meta_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });
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
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "onMessage", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_received_process_reply_FINEST", new Object[] { message.getStringProperty(AsynchAEMessage.MessageFrom) });

				handleProcessReply(message);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Below methods added by Adam
	/**
	 * Gets the ProcessingResourceMetadata for the asynchronous AnalysisEngine.
	 */
	public ProcessingResourceMetaData getMetaData() throws ResourceInitializationException
	{
		return resourceMetadata;
	}

	public void sendAndReceiveCAS(CAS aCAS) throws ResourceProcessException
	{
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
			
			// send CAS
			casReferenceId = sendCAS(aCAS);

			ClientRequest cachedRequest = null;
			if ( clientCache.containsKey(casReferenceId) )
			{
				cachedRequest = (ClientRequest)clientCache.get(casReferenceId);
			}
			else
			{
				throw new ResourceProcessException(new Exception("No Entry For Cas Reference Id:"+casReferenceId+" In the Client Cache"));
			}
			synchronized (threadMonitor.getMonitor())
			{

				while (!threadMonitor.wasSignaled && running && !error)
				{
					try
					{
						threadMonitor.getMonitor().wait();
					}
					catch (InterruptedException e)
					{
					}
				}
				clientCache.remove(cachedRequest.getCasReferenceId());
				threadMonitor.reset();
				if (error && exc != null)
				{
					throw new ResourceProcessException(exc);
				}
				error = false;
			}
	}

	protected void notifyOnTimout(CAS aCAS, String anEndpoint, int aTimeoutKind)
	{

		ProcessTrace pt = new ProcessTrace_impl();
		UimaEEProcessStatusImpl status = new UimaEEProcessStatusImpl(pt);

		switch (aTimeoutKind)
		{
		case (MetadataTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_meta_timeout_INFO", new Object[] { anEndpoint });

			status.addEventStatus("GetMeta", "Failed", new UimaEEMetaRequestTimeout());
			notifyListeners(null, status, AsynchAEMessage.GetMeta);
			synchronized (metadataReplyMonitor)
			{
				abort = true;
				metadataReplyMonitor.notifyAll();
			}
			break;
		case (CpCTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_timeout_INFO", new Object[] { anEndpoint });
			status.addEventStatus("CpC", "Failed", new UimaEECollectionProcessCompleteTimeout());
			notifyListeners(null, status, AsynchAEMessage.CollectionProcessComplete);
			receivedCpcReply = true;
			synchronized(endOfCollectionMonitor)
			{
				endOfCollectionMonitor.notifyAll();
			}
			break;

		case (ProcessTimeout):
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "notifyOnTimout", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_process_timeout_INFO", new Object[] { anEndpoint });
			exc = new UimaEEProcessCasTimeout();
			status.addEventStatus("Process", "Failed", exc);
			notifyListeners(aCAS, status, AsynchAEMessage.Process);

			if (sendAndReceiveCAS != null)
			{
				synchronized (sendAndReceiveCasMonitor)
				{
					error = true;
					sendAndReceiveCasMonitor.notifyAll();
				}
				sendAndReceiveCAS = aCAS;
			}
			else
			{
				synchronized (gater)
				{
					if (howManyBeforeReplySeen > 0)
					{
						howManyBeforeReplySeen--;
					}
					gater.notifyAll();
				}
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
		
		public ClientRequest(String aCasReferenceId, BaseUIMAAsynchronousEngineCommon_impl aUimaEEEngine) //, long aTimeout)
		{
//			timeout = aTimeout;
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

			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "startTimer", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_starting_timer_FINEST", new Object[] { endpoint });
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run()
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(), "run", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timer_expired_INFO", new Object[] { endpoint, casReferenceId });
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
					removeEntry(casReferenceId);
					int timeOutKind;
					if (isMetaRequest())
					{
						timeOutKind = MetadataTimeout;
						initialized = false;
						abort = true;
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
					}
					uimaEEEngine.notifyOnTimout(cas, endpoint, timeOutKind);
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
	private static final String copyright = com.ibm.uima.copyright.Copyright.COPYRIGHT;
}
