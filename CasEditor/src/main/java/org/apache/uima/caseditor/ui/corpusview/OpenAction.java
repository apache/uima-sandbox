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

package org.apache.uima.caseditor.ui.corpusview;

import org.apache.uima.caseditor.FileEditorInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Opens a file in the default editor.
 */
final class OpenAction extends ResourceAction {

  private IWorkbenchPage mPage;

  protected OpenAction(IWorkbenchPage page) {
    super("Open");
    mPage = page;
  }

  @Override
  public void run() {

    IResource selectedResource = getSelectedResources().get(0);

    if (selectedResource instanceof IFile) {

      IFile file = (IFile) selectedResource;

      IEditorRegistry editorReg = PlatformUI.getWorkbench().getEditorRegistry();

      IEditorDescriptor descriptor = editorReg.getDefaultEditor(file.getName());

      if (descriptor == null) {
        descriptor = editorReg.findEditor("org.apache.uima.caseditor.texteditor");
      }

      try {
        mPage.openEditor(new FileEditorInput(file), descriptor.getId(), true);
      } catch (PartInitException e) {
        MessageDialog.openError(mPage.getWorkbenchWindow().getShell(), "Error during opening!", e
                .getMessage());
      }
    }
  }

  @Override
  protected boolean updateSelection(IStructuredSelection selection) {
    if (getSelectedResources().size() != 1) {
      return false;
    } else if (getSelectedResources().get(0).getType() != IResource.FILE) {
      return false;
    } else {
      return true;
    }
  }
}
