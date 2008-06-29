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

package org.apache.uima.adapter.jms.activemq;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ServiceNotFoundException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.OutputChannel;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.jmx.ServicePerformance;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.aae.monitor.Monitor;
import org.apache.uima.aae.monitor.statistics.LongNumericStatistic;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.util.Level;

public class JmsOutputChannel implements OutputChannel
{
	
	private static final Class CLASS_NAME = JmsOutputChannel.class;
	private static final long INACTIVITY_TIMEOUT = 1800000;  // 30 minutes in term of millis
	private CountDownLatch controllerLatch = new CountDownLatch(1);
	
	private ActiveMQConnectionFactory connectionFactory;
	//	Name of the external queue this service uses to receive messages
	private String serviceInputEndpoint;
	//	Name of the internal queue this services uses to receive messages from delegates
	private String controllerInputEndpoint;
	//	Name of the queue used by Cas Multiplier to receive requests to free CASes
	private String secondaryInputEndpoint;
	//	The service controller
	private AnalysisEngineController analysisEngineController;
	//	Cache containing connections to destinations this service interacts with
	//	Each entry in this cache has an inactivity timer that times amount of time
	//	elapsed since the last time a message was sent to the destination. 
	private ConcurrentHashMap connectionMap = new ConcurrentHashMap();
	
	private String serverURI;
	
	private String serviceProtocolList ="";

	private boolean aborting = false;
	
	private Destination freeCASTempQueue;
	/**
	 * Sets the ActiveMQ Broker URI 
	 */
	public void setServerURI( String aServerURI )
	{
		serverURI = aServerURI;
	}
	protected void setFreeCasQueue( Destination destination)
	{
		freeCASTempQueue = destination;
	}
	public String getServerURI()
	{
		return System.getProperty("BrokerURI");
	}
	public String getName()
	{
		return "";
	}
	/**
	 * 
	 * @param connectionFactory
	 */
	public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory)
	{
		this.connectionFactory = connectionFactory;
	}

	public void setServiceInputEndpoint(String anEnpoint)
	{
		serviceInputEndpoint = anEnpoint;
	}
	
	public void setSecondaryInputQueue( String anEndpoint )
	{
		secondaryInputEndpoint = anEndpoint;
	}

	public ActiveMQConnectionFactory getConnectionFactory()
	{
		return this.connectionFactory;
	}
	
	
	public void initialize() throws AsynchAEException
	{
		if ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connector_list__FINE",
                    new Object[] { System.getProperty("ActiveMQConnectors") });
			//	Aggregate controller set this System property at startup in
			//  org.apache.uima.adapter.jms.service.UIMA_Service.startInternalBroker()
			serviceProtocolList = System.getProperty("ActiveMQConnectors");
		}

		try
		{
			String uri = System.getProperty("BrokerURI");
			setServerURI(uri);
		}
		catch( Exception e) 
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
                    new Object[] { JmsConstants.threadName(), e });
		}
		
	}
	/**
	 * Serializes CAS using indicated Serializer.
	 * 
	 * @param aCAS - CAS instance to serialize
	 * @param aSerializerKey - a key identifying which serializer to use
	 * @return - String - serialized CAS as String
	 * @throws Exception
	 */
	public String serializeCAS(boolean isReply, CAS aCAS, String aCasReferenceId, String aSerializerKey) throws Exception
	{
		
		long start = System.nanoTime();
		
		String serializedCas = null;
		
		if ( isReply || "xmi".equalsIgnoreCase(aSerializerKey ) )
		{

			CacheEntry cacheEntry = 
				getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				 
			XmiSerializationSharedData serSharedData;
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "serializeCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_serialize_cas__FINE",
                    new Object[] {aCasReferenceId});
			if ( isReply )
			{
				serSharedData = cacheEntry.getDeserSharedData();
			    serializedCas = UimaSerializer.serializeCasToXmi(aCAS, serSharedData);
			}
			else
			{
				serSharedData = cacheEntry.getDeserSharedData();//new XmiSerializationSharedData();
				if (serSharedData == null) {
					serSharedData = new XmiSerializationSharedData();
					cacheEntry.setXmiSerializationData(serSharedData);
				}
			    serializedCas = UimaSerializer.serializeCasToXmi(aCAS, serSharedData);
			    int maxOutgoingXmiId = serSharedData.getMaxXmiId();				
				//	Save High Water Mark in case a merge is needed
			    getAnalysisEngineController().
					getInProcessCache().
						getCacheEntryForCAS(aCasReferenceId).
							setHighWaterMark(maxOutgoingXmiId);
			}
			
		}
		else if ( "xcas".equalsIgnoreCase(aSerializerKey))
		{
			//	Default is XCAS
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try
			{
				UimaSerializer.serializeToXCAS(bos, aCAS, null, null, null);
				serializedCas = bos.toString();
			}
			catch ( Exception e)
			{
				throw e;
			}
			finally
			{
				bos.close();
			}
		}
//		if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
//		{
//			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
//	                "serializeCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_dump_serialized_cas__FINEST",
//	                new Object[] {aCasReferenceId, serializedCas});
//		}

		LongNumericStatistic statistic;
		if ( (statistic = getAnalysisEngineController().getMonitor().getLongNumericStatistic("",Monitor.TotalSerializeTime)) != null )
		{
			statistic.increment(System.nanoTime() - start);
		}
		
		return serializedCas;
	}
	/**
	 * This method verifies that the destination (queue) exists. It opens
	 * a connection the a broker, creates a session and a message producer.
	 * Finally, using the message producer, sends an empty message to 
	 * a queue. This API support enables checking for existence of the
	 * reply (temp) queue before any processing of a cas is done. This is
	 * an optimization to prevent expensive processing if the client
	 * destination is no longer available.
	 */
	public void bindWithClientEndpoint( Endpoint anEndpoint ) throws Exception
	{
	  // check if the reply endpoint is a temp destination
	  if ( anEndpoint.getDestination() != null )
	  {
	    // create message producer if one doesnt exist for this destination
	    JmsEndpointConnection_impl endpointConnection = 
	      getEndpointConnection(anEndpoint);
	    // Create empty message
	    TextMessage tm = endpointConnection.produceTextMessage("");
	    // test sending a message to reply endpoint. This tests existence of
	    // a temp queue. If the client has been shutdown, this will fail
	    // with an exception.
	    endpointConnection.send(tm, false);
	  }
	}
	/**
	 * Returns {@link JmsEndpointConnection_impl} instance bound to a destination defined in the {@link Endpoint}
	 * The endpoint identifies the destination that should receive the message. This method refrences a cache
	 * that stores active connections. Active connections are those that are fully bound and being used for 
	 * communication. The key to locate the entry in the connection cache is the queue name + broker URI. This
	 * uniquely identifies the destination. If an entry does not exist in the cache, this routine will create
	 * a new connection, initialize it, and cache it for future use. The cache is purely for optimization, to
	 * prevent openinig a connection for every message which is a costly operation. Instead the connection is
	 * open, cached and reused. The {@link JmsEndpointConnection_impl} instance is stored in the cache, and
	 * uses a timer to make sure stale connection are removed. If a connection is not used in a given time
	 * interval, the connection is considered stale and is dropped from the cache.  
	 * 
	 * @param anEndpoint - endpoint configuration containing connection information to a destination
	 * @return - 
	 * @throws AsynchAEException
	 */
	private JmsEndpointConnection_impl getEndpointConnection( Endpoint anEndpoint ) 
	throws AsynchAEException, ServiceShutdownException, ConnectException
	{
		
		try
		{
			controllerLatch.await();
		}
		catch(InterruptedException e){}
		
		JmsEndpointConnection_impl endpointConnection=null;
		
		//	create a key to lookup the connection
		String key = anEndpoint.getEndpoint()+anEndpoint.getServerURI();
		String destination = anEndpoint.getEndpoint();
		if ( anEndpoint.getDestination() != null && anEndpoint.getDestination() instanceof ActiveMQDestination )
		{
			destination = ((ActiveMQDestination)anEndpoint.getDestination()).getPhysicalName();
		    key = destination;
		}
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_acquiring_connection_to_endpoint__FINE",
                new Object[] { getAnalysisEngineController().getComponentName(), destination, anEndpoint.getServerURI() });
//        new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint(), anEndpoint.getServerURI() });
		
		//	check the cache first
		if ( !connectionMap.containsKey(key) )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_create_new_connection__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(), destination, anEndpoint.getServerURI() });
		
			endpointConnection = new JmsEndpointConnection_impl(connectionMap, anEndpoint); //.getServerURI(), anEndpoint.getEndpoint(),anEndpoint.remove());
			if ( System.getProperty(JmsConstants.SessionTimeoutOverride) != null)
			{
				try
				{
					int overrideTimeoutValue = 
						Integer.parseInt(System.getProperty(JmsConstants.SessionTimeoutOverride));
					endpointConnection.setInactivityTimeout(overrideTimeoutValue);  // If the connection is not used within this interval it will be removed
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_override_connection_timeout__FINE",
		                    new Object[] { analysisEngineController, overrideTimeoutValue, destination, anEndpoint.getServerURI() });
				}
				catch( NumberFormatException e) { /* ignore. use the default */ }
			}
			else
			{
				endpointConnection.setInactivityTimeout(INACTIVITY_TIMEOUT);  // If the connection is not used within this interval it will be removed
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_timeout__FINE",
	                    new Object[] { analysisEngineController, INACTIVITY_TIMEOUT, destination, anEndpoint.getServerURI() });
			}
			endpointConnection.setAnalysisEngineController(getAnalysisEngineController());
			//	Connection is not in the cache, create a new connection, initialize it and cache it
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_open_new_connection_to_endpoint__FINE",
                    new Object[] { destination, anEndpoint.getServerURI() });
			endpointConnection.open();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_opened_to_endpoint__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(),destination, anEndpoint.getServerURI() });

			//	Cache the connection for future use. If not used, connections expire after 50000 millis 
			connectionMap.put( key, endpointConnection);
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_reusing_existing_connection__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(), destination, anEndpoint.getServerURI() });
			//	Retrieve connection from the connection cache
			endpointConnection = (JmsEndpointConnection_impl)connectionMap.get(key);
			// check the state of the connection and re-open it if necessary
			if ( endpointConnection != null && !endpointConnection.isOpen() )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connection_closed_reopening_endpoint__FINE",
	                    new Object[] { destination });
				endpointConnection.open();
			}
		}
		return endpointConnection;
	}

	/**
	 * Sends request message to a delegate.
	 * 
	 * @param aCommand - the type of request [Process|GetMeta]
	 * @param anEndpoint - the destination where the delegate receives messages 
	 * 
	 * @throws AsynchAEException
	 */
	public void sendRequest(int aCommand, String aCasReferenceId, Endpoint anEndpoint) throws AsynchAEException
	{
		try
		{
			
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);

			TextMessage tm = endpointConnection.produceTextMessage("");
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
			tm.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
			populateHeaderWithRequestContext(tm, anEndpoint, aCommand);
			
			// Only used to send a Stop or ReleaseCas request so probably no need to start a connection timer ?
			endpointConnection.send(tm, true);
			if ( aCommand == AsynchAEMessage.ReleaseCAS )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "sendRequest", 
						JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_release_cas_req__FINE", new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint(),aCasReferenceId });
			}
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_shutdown__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	/**
	 * Sends request message to a delegate.
	 * 
	 * @param aCommand - the type of request [Process|GetMeta]
	 * @param anEndpoint - the destination where the delegate receives messages 
	 * 
	 * @throws AsynchAEException
	 */
	public void sendRequest(int aCommand, Endpoint anEndpoint)
	{
		try
		{
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);

			TextMessage tm = endpointConnection.produceTextMessage(null);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
			tm.setText("");    // Need this to prevent the Broker from throwing an exception when sending a message to C++ service
			
			populateHeaderWithRequestContext(tm, anEndpoint, aCommand);
			boolean startTimer = false;
			//	Start timer for endpoints that are remote and are managed by a different broker
			//	than this service. If an endpoint contains a destination object, the outgoing
			//	request will contain a JMSReplyTo object which will point to a temp queue
			if ( anEndpoint.isRemote() && anEndpoint.getDestination() == null)
			{
				startTimer = true;
			}
			
			
			if ( endpointConnection.send(tm, startTimer) != true )
			{
				throw new ServiceNotFoundException();
			}
			
			
			if ( aCommand == AsynchAEMessage.CollectionProcessComplete )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "sendRequest", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_send_cpc_req__FINE", new Object[] { anEndpoint.getEndpoint() });
			}
			else if ( aCommand == AsynchAEMessage.ReleaseCAS )
			{
	            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_releasecas_request__endpoint__FINEST",
	                    new Object[] {getAnalysisEngineController().getName(), endpointConnection.getEndpoint()});
			}
			else 
			{
	            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_metadata_request__endpoint__FINEST",
	                    new Object[] { endpointConnection.getEndpoint(), endpointConnection.getServerUri() });
			}
		}
		catch ( Exception e)
		{
			if ( anEndpoint != null && aCommand == AsynchAEMessage.GetMeta )
			{
				anEndpoint.cancelTimer();
			}
			// Handle the error
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, aCommand);
			errorContext.add(AsynchAEMessage.Endpoint, anEndpoint);
			getAnalysisEngineController().getErrorHandlerChain().handle(e, errorContext, getAnalysisEngineController());
		}
	}

	public void sendRequest( String aCasReferenceId, Endpoint anEndpoint) throws AsynchAEException
	{
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_endpoint__FINE",
                new Object[] { anEndpoint.getEndpoint() });

		try
		{
			if (anEndpoint.isRemote())
			{
				long t1 = System.nanoTime();
				String serializedCAS = getSerializedCasAndReleaseIt(false, aCasReferenceId,anEndpoint, anEndpoint.isRetryEnabled());
				if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
				{
		            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
		                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_serialized_cas__FINEST",
		                    new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint(),aCasReferenceId,serializedCAS  });
				}
				
				
				//	Send process request to remote delegate and start timeout timer
				sendCasToRemoteEndpoint(true, serializedCAS, null, aCasReferenceId, anEndpoint, true, 0);
			}
			else
			{
				sendCasToCollocatedDelegate(true, aCasReferenceId, null, anEndpoint, true,0 );
			}
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}

		catch ( AsynchAEException e)
		{
			throw e;
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	/**
	 * Sends request message to process CAS to the given destinations. This method enables
	 * processing of the same CAS in multiple Analysis Engines at the same time. 
	 * 
	 * @param aCasReferenceId
	 * @param anEndpoint
	 * @throws AsynchAEException
	 */
	public void sendRequest( String aCasReferenceId, Endpoint[] endpoints) throws AsynchAEException
	{
		Endpoint currentEndpoint = null;
		try
		{
			boolean cacheSerializedCas = endpointRetryEnabled(endpoints);
			
			//	Serialize CAS using serializer defined in the first endpoint. All endpoints in the parallel Flow 
			//	must use the same format (either XCAS or XMI)
			String serializedCAS = getSerializedCasAndReleaseIt(false, aCasReferenceId, endpoints[0], cacheSerializedCas);
			//	Using provided endpoints, create JMS message for each destination and sent the serialized CAS to it.
			for( int i=0; i < endpoints.length; i++)
			{
				//	For remote delegates, optionally cache serialized CAS in case a retry on timeout is required.
				if (endpoints[i].isRemote())
				{

					currentEndpoint = endpoints[i];
					if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
					{
			            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
			                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_serialized_cas__FINEST",
			                    new Object[] { getAnalysisEngineController().getComponentName(), endpoints[i].getEndpoint(),aCasReferenceId,serializedCAS  });
					}
					
					
					
					
					
					sendCasToRemoteEndpoint(true, serializedCAS, null, aCasReferenceId, endpoints[i], true, 0);
				}
				else
				{
					//	Currently this use case is not supported. Parallel processing of CAS is only supported with remote Delegates
				}
			}
		}
		catch ( Exception e)
		{
			// Handle the error
			ErrorContext errorContext = new ErrorContext();
			errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
			errorContext.add(AsynchAEMessage.Endpoint, currentEndpoint);
			errorContext.add(AsynchAEMessage.CasReference, aCasReferenceId);
			getAnalysisEngineController().getErrorHandlerChain().handle(e, errorContext, getAnalysisEngineController());
		}
	}
	
	public void sendReply( CAS aCas, String anInputCasReferenceId,  String aNewCasReferenceId, Endpoint anEndpoint, long sequence ) throws AsynchAEException
	{
		try
		{
			anEndpoint.setReplyEndpoint(true);
			if ( anEndpoint.isRemote() )
			{
				//	Serializes CAS and releases it back to CAS Pool
				String serializedCAS = getSerializedCas(true, aNewCasReferenceId, anEndpoint, anEndpoint.isRetryEnabled());
				sendCasToRemoteEndpoint(false, serializedCAS, anInputCasReferenceId, aNewCasReferenceId, anEndpoint, false, sequence);
			}
			else
			{
	            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_reply_with_sequence__FINE",
	                    new Object[] { anEndpoint.getEndpoint(), aNewCasReferenceId, sequence });
				sendCasToCollocatedDelegate(false, anInputCasReferenceId, aNewCasReferenceId, anEndpoint, false, sequence);
			}
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}
		catch (AsynchAEException e)
		{
			throw e;
		}
		
		catch (Exception e)
		{
			throw new AsynchAEException(e);
		}
		
	}
	
	public void sendReply( CacheEntry entry, Endpoint anEndpoint ) throws AsynchAEException
	{
		try
		{
			anEndpoint.setReplyEndpoint(true);
			if ( anEndpoint.isRemote() )
			{
				//	Serializes CAS and releases it back to CAS Pool
				String serializedCAS = getSerializedCas(true, entry.getCasReferenceId(), anEndpoint, anEndpoint.isRetryEnabled());
				sendCasToRemoteEndpoint(false, serializedCAS, entry, anEndpoint, false);
			}
			else
			{
	            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_reply_with_sequence__FINE",
	                    new Object[] { anEndpoint.getEndpoint(), entry.getCasReferenceId(), entry.getCasSequence() });
				sendCasToCollocatedDelegate(false, entry.getInputCasReferenceId(), entry.getCasReferenceId(), anEndpoint, false, entry.getCasSequence());
			}
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}
		catch (AsynchAEException e)
		{
			throw e;
		}
		
		catch (Exception e)
		{
			throw new AsynchAEException(e);
		}
		
	}

	public void sendReply( int aCommand, Endpoint anEndpoint ) throws AsynchAEException
	{
		anEndpoint.setReplyEndpoint(true);
		try
		{
			if ( aborting )
			{
				return;
			}
			anEndpoint.setReplyEndpoint(true);
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);

			TextMessage tm = endpointConnection.produceTextMessage(null);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
			populateHeaderWithResponseContext(tm, anEndpoint, aCommand);
			
			endpointConnection.send(tm, false); 
			addIdleTime(tm);
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_reply_sent__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
                    new Object[] { e});
			
		}

		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}

		catch (AsynchAEException e)
		{
			throw e;
		}
		catch ( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}

	
	
	/**
	 * Sends JMS Reply Message to a given endpoint. The reply message contains given Throwable (with full stack)
	 *   
	 * @param t - Throwable to include in the reply message
	 * @param anEndpoint - an endpoint to receive the reply message
	 * @param aCasReferenceId - a unique CAS reference id
	 * 
	 * @throws AsynchAEException
	 */
	public void sendReply( String aCasReferenceId, Endpoint anEndpoint ) throws AsynchAEException
	{
		anEndpoint.setReplyEndpoint(true);
		try
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_replyto_endpoint__FINE",
                    new Object[] { anEndpoint.getEndpoint(), aCasReferenceId });
			if ( anEndpoint.isRemote() )
			{
				
				String serializedCAS = getSerializedCas(true, aCasReferenceId, anEndpoint, false);
				sendCasToRemoteEndpoint(false, serializedCAS, null, aCasReferenceId, anEndpoint, false, 0);
			}
			else
			{
				sendCasToCollocatedDelegate(false, aCasReferenceId, null, anEndpoint, false, 0);
			}
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}

		catch (AsynchAEException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	/**
	 * Sends JMS Reply Message to a given endpoint. The reply message contains given Throwable (with full stack)
	 *   
	 * @param t - Throwable to include in the reply message
	 * @param anEndpoint - an endpoint to receive the reply message
	 * @param aCasReferenceId - a unique CAS reference id
	 * 
	 * @throws AsynchAEException
	 */
	public void sendReply(Throwable t, String aCasReferenceId, Endpoint anEndpoint, int aCommand ) throws AsynchAEException
	{
		anEndpoint.setReplyEndpoint(true);
		try
		{
			Throwable wrapper = null;
			if ( !(t instanceof UimaEEServiceException) )
			{
				//	Strip off AsyncAEException and replace with UimaEEServiceException
				if ( t instanceof AsynchAEException && t.getCause() != null )
				{
					wrapper = new UimaEEServiceException(t.getCause());
				}
				else
				{
					wrapper = new UimaEEServiceException(t);
				}
			}
			if ( aborting )
			{
				return;
			}
			anEndpoint.setReplyEndpoint(true);
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
			//	Create Message that will contain serialized Exception with stack
			ObjectMessage om = endpointConnection.produceObjectMessage();
			if ( wrapper == null )
			{
				om.setObject(t);
			}
			else
			{
				om.setObject(wrapper);
			}
			//	Add common header properties
			populateHeaderWithResponseContext(om, anEndpoint, aCommand); //AsynchAEMessage.Process);
			
			om.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.Exception); 
			if ( aCasReferenceId != null )
			{
				om.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
			}
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_exception__FINE",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint() });

			//	Dispatch Message to destination
			endpointConnection.send(om, false);
			addIdleTime(om);
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}
		catch (AsynchAEException e)
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
		}
		catch (Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	/**
	 * 
	 * @param aMetadata
	 * @param anEndpoint
	 * @throws AsynchAEException
	 */
	public void sendReply(ProcessingResourceMetaData aProcessingResourceMetadata, Endpoint anEndpoint, boolean serialize) 
	throws AsynchAEException
	{
		if ( aborting )
		{
			return;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			anEndpoint.setReplyEndpoint(true);
			//	Initialize JMS connection to given endpoint 
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_produce_txt_msg__FINE",
                    new Object[] { });
			
			TextMessage tm = endpointConnection.produceTextMessage("");

			
			// Collocated Aggregate components dont send metadata just empty reply
			// Such aggregate has merged its typesystem already since it shares
			// CasManager with its parent
			if ( serialize )
			{
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_serializing_meta__FINE",
	                    new Object[] { });
				//	Serialize metadata
				aProcessingResourceMetadata.toXML(bos);
				tm.setText(bos.toString());
			}
						
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.Metadata);

			populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.GetMeta);
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_metadata_reply__endpoint__FINEST",
                    new Object[] { serviceInputEndpoint, anEndpoint.getEndpoint() });
			endpointConnection.send(tm, false);
		}
		catch( JMSException e)
		{
			e.printStackTrace();
			//	Unable to establish connection to the endpoint. Log it and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
			
		}

		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}
		catch ( Exception e)
		{
			e.printStackTrace();
			throw new AsynchAEException(e);
		}
		finally
		{
			try
			{
				bos.close();
			}
			catch ( Exception e)
			{
			}
		}
	}
	
	
	
	private String getSerializedCas( boolean isReply, String aCasReferenceId, Endpoint anEndpoint, boolean cacheSerializedCas ) throws Exception
	{
		CAS cas = null;
		try
		{
			String serializedCAS = null;
			//	Using Cas reference Id retrieve CAS from the shared Cash
			cas = getAnalysisEngineController().getInProcessCache().getCasByReference(aCasReferenceId);
			ServicePerformance casStats = getAnalysisEngineController().getCasStatistics(aCasReferenceId);
			if ( cas == null )
			{
				serializedCAS = getAnalysisEngineController().getInProcessCache().getSerializedCAS( aCasReferenceId );
			}
			else
			{
				CacheEntry entry = getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				long t1 = System.nanoTime();
				//	Serialize CAS for remote Delegates
				String serializer = anEndpoint.getSerializer();
				if ( serializer == null || serializer.trim().length() == 0)
				{
					serializer = "xmi";
				}
				serializedCAS = serializeCAS(isReply, cas, aCasReferenceId, serializer);
				long timeToSerializeCas = System.nanoTime()-t1;
				entry.incrementTimeToSerializeCAS(timeToSerializeCas);
				casStats.incrementCasSerializationTime(timeToSerializeCas);
				getAnalysisEngineController().getServicePerformance().
					incrementCasSerializationTime(timeToSerializeCas);
				if ( cacheSerializedCas )
				{
					getAnalysisEngineController().getInProcessCache().saveSerializedCAS(aCasReferenceId, serializedCAS);
				}
			}
			return serializedCAS;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
	}
	
	
	
	private String getSerializedCasAndReleaseIt( boolean isReply, String aCasReferenceId, Endpoint anEndpoint, boolean cacheSerializedCas ) throws Exception
	{
		CAS cas = null;
		try
		{
			
			
/*			
			String serializedCAS = null;
			//	Using Cas reference Id retrieve CAS from the shared Cash
			cas = getAnalysisEngineController().getInProcessCache().getCasByReference(aCasReferenceId);

			if ( cas == null )
			{
				serializedCAS = getAnalysisEngineController().getInProcessCache().getSerializedCAS( aCasReferenceId );
			}
			else
			{
				CacheEntry entry = getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				long t1 = System.nanoTime();
				//	Serialize CAS for remote Delegates
				String serializer = anEndpoint.getSerializer();
				if ( serializer == null || serializer.trim().length() == 0)
				{
					serializer = "xmi";
				}
				serializedCAS = serializeCAS(isReply, cas, aCasReferenceId, serializer);
				entry.incrementTimeToSerializeCAS(System.nanoTime()-t1);
				if ( cacheSerializedCas )
				{
					getAnalysisEngineController().getInProcessCache().saveSerializedCAS(aCasReferenceId, serializedCAS);
				}
			}
			return serializedCAS;
*/			

			return getSerializedCas(isReply, aCasReferenceId, anEndpoint, cacheSerializedCas);
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		finally
		{
			if ( getAnalysisEngineController() instanceof PrimitiveAnalysisEngineController && anEndpoint.isRemote() )
			{
				getAnalysisEngineController().dropCAS(aCasReferenceId, true );
			}
		}
	}
	
	private boolean endpointRetryEnabled( Endpoint[] endpoints)
	{
		for( int i=0; i < endpoints.length; i++)
		{
			if ( endpoints[i].isRetryEnabled() )
			{
				return true;
			}
		}
		return false;
	}
	private void populateStats( TextMessage aTextMessage, Endpoint anEndpoint, String aCasReferenceId, int anAdminCommand, boolean isRequest) throws Exception
	{
		if ( anEndpoint.isFinal() )
		{
			//aTextMessage.setLongProperty(AsynchAEMessage.TotalTimeSpentInAnalytic, (System.nanoTime()-anEndpoint.getEntryTime()));
			aTextMessage.setLongProperty("SENT-TIME", System.nanoTime());
		}

		if ( anAdminCommand == AsynchAEMessage.Process )
		{
			if ( isRequest )
			{
				long departureTime = System.nanoTime();
				getAnalysisEngineController().saveTime( departureTime, aCasReferenceId,  anEndpoint.getEndpoint());
			}
			else 
			{
			
				ServicePerformance casStats =
					getAnalysisEngineController().getCasStatistics(aCasReferenceId);
				
				aTextMessage.setLongProperty(AsynchAEMessage.TimeToSerializeCAS, casStats.getRawCasSerializationTime());
//					aTextMessage.setLongProperty(AsynchAEMessage.TimeWaitingForCAS, entry.getTimeWaitingForCAS());
				aTextMessage.setLongProperty(AsynchAEMessage.TimeToDeserializeCAS, casStats.getRawCasDeserializationTime());
				aTextMessage.setLongProperty(AsynchAEMessage.TimeInProcessCAS, casStats.getRawAnalysisTime());
				aTextMessage.setLongProperty(AsynchAEMessage.IdleTime, anEndpoint.getIdleTime() );
				
				String lookupKey = getAnalysisEngineController().getName();//getInProcessCache().getMessageAccessorByReference(aCasReferenceId).getEndpointName();
				long arrivalTime = getAnalysisEngineController().getTime( aCasReferenceId, lookupKey); //serviceInputEndpoint);
				long timeInService = System.nanoTime()-arrivalTime;
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "populateStats", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timein_service__FINEST",
	                    new Object[] { serviceInputEndpoint, (double)timeInService/(double)1000000 });
			}
		}	
	}
	private long getCommandTimeoutValue( Endpoint anEndpoint, int aCommand )
	{
		switch( aCommand )
		{
		case AsynchAEMessage.GetMeta:
			return anEndpoint.getMetadataRequestTimeout();
		case AsynchAEMessage.Process:
			return anEndpoint.getProcessRequestTimeout();
		}
		return 0; // no match for the command
	}
	/**
	 * Adds Request specific properties to the JMS Header.
	 * @param aMessage
	 * @param anEndpoint
	 * @param aCommand
	 * @throws Exception
	 */
	private void populateHeaderWithRequestContext( Message aMessage, Endpoint anEndpoint, int aCommand ) throws Exception
	{
		aMessage.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request); 
		aMessage.setIntProperty(AsynchAEMessage.Command, aCommand); 
		long timeout = getCommandTimeoutValue(anEndpoint, aCommand);
		if ( timeout > 0 )
		{
			aMessage.setJMSExpiration(timeout);
		}
		if ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController )
		{
			aMessage.setStringProperty(AsynchAEMessage.MessageFrom, controllerInputEndpoint);
			if (anEndpoint.isRemote())
			{
				String protocol = serviceProtocolList; 
				if ( anEndpoint.getServerURI().trim().toLowerCase().startsWith("http") || (anEndpoint.getReplyToEndpoint() != null && anEndpoint.getReplyToEndpoint().trim().length() > 0))
				{
					protocol = anEndpoint.getServerURI().trim();
					//	protocol = extractURLWithProtocol(serviceProtocolList, "http");
					
					//	get the replyto endpoint name
					String replyTo = anEndpoint.getReplyToEndpoint();
					if (replyTo == null && anEndpoint.getDestination() == null)
					{
						throw new AsynchAEException("replyTo endpoint name not specified for HTTP-based endpoint:"+anEndpoint.getEndpoint());
					}
					if ( replyTo == null )
					{
						replyTo="";
					}
					aMessage.setStringProperty(AsynchAEMessage.MessageFrom, replyTo);
					
				}
				
				Object destination;
				if ( (destination =  anEndpoint.getDestination()) != null )
				{
					aMessage.setJMSReplyTo((Destination)destination);
					aMessage.setStringProperty(UIMAMessage.ServerURI, anEndpoint.getServerURI());
				}
				else
				{
					aMessage.setStringProperty(UIMAMessage.ServerURI, protocol);
				}

				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "populateHeaderWithRequestContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_new_msg_to_remote_FINE",
	                    new Object[] {getAnalysisEngineController().getComponentName(), anEndpoint.getServerURI(), anEndpoint.getEndpoint()});
			}
			else
			{
				aMessage.setStringProperty(UIMAMessage.ServerURI, anEndpoint.getServerURI());
			}
		}
	}

	/**
	 * Adds Response specific properties to the JMS Header
	 * @param aMessage
	 * @param anEndpoint
	 * @param aCommand
	 * @throws Exception
	 */
	private void populateHeaderWithResponseContext( Message aMessage, Endpoint anEndpoint, int aCommand ) throws Exception
	{
		aMessage.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Response); 
		aMessage.setIntProperty(AsynchAEMessage.Command, aCommand); 
		aMessage.setStringProperty(AsynchAEMessage.MessageFrom, serviceInputEndpoint);
		
		if (anEndpoint.isRemote())
		{
			aMessage.setStringProperty(UIMAMessage.ServerURI, getServerURI());
			String hostIP = null;
			try
			{
				hostIP = InetAddress.getLocalHost().getHostAddress();
			}
			catch ( Exception e) {  /* silently deal with this error */ }
			if ( hostIP != null )
			{
				aMessage.setStringProperty(AsynchAEMessage.ServerIP,hostIP);
			}
		}
		else
		{
			aMessage.setStringProperty(UIMAMessage.ServerURI, anEndpoint.getServerURI());
		}
	}
	/**
	 * @deprecated
	 * @param aMessage
	 * @param anEndpoint
	 * @param isProcessRequest
	 * @throws Exception
	 */
	private void populateHeaderWithContext( Message aMessage, Endpoint anEndpoint, boolean isProcessRequest ) throws Exception
	{
		if (anEndpoint.isRemote())
		{
			aMessage.setStringProperty(AsynchAEMessage.MessageFrom, controllerInputEndpoint);
			
			if ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController && isProcessRequest)
			{
				String protocol = serviceProtocolList; 
				if ( anEndpoint.getServerURI().trim().toLowerCase().startsWith("http"))
				{
					protocol = extractURLWithProtocol(serviceProtocolList, "http");
				}
				UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "populateHeaderWithContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_remote_FINE",
	                    new Object[] {protocol, anEndpoint.getEndpoint()});

				aMessage.setStringProperty(UIMAMessage.ServerURI, protocol);
			}
			else
			{
				aMessage.setStringProperty(UIMAMessage.ServerURI, serverURI);
			}
		}
		else
		{
			aMessage.setStringProperty(AsynchAEMessage.MessageFrom, controllerInputEndpoint);
			aMessage.setStringProperty(UIMAMessage.ServerURI, anEndpoint.getServerURI());
		}
		if ( isProcessRequest )
		{
			aMessage.setIntProperty(AsynchAEMessage.Command, AsynchAEMessage.Process); 
			if (getAnalysisEngineController() instanceof AggregateAnalysisEngineController)
			{
				aMessage.setStringProperty(AsynchAEMessage.MessageFrom, controllerInputEndpoint);
				aMessage.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Request); 

			}
			else
			{
				aMessage.setStringProperty(AsynchAEMessage.MessageFrom, serviceInputEndpoint);
				aMessage.setIntProperty(AsynchAEMessage.MessageType, AsynchAEMessage.Response); 
			}
			
		}

	}

	public AnalysisEngineController getAnalysisEngineController()
	{
		return analysisEngineController;
	}

	public void setController(AnalysisEngineController analysisEngineController)
	{
		this.analysisEngineController = analysisEngineController;
		controllerLatch.countDown();
	}

	public String getControllerInputEndpoint()
	{
		return controllerInputEndpoint;
	}

	public void setControllerInputEndpoint(String controllerInputEndpoint)
	{
		this.controllerInputEndpoint = controllerInputEndpoint;
	}

	private void ackMessage(String aCasReferenceId) throws Exception
	{
		if ( getAnalysisEngineController().getInputChannel().getSessionAckMode() == Session.CLIENT_ACKNOWLEDGE )
		{
			((Message)getAnalysisEngineController().
					getInProcessCache().
						getMessageAccessorByReference(aCasReferenceId).
							getRawMessage()).acknowledge();

			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                "ackMessage", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_ack_msg__FINE",
	                new Object[] { aCasReferenceId  });
			
		}

	}
	private boolean requiresAck( Message aMessage, boolean isFinal, int ackMode) throws JMSException
	{
		if ( aMessage.getIntProperty(AsynchAEMessage.MessageType) == AsynchAEMessage.Response && 
			 isFinal && ackMode == Session.CLIENT_ACKNOWLEDGE )
		{
			return true;
		}
		return false;
	}
	private void sendCasToRemoteEndpoint( boolean isRequest, String aSerializedCAS, String anInputCasReferenceId, String aCasReferenceId, Endpoint anEndpoint, boolean startTimer, long sequence ) 
	throws AsynchAEException, ServiceShutdownException
	{
		
		try
		{
			if ( aborting )
			{
				return;
			}
			//	Get the connection object for a given endpoint
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
			//	Create empty JMS Text Message
			TextMessage tm = endpointConnection.produceTextMessage("");
			
			//	Save Serialized CAS in case we need to re-send it for analysis
			if ( anEndpoint.isRetryEnabled() && getAnalysisEngineController().getInProcessCache().getSerializedCAS(aCasReferenceId) == null)
			{
				getAnalysisEngineController().getInProcessCache().saveSerializedCAS(aCasReferenceId, aSerializedCAS);
			}

			tm.setText(aSerializedCAS);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.XMIPayload); 
			//	Add Cas Reference Id to the outgoing JMS Header
			tm.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
			//	Add common properties to the JMS Header
			if ( isRequest == true )
			{
				populateHeaderWithRequestContext(tm, anEndpoint, AsynchAEMessage.Process); 
			}
			else
			{
				populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.Process);
			}
			//	The following is true when the analytic is a CAS Multiplier
			if ( sequence > 0 && !isRequest )
			{
				//	Override MessageType set in the populateHeaderWithContext above.
				//	Make the reply message look like a request. This message will contain a new CAS 
				//	produced by the CAS Multiplier. The client will treat this CAS
				//	differently from the input CAS. 
				tm.setIntProperty( AsynchAEMessage.MessageType, AsynchAEMessage.Request);
				tm.setStringProperty(AsynchAEMessage.InputCasReference, anInputCasReferenceId);
				//	Add a sequence number assigned to this CAS by the controller
				tm.setLongProperty(AsynchAEMessage.CasSequence, sequence);
				isRequest = true;
				//	Add the name of the FreeCas Queue
//				if ( secondaryInputEndpoint != null )
//				{
//					tm.setStringProperty(AsynchAEMessage.MessageFrom, secondaryInputEndpoint);
//				}
				
				if ( freeCASTempQueue != null )
				{
					//	Attach a temp queue to the outgoing message. This a queue where
					//  Free CAS notifications need to be sent from the client
					tm.setJMSReplyTo(freeCASTempQueue);
				}
				if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) )
				{
					CacheEntry cacheEntry = getCacheEntry(aCasReferenceId);
					if ( cacheEntry != null )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                    "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service_detail__FINE",
			                    new Object[] {getAnalysisEngineController().getComponentName(),"Remote", anEndpoint.getEndpoint(), aCasReferenceId, anInputCasReferenceId, cacheEntry.getInputCasReferenceId() });
					}
				}
			}

			//	Add stats
			populateStats(tm, anEndpoint, aCasReferenceId, AsynchAEMessage.Process, isRequest);
			if ( startTimer)
			{
				//	Start a timer for this request. The amount of time to wait
				//	for response is provided in configuration for the endpoint
				anEndpoint.startProcessRequestTimer(aCasReferenceId);
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_new_msg_to_remote_FINE",
                    new Object[] {getAnalysisEngineController().getName(),endpointConnection.getServerUri(), endpointConnection.getEndpoint() });

			//	By default start a timer associated with a connection to the endpoint. Once a connection is established with an
			//	endpoint it is cached and reused for subsequent messaging. If the connection is not used within a given interval
			//	the timer silently expires and closes the connection. This mechanism is similar to what Web Server does when
			//	managing sessions. In case when we want the remote delegate to respond to a temporary queue, which is implied
			//	by anEndpoint.getDestination != null, we dont start the timer.
			boolean startConnectionTimer = true;
			
			if ( anEndpoint.getDestination() != null || !isRequest )
			{
				startConnectionTimer = false;
			}
			
			// ----------------------------------------------------
			//	Send Request Messsage to the Endpoint
			// ----------------------------------------------------
			endpointConnection.send(tm, startConnectionTimer);

//			if ( getAnalysisEngineController().isTopLevelComponent() )
//			{
//				getAnalysisEngineController().getInProcessCache().dumpContents(getAnalysisEngineController().getComponentName());
//			}
			if ( !isRequest )
			{
				addIdleTime(tm);
			}
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
			
		}

		catch( ServiceShutdownException e)
		{
			throw e;
		}
		catch( AsynchAEException e)
		{
			throw e;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		
	}
	
	private void sendCasToRemoteEndpoint( boolean isRequest, String aSerializedCAS, CacheEntry entry,  Endpoint anEndpoint, boolean startTimer ) 
	throws AsynchAEException, ServiceShutdownException
	{
		
		try
		{
			if ( aborting )
			{
				return;
			}
			//	Get the connection object for a given endpoint
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
			//	Create empty JMS Text Message
			TextMessage tm = endpointConnection.produceTextMessage("");
			
			//	Save Serialized CAS in case we need to re-send it for analysis
			if ( anEndpoint.isRetryEnabled() && getAnalysisEngineController().getInProcessCache().getSerializedCAS(entry.getCasReferenceId()) == null)
			{
				getAnalysisEngineController().getInProcessCache().saveSerializedCAS(entry.getCasReferenceId(), aSerializedCAS);
			}

			tm.setText(aSerializedCAS);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.XMIPayload); 
			//	Add Cas Reference Id to the outgoing JMS Header
			tm.setStringProperty(AsynchAEMessage.CasReference, entry.getCasReferenceId());
			//	Add common properties to the JMS Header
			if ( isRequest == true )
			{
				populateHeaderWithRequestContext(tm, anEndpoint, AsynchAEMessage.Process); 
			}
			else
			{
				populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.Process);
			}
			//	The following is true when the analytic is a CAS Multiplier
			if ( entry.isSubordinate() && !isRequest )
			{
				//	Override MessageType set in the populateHeaderWithContext above.
				//	Make the reply message look like a request. This message will contain a new CAS 
				//	produced by the CAS Multiplier. The client will treat this CAS
				//	differently from the input CAS. 
				tm.setIntProperty( AsynchAEMessage.MessageType, AsynchAEMessage.Request);

				isRequest = true;
				//	Save the id of the parent CAS
				tm.setStringProperty(AsynchAEMessage.InputCasReference, getTopParentCasReferenceId(entry.getCasReferenceId()));
				//	Add a sequence number assigned to this CAS by the controller
				tm.setLongProperty(AsynchAEMessage.CasSequence, entry.getCasSequence());
				//	If this is a Cas Multiplier, add a reference to a special queue where
				//	the client sends Free Cas Notifications
				if ( freeCASTempQueue != null )
				{
					//	Attach a temp queue to the outgoing message. This is a queue where
					//  Free CAS notifications need to be sent from the client
					tm.setJMSReplyTo(freeCASTempQueue);
				}
				if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) )
				{
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                    "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service_detail__FINE",
		                    new Object[] {getAnalysisEngineController().getComponentName(),"Remote", anEndpoint.getEndpoint(), entry.getCasReferenceId(), entry.getInputCasReferenceId(), entry.getInputCasReferenceId() });
				}
			}

			//	Add stats
			populateStats(tm, anEndpoint, entry.getCasReferenceId(), AsynchAEMessage.Process, isRequest);
			if ( startTimer)
			{
				//	Start a timer for this request. The amount of time to wait
				//	for response is provided in configuration for the endpoint
				anEndpoint.startProcessRequestTimer(entry.getCasReferenceId());
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_new_msg_to_remote_FINE",
                    new Object[] {getAnalysisEngineController().getName(),endpointConnection.getServerUri(), endpointConnection.getEndpoint() });

			//	By default start a timer associated with a connection to the endpoint. Once a connection is established with an
			//	endpoint it is cached and reused for subsequent messaging. If the connection is not used within a given interval
			//	the timer silently expires and closes the connection. This mechanism is similar to what Web Server does when
			//	managing sessions. In case when we want the remote delegate to respond to a temporary queue, which is implied
			//	by anEndpoint.getDestination != null, we dont start the timer.
			boolean startConnectionTimer = true;
			
			if ( anEndpoint.getDestination() != null || !isRequest )
			{
				startConnectionTimer = false;
			}
			
			// ----------------------------------------------------
			//	Send Request Messsage to the Endpoint
			// ----------------------------------------------------
			endpointConnection.send(tm, startConnectionTimer);

//			if ( getAnalysisEngineController().isTopLevelComponent() )
//			{
//				getAnalysisEngineController().getInProcessCache().dumpContents(getAnalysisEngineController().getComponentName());
//			}
			if ( !isRequest )
			{
				addIdleTime(tm);
			}
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
			
		}

		catch( ServiceShutdownException e)
		{
			throw e;
		}
		catch( AsynchAEException e)
		{
			throw e;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		
	}
	
	private String getTopParentCasReferenceId( String casReferenceId ) throws Exception
	{
		if ( !getAnalysisEngineController().getInProcessCache().entryExists(casReferenceId) )
		{
			return null;
		}
		
		CacheEntry entry = getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(casReferenceId); 
		if ( entry.isSubordinate() )
		{
			//	Recurse until the top CAS reference Id is found
			return getTopParentCasReferenceId(entry.getInputCasReferenceId());
		}
		//	Return the top ancestor CAS id
		return entry.getCasReferenceId();
	}
	
	private boolean isProcessReply( Message aMessage )
	{
		try
		{
			if ( aMessage.getIntProperty(AsynchAEMessage.MessageType) == AsynchAEMessage.Response && 
					 aMessage.getIntProperty(AsynchAEMessage.Command) == AsynchAEMessage.Process )
			{
				return true;
			}
		}
		catch( JMSException e) {}
		return false;
	}
	private void addIdleTime( Message aMessage )
	{

/*
		if ( isProcessReply(aMessage ) && 
			 ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController || 
			   !getAnalysisEngineController().isCasMultiplier() ) )

*/
		long t = System.nanoTime();
		getAnalysisEngineController().saveReplyTime(t, "");
	}
	private void sendCasToCollocatedDelegate(boolean isRequest, String anInputCasReferenceId, String aNewCasReferenceId, Endpoint anEndpoint, boolean startTimer, long sequence) 
	throws AsynchAEException, ServiceShutdownException
	{
		try
		{
			//	Determine which of the CAS id's to log. If the CAS was generated by a CM its sequence
			//	 will be > 0. In this instance use the id in 'aNewCASReferenceId'. Otherwise use the
			//	the default which is the id of an Input CAS.
			String id2Log = anInputCasReferenceId;
			if ( sequence > 0 )
			{
				id2Log = aNewCasReferenceId;
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendCasToCollocatedDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service__FINE",
                    new Object[] {anEndpoint.getEndpoint(), id2Log});
			//	Get the connection object for a given endpoint
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
			//	Create empty JMS Text Message
			TextMessage tm = endpointConnection.produceTextMessage("");
			//	Add common properties to the JMS Header
			if ( isRequest == true )
			{
				populateHeaderWithRequestContext(tm, anEndpoint, AsynchAEMessage.Process); 
			}
			else
			{
				populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.Process);
			}
			
			
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.CASRefID); 
			//	Add Cas Reference Id to the outgoing JMS Header
			if ( aNewCasReferenceId != null )
			{
				tm.setStringProperty(AsynchAEMessage.CasReference, aNewCasReferenceId);
/*
				//	Add the initial Input Cas Reference Id. This is the top ancestor from
				//	which all other CASes are produced
				addTopCASParentReferenceId(tm, anInputCasReferenceId);
*/
				tm.setStringProperty(AsynchAEMessage.InputCasReference, anInputCasReferenceId);
				tm.setLongProperty(AsynchAEMessage.CasSequence, sequence);
				//	Override MessageType set in the populateHeaderWithContext above.
				//	Add stats
				populateStats(tm, anEndpoint, aNewCasReferenceId, AsynchAEMessage.Process, isRequest);
				
				//	Make the reply message look like a request. This message will contain a new CAS 
				//	produced by the CAS Multiplier. The client will treat this CAS
				//	differently from the input CAS. 
				tm.setIntProperty( AsynchAEMessage.MessageType, AsynchAEMessage.Request);
				isRequest = true;

				if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) )
				{
					CacheEntry cacheEntry = getCacheEntry(aNewCasReferenceId);
					if ( cacheEntry != null )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                    "sendCasToCollocatedDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service_detail__FINE",
			                    new Object[] {getAnalysisEngineController().getComponentName(),"Collocated", anEndpoint.getEndpoint(), aNewCasReferenceId, anInputCasReferenceId, cacheEntry.getInputCasReferenceId() });
					}
				}

			
			}
			else
			{
				tm.setStringProperty(AsynchAEMessage.CasReference, anInputCasReferenceId);
				
				if ( getAnalysisEngineController().getInProcessCache() != null &&
						getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(anInputCasReferenceId).isAborted() )	
				{
					tm.setBooleanProperty(AsynchAEMessage.Aborted, true);
				}

				//	Add stats
				populateStats(tm, anEndpoint, anInputCasReferenceId, AsynchAEMessage.Process, isRequest);
				if ( startTimer )
				{
					//	Start a timer for this request. The amount of time to wait
					//	for response is provided in configuration for the endpoint
					anEndpoint.startProcessRequestTimer(anInputCasReferenceId);
				}
			}
			// ----------------------------------------------------
			//	Send Request Messsage to Delegate
			// ----------------------------------------------------
			endpointConnection.send(tm, startTimer);
			if ( !isRequest )
			{
				addIdleTime(tm);
			}
			
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToCollocatedDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
			
		}
		catch( ServiceShutdownException e)
		{
			throw e;
		}
		catch( Exception e)
		{
			throw new AsynchAEException(e);
		}
		
	}

	private CacheEntry getCacheEntry( String aCasReferenceId) throws Exception
	{
		CacheEntry cacheEntry = null;
		if ( getAnalysisEngineController().getInProcessCache().entryExists(aCasReferenceId) )
		{
			cacheEntry = 
				getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
		}
		return cacheEntry; 
	}
	private void addTopCASParentReferenceId( TextMessage tm, String aCasReferenceId) throws Exception
	{
		CacheEntry cacheEntry = null;
		if ( ( cacheEntry = getCacheEntry(aCasReferenceId)) != null )
		{
			if ( cacheEntry.getInputCasReferenceId() == null )
			{
				tm.setStringProperty(AsynchAEMessage.InputCasReference, aCasReferenceId);
			}
			else
			{
				tm.setStringProperty(AsynchAEMessage.InputCasReference, cacheEntry.getInputCasReferenceId());
			}
		}
		
	}
	public void stop()
	{
		aborting = true;
		try
		{
			Iterator it = connectionMap.keySet().iterator();
			JmsEndpointConnection_impl endpointConnection=null;
			while( it.hasNext() )
			{
				
				String key = (String)it.next();
				Object value = connectionMap.get(key);
				if ( value instanceof JmsEndpointConnection_impl )
				{
					endpointConnection = (JmsEndpointConnection_impl)value;
					endpointConnection.abort();
					UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
		                    "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_forced_endpoint_close__INFO",
		                    new Object[] {getAnalysisEngineController().getName(),endpointConnection.getEndpoint(), endpointConnection.getServerUri() });
				}
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_output_channel_aborted__INFO",
	                new Object[] {getAnalysisEngineController().getName()});
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String extractURLWithProtocol(String aProtocolList,  String aProtocol )
	{
		StringTokenizer tokenizer = new StringTokenizer(aProtocolList, ",");
		while( tokenizer.hasMoreTokens() )
		{
			String token = tokenizer.nextToken().trim();
			if ( token.toLowerCase().startsWith(aProtocol.toLowerCase()))
			{
				return token;
			}
		}
		return null;
	}

}
