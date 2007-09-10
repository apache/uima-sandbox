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

package org.apache.uima.caseditor.ui.property;

import java.awt.Color;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.core.model.DotCorpusElement;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.model.TypesystemElement;
import org.apache.uima.caseditor.core.model.dotcorpus.AnnotationStyle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * This is the <code>AnnotationPropertyPage</code>. this page configures the
 * project dependent and type dependent annotation appearance in the
 * <code>AnnotationEditor</code>.
 */
public class AnnotationPropertyPage extends PropertyPage {
  private DotCorpusElement mDotCorpusElement;

  private Combo mStyleCombo;

  private ColorSelector mColorSelector;

  private List mTypeList;

  private AnnotationStyle mCurrentSelectedAnnotation = null;

  private NlpProject mProject;

  /**
   * Creates the annotation property page controls.
   */
  @Override
  protected Control createContents(Composite parent) {
    mProject = ((INlpElement) getElement()).getNlpProject();

    mDotCorpusElement = mProject.getDotCorpus();

    TypesystemElement typesystem = mProject.getTypesystemElement();

    if (typesystem == null) {
      Label message = new Label(parent, SWT.NONE);
      message.setText("Please set a valid typesystem file first.");

      return message;
    }


    Composite base = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    base.setLayout(layout);

    // type text
    Label typeText = new Label(base, SWT.NONE);
    typeText.setText("Annotation types:");

    GridData typeTextGridData = new GridData();
    typeTextGridData.horizontalSpan = 2;
    typeText.setLayoutData(typeTextGridData);

    // type list
    mTypeList = new List(base, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
    GridData typeListGridData = new GridData();
    typeListGridData.horizontalAlignment = SWT.FILL;
    typeListGridData.grabExcessVerticalSpace = true;
    typeListGridData.verticalAlignment = SWT.FILL;
    typeListGridData.verticalSpan = 2;
    mTypeList.setLayoutData(typeListGridData);

    TypeSystem typeSytstem = mProject.getTypesystemElement().getTypeSystem();

    Type annotationType = typeSytstem.getType(CAS.TYPE_NAME_ANNOTATION);

    java.util.List types = typeSytstem.getProperlySubsumedTypes(annotationType);
    // Vector types = typeSytstem.getDirectlySubsumedTypes(annotationType);

    for (Object typeObject : types) {
      Type type = (Type) typeObject;

      mTypeList.add(type.getName());
    }

    mTypeList.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        itemSelected();
      }

      public void widgetDefaultSelected(SelectionEvent e) {
        // not needed
      }

    });

    Composite settingsComposite = new Composite(base, SWT.NONE);

    GridLayout settingsLayout = new GridLayout();
    settingsLayout.numColumns = 2;
    settingsComposite.setLayout(settingsLayout);

    // text style combo
    Label styleText = new Label(settingsComposite, SWT.READ_ONLY);

    styleText.setText("Style:");

    // style combo
    mStyleCombo = new Combo(settingsComposite, SWT.READ_ONLY | SWT.DROP_DOWN);
    mStyleCombo.setEnabled(false);
    mStyleCombo.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        styleChanged();
      }

      public void widgetDefaultSelected(SelectionEvent e) {
        // not needed
      }

    });
    AnnotationStyle.Style possibleStyles[] = AnnotationStyle.Style.values();

    for (AnnotationStyle.Style style : possibleStyles) {
      mStyleCombo.add(style.name());
    }

    // text color label
    Label colorText = new Label(settingsComposite, SWT.NONE);
    colorText.setText("Color:");

    mColorSelector = new ColorSelector(settingsComposite);
    mColorSelector.setEnabled(false);
    mColorSelector.addListener(new IPropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        colorChanged();
      }
    });

    mTypeList.select(0);

    if (mTypeList.getSelectionIndex() != -1) {
      itemSelected();
    }

    return base;
  }

  private void colorChanged() {
    if (mCurrentSelectedAnnotation == null) {
      mCurrentSelectedAnnotation = getDefaultAnnotation();
    }

    RGB colorRGB = mColorSelector.getColorValue();

    Color color = new Color(colorRGB.red, colorRGB.green, colorRGB.blue);

    mCurrentSelectedAnnotation = new AnnotationStyle(mCurrentSelectedAnnotation.getAnnotation(),
            mCurrentSelectedAnnotation.getStyle(), color);

    mDotCorpusElement.setStyle(mCurrentSelectedAnnotation);
  }

  private void styleChanged() {
    if (mCurrentSelectedAnnotation == null) {
      mCurrentSelectedAnnotation = getDefaultAnnotation();
    }

    mCurrentSelectedAnnotation = new AnnotationStyle(mCurrentSelectedAnnotation.getAnnotation(),
            AnnotationStyle.Style.valueOf(mStyleCombo.getText()), mCurrentSelectedAnnotation
                    .getColor());

    mDotCorpusElement.setStyle(mCurrentSelectedAnnotation);
  }

  private void itemSelected() {
    String name = mTypeList.getItem(mTypeList.getSelectionIndex());

    TypeSystem typesystem = mProject.getTypesystemElement().getTypeSystem();

    AnnotationStyle style = mDotCorpusElement.getAnnotation(typesystem.getType(name));

    mCurrentSelectedAnnotation = style;

    if (style == null) {
      style = new AnnotationStyle(name, AnnotationStyle.DEFAULT_STYLE,
              AnnotationStyle.DEFAULT_COLOR);
    }

    mStyleCombo.setText(style.getStyle().name());
    mStyleCombo.setEnabled(true);

    Color color = style.getColor();
    mColorSelector.setColorValue(new RGB(color.getRed(), color.getGreen(), color.getBlue()));
    mColorSelector.setEnabled(true);
  }

  private AnnotationStyle getDefaultAnnotation() {
    return new AnnotationStyle(mTypeList.getItem(mTypeList.getSelectionIndex()),
            AnnotationStyle.DEFAULT_STYLE, AnnotationStyle.DEFAULT_COLOR);
  }

  /**
   * Executed after the OK button was pressed.
   */
  @Override
  public boolean performOk() {
    // workaround for type system not present problem
    if (mProject.getTypesystemElement() == null
            || mProject.getTypesystemElement().getTypeSystem() == null) {
      return true;
    }

    try {
      mDotCorpusElement.serialize();
    } catch (CoreException e) {
      CasEditorPlugin.log(e);
      return false;
    }

    return true;
  }
}