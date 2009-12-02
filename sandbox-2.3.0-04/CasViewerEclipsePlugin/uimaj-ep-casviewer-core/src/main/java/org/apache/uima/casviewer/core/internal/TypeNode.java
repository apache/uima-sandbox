package org.apache.uima.casviewer.core.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.FeatureStructure;

public class TypeNode extends BaseNode {
    
 // Temporary hide from view. It is used by Journal to show types changed by AE(s)
    protected boolean disable = false; // Temporary hide from view
   	
    // Used to display "Features" label which contains the definition of features
    protected TypeNode featuresLabel = null;
    
    // Used to contains the annotations of this types
    // Possible classes:  AnnotationListNode, DiffAnnotationListNode
    //                    List<FeatureStructure>
    protected AnnotationObjectsNode annotationNode = null;
    
    // Attributes used by Cas Viewer
    protected int               fsCount = 0;    // number of FS for this type (NOT including number from sub-types)
    protected int               fsTotal = 0;    // number of FS for this type (including number from sub-types)
    protected Object            bgColor = null; // background color (Color)
    protected boolean           hidden = false;
    
    /************************************************************************/

	public TypeNode () {
      super();
	}
	
	public TypeNode (TypeNode parent, String label) {
        super();
		this.parent = parent;
		this.label 	= new String(label);
	}
	
	public TypeNode (TypeNode parent, String label, int objType, Object obj) {
	    super();
	    this.parent = parent;
	    this.label  = new String(label);
	    this.objectType = objType;
	    this.object = obj;
	}

    
	@Deprecated
    public void addChildren(TypeNode[] list) {
        if (childrenList == null) {
            childrenList = new ArrayList();
        }
        // Set parent node for all children to "this"
        for (int i=0; i<list.length; ++i) {
            this.childrenList.add(list[i]);
            list[i].setParent(this);
        }
    }
    
    public void addChildren(List<TypeNode> list) {
        if (childrenList == null) {
            childrenList = new ArrayList();
        }
        childrenList.addAll(list);
        // Set parent node for all children to "this"
        for (TypeNode node: list) {
            node.setParent(this);
        }
    }
	
    public Iterator getChildrenIterator () {
        if (childrenList == null) return null;
        return childrenList.iterator();
    }

    public Object[] getChildrenArray() {
        if (childrenList == null || childrenList.size() == 0) {
            return null;
        }
        return (TypeNode [])childrenList.toArray(new TypeNode[childrenList.size()]);
    }    
     
    public int getChildrenCount () {
        return (childrenList == null? 0:childrenList.size());
    }

    public TypeNode getChildAt(int index) {
      if (childrenList == null || index >= childrenList.size()) {
          return null;
      }
      return (TypeNode)childrenList.get(index);
  }    

	
	public TypeNode insertChild (TypeNode childNode) {
		if (childrenList == null) {
		    childrenList = (List<BaseNode>) new ArrayList();
		    // children = new TreeMap(new MyComparator(false));
		} else {
			// Check duplication
			// if (children.containsKey(childNode.label)) {
			if (childrenList.contains(childNode)) {
				// System.out.println("[insertChild] Duplicate Label:" + childNode.label);
				return (TypeNode) childrenList.get(childrenList.indexOf(childNode));
			}
		}
		// children.put (childNode.label, childNode);
		childrenList.add (childNode);
		childNode.parent = this;
		return null;
	}
    
    /**
     * @return the bgColor
     */
    public Object getBgColor() {
        return bgColor;
    }

    /**
     * @param bgColor the bgColor to set
     */
    public void setBgColor(Object bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @return the grayed
     */
//    public boolean isGrayed() {
//        return grayed;
//    }

    /**
     * @param grayed the grayed to set
     */
//    public void setGrayed(boolean grayed) {
//        this.grayed = grayed;
//    }

    /**
     * @return Returns the fsTotal.
     */
    public int getFsTotal() {
        return fsTotal;
    }

    /**
     * @param fsTotal The fsTotal to set.
     */
    public void setFsTotal(int fsTotal) {
        this.fsTotal = fsTotal;
    }

    /**
     * @return Returns the firstChild.
     */
    public TypeNode getFirstChild() {
        return featuresLabel;
    }

    /**
     * @param firstChild The firstChild to set.
     */
    public void setFirstChild(TypeNode firstChild) {
        this.featuresLabel = firstChild;
    }

    /**
     * @param children The children to set.
     */
    public void setChildren(List children) {
        this.childrenList = children;
    }

    /**
     * @return the fsCount
     */
    public int getFsCount() {
        return fsCount;
    }

    /**
     * @param fsCount the fsCount to set
     */
    public void setFsCount(int fsCount) {
        this.fsCount = fsCount;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the annotationNode
     */
    public AnnotationObjectsNode getAnnotationNode() {
        return annotationNode;
    }

    /**
     * @param annotationNode the annotationNode to set
     */
    public void setAnnotationNode(AnnotationObjectsNode annotationNode) {
        this.annotationNode = annotationNode;
    }

    /**
     * @return the disable
     */
    public boolean isDisable() {
        return disable;
    }

    /**
     * @param disable the disable to set
     */
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    
} // TypeNode
