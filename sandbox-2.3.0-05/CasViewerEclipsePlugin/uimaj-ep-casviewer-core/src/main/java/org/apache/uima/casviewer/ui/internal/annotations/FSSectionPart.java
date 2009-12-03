package org.apache.uima.casviewer.ui.internal.annotations;

import org.apache.uima.casviewer.ui.internal.index.FSIndexLabelProvider;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.apache.uima.casviewer.ui.internal.util.FormSection;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;


public class FSSectionPart extends AbstractSectionPart {

    static private FSSectionPart  _instance = null;
    
    private Tree                    tree = null;
    private TreeViewer              treeViewer = null;
    private FSIndexLabelProvider    labelProvider;
    private TypeSystemStyle    typesystemStyle;
    
    public FSSectionPart(Section section) {
        super(section);
    }

    protected FSSectionPart (IManagedForm managedForm, Composite parent,
            Section section, int style, String title, String description)
    {
        super(managedForm, parent, section, style, title, description);
        _instance       = this;
       
        managedForm.addPart(this);
        initialize (managedForm);  // Need this code. Otherwise, exception in SectionPart !!!
    }
    
    static public FSSectionPart createInstance (IManagedForm managedForm, 
            Composite parent, int style, String title, String description,
            TypeSystemStyle tsStyle)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = FormSection.createGridDataSection(toolkit, parent,
                style, title, description, 5, 8,
                GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING,
                1, 1);
                
        FSSectionPart sectionPart = new FSSectionPart (managedForm, parent, section, style, title, description);
        sectionPart.typesystemStyle = tsStyle;
        sectionPart.createContents();
                
        return sectionPart;
    }
    
    /*************************************************************************/
    
    public void setTypeSystemStyle (TypeSystemStyle tsStyle)
    {
        labelProvider.setTypeSystemStyle(tsStyle);
    }
    
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
        
        section.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                // Trace.err();
                // TODO Resize
//                managedForm.reflow(true);
//                managedForm.refresh();
            }
        });
        
        ///////////////////////////////////////////////////////////////////////
        
        // Create Composite
        Composite client = FormSection.createGridLayoutContainer (toolkit, section,
                1, 0, 0);
        client.setLayoutData(new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING));
        
        toolkit.paintBordersFor(client);        
        section.setClient(client);   
        
        /*********************************************************************/    
        
        // Create Tree for Type System
        // tree = toolkit.createTree(client, SWT.CHECK);
        tree = toolkit.createTree(client, SWT.NONE);
        // final TableTree tree = new TableTree (client, SWT.NULL);
        GridData gd = new GridData(GridData.FILL_BOTH|GridData.VERTICAL_ALIGN_BEGINNING);
        gd.grabExcessVerticalSpace   = true;
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 160;
        tree.setLayoutData(gd);
        
        // Create Viewer for Tree
        treeViewer = new TreeViewer(tree);
        // Set Content/Label Provider
        treeViewer.setContentProvider(new FSContentProvider());
        FSIndexLabelProvider labelProvider = new FSIndexLabelProvider(typesystemStyle);
        treeViewer.setLabelProvider(labelProvider); 

        // FIXME Cannot use setFilters() (valid for Eclipse 3.3)
        FeatureViewerFilter[] f = new FeatureViewerFilter[1];
        f[0] =  new FeatureViewerFilter();
        treeViewer.addFilter(f[0]);
//        treeViewer.setFilters(new ViewerFilter[] { new ViewerFilter() {
//            @Override
//            public boolean select(Viewer viewer, Object parentElement, Object element) {
//                // Trace.err(element.getClass().getName());
//                if (element instanceof UFeature) {
//                    if ( ((UFeature) element).getShortName().equals("begin") ) {
//                        // Trace.err("Skip: " + ((UFeature) element).getShortName());
//                        return false;
//                    }            
//                }
//                return true;
//            }
//        }});
        return section;
    }
    
    /*************************************************************************/

    /**
     * model is a List of UFeatureStructure
     * Note: If model == null, clear TreeViewer
     */
    public void setInput (Object model, String aTitle) {
        // work-around for UIMA bug in FeatureStructureImplC.equal (CastTypeException)
        treeViewer.getTree().removeAll();
        treeViewer.setInput(model);
        if (model != null) {
            treeViewer.expandToLevel(2);
        }
    }

} // FeatureStructureSectionPart
