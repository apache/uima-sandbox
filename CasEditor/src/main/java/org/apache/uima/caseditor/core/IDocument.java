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

package org.apache.uima.caseditor.core;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.util.Span;
import org.eclipse.core.runtime.CoreException;

/**
 * TODO: add javadoc here
 */
public interface IDocument {
	
  /**
   * Adds a given change listener.
   * 
   * @param listener
   */
  void addChangeListener(IDocumentListener listener);

  /**
   * Removes the given change listener.
   * 
   * @param listener
   */
  void removeChangeListener(IDocumentListener listener);

  /**
   * Retrives the CAS.
   * 
   * @return the CAS
   */
  CAS getCAS();

  /**
   * Adds a {@link FeatureStructure} to the document.
   * 
   * @param structure -
   *          the {@link FeatureStructure} to add.
   */
  void addFeatureStructure(FeatureStructure structure);

  /**
   * Adds the {@link FeatureStructure}s.
   * 
   * @param structures
   */
  void addFeatureStructures(Collection<FeatureStructure> structures);

  /**
   * Adds the {@link AnnotationFS}s.
   * 
   * @param annotations
   */
  void addAnnotations(Collection<AnnotationFS> annotations);

  /**
   * Removes an {@link FeatureStructure} from the Document.
   * 
   * @param structure -
   *          the {@link FeatureStructure} to remove.
   */
  void removeFeatureStructure(FeatureStructure structure);

  /**
   * Removes the given {@link FeatureStructure}s.
   * 
   * @param structuresToRemove
   */
  void removeFeatureStructures(Collection<FeatureStructure> structuresToRemove);

  /**
   * Removes the given {@link AnnotationFS}s.
   * 
   * @param annotationsToRemove
   */
  void removeAnnotations(Collection<AnnotationFS> annotationsToRemove);

  /**
   * Remove all annotations.
   * 
   * @deprecated
   */
  void removeAnnotation();

  /**
   * Updates the given {@link FeatureStructure}.
   * 
   * @param structure
   */
  void update(FeatureStructure structure);

  /**
   * Updates the given {@link FeatureStructure}s.
   * 
   * @param structures
   */
  void updateFeatureStructure(Collection<FeatureStructure> structures);

  /**
   * Updates the given {@link AnnotationFS}s.
   * 
   * @param annotations
   */
  void updateAnnotations(Collection<AnnotationFS> annotations);

  /**
   * Returns all <code>Annotation</code>s of the given type.
   * 
   * @param type -
   *          type of the requested <code>Annotation</code>s.
   * @return - return all <code>Annotation</code> of the given type or null if no
   *         <code>Annotation</code> of this type exist.
   */
  Collection<AnnotationFS> getAnnotations(Type type);

  /**
   * Retrives the view map.
   * 
   * @param annotationType
   * @return the view map
   */
  Map<Integer, AnnotationFS> getView(Type annotationType);

  /**
   * Retrives the annotations of the given type inside the given span.
   * 
   * @param type
   * @param span
   * @return the annotations
   */
  Collection<AnnotationFS> getAnnotation(Type type, Span span);

  /**
   * Retrvies the text.
   * 
   * @return the text as string
   */
  String getText();

  /**
   * Retrives the text betweene start and end offstes.
   * 
   * @param start
   * @param end
   * @return the text
   */
  String getText(int start, int end);

  /**
   * Retrives the requested type.
   * 
   * @param type
   * @return the type
   */
  Type getType(String type);

  /**
   * Sets the content of this document.
   * 
   * @param content
   * @throws CoreException
   */
  void setContent(InputStream content) throws CoreException;

  /**
   * Serializes the doucment to the given OutputStream.
   * 
   * @param out
   * @throws CoreException
   */
  void save() throws CoreException;
}