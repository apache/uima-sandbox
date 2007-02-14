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


import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.1 $, $Date: 2007/01/04 14:37:52 $
 */
class FileSelectionFieldEditor extends StringButtonFieldEditor
{
    private IProject mProject;
    
    FileSelectionFieldEditor(String name, String labelText, Composite parent,
            IProject project)
    {
        super(name, labelText, parent);
        
        mProject = project;
    }
    
    @Override
    protected String changePressed()
    {
        // String currentText = getTextControl().getText();
        
        // IFile candidateFile = mProject.getFile(currentText);
        
        // if (!candidateFile.exists())
        // {
        // return null;
        // }
        
        // TODO: preselect entered text entry
        
        final ElementTreeSelectionDialog fileSelectionDialog = new ElementTreeSelectionDialog(
                getShell(), new WorkbenchLabelProvider(),
                new BaseWorkbenchContentProvider());
        
        fileSelectionDialog.setInput(mProject);
        fileSelectionDialog.setTitle("testTitle");
        fileSelectionDialog.setMessage("testMessage");
        fileSelectionDialog.setValidator(new ISelectionStatusValidator()
        {
            public IStatus validate(Object[] selection)
            {
                if (selection.length == 1)
                {
                    if (selection[0] instanceof IFile)
                    {
                        return Status.OK_STATUS;
                    }
                }
                
                return new Status(IStatus.ERROR, TaeCorePlugin.ID, 0,
                        "Please select a file!", null);
            }
        });
        
        fileSelectionDialog.open();
        
        Object[] results = fileSelectionDialog.getResult();
        
        if (results.length != 1)
        {
            return null;
        }
        
        return ((IFile) results[0]).getFullPath().removeFirstSegments(1)
                .toString();
    }
}