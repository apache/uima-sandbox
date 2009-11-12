package org.apache.uima.casviewer.ui.internal.style;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Generic color manager.
 */
public class ColorManager { // implements ISharedTextColors {   
    
    private static int colorManager = 0;
    
    private ColorManager() {
    }
    
    public static ColorManager createColorManager() {
        ++colorManager;
        return new ColorManager();
    }
        
    protected Map fColorTable= new HashMap(10);
    
    public Color getColor(int hexColorValue) {
        Color color= (Color) fColorTable.get(hexColorValue);
        if (color == null) {
            RGB rgb = new RGB((hexColorValue >> 16) & 0xFF, (hexColorValue >> 8) & 0xFF, hexColorValue & 0xFF);
            color = new Color(Display.getCurrent(), rgb);
            fColorTable.put(hexColorValue, color);
            // Trace.err("rgb: " + rgb.toString());
        }
        return color;
    }
    
    public Color getColor(RGB rgb) {
        Color color= (Color) fColorTable.get(rgb);
        if (color == null) {
            color= new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
    
//    static private RGB hexToRBG (String number)
//    {
//        int i = Integer.decode(number.toLowerCase()).intValue();
//        return new RGB((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
//    }

    public void dispose() 
    {
        // Trace.err("Dispose " + fColorTable.values().size() + " colors");
//        if (--colorManager > 0) {
//            Trace.bug(colorManager + " ColorManager instances remained");
//        }
        Iterator e = fColorTable.values().iterator();
        while (e.hasNext()) {
            Color c = (Color) e.next();
            // Trace.trace("   " + c);
            c.dispose();
        }
    }
    
    protected void finalize() throws Throwable
    {
        System.out.println("ColorManager.finalize");
        Iterator e= fColorTable.values().iterator();
        // Trace.err("Dispose " + fColorTable.values().size() + " colors");
        while (e.hasNext()) {
            Color c = (Color) e.next();
            // Trace.trace("   " + c);
            c.dispose();
        }
    }
   
}


