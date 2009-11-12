package org.apache.uima.casviewer.core.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.tools.internal.util.CasHelper;

/**
 * 
 * 
 * Notes:
 *  - Each CAS view may have differently selected types
 *  
 *  - CAS defaultView = cas.getView(CAS.NAME_DEFAULT_SOFA);
 */
public class CASObjectView implements ICASObjectView
{
    static final public String CASDIFF_VIEW_TYPE           = "org.apache.uima.casviewer.CasDiffView";
    static final public String CASDIFF_VIEW_SUPER_TYPE     = "uima.cas.AnnotationBase";
    static final public String CASDIFF_VIEW_FEATURE_ADDED    = "addedFeatureStructures";
    static final public String CASDIFF_VIEW_FEATURE_DELETED  = "deletedFeatureStructures";
    static final public String CASDIFF_VIEW_FEATURE_CHANGED  = "changedFeatureStructures";
    static final public String CASDIFF_VIEW_FEATURE_MATCHED  = "matchedFeatureStructures";

    
    private BaseCASObject           baseCASObject;
    private SofaFS                  sofa;
    private String                  sofaId;
//    private TypeSystemStyle         tsStyle;
    
    protected CAS                   casView;
    protected TypeSystemDescription typeSystemDescription;
    protected TypePriorities        typePriorities;
    protected TypeSystem            typeSystem;

    protected CasIndexRepository    casIndexRepository;
    
    // Key: type name; Value: list of annotations (AnnotationObject) for that type
    private Map<String, List<AnnotationObject>> typeName2AnnotationsMap = new HashMap<String, List<AnnotationObject>>(10);    

    // Key: Entity; Value: list of EntityAnnotation's instances for this Entity
//    private Map             mapEntity2AnnotationList = new HashMap();
    
    // List of types having annotations. These "types" are collected from AnnotationIndex.
    // Therefore, this list may NOT contains all the types in the type system.
    // The map "typeName2AnnotationsMap" can be used to get all the annotation for a 
    // particular type name.
    
    // List of types having annotations. 
    private List<Type>      typesHavingAnnotations;
    
    // List of annotations indexed by the default AnnotationIndex
    private LinkedList<AnnotationObject> indexedAnnotationList = new LinkedList<AnnotationObject>();
    
    private boolean         isProcessed = false; // used by processAnnotatedTypes()
    
    // List of pre-selected types (read from type system style file)
    protected Set           preSelectedTypes = new HashSet();
    
    // List of hidden types (read from type system style file)
    // These types never appear in UI of CASView
    protected Set           hiddenTypes = new HashSet();    
    
    // List of currently selected Types for this CAS View
    protected Set           currentlySelectedTypes = new HashSet();
    
    protected Set           visibleTypes;
    private List mHighFrequencyTypes = new ArrayList();
    
    //Mode constants
//    private static final short MODE_ANNOTATIONS = 0;
//    private static final short MODE_ENTITIES = 1;
//    private short mViewMode = MODE_ANNOTATIONS;
    
    private static String[] DEFAULT_HIDDEN_FEATURES = { "sofa" };
//    private boolean mConsistentColors = true;

    private Set<String> mHiddenFeatureNames = new HashSet<String>();

//    private Boolean mEntityViewEnabled; //null if user has made no choice

    /*************************************************************************/

    public CASObjectView (BaseCASObject aCASObject, SofaFS aSofaFS) {
        this(aCASObject, aSofaFS, aSofaFS.getStringValue(aSofaFS.getType().getFeatureByBaseName(CAS.FEATURE_BASE_NAME_SOFAID)));
    }

    @Deprecated // Use CASObjectView (BaseCASObject aCASObject, SofaFS sofa)
    protected CASObjectView (BaseCASObject aCASObject, SofaFS sofa, String sofaId) {
        this.baseCASObject = aCASObject;
        this.sofa       = sofa;
        this.sofaId     = sofaId;
        this.casView = aCASObject.getCAS().getView(sofa);
        this.typeSystemDescription = aCASObject.typeSystemDescription;
        this.typePriorities        = aCASObject.typePriorities;
    }
    
    /*************************************************************************/

    // Used by FSIndexSectionPart
    static public AnnotationObject createAnnotationObjectFromFS (FeatureStructure fs, boolean show)
    {
        int begin;
        int end;
        Type type = fs.getType();
        if (fs instanceof AnnotationFS) {
            begin = fs.getIntValue(type.getFeatureByBaseName("begin"));
            end = fs.getIntValue(type.getFeatureByBaseName("end"));
        } else {
            begin = 0;
            end = 0;
        }
        return new AnnotationObject(fs, begin, end, show);
    }
    
    /*************************************************************************/

    /**
     * Set the list of types that occur most frequently.  This method assigns
     * the most distinguishable colors to these types.
     * 
     * @param aTypeNames names of types that are occur frequently.  Ideally these should be
     *    ordered by frequency, with the most frequent being first.
     */
    public void setHighFrequencyTypes(String[] aTypeNames)
    {
        //store these types for later
        mHighFrequencyTypes.clear();
        mHighFrequencyTypes.addAll(Arrays.asList(aTypeNames));
        // mTypeNameToColorMap.clear();
        // assignColors(mHighFrequencyTypes);
    }

    /**
     * Set the list of types that will be highlighted in the viewer.  Types not in this
     * list will not appear in the legend and will never be highlighted.  If this method
     * is not called, the default is to show all types in the CAS (except those specifically
     * hidden by a call to {@link #setHiddenTypes(String[])}. 
     * 
     * @param aTypeNames names of types that are to be highlighted.  Null indicates that
     *   all types in the CAS should be highlighted.
     */
    public void setDisplayedTypes(String[] aDisplayedTypeNames)
    {
        if (aDisplayedTypeNames == null) {
            visibleTypes = null;
        } else {
            visibleTypes = new HashSet();
            visibleTypes.addAll(Arrays.asList(aDisplayedTypeNames));
        }
    }

    /**
     * Set the list of types that will NOT be highlighted in the viewer.
     * 
     * @param aTypeNames names of types that are never to be highlighted.
     */
    public void setHiddenTypes(String[] aTypeNames)
    {
        hiddenTypes.clear();
        hiddenTypes.addAll(Arrays.asList(aTypeNames));
    }

    /**
     * Configures the initially selected types in the viewer.  If not called, all
     * types will be initially selected.
     *  
     * @param aTypeNames array of fully-qualified names of types to be initially selected
     */
    public void setInitiallySelectedTypes(String[] aTypeNames)
    {
        preSelectedTypes = new HashSet();
        for (int i = 0; i < aTypeNames.length; i++)
        {
            preSelectedTypes.add(aTypeNames[i]);
        }
        
        currentlySelectedTypes.addAll(preSelectedTypes);
    }

    /**
     * Configures the viewer to hide certain features in the
     * annotation deatail pane.
     *  
     * @param aFeatureName array of (short) feature names to be hidden
     */
    public void setHiddenFeatures(String[] aFeatureNames)
    {
        mHiddenFeatureNames.clear();
        //add default hidden features
        mHiddenFeatureNames.addAll(Arrays.asList(DEFAULT_HIDDEN_FEATURES));
        //add user-defined hidden features
        mHiddenFeatureNames.addAll(Arrays.asList(aFeatureNames));
    }
       
    /*************************************************************************/
    
    /* (non-Javadoc)
     * @see com.ibm.uima.casviewer.core.ICASObjectView#getTypeTree()
     */    
    public TypeTree getTypeTree ()
    {
        return baseCASObject.getTypeTree();
    }
    
    public TypeTree newTypeTree ()
    {
        return baseCASObject.newTypeTree();
    }
        
    /**
     *  Decorate each TypeNode of TypeTree with AnnotationObjectsNode containing:
     *   - AnnotationObject(s) 
     * 
     * @param typeTree
     * @return void
     */
    public void decorateTypeTreeWithAnnotationObjects (TypeTree typeTree) { 
        TypeNode typeNode;
        for (Type type: getTypesHavingAnnotations()) {
            List<AnnotationObject> annotationlist = getAnnotationsForTypeName(type.getName());
            if ( CASDIFF_VIEW_TYPE.equals(type.getName()) ) {
                Trace.err("Found " + CASDIFF_VIEW_TYPE);
            } else {
                typeNode = typeTree.getTypeNode(type.getName());
                if (typeNode != null) {
                    AnnotationObjectsNode n = typeNode.getAnnotationNode();
                    if (n == null) {
                        n = new AnnotationObjectsNode(annotationlist);
                        typeNode.setAnnotationNode(n);
                    } else {
                        // Replace OLD annotations
                        n.setAnnotationList(annotationlist);
                    }
                } else {
                    Trace.err("Cannot find type in tree: " + type.getName());
                }
            }
        }       
    }
    
    /**
     * Visit tree from "fromNode" and add annotation objects to 
     * each type. The AnnotationObject(s) are newly created 
     * and put under a new BaseNode. 
     * 
     *          fromNode
     *              - AnnotationListNode annotationNode
     *                       List<AnnotationObject>
     *                              |- AnnotationObject 1
     *                              |- AnnotationObject 2
     *                              |- ...
     *                                  List<AnnotationObject>
     * 
     * @param fromNode
     * @param casView
     * @return void
     */
    static public void visitTreeAndAddAnnotations (TypeNode fromNode, CAS casView)
    {        
        // Get "Type" object to work with FSIndex when retrieving FS from index
        String typeName = ((TypeDescription) fromNode.getObject()).getName();
        Type type = casView.getTypeSystem().getType(typeName);

        // Is sub-type of AnnotationBase ?
        Type annotationBase = casView.getTypeSystem().getType("uima.cas.AnnotationBase");
        if (!typeName.equals("uima.cas.TOP") &&
                !casView.getTypeSystem().subsumes(annotationBase, type)) {
            return;
        }
        FSIndex fsIndex = CasHelper.getIndexForType(casView, typeName);
        if  (!typeName.equals("uima.cas.TOP") && !typeName.equals("uima.cas.AnnotationBase") 
                && (fsIndex == null || fsIndex.size() == 0)) {
            // Skip Type having NO Annotation
            return;
        }

        // Add FeatureStructures as children of fromNode
        if (fsIndex != null) {
            FSIterator iter = fsIndex.iterator();
            List<AnnotationObject> selfAnnotations = new ArrayList<AnnotationObject>();
            while (iter.isValid()) {
                FeatureStructure fs = iter.get();
                if (fs.getType() == type) {
                    // Create new object for this type's annotation
                    String coveredText = null;
                    if (fs instanceof AnnotationFS) {
                        coveredText = ((AnnotationFS)fs).getCoveredText();
                    }
                    selfAnnotations.add(new AnnotationObject (fs, coveredText));
                }
                iter.moveToNext();
            }
            if (selfAnnotations.size() > 0) {
                AnnotationObjectsNode n = new AnnotationObjectsNode(selfAnnotations);
                fromNode.setAnnotationNode(n);
            }
        }
        
        // Visit Children Types 
        if ( fromNode.getChildren() != null ) {
            // Collection c = fromNode.getChildren().values();
            Iterator<TypeNode> iter = fromNode.getChildrenIterator();                
            while (iter.hasNext()) {
                visitTreeAndAddAnnotations (iter.next(), casView);
            }                  
        }        
    }
        
    /*************************************************************************/
    
    public void setTypeSelectionByName (List types, boolean selection)
    {
        if ( selection ) {
            currentlySelectedTypes.add(types);
        } else {
            if (types == null) {
                currentlySelectedTypes.clear();
            } else {  
                currentlySelectedTypes.remove(types);
            }
        }       
    }

    public List<Type> getTypesHavingAnnotations ()
    {
        if (!isProcessed) {
            processCasView();
            // processEntities ();
        }
        // typeName2TypeMap.values();
        return typesHavingAnnotations;
    }
    
    public LinkedList<AnnotationObject> getIndexedAnnotations ()
    {
        if (!isProcessed) {
            processCasView();
        }

        return indexedAnnotationList;        
    }
    
    public String getDocumentText() {
        if (casView == null) {
            return "";
        }
        return casView.getDocumentText();
    }

    public Type getTypeFromTypeSystem (String typeName) {
        return getTypeSystem().getType(typeName);
    }
    
    public List<AnnotationObject> getAnnotationsForTypeName(String typeName) {
        return typeName2AnnotationsMap.get(typeName);
    }

    protected void preProcessCasView () {
    }
    
    protected AnnotationObject createAnnotationObject(FeatureStructure fs, 
                                    int begin, int end, String doc, int maxTextSpan) {
        AnnotationObject newAnnot = new AnnotationObject (fs, begin, end);
        newAnnot.setTextSpan(CasHelper.getAnnotationTextSpan(doc, (AnnotationFS) fs, maxTextSpan));
        
        return newAnnot;
    }

    private void processCasView () 
    {
        if (isProcessed) return;
        isProcessed = true;
        
        if (typesHavingAnnotations == null) {
            typesHavingAnnotations = new ArrayList<Type> ();
        } else {
            typesHavingAnnotations.clear();
        }
        
        // Clear previous annotations
        indexedAnnotationList.clear();
                
        String  doc = casView.getDocumentText();
        if (doc == null) {
            doc = "";
        }

        preProcessCasView ();
        
        // Iterate over the default annotation index.
        // Create a list "annotListPerType" of annotations for EACH type
        List<AnnotationObject>    annotListPerType = null;// For each type, list of its annotations    
        FSIterator iter = casView.getAnnotationIndex().iterator(); // Get the default annotation index.
        while (iter.isValid())
        {
            // Get 1 annotation
            AnnotationFS fs = (AnnotationFS) iter.get();
            iter.moveToNext();
            
            // Get the type of this annotation
            Type type = fs.getType();
            
            // PrintAnnotations.printFS((FeatureStructure) fs, mTCAS, 1, System.out);
            int begin = fs.getBegin();  // start from 0
            int end   = fs.getEnd();
//          if (begin != end && end == mTCAS.getDocumentText().length()) {
//              end--;
//          }
            if (begin == end) {
                // Skip 0-length annotations
                // E.g., SourceDocumentInformation
                continue;
            }
            
            // See this Type before ?
            if ( ! typesHavingAnnotations.contains(type) ) {
                // New Type for Annotations
                // Check that this type should be displayed.
                if ((visibleTypes != null && ! typeNamesContains(visibleTypes, type.getName()))
                        || typeNamesContains(hiddenTypes, type.getName())) {
                    continue;
                }

                typesHavingAnnotations.add(type);
                
                // Map type name -> Type
                // typeName2TypeMap.put(type.getName(), type);
                
                // Map type name -> annotation list
                annotListPerType = new ArrayList<AnnotationObject>();
                typeName2AnnotationsMap.put(type.getName(), annotListPerType);
            } else {
                // Existing Type. Get existing annotation list
                annotListPerType = typeName2AnnotationsMap.get(type.getName());
            }
            // Select color for the type 
//            if (tsStyle != null) {
//                typeStyle = tsStyle.getTypeStyle(type.getName());
//            }
            
            // Create new object for this type's annotation
            // Try to reuse AnnotationObject created at somewhere
            AnnotationObject newAnnot = createAnnotationObject(fs, begin, end, doc, 50);
            annotListPerType.add(newAnnot);
                        
            // Sort Annotations (sortedAnnotationList) by BEGIN feature
//            AnnotationObject annot;
//            boolean done = false;
            // Trace.trace("sortedAnnotationList:" + sortedAnnotationList.size());
            indexedAnnotationList.add(newAnnot);
            
            // Sorted ? Use order set by AnnotationIndexRepository 
            // addAnnotationToSortedList(indexedAnnotationList, newAnnot);
                       
//            if (type.getName().compareTo("uima.tcas.DocumentAnnotation") != 0 && begin != end) {
//                // Trace.trace( type.getName() + "Begin: " + begin + " ; end: " + end);
//                StyleRange styleRange = new StyleRange ();
//                styleRange.foreground = styledTextDoc.getDisplay().getSystemColor(SWT.COLOR_BLUE);
//                styleRange.start = begin;
//                styleRange.length= end - begin;
//                styledTextDoc.setStyleRange(styleRange);
//            }
        }
//        for (int i=0; i<indexedAnnotationList.size(); ++i) {
//            Annot annot = ((Annot) indexedAnnotationList.get(i));
//            Trace.trace("annot: " + annot.type.getShortName() + "["
//                    + annot.begin + " , " + annot.end + "]");
//        }

        // Trace.trace("annotatedTypes.size(): " + annotatedTypes.size());
        // Trace.trace("indexedAnnotationList.size(): " + indexedAnnotationList.size());
    } // processAnnotatedTypes
       

    /**
     * Does wildcard matching to determine if a give type name is "contained" in
     * a Set of type names.
     * 
     * @param names
     *            Type names, which may include wildcards (e.g, uima.tt.*)
     * @param name
     *            A type name
     * @return True iff name matches a name in type names
     */
    private boolean typeNamesContains(Set<String> names, String name)
    {
      if (names.contains(name))
        return true;
      else
      {
        Iterator<String> namesIterator = names.iterator();
        while (namesIterator.hasNext())
        {
          String otherName = (String) namesIterator.next();
          if (otherName.indexOf('*') != -1) {
            if (wildCardMatch(name, otherName)){
              return true;
            }
          } else {
            if (otherName.equalsIgnoreCase(name))
            {
              return true;
            }
          }
        }
      }
      return false;
    }

    /**
     * Helper for {@link #typeNamesContains(HashSet, String)}.
     * 
     * @param s
     *            A litteral string
     * @param pattern
     *            A string that includes one or more *'s as wildcards
     * @return True iff the string matches the pattern.   
     */
    private boolean wildCardMatch(String s, String pattern)
    {
      StringBuffer regexpPatternBuffer = new StringBuffer();
      for (int i = 0; i < pattern.length(); i++)
      {
        char c = pattern.charAt(i);
        if (c == '*')
          regexpPatternBuffer.append('.');
        else if (c == '.')
          regexpPatternBuffer.append('\\');
        if (Character.isLetter(c))
        {
          regexpPatternBuffer.append('(').append(Character.toLowerCase(c)).append('|').append(
              Character.toUpperCase(c)).append(')');
        }
        else
        {
          regexpPatternBuffer.append(c);
        }
      }

      return s.matches(new String(regexpPatternBuffer));
    }

    protected void addAnnotationToSortedList (LinkedList sortedAnnotationList, AnnotationObject newAnnot)
    {
        AnnotationObject annot;
        int begin = newAnnot.begin;
        int end   = newAnnot.end;
        boolean done = false;
        for (int i=0; i<sortedAnnotationList.size(); ++i) {
            // Trace.trace("" + i);
            annot = (AnnotationObject) sortedAnnotationList.get(i);
            int s = annot.begin;
            int e = annot.end;
            if (e <= begin) {
                //       begin<------>end
                //  s<---->e
                continue;
            } else if (e <= end) {
                if (s > begin) {
                    //  begin<-------------->end
                    //         s<-------->e
                    sortedAnnotationList.add(i, newAnnot);
                    Trace.bug("Insert annot " + newAnnot.type.getName() + "(begin:" + newAnnot.begin + ")" + " at " + i);
                    done = true;
                    break;
                } else {
                    // s <= begin
                    //       begin<-------------->end
                    //  s<----------->e
                    continue; // goto next
                }
            } else {
                // e > end
                //  begin<------>end
                //  .......-------->e
                if (s <= begin) {
                    //  begin<------>end
                    //  s<------------>e
                    continue;
                } else {
                    // s > begin
                    //  begin<------>end
                    //         s<--------->e
                    sortedAnnotationList.add(i, newAnnot);
                    Trace.bug("Insert annot " + newAnnot.type.getName() + "(begin:" + newAnnot.begin + ")" + " at " + i);
                    done = true;
                    break;
                } 
            }                    
        }
        if (!done) {
            if (sortedAnnotationList.size() > 0) {
                sortedAnnotationList.addLast(newAnnot);
                // Trace.trace("NEW annot is inserted at LAST");
            } else {
                // 1st element
                sortedAnnotationList.addFirst(newAnnot);
                // Trace.trace("NEW annot is inserted at FIRST");
            }
//        } else {
//            Trace.trace("NEW annot at " + (indexAnnotation-1) + " is inserted at: " + i);
        }
        
    }
    
    /*************************************************************************/
    
    public CasIndexRepository getCasIndexRepository () {
        if (casIndexRepository == null) {
            casIndexRepository = CasIndexRepository.createInstance(getCurrentView());
        }
        return casIndexRepository;
    }
    
    public TypeSystem getTypeSystem () {
        if (typeSystem == null) {
            typeSystem = baseCASObject.getTypeSystem();
        }
        return typeSystem;
    }
    
    public TypeSystemDescription getTypeSystemDescription() {
        return typeSystemDescription;
    }
    
    public TypePriorities getTypePriorities() {
        return typePriorities;
    }

    public SofaFS getSofa() {
        return sofa;
    }

    public String getSofaId() {
        return sofaId;
    }

    public CAS getCurrentView() {
        return casView;
    }

    public Type getTypeByName(String typeName) {
        return typeSystem.getType(typeName);
    }

    /**
     * @return the baseCASObject
     */
    public BaseCASObject getBaseCASObject() {
        return baseCASObject;
    }

}
