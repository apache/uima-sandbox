package org.apache.uima.casviewer.ui.internal.style;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.apache.uima.casviewer.core.internal.style.BaseColor;
import org.apache.uima.casviewer.core.internal.style.BaseTypeStyle;
import org.apache.uima.casviewer.core.internal.style.TypesystemBaseStyle;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.util.htmlview.AnnotationViewGenerator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * How the style is assigned to each type ? (see addPages() of the Viewer)
 *   
        // Create Type System's Style Object
        tsStyle = TypeSystemStyle.createInstance(null);
        tsStyle.preSelectAllAnnotations(prefPreselectAll);
        
        // Assign style to each type and populate the default type mapping
        List typeStyleList = tsStyle.assignStyleToTypes(typeNames);
        
        // Get style file and apply, if any
        if (inputIsFile) {
          File styleFile = getStyleFile (inputFileName, ".style.xml");
          if (styleFile != null) {
              // All styles from imported file will be used (instead of default styles)
              tsStyle.importStyleMapFile(styleFile);
          }
        }
 * 
 *
 */
public class TypeSystemStyle extends TypesystemBaseStyle {    
    
    static public Color         SYSTEM_COLOR_BLACK; 
    static public Color         SYSTEM_COLOR_WHITE; 
    
    static private int          totalInstances = 0;                 
    
    private ColorManager        colorManager = ColorManager.createColorManager();
    
    // Preferences
//    private RGB                 defaultForegroundRGB = new RGB(0, 0, 0);
//    private RGB                 defaultBackgroundRGB = new RGB (255, 255, 255);
    private boolean             preSelectAll = false;
    
    // typeNameToTypeStyleMap: Map from Type full name to TypeStyle object as defined
    // in the imported style map (NO map if no style file)
    // In Style file, multiple types may share the same color
    // "mapBgFgColors" will contain the actual number of colors (no duplication)
    // typeNameToTypeStyleMap is used by "ColoredTypeTreeContentProvider" 
    // to display imported types in tree (by calling getStyleTypeMap());

//    protected Map<String, TypeStyle> typeNameToTypeStyleMap = new HashMap();  // Map of types having style
    protected boolean           hasTypeSystemStyle = false; // true: style is imported; false: default style
    protected String            importedStyleMapFile = null;
    
    // The following lists are set when style file is imported 
//    protected List<Color>  colorList       = new ArrayList<Color>();
    protected List<String> preSelectedList = new ArrayList<String>();
    protected List<String> hiddenTypeNameList = new ArrayList<String>();
   
    // Used to restore default colors to types
    protected List<String> typeNameListSortedByDefColors = new ArrayList<String>();
    
    // Map between type's name and type's default style
    protected Map<String, TypeStyle> defaultTypeStyleMap = new HashMap<String, TypeStyle>();  
            
    /*************************************************************************/
                        
    private TypeSystemStyle() {
        super();
        SYSTEM_COLOR_BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
        SYSTEM_COLOR_WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
    }
    
    /**
     * Initialize color mapping for types.
     * 
     * @return
     * @return TypeStyle
     */
    static public TypeSystemStyle createInstance (String defaultStyleFileName)
    {
        ++totalInstances;
        return new TypeSystemStyle();
    }
    
    /**
     * Dispose all created Color objects
     * 
     * @return void
     */
    public void dispose ()
    {
        colorManager.dispose();
        // Trace.trace(--totalInstances + " TypeSystemStyle instances remained");
    }
    
    /*************************************************************************/
                        
    static public TypeSystemStyle createStyleForTypesHavingAnnotations (ICASObjectView casViewObject, 
                        final File styleMapFile) {
        // Collect types having annotations 
        List<Type> typesHavingAnnotations = casViewObject.getTypesHavingAnnotations(); 
        List<String> typeNames = new ArrayList<String>(typesHavingAnnotations.size());
        for (int i=0; i<typesHavingAnnotations.size(); ++i) {
            typeNames.add(typesHavingAnnotations.get(i).getName());
        }        
        
        TypeSystemStyle tsStyle = TypeSystemStyle.createInstance(null);
        tsStyle.assignStyleToTypes(typeNames, styleMapFile);
        
        return tsStyle;
    }
    
    /*************************************************************************/
                        
    private TypeStyle createTypeStyle (String name, String label, BaseColor color) {
        return new TypeStyle (name, label, 
                colorManager.getColor(color.getFgColorValue()),
                colorManager.getColor(color.getBgColorValue()));        
    }

    private TypeStyle createTypeStyle (BaseTypeStyle b) {
        return new TypeStyle (b.getTypeName(), b.getTypeShortName(), 
                colorManager.getColor(b.getFgColorValue()),
                colorManager.getColor(b.getBgColorValue()));        
    }

    public ColorManager getColorManager() {
        return colorManager;
    }
    
    public void updateTypeStyleColor (TypeStyle typeStyle, RGB fgRgb, RGB bgRgb) {
        if (fgRgb != null) {
            typeStyle.fgColor = colorManager.getColor(fgRgb);
        }
        if (bgRgb != null) {
            typeStyle.bgColor = colorManager.getColor(bgRgb);
        }
    }
    
    /**
     * Pre-select all annotations when XCAS file is open
     * 
     * @param prefPreselectAll
     * @return void
     */
    public void preSelectAllAnnotations(boolean prefPreselectAll)
    {
        this.preSelectAll = prefPreselectAll;
    }
    
    /**
     * Assign Style to types (use default colors from TypesystemBaseStyle)
     * 
     * @param typeNames List of type names
     * @return List     List of TypeStyle
     */
    public List<TypeStyle> assignStyleToTypes (List<String> typeNames)
    {
        typeNameListSortedByDefColors.clear();
        defaultTypeStyleMap.clear();
        assignBaseStyleToTypes(typeNames.toArray(new String[typeNames.size()]), null);
        
        List<TypeStyle> list = new ArrayList<TypeStyle> (typeNames.size());
        for (String name: typeNames) {
            TypeStyle s = createTypeStyle(getBaseStyle(name));
            list.add(s);
            defaultTypeStyleMap.put(name, s);
            typeNameListSortedByDefColors.add(name);
        }
        return list;
    }
    
    public List<TypeStyle> assignStyleToTypes(List<String> typeNames, final File styleMapFile) { 
        if (styleMapFile != null) {
            return importAndApplyStyleMapFile(typeNames, styleMapFile);
        } else {
            return assignStyleToTypes(typeNames);
        }
    }
    
    /*************************************************************************/
    
    /**
     * Print colors for types from the imported style map
     */
    public void printColors ()
    {
        TypeStyle   typeStyle;
        Iterator iter = defaultTypeStyleMap.values().iterator();
        while ( iter.hasNext() ) {
            typeStyle = (TypeStyle) iter.next();
//            Trace.trace("FG: " + typeStyle.rgbForeground
//                      + "   BG: " + typeStyle.rgbBackground);
            
        }        
    }

//    public void printMergedColors ()
//    {
//        Trace.trace(" -- Colors --");
//        int i = 0;
//        Iterator iter = mapBgFgColors.entrySet().iterator();
//        while ( iter.hasNext() ) {
//            Map.Entry e = (Map.Entry) iter.next();
//            System.out.println("  #" + i + "    BG: " + e.getKey() + "   FG: " + e.getValue());
//            ++i;
//        }        
//    }
    
    /*************************************************************************/
    
    public TypeStyle getTypeStyle (String typeName)
    {
        // Look up if this type already has style
        TypeStyle style = (TypeStyle) defaultTypeStyleMap.get(typeName);
        if (style != null) {
            //Trace.err("OLD Style for " + typeName + "\n   " + style.toString());
            return style;
        }
        if (hasTypeSystemStyle) {
            // "DocumentAnnotation" will come here !
            // Trace.bug("The style of " + typeName + " should be ALREADY defined from imported file");
            // Use imported style
            // Create a style without color
            style = new TypeStyle(typeName, typeName, null, null);
            defaultTypeStyleMap.put(typeName, style);
            return style;
        }
        
        style = new TypeStyle(typeName, typeName, null, null);
        defaultTypeStyleMap.put(typeName, style);
        Trace.err("Cannot get style for: " + typeName);
        return style;
    }
    
    /**
     *  Parse color string.
     * 
     * @param display   Display object. If null, Color object will not be created
     * @param styleColor String having the following format: "#c0c0c0" or "silver"
     *        <style>color:black;background:lightblue;checked:true;hidden:false;</style> 
     * @return TypeColor
     */
    protected TypeStyle getTypeStyleFromString (TypeStyle typeStyle, Display display, String styleColor)
    {
        if (typeStyle == null) {
            typeStyle = new TypeStyle();
        }
        
        StringTokenizer token = new StringTokenizer(styleColor, ":;");
        if (!token.hasMoreTokens()) {
            return null; // No token
        }
            
        // Get foreground color
        token.nextToken();
        String fgString = token.nextToken().toLowerCase().trim();
        if (fgString.startsWith("#")) {
            if (display != null) {
                typeStyle.setForeground(decode(display, fgString));
            }
        } else {
            // Color name is used
            String newFgString = (String) colorNameMap.get(fgString);
            if (newFgString != null) {
                if (display != null) {
                    typeStyle.setForeground(decode(display, newFgString));
                }
            } else {
                // Unknown color name
                Trace.err("Unknown FG color name: " + fgString);
                if (display != null)
                    typeStyle.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            }
        }
        
        // Get background color
        token.nextToken();
        String bgString = token.nextToken().toLowerCase().trim();
        if (bgString.startsWith("#")) {
            if (display != null)
                typeStyle.setBackground(decode(display, bgString));
        } else {
            // Color name is used
            String newBgString = (String) colorNameMap.get(bgString);
            if (newBgString != null) {
                if (display != null)
                    typeStyle.setBackground(decode(display, newBgString));
            } else {
                // Unknown color name
                Trace.err("Unknown BG color name: " + bgString);
                if (display != null)
                    typeStyle.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
            }
        }
        
        // Parses the string "checked:false"
        boolean checked = false;     // default to Checked
        if(token.hasMoreTokens() ) {
            String ck = token.nextToken();  //checked
            String tf = token.nextToken() ; //true or false
            if(ck.equals( "checked")) {
                if(tf.equals( "false")) {
                    checked = false;
                } else if(tf.equals( "true")) {
                    checked = true;
                }
            }
        }
        typeStyle.setChecked(checked);
        
        // Parses the string "hidden:false"
        boolean hidden = false;     // default to Hidden
        if(token.hasMoreTokens() ) {
            String ck = token.nextToken();  //checked
            String tf = token.nextToken() ; //true or false
            if(ck.equals( "hidden")) {
                if(tf.equals("true")) {
                    hidden = true;
                }
            }
        }
        typeStyle.setHidden(hidden);
        
        return typeStyle;
    }        

    /**
     * Decode string into RGB values and create a Color object (re-use Color if it exists).
     * 
     * @param display
     * @param number        Must be in the form of "#xxxxxx" (used for lookup to Map)
     * @throws NumberFormatException
     * @return Color
     */
    private Color decode(Display display, String number) throws NumberFormatException 
    {
        return colorManager.getColor(Integer.decode(number.toLowerCase()).intValue());
    }
        
    static private RGB hexToRBG (String number)
    {
        int i = Integer.decode(number.toLowerCase()).intValue();
        return new RGB((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public void restoreDefaultStyle ()
    {
        // Remove all previous mapping
        indexUsedColors = 0;
        hasTypeSystemStyle = false;
        preSelectedList.clear();
//        colorList.clear();
        hiddenTypeNameList.clear();
//        typeNameToTypeStyleMap.clear();
        
        List<String> temp = new ArrayList<String>(typeNameListSortedByDefColors);
        assignStyleToTypes(temp);
        
        // Assign default color to type
//        for (int i=0; i<typeNameListSortedByDefColors.size(); ++i) {
//            // Look up if this type already has style
//            TypeStyle style = (TypeStyle) defaultTypeStyleMap.get(typeNameListSortedByDefColors.get(i));
//            if (style == null) {
//                Trace.bug(typeNameListSortedByDefColors.get(i) + " is not in defaultTypeStyleMap");
//                return;
//            }
//            BaseTypeStyle b = getBaseStyle(style.getTypeName());
//            style.setForeground(colorManager.getColor(b.getFgColorValue()));
//            style.setBackground(colorManager.getColor(b.getBgColorValue()));
//            
//        }
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
    
    public String saveTypeSystemStyle (String fileName)
    {
        List<TypeStyle> typeStyles = getCurrentTypeStyleList ();
        
        StringBuffer buf = new StringBuffer();
        buf.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        buf.append("<styleMap>\n");
        for (int i=0; i<typeStyles.size(); ++i) {
            TypeStyle style = (TypeStyle) typeStyles.get(i);
            if (style.fgColor == null) {
                continue;
            }
            buf.append("    <rule>\n");
            buf.append("        <pattern>");
            buf.append(style.getTypeName());
            buf.append("</pattern>\n");
            buf.append("        <label>");
            buf.append(getShortName(style.getTypeLabel()));
            buf.append("</label>\n");
            buf.append("        <style>");
            buf.append("color:#" + toHexString(style.fgColor.getRGB())
                    + ";background:#" + toHexString(style.bgColor.getRGB()) 
                    + ";checked:" + Boolean.toString(style.isChecked()) 
                    + ";hidden:" + Boolean.toString(style.isHidden()) + ";");
            buf.append("</style>\n");
            buf.append("    </rule>\n");
        }
        buf.append("</styleMap>\n");
        
        FileWriter out = null;
        try {
            out = new FileWriter (fileName);
            out.write(buf.toString());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return buf.toString();
    }
    
    /*************************************************************************/
    
//    private static String geShortName (String fullName) {
//        if (fullName == null) return null;
//        
//        int index = fullName.lastIndexOf('.');
//        if ( index < 0 ) {
//            return fullName;
//        } else {
//            return fullName.substring(index+1);
//        }
//    } // getShortName
    
    /*************************************************************************/
    
    /**
     *  From imported style file, the following info is created:
     *      - List of pre-selected types
     *      - List of hidden types
     *      - List of types having color assigned
     * 
     * 
     * 
        <?xml version="1.0" encoding="ISO-8859-1"?>
        <styleMap>
            <rule>
                <pattern>example.PersonTitle</pattern>
                <label>PersonTitle</label>
                <style>color:black;background:lightblue;checked:true;hidden:false;</style>
            </rule>
            <rule>
                <pattern>GovernmentTitle[@sofa='']</pattern>
                <label>GovernmentTitle:sofa</label>
                <style>color:black;background:white;checked:false;hidden:true;</style>
            </rule>
            <rule>
                <pattern>GovernmentTitle[@begin='']</pattern>
                <label>GovernmentTitle:begin</label>
                <style>color:black;background:white;checked:false;hidden:true;</style>
            </rule>
        </styleMap>
          
    */
    public List<TypeStyle> importAndApplyStyleMapFile(List<String> typeNames, final File styleMapFile) 
    {
        // Trace.err("Import: " + styleMapFile.getAbsolutePath());
        if (!styleMapFile.exists()) {
            return null;
        }
        setImportedStyleMapFile(styleMapFile.getAbsolutePath());
        
        // Remove all previous mapping
        hasTypeSystemStyle = false;
//        indexUsedColors = 0;
        preSelectedList.clear();
//        colorList.clear();
        List<String> hiddenList = new ArrayList<String>();
        
        Map<String, TypeStyle> typeNameToTypeStyleMap = new HashMap<String, TypeStyle>();
        
        //  hiddenList.add("uima.cpm.FileLocation"); //$NON-NLS

        Document parse = null;
        try {
            DocumentBuilder db = 
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
            FileInputStream stream = new FileInputStream(styleMapFile);
            parse = db.parse(stream);
            stream.close(); 

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;

        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
            return null;

        } catch (SAXException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        final Node root = parse.getDocumentElement();
        final NodeList nodeList = root.getChildNodes();
        // final ColorParser cParser = new ColorParser();

        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node node = nodeList.item(i);
            final String nodeName = node.getNodeName();
            // "rule" node ?
            if ( ! nodeName.equals("rule") ) { //$NON-NLS-1$
                continue;
            }
            
            // Collect type or pattern, label, and color text style
            NodeList childrenList = node.getChildNodes();
            String type = ""; //$NON-NLS-1$ 
            String label = ""; //$NON-NLS-1$ 
            String colorText = ""; //$NON-NLS-1$ 
            for (int j = 0; j < childrenList.getLength(); ++j) {
                final Node child = childrenList.item(j);
                final String childName = child.getNodeName();

                if (childName.equals("pattern")) { //$NON-NLS-1$ 
                    type = getTextValue(child);
                } else if (childName.equals("label")) { //$NON-NLS-1$ 
                    label = getTextValue(child);
                } else if (childName.equals("style")) { //$NON-NLS-1$ 
                    colorText = getTextValue(child);
                }
            }
            
            // Create a new TypeStyle
            TypeStyle typeStyle = createTypeStyle (type, label, colorText);
            if (typeStyle.isChecked()) {
                preSelectedList.add(typeStyle.getTypeName()); 
            }
            if (!typeStyle.isHidden()) {
//                colorList.add(typeStyle.getBackground()); 
            } else {
                // Hidden Type
                hiddenList.add(typeStyle.getTypeName()); 
            }
            
            typeNameToTypeStyleMap.put(typeStyle.getTypeName(), typeStyle);
        } // for
        hasTypeSystemStyle = true;
        
        // Apply the imported style file to types
        defaultTypeStyleMap.clear();
        if (typeNames != null) {
            // Save type names
            typeNameListSortedByDefColors.clear();
            typeNameListSortedByDefColors = new ArrayList<String>(typeNames);
        }
        List<TypeStyle> returnedList = new ArrayList<TypeStyle> (typeNames.size());
        for (String name: typeNameListSortedByDefColors) {
            TypeStyle style = typeNameToTypeStyleMap.get(name);
            if (style == null) {
                // Assign empty style
                style = new TypeStyle(name, TypeStyle.getShortName(name), null, null);
            }
            returnedList.add(style);
            defaultTypeStyleMap.put(name, style);
        }
        
        // Set hidden type names
        setHiddenTypeNames(hiddenList);
        
        return returnedList;
//        Iterator iter = defaultTypeStyleMap.entrySet().iterator();
//        int i = 0;
//        while (iter.hasNext()) {
//            Map.Entry e = (Map.Entry) iter.next();
//            if (typeNameToTypeStyleMap.containsKey(e.getKey())) {
//                // Overlap, i.e. type in default style is also in imported style
//                ((TypeStyle) e.getValue()).copyStyle((TypeStyle)typeNameToTypeStyleMap.get(e.getKey()));
//            } else {
//                // Clear style in defaultTypeStyleMap
//                ((TypeStyle) e.getValue()).clearStyle ();
//            }
//        }
        // mergeColors(styledTypeMap, mapBgFgColors);

//      _viewer.assignColorsFromList(colorList, typeList);
//      _viewer.assignCheckedFromList(notCheckedList);

//      hiddenList.add(HIDDEN_TYPES[0]); 
//      final String[] hiddenArr = new String[hiddenList.size()];
//      hiddenList.toArray(hiddenArr);
//      _viewer.setHiddenTypes(hiddenArr);

    }
    
    private TypeStyle createTypeStyle (String typeName, String typeLabel, String colorText) 
    {
        TypeStyle typeStyle = new TypeStyle(typeName, typeLabel, null, null);
        getTypeStyleFromString(typeStyle, Display.getCurrent(), colorText);
        return typeStyle;
    }
    
    /**
     * Assumes node has a text field and extracts its value.
     * @param node Node 
     * @return String possibly <code>null</code> 
     */
    static private String getTextValue(final Node node) {
        final Node first = node.getFirstChild();

        if (first != null) {
            return ((Text) first).getNodeValue().trim();

        } else { 
            return null;
        } 
    }

    
    static private File getStyleMapFile(AnalysisEngineDescription tad, String descFileName) throws IOException
    {
        File styleMapFile = new File(descFileName + "StyleMap.xml");
        if (!styleMapFile.exists()) {
            //generate default style map
            String xml = AnnotationViewGenerator.autoGenerateStyleMap(tad.getAnalysisEngineMetaData());
            
            PrintWriter writer;
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(styleMapFile)));
                writer.println(xml);
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return styleMapFile;
    }  
    
    static private File getStyleMapFile(TypeSystemDescription tsd, String descFileName) throws IOException
    {
        File styleMapFile = new File(descFileName + "StyleMap.xml");
        if (!styleMapFile.exists())
        {
            //generate default style map
            String xml = AnnotationViewGenerator.autoGenerateStyleMap(tsd);
            
            PrintWriter writer;
            try
            {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(styleMapFile)));
                writer.println(xml);
                writer.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return styleMapFile;
    }

    /**
     * @return the styledTypeMap
     * 
     * Called from "ColoredTypeTreeContentProvider" to display imported types in tree
     */
    public Map<String,TypeStyle> getStyledTypeMap() {
        return defaultTypeStyleMap;
    }

    public List<TypeStyle> getCurrentTypeStyleList () 
    {
        Iterator<TypeStyle> it = defaultTypeStyleMap.values().iterator();
        List<TypeStyle> list = new ArrayList<TypeStyle>(defaultTypeStyleMap.size());
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }
    
    /**
     * @return the mapBgFgColors
     */
//    public Map getMapBgFgColors() {
//        return mapBgFgColors;
//    }
//
//    /**
//     * @param mapBgFgColors the mapBgFgColors to set
//     */
//    public void setMapBgFgColors(Map mapBgFgColors) {
//        this.mapBgFgColors = mapBgFgColors;
//    }

    /**
     * @return the defaultColors
     */
//    public List getDefaultColors_NOT_USED () {
//        return defaultColors;
//    }

    /**
     * @param defaultColors the defaultColors to set
     */
//    public void setDefaultColors_NOT_USED (List defaultColors) {
//        this.defaultColors = defaultColors;
//    }

    /**
     * @return the hasTypeSystemStyle
     */
    public boolean hasTypeSystemStyle() {
        return hasTypeSystemStyle;
    }

    /**
     * @return the hiddenList
     */
    public List<String> getHiddenList() {
        return hiddenTypeNameList;
    }

    public void setHiddenTypeNames (List<String> typeNames) {
        hiddenTypeNameList = typeNames;
    }
    
    /**
     * @return the preSelectedList
     */
    public List<String> getPreSelectedList() {
        return preSelectedList;
    }
    
    /*************************************************************************/
    
    public static String getShortName (String fullName) {
        if (fullName == null) return null;
        
        int index = fullName.lastIndexOf('.');
        if ( index < 0 ) {
            return fullName;
        } else {
            return fullName.substring(index+1);
        }
    }

    /**
     * @return the importedStyleMapFile
     */
    public String getImportedStyleMapFile() {
        return importedStyleMapFile;
    }

    /**
     * @param importedStyleMapFile the importedStyleMapFile to set
     */
    public void setImportedStyleMapFile(String importedStyleMapFile) {
        this.importedStyleMapFile = importedStyleMapFile;
    }
}
