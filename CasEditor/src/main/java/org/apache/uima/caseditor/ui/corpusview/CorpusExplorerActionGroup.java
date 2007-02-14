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

package org.apache.uima.caseditor.ui.corpusview;


import org.apache.uima.caseditor.core.model.INlpElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.ExportResourcesAction;
import org.eclipse.ui.actions.ImportResourcesAction;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * Main corpus explorer action group.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.1 $, $Date: 2007/01/04 14:37:51 $
 */
final class CorpusExplorerActionGroup extends ActionGroup implements
        ICorpusExplorerActionGroup
{
    private OpenActionGroup mOpenActionGroup;
    
    private RefactorGroup mRefactorGroup;
    
    protected ImportResourcesAction mImportAction;
    
    protected ExportResourcesAction mExportAction;
    
    private WorkspaceActionGroup mWorkspaceGroup;
    
    private AnnotatorActionGroup mAnnotatorActionGroup;
    
    private ConsumerCorpusActionGroup mConsumerCorpusActionGroup;
    
    private PropertyDialogAction mPropertyAction;
    
    private IWorkbenchWindow mWindow;
    
    private IAction mRetargetPropertiesAction;
    
    /**
     * Creates a <code>CorpusExplorerActionGroup</code> object.
     * 
     * @param view -
     *            the coresponding <code>CorpusExplorerView</code>
     */
    CorpusExplorerActionGroup(CorpusExplorerView view)
    {
        mWindow = view.getSite().getPage().getWorkbenchWindow();
        
        Shell shell = view.getSite().getShell();
        
        mOpenActionGroup = new OpenActionGroup(view.getSite().getPage());
        
        mRefactorGroup = new RefactorGroup(shell, mWindow);
        
        mImportAction = new ImportResourcesAction(mWindow);
        
        mExportAction = new ExportResourcesAction(mWindow);
        
        mWorkspaceGroup = new WorkspaceActionGroup(shell, mWindow);
        
        mAnnotatorActionGroup = new AnnotatorActionGroup(shell);
        
        mConsumerCorpusActionGroup = new ConsumerCorpusActionGroup(shell);
        
        mPropertyAction = new PropertyDialogAction(
                new SameShellProvider(shell), view.getTreeViewer());
        
        mRetargetPropertiesAction = ActionFactory.PROPERTIES.create(mWindow);
    }
    
    /**
     * Fills the context menu with all the actions.
     */
    @Override
    public void fillContextMenu(IMenuManager menu)
    {
        IStructuredSelection selection = (IStructuredSelection) getContext()
                .getSelection();
        
        // For action order see "Eclipse User Interface Guidelines"
        
        // 1. New actions
        menu.add(IDEActionFactory.NEW_WIZARD_DROP_DOWN.create(mWindow));
        menu.add(new Separator());
        
        // 2. Open actions
        mOpenActionGroup.fillContextMenu(menu);
        menu.add(new Separator());
        
        // 3. Navigate + Show In
        
        // 4.1 Cut, Copy, Paste, Delete, Rename and other refactoring commands
        mRefactorGroup.fillContextMenu(menu);
        menu.add(new Separator());
        
        // 4.2
        menu.add(ActionFactory.IMPORT.create(mWindow));
        
        menu.add(ActionFactory.EXPORT.create(mWindow));
        
        menu.add(new Separator());
        
        // 5. Other Plugin Additons
        mWorkspaceGroup.fillContextMenu(menu);
        menu.add(new Separator());
        
        // 5.2 annotator additions
        MenuManager taggerMenu = new MenuManager("Annotator");
        menu.add(taggerMenu);
        
        mAnnotatorActionGroup.fillContextMenu(taggerMenu);
        
        // 5.3 consumer additions
        MenuManager trainerMenu = new MenuManager("Consumer");
        menu.add(trainerMenu);
        
        mConsumerCorpusActionGroup.fillContextMenu(trainerMenu);
        
        // 5.4 Annotator Plugin Additions
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        // 6. Properties action
        boolean isOnlyOneResourceSelected = selection.size() == 1;
        if (isOnlyOneResourceSelected)
        {
            menu.add(mRetargetPropertiesAction);
        }
    }
    
    /**
     * Fills the action bars
     */
    @Override
    public void fillActionBars(IActionBars actionBars)
    {
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(),
                mPropertyAction);
        
        actionBars.updateActionBars();
        
        mOpenActionGroup.fillActionBars(actionBars);
        mRefactorGroup.fillActionBars(actionBars);
        mWorkspaceGroup.fillActionBars(actionBars);
    }
    
    /**
     * Updates the actions.
     */
    @Override
    public void updateActionBars()
    {
        IStructuredSelection selection = (IStructuredSelection) getContext()
                .getSelection();
        
        mPropertyAction.setEnabled(selection.size() == 1);
        
        mOpenActionGroup.updateActionBars();
        mRefactorGroup.updateActionBars();
        mWorkspaceGroup.updateActionBars();
    }
    
    /**
     * Sets the context to the action groups.
     */
    @Override
    public void setContext(ActionContext context)
    {
        super.setContext(context);
        
        mOpenActionGroup.setContext(context);
        mRefactorGroup.setContext(context);
        mWorkspaceGroup.setContext(context);
        mAnnotatorActionGroup.setContext(context);
        mConsumerCorpusActionGroup.setContext(context);
    }
    
    /**
     * Executes the default action, in this case the open action.
     */
    public void executeDefaultAction(IStructuredSelection selection)
    {
        if (selection.getFirstElement() instanceof INlpElement)
        {
            INlpElement nlpElement = (INlpElement) selection.getFirstElement();
            
            mOpenActionGroup.executeDefaultAction(new StructuredSelection(
                    nlpElement.getResource()));
        }
        else
        {
            mOpenActionGroup.executeDefaultAction(selection);
        }
    }
    
    /**
     * Dispose all resources created by the current object.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        
        mOpenActionGroup.dispose();
        mRefactorGroup.dispose();
        mImportAction.dispose();
        mExportAction.dispose();
        mWorkspaceGroup.dispose();
        mAnnotatorActionGroup.dispose();
        mConsumerCorpusActionGroup.dispose();
        mPropertyAction.dispose();
    }
}