/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/

package org.apache.uima.cev.preferences;

import org.apache.uima.cev.CEVPlugin;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CEVViewerTreeViewPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

  public CEVViewerTreeViewPreferencePage() {
    super(GRID);
    setPreferenceStore(CEVPlugin.getDefault().getPreferenceStore());
  }

  @Override
  public void createFieldEditors() {
    RadioGroupFieldEditor editor = new RadioGroupFieldEditor(
            CEVPreferenceConstants.P_ANNOTATION_BROWSER_TREE_ORDER,
            "Default tree order for the Annotation Browser", 1,
            CEVPreferenceConstants.P_ANNOTATION_BROWSER_TREE_ORDER_VALUE, getFieldEditorParent());
    addField(editor);
    editor.setEnabled(false, getFieldEditorParent());

    addField(new RadioGroupFieldEditor(CEVPreferenceConstants.P_ANNOTATION_REPR,
            "Default annotation representation in Annotation Browser and Selection Browser", 1,
            CEVPreferenceConstants.P_ANNOTATION_REPR_VALUES, getFieldEditorParent()));
    addField(new BooleanFieldEditor(CEVPreferenceConstants.P_ANNOTATION_EDITOR_TRIM,
            "Trim selected text in annotation editor", getFieldEditorParent()));
    addField(new BooleanFieldEditor(CEVPreferenceConstants.P_AUTO_REFRESH, "Reload changed CAS",
            getFieldEditorParent()));
    addField(new BooleanFieldEditor(CEVPreferenceConstants.P_SELECT_ONLY,
            "Initiate the creation of annotations with the selection of text",
            getFieldEditorParent()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench) {
  }

}
