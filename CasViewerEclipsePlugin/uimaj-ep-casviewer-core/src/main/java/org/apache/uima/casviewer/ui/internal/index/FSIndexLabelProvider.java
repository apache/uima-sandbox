package org.apache.uima.casviewer.ui.internal.index;

import java.util.List;

import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.UFSIndex;
import org.apache.uima.casviewer.core.internal.UFeature;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.casviewer.ui.internal.style.TypeStyle;
import org.apache.uima.casviewer.ui.internal.style.TypeSystemStyle;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class FSIndexLabelProvider extends LabelProvider
                                    implements IColorProvider { //, IFontProvider {

    // Preferences
    private boolean hideNoValueFeature      = true;    
    private boolean showShortName           = true; // true: type's short name; false: full name
    private boolean showOneLine             = false;
    private TypeSystemStyle    typesystemStyle;
    
    public FSIndexLabelProvider(TypeSystemStyle tsStyle) {
        super();
        this.typesystemStyle = tsStyle;
    }
    
    public FSIndexLabelProvider(TypeSystemStyle tsStyle, boolean showShortName) {
        super();
        this.typesystemStyle = tsStyle;
        this.showShortName = showShortName;
    }

    public void showOneLineView (boolean showOneLine) {
        this.showOneLine = showOneLine;
    }
    
    public void hideNoValueFeature (boolean hide)
    {
        hideNoValueFeature = hide;
    }
    
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

    // 1st element to come: BaseNode ITEM_TYPE_LABEL_U_FS_INDEX
    public String getText(Object element) {
        if (element instanceof BaseNode) {
            Object obj = ((BaseNode)element).getObject();
            if (obj == null) {
                return ((BaseNode) element).getLabel();
            }
            
            int kind = ((BaseNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX
                || kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX) {
                // Short Name ?
                String  typeName;
                if (showShortName) {
                    typeName = ((UFSIndex) obj).getType().getShortName();
                } else {
                    typeName = ((UFSIndex) obj).getType().getName();
                }
                return ((UFSIndex) obj).getLabel() + " - " + typeName
                        + "[" + ((UFSIndex) obj).size() + "]"; 
                
            } else if (kind == IItemTypeConstants.ITEM_TYPE_FS_INDEX) {
                return ((BaseNode) element).getLabel() 
                        + " [" + ((FSIndex) obj).size() + "]"; 
            }
            return ((BaseNode) element).getLabel();
            
        } else if (element instanceof FSIndex) {
            FSIndex index = (FSIndex) element;
            return index.getType() + "[" + index.size() + "]";
            
        } else if (element instanceof UFSIndex) {
            UFSIndex index = (UFSIndex) element;
            return index.getLabel() + " - " + index.getType() + "[" + index.size() + "]";
            
        } else if (element instanceof UFeatureStructure) {
            int index = ((UFeatureStructure) element).getIndexInIterator();
            String text = "";
            if (index >= 0) {
                text = "[" + index + "] - ";
            }
            
            // Show feature values in ONE line with feature name
            String values = "";
            FeatureStructure fs = ((UFeatureStructure)element).getUimaFeatureStructure();
            if (fs instanceof AnnotationFS) {
                values = "- " + ((AnnotationFS)fs).getCoveredText() + " ";
            }
            if ( showOneLine ) {
                List aFeatures = fs.getType().getFeatures();
                if (aFeatures.size() > 0) {
                    values += "(";
                    for (int i=0; i<aFeatures.size(); ++i) {
                        Feature f = (Feature) aFeatures.get(i);
                        // Skip "sofa" ?
                        if (f.getShortName().equalsIgnoreCase("sofa")) {
                            continue;
                        }
                        String textFeature = "";
                        if (f.getRange().isPrimitive()) {
                            textFeature = ": " + fs.getFeatureValueAsString(f);
                        } else {
                            // Non-primitive feature
                            String extra = "";
                            if (fs.getFeatureValue(f) == null) {
                                if (hideNoValueFeature) {
                                    continue;
                                }
                                extra = " (no value)";
                            }
                            textFeature = " - " + f.getRange().getShortName() + extra;
                        }
                        // Display as "Short Name" {"Value" | "(no value)"}
                        values += f.getShortName() + textFeature + " ; ";
                    }  
                    values += ")";
                }            
            } else {
            }
            return text + ((UFeatureStructure) element).getType().getShortName()
                        + " " + values;
            
        } else if (element instanceof FeatureStructure) {
            return ((FeatureStructure) element).getType().getShortName();
            
        } else if (element instanceof UFeature) {
            String text = "";
            if (((UFeature) element).isPrimitive()) {
                text = ": " + ((UFeature) element).getValueAsString();
            } else {
                // Non-primitive feature
                String extra = "";
                if (((UFeature) element).getFeatureValue() == null) {
                    extra = " (no value)";
                }
                text = " - " + ((UFeature) element).getRange().getShortName() + extra;
            }
            return ((UFeature) element).getShortName() + text;
            
        } else if (element instanceof Feature) {
            return ((Feature) element).getShortName();
            
        } else if (element instanceof String) {
            return (String) element;
        } else if (element instanceof Integer) {
            return "" + (Integer)element;
        } else if (element instanceof Float) {
            return "" + (Float) element;
        } else if (element instanceof Boolean) {
            return ((Boolean)element).toString();
        }
            
        return "Unknown";
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.getObject())
     */
    public Color getBackground(Object element) {
        if ( element instanceof UFeatureStructure ) {
            return getTypeBackground(((UFeatureStructure) element).getType().getName());
        } else {
            // Trace.trace("Unknown " + element.getClass().getName());
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
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.getObject())
     */
    public Font getFont(Object element) {
        return null;
    }
    public Color getForeground(Object element) {
        return null;
    }       

    
}
