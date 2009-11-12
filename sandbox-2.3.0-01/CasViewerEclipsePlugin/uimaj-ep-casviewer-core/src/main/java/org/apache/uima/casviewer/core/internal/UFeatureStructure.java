package org.apache.uima.casviewer.core.internal;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;

public class UFeatureStructure {
    
    private FeatureStructure    uimaFeatureStructure;
    
    // index in FSIterator (-1: this FS is NOT created from FSIterator)
    // Used by "FSIndexLabelProvider" to show FS[index] in tree viewer
    private int                 indexInIterator = 0;    

    // Can be used for any purpose.
    // Currently, it is used to refer to FSNode
    // Used by "FSIndexSectionPart.setInput" to refer to "AnnotationObject"
    private Object              userData    = null;
    
    public UFeatureStructure() {
        super();
    }

    public UFeatureStructure(FeatureStructure uimaFeatureStructure) {
        this(uimaFeatureStructure, 0);
    }

    public UFeatureStructure(FeatureStructure uimaFeatureStructure, int index) {
        super();
        this.uimaFeatureStructure = uimaFeatureStructure;
        this.indexInIterator = index;
    }
    
    /*************************************************************************/
    /*                          Delegate Methods                             */
    /*************************************************************************/

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#clone()
     */
//    public Object clone() throws CASRuntimeException {
//        return uimaFeatureStructure.clone();
//    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#equals(java.lang.Object)
     */
    // Will create UIMA bug in FeatureStructure.equals
//    public boolean equals(Object arg0) throws ClassCastException {
//        return uimaFeatureStructure.equals(arg0);
//    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getCAS()
     */
    public CAS getCAS() {
        return uimaFeatureStructure.getCAS();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getFeatureValue(org.apache.uima.cas.Feature)
     */
    public FeatureStructure getFeatureValue(Feature arg0) throws CASRuntimeException {
        return uimaFeatureStructure.getFeatureValue(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getFeatureValueAsString(org.apache.uima.cas.Feature)
     */
    public String getFeatureValueAsString(Feature arg0) throws CASRuntimeException {
        return uimaFeatureStructure.getFeatureValueAsString(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getFloatValue(org.apache.uima.cas.Feature)
     */
    public float getFloatValue(Feature arg0) throws CASRuntimeException {
        return uimaFeatureStructure.getFloatValue(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getIntValue(org.apache.uima.cas.Feature)
     */
    public int getIntValue(Feature arg0) throws CASRuntimeException {
        return uimaFeatureStructure.getIntValue(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getStringValue(org.apache.uima.cas.Feature)
     */
    public String getStringValue(Feature arg0) throws CASRuntimeException {
        return uimaFeatureStructure.getStringValue(arg0);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#getType()
     */
    public Type getType() {
        return uimaFeatureStructure.getType();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#hashCode()
     */
    public int hashCode() {
        return uimaFeatureStructure.hashCode();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#setFeatureValue(org.apache.uima.cas.Feature, org.apache.uima.cas.FeatureStructure)
     */
    public void setFeatureValue(Feature arg0, FeatureStructure arg1) throws CASRuntimeException {
        uimaFeatureStructure.setFeatureValue(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#setFeatureValueFromString(org.apache.uima.cas.Feature, java.lang.String)
     */
    public void setFeatureValueFromString(Feature arg0, String arg1) throws CASRuntimeException {
        uimaFeatureStructure.setFeatureValueFromString(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#setFloatValue(org.apache.uima.cas.Feature, float)
     */
    public void setFloatValue(Feature arg0, float arg1) throws CASRuntimeException {
        uimaFeatureStructure.setFloatValue(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#setIntValue(org.apache.uima.cas.Feature, int)
     */
    public void setIntValue(Feature arg0, int arg1) throws CASRuntimeException {
        uimaFeatureStructure.setIntValue(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.FeatureStructure#setStringValue(org.apache.uima.cas.Feature, java.lang.String)
     */
    public void setStringValue(Feature arg0, String arg1) throws CASRuntimeException {
        uimaFeatureStructure.setStringValue(arg0, arg1);
    }
    
    /*************************************************************************/

    /**
     * @return Returns the indexInIterator.
     */
    public int getIndexInIterator() {
        return indexInIterator;
    }

    /**
     * @param indexInIterator The indexInIterator to set.
     */
    public void setIndexInIterator(int indexInIterator) {
        this.indexInIterator = indexInIterator;
    }

    /**
     * @return Returns the uimaFeatureStructure.
     */
    public FeatureStructure getUimaFeatureStructure() {
        return uimaFeatureStructure;
    }

    /**
     * @param uimaFeatureStructure The uimaFeatureStructure to set.
     */
    public void setUimaFeatureStructure(FeatureStructure uimaFeatureStructure) {
        this.uimaFeatureStructure = uimaFeatureStructure;
    }

    /**
     * @return Returns the userData.
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * @param userData The userData to set.
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

}
