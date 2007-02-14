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

package org.apache.uima.caseditor.core.model.delta;


import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.core.resources.IResource;

/**
 * A element delta represents changes in the state of a element tree between two discrete points in
 * time.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.3.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public interface INlpElementDelta {
  /**
   * Accepts the given visitor. The only kinds of resource deltas visited are <code>ADDED</code>,
   * <code>REMOVED</code>, and <code>CHANGED</code>.
   * 
   * @param visotor
   */
  void accept(INlpModelDeltaVisitor visotor);

  /**
   * Returns resource deltas for all children of this resource which were added, removed, or
   * changed. Returns an empty array if there are no affected children.
   * 
   * @return - childs or empty array
   */
  public INlpElementDelta[] getAffectedChildren();

  /**
   * Returns true if the given element is an nlp element.
   * 
   * @return true if an nlp element otherwise false
   */
  boolean isNlpElement();

  /**
   * Retrives the nlp element.
   * 
   * @return the nlp element or if non null.
   */
  INlpElement getNlpElement();

  /**
   * Retrives the resource belonging to this delta.
   * 
   * @return the resource
   */
  IResource getResource();

  /**
   * Retrives the kind.
   * 
   * @return the kind
   */
  Kind getKind();

  /**
   * Retrives the flags.
   * 
   * @return the flags
   */
  int getFlags();
}