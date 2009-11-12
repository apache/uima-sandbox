package org.apache.uima.tools.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.uima.casviewer.core.internal.TypeSystemUtilModified;
import org.apache.uima.casviewer.ui.internal.model.ICASViewer;
import org.apache.uima.casviewer.ui.internal.model.ICASViewerListener;
import org.apache.uima.casviewer.ui.internal.style.ColoredTypeTreeSectionPart;
import org.apache.uima.casviewer.ui.internal.style.DefaultColorTreeSectionPart;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.Form2Panel;
import org.apache.uima.casviewer.viewer.internal.GenericCasViewer;
import org.apache.uima.casviewer.viewer.internal.Messages;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.uct.viewer.internal.page.CASViewControl;
import org.apache.uima.uct.viewer.internal.page.HeaderPage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.xml.sax.SAXException;

/*

    **
     * Attribute name for the last path used to open a file/directory chooser
     * dialog.
     *
    protected static final String LAST_PATH_SETTING = "LAST_PATH_SETTING"; //$NON-NLS-1$
        IDialogSettings dialogSettings= JDIDebugUIPlugin.getDefault().getDialogSettings();
        String lastUsedPath= dialogSettings.get(LAST_PATH_SETTING);
        if (lastUsedPath == null) {
            lastUsedPath= ""; //$NON-NLS-1$
        }

    public ISourceContainer[] addSourceContainers(Shell shell, ISourceLookupDirector director) {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        String rootDir = DebugUIPlugin.getDefault().getDialogSettings().get(ROOT_DIR);
        dialog.setText(SourceLookupUIMessages.ExternalArchiveSourceContainerBrowser_2); 
        dialog.setFilterExtensions(new String[]{"*.jar;*.zip"});  //$NON-NLS-1$
        if (rootDir != null) {
            dialog.setFilterPath(rootDir);
        }
        dialog.open();
        String[] fileNames= dialog.getFileNames();
        int nChosen= fileNames.length;          
        if (nChosen > 0) {
            rootDir = dialog.getFilterPath();
            IPath filterPath= new Path(rootDir);
            ISourceContainer[] containers= new ISourceContainer[nChosen];
            for (int i= 0; i < nChosen; i++) {
                IPath path= filterPath.append(fileNames[i]).makeAbsolute(); 
                // TODO: configure auto-detect
                containers[i]= new ExternalArchiveSourceContainer(path.toOSString(), true);
            }
            DebugUIPlugin.getDefault().getDialogSettings().put(ROOT_DIR, rootDir);
            return containers;
        }
        return new ISourceContainer[0];
    }


*/

/**
 * 
 * 
 * Note: Need to support Status Line
 */
public class CorePreferencePage extends HeaderPage implements IPropertyChangeListener
{
    static private final String     FORM_TITLE = "Preferences";
    static private final String     PAGE_TITLE = "Preferences"; // Displayed at the bottom
    static private final String     DEFAULT_STYLE_FILE_NAME = "myTypeStyle.style.xml";
    
    private TypeSystemStyle                 tsStyle;
    private Object                          inputObject;
    private Object                          inputDefaultTypeStyles; // List/*TypeColor*/
    
    private DefaultColorTreeSectionPart     defaultStyleSection;
    private ColoredTypeTreeSectionPart      importedStyleSection;
    
    private IManagedForm            managedForm = null;
    private String                  formTitle = null;

    /*************************************************************************/
    
	protected CorePreferencePage(FormEditor editor, TypeSystemStyle typeStemStyle,
                                   String title) 
    {
		super(editor, PAGE_TITLE);
        this.tsStyle    = typeStemStyle;
        this.formTitle  = title;
	}
    
    /**
     * Create a preference page for CAS viewier
     * 
     * @param editor
     * @param typeStemStyle
     * @param title
     * @return CASViewerPreferencePage
     */
    static public CorePreferencePage createInstance (FormEditor editor, 
            TypeSystemStyle typeStemStyle, String title)
    {
        String formTitle;
        if (title == null || title.trim().length() == 0) {
            formTitle = FORM_TITLE;
        } else {
            formTitle = title;
        }
        String importedStyle = typeStemStyle.getImportedStyleMapFile();
        if (importedStyle != null) {
            formTitle += " (" + new File(importedStyle).getName() + ")";
        }
        return new CorePreferencePage(editor, typeStemStyle, formTitle);
    }
    
    public void dispose () {
        super.dispose();
    }
    
    public String getId () {
        return GenericCasViewer.PAGE_ID_PREFERENCE_PAGE;
    }
    
    /*************************************************************************/
	
	/**
	 * Called by the framework to fill in the contents
	 */
    protected void createFormContent (IManagedForm mForm) 
    { 
        this.managedForm = mForm;
        this.toolkit = managedForm.getToolkit();
        // createToolBarActions(managedForm);
        managedForm.getForm().setText(formTitle);

        ///////////////////////////////////////////////////////////////////////
        
        final Menu subMenu = new Menu (managedForm.getForm().getBody());

        MenuItem itemMenuImportStyleFile = new MenuItem(subMenu, SWT.NONE);
        itemMenuImportStyleFile.setText("Import style from file...");
        itemMenuImportStyleFile.setData(null);
        itemMenuImportStyleFile.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e)
            {
                FileDialog dialog = new FileDialog(managedForm.getForm().getShell(), SWT.SINGLE);
                dialog.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$;
                // dialog.setFilterPath(lastUsedPath);
                String result = dialog.open();
                if (result == null) {
                    return;
                }
                String fileName = result.trim();
                tsStyle.importAndApplyStyleMapFile(null, new File(fileName));
                fileName = fileName.replace('\\', '/');
                int i = fileName.lastIndexOf("/");
                if (i != -1) {
                    fileName = fileName.substring(i+1);
                }
                setInput(tsStyle, "Custom Style (" + fileName + ")");
//                if (tsStyle.getDefaultColors() != null) {
//                    tsStyle.populateDefaultColors ();
//                    setDefaultColors(tsStyle.getDefaultColors());
//                }
                
                ((ICASViewerListener) editor).onActionInvocation(this, 
                        ICASViewerListener.PAGE_EVENT_REFRESH_STYLE, null);
                            
            }

            public void widgetDefaultSelected(SelectionEvent e){}
        });

        MenuItem itemMenu = new MenuItem(subMenu, SWT.NONE);
        itemMenu.setText("Export style to file...");
        itemMenu.setData(null);
        itemMenu.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e)
            {
                doSaveCurrentStyles(false);
            }

            public void widgetDefaultSelected(SelectionEvent e){}
        });
        
        MenuItem itemMenuRestoreDefaults = new MenuItem(subMenu, SWT.NONE);
        itemMenuRestoreDefaults.setText("Restore default colors");
        itemMenuRestoreDefaults.setData(null);
        itemMenuRestoreDefaults.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e)
            {
                ((ICASViewerListener) editor).onActionInvocation(this, 
                        ICASViewerListener.PAGE_EVENT_RESTORE_DEFAULT_STYLE, null);
            }

            public void widgetDefaultSelected(SelectionEvent e){}
        });
        
        new MenuItem(subMenu, SWT.SEPARATOR);

        itemMenu = new MenuItem(subMenu, SWT.NONE);
        itemMenu.setText("Save type system to file...");
        itemMenu.setData(null);
        itemMenu.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e)
            {
                doSaveTypesystem();
            }

            public void widgetDefaultSelected(SelectionEvent e){}
        });
        
        Action optionsAction = new Action("Options", Action.AS_PUSH_BUTTON) {
            public void run() {
                subMenu.setVisible(true);
            }
        };
        optionsAction.setDescription("Import/Export style file");
        optionsAction.setText("Options");
        optionsAction.setChecked(false);
        optionsAction.setToolTipText("Options...");
        optionsAction.setImageDescriptor(ImageLoader.getInstance()
                .getImageDescriptor(ImageLoader.ICON_UI_IDE_IMPORT_PREF));
        managedForm.getForm().getToolBarManager().add(optionsAction);                
        managedForm.getForm().getToolBarManager().update(true);
        managedForm.getForm().getBody().setLayout(new GridLayout(1, false)); // this is required !
        
        Form2Panel form2Panel = setup2ColumnLayout(managedForm.getForm().getBody(), 55, 45);

        defaultStyleSection = DefaultColorTreeSectionPart.createInstance(this, managedForm, form2Panel.left,
                Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION,
                Messages.getString("DefaultColorTreeSection.Title"),
                Messages.getString("DefaultColorTreeSection.Description"),
                true, tsStyle);
        
        importedStyleSection = ColoredTypeTreeSectionPart.createInstance(managedForm, form2Panel.right,
                Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION,
                Messages.getString("CustomColorTreeSection.Title"),
                Messages.getString("CustomColorTreeSection.Description"),
                false);
        
        if (inputDefaultTypeStyles != null) {
            defaultStyleSection.setInput(inputDefaultTypeStyles, null);
        }
        
        if (inputObject != null) {
            importedStyleSection.setInput(inputObject, null);
        }
    }
    
    protected Form2Panel setup2ColumnLayout(Composite parent, int w1, int w2) 
    {
        parent.setLayout(new GridLayout(1, false)); // this is required !
        //    form.setLayoutData(new GridData(GridData.FILL_BOTH)); // does nothing
        Composite xtra = toolkit.createComposite(parent);
        xtra.setLayout(new GridLayout(1, false));
        xtra.setLayoutData(new GridData(GridData.FILL_BOTH));
        Control c = xtra.getParent();
        while ( ! (c instanceof ScrolledComposite))
            c = c.getParent();
        ((GridData)xtra.getLayoutData()).widthHint = c.getSize().x;
        ((GridData)xtra.getLayoutData()).heightHint = c.getSize().y;
        SashForm sashForm = new SashForm(xtra, SWT.HORIZONTAL);
        //      toolkit.adapt(sashForm, true, true); // makes the bar invisible (white)
        
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH)); // needed
        //      sashForm.setLayout(new GridLayout(1, false));
        //      form.setLayoutData(new GridData(GridData.FILL_BOTH)); //does nothing
        
        final Composite leftPanel = newnColumnSection(sashForm, 1);
        ((GridLayout) leftPanel.getLayout()).marginHeight = 5;
        ((GridLayout) leftPanel.getLayout()).marginWidth = 5;
        final Composite rightPanel = newnColumnSection(sashForm, 1);
        ((GridLayout) rightPanel.getLayout()).marginHeight = 5;
        ((GridLayout) rightPanel.getLayout()).marginWidth = 5;
        sashForm.setWeights(new int[] { w1, w2 });
//        float leftPanelPercent = (float)w1 / (float)(w1 + w2);
//        float rightPanelPercent = (float)w2 / (float)(w1 + w2);        
        sashForm.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                leftPanel.layout();
                rightPanel.layout();
            }
        });
        
        return new Form2Panel(xtra, leftPanel, rightPanel);
    }
    
    protected Composite newnColumnSection(Composite parent, int cols) 
    {
        Composite section = toolkit.createComposite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = cols;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        section.setLayout(layout);
        section.setLayoutData(new GridData(GridData.FILL_BOTH));
        return section;
    }    
    
    /*************************************************************************/

    /**
     * 
     * @param input     TypeSystemStyle object
     * @param title
     * @return void
     */
    public void setInput (Object input, String sectionTitle)
    {
        // Trace.trace();
        if (input == null) {
            return;
        }
        if (importedStyleSection == null) {
            inputObject = input;
            return;
        }
        importedStyleSection.setInput(input, sectionTitle);
        
        defaultStyleSection.setInput(tsStyle.getCurrentTypeStyleList(), null);
    }    
    
    /**
     * Set the default styles to the specified type styles
     * 
     * @param colors         List<TypeStyle>
     * @return void
     */
    public void setDefaultTypeStyles (Object typeStyles)
    {
        if (defaultStyleSection == null) {
            inputDefaultTypeStyles = typeStyles;
            return;
        }
        defaultStyleSection.setInput(typeStyles, null);        
    }
    
    public void restoreDefaultTypeStyles ()
    {
        defaultStyleSection.setInput(tsStyle.getCurrentTypeStyleList(), null);
    }
    
    // Called from DefaultColorTreeSectionPart
    public void propertyChange(PropertyChangeEvent event) {
        ((ICASViewerListener) editor).onActionInvocation(this, 
                ICASViewerListener.PAGE_EVENT_UPDATE_TYPE_STYLE, event.getSource());        
    }
    
    public void doSaveTypesystem() 
    {
        // Use FileDialog
        FileDialog dialog = new FileDialog(getSite().getShell(), SWT.SAVE|SWT.SINGLE);
        dialog.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$;
        // dialog.setFilterPath(lastUsedPath);
        String result = dialog.open();
        if (result == null) {
            return;
        }
        IPath iPath = new Path(result.trim());

        final String fileName = iPath.toOSString();
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            public void execute(final IProgressMonitor monitor)
            throws CoreException {                     
                TypeSystemDescription tsDesc = TypeSystemUtilModified
                .typeSystem2TypeSystemDescription(((ICASViewer) getEditor()).getCAS().getTypeSystem());
                FileOutputStream outTs;
                try {
                    outTs = new FileOutputStream (fileName);
                    tsDesc.toXML(outTs);
                    outTs.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        runOperation(op, iPath, "Error saving", "Error occurred during save operation");
    }
    
    private boolean runOperation (WorkspaceModifyOperation op, 
                        IPath iPath, String title, String msg) {
        Shell shell = getSite().getShell();    
        boolean success = false;
        try {
            new ProgressMonitorDialog(shell).run(false, true, op);
            success = true;
        } catch (InterruptedException x) {
            x.printStackTrace();
        } catch (InvocationTargetException x) {
            Throwable targetException = x.getTargetException();

            if (targetException instanceof CoreException) {
                CoreException coreException = (CoreException) targetException;
                IStatus status = coreException.getStatus();
                if (status != null) {
                    switch (status.getSeverity()) {
                    case IStatus.INFO :
                        MessageDialog.openInformation(shell, title, msg);
                        break;
                    case IStatus.WARNING :
                        MessageDialog.openWarning(shell, title, msg);
                        break;
                    default :
                        MessageDialog.openError(shell, title, msg);
                    }
                } else {
                    MessageDialog.openError(shell, title, msg);
                }
            }
        }
        finally {
            if (success) {
                IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(iPath);
                try {
                    if (iFile != null) {
                        iFile.refreshLocal(IResource.DEPTH_INFINITE, null);
                    }
                } catch (CoreException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }   
        }
        return success;
    }
    
   
    // Will save in the following format:
    // "order:1; color:#000000; background:#f2edb6;"
    public void doSaveCurrentStyles(boolean useFileSystem ) 
    {
        Shell shell = getSite().getShell();
        
        IPath iPath = null;
        if (useFileSystem) {
            // Use FileDialog
            FileDialog dialog = new FileDialog(shell, SWT.SAVE|SWT.SINGLE);
            dialog.setFilterExtensions(new String[] { "*.style.xml", "*.xml" }); //$NON-NLS-1$;
            // dialog.setFilterPath(lastUsedPath);
            String result = dialog.open();
            if (result == null) {
                return;
            }
            iPath = new Path(result.trim());
        } else {
            // Use Eclipse's SaveAsDialog
            SaveAsDialog dialog = new SaveAsDialog(shell);
            dialog.setOriginalName(DEFAULT_STYLE_FILE_NAME);
            dialog.create();
            dialog.getShell().setText("Save as Type System style"); 
            dialog.setMessage("Generate a Type System style and save it as a file with extension \"style.xml\".");     
            if (dialog.open() == Dialog.CANCEL) {
                return;
            }
    
            IPath filePath = dialog.getResult();
            String projectName = filePath.segment(0);
            iPath = filePath.removeFirstSegments(1);
            IProject project = ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(projectName);
            iPath = project.getLocation().append(iPath).removeFileExtension().addFileExtension("style.xml");
        }
        
        final String fileName = iPath.toOSString();
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            public void execute(final IProgressMonitor monitor)
                throws CoreException {
                String s = tsStyle.saveTypeSystemStyle(fileName);
                // Trace.trace(s);
            }
        };
        runOperation(op, iPath, "Error saving", "Error occurred during save operation");
    }
    
}
