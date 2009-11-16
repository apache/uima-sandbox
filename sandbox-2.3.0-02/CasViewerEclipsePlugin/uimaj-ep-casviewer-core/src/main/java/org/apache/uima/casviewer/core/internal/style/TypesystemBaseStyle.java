package org.apache.uima.casviewer.core.internal.style;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.uima.tools.debug.util.Trace;

/**
 *  Base style of the type system.
 *  This class should NOT depend on any Eclipse library
 */
public class TypesystemBaseStyle {

  // Map from "#xxxxxx" hex value to color name, and vice-versa (e.g., "#c0c0c0" <-> "silver")
  static protected Map<String, String>      colorNameMap;
  static protected boolean      isInitialized = false;  // true: colorNameMap is initialized

  private BaseTypeStyle STYLE_EMPTY = new BaseTypeStyle();  
  protected Map<String, BaseTypeStyle> mapTypename2Typestyle = new HashMap<String, BaseTypeStyle>();

  // Default colors
  protected int                 indexUsedColors = 0;    // index of next available color
  protected List<BaseColor>     defaultBaseColors = new ArrayList<BaseColor>() ;

  final static protected String[] STYLES = 
  {
      "color:black; background:#f2edb6;",
      "color:black; background:#f2b6b6;",
      "color:black; background:#b6d4f2;",
      "color:black; background:#b6f2b6;",
      "color:black; background:#e8b6f2;",
      
      "color:black; background:#f2d4b6;",
      "color:black; background:#def2b6;",
      "color:black; background:#f2b6d4;",
      "color:black; background:#b6f2de;",
      "color:black; background:#c0b6f2;",
      
      "color:black; background:#f2e879;",
      "color:black; background:#f27979;",
      "color:black; background:#79b6f2;",
      "color:black; background:#79f279;",
      "color:white; background:#de79f2;",
      
      "color:white; background:#f2b679;",
      "color:black; background:#caf279;",
      "color:white; background:#f279b6;",
      "color:white; background:#79f2ca;",
      "color:white; background:#8d79f2;",
      
      "color:black; background:#f2e33d;",
      "color:black; background:#f23d3d;",
      "color:black; background:#3d97f2;",
      "color:black; background:#3df23d;",
      "color:black; background:#d43df2;",
      
      "color:black; background:#f2973d;",
      "color:black; background:#b6f23d;",
      "color:black; background:#f23d97;",
      "color:black; background:#3df2b6;",
//      "color:black; background:#5b3df2;",

      "color:black; background:lightblue;",  
      "color:black; background:lightgreen;",
      "color:black; background:orange;",     
      "color:black; background:yellow;",
      "color:black; background:pink;",       
      
      "color:black; background:salmon;", 
      "color:black; background:cyan;",       
      "color:black; background:violet;", 
      "color:black; background:tan;",        
      "color:white; background:brown;", 
      
      "color:white; background:blue;",       
      "color:white; background:green;", 
      "color:white; background:red;",        
      "color:white; background:mediumpurple;"
  };

  /*************************************************************************/
  
  /**
   * Create mapping between #xxxxxx <-> color name
   * (e.g., "#c0c0c0" <-> "silver")
   * 
   * @return void
   */
  static protected void initializeColorNameMap()
  {
      if ( !isInitialized ) {
          isInitialized = true;
      }
      colorNameMap = new HashMap<String, String>();
      colorNameMap.put("#000000", "black");
      colorNameMap.put("#c0c0c0", "silver");
      colorNameMap.put("#808080", "gray");
      colorNameMap.put("#ffffff", "white");
      colorNameMap.put("#800000", "maroon");
      colorNameMap.put("#ff0000", "red");
      colorNameMap.put("#800080", "purple");
      colorNameMap.put("#ff00ff", "fuchsia");
      colorNameMap.put("#008000", "green");
      colorNameMap.put("#00ff00", "lime");
      colorNameMap.put("#808000", "olive");
      colorNameMap.put("#ffff00", "yellow");
      colorNameMap.put("#000080", "navy");
      colorNameMap.put("#0000ff", "blue");
      colorNameMap.put("#00ffff", "aqua");
      colorNameMap.put("#000000", "black");
      colorNameMap.put("#add8e6", "lightblue");
      colorNameMap.put("#90ee90", "lightgreen");
      colorNameMap.put("#ffa500", "orange");
      colorNameMap.put("#ffc0cb", "pink");
      colorNameMap.put("#fa8072", "salmon");
      colorNameMap.put("#00ffff", "cyan");
      colorNameMap.put("#ee82ee", "violet");
      colorNameMap.put("#d2b48c", "tan");
      colorNameMap.put("#a52a2a", "brown");
      colorNameMap.put("#ffffff", "white");
      colorNameMap.put("#9370db", "mediumpurple");
      //in other order for lookup
      colorNameMap.put("black", "#000000");
      colorNameMap.put("silver", "#c0c0c0");
      colorNameMap.put("gray", "#808080");
      colorNameMap.put("white", "#ffffff");
      colorNameMap.put("maroon", "#800000");
      colorNameMap.put("red", "#ff0000");
      colorNameMap.put("purple", "#800080");
      colorNameMap.put("fuchsia", "#ff00ff");
      colorNameMap.put("green", "#008000");
      colorNameMap.put("lime", "#00ff00");
      colorNameMap.put("olive", "#808000");
      colorNameMap.put("yellow", "#ffff00");
      colorNameMap.put("navy", "#000080");
      colorNameMap.put("blue", "#0000ff");
      colorNameMap.put("aqua", "#00ffff");
      colorNameMap.put("black", "#000000");
      colorNameMap.put("lightblue", "#add8e6");
      colorNameMap.put("lightgreen", "#90ee90");
      colorNameMap.put("orange", "#ffa500");
      colorNameMap.put("pink", "#ffc0cb");
      colorNameMap.put("salmon", "#fa8072");
      colorNameMap.put("cyan", "#00ffff");
      colorNameMap.put("violet", "#ee82ee");
      colorNameMap.put("tan", "#d2b48c");
      colorNameMap.put("brown", "#a52a2a");
      colorNameMap.put("white", "#ffffff");
      colorNameMap.put("mediumpurple", "#9370db");
  }
    
  /**
   * Assign style to types. If the style file is null, the default styles will
   * be used.
   * 
   * @param typeNames       Array of type's full name
   * @param styleMapFile    Style file
   * @return List<TypeStyle>
   */
  public void assignBaseStyleToTypes (String[] typeNames, File styleMapFile)
  {
      // Clear old styles
      mapTypename2Typestyle.clear();
      
      // Read styles from style file ?
      if (styleMapFile != null) {
        
      } else {
        // Use default styles
        assignDefaultBaseStyleToTypes(typeNames);
      }
  }

  /**
   * Assign default style to type (used default colors)
   * 
   * @param typeNames
   * @return void
   */
  protected void assignDefaultBaseStyleToTypes (String[] typeNames) {
    for (int i=0; i<typeNames.length; ++i) {
      // Create and register new color for type
      if (indexUsedColors >= defaultBaseColors.size()) {
          indexUsedColors = 0;    // re-use colors
      }
      BaseTypeStyle bs = new BaseTypeStyle(typeNames[i], defaultBaseColors.get(indexUsedColors++));
      mapTypename2Typestyle.put(typeNames[i], bs);
    }
  }

  /**
   * Get style of the specified type name
   * 
   * @param typeName
   * @return BaseStyle
   */
  public BaseTypeStyle getBaseStyle (String typeName) {
    BaseTypeStyle bs = mapTypename2Typestyle.get(typeName);
    if (bs != null) {
      return bs;
    }
    Trace.err("No style for " + typeName);
    return STYLE_EMPTY;
  }

  /*************************************************************************/

  /**
   * 
   */
  public TypesystemBaseStyle() {
    initializeColorNameMap();
    initializeDefaultBaseColors ();
  }
  
  /**
   * Initialize color mapping for types.
   * 
   * @return
   * @return TypeStyle
   */
  static public TypesystemBaseStyle createInstance (String defaultStyleFileName)
  {
//      if (instance != null) {
//          Trace.trace("Style is ALREADY setup");
//          return instance;
//      }
      // ++totalInstances;
      return new TypesystemBaseStyle();
  }

  
  /**
   * Create default colors used by CAS Viewer
   * 
   * @return void
   */
  protected void initializeDefaultBaseColors ()
  {
      for (int i=0; i<STYLES.length; ++i) {
        BaseColor c = getBaseColorFromString(STYLES[i]);
        defaultBaseColors.add(c);
      }        
  }
  
  /**
   *  Parse color string.
   * 
   * @param styleColor String having the following format: "#c0c0c0" or "silver"
   *        <style>color:black;background:lightblue;checked:true;hidden:false;</style> 
   * @return BaseColor
   */
  protected BaseColor getBaseColorFromString (String styleColor)
  {
    StringTokenizer token = new StringTokenizer(styleColor, ":;");
    if (!token.hasMoreTokens()) {
      return null; // No token
    }

    // BaseColor c = null;
    int fg, bg;
    
    // Get foreground color
    token.nextToken();
    String hexString = token.nextToken().toLowerCase().trim();
    if (!hexString.startsWith("#")) {
      // Color name is used
      hexString = (String) colorNameMap.get(hexString);
      if (hexString == null) {
        // Unknown color name
        Trace.err("Unknown FG color name: " + hexString);
        hexString = "#000000";
      }
    }
    fg = Integer.parseInt(hexString.substring(1), 16);
    
    // Get background color
    token.nextToken();
    hexString = token.nextToken().toLowerCase().trim();
    if (!hexString.startsWith("#")) {
      // Color name is used
      hexString = (String) colorNameMap.get(hexString);
      if (hexString == null) {
        // Unknown color name
        Trace.err("Unknown FG color name: " + hexString);
        hexString = "#FFFFFF";
      }
    }
    bg = Integer.parseInt(hexString.substring(1), 16);
    // Trace.err("bg: " + bg);
    return new BaseColor(fg, bg);
  }        
   

  /*************************************************************************/

  /*************************************************************************/
  

  /*************************************************************************/
  

}
