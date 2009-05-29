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

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.UIMAEE_Constants;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.UimaEEServiceException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.util.Level;

public class DefaultDelegateErrorHandler extends ErrorHandlerBase implements ErrorHandler
{
	private static final Class CLASS_NAME = DefaultDelegateErrorHandler.class;

	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		
		if ( t instanceof UimaEEServiceException)
		{
			t.printStackTrace();
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, getClass().getName(), "handleError", 
					UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_exception__WARNING", new Object[] { t });
      }
			if ( anErrorContext.containsKey(AsynchAEMessage.Endpoint) )
			{
				Endpoint endpoint = (Endpoint)anErrorContext.get(AsynchAEMessage.Endpoint);
				String casReferenceId = (String)anErrorContext.get(AsynchAEMessage.CasReference);
        String parentCasReferenceId = (String)anErrorContext.get(AsynchAEMessage.InputCasReference);

				try
				{
					aController.getOutputChannel().sendReply(t, casReferenceId, parentCasReferenceId, endpoint, AsynchAEMessage.Process );
				}
				catch( Throwable e)
				{
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

}
