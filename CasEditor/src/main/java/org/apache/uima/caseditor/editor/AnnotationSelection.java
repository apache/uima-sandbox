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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.uima.AnnotationComparator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * The annotation collection contains only {@link AnnotationFS}s objects which are selected by a
 * {@link IStructuredSelection}.
 * 
 * Its also possible to retrive the frist and last annotation
 */
public class AnnotationSelection {

  private List<AnnotationFS> mAnnotations;
  
  /**
   * Initializes the current instance.
   * 
   * @param structures
   */
  public AnnotationSelection(Collection<FeatureStructure> structures) {
    mAnnotations = new ArrayList<AnnotationFS>(structures.size());
    
    for (FeatureStructure structure : structures) {
      add(structure);
    }
    
    Collections.sort(mAnnotations, new AnnotationComparator());
  }
  
  /**
   * Initializes a the current instance with all AnnotationFS object that are contained in the
   * {@link StructuredSelection}.
   * 
   * Note: {@link AnnotationFS} instances will be sorted in this selection, the natural odering of
   * the selection is destroyed
   * 
   * @param selection
   */
  public AnnotationSelection(IStructuredSelection selection) {

    mAnnotations = new ArrayList<AnnotationFS>(selection.size());

    for (Iterator<FeatureStructure> it = new FeatureStructureSelectionIterator(selection); 
        it.hasNext();) {
      add(it.next());
    }
    
    Collections.sort(mAnnotations, new AnnotationComparator());
  }

  private void add(FeatureStructure structure) {
    if (structure instanceof AnnotationFS) {
      mAnnotations.add((AnnotationFS) structure);
    }
  }
  
  /**
   * Indicates that the selection is empty.
   * 
   * @return true if emtpy
   */
  public boolean isEmpty() {
    return size() == 0;
  }
  
  /**
   * Retrives the size of the collection.
   * 
   * @return the size
   */
  public int size() {
    return mAnnotations.size();
  }
  
  /**
   * Retrives the first selected element.
   * 
   * Note: If {@link #size()} == 0 then frist and last element are the same instance.
   * 
   * @return the last element
   */
  public AnnotationFS getFirst() {
    return isEmpty() ? null : mAnnotations.get(0);
  }

  /**
   * Retrives the last selected element.
   * 
   * Note: If {@link #size()} == 0 then frist and last element are the same instance.
   * 
   * @return the last element
   */
  public AnnotationFS getLast() {
    return isEmpty() ? null : mAnnotations.get(size() - 1);
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
  
  /**
   * Retrives a human readable string.
   * @return humand readable string
   */
  @Override
  public String toString() {
    return mAnnotations.toString();
  }
}