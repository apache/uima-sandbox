package org.apache.uima.casviewer.viewer.internal;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ReportError {

    /**
     * 
     */
    public ReportError() {
        // TODO Auto-generated constructor stub
    }
    

    /**
     * Getting exception messages down to root
     * 
     */
    static public String getMessagesToRootCause(Throwable e) 
    {
        boolean wantStackTrace = false;
        StringBuffer b = new StringBuffer(200);
        String messagePart = e.getMessage();
        if (null == messagePart) {
            b.append(e.getClass().getName());
            wantStackTrace = true;
        } else {
            b.append(messagePart);
        }
        Throwable cur = e;
        Throwable next;

        while (null != (next = cur.getCause())) {
            String message = next.getMessage();
            wantStackTrace = false; // only do stack trace if last item has no message
            if (null == message) {
                b.append(next.getClass().getName());
                wantStackTrace = true;
            }
            if (null != message && ! message.equals(messagePart)) {
                b.append("\r\nCaused by:").append(message); //$NON-NLS-1$
                messagePart = message;
            }
            cur = next;
        }
        if (wantStackTrace) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(ba);
            cur.printStackTrace(ps);
            ps.flush();
            b.append(ba.toString());
            ps.close(); 
        }     
        return b.toString();
    }
    
    public static void openExceptionError(Throwable e) {
        
        MessageDialog.openError(null, e.getClass().getName(), getMessagesToRootCause(e));
    }

    /*************************************************************************/
    
    /**
     * Convenience method to open a standard error dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the dialog's title, or <code>null</code> if none
     * @param message
     *            the message
     */
    public static void openError(Shell parent, String title, String message) {
        MessageDialog.openError(parent, title, message);
    }
    
    public static void openInformation(Shell parent, String title, String message) {
        MessageDialog.openInformation(parent, title, message);
    }

    /**
     * Write the given message to the log 
     * @param msg String non-<code>null</code>
     */
    public static void logMessage(final String msg) { 
        final IStatus status = status(IStatus.INFO, CasViewerGenericPlugin.PLUGIN_ID, msg, null); 
        CasViewerGenericPlugin.getDefault().getLog().log(status); 
    }

    /*************************************************************************/
    
    protected static IStatus status(int level, String id, final String msg, final Throwable t) { 
        return new Status(level, id, IStatus.OK, msg, t);
    }
    
    protected static int reportError(final IStatus status, final String msg, final String title) { 
        final int[] result = new int[1]; 
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                final IWorkbenchWindow iww = window();
                if (null != iww) {
                    final Shell shell = iww.getShell();
                    if (null != shell) {
                        result[0] = ErrorDialog.openError(shell, title, msg, status); 
                    }
                } 
                CasViewerGenericPlugin.getDefault().getLog().log(status); 
            }
        });
        return result[0]; 
    }
    
    /**
     * Get the active Workbench window
     * @return IWorkbenchWindow
     */
    public static IWorkbenchWindow window() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }
    
}
