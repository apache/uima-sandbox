package org.apache.uima.casviewer.ui.internal.style;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.core.internal.TypeTree;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;


public class ColoredTypeTreeContentProvider implements ITreeContentProvider {
    
    private static final int ITEM_TYPE_COLORED_TYPE          = 1000;
    private static final int ITEM_TYPE_TYPECOLOR             = 1010;
    private static final int ITEM_TYPE_TYPESTYLE             = 1020;
    
	private static Object[] EMPTY_ARRAY = new Object[0];
    private boolean         _includeFeature = true; 
    private boolean         _isTableTree    = true; // viewer is a table tree
    
    /*************************************************************************/
    public ColoredTypeTreeContentProvider () {
        super();
    }
    
    public ColoredTypeTreeContentProvider (boolean isTableTree) {
        super();
        _isTableTree = isTableTree;
    }
    
    public void includeFeature (boolean includeFeature) {
        _includeFeature = includeFeature;
    }
    
    public void setViewerType (boolean isTableTree)
    {
        _isTableTree = isTableTree;
    }
    /*************************************************************************/
    
    // With Java 5, use Formatter (or String.format)
    static private String toHexString (int i)
    {
        String hex = Integer.toHexString(i).toUpperCase();
        return hex.length()==1? "0"+hex : hex;
    }

    static private String toHexString (RGB rgb)
    {
        String r = Integer.toHexString(rgb.red).toUpperCase();
        r = r.length()==1? "0"+r : r;
        String g = Integer.toHexString(rgb.green).toUpperCase();
        g = g.length()==1? "0"+g : g;
        String b = Integer.toHexString(rgb.blue).toUpperCase();
        b = b.length()==1? "0"+b : b;
        
        return r+g+b;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren (Object parent) 
    {
	    if (parent instanceof List) {
            // List<TypeStyle> of default colors
            List list = (List) parent;
            if (list == null || list.size() == 0) {
                return EMPTY_ARRAY;
            }
            Object[] objs = new Object[list.size()];
            
            String      text = "";
            NumberFormat formatter = new DecimalFormat("00");
            for (int i=0; i<list.size(); ++i) {
                Color   bgColor = null;
                int     kind;
                if (list.get(i) instanceof TypeStyle) {
                    kind = ITEM_TYPE_TYPESTYLE;
                    text = ((TypeStyle) list.get(i)).getTypeName();
                    bgColor = ((TypeStyle) list.get(i)).getBackground();
                    
//                } else if (list.get(i) instanceof TypeColor) {
//                    TypeColor typeColor = (TypeColor) list.get(i);
//                    kind = ITEM_TYPE_TYPECOLOR;
//                    text = toHexString(typeColor.rgbForeground) + "  " 
//                         + toHexString(typeColor.rgbBackground);
//                    bgColor = typeColor.bgColor;
//                } else if (list.get(i) instanceof ColoredType) {
//                    kind = ITEM_TYPE_COLORED_TYPE;
//                    text = ((ColoredType) list.get(i)).getTypeName();
//                    if (((ColoredType) list.get(i)).getColor() != null) {
//                        bgColor = ((ColoredType) list.get(i)).getColor().bgColor;
//                    }
                } else {
                    continue;
                }
                TypeNode node = new TypeNode (null, text);
                node.setObjectType(kind);
                node.setObject(list.get(i));
                node.setBgColor(bgColor);
                objs[i] = node;
            }
            return objs;
            
        } else if (parent instanceof TypeSystemStyle) {
            Map map = ((TypeSystemStyle) parent).getStyledTypeMap();
            if (map == null || map.size() == 0) {
                Trace.err("getStyledTypeMap() == null");
                return EMPTY_ARRAY;
            }
            Object[] objs = new Object[map.size()];
            Iterator iter = map.entrySet().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                TypeNode node = new TypeNode (null, (String) e.getKey());
                node.setObjectType(ITEM_TYPE_TYPESTYLE);
                node.setObject(e.getValue());
                node.setBgColor(((TypeStyle) e.getValue()).getBackground());
                objs[i++] = node;
            }
            return objs;
            
            
        } else if (parent instanceof TypeNode) {
	        if ( ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE 
	          || ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_UNKNOW ) {
                TypeDescription t = (TypeDescription)((TypeNode)parent).getObject();
                // Trace.trace("TypeDescription.name: " + t.getName());
               
	            int		count = 0;
            	TypeNode fNode = null;
	            // Add "Features" or "Allowed values" Node
                	            
                // Count the number of types having annotations
                Object[] childrenArray = null;
                if ( !_isTableTree && ((TypeNode)parent).getChildren() != null ) {
                    childrenArray = ((TypeNode)parent).getChildrenArray();
                    for (int j=0; j<childrenArray.length ; ++j) {
                        TypeNode node = (TypeNode) childrenArray[j];
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
                    // count += childrenArray.length;
                } 
                // Trace.trace("TypeDescription.name: " + t.getName() + " has " + count + " children");
	            Object[] objs = new Object[count];
	            int index = 0;
	            if ( fNode != null ) {
	                objs[index++] = fNode;
	            }
	            
                if ( childrenArray != null ) {
                    for (int j=0; j<childrenArray.length ; ++j) {
//                        if ( ((TypeNode) childrenArray[j]).getFsTotal() > 0 ) {
//                            objs[index++] = (TypeNode) childrenArray[j];
//                        }
                        TypeNode node = (TypeNode) childrenArray[j];
                        if ( ((TypeDescription) node.getObject()).getName().endsWith("AnnotationBase") ) {
                            // "uima.cas.AnnotationBase"
                            if ( node.getChildren() != null ) {
                                 if ( ((TypeNode) node.getChildrenArray()[0]).getFsTotal() > 0 ) {
                                     objs[index++] = (TypeNode) node.getChildrenArray()[0];
                                 }
                            }
                        } else {
                            if (node.getFsTotal() > 0 ) {
                                objs[index++] = node;                            }                            
                        }
                    }                   
                }
	            return objs;
	        } else if ( ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
//	            List 	featureList = ((TypeDescription)((TypeNode)parent).getObject()).getFeatureList();
//                ArrayList featureNodeList = (ArrayList) ((TypeNode)parent).getChildren();
//                if (featureNodeList == null) {
//                    FeatureMetadata f;
//                    featureNodeList = new ArrayList();
//    	            for (int i=0; i<featureList.size(); ++i ) {
//    	                f = (FeatureMetadata) featureList.get(i);
//    	                // System.out.println("feature2:" + f.getShortName());
//                        TypeNode fNode = new TypeNode((TypeNode)parent, f.getName());
//    		            fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_FEATURE);
//    		            fNode.setObject(f);
//    		            featureNodeList.add(fNode);
//    	            }
//                    ((TypeNode)parent).setChildren(featureNodeList);
//                }
//	            return featureNodeList.toArray(new TypeNode[featureNodeList.size()]);

            // } else if ( ((TypeNode)parent).getObjectType() == IItemTypeConstants.ITEM_TYPE_CATEGORY ) {
                
            } else {
	            // System.out.println("getChildren - Unknown Type: " + ((TreeElementNode)parent).getObjectType());                
	            if ( ((TypeNode)parent).getChildren() != null ) {
                    return ((TypeNode)parent).getChildrenArray();
	            }
	        }            
        } else if ( parent instanceof TypeDescription ) {
            if (((TypeDescription) parent).getFeatures() != null) {
                return ((TypeDescription) parent).getFeatures();
            }            
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
        // Trace.trace();
        if (element instanceof TypeNode) {
            return ((TypeNode)element).getParent();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) 
    {
        if ( element instanceof TypeSystemStyle
                || element instanceof List ) {
            return true;
            
        } else if (element instanceof TypeNode
           || element instanceof TypeDescription) {
            if ( element instanceof TypeDescription
                    || ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE 
                    || ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_UNKNOW) {
                TypeDescription t;
                if ( element instanceof TypeDescription ) {
                    t = (TypeDescription) element;
                } else {
                    // Check if the type has "type" children
                    if ( ((TypeNode)element).getChildren() != null ) {
                        return true;
                    }
                    t = (TypeDescription)((TypeNode)element).getObject();
                }
                // Check if the type has "features"
//                if ( _includeFeature && t != null ) {
//                    List    featureList = t.getFeatureList();
//                    if ( featureList != null && featureList.size() > 0 ) {
//                        return true;
//                    }
//                }
            } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
                return true;
                // For Category
//            } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_CATEGORY ) {
//                if ( ((TypeNode)element).getChildren() != null ) {
//                    return true;
//                }
//                
//            // For View Mapping
//            } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_VIEWMAPPING ) {
//                if ( ((TypeNode)element).getChildren() != null ) {
//                    return true;
//                }
            }
        } else if ( element instanceof TypeTree ) {
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
        if ( inputElement instanceof TypeSystemStyle
                || inputElement instanceof List ) {
            return getChildren(inputElement);
            
        } else if ( inputElement instanceof TypeTree ) {
            if ( _isTableTree ) {
                // Trace.trace("_isTableTree");
                List<TypeNode> list = ((TypeTree) inputElement).getNodeListFromHierarchy(true, true);
                
                // Return ONLY types having annotations
                List<TypeNode>   typesHavingAnnotations = new ArrayList<TypeNode>();
                for (int i=0; i<list.size(); ++i) {
                    // Has annotations ?
                    if ( list.get(i).getFsTotal() > 0 ) {
                        typesHavingAnnotations.add(list.get(i));
                    }                    
                }
                return typesHavingAnnotations.toArray();
            } else {
                // TreeViewer
                // Trace.trace("getRoot().getLabel: " + ((TypeTree)inputElement).getRoot().getLabel());
                if ( TypeTree.isTypeSystemRootName(((TypeTree)inputElement).getRoot().getLabel()) ) {             
                    return getChildren(((TypeTree)inputElement).getRoot().getChildrenArray()[0]);
                    // return getChildren(((TypeTree)inputElement).getRoot());
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
        // TODO Auto-generated method stub        
    }


    /**
     * 
     *  newInput    List<TypeStyle>
     */                  
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//        if (newInput != null) {
//            Trace.trace("newInput: " + newInput.getClass().getName());
//        }        
    }

} // 
