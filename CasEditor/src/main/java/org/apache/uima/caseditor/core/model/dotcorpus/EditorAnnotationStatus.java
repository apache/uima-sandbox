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

package org.apache.uima.caseditor.core.model.dotcorpus;

import java.util.Collection;
import java.util.Collections;

import org.apache.uima.cas.Type;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.2 $, $Date: 2007/01/04 14:56:24 $
 */
public class EditorAnnotationStatus {
  private Type mMode;

  private Collection<Type> mDisplayAnnotations;

  /**
   * Initializes a new instance.
   * 
   * @param mode
   * @param displayAnnotations
   */
  public EditorAnnotationStatus(Type mode, Collection<Type> displayAnnotations) {
    if (mode == null) {
      throw new IllegalArgumentException("Mode must not be null!");
    }

    mMode = mode;

    if (displayAnnotations != null) {
      mDisplayAnnotations = Collections.unmodifiableCollection(displayAnnotations);
    } else {
      mDisplayAnnotations = Collections.emptyList();
    }
  }

  /**
   * Retrives the editor mode.
   * 
   * @return the editor mode
   */
  public Type getMode() {
    return mMode;
  }

  /**
   * Retrives the annotations wich a displayed in the editor.
   * 
   * @return the display annotations
   */
  public Collection<Type> getDisplayAnnotations() {
    return mDisplayAnnotations;
  }
}