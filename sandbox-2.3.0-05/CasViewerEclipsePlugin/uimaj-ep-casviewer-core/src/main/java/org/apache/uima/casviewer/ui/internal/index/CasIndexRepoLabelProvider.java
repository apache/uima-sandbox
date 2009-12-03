package org.apache.uima.casviewer.ui.internal.index;

import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.UFSIndex;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class CasIndexRepoLabelProvider extends LabelProvider implements IColorProvider {

    private boolean         showShortName = true; // true: type's short name; false: full name
    private TypeSystemStyle typesystemStyle;
    
    public CasIndexRepoLabelProvider(TypeSystemStyle tsStyle) {
        super();
        this.typesystemStyle = tsStyle;
    }
    public CasIndexRepoLabelProvider(TypeSystemStyle tsStyle, boolean showShortName) {
        super();
        this.typesystemStyle = tsStyle;
        this.showShortName = showShortName;
    }

    /*************************************************************************/
    
    public void setTypeSystemStyle (TypeSystemStyle style)
    {
        Trace.trace(style==null? "style==null":"");
        this.typesystemStyle = style;
    }

    /*************************************************************************/
    
    public Image getImage(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getText(Object element) {
        if (element instanceof BaseNode) {
            Object obj = ((BaseNode)element).getObject();
            if (obj == null) {
                return ((BaseNode) element).getLabel();
            }
            
            int kind = ((BaseNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                // Short Name ?
                String  typeName;
                if (showShortName) {
                    typeName = ((UFSIndex) obj).getType().getShortName();
                } else {
                    typeName = ((UFSIndex) obj).getType().getName();
                }
                String  text;
                if (((UFSIndex) obj).getIndexLevel() == 1) {
                    text = ((UFSIndex) obj).getLabel()
                           + " - " + typeName;
                } else {
                    // Index of sub-type
                    text = typeName; // ((TreeNode) element).getLabel();
                }
                return text + " [" + ((UFSIndex) obj).size() 
                            + " : " + ((UFSIndex) obj).getSelfFSCount() + "]"; 
                
            } else if (kind == IItemTypeConstants.ITEM_TYPE_FS_INDEX) {
                return ((BaseNode) element).getLabel() 
                        + " [" + ((FSIndex) obj).size() + "]"; 
            }
            return ((BaseNode) element).getLabel();
            
        } else if (element instanceof FSIndex) {
            FSIndex index = (FSIndex) element;
            return index.getType() + " [" + index.size() + "]";
            
        } else if (element instanceof UFSIndex) {
            UFSIndex index = (UFSIndex) element;
            return index.getType() + " [" + index.size() + "]";
            
        } else if (element instanceof UFeatureStructure) {
            return "FS[" + ((UFeatureStructure) element).getIndexInIterator() + "] - " + ((UFeatureStructure) element).getType().getShortName();
            
        } else if (element instanceof FeatureStructure) {
            return ((FeatureStructure) element).getType().getShortName();
        }
            
        return "Unknown";
    }
    
    public Color getForeground(Object element) {
        return null;
    }       
    
    public Color getBackground(Object element) {
        if (element instanceof BaseNode) {
            Object obj = ((BaseNode)element).getObject();
            if (obj == null) {
                return null;
            }
            
            // Get BG based on type name
            String  typeName = null;
            int kind = ((BaseNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                if (((UFSIndex) obj).getSelfFSCount() == 0) {
                    // No self-annotation
                    return null;
                }
                return getTypeBackground(((UFSIndex) obj).getType().getName());
            }
        }
        else if ( element instanceof UFeatureStructure ) {
            if ( ((UFeatureStructure) element).getUserData() != null ) {
                AnnotationObject annot = (AnnotationObject) ((UFeatureStructure) element).getUserData();                
                return getTypeBackground(annot.getTypeName());
            }
        } else {
            Trace.trace("Unknown " + element.getClass().getName());
        }
        return null; 
    }

    protected Color getTypeBackground (String typeName)
    {
        if (typesystemStyle != null) {   
            TypeStyle style = typesystemStyle.getTypeStyle(typeName);
            if (style != null) {
                // Trace.trace("HAS bg for " + type.getName() + " " + style.bgColor);
                return style.bgColor;
            // } else {
                // Trace.trace("NO bg for " + type.getName());
            }
        }
        // Trace.trace("typesystemStyle == null");
        return null;
    }
    
}
