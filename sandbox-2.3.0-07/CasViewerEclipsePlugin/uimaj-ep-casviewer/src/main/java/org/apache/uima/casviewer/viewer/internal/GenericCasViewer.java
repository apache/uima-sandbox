package org.apache.uima.casviewer.viewer.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.casviewer.core.internal.BaseCASObject;
import org.apache.uima.casviewer.core.internal.CASObjectView;
import org.apache.uima.casviewer.core.internal.TypeSystemUtils2;
import org.apache.uima.casviewer.core.internal.casreader.XcasOrXmiUtil;
import org.apache.uima.casviewer.ui.internal.model.ICASViewControl;
import org.apache.uima.casviewer.ui.internal.model.ICASViewer;
import org.apache.uima.casviewer.ui.internal.model.ICASViewerListener;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.preferences.CorePreferencePage;
import org.apache.uima.tools.preferences.PreferenceConstants;
import org.apache.uima.tools.preferences.ViewerPreferencePage;
import org.apache.uima.uct.viewer.internal.page.CASViewPage;
import org.apache.uima.uct.viewer.internal.page.ICASViewPage;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.StatusLineContributionItem;
import org.xml.sax.SAXException;


public class GenericCasViewer extends FormEditor 
                              implements ICASViewer, ICASViewerListener 
{ 

    static public final String ID ="org.apache.uima.casviewer.viewer.GenericCasViewer";
    static private final String         DEFAULT_SOFA_TITLE = Messages.getString("DefaultSofa.Title");
    private static final String         DEFAULT_TYPE_SYSTEM_FOR_XCAS = "typesystem.xml";
    private static final String         DEFAULT_TYPE_PRIORITIES_FOR_XCAS = "typepriorities.xml";
    
    public static String                PAGE_ID_PREFERENCE_PAGE = "preferencePageId";
    
    static private GenericCasViewer     _instance = null;
    private IEditorSite                 editorSite    = null;
    protected BaseCASObject               baseCASObject;
    private TypeSystemStyle             typesystemStyle;
    
    protected boolean                     inputIsFile = true;
    protected String                      inputFileName = null;
    protected CAS                         inputCAS;
//    private TypeSystemDescription       typeSystem = null;
    private String                      typesystemStyleFile;

    private String                      title = null;
    private String                      pageZeroText = "";

    protected ICASViewPage[]            casViewPages;
    private CorePreferencePage          preferencePage;
    private int                         preferencePageIndex;
    private List<String>                preSelectedTypeNames = null;
    
    // Preferences
    protected boolean                     prefPreselectAll;
    protected boolean                     prefHideFeaturesInTypeSystem;
    protected boolean                     prefHideNoValueFeature;
    protected boolean                     prefHidePreferencePage;
    protected boolean                     prefFlatLayout4Types;
    protected boolean                     prefFlatLayout4FS;    
    
    /*************************************************************************/
    /*                          Preferences Listener                         */
    /*************************************************************************/
    private final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() 
    {

        public void propertyChange(PropertyChangeEvent event) 
        {            
            int n = casViewPages.length;
            if (event.getProperty().equals(PreferenceConstants.P_PRE_SELECT_ALL_ANNOTATIONS)) {
                // Pre-select All Annotations ?
                
            } else if (event.getProperty().equals(PreferenceConstants.P_HIDE_FEATURES_IN_TYPE_SYSTEM)) {
                // Hide features in type system's tree
                for (int i=0; i<n; ++i) {
                    casViewPages[i].setPreference(ICASViewControl.PREF_HIDE_FEATURES_IN_TYPE_SYSTEM,
                            Boolean.valueOf((String)event.getNewValue()).booleanValue());
                }                
            } else if (event.getProperty().equals(PreferenceConstants.P_HIDE_FEATURE_NO_VALUE)) {
                // Hide feature having no value when showing Flat Structure
                for (int i=0; i<n; ++i) {
                    casViewPages[i].setPreference(ICASViewControl.PREF_HIDE_FEATURE_NO_VALUE,
                            Boolean.valueOf((String)event.getNewValue()).booleanValue());
                }                                
            } else if (event.getProperty().equals(PreferenceConstants.P_HIDE_PREFERENCE_PAGE)) {
                // Hide/Show preference page
                if (!Boolean.valueOf((String)event.getNewValue()).booleanValue()) {
                    // Show
                    if (prefHidePreferencePage) {
                        try {
                            if (preferencePage == null) {
                                Trace.err("preferencePage == null");
                                return;
                            }
                            preferencePageIndex = addPage(preferencePage);
                            if (getPageCount() <= 2) {
                                showTabs();
                            }
                        } catch (PartInitException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Hide
                    if (findPage(PAGE_ID_PREFERENCE_PAGE) != null) {
                        removePage(preferencePageIndex);
                        hideTabs();
                    }
                }
                prefHidePreferencePage = Boolean.valueOf((String)event.getNewValue()).booleanValue();

            } else if (event.getProperty().equals(PreferenceConstants.P_FLAT_LAYOUT_FOR_TYPES)) {
                Trace.err("P_FLAT_LAYOUT_FOR_TYPES");
                for (int i=0; i<n; ++i) {
                    casViewPages[i].setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_TYPES,
                            Boolean.valueOf((String)event.getNewValue()).booleanValue());
                }                

            } else if (event.getProperty().equals(PreferenceConstants.P_FLAT_LAYOUT_FOR_FS)) {
                Trace.err("P_FLAT_LAYOUT_FOR_FS");
                for (int i=0; i<n; ++i) {
                    casViewPages[i].setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_FS,
                            Boolean.valueOf((String)event.getNewValue()).booleanValue());
                }                

            } else if (event.getProperty().equals(PreferenceConstants.P_STYLE_FILE_EXTENSION)) {
                // Style file's extension: .style.xml (default)                
                
            } else if (event.getProperty().equals(PreferenceConstants.P_DEFAULT_FOREGROUND)) {
                // Foreground color has changed
                // Trace.err("Foreground color has changed");
                
            } else if (event.getProperty().equals(PreferenceConstants.P_DEFAULT_BACKGROUND)) {
                // Background color has changed
                
            }
        }
    };
    
    /*************************************************************************/
    
    static public void callGenericCASViewer (CAS aCAS, String title, boolean sharedView)
    {
        callGenericCASViewer(aCAS, title, sharedView, null);
    }
    
    static public void callGenericCASViewer (CAS aCAS, String title, boolean sharedView, String typesystemStyleFile)
    {
        IWorkbenchWindow activeWorkbenchWindow =
            PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page != null) {
            try {
                GenericCasViewer viewer = null;
                if (sharedView) {
                    viewer = GenericCasViewer.getInstance ();
                }
                if (viewer == null) {                
                    viewer = (GenericCasViewer) page.openEditor(new GenericEditorInput(aCAS, title, typesystemStyleFile), ID);
                    return;
                }
                if (viewer != null) { 
                    viewer.setInput(aCAS, title);
                }
            } catch (PartInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }    
    }

    static public void callGenericCASViewer (String xcasFileName, String title, boolean sharedView)
    {
        callGenericCASViewer (xcasFileName, title, sharedView, null);
    }
    
    static public void callGenericCASViewer (String xcasFileName, String title, boolean sharedView,
                                TypeSystemDescription aTypeSystemDescription)
    {
        callGenericCASViewer (xcasFileName, title, sharedView, aTypeSystemDescription,
                null);
        
    }
    
    static public void callGenericCASViewer (String xcasFileName, String title, boolean sharedView,
                                    TypeSystemDescription aTypeSystemDescription,
                                    List<String> preSelectedTypeNames)
    {
        IWorkbenchWindow activeWorkbenchWindow =
            PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page != null) {
            try {
                GenericCasViewer viewer = null;
                if (sharedView) {
                    viewer = GenericCasViewer.getInstance ();
                }
                if (viewer == null) {
                    // New Editor instance
                    IEditorPart part = page.openEditor(new GenericEditorInput(xcasFileName, aTypeSystemDescription, preSelectedTypeNames), ID);
                    if (part instanceof GenericCasViewer) {
                        viewer = (GenericCasViewer) part;
                    } else {
                        part.dispose();
                    }
                } else {
                    // Reuse old instance
                    viewer.setInput(xcasFileName, title);
                }
            } catch (PartInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }    
    }
    
    /*************************************************************************/
    /*          The following methods can be override by subclass            */
    /*************************************************************************/
    
    protected BaseCASObject createBaseCASObject () {
        if (inputIsFile) {
            // Create BaseCASObject from xcas/xmi file
            // long timeStart = System.currentTimeMillis();
            baseCASObject = createBaseCASObjectFromFile (inputFileName);
            // Trace.err("Create CAS from file Performance: " + (System.currentTimeMillis() - timeStart) + " ms");
            if (baseCASObject == null) {
                return null;
            }
        } else {
            baseCASObject = new BaseCASObject (inputCAS);
            inputFileName = "";
        }
        return baseCASObject;
    }
    
    protected ICASViewPage createCASViewPage (FormEditor editor, BaseCASObject aBaseCASObject, 
                                SofaFS sofa, String inFileName, String title,
                                TypeSystemStyle tsStyle, String tsStyleFileName) {
        ICASViewPage page = new CASViewPage(this, new CASObjectView(aBaseCASObject, sofa), 
                inFileName, title, tsStyle, tsStyleFileName);
        page.setStatusLineManager(getStatusLineManager());
        page.setPreference(ICASViewControl.PREF_HIDE_FEATURES_IN_TYPE_SYSTEM, prefHideFeaturesInTypeSystem);
        page.setPreference(ICASViewControl.PREF_HIDE_FEATURE_NO_VALUE, prefHideNoValueFeature);
        page.setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_TYPES, prefFlatLayout4Types);
        page.setPreference(ICASViewControl.PREF_FLAT_LAYOUT_FOR_FS, prefFlatLayout4FS);

        return page;
    }
    
    protected TypeSystemDescription createTypeSystemDescription (String typesystemFileName, ResourceManager rm) throws InvalidXMLException, ResourceInitializationException, IOException {
        return TypeSystemUtils2.getTypeSystemDescription(typesystemFileName, rm);                
    }

    /*************************************************************************/
    
    public GenericCasViewer() {
        super();
        _instance = this;
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException 
    {
        if ( ! (input instanceof IFileEditorInput) 
             && ! (input instanceof GenericEditorInput)
             && ! (input instanceof IPathEditorInput)) {
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        }
        
        if ( input instanceof IFileEditorInput ) {
            IFile file = ((IFileEditorInput) input).getFile();
            inputFileName = file.getLocation().toOSString();
        } else if (input instanceof GenericEditorInput) {
            Object obj = ((GenericEditorInput) input).getInput();
            preSelectedTypeNames = ((GenericEditorInput) input).getPreSelectedTypeNames();
            if (obj instanceof String) {
                inputFileName = ((GenericEditorInput) input).getName();
//                typeSystem = ((GenericEditorInput) input).getTypeSystemDescription();
            } else if (obj instanceof CAS) {
                inputIsFile = false;
                inputCAS = (CAS) ((GenericEditorInput) input).getInputCAS();
                title = ((GenericEditorInput) input).getTitle();
                typesystemStyleFile = ((GenericEditorInput) input).getTypesystemStyleFile();
            } else {
                // Trace.trace("input instanceof " + input.getClass().getName());
                throw new PartInitException("Invalid Input: Must be a File or a CAS");
            }
        } else if (input instanceof IPathEditorInput) {
            inputFileName = ((IPathEditorInput) input).getPath().toOSString();  
            // Trace.trace("IPathEditorInput: " + inputFileName);
            // Path path = new Path(inputFileName);
            // IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

        }
        super.init (site, input);
        editorSite = site;
        
        setPartName ("CAS Viewer (" + CasViewerGenericPlugin.getDefault().getVersion() + ")");

        // Get Preferences
        prefHideFeaturesInTypeSystem = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_HIDE_FEATURES_IN_TYPE_SYSTEM, true);
        prefPreselectAll = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_PRE_SELECT_ALL_ANNOTATIONS, false);
        prefHideNoValueFeature = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_HIDE_FEATURE_NO_VALUE, true);
        prefHidePreferencePage = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_HIDE_PREFERENCE_PAGE, true);
        prefFlatLayout4Types = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_FLAT_LAYOUT_FOR_TYPES, true);
        prefFlatLayout4FS = ViewerPreferencePage
                    .getPreferenceBoolean(PreferenceConstants.P_FLAT_LAYOUT_FOR_FS, true);
        
        // Add Preferences Listener
        CasViewerGenericPlugin.getDefault().getPluginPreferences()
                .addPropertyChangeListener(propertyChangeListener);                
    } // init
        
    // Used to share viewer
    public static GenericCasViewer getInstance () {
        return _instance;
    }
    
    public void dispose () {
        if (typesystemStyle != null) {
            typesystemStyle.dispose();
        }
        
        CasViewerGenericPlugin.getDefault().getPluginPreferences()
                .removePropertyChangeListener(propertyChangeListener);
        _instance = null;
    }
        
    /**
     * If there is just one page in the multi-page editor part,
     * this hides the single tab at the bottom.
     */
    protected void hideTabs() {
        if (getPageCount() <= 1) {
            setPageText(0, "");
            if (getContainer() instanceof CTabFolder) {
                int t = ((CTabFolder)getContainer()).getTabHeight();
                ((CTabFolder)getContainer()).setTabHeight(1);
                Point point = getContainer().getSize();
                getContainer().setSize(point.x, point.y + (t-1));
            }
        }
    }

    /**
     * If there is more than one page in the multi-page editor part,
     * this shows the tabs at the bottom.
     */
    protected void showTabs() {
        if (getPageCount() > 1) {
            setPageText(0, pageZeroText);
            if (getContainer() instanceof CTabFolder) {
                ((CTabFolder)getContainer()).setTabHeight(SWT.DEFAULT);
                int t = ((CTabFolder)getContainer()).getTabHeight();
                Point point = getContainer().getSize();
                getContainer().setSize(point.x, point.y - t);
            }
        }
    }
        
    /*************************************************************************/

    public void createPages() {
        super.createPages();
        // Ensures that this CAS Viewer will only display the page's tab
        // area if there are more than one page
        getContainer().addControlListener(new ControlAdapter(){
            boolean guard = false;
            @Override
            public void controlResized(ControlEvent event) {
                if (!guard) {
                    guard = true;
                    hideTabs();
                    guard = false;
                }
            }
        });
    }
    
    protected void addPages()
    {               
        // Create BaseCASObject from file or CAS
        if ( createBaseCASObject () == null ) {
            // Give up
            return;
        }
        
        /*********************************************************************/
        
        // Create a list of type's names from the Type System Tree
        // (no built-in types and sorted by Short Name)
        List<TypeDescription> types = baseCASObject.getTypeTree().getTypeListFromHierarchy(false, true);
        types.remove(baseCASObject.getTypeTree().getRoot().getObject());
        List<String> typeNames = new ArrayList<String>(types.size());
        for (int i=0; i<types.size(); ++i) {
            typeNames.add(types.get(i).getName());
        }        
        
        // Create Type System's Style Object
        // This TS style will be passed to each CASViewPage (pass to CASViewControl)
        // It implies that ALL pages (sofas) will SHARE the same style.
        typesystemStyle = TypeSystemStyle.createInstance(null);
        // typesystemStyle.preSelectAllAnnotations(prefPreselectAll);
        if (preSelectedTypeNames == null && prefPreselectAll) {
            // "preSelectedTypeNames" is passed through GenericEditorInput
            // So, give "preSelectedTypeNames" high priority over Preferences's prefPreselectAll
            preSelectedTypeNames = typeNames;
        }
        
        // Get style file and apply, if any
        File styleFile = null;
        if (typesystemStyleFile != null) {
            // TS style file in specified as input
            styleFile = new File (typesystemStyleFile);
            if (!styleFile.exists()) {
                styleFile = null;
            }
        } else if (inputIsFile) {
            // Try to get TS style file based on input file name
            styleFile = getStyleFile (inputFileName, ".style.xml");
            if (styleFile != null) {
                typesystemStyleFile = styleFile.getAbsolutePath();
            }
        }
        // Has imported styles ?
//        if (styleFile != null) {
//            Trace.err("Import style: " + styleFile.getName());
//            // All styles from imported file will be used (instead of default styles)
//            typesystemStyle.importAndApplyStyleMapFile(typeNames, styleFile);
//        } else {
//            // No Import. Assign "default" style to each type.
//            typesystemStyle.assignStyleToTypes(typeNames);
//        }        
        typesystemStyle.assignStyleToTypes(typeNames, styleFile);

        /*********************************************************************/
        
        createPagesFromCASObject (baseCASObject);
        pageZeroText = getPageText(0);
        
        // Select some types ?
        if (preSelectedTypeNames != null) {
            selectTypesByName(preSelectedTypeNames);
        }
        
        /*********************************************************************/
        
        preferencePage = CorePreferencePage.createInstance(this, typesystemStyle, null);
        try {
            // Show/Hide Preference Page
            if (!prefHidePreferencePage) {
                preferencePageIndex = addPage(preferencePage);
            }
        } catch (PartInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Display current type styles
        preferencePage.setDefaultTypeStyles(typesystemStyle.getCurrentTypeStyleList());       
    }
    
    private void deletePages ()
    {
        if (casViewPages == null) {
            return;
        }
        // Dispose OLD pages
        for (int i=0; i<casViewPages.length; ++i) {
            this.removePage(casViewPages[i].getIndex());
            casViewPages[i].dispose();
        }
        casViewPages = null;  
        
        if (preferencePage != null) {
            if (!prefHidePreferencePage) {
                // Only valid if preferencePage is added 
                this.removePage(preferencePage.getIndex());
            }
            preferencePage.dispose();
            preferencePage = null;
        }
        
        // Dispose Color objects
//        TypeSystemStyle.dispose();
    }
    
    protected void createPagesFromCASObject (BaseCASObject aBaseCASObject)
    {
        // Create 1 page per CAS's view (sofa)
        try {
            FSIterator it = aBaseCASObject.getCAS().getSofaIterator();
            List<ICASViewPage> pageList = new ArrayList<ICASViewPage>(5);
            while (it.hasNext()) {
                SofaFS sofa = (SofaFS) it.next();
                // If NO document text, don't create page
                String docText = sofa.getCAS().getView(sofa).getDocumentText();
                if (docText == null || docText.trim().length() == 0) {
                	continue;
                }
                
//                CasToInlineXhtml c = new CasToInlineXhtml();
//                try {
//                    String s = c.generateXHTML(aCASObject.getCAS(), null);
//                    Trace.trace(s);
//                } catch (CASException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                
                ICASViewPage page = createCASViewPage(this, aBaseCASObject, sofa, 
                                        inputFileName, title, 
                                        typesystemStyle, typesystemStyleFile);
                addPage(page);
                pageList.add(page);
            }
            casViewPages = (ICASViewPage[]) pageList.toArray(new ICASViewPage[pageList.size()]);
            
            contributeToStatusLine(getStatusLineManager());
            ((StatusLineContributionItem)fStatusFields.get(STATUS_FIELD_DEFS[2])).setText("CAS Viewer");
            
        } catch (PartInitException e) {
            e.printStackTrace();
        }        
    }
    
    /**
     * 	Create BaseCASObject from xcas/xmi file
     * 
     * @param xcasOrxmiFileName
     * @return
     */
    protected BaseCASObject createBaseCASObjectFromFile (String xcasOrxmiFileName)
    {
        BaseCASObject   casObject = null;
        String          typesystemFileName = null;

        // Create Visual CAS
        if (xcasOrxmiFileName != null && xcasOrxmiFileName.trim().length()>0 ) {
            
            String s = xcasOrxmiFileName.replace('\\', '/');
            int lastIndex = s.lastIndexOf('/');

            // Look for TypePriorities xml file
            String typePrioritiesFileName = null;
            if ( lastIndex != -1) {
                typePrioritiesFileName = s.substring(0, lastIndex+1) + DEFAULT_TYPE_PRIORITIES_FOR_XCAS;
            }
            if ( ! (new File(typePrioritiesFileName)).exists() ) {
                // No Type Priorities file
                // Trace.err("NO priority file: " + typePrioritiesFileName);
                typePrioritiesFileName = null;
            }
            
            // Which file format (XMI or XCAS) ?
            if (xcasOrxmiFileName.endsWith("xmi") || xcasOrxmiFileName.endsWith("xcas")) {
                // Look for Type System used with this Xcas
                // Prefix with the path from Xcas instance file. Make the file absolute
                String path = ".";
                if ( lastIndex != -1) {
                    path = s.substring(0, lastIndex);
                }
                File directory = new File(path);
                if ( directory.isDirectory() ) {
                    typesystemFileName = findTypesystemmFileInDirectory(directory, DEFAULT_TYPE_SYSTEM_FOR_XCAS);
                }

                if ( typesystemFileName == null ) {
                    typesystemFileName = findTypesystemmFileInNLPProject();
                    if ( typesystemFileName != null ) {
                        Trace.err("Get TS file from NLP project: " + typesystemFileName);
                    }
                }
                if ( typesystemFileName == null ) {
                    // No Type System file
                    int returnCode = PopupMessage.popOkCancel("No Type System", 
                          "Cannot find type system file (" + DEFAULT_TYPE_SYSTEM_FOR_XCAS 
                          + ") to be used with the selected XmiCas file.\n\n"
                          + "Do you wish to select the type system Xml descriptor ?", 
                            MessageDialog.QUESTION);
                    if ( returnCode != Window.CANCEL ) {
                        typesystemFileName =  selectTypesystemFile();
                    } else {
                        return null;
                    }
                    if (typesystemFileName == null) {
                        return null;
                    }
                }
                
                // Create TypeSystemDescription from UIMA descriptor
                TypeSystemDescription typeSystemDescription = null;
                try {
                    typeSystemDescription = createTypeSystemDescription(typesystemFileName, 
                                    createResourceManagerFromFileName(typesystemFileName));
                } catch (InvalidXMLException e1) {
                    e1.printStackTrace();
                } catch (ResourceInitializationException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }        
                if (typeSystemDescription == null) {
                    return null;
                }
                
                try {
//                    List<String> excludedTypeNames = new ArrayList<String>();
//                    excludedTypeNames.add("com.ibm.hutt.GPE");
//                    casObject = XcasOrXmiUtil.createBaseCASObjectFromFile(typesystemFileName, 
//                                        xcasOrxmiFileName, typePrioritiesFileName);
//                                        excludedTypeNames);
                    casObject = XcasOrXmiUtil.createBaseCASObjectFromFile(new File(xcasOrxmiFileName), 
                            typeSystemDescription);
                    casObject.setXmiFileLocation(xcasOrxmiFileName);
                } catch (ResourceInitializationException e) {
                    ReportError.openExceptionError(e);
                } catch (IOException e) {
                    ReportError.openExceptionError(e);
                } catch (SAXException e) {
                    ReportError.openExceptionError(e);
                }
            } else {
                Trace.bug("Cannot open this unknown file: " + xcasOrxmiFileName);                
            }
        }        
        return casObject;
    } // createCASObjectFromFile   
    
    protected String selectTypesystemFile() 
    {        
        IResource resource = WorkspaceResourceDialog.getWorkspaceResourceElement (getContainer().getShell(), 
                ResourcesPlugin.getWorkspace().getRoot(),
                "Select UIMA descriptor",
                "Select Type System or Analysis Engine's Xml descriptor file");
        if (resource != null && resource instanceof IFile) {
            String xmlFile = resource.getLocation().toString();
            // Trace.err(xmlFile);
            return xmlFile;
        }
        return null;
    }
    
    private IFile selectTypesystemFile(Shell shell) 
    {        
        IResource resource = WorkspaceResourceDialog.getWorkspaceResourceElement (shell, 
                ResourcesPlugin.getWorkspace().getRoot(),
                "Select UIMA Descriptor",
                "Select type system's (or UIMA descriptor) Xml descriptor file from Workspace.");
        if (resource != null && resource instanceof IFile) {
            return (IFile) resource;
        }
        return null;
    }    

    /**
     * Find file having name ended with the specified string
     * (case-insensitive)
     * 
     * @param directory
     * @param nameEndsWith
     * @return File
     */
    protected String findTypesystemmFileInDirectory (File directory, String nameEndsWith) {    
        File[] contents = directory.listFiles();
        // Look for nameEndsWith
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().toLowerCase().endsWith(nameEndsWith.toLowerCase())) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
    
    private static final String DOT_CORPUS_FILENAME = ".corpus";
    
    protected String findTypesystemmFileInNLPProject () {
//        IResource dotCorpusResource = getProject().getFile(DOT_CORPUS_FILENAME);
//        if (dotCorpusResource instanceof IFile) {
//            if (((IFile) dotCorpusResource).exists()) {
//                try {
//                    String tsFileName = DotCorpusReader.parseDotCorpus(((IFile) dotCorpusResource).getContents());
//                    if (tsFileName != null) {
//                        return getProject().getFile(tsFileName).getLocation().toPortableString();
//                    }
//                } catch (CoreException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return null;
    }
    
    
    protected File getStyleFile (String xcasOrxmiFileName, String dotExtension)
    {
        String s = xcasOrxmiFileName.replace('\\', '/');
        int lastIndex = s.lastIndexOf('/');
        String path = ".";
        if ( lastIndex != -1) {
            path = s.substring(0, lastIndex);
        }
        
        File directory = new File(path);
        if ( ! directory.isDirectory() ) {
            return null;
        }

        File[] contents = directory.listFiles();
        // Look for "*StyleMap.xml"
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().endsWith("StyleMap.xml")) {
                // Trace.trace(">>> Found OLD style file: " + file.getName());
                return file;
            }
        }
        /// Look for "*.style.xml"
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().endsWith(dotExtension)) {
                // Trace.trace(">>> Found style file: " + file.getName());
                return file;
            }
        }
        return null;
    }
    
    /*************************************************************************/
    
/*    private void testSelectTypes (List types)
    {
        // Trace.trace();
        List typeNames = new ArrayList(5);
        typeNames.add("com.ibm.cab.types.transcripts.Acronym");
        typeNames.add("com.ibm.cab.types.transcripts.AreaCode");
        typeNames.add("com.ibm.cab.types.transcripts.IgnoredSection");
        
        int n = this.getPageCount();
        for (int i=0; i<n; ++i) {
            casViewPages[i].selectTypesByName(typeNames);
        }
        
        
        // For Testing !
        List list = getSelectedTypeNames();
        Trace.trace("getSelectedTypeNames: " + list.size());
        for (int i=0; i<list.size(); ++i) {
            Trace.trace("    getSelectedTypeNames: " + list.get(i));
        }
    }
*/
    
    /*************************************************************************/

    public void doSave(IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        
    }

    public void doSaveAs() {
        // TODO Auto-generated method stub
        
    }

    public boolean isSaveAsAllowed() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Returns the status line manager of this editor.
     * @return the status line manager of this editor
     */
    static public IStatusLineManager getStatusLineManager() 
    {
        if (_instance == null) return null;
//      IActionBars bars = sourceViewer.getViewSite().getActionBars();
//      IStatusLineManager statusLine = bars.getStatusLineManager();  
        
        IEditorActionBarContributor contributor= _instance.getEditorSite().getActionBarContributor();
        if (!(contributor instanceof EditorActionBarContributor))
            return null;

        IActionBars actionBars= ((EditorActionBarContributor) contributor).getActionBars();
        if (actionBars == null)
            return null;

        return actionBars.getStatusLineManager();
    }
    
    /**
     * Status field definition.
     */
    private static class StatusFieldDef {

        private String category;
        private String actionId;
        private boolean visible;
        private int widthInChars;

        private StatusFieldDef(String category, String actionId, boolean visible, int widthInChars) {
            // Assert.isNotNull(category);
            this.category= category;
            this.actionId= actionId;
            this.visible= visible;
            this.widthInChars= widthInChars;
        }
    }
    public final static int DEFAULT_CHAR_WIDTH = 40;

    /**
     * The status fields to be set to the editor
     */
    Map fStatusFields= new HashMap(3);
    private final static StatusFieldDef[] STATUS_FIELD_DEFS= {
        new StatusFieldDef(ITextEditorActionConstants.STATUS_CATEGORY_FIND_FIELD, null, false, 15),
        new StatusFieldDef(ITextEditorActionConstants.STATUS_CATEGORY_ELEMENT_STATE, null, true, DEFAULT_CHAR_WIDTH + 1),
        new StatusFieldDef(ITextEditorActionConstants.STATUS_CATEGORY_INPUT_MODE, ITextEditorActionDefinitionIds.TOGGLE_OVERWRITE, true, DEFAULT_CHAR_WIDTH),
        new StatusFieldDef(ITextEditorActionConstants.STATUS_CATEGORY_INPUT_POSITION, ITextEditorActionConstants.GOTO_LINE, true, DEFAULT_CHAR_WIDTH)
    };

    public void contributeToStatusLine(IStatusLineManager statusLineManager) {
        
        for (int i= 0; i < STATUS_FIELD_DEFS.length; i++) {
            StatusFieldDef fieldDef= STATUS_FIELD_DEFS[i];
            fStatusFields.put(fieldDef, new StatusLineContributionItem(fieldDef.category, fieldDef.visible, fieldDef.widthInChars));
        }
        
        for (int i= 0; i < STATUS_FIELD_DEFS.length; i++) {
            statusLineManager.add((IContributionItem)fStatusFields.get(STATUS_FIELD_DEFS[i]));
        }
    }
    
    /*************************************************************************/

    // ICASViewerListener
    public void onActionInvocation(Object source, int flags, Object obj) 
    {
        // Trace.trace();
        int n = casViewPages.length; 
        if (flags == ICASViewerListener.PAGE_EVENT_REFRESH_STYLE) { 
            // Notify when a style file is imported
//            List types = aCASObject.getTypeTree().getTypeListFromHierarchy(false, true);
//            types.remove(aCASObject.getTypeTree().getRoot().getObject());
//            List typeNames = new ArrayList(types.size());
//            for (int i=0; i<types.size(); ++i) {
//                typeNames.add(((TypeMetadata) types.get(i)).getName());
//            }
            
            for (int i=0; i<n; ++i) {
                casViewPages[i].refreshTypeSystemStyle();
            }
        } else if (flags == ICASViewerListener.PAGE_EVENT_RESTORE_DEFAULT_STYLE) {
            typesystemStyle.restoreDefaultStyle ();
            for (int i=0; i<n; ++i) {
                casViewPages[i].refreshTypeSystemStyle();
            }
            preferencePage.restoreDefaultTypeStyles();
            
        } else if (flags == ICASViewerListener.PAGE_EVENT_REFRESH_INPUT) {
            refreshInput();

        } else if (flags == ICASViewerListener.PAGE_EVENT_UPDATE_TYPE_STYLE) {
            // Call when the type's style is modified in preference page
            for (int i=0; i<n; ++i) {
                casViewPages[i].refreshTypeSystemStyle();
            }
        }
        
    }
    
    /*************************************************************************/
    /*                          ICASViewerListener Listeners                          */
    /*************************************************************************/
    
    private ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    
    public void addSelectionListener(ICASViewerListener listener)
    {
        selectionListeners.add(listener);
    }
    
    public void removeSelectionListener(ICASViewerListener listener)
    {
        selectionListeners.remove(listener);
    }
    
    /*************************************************************************/
    /*                      ICASViewer Implementation                        */
    /*************************************************************************/
    
    /**
     *  Sets the input to this CAS Viewer.
     *  The viewer will open this file.
     * 
     * @param xcasOrxmiFileName Full path name of xcas/xmi file
     * @param title             Title to be shown in each page (can be null)
     * @return void
     */
    public void setInput(String xcasOrxmiFileName, String title) 
    {
        if (xcasOrxmiFileName == null || xcasOrxmiFileName.trim().length() == 0) {
            return;
        }
        inputFileName = new String(xcasOrxmiFileName.trim()); 
        this.title    = title;
        deletePages ();

        this.addPages();
        setActivePage(0);
    }

    public void setInput(CAS aCAS, String title) 
    {
        if (aCAS == null) {
            Trace.err("aCAS == null");
            return;
        }
        deletePages ();
        this.title    = title;
        
        inputCAS = aCAS;
        this.addPages();
        setActivePage(0);
    }
    
    /**
     * Refresh current input file
     * 
     * @return void
     */
    public void refreshInput ()
    {
        if (inputFileName != null) {
            // Try to preserve current sofa
            int current = getActivePage();
            setInput(inputFileName, null);
            setActivePage(current);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASViewer#getCAS()
     */
    public CAS getCAS() {
        if (baseCASObject != null) {
            return baseCASObject.getCAS();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASViewer#getSelectedTypeNames()
     */
    public List getSelectedTypeNames() {
        if (casViewPages.length > 0) {
            return casViewPages[0].getSelectedTypeNames();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASViewer#setSelectedTypesByName(java.util.List)
     */
    public void selectTypesByName(List typeNames) {
        for (int i=0; i<casViewPages.length; ++i) {
            casViewPages[i].selectTypesByName(typeNames);
        }        
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASViewer#deselectTypesByName(java.util.List)
     */
    public void deselectTypesByName(List typeNames) {
        for (int i=0; i<casViewPages.length; ++i) {
            casViewPages[i].deselectTypesByName(typeNames);
        }        
    }    
    
    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASViewer#selectAndReveal(org.apache.uima.cas.FeatureStructure)
     */
    public void selectAndReveal(FeatureStructure featureStructure) {
    }

    public void setSelectedAnnotation(Object annotation) {
    }
    
    /*********************************************************************/
    
    private static final String DATAPATH_PROPERTY_KEY = "CDEdataPath";
    
    public String getDataPath() {
        String dataPath = null;
        try {
          IProject project = getProject();
          if (project != null) {
              dataPath = project.getPersistentProperty(new QualifiedName("", DATAPATH_PROPERTY_KEY));
          }
        } catch (CoreException e) {
        }
        if (null == dataPath) {
          dataPath = "";
        }
        return dataPath;
    }
    
    public ResourceManager createResourceManager(String classPath) {
        // workspacePath = TAEConfiguratorPlugin.getWorkspace().getRoot().getLocation().toString();
        ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();

        try {
          if (null == classPath)
            classPath = getProjectClassPath();
          resourceManager.setExtensionClassPath(this.getClass().getClassLoader(), classPath, true);
          resourceManager.setDataPath(getDataPath());
        } catch (MalformedURLException e) {
          // throw new InternalErrorCDE(Messages.getString("MultiPageEditor.14"), e1); //$NON-NLS-1$
            e.printStackTrace();
        } catch (CoreException e) {
          // throw new InternalErrorCDE(Messages.getString("MultiPageEditor.15"), e1); //$NON-NLS-1$
            e.printStackTrace();
        }
        return resourceManager;
    }
 
    protected IFile getFile () {
        IFile iFile = null;
        if (inputIsFile) {
            // Trace.err("Input file: " + inputFileName);
            Path path = new Path(inputFileName);
            iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
//            IFile[] iFiles = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(path);
//            if (iFiles.length > 0) {
//                iFile = iFiles[0];
//                for (int i=0; i<iFiles.length; ++i) {
//                    Trace.err(iFiles[i].toString());
//                }
//            }
//        } else {
//            Trace.err("Input is NOT a file");
        }
        return iFile;
    }
    
    public IProject getProject() {
        IFile iFile = getFile();
        if (null == iFile) {
            // Trace.err("Not an IFile");
            return null;
        }
        return iFile.getProject();
    }
    
    public IJavaProject getJavaProject() {
        IProject project = getProject();
        
        try {
            if (null == project || !project.isNatureEnabled("org.eclipse.jdt.core.javanature")) { //$NON-NLS-1$
              return null; 
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        IJavaProject javaProj = JavaCore.create(project);
        
        return javaProj;
    }

    final public static String PATH_SEPARATOR = System.getProperty("path.separator"); //$NON-NLS-1$

    private long cachedStamp = -1;

    private String cachedClassPath = null;

    public String getProjectClassPath() throws CoreException {
      IProject project = getProject();
      Trace.err("Project name: " + (project==null? "null" : project.getName()));
      
      if (null == project || !project.isNatureEnabled("org.eclipse.jdt.core.javanature")) { //$NON-NLS-1$
        return ""; //$NON-NLS-1$
      }
      IJavaProject javaProj = JavaCore.create(project);
      IProject projectRoot = javaProj.getProject();

      IResource classFileResource = projectRoot.findMember(".classpath"); //$NON-NLS-1$
      long stamp = classFileResource.getModificationStamp();
      if (stamp == cachedStamp) {
        return cachedClassPath;
      }
      cachedStamp = stamp;

      StringBuffer result = new StringBuffer(1000);

      // TODO Depend on o.e.jdt.launching plugin
      String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProj);

      for (int i = 0; i < classPaths.length; i++) {
        String classPath = classPaths[i];

        URLClassLoader checker = null;
        try {
          // ignore this entry if it is the Java JVM path
          checker = new URLClassLoader(new URL[] { new File(classPath).toURL() });

        } catch (MalformedURLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (null == checker
        // || null != checker.findResource("java/lang/Object.class") //$NON-NLS-1$
                || null != checker.findResource("org/apache/uima/impl/UIMAFramework_impl.class")) //$NON-NLS-1$
          continue;
        if (result.length() > 0)
          result = result.append(PATH_SEPARATOR);
        result = result.append(classPath);
      }
      cachedStamp = stamp;
      cachedClassPath = result.toString();
      return cachedClassPath;
    }

    public String getImportedStyleFileName() {
        return typesystemStyleFile;
    }
    
    /**********************   Testing   ********************************/
    
    public String getTypesystemStyle (IFile typesystemIFile) {
        String name = typesystemIFile.getName();
        int i = name.lastIndexOf('.');
        IFile styleIFile = typesystemIFile.getParent()
                .getFile(new Path(name.substring(0, i) + ".style.xml"));
        if (!styleIFile.exists()) {
            Trace.err("NO Map file: " + styleIFile.getFullPath());  
            return null;
        }
        Trace.err("Map file: " + styleIFile.getFullPath());
        return styleIFile.getLocation().toPortableString();
    }

    private TypeSystemDescription getTypeSystemDescription (String tsFileName) throws InvalidXMLException, ResourceInitializationException, IOException {
        return TypeSystemUtils2.getTypeSystemDescription(tsFileName, createResourceManagerFromFileName(tsFileName));        
    }
    
    private TypeSystemDescription tsDescription = null;
    // 
    private TypeSystemDescription getTypeSystemDescription (IFile typesystemIFile) {
        if (tsDescription != null) {
            return tsDescription;
        }
        
        // Select TS from Workspace ?
        if (typesystemIFile == null) {
            typesystemIFile =  selectTypesystemFile(getEditorSite().getShell());
        }
        if (typesystemIFile == null) {
            return null;
        }
        typesystemStyleFile = getTypesystemStyle(typesystemIFile);
        
        IPath iPath = typesystemIFile.getLocation();
        if (iPath == null) {
            Trace.err("iPath == null for " + typesystemIFile.getRawLocation());
            return null;
        }
        // Remember the file name
        // typesystemFileName = new String(iPath.toPortableString());
        
        XMLInputSource in = null;
        try {
            in = new XMLInputSource(typesystemIFile.getLocation().toOSString());
            TypeSystemDescription typeDesc = UIMAFramework.getXMLParser().parseTypeSystemDescription(
                    in);
            typeDesc.resolveImports(createResourceManager(typesystemIFile));
            tsDescription = typeDesc;
            
            // setInputFileTitle(typesystemIFile.getName(), false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        }
        
        return tsDescription;
    }

    public ResourceManager createResourceManagerFromFileName(String fileName) {
        Path path = new Path(fileName);
        IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
        if (iFile == null) {
            // The specified file is NOT in workspace
            return null;
        }
        
        return createResourceManager (iFile);
    }
    
    public ResourceManager createResourceManager(IFile iFile) {
        String classPath = getProjectClassPath(iFile);
        String dataPath = getProjectDataPath(iFile);
        if ( (classPath == null || classPath.length() == 0)
          && (dataPath == null || dataPath.length() == 0)) {
            return null;
        }
        
        ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();
        try {
            if ( classPath != null && classPath.length() > 0 ) {
                resourceManager.setExtensionClassPath(this.getClass().getClassLoader(), classPath, true);
            }
            if ( dataPath != null && dataPath.length() > 0 ) {
                resourceManager.setDataPath(dataPath);
            }
        } catch (MalformedURLException e) {
        }
        return resourceManager;
    }
    
    /*************************************************************************/
    
    static public IJavaProject getJavaProject(IFile typesystemIFile) {
        if (typesystemIFile == null) {
            return null;
        }
        IProject project = typesystemIFile.getProject();
        
        try {
            if (null == project || !project.isNatureEnabled("org.eclipse.jdt.core.javanature")) { //$NON-NLS-1$
              return null; 
            }
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
        IJavaProject javaProj = JavaCore.create(project);
        
        return javaProj;
    }
    
//    final public static String PATH_SEPARATOR = System.getProperty("path.separator"); //$NON-NLS-1$
//
//    private long cachedStamp = -1;
//
//    private String cachedClassPath = null;

    public String getProjectClassPath(IFile iFile) {
        IJavaProject javaProj = getJavaProject(iFile);
        if (javaProj == null) {
            return "";
        }
        IProject projectRoot = javaProj.getProject();

        IResource classFileResource = projectRoot.findMember(".classpath"); //$NON-NLS-1$
        long stamp = classFileResource.getModificationStamp();
        if (stamp == cachedStamp)
            return cachedClassPath;
        cachedStamp = stamp;

        StringBuffer result = new StringBuffer(1000);

        String[] classPaths;
        try {
            classPaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProj);
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < classPaths.length; i++) {
            String classPath = classPaths[i];

            URLClassLoader checker = null;
            try {
                // ignore this entry if it is the Java JVM path
                checker = new URLClassLoader(new URL[] { new File(classPath).toURL() });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (null == checker
                    // || null != checker.findResource("java/lang/Object.class") //$NON-NLS-1$
                    || null != checker.findResource("org/apache/uima/impl/UIMAFramework_impl.class")) { //$NON-NLS-1$
                Trace.err("Skip calsspath: " + classPath);
                continue;
            }
            if (result.length() > 0) {
                result = result.append(PATH_SEPARATOR);
            }
            result = result.append(classPath);
        }
        cachedStamp = stamp;
        cachedClassPath = result.toString();
        return cachedClassPath;
    }
    
//    private static final String DATAPATH_PROPERTY_KEY = "CDEdataPath";
    
    public static String getProjectDataPath(IFile iFile) {
        IProject project = iFile.getProject();
        if (project != null) {
            try {
                String dataPath = project.getPersistentProperty(new QualifiedName("", DATAPATH_PROPERTY_KEY));
                if (dataPath != null && dataPath.trim().length() > 0) {
                    return dataPath.trim();
                }
            } catch (CoreException e) {
            }
        }
        return "";
    }
    
}
