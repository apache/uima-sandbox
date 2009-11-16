package org.apache.uima.casviewer.ui.internal.style;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Type;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 	SectionPart that shows the default colors
 * 
 */
public class DefaultColorTreeSectionPart extends AbstractSectionPart 
                                 implements ISelectionProvider, SelectionListener {
    
    final private static int SECTION_STYLE_COMMON  = Section.TWISTIE | Section.EXPANDED;
    
    private DefaultColorTreeSectionPart _instance;
    private IPropertyChangeListener     propertyChangeListener;
    private TypeSystemStyle             tsStyle;
    private Tree                        tree = null;
    private TreeViewer                  treeViewer = null;
//    private TypeSystemViewerFilter  _typeTreeViewerFiler = null;
    private ColoredTypeTreeContentProvider contentProvider = null;
    private TypeTree                _typeHierarchy = null;
    private Object                      inputObject;    // List<ColoredType>
    
    private TypeNode            _selectedTreeNode = null;   // Selected Node in Type Tree
    private boolean                 _fireTreeSelection = true;
    
    private TreeItem                currentSelectedTreeItem;
    private int                     currentSelectedColumn;
    
    /*************************************************************************/

    protected DefaultColorTreeSectionPart (IPropertyChangeListener listener,
            IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description,
            boolean fireTreeSelection, TypeSystemStyle tsStyle) 
    {
        super(managedForm, parent, section, style, title, description);
        _instance          = this;
        propertyChangeListener = listener;
        _fireTreeSelection = fireTreeSelection;
        this.tsStyle       = tsStyle;
        ///////////////////////////////////////////////////////////////////////
        
        // final SectionPart spart = new SectionPart(section);
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!        
    }
    
    static public DefaultColorTreeSectionPart createInstance (IPropertyChangeListener listener, 
            final IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            boolean fireTreeSelection,  TypeSystemStyle tsStyle) 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 5, 8,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
        toolkit.createCompositeSeparator(section);
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                //  parent.layout(true);
                managedForm.reflow(true);
                // section.layout(true);
            }
        });        
        DefaultColorTreeSectionPart sectionPart = new DefaultColorTreeSectionPart (listener,
                managedForm, parent, 
                section, style, title, description,
                fireTreeSelection, tsStyle);         
        sectionPart.createContents();
        
        return sectionPart;
    } // createInstance
   
    public void setText (String title)
    {
        if (title == null) return;
        section.setText(title);
    }
        
        
    /**
     * 
     *  input       List<TypeStyle>
     */
    public void setInput (Object input, String title)
    {
        // Trace.trace();
        if (treeViewer == null) {
            inputObject = input;
            return;
        }
        
        if (input == null) {
            treeViewer.getTree().removeAll();
            return;
        }
        // long start = System.currentTimeMillis();  
        treeViewer.setInput(input);
        treeViewer.expandToLevel(2);

        if (title != null) {
            setText (title);
        }
        section.redraw();
        section.update();
        // Trace.logPerformanceTime(" _typeTreeViewer: ", start);
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
        tree = toolkit.createTree(client, SWT.FULL_SELECTION);
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
        /*
         * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
         * Therefore, it is critical for performance that these methods be
         * as efficient as possible.
         */
/*
        tree.addListener(SWT.EraseItem, new Listener() {
            public void handleEvent(Event event) {          
                if ((event.detail & SWT.SELECTED) != 0) {
                    GC gc = event.gc;
                    Rectangle area = tree.getClientArea();
                    
                     * If you wish to paint the selection beyond the end of
                     * last column, you must change the clipping region.
                     
                    int columnCount = tree.getColumnCount();
                    if (event.index == columnCount - 1 || columnCount == 0) {
                        int width = area.x + area.width - event.x;
                        if (width > 0) {
                            Region region = new Region();
                            gc.getClipping(region);
                            region.add(event.x, event.y, width, event.height); 
                            gc.setClipping(region);
                            region.dispose();
                        }
                    }
                    gc.setAdvanced(true);
                    if (gc.getAdvanced()) gc.setAlpha(127);                             
                    Rectangle rect = event.getBounds();
                    Color foreground = gc.getForeground();
                    Color background = gc.getBackground();
                    gc.setForeground(tree.getDisplay().getSystemColor(SWT.COLOR_RED));
                    // gc.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    gc.drawRectangle(rect);
                    // gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
                    // restore colors for subsequent drawing
                    gc.setForeground(foreground);
                    // gc.setBackground(background);
                    event.detail &= ~SWT.SELECTED;                  
                }                       
            }
        });     
        
*/        
        tree.addListener (SWT.MouseUp, new Listener () {
            public void handleEvent (Event event) {
                Point point = new Point (event.x, event.y);
                // System.out.println("Click at x=" + event.x + " y=" + event.y);
                currentSelectedTreeItem = tree.getItem (point);
                if (currentSelectedTreeItem != null) {
                    // Find what column is clicked
                    TreeColumn[] columns = tree.getColumns();
                    int x = 0; 
                    int w = 0;
                    int i = 0;
                    currentSelectedColumn = 0;
                    for (i=0; i<columns.length; ++i) {
                        w = columns[i].getWidth();
                        if (x < event.x && event.x < (x+w)) {
                            // System.out.println("Found column:" + i);
                            currentSelectedColumn = i;
                            break;
                        }
                        x += w;
                    }
                    // System.out.println ("Mouse up at column " + i + " of " + currentSelectedTreeItem.getText());
                }
            }
        });

/*
        
    table.addListener (SWT.MouseDown, new Listener () {
        public void handleEvent (Event event) {
            Rectangle clientArea = table.getClientArea ();
            Point pt = new Point (event.x, event.y);
            int index = table.getTopIndex ();
            while (index < table.getItemCount ()) {
                boolean visible = false;
                TableItem item = table.getItem (index);
                for (int i=0; i < columnCount; i++) {
                    Rectangle rect = item.getBounds (i);
                    if (rect.contains (pt)) {
                        System.out.println ("Item " + index + "-" + i);
                    }
                    if (!visible && rect.intersects (clientArea)) {
                        visible = true;
                    }
                }
                if (!visible) return;
                index++;
            }
        }
    });
        
*/        
        // Create Viewer for Tree
        treeViewer = new TreeViewer(tree);
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
        String headers[] = { "RGB Values", "", "FG", "", "BG", "Selected", "Hidden" };
        final TreeColumn columns[] = new TreeColumn[headers.length];
        for (int i = 0; i < headers.length; i++) {
             TreeColumn tc = new TreeColumn(tree, SWT.NONE, i);
            tc.setResizable(true);
            tc.setText(headers[i]);
            if (i==0) {
                tc.setWidth(250);
            } else if (i==1 || i==3) {
                tc.setWidth(4);
            } else if (i==2 || i==4) {
                tc.setWidth(40);
            } else {
                tc.setWidth(60);
            }
            columns[i] = tc;
        }
        // tree.pack(true);
        
        contentProvider = new ColoredTypeTreeContentProvider(false);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(new DefaultColorTreeLabelProvider(treeViewer, false));                      
//        _typeTreeViewer.addFilter(_typeTreeViewerFiler = new TypeSystemViewerFilter());
        treeViewer.setSorter(new ViewerSorter());
        
//        TypeSystemToolTips treeListener = new TypeSystemToolTips (tree);
//        tree.addListener (SWT.Dispose, treeListener);
//        tree.addListener (SWT.KeyDown, treeListener);
//        tree.addListener (SWT.MouseMove, treeListener);
//        tree.addListener (SWT.MouseHover, treeListener);
        
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
        
        if (inputObject != null) {
            setInput (inputObject, "???");
        }
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
        // Trace.trace();
        
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
        fillContextMenuForAll(menu, null);
        // menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    } // fillContextMenu
    
/*    
                        TreeItem item = tree.getItem (new Point (event.x, event.y));
                        if (item != null) {
                            // Find what column is clicked
                            TreeColumn[] columns = tree.getColumns();
                            int x = 0; 
                            int w = 0;
                            int i = 0;
                            for (i=0; i<columns.length; ++i) {
                                w = columns[i].getWidth();
                                if (x < event.x && event.x < (x+w)) {
                                    System.out.println("Found column:" + i);
                                    break;
                                }
                                x += w;
                            }
                            // treeViewer.setSelection(new StructuredSelection(item));
                             
*/
    
    protected void fillContextMenuForAll (IMenuManager menu, Object selectedObject)
    {        
        if (currentSelectedTreeItem == null) {
            return;
        }
        menu.add(new Separator());

        Action action = new Action() {
            public void run() 
            {
                if (currentSelectedTreeItem == null) {
                    return;
                }
                TypeStyle typeStyle = (TypeStyle) ((TypeNode) currentSelectedTreeItem.getData()).getObject();
                RGB value = null, fgRGB = null, bgRGB = null;

                if (currentSelectedColumn == 5) {
                    // Select/Deselect
                    typeStyle.setChecked(!typeStyle.isChecked());  
                    treeViewer.refresh();
                    return;
                } else if (currentSelectedColumn == 6) {
                    // Hide/Hidden
                    typeStyle.setHidden(!typeStyle.isHidden()); 
                    treeViewer.refresh();
                    return;
                } else {
                    fgRGB = typeStyle.getForeground()!=null? typeStyle.getForeground().getRGB():null;
                    bgRGB = typeStyle.getBackground()!=null? typeStyle.getBackground().getRGB():null;
                    if (currentSelectedColumn == 2) {
                        // Edit Foreground Color
                        value = fgRGB;
                    } else {
                        // Edit Background Color
                        value = bgRGB;
                    }                    
                }
                ColorDialog dialog = new ColorDialog(tree.getShell());
                if (value != null) {
                    dialog.setRGB(value);
                }
                value = dialog.open();
                if (value != null) {
                    if (currentSelectedColumn == 2) {
                        fgRGB = value;
                        tsStyle.updateTypeStyleColor(typeStyle, value, null);
                    } else {
                        bgRGB = value;
                        tsStyle.updateTypeStyleColor(typeStyle, null, value);
                    }
//                    tsStyle.setTypeStyle(typeStyle.getTypeName(),
//                                fgRGB, bgRGB);
                    treeViewer.refresh(); // currentSelectedTreeItem);
                    
                    // Notify listener
                    propertyChangeListener.propertyChange(new PropertyChangeEvent(typeStyle,
                            "", null, null));
                }
            }
        };
        TypeStyle typeStyle = (TypeStyle) ((TypeNode) currentSelectedTreeItem.getData()).getObject();
        String text;
        if (currentSelectedColumn == 5) {
            if (typeStyle.isChecked()) {
                text = "Deselect";
            } else {
                text = "Select";
            }
        } else if (currentSelectedColumn == 6) {
            if (typeStyle.isHidden()) {
                text = "Show";
            } else {
                text = "Hide";
            }
        } else {
            text = "Edit color...";
        }
        action.setText(text); 
        menu.add(action);
        action.setEnabled(true);
    }    
    
    private void setSelectedTreeNode (SelectionChangedEvent event) 
    {
        ISelection selection = event.getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection)selection;
            if (ssel.size() > 0) {
                // Trace.trace("class:" + ssel.getFirstElement().getClass().getName());
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
        // Trace.trace("Selected obj: " + item.getClass().getName());
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
    
    /*************************************************************************/
    
    public void refreshTypeSystemStyle ()
    {
        treeViewer.refresh();
    }
     
    
}
