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

import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This is a lightweight popup dialog which creates an annotation of the
 * chosen type.
 */
class QuickTypeSelectionDialog extends PopupDialog {

  private final AnnotationEditor editor;

  private Text filterText;

  QuickTypeSelectionDialog(Shell parent, AnnotationEditor editor) {
    super(parent, PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE, true, true, false, true, null, null);

    this.editor = editor;
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);

    filterText = new Text(composite, SWT.NONE);
    filterText.setBackground(parent.getBackground());
    filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.LINE_DOT);
    separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final TreeViewer typeTree = new TreeViewer(composite, SWT.SINGLE | SWT.V_SCROLL);
    typeTree.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
            GridData.FILL_VERTICAL));

    filterText.addKeyListener(new KeyListener() {

      public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP) {
          typeTree.getControl().setFocus();
        }
      }

      public void keyReleased(KeyEvent e) {
        typeTree.refresh(false);
      }
    });

    typeTree.setContentProvider(new ITreeContentProvider() {

      public Object[] getChildren(Object parentElement) {
        return null;
      }

      public Object getParent(Object element) {
        return null;
      }

      public boolean hasChildren(Object element) {
        return false;
      }

      public Object[] getElements(Object inputElement) {
        TypeSystem typeSystem = (TypeSystem) inputElement;

        List types = typeSystem.getProperlySubsumedTypes(typeSystem
                .getType(CAS.TYPE_NAME_ANNOTATION));

        return types.toArray();
      }

      public void dispose() {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      }
    });

    typeTree.setFilters(new ViewerFilter[] { new ViewerFilter() {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element) {

        // check if the string from the filterText is contained in the type name
        Type type = (Type) element;

        return type.getName().contains(filterText.getText());
      }
    } });

    typeTree.getControl().addMouseMoveListener(new MouseMoveListener() {

      public void mouseMove(MouseEvent e) {

        Tree tree = (Tree) typeTree.getControl();

        TreeItem item = tree.getItem(new Point(e.x, e.y));

        if (item != null) {
          tree.setSelection(item);
        }
      }
    });

    typeTree.addOpenListener(new IOpenListener() {

      public void open(OpenEvent event) {

        StructuredSelection selection = (StructuredSelection) event.getSelection();

        Type annotationType = (Type) selection.getFirstElement();

        if (annotationType != null) {
          Point textSelection = editor.getSelection();

          AnnotationFS annotation = editor.getDocument().getCAS().createAnnotation(annotationType,
                  textSelection.x, textSelection.y);

          editor.getDocument().addFeatureStructure(annotation);

          if (annotation.getType().equals(editor.getAnnotationMode())) {
            editor.setAnnotationSelection(annotation);
          }
        }

        QuickTypeSelectionDialog.this.close();
      }
    });

    typeTree.setInput(editor.getDocument().getCAS().getTypeSystem());

    ISelection modeSelection = new StructuredSelection(new Object[] { editor.getAnnotationMode() });

    typeTree.setSelection(modeSelection, true);

    return composite;
  }

  @Override
  protected Point getInitialSize() {
    return new Point(250, 300);
  }
}