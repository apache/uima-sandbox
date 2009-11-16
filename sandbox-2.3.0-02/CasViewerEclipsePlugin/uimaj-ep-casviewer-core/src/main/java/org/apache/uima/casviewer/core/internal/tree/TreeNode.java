package org.apache.uima.casviewer.core.internal.tree;

import java.util.ArrayList;
import java.util.List;


public class TreeNode {
    
    private final static int ITEM_TYPE_UNKNOW = 0;
    
    private String          label;
    private TreeNode        parent;
    private List            children;
    private Object          object;
    private int             objectType    = ITEM_TYPE_UNKNOW; // default
    
    public TreeNode(String label, Object object) {
        super();
        this.label  = label;
        this.object = object;
        children = new ArrayList();
    }
    public void addChild(TreeNode child) {
        children.add(child);
        child.setParent(this);
    }
    public void removeChild(TreeNode child) {
        children.remove(child);
        child.setParent(null);
    }
    
    public TreeNode [] getChildren() {
        return (TreeNode [])children.toArray(new TreeNode[children.size()]);
    }
    public boolean hasChildren() {
        return children.size()>0;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public TreeNode getParent() {
        return parent;
    }
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    public int getObjectType() {
        return objectType;
    }
    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
