package org.apache.uima.uct.viewer.internal.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Type;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.casviewer.ui.internal.annotations.FSSectionPart;
import org.apache.uima.casviewer.ui.internal.document.AnnotatedTextComposite;
import org.apache.uima.casviewer.ui.internal.document.AnnotatedTextSectionPart;
import org.apache.uima.casviewer.ui.internal.index.CasIndexRepoSectionPart;
import org.apache.uima.casviewer.ui.internal.index.FSIndexSectionPart;
import org.apache.uima.casviewer.ui.internal.model.ICASViewControl;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.type.TypesTreeSectionPart;
import org.apache.uima.casviewer.ui.internal.util.Form2Panel;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.casviewer.ui.internal.util.IObjectSelectionListener;
import org.apache.uima.casviewer.viewer.internal.Messages;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.internal.util.CasHelper;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.themes.ITheme;

/**
 * 
 * 
 * Note: CASViewControl will listen to AnnotatedTextSectionPart
 */
public class CASViewControl implements  // SelectionListener,
        IObjectSelectionListener, ICASViewControl {

    private ICASObjectView casViewObject;
    protected TypeTree typesystemTree;
    private TypeSystemStyle typesystemStyle;
    private String typesystemStyleFile;

    private Form2Panel form2Panel;
    private IManagedForm managedForm = null;
    private FormToolkit toolkit;
    private Composite parent;
    private Composite casViewControl;

    // Style for SashForm
    private int casViewControlSashFormStyle = SWT.VERTICAL;
    private int typeSystemSashFormStyle = SWT.VERTICAL;
    private int indexRepositorySashFormStyle = SWT.VERTICAL;

    private AnnotatedTextSectionPart docSection;
    private TypesTreeSectionPart typeTreeSection;
    private CasIndexRepoSectionPart indexRepositorySection;
    private FSIndexSectionPart fsIndexSection;
    private FSSectionPart fsSectionPart = null;
    private String inputFileName;
    private List<String> preSelectedTypeNames;

    // Preferences
    private boolean prefHideFeaturesInTypeSystem = true;
    private boolean prefHideNoValueFeature = true;
    private boolean prefFlatLayout4Types;
    private boolean prefFlatLayout4FS;

    private IWorkbenchPart workbenchPart;
    private IWorkbenchPartSite workbenchPartSite;

    protected static class SashFormWeightWatcher extends ControlAdapter {
        static final private int SECTION_MIN_SIZE = 40;
        
        private SashForm sashForm;
        private SectionPart first;
        private SectionPart second;
        private boolean isVertical;
        private int[] sashWeights = new int[2];
        private int collapseCOunt = 0;
        private int firstWeight;
        private int secondWeight;
        int total;
        int min;
        private int firstMinSize;
        private int secondMinSize;
        
        protected SashFormWeightWatcher (SashForm sashForm, SectionPart first, SectionPart second, boolean isVertical) {
            this.sashForm = sashForm;
            this.first = first;
            this.second = second;
            this.isVertical = isVertical;
        }

        /* (non-Javadoc)
         * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse.swt.events.ControlEvent)
         */
        @Override
        public void controlResized(ControlEvent e) {
            super.controlResized(e);
            sashWeights = sashForm.getWeights();
            
            total = sashWeights[0] + sashWeights[1];
            Point p = sashForm.getSize();
            int inDots;
            if (isVertical) {
                inDots = p.y;
            } else {
                inDots = p.x;
            }
            if (inDots != 0) {
                min = (total*SECTION_MIN_SIZE)/inDots;
            }
            // Trace.err("inDots: " + inDots + " ; min: " + min
            //        + " ; sashWeights: " + sashWeights[0] + " : " + sashWeights[1]);
        }
        
        protected void expansionStateChanged (boolean expand, SectionPart child) {
            if (expand) {
                --collapseCOunt;
            } else {
                ++collapseCOunt;
            }
            int[] weights; //  = sashForm.getWeights();
            if (child == first) {
                if (expand) {
                    // "first" is expanded
                    if (collapseCOunt > 0) {
                        // Another child is collapsed
                        weights = new int[] { total - min, min };
                    } else {
                        // Restore weights
                        weights = sashForm.getWeights();
                        if (firstMinSize == getSizeForSash(isVertical, 
                                first.getSection().getSize()) ) { 
                            // Sash is NOT moved while I am collapsed
                            weights = sashWeights;
                        }
                    }
                } else {
                    // "first" is collapsed
                    if (collapseCOunt > 1) {
                        // Another child is ALSO collapsed
                        // Restore weights
                        weights = sashWeights;
                    } else{
                        sashWeights = sashForm.getWeights();
                        weights = new int[] { min, total - min };                         
                    }
                }
            } else if (child == second) {
                if (expand) {
                    // "second" is expanded
                    if (collapseCOunt > 0) {
                        // Another child is collapsed
                        weights = new int[] { min, total - min };
                    } else {
                        // Restore weights
                        weights = sashForm.getWeights();
                        if (secondMinSize == getSizeForSash(isVertical, 
                                second.getSection().getSize())) { 
                            // Sash is NOT moved while I am collapsed
                            weights = sashWeights;
                        }
                    }
                } else {
                    // "second" is collapsed
                    if (collapseCOunt > 1) {
                        // Another child is ALSO collapsed
                        // Restore weights
                        weights = sashWeights;
                    } else{
                        sashWeights = sashForm.getWeights();
                        weights = new int[] { total - min, min };                        
                    }
                }
            } else {
                return;
            }
            // Trace.err("weights: " + weights[0] + " : " + weights[1]);
            sashForm.setWeights(weights);
            
            // Remember the size if child is collapsed
            if (!expand) {                
                if (child == first) {                    
                    firstMinSize = getSizeForSash(isVertical, first.getSection().getSize());   
                } else {
                    secondMinSize = getSizeForSash(isVertical, second.getSection().getSize());                    
                }                
            }
        }
        
        private int getSizeForSash (boolean vertical, Point size) {
            if (vertical) {
                return size.y;
            } else {
                return size.x;
            }
        }
    }
    
    /** ********************************************************************** */

    protected CASViewControl(IManagedForm managedForm, Composite parent,
            ICASObjectView aCASViewObject,
            TypeSystemStyle tsStyle) {
        super();
        this.managedForm = managedForm;
        this.toolkit = managedForm.getToolkit();
        this.parent = parent;
        this.casViewObject = aCASViewObject;
        this.typesystemStyle = tsStyle;
    }

    protected CASViewControl(IManagedForm managedForm, Composite parent,
            CAS aTCAS,
            TypeSystemStyle tsStyle) {
        super();
        this.managedForm = managedForm;
        this.toolkit = managedForm.getToolkit();
        this.parent = parent;
        this.typesystemStyle = tsStyle;
    }

    public IAction getGlobalAction(String id) {
        return docSection.getGlobalAction(id);
    }

    /** ********************************************************************** */

    // public SourceViewer getSourceViewer () {
    // return (SourceViewer) sourceViewer;
    // }
    //
    // public void setSourceViewer (SourceViewer sourceViewer) {
    // this.sourceViewer = sourceViewer;
    // }
    static public CASViewControl createInstance(IWorkbenchPart part,
            IWorkbenchPartSite site, IManagedForm managedForm,
            Composite parent, ICASObjectView aCASViewObject,
            TypeSystemStyle tsStyle, String tsStyleFileName) {
        CASViewControl instance = new CASViewControl(managedForm, parent,
                aCASViewObject, tsStyle);
        instance.workbenchPartSite = site;
        instance.workbenchPart = part;
        if (tsStyleFileName != null) {
            instance.typesystemStyle = TypeSystemStyle.createStyleForTypesHavingAnnotations(aCASViewObject, 
                new File(tsStyleFileName));
        } else {
//            instance.typesystemStyle = tsStyle;
            instance.typesystemStyle = TypeSystemStyle.createStyleForTypesHavingAnnotations(aCASViewObject, 
                    null);
        }
        instance.typesystemStyleFile = tsStyleFileName;
        
        return instance;
    }

    /**
     * Set CAS Control's SashForm style to vertical (SWT.VERTICAL) or horizontal
     * (SWT.HORIZONTAL)
     * 
     * @param style
     * @return void
     */
    public void setCASViewControlSashFormStyle(int style) {
        casViewControlSashFormStyle = style;
    }

    /**
     * Set type system's SashForm style to vertical (SWT.VERTICAL) or horizontal
     * (SWT.HORIZONTAL)
     * 
     * @param style
     * @return void
     */
    public void setTypeSystemSashFormStyle(int style) {
        typeSystemSashFormStyle = style;
    }

    /**
     * Set CAS Control's SashForm style to vertical (SWT.VERTICAL) or horizontal
     * (SWT.HORIZONTAL)
     * 
     * @param style
     * @return void
     */
    public void setIndexRepositorySashFormStyle(int style) {
        indexRepositorySashFormStyle = style;
    }

    /** ********************************************************************** */

    public void dispose() {
        if (casViewControl != null) {
            casViewControl.dispose();
            casViewControl = null;
        }
    }

    public Composite getControl() {
        return casViewControl;
    }

    public void setInput(ICASObjectView icasViewObject, String title) {
        this.casViewObject = icasViewObject;

        docSection.setInput(casViewObject, "No title");
        indexRepositorySection.setInput(casViewObject, null);

        showTypeSystem();        
    }

    // Called from CASViewPage.refreshTypeSystemStyle ()
    public void refreshTypeSystemStyle() {
        // Get currently selected type's names
        List<String> selectedTypeNames = typeTreeSection.getSelectedTypeNames();
        
        showTypeSystem();
        if (selectedTypeNames != null && selectedTypeNames.size() > 0) {
            setTypeSelectionByName(selectedTypeNames, true);
        }
        indexRepositorySection.refreshTypeSystemStyle();
        fsIndexSection.refreshTypeSystemStyle();
        docSection.refreshTypeSystemStyle(AnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO);

        selectedTypeNames = typesystemStyle.getPreSelectedList();
        if (selectedTypeNames != null && selectedTypeNames.size() > 0) {
            setTypeSelectionByName(selectedTypeNames, true);
        }
    }

    public void setStatusLineManager(IStatusLineManager statusLine) {
        docSection.setStatusLineManager(statusLine);
    }

    /** ********************************************************************** */

    // Note: If "types" is null, deselect ALL
    public void setTypeSelectionByName(List<String> types, boolean selection) {
        if (casViewObject == null) {
            preSelectedTypeNames = types;
            return;
        }
        // Book-keeping
        casViewObject.setTypeSelectionByName(types, selection);

        typeTreeSection.setTypeSelectionByName(types, selection);
        if (types != null) {
            for (int i = 0; i < types.size(); ++i) {
                TypeNode node = typeTreeSection.getTypeNode(types.get(i));
                if (node != null) {
                    docSection.showAnnotationsForTypeName(((TypeDescription) node
                            .getObject()).getName(), selection, false);
                }
            }
            docSection
                    .refreshTypeSystemStyle(AnnotatedTextSectionPart.TAB_VIEW_ANNOTATIONS);
        } else {
            // Hide all annotations
            docSection.hideAllAnnotationsForView(
                    AnnotatedTextSectionPart.TAB_VIEW_ANNOTATIONS, true);
        }
    }

    public List getSelectedTypeNames() {
        return typeTreeSection.getSelectedTypeNames();
    }

    public void setInitiallySelectedTypes(String[] typeNames) {
        List<String> list = new ArrayList<String>(typeNames.length);
        for (int i = 0; i < typeNames.length; ++i) {
            list.add(typeNames[i]);
        }
        setTypeSelectionByName(list, true);
    }

    /** ********************************************************************** */

    /* (non-Javadoc)
     * @see org.apache.uima.casviewer.ui.internal.model.ICASViewControl#createContents()
     */
    public void createContents() {
        casViewControl = FormSection.createGridLayoutContainer(toolkit, parent,
                1, 2, 2);
        casViewControl.setLayoutData(new GridData(GridData.FILL_BOTH));
        casViewControl.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                managedForm.reflow(true);
            }
        });

        form2Panel = setup2ColumnLayout(casViewControl, 50, 50);
        
        docSection = AnnotatedTextSectionPart.createInstance(
                typesystemStyle, workbenchPart, workbenchPartSite, managedForm,
                form2Panel.left, Section.TITLE_BAR | Section.DESCRIPTION,
                Messages.getString("DocSection.Title"), Messages
                        .getString("DocSection.Description"), false);
        docSection.createContents();
        
        AnnotatedTextComposite annotatedTextComposite = AnnotatedTextComposite.createInstance((Composite)docSection.getSection().getClient(), 
                typesystemStyle, true);
        docSection.setAnnotatedTextComposite(annotatedTextComposite);
        
        docSection.addSelectionListener(this); // Will trigger "objectSelected"
        // docSection.setStatusLineManager(GenericCasViewer.getStatusLineManager());
        // sourceViewer = docSection.getSourceViewer();

        createTabFolderForTypesAndIndexes(managedForm, form2Panel.right);

        // If we have input, show the contents
        if (casViewObject != null && casViewObject.getCurrentView() != null) {
            docSection.setInput(casViewObject, "No title");
            indexRepositorySection.setInput(casViewObject, null);

            showTypeSystem();

            // Pre-select types
            if (typesystemStyle.getPreSelectedList().size() > 0) {
                setTypeSelectionByName(typesystemStyle.getPreSelectedList(), true);
            }
//            if (testCASDiff) {
//                diffTypeTreeSection.setInput(casViewObject, typesystemStyle);
//            }
        }
    } // createContents

    protected Form2Panel setup2ColumnLayout(Composite parent, int w1, int w2) {
        parent.setLayout(new GridLayout(1, false)); // this is required !
        // form.setLayoutData(new GridData(GridData.FILL_BOTH)); // does nothing
        Composite xtra = toolkit.createComposite(parent);
        xtra.setLayout(new GridLayout(1, false));
        xtra.setLayoutData(new GridData(GridData.FILL_BOTH));
        Control c = xtra.getParent();
        while (!(c instanceof ScrolledComposite))
            c = c.getParent();
        ((GridData) xtra.getLayoutData()).widthHint = c.getSize().x;
        ((GridData) xtra.getLayoutData()).heightHint = c.getSize().y;
        final SashForm sashForm = new SashForm(xtra, SWT.HORIZONTAL);
        // toolkit.adapt(sashForm, true, true); // makes the bar invisible
        // (white)

        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH)); // needed
        // sashForm.setLayout(new GridLayout(1, false));
        // form.setLayoutData(new GridData(GridData.FILL_BOTH)); //does nothing

        final Composite leftPanel = newnColumnSection(sashForm, 1);
        ((GridLayout) leftPanel.getLayout()).marginHeight = 5;
        ((GridLayout) leftPanel.getLayout()).marginWidth = 5;
        final Composite rightPanel = newnColumnSection(sashForm, 1);
        ((GridLayout) rightPanel.getLayout()).marginHeight = 5;
        ((GridLayout) rightPanel.getLayout()).marginWidth = 5;
        sashForm.setWeights(new int[] { w1, w2 });
        float leftPanelPercent = (float) w1 / (float) (w1 + w2);
        float rightPanelPercent = (float) w2 / (float) (w1 + w2);
        // sashForm.addControlListener(new ControlAdapter() {
        // public void controlResized(ControlEvent e) {
        // leftPanel.layout();
        // rightPanel.layout();
        // }
        // });

        return new Form2Panel(xtra, leftPanel, rightPanel);
    }

    protected Composite newnColumnSection(Composite parent, int cols) {
        Composite section = toolkit.createComposite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = cols;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        section.setLayout(layout);
        section.setLayoutData(new GridData(GridData.FILL_BOTH));
        return section;
    }

    private void createTabFolderForTypesAndIndexes(
            final IManagedForm managedForm, Composite sashForm) {
        final Composite compTop = FormSection.createGridLayoutContainer(
                toolkit, sashForm, 1, 2, 2);
        // ((GridLayout) compTop.getLayout()).makeColumnsEqualWidth = true;
        // compTop.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL,
        // GridData.VERTICAL_ALIGN_FILL, true, true));
        compTop.setLayoutData(new GridData(GridData.FILL_BOTH));
        toolkit.paintBordersFor(compTop);

        final CTabFolder tabFolder = new CTabFolder(compTop, SWT.FLAT
                | SWT.BORDER | SWT.TOP);
        tabFolder.setUnselectedCloseVisible(false);
        // tabFolder.setEnabled(false);
        // tabFolder.setMaximizeVisible(true);
        // tabFolder.setMinimizeVisible(true);
        // tabFolder.marginHeight = 10;
        tabFolder.setSimple(false);
        toolkit.adapt(tabFolder, true, true);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        tabFolder.setLayoutData(gd);

        Display display = sashForm.getDisplay();
        ITheme theme = PlatformUI.getWorkbench().getThemeManager()
                .getCurrentTheme();
        ColorRegistry colorRegistry = theme.getColorRegistry();

        compTop.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        // tabFolder.setBackground(compTop.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        drawGradient(tabFolder, colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR),
                new Color[] {colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START),
                             colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END) },
                new int[] { theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT) },
                theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL));

        tabFolder.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                int currentView = -1;
                int index = tabFolder.getSelectionIndex();
                if (index >= 0) {
                    if (index == 0) {
                        currentView = AnnotatedTextSectionPart.TAB_VIEW_ANNOTATIONS;
                    } else if (index == 1) {
                        currentView = AnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO;
                    } else if (index == 2) {
                        currentView = AnnotatedTextSectionPart.TAB_VIEW_CASDIFF;
                    }
                    if (currentView != -1) {
                        docSection.switchViewForAnnotations(currentView);
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        compTop.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                // Trace.err("compTop");
                // TODO Resize
                // tabFolder.layout(true, true);
                // managedForm.reflow(true);
                // managedForm.refresh();
            }
        });

        /** ****************************************************************** */
        /* Create Tab Item for "Type System" */
        /** ****************************************************************** */
        createCASTypesTab(managedForm, tabFolder);
        createIndexRepositoryTab(managedForm, tabFolder);
//        if (testCASDiff) {
//            createCASDiffTab(managedForm, tabFolder);
//        }
        tabFolder.setSelection(0);
    }
    
    public void createCASTypesTab(final IManagedForm mForm, CTabFolder tabFolder) {
        CTabItem _tabItem = new CTabItem(tabFolder, SWT.NONE);
        _tabItem.setText(Messages.getString("TypesAndAnnotationsTab")); //$NON-NLS-1$       

        /** ****************************************************************** */

        final SashForm tabBody = new SashForm(tabFolder,
                typeSystemSashFormStyle);
        tabBody.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // tabBody.addControlListener(new ControlAdapter() {
        // public void controlResized(ControlEvent e) {
        // Trace.err("tabBody");
        // tabBody.layout(true, true);
        // }
        // });

        // Composite tabBody = FormSection.createGridLayoutContainer (toolkit,
        // tabFolder,
        // 1, 10, 15);
        // GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        // tabBody.setLayoutData (gd);
        // toolkit.paintBordersFor(tabBody);
        _tabItem.setControl(tabBody);

        typeTreeSection = TypesTreeSectionPart.createInstance(this, mForm,
                tabBody, Section.TITLE_BAR | Section.TWISTIE
                        | Section.DESCRIPTION, Messages
                        .getString("AnnotationTreeSection.Title"), Messages
                        .getString("AnnotationTreeSection.Description"), 5, 8,
                false, prefHideFeaturesInTypeSystem);
        typeTreeSection.setViewerLayoutToFlatOrTree(prefFlatLayout4Types);
        // typeTreeSection.addSelectionListener(this);

        // typeTreeSection.getSection().addControlListener(new ControlAdapter()
        // {
        // public void controlResized(ControlEvent e) {
        // Trace.err("typeTreeSection");
        // mForm.reflow(true);
        // mForm.refresh();
        // }
        // });

        fsSectionPart = FSSectionPart.createInstance(mForm, tabBody,
                Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION,
                Messages.getString("FSSection.Title"), Messages
                        .getString("FSSection.Description"),
                        typesystemStyle);

        final SashFormWeightWatcher watcher = new SashFormWeightWatcher(tabBody, typeTreeSection, fsSectionPart, true);
        tabBody.addControlListener(watcher);
        typeTreeSection.getSection().addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                managedForm.reflow(true);
                watcher.expansionStateChanged(e.getState(), typeTreeSection);
            }
        });
        fsSectionPart.getSection().addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                managedForm.reflow(true);
                watcher.expansionStateChanged(e.getState(), fsSectionPart);
            }
        });

    } // createCASTypesTab

    public void createIndexRepositoryTab(IManagedForm mForm,
            CTabFolder tabFolder) {
        CTabItem _tabItem = new CTabItem(tabFolder, SWT.NONE);
        _tabItem.setText(Messages.getString("IndexRepositoryTab")); //$NON-NLS-1$  

        /** ****************************************************************** */

        final SashForm tabBody = new SashForm(tabFolder,
                indexRepositorySashFormStyle);
        tabBody.setLayoutData(new GridData(GridData.FILL_BOTH));
        _tabItem.setControl(tabBody);

        indexRepositorySection = CasIndexRepoSectionPart.createInstance(
                mForm, tabBody, Section.TITLE_BAR | Section.TWISTIE
                        | Section.DESCRIPTION, Messages
                        .getString("IndexRepositorySection.Title"), Messages
                        .getString("IndexRepositorySection.Description"),
                        typesystemStyle);
        indexRepositorySection.addSelectionListener(this); // Will trigger "objectSelected"

        fsIndexSection = FSIndexSectionPart.createInstance(this, mForm,
                tabBody, Section.TITLE_BAR | Section.TWISTIE
                        | Section.DESCRIPTION, Messages
                        .getString("FSIndexSection.Title"), Messages
                        .getString("FSIndexSection.Description"),
                        prefHideNoValueFeature, typesystemStyle);
        fsIndexSection.setViewerLayoutToFlatOrTree(prefFlatLayout4FS);

        
        final SashFormWeightWatcher watcher = new SashFormWeightWatcher(tabBody, 
                indexRepositorySection, fsIndexSection, true);
        tabBody.addControlListener(watcher);
        indexRepositorySection.getSection().addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                watcher.expansionStateChanged(e.getState(), indexRepositorySection);
            }
        });
        fsIndexSection.getSection().addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                watcher.expansionStateChanged(e.getState(), fsIndexSection);
            }
        });
        
    } // createIndexRepositoryTab


    /* (non-Javadoc)
     * @see org.apache.uima.casviewer.ui.internal.util.IObjectSelectionListener#objectSelected(java.lang.Object, java.lang.Object)
     */
    public void objectSelected(Object source, Object selectedObject) {
        if (source instanceof AnnotatedTextSectionPart) {
            fsSectionPart.setInput(selectedObject, "");
            
        } else if (source instanceof CasIndexRepoSectionPart) {
            fsIndexSection.setInput(selectedObject, "");
        }
    }

    /** ********************************************************************** */

    /**
     * Sets the gradient for the selected tab
     * 
     * @param fgColor
     * @param bgColors
     * @param percentages
     * @param vertical
     */
    protected void drawGradient(CTabFolder tabFolder, Color fgColor,
            Color[] bgColors, int[] percentages, boolean vertical) {
        tabFolder.setSelectionForeground(fgColor);
        tabFolder.setSelectionBackground(bgColors, percentages, vertical);
    }

    /** ********************************************************************** */

    /**
     * Will be called when node in "typeTreeSection" is selected
     */
//    public void widgetSelected(SelectionEvent event) {
//        Trace.err();
//        Object obj = ((TreeItem) event.item).getData();
//        if (obj instanceof TypeNode) {
//            if (((TypeNode) obj).getObjectType() != IItemTypeConstants.ITEM_TYPE_TYPE) {
//                ((TreeItem) event.item).setChecked(true);
//            } else {
//                // If this type doesn't have annotations --> DO NOTHING
//                if (((TypeNode) obj).getBgColor() != null) {
//                    docSection.showAnnotationsForTypeName(
//                            ((TypeDescription) ((TypeNode) obj).getObject())
//                                    .getName(), ((TreeItem) event.item)
//                                    .getChecked(), true);
//                }
//            }
//        }
//    }
//
//    public void widgetDefaultSelected(SelectionEvent e) {
//    }

    /** ********************************************************************** */

    protected void showTypeSystem() {
        // Create Type System Tree
        typesystemTree = casViewObject.getTypeTree();

        // ONLY show background color to node of type having annotations.
        // The type itself has the BG color from default style or imported style.
        List<Type> typesHavingAnnotations = casViewObject.getTypesHavingAnnotations(); 
        if (typesHavingAnnotations != null) {
            for (Type t: typesHavingAnnotations) {
                TypeNode node = typesystemTree.getTypeNode(t.getName());
                if (node != null) {
                    // Assign color to node
                    TypeStyle s = typesystemStyle.getTypeStyle(t.getName());
                    if (s.isHidden()) {
                        node.setHidden(true);
                    }
                    node.setBgColor(s.getBackground());
                }
            }
        }

        // Set total annotations for each type's branch in the tree
        List list = typesystemTree.getTypeListFromHierarchy(true, false);
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i) instanceof TypeDescription) {
                    Type type = casViewObject
                            .getTypeFromTypeSystem(((TypeDescription) list.get(i))
                                    .getName());
                    if (type != null) {
                        TypeNode node = typesystemTree.getTypeNode(type.getName());
                        if (node == null) {
                            // Trace.trace("NOT in tree: " +
                            // type.getShortName());
                            continue;
                        }
                        // Set total FS for this type
                        FSIndex fsIndex = CasHelper.getIndexForType(
                                casViewObject.getCurrentView(), type);
                        if (fsIndex != null) {
                            // This number includes FS from sub-types
                            node.setFsTotal(fsIndex.size());
                        }
                        node.setFsCount(CasHelper.getTotalFSForType(
                                casViewObject.getCurrentView(), type));
                    }
                }
            }
        }

        // Update Type System Section
        // typeTreeSection.setTypeSystemStyle(casViewObject.getTypeSystemStyle());
        typeTreeSection.setInput(typesystemTree, null);
    } // showTypeSystem

    /** ********************************************************************** */

    public void onActionInvocation(Object source, int flags, Object obj) {
        if (flags == ICASViewControl.ACTION_DESELECT_ALL) {
            if (source instanceof TypesTreeSectionPart) {
                setTypeSelectionByName(null, false);

            } else if (source instanceof FSIndexSectionPart) {
                docSection.hideAllAnnotationsForView(
                        AnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO, false);
            }
        } else if (flags == ICASViewControl.ACTION_SET_FILTERS) {
            // TypeTree tsTree = casViewObject.getTypeTree();
            // SelectTypesWizard uWizard = new SelectTypesWizard(tsTree, "");
//            SelectFromClasspathWizard uWizard = new SelectFromClasspathWizard(null, "");
//            WizardDialog wd = new WizardDialog(parent.getShell(), uWizard);
//            wd.create();
//            //wd.getShell().setSize(450, 600);
//            int result = wd.open();
//            if (result == Window.OK) {
//                // return uWizard.getSelectedTypes ();
//            }      
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.uima.casviewer.ui.internal.model.ICASViewControl#setListForAnnotationView(int, java.util.List)
     */
    public void setAnnotationObjectListForView (int viewIndex, List<AnnotationObject> list) {
        docSection.setAnnotationObjectListForView(viewIndex, list);
    }
    
    public void showAnnotationsForView (int forView) {
        docSection.showAnnotationsForView(forView);
    }
    
    /* (non-Javadoc)
     * @see org.apache.uima.casviewer.ui.internal.model.ICASViewControl#showAnnotationsByTypenameForView(java.util.List, int)
     */
    public void showAnnotationsByTypenameForView (List<String> typeNames, int forView) {
        docSection.hideAllAnnotationsForView(forView, false);
        for (String name: typeNames) {
            docSection.showAnnotationsForTypeName(name, true, false);
        }
        docSection.showAnnotationsForView(forView);
    }
     
    /**
     * Show "additional" annotations for the current view
     * 
     * @param viewIndex
     * @param list
     * @return void
     */
    public void showMoreAnnotationObjects (int viewIndex, List<AnnotationObject> list) {
        docSection.showMoreAnnotationObjects(viewIndex, list);
    }

    
    /** ********************************************************************** */

    public void setPreference(int attribute, boolean value) {
        if (attribute == ICASViewControl.PREF_HIDE_FEATURES_IN_TYPE_SYSTEM) {
            // Hide features in type system's tree
            prefHideFeaturesInTypeSystem = value;
            if (typeTreeSection != null) {
                typeTreeSection.hideFeature(value);
            }
        } else if (attribute == ICASViewControl.PREF_HIDE_FEATURE_NO_VALUE) {
            // Hide feature having no value
            prefHideNoValueFeature = value;
            if (fsIndexSection != null) {
                fsIndexSection.hideNoValueFeature(value);
            }
        } else if (attribute == ICASViewControl.PREF_FLAT_LAYOUT_FOR_TYPES) {
            prefFlatLayout4Types = value;
            if (typeTreeSection != null) {
                typeTreeSection.setViewerLayoutToFlatOrTree(value);
            }
        } else if (attribute == ICASViewControl.PREF_FLAT_LAYOUT_FOR_FS) {
            prefFlatLayout4FS = value;
            if (fsIndexSection != null) {
                fsIndexSection.setViewerLayoutToFlatOrTree(value);
            }
        }
    }

    public void setPreference(int attribute, String value) {
    }

    public void enableTypes(boolean enable, List<String> typeNames) {
        // TODO Auto-generated method stub
        
    }

    /*************************************************************************/
    
    

//    private boolean testCASDiff = false;
//    private DiffTypesTreeSectionPart diffTypeTreeSection;
//    public void createCASDiffTab(final IManagedForm mForm, CTabFolder tabFolder) {
//        CTabItem _tabItem = new CTabItem(tabFolder, SWT.NONE);
//        _tabItem.setText("CAS Diff"); //$NON-NLS-1$       
//
//        /** ****************************************************************** */
//
//        final SashForm tabBody = new SashForm(tabFolder,
//                typeSystemSashFormStyle);
//        tabBody.setLayoutData(new GridData(GridData.FILL_BOTH));        
//        _tabItem.setControl(tabBody);
//
//        diffTypeTreeSection = DiffTypesTreeSectionPart.createInstance(this, mForm,
//                tabBody, Section.TITLE_BAR | Section.TWISTIE
//                        | Section.DESCRIPTION, Messages
//                        .getString("AnnotationTreeSection.Title"), Messages
//                        .getString("AnnotationTreeSection.Description"), 5, 8,
//                false, prefHideFeaturesInTypeSystem);
//        diffTypeTreeSection.setViewerLayoutToFlatOrTree(prefFlatLayout4Types);
//
//
//
//
//    } // createCASDiffTab
    
    
} // CASViewControl
