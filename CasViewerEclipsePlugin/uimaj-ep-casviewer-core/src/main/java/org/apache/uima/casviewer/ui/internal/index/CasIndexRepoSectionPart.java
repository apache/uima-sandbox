package org.apache.uima.casviewer.ui.internal.index;

import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.casviewer.ui.internal.util.IObjectSelectionListener;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class CasIndexRepoSectionPart extends AbstractSectionPart implements ISelectionChangedListener {

    // private CheckboxTreeViewer indexRepoViewer;   
    private  TreeViewer             treeViewer;   
    private Label                   inputDocName = null;
    private TypeSystemStyle         typesystemStyle;
    
    protected CasIndexRepoSectionPart (IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description)
    {
        super(managedForm, parent, section, style, title, description);        
        
        // final SectionPart spart = new SectionPart(section);
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!       
    }
    
    static public CasIndexRepoSectionPart createInstance (IManagedForm managedForm, 
            Composite parent, int style, String title,
            String description, TypeSystemStyle tsStyle)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 5, 8,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
                
        CasIndexRepoSectionPart sectionPart = new CasIndexRepoSectionPart (managedForm, parent, 
                section, style, title, description);
        sectionPart.typesystemStyle = tsStyle;
        sectionPart.createContents();
                
        return sectionPart;
    }
    
    /*************************************************************************/
    
    public Section createContents () 
    {
        FormToolkit toolkit = managedForm.getToolkit();
        // Section section = getSection();
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
        
        createIndexRepositoryTreeViewer(toolkit, client);
               
        createSectionToolbar (toolkit, section, sectionToolbarComposite, treeViewer);
        
        return section;
    }    
    
    protected void createSectionToolbar (FormToolkit toolkit, Section section, 
            Composite toolbarComposite, final TreeViewer treeViewer)
    {
        AbstractSectionPart.createExpandAllMenu (toolkit, section, toolbarComposite, 
                treeViewer);
        AbstractSectionPart.createCollapseAllMenu(toolkit, section, toolbarComposite, 
                treeViewer);        
    }
    
    /**
     * Create the TreeViewer for the index repository.
     * 
     * @param parent
     */
    private void createIndexRepositoryTreeViewer (FormToolkit toolkit, Composite parent) 
    {                
        // Create Tree for FSIndex
        Tree tree = toolkit.createTree(parent, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 160;
        tree.setLayoutData(gd);
        
        // Create Viewer for Tree
        treeViewer = new TreeViewer(tree);         
        treeViewer.addSelectionChangedListener(this);
        
        // Set Content/Label Provider
        treeViewer.setContentProvider(new CasIndexRepoContentProvider());
        treeViewer.setLabelProvider(new CasIndexRepoLabelProvider(typesystemStyle));
        treeViewer.setSorter(new CasIndexRepoSorter());        
    } // createIndexRepositoryTreeViewer
    
    /**
     * An index is selected
     * 
     * @param event
     * @return void
     */
    public void selectionChanged (SelectionChangedEvent event) 
    {
        ISelection selection = event.getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection)selection;
            if (ssel.size() == 0 || !(ssel.getFirstElement() instanceof BaseNode)) {
                return;
            }

            final BaseNode _selectedTreeNode = (BaseNode) ssel.getFirstElement();
            if (_selectedTreeNode.getObjectType() == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                Object[] listeners = selectionListeners.getListeners();
                for (int i = 0; i < listeners.length; ++i) {
                    final IObjectSelectionListener l = (IObjectSelectionListener) listeners[i];
                    
                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            // see CASViewControl
                            l.objectSelected(CasIndexRepoSectionPart.this, _selectedTreeNode.getObject());
                        }
                    });
                    
                }                       
            }

        }
    }
        
    public void setInput(ICASObjectView casView, String title) {
        treeViewer.setInput(casView.getCasIndexRepository());
    }
    
//    public void setFSIndexSectionPart (FSIndexSectionPart fsIndexSectionPart)
//    {
//        this.fsIndexSectionPart = fsIndexSectionPart;
//        
//    }
    
    public void setDocumentInputName (String docName) 
    {
        inputDocName.setText(docName.substring(1+docName.lastIndexOf("\\")));
    }
    /*************************************************************************/
    
    public void refreshTypeSystemStyle ()
    {
        // treeViewer.refresh();
    }
    
    /*************************************************************************/
    /*                     IObjectSelectionListener                          */
    /*************************************************************************/
    
    private ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    
    /**
     * Note: CASViewControl is the listener
     */
    public void addSelectionListener(IObjectSelectionListener listener)
    {
        selectionListeners.add(listener);
    }
    
    public void removeSelectionListener(IObjectSelectionListener listener)
    {
        selectionListeners.remove(listener);
    }
    
}
