package org.apache.uima.casviewer.ui.internal.annotations;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.DebugFSLogicalStructure;
import org.apache.uima.casviewer.core.internal.CasIndexRepository;
import org.apache.uima.casviewer.core.internal.IItemTypeConstants;
import org.apache.uima.casviewer.core.internal.UFeature;
import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.apache.uima.casviewer.core.internal.tree.TreeNode;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.tools.debug.util.Trace;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 *
 */
public class FSContentProvider implements ITreeContentProvider {

    private Object[]    EMPTY_ARRAY = new Object[0];
    
    public FSContentProvider() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren (Object parent) 
    {
        if (parent instanceof TreeNode) {
            int kind = ((TreeNode)parent).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS) {
                FeatureStructure fs = (FeatureStructure) ((TreeNode)parent).getObject();
                List<Feature> aFeatures = fs.getType().getAppropriateFeatures();
                if (aFeatures.size() > 0) {
                    Object[] objs = new Object[aFeatures.size()];
                    for (int i=0; i<aFeatures.size(); ++i) {
                        objs[i] = new UFeature(fs, (Feature) aFeatures.get(i));
                    }
                    return objs;
                }            
            }
            
        } else if (parent instanceof UFeatureStructure
                || parent instanceof FeatureStructure) {
            FeatureStructure fs;
            if (parent instanceof UFeatureStructure) {
                fs = ((UFeatureStructure)parent).getUimaFeatureStructure();
            } else {
                fs = (FeatureStructure) parent;
            }
            
            List<Feature> aFeatures = fs.getType().getAppropriateFeatures();
            if (aFeatures.size() > 0) {
                // Object[] objs = new Object[aFeatures.size()];
                List<UFeature> list = new ArrayList<UFeature>();
                for (int i=0; i<aFeatures.size(); ++i) {                   
                    // objs[i] = new UFeature(fs, (Feature) aFeatures.get(i));
//                    if ( !aFeatures.get(i).getShortName().equals("sofa") ) {
                        list.add(new UFeature(fs, (Feature) aFeatures.get(i)));
//                    }
                }
                return list.toArray();
            }            
            
        } else if (parent instanceof UFeature) {
            return getChildren ((UFeature)parent);

        } else if (parent instanceof List) {
            if ( ((List) parent).size() > 0 ) {
                return ((List) parent).toArray();
            }
        }
        return null;
    } // getChildren

    /*            
    FSList links = otherEntity.getLinks();
    if (links != null)
        while (links instanceof NonEmptyFSList) {
            NonEmptyFSList nLinks = (NonEmptyFSList) links;
            Link link = (Link) nLinks.getHead();
            if ((link.getFrom().equals(otherEntity))
                    && (link instanceof Argument)) {
                // Special case -- argument links from
                // otherEntity are already in entity
                Link.removeLink(link.getTo(), link);
            } else {
                if (link.getFrom().equals(otherEntity))
                    link.setFrom(entity);
                else if (link.getTo().equals(otherEntity))
                    link.setTo(entity);
                Link.addLink(entity, link);
            }
            links = nLinks.getTail();
        }

*/            
    
    public Object[] getChildren (UFeature uFeature)
    {
        // Non-primitive feature ONLY
        if ( uFeature.isPrimitive() ) {
            // Primitive feature
            Trace.bug("SHOULD NOT BE primitive feature");
            return EMPTY_ARRAY;
        }
        FeatureStructure fs = uFeature.getFeatureValue();
        if (fs == null) {
            return EMPTY_ARRAY;
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
        if (fs instanceof FSArray) {
            if ( ((FSArray) fs).size() == 0 ) {
                // Trace.trace(fs.getType().getShortName() + " is ArrayFS with length = " + ((ArrayFS) fs).size());
                return EMPTY_ARRAY;
            }
            return ((FSArray) fs).toArray();
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
        
        
        List<Feature> aFeatures = fs.getType().getAppropriateFeatures();
        if (aFeatures.size() > 0) {
            Object[] objs = new Object[aFeatures.size()];
            for (int i=0; i<aFeatures.size(); ++i) {
                objs[i] = new UFeature(fs, (Feature) aFeatures.get(i));
            }
            return objs;
        }            
        return EMPTY_ARRAY;
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
        if (element instanceof TreeNode) {
            if ( ((TreeNode)element).hasChildren() ) {
                return true;
            }
            // Check item type
            int kind = ((TreeNode)element).getObjectType();
            if (kind == IItemTypeConstants.ITEM_TYPE_LABEL_U_FS) {
                return true;
            }
        } else if (element instanceof FeatureStructure
                || element instanceof UFeatureStructure) {
            return true;
        } else if (element instanceof UFeature) {
            if ( ! ((UFeature)element).isPrimitive() ) {
                // Non-primitive feature
                if ( ((UFeature)element).getFeatureValue() != null ) {
                    return getChildren((UFeature)element).length > 0;
                }
            }
        } else if (element instanceof List) {
            return ((List) element).size() > 0;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) 
    {
        if ( inputElement instanceof FeatureStructure ) {
            // Add label for FSIndex
            TreeNode fNode = new TreeNode(((FeatureStructure) inputElement).getType().getName(), null);
            fNode.setObjectType(IItemTypeConstants.ITEM_TYPE_LABEL_U_FS);
            fNode.setObject(inputElement);
            Object[] objs = new Object[1];
            objs[0] = fNode;
            return objs;
        } else if (inputElement instanceof List){   
            // List of UFeatureStructure
            if ( ((List) inputElement).size() > 0 ) {
                return ((List) inputElement).toArray();
            }
        }
        Trace.trace("Unknown inputElement: " + inputElement.getClass().getName());
        return new Object[0];
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /**
     * newInput is a List of UFeatureStructure
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//        if (newInput != null) {
//            if ( !(newInput instanceof UFSIndex) ) {
//                Trace.bug("Unknown input: " + newInput.getClass().getName());
//                return;
//            }
//            uFSIndex = (UFSIndex) newInput;
//        }
    }

    /*************************************************************************/
    
    public static void getFSValues (FeatureStructure aFS, CAS aTCAS, int aNestingLevel, 
            PrintStream aOut)
    {
        Type stringType = CasIndexRepository.stringType;
        
        // printTabs(aNestingLevel, aOut);
        aOut.println(aFS.getType().getName());
        
        //print all features
        List<Feature> aFeatures = aFS.getType().getAppropriateFeatures();
        Iterator<Feature> iter = aFeatures.iterator();
        while (iter.hasNext())
        {
            Feature feat = (Feature)iter.next();
            // printTabs(aNestingLevel + 1, aOut);
            //print feature name
            aOut.print(feat.getShortName());
            aOut.print(" = ");
            //print feature value (how we get this depends on feature's range type)
            String rangeTypeName = feat.getRange().getName();
            if (aTCAS.getTypeSystem().subsumes(stringType, feat.getRange())) {
                // must check for sub-types of string
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
