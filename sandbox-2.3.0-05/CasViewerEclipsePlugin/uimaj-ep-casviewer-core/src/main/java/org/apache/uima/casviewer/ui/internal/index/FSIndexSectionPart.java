package org.apache.uima.casviewer.ui.internal.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.CASObjectView;
import org.apache.uima.casviewer.core.internal.UFSIndex;
import org.apache.uima.casviewer.core.internal.UFeature;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.casviewer.ui.internal.annotations.FeatureStructureViewerFilter;
import org.apache.uima.casviewer.ui.internal.document.AbstractAnnotatedTextSectionPart;
import org.apache.uima.casviewer.ui.internal.document.AnnotatedTextSectionPart;
import org.apache.uima.casviewer.ui.internal.model.ICASViewControl;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.casviewer.ui.internal.util.TreeViewerHelper;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Tree Structure:
 *  FS Index (root) --> model: TreeNode
 *    - FS[0]       --> UFeatureStructure
 *    - FS[1]
 *    ...
 *
 */
public class FSIndexSectionPart extends AbstractSectionPart {

    private ICASViewControl              casViewControl;
    private FSIndexContentProvider  contentProvider;
    private FSIndexLabelProvider    labelProvider;
    private Tree                    tree = null;
    private CheckboxTreeViewer      treeViewer = null;
    private FeatureStructureViewerFilter filter;
    private TypeSystemStyle    typesystemStyle;
    
    // List of AnnotationObject used for showing FS in document
    private List<AnnotationObject> annotationObjectList = new ArrayList<AnnotationObject>();
    
    // Preferences
    private boolean hideNoValueFeature =  false;
    private boolean prefFlatLayout4Features = false;
    
    /*************************************************************************/
    
    protected FSIndexSectionPart(Section section) {
        super(section);
    }
    
    protected FSIndexSectionPart (ICASViewControl control,
            IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description,
            boolean hide)
    {
        super(managedForm, parent, section, style, title, description);
        this.hideNoValueFeature = hide;    
        casViewControl = control;
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!        
    }
    
    /*************************************************************************/
    
    /**
     * When the view is "Flat Structure", hide feature having no value.
     * 
     * @param hide
     * @return void
     */
    public void hideNoValueFeature (boolean hide)
    {
        // Trace.err("Hide feature having no value");
        if (hideNoValueFeature == hide) {
            return;
        }
        hideNoValueFeature = hide;
        if (contentProvider != null) {
            contentProvider.hideNoValueFeature(hide);
            labelProvider.hideNoValueFeature(hide);
            TreeViewerHelper.refreshWithCheckedState(treeViewer, true);
        }
    }
    
    /**
     * Hide all feature structures of types having the specified names
     * 
     * @param typeNames
     * @return void
     */
    public void setHiddenTypeNames (List<String> typeNames) {
        filter.setHiddenTypeNames(typeNames);
        treeViewer.refresh();
    }
     
    public void setViewerLayoutToFlatOrTree (boolean flat)
    {
        if (prefFlatLayout4Features == flat) {
            return;
        }
        prefFlatLayout4Features = flat;
        if (contentProvider != null) {
            contentProvider.showOneLineView(flat);
            labelProvider.showOneLineView(flat);
            TreeViewerHelper.refreshWithCheckedState(treeViewer, true);
        }
    }

    /*************************************************************************/
    
//    protected static IPreferenceStore store() {
//        return CasViewerCorePlugin.getDefault().getPreferenceStore();
//    }

    static public FSIndexSectionPart createInstance (ICASViewControl control,
            IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            boolean hide, TypeSystemStyle tsStyle)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 5, 8,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
                
        FSIndexSectionPart sectionPart = new FSIndexSectionPart (control,
                managedForm, parent, section,
                style, title, description, hide);
        sectionPart.typesystemStyle = tsStyle;
        sectionPart.createContents();
                
        return sectionPart;
    }
        
    /*************************************************************************/
    
    public Section createContents () 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = getSection();
        // Expand | Collapse
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                //  parent.layout(true);
                managedForm.reflow(true);
            }
        });        
        section.setExpanded(true);

        ///////////////////////////////////////////////////////////////////////
        
        // Create ToolBar
        Composite sectionToolbarComposite = FormSection.createGridLayoutContainer (toolkit, section,
                3, 0, 0);

        section.setTextClient(sectionToolbarComposite);
        
        ///////////////////////////////////////////////////////////////////////
        
        // Create Composite
        Composite client = FormSection.createGridLayoutContainer (toolkit, section,
                1, 0, 0);
        client.setLayoutData(new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING));
        
        toolkit.paintBordersFor(client);        
        section.setClient(client);   
        
        /*********************************************************************/    
        
        // Create Tree for FSIndex
        tree = toolkit.createTree(client, SWT.CHECK);
        GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 160;
        tree.setLayoutData(gd);
        
        // Create Viewer for Tree
        // treeViewer = new CheckboxTreeViewer(tree);
        treeViewer = new ContainerCheckedTreeViewer(tree);
        treeViewer.setUseHashlookup(true);
        treeViewer.addCheckStateListener(new ICheckStateListener() {
            
            /**
             * Tree Structure:
             *  FS Index (root) --> model: TreeNode
             *    - FS[0]       --> UFeatureStructure
             *    - FS[1]
             *    ...
             *    
             *    Note: The value of feature of a FS can be "another" FS....
             *          which can create a "cycle".
             *          Checking the tree node can create a stack overflow. 
             *
             */
            public void checkStateChanged(CheckStateChangedEvent event) {
                Object obj = event.getElement();
                if (obj instanceof UFeature) {
                    return;
                }                    
                // Trace.err("Check obj:" + obj.getClass().getName());
                
                // TODO Optimize by creating a list of "showed" AnnotationObjects
                for (AnnotationObject annot: annotationObjectList) {
                    annot.show = false;
                }
                
                //
                // Collect and set AnnotationObjects to show
                //
                // Prevent Stack Overflow by NOT using treeViewer.getCheckedElements()
                TreeItem itemRoot = treeViewer.getTree().getItem(0); // get root
                
                // Get all checked FS under root
                TreeItem[] children = itemRoot.getItems();
                for (int i=0; i<children.length; ++i) {
                    // Collect only "real" checked items (NOT grayed items)
                    if ( ! children[i].getGrayed() ) {
                        AnnotationObject annot = (AnnotationObject) ((UFeatureStructure)children[i].getData()).getUserData();
                        if (annot != null) {
                            annot.show = children[i].getChecked();                              
                        } else {
                            // FIXME Remove Trace.bug
                            Trace.bug("NO associated annotation");
                        }
                    }
                }
                
                casViewControl.showAnnotationsForView(AbstractAnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO/*, fsDocumentAnnotList*/);                
            }       
        });
        
        // Set Content/Label Provider
        contentProvider = new FSIndexContentProvider(prefFlatLayout4Features);
        labelProvider   = new FSIndexLabelProvider(typesystemStyle);
        contentProvider.hideNoValueFeature(hideNoValueFeature);
        labelProvider.showOneLineView(prefFlatLayout4Features);
        labelProvider.hideNoValueFeature(hideNoValueFeature);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider); 
        filter = new FeatureStructureViewerFilter();
        treeViewer.addFilter(filter);
        
        createSectionToolbar (toolkit, section, sectionToolbarComposite, 
                treeViewer);
        
        return section;
    }    
    
    protected void createSectionToolbar (FormToolkit toolkit, Section section, 
            Composite toolbarComposite, final CheckboxTreeViewer treeViewer)
    {
        AbstractSectionPart.createExpandAllMenu (toolkit, section, toolbarComposite, 
                treeViewer);
        AbstractSectionPart.createCollapseAllMenu(toolkit, section, toolbarComposite, 
                treeViewer);
        
        // Create Menu
        final Menu subMenu = new Menu (toolbarComposite); 
        MenuItem item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Deselect All");
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                // TODO Optimize by creating a list of "showed" AnnotationObjects
                for (AnnotationObject annot: annotationObjectList) {
                    annot.show = false;
                }
                casViewControl.showAnnotationsForView(AbstractAnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO/*, fsDocumentAnnotList*/);
                treeViewer.setAllChecked(false);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        new MenuItem(subMenu, SWT.SEPARATOR);

        item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Show Flat Structure");
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                // Trace.trace("Show Input Types");
//                _typeTreeViewerFiler.setFilterFlags(TypeSystemViewerFilter.FILTER_TYPE_SHOW_INPUT, true);
                contentProvider.showOneLineView(true);
                labelProvider.showOneLineView(true);
                treeViewer.refresh(true);
                treeViewer.expandToLevel(2);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub                
            }
        });
        
        item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Show Tree Structure");
        item.setData(null);
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                // Trace.trace("Show Output Types");
//                _typeTreeViewerFiler.setFilterFlags(TypeSystemViewerFilter.FILTER_TYPE_SHOW_OUTPUT, true);
                contentProvider.showOneLineView(false);
                labelProvider.showOneLineView(false);
                // _typeTreeViewer.setInput(_typeHierarchy);
                treeViewer.refresh(true);
                treeViewer.expandToLevel(2);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        info.setImage(ImageLoader.getInstance().getImage(ImageLoader.ICON_UI_VIEW_MENU));
        info.setToolTipText("Flat or Tree...");
        info.setBackground(section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                subMenu.setVisible(true);
            }
        });        
                
    }
       
    /*************************************************************************/

    /* (non-Javadoc)
     * 
     * Called from IndexRepositorySectionPart.setSelectedTreeNode when an index
     * is selected
     */
    public void setInput(Object model, String title) {
        // "model" will be UFSIndex
        if (model == null || !(model instanceof UFSIndex)) {
            return;
        }
        // Set Show/Hide attributes of AnnotationObject
        AnnotationObject   annot;
        annotationObjectList.clear();
        UFeatureStructure[] uFSs = ((UFSIndex) model).getUFeatureStructures();
        for (int i=0; i<uFSs.length; ++i) {
            // Is it sub-type of AnnotationFS
            if (uFSs[i].getUimaFeatureStructure() instanceof AnnotationFS) {
                // Lazily creating AnnotationObject
                // Currently, UFeatureStructure.UserData is used to refer to FSNode
                // Used by "FSIndexSectionPart.setInput" to refer to "AnnotationObject"
                if (uFSs[i].getUserData() == null) {
                    annot = CASObjectView.createAnnotationObjectFromFS(uFSs[i].getUimaFeatureStructure(), false);
                    uFSs[i].setUserData(annot);
                } else {
                    annot = (AnnotationObject) uFSs[i].getUserData();
                    annot.setShow(false); // Hide 
                }
                annotationObjectList.add(annot);
            }
        }
        casViewControl.setAnnotationObjectListForView(AnnotatedTextSectionPart.TAB_VIEW_INDEX_REPO, 
                annotationObjectList);
        treeViewer.setInput(model);
        treeViewer.expandToLevel(2);        
    }
    
    /*************************************************************************/

    static protected void createRootIndexTree(CAS aCas) {
        if (aCas != null) {
            FSIndexRepository ir = aCas.getIndexRepository();
            Iterator<String> it = ir.getLabels();
            while (it.hasNext()) {
                String label = it.next();
                FSIndex index1 = ir.getIndex(label);
//                Trace.trace("label: " + label + " ; type: " + index1.getType()
//                        + " ; size: " + index1.size());
                createIndexSubTree(index1.getType(), aCas.getTypeSystem(),
                         label, ir, 1);
            }
        }
    }
    
    static private void createIndexSubTree(Type type, TypeSystem ts,
                                String label, FSIndexRepository ir, int level) 
    {
//        for (int i=0; i<level; ++i) {
//            System.out.print( "    ");
//        }
//        int size = ir.getIndex(label, type).size();
//        System.out.println( "type: " + type + " ; label: " + label + " ; size:" + size);

        Vector types = ts.getDirectlySubsumedTypes(type);
        final int max = types.size();
        for (int i = 0; i < max; i++) {
            createIndexSubTree((Type) types.get(i), ts, label, ir, level+1);            
        }        
    }

    /*************************************************************************/
    
    public void refreshTypeSystemStyle ()
    {
        TreeViewerHelper.refreshWithCheckedState(treeViewer, true);
    }
     
} // FSIndexSectionPart
