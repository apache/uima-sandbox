package org.apache.uima.casviewer.core.internal;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.FeatureImpl;

/**
 * 
 *
 */
public class UFeature {
        
    private FeatureImpl             uimaFeature;
    private FeatureStructure        fs;
    private boolean                 primitive;
    private String                  valueAsString = "";
    

    /*************************************************************************/
    
    /**
     * 
     */
    public UFeature(FeatureStructure fs, Feature uimaFeature) {
        super();
        this.fs = fs;
        this.uimaFeature = (FeatureImpl) uimaFeature;
        Type parent = fs.getCAS().getTypeSystem().getParent(getRange());
//        if (parent == null) {
//            // E.g., TOP
//            Trace.trace(getRange() + " has NO parent");
//        }
        if (parent != null && parent.getName().equals(CAS.TYPE_NAME_STRING) ) {
            primitive = true;
            valueAsString = fs.getStringValue(uimaFeature);
            return;
//        } else {
//            primitive = getRange().isPrimitive();
        }
        primitive = getRange().isPrimitive();
        if (primitive) {
            valueAsString = fs.getFeatureValueAsString(uimaFeature);
        }
    }

    /*************************************************************************/
    /*                          Delegate Methods                             */
    /*************************************************************************/

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#getDomain()
     */
    public Type getDomain() {
        return uimaFeature.getDomain();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#getName()
     */
    public String getName() {
        return uimaFeature.getName();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#getRange()
     */
    public Type getRange() {
        return uimaFeature.getRange();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#getShortName()
     */
    public String getShortName() {
        return uimaFeature.getShortName();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#getTypeSystem()
     */
    public TypeSystem getTypeSystem() {
        return uimaFeature.getTypeSystem();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.cas.impl.FeatureImpl#toString()
     */
    public String toString() {
        return uimaFeature.toString();
    }

    /*************************************************************************/

    /**
     * @return Returns the primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * @return Returns the valueAsString.
     */
    public String getValueAsString() {
        return valueAsString;
    }

    public FeatureStructure getFeatureValue () {
        if ( primitive ) return null;
//        FeatureStructure f = fs.getFeatureValue(uimaFeature);
//        if (f == null) {
//            Trace.bug("No FeatureValue for range:" + getRange().getName());
//        }
        return fs.getFeatureValue(uimaFeature);
    }
    
    /*************************************************************************/
    
 

}
