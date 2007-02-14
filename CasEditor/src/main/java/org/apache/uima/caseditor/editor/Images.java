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

package org.apache.uima.caseditor.editor;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.3.2.2 $, $Date: 2007/01/04 15:00:55 $
 */
public enum Images {
  /**
   * The wide left side image.
   */
  WIDE_LEFT_SIDE("WideLeftSide.bmp"),

  /**
   * The lower left side image.
   */
  LOWER_LEFT_SIDE("LowerLeftSide.bmp"),

  /**
   * The wide right side image.
   */
  WIDE_RIGHT_SIDE("WideRightSide.bmp"),

  /**
   * The lower right side image.
   */
  LOWER_RIGHT_SIDE("LowerRightSide.bmp"),

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