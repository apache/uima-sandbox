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

package org.apache.uima.caseditor.editor.fsview;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.core.uima.StrictTypeConstraint;
import org.apache.uima.caseditor.core.util.Primitives;
import org.apache.uima.caseditor.editor.AnnotationDocument;
import org.apache.uima.caseditor.editor.FeatureValue;
import org.apache.uima.caseditor.editor.ModelFeatureStructure;
import org.apache.uima.jcas.cas.StringArray;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TODO: add javadoc here
 */
final class FeatureStructureTreeContentProvider implements ITreeContentProvider {
  private AnnotationDocument mDocument;

  private CAS mCAS;

  private Type mCurrentType;

  FeatureStructureTreeContentProvider(AnnotationDocument document, CAS tcas) {
    mCAS = tcas;
    mDocument = document;
  }

  public Object[] getElements(Object inputElement) {
    if (mCurrentType == null) {
      return new Object[] {};
    }

    FSIndex index = mCAS.getIndexRepository().getIndex("TOPIndex");

    assert index != null : "Unable to retrive the TOPIndex!";

    StrictTypeConstraint typeConstrain = new StrictTypeConstraint(mCurrentType);

    FSIterator strictTypeIterator = mCAS.createFilteredIterator(index.iterator(), typeConstrain);

    LinkedList<ModelFeatureStructure> featureStrucutreList = new LinkedList<ModelFeatureStructure>();

    for (int i = 0; strictTypeIterator.hasNext(); i++) {
      featureStrucutreList.add(new ModelFeatureStructure(mDocument,
              (FeatureStructure) strictTypeIterator.next()));
    }

    ModelFeatureStructure[] featureStructureArray = new ModelFeatureStructure[featureStrucutreList
            .size()];

    featureStrucutreList.toArray(featureStructureArray);

    return featureStructureArray;
  }

  public void dispose() {
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    if (newInput == null) {
      // clear current input
      mCurrentType = null;
      return;
    }

    mCurrentType = (Type) newInput;
  }

  /**
   * Retrives children for a FeatureStrcuture and for FeatureValues if they have childs.
   * 
   * @param parentElement
   * @return the childs
   */
  public Object[] getChildren(Object parentElement) {
    Collection<Object> childs = new LinkedList<Object>();

    FeatureStructure featureStructure;

    if (parentElement instanceof ModelFeatureStructure) {
      featureStructure = ((ModelFeatureStructure) parentElement).getStructre();
    } else if (parentElement instanceof FeatureValue) {
      FeatureValue value = (FeatureValue) parentElement;

      if (parentElement instanceof StringArray) {
        StringArray array = (StringArray) parentElement;
        return array.toArray();
      }

      featureStructure = (FeatureStructure) value.getValue();
    } else {
      assert false : "Unexpected element!";

      return new Object[] {};
    }

    Type type = featureStructure.getType();

    Vector featureTypes = type.getAppropriateFeatures();

    Iterator featuresItertor = featureTypes.iterator();

    while (featuresItertor.hasNext()) {
      Feature feature = (Feature) featuresItertor.next();

      if (Primitives.isPrimitive(feature)) {
        // create a new pair
        // feature and value
        // add string
        childs.add(new FeatureValue(mDocument, featureStructure, feature));
      } else {
        childs.add(new FeatureValue(mDocument, featureStructure, feature));
      }
    }

    assert childs.size() > 0;

    return childs.toArray();
  }

  public Object getParent(Object element) {
    return null;
  }

  public boolean hasChildren(Object element) {
    if (element instanceof IAdaptable
            && ((IAdaptable) element).getAdapter(FeatureStructure.class) != null) {
      return true;
    } else if (element instanceof FeatureValue) {
      FeatureValue featureValue = (FeatureValue) element;

      if (Primitives.isPrimitive(featureValue.getFeature())) {
        Object value = featureValue.getValue();

        if (value == null) {
          return false;
        }

        if (value instanceof StringArray) {
          StringArray array = (StringArray) featureValue.getValue();

          if (array.size() > 0) {
            return true;
          } else {
            return false;
          }
        }

        return false;
      } else {
        return featureValue.getValue() != null ? true : false;
      }
    } else {
      assert false : "Unexpected element";

      return false;
    }
  }
}