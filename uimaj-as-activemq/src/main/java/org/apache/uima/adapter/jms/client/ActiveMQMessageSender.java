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

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;

/**
 * Initializes JMS session and creates JMS MessageProducer to be used for
 * sending messages to a given destination. It extends BaseMessageSender which
 * starts the worker thread and is tasked with sending messages. The application
 * threads share a common 'queue' with the worker thread. The application
 * threads add messages to the pendingMessageList 'queue' and the worker thread
 * consumes them.
 * 
 */
public class ActiveMQMessageSender extends BaseMessageSender {

	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;
	private String destinationName = null;

	public ActiveMQMessageSender(Connection aConnection,
			List pendingMessageList, String aDestinationName,
			BaseUIMAAsynchronousEngineCommon_impl engine) throws Exception {
		super(pendingMessageList, engine);
		connection = aConnection;
		destinationName = aDestinationName;
	}

	/**
	 * Creates a jms session object used to instantiate message producer
	 */
	protected void initializeProducer() throws Exception {
		session = connection.createSession(false, 0);
		Queue producerDestination = session.createQueue(destinationName);
		producer = session.createProducer(producerDestination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}

	/**
	 * Returns the full name of the destination queue
	 */
	protected String getDestinationEndpoint() throws Exception {
		return ((ActiveMQDestination) producer.getDestination())
				.getPhysicalName();
	}

	/**
	 * Returns jsm MessageProducer
	 */
	public MessageProducer getMessageProducer() {
		return producer;
	}
	public TextMessage createTextMessage() throws Exception
	{
    	if ( session == null )
    	{
    		throw new JMSException("Unable To Create JMS TextMessage. Reason: JMS Session Not Initialized");
    	}
	  return session.createTextMessage();
	}
	/**
	 * Cleanup any jms resources used by the worker thread
	 */
	protected void cleanup() throws Exception {
		if (session != null) {
			session.close();
		}
		if (producer != null) {
			producer.close();
		}
	}
}