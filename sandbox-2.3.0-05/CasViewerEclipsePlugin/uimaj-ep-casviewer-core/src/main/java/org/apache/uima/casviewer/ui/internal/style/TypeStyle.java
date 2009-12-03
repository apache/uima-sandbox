package org.apache.uima.casviewer.ui.internal.style;

import org.apache.uima.casviewer.core.internal.style.BaseTypeStyle;
import org.eclipse.swt.graphics.Color;

/**
 * Augment BaseStyle class with Eclipse-specific color classes 
 *
 */
public class TypeStyle extends BaseTypeStyle {

    protected String            typeLabel;  // User-friendly label of type 
    
    public Color                fgColor;
    public Color                bgColor;

    /*************************************************************************/
    
    protected TypeStyle (){        
    }
    
    public TypeStyle (String name, String label, Color f, Color b) {
        typeName    = name;
        typeLabel   = label;
        fgColor = f;
        bgColor = b;
    }
    
    public void copyStyle (TypeStyle from)
    {
        fgColor   = from.fgColor;
        bgColor   = from.bgColor;
        isChecked = from.isChecked;
        isHidden  = from.isHidden;
    }
    
    /*************************************************************************/
    
    public void clearStyle () {
        fgColor   = null;
        bgColor   = null;
        isChecked = false;
        isHidden  = false;
    }
    
    public Color getForeground () {
        return fgColor;
    }

    public void setForeground (Color color) {
        fgColor = color;
    }

    public Color getBackground () {
        return bgColor;
    }

    public void setBackground (Color color) {
        bgColor = color;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String toString() {
        return "TypeStyle - typeName: " + typeName + " ; bgColor: " + bgColor;
    }
}
