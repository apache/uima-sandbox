package org.apache.uima.casviewer.viewer.internal;

import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
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
//      PluginSelectionDialog dialog = new PluginSelectionDialog(fViewer
//              .getControl().getShell(), true, false);
//      dialog.create();
//      if (dialog.open() == Window.OK) {
//          handleFocusOn(dialog.getFirstResult());
//      }
        
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
            if (resource instanceof IContainer) {
                Trace.trace("CONTAINER: ");
            }
            String arg = resource.getFullPath().toString();
            // String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
            // Trace.trace("resource.getFullPath().toString():" + arg);
            // Trace.trace(fileLoc);
            // selectedElement = resource.getLocation().toOSString();
        }
        return resource;
    }


    public static IResource getWorkspaceResourceElement_REMOVE (Shell shell, IResource root) 
    {
        IResource resource = null;
//      PluginSelectionDialog dialog = new PluginSelectionDialog(fViewer
//              .getControl().getShell(), true, false);
//      dialog.create();
//      if (dialog.open() == Window.OK) {
//          handleFocusOn(dialog.getFirstResult());
//      }
        
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
            if (resource instanceof IContainer) {
                Trace.trace("CONTAINER: ");
            }
            String arg = resource.getFullPath().toString();
            // String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
            // Trace.trace("resource.getFullPath().toString():" + arg);
            // Trace.trace(fileLoc);
            // selectedElement = resource.getLocation().toOSString();
        }
        return resource;
    }

    public static IResource getWorkspaceResourceElement (Shell shell) 
    {
        IResource resource = null;
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, 
                new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        dialog.setTitle("Select Cpe descriptor"); 
        dialog.setMessage("Select Cpe Xml descriptor file"); 
        dialog.setInput(ResourcesPlugin.getWorkspace().getRoot()); 
        dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
        int buttonId = dialog.open();
        if (buttonId == IDialogConstants.OK_ID) {
            resource = (IResource) dialog.getFirstResult();
            if (!resource.isAccessible()) {
                return null;
            }
            if (resource instanceof IContainer) {
                Trace.trace("CONTAINER: ");
            }
            String arg = resource.getFullPath().toString();
            // String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
            // Trace.trace(fileLoc);
            // selectedElement = resource.getLocation().toOSString();
        }
        return resource;
    }
    
}
