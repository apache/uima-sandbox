package org.apache.uima.casviewer.ui.internal.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Type;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.casviewer.ui.internal.type.TypeSystemSorter;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * 	SectionPart that shows the imported style
 * 
 */
public class ColoredTypeTreeSectionPart extends AbstractSectionPart 
                                 implements ISelectionProvider, SelectionListener {
    
    final private static int SECTION_STYLE_COMMON  = Section.TWISTIE | Section.EXPANDED;
    
    private ColoredTypeTreeSectionPart     _instance;
    private Tree                    tree = null;
    private CheckboxTreeViewer      treeViewer = null;
    // private TreeViewer      treeViewer = null;
//    private TypeSystemViewerFilter  _typeTreeViewerFiler = null;
    private ColoredTypeTreeContentProvider contentProvider = null;
    private TypeTree            _typeHierarchy = null;
    
    private TypeNode            _selectedTreeNode = null;   // Selected Node in Type Tree
    private boolean                 _fireTreeSelection = true;
    
    /*************************************************************************/

    protected ColoredTypeTreeSectionPart (IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description,
            boolean fireTreeSelection) 
    {
        super(managedForm, parent, section, style, title, description);
        _instance       = this;
        _fireTreeSelection = fireTreeSelection;
        ///////////////////////////////////////////////////////////////////////
        
        // final SectionPart spart = new SectionPart(section);
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!        
    }
    
    static public ColoredTypeTreeSectionPart createInstance (final IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            boolean fireTreeSelection) 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 5, 8,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                //  parent.layout(true);
                managedForm.reflow(true);
                // section.layout(true);
            }
        });        
        ColoredTypeTreeSectionPart sectionPart = new ColoredTypeTreeSectionPart (managedForm, parent, 
                section, style, title, description,
                fireTreeSelection);         
        sectionPart.createContents();
        
        return sectionPart;
    } // createInstance

    static public ColoredTypeTreeSectionPart createTableWrapDataInstance (IManagedForm managedForm, 
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
        ColoredTypeTreeSectionPart sectionPart = new ColoredTypeTreeSectionPart (managedForm, parent,
                section, style, title, description,
                fireTreeSelection);         
        sectionPart.createContents();
        return sectionPart;
    }
    
    public void setText (String title)
    {
        if (title == null) return;
        section.setText(title);
    }
        
    /**
     * Grey out tree by starting from root for type-node with 0 annotation
     * 
     * @param element
     * @return void
     */
    private void grayTree (TypeTree element)
    {        
        Object[] children = contentProvider.getChildren(element.getRoot().getChildrenArray()[0]);
        for (int i=0; i<children.length; ++i) {
            if ( ((TypeNode) children[i]).getBgColor() == null ) {
                 //   && ! _typeTreeViewer.getGrayed(children[i])) {
                // treeViewer.setChecked(children[i], true);
                // treeViewer.setGrayed(children[i], true);
                treeViewer.setGrayChecked(children[i], true);
            }
            Object[] objs = contentProvider.getChildren(children[i]);
            for (int k=0; k<objs.length; ++k) {
                if ( ((TypeNode) objs[k]).getBgColor() == null ) {
                   // treeViewer.setChecked(objs[k], true);
                   // treeViewer.setGrayed(objs[k], true);
                   treeViewer.setGrayChecked(objs[k], true);
                }
            }
        }        
    }
        
    /**
     * 
     * @param input     TypeSystemStyle object
     * @param title
     * @return void
     */
    public void setInput (Object input, String title_NOT_USED)
    {
        // Trace.trace();
        if (input == null) {
            treeViewer.getTree().removeAll();
            return;
        }
        section.setRedraw(false);
        // long start = System.currentTimeMillis();  
//        _typeHierarchy = (UimaTypeTree) input;
        // _typeHierarchy.dumpTree(_typeHierarchy.getRoot());
        treeViewer.setInput(input);
        treeViewer.expandToLevel(2);

        if (title != null) {
            setText (title);
        }

        section.setExpanded(true);
        section.setRedraw(true);
        section.update();
        // managedForm.reflow(true);
        // Trace.logPerformanceTime(" _typeTreeViewer: ", start);
    }
    
    
    public List getSelectedTypeNames() 
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
        
        List    list = new ArrayList(nodes.length);
        for (int i=0; i<nodes.length; ++i) {
            if ( ! listGrayed.contains(nodes[i])) {
                list.add(new String(((TypeDescription) ((TypeNode) nodes[i]).getObject()).getName()));
            }
        }
        return list;
    }
    
    // Note: "types" should NOT be null
    public void setTypeSelectionByName (List types, boolean selection)
    {
        if (_typeHierarchy != null) {
            // Trace.trace();
            for (int i=0; i<types.size(); ++i) {
                TypeNode node = _typeHierarchy.getTypeNode((String) types.get(i));
                if (node != null) {
                    treeViewer.setChecked(node, selection);
                    // fireSelectionChanged(new StructuredSelection(node));
                } else {
                    Trace.err("Cannot find TreeBaseNode for type " + (String) types.get(i));
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
    
    public TypeTree getTypeSystemTree ()
    {
        return _typeHierarchy;
    }
    
    /*************************************************************************/
    
    private FSIndex getIndexForType(CAS aTCAS, String typeName)
    {
        return getIndexForType(aTCAS, aTCAS.getTypeSystem().getType(typeName));
    }
    
    private FSIndex getIndexForType(CAS aTCAS, Type aType)
    {
        if (aType != null) {
            return null;
        }
        
        Iterator iter = aTCAS.getIndexRepository().getLabels();
        while (iter.hasNext())
        {
            String label = (String) iter.next();
            FSIndex index = aTCAS.getIndexRepository().getIndex(label);
            if (aTCAS.getTypeSystem().subsumes(index.getType(), aType))
            {
                return aTCAS.getIndexRepository().getIndex(label, aType);
            }
        }
        return null;
    }
    
    /**
     * 
     * 
     * @param tcas
     * @param tsDesc
     * @param AnnotationTypes   List of types having annotations in CAS
     * @return void
     */
/*    public void setInput (CAS tcas, TypeSystemDescription tsDesc, List annotationTypes)
    {
        // Create Type System Tree
        UimaTypeTree tsTree = UimaTypeUtil.createUimaTypeTreeFromXmlDescriptor(tsDesc);

        // Set background color for types havings annotations
        // List list = annotationTypes;
        if (annotationTypes != null) {
            for (int i=0; i<annotationTypes.size(); ++i) {
                TreeBaseNode node = tsTree.getTypeNode(((Type)annotationTypes.get(i)).getName());
                if (node != null) {
                    TypeColor c = TypeSystemStyle.getTypeStyle().getTypeColor(((Type)annotationTypes.get(i)).getName());
                    if (c != null) {
                        node.setBgColor(c.bgColor);
                        // Trace.trace("Set BG color for " + ((Type)list.get(i)).getName());
                        // Gray out checkbox
                        node.setGrayed(false);
//                    } else {
//                        // Gray out checkbox
//                        node.setGrayed(true);                        
                    }
                    
                }
            }
        }
 
        // Testing
//        FSIndex fsIndex1 = getIndexForType(tcas, "uima.tcas.Annotation");
//        if (fsIndex1 != null) {
//            Trace.trace("FsTotal for Annotation:" + fsIndex1.size());
//        } else {
//            Trace.trace("NO index for type: " + "uima.tcas.Annotation");
//        }
        
        // Set total annotations for each type in the tree
        List list = tsTree.getTypeListFromHierarchy(true, false);
        if (list != null) {
            for (int i=0; i<list.size(); ++i) {
                if (list.get(i) instanceof TypeMetadata) {
                    Type type = tcas.getTypeSystem()
                        .getType(((TypeMetadata)list.get(i)).getName());
                    if (type != null) {
                        TreeBaseNode node = tsTree.getTypeNode(type.getName());
                        if (node == null) {
                            Trace.trace("NOT in tree: " + type.getShortName());
                            
                            continue;
                        }
                        // Set total FS for this type
                        FSIndex fsIndex = getIndexForType(tcas, type);
                        if (fsIndex != null) {
                            node.setFsTotal(fsIndex.size());
                        } else {
                            Trace.trace("NO index for type: " + type.getName());
                        }
                    } else {
                        Trace.trace("NOT in Cas: " + ((TypeMetadata)list.get(i)).getName());
                    }
                }
            }
        }
        
        setInput(tsTree, "Colored Type Tree");
    }
*/
    /*************************************************************************/
        
    protected Section createContents () 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = getSection();
        section.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                // section.layout(true);
                managedForm.reflow(true);
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
//        Composite sectionToolbarComposite = FormSection.createGridLayoutContainer (toolkit, section,
//                4, 0, 0);
//        section.setTextClient(sectionToolbarComposite);
        
        ///////////////////////////////////////////////////////////////////////
        
        // toolkit.createCompositeSeparator(section);
        
        // Create Composite
        Composite client = FormSection.createGridLayoutContainer (toolkit, section,
                1, 10, 10);        
        GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        client.setLayoutData(gd);
        
        toolkit.paintBordersFor(client);        
        section.setClient(client);   
        
        /*********************************************************************/        
        
        // Create Tree for Type System
        tree = toolkit.createTree(client, SWT.CHECK);
        // final TableTree tree = new TableTree (client, SWT.NULL);
        gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 160;
        tree.setLayoutData(gd);
        // tree.setVisible(false);
        tree.addSelectionListener(this);
//        tree.addListener (SWT.Selection, new Listener () {
//            public void handleEvent (Event event) {
//                String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
//                System.out.println (tree.getSelection()[0] + " " + string);
//                if ( ! ((TreeItem)event.item).getGrayed() ) {
//                    ((TreeItem)event.item).setGrayed(true);
//                }
//                event.doit = false;
//                ((TreeItem)event.item).setChecked(false);
//            }
//        });
        
        // Create Viewer for Tree
        treeViewer = new CheckboxTreeViewer(tree);
        treeViewer.setUseHashlookup(true);
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Trace.trace();
                if (_fireTreeSelection) {
                    setSelectedTreeNode (event);
                    managedForm.fireSelectionChanged(_instance, event.getSelection());
                }
            }
        });
        
        // For Table Tree Viewer
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        String headers[] = { "Type Name", "", "FG","", "BG", "Selected", "Hide" };
        final TreeColumn columns[] = new TreeColumn[headers.length];
        for (int i = 0; i < headers.length; i++) {
             TreeColumn tc = new TreeColumn(tree, SWT.NONE, i);
            tc.setResizable(true);
            tc.setText(headers[i]);
            if (i==0) {
                tc.setWidth(250);
            } else if (i==1 || i==3) {
                tc.setWidth(4);
            } else {
                tc.setWidth(60);
            }
            columns[i] = tc;
        }
        tree.pack(true);
        
        contentProvider = new ColoredTypeTreeContentProvider(false);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(new ColoredTypeTreeLabelProvider(treeViewer, false));                      
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
//                if (element instanceof TreeBaseNode) {
//                    if ( ((TreeBaseNode) element).getBgColor() == null ) {
//                        treeViewer.setGrayed(event.getElement(), true);
//                        treeViewer.setChecked(event.getElement(), true);
//                    }
//                }
//            }            
//        });       
        
        treeViewer.addTreeListener(new ITreeViewerListener() {
            public void treeCollapsed(TreeExpansionEvent event) 
            {
                Object element= event.getElement();
                if ( ((TypeNode) element).getBgColor() == null ) {
                    // && ! treeViewer.getGrayed(element)) { 
                    Trace.trace("Set gray: " + ((TypeNode) element).getLabel());
                    treeViewer.setChecked(element, true);
                    treeViewer.setGrayed(element, true);
               }
            }
            public void treeCollapsed_SAVE(TreeExpansionEvent event) 
            {
                Trace.trace();
                Object element= event.getElement();
                if (element instanceof TypeNode) {
//                if (_typeTreeViewer.getGrayed(element) == false)
//                    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
//                    public void run() {
//                        setSubtreeChecked(element, fTree.getChecked(element), false);
//                    }
//                });
//                    Widget item = _typeTreeViewer.testFindItem(element);
//                    _typeTreeViewer.getTree().
                    if ( ((TypeNode) element).getBgColor() == null ) {
                         // && ! treeViewer.getGrayed(element)) { 
                        Trace.trace("Set gray: " + ((TypeNode) element).getLabel());
//                        TreeItem treeItem = (TreeItem) treeViewer.testFindItem(element);
//                        treeItem.setChecked(true);
//                        treeItem.setGrayed(true);
                        // treeViewer.setChecked(element, true);
                        // treeViewer.setGrayed(element, true);
                    }
                    Object[] children = contentProvider.getChildren(element);
                    for (int i=0; i<children.length; ++i) {
                        if ( ((TypeNode) children[i]).getBgColor() == null ) {
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
                if (element instanceof TypeNode) {
//                if (_typeTreeViewer.getGrayed(element) == false)
//                    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
//                    public void run() {
//                        setSubtreeChecked(element, fTree.getChecked(element), false);
//                    }
//                });
//                    Widget item = _typeTreeViewer.testFindItem(element);
//                    _typeTreeViewer.getTree().
                    if ( ((TypeNode) element).getBgColor() == null ) {
                        //  && ! treeViewer.getGrayed(element)) {                            
                        treeViewer.setChecked(element, true);
                        treeViewer.setGrayed(element, true);
                    }
                    Object[] children = contentProvider.getChildren(element);
                    for (int i=0; i<children.length; ++i) {
                        if ( ((TypeNode) children[i]).getBgColor() == null ) {
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
        
        // createSectionToolbar (toolkit, section, sectionToolbarComposite, treeViewer);
        
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
            Composite toolbarComposite, final TreeViewer treeViewer)
    {
        AbstractSectionPart.createExpandAllMenu (toolkit, section, toolbarComposite, 
                treeViewer);
        AbstractSectionPart.createCollapseAllMenu(toolkit, section, toolbarComposite, 
                treeViewer);
/*        
        final ImageHyperlink infoView = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(infoView, true, true);
        infoView.setImage(ImageRegistryUtil.getInstance().getImage(ImageRegistryUtil.IMG_FULL_NAME));
        infoView.setToolTipText("Show Full Name");
        infoView.setBackground(section.getTitleBarGradientBackground());
        infoView.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                if ( ((AnnotationTreeLabelProvider) treeViewer.getLabelProvider())
                        .switchNameView(true) ) {
                    infoView.setToolTipText("Show Short Name");
                } else {
                    infoView.setToolTipText("Show Full Name");
                }
            }
        });
 
        // Create Menu
        final Menu subMenu = new Menu (toolbarComposite); 
        MenuItem item = new MenuItem(subMenu, SWT.NONE);
        item.setText("Show Flat Structure");
        item.addSelectionListener(new SelectionListener () {
            public void widgetSelected (SelectionEvent event) {
                // Trace.trace("Show Input Types");
//                _typeTreeViewerFiler.setFilterFlags(TypeSystemViewerFilter.FILTER_TYPE_SHOW_INPUT, true);
                contentProvider.setViewerType(true);
                treeViewer.refresh(true);
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
                contentProvider.setViewerType(false);
                // _typeTreeViewer.setInput(_typeHierarchy);
                treeViewer.refresh(true);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub                
            }
        });

        ImageHyperlink info = new ImageHyperlink(toolbarComposite, SWT.NULL);
        toolkit.adapt(info, true, true);
        info.setImage(ImageRegistryUtil.getInstance().getImage(ImageRegistryUtil.IMG_VIEW_MENU));
        info.setToolTipText("Flat or Tree...");
        info.setBackground(section.getTitleBarGradientBackground());
        info.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                subMenu.setVisible(true);
            }
        }); 
*/               
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
    
    private ListenerList selectionListeners = new ListenerList();
    private ListenerList selectionChangedListeners = new ListenerList();
    
    public void addSelectionListener(SelectionListener listener)
    {
        selectionListeners.add(listener);
    }
    
    public void removeSelectionListener(SelectionListener listener)
    {
        selectionListeners.remove(listener);
    }
    
    protected void fireSelection (final SelectionEvent event) {
        // fire the event
        Object[] listeners = selectionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final SelectionListener l = (SelectionListener) listeners[i];
            l.widgetSelected(event);
//            Platform.run(new SafeRunnable() {
//                public void run() {
//                    l.widgetSelected(event);
//                }
//            });
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.add(listener);
    }

    public ISelection getSelection() {
        if (treeViewer == null)
            return StructuredSelection.EMPTY;
        return treeViewer.getSelection();
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.remove(listener);
    }
    

    public void setSelection(ISelection selection) {
        if (treeViewer == null)
            return;
        treeViewer.setSelection(selection);
    }
    
    /**
     * Fires a selection changed event.
     *
     * @param selection the new selection
     */
    protected void fireSelectionChanged(ISelection selection) {
        // create an event
        final SelectionChangedEvent event = new SelectionChangedEvent(this,
                selection);

        // fire the event
        Object[] listeners = selectionChangedListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
            l.selectionChanged(event);
//            Platform.run(new SafeRunnable() {
//                public void run() {
//                    l.selectionChanged(event);
//                }
//            });
        }
    }

    public void selectionChanged(Object item) {
//        IFormPage page = editor.getActivePageInstance();
//        String id = getParentPageId(item);
//        IFormPage newPage=null;
//        if (id!=null && (page==null || !page.getId().equals(id)))
//            newPage = editor.setActivePage(id);
//        IFormPage revealPage = newPage!=null?newPage:page;
//        if (revealPage!=null && !(item instanceof IFormPage))
//            revealPage.selectReveal(item);
        
    }
    
    public void selectionChanged(SelectionChangedEvent event) 
    {
        ISelection selection = event.getSelection();
        if (selection.isEmpty() == false
                && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            Object item = ssel.getFirstElement();
            selectionChanged(item);
        }
        fireSelectionChanged(selection);
    }

    public void widgetSelected(SelectionEvent e) {
        // Trace.trace();
        fireSelection(e);
        
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub        
    }
    
}
