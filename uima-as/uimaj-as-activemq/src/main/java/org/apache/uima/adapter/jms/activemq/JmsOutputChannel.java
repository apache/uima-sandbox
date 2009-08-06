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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.management.ServiceNotFoundException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.Channel;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.OutputChannel;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaSerializer;
import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.controller.LocalCache.CasStateEntry;
import org.apache.uima.aae.delegate.Delegate;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.error.ServiceShutdownException;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.jmx.ServiceInfo;
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

	private volatile boolean aborting = false;
	
	private Destination freeCASTempQueue;

  private String hostIP = null;
  private UimaSerializer uimaSerializer = new UimaSerializer();
  //  By default every message will have expiration time added
  private volatile boolean addTimeToLive = true;

  public JmsOutputChannel()
  {
    try
    {
      hostIP = InetAddress.getLocalHost().getHostAddress();
    }
    catch ( Exception e) {  /* silently deal with this error */ }
    //  Check the environment for existence of NoTTL tag. If present,
    //  the deployer of the service wants to avoid message expiration.
    if ( System.getProperty("NoTTL") != null) {
      addTimeToLive = false;
    }

  }
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_connector_list__FINE",
                    new Object[] { System.getProperty("ActiveMQConnectors") });
      }
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
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "initialize", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
                    new Object[] { JmsConstants.threadName(), e });
      }
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
		
		long start = getAnalysisEngineController().getCpuTime();
		
		String serializedCas = null;
		
		if ( isReply || "xmi".equalsIgnoreCase(aSerializerKey ) )
		{
			CacheEntry cacheEntry = 
				getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
				 
			XmiSerializationSharedData serSharedData;
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "serializeCAS", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_serialize_cas__FINE",
                    new Object[] {aCasReferenceId});
      }
			if ( isReply )
			{
				serSharedData = cacheEntry.getDeserSharedData();
				if (cacheEntry.acceptsDeltaCas())  {
          serializedCas = uimaSerializer.serializeCasToXmi(aCAS, serSharedData, cacheEntry.getMarker());
			      cacheEntry.setSentDeltaCas(true);
				} else {
          serializedCas = uimaSerializer.serializeCasToXmi(aCAS, serSharedData);
				  cacheEntry.setSentDeltaCas(false);
				}
			}
			else
			{
				serSharedData = cacheEntry.getDeserSharedData();
				if (serSharedData == null) {
					serSharedData = new XmiSerializationSharedData();
					cacheEntry.setXmiSerializationData(serSharedData);
				}
        serializedCas = uimaSerializer.serializeCasToXmi(aCAS, serSharedData);
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
        uimaSerializer.serializeToXCAS(bos, aCAS, null, null, null);
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

		LongNumericStatistic statistic;
		if ( (statistic = getAnalysisEngineController().getMonitor().getLongNumericStatistic("",Monitor.TotalSerializeTime)) != null )
		{
			statistic.increment(getAnalysisEngineController().getCpuTime() - start);
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
	    endpointConnection.send(tm, 0, false);
	  }
	}
	private long getInactivityTimeout( String destination, String brokerURL ) {
    if (System.getProperty(JmsConstants.SessionTimeoutOverride) != null) {
      try {
        long overrideTimeoutValue = Long.parseLong(System.getProperty(JmsConstants.SessionTimeoutOverride));
        // endpointConnection.setInactivityTimeout(overrideTimeoutValue); // If the connection is
        // not used within this interval it will be removed
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(
                  Level.FINE,
                  CLASS_NAME.getName(),
                  "getEndpointConnection",
                  JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAJMS_override_connection_timeout__FINE",
                  new Object[] { analysisEngineController, overrideTimeoutValue, destination,
                    brokerURL });
        }
        return overrideTimeoutValue;
      } catch (NumberFormatException e) {
        /* ignore. use the default */}
    } else {
      // endpointConnection.setInactivityTimeout(INACTIVITY_TIMEOUT); // If the connection is not
      // used within this interval it will be removed
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                CLASS_NAME.getName(),
                "getEndpointConnection",
                JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAJMS_connection_timeout__FINE",
                new Object[] { analysisEngineController, INACTIVITY_TIMEOUT, destination,
                  brokerURL });
      }
    }
    return (int)INACTIVITY_TIMEOUT;   // default
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
	throws AsynchAEException, ServiceShutdownException, ConnectException {
		
		try {
      controllerLatch.await();
    } catch (InterruptedException e) {
    }

    JmsEndpointConnection_impl endpointConnection = null;

    // First get a Map containing destinations managed by a broker provided by the client
    BrokerConnectionEntry brokerConnectionEntry = null;
    if (connectionMap.containsKey(anEndpoint.getServerURI())) {
      brokerConnectionEntry = (BrokerConnectionEntry) connectionMap.get(anEndpoint.getServerURI());
      //  Findbugs thinks that the above may return null, perhaps due to a race condition. Add
      //  the null check just in case
      if ( brokerConnectionEntry == null ) {
        throw new  AsynchAEException("Controller:"+getAnalysisEngineController().getComponentName()+" Unable to Lookup Broker Connection For URL:"+anEndpoint.getServerURI());
      }
    } else {
      brokerConnectionEntry = new BrokerConnectionEntry();
      connectionMap.put(anEndpoint.getServerURI(), brokerConnectionEntry);
      ConnectionTimer connectionTimer = new ConnectionTimer(brokerConnectionEntry);
      connectionTimer.setAnalysisEngineController(getAnalysisEngineController());
      brokerConnectionEntry.setConnectionTimer(connectionTimer);
    }
    
    // create a key to lookup the endpointConnection object
    String key = anEndpoint.getEndpoint() + anEndpoint.getServerURI();
    String destination = anEndpoint.getEndpoint();
    if (anEndpoint.getDestination() != null
            && anEndpoint.getDestination() instanceof ActiveMQDestination) {
      destination = ((ActiveMQDestination) anEndpoint.getDestination()).getPhysicalName();
      key = destination;
    }
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(
              Level.FINE,
              CLASS_NAME.getName(),
              "getEndpointConnection",
              JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
              "UIMAJMS_acquiring_connection_to_endpoint__FINE",
              new Object[] { getAnalysisEngineController().getComponentName(), destination,
                  anEndpoint.getServerURI() });
    }

    // check the cache first
    if (!brokerConnectionEntry.endpointExists(key)) {
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                CLASS_NAME.getName(),
                "getEndpointConnection",
                JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAJMS_create_new_connection__FINE",
                new Object[] { getAnalysisEngineController().getComponentName(), destination,
                    anEndpoint.getServerURI() });
      }
      endpointConnection = new JmsEndpointConnection_impl(brokerConnectionEntry, anEndpoint, getAnalysisEngineController()); 
      brokerConnectionEntry.addEndpointConnection(key, endpointConnection);
      long replyQueueInactivityTimeout = getInactivityTimeout( destination, anEndpoint.getServerURI() );
      brokerConnectionEntry.getConnectionTimer().setInactivityTimeout(replyQueueInactivityTimeout);

      // Connection is not in the cache, create a new connection, initialize it and cache it
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAJMS_open_new_connection_to_endpoint__FINE",
                new Object[] { destination, anEndpoint.getServerURI() });
      }
      
      /**
       * Open connection to a broker, create JMS session and MessageProducer
       */
      endpointConnection.open();
      brokerConnectionEntry.getConnectionTimer()
        .setConnectionCreationTimestamp(endpointConnection.connectionCreationTimestamp);
      
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                CLASS_NAME.getName(),
                "getEndpointConnection",
                JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAJMS_connection_opened_to_endpoint__FINE",
                new Object[] { getAnalysisEngineController().getComponentName(), destination,
                    anEndpoint.getServerURI() });
      }
      // Cache the connection for future use. If not used, connections expire after 50000 millis
      // connectionMap.put( key, endpointConnection);
    } else {
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(
                Level.FINE,
                CLASS_NAME.getName(),
                "getEndpointConnection",
                JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                "UIMAJMS_reusing_existing_connection__FINE",
                new Object[] { getAnalysisEngineController().getComponentName(), destination,
                    anEndpoint.getServerURI() });
      }
      // Retrieve connection from the connection cache
      endpointConnection = brokerConnectionEntry.getEndpointConnection(key);
      // check the state of the connection and re-open it if necessary
      if (endpointConnection != null && !endpointConnection.isOpen()) {
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                  "getEndpointConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                  "UIMAJMS_connection_closed_reopening_endpoint__FINE",
                  new Object[] { destination });
        }
        endpointConnection.open();
        brokerConnectionEntry.getConnectionTimer()
                .setConnectionCreationTimestamp(System.nanoTime());
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
			if ( aCommand == AsynchAEMessage.ReleaseCAS || aCommand == AsynchAEMessage.Stop)
			{

		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "sendRequest", 
						JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_release_cas_req__FINE", new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint(),aCasReferenceId });
		    }
			}
      // Only used to send a Stop or ReleaseCas request so probably no need to start a connection timer ?
      endpointConnection.send(tm, 0, true);
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
	    }
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_shutdown__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
	    }
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
	  Delegate delegate = null;
	  try
		{
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);
			
			TextMessage tm = endpointConnection.produceTextMessage(null);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
			tm.setText("");    // Need this to prevent the Broker from throwing an exception when sending a message to C++ service
			
			populateHeaderWithRequestContext(tm, anEndpoint, aCommand);
			
			//	For remotes add a special property to the message. This property
			//	will be echoed back by the service. This property enables matching
			//	the reply with the right endpoint object managed by the aggregate.
			if ( anEndpoint.isRemote() )
			{
				tm.setStringProperty(AsynchAEMessage.EndpointServer, anEndpoint.getServerURI());
			}
			boolean startTimer = false;
			//	Start timer for endpoints that are remote and are managed by a different broker
			//	than this service. If an endpoint contains a destination object, the outgoing
			//	request will contain a JMSReplyTo object which will point to a temp queue
			if ( anEndpoint.isRemote() && anEndpoint.getDestination() == null)
			{
				startTimer = true;
			}
			if ( aCommand == AsynchAEMessage.CollectionProcessComplete )
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(), "sendRequest", 
						UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_send_cpc_req__FINE", new Object[] { anEndpoint.getEndpoint() });
		    }
			}
			else if ( aCommand == AsynchAEMessage.ReleaseCAS )
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_releasecas_request__endpoint__FINEST",
	                    new Object[] {getAnalysisEngineController().getName(), endpointConnection.getEndpoint()});
		    }
			}
			else if ( aCommand == AsynchAEMessage.GetMeta )
			{
				if ( anEndpoint.getDestination() != null )
				{
					String replyQueueName = ((ActiveMQDestination)anEndpoint.getDestination()).getPhysicalName().replaceAll(":","_");
					if ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController )
					{
						String delegateKey =
							((AggregateAnalysisEngineController)getAnalysisEngineController()).lookUpDelegateKey(anEndpoint.getEndpoint());
						ServiceInfo serviceInfo =((AggregateAnalysisEngineController)getAnalysisEngineController()).getDelegateServiceInfo(delegateKey);
						if (serviceInfo != null )
						{
							serviceInfo.setReplyQueueName(replyQueueName);
							serviceInfo.setServiceKey(delegateKey);
						}
						delegate = lookupDelegate(delegateKey);
						if ( delegate.getGetMetaTimeout() > 0 ) {
	            delegate.startGetMetaRequestTimer();
						}
					}
				}
				else if ( !anEndpoint.isRemote())
				{
					ServiceInfo serviceInfo =((AggregateAnalysisEngineController)getAnalysisEngineController()).getServiceInfo();
					if (serviceInfo != null )
					{
						serviceInfo.setReplyQueueName(controllerInputEndpoint);
					}
				}
			}
			else 
			{
		    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
		      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_metadata_request__endpoint__FINEST",
	                    new Object[] { endpointConnection.getEndpoint(), endpointConnection.getServerUri() });
		    }
			}
      if ( endpointConnection.send(tm, 0, startTimer) != true )
      {
        throw new ServiceNotFoundException();
      }
      
		}
		catch ( Exception e)
		{
      if ( delegate != null && aCommand == AsynchAEMessage.GetMeta )
			{
			  delegate.cancelDelegateTimer();
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
		
    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_msg_to_endpoint__FINE",
                new Object[] { anEndpoint.getEndpoint() });
    }
		try
		{
			if (anEndpoint.isRemote())
			{
			  if ( anEndpoint.getSerializer().equals("xmi")) {
	        String serializedCAS = getSerializedCasAndReleaseIt(false, aCasReferenceId,anEndpoint, anEndpoint.isRetryEnabled());
	        if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
	        {
	                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                        "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_serialized_cas__FINEST",
	                        new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint(),aCasReferenceId,serializedCAS  });
	        }
	        //  Send process request to remote delegate and start timeout timer
	        sendCasToRemoteEndpoint(true, serializedCAS, null, aCasReferenceId, anEndpoint, true, 0);
			  } else {
	        byte[] serializedCAS = getBinaryCasAndReleaseIt(false, aCasReferenceId,anEndpoint, anEndpoint.isRetryEnabled());
	        if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
	        {
	                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                        "sendRequest", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_binary_cas__FINEST",
	                        new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint(),aCasReferenceId,serializedCAS  });
	        }
	        
	        
	        //  Send process request to remote delegate and start timeout timer
	        sendCasToRemoteEndpoint(true, serializedCAS, null, aCasReferenceId, anEndpoint, true, 0);
			    
			  }
			  
			}
			else
			{
        // Not supported
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
      // The default serialization strategy for parallel step is xmi.
      // Binary serialization doesnt support merge.
      endpoints[0].setSerializer("xmi");
			
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
					
					
					// The default serialization strategy for parallel step is xmi.
					// Binary serialization doesnt support merge.
					endpoints[i].setSerializer("xmi");
					
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
        // Not supported
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
			  if ( anEndpoint.getSerializer().equals("xmi")) {
	        //  Serializes CAS and releases it back to CAS Pool
	        String serializedCAS = getSerializedCas(true, entry.getCasReferenceId(), anEndpoint, anEndpoint.isRetryEnabled());
	        sendCasToRemoteEndpoint(false, serializedCAS, entry, anEndpoint, false);
			  } else {
			    byte[] binaryCas = getBinaryCas(true, entry.getCasReferenceId(), anEndpoint, anEndpoint.isRetryEnabled());
			    if ( binaryCas == null ) {
			      return;
			    }
			    sendCasToRemoteEndpoint(false, binaryCas, entry, anEndpoint, false);
			  }
			    
			}
			else
			{
        // Not supported
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
  public void sendReply( int aCommand, Endpoint anEndpoint, String aCasReferenceId ) throws AsynchAEException {
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
      tm.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);

      //  If this service is a Cas Multiplier add to the message a FreeCasQueue. 
      //  The client may need send Stop request to that queue.
      if ( aCommand == AsynchAEMessage.ServiceInfo && 
           getAnalysisEngineController().isCasMultiplier() &&
           freeCASTempQueue != null ) {
          //  Attach a temp queue to the outgoing message. This a queue where
          //  Free CAS notifications need to be sent from the client
          tm.setJMSReplyTo(freeCASTempQueue);
      }
      endpointConnection.send(tm, 0, false); 
      addIdleTime(tm);
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_reply_sent__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
      }
    }
    catch( JMSException e)
    {
      //  Unable to establish connection to the endpoint. Logit and continue
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
                    new Object[] { e});
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
    catch ( Exception e)
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
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);

			TextMessage tm = endpointConnection.produceTextMessage(null);
			tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None); 
			populateHeaderWithResponseContext(tm, anEndpoint, aCommand);
			
			endpointConnection.send(tm, 0, false); 
			addIdleTime(tm);
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cpc_reply_sent__FINE",
                    new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
	    }
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING",
                    new Object[] { e});
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
	    if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_replyto_endpoint__FINE",
                    new Object[] { anEndpoint.getEndpoint(), aCasReferenceId });
	    }
			if ( anEndpoint.isRemote() )
			{
			  CacheEntry entry = null;
			  try {
          entry =  getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
			    
			  } catch ( Exception e ) {
		      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
		        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
		                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_cas_not_found__INFO",
		                    new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint(), aCasReferenceId });
		      }
			    return;
			  }
        if ( anEndpoint.getSerializer().equals("xmi")) {
          //  Serializes CAS and releases it back to CAS Pool
          String serializedCAS = getSerializedCas(true, aCasReferenceId, anEndpoint, false);
          sendCasToRemoteEndpoint(false, serializedCAS, null, aCasReferenceId, anEndpoint, false, 0);
        } else {
          byte[] binaryCas = getBinaryCas(true, entry.getCasReferenceId(), anEndpoint, anEndpoint.isRetryEnabled());
          if ( binaryCas == null ) {
            return;
          }
          sendCasToRemoteEndpoint(false, binaryCas, entry, anEndpoint, false);
        }
			}
			else
			{
			  // Not supported
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
	public void sendReply(Throwable t, String aCasReferenceId, String aParentCasReferenceId, Endpoint anEndpoint, int aCommand ) throws AsynchAEException
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
				if ( aParentCasReferenceId != null ) {
	        om.setStringProperty(AsynchAEMessage.InputCasReference, aParentCasReferenceId);
				}
			}
			
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_exception__FINE",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint() });
      }
			//	Dispatch Message to destination
			endpointConnection.send(om, 0, false);
			addIdleTime(om);
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
		}
		catch( ServiceShutdownException e)
		{
			e.printStackTrace();
		}
		catch (AsynchAEException e)
		{
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
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
		long msgSize = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			anEndpoint.setReplyEndpoint(true);
			//	Initialize JMS connection to given endpoint 
			JmsEndpointConnection_impl endpointConnection = 
				getEndpointConnection(anEndpoint);
			
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_produce_txt_msg__FINE",
                    new Object[] { });
      }
			TextMessage tm = endpointConnection.produceTextMessage("");

			
			// Collocated Aggregate components dont send metadata just empty reply
			// Such aggregate has merged its typesystem already since it shares
			// CasManager with its parent
			if ( serialize )
			{
	      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_serializing_meta__FINE",
	                    new Object[] { });
	      }
				//	Serialize metadata
				aProcessingResourceMetadata.toXML(bos);
				tm.setText(bos.toString());
				msgSize = bos.toString().length();
			}
						
      tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.Metadata);
      //  This service supports Binary Serialization
      tm.setIntProperty(AsynchAEMessage.Serialization, AsynchAEMessage.BinarySerialization);

			populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.GetMeta);
      if ( freeCASTempQueue != null )
      {
        //  Attach a temp queue to the outgoing message. This a queue where
        //  Free CAS notifications need to be sent from the client
        tm.setJMSReplyTo(freeCASTempQueue);
      }

      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_metadata_reply__endpoint__FINEST",
                    new Object[] { serviceInputEndpoint, anEndpoint.getEndpoint() });
      }
			endpointConnection.send(tm, msgSize, false);
		}
		catch( JMSException e)
		{
			e.printStackTrace();
			//	Unable to establish connection to the endpoint. Log it and continue
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendReply", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
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
	
	private byte[] getBinaryCas( boolean isReply, String aCasReferenceId, Endpoint anEndpoint, boolean cacheSerializedCas ) throws Exception
	{
    CAS cas = null;
    try
    {
      byte[] serializedCAS = null;
      //  Using Cas reference Id retrieve CAS from the shared Cash
      cas = getAnalysisEngineController().getInProcessCache().getCasByReference(aCasReferenceId);
      ServicePerformance casStats = getAnalysisEngineController().getCasStatistics(aCasReferenceId);
        CacheEntry entry = getAnalysisEngineController().getInProcessCache().getCacheEntryForCAS(aCasReferenceId);
        long t1 = getAnalysisEngineController().getCpuTime();
        //  Serialize CAS for remote Delegates
        String serializer = anEndpoint.getSerializer();
        if ( cas == null || entry == null ) {
          return null;
        }
        if ( serializer.equals("binary")) {
          if (entry.acceptsDeltaCas() && isReply)  {
            serializedCAS = uimaSerializer.serializeCasToBinary(cas, entry.getMarker());
            entry.setSentDeltaCas(true);
          } else {
            serializedCAS = uimaSerializer.serializeCasToBinary(cas);
            entry.setSentDeltaCas(false);
          }
        } else {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                  "getBinaryCas", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_invalid_serializer__WARNING",
                  new Object[] { getAnalysisEngineController().getName(),serializer, anEndpoint.getEndpoint()});
          }
          throw new UimaEEServiceException("Invalid Serializer:"+serializer+" For Endpoint:"+anEndpoint.getEndpoint());
        }
        long timeToSerializeCas = getAnalysisEngineController().getCpuTime()-t1;
        
        getAnalysisEngineController().incrementSerializationTime(timeToSerializeCas);
        
        entry.incrementTimeToSerializeCAS(timeToSerializeCas);
        casStats.incrementCasSerializationTime(timeToSerializeCas);
        getAnalysisEngineController().getServicePerformance().
          incrementCasSerializationTime(timeToSerializeCas);
      return serializedCAS;
    }
    catch( Exception e)
    {
      throw new AsynchAEException(e);
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
				long t1 = getAnalysisEngineController().getCpuTime();
				//	Serialize CAS for remote Delegates
				String serializer = anEndpoint.getSerializer();
				if ( serializer == null || serializer.trim().length() == 0)
				{
					serializer = "xmi";
				}
				serializedCAS = serializeCAS(isReply, cas, aCasReferenceId, serializer);
				long timeToSerializeCas = getAnalysisEngineController().getCpuTime()-t1;
				getAnalysisEngineController().incrementSerializationTime(timeToSerializeCas);
				
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
  private byte[] getBinaryCasAndReleaseIt( boolean isReply, String aCasReferenceId, Endpoint anEndpoint, boolean cacheSerializedCas ) throws Exception
  {
    try
    {
      return getBinaryCas(isReply, aCasReferenceId, anEndpoint, cacheSerializedCas);
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
	
	
	private String getSerializedCasAndReleaseIt( boolean isReply, String aCasReferenceId, Endpoint anEndpoint, boolean cacheSerializedCas ) throws Exception
	{
		try
		{
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
	private void populateStats( Message aTextMessage, Endpoint anEndpoint, String aCasReferenceId, int anAdminCommand, boolean isRequest) throws Exception
	{
		if ( anEndpoint.isFinal() )
		{
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
				aTextMessage.setLongProperty(AsynchAEMessage.TimeToDeserializeCAS, casStats.getRawCasDeserializationTime());
        aTextMessage.setLongProperty(AsynchAEMessage.TimeInProcessCAS, casStats.getRawAnalysisTime());
        aTextMessage.setLongProperty(AsynchAEMessage.TimeWaitingForCAS,
                getAnalysisEngineController().getServicePerformance().getTimeWaitingForCAS());
        long iT =getAnalysisEngineController().getIdleTimeBetweenProcessCalls(AsynchAEMessage.Process); 
        aTextMessage.setLongProperty(AsynchAEMessage.IdleTime, iT );
				String lookupKey = getAnalysisEngineController().getName();
				long arrivalTime = getAnalysisEngineController().getTime( aCasReferenceId, lookupKey); //serviceInputEndpoint);
				long timeInService = getAnalysisEngineController().getCpuTime()-arrivalTime;
	      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                    "populateStats", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_timein_service__FINEST",
	                    new Object[] { serviceInputEndpoint, (double)timeInService/(double)1000000 });
	      }
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
		//TODO override default based on system property
		aMessage.setBooleanProperty(AsynchAEMessage.AcceptsDeltaCas, true); 
		long timeout = getCommandTimeoutValue(anEndpoint, aCommand);
    //  If the timeout is defined in the Deployment Descriptor and
    //  the service is configured to use time to live (TTL), add
    //  JMS message expiration time. The TTL is by default always
    //  added to the message. To override this add "-DNoTTL" to the 
    //  command line.
    if ( timeout > 0 && addTimeToLive )
		{
      Delegate delegate = lookupDelegate(anEndpoint.getDelegateKey());
			long ttl = timeout;
			// How many CASes are in the list of CASes pending reply for this delegate
			int currentOutstandingCasListSize = delegate.getCasPendingReplyListSize();
			if ( currentOutstandingCasListSize > 0 ) {
			  // increase the time-to-live
			  ttl *= currentOutstandingCasListSize;
			}
      aMessage.setJMSExpiration(ttl);
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

        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	                    "populateHeaderWithRequestContext", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_new_msg_to_remote_FINE",
	                    new Object[] {getAnalysisEngineController().getComponentName(), anEndpoint.getServerURI(), anEndpoint.getEndpoint()});
        }
			}
			else // collocated
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
			if ( hostIP != null )
			{
				aMessage.setStringProperty(AsynchAEMessage.ServerIP,hostIP);
			}
			if ( anEndpoint.getEndpointServer() != null )
			{
				aMessage.setStringProperty(AsynchAEMessage.EndpointServer, anEndpoint.getEndpointServer());
			}
		}
		else
		{
			aMessage.setStringProperty(UIMAMessage.ServerURI, anEndpoint.getServerURI());
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

	private void dispatch( Message aMessage,  Endpoint anEndpoint, CacheEntry entry, boolean isRequest, JmsEndpointConnection_impl endpointConnection, long msgSize ) throws Exception {
	  //  Add stats
    populateStats(aMessage, anEndpoint, entry.getCasReferenceId(), AsynchAEMessage.Process, isRequest);
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                  "dispatch", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_sending_new_msg_to_remote_FINE",
                  new Object[] {getAnalysisEngineController().getName(),endpointConnection.getServerUri(), endpointConnection.getEndpoint() });
    }
	  //  By default start a timer associated with a connection to the endpoint. Once a connection is established with an
	  //  endpoint it is cached and reused for subsequent messaging. If the connection is not used within a given interval
	  //  the timer silently expires and closes the connection. This mechanism is similar to what Web Server does when
	  //  managing sessions. In case when we want the remote delegate to respond to a temporary queue, which is implied
	  //  by anEndpoint.getDestination != null, we dont start the timer.
	  boolean startConnectionTimer = isRequest ? false : true;  // connection time is for replies
	  // ----------------------------------------------------
	  //  Send Request Messsage to the Endpoint
	  // ----------------------------------------------------
	  //  Add the CAS to the delegate's list of CASes pending reply. Do the add before
	  //  the send to eliminate a race condition where the reply is received (on different
	  //  thread) *before* the CAS is added to the list.
	  if ( isRequest ) {
	    anEndpoint.setWaitingForResponse(true);
	    // Add CAS to the list of CASes pending reply
	    addCasToOutstandingList(entry, isRequest, anEndpoint.getDelegateKey());
	  } else {
	    addIdleTime(aMessage);
	  }

	  if ( endpointConnection.send(aMessage, msgSize, startConnectionTimer) == false ) {
	    if ( isRequest ) {
	      Delegate delegate = lookupDelegate(anEndpoint.getDelegateKey());
	      //  Removes the failed CAS from the list of CASes pending reply. This also
	      //  cancels the timer if this CAS was the oldest pending CAS, and if there
	      //  are other CASes pending a fresh timer is started.
	      removeCasFromOutstandingList(entry, isRequest, anEndpoint.getDelegateKey());
				//	Mark this delegate as Failed
	      delegate.getEndpoint().setStatus(Endpoint.FAILED);
				//	Destroy listener associated with a reply queue for this delegate
        InputChannel ic = getAnalysisEngineController().getInputChannel(delegate.getEndpoint().getDestination().toString());
	      ic.destroyListener(delegate.getEndpoint().getDestination().toString(), anEndpoint.getDelegateKey());
				//	Setup error context and handle failure in the error handler
	      ErrorContext errorContext = new ErrorContext();
	      errorContext.add(AsynchAEMessage.Command, AsynchAEMessage.Process);
	      errorContext.add(AsynchAEMessage.CasReference, entry.getCasReferenceId());
	      errorContext.add(AsynchAEMessage.Endpoint, anEndpoint);
	      // Failure on send treat as timeout
	      delegate.handleError(new MessageTimeoutException(), errorContext);
	    } else {
	      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                    "dispatch", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_reply_failed__INFO",
	                    new Object[] {getAnalysisEngineController().getComponentName(),endpointConnection.getServerUri(), endpointConnection.getEndpoint() });
	      }
	    }
	  }
	}
	private void sendCasToRemoteEndpoint( boolean isRequest, String aSerializedCAS, String anInputCasReferenceId, String aCasReferenceId, Endpoint anEndpoint, boolean startTimer, long sequence) 
	throws AsynchAEException, ServiceShutdownException
	{
		long msgSize = 0;
		try
		{
			if ( aborting )
			{
				return;
			}
      CacheEntry entry = this.getCacheEntry(aCasReferenceId);
      if ( entry == null ) {
        throw new AsynchAEException("Controller:"+getAnalysisEngineController().getComponentName()+" Unable to Send Message To Remote Endpoint: "+anEndpoint.getEndpoint()+" CAS:"+aCasReferenceId+" Not In The Cache");
      }

			//	Get the connection object for a given endpoint
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
			if ( endpointConnection == null ) {
			  throw new AsynchAEException("Controller:"+getAnalysisEngineController().getComponentName()+" Unable to Send Message To Remote Endpoint: "+anEndpoint.getEndpoint()+" Connection is Invalid. InputCasReferenceId:"+anInputCasReferenceId+" CasReferenceId:"+aCasReferenceId+" Sequece:"+sequence);
			}
			if( !endpointConnection.isOpen() ) {
			  if ( !isRequest ) {
			   return;
			  }
			}
			TextMessage tm = null;
			try {
	      //  Create empty JMS Text Message
	       tm = endpointConnection.produceTextMessage("");
			} catch (AsynchAEException ex) {
			  System.out.println("UIMA AS Service:"+getAnalysisEngineController().getComponentName()+" Unable to Send Reply Message To Remote Endpoint: "+anEndpoint.getDestination()+". Broker:"+anEndpoint.getServerURI()+" is Unavailable. InputCasReferenceId:"+anInputCasReferenceId+" CasReferenceId:"+aCasReferenceId+" Sequece:"+sequence);			  
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
			  return;
			}
			//	Save Serialized CAS in case we need to re-send it for analysis
			if ( anEndpoint.isRetryEnabled() && getAnalysisEngineController().getInProcessCache().getSerializedCAS(aCasReferenceId) == null)
			{
				getAnalysisEngineController().getInProcessCache().saveSerializedCAS(aCasReferenceId, aSerializedCAS);
			}
			if ( aSerializedCAS != null ) {
			  msgSize = aSerializedCAS.length();
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
				tm.setBooleanProperty(AsynchAEMessage.SentDeltaCas, entry.sentDeltaCas());
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
				if ( freeCASTempQueue != null )
				{
					//	Attach a temp queue to the outgoing message. This a queue where
					//  Free CAS notifications need to be sent from the client
					tm.setJMSReplyTo(freeCASTempQueue);
				}
				if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) )
				{
					if ( entry != null )
					{
						UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
			                    "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service_detail__FINE",
			                    new Object[] {getAnalysisEngineController().getComponentName(),"Remote", anEndpoint.getEndpoint(), aCasReferenceId, anInputCasReferenceId, entry.getInputCasReferenceId() });
					}
				}
			}
      dispatch(tm, anEndpoint, entry, isRequest, endpointConnection, msgSize);
		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Log it and continue
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }			
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

  private void sendCasToRemoteEndpoint( boolean isRequest, byte[] aSerializedCAS, String anInputCasReferenceId, String aCasReferenceId, Endpoint anEndpoint, boolean startTimer, long sequence) 
  throws AsynchAEException, ServiceShutdownException
  {
    long msgSize = 0;
    try
    {
      if ( aborting )
      {
        return;
      }
      if ( aSerializedCAS != null ) {
        msgSize = aSerializedCAS.length;
      }
      CacheEntry entry = this.getCacheEntry(aCasReferenceId);
      if ( entry == null ) {
        throw new AsynchAEException("Controller:"+getAnalysisEngineController().getComponentName()+" Unable to Send Message To Remote Endpoint: "+anEndpoint.getEndpoint()+" CAS:"+aCasReferenceId+" Not In The Cache");
      }
      //  Get the connection object for a given endpoint
      JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
      if ( endpointConnection == null ) {
        throw new AsynchAEException("Controller:"+getAnalysisEngineController().getComponentName()+" Unable to Send Message To Remote Endpoint: "+anEndpoint.getEndpoint()+" Connection is Invalid. InputCasReferenceId:"+anInputCasReferenceId+" CasReferenceId:"+aCasReferenceId+" Sequece:"+sequence);
      }
      if( !endpointConnection.isOpen() ) {
        if ( !isRequest ) {
         return;
        }
      }

      BytesMessage tm = null;
      try {
         //  Create empty JMS Text Message
         tm = endpointConnection.produceByteMessage();
      } catch (AsynchAEException ex) {
        System.out.println("UIMA AS Service:"+getAnalysisEngineController().getComponentName()+" Unable to Send Reply Message To Remote Endpoint: "+anEndpoint.getDestination()+". Broker:"+anEndpoint.getServerURI()+" is Unavailable. InputCasReferenceId:"+anInputCasReferenceId+" CasReferenceId:"+aCasReferenceId+" Sequece:"+sequence);       
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
        return;
      }

      tm.writeBytes(aSerializedCAS);
      tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.BinaryPayload); 
      //  Add Cas Reference Id to the outgoing JMS Header
      tm.setStringProperty(AsynchAEMessage.CasReference, aCasReferenceId);
      //  Add common properties to the JMS Header
      if ( isRequest == true )
      {
        populateHeaderWithRequestContext(tm, anEndpoint, AsynchAEMessage.Process); 
      }
      else
      {
        populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.Process);
        tm.setBooleanProperty(AsynchAEMessage.SentDeltaCas, entry.sentDeltaCas());
      }
      //  The following is true when the analytic is a CAS Multiplier
      if ( sequence > 0 && !isRequest )
      {
        //  Override MessageType set in the populateHeaderWithContext above.
        //  Make the reply message look like a request. This message will contain a new CAS 
        //  produced by the CAS Multiplier. The client will treat this CAS
        //  differently from the input CAS. 
        tm.setIntProperty( AsynchAEMessage.MessageType, AsynchAEMessage.Request);
        tm.setStringProperty(AsynchAEMessage.InputCasReference, anInputCasReferenceId);
        //  Add a sequence number assigned to this CAS by the controller
        tm.setLongProperty(AsynchAEMessage.CasSequence, sequence);
        isRequest = true;
        if ( freeCASTempQueue != null )
        {
          //  Attach a temp queue to the outgoing message. This a queue where
          //  Free CAS notifications need to be sent from the client
          tm.setJMSReplyTo(freeCASTempQueue);
        }
        if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINE) )
        {
          if ( entry != null )
          {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                          "sendCasToRemoteEndpoint", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_send_cas_to_collocated_service_detail__FINE",
                          new Object[] {getAnalysisEngineController().getComponentName(),"Remote", anEndpoint.getEndpoint(), aCasReferenceId, anInputCasReferenceId, entry.getInputCasReferenceId() });
          }
        }
      }
      dispatch(tm, anEndpoint, entry, isRequest, endpointConnection, msgSize);
    }
    catch( JMSException e)
    {
      //  Unable to establish connection to the endpoint. Logit and continue
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
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
		CasStateEntry casStateEntry = null;
		long msgSize=0;
		try
		{
			if ( aborting )
			{
				return;
			}
			casStateEntry = getAnalysisEngineController().getLocalCache().lookupEntry(entry.getCasReferenceId());
			//	Get the connection object for a given endpoint
			JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
      if( !endpointConnection.isOpen() ) {
        if ( !isRequest ) {
         return;
        }
      }
			//	Create empty JMS Text Message
      TextMessage tm = null;
      try {
         //  Create empty JMS Text Message
        tm = endpointConnection.produceTextMessage("");
      } catch (AsynchAEException ex) {
        System.out.println("UIMA AS Service:"+getAnalysisEngineController().getComponentName()+" Unable to Send Reply Message To Remote Endpoint: "+anEndpoint.getDestination()+". Broker:"+anEndpoint.getServerURI()+" is Unavailable. CasReferenceId:"+casStateEntry.getCasReferenceId());       
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                new Object[] { getAnalysisEngineController().getComponentName(), anEndpoint.getEndpoint()});
        return;
      }
			
			//	Save Serialized CAS in case we need to re-send it for analysis
			if ( anEndpoint.isRetryEnabled() && getAnalysisEngineController().getInProcessCache().getSerializedCAS(entry.getCasReferenceId()) == null)
			{
				getAnalysisEngineController().getInProcessCache().saveSerializedCAS(entry.getCasReferenceId(), aSerializedCAS);
			}
      if ( aSerializedCAS != null ) {
        msgSize = aSerializedCAS.length();
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
				tm.setBooleanProperty(AsynchAEMessage.SentDeltaCas, entry.sentDeltaCas());
			}
			//	The following is true when the analytic is a CAS Multiplier
			if ( casStateEntry.isSubordinate() && !isRequest )
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

      dispatch(tm, anEndpoint, entry, isRequest, endpointConnection, msgSize);

		}
		catch( JMSException e)
		{
			//	Unable to establish connection to the endpoint. Logit and continue
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
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
	

  private void sendCasToRemoteEndpoint( boolean isRequest, byte[] aSerializedCAS, CacheEntry entry,  Endpoint anEndpoint, boolean startTimer ) 
  throws AsynchAEException, ServiceShutdownException
  {
    CasStateEntry casStateEntry = null;
    long msgSize=0;
    try
    {
      if ( aborting )
      {
        return;
      }
      casStateEntry = getAnalysisEngineController().getLocalCache().lookupEntry(entry.getCasReferenceId());
      //  Get the connection object for a given endpoint
      JmsEndpointConnection_impl endpointConnection = getEndpointConnection(anEndpoint);
      
      if ( aSerializedCAS != null ) {
        msgSize = aSerializedCAS.length;
      }
      if( !endpointConnection.isOpen() ) {
        if ( !isRequest ) {
         return;
        }
      }
      //  Create empty JMS Text Message
      BytesMessage tm = null;
      try {
        //  Create empty JMS Text Message
        tm = endpointConnection.produceByteMessage();
      } catch (AsynchAEException ex) {
        System.out.println("UIMA AS Service:"+getAnalysisEngineController().getComponentName()+" Unable to Send Reply Message To Remote Endpoint: "+anEndpoint.getDestination()+". Broker:"+anEndpoint.getServerURI()+" is Unavailable. CasReferenceId:"+casStateEntry.getCasReferenceId());       
        return;
      }
      tm.writeBytes(aSerializedCAS);
      tm.setIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.BinaryPayload); 
      //  Add Cas Reference Id to the outgoing JMS Header
      tm.setStringProperty(AsynchAEMessage.CasReference, entry.getCasReferenceId());
      //  Add common properties to the JMS Header
      if ( isRequest == true )
      {
        populateHeaderWithRequestContext(tm, anEndpoint, AsynchAEMessage.Process); 
      }
      else
      {
        populateHeaderWithResponseContext(tm, anEndpoint, AsynchAEMessage.Process);
      }
      if ( casStateEntry == null ) {
        return;
      }
      //  The following is true when the analytic is a CAS Multiplier
      if ( casStateEntry.isSubordinate() && !isRequest )
      {
        //  Override MessageType set in the populateHeaderWithContext above.
        //  Make the reply message look like a request. This message will contain a new CAS 
        //  produced by the CAS Multiplier. The client will treat this CAS
        //  differently from the input CAS. 
        tm.setIntProperty( AsynchAEMessage.MessageType, AsynchAEMessage.Request);

        isRequest = true;
        //  Save the id of the parent CAS
        tm.setStringProperty(AsynchAEMessage.InputCasReference, getTopParentCasReferenceId(entry.getCasReferenceId()));
        //  Add a sequence number assigned to this CAS by the controller
        tm.setLongProperty(AsynchAEMessage.CasSequence, entry.getCasSequence());
        //  If this is a Cas Multiplier, add a reference to a special queue where
        //  the client sends Free Cas Notifications
        if ( freeCASTempQueue != null )
        {
          //  Attach a temp queue to the outgoing message. This is a queue where
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
      dispatch(tm, anEndpoint, entry, isRequest, endpointConnection, msgSize);
      
    }
    catch( JMSException e)
    {
      //  Unable to establish connection to the endpoint. Logit and continue
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
                    "sendCasToRemoteDelegate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_unable_to_connect__INFO",
                    new Object[] { getAnalysisEngineController().getName(), anEndpoint.getEndpoint()});
      }
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
	private Delegate lookupDelegate( String aDelegateKey ) {
    if ( getAnalysisEngineController() instanceof AggregateAnalysisEngineController ) {
      Delegate delegate = 
        ((AggregateAnalysisEngineController)getAnalysisEngineController()).lookupDelegate(aDelegateKey);
      return delegate;             
    }
    return null;
	}
  private void addCasToOutstandingList(CacheEntry entry, boolean isRequest, String aDelegateKey) {
    Delegate delegate = null;
    if ( isRequest && (delegate = lookupDelegate(aDelegateKey)) != null) {
      delegate.addCasToOutstandingList(entry.getCasReferenceId());             
    }
  }
  private void removeCasFromOutstandingList(CacheEntry entry, boolean isRequest, String aDelegateKey) {
    Delegate delegate = null;
    if ( isRequest && (delegate = lookupDelegate(aDelegateKey)) != null) {
      delegate.removeCasFromOutstandingList(entry.getCasReferenceId());             
    }
  }
	
	
	private String getTopParentCasReferenceId( String casReferenceId ) throws Exception
	{
    if ( !getAnalysisEngineController().getLocalCache().containsKey(casReferenceId) ) {
			return null;
		}
    CasStateEntry casStateEntry = getAnalysisEngineController().getLocalCache().lookupEntry(casReferenceId);
		
		if ( casStateEntry.isSubordinate() )
		{
			//	Recurse until the top CAS reference Id is found
      return getTopParentCasReferenceId(casStateEntry.getInputCasReferenceId());
		}
		//	Return the top ancestor CAS id
    return casStateEntry.getCasReferenceId();
	}
	
	private void addIdleTime( Message aMessage )
	{
		long t = System.nanoTime();
		getAnalysisEngineController().saveReplyTime(t, "");
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
  public void stop() {
    stop(Channel.CloseAllChannels);
  }
	public void stop(int channelsToClose)
	{
		aborting = true;
		try
		{
		  //  Fetch iterator over all Broker Connections. This service may be connected
		  //  to many brokers. Each broker connection may handle multiple sessions to
		  //  different reply queues
			Iterator it = connectionMap.keySet().iterator();
			JmsEndpointConnection_impl endpointConnection=null;
			// iterate over connections
			while( it.hasNext() )	{
			  // The key is the broker URL
				String key = (String)it.next();
				//  Fetch a connection object for a given URL
				Object value = connectionMap.get(key);
				
				if ( value instanceof BrokerConnectionEntry ) {
				  BrokerConnectionEntry brokerConnectionEntry = (BrokerConnectionEntry)value;
				  //  A connection object may have many endpoint objects. There is a separate 
				  //  endpoint object per reply queue. 
				  Iterator replyEndpointIterator = brokerConnectionEntry.endpointMap.keySet().iterator();
				  //  Iterate over endpoints, each representing a reply queue
				  while( replyEndpointIterator.hasNext()) {
				    //  Get endpoint object for a reply queue. The abort() call below
				    //  just closes a session and a producer. The JMS Connection is closed
				    //  outside of this while-loop when we clean up all the sessions.
	          endpointConnection = brokerConnectionEntry.endpointMap.get(replyEndpointIterator.next());
	          // Close the session and the producer
	          endpointConnection.abort();
	          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
	            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                        "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_forced_endpoint_close__INFO",
	                        new Object[] {getAnalysisEngineController().getName(),endpointConnection.getEndpoint(), endpointConnection.getServerUri() });
	          }
				  }
				  //  Cancel any pending timers and finally close the JMS Connection to the
				  //  broker
				  if ( brokerConnectionEntry != null && brokerConnectionEntry.getConnectionTimer() != null ) {
				    brokerConnectionEntry.getConnectionTimer().cancelTimer();
				    try {
	            brokerConnectionEntry.getConnection().close();
				    } catch ( Exception ex) { /* ignore, we are stopping */ } 
				  }
				}
			}
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, CLASS_NAME.getName(),
	                "stop", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_output_channel_aborted__INFO",
	                new Object[] {getAnalysisEngineController().getName()});
      }
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
  public void cancelTimers() {
    if ( connectionMap.size() > 0 ) {
      Iterator<String> it = connectionMap.keySet().iterator();
      while (it.hasNext() ) {
        String key = it.next();
        BrokerConnectionEntry ce = (BrokerConnectionEntry) connectionMap.get(key);
        if ( ce != null && ce.getConnectionTimer() != null ) {
          ce.getConnectionTimer().cancelTimer();
        }
      }
    }
  }

	public static class BrokerConnectionEntry {
	  private String brokerURL;
	  private Connection connection;
	  private ConnectionTimer connectionTimer;
	  
	  Map<Object, JmsEndpointConnection_impl> endpointMap = 
	    new ConcurrentHashMap<Object, JmsEndpointConnection_impl>();
	  
    public String getBrokerURL() {
      return brokerURL;
    }
    public void setConnectionTimer( ConnectionTimer aConnectionTimer ) {
      connectionTimer = aConnectionTimer;
    }
    
    public ConnectionTimer getConnectionTimer() {
      return connectionTimer;
    }
    public void setBrokerURL(String brokerURL) {
      this.brokerURL = brokerURL;
    }
    public Connection getConnection() {
      return connection;
    }
    public void setConnection(Connection connection) {
      this.connection = connection;
    }
	  public void addEndpointConnection(Object key, JmsEndpointConnection_impl endpointConnection) {
	    endpointMap.put(key, endpointConnection);
	  }
	  public JmsEndpointConnection_impl getEndpointConnection( Object key) {
	    return endpointMap.get(key);
	  }
	  public boolean endpointExists(Object key ) {
	    return endpointMap.containsKey(key);
	  }
	  public void removeEndpoint(Object key) {
	   endpointMap.remove(key); 
	  }
	}
	
	protected class ConnectionTimer {
    private final Class CLASS_NAME = ConnectionTimer.class;

    private Timer timer;

    private long inactivityTimeout;

    private AnalysisEngineController controller;

    private BrokerConnectionEntry brokerDestinations;

    private long connectionCreationTimestamp;

    private String componentName = "";
    public ConnectionTimer(BrokerConnectionEntry aBrokerDestinations ) {
      brokerDestinations = aBrokerDestinations;
    }
    public void setInactivityTimeout( long anInactivityTimeout ) {
      inactivityTimeout = anInactivityTimeout;
    }
    public void setAnalysisEngineController( AnalysisEngineController aController ) {
      controller = aController;
      if ( controller != null ) {
        componentName = controller.getComponentName();
      }
    }
    public void setConnectionCreationTimestamp(long aConnectionCreationTimestamp) {
      connectionCreationTimestamp = aConnectionCreationTimestamp;
    }
    public void startTimer(long aConnectionCreationTimestamp, final Endpoint endpoint) {
      startTimer(aConnectionCreationTimestamp, endpoint, inactivityTimeout, componentName);
    }
    public synchronized void startTimer(long aConnectionCreationTimestamp, final Endpoint endpoint, long currentInactivityTimeout, String aComponentName) {
      final long cachedConnectionCreationTimestamp = aConnectionCreationTimestamp;
      Date timeToRun = new Date(System.currentTimeMillis() + currentInactivityTimeout);
      if (timer != null) {
        timer.cancel();
      }
      if (controller != null) {
        timer = new Timer("Controller:" + aComponentName
                + ":Reply TimerThread-:" + endpoint + ":" + System.nanoTime());
      } else {
        timer = new Timer("Reply TimerThread-:" + endpoint + ":"
                + System.nanoTime());
      }
      timer.schedule(new TimerTask() {
        public void run() {
          if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.CONFIG)) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.CONFIG, CLASS_NAME.getName(),
                    "startTimer", JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
                    "UIMAJMS_inactivity_timer_expired_CONFIG",
                    new Object[] { Thread.currentThread().getName(), inactivityTimeout, endpoint });
          }
          if (connectionCreationTimestamp <= cachedConnectionCreationTimestamp) {
            try {
              if (brokerDestinations.getConnection() != null
                      && !((ActiveMQConnection) brokerDestinations.getConnection()).isClosed()) {
                try {
                  brokerDestinations.getConnection().stop();
                  brokerDestinations.getConnection().close();
                } catch (Exception e) {
                  // Ignore this for now. Attempting to close connection that has been closed
                  // Ignore we are shutting down
                }
              }
              brokerDestinations.setConnection(null);
            } catch (Exception e) {
            } finally {
              removeDestinationFromManagedList(brokerDestinations, endpoint);
            }
          } 
          cancelTimer();
        }
      }, timeToRun);
    }

    private void removeDestinationFromManagedList(BrokerConnectionEntry brokerDestinations,
            Endpoint endpoint) {
      String key = endpoint.getEndpoint() + endpoint.getServerURI();
      String destination = endpoint.getEndpoint();
      if (endpoint.getDestination() != null
              && endpoint.getDestination() instanceof ActiveMQDestination) {
        destination = ((ActiveMQDestination) endpoint.getDestination()).getPhysicalName();
        key = destination;
      }
      if (brokerDestinations.endpointExists(key)) {
        brokerDestinations.removeEndpoint(key);
      }

    }

    private void cancelTimer() {
      if (timer != null) {
        timer.cancel();
        timer.purge();
      }
    }

    public synchronized void stopTimer() {
      cancelTimer();
      timer = null;
    }
  }
}
