package org.apache.uima.casviewer.ui.internal.style;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.TypeNode;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class DefaultColorTreeLabelProvider extends LabelProvider 
            implements ITableLabelProvider, ITableColorProvider, IFontProvider  
{
    final static int COLUMN_FOREGROUND_COLOR    = 2;
    final static int COLUMN_BACKGROUND_COLOR    = 4;
    final static int COLUMN_PRE_SELECTED        = 5;
    final static int COLUMN_HIDDEN              = 6;
    
	private StructuredViewer	_viewer          = null;
	private Map 			    imageCache      = new HashMap(11);
	private boolean             _showFullName 	= false; // If true, will show "full name"
	
	protected DefaultColorTreeLabelProvider () {
	}
	
	public DefaultColorTreeLabelProvider (StructuredViewer viewer) {
		super();
		_viewer = viewer;
	}

	public DefaultColorTreeLabelProvider (StructuredViewer viewer, boolean showFullName) {
		super();
		_viewer = viewer;
		_showFullName = showFullName;
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
        Trace.trace();
        _showFullName = !_showFullName;
        if (refresh) {
            _viewer.refresh();
        }
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
                                
            } else {
				// throw unknownElement(element);
                // Trace.err("unknownElement");
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
	    if (element instanceof TypeNode) {
            if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_TYPE ) {
                if (((TypeNode)element).getObject() != null) {
                    String ioText = "";
                    if (_showFullName) {
                        return ((TypeDescription)((TypeNode)element).getObject()).getName()+ioText
                                + " [" + ((TypeNode)element).getFsTotal() + "]";
                    } else {
                        // System.out.println(((TypeDescription)((TypeNode)element).getObject()).getShortName());
                        return ((TypeDescription)((TypeNode)element).getObject()).getName()+ioText
                                + " [" + ((TypeNode)element).getFsTotal() + "]";
                    }
                } else {
                    return ((TypeNode)element).getLabel();
                }
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_LABEL_FEATURES ) {
		        return ((TypeNode)element).getLabel(); // new String("features");
		        
	        } else if ( ((TypeNode)element).getObjectType() == IItemTypeConstants.ITEM_TYPE_FEATURE ) {
	        	// return ((TreeFeatureNode)element).getLabel();
//                if (_showFullName) {
//                    // System.err.println("[_showFullName]");
//                    return ((FeatureMetadata)((TypeNode)element).getObject()).getFullName();
//                } else {
//                    return ((FeatureMetadata)((TypeNode)element).getObject()).getName();
//                }

			} else {
//			    Trace.err("Unknown element.getObjectType() (= " 
//			    		+ ((TypeNode)element).getObjectType()
//			            + ") for label: " + ((TypeNode)element).getLabel());
			    return ((TypeNode)element).getLabel(); // "Unknow element.getObjectType()";
		    } 
	        	        
	    } else if (element instanceof TypeDescription) {
	        return ((TypeDescription)element).getName();
	        
//	    } else if (element instanceof FeatureMetadata) {
//            // System.err.println("[0 FeatureMetadata _showFullName]");
//	    	if (_showFullName) {
//                // System.err.println("[FeatureMetadata _showFullName]");
//	    		return ((FeatureMetadata)element).getFullName();
//	    	} else {
//	    		return ((FeatureMetadata)element).getName();
//	    	}

	    } else if (element instanceof String) {
	        return (String) element;
	    }
	    System.out.println("Unknown element: " + element.getClass().getName());
	    return "Unknow";
	} // getText
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.getObject())
     */
    public Font getFont(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnText(Object element, int index) 
    {
        if (element instanceof TypeNode) {
            Object obj = ((TypeNode) element).getObject();
            if ( obj instanceof TypeStyle ) {
                if (index == 0) {
                    // Display type's name from node's label
                    return ((TypeNode)element).getLabel();
                    
                } else if (index == COLUMN_PRE_SELECTED) {
                    if ( ((TypeStyle) obj).isChecked() ) {
                        return "selected";
                    }
                    return "";
                } else if (index == COLUMN_HIDDEN) {
                    if ( ((TypeStyle) obj).isHidden() ) {
                        return "hidden";
                    }
                    return "";
                } else {
                    return "";
                }
            } else {
                Trace.trace("Unknown obj: " + obj.getClass().getName());
            }
            // return getText(element);
            
        }
        // Trace.trace("Unknown element: " + element.getClass().getName());
        return "???";
    }
    
    public Image getColumnImage(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
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
     */
    public Color getBackground(Object element) {
        // System.out.println("getBackground: ");
        if ( element instanceof TypeNode ) {
            if ( ((TypeNode) element).getBgColor() != null ) {
                return (Color) ((TypeNode) element).getBgColor();
            }
        } else {
            // ((TreeViewer)_viewer).getTree().tes
        }
        return null; 
    }

    public Color getBackground(Object element, int columnIndex) {
        if ( element instanceof TypeNode ) {
            Object obj = ((TypeNode) element).getObject();
            if ( obj instanceof TypeStyle ) {
                if (columnIndex == 0 || columnIndex == 4) {
                    return (Color) ((TypeNode) element).getBgColor();
                } else if (columnIndex == 2) {
                    if ( obj instanceof TypeStyle ) {
                        TypeStyle typeColor = (TypeStyle) obj;
                        if (typeColor != null) {
                            return (Color) typeColor.fgColor;
                        }
//                    } else if ( obj instanceof ColoredType ) {
//                        return ((ColoredType) obj).getColor().fgColor;
                    }
                }
                
//            } else if ( (obj instanceof TypeColor) || (obj instanceof ColoredType) ) {
//                if (columnIndex == 0 || columnIndex == 4) {
//                    return (Color) ((TypeNode) element).getBgColor();
//                } else if (columnIndex == 2) {
//                    if ( obj instanceof TypeColor ) {
//                        TypeColor typeColor = (TypeColor) obj;
//                        if (typeColor != null) {
//                            return (Color) typeColor.fgColor;
//                        }
//                    } else if ( obj instanceof ColoredType ) {
//                        // Some types may NOT have color
//                        if (((ColoredType) obj).getColor() != null) {
//                            return ((ColoredType) obj).getColor().fgColor;
//                        }
//                    }
//                }
            }
        }
        return null;
    }

    public Color getForeground(Object element, int columnIndex) {
        if ( element instanceof TypeNode ) {
            if (columnIndex == 0) {
                Object obj = ((TypeNode) element).getObject();
                if ( obj instanceof TypeStyle ) {
                    return ((TypeStyle) obj).fgColor;
                }            
            }
        }
        return null;
    }		
    
}
