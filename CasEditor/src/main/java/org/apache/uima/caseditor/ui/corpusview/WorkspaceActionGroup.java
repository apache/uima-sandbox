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

import java.util.Iterator;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.Images;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.CloseResourceAction;
import org.eclipse.ui.actions.OpenResourceAction;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * This group contains workspace actions.
 */
final class WorkspaceActionGroup extends ActionGroup
{
    /**
     * The action to actually open resources.
     */
    private OpenResourceAction mOpenProjectAction;
    
    /**
     * The retarget action to open a project.
     */
    private IAction mRetargetOpenProjectAction;
    
    /**
     * The action to actually close resources.
     */
    private CloseResourceAction mCloseProjectAction;
    
    /**
     * The retarget action to close.
     */
    private IAction mRetargetCloseAction;
    
    /**
     * Action to actually refresh resources.
     */
    private RefreshAction mRefreshAction;
    
    /**
     * The retarget action to refresh.
     */
    private IAction mRetargetRefreshAction;
    
    /**
     * Initializes a new instance.
     * 
     * @param shell
     * @param window
     */
    WorkspaceActionGroup(Shell shell, IWorkbenchWindow window)
    {
        // open
        mOpenProjectAction = new OpenResourceAction(shell);
        
        mRetargetOpenProjectAction = IDEActionFactory.OPEN_PROJECT
                .create(window);
        
        // close
        mCloseProjectAction = new CloseResourceAction(shell);
        
        mRetargetCloseAction = IDEActionFactory.CLOSE_PROJECT.create(window);
        
        // refresh
        mRefreshAction = new RefreshAction(shell);
        
        mRetargetRefreshAction = ActionFactory.REFRESH.create(window);
        
        mRetargetRefreshAction.setImageDescriptor(
                CasEditorPlugin.getTaeImageDescriptor(
                Images.EXPLORER_E_REFRESH));

        mRetargetRefreshAction.setDisabledImageDescriptor(
                CasEditorPlugin.getTaeImageDescriptor(
                Images.EXPLORER_D_REFRESH));
    }
    
    /**
     * Fills the context menu with the actions.
     */
    @Override
    public void fillContextMenu(IMenuManager menu)
    {
        IStructuredSelection selection = CorpusExplorerUtil
                .convertNLPElementsToResources((IStructuredSelection) getContext()
                        .getSelection());
        
        boolean hasOnlyProjectSelections = true;
        
        boolean hasOpenProjects = false;
        boolean hasClosedProjects = false;
        
        boolean isEverythingKnown = false;
        
        // iterate over resources until they are all consumed
        // or we know already which operations could be run
        Iterator resources = selection.iterator();
        
        while (resources.hasNext() && !isEverythingKnown)
        {
            isEverythingKnown = !hasOnlyProjectSelections && hasOpenProjects
                    && hasClosedProjects;
            
            IResource resource = (IResource) resources.next();
            
            boolean isProjectSelection = resource instanceof IProject;
            
            if (!isProjectSelection)
            {
                hasOnlyProjectSelections = false;
                continue;
            }
            
            IProject project = (IProject) resource;
            
            if (project.isOpen())
            {
                hasOpenProjects = true;
            }
            else
            {
                hasClosedProjects = true;
            }
        }
        
        // if nothing is closed add the refresh action
        if (!hasClosedProjects)
        {
            menu.add(mRetargetRefreshAction);
        }
        
        if (!hasOnlyProjectSelections)
        {
            // do not run project open/close actions if anyting
            // else than a project is selected
            
            return;
        }
        
        if (hasOpenProjects)
        {
            // open projects can be closed
            menu.add(mRetargetCloseAction);
        }
        
        if (hasClosedProjects)
        {
            menu.add(mRetargetOpenProjectAction);
        }
    }
    
    /**
     * Fills the action bars.
     */
    @Override
    public void fillActionBars(IActionBars actionBars)
    {
        actionBars.setGlobalActionHandler(
                IDEActionFactory.OPEN_PROJECT.getId(), mOpenProjectAction);
        
        actionBars.setGlobalActionHandler(IDEActionFactory.CLOSE_PROJECT
                .getId(), mCloseProjectAction);
        
        actionBars.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
                mRefreshAction);
        
        actionBars.updateActionBars();
    }
    
    /**
     * Updates the action.
     */
    @Override
    public void updateActionBars()
    {
        IStructuredSelection selection = (IStructuredSelection) getContext()
                .getSelection();
        
        mCloseProjectAction.selectionChanged(selection);
        mOpenProjectAction.selectionChanged(selection);
        mRefreshAction.selectionChanged(selection);
    }
}