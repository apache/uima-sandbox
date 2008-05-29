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

package org.apache.uima.aae.error.handler;

import java.net.ConnectException;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.BaseAnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.Threshold;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.util.Level;

public class GetMetaErrorHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = GetMetaErrorHandler.class;

	private Map delegateMap = null;
	
	public GetMetaErrorHandler( Map aDelegateMap )
	{
		delegateMap = aDelegateMap;
	}
	
	public Map getEndpointThresholdMap()
	{
		return delegateMap;
	}
	private boolean terminate( Threshold aThreshold )
	{
		return (aThreshold == null || 
				aThreshold.getAction() == null || 
				aThreshold.getAction().trim().length() == 0 || 
				ErrorHandler.TERMINATE.equalsIgnoreCase(aThreshold.getAction() ));
	}
	private boolean isConnectionFailure( Exception e)
	{
    if ( e != null && e.getCause() != null &&  e.getCause() instanceof ConnectException )
    {
      return true;
    }
    return false;
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		//	GetMeta errors are only handled by the Aggregate AS
		if ( aController instanceof PrimitiveAnalysisEngineController || 
			 !isHandlerForError(anErrorContext, AsynchAEMessage.GetMeta) )
		{
			return false;
		}
		
		UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
				UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", t);

		Endpoint endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);

		if ( endpoint != null && aController instanceof AggregateAnalysisEngineController )
		{
/*			
      if ( isConnectionFailure( (Exception)t ) )
      {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
                "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_terminate_service__INFO",
                new Object[] {aController.getComponentName(), endpoint.getEndpoint()});
        aController.terminate();
        aController.notifyListenersWithInitializationStatus((Exception)t);
        return true;
      }
*/
      Threshold threshold = super.getThreshold(endpoint, delegateMap, aController);
	    	String key = ((AggregateAnalysisEngineController)aController).lookUpDelegateKey(endpoint.getEndpoint());
	    	//	If threshold is not defined, assume action=terminate
	    	if (  threshold == null || threshold.getMaxRetries() == 0 || 
	    			  ( super.retryLastCommand(AsynchAEMessage.GetMeta, endpoint, aController, key, threshold, anErrorContext) == false )	
	    	        )
	    	{
	    		if ( terminate(threshold ) )
	    		{
	    			System.out.println("!!!!!!!!!!!! Exceeded Threshold Terminating !!!!!!!!!!!!!!");
	    			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, CLASS_NAME.getName(),
	    	                "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_terminate_service__INFO",
	    	                new Object[] {aController.getComponentName(), endpoint.getEndpoint()});
//	    			aController.propagateShutdownEventToTheTopComponent();
	    			aController.terminate();
	    			aController.notifyListenersWithInitializationStatus((Exception)t);
	    		}
	    		else
	    		{
		    		aController.takeAction(threshold.getAction(), endpoint.getEndpoint(), anErrorContext);
	    		}
	    	}
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.INFO, getClass().getName(), "handleError", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_no_endpoint_for_getmeta_retry__INFO", new Object[] { aController.getName()});
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
}
