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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.uima.aae.InProcessCache.CacheEntry;
import org.apache.uima.aae.controller.AggregateAnalysisEngineController;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.error.ErrorContext;
import org.apache.uima.aae.error.ErrorHandler;
import org.apache.uima.aae.error.ErrorHandlerBase;
import org.apache.uima.aae.error.MessageTimeoutException;
import org.apache.uima.aae.message.AsynchAEMessage;
import org.apache.uima.aae.monitor.Monitor;

public class DefaultTimeoutHandler extends ErrorHandlerBase implements ErrorHandler 
{
	public DefaultTimeoutHandler( Map anEndpointThreasholdMap )
	{
		super(anEndpointThreasholdMap);
	}
	public boolean handleError(Throwable t, ErrorContext anErrorContext, AnalysisEngineController aController)
	{
		if (t instanceof MessageTimeoutException)
		{
			if (anErrorContext.containsKey(AsynchAEMessage.Command) && anErrorContext.containsKey(AsynchAEMessage.Endpoint))
			{
				Endpoint endpoint = null;
				try
				{
					endpoint = (Endpoint) anErrorContext.get(AsynchAEMessage.Endpoint);
					int command = ((Integer) anErrorContext.get(AsynchAEMessage.Command)).intValue();
					if (AsynchAEMessage.Process == command)
					{
						if ( anErrorContext.containsKey(AsynchAEMessage.CasReference ) )
						{
							
							if (!super.exceedsThreshold(Monitor.ProcessRequestTimeoutCount, endpoint.getEndpoint(), aController))
							{
								if ( endpoint.isRetryEnabled() && aController instanceof AggregateAnalysisEngineController )
								{
									String casReferenceId = (String)anErrorContext.get( AsynchAEMessage.CasReference);
									((AggregateAnalysisEngineController) aController).retryProcessCASRequest(casReferenceId, endpoint, false);
									return true; // Handled the exception
								}
							}
							else
							{
								String action = getAction(Monitor.MetadataRequestTimeoutCount, endpoint.getEndpoint());

								if ( ErrorHandler.DISABLE.equalsIgnoreCase(action))
								{
									List list = new ArrayList();
									list.add(endpoint.getEndpoint());
									((AggregateAnalysisEngineController)aController).disableDelegates(list);
									return true;
								}
							}
							
						}
						else
						{
							//	unhandled error. Missing CasReference Id in error context
						}
					}
					else if (AsynchAEMessage.GetMeta == command)
					{
						String key = ((AggregateAnalysisEngineController) aController).lookUpDelegateKey(endpoint.getEndpoint());
						if (!exceedsThreshold(Monitor.MetadataRequestTimeoutCount, key, aController))
						{
							if (aController instanceof AggregateAnalysisEngineController)
							{
								((AggregateAnalysisEngineController) aController).retryMetadataRequest(endpoint);
								return true; // Handled the exception
							}
						}
						else
						{
							String action = getAction(Monitor.MetadataRequestTimeoutCount, key); //endpoint.getEndpoint());
							if ( action != null )
							{
							    aController.takeAction(action, endpoint.getEndpoint(), anErrorContext);
								return true; // Handled the exception
							}
						}
					}
				}
				catch ( Exception e)
				{
					//e.printStackTrace();
					// Log this Exception and return false. Let the Default
					// Catch All
					// Handler handle this situation
				}
			}
		}
		return false;
	}


}
