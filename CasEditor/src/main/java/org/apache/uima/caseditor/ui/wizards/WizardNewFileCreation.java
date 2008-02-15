package org.apache.uima.caseditor.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard to create a new file.
 */
final public class WizardNewFileCreation extends Wizard implements INewWizard
{
    /**
     * The ID of the new nlp project wizard.
     */
    public static final String ID = 
        "org.apache.uima.caseditor.ui.wizards.WizardNewFileCreation";
    
    // private WizardNewFileCreationPage mMainPage;

    private IStructuredSelection selection;
    
    /**
     * Initializes the <code>NLPProjectWizard</code>.
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        this.selection = selection;
        setWindowTitle("New file");
    }
    
    /**
     * Adds the project wizard page to the wizard.
     */
    @Override
    public void addPages()
    {
//        mMainPage = new WizardNewFileCreationPage("File", selection);
//        mMainPage.setTitle("File creation");
//        mMainPage.setDescription("Create a file");
//        addPage(mMainPage);
    }
    
    /**
     * Creates the nlp project.
     */
    @Override
    public boolean performFinish()
    {
        // mMainPage.createNewFile();
        return true;
    }
}
