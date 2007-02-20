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

package org.apache.uima.caseditor.ui.action;

import java.lang.reflect.InvocationTargetException;

import org.apache.uima.UIMAException;
import org.apache.uima.caseditor.CasEditorPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * TODO: add javadoc here
 */
public class RunnableAction extends Action
{
    private Shell mShell;
    private IRunnableWithProgress mRunnable;
    private String mName;
    
    /**
     * Initializes a new instance.
     * 
     * @param shell
     * @param name
     * @param runnable
     */
    public RunnableAction(Shell shell, String name, IRunnableWithProgress runnable)
    {
        Assert.isNotNull(shell);
        mShell = shell;
        
        Assert.isNotNull(name);
        mName = name;
        setText(name);
        
        Assert.isNotNull(runnable);
        mRunnable = runnable;
    }
    
    @Override
    public void run()
    {
        IProgressService progressService = PlatformUI.getWorkbench()
                .getProgressService();

        try
        {
            progressService.run(true, false, mRunnable);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e.getCause();
            
            Status status;
            // TODO: Workarround for UIMAException problems ...
            if (cause instanceof UIMAException)
            {
                UIMAException uimaException = (UIMAException) cause;
                
                Object[] argument = uimaException.getArguments();
                
                    status = new Status(IStatus.ERROR, CasEditorPlugin.ID, 0,
                            argument.length > 0 ? argument[0].toString() : 
                            "Unkown error, see log.", cause);
            }
            else
            {
                status = new Status(IStatus.ERROR, CasEditorPlugin.ID, 0,
                        cause.getMessage() != null ? cause.getMessage() : 
                            "Unkown error, see log.", cause);
            }
            
            ErrorDialog.openError(mShell, "Unexpected exception in " + 
                    mName, null, status);            
        }
        catch (InterruptedException e)
        {
            // task terminated ... just ignore
        }
    }
}