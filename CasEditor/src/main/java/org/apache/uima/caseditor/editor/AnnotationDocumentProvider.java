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

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProviderExtension;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

/**
 * TODO: add javadoc here
 */
public class AnnotationDocumentProvider extends FileDocumentProvider implements
        IDocumentProviderExtension {
  private Map<IEditorInput, IStatus> mDocumentStatusMap = new HashMap<IEditorInput, IStatus>();

  @Override
  protected IDocument createEmptyDocument() {
    return new AnnotationDocument();
  }

  @Override
  protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding)
          throws CoreException {
    if (!(editorInput instanceof IFileEditorInput)) {
      return super.setDocumentContent(document, editorInput, encoding);
    }

    IFile file = ((IFileEditorInput) editorInput).getFile();

    INlpElement element = CasEditorPlugin.getNlpModel().findMember(file);

    if (!(element instanceof DocumentElement)) {
      IStatus status = new Status(IStatus.INFO, PlatformUI.PLUGIN_ID, IStatus.OK,
              "There is a problem with the typesystem, document could "
                      + "not be created (maybe typesystem not set for "
                      + "project or xcas file outside of project folder).", null);

      mDocumentStatusMap.put(editorInput, status);

      throw new CoreException(status);
    }

    AnnotationDocument annotationDocument = (AnnotationDocument) document;

    DocumentElement documentElement = (DocumentElement) element;

    org.apache.uima.caseditor.core.IDocument workingCopy;

    try {
      workingCopy = documentElement.getDocument();
    } catch (CoreException e) {
      IStatus status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK,
              "There is a problem with the typesystem, document could "
                      + "not be created (maybe typesystem not set for "
                      + "project or xcas file outside of project folder).", null);

      mDocumentStatusMap.put(editorInput, status);

      throw e;
    }

    annotationDocument.setProject(element.getNlpProject());

    annotationDocument.setDocument(workingCopy);

    return true;
  }

  @Override
  protected void doSaveDocument(IProgressMonitor monitor, Object element,
          IDocument document, boolean overwrite) throws CoreException {
    // TODO: refactor this an use DocumentElement for synchronization
    if (element instanceof IFileEditorInput) {

      IFileEditorInput input = (IFileEditorInput) element;

      FileInfo info = (FileInfo) getElementInfo(element);
      
      IFile file = input.getFile();

      AnnotationDocument annotationDocument = (AnnotationDocument) document;

        if (info != null && !overwrite) {
          checkSynchronizationState(info.fModificationStamp, file);
        }

        fireElementStateChanging(element);
        try {
          // file.setContents(stream, overwrite, true, monitor);
        	annotationDocument.save();
        } catch (CoreException e) {
          fireElementStateChangeFailed(element);
          throw e;
        } catch (RuntimeException e) {
          fireElementStateChangeFailed(element);
          throw e;
        }

        if (info != null) {
          ResourceMarkerAnnotationModel model = (ResourceMarkerAnnotationModel) info.fModel;
          model.updateMarkers(info.fDocument);

          info.fModificationStamp = computeModificationStamp(file);
        }

    } else {
      super.doSaveDocument(monitor, element, document, overwrite);
    }
  }

  /**
   * Retries the status of an previusly created document.
   * @param element 
   * 
   * @return the status
   */
  public IStatus getStatus(Object element) {
    IStatus status = mDocumentStatusMap.get(element);

    if (status == null) {
      status = super.getStatus(element);
    }

    return status;
  }
}