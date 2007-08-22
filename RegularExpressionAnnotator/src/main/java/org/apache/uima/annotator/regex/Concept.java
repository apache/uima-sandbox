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
public interface Concept {

	/**
	 * Get the concept name.
	 * 
	 * @return returns the concept name.
	 */
	public String getName();
	
	/**
	 * Gets an array of annotations for this rule.
	 * 
	 * @return returns an array of annotations for this concept.
	 */
	public Annotation[] getAnnotations();

	/**
	 * Adds the given annotation to this concept.
	 * 
	 * @param aAnnot The annotation to be added.
	 */
	public void addAnnotation(Annotation aAnnot);

  /**
   * Gets an array of rules for this concept
   * 
   * @return returns an array of rules for this concept.
   */
  public Rule[] getRules();

  /**
   * Adds the given rule to this concept
   * 
   * @param aRule The rule to be added.
   */
  public void addRule(Rule aRule);


	
}