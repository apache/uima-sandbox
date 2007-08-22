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

import org.apache.uima.cas.Type;

/**
 * 
 */
public interface Rule {

  public static final int MATCH_FIRST = 1;
  public static final int MATCH_ALL = 2;
  public static final int MATCH_COMPLETE = 3;

	/**
	 * Get the regular expression of this rule.
	 * 
	 * @return returns the regular expression of this rule.
	 */
	public Pattern getRegexPattern();

  /**
   * Get the rule id
   * 
   * @return returns the rule id
   */
  public String getId();

  /**
   * Get the rule confidence
   * 
   * @return returns the rule confidence
   */
  public float getConfidence();

	/**
	 * Get the match strategy of this rule;
	 * 
	 * @return returns the match strategy of this rule.
	 */
	public int getMatchStrategy();
	
	/**
	 * Get the match type of this rule.
	 * 
	 * @return returns the match type of this rule.
	 */
	public Type getMatchType();

  /**
   * Adds the given feature to the match type filter features
   * 
   * @param aFeature The feature to be added.
   */
  public void addFilterFeature(FilterFeature aFeature);
  
  /**
   * Retuns the match type filter features
   * 
   * @return returns the match type feature filters
   */
  public FilterFeature[] getMatchTypeFilterFeatures();

  /**
   * Adds the given feature to the match type annotation update features
   * 
   * @param aFeature The feature to be added.
   */
  public void addUpdateFeature(Feature aFeature);
  
  /**
   * Retuns the match type annotation update features
   * 
   * @return returns the match type annotation update features
   */
  public Feature[] getMatchTypeUpdateFeatures();

  /**
   * Adds the given exception to this rule
   * 
   * @param aException The exception to be added.
   */
  public void addException(RuleException aException);
  
  /**
   * Retuns the exceptions for this rule
   * 
   * @return returns the exceptions for this rule
   */
  public RuleException[] getExceptions();

	
}