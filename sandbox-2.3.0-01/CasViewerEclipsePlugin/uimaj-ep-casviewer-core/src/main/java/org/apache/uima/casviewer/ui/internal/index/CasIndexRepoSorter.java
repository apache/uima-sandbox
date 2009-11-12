package org.apache.uima.casviewer.ui.internal.index;

import java.text.Collator;

import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.eclipse.jface.viewers.ViewerSorter;

public class CasIndexRepoSorter extends ViewerSorter {

    public CasIndexRepoSorter() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param collator
     */
    public CasIndexRepoSorter(Collator collator) {
        super(collator);
        // TODO Auto-generated constructor stub
    }
    
    public int category(Object element) 
    {
        if (element instanceof BaseNode) {
            int kind = ((BaseNode) element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX
                // || kind == ITEM_TYPE_LABEL_U_FS_INDEX
               ) {
                return 0;
            }
            
        }
        
        return 1;
    }

}
