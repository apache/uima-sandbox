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

import java.awt.Color;


/**
 * The <code>AnnotationStyle</code> describes the look of an certain annotation type in the
 * <code>AnnotationEditor</code>.
 */
public final class AnnotationStyle {
  /**
   * The styles that can be used to draw an annotation.
   */
  public enum Style {

    TEXT_COLOR,

    TOKEN,

    /**
     * The squiggles style.
     */
    SQUIGGLES,

    /**
     * The box style.
     */
    BOX,

    /**
     * The underline style.
     */
    UNDERLINE,

    /**
     * The bracket style.
     */
    BRACKET
  }

  /**
   * The default <code>DrawingStyle<code>.
   */
  public static final Style DEFAULT_STYLE = Style.SQUIGGLES;

  /**
   * The default drawing color.
   */
  public static final Color DEFAULT_COLOR = new Color(0xff, 0, 0);

  private String mAnnotation;

  private Style mStyle;

  private Color mColor;

  /**
   * Initialize a new instance.
   *
   * @param annotation -
   *          the annotation type
   * @param style -
   *          the drawing style
   * @param color -
   *          annotation color
   */
  public AnnotationStyle(String annotation, Style style, Color color) {
    // annoatation
    if (annotation == null) {
      throw new IllegalArgumentException("annotation must be not null!");
    }

    mAnnotation = annotation;

    // style
    if (style == null) {
      throw new IllegalArgumentException("style must be not null!");
    }

    mStyle = style;

    // color
    if (color == null) {
      throw new IllegalArgumentException("color must be not null!");
    }

    mColor = color;
  }

  /**
   * Retrives the annoation type.
   *
   * @return - annotation type.
   */
  public String getAnnotation() {
    return mAnnotation;
  }

  /**
   * Retrives the drawing style of the annotation.
   *
   * @return - annotation drawing style
   */
  public Style getStyle() {
    return mStyle;
  }

  /**
   * Retrives the color of the annotation.
   *
   * @return - annotation color
   */
  public Color getColor() {
    return mColor;
  }

  /**
   * Compares if current is equal to another object.
   */
  @Override
  public boolean equals(Object object) {
    boolean isEqual;

    if (object != this) {
      if (object instanceof AnnotationStyle) {
        AnnotationStyle style = (AnnotationStyle) object;

        isEqual = mAnnotation.equals(style.mAnnotation) && mStyle.equals(style.mStyle)
                && mColor.equals(style.mColor);
      } else {
        isEqual = false;
      }
    } else {
      isEqual = true;
    }

    return isEqual;
  }

  /**
   * Generates a hash code using of toString()
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /**
   * Represents this object as string.
   */
  @Override
  public String toString() {
    String annotationStyle = "Type: " + mAnnotation;
    annotationStyle += " Style: " + mStyle.name();
    annotationStyle += " Color: " + mColor.toString();

    return annotationStyle;
  }
}