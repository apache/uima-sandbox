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

package org.apache.uima.simpleserver.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.uima.simpleserver.servlet.SimpleServerServlet;
import org.apache.uima.test.junit_extension.JUnitExtension;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

public class ServerTest {

  public static class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      response.setCharacterEncoding(SimpleServerServlet.DEFAULT_CODE_PAGE);
      response.getWriter().println("<h1>Hello SimpleServlet</h1>");
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private static final String localhost = "127.0.0.1";

  private static final String httpprotocol = "http";

  private static Server server = null;

  private static int port = -1;

  @BeforeClass
  public static void setUp() {
    // Set up the server
    server = new Server();
    Connector connector = new SelectChannelConnector();
    port = findFreePort();
    assertTrue("Could not find a free port to run Jetty", (port >= 0));
    System.out.println("Using port: " + port);
    connector.setPort(port);
    connector.setHost(localhost);
    server.addConnector(connector);
    server.setStopAtShutdown(true);

    // Set up the servlet handler
    ServletHandler servletHandler = new ServletHandler();
    server.setHandler(servletHandler);

    // Add servlets
    servletHandler.addServletWithMapping(HelloServlet.class, "/hello");
    
    // Set up UIMA servlet
    SimpleServerServlet uimaServlet = new SimpleServerServlet(true);
    File descriptorFile = JUnitExtension.getFile("desc/simpleServerTestDescriptor.xml");
    assertTrue(descriptorFile.exists());
    File specFile = JUnitExtension.getFile("serverspec/spec1.xml");
    assertTrue(specFile.exists());
    try {
      uimaServlet.init(descriptorFile, specFile);
    } catch (ServletException e1) {
      e1.printStackTrace();
      assertTrue(false);
    }
    ServletHolder uimaServletHolder = new ServletHolder(uimaServlet);
    servletHandler.addServletWithMapping(uimaServletHolder, "/uima");

    // Start the server
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
      assertTrue("Exception running Jetty", false);
    }
  }

  /**
   * Test connection to Jetty. If this test fails, none of the others will go through. Start
   * investigating here.
   */
  @Test
  public void test() {
    HttpClient httpClient = new DefaultHttpClient();
    HttpGet method = null;
    URL url = null;
    try {
      url = new URL(httpprotocol, localhost, port, "/hello");
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      System.out.println("URL: " + url.toString());
      method = new HttpGet(url.toString());
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    HttpResponse response = null;
    try {
      response = httpClient.execute(method);
    } catch (HttpException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (InterruptedException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    assertNotNull(response);
    assertTrue(response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK);

    String out = null;
    try {
      Reader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
          SimpleServerServlet.DEFAULT_CODE_PAGE));
      char[] chars = new char[1024];
      int len = 0;
      StringBuffer buf = new StringBuffer();
      while ((len = reader.read(chars)) >= 0) {
        buf.append(chars, 0, len);
      }
      out = buf.toString();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IllegalStateException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    System.out.println(out);
  }

  @Test
  public void test1() {
    HttpClient httpClient = new DefaultHttpClient();
    HttpGet method = null;
    URL url = null;
    try {
      url = new URL(httpprotocol, localhost, port, "/uima?text=foo%20bar");
      
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      System.out.println("URL: " + url.toString());
      method = new HttpGet(url.toString());
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    HttpResponse response = null;
    try {
      response = httpClient.execute(method);
    } catch (HttpException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (InterruptedException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    assertNotNull(response);
    assertTrue(response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK);

    String out = null;
    try {
      Reader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
          SimpleServerServlet.DEFAULT_CODE_PAGE));
      char[] chars = new char[1024];
      int len = 0;
      StringBuffer buf = new StringBuffer();
      while ((len = reader.read(chars)) >= 0) {
        buf.append(chars, 0, len);
      }
      out = buf.toString();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IllegalStateException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    System.out.println(out);
  }

  @AfterClass
  public static void tearDown() {
    try {
      server.stop();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      assertTrue("Exception shutting down Jetty", false);
    }
  }

  private static final int findFreePort() {
    int p = -1;
    try {
      // Create a new server socket on an unused port.
      ServerSocket serverSocket = new ServerSocket(0);
      p = serverSocket.getLocalPort();
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return p;
  }

}
