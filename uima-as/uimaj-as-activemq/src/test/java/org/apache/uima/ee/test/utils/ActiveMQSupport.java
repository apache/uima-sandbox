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

package org.apache.uima.ee.test.utils;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.Connector;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.region.policy.IndividualDeadLetterStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.SharedDeadLetterStrategy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;

public class ActiveMQSupport extends TestCase
{
	protected BrokerService broker;
	protected String uri = null;
	protected TransportConnector  tcpConnector = null;
	protected static final String relativePath = 
		"src"+System.getProperty("file.separator")+
		"test"+System.getProperty("file.separator")+
		"resources"+System.getProperty("file.separator")+
		"deployment";

	protected void setUp() throws Exception
	{
		System.out.println("\nSetting Up");
		super.setUp();
		broker = createBroker();
		/*
		 * ObjectName on = broker.getBrokerObjectName(); if (
		 * broker.getManagementContext().getMBeanServer().isRegistered(on)) {
		 * broker.getManagementContext().getMBeanServer().unregisterMBean(on); }
		 */
		// broker.getManagementContext().createCustomComponentMBeanName("org.apache.uima.ee.test.broker",
		// "TestBroker");//setJmxDomainName("org.apache.uima.ee.test.broker");
		broker.start();
		broker.setMasterConnectorURI(uri);
	}

	protected String addHttpConnector(int aDefaultPort) throws Exception
	{
		TransportConnector connector = null;
		try
		{
			String httpURI = generateInternalURI("http", aDefaultPort);
			connector = broker.addConnector(httpURI);
			System.out.println("Adding HTTP Connector:" + connector.getConnectUri());
			connector.start();
			return httpURI;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	private String generateInternalURI(String aProtocol, int aDefaultPort) throws Exception
	{
		boolean success = false;
		int openPort = aDefaultPort;
		ServerSocket ssocket = null;

		while (!success)
		{
			try
			{
				ssocket = new ServerSocket(openPort);
//				String uri = aProtocol + "://" + ssocket.getInetAddress().getLocalHost().getCanonicalHostName() + ":" + openPort;
				String uri = aProtocol + "://localhost:"+ openPort;
				success = true;
				return uri;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			finally
			{
				try
				{
					if (ssocket != null)
					{
						ssocket.close();
					}
				}
				catch (IOException ioe)
				{
				}
			}
		}
		return null;

	}

	protected String getBrokerUri()
	{
		return uri;
	}

	protected ConnectionFactory createConnectionFactory() throws Exception
	{
		return new ActiveMQConnectionFactory(uri);
	}

	protected Connection getConnection() throws Exception
	{
		return createConnectionFactory().createConnection();
	}

	protected BrokerService createBroker() throws Exception
	{
		ServerSocket ssocket = null;
		System.out.println("Creating Broker <<<<<<<<<<<<<<<<");
		try
		{
			ssocket = new ServerSocket();
			String hostName = ssocket.getInetAddress().getLocalHost().getCanonicalHostName();
			uri = "tcp://" + hostName + ":8118";
			BrokerService broker = BrokerFactory.createBroker(new URI("broker:()/" + hostName + "?persistent=false"));
			tcpConnector = broker.addConnector(uri);
			
			
			PolicyEntry policy = new PolicyEntry();
	        policy.setDeadLetterStrategy(new SharedDeadLetterStrategy());

	        PolicyMap pMap = new PolicyMap();
	        pMap.setDefaultEntry(policy);

	        broker.setDestinationPolicy(pMap);

			
			return broker;
		}
		finally
		{
			if (ssocket != null)
				ssocket.close();
		}
	}
	protected void stopBroker() throws Exception
	{
		if ( broker != null )
		{
			System.out.println("Stopping Broker");
			/*
					Set set = broker.getManagementContext().getMBeanServer().queryNames(new ObjectName("uima.ee:*"), null);
					if ( set.isEmpty())
					{
						System.out.println("JMX Server Query For uima.ee Domain Returned No Objects");
					}
					else
					{
						System.out.println("JMX Server Query For uima.ee Domain Returned Valid Objects");
						
					}
					Iterator it = set.iterator();
					while (it.hasNext())
					{
						broker.getManagementContext().getMBeanServer().unregisterMBean((ObjectName) it.next());
					}
			*/	
			if ( tcpConnector != null )
			{
				tcpConnector.stop();
				System.out.println("Broker Connector:"+tcpConnector.getUri().toString()+ " is stopped");
			}
			broker.stop();
			broker = null;
		}
		
	}
	protected void tearDown() throws Exception
	{
		stopBroker();
		System.out.println("Tearing Down");
		super.tearDown();
	}

}
