package org.apache.uima.casviewer.ui.internal.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.resource.metadata.AllowedValue;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 *
 * TODO Will need to remember the tree's nodes to be able to update the node
 *      with new annotation (or delete annotation)
 */
public class TypesTreeContentProvider implements ITreeContentProvider {
    
	private static Object[] EMPTY_ARRAY = new Object[0];
    private boolean         _includeFeature = false; 
    private boolean         useFlatLayout   = true;
    
    private TypeTree        uimaTypeTree;   // Type tree to be displayed
    private TypeNode        topNode;        // node of TOP
    private boolean         showAllTypes = false; // Show all types (even do not have annotations)
    private TypeSystemStyle typesystemStyle;
    
    /*************************************************************************/
    
    public TypesTreeContentProvider () {
        super();
    }
    
    public TypesTreeContentProvider (boolean flat) {
        super();
        useFlatLayout = flat;
    }
    
    public void setTypeSystemStyle (TypeSystemStyle style)
    {
        this.typesystemStyle = style;
    }
    
    
    public void hideFeature (boolean hideFeature) {
        _includeFeature = ! hideFeature;
    }
    
    public void setViewerLayoutToFlatOrTree (boolean flat)
    {
        useFlatLayout = flat;
    }
    
    public void setShowAllTypes (boolean show) {
        showAllTypes = show;
    }
    
    public boolean isShowAllTypes () {
        return showAllTypes;
    }
    
    /*************************************************************************/
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren (Object parent) 
    {
	    if (parent instanceof TypeNode) {
	        if ( ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE 
	          || ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_UNKNOW ) {
                TypeDescription t = (TypeDescription)((TypeNode)parent).getObject();

//                TypeNode[] nodes = (TypeNode[]) ((TypeNode)parent).getChildrenArray();
//                if (nodes != null) {
//                    // return nodes;                       
//                }
               
                //
                // Create children nodes
                //
	            int		count = 0;
            	TypeNode fNode = null;
            	
	            // Add "Features" or "Allowed values" Node                
	            if ( _includeFeature && t != null ) {
	            	FeatureDescription[] featureList = t.getFeatures();
	            	if ( featureList != null && featureList.length > 0 ) {
                        fNode = ((TypeNode)parent).getFirstChild();
                        if (fNode == null) {
                            String label = "Features";

    	            		// Display "features" node
    	            		fNode = new TypeNode((TypeNode)parent, label);
    	            		fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES);
    	            		fNode.setObject(t);
                            ((TypeNode)parent).setFirstChild(fNode);
                        }
	            		count = 1;
	            	} else {
	            	    // TODO Add "AllowedValue" support
	            	    AllowedValue[] a = t.getAllowedValues();
                        // label = "Allowed values";
	            	}
	            }
	            
                // Count the number of sub-types having annotations
                Object[] childrenArray = null;
                if (!useFlatLayout && ((TypeNode)parent).getChildren() != null ) {
                    // Show Tree layout
                    // Get sub-types as array
                    childrenArray = ((TypeNode)parent).getChildrenArray();
                    if (showAllTypes) {
                        count += childrenArray.length;
                    } else {
                        count += countTypesHavingAnnotation(childrenArray);
                    }
                } 
                // Trace.trace("TypeDescription.name: " + t.getName() + " has " + count + " children");
	            Object[] objs = new TypeNode[count];
	            int index = 0;
	            if ( fNode != null ) {
	                objs[index++] = fNode; // Label for feature or allowed values
	            }
	            
                if ( childrenArray != null ) {
                    for (int j=0; j<childrenArray.length ; ++j) {
                        TypeNode node = (TypeNode) childrenArray[j];
                        if ( ((TypeDescription) node.getObject()).getName().endsWith("AnnotationBase") ) {
                            // "node" is "uima.cas.AnnotationBase"
                            // Add children of "uima.cas.AnnotationBase"
                            if ( node.getChildren() != null ) {
                                 if ( showAllTypes || ((TypeNode) node.getChildrenArray()[0]).getFsTotal() > 0 ) {
                                     objs[index++] = (TypeNode) node.getChildrenArray()[0];
                                 }
                            }
                        } else {
                            if (showAllTypes || node.getFsTotal() > 0 ) {
                                // Trace.err("node.getFsTotal(): " + node.getFsTotal());
                                objs[index++] = node;                            
//                            } else {
//                                Trace.err("NO annotation");
                            }
                        }
                    }                   
                }
//                if (objs.length > 0) {
//                    // ((TypeNode) parent).addChildren((TypeNode[])objs);
//                }
	            return objs;
	        } else if ( ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
	            TypeDescription type = (TypeDescription)((TypeNode)parent).getObject();
                FeatureDescription[] featureList = type.getFeatures();
                if ( featureList != null && featureList.length > 0 ) {
                    List featureNodeList = (ArrayList) ((TypeNode)parent).getChildren();
                    if (featureNodeList == null) {
                        // Not yet created. Create a new one
                        FeatureDescription f;
                        featureNodeList = new ArrayList();
        	            for (int i=0; i<featureList.length; ++i ) {
        	                f = (FeatureDescription) featureList[i];
        	                // System.out.println("feature2:" + f.getShortName());
                            TypeNode fNode = new TypeNode((TypeNode)parent, f.getName());
        		            fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_FEATURE);
        		            fNode.setObject(f);
        		            featureNodeList.add(fNode);
        	            }
                        ((TypeNode)parent).setChildren(featureNodeList);
                    }
    	            return featureNodeList.toArray(new TypeNode[featureNodeList.size()]);
                }
            } else {
	            // System.out.println("getChildren - Unknown Type: " + ((TreeElementNode)parent).getObjectType());                
	            if ( ((TypeNode)parent).getChildren() != null ) {
                    return ((TypeNode)parent).getChildrenArray();
	            }
	        }            
        } else if ( parent instanceof TypeDescription ) {
//            if (((TypeDescription) parent).getFeatureList() != null) {
//                return ((TypeDescription) parent).getFeatureList().toArray();
//            }
//	    } else if (parent instanceof FeatureMetadata
//                || parent instanceof TreeFeatureNode) {
            
        } else if (parent instanceof TypeTree) {
            Object[] objs = new Object[1];
            objs[0] = ((TypeTree)parent).getRoot ();
            return objs;
            
        } else {
            Trace.err("Unknow parent: " + parent.getClass().getName());
	    }
	    
        return EMPTY_ARRAY;
    } // getChildren

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) 
    {
        if (element instanceof TypeNode) {
            return ((TypeNode)element).getParent();
//        } else if (element instanceof TreeFeatureNode) {
//            return ((TreeFeatureNode)element).getParent();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) 
    {
        if (element instanceof TypeNode
           || element instanceof TypeDescription) {
            if ( element instanceof TypeDescription
                    || ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE 
                    || ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_UNKNOW) {
                TypeDescription t;
                if ( element instanceof TypeDescription ) {
                    t = (TypeDescription) element;
                } else {
                    // Check if the type has "type" children
                    if ( !useFlatLayout && ((TypeNode)element).getChildren() != null ) {
                        // Get sub-types as array
                        if (showAllTypes || countTypesHavingAnnotation(
                                 ((TypeNode)element).getChildrenArray()) > 0 ) {
                            return true;
                        }
                    }
                    t = (TypeDescription)((TypeNode)element).getObject();
                }
                // Check if the type has "features"
                if ( _includeFeature && t != null ) {
//                    List    featureList = t.getFeatureList();
//                    if ( featureList != null && featureList.size() > 0 ) {
//                        return true;
//                    }
                }
            } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
                return true;
            }
        } else if ( element instanceof TypeTree ) {
            Trace.trace();
        	return true;
        } else {
            Trace.err("Unknow element: " + element.getClass().getName());
        }
        
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) 
    {
        if (inputElement == null) {
            return EMPTY_ARRAY;
        }
        
        // Trace.trace(inputElement.getClass().getName());
        if ( inputElement instanceof TypeTree ) {
            if ( useFlatLayout ) {
                // Get node for TOP and remove "AnnotationBase" if any
                TypeNode top = (TypeNode) ((TypeTree)inputElement).getRoot().getChildrenArray()[0];
                TypeNode annotationBaseNode = null;
                for (int i=0; i<top.getChildrenArray().length; ++i) {
                    TypeNode node = (TypeNode) top.getChildrenArray()[i];
                    if ( ((TypeDescription) node.getObject()).getName().endsWith("uima.tcas.AnnotationBase") ) {
                        // "node" is "uima.cas.AnnotationBase"
                        annotationBaseNode = node;
                        break;
                    }
                }
                
                // Get the list of TypeNode from the type tree
                List<TypeNode> list = ((TypeTree) inputElement).getNodeListFromHierarchy(true, true);                
                if (showAllTypes) {
                    // Return all types (even do NOT have annotations)
                    return list.toArray();
                }
                // Return ONLY nodes having annotations
                ArrayList   typesHavingAnnotations = new ArrayList();
                for (int i=0; i<list.size(); ++i) {
                    // Has annotations ?
                    if ( !list.get(i).isHidden() && list.get(i).getFsCount() > 0 
                         && !list.get(i).isDisable()) {
                        typesHavingAnnotations.add(list.get(i));
                    }  
                }
                return typesHavingAnnotations.toArray();
            } else {
                // TreeViewer
                // Trace.trace("getRoot().getLabel: " + ((UimaTypeTree)inputElement).getRoot().getLabel());             
                if (topNode == null) {
                    topNode = new TypeNode(null, "");
                    topNode.setObjectType(IItemTypeConstants.ITEM_TYPE_TYPE_SYSTEM);
                    topNode.setObject(inputElement);                   
                }
                if ( TypeTree.isTypeSystemRootName(((TypeTree)inputElement).getRoot().getLabel()) ) {  
                    // Root of UimaTypeTree is "SUPER"
                    // Return the children of TOP
                    TypeNode[] objs = ( TypeNode[]) topNode.getChildrenArray();
                    if (objs == null) {
                        // 1st time. Create children of TOP
                        
                        // Get node for TOP
                        TypeNode top = (TypeNode) ((TypeTree)inputElement).getRoot().getChildrenArray()[0];
                        int size = top.getChildrenArray().length;

                        // Return ONLY nodes having annotations
                        List<TypeNode>   typesHavingAnnotations = new ArrayList<TypeNode>();
                        int count = 0;
                        for (int i=0; i<size; ++i) {
                            TypeNode node = (TypeNode) top.getChildrenArray()[i];
                            if ( ((TypeDescription) node.getObject()).getName().endsWith("AnnotationBase") ) {
                                // Add "uima.cas.AnnotationBase"
                                typesHavingAnnotations.add((TypeNode) node.getChildrenArray()[0]);
                            } else {
                                if ( node.getFsTotal() > 0 ) {
                                    typesHavingAnnotations.add(node);
                                }
                            }
                        }
                        // topNode.addChildren((TypeNode[])typesHavingAnnotations.toArray(new TypeNode[typesHavingAnnotations.size()]));                
                        topNode.addChildren(typesHavingAnnotations);                
                        return topNode.getChildrenArray();

                    }
                    // return getChildren(((UimaTypeTree)inputElement).getRoot().getChildrenArray()[0]);
                    return objs;
                } else {
                    return getChildren(inputElement);
                }
            }
        } else {
            Trace.err("Unknow input: " + inputElement.getClass().getName());
        }
        return EMPTY_ARRAY;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /**
     * 
     *  newInput         instance of TypeTree
     */   
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput != null) {
            if ( newInput instanceof TypeTree) {
                uimaTypeTree = (TypeTree) newInput;
            } else {
                Trace.trace(" UNKNOWN newInput: " + newInput.getClass().getName());
            }
        }       
    }
    
    /*************************************************************************/
    /*                              Helpers                                  */
    /*************************************************************************/
    
    /**
     *  Calculate the number of types having annotations
     *  
     * @param nodes     An Array of TypeNode
     * @return int      Return the number of types having annotations
     */
    private int countTypesHavingAnnotation (Object[] nodes)
    {
        int count = 0;
        for (int j=0; j<nodes.length ; ++j) {
            TypeNode node = (TypeNode) nodes[j];
            if ( ((TypeDescription) node.getObject()).getName().endsWith("AnnotationBase") ) {
                // "uima.cas.AnnotationBase"
                if ( node.getChildren() != null ) {
                     if ( ((TypeNode) node.getChildrenArray()[0]).getFsTotal() > 0 ) {
                         count++;
                     }
                }
            } else {
                if (node.getFsTotal() > 0 ) {
                    count++;
                }                   
            }
        }
        return count;
    }

}
