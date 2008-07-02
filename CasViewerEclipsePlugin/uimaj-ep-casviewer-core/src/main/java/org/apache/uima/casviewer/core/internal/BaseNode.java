package org.apache.uima.casviewer.core.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.tools.debug.util.Trace;

// See also TreeNode
public class BaseNode implements Serializable {
  private static final long serialVersionUID = 3763098548568601397L;

  protected String label = null;
  protected BaseNode parent = null;
  protected List<BaseNode> childrenList = null;
  
  protected int             objectType = 0;
  protected Object          object = null;  

  /************************************************************************/

  public BaseNode() {
  }

  public BaseNode(String label, Object object, List list) {
      super();
      this.label  = label;
      this.object = object;
      childrenList = list;
  }

  /**
   * @return Returns the label.
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label The label to set.
   */
  public void setLabel(String label) {
    this.label = label;
  }
  
  /**
   * @return Returns the object.
   */
  public Object getObject() {
      return object;
  }
  
  /**
   * @param object The object to set.
   */
  public void setObject(Object object) {
      this.object = object;
  }
  
  /**
   * @param object The object to set.
   */
  public void setObject(Object object, int objectType) {
      this.object = object;
      this.objectType = objectType;
  }
  
  /**
   * @return Returns the objectType.
   */
  public int getObjectType() {
      return objectType;
  }
  /**
   * @param objectType The objectType to set.
   */
  public void setObjectType(int objectType) {
      this.objectType = objectType;
  }
 
  /**
   * @return Returns the parent.
   */
  public BaseNode getParent() {
    return parent;
  }
  
  /**
   * @param parent The parent to set.
   */
  public void setParent(BaseNode parent) {
    this.parent = parent;
  }

  /**
   * @return Returns the children.
   */
  public List getChildren() {
    return childrenList;
  }    

  public Object removeChild (BaseNode childNode) {
    if (childrenList == null) {
      Trace.err(label + " doesn't have children");
      return null;
    }
    if (childrenList.indexOf(childNode) == -1) {
      Trace.err(label + " doesn't have " + childNode.label + " as child");
      return null;
    }
    return childrenList.remove(childrenList.indexOf(childNode));
  }   

  public void addChild (BaseNode childNode) {
    if (childrenList == null) {
      return ;
    }
    childrenList.add (childNode);
  }


}
