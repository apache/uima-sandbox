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

package org.apache.uima.caseditor;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * All images in the cas editor are referenced here. 
 * 
 * Call {@link CasEditorPlugin#getTaeImageDescriptor(Images)} to retrive
 * an actual {@link ImageDescriptor}.
 */
public enum Images {
  
  MODEL_PROJECT_OPEN("tango/project-open.png"),
  
  MODEL_PROJECT_CLOSED("tango/project-closed.png"),
  
  MODEL_FILE("tango/file.png"),
  
  MODEL_FOLDER("tango/folder.png"),
  
  /**
   * The corpus image.
   */
  MODEL_CORPUS("tango/corpus.png"),
  
  /**
   * The document image.
   */
  MODEL_DOCUMENT("tango/document.png"),
  
  /**
   * The source folder image.
   */
  MODEL_PROCESSOR_FOLDER("tango/processor.png"),
  
  /**
   * Image for the typesystem element.
   */
  MODEL_TYPESYSTEM("model/typesystem.gif"),
  
  /**
   * The enabled refresh icon.
   */
  EXPLORER_E_REFRESH("eceview16/refresh_nav.gif"),
  
  /**
   * The disabled refresh icon.
   */
  EXPLORER_D_REFRESH("dceview16/refresh_nav.gif"),
  
  /**
   * The wide left side image.
   */
  WIDE_LEFT_SIDE("WideLeftSide.png"),

  /**
   * The lower left side image.
   */
  LOWER_LEFT_SIDE("LowerLeftSide.png"),

  /**
   * The wide right side image.
   */
  WIDE_RIGHT_SIDE("WideRightSide.png"),

  /**
   * The lower right side image.
   */
  LOWER_RIGHT_SIDE("LowerRightSide.png"),

  /**
   * The merge image.
   */
  MERGE("merge.png"),

  /**
   * The add image.
   */
  ADD("add.png");

  private String mPath;

  private Images(String path) {
    mPath = path;
  }

  /**
   * Retrives the Path. The path is a handel for the shared image.
   * 
   * @return the id
   */
  String getPath() {
    return mPath;
  }
}
