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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.actions.OpenWithMenu;

/**
 * The OpenActionGroup contains the Open Action and the Open With Menu. This
 * class is resonsible to add the action and the menu to the context menu
 * depending on the current selection.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.1 $, $Date: 2007/01/04 14:37:51 $
 */
final class OpenActionGroup extends ActionGroup implements
        ICorpusExplorerActionGroup
{
    /**
     * The editor WorkbenchPage.
     */
    private IWorkbenchPage mPage;
    
    /**
     * The cached OpenFileAction instance.
     */
    private OpenFileAction mOpenFileAction;
    
    /**
     * Initializes a new instance.
     * 
     * @param page
     */
    OpenActionGroup(IWorkbenchPage page)
    {
        mPage = page;
        mOpenFileAction = new OpenFileAction(mPage);
    }
    
    /**
     * TODO: add javadoc comment
     */
    @Override
    public void fillContextMenu(IMenuManager menu)
    {
        IStructuredSelection selection = CorpusExplorerUtil
                .convertNLPElementsToResources((IStructuredSelection) getContext()
                        .getSelection());
        
        mOpenFileAction.selectionChanged(selection);
        menu.add(mOpenFileAction);
        
        fillOpenWithMenu(menu, selection);
    }
    
    private void fillOpenWithMenu(IMenuManager menu,
            IStructuredSelection selection)
    {
        if (selection.size() != 1)
        {
            return;
        }
        
        Object element = selection.getFirstElement();
        if (!(element instanceof IFile))
        {
            return;
        }
        
        MenuManager submenu = new MenuManager("Open With");
        submenu.add(new OpenWithMenu(mPage, (IFile) element));
        
        menu.add(submenu);
    }
    
    /**
     * Executes the default action.
     */
    public void executeDefaultAction(IStructuredSelection selection)
    {
        mOpenFileAction.selectionChanged(selection);
        mOpenFileAction.run();
    }
}