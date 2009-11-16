package org.apache.uima.casviewer.core.internal;

import java.util.List;

/**
 * A node that contains a list of AnnotationObject(s)
 * 
 */
public class AnnotationObjectsNode {

    protected List<AnnotationObject> annotationList;
    
    public AnnotationObjectsNode () {
        
    }

    public AnnotationObjectsNode(List<AnnotationObject> list) {
        annotationList = list;
    }

    /**
     * @return the annotationList
     */
    public List<AnnotationObject> getAnnotationList() {
        return annotationList;
    }

    /**
     * @param annotationList the annotationList to set
     */
    public void setAnnotationList(List<AnnotationObject> list) {
        this.annotationList = list;
    }

}
