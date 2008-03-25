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

import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * The <code>TypeSelctionPane</code> notifies a listener about the selected type. Types are
 * retrieved format the type system.
 *
 * TODO: Should be a types set to default ?
 */
public final class TypeSelectionPane extends Composite {

  private ITypePaneListener mListener;

  private TypeSystem mTypeSystem;

  private Combo mTypeCombo;

  public TypeSelectionPane(Composite parent, Type superType, TypeSystem typeSystem) {
    this(parent, superType, typeSystem, new LinkedList<Type>());
  }

  public TypeSelectionPane(Composite parent, Type superType, TypeSystem typeSystem,
          Collection<Type> filterTypes) {
    super(parent, SWT.NONE);

    mTypeSystem = typeSystem;

    GridLayout layout = new GridLayout();
    layout.numColumns = 2;

    setLayout(layout);

    Label mTypeLabel = new Label(this, SWT.NONE);
    mTypeLabel.setText("Type: ");

    GridData typeLabelData = new GridData();
    typeLabelData.horizontalAlignment = SWT.LEFT;
    mTypeLabel.setLayoutData(typeLabelData);

    // insert list box

    mTypeCombo = new Combo(this, SWT.READ_ONLY);
    mTypeCombo.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        Type newType = mTypeSystem.getType(mTypeCombo.getText());

        if (mListener != null && newType != null) {
          mListener.typeChanged(newType);
        }
      }
    });

    GridData typeComboData = new GridData();
    typeComboData.grabExcessHorizontalSpace = true;
    typeComboData.horizontalAlignment = SWT.FILL;
    mTypeCombo.setLayoutData(typeComboData);

    LinkedList<String> typeNameList = new LinkedList<String>();

    typeNameList.add(superType.getName());

    // get a collection of all types
    Iterator typeIterator = mTypeSystem.getProperlySubsumedTypes(superType).iterator();

    while (typeIterator.hasNext()) {
      Type type = (Type) typeIterator.next();

      if (!filterTypes.contains(type)) {
        typeNameList.add(type.getName());
      }
    }

    mTypeCombo.setItems(typeNameList.toArray(new String[typeNameList.size()]));

    // select the super type, its the first element (and must be there)
    mTypeCombo.select(0);
  }

  public void setListener(ITypePaneListener listener) {
    mListener = listener;
  }

  public Type getType() {
    return mTypeSystem.getType(mTypeCombo.getText());
  }
}