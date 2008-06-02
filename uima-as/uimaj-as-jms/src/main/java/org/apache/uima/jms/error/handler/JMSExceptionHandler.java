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

package org.apache.uima.jms.error.handler;

import java.net.ConnectException;

import javax.jms.JMSException;

import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.PrimitiveAnalysisEngineController;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.message.AsynchAEMessage;

public class JMSExceptionHandler extends ErrorHandlerBase implements ErrorHandler
{
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if ( t instanceof JMSException )
		{
			
			Throwable cause = t.getCause();
			boolean handled = false;
			if ( cause != null )
			{
				if ( cause instanceof ConnectException )
				{
					handleConnectError((ConnectException)cause, anErrorContext, aController);
					handled = true;
				}
			}
			if ( !handled )
			{
				if ( cause == null )
				{
					System.out.println("No Handler for JMS Exception Cause ::"+t.getLocalizedMessage());
				}
				else
				{
					System.out.println("No Handler for JMS Exception Cause ::"+cause.getLocalizedMessage());
				}
				t.printStackTrace();
				
			}
			return true;
		}
		else if (t instanceof  java.lang.IllegalArgumentException &&
			t.getCause() != null && t.getCause() instanceof java.net.URISyntaxException)
		{
			//	 Handled invalid syntax in the URI
			System.out.println("Invalid JMS Destination::"+t.getMessage());
			return true;
		}
		return false;
	}

	private void handleConnectError( ConnectException exception, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		System.out.println("Handling JMS Connect Exception Due To::"+exception.getLocalizedMessage());
		System.out.println("Exception Cause::"+exception.getClass().getName()+":::Message::"+exception.getLocalizedMessage());
		
		String casReferenceId = (String)anErrorContext.get(AsynchAEMessage.CasReference);
		Endpoint endpoint = (Endpoint)anErrorContext.get(AsynchAEMessage.Endpoint);
		
		//	If this is a PrimitiveController and not collocated with its aggregate
		//	drop the CAS
		if ( !( aController instanceof AggregateAnalysisEngineController ) )
		{
			CacheEntry entry = null;
			
			try
			{
				entry = aController.getInProcessCache().getCacheEntryForCAS(casReferenceId);
				if ( endpoint.isRemote() && entry != null )
				{
					aController.dropCAS(casReferenceId, true );
				}
			}
			catch( AsynchAEException e) 
			{
				System.out.println("Cas:"+casReferenceId+" Not Found In the Cache.");
			}
		}
	}
	

}
