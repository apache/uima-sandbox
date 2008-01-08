/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 8, 2006, 5:47:09 PM
 * source:  ElementTreeDialog.java
 */
package org.apache.uima.tools.internal.uima.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceSorter;


/**
 * 
 *
 */
public class WorkspaceResourceDialog 
{
  
    public static IResource getWorkspaceResourceElement (Shell shell, IResource root,
                                    String dialogTitle, String dialogMessage) 
    {
        IResource resource = null;
        
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, 
                new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        dialog.setTitle(dialogTitle); 
        dialog.setMessage(dialogMessage); 
        dialog.setInput(root); 
        dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
        int buttonId = dialog.open();
        if (buttonId == IDialogConstants.OK_ID) {
            resource = (IResource) dialog.getFirstResult();
            if (!resource.isAccessible()) {
                return null;
            }
//            String arg = resource.getFullPath().toString();
//            String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
        }
        return resource;
    }

    public static IResource getWorkspaceResourceElement_REMOVE (Shell shell, IResource root) 
    {
        IResource resource = null;
        
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, 
                new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        dialog.setTitle("Select UIMA descriptor"); 
        dialog.setMessage("Select UIMA Xml descriptor file"); 
        dialog.setInput(root); 
        dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
        int buttonId = dialog.open();
        if (buttonId == IDialogConstants.OK_ID) {
            resource = (IResource) dialog.getFirstResult();
            if (!resource.isAccessible()) {
                return null;
            }
//            String arg = resource.getFullPath().toString();
//            String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
        }
        return resource;
    }

}
