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

/**
 * 
 */
public interface Feature {
	
  public static final int STRING_FEATURE = 1;
  public static final int INTEGER_FEATURE = 2;
  public static final int FLOAT_FEATURE = 3;
  public static final int REFERENCE_FEATURE = 4;
  public static final int CONFIDENCE_FEATURE = 5;
  public static final int RULEID_FEATURE = 6;
  
	/**
	 * Get the feature name of this feature
	 * 
	 * @return returns the feature name
	 */
	public String getName();

	/**
	 * Get the feature type.
	 * 
	 * @return returns the feature type.
	 */
	public int getType();

	/**
	 * Get the feature value of this feature.
	 * 
	 * @return returns the feature value of this feature.
	 */
	public String getValue();

  /**
   * Get the UIMA feature value of this feature object
   * 
   * @return returns the UIMA feature object.
   */
  public org.apache.uima.cas.Feature getFeature();

}