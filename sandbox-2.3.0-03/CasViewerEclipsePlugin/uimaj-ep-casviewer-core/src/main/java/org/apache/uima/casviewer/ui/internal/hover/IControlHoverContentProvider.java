package org.apache.uima.casviewer.ui.internal.hover;

import org.eclipse.swt.widgets.Control;


/**
 * IControlHoverContentProvider
 *
 */
public interface IControlHoverContentProvider {

    /**
     * @param control
     * @return
     */
    public String getHoverContent(Control control);
    
}
