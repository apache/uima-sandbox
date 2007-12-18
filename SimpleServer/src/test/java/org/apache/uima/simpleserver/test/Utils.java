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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.Servlet;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.uima.simpleserver.servlet.SimpleServerServlet;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

public class Utils {

  public static Server createServer() {
    Server server = new Server();
    Connector connector = new SelectChannelConnector();
    final int port = findFreePort();
    assertTrue("Could not find a free port to run Jetty", (port >= 0));
    System.out.println("Using port: " + port);
    connector.setPort(port);
    connector.setHost("127.0.0.1");
    server.addConnector(connector);
    server.setStopAtShutdown(true);

    // Set up the servlet handler
    server.setHandler(new ServletHandler());

    return server;
  }

  public static void addServletWithMapping(Server server, Class<?> servlet, String pathSpec) {
    ((ServletHandler) server.getHandler()).addServletWithMapping(servlet, pathSpec);
  }

  public static void addServletWithMapping(Server server, Servlet servlet, String pathSpec) {
    ((ServletHandler) server.getHandler()).addServletWithMapping(new ServletHolder(servlet),
        pathSpec);
  }
  
  public static String getHost(Server server) {
    return server.getConnectors()[0].getHost();
  }
  
  public static int getPort(Server server) {
    return server.getConnectors()[0].getPort();
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

  public static HttpResponse callGet(String host, int port, String file) {
    HttpClient httpClient = new DefaultHttpClient();
    HttpGet method = null;
    URL url = null;
    try {
      url = new URL("http", host, port, file);
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
      assertTrue(false);
    }
    try {
      System.out.println("URL: " + url.toString());
      method = new HttpGet(url.toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
      assertTrue(false);
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
    return response;
  }
  
  public static String getResponseContent(HttpResponse response) {
    try {
      Reader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
          SimpleServerServlet.DEFAULT_CODE_PAGE));
      char[] chars = new char[1024];
      int len = 0;
      StringBuffer buf = new StringBuffer();
      while ((len = reader.read(chars)) >= 0) {
        buf.append(chars, 0, len);
      }
      return buf.toString();
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
    // Unreachable
    return null;
  }
  
}
