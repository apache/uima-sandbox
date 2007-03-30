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

package org.apache.uima.caseditor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  private Map<ActionFactory, IWorkbenchAction> actions = new HashMap<ActionFactory, IWorkbenchAction>();

  private IWorkbenchWindow window;

//  private OpenWorkspaceAction openWorkspaceAction;

  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
  }

  protected void makeActions(IWorkbenchWindow window) {

    this.window = window;
    ActionFactory actionsToCreate[] = new ActionFactory[] { ActionFactory.NEW_WIZARD_DROP_DOWN,
        ActionFactory.NEW, 
        ActionFactory.IMPORT, 
        ActionFactory.EXPORT, 
        ActionFactory.SAVE,
        ActionFactory.SAVE_ALL, 
        ActionFactory.CLOSE, 
        ActionFactory.CLOSE_ALL, 
        ActionFactory.CUT,
        ActionFactory.COPY, 
        ActionFactory.PASTE, 
        ActionFactory.SELECT_ALL, 
        ActionFactory.DELETE,
        ActionFactory.QUIT, 
        ActionFactory.ABOUT, 
        ActionFactory.REVERT, 
        ActionFactory.PROPERTIES,
        ActionFactory.FORWARD_HISTORY, 
        ActionFactory.BACKWARD_HISTORY, 
        ActionFactory.LOCK_TOOL_BAR,
        ActionFactory.RESET_PERSPECTIVE, 
        ActionFactory.PREFERENCES, 
        ActionFactory.OPEN_NEW_WINDOW,
        ActionFactory.NEW_EDITOR, 
//        ActionFactory.EDIT_ACTION_SETS,
        ActionFactory.MAXIMIZE,
        ActionFactory.MINIMIZE,
        ActionFactory.ACTIVATE_EDITOR,
        ActionFactory.NEXT_EDITOR,
        ActionFactory.PREVIOUS_EDITOR,
        ActionFactory.SHOW_OPEN_EDITORS,
        ActionFactory.SHOW_WORKBOOK_EDITORS,
        ActionFactory.NEXT_PART,
        ActionFactory.PREVIOUS_PART,
        ActionFactory.SHOW_PART_PANE_MENU,
        ActionFactory.SHOW_VIEW_MENU
    };

    for (ActionFactory factory : actionsToCreate) {
      IWorkbenchAction action = factory.create(window);
      actions.put(factory, action);
      register(action);
    }
    
//    openWorkspaceAction = new OpenWorkspaceAction(window);
  }

  protected void fillMenuBar(IMenuManager menuBar) {

    // File menu
    MenuManager fileMenu = new MenuManager("File", IWorkbenchActionConstants.M_FILE);
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
    fileMenu.add(actions.get(ActionFactory.NEW));
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
    fileMenu.add(new Separator());
    fileMenu.add(actions.get(ActionFactory.CLOSE));
    fileMenu.add(actions.get(ActionFactory.CLOSE_ALL));
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
    fileMenu.add(new Separator());
    fileMenu.add(actions.get(ActionFactory.SAVE));
    fileMenu.add(actions.get(ActionFactory.SAVE_ALL));
    fileMenu.add(actions.get(ActionFactory.REVERT));
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
    fileMenu.add(new Separator());
//    fileMenu.add(openWorkspaceAction);
    fileMenu.add(new Separator());
    fileMenu.add(actions.get(ActionFactory.IMPORT));
    fileMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    fileMenu.add(actions.get(ActionFactory.PROPERTIES));
    fileMenu.add(ContributionItemFactory.REOPEN_EDITORS.create(window));
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MRU));
    fileMenu.add(new Separator());
    fileMenu.add(actions.get(ActionFactory.QUIT));
    fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
    menuBar.add(fileMenu);

    // Edit menu
    MenuManager editMenu = new MenuManager("Edit");
    editMenu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
    editMenu.add(actions.get(ActionFactory.COPY));
    editMenu.add(actions.get(ActionFactory.PASTE));
    editMenu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
    editMenu.add(new Separator());
    editMenu.add(actions.get(ActionFactory.DELETE));
    editMenu.add(actions.get(ActionFactory.SELECT_ALL));
    editMenu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
    menuBar.add(editMenu);

    // Window menu
    MenuManager windowMenu = new MenuManager("Window");
    windowMenu.add(actions.get(ActionFactory.OPEN_NEW_WINDOW));
    windowMenu.add(actions.get(ActionFactory.NEW_EDITOR));
    windowMenu.add(new Separator());
//    windowMenu.add(actions.get(ActionFactory.EDIT_ACTION_SETS));
    windowMenu.add(actions.get(ActionFactory.RESET_PERSPECTIVE));
    windowMenu.add(new Separator());

    MenuManager subMenu = new MenuManager("Navigate");
    subMenu.add(actions.get(ActionFactory.SHOW_PART_PANE_MENU));
    subMenu.add(actions.get(ActionFactory.SHOW_VIEW_MENU));
    subMenu.add(new Separator());
    subMenu.add(actions.get(ActionFactory.MAXIMIZE));
    subMenu.add(actions.get(ActionFactory.MINIMIZE));
    subMenu.add(new Separator());
    subMenu.add(actions.get(ActionFactory.ACTIVATE_EDITOR));
    subMenu.add(actions.get(ActionFactory.NEXT_EDITOR));
    subMenu.add(actions.get(ActionFactory.PREVIOUS_EDITOR));
    subMenu.add(actions.get(ActionFactory.SHOW_OPEN_EDITORS));
    subMenu.add(actions.get(ActionFactory.SHOW_WORKBOOK_EDITORS));
    subMenu.add(new Separator());
    subMenu.add(actions.get(ActionFactory.NEXT_PART));
    subMenu.add(actions.get(ActionFactory.PREVIOUS_PART));
    windowMenu.add(subMenu);
    
    windowMenu.add(new Separator());
    
    windowMenu.add(actions.get(ActionFactory.PREFERENCES));
    windowMenu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));
    menuBar.add(windowMenu);

    // Help menu
    MenuManager helpMenu = new MenuManager("Help", IWorkbenchActionConstants.M_FILE);
    editMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
    helpMenu.add(actions.get(ActionFactory.ABOUT));
    editMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
    menuBar.add(helpMenu);
  }

  @Override
  protected void fillCoolBar(ICoolBarManager coolBar) {

    IMenuManager popUpMenu = new MenuManager();
    popUpMenu.add(new ActionContributionItem(actions.get(ActionFactory.LOCK_TOOL_BAR)));
//    popUpMenu.add(new ActionContributionItem(actions.get(ActionFactory.EDIT_ACTION_SETS)));
    coolBar.setContextMenuManager(popUpMenu);

    coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_FILE));
    ToolBarManager fileToolBar = new ToolBarManager();
    fileToolBar.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
    fileToolBar.add(actions.get(ActionFactory.NEW_WIZARD_DROP_DOWN));
    fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_GROUP));
    fileToolBar.add(actions.get(ActionFactory.SAVE));
    coolBar.add(fileToolBar);

    coolBar.add(new GroupMarker(IWorkbenchActionConstants.HISTORY_GROUP));
    ToolBarManager navigateToolBar = new ToolBarManager();
    navigateToolBar.add(new Separator(IWorkbenchActionConstants.HISTORY_GROUP));
    navigateToolBar.add(actions.get(ActionFactory.BACKWARD_HISTORY));
    navigateToolBar.add(actions.get(ActionFactory.FORWARD_HISTORY));
    coolBar.add(navigateToolBar);
  }
}
