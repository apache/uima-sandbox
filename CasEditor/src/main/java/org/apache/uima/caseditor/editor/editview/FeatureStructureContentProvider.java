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

package org.apache.uima.caseditor.editor.editview;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.cas.CommonArrayFS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.core.AbstractDocumentListener;
import org.apache.uima.caseditor.core.TaeError;
import org.apache.uima.caseditor.core.util.Primitives;
import org.apache.uima.caseditor.editor.AnnotationDocument;
import org.apache.uima.caseditor.editor.ArrayValue;
import org.apache.uima.caseditor.editor.FeatureValue;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

final class FeatureStructureContentProvider extends AbstractDocumentListener
        implements ITreeContentProvider {

  private AnnotationDocument mDocument;

  private Viewer viewer;

  FeatureStructureContentProvider(AnnotationDocument document) {
    mDocument = document;
  }

  public Object[] getElements(Object inputElement) {

    if (inputElement != null) {
      FeatureStructure featureStructure = (FeatureStructure) inputElement;

      Type type = featureStructure.getType();

      List featureTypes = type.getFeatures();

      Collection<FeatureValue> featureValues = new LinkedList<FeatureValue>();

      Iterator featuresItertor = featureTypes.iterator();

      while (featuresItertor.hasNext()) {
        Feature feature = (Feature) featuresItertor.next();

        featureValues.add(new FeatureValue(mDocument, featureStructure, feature));
      }

      return featureValues.toArray();
    } else {
      return new Object[0];
    }
  }

  public void dispose() {
  }

  public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {

    this.viewer = viewer;

    if (oldInput != null) {
      mDocument.removeChangeListener(this);
    }

    if (newInput == null) {
      // this means empty input
      return;
    }

    mDocument.addChangeListener(this);

    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        viewer.refresh();
      }
    });
  }

  public void added(Collection<FeatureStructure> newFeatureStructure) {
  }

  public void changed() {
    // TODO: check if fs still exists

    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        viewer.setSelection(viewer.getSelection());
        viewer.refresh();
      }
    });
  }

  public void removed(Collection<FeatureStructure> deletedFeatureStructure) {
    // TODO: set viewer to null if current fs was deleted
  }

  public void updated(Collection<FeatureStructure> featureStructure) {
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        viewer.setSelection(viewer.getSelection());
        viewer.refresh();
      }
    });
  }

  public Object[] getChildren(Object parentElement) {

    FeatureValue value = (FeatureValue) parentElement;

    if (!value.getFeature().getRange().isArray()) {
      FeatureStructure childStructure = (FeatureStructure) value.getValue();
      return getElements(childStructure);
    }
    else {
      FeatureStructure arrayFS = value.getFeatureStructure().getFeatureValue(value.getFeature());

      CommonArrayFS array = (CommonArrayFS) arrayFS;

      // TODO: is is a bug in eclipse ??
      if (arrayFS == null) {
        return new Object[0];
      }

      ArrayValue arrayValues[] = new ArrayValue[array.size()];

      for (int i = 0; i < array.size(); i++) {
        arrayValues[i] = new ArrayValue(arrayFS, i);
      }

      return arrayValues;
    }
  }

  public Object getParent(Object element) {
    return null;
  }

  public boolean hasChildren(Object element) {

    if (element instanceof FeatureValue) {
      FeatureValue value = (FeatureValue) element;

      if (Primitives.isPrimitive(value.getFeature())) {
        return false;
      }
      else {
        return value.getValue() != null;
      }
    } else if (element instanceof ArrayValue) {
      return false;
    }
    else {
      throw new TaeError("Unkown element type");
    }
  }
}