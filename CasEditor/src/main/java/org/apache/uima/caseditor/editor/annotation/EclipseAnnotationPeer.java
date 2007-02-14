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

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.2 $, $Date: 2007/01/04 15:00:56 $
 */
public class EclipseAnnotationPeer extends org.eclipse.jface.text.source.Annotation {
  private AnnotationFS mAnnotation;

  /**
   * Initializes a new instance.
   * 
   * @param name
   * @param isPersitent
   * @param text
   */
  public EclipseAnnotationPeer(String name, boolean isPersitent, String text) {
    super(name, isPersitent, text);
  }

  /**
   * Sets the annotation.
   * 
   * @param annotation
   */
  public void setAnnotation(AnnotationFS annotation) {
    mAnnotation = annotation;
    setText(annotation.getCoveredText());
  }

  /**
   * Retrives the annotation.
   * 
   * @return the annotation
   */
  public AnnotationFS getAnnotationFS() {
    return mAnnotation;
  }

  /**
   * Retives the type of the annotation.
   * 
   * @return the type
   */
  public Type getAnnotationType() {
    return mAnnotation.getType();
  }
}