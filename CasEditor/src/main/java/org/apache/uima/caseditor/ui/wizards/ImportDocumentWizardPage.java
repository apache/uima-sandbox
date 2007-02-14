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

package org.apache.uima.caseditor.ui.wizards;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;


/**
 * The main page of the ImportDocumentWizard.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.2.2.1 $, $Date: 2007/01/04 14:37:51 $
 */
final class ImportDocumentWizardPage extends
        WizardFileSystemResourceImportPage1
{
    
    @Override
    protected void createSourceGroup(Composite parent)
    {
        createRootDirectoryGroup(parent);
        createFileSelectionGroup(parent);
        createButtonsGroup(parent);
    }
    
    public ImportDocumentWizardPage(IWorkbench workbench,
            IStructuredSelection selection)
    {
        super(workbench, selection);
    }
    
    @Override
    protected boolean importResources(List fileSystemObjects)
    {
        IImportStructureProvider importProvider = new DocumentImportStructureProvider(
                getContainerFullPath());
        
        ImportOperation operation = new ImportOperation(getContainerFullPath(),
                getSourceDirectory(), importProvider, this, fileSystemObjects);
        
        operation.setContext(getShell());
        
        return executeImportOperation(operation);
    }
}