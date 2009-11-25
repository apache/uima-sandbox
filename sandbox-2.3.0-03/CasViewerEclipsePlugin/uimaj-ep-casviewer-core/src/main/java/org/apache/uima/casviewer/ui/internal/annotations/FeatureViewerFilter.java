package org.apache.uima.casviewer.ui.internal.annotations;

import org.apache.uima.casviewer.core.internal.UFeature;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FeatureViewerFilter extends ViewerFilter {

    public FeatureViewerFilter() {
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof UFeature) {
            if ( ((UFeature) element).getShortName().equals("sofa") ) {
                return false;
            }            
        }
        return true;
    }

}
