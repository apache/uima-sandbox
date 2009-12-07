package org.apache.uima.casviewer.ui.internal.annotations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.casviewer.core.internal.UFeatureStructure;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FeatureStructureViewerFilter extends ViewerFilter {
    
    protected final Set<String> hiddenTypeNames = new HashSet<String>(); 

    public FeatureStructureViewerFilter() {
        hiddenTypeNames.add("uima.tcas.DocumentAnnotation");
        hiddenTypeNames.add("com.ibm.uima.examples.SourceDocumentInformation");
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof UFeatureStructure) {
            return !hiddenTypeNames.contains(((UFeatureStructure) element).getType().getName());
        }
        return true;
    }

    public void setHiddenTypeNames (List<String> names) {
        if (names != null) {
            hiddenTypeNames.addAll(names);
        } else {
            hiddenTypeNames.clear();
        }
    }
}
