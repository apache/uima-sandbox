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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TemporaryQueue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.UimaAsThreadFactory;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.error.handler.GetMetaErrorHandler;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.JmsException;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class UimaDefaultMessageListenerContainer extends DefaultMessageListenerContainer
implements ExceptionListener
{
	private static final Class CLASS_NAME = UimaDefaultMessageListenerContainer.class;
	private String destinationName="";
	private Endpoint endpoint;
	private volatile boolean freeCasQueueListener;
	private AnalysisEngineController controller;
	private volatile boolean failed = false;
	private Object mux = new Object();
	private final UimaDefaultMessageListenerContainer __listenerRef;
	private TaskExecutor taskExecutor = null;	
	private ConnectionFactory connectionFactory = null;
	private Object mux2 = new Object();
  private ThreadGroup threadGroup = null;
  private ThreadFactory tf = null; 
	//	stores number of consumer threads
  private int cc=0;
  //	stores message listener plugged in by Spring
  private Object ml=null;
  //	if cc > 0, a new listener will be injected between
  //	spring and JmsInputChannel Pojo Listener. This 
  //	listener purpose is to increment number of children for
  //	an input CAS. 
  private ConcurrentMessageListener concurrentListener = null;
  private volatile boolean awaitingShutdown = false;
  
  public UimaDefaultMessageListenerContainer()
	{
		super();
		__listenerRef = this;
    setRecoveryInterval(5);
		setAcceptMessagesWhileStopping(false);
		setExceptionListener(this);
    threadGroup = new ThreadGroup("ListenerThreadGroup_"+Thread.currentThread().getThreadGroup().getName());
	}
	public UimaDefaultMessageListenerContainer(boolean freeCasQueueListener)
	{
		this();
		this.freeCasQueueListener = freeCasQueueListener;
	}
	public void setController( AnalysisEngineController aController)
	{
		controller = aController;
	}
	/**
	 * 
	 * @param t
	 * @return
	 */
	private boolean disableListener( Throwable t)
	{
		System.out.println(t.toString());
		if ( t.toString().indexOf("SharedConnectionNotInitializedException") > 0 ||	
			 ( t instanceof JMSException && t.getCause() != null && t.getCause() instanceof ConnectException ) )
			return true;
		return false;
	}
	/**
	 * Stops this Listener
	 */
	private  void handleListenerFailure() {
	  // If shutdown already, nothing to do
	  if ( awaitingShutdown ) {
      return;
    }
    try {
      if ( controller instanceof AggregateAnalysisEngineController ) {
        String delegateKey = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(endpoint.getEndpoint());
        InputChannel iC = null;
        String queueName = null;
        if ( endpoint.getDestination() != null ) {
          queueName = endpoint.getDestination().toString();
        } else {
          queueName = endpoint.getEndpoint();
        }
        iC = ((AggregateAnalysisEngineController)controller).getInputChannel(queueName);
        if ( iC != null ) {
          iC.destroyListener(queueName, delegateKey);
        } else {
          System.out.println(">>> Listener Unable To LookUp InputChannel For Queue:"+queueName);
        }
      }
    } catch( Exception e) {
      e.printStackTrace();
    }
  }
	/**
	 * Handles failure on a temp queue
	 * @param t
	 */
  private void handleTempQueueFailure(Throwable t) {
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
            "handleTempQueueFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
            new Object[] {  endpoint.getDestination(), getBrokerUrl(), t });
    }
    // Check if the failure is due to the failed connection. Spring (and ActiveMQ) dont seem to provide 
    // the cause. Just the top level IllegalStateException with a text message. This is what we need to
    //  check for.
    if ( t instanceof javax.jms.IllegalStateException && t.getMessage().equals("The Consumer is closed")) {
      if ( controller != null && controller instanceof AggregateAnalysisEngineController ) {
        String delegateKey = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(endpoint.getEndpoint());
        try {
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                  "handleTempQueueFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopping_listener_INFO",
                  new Object[] {  controller.getComponentName(), endpoint.getDestination(),delegateKey });
          }
          // Stop current listener 
          handleListenerFailure();
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                  "handleTempQueueFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_stopped_listener_INFO",
                  new Object[] {  controller.getComponentName(), endpoint.getDestination() });
          }
         } catch ( Exception e ) {
          e.printStackTrace();
        }
      }
    } else if ( disableListener(t)) {
      handleQueueFailure(t);
    }
  }
  
  private ErrorHandler fetchGetMetaErrorHandler() {
    ErrorHandler handler = null;
    Iterator it = controller.getErrorHandlerChain().iterator();
    //  Find the error handler for GetMeta in the Error Handler List provided in the 
    //  deployment descriptor
    while ( it.hasNext() )
    {
      handler = (ErrorHandler)it.next();
      if ( handler instanceof GetMetaErrorHandler )
      {
        return handler;
      }
    }
    return null;
  }
  /**
   * Handles failures on non-temp queues
   * @param t
   */
  private void handleQueueFailure(Throwable t) {
    final String endpointName = 
      (getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
            "handleQueueFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
            new Object[] {  endpointName, getBrokerUrl(), t });
    }
    boolean terminate = true;
    //  Check if the failure is severe enough to disable this listener. Whether or not this listener is actully
    //  disabled depends on the action associated with GetMeta Error Handler. If GetMeta Error Handler is
    //  configured to terminate the service on failure, this listener will be terminated and the entire service
    //  will be stopped.
    if (  disableListener(t) ) {
      endpoint.setReplyDestinationFailed();
      //  If this is a listener attached to the Aggregate Controller, use GetMeta Error
      //  Thresholds defined to determine what to do next after failure. Either terminate
      //  the service or disable the delegate with which this listener is associated with
      if ( controller != null && controller instanceof AggregateAnalysisEngineController )
      {
        ErrorHandler handler = fetchGetMetaErrorHandler();
        //  Fetch a Map containing thresholds for GetMeta for each delegate. 
        Map thresholds = handler.getEndpointThresholdMap(); 
        //  Lookup delegate's key using delegate's endpoint name
        String delegateKey = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(endpoint.getEndpoint());
        //  If the delegate has a threshold defined on GetMeta apply Action defined 
        if ( delegateKey != null && thresholds.containsKey(delegateKey))
        {
          //  Fetch the Threshold object containing error configuration
          Threshold threshold = (Threshold) thresholds.get(delegateKey);
          //  Check if the delegate needs to be disabled
          if (threshold.getAction().equalsIgnoreCase(ErrorHandler.DISABLE)) {
            //  The disable delegate method takes a list of delegates
            List list = new ArrayList();
            //  Add the delegate to disable to the list
            list.add(delegateKey);
            try {
              System.out.println(">>>> Controller:"+controller.getComponentName()+" Disabling Listener On Queue:"+endpoint.getEndpoint()+". Component's "+delegateKey+" Broker:"+getBrokerUrl()+" is Invalid");
              if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.INFO) ) {
                UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, this.getClass().getName(),
                          "handleQueueFailure", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_disabled_delegate_bad_broker__INFO",
                          new Object[] {  controller.getComponentName(), delegateKey, getBrokerUrl() });
              }
              //  Remove the delegate from the routing table. 
              ((AggregateAnalysisEngineController) controller).disableDelegates(list);
              terminate = false; //just disable the delegate and continue
            } catch (Exception e) {
              e.printStackTrace();
              terminate = true;
            }
          }
        }
      }
    }
    System.out.println("****** Unable To Connect Listener To Broker:"+getBrokerUrl());
    System.out.println("****** Closing Listener on Queue:"+endpoint.getEndpoint());
    setRecoveryInterval(0);
    
    //  Spin a shutdown thread to terminate listener. 
    new Thread() {
      public void run()
      {
        try
        {
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
            UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                      "handleQueueFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_disable_listener__WARNING",
                      new Object[] {  endpointName, getBrokerUrl() });
          }
          shutdown();
        }
        catch( Exception e) { e.printStackTrace();}
      }
    }.start();

    if ( terminate )
    {
      terminate(t);
    }

  }
  /**
	 * This method is called by Spring when a listener fails
	 */
	protected void handleListenerSetupFailure( Throwable t, boolean alreadyHandled )
	{
	  // If shutdown already, nothing to do
	  if ( awaitingShutdown ) {
      return;
    }
	  // If controller is stopping not need to recover the connection
	  if ( controller != null && controller.isStopped()) {
	    return;
	  }
	  if ( endpoint == null ) {
	    super.handleListenerSetupFailure(t, true);
      String controllerId = ""; 
      if (controller != null ) {
        controllerId = "Uima AS Service:"+controller.getComponentName();
      }
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                  "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_listener_connection_failure__WARNING",
                  new Object[] {  controllerId, getBrokerUrl() });
      }
      System.out.println(controllerId+" Listener Unable to Connect to Broker:" +getBrokerUrl()+" Retrying ....");
      // This code executes during initialization of the service. The Endpoint is not yet 
      // available. The connection to a broker cannot be established. Keep trying until
      // the broker becomes available.
      refreshConnectionUntilSuccessful();
      System.out.println(controllerId+" Listener Established Connection to Broker:" +getBrokerUrl());
      if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                  "handleListenerSetupFailure", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_listener_connection_recovered__WARNING",
                  new Object[] {  controllerId, getBrokerUrl() });
      }
      return;
	  }

	  // Connection failure that occurs AFTER the service initialized. 
	  t.printStackTrace();
 
	  synchronized( mux ) {
	      if ( !failed ) {
	        // Check if this listener is attached to a temp queue. If so, this is a listener
	        // on a reply queue. Handle temp queue listener failure differently than an
	        // input queue listener. 
	        if ( endpoint.isTempReplyDestination()) {
	          handleTempQueueFailure(t);
	        } else {
	          // Handle non-temp queue failure
	          handleQueueFailure(t);
	        }
	      }
	      failed = true;
	    }
	}

	private void terminate(Throwable t) {
    // ****************************************
    //  terminate the service
    // ****************************************
    System.out.println(">>>> Terminating Controller:"+controller.getComponentName()+" Unable To Initialize Listener Due to Invalid Broker URL:"+getBrokerUrl());
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                "terminate", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_terminate_service_dueto_bad_broker__WARNING",
                new Object[] {  controller.getComponentName(), getBrokerUrl() });
    }
    controller.notifyListenersWithInitializationStatus(new ResourceInitializationException(t));
    if ( !controller.isStopped() && !controller.isAwaitingCacheCallbackNotification()) {
    controller.stop();
    }
	}
	protected void handleListenerException( Throwable t )
	{
	  // Already shutdown, nothing to do
    if ( awaitingShutdown ) {
      return;
    }
    t.printStackTrace();
    String endpointName = 
        (getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
      
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
                "handleListenerException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
                new Object[] {  endpointName, getBrokerUrl(), t });
    }
    super.handleListenerException(t);
	}

	private void allPropertiesSet() {
	  super.afterPropertiesSet();
	}
	private void injectConnectionFactory() {
	  while( connectionFactory == null ) {
	    try {
	      Thread.sleep(50);
	    } catch (Exception e){}
	  }
	  String brokerURL = ((ActiveMQConnectionFactory)connectionFactory).getBrokerURL();
    System.out.println(">>> Injecting Listener Connection Factory With Broker URL:" + brokerURL);
	  super.setConnectionFactory(connectionFactory);
	}
	private void injectTaskExecutor() {
	  super.setTaskExecutor(taskExecutor);
	}
	private boolean isGetMetaListener() {
	  return getMessageSelector() != null && __listenerRef.getMessageSelector().equals( "Command=2001");
	}
	private boolean isActiveMQDestination() {
	  return getDestination() != null && getDestination() instanceof ActiveMQDestination;
	}
	
	public void initializeContainer() {
    try {

      injectConnectionFactory();
      initializeTaskExecutor();
      injectTaskExecutor();
      super.initialize();
    } catch( Exception e) {
      e.printStackTrace();
    }
	}
	/**	Intercept Spring call to increment number of consumer
   *  threads. If the value > 1, don't propagate to
   *  Spring. A new listener will be injected and it will
   *  use provided number of consumer threads.
   **/
  public void setConcurrentConsumers(int concurrentConsumers) {
    cc = concurrentConsumers;
    if ( this.freeCasQueueListener ) {
      super.setConcurrentConsumers(concurrentConsumers);
    }
  }
  /** Intercept Spring call to inject application Pojo
    * listener. Don't propagate the listener up to Spring
    * just yet. If more than one consumer thread is used, 
    * a different listener will be injected.
    **/
  public void setMessageListener(Object messageListener) {
    ml = messageListener;
    if ( this.freeCasQueueListener ) {
      super.setMessageListener(messageListener);
    }
  }
	/**
	 * Called by Spring and some Uima AS components when all properties have been set.
	 * This method spins a thread in which the listener is initialized. 
	 */
	public void afterPropertiesSet()
	{
	  if ( endpoint != null ) {
	    // Endpoint has been plagged in from spring xml. This means this is a listener
	    // for a reply queue. We need to rewire things a bit. First make Spring use
	    // one thread to make sure we receive messages in order. To fix a race condition
	    // where a parent CAS is processed first instead of its last child, we need to
	    // assure that we get the child first. We need to update the counter of the 
	    // parent CAS to reflect that there is another child. In the race condition that
	    // was observed, the parent was being processed first in one thread. The parent
	    // reached the final step and subsequently was dropped. Subsequent to that, a 
	    // child CAS processed on another thread begun executing and failed since a look
	    // on its parent resulted in CAS Not Found In Cache Exception.
      //  Make sure Spring uses one thread
	    super.setConcurrentConsumers(1);
	    if ( cc > 1 ) {
	      try {
	        concurrentListener = new ConcurrentMessageListener(cc, ml);
	        super.setMessageListener(concurrentListener);
	      } catch( Exception e) {
	        e.printStackTrace();
	        return;
	      }
	    } else {
	      super.setMessageListener(ml);
	    }
	  } else {
      super.setMessageListener(ml);
      super.setConcurrentConsumers(cc);
	  }
	  
	  
	  
	  
	  
	  Thread t = new Thread() {
	    public void run() {
        Destination destination = __listenerRef.getDestination();
	      try {
	        // Wait until the connection factory is injected by Spring
	        while (connectionFactory == null) {
	          try {
	            Thread.sleep(50);
	          } catch ( InterruptedException ex) {}
	        }
          System.setProperty("BrokerURI", ((ActiveMQConnectionFactory) connectionFactory)
                  .getBrokerURL());
	        boolean done = false;
	        //  Wait for controller to be injected by Uima AS
          if (isActiveMQDestination() && !isGetMetaListener()
                  && !((ActiveMQDestination) destination).isTemporary()) {
            System.out.println("Waiting For Controller. Destination::" + destination
                    + " Listener Instance:" + __listenerRef.hashCode());
            //  Add self to InputChannel
            connectWithInputChannel();
            //  Wait for InputChannel to plug in a controller
            done = true;
            while (controller == null)
              try {
                Thread.sleep(50);
              } catch ( InterruptedException ex) {}
              ;
          }
          //  Plug in connection Factory to Spring's Listener
          __listenerRef.injectConnectionFactory();
          //  Initialize the TaskExecutor. This call injects a custom Thread Pool into the
          //  TaskExecutor provided in the spring xml. The custom thread pool initializes
          //  an instance of AE in a dedicated thread
          initializeTaskExecutor();
          //  Plug in TaskExecutor to Spring's Listener
          __listenerRef.injectTaskExecutor();
          //  Notify Spring Listener that all properties are ready
          __listenerRef.allPropertiesSet();
          if (isActiveMQDestination()) {
            destinationName = ((ActiveMQDestination) destination).getPhysicalName();
          }
          if ( !done ) {
            connectWithInputChannel();
            done = true;
          }
          if ( concurrentListener != null ) {
            concurrentListener.setAnalysisEngineController(controller);
          }
          
          
          
          
          
	      } catch ( Exception e ) {
	        e.printStackTrace();
	        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "afterPropertiesSet", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
	                new Object[] { destination, getBrokerUrl(), e });
	      }
	    }
	  };
	  t.start();
	}
	/**
	 * Inject instance of this listener into the InputChannel
	 * 
	 * @throws Exception
	 */
	private void connectWithInputChannel() throws Exception {
	  Object pojoListener = getPojoListener();

	  
    if (pojoListener instanceof JmsInputChannel) {
      //  Wait until InputChannel has a valid controller. The controller will be plug in
      //  by Spring on a different thread
      while( (((JmsInputChannel) pojoListener).getController()) == null ) {
        try {
          Thread.currentThread().sleep(50);
        } catch ( Exception e) {}
      }
      ((JmsInputChannel) pojoListener).setListenerContainer(__listenerRef);
    } else if (pojoListener instanceof ModifiableListener) {
      ((ModifiableListener) pojoListener).setListener(__listenerRef);
    }
	}
	public String getDestinationName()
	{
		
		return destinationName;
	}
	public String getEndpointName()
	{
		if ( getDestination() != null )
		{
			return ((ActiveMQDestination)getDestination()).getPhysicalName();
		}
		return null;
	}
	public String getBrokerUrl()
	{
    return ((ActiveMQConnectionFactory)connectionFactory).getBrokerURL();
	}
	/**
	 * Overrides specified Connection Factory. Need to append maxInactivityDuration=0 to the 
	 * broker URL. The Connection Factory is immutable thus we need to intercept the one
	 * provided in the deployment descriptor and create a new one with rewritten Broker URL. 
	 * We will inject the prefetch policy to the new CF based on what is found in the CF
	 * in the deployment descriptor.
	 */
	public void setConnectionFactory(ConnectionFactory aConnectionFactory) {
	  connectionFactory = aConnectionFactory;
    if (connectionFactory instanceof ActiveMQConnectionFactory) {
      String brokerURL = ((ActiveMQConnectionFactory) connectionFactory).getBrokerURL();
      if (brokerURL != null) {
        //  http connections should not turn off inactivity timeout. The broker doesnt
        //  create a temp queue if the inactivity is in the url.
        //  Check if the url contains inactivity parameter
        if ( brokerURL.startsWith("http") ) {
          int indx = 0;
          if( (indx = brokerURL.indexOf("?wireFormat.maxInactivityDuration")) > 0) {
            //  strip off the inactivity parameter from the broker url
            brokerURL = brokerURL.substring(0, indx);
          }
        } else {
          // Turns off inactivity Monitoring on the connection
          if( brokerURL.indexOf("?wireFormat.maxInactivityDuration") < 0) {
            brokerURL += "?wireFormat.maxInactivityDuration=0";
          }
        }
        // Save the Prefetch Policy provided in the given CF
        ActiveMQPrefetchPolicy prefetch = ((ActiveMQConnectionFactory) connectionFactory)
                .getPrefetchPolicy();
        // Instantiate new CF with a new Broker URL and inject the Prefetch Policy
        ActiveMQConnectionFactory acf = new ActiveMQConnectionFactory(brokerURL);
        if (prefetch != null) {
          acf.setPrefetchPolicy(prefetch);
        }
        connectionFactory = acf;
      } else {
        // brokerURL = null is handled in the dd2spring
      }
    } 
    super.setConnectionFactory(connectionFactory);
	}
	
	
	public void setDestinationResolver( DestinationResolver resolver )
	{
		((TempDestinationResolver)resolver).setListener(this);
		super.setDestinationResolver(resolver);
	}
	public void closeConnection() throws Exception
	{
		try
		{
			setRecoveryInterval(0);
			setAcceptMessagesWhileStopping(false);
			setAutoStartup(false);
			getSharedConnection().close();
		}
		catch( Exception e)
		{
	    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "closeConnection", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                new Object[] {Thread.currentThread().getId(), e});
	    }
		}
	}
	public void setDestination( Destination aDestination )
	{
		super.setDestination(aDestination);
		if ( endpoint != null)
		{
			endpoint.setDestination(aDestination);
			if ( aDestination instanceof TemporaryQueue ) {
			  endpoint.setTempReplyDestination(true);
			  String serviceName = "";
			  if ( controller != null ) {
			    serviceName = ">>>Controller:"+controller.getComponentName();
			  }
			  System.out.println(serviceName+" Resolver Plugged In a Temp Queue:"+aDestination);
			  Object pojoListener = getPojoListener();
        if ( pojoListener != null && pojoListener instanceof InputChannel ) {
          ((JmsInputChannel)pojoListener).setListenerContainer(this);
        }
			}
			endpoint.setServerURI(getBrokerUrl());
		}
	}
	private Object getPojoListener() {
	  Object pojoListener = null;
	  if ( ml != null ) {
      pojoListener = ml;
    } else if ( getMessageListener() != null ){
      pojoListener= getMessageListener();
    }
	  return pojoListener;
	}
	public Destination getListenerEndpoint()
	{
		return getDestination(); 
	}

	public void onException(JMSException arg0)
	{
    if ( awaitingShutdown ) {
      return;
    }
	  arg0.printStackTrace();
    String endpointName = 
      (getDestination() == null) ? "" : ((ActiveMQDestination)getDestination()).getPhysicalName(); 
    
    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
              "onException", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_jms_listener_failed_WARNING",
              new Object[] {  endpointName, getBrokerUrl(), arg0});
    }
	}

	public void setTargetEndpoint( Endpoint anEndpoint )
	{
		endpoint = anEndpoint;
	}
	public boolean isFreeCasQueueListener()
	{
		return freeCasQueueListener;
	}
  protected void setModifiedTaskExecutor( TaskExecutor taskExecutor) {
    super.setTaskExecutor(taskExecutor);
    System.out.println("Injected Updated Task Executor Into Listener For Destination:"+getDestination());
  }
  /**
   * Delegate shutdown to the super class
   */
  public void doDestroy() {
    super.destroy();
  }
  /**
   * Spins a shutdown thread and stops Sprint and ActiveMQ threads. 
   * 
   */
  public void destroy() {
    if ( awaitingShutdown ) {
      return;
    }
    awaitingShutdown = true;
    if ( getDestination() != null ) {
      System.out.println("Listener:"+getDestination()+" Destroy Called. Active Consumer Count:"+super.getActiveConsumerCount());
    } else {
      System.out.println("Listener:"+getDestinationName()+" Destroy Called. Active Consumer Count:"+super.getActiveConsumerCount());
    }
    //  Spin a thread that will wait until all threads complete. This is needed to avoid
    //  memory leak caused by the fact that we did not wait to collect the threads
    Thread threadGroupDestroyer = new Thread(threadGroup.getParent().getParent(),"threadGroupDestroyer") {
        public void run() {
          try {
            //  stop Spring listener and ActiveMQ threads
            __listenerRef.stop();
            __listenerRef.closeConnection();
          } catch( Exception e) {}
          //  If using non-default TaskExecutor, stop its threads
          if ( taskExecutor != null && taskExecutor instanceof ThreadPoolTaskExecutor) {
            ((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().shutdown();
            while ( ((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().getActiveCount() > 1 && 
                    !((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().isTerminated() ) {
              try {
                ((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().awaitTermination(100,TimeUnit.MILLISECONDS); 
              } catch ( Exception e){}
            }
          }
          if ( taskExecutor != null ) {
            System.out.println(">>>>> Thread:"+Thread.currentThread().getId()+ " +++++++++ Listener:"+getDestination()+" Controller ThreadPoolExecutor Stopped ...");
          }
          //  Shutdown the listener
          __listenerRef.shutdown();
          if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.FINEST) ) {
            threadGroup.getParent().list();
          }
          System.out.println(">>>>> Thread:"+Thread.currentThread().getId()+ " ThreadGroupDestroyer waiting for threads to stop. Active thread count:"+threadGroup.activeCount());
          //  Wait until all threads are accounted for
          while (threadGroup.activeCount() > 0) {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
            }
          }
          System.out.println(">>>>> Thread:"+Thread.currentThread().getId()+ " ThreadGroupDestroyer all threads stopped");

          try {
            synchronized(threadGroup ) {
              if ( !threadGroup.isDestroyed() ) {
                threadGroup.destroy();
              }
            }
            System.out.println(">>>>> Thread:"+Thread.currentThread().getId()+ " >>>>>>>>>>>> Listener:"+getDestinationName()+" Thread Group Destroyed");
          } catch( Exception e) {}   // Ignore 
        }
      };
      threadGroupDestroyer.start();
  }
  /**
   * Called by Spring to inject TaskExecutor
   */
  public void setTaskExecutor( TaskExecutor aTaskExecutor) {
    taskExecutor = aTaskExecutor;
  }
  /**
   * This method initializes ThreadPoolExecutor with a custom ThreadPool. 
   * Each thread produced by the ThreadPool is used to first initialize
   * an instance of the AE before the thread is added to the pool. From 
   * this point on, a thread used to initialize the AE will also be used
   * to call this AE's process() method.
   *   
   * @throws Exception
   */
  private void initializeTaskExecutor() throws Exception {
    //  TaskExecutor is only used with primitives
    if (controller instanceof PrimitiveAnalysisEngineController) {
      //  in case the taskExecutor is not plugged in yet, wait until one 
      //  becomes available. The TaskExecutor is plugged in by Spring
      synchronized( mux2 ) {
        while( taskExecutor == null ) {
          mux2.wait(20);
        }
      }
      //  Create a Custom Thread Factory. Provide it with an instance of
      //  PrimitiveController so that every thread can call it to initialize
      //  the next available instance of a AE.
      tf = new UimaAsThreadFactory(threadGroup, (PrimitiveAnalysisEngineController) controller);
      //   This ThreadExecutor will use custom thread factory instead of defult one
      ((ThreadPoolTaskExecutor) taskExecutor).setThreadFactory(tf);
      //  Initialize the thread pool
      ((ThreadPoolTaskExecutor) taskExecutor).initialize();
      //  Make sure all threads are started. This forces each thread to call
      //  PrimitiveController to initialize the next instance of AE
      ((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().prestartAllCoreThreads();
    }
  }
  public void stop() throws JmsException {
    setAcceptMessagesWhileStopping(false);
    destroy();
  }
}
