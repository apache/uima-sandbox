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

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.uima.cas.CAS;
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
 * The <code>TypeSelctionPane</code> notfies a listener about the selected type. Types are
 * retrived formt he type system.
 * 
 * TODO: Should be a types set to default ?
 */
final class TypeSelectionPane extends Composite {
  private static String[] filterTypes = new String[] { CAS.TYPE_NAME_ARRAY_BASE,
      CAS.TYPE_NAME_BOOLEAN_ARRAY, CAS.TYPE_NAME_BYTE_ARRAY, CAS.TYPE_NAME_SHORT_ARRAY,
      CAS.TYPE_NAME_LONG_ARRAY, CAS.TYPE_NAME_FLOAT_ARRAY, CAS.TYPE_NAME_DOUBLE_ARRAY,
      CAS.TYPE_NAME_BYTE, CAS.TYPE_NAME_ANNOTATION_BASE, CAS.TYPE_NAME_SHORT, CAS.TYPE_NAME_LONG,
      CAS.TYPE_NAME_FLOAT, CAS.TYPE_NAME_DOUBLE, CAS.TYPE_NAME_BOOLEAN,
      CAS.TYPE_NAME_EMPTY_FLOAT_LIST, CAS.TYPE_NAME_EMPTY_FS_LIST,
      CAS.TYPE_NAME_EMPTY_INTEGER_LIST, CAS.TYPE_NAME_EMPTY_STRING_LIST, CAS.TYPE_NAME_FLOAT,
      CAS.TYPE_NAME_FLOAT_ARRAY, CAS.TYPE_NAME_FLOAT_LIST, CAS.TYPE_NAME_FS_ARRAY,
      CAS.TYPE_NAME_FS_LIST, CAS.TYPE_NAME_INTEGER, CAS.TYPE_NAME_INTEGER_ARRAY,
      CAS.TYPE_NAME_INTEGER_LIST, CAS.TYPE_NAME_LIST_BASE, CAS.TYPE_NAME_NON_EMPTY_FLOAT_LIST,
      CAS.TYPE_NAME_NON_EMPTY_FS_LIST, CAS.TYPE_NAME_NON_EMPTY_INTEGER_LIST,
      CAS.TYPE_NAME_NON_EMPTY_STRING_LIST, CAS.TYPE_NAME_SOFA, CAS.TYPE_NAME_STRING,
      CAS.TYPE_NAME_STRING_ARRAY, CAS.TYPE_NAME_STRING_LIST, CAS.TYPE_NAME_TOP };

  private ITypePaneListener mListener;

  private TypeSystem mTypeSystem;

  private Combo mTypeCombo;

  TypeSelectionPane(Composite parent, TypeSystem typeSystem) {
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

    mTypeCombo = new Combo(this, SWT.NONE);
    mTypeCombo.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        Type newType = mTypeSystem.getType(mTypeCombo.getText());

        if (newType != null) {
          mListener.typeChanged(newType);
        }
      }
    });

    GridData typeComboData = new GridData();
    typeComboData.grabExcessHorizontalSpace = true;
    typeComboData.horizontalAlignment = SWT.FILL;
    mTypeCombo.setLayoutData(typeComboData);

    LinkedList<String> typeNameList = new LinkedList<String>();

    // get a collection of all types
    Iterator typeIterator = mTypeSystem.getTypeIterator();

    while (typeIterator.hasNext()) {
      Type type = (Type) typeIterator.next();

      boolean isFilteredType = false;

      // TODO: use a collection here and call contains ...
      for (String filteredType : filterTypes) {
        if (type.getName().equals(filteredType)) {
          isFilteredType = true;
        }
      }

      if (!isFilteredType) {
        typeNameList.add(type.getName());
      }
    }

    mTypeCombo.setItems(typeNameList.toArray(new String[typeNameList.size()]));
  }

  void setListener(ITypePaneListener listener) {
    mListener = listener;
  }
}