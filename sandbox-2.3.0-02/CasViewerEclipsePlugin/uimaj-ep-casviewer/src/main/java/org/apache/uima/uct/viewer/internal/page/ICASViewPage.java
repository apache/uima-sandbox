package org.apache.uima.uct.viewer.internal.page;

import java.util.List;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.forms.editor.IFormPage;

public interface ICASViewPage extends IFormPage {

    public void selectTypesByName (List typeNames);
    public void deselectTypesByName (List typeNames);
    public List getSelectedTypeNames();
    
    public void refreshTypeSystemStyle ();
    public void refreshInput();
    
    public void setStatusLineManager (IStatusLineManager statusLine);
    
    public void setPreference (int attribute, boolean value);
}
