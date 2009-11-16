package org.apache.uima.casviewer.ui.internal.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

/**
 * Avoid "gray" out parent node when child node is checked.
 * 
 *
 */
public class CustomCheckedTreeViewer extends ContainerCheckedTreeViewer 
{
    public CustomCheckedTreeViewer(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub
    }

    public CustomCheckedTreeViewer(Tree tree) {
        super(tree);
    }
    
    /**
     * Update element after a checkstate change.
     * @param element
     */
    protected void doCheckStateChanged(Object element) {
        Widget item = findItem(element);
        if (item instanceof TreeItem) {
            // ((TreeItem)item).setGrayed(false);
            updateChildrenItems((TreeItem)item);
        }
    }


    /**
     * Updates the check state of all created children
     */
    private void updateChildrenItems(TreeItem parent) {
        Item[] children = getChildren(parent);
        boolean state = parent.getChecked();
        for (int i = 0; i < children.length; i++) {
            TreeItem curr = (TreeItem) children[i];
            if (curr.getData() != null
                    && ((curr.getChecked() != state) /* || curr.getGrayed()*/)) {
                curr.setChecked(state);
                // curr.setGrayed(false);
                updateChildrenItems(curr);
            }
        }
    }
}
