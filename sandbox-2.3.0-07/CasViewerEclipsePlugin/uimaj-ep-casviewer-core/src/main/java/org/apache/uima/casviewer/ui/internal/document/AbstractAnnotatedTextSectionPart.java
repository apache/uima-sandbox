package org.apache.uima.casviewer.ui.internal.document;

import java.util.List;

import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.ui.internal.util.AbstractSectionPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractAnnotatedTextSectionPart extends AbstractSectionPart {

    final static public int                TAB_VIEW_ANNOTATIONS    = 1;
    final static public int                TAB_VIEW_INDEX_REPO     = 2;    
    final static public int                TAB_VIEW_CASDIFF        = 3;    
    final static public int                TAB_VIEW_CASDELTA       = 4;    

    protected List                  list4fsIndex = null;
    protected List                  list4CasDiffAnnotations = null;
    
    public AbstractAnnotatedTextSectionPart(IManagedForm managedForm,
            Composite parent, Section section, int style, String title,
            String description) {
        super(managedForm, parent, section, style, title, description);
    }

    // Called from CASViewControl.setAnnotationObjectListForView
    public void setAnnotationObjectListForView (int viewIndex, List list)
    {
        if (viewIndex == TAB_VIEW_INDEX_REPO) { 
            list4fsIndex = list;
        } else if (viewIndex == TAB_VIEW_CASDIFF) {
            list4CasDiffAnnotations = list;
        }
    }

}
