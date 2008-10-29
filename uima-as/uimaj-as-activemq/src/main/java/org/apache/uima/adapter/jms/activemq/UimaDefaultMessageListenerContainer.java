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

import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.InputChannel;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.error.handler.GetMetaErrorHandler;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;

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
	
	public UimaDefaultMessageListenerContainer()
	{
		super();
    setRecoveryInterval(5);
//    setRecoveryInterval(60000);
		setAcceptMessagesWhileStopping(false);
		setExceptionListener(this);
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
	private synchronized void handleListenerFailure() {
    try {
      if ( controller instanceof AggregateAnalysisEngineController ) {
        String delegateKey = ((AggregateAnalysisEngineController)controller).lookUpDelegateKey(endpoint.getEndpoint());
        if ( endpoint.getDestination() != null ) {
          InputChannel iC = ((AggregateAnalysisEngineController)controller).getInputChannel(endpoint.getDestination().toString());
          iC.destroyListener(endpoint.getDestination().toString(), delegateKey);
        } else {
          InputChannel iC = ((AggregateAnalysisEngineController)controller).getInputChannel(endpoint.getEndpoint());
          iC.destroyListener(endpoint.getEndpoint(), delegateKey);
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
//      terminate(t);
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
	  // If controller is stopping not need to recover the connection
	  if ( controller != null && controller.isStopped()) {
	    return;
	  }
	  t.printStackTrace();

	  
	  if ( endpoint == null ) {
	    
      super.handleListenerSetupFailure(t, false);
      terminate(t);
      return;
	  }
 
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
    controller.stop();
	}
	protected void handleListenerException( Throwable t )
	{
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

	public void afterPropertiesSet()
	{
		super.afterPropertiesSet();
		try
		{
			System.setProperty("BrokerURI", ((ActiveMQConnectionFactory)super.getConnectionFactory()).getBrokerURL());

			Destination destination = super.getDestination();
			if (destination != null && destination instanceof ActiveMQDestination )
			{
				destinationName = ((ActiveMQDestination)destination).getPhysicalName();
			}
			if ( getMessageListener() instanceof JmsInputChannel )
			{
				((JmsInputChannel)getMessageListener()).setListenerContainer(this);
			}
			else if ( getMessageListener() instanceof ModifiableListener)
			{
				((ModifiableListener)getMessageListener()).setListener(this);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
	    if ( UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING) ) {
	      UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, this.getClass().getName(),
	                "afterPropertiesSet", JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING",
	                new Object[] {Thread.currentThread().getId(), e});
	    }
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
		return ((ActiveMQConnectionFactory)super.getConnectionFactory()).getBrokerURL();
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
			  System.out.println("Resolver Plugged In a Temp Queue:"+aDestination);
			  if ( getMessageListener() != null && getMessageListener() instanceof InputChannel ) {
			    ((JmsInputChannel)getMessageListener()).setListenerContainer(this);
			  }
			}
			endpoint.setServerURI(getBrokerUrl());
		}
	}
	public Destination getListenerEndpoint()
	{
		return getDestination(); 
	}

	public void onException(JMSException arg0)
	{
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
}
