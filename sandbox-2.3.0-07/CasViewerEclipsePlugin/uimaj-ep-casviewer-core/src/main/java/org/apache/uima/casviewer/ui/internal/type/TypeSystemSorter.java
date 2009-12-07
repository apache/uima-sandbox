package org.apache.uima.casviewer.ui.internal.type;

import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.eclipse.jface.viewers.ViewerSorter;

public class TypeSystemSorter extends ViewerSorter {

    public TypeSystemSorter() {
        super();
    }

    public int category(Object element) {
        if (element instanceof TypeNode) {
            if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) { 
                return 0;
            }
        }
        return 1;
    }
    
    
}
