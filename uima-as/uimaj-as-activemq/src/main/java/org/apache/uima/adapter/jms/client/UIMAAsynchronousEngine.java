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

package org.apache.uima.adapter.jms.client;

import java.net.URI;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.springframework.context.ApplicationContext;

public interface UIMAAsynchronousEngine
{
	public void initialize( String[] configFiles ) throws ResourceInitializationException;
	public void addListener( StatusCallbackListener aListener);
	public void sendCAS( CAS aCAS ) throws ResourceProcessException;
	public CAS getCAS() throws Exception;
	public void process( URI aCollection ) throws ResourceProcessException;

}
