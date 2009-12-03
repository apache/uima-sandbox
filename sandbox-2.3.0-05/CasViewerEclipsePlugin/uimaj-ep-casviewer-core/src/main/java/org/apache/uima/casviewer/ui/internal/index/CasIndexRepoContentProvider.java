package org.apache.uima.casviewer.ui.internal.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.CasIndexRepository;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.UFSIndex;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CasIndexRepoContentProvider implements ITreeContentProvider {

    private boolean showAllIndexes = false; // Show all indexes (even do not have annotations)

    public CasIndexRepoContentProvider() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parent) 
    {
        if (parent instanceof BaseNode) {
            int kind = ((BaseNode)parent).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                UFSIndex index = (UFSIndex) ((BaseNode)parent).getObject();
                // Has FS ?
                if (index.size() > 0) {   
                    // Return nodes of sub-indexes (children UFSIndex)
                    if (((BaseNode)parent).getChildren() != null) {
                        if (showAllIndexes) {
                            // Show all indexes
                            return ((BaseNode)parent).getChildren().toArray();
                        } else {
                            // Count indexes having FS
                            List<BaseNode> nodes = ((BaseNode)parent).getChildren();
                            int count = 0;
                            for (BaseNode node: nodes) {
                                if ( ((UFSIndex) node.getObject()).size() > 0 ) {
                                    ++count;
                                }
                            }
                            if (count > 0) {
                                Object[] objs = new Object[count];
                                int k =0;
                                for (BaseNode node: nodes) {
                                    if ( ((UFSIndex) node.getObject()).size() > 0 ) {
                                        objs[k++] = node;
                                    }
                                }
                                return objs;
                            }
                            
                        }
                    }
                }                
            } else if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX) {
                UFSIndex index = (UFSIndex) ((BaseNode)parent).getObject();
                if (index.size() == 0) { 
                    return null;
                }
                FSIterator it = index.iterator();
                
                Object[] objs = new Object[index.size()];
                int i = 0;
                for (it.moveToFirst(); it.isValid(); it.moveToNext()) {
                    // FeatureStructure fs = it.get();
                    objs[i] = new UFeatureStructure(it.get(), i);
                    ++i;
                }
                return objs;
            }
            
        } else if (parent instanceof UFSIndex) {
            UFSIndex index = (UFSIndex) parent;
            if (index.size() == 0) { 
                return null;
            }
            FSIterator it = index.iterator();
            
            Object[] objs = new Object[index.size()];
            int i = 0;
            for (it.moveToFirst(); it.isValid(); it.moveToNext()) {
                // FeatureStructure fs = it.get();
                objs[i] = new UFeatureStructure(it.get(), i);
                ++i;
            }
            return objs;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) 
    {
        if (element instanceof BaseNode) {
            // Check item type
            int kind = ((BaseNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                UFSIndex index = (UFSIndex) ((BaseNode)element).getObject();
                // Has FS and sub-indexes ?
                if ( index.size() > 0 && ((BaseNode)element).getChildren().size() > 0 ) {
                    if (showAllIndexes) {
                        return true;
                    } else {
                        // Show sub-indexes having FS
                        List<BaseNode> nodes = ((BaseNode)element).getChildren();
                        int count = 0;
                        for (BaseNode node: nodes) {
                            if ( ((UFSIndex) node.getObject()).size() > 0 ) {
                                ++count;
                            }
                        }
                        if (count > 0) {
                            return true;
                        }
                    }
                }
            } else if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX) {
                return true;
            }
        } else if (element instanceof UFSIndex) {
            if ( ((UFSIndex) element).size() > 0 ) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) 
    {
        if ( inputElement instanceof CasIndexRepository ) {
            BaseNode root = ((CasIndexRepository) inputElement).getRoot();
            if (root != null) {
                /*      TreeNode
                *          - label     : type's name
                *          - obj type  : ITEM_TYPE_U_FS_INDEX
                *          - object    : UFSIndex
                */
                return root.getChildren().toArray();
            }
        } else if ( inputElement instanceof CAS ) {
            CAS aCas = (CAS) inputElement;
            FSIndexRepository ir = aCas.getIndexRepository();
            Iterator it = ir.getLabels();
            List list = new ArrayList();
            while (it.hasNext()) {
                String label = (String) it.next();
                FSIndex index1 = ir.getIndex(label);
                list.add(index1);
                Trace.trace("label: " + label + " ; type: " + index1.getType()
                        + " ; size: " + index1.size());
            }
            return list.toArray(new FSIndex[list.size()]);
            
        } else if ( inputElement instanceof FSIndex ) {
            // Add label for FSIndex
            BaseNode fNode = new BaseNode(((FSIndex) inputElement).getType().getName(), null, new ArrayList());
            fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_FS_INDEX);
            fNode.setObject(inputElement);
            Object[] objs = new Object[1];
            objs[0] = fNode;
            return objs;
            
        } else if ( inputElement instanceof UFSIndex ) {
            // Add label for FSIndex
            BaseNode fNode = new BaseNode(((UFSIndex) inputElement).getUimaFSIndex().getType().getName(), null, new ArrayList());
            fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX);
            fNode.setObject(inputElement);
            Object[] objs = new Object[1];
            objs[0] = fNode;
            return objs;
        }
        Trace.trace("Unknown inputElement: " + inputElement.getClass().getName());
        return new Object[0];
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // input is "CasIndexRepository"

    }

}
