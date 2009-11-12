package org.apache.uima.casviewer.ui.internal.hover;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.swt.widgets.Shell;

public class CasViewerDefaultInformationControl extends DefaultInformationControl {

    private boolean fDisposed = false;
    
    public CasViewerDefaultInformationControl(Shell parent, int style, IInformationPresenter presenter, String statusFieldText) {
        super(parent, style, presenter, statusFieldText);
    }
    
    /*
     * @see IInformationControl#dispose()
     */
    public void dispose() {
        fDisposed = true;
        super.dispose();
    }
    
    public boolean isDisposed() {
        return fDisposed;
    }

}
