package org.apache.uima.caseditor.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard to create a new folder.
 */
final public class WizardNewFolderCreation extends Wizard implements INewWizard
{
    /**
     * The ID of the new nlp porject wizard.
     */
    public static final String ID = 
        "org.apache.uima.caseditor.ui.wizards.WizardNewFolderCreation";
    
//    private WizardNewFolderCreationPage mMainPage;

    private IStructuredSelection selection;
    
    /**
     * Initializes the <code>NLPProjectWizard</code>.
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        this.selection = selection;
        setWindowTitle("New folder");
    }
    
    /**
     * Adds the project wizard page to the wizard.
     */
    @Override
    public void addPages()
    {
//        mMainPage = new WizardNewFolderCreationPage("Folder", selection);
//        mMainPage.setTitle("Folder creation");
//        mMainPage.setDescription("Create a folder");
//        addPage(mMainPage);
    }
    
    /**
     * Creates the nlp project.
     */
    @Override
    public boolean performFinish()
    {
//        mMainPage.createNewFolder();
        return true;
    }
}
