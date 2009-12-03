package org.apache.uima.casviewer.viewer.internal;


import org.apache.uima.uct.viewer.internal.page.CASViewPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class EditorActionBarContributor extends MultiPageEditorActionBarContributor {

    private GenericCasViewer editor;
    
	/**
	 * Creates a multi-page contributor.
	 */
	public EditorActionBarContributor() {
		super();
        //Trace.trace();
	}

	public void setActiveEditor(IEditorPart part) {
      if (part == null) {
        // Trace.err("Active editor: NULL");  
        return;
      }
      // Trace.err("Active editor:" + part.getClass().getName());
      editor = (GenericCasViewer) part;
      
//      IActionBars actionBars = getActionBars();
//      if (actionBars != null) {
//        actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), getAction(editor,
//                ITextEditorActionConstants.FIND));
//        actionBars.updateActionBars();
//      }
	}

	public void setActivePage(IEditorPart part) {
      
//      if (part != null) {
//        Trace.err("Active page:" + part.getClass().getName());
//      } else {
//        Trace.err("Active page: NULL");        
//      }
      if (editor == null) {
        return;
      }
//      IFormPage newPage = editor.getActivePageInstance();
//      Trace.err("newPage:" + newPage.getClass().getName());
      
      IActionBars actionBars = getActionBars();
      if (actionBars != null) {
          IAction action = getAction(editor, ITextEditorActionConstants.FIND);
          if (action != null) {
              actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), action);
              actionBars.updateActionBars();
          }
      }
	}
    
      /**
       * Returns the action registered with the given text editor.
       * 
       * @return IAction or null if editor is null.
       */
      protected IAction getAction(GenericCasViewer editor, String actionID) {
        IFormPage page = editor.getActivePageInstance();
        if (page != null && page instanceof CASViewPage) {
            return ((CASViewPage) page).getGlobalAction(actionID);
        }
        return null;
      }

    	
}
