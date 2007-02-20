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


import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.editor.AnnotationEditor;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * This ia a custom SourceViewerDecorationSupport to support drawing of annoations with dynamically
 * (not configured in plugin.xml) chaning types. These annotations can have configured appeareance.
 */
public class AnnotationViewerDecorationSupport extends SourceViewerDecorationSupport {
  private NlpProject mProject;

  /**
   * Initializes a new AnnotationViewerDecorationSupport.
   * 
   * @param sourceViewer
   * @param overviewRuler
   * @param annotationAccess
   * @param sharedTextColors
   * @param editor
   */
  public AnnotationViewerDecorationSupport(ISourceViewer sourceViewer,
          IOverviewRuler overviewRuler, IAnnotationAccess annotationAccess,
          ISharedTextColors sharedTextColors, AnnotationEditor editor) {
    super(sourceViewer, overviewRuler, annotationAccess, sharedTextColors);

    mProject = editor.getDocument().getProject();
  }

  @Override
  protected AnnotationPainter createAnnotationPainter() {
    // get all annotation types
    // put them with default painter to the map

    AnnotationPainter painter = super.createAnnotationPainter();

    // register custom annotation painter for squiggles ...
    // this painter paints any other annotation than "eclipse
    // annotation peer" with default anntoation painter
    // eclipse annotation peer annotation are painted by
    // an custom annotatin painter which is specified

    painter.addDrawingStrategy(AnnotationPreference.STYLE_SQUIGGLES, new AnnotationDrawingStrategy(
            mProject));

    return painter;
  }
}