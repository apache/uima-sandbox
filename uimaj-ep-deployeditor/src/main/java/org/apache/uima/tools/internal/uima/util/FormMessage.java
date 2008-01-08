package org.apache.uima.tools.internal.uima.util;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.ui.forms.widgets.Form;

public class FormMessage {
  // static final public String DDE_MSG_TOP_DESCRIPTOR_IS_NOT_SET = "topMessageIsNotSet";

  /** ********************************************************************** */

  static public void setMessage(Form form, String message, int msgType) {
    form.setMessage(message, msgType);
  }

  static public void setMessageInfo(Form form, String message) {
    form.setMessage(message, IMessageProvider.INFORMATION);
  }

  static public void setMessageWarning(Form form, String message) {
    form.setMessage(message, IMessageProvider.WARNING);
  }

  static public void setMessageError(Form form, String message) {
    form.setMessage(message, IMessageProvider.ERROR);
  }

}
