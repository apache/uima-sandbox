package org.apache.uima.casviewer.core.internal;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.tools.internal.util.CasHelper;

public class UFSIndex {

    private FSIndex         uimaFSIndex;
    private String          label;          // Label of FSIndex
    private int             indexLevel = 1; // 1: Top index; >1: sub-type's index

    // Attributes used by Cas Viewer
    protected int               selfFSCount = 0;    // number of FS for this type (NOT including number from sub-types)
    
    private CasIndexRepository  indexRepository;
    
    // List of FeatureStructures (Lazy loading)
    // private List                fsList  = null; 
    
    // Array of UFeatureStructures (Lazy loading)
    private UFeatureStructure[]     uFeatureStructures = null;   
    
    /*************************************************************************/
    
    private UFSIndex() {
        super();
    }

    public UFSIndex(CasIndexRepository indexRepository, String label, FSIndex fsIndex) {
        this(indexRepository, label, fsIndex, 1);
    }

    public UFSIndex(CasIndexRepository indexRepository, String label, FSIndex uimaFSIndex, int level) {
        super();
        this.indexRepository = indexRepository;
        this.label = label;
        this.uimaFSIndex = uimaFSIndex;
        this.indexLevel = level;
        this.selfFSCount = CasHelper.getTotalFSForType(uimaFSIndex, uimaFSIndex.getType());
    }
    
    public UFeatureStructure[] getUFeatureStructures ()
    {
        // Lazy loading
        if (uFeatureStructures == null) {
            int index = 0;
            uFeatureStructures = new UFeatureStructure[uimaFSIndex.size()];
            FSIterator iter = iterator();
            while (iter.isValid()) {
                uFeatureStructures[index] = new UFeatureStructure (iter.get(), index);
                index++;
                iter.moveToNext();
            }
            
        }
        return uFeatureStructures;
    }
    
    /*************************************************************************/
    /*                          Delegate Methods                             */
    /*************************************************************************/

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#compare(org.apache.uima.cas.FeatureStructure, org.apache.uima.cas.FeatureStructure)
     */
    public int compare(FeatureStructure arg0, FeatureStructure arg1) {
        return uimaFSIndex.compare(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#contains(org.apache.uima.cas.FeatureStructure)
     */
    public boolean contains(FeatureStructure arg0) {
        return uimaFSIndex.contains(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#find(org.apache.uima.cas.FeatureStructure)
     */
    public FeatureStructure find(FeatureStructure arg0) {
        return uimaFSIndex.find(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#getIndexingStrategy()
     */
    public int getIndexingStrategy() {
        return uimaFSIndex.getIndexingStrategy();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#getType()
     */
    public Type getType() {
        return uimaFSIndex.getType();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#iterator()
     */
    public FSIterator iterator() {
        return uimaFSIndex.iterator();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#iterator(org.apache.uima.cas.FeatureStructure)
     */
    public FSIterator iterator(FeatureStructure arg0) {
        return uimaFSIndex.iterator(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FSIndex#size()
     */
    public int size() {
        return uimaFSIndex.size();
    }
    
    /*************************************************************************/

    /**
     * @return Returns the uimaFSIndex.
     */
    public FSIndex getUimaFSIndex() {
        return uimaFSIndex;
    }

    /**
     * @param uimaFSIndex The uimaFSIndex to set.
     */
    public void setUimaFSIndex(FSIndex uimaFSIndex) {
        this.uimaFSIndex = uimaFSIndex;
    }


    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }


    /**
     * @param label The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the indexLevel.
     */
    public int getIndexLevel() {
        return indexLevel;
    }

    /**
     * @param indexLevel The indexLevel to set.
     */
    public void setIndexLevel(int indexLevel) {
        this.indexLevel = indexLevel;
    }
    
    public CAS getCas () {
        return indexRepository.getCas();
    }

    /**
     * Get list of FeatureStructure objects
     * 
     * @return Returns the fsList.
     */
//    public List getFSList() {
//        if (fsList == null) {
//            // Lazy loading
//            FSIterator iter = iterator();
//            fsList = new ArrayList ();
//            while (iter.isValid()) {
//                fsList.add (iter.get());
//                iter.moveToNext();
//            }
//        }
//        return fsList;
//    }

    /**
     * @param fsList The fsList to set.
     */
//    public void setFSList(List fsList) {
//        this.fsList = fsList;
//    }

    /**
     * @return Returns the indexRepository.
     */
    public CasIndexRepository getIndexRepository() {
        return indexRepository;
    }

    /**
     * @param indexRepository The indexRepository to set.
     */
    public void setIndexRepository(CasIndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    /*************************************************************************/
    
    public static void getFSValues (FeatureStructure aFS, CAS aTCAS, int aNestingLevel, 
            PrintStream aOut)
    {
        UFeatureStructure uFS = new UFeatureStructure(aFS);
        
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

    /**
     * @return the selfFSCount
     */
    public int getSelfFSCount() {
        return selfFSCount;
    }

    /**
     * @param selfFSCount the selfFSCount to set
     */
    public void setSelfFSCount(int selfFSCount) {
        this.selfFSCount = selfFSCount;
    }
    
}
