package org.apache.uima.casviewer.ui.internal.document;

import org.eclipse.jface.text.source.Annotation;

public class VisualDecoration extends Annotation implements IVisualDecoration {
    
    protected int casDiffStatus = 0;    // AnnotationObject.DIFF_STATUS_ADDED, ...
    
    protected String uimaTypeName;      // Type name from UIMA

    public VisualDecoration() {
    }

    public VisualDecoration(boolean isPersistent) {
        super(isPersistent);
    }

    public VisualDecoration(String type, boolean isPersistent, String text,
            String uimaTypeName) {
        super(type, isPersistent, text);
        this.uimaTypeName = uimaTypeName;
    }

    /**
     * @return the uimaTypeName
     */
    public String getUimaTypeName() {
        return uimaTypeName;
    }

    /**
     * @param uimaTypeName the uimaTypeName to set
     */
    public void setUimaTypeName(String uimaTypeName) {
        this.uimaTypeName = uimaTypeName;
    }


}
