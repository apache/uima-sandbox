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

package org.apache.uima.caseditor.editor.annotation;

import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;

/**
 * An enumertation of all available {@link IDrawingStrategy}.
 */
public enum DrawingStyle {
  
  TEXT_COLOR(new TextColorDrawingStrategy()),
  
  /**
   * The squiggles {@link IDrawingStrategy}.
   */
  SQUIGGLES(new org.eclipse.jface.text.source.AnnotationPainter.SquigglesStrategy()),

  /**
   * The box {@link IDrawingStrategy}.
   */
  BOX(new BoxDrawingStrategy()),

  /**
   * The underline {@link IDrawingStrategy}.
   */
  UNDERLINE(new UnderlineDrawingStrategy()),

  /**
   * The bracket {@link IDrawingStrategy}.
   */
  BRACKET(new BracketDrawingStrategy());

  private IDrawingStrategy mStrategy;

  private DrawingStyle(IDrawingStrategy strategy) {
    mStrategy = strategy;
  }

  public IDrawingStrategy getStrategy() {
    return mStrategy;
  }
}