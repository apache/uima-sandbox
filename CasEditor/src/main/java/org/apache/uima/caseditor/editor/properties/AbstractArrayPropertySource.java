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

package org.apache.uima.caseditor.editor.properties;


import org.apache.uima.cas.CommonArrayFS;
import org.apache.uima.caseditor.editor.ModelFeatureStructure;
import org.apache.uima.caseditor.editor.properties.validator.CellEditorValidatorFacotory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

abstract class AbstractArrayPropertySource implements IPropertySource {
  private ModelFeatureStructure mStructure;

  protected CommonArrayFS mArray;

  public AbstractArrayPropertySource(ModelFeatureStructure structure) {
    mStructure = structure;
    mArray = (CommonArrayFS) structure.getStructre();
  }

  /**
   * Always returns null. There are no editable value.
   */
  public Object getEditableValue() {
    return null;
  }

  protected abstract Class getPrimitiveType();

  /**
   * Returns for every array entry one {@link TextPropertyDescriptor}.
   */
  public IPropertyDescriptor[] getPropertyDescriptors() {
    IPropertyDescriptor descriptors[] = new IPropertyDescriptor[mArray.size()];

    for (int i = 0; i < mArray.size(); i++) {
      TextPropertyDescriptor primitiveProperty = new TextPropertyDescriptor(i, Integer.toString(i));

      primitiveProperty.setValidator(CellEditorValidatorFacotory
              .createValidator(getPrimitiveType()));

      descriptors[i] = primitiveProperty;
    }

    return descriptors;
  }

  protected abstract String get(int i);

  public Object getPropertyValue(Object id) {
    return get((Integer) id);
  }

  public boolean isPropertySet(Object id) {
    return true;
  }

  public void resetPropertyValue(Object id) {
  }

  protected abstract void set(int i, String value);

  public void setPropertyValue(Object id, Object value) {
    set((Integer) id, (String) value);

    mStructure.update();
  }
}