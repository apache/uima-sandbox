package org.apache.uima.uct.viewer.internal.page;

import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;


/**
 *  A CTab that contains a CASViewControl
 *
 */
public class CASViewTab 
{
    
    protected CTabItem          tabItem;
    protected CASViewControl    casViewControl;

    /**
     * 
     */
    public CASViewTab (CTabItem tabItem, CASViewControl control) {
        this.tabItem        = tabItem;
        this.casViewControl = control;
    }
    
    public void dispose ()
    {
        if (casViewControl != null) {
            casViewControl.dispose();
            casViewControl = null;
        }
        tabItem.dispose();
        tabItem = null;        
    }
    
    static public CASViewTab createCASViewTab (IManagedForm managedForm, CTabFolder tabFolder,
            String tabTitle, ICASObjectView casViewerObject)
    {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
        tabItem.setText(tabTitle); //$NON-NLS-1$       
                
        /*********************************************************************/

        CASViewControl control = CASViewControl.createInstance (null, null, managedForm, tabFolder, 
                casViewerObject, null, null);
        control.createContents();
        Composite tabBody = control.getControl();
        
        tabBody.setLayoutData(new GridData(GridData.FILL_BOTH));
        tabItem.setControl(tabBody);      
        
        return new CASViewTab(tabItem, control);
    }

    /**
     * @return the casViewControl
     */
    public CASViewControl getCasViewControl() {
        return casViewControl;
    }

    /**
     * @return the tabItem
     */
    public CTabItem getTabItem() {
        return tabItem;
    }
    

}
