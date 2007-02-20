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


import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.model.dotcorpus.AnnotationStyle;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * The AnnotationDrawingStrategy of this editor.
 */
public class AnnotationDrawingStrategy implements
        org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy {
  private IDrawingStrategy mDefaultDrawingStrategy;

  private NlpProject mProject;

  /**
   * Initializes a new instance.
   * 
   * @param project
   */
  public AnnotationDrawingStrategy(NlpProject project) {
    mProject = project;

    mDefaultDrawingStrategy = new org.eclipse.jface.text.source.AnnotationPainter.SquigglesStrategy();
  }

  /**
   * Draws an annotation of the given length start at the given offset in the given color in a
   * configured style onto the specified GC.
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
    // default drawing strategy is Squiggles
    if (annotation instanceof EclipseAnnotationPeer) {
      EclipseAnnotationPeer peerAnnotation = (EclipseAnnotationPeer) annotation;

      Type annotationType = peerAnnotation.getAnnotationType();

      AnnotationStyle painter = mProject.getDotCorpus().getAnnotation(annotationType);

      IDrawingStrategy strategy = DrawingStyle.valueOf(painter.getStyle().name()).getStrategy();

      java.awt.Color colorAWT = painter.getColor();

      Color colorSWT = new Color(Display.getDefault(), colorAWT.getRed(), colorAWT.getGreen(),
              colorAWT.getBlue());

      strategy.draw(annotation, gc, textWidget, offset, length, colorSWT);
    } else {
      mDefaultDrawingStrategy.draw(annotation, gc, textWidget, offset, length, color);
    }
  }
}