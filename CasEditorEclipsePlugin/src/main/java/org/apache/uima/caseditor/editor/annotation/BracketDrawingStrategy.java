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

import org.apache.uima.cas.text.AnnotationFS;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Draws brackets arround an annotation.
 */
public class BracketDrawingStrategy implements IDrawingStrategy {
  private static final int BRACKET_WIDTH = 3;

  /**
   * Draws an opening bracket to the begin and a closing bracket to the end of the given annotation.
   *
   * @param annotation
   * @param gc
   * @param textWidget
   * @param offset
   * @param length
   * @param color
   */
  public void draw(Annotation annotation, GC gc, StyledText textWidget, int offset, int length,
          Color color) {
    if (length > 0) {
      if (annotation instanceof EclipseAnnotationPeer) {
        AnnotationFS annotationFS = ((EclipseAnnotationPeer) annotation).getAnnotationFS();

        if (gc != null) {
          Rectangle bounds = textWidget.getTextBounds(offset, offset + length - 1);

          gc.setForeground(color);

          boolean isDrawOpenBracket = annotationFS.getBegin() == offset;
          if (isDrawOpenBracket) {
            gc.drawLine(bounds.x, bounds.y + bounds.height - 1, bounds.x + BRACKET_WIDTH, bounds.y
                    + bounds.height - 1);

            gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);

            gc.drawLine(bounds.x, bounds.y, bounds.x + BRACKET_WIDTH, bounds.y);
          }

          boolean isDrawCloseBracket = annotationFS.getEnd() == offset + length;
          if (isDrawCloseBracket) {
            gc.drawLine(bounds.x + bounds.width, bounds.y + bounds.height - 1, bounds.x
                    + bounds.width - BRACKET_WIDTH, bounds.y + bounds.height - 1);

            gc.drawLine(bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y
                    + bounds.height - 1);

            gc.drawLine(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width - BRACKET_WIDTH,
                    bounds.y);
          }
        } else {
          textWidget.redrawRange(offset, length, true);
        }
      }
    }
  }
}