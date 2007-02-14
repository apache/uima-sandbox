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
 * read files, convert them and save them in the folder select project select
 * dst folder select src
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.1 $, $Date: 2007/01/04 14:37:51 $
 */
public final class ImportDocumentWizard extends Wizard implements IImportWizard
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
        return mMainPage.finish();
    }
}