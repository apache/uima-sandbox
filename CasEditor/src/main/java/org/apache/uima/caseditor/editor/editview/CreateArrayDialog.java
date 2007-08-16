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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CreateArrayDialog extends IconAndMessageDialog {

  private int arraySize;

  protected CreateArrayDialog(Shell parentShell) {
    super(parentShell);

    message = "Please enter the size of the new array.";
  }

  @Override
  protected void configureShell(Shell newShell) {
    newShell.setText("Array Size");
  }

  @Override
  protected Control createDialogArea(Composite parent) {

   createMessageArea(parent);

    Composite labelAndText = (Composite) super.createDialogArea(parent);
    ((GridLayout) labelAndText.getLayout()).numColumns = 2;

    GridData labelAndTextData = new GridData(GridData.FILL_BOTH);
    labelAndTextData.horizontalSpan = 2;
    labelAndText.setLayoutData(labelAndTextData);

    GridLayout labelAndTextLayout = new GridLayout();
    labelAndTextLayout.numColumns = 2;

    labelAndText.setLayout(labelAndTextLayout);

    Label sizeLabel = new Label(labelAndText, SWT.NONE);
    GridData sizeLabelData = new GridData();
    sizeLabelData.horizontalAlignment = SWT.LEFT;
    sizeLabelData.grabExcessHorizontalSpace = false;
    sizeLabel.setText("Size:");

    final Text sizeText = new Text(labelAndText, SWT.BORDER);
    GridData sizeTextData = new GridData();
    sizeTextData.grabExcessHorizontalSpace = true;
    sizeTextData.horizontalAlignment = SWT.FILL;
    sizeText.setLayoutData(sizeTextData);

    sizeText.addModifyListener(new ModifyListener(){

      public void modifyText(ModifyEvent event) {
        try {
        arraySize = Integer.parseInt(sizeText.getText());
        }
        catch (NumberFormatException e) {
          arraySize = -1;
        }
      }});

    return labelAndText;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Create", true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize() {
    return new Point(500, 200);
  }

  int getArraySize() {
    return arraySize;
  }

  @Override
  protected Image getImage() {
    return getShell().getDisplay().getSystemImage(SWT.ICON_QUESTION);
  }
}