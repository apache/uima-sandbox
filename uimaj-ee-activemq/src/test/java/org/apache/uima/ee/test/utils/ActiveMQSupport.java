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
		try
		{
			ssocket = new ServerSocket();
			String hostName = ssocket.getInetAddress().getLocalHost().getCanonicalHostName();
			uri = "tcp://" + hostName + ":8118";
			BrokerService broker = BrokerFactory.createBroker(new URI("broker:()/" + hostName + "?persistent=false"));
			tcpConnector = broker.addConnector(uri);
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
