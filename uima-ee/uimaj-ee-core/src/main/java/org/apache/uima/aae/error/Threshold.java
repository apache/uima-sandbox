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

package org.apache.uima.aae.error;

public class Threshold
{
	private long threshold;

	private String action;

	private long window;

	private int maxRetries;

	private boolean continueOnRetryFailure;

	public Threshold() {}
	
	public Threshold(long aThreshold, long aWindow, String anAction)
	{
		this.threshold = aThreshold;
		this.window = aWindow;
		action = anAction;
	}

	public Threshold(long aThreshold, String anAction)
	{
		this.threshold = aThreshold;
		this.window = 100;
		action = anAction;
	}

	public long getWindow()
	{
		return window;
	}

	public void setWindow(long aWindow)
	{
		window = aWindow;
	}

	public long getThreshold()
	{
		return threshold;
	}

	public long getThresholdCount()
	{
		return threshold;
	}
	
//	public void setThresholdCount( long aCount )
	public void setThreshold( long aCount )
	{
		threshold = aCount;
	}
	public String getAction()
	{
		return action;
	}

	public boolean exceeded(long value)
	{
		if (threshold == 0) {
		  return false;  
    }
    return (value >= threshold-1);
	}
	public boolean maxRetriesExceeded( long value )
	{
		return (value >= maxRetries);
	}

	public long getMax()
	{
		return threshold;
	}

	public int getMaxRetries()
	{
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries)
	{
		this.maxRetries = maxRetries;
	}

	public boolean getContinueOnRetryFailure()
	{
		return continueOnRetryFailure;
	}

	public void setContinueOnRetryFailure(boolean continueOnRetryFailure)
	{
		this.continueOnRetryFailure = continueOnRetryFailure;
	}


	public void setAction(String action)
	{
		this.action = action;
	}
}
