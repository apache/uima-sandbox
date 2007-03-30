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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;

public class FileDocumentProvider extends AbstractDocumentProvider {

  @Override
  protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
    return new AnnotationModel();
  }

  @Override
  protected IDocument createDocument(Object element) throws CoreException {
    
    FileEditorInput input = (FileEditorInput) element;
    
    IFile inputFile = input.getFile();
    
    InputStream in = inputFile.getContents();
    Reader reader = new InputStreamReader(in);
    
    IDocument document = new Document();
    
    try {
    
    StringBuilder documentText = new StringBuilder();
    char buffer[] = new char[1];
    int length = -1;
    while ((length = reader.read(buffer)) != -1) {
      documentText.append(buffer, 0, length);
    }
    
    document.set(documentText.toString());
    }
    catch (IOException e) {
      IStatus s= new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, e.getMessage(), e);
      throw new CoreException(s);
    }
    finally {
      try {
        if (in != null) {
        in.close();
        }
      } catch (IOException e) {
      }
    }
    
    return document;
  }

  @Override
  protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
    // save document text <- use platform default encoding
  }

  @Override
  protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
    return null;
  }
}
