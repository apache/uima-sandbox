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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class FileEditorInputFactory implements IElementFactory {

  public static final String ID = "org.apache.uima.caseditor.FileEditorInputFactory";

  public static final String PATH = "path";

  public IAdaptable createElement(IMemento memento) {

    String fileName = memento.getString(PATH);

    if (fileName != null) {
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileName));

      if (file != null) {
        return new FileEditorInput(file);
      }
    }

    // element could not be re-created
    return null;
  }

  public static void saveState(IMemento memento, FileEditorInput input) {
    memento.putString(PATH, input.getFile().getFullPath().toString());
  }
}
