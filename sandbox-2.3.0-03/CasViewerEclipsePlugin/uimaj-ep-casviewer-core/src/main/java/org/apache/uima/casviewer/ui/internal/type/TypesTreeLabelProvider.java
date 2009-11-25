package org.apache.uima.casviewer.ui.internal.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class TypesTreeLabelProvider extends LabelProvider 
									 implements IColorProvider, IFontProvider  {
	private StructuredViewer   _viewer          = null;
	private Map 			   imageCache      = new HashMap(11);
    private boolean            useFlatLayout = true;
	private boolean            _showFullName 	= false; // If true, will show "full name"
	private TypeSystemStyle    typesystemStyle;
    
	protected TypesTreeLabelProvider () {
	}
	
	public TypesTreeLabelProvider (StructuredViewer viewer) {
		super();
		_viewer = viewer;
	}

	public TypesTreeLabelProvider (StructuredViewer viewer, boolean showFullName, 
	        boolean flat) {
		super();
		_viewer = viewer;
		_showFullName = showFullName;
		useFlatLayout = flat;
	}
        	
    public void setTypeSystemStyle (TypeSystemStyle style)
    {
        this.typesystemStyle = style;
    }
    
    public void setViewerLayoutToFlatOrTree (boolean flat)
    {
        useFlatLayout = flat;
    }
    
    /*************************************************************************/
    
//    public void showFullName (boolean showFullName, boolean refresh) 
//    {
//        if ( _showFullName != showFullName ) {
//            _showFullName = showFullName;
//            if (refresh) {
//                _viewer.refresh();
//            }
//        }
//    }
    
    /**
     * 
     * @return true if Full Name View
     */
    public boolean switchNameView (boolean refresh) 
    {
        // Trace.trace();
        _showFullName = !_showFullName;
        if (refresh) {
            _viewer.refresh();
        }
        return _showFullName;
    }
    
    public boolean isFullNameView () {
        return _showFullName;
    }
    
    /*************************************************************************/
    
    
	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) 
    {
        String imageFile = null;
        
		ImageDescriptor descriptor = null;
	    if (element instanceof TypeNode
            || element instanceof TypeDescription) {
            
            // Object object = ((TypeNode)element).getObject();
	        if ( element instanceof TypeDescription
                || ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE ) {
                TypeDescription t;
                if (element instanceof TypeDescription) {
                    t = (TypeDescription) element;
                } else {
                    t = (TypeDescription)((TypeNode)element).getObject();
                }
                imageFile = "type.gif";
                descriptor = ImageLoader.getInstance().getImageDescriptor(imageFile);
                
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
//				return PlatformUI.getWorkbench().getSharedImages().getImage(
//				        		ISharedImages.IMG_OBJ_FOLDER);
                
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_FEATURE ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor("final_co.gif");

            } else {
				// throw unknownElement(element);
                Trace.err("unknownElement");
			    return null;
			}
	    } else {
			// throw unknownElement(element);
		    return null;
	    }
		//obtain the cached image corresponding to the descriptor
        if (descriptor != null) {
    		Image image = (Image)imageCache.get(descriptor);
    		if (image == null) {
    			image = descriptor.createImage();
                if (image != null) {
                    imageCache.put(descriptor, image);
                }
    		}
    		return image;
        } else {
            return null;
        }
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) 
	{
        // Trace.trace();
	    if (element instanceof TypeNode) {
	        if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE ) {
                // Trace.trace(((TypeNode)element).getLabel());
                if (((TypeNode)element).getObject() != null) {
                    String ioText = "";
                    if (_showFullName) {
                        return ((TypeDescription)((TypeNode)element).getObject()).getName()+ioText
                                + " [" + ((TypeNode)element).getFsTotal() + " : "
                                + ((TypeNode)element).getFsCount() + "]";
                    } else {
                        return ((TypeNode)element).getLabel()+ioText
                        + " [" + ((TypeNode)element).getFsTotal() + " : "
                        + ((TypeNode)element).getFsCount() + "]";
                    }
                } else {
                    return ((TypeNode)element).getLabel();
                }
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
		        return ((TypeNode)element).getLabel(); // new String("features");
		        
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_FEATURE ) {
                return ((FeatureDescription)((TypeNode)element).getObject()).getName();

			} else {
			    Trace.err("Unknown element.getObjectType() (= " 
			    		+ ((TypeNode)element).getObjectType()
			            + ") for label: " + ((TypeNode)element).getLabel());
			    return ((TypeNode)element).getLabel(); // "Unknow element.getObjectType()";
		    } 
	    } else if (element instanceof TypeDescription) {
	        return ((TypeDescription)element).getName();
	        

	    } else if (element instanceof String) {
	        return (String) element;
	    }
	    Trace.err("Unknown element: " + element.getClass().getName());
	    return "Unknow";
	}

	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName());
	}	
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.getObject())
     */
    public Color getForeground (Object element) {
        if ( element instanceof TypeNode ) {
            int color = -1;
        	if (((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES) {
                color = SWT.COLOR_BLUE;
        		
        	} else if (((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE)  {
//                if (((TypeNode)element).getObject() != null) {
//            		int status = ((TypeDescription)((TypeNode)element).getObject()).getModificationAttribut();
//            		if (((TypeDescription)((TypeNode)element).getObject()).getValidityAttribut() != ItemAttributes.STATUS_VALID) { 
//                        color = SWT.COLOR_RED;
//                    } else if (status == ItemAttributes.MODIFICATION_UPDATE) {
//    	        		System.out.println("getForeground: COLOR_GREEN");
//                        color = SWT.COLOR_GREEN;
//    	        	}
//                }
            } else if (((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_FEATURE)  {
        	}
            if (color != -1) {
                return _viewer.getControl().getDisplay().getSystemColor(color);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.getObject())
     *
     * Note: Type's node BG color is get from the node field TypeNode.getBgColor()
     *       Type system style "typesystemStyle" is NOT used.
     *       The value of "TypeNode.getBgColor()" is set by CASViewControl.showTypeSystem()
     *       for types having annotations.
     */
    public Color getBackground(Object element) {
        if ( element instanceof TypeNode ) {
            if ( ((TypeNode) element).getBgColor() != null ) {
                return (Color) ((TypeNode) element).getBgColor();
//            } else {
//                // Try to get type's style by type name
//                if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE) {
//                    return getTypeBackground ((TypeDescription)((TypeNode)element).getObject());
//                }
            }
        } else {
        }
        return null; 
    }
    /**
     * Note: typesystemStyle is NOT set
     * 
     * @param type
     * @return Color
     */
    protected Color getTypeBackground (TypeDescription type)
    {
        if (typesystemStyle != null) {   
            TypeStyle style = typesystemStyle.getTypeStyle(type.getName());
            if (style != null) {
                Trace.err("BG for " + type.getName() + ": " + style.bgColor.toString());
                return style.bgColor;
            } else {
                Trace.err("NO BG for " + type.getName());                
            }
        }
        // Trace.trace("typesystemStyle == null");
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.getObject())
     */
    public Font getFont(Object element) {
        // TODO Auto-generated method stub
        return null;
    }		
    
}
