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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.Connector;
import org.apache.activemq.broker.TransportConnection;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.region.policy.IndividualDeadLetterStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.SharedDeadLetterStrategy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.error.handler.GetMetaErrorHandler;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.util.Level;

public class ActiveMQSupport extends TestCase {
  private static final Class CLASS_NAME = ActiveMQSupport.class;

  protected static BrokerService broker;

  protected String uri = null;

  protected static ThreadGroup brokerThreadGroup = null;

  protected TransportConnector tcpConnector = null;

  protected static final String relativePath = "src" + System.getProperty("file.separator")
          + "test" + System.getProperty("file.separator") + "resources"
          + System.getProperty("file.separator") + "deployment";

  protected static final String relativeDataPath = "src" + System.getProperty("file.separator")
          + "test" + System.getProperty("file.separator") + "resources"
          + System.getProperty("file.separator") + "data";

  private static Thread brokerThread = null;

  private TransportConnector httpConnector = null;

  public Semaphore brokerSemaphore = new Semaphore(1);

  protected synchronized void setUp() throws Exception {
    System.out.println("\nSetting Up New Test - Thread Id:" + Thread.currentThread().getId());
    super.setUp();
    if (brokerThreadGroup == null) {
      brokerThreadGroup = new ThreadGroup("BrokerThreadGroup");

      // Acquire a semaphore to force this thread to wait until the broker
      // starts and initializes
      brokerSemaphore.acquire();

      brokerThread = new Thread(brokerThreadGroup, "BrokerThread") {
        public void run() {
          try {
            broker = createBroker();
            broker.start();
            broker.setMasterConnectorURI(uri);
            brokerSemaphore.release(); // broker started
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };

      brokerThread.start();
      try {
        // wait for the broker to start and initialize. The semaphore is
        // released
        // in the run method above
        brokerSemaphore.acquire();
      } finally {
        brokerSemaphore.release();
      }
    } else {
      // Remove messages from all queues
      broker.deleteAllMessages();
    }
  }

  protected String addHttpConnector(int aDefaultPort) throws Exception {
    try {
      String httpURI = generateInternalURI("http", aDefaultPort);
      httpConnector = broker.addConnector(httpURI);
      System.out.println("Adding HTTP Connector:" + httpConnector.getConnectUri());
      httpConnector.start();
      return httpURI;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  protected void removeHttpConnector() throws Exception {
    httpConnector.stop();
  }

  private String generateInternalURI(String aProtocol, int aDefaultPort) throws Exception {
    boolean success = false;
    int openPort = aDefaultPort;
    ServerSocket ssocket = null;

    while (!success) {
      try {
        ssocket = new ServerSocket(openPort);
        // String uri = aProtocol + "://" +
        // ssocket.getInetAddress().getLocalHost().getCanonicalHostName()
        // + ":" + openPort;
        String uri = aProtocol + "://localhost:" + openPort;
        success = true;
        return uri;
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      } finally {
        try {
          if (ssocket != null) {
            ssocket.close();
          }
        } catch (IOException ioe) {
        }
      }
    }
    return null;

  }

  protected String getBrokerUri() {
    return uri;
  }

  protected ConnectionFactory createConnectionFactory() throws Exception {
    return new ActiveMQConnectionFactory(uri);
  }

  protected Connection getConnection() throws Exception {
    return createConnectionFactory().createConnection();
  }

  public BrokerService createBroker() throws Exception {
    return createBroker(8118, true);
  }

  protected BrokerService createBroker(int port, boolean useJmx) throws Exception {
    ServerSocket ssocket = null;
    System.out.println(">>>> Starting Broker On Port:" + port);
    try {
      ssocket = new ServerSocket();
      String hostName = ssocket.getInetAddress().getLocalHost().getCanonicalHostName();
      uri = "tcp://" + hostName + ":" + port;
      BrokerService broker = BrokerFactory.createBroker(new URI("broker:()/" + hostName
              + "?persistent=false"));
      broker.setUseJmx(useJmx);
      tcpConnector = broker.addConnector(uri);

      PolicyEntry policy = new PolicyEntry();
      policy.setDeadLetterStrategy(new SharedDeadLetterStrategy());

      PolicyMap pMap = new PolicyMap();
      pMap.setDefaultEntry(policy);

      broker.setDestinationPolicy(pMap);

      return broker;
    } finally {
      if (ssocket != null)
        ssocket.close();
    }
  }

  protected void stopBroker() throws Exception {
    if (broker != null) {
      System.out.println(">>> Stopping Broker");
      if (tcpConnector != null) {
        tcpConnector.stop();
        System.out.println("Broker Connector:" + tcpConnector.getUri().toString() + " is stopped");
      }
      broker.deleteAllMessages();
      broker.stop();
      System.out.println(">>> Broker Stopped");
    }
  }

  protected synchronized void tearDown() throws Exception {
    System.out.println("Tearing Down - Collecting All Threads and Waiting For Them to Stop ...");
    super.tearDown();
    System.out.println("Tearing Down - Collecting All Threads and Waiting For Them to Stop ...");
    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
    threadGroup.list();
    // Wait unit all non-amq threads stop
    while (brokerThreadGroup.activeCount() > 0) {
      Thread[] threads = new Thread[threadGroup.activeCount()];
      System.out.println("Active Thread Count:" + threadGroup.activeCount()
              + " Active ThreadGroup Count:" + threadGroup.activeGroupCount());
      threadGroup.list();
      threadGroup.enumerate(threads);
      boolean foundExpectedThreads = true;

      for (Thread t : threads) {
        try {
          String tName = t.getName();
          // The following is necessary to account for the AMQ threads
          // Any threads not named in the list below will cause a wait
          // and retry until all non-amq threads are stopped
          if (!tName.startsWith("main") && !tName.equalsIgnoreCase("timer-0")
                  && !tName.equals("ReaderThread") && !tName.equals("BrokerThreadGroup")
                  && !tName.startsWith("ActiveMQ")) {
            foundExpectedThreads = false;
            System.out.println("----- Waiting For Thread:" + tName + " To Stop");
            break; // from for
          }
        } catch (Exception e) {
        }
      }
      if (foundExpectedThreads) {
        break; // from while
      }
      Object syncMonitor = new Object();
      try {
        synchronized (syncMonitor) {
          syncMonitor.wait(500);
        }
      } catch (InterruptedException e) {
      }
    }
  }

}
