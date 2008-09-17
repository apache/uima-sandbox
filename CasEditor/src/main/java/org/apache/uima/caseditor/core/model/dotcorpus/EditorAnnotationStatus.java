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
import java.util.HashSet;

import org.apache.uima.cas.Type;

/**
 * TODO: add javadoc here
 */
public class EditorAnnotationStatus {
  private String mMode;

  private Collection<String> mDisplayAnnotations = new HashSet<String>();

  /**
   * Initializes a new instance.
   *
   * @param mode
   * @param displayAnnotations
   */
  public EditorAnnotationStatus(String mode, Collection<Type> displayAnnotations) {
    if (mode == null) {
      throw new IllegalArgumentException("Mode must not be null!");
    }

    mMode = mode;

    if (displayAnnotations != null) {

      for (Type type : displayAnnotations) {
        mDisplayAnnotations.add(type.getName());
      }
    }
  }

  /**
   * Retrieves the editor mode.
   *
   * @return the editor mode
   */
  public String getMode() {
    return mMode;
  }

  /**
   * Retrieves the annotations which a displayed in the editor.
   *
   * @return the display annotations
   */
  public Collection<String> getDisplayAnnotations() {
    return mDisplayAnnotations;
  }
}