package org.apache.uima.casviewer.core.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;

/**
 * Create a CasIndexRepository (a tree of TreeNode) and build indexes hierarchy as follows:
 *      _root
 *          "AnnotationIndex" - Annotation (default index)
 *              ... (indexes of subtypes of Annotation) ...
 *          Index_Name_1    - Type1             (as defined in xml descriptor)
 *              Index_for_Subtype_of_Type1
 *              Index_for_Subtype_of_Type1
 *          Index_Name_2    - Type2             (as defined in xml descriptor)
 *              ... (indexes of subtypes) ...
 * 
 */
public class CasIndexRepository {

    static public Type      stringType = null;
    
    /*************************************************************************/
    
    private CAS             aCas = null;
    private BaseNode        _root = null;
    
    // Key: type name; Value: list of annotations for that type
    // private Map     typeName2AnnotationsMap = new HashMap(10);    
    private Map     typeName2UFSIndexMap = new HashMap(10);    
    private Map     typeName2TypeMap     = new HashMap(10);
    
    /*************************************************************************/
    
    private CasIndexRepository() {
        super();
    }
    
    protected CasIndexRepository(CAS aCas) {
        super();
        this.aCas = aCas;
        stringType = aCas.getTypeSystem().getType(CAS.TYPE_NAME_STRING);        
    }
    
    /**
     * Create a CasIndexRepository and build indexes hierarchy as follows:
     * 
     * @param aCas
     * @return
     * @return CasIndexRepository
     */
    public static CasIndexRepository createInstance(CAS aCas)
    {
        CasIndexRepository instance = new CasIndexRepository (aCas);
        instance.createRootIndexTree(aCas);
        
        return instance;
    }
    
    /*************************************************************************/
    
    public BaseNode getRoot ()
    {
        return _root;
    }
    
    public CAS getCas () {
        return aCas;
    }
    
    /**
     * Get list of annotations from type full name
     * 
     * @param typeName
     * @return void
     */
//    public List getAnnotationListByTypeName (String typeName)
//    {
//        UFSIndex uFSIndex = (UFSIndex) typeName2UFSIndexMap.get(typeName);
//        if (uFSIndex != null) {
//            return uFSIndex.getFSList();
//        }
//        return null;
//    } // getAnnotationListByTypeName
    
    /**
     * Get UIMA type object by type's full name
     * 
     * @param typeName
     * @return
     * @return Type
     */
    public Type getTypeByName (String typeName) 
    {
        return (Type) typeName2TypeMap.get(typeName);
    } // getTypeByName
    
    /*************************************************************************/
        
    /**
     *  Create tree of indexes. The roots are the named indexes as defined
     *  in the Xml descriptor.
     *  
     * @param aCas
     * @return void
     */
    protected void createRootIndexTree(CAS aCas) {
        if (aCas == null) {
            return;
        }
        if (_root == null) {
            _root = new BaseNode("root", null, new ArrayList());
        }
        
        FSIndexRepository ir = aCas.getIndexRepository();
        Iterator it = ir.getLabels();
        while (it.hasNext()) {
            String label = (String) it.next();
            FSIndex index = ir.getIndex(label);
//            Trace.trace("label: " + label + " ; type: " + index.getType()
//                    + " ; size: " + index.size());
            createIndexSubTree(_root, index.getType(), aCas.getTypeSystem(),
                    label, ir, 1);
        }        
    }
    
    /**
     * 
     *  TreeNode
     *          - label     : type's name
     *          - obj type  : ITEM_TYPE_U_FS_INDEX
     *          - object    : UFSIndex
     * 
     * @param root
     * @param type
     * @param ts
     * @param label
     * @param ir
     * @param level
     * @return void
     */
    private void createIndexSubTree(BaseNode root, Type type, TypeSystem ts,
                                String label, FSIndexRepository ir, int level) 
    {
        // Get index for (label, type)
        FSIndex index = ir.getIndex(label, type);
        UFSIndex uFSIndex = new UFSIndex(this, label, index, level);

        // Add to tree
        BaseNode node = new BaseNode(type.getShortName(), uFSIndex, new ArrayList());
        node.setObjectType(IItemTypeConstants.ITEM_TYPE_U_FS_INDEX);
        root.addChild(node);
        
        // Book-keeping (type name,type) and (type name, UFSIndex)
        if ( ! typeName2TypeMap.containsKey(type.getName()) ) {
            // New Type
            typeName2TypeMap.put(type.getName(), type);
            typeName2UFSIndexMap.put(type.getName(), uFSIndex);
        }
        
        Vector types = ts.getDirectlySubsumedTypes(type);
        final int max = types.size();
        for (int i = 0; i < max; i++) {
            createIndexSubTree(node, (Type) types.get(i), ts, label, ir, level+1);            
        }        
    }
    
    /*************************************************************************/
    

} // CasIndexRepository
