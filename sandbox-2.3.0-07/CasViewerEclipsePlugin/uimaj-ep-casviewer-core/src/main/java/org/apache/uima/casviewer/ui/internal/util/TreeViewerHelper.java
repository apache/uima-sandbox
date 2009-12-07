package org.apache.uima.casviewer.ui.internal.util;

import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * 
 *
 */
public class TreeViewerHelper {
    
    /**
     * Refresh tree with preservation of checked state.
     * 
     * @param treeViewer
     * @return void
     */
    static public void refreshWithCheckedState (CheckboxTreeViewer treeViewer,
            boolean updateLabels)
    {
        Object[] objs = treeViewer.getCheckedElements();
        treeViewer.getTree().setRedraw(false);
        treeViewer.refresh(updateLabels);
        treeViewer.setCheckedElements(objs);
        treeViewer.expandToLevel(2);
        treeViewer.getTree().setRedraw(true);
    }

}
