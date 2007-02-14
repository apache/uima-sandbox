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

package org.apache.uima.caseditor.core.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Interface for all nlp element.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public interface INlpElement {
  /**
   * Retrives the name of the nlp element.
   * 
   * @return the name
   */
  String getName();

  /**
   * Retrives the corresponding resource.
   * 
   * @return correspondign resource
   */
  IResource getResource();

  /**
   * Retrives the parent element.
   * 
   * @return the parent element
   */
  INlpElement getParent();

  /**
   * Retrives the INlpElement parent of a resource.
   * 
   * @param resource
   * @return the INlpElement parent or null if none
   * @throws CoreException
   */
  INlpElement getParent(IResource resource) throws CoreException;

  /**
   * Retrives the nlp project.
   * 
   * @return the project
   */
  NlpProject getNlpProject();

  /**
   * Searches the corresponding nlp element for the given resource.
   * 
   * @param resource
   *          the resource to search
   * @return the nlp element or null if not found
   */
  INlpElement findMember(IResource resource);
}
