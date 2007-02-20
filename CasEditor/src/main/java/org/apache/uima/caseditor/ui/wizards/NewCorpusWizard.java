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


import org.apache.uima.caseditor.core.model.NlpProject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFolderMainPage;

/**
 * TODO: add javadoc here
 */
public final class NewCorpusWizard extends Wizard implements INewWizard
{
    public static String ID = "org.apache.uima.caseditor.wizards.NewDocumentWizard";
    
    private WizardNewFolderMainPage mMainPage;
    
    private IStructuredSelection mCurrentResourceSelection;
    
    // private NewCorpusWizardPage mCorpusWizardPage;
    
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        mCurrentResourceSelection = selection;
    }
    
    @Override
    public void addPages()
    {
        mMainPage = new WizardNewFolderMainPage("New corpus wizard",
                mCurrentResourceSelection);
        
        addPage(mMainPage);
        
        // mCorpusWizardPage = new NewCorpusWizardPage();
        // mCorpusWizardPage.setDescription("description");
        
        // addPage(mCorpusWizardPage);
    }
    
    @Override
    public boolean performFinish()
    {
        IFolder newFolder = mMainPage.createNewFolder();
        
        IProject project = newFolder.getProject();
        
        NlpProject nlpProject;
        try
        {
            if (!project.hasNature("Annotator.NLPProject"))
            {
                return false;
            }
            
            nlpProject = (NlpProject) project.getNature("Annotator.NLPProject");
        }
        catch (CoreException e)
        {
            return false;
        }
        
        nlpProject.getDotCorpus().addCorpusFolder(
                newFolder.getFullPath().toString());
        
        return true;
    }
}