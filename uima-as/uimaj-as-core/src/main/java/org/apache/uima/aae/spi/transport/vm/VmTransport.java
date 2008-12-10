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

package org.apache.uima.aae.spi.transport.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.uima.aae.UimaAsContext;
import org.apache.uima.aae.UimaAsThreadFactory;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.BaseAnalysisEngineController;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.UimaSpiException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.message.UIMAMessage;
import org.apache.uima.aae.spi.transport.SpiListener;
import org.apache.uima.aae.spi.transport.UimaMessageDispatcher;
import org.apache.uima.aae.spi.transport.UimaMessageListener;
import org.apache.uima.aae.spi.transport.UimaTransport;

/**
 * This class provides implementation for internal messaging between collocated Uima AS services.
 * It uses {@link UimaMessageDispatcher} to send messages to {@link UimaMessageListener}.
 * 
 *
 */
public class VmTransport implements UimaTransport {

  private List<SpiListener> spiListeners = new ArrayList<SpiListener>();

  private ConcurrentHashMap<String, UimaVmMessageDispatcher> dispatchers = new ConcurrentHashMap<String, UimaVmMessageDispatcher>();

  private ThreadPoolExecutor executor = null;

  private ThreadGroup threadGroup = null;
  //  Create a queue for work items. The queue has a JMX wrapper to expose the 
  //  size.
  private BlockingQueue<Runnable> workQueue = null;

  private VmTransport vmConnector;

  private UimaVmMessageDispatcher dispatcher;

  private UimaVmMessageListener listener;

  private AnalysisEngineController controller;

  private UimaAsContext context;

  public VmTransport(UimaAsContext aContext) {
    this( aContext, null);
  }
  public VmTransport(UimaAsContext aContext, AnalysisEngineController aController) {
    context = aContext;
    controller = aController;
    threadGroup = new ThreadGroup("VmThreadGroup_"+controller.getComponentName());
  }

  public void addSpiListener(SpiListener listener) {
    spiListeners.add(listener);
  }
  public UimaVmMessage produceMessage() {
    return new UimaVmMessage();
  }

  public UimaVmMessage produceMessage(int aCommand, int aMessageType, String aMessageFrom) {
    UimaVmMessage message = produceMessage();
    message.addIntProperty(AsynchAEMessage.Command, aCommand);
    message.addIntProperty(AsynchAEMessage.MessageType, aMessageType);
    switch (aCommand) {
      case AsynchAEMessage.GetMeta:
        message.addIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.Metadata);
        break;
      case AsynchAEMessage.CollectionProcessComplete:
        message.addIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.None);
        break;
      case AsynchAEMessage.Process:
        message.addIntProperty(AsynchAEMessage.Payload, AsynchAEMessage.CASRefID);
        break;
    }

    message.addStringProperty(AsynchAEMessage.MessageFrom, aMessageFrom);
    message.addStringProperty(UIMAMessage.ServerURI, "vm://localhost");
    return message;
  }

  public void startIt() throws UimaSpiException {
    dispatcher = new UimaVmMessageDispatcher(executor, null, (String) context.get("EndpointName"));
  }

  public void stopIt() throws UimaSpiException {
    executor.purge();
    executor.shutdownNow();
    
    Set <Entry<String, UimaVmMessageDispatcher>> set = dispatchers.entrySet();
    for( Entry<String, UimaVmMessageDispatcher> entry: set) {
      UimaVmMessageDispatcher dispatcher = entry.getValue();
      dispatcher.stop();
    }
  }
  public void destroy() {
    try {
      stopIt();
    } catch ( Exception e) { e.printStackTrace();}
  }

  protected ThreadPoolExecutor getExecutorInstance() {
    if (executor == null) {
      int concurrentConsumerCount = context.getConcurrentConsumerCount();
      workQueue = new UimaVmQueue(concurrentConsumerCount);
      // Create a ThreadPoolExecutor with as many threads as needed. The pool has 
      // a fixed number of threads that never expire and are never passivated.
      executor = new ThreadPoolExecutor(concurrentConsumerCount, concurrentConsumerCount, Long.MAX_VALUE,
              TimeUnit.NANOSECONDS, workQueue);
      if ( controller instanceof PrimitiveAnalysisEngineController ) {
        ThreadFactory tf = new UimaAsThreadFactory(threadGroup, (PrimitiveAnalysisEngineController)controller);
        executor.setThreadFactory(tf);
        executor.prestartAllCoreThreads();
      }
    }
    return executor;
  }

  public void registerWithJMX(AnalysisEngineController aController, String queueKind /* ReplyQueue or InputQueue */) {
    try {
      ((UimaVmQueue)workQueue).setConsumerCount(context.getConcurrentConsumerCount());
      aController.registerVmQueueWithJMX(workQueue, queueKind);
    } catch( Exception e) {
      e.printStackTrace();
    }


  }
  public UimaMessageDispatcher getMessageDispatcher() throws UimaSpiException {
    return dispatcher;
  }

  public UimaMessageListener getUimaMessageListener() {
    return listener;
  }

  public UimaMessageListener produceUimaMessageListener()
          throws UimaSpiException {
    listener = new UimaVmMessageListener(controller);
    return listener;
  }

  public UimaMessageDispatcher getUimaMessageDispatcher() throws UimaSpiException {
    return getUimaMessageDispatcher(controller.getName());
  }

  public UimaMessageDispatcher getUimaMessageDispatcher(String aKey) throws UimaSpiException {
    return dispatchers.get(aKey);
  }
  public UimaVmMessageDispatcher produceUimaMessageDispatcher(UimaTransport aTransport) throws UimaSpiException {
    UimaVmMessageDispatcher dispatcher = null;
    String endpointName = (String) context.get("EndpointName");
    if (!controller.isTopLevelComponent() ) {
      endpointName = controller.getParentController().getName();
    }
    dispatcher = new UimaVmMessageDispatcher(((VmTransport) aTransport).getExecutorInstance(),
            ((VmTransport) aTransport).getUimaMessageListener(), endpointName);
    if ( controller instanceof AggregateAnalysisEngineController ||
            ((VmTransport) aTransport).controller instanceof AggregateAnalysisEngineController   
       ) {
      //  Store the dispatcher under delegate's name
      dispatchers.put(((VmTransport) aTransport).controller.getName(), dispatcher);
    } else {
      dispatchers.put(controller.getName(), dispatcher);
    }
    return dispatcher;
  }

}
