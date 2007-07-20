package org.apache.uima.caseditor.ui.model;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.Images;
import org.eclipse.jface.resource.ImageDescriptor;

public class TypesystemAdapter extends SingleElementAdapter {
	
    /**
     * Retrives the document element <code>ImageDescriptor</code>.
     */
    @Override
    public ImageDescriptor getImageDescriptor(Object object) {
        return CasEditorPlugin.getTaeImageDescriptor(Images.MODEL_TYPESYSTEM);
    }
}
