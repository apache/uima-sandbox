package org.apache.uima.casviewer.ui.internal.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.casviewer.ui.internal.document.AnnotatedTextSectionPart;
import org.apache.uima.casviewer.ui.internal.model.ICASViewControl;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.casviewer.ui.internal.util.CustomCheckedTreeViewer;
import org.apache.uima.casviewer.ui.internal.util.TreeViewerHelper;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * 	SectionPart that shows the type system of CAS's sofa
 * 
 *  
 */
public class TypesTreeSectionPart extends AbstractSectionPart 
                     //  implements /*ISelectionProvider,*/ SelectionListener 
{    
    final private static int SECTION_STYLE_COMMON  = Section.TWISTIE | Section.EXPANDED;
    
    private ICASViewControl         casViewControl;
    private TypesTreeSectionPart    _instance;
    private CustomCheckedTreeViewer treeViewer = null;
    
//    private TypeSystemViewerFilter  _typeTreeViewerFiler = null;
    private TypesTreeContentProvider contentProvider = null;
    private TypesTreeLabelProvider labelProvider;
    private TypeTree            _typeHierarchy = null;
    
    // List of all selected types. Some types may not have annotations
//    private List                    selectedTypes; 
    
    private TypeNode                _selectedTreeNode = null;   // Selected Node in Type Tree
    private boolean                 _fireTreeSelection = true;
    
    // Preferences
    private boolean         prefHideFeaturesInTypeSystem = true;
    private boolean         prefFlatLayout4Types = false;
    private boolean         prefUseFilteredTree = true;  
    
    /*************************************************************************/

    protected TypesTreeSectionPart (ICASViewControl control,
            IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description,
            boolean fireTreeSelection, boolean hideFeature) 
    {
        super(managedForm, parent, section, style, title, description);
        _instance           = this;
        casViewControl      = control;
//        selectedTypes       = new ArrayList();
        _fireTreeSelection  = fireTreeSelection;
        prefHideFeaturesInTypeSystem = hideFeature;
        
        ///////////////////////////////////////////////////////////////////////
        
        // final SectionPart spart = new SectionPart(section);
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!        
    }
    
    static public TypesTreeSectionPart createInstance (ICASViewControl control,
            final IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            int marginWidth, int marginHeight,
            boolean fireTreeSelection, boolean hideFeature) 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description,
                marginWidth, marginHeight,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                //  parent.layout(true);
                managedForm.reflow(true);
                // section.layout(true);
            }
        });        
        TypesTreeSectionPart sectionPart = new TypesTreeSectionPart (control,
                managedForm, parent, 
                section, style, title, description,
                fireTreeSelection, hideFeature);         
        sectionPart.createContents();
        
        return sectionPart;
    } // createInstance

    static public TypesTreeSectionPart createTableWrapDataInstance (IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            String  sectionKey, 
            // IWorkbenchPartSite  workbenchPartSite, 
            boolean fireTreeSelection, boolean showIOType) 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createTableWrapDataSection(toolkit, parent,
                style, title, description, 10, 0,
                TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB,
                1, 1);
        TypesTreeSectionPart sectionPart = new TypesTreeSectionPart (null, 
                managedForm, parent,
                section, style, title, description,
                fireTreeSelection, showIOType);         
        sectionPart.createContents();
        return sectionPart;
    }
    
    public void setText (String title)
    {
        if (title == null) return;
        section.setText(title);
    }
        
    public void hideFeature (boolean hide) {
        prefHideFeaturesInTypeSystem = hide;
        if (contentProvider != null) {
            contentProvider.hideFeature(hide);
            Object[] objs = treeViewer.getCheckedElements();
            treeViewer.getTree().setRedraw(false);
            treeViewer.refresh();
            treeViewer.setCheckedElements(objs);
            treeViewer.getTree().setRedraw(true);
        }
    }
    
    public void setViewerLayoutToFlatOrTree (boolean flat)
    {
        prefFlatLayout4Types = flat;
        if (contentProvider != null) {
            List list = getSelectedTypeNames();
            contentProvider.setViewerLayoutToFlatOrTree(flat);
            TreeViewerHelper.refreshWithCheckedState(treeViewer, false);
            if (list != null && list.size() > 0) {
                setTypeSelectionByName(list, true);
            }
        }
    }

    /*************************************************************************/
       
    public void setTypeSystemStyle (TypeSystemStyle tsStyle)
    {
        contentProvider.setTypeSystemStyle(tsStyle);
        labelProvider.setTypeSystemStyle(tsStyle);
    }
    
    public void setInput (Object input, String title)
    {
        if (input == null) {
            treeViewer.getTree().removeAll();
            return;
        }
        // long start = System.currentTimeMillis();  
        _typeHierarchy = (TypeTree) input;
        // _typeHierarchy.dumpTree(_typeHierarchy.getRoot());
        treeViewer.setInput(input);
        treeViewer.expandToLevel(2);

        if (title != null) {
            setText (title);
        }

        section.redraw();
        section.update();
        // Trace.logPerformanceTime(" _typeTreeViewer: ", start);
    }
    
    public void enableTypes (boolean enable, List<String> typeNames) {
        TypeNode node;
        for (String name: typeNames) {
            node = _typeHierarchy.getTypeNode(name);
            if (node != null) {
//                Trace.err("Disable " + name);
                node.setDisable(enable);
            }
        }
        treeViewer.refresh();
    }
       
    public List<String> getSelectedTypeNames() 
    {
        Object[] nodes = treeViewer.getCheckedElements();
        Object[] grayedNodes = treeViewer.getGrayedElements();
        if (nodes == null || nodes.length == 0) {
            return null;
        }
        List listGrayed = null;
        if (grayedNodes != null && grayedNodes.length > 0) {
            listGrayed = Arrays.asList(grayedNodes);
        }
        
        List<String> list = new ArrayList<String>(nodes.length);
        for (int i=0; i<nodes.length; ++i) {
            if (listGrayed == null || ! listGrayed.contains(nodes[i])) {
                String name;
                if (((TypeNode) nodes[i]).getObject() instanceof TypeDescription) {
                    name = ((TypeDescription) ((TypeNode) nodes[i]).getObject()).getName();
//                } else if (((TypeNode) nodes[i]).getObject() instanceof FeatureDescription) {
//                    name = ((FeatureDescription) ((TypeNode) nodes[i]).getObject()).getName();
                } else {
                    continue;
                }
                list.add(new String(name));
            }
        }
        return list;
    }
    
    // Note: If "types" is null, deselect ALL
    public void setTypeSelectionByName (List types, boolean selection)
    {
        if (_typeHierarchy != null) {
            if (types == null) {  
                // Deselect ALL
                Object[] objs = treeViewer.getCheckedElements();
                for (int i=0; i<objs.length; ++i) {
                    if (!treeViewer.getGrayed(objs[i])) {
                        treeViewer.setChecked(objs[i], false);
                    }
                }
                return;
            }
            
            for (int i=0; i<types.size(); ++i) {
                TypeNode node = _typeHierarchy.getTypeNode((String) types.get(i));
                if (node != null) {
                    treeViewer.setChecked(node, selection);
                    // fireSelectionChanged(new StructuredSelection(node));
                } else {
                    Trace.err("Cannot find TypeNode for type " + (String) types.get(i));
                }
            }
        } else {
            Trace.err("_typeHierarchy == null");
        }
    }

    public TypeNode getTypeNode (String typeFullName) {
        if (_typeHierarchy != null) {
            return _typeHierarchy.getTypeNode(typeFullName);
        }
        return null;
    }
    
    public Control getTreeControl ()
    {
        return treeViewer.getControl();
    }
    
    public TreeViewer getTreeViewer ()
    {
        return treeViewer;
    }
    
    public void addTypesToHierarchy (ArrayList types)
    {
        _typeHierarchy.addTypesToHierarchy(types);
        // Refresh Tree
        treeViewer.refresh();
        section.redraw();
        section.update();    
    }
    
    public void addAnnotation (AnnotationFS fs, TypeStyle typeStyle)
    {
        TypeNode node = _typeHierarchy.getTypeNode(fs.getType().getName());
        TreeItem  item = (TreeItem) treeViewer.testFindItem(node);
        if (item != null) {
            TypeNode n = (TypeNode) item.getData();
            n.setFsCount(n.getFsCount()+1);
            Color bg = typeStyle.getBackground();
            n.setBgColor(bg);
//            if (bg != null) {
//                // Gray out checkbox
//                n.setGrayed(false);
//            }
            treeViewer.refresh(n);
            Trace.err("getFsCount: " + n.getFsCount());
        }
    }
    
    public TypeTree getTypeSystemTree ()
    {
        return _typeHierarchy;
    }
    
    /*************************************************************************/
    
//    private FSIndex getIndexForType(CAS aTCAS, String typeName)
//    {
//        return getIndexForType(aTCAS, aTCAS.getTypeSystem().getType(typeName));
//    }
//    
//    private FSIndex getIndexForType(CAS aTCAS, Type aType)
//    {
//        if (aType != null) {
//            return null;
//        }
//        
//        Iterator iter = aTCAS.getIndexRepository().getLabels();
//        while (iter.hasNext())
//        {
//            String label = (String) iter.next();
//            FSIndex index = aTCAS.getIndexRepository().getIndex(label);
//            if (aTCAS.getTypeSystem().subsumes(index.getType(), aType))
//            {
//                return aTCAS.getIndexRepository().getIndex(label, aType);
//            }
//        }
//        return null;
//    }
    
    /*************************************************************************/
    
    public static Combo createLabelAndCombo (FormToolkit toolkit, Composite parent,
            String labelText, int style) 
    {
        Label label = toolkit.createLabel(parent, labelText);
        label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
        Combo ccombo = new Combo (parent, style);
        toolkit.adapt(ccombo);
        
        FormSection.fillIntoGridOrTableLayout (parent, label, ccombo, 10, 0);
        
        return ccombo;
    } // createLabelAndCCombo

    protected Section createContents () 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = getSection();
        section.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                // section.layout(true);
                // TODO Resize
                // managedForm.reflow(true);
            }
        });
        // Expand | Collapse
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                // System.out.println("form.reflow");
                // managedForm.getForm().layout(true);
                // Point pt = __parent.getSize();
                // System.err.println("parent x: " + pt.x + " ; parent y: " + pt.y);
                //  pt = getSection().getSize();
                // System.err.println(" Section  x: " + pt.x + " ; y: " + pt.y);
//                if ( e.getState() ) {
//                    // Expand
//                    TableWrapData td = (TableWrapData) section.getLayoutData();
//                    td.maxHeight = 400;
//                }
//                __parent.layout(true);
                
//                ((Composite) getSection().getClient()).layout(true);
//                if (tree == null) {
//                    tree.layout(true);
//                }
//                __managedForm.getForm().layout(true);
                managedForm.reflow(true);
                // pt = getSection().getSize();
                // System.err.println(" Section  x: " + pt.x + " ; y: " + pt.y);
            }
        });        
        section.setExpanded(false);
        
        ///////////////////////////////////////////////////////////////////////
        
        // Create ToolBar
        Composite sectionToolbarComposite = FormSection.createGridLayoutContainer (toolkit, section,
                4, 0, 0);
        section.setTextClient(sectionToolbarComposite);
        
        ///////////////////////////////////////////////////////////////////////
        
        // toolkit.createCompositeSeparator(section);
        
        // Create Composite
        Composite client = FormSection.createGridLayoutContainer (toolkit, section,
                2, 0, 0);        
        GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        client.setLayoutData(gd);
        
        toolkit.paintBordersFor(client);        
        section.setClient(client);   
        
//        immediateCreationButton = toolkit.createButton(client, 
//                "Immediately create annotation after highliting text", SWT.CHECK);
//        immediateCreationButton.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
//        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//        gd.horizontalSpan = 2;
//        gd.grabExcessHorizontalSpace = true;
//        immediateCreationButton.setLayoutData(gd);
//        
//        selectedTypesCombo = createLabelAndCombo (toolkit, client, 
//                "Type used for adding annotation:", SWT.READ_ONLY);
                
        /*********************************************************************/        
        
        // Create Tree for Type System
//        if (prefUseFilteredTree) {
//            // Create Filtered Tree
//            PatternFilter patternFilter = new PatternFilter();
//            patternFilter.setIncludeLeadingWildcard(true);
//            FilteredTree fFilteredTree = new FilteredTree(client, 
//                    SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, 
//                    patternFilter);
//            fFilteredTree.setBackground(parent.getDisplay().getSystemColor(
//                    SWT.COLOR_LIST_BACKGROUND));
//            // Get Viewer
//            // viewer = fFilteredTree.getViewer();
//            
//        }
        Tree tree = toolkit.createTree(client, SWT.CHECK);
        // final TableTree tree = new TableTree (client, SWT.NULL);
        gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.horizontalSpan = 2;
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 160;
        tree.setLayoutData(gd);
        
        // Create Viewer for Tree
        treeViewer = new CustomCheckedTreeViewer(tree);
        treeViewer.setUseHashlookup(true);
//        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//            public void selectionChanged(SelectionChangedEvent event) {
//                // Trace.trace();
//                if (_fireTreeSelection) {
//                    setSelectedTreeNode (event);
//                    managedForm.fireSelectionChanged(_instance, event.getSelection());
//                }
//            }
//        });
        treeViewer.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                Object[] objs = treeViewer.getCheckedElements();
                // Collect type names that are checkedand have annotations
                List<String> checkedList = new ArrayList<String>();                
                for (Object obj: objs) {
                    if (obj instanceof TypeNode) {
                        if (((TypeNode) obj).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE
                            && ((TypeNode) obj).getBgColor() != null) {
                            checkedList.add(((TypeDescription) ((TypeNode) obj).getObject()).getName());                           
                        }
                    }            
                }
                // Need to hide previous annotations even if checkedList.size()==0
                casViewControl.showAnnotationsByTypenameForView(checkedList, AnnotatedTextSectionPart.TAB_VIEW_ANNOTATIONS);
            }
        });

        contentProvider = new TypesTreeContentProvider(prefFlatLayout4Types);
        contentProvider.hideFeature(prefHideFeaturesInTypeSystem);
        treeViewer.setContentProvider(contentProvider);
        labelProvider = new TypesTreeLabelProvider(treeViewer, false, prefFlatLayout4Types);
        treeViewer.setLabelProvider(labelProvider);                      
//        _typeTreeViewer.addFilter(_typeTreeViewerFiler = new TypeSystemViewerFilter());
        treeViewer.setSorter(new TypeSystemSorter());
        
//        TypeSystemToolTips treeListener = new TypeSystemToolTips (tree);
//        tree.addListener (SWT.Dispose, treeListener);
//        tree.addListener (SWT.KeyDown, treeListener);
//        tree.addListener (SWT.MouseMove, treeListener);
//        tree.addListener (SWT.MouseHover, treeListener);
        
//        treeViewer.addCheckStateListener(new ICheckStateListener () {
//            public void checkStateChanged(CheckStateChangedEvent event) {
//                Object element = event.getElement();
//                if (element instanceof TypeNode) {
//                    if ( ((TypeNode) element).getBgColor() == null ) {
//                        treeViewer.setGrayed(event.getElement(), true);
//                        treeViewer.setChecked(event.getElement(), true);
//                    }
//                }
//            }            
//        });       
 
/*        
        treeViewer.addTreeListener(new ITreeViewerListener() {
            public void treeCollapsed(TreeExpansionEvent event) 
            {
                Object element= event.getElement();
                if ( ((TreeBaseNode) element).getBgColor() == null ) {
                    // && ! treeViewer.getGrayed(element)) { 
                    Trace.trace("Set gray: " + ((TreeBaseNode) element).getLabel());
                    treeViewer.setChecked(element, true);
                    treeViewer.setGrayed(element, true);
               }
            }
            public void treeCollapsed_SAVE(TreeExpansionEvent event) 
            {
                Trace.trace();
                Object element= event.getElement();
                if (element instanceof TreeBaseNode) {
//                if (_typeTreeViewer.getGrayed(element) == false)
//                    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
//                    public void run() {
//                        setSubtreeChecked(element, fTree.getChecked(element), false);
//                    }
//                });
//                    Widget item = _typeTreeViewer.testFindItem(element);
//                    _typeTreeViewer.getTree().
                    if ( ((TreeBaseNode) element).getBgColor() == null ) {
                         // && ! treeViewer.getGrayed(element)) { 
                        Trace.trace("Set gray: " + ((TreeBaseNode) element).getLabel());
//                        TreeItem treeItem = (TreeItem) treeViewer.testFindItem(element);
//                        treeItem.setChecked(true);
//                        treeItem.setGrayed(true);
                        // treeViewer.setChecked(element, true);
                        // treeViewer.setGrayed(element, true);
                    }
                    Object[] children = contentProvider.getChildren(element);
                    for (int i=0; i<children.length; ++i) {
                        if ( ((TreeBaseNode) children[i]).getBgColor() == null ) {
                             //   && ! _typeTreeViewer.getGrayed(children[i])) {                            
                            treeViewer.setChecked(children[i], true);
                            treeViewer.setGrayed(children[i], true);
                        }
                    }
                    
                } else {
                    if (!treeViewer.getGrayed(element)) {
                        treeViewer.setChecked(element, true);
                        treeViewer.setGrayed(element, true);
                    }
                }
            }
            public void treeExpanded(TreeExpansionEvent event) {
                Object element= event.getElement();
                if (element instanceof TreeBaseNode) {
//                if (_typeTreeViewer.getGrayed(element) == false)
//                    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
//                    public void run() {
//                        setSubtreeChecked(element, fTree.getChecked(element), false);
//                    }
//                });
//                    Widget item = _typeTreeViewer.testFindItem(element);
//                    _typeTreeViewer.getTree().
                    if ( ((TreeBaseNode) element).getBgColor() == null ) {
                        //  && ! treeViewer.getGrayed(element)) {                            
                        treeViewer.setChecked(element, true);
                        treeViewer.setGrayed(element, true);
                    }
                    Object[] children = contentProvider.getChildren(element);
                    for (int i=0; i<children.length; ++i) {
                        if ( ((TreeBaseNode) children[i]).getBgColor() == null ) {
                             //   && ! _typeTreeViewer.getGrayed(children[i])) {                            
                            treeViewer.setChecked(children[i], true);
                            treeViewer.setGrayed(children[i], true);
                        }
                    }
                    
                } else {
                    if (!treeViewer.getGrayed(element)) {
                        treeViewer.setChecked(element, true);
                        treeViewer.setGrayed(element, true);
                    }
                }
            }
        });
*/        
        createSectionToolbar (toolkit, section, sectionToolbarComposite, 
                             treeViewer);
        /*********************************************************************/
//        DnDTreeViewer dnd = new DnDTreeViewer (this);
//        dnd.initDragAndDrop (_typeTreeViewer);
        
        /*********************************************************************/
        /*                      Set Selection Provider                       */
        /*********************************************************************/
/*        if (__workbenchPartSite != null) {
            __workbenchPartSite.setSelectionProvider(_typeTreeViewer);
        }
*/        
        createContextMenu(treeViewer.getControl());
        section.setExpanded(true);
        
        
        // Trace.trace();
        return section;
    } // createContents
    
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
                casViewControl.onActionInvocation(TypesTreeSectionPart.this, ICASViewControl.ACTION_DESELECT_ALL, null);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        final MenuItem itemName = new MenuItem(subMenu, SWT.NONE);
        if ( ((TypesTreeLabelProvider) treeViewer.getLabelProvider()).isFullNameView() ) {
            itemName.setText("Show Short Name");
        } else {
            itemName.setText("Show Full Name");
        }
        itemName.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                ((TypesTreeLabelProvider) treeViewer.getLabelProvider())
                        .switchNameView(false);   
                TreeViewerHelper.refreshWithCheckedState(treeViewer, true);
                
                if ( ((TypesTreeLabelProvider) treeViewer.getLabelProvider()).isFullNameView() ) {
                    itemName.setText("Show Short Name");
                } else {
                    itemName.setText("Show Full Name");
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        // Show All Types
//        final MenuItem itemShowTypes = new MenuItem(subMenu, SWT.NONE);
//        if (contentProvider.isShowAllTypes()) {
//            itemShowTypes.setText("Show Types Having Annotations");
//        } else {
//            itemShowTypes.setText("Show All Types");
//        }
//        itemShowTypes.addSelectionListener(new SelectionListener () {
//            public void widgetSelected (SelectionEvent event) {
//                contentProvider.setShowAllTypes(!contentProvider.isShowAllTypes());
//                TreeViewerHelper.refreshWithCheckedState(treeViewer, false);
//                if (contentProvider.isShowAllTypes()) {
//                    itemShowTypes.setText("Show Types Having Annotations");
//                } else {
//                    itemShowTypes.setText("Show All Types");
//                }
//            }
//
//            public void widgetDefaultSelected(SelectionEvent e) {
//            }
//        });
 
        new MenuItem(subMenu, SWT.SEPARATOR);
        
        item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Show Flat Structure");
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                List list = getSelectedTypeNames();
                contentProvider.setViewerLayoutToFlatOrTree(true);
                labelProvider.setViewerLayoutToFlatOrTree(true);
                TreeViewerHelper.refreshWithCheckedState(treeViewer, false);
                if (list != null && list.size() > 0) {
                    setTypeSelectionByName(list, true);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Show Tree Structure");
        item.setData(null);
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
//                _typeTreeViewerFiler.setFilterFlags(TypeSystemViewerFilter.FILTER_TYPE_SHOW_OUTPUT, true);
                List list = getSelectedTypeNames();
                contentProvider.setViewerLayoutToFlatOrTree(false);
                labelProvider.setViewerLayoutToFlatOrTree(false);
                TreeViewerHelper.refreshWithCheckedState(treeViewer, false);
                if (list != null && list.size() > 0) {
                    setTypeSelectionByName(list, true);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        new MenuItem(subMenu, SWT.SEPARATOR);

        // Filters
/*        
        item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Set Filters");
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                casViewControl.onActionInvocation(TypesTreeSectionPart.this, ICASViewControl.ACTION_SET_FILTERS, null);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
*/        
        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        // info.setImage(ImageRegistryUtil.getInstance().getImage(ImageRegistryUtil.IMG_VIEW_MENU));
        info.setImage(ImageLoader.getInstance().getImage(ImageLoader.ICON_UI_VIEW_MENU));
        info.setToolTipText("Options...");
        info.setBackground(section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                subMenu.setVisible(true);
            }
        });        
    }
    
    /*************************************************************************/
    
    /**
     * Creates a pop-up menu on the given control
     * 
     * @param menuControl the control with which the pop-up
     *  menu will be associated
     */
    private void createContextMenu(Control menuControl) {
        MenuManager menuMgr = new MenuManager("#PopUp"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });
        Menu menu = menuMgr.createContextMenu(menuControl);
        menuControl.setMenu(menu);
        
        // register the context menu such that other plugins may contribute to it
        // 
//        if (__workbenchPartSite != null) {
//            __workbenchPartSite.registerContextMenu(menuMgr, _typeTreeViewer);
//        }
    }   
    
    /**
     * Adds items to the context menu
     * 
     * @param menu The menu to contribute to
     */
    protected void fillContextMenu (IMenuManager menu) 
    {
        Trace.trace();
        
//        if (_selectedTreeNode == null) {
//            return;
//        }
//        Action loadAction = new Action("load ?", Action.AS_PUSH_BUTTON) {
//            public void run() {
//                
//            }
//        };
        if (_selectedTreeNode != null) {
//            if (_selectedTreeNode.getObjectType() == ItemTypes.ITEM_TYPE_TYPE ) {
//                createContextMenuForType(menu, (TypeMetadata) _selectedTreeNode.getObject());
//          } else if (_selectedTreeNode.getObjectType() == ItemTypes.ITEM_TYPE_FEATURE ) {
//              createContextMenuForFeature(menu);;
//            }
        }
        // menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    } // fillContextMenu
    
    private void setSelectedTreeNode (SelectionChangedEvent event) 
    {
        ISelection selection = event.getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection)selection;
            if (ssel.size() > 0) {
                if (ssel.getFirstElement() instanceof TypeNode) {           
                    _selectedTreeNode = (TypeNode) ssel.getFirstElement();
//                    if ( _selectedTreeNode.getBgColor() == null ) {
//                        // && ! treeViewer.getGrayed(element)) { 
//                        Trace.trace("Set gray: " + _selectedTreeNode.getLabel());
//                        treeViewer.setChecked(_selectedTreeNode, true);
//                        treeViewer.setGrayed(_selectedTreeNode, true);
//                    }
                    return;
                } else {
                    // Trace.trace("class:" + ssel.getFirstElement().getClass().getName());
                }
            }
        }
        _selectedTreeNode = null;
    }

    
    /*************************************************************************/
    
//    private ListenerList selectionListeners = new ListenerList();
//    private ListenerList selectionChangedListeners = new ListenerList();
    
/*    public void addSelectionListener(SelectionListener listener)
    {
        selectionListeners.add(listener);
    }
    
    public void removeSelectionListener(SelectionListener listener)
    {
        selectionListeners.remove(listener);
    }
    
    protected void fireSelection (final SelectionEvent event) 
    {
        String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
        // Trace.err("event.detail: " + string);
        if (event.detail == SWT.CHECK) {
            TypeNode obj = (TypeNode) ((TreeItem)event.item).getData();
            TypeDescription type = (TypeDescription) obj.getObject();
            if ( ((TreeItem)event.item).getChecked() ) {
                // Trace.err("checked");
//                selectedTypes.add(type.getName());
            } else {
                // Trace.err("NOT checked");
//                selectedTypes.remove(type.getName());
            }
        }
        
        // Fire the event
        Object[] listeners = selectionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final SelectionListener l = (SelectionListener) listeners[i];
            l.widgetSelected(event); // CASViewControl
        }
    }
*/    
    ///////////////////////////////////////////////////////////////////////////
    
//    public void addSelectionChangedListener(ISelectionChangedListener listener) {
//        selectionChangedListeners.add(listener);
//    }
//
//    public ISelection getSelection() {
//        if (treeViewer == null)
//            return StructuredSelection.EMPTY;
//        return treeViewer.getSelection();
//    }
//
//    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
//        selectionChangedListeners.remove(listener);
//    }
//    
//
//    public void setSelection(ISelection selection) {
//        if (treeViewer == null)
//            return;
//        treeViewer.setSelection(selection);
//    }
    
    /**
     * Fires a selection changed event.
     *
     * @param selection the new selection
     */
//    protected void fireSelectionChanged(ISelection selection) {
//        // create an event
//        final SelectionChangedEvent event = new SelectionChangedEvent(this,
//                selection);
//
//        // fire the event
//        Object[] listeners = selectionChangedListeners.getListeners();
//        for (int i = 0; i < listeners.length; ++i) {
//            final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
//            l.selectionChanged(event);
//        }
//    }

    
//    public void selectionChanged(SelectionChangedEvent event) 
//    {
//        ISelection selection = event.getSelection();
//        fireSelectionChanged(selection);
//    }

//    public void widgetSelected(SelectionEvent e) {
//        // Trace.trace();
//        fireSelection(e);       
//    }
//
//    public void widgetDefaultSelected(SelectionEvent e) {
//        // TODO Auto-generated method stub        
//    }
    
}
