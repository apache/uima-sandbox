package org.apache.uima.casviewer.viewer.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 *  Need a better way to show error
 */
public class PopupMessage extends MessageDialog 
{

    
    /**
     * @param parentShell
     * @param dialogTitle
     * @param dialogTitleImage
     * @param dialogMessage
     * @param dialogImageType
     * @param dialogButtonLabels
     * @param defaultIndex
     */
    public PopupMessage(Shell parentShell, String dialogTitle, Image dialogTitleImage,
            String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
      super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType,
              dialogButtonLabels, defaultIndex);
      // TODO Auto-generated constructor stub
    }

    final private static String[] YES_NO = { "Yes", "No" };

    final private static String[] OKstring = { "OK" };

    /**
     * Pops up a warning message with an "OK" and "Cancel" button
     * 
     * @param title
     *          of the warning
     * @param message
     * @param type
     *          one of MessageDialog.NONE for a dialog with no image MessageDialog.ERROR for a dialog
     *          with an error image MessageDialog.INFORMATION for a dialog with an information image
     *          MessageDialog.QUESTION for a dialog with a question image MessageDialog.WARNING for a
     *          dialog with a warning image
     */
    public static int popOkCancel(String title, String message, int type) {
      return popMessage(title, message, type, YES_NO);
    }

    /**
     * Pops up a warning message with an "OK" button
     * 
     * @param title
     *          of the warning
     * @param message
     * @param type
     *          one of MessageDialog.NONE for a dialog with no image MessageDialog.ERROR for a dialog
     *          with an error image MessageDialog.INFORMATION for a dialog with an information image
     *          MessageDialog.QUESTION for a dialog with a question image MessageDialog.WARNING for a
     *          dialog with a warning image
     * @return Window.OK or Window.CANCEL. If window is closed, Window.CANCEL is returned.
     */

    public static void popMessage(String title, String message, int type) {
      popMessage(title, message, type, OKstring);
    }

    public static int popMessage(String title, String message, int type, String[] buttons) {
        PopupMessage dialog = new PopupMessage(new Shell(), title, null, message, type, buttons, 0);
      dialog.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
      int returnCode = dialog.open();
      if (returnCode == -1)
        returnCode = Window.CANCEL; // Cancel code
      return returnCode;
    }
    
    static public void displayError (Shell shell, String msg)
    {
        Status status = new Status(IStatus.ERROR, "status", 0, "Error", null);
        ErrorDialog dlg = new ErrorDialog(shell, "Title", msg, status, IStatus.ERROR);
        dlg.open();
    }

}
