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
package org.apache.uima.annotator.regex;

import java.util.regex.Pattern;

/**
 * 
 */
public interface FilterFeature {
	
	/**
	 * Get the feature name of this feature
	 * 
	 * @return returns the feature name
	 */
	public String getName();

	/**
	 * Get the pattern for this filter feature
	 * 
	 * @return returns the pattern for this filter feature
	 */
	public Pattern getPattern();

  /**
   * Get the UIMA feature value of this feature object
   * 
   * @return returns the UIMA feature object.
   */
  public org.apache.uima.cas.Feature getFeature();

}