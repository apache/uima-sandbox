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

package org.apache.uima.caseditor.ui.model;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class FolderAdpater implements IWorkbenchAdapter {

  public Object[] getChildren(Object o) {
    IFolder folder = (IFolder) o;

    try {
      return folder.members();
    } catch (CoreException e) {
      // TODO: log it
      e.printStackTrace();
      return null;
    }
  }

  public ImageDescriptor getImageDescriptor(Object object) {
    ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
    return sharedImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
  }

  public String getLabel(Object o) {
    IFolder folder = (IFolder) o;

    return folder.getName();
  }

  public Object getParent(Object o) {
    IFolder folder = (IFolder) o;

    // first check if there is an nlp element parent,
    // if this is not the case than use the resource parent

    INlpElement parent;
    try {
      parent = org.apache.uima.caseditor.CasEditorPlugin.getNlpModel().getParent(folder);
    } catch (CoreException e) {
      CasEditorPlugin.log(e);
      parent = null;
    }

    if (parent != null) {
      return parent;
    }
    else {
      return folder.getParent();
    }
  }
}
