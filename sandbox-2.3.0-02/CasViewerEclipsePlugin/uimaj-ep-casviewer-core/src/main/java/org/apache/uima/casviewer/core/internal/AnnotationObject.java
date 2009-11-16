package org.apache.uima.casviewer.core.internal;

import java.util.List;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.casviewer.core.internal.style.BaseTypeStyle;


public class AnnotationObject // implements ITreeNodeComparator 
{
    protected static Object[] EMPTY_ARRAY = new Object[0];

    public Type      type;
    public int       begin;
    public int       end;
    public boolean   show = false;   

    protected FeatureStructure      featureStructure;
    
    // Used by CASDiff
    public static final int         DIFF_STATUS_NONE    = 0x00;
    public static final int         DIFF_STATUS_ADDED   = 0x01;
    public static final int         DIFF_STATUS_DELETED = 0x02;
    public static final int         DIFF_STATUS_MATCH   = 0x04;
    
    protected int                   diffStatus;         // DIFF_STATUS_xxx
    protected String                textSpan;

    protected boolean annotationFS = true;
    
    /*************************************************************************/
    public AnnotationObject(FeatureStructure fs) {
        this.type   = fs.getType();
        featureStructure = fs;
        if (fs instanceof AnnotationFS) {
            this.textSpan = ((AnnotationFS) fs).getCoveredText();
            this.begin = ((AnnotationFS) fs).getBegin();
            this.end = ((AnnotationFS) fs).getEnd();
        } else {
            annotationFS = false;
        }
    }
    
    protected AnnotationObject(AnnotationFS f, String text) {
        this((FeatureStructure)f, text);
    }

    public AnnotationObject(FeatureStructure f, String text) {
        this.type   = f.getType();
        featureStructure = f;
        textSpan = text;
    }

    public AnnotationObject (FeatureStructure f, int begin, int end) {
        this.type   = f.getType();
        this.begin  = begin;
        this.end    = end;
        this.featureStructure = f;
    }
    
    public AnnotationObject (FeatureStructure f, int begin, int end, boolean show) {
        this.type   = f.getType();
        this.featureStructure = f;
        this.begin  = begin;
        this.end    = end;
        this.show   = show;
    }
    
    public AnnotationObject cloneMe () {
        AnnotationObject c = new AnnotationObject(featureStructure, begin, end, show);
        c.diffStatus = diffStatus;
        c.textSpan   = textSpan;
        return c;
    }

    /*************************************************************************/
    
    public Type getType () {
        return type;
    }
    
    public String getTypeName() {
        return type.getName();
    }

    public String getAnnotationTypeShortName() {
        return type.getShortName();
    }

    /**
     * @return Returns the show.
     */
    public boolean isShow() {
        return show;
    }

    /**
     * @param show The show to set.
     */
    public void setShow(boolean show) {
        this.show = show;
    }

    /*************************************************************************/
    /*                          For CASDiff                                  */ 
    /*************************************************************************/

    public String getText ()
    {
        return textSpan;
    }
    
    public void setTextSpan (String t) {
        textSpan = t;
    }
    public String getTypeShortName () {
        return featureStructure.getType().getShortName();
    }
    
    public FeatureStructure getFeatureStructure() {
        return featureStructure;
    }

    public void setFeatureStructure(FeatureStructure fs) {
        featureStructure = fs;
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.tools.casdiff.ITreeNodeComparator#getChildren()
     */
    public Object[] getFeatures() {
        if (featureStructure == null) {
            return EMPTY_ARRAY;
        }
        List<Feature> aFeatures = featureStructure.getType().getFeatures();
        if (aFeatures.size() > 0) {
            Object[] objs = new Object[aFeatures.size()];
            for (int i=0; i<aFeatures.size(); ++i) {
                objs[i] = new UFeature(featureStructure, aFeatures.get(i));
                // objs[i] = (Feature) aFeatures.get(i);
            }
            return objs;
        }            
        
        return EMPTY_ARRAY;
    }

    /*************************************************************************/
    
    /**
     * Compare FeatureStructure with "other"
     * 
     * @param other
     * @return int  -1 if this.begin < other.begin;
     *               0 equal;
     *               1 if this.begin > other.begin;
     */
    public int compareFS(AnnotationObject other) {
        if ( other == null ) {
            return 1;
        }
        
        FeatureStructure otherF = ((AnnotationObject) other).featureStructure;
        if (featureStructure instanceof AnnotationFS) {
            if ( ((AnnotationFS) featureStructure).getBegin() == ((AnnotationFS) otherF).getBegin() 
                 && ((AnnotationFS) featureStructure).getEnd() == ((AnnotationFS) otherF).getEnd() ) {
                return 0;
            } else {
                if ( ((AnnotationFS) featureStructure).getBegin() < ((AnnotationFS) otherF).getBegin() ) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        
        if ( featureStructure.getType().getName().equals(otherF.getType().getName()) ) {
            return 0;
        } else {               
            return 0;
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.uima.tools.casdiff.ITreeNodeComparator#compareContents(java.lang.Object, java.lang.Object)
     */
    public int compareContents(Object other) {
        if (other != null && getClass() == other.getClass()) {
            int c = textSpan.compareTo(((AnnotationObject)other).getText());
            return c;
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ibm.uima.tools.casdiff.ITreeNodeComparator#getChildren()
     */
    public Object[] getChildren() {
        return EMPTY_ARRAY;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     * Note: getLabel() == null when compare to FS that is NOT an AnnotationFS 
     */
//    public boolean equals(Object other) {
//        if ( other != null && getClass() == other.getClass()
//                && getLabel().equals(((AnnotationObject) other).getLabel()) ) {
//            FeatureStructure otherF = ((AnnotationObject) other).featureStructure;
//            if (featureStructure instanceof AnnotationFS) {
//                if ( ((AnnotationFS) featureStructure).getBegin() == ((AnnotationFS) otherF).getBegin() 
//                     && ((AnnotationFS) featureStructure).getEnd() == ((AnnotationFS) otherF).getEnd() ) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//            
//            if ( featureStructure.getType().getName().equals(otherF.getType().getName()) ) {
//                return true;
//            }
//        }
//        
//        return super.equals(other);
//    }
//
//    /* (non-Javadoc)
//     * @see java.lang.Object#hashCode()
//     */
//    public int hashCode() {
//        int hashCode= 1;
//        return hashCode;
//    }

    /* (non-Javadoc)
     * @see com.ibm.uima.tools.casdiff.ITreeNode#getLabel()
     */
    public String getLabel() {
        return getText ();
    }

    /**
     * @return the diffStatus
     */
    public int getDiffStatus() {
        return diffStatus;
    }

    /**
     * @param diffStatus the diffStatus to set
     */
    public void setDiffStatus(int diffStatus) {
        this.diffStatus = diffStatus;
    }

    /**
     * @return the annotationFS
     */
    public boolean isAnnotationFS() {
        return annotationFS;
    }

    /**
     * @param annotationFS the annotationFS to set
     */
    public void setAnnotationFS(boolean annotationFS) {
        this.annotationFS = annotationFS;
    }
    

}
