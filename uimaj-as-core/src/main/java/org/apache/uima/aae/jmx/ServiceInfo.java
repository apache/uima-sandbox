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

package org.apache.uima.aae.jmx;

public class ServiceInfo implements ServiceInfoMBean
{
	/**
	 * 
	 */
//	private static final long serialVersionUID = -4662094240276750977L;
	private static final String label="Service Info";
	private String brokerURL="";
	private String inputQueueName="";
	private String replyQueueName="";
	private String state="";
	private String[] deploymentDescriptor= new String[] {""};
	private boolean casMultiplier;
	private boolean topLevel;
	private String serviceKey;
	private boolean aggregate;
	
	public ServiceInfo()
	{
		this(false);
	}
	
	public ServiceInfo( boolean isaCasMultiplier )
	{
		casMultiplier = isaCasMultiplier;
	}
	public String getLabel()
	{
		return label;
	}
	public String getBrokerURL()
	{
		return brokerURL;
	}

	public String[] getDeploymentDescriptor()
	{
		return deploymentDescriptor;
	}
	public void setDeploymentDescriptor(String deploymentDescriptor)
	{
		this.deploymentDescriptor[0] = deploymentDescriptor;
	}
	public void setBrokerURL(String aBrokerURL)
	{
		brokerURL = aBrokerURL;
	}
	public String getInputQueueName()
	{
		return inputQueueName;
	}
	public void setInputQueueName( String anInputQueueName)
	{
		inputQueueName = anInputQueueName;
	}
	public String getState()
	{
		return state;
	}
	public void setState( String aState )
	{
		state = aState;
	}
	public boolean isCASMultiplier()
	{
		return casMultiplier;
	}
	public void setCASMultiplier()
	{
		casMultiplier = true;
	}
	public void setTopLevel()
	{
		topLevel = true;
	}
	public boolean isTopLevel()
	{
		return topLevel;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	public String getReplyQueueName()
	{
		return replyQueueName;
	}
	
	public void setReplyQueueName(String aReplyQueueName)
	{
		replyQueueName = aReplyQueueName;
	}

	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

}
