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

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.client.UimaASProcessStatus;
import org.apache.uima.aae.client.UimaASProcessStatusImpl;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UimaMessageValidator;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngineCommon_impl.ClientRequest;
import org.apache.uima.adapter.jms.message.PendingMessage;
import org.apache.uima.cas.CAS;
import org.apache.uima.util.Level;
import org.apache.uima.util.impl.ProcessTrace_impl;

/**
 * Creates a worker thread for sending messages. This is an abstract
 * implementation that provides a thread with run logic. The concrete
 * implementation of the Worker Thread must extend this class. The application
 * threads share a special in-memory queue with this worker thread. The
 * application threads add jms messages to the pendingMessageList queue and the
 * worker thread consumes them. The worker thread terminates when the uima ee
 * client calls doStop() method.
 */
public abstract class BaseMessageSender implements Runnable,
		MessageSender {
	private static final Class CLASS_NAME = BaseMessageSender.class;

	// A reference to a shared queue where application threads enqueue messages
	// to be sent
  protected BlockingQueue<PendingMessage> messageQueue = 
    new LinkedBlockingQueue<PendingMessage>();
	// Global flag controlling lifecycle of this thread. It will be set to true
	// when the
	// uima ee engine calls doStop()
	protected volatile boolean done;
	// A reference to the uima ee client engine
	protected BaseUIMAAsynchronousEngineCommon_impl engine;
	// Global flag to indicate failure of the worker thread
	protected volatile boolean workerThreadFailed;
	// If the worker thread fails, store the reason for the failure
	protected Exception exception;

	// These are required methods to be implemented by a concrete implementation

	// Returns instance of JMS MessageProducer
	public abstract MessageProducer getMessageProducer();

	// Provides implementation-specific implementation logic. It is expected
	// that this
	// message creates an instance of JMS MessageProducer
	protected abstract void initializeProducer() throws Exception;

	// Releases resources
	protected abstract void cleanup() throws Exception;

	// Returns the name of the destination
	protected abstract String getDestinationEndpoint() throws Exception;

	private MessageProducer producer = null;
	
	
	public BaseMessageSender(	BaseUIMAAsynchronousEngineCommon_impl anEngine) {
	  messageQueue = anEngine.pendingMessageQueue;
		engine = anEngine;
		try {
		  //  Acquire a shared lock. Release it in the run() method once we initialize
		  //  the producer.
	    engine.producerSemaphore.acquire();
		} catch( InterruptedException e) {}
	}

  /**
   * Stops the worker thread
   */
  public void doStop() {
    done = true;
    //  Create an empty message to deliver to the queue that is blocking
    PendingMessage emptyMessage  = new PendingMessage(0);
    messageQueue.add(emptyMessage);
  }
	/**
	 * Return the Exception that caused the failure in this worker thread
	 * 
	 * @return - Exception
	 */
	public Exception getReasonForFailure() {
		return exception;
	}

	/**
	 * The uima ee client should call this method to check if there was a
	 * failure. The method returns true if there was a failure or false
	 * otherwise. If true, the uima ee client can call getReasonForFailure() to
	 * get the reason for failure
	 */
	public boolean failed() {
		return workerThreadFailed;
	}

	/**
	 * Initializes jms message producer and starts the main thread. This thread
	 * waits for messages enqueued by application threads. The application
	 * thread adds a jms message to the pendingMessageList 'queue' and signals
	 * this worker that there is a new message. The worker thread removes the
	 * message and sends it out to a given destination. All messages should be
	 * fully initialized The worker thread does check the message nor adds
	 * anything new to the message. It just sends it out.
	 */
	public void run() {
		String destination = null;
		
		//  Create and initialize the producer. 
		try {
			initializeProducer();
      destination = getDestinationEndpoint();
      if (destination == null) {
        throw new InvalidDestinationException(
            "Unable to determine the destination");
      }
		} catch (Exception e) {
			workerThreadFailed = true;
			exception = e;
			e.printStackTrace();
			return;

		} finally {
      engine.producerSemaphore.release();
		}

		engine.onProducerInitialized();

		producer = getMessageProducer();
		
		// Wait for messages from application threads. The uima ee client engine
		// will call doStop() which sets the global flag 'done' to true.
		PendingMessage pm = null;
		while (!done) {
      // Remove the oldest message from the shared 'queue'
//		  //  Wait for a new message
      try {
        pm = messageQueue.take();
      } catch ( InterruptedException e) {
      }
      if (done) {
        break; // done in this loop
      }

			try {
				//	Request JMS Message from the concrete implementation
			  Message message = null;
			  // Determine if this a CAS Process Request
			  boolean casProcessRequest = isProcessRequest(pm);
			  // Only Process request can be serialized as binary
			  if ( casProcessRequest && engine.getSerializationStrategy().equals("binary") ) {
          message = createBytesMessage();
			  } else {
          message = createTextMessage();
			  }
			  
				initializeMessage( pm, message );
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST,
						CLASS_NAME.getName(), "run",
						JmsConstants.JMS_LOG_RESOURCE_BUNDLE,
						"UIMAJMS_sending_msg_to_endpoint__FINEST",
						new Object[] { UimaMessageValidator.decodeIntToString(AsynchAEMessage.Command, message.getIntProperty(AsynchAEMessage.Command)), UimaMessageValidator.decodeIntToString(AsynchAEMessage.MessageType,message.getIntProperty(AsynchAEMessage.MessageType)), destination });
        }
				if ( casProcessRequest )
				{
					ClientRequest cacheEntry = (ClientRequest)
					engine.getCache().get(pm.get(AsynchAEMessage.CasReference));
					if ( cacheEntry != null )
					{
						//	Use Process Timeout value for the time-to-live property in the 
						//	outgoing JMS message. When this time is exceeded
						//	while the message sits in a queue, the JMS Server will remove it from
						//	the queue. What happens with the expired message depends on the 
						//	configuration. Most JMS Providers create a special dead-letter queue
						//	where all expired messages are placed. NOTE: In ActiveMQ expired msgs in the DLQ 
						//	are not auto evicted yet and accumulate taking up memory.
						long timeoutValue = cacheEntry.getProcessTimeout();

						if ( timeoutValue > 0 )
						{
							// Set high time to live value
              producer.setTimeToLive(10*timeoutValue);
						}
						if (  pm.getMessageType() == AsynchAEMessage.Process )
						{
							cacheEntry.setCASDepartureTime(System.nanoTime());
						}
		        UimaASProcessStatus status = new UimaASProcessStatusImpl(new ProcessTrace_impl(), cacheEntry.getCasReferenceId());
		        //  Notify engine before sending a message
		        engine.onBeforeMessageSend(status);
					}
				}
				
				producer.send(message);
			} catch (Exception e) {
				handleException(e, destination);
			}

		}
		try {
			cleanup();
		} catch (Exception e) {
			handleException(e, destination);
		}
	}
	private void initializeMessage( PendingMessage aPm, Message anOutgoingMessage)
	throws Exception
	{
		//	Populate message properties based on outgoing message type
		switch( aPm.getMessageType())
		{
		case AsynchAEMessage.GetMeta:
			engine.setMetaRequestMessage(anOutgoingMessage);
			break;
			
		case AsynchAEMessage.Process:
			String casReferenceId =
				(String)aPm.get(AsynchAEMessage.CasReference);
			if ( engine.getSerializationStrategy().equals("xmi")) {
	      String serializedCAS = 
	        (String) aPm.get(AsynchAEMessage.CAS);
	      engine.setCASMessage(casReferenceId, serializedCAS, anOutgoingMessage);
			} else {
		     byte[] serializedCAS = 
		        (byte[]) aPm.get(AsynchAEMessage.CAS);
		      engine.setCASMessage(casReferenceId, serializedCAS, anOutgoingMessage);

			}
			//	Message Expiration for Process is added in the main run() loop
			//	right before the message is dispatched to the AS Service
			break;
			
		case AsynchAEMessage.CollectionProcessComplete:
			engine.setCPCMessage(anOutgoingMessage);
			break;
		}
	}
	private boolean isProcessRequest( PendingMessage pm ) {
    return pm.getMessageType() == AsynchAEMessage.Process;
	}
	private void handleException(Exception e, String aDestination) {
		workerThreadFailed = true;
		exception = e;
		e.printStackTrace();
		// Notify the engine that there was an exception.
		engine.onException(e, aDestination);

	}
	/**
	 * @override
	 */
	public MessageProducer getMessageProducer(Destination destination) throws Exception {
		return null;
	}

}
