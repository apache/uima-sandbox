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

package org.apache.uima.caseditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

public class FileEditorInput implements IEditorInput, IPathEditorInput, IPersistableElement {

  private IFile mFile;

  public FileEditorInput(IFile file) {
    if (file == null) {
      throw new IllegalArgumentException();
    }
    mFile = file;
  }

  public IFile getFile() {
    return mFile;
  }

  public boolean exists() {
    return mFile.exists();
  }

  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  public String getName() {
    return mFile.getName();
  }

  public IPersistableElement getPersistable() {
    return this;
  }

  public String getToolTipText() {
    return mFile.getFullPath().toString();
  }

  public Object getAdapter(Class adapter) {
    return null;
  }

  public IPath getPath() {
    return mFile.getLocation();
  }

  public String getFactoryId() {
    return FileEditorInputFactory.ID;
  }

  public void saveState(IMemento memento) {
    FileEditorInputFactory.saveState(memento, this);
  }

  @Override
  public int hashCode() {
    return mFile.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof FileEditorInput) {
      FileEditorInput other = (FileEditorInput) obj;
      return mFile.equals(other.getFile());
    } else {
      return false;
    }
  }
}
