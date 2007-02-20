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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * The {@link ImportDocumentWizard} can import text files in different formats
 * into a corpus folder. The imported files are automatically converted to the 
 * xcas file format.
 * 
 * Notes:
 * If two files have the same name but a differnt file extension. The
 * second file gets a number attached.
 */
public class ImportDocumentWizard2 extends Wizard implements IImportWizard
{
    private ImportDocumentWizardPage mMainPage;
    
    private IStructuredSelection mCurrentResourceSelection;
    
    private IWorkbench mWorkbench = null;

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        mWorkbench = workbench;
        mCurrentResourceSelection = selection;
    }
    
    @Override
    public void addPages()
    {
        mMainPage = new ImportDocumentWizardPage(mWorkbench,
                mCurrentResourceSelection);
        
        addPage(mMainPage);
    }
    
    @Override
    public boolean performFinish()
    {
        return false;
    }
}
