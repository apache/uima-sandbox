package org.apache.uima.uct.viewer.internal.page;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class HeaderPage extends FormPage {

    protected FormEditor    editor;
    protected FormToolkit   toolkit;
    
    public HeaderPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        this.editor = editor;
    }
    
    public HeaderPage(FormEditor formEditor, String pageTitle) {
        this(formEditor, "UID_" + pageTitle, pageTitle);
      }

    public HeaderPage(String id, String title) {
        super(id, title);
        // TODO Auto-generated constructor stub
    }

}
