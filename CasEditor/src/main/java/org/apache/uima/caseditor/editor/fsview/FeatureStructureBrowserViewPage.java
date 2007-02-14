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


import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.editor.AnnotationEditor;
import org.apache.uima.caseditor.editor.Images;
import org.apache.uima.caseditor.editor.TaeEditorPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.Page;

/**
 * TODO: add javadoc here TODO: update if annotation model was changed TODO: use
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.5.2.2 $, $Date: 2007/01/04 15:00:57 $
 */
public final class FeatureStructureBrowserViewPage extends Page {
  private class CreateAction extends Action {
    // TOOD: extract it and add setType(...)
    public void run() {
      // TODO: check if an AnnotationFS was created, if so
      // add it to the document

      mEditor.getDocument().fireDocumentChanged();

      // inserts a new feature strucutre of current type
      if (mCurrentType == null) {
        return;
      }

      FeatureStructure newFeatureStructure = mCAS.createFS(mCurrentType);

      mCAS.getIndexRepository().addFS(newFeatureStructure);

      mFSList.refresh();
    }
  }

  private class DeleteAction extends Action {
    @Override
    public void run() {
      // mEditor.getDocument().fireDocumentChanged();

      // get current selected feature structure
      /*
       * TreeItem[] selectedItems = mInstanceTree.getList().getSelection();
       * 
       * for (int i = 0; i < selectedItems.length; i++) { TreeItem item = selectedItems[i];
       * 
       * FeatureStructure structure = (FeatureStructure) ((IAdaptable) item
       * .getData()).getAdapter(FeatureStructure.class);
       * 
       * if (structure == null) { continue; }
       * 
       * mCAS.getIndexRepository().removeFS(structure); }
       * 
       * mInstanceTree.refresh();
       */
    }
  }

  private class SelectAllAction extends Action {
    @Override
    public void run() {
      mFSList.getList().selectAll();
    }
  }

  private AnnotationEditor mEditor;

  private CAS mCAS;

  private ListViewer mFSList;

  private Composite mInstanceComposite;

  private Type mCurrentType;

  private Action mDeleteAction;

  private Action mSelectAllAction;

  /**
   * Initializes a new instance.
   * 
   * @param editor
   */
  public FeatureStructureBrowserViewPage(AnnotationEditor editor) {
    mCAS = editor.getDocument().getCAS();
    mEditor = editor;

    mDeleteAction = new DeleteAction();

    mSelectAllAction = new SelectAllAction();

    // TODO:
    // add here a change listener, for add and remove operations
    // the view must maybe updated
  }

  @Override
  public void createControl(Composite parent) {
    mInstanceComposite = new Composite(parent, SWT.NONE);

    GridLayout layout = new GridLayout();

    layout.numColumns = 1;

    mInstanceComposite.setLayout(layout);

    TypeSelectionPane mTypePane = new TypeSelectionPane(mInstanceComposite, mCAS.getTypeSystem());

    GridData typePaneData = new GridData();
    typePaneData.grabExcessHorizontalSpace = true;
    typePaneData.grabExcessVerticalSpace = false;
    typePaneData.horizontalAlignment = SWT.FILL;
    mTypePane.setLayoutData(typePaneData);

    mFSList = new ListViewer(mInstanceComposite, SWT.MULTI);
    GridData instanceListData = new GridData();
    instanceListData.grabExcessHorizontalSpace = true;
    instanceListData.grabExcessVerticalSpace = true;
    instanceListData.horizontalAlignment = SWT.FILL;
    instanceListData.verticalAlignment = SWT.FILL;
    mFSList.getList().setLayoutData(instanceListData);
    mFSList
            .setContentProvider(new FeatureStructureTreeContentProvider(mEditor.getDocument(), mCAS));
    mFSList.setLabelProvider(new FeatureStructureLabelProvider());

    mFSList.setUseHashlookup(true);

    mTypePane.setListener(new ITypePaneListener() {
      public void typeChanged(Type newType) {
        mCurrentType = newType;

        mFSList.setInput(newType);
      }
    });

    getSite().setSelectionProvider(mFSList);
  }

  /**
   * Retrives the control
   * 
   * @return the control
   */
  @Override
  public Control getControl() {
    return mInstanceComposite;
  }

  /**
   * Adds the following actions to the toolbar: {@link CreateAction} {@link DereferenceAction}
   * {@link DeleteAction}
   * 
   * @param menuManager
   * @param toolBarManager
   * @param statusLineManager
   */
  @Override
  public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager,
          IStatusLineManager statusLineManager) {
    // create
    Action createAction = new CreateAction();
    createAction.setText("Create");
    createAction.setImageDescriptor(TaeEditorPlugin.getTaeImageDescriptor(Images.ADD));
    toolBarManager.add(createAction);

    // delete
    toolBarManager.add(ActionFactory.DELETE.create(getSite().getWorkbenchWindow()));
  }

  /**
   * Sets global action handlers for: delete select all
   * 
   * @param actionBars
   */
  public void setActionBars(IActionBars actionBars) {
    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), mDeleteAction);

    actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), mSelectAllAction);

    super.setActionBars(actionBars);
  }

  /**
   * Sets the focus.
   */
  @Override
  public void setFocus() {
    mInstanceComposite.setFocus();
  }
}