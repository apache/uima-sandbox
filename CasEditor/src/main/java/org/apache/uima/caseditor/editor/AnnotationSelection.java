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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.uima.AnnotationComparator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * The annotation collection contains only {@link AnnotationFS}s objects which are selected by a
 * {@link IStructuredSelection}.
 * 
 * Its also possible to retrive the frist and last annotation
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.2 $, $Date: 2007/01/04 15:00:54 $
 */
public class AnnotationSelection {
  private List<AnnotationFS> mAnnotations;

  /**
   * Initializes a the current instance with all AnnotationFS obejct that are contained in the
   * {@link StructuredSelection}.
   * 
   * Note: {@link AnnotationFS} instances will be sorted in this selection, the natural odering of
   * the selection is destroyed
   * 
   * @param selection
   */
  public AnnotationSelection(IStructuredSelection selection) {
    mAnnotations = new ArrayList<AnnotationFS>(selection.size());

    for (Object item : selection.toList()) {
      AnnotationFS annotation = null;

      if (item instanceof IAdaptable) {
        annotation = (AnnotationFS) ((IAdaptable) item).getAdapter(AnnotationFS.class);
      }

      if (annotation != null) {
        mAnnotations.add(annotation);
      }
    }

    Collections.sort(mAnnotations, new AnnotationComparator());
  }

  /**
   * Retrives the size of the selection.
   * 
   * @return the size
   */
  public int size() {
    return mAnnotations.size();
  }

  /**
   * Indicates that the selection contains no elements.
   * 
   * @return true if empty
   */
  public boolean isEmtpy() {
    return size() == 0;
  }

  /**
   * Retrives the first selected element.
   * 
   * Note: If {@link #size()} == 0 then frist and last element are the same instance.
   * 
   * @return the last element
   */
  public AnnotationFS getFirst() {
    return mAnnotations.isEmpty() ? null : mAnnotations.get(0);
  }

  /**
   * Retrives the last selected element.
   * 
   * Note: If {@link #size()} == 0 then frist and last element are the same instance.
   * 
   * @return the last element
   */
  public AnnotationFS getLast() {
    return mAnnotations.isEmpty() ? null : mAnnotations.get(size() - 1);
  }

  /**
   * Retrives an ordered list of {@link AnnotationFS} objects.
   * 
   * @see AnnotationComparator is used for ordering the annotations
   * 
   * @return all selected {@link AnnotationFS} objects
   */
  public List<AnnotationFS> toList() {
    return Collections.unmodifiableList(mAnnotations);
  }
}