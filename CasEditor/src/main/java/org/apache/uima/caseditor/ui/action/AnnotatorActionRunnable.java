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

package org.apache.uima.caseditor.ui.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.caseditor.core.IDocument;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.uima.AnnotatorConfiguration;
import org.apache.uima.caseditor.editor.AnnotationEditor;
import org.apache.uima.resource.ResourceInitializationException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 */
public final class AnnotatorActionRunnable implements IRunnableWithProgress {
  private AnnotatorConfiguration mAnnotatorConfiguration;

  private Collection<DocumentElement> mDocuments;

  /**
   * Initializes a new instance.
   *
   * @param annotator
   * @param documents
   * @param shell
   */
  public AnnotatorActionRunnable(AnnotatorConfiguration annotator,
          Collection<DocumentElement> documents) {
    mAnnotatorConfiguration = annotator;
    mDocuments = documents;
  }

  /**
   * Creates a list of all {@link AnnotationEditor} which are currently opened.
   */
  private AnnotationEditor[] getAnnotationEditors() {

    ArrayList<AnnotationEditor> dirtyParts = new ArrayList<AnnotationEditor>();
    IWorkbenchWindow windows[] = PlatformUI.getWorkbench().getWorkbenchWindows();
    for (IWorkbenchWindow element : windows) {
      IWorkbenchPage pages[] = element.getPages();
      for (IWorkbenchPage page : pages) {
        IEditorPart[] parts = page.getEditors();

        for (IEditorPart part : parts) {
          if (part instanceof AnnotationEditor) {
            AnnotationEditor editor = (AnnotationEditor) part;
            dirtyParts.add(editor);
          }
        }
      }
    }

    return dirtyParts.toArray(new AnnotationEditor[dirtyParts.size()]);
  }

  /**
   * Executes the action.
   */
  public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
    monitor.beginTask("Tagging", IProgressMonitor.UNKNOWN);

    monitor.subTask("Initializing tagger, " + "please stand by.");

    AnalysisEngine annotatorInstance;

    try {
      annotatorInstance = mAnnotatorConfiguration.createAnnotator();
    } catch (final ResourceInitializationException e) {
      throw new InvocationTargetException(e);
    }

    monitor.subTask("Tagging, please stand by.");

    Map<DocumentElement, AnnotationEditor> editorMap =
        new HashMap<DocumentElement, AnnotationEditor>();

    for (AnnotationEditor annotationEditor : getAnnotationEditors()) {
      editorMap.put(annotationEditor.getDocument().getDocumentElement(), annotationEditor);
    }

    for (DocumentElement element : mDocuments) {

      final IDocument document;
      try {
        document = element.getDocument(); // retrieve the working copy
      } catch (CoreException e1) {
        throw new InvocationTargetException(e1);
      }

      try {
        annotatorInstance.process(document.getCAS());
      } catch (AnalysisEngineProcessException e) {
        throw new InvocationTargetException(e);
      }

      // currently all updates are made from the ui thread, just post this call to change
      // to the ui job queue

      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          document.changed();
        }
      });

      try {

        if (editorMap.get(element) == null) {
          // file is not opened in any editor, just save the changes
          element.saveDocument();
        }
        else if (!editorMap.get(element).isDirty()) {
          // element is opened in editor and not dirty
          AnnotationEditor editor = editorMap.get(element);
          editor.setDirty();
        }
        else {
          // element is opened in editor and dirty, do nothing
        }
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    annotatorInstance.destroy();
    annotatorInstance = null;

    monitor.done();
  }
}