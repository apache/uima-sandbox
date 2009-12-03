package org.apache.uima.casviewer.ui.internal.index;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.ArrayFS;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.DebugFSLogicalStructure;
import org.apache.uima.casviewer.core.internal.BaseNode;
import org.apache.uima.casviewer.core.internal.CasIndexRepository;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.UFSIndex;
import org.apache.uima.casviewer.core.internal.UFeature;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FSIndexContentProvider implements ITreeContentProvider 
{
    private static Object[] EMPTY_ARRAY = new Object[0];

    private BaseNode               rootNode    = null;

    // Preferences
    private boolean hideNoValueFeature      =  false;    
    private boolean showOneLine             = false;
    
    /*************************************************************************/
    
    public FSIndexContentProvider(boolean showOneLine) {
        super();
        this.showOneLine = showOneLine;
    }

    public void showOneLineView (boolean showOneLine) {
        this.showOneLine = showOneLine;
    }
    
    public void hideNoValueFeature (boolean hide)
    {
        hideNoValueFeature = hide;
    }
    
    public void setViewerLayoutToFlatOrTree (boolean flat)
    {
        showOneLine = flat;
    }

    /*************************************************************************/

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parent) 
    {
        if (parent instanceof BaseNode) {
            int kind = ((BaseNode)parent).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX) {
                UFSIndex index = (UFSIndex) ((BaseNode)parent).getObject();
                if (index.size() == 0) {
                    // May have other children 
                    return ((BaseNode)parent).getChildren().toArray();
                }
                
                // Add label "feature structures" node
                BaseNode fNode = new BaseNode("FS [" + index.size() + "]", null, new ArrayList());
                fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX);
                fNode.setObject(index);
                
                // Add children, if any
                int count = 1;
                if (((BaseNode)parent).getChildren() != null) {
                    count += ((BaseNode)parent).getChildren().size();
                }
                Object[] objs = new Object[count];
                objs[0] = fNode;
                if (count > 1) {
                    List<BaseNode> nodes = ((BaseNode)parent).getChildren();
                    int i = 1;
                    for (BaseNode node: nodes) {
                        objs[i++] = node;
                    }
                }
                
                return objs;
                
            } else if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX) {
//                Object[] objs = ((BaseNode)parent).getChildren();
//                if (objs != null) {
//                    return objs;
//                }
                
                UFSIndex index = (UFSIndex) ((BaseNode)parent).getObject();
                if (index.size() == 0) { 
                    return null;
                }
//                FSIterator it = index.iterator();
//                
//                Object[] objs = new Object[index.size()];
//                int i = 0;
//                for (it.moveToFirst(); it.isValid(); it.moveToNext()) {
//                    // FeatureStructure fs = it.get();
//                    objs[i] = new UFeatureStructure(it.get(), i);
//                    ++i;
//                }
//                return objs;
//                objs = index.getUFeatureStructures();
//                ((BaseNode)parent).addChild()
                return index.getUFeatureStructures();
            }
            
        } else if (parent instanceof UFSIndex) {
            UFSIndex index = (UFSIndex) parent;
            if (index.size() == 0) { 
                return null;
            }
//            FSIterator it = index.iterator();
//            
//            Object[] objs = new Object[index.size()];
//            int i = 0;
//            for (it.moveToFirst(); it.isValid(); it.moveToNext()) {
//                // FeatureStructure fs = it.get();
//                objs[i] = new UFeatureStructure(it.get(), i);
//                ++i;
//            }
//            return objs;
            return index.getUFeatureStructures();
            
        } else if (parent instanceof UFeatureStructure
                || parent instanceof FeatureStructure) {
            if ( showOneLine ) {
                return null;
            }
            
            FeatureStructure fs;
            if (parent instanceof UFeatureStructure) {
                fs = ((UFeatureStructure)parent).getUimaFeatureStructure();
            } else {
                fs = (FeatureStructure) parent;
            }
            
            List aFeatures = fs.getType().getAppropriateFeatures();
            if (aFeatures.size() > 0) {
                Object[] objs = new Object[aFeatures.size()];
                for (int i=0; i<aFeatures.size(); ++i) {
                    objs[i] = new UFeature(fs, (Feature) aFeatures.get(i));
                }
                return objs;
            }            
        } else if (parent instanceof UFeature) {
            return getChildren ((UFeature)parent);
        }
        return null;
    }
        
    public Object[] getChildren (UFeature uFeature)
    {
        // Non-primitive feature ONLY
        if ( uFeature.isPrimitive() ) {
            // Primitive feature
            Trace.bug("SHOULD NOT BE primitive feature: feature name=" 
                    + uFeature.getShortName() + " ; range type: " + uFeature.getRange().getName());
            return null;
        }
        FeatureStructure fs = uFeature.getFeatureValue();
        if (fs == null) {
            // Trace.bug("No FeatureValue");
            return null;
        }
        if (fs instanceof StringArrayFS) {
            return ((StringArrayFS) fs).toArray();
        }
        if (fs instanceof IntArrayFS) {
            int[] intArray = ((IntArrayFS) fs).toArray();
            Object[] objs = new Object[intArray.length];
            for (int i=0; i<intArray.length; ++i) {
                objs[i] = new Integer(intArray[i]);
            }
            return objs;
        }
        if (fs instanceof FloatArrayFS) {
            float[] floatArray = ((FloatArrayFS) fs).toArray();
            Object[] objs = new Object[floatArray.length];
            for (int i=0; i<floatArray.length; ++i) {
                objs[i] = new Float(floatArray[i]);
            }
            return objs;
        }
        if (fs instanceof ArrayFS) {
            // if ( ((ArrayFS) fs).size() == 0 ) {
                Trace.trace(fs.getType().getShortName() + " is ArrayFS with length = " + ((ArrayFS) fs).size());
            // }
            return ((ArrayFS) fs).toArray();
        }
        
        TypeSystem ts = fs.getCAS().getTypeSystem();
        Type fsType   = fs.getType();
        if (ts.subsumes(ts.getType("uima.cas.FloatList"), fsType)) {
            return (Object[]) DebugFSLogicalStructure.floatListToArray(fs);
        }
        if (ts.subsumes(ts.getType("uima.cas.IntegerList"), fsType)) {
            return (Object[]) DebugFSLogicalStructure.integerListToArray(fs);
        }
        if (ts.subsumes(ts.getType("uima.cas.StringList"), fsType)) {
            return (Object[]) DebugFSLogicalStructure.stringListToArray(fs);
        }
        if (ts.subsumes(ts.getType("uima.cas.FSList"), fsType)) {
            return (Object[]) DebugFSLogicalStructure.fsListToArray(fs);
        }
        
        
        List aFeatures = fs.getType().getAppropriateFeatures();
        if (aFeatures.size() > 0) {
            Object[] objs = new Object[aFeatures.size()];
            for (int i=0; i<aFeatures.size(); ++i) {
                objs[i] = new UFeature(fs, (Feature) aFeatures.get(i));
            }
            return objs;
        }            
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) 
    {
        if (element instanceof BaseNode) {
            if ( ((BaseNode)element).getChildren().size() > 0 ) {
                return true;
            }
            // Check item type
            int kind = ((BaseNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_U_FS_INDEX
                || kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX) {
                if ( ((UFSIndex) ((BaseNode)element).getObject()).size() > 0 ) {
                    // Index has annotations
                    return true;
                }
            } else if (kind == IItemTypeConstants.ITEM_TYPE_U_FS) {
                return true;
            }
        } else if (element instanceof UFSIndex) {
            if ( ((UFSIndex) element).size() > 0 ) {
                return true;
            }
        } else if (element instanceof UFeatureStructure
                || element instanceof FeatureStructure) {
            return !showOneLine;
        } else if (element instanceof UFeature) {
            if ( ! ((UFeature)element).isPrimitive() ) {
                // Non-primitive feature
                if ( ((UFeature)element).getFeatureValue() != null ) {
                    return true;
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) 
    {
        if ( inputElement instanceof UFSIndex ) {
            if (rootNode == null) {
                rootNode = new BaseNode(((UFSIndex) inputElement).getUimaFSIndex().getType().getName(), null, new ArrayList());
                rootNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_U_FS_INDEX);
                rootNode.setObject(inputElement);
            }
            
            // Add label for FSIndex
            Object[] objs = new Object[1];
            objs[0] = rootNode;
            return objs;
        }
        Trace.trace("Unknown inputElement: " + inputElement.getClass().getName());
        return EMPTY_ARRAY;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    // "newInput" will be UFSIndex
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput != null) {
            if ( !(newInput instanceof UFSIndex) ) {
                Trace.bug("Unknown input: " + newInput.getClass().getName());
                return;
            }
            rootNode = null;
        }
    }

    /*************************************************************************/
    
    public static void getFSValues (FeatureStructure aFS, CAS aTCAS, int aNestingLevel, 
            PrintStream aOut)
    {
        Type stringType = CasIndexRepository.stringType;
        
        // printTabs(aNestingLevel, aOut);
        aOut.println(aFS.getType().getName());
        
        //print all features
        List aFeatures = aFS.getType().getAppropriateFeatures();
        Iterator iter = aFeatures.iterator();
        while (iter.hasNext())
        {
            Feature feat = (Feature)iter.next();
            // printTabs(aNestingLevel + 1, aOut);
            //print feature name
            aOut.print(feat.getShortName());
            aOut.print(" = ");
            //prnt feature value (how we get this depends on feature's range type)
            String rangeTypeName = feat.getRange().getName();
            if (aTCAS.getTypeSystem().subsumes(stringType, feat.getRange())) {
//              must check for subtypes of string
                String str = aFS.getStringValue(feat);
                if (str == null) {
                    aOut.println("null");
                } else {
                    aOut.print("\"");
                    if (str.length() > 64) {
                        str = str.substring(0,64) + "...";
                    }  
                    aOut.print(str);
                    aOut.println("\""); 
                } 
            } else if (CAS.TYPE_NAME_INTEGER.equals(rangeTypeName)) {
                aOut.println(aFS.getIntValue(feat));        
            } else if (CAS.TYPE_NAME_FLOAT.equals(rangeTypeName)) {
                aOut.println(aFS.getFloatValue(feat));        
            } else if (CAS.TYPE_NAME_STRING_ARRAY.equals(rangeTypeName)) {
                StringArrayFS arrayFS = (StringArrayFS)aFS.getFeatureValue(feat);
                if (arrayFS == null) {
                    aOut.println("null");
                } else {  
                    String[] vals = arrayFS.toArray();
                    aOut.print("[");
                    for (int i=0; i < vals.length - 1; i++) {
                        aOut.print(vals[i]);
                        aOut.print(',');
                    }
                    if (vals.length > 0) {
                        aOut.print(vals[vals.length - 1]);
                    }
                    aOut.println("]\"");
                }  
            } else if (CAS.TYPE_NAME_INTEGER_ARRAY.equals(rangeTypeName)) {
                IntArrayFS arrayFS = (IntArrayFS)aFS.getFeatureValue(feat);
                if (arrayFS == null) {
                    aOut.println("null");
                } else {  
                    int[] vals = arrayFS.toArray();
                    aOut.print("[");
                    for (int i=0; i < vals.length - 1; i++)
                    {
                        aOut.print(vals[i]);
                        aOut.print(',');
                    }
                    if (vals.length > 0)
                    {
                        aOut.print(vals[vals.length - 1]);
                    }
                    aOut.println("]\"");
                }  
            } else if (CAS.TYPE_NAME_FLOAT_ARRAY.equals(rangeTypeName)) {
                FloatArrayFS arrayFS = (FloatArrayFS)aFS.getFeatureValue(feat);
                if (arrayFS == null)
                {
                    aOut.println("null");
                } else {  
                    float[] vals = arrayFS.toArray();
                    aOut.print("[");
                    for (int i=0; i < vals.length - 1; i++)
                    {
                        aOut.print(vals[i]);
                        aOut.print(',');
                    }
                    if (vals.length > 0) {
                        aOut.print(vals[vals.length - 1]);
                    }
                    aOut.println("]\"");
                }  
            } else {
                // non-primitive type
                FeatureStructure val = aFS.getFeatureValue(feat);
                if (val == null) {
                    aOut.println("null");
                } else {
                    getFSValues(val, aTCAS, aNestingLevel + 1, aOut);
                }    
            }      
        }   
    } // getFSValues
    
}
