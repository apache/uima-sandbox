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

package org.apache.uima.caseditor.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;


import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.IDocument;
import org.apache.uima.caseditor.core.IDocumentListener;
import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.uima.DocumentUimaImpl;
import org.apache.uima.caseditor.core.util.Span;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The document element contains the uima cas document.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.7.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public final class DocumentElement extends AbstractNlpElement implements INlpElement, IDocument,
        IAdaptable {
  private CorpusElement mParent;

  private IFile mDocumentFile;

  private IDocument mDocument;

  /**
   * Initializes a new instance.
   * 
   * @param corpus
   * @param documentFile
   */
  DocumentElement(CorpusElement corpus, IFile documentFile) {
    mParent = corpus;
    mDocumentFile = documentFile;
  }

  /**
   * Retrives the TCAS object of the current document.
   */
  public CAS getCAS() {
    if (getDocument() != null) {
      return getDocument().getCAS();
    } else {
      return null;
    }
  }

  /**
   * Retrives the coresponding resource.
   */
  public IResource getResource() {
    return mDocumentFile;
  }

  /**
   * Retrives the name.
   */
  public String getName() {
    return mDocumentFile.getName();
  }

  private synchronized IDocument getDocument() {
    if (mDocument != null) {
      return mDocument;
    }

    try {
      mDocument = getWorkingCopy();
    } catch (CoreException e1) {
      TaeCorePlugin.log(e1);
      return null;
    }

    mDocument.addChangeListener(new IDocumentListener() {
      public void added(FeatureStructure newAnnotation) {
        writeToFile();
      }

      public void added(Collection<FeatureStructure> newAnnotations) {
        writeToFile();
      }

      public void removed(FeatureStructure deletedAnnotation) {
        writeToFile();
      }

      public void removed(Collection<FeatureStructure> deletedAnnotations) {
        writeToFile();
      }

      public void updated(FeatureStructure annotation) {
        writeToFile();
      }

      public void updated(Collection<FeatureStructure> annotations) {
        writeToFile();
      }

      private void writeToFile() {
        try {
          ByteArrayOutputStream outStream = new ByteArrayOutputStream(40000);
          serialize(outStream);

          InputStream stream = new ByteArrayInputStream(outStream.toByteArray());

          mDocumentFile.setContents(stream, true, false, null);
        } catch (CoreException e) {
          // TODO: handle it
          e.printStackTrace();
        }
      }
    });

    return mDocument;

  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   * 
   * @param out
   * @throws CoreException
   */
  public synchronized void serialize(OutputStream out) throws CoreException {
    if (getDocument() != null) {
      getDocument().serialize(out);
    }
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void addFeatureStructure(FeatureStructure annotation) {
    getDocument().addFeatureStructure(annotation);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void addFeatureStructures(Collection<FeatureStructure> annotations) {
    getDocument().addFeatureStructures(annotations);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public void addAnnotations(Collection<AnnotationFS> annotations) {
    getDocument().addAnnotations(annotations);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void removeAnnotation() {
    getDocument().removeAnnotation();
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void removeFeatureStructure(FeatureStructure annotation) {
    getDocument().removeFeatureStructure(annotation);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public void removeAnnotations(Collection<AnnotationFS> annotationsToRemove) {
    getDocument().removeAnnotations(annotationsToRemove);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void removeFeatureStructures(Collection<FeatureStructure> annotationsToRemove) {
    getDocument().removeFeatureStructures(annotationsToRemove);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public void update(FeatureStructure annotation) {
    getDocument().update(annotation);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public void updateFeatureStructure(Collection<FeatureStructure> annotations) {
    getDocument().updateFeatureStructure(annotations);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public void updateAnnotations(Collection<AnnotationFS> annotations) {
    getDocument().updateAnnotations(annotations);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized Collection<AnnotationFS> getAnnotations(Type type) {
    return getDocument().getAnnotations(type);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized Map<Integer, AnnotationFS> getView(Type annotationType) {
    return getDocument().getView(annotationType);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void addChangeListener(IDocumentListener listener) {
    getDocument().addChangeListener(listener);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized void removeChangeListener(IDocumentListener listener) {
    getDocument().removeChangeListener(listener);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized Collection<AnnotationFS> getAnnotation(Type type, Span span) {
    return getDocument().getAnnotation(type, span);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized String getText() {
    return getDocument().getText();
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public synchronized String getText(int start, int end) {
    return getDocument().getText(start, end);
  }

  /**
   * Forwards the call to {@link IDocument} instance retrived by getDocument().
   */
  public Type getType(String type) {
    return getDocument().getType(type);
  }

  /**
   * Does nothing.
   * 
   * @param content
   */
  public synchronized void setContent(InputStream content) {
    // must not be implemented
  }

  /**
   * Retrives the parent.
   */
  public INlpElement getParent() {
    return mParent;
  }

  /**
   * Retrives the working copy.
   * 
   * @return the working copy
   * @throws CoreException
   */
  public IDocument getWorkingCopy() throws CoreException {
    InputStream in = mDocumentFile.getContents();

    // TODO: create typeSystemExtension.xml element ...
    // and pass this to th UIMADocument
    DocumentUimaImpl document = new DocumentUimaImpl((NlpProject) mParent.getParent());

    document.setContent(in);

    return document;
  }

  /**
   * Writes the document element to the file system.
   * 
   * @throws CoreException
   */
  public void writeToFile() throws CoreException {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream(40000);
    serialize(outStream);

    InputStream stream = new ByteArrayInputStream(outStream.toByteArray());

    mDocumentFile.setContents(stream, true, false, null);
  }

  /**
   * Retrives the coresponding {@link NlpProject} instance.
   * 
   * @return the {@link NlpProject} instance
   */
  public NlpProject getNlpProject() {
    return (NlpProject) getParent().getParent();
  }

  /**
   * Not implemented.
   */
  @Override
  void addResource(IResource resource) {
    // not needed here, there are no resources
  }

  @Override
  void changedResource(IResource resource, INlpElementDelta delta) {
    mDocument = null;
  }

  /**
   * Not implemented.
   */
  @Override
  void removeResource(IResource resource) {
    // not needed here, there are no resources
  }

  /**
   * Generates a hash code for the current instance.
   */
  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}