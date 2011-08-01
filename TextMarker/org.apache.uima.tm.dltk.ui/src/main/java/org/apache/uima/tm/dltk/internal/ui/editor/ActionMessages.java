package org.apache.uima.tm.dltk.internal.ui.editor;

import org.eclipse.osgi.util.NLS;

public final class ActionMessages extends NLS {

  private static final String BUNDLE_NAME = "org.apache.uima.tm.dltk.internal.ui.editor.ActionMessages";//$NON-NLS-1$

  private ActionMessages() {
    // Do not instantiate
  }

  public static String MemberFilterActionGroup_hide_variables_label;

  public static String MemberFilterActionGroup_hide_variables_tooltip;

  public static String MemberFilterActionGroup_hide_variables_description;

  public static String MemberFilterActionGroup_hide_functions_label;

  public static String MemberFilterActionGroup_hide_functions_tooltip;

  public static String MemberFilterActionGroup_hide_functions_description;

  public static String MemberFilterActionGroup_hide_classes_label;

  public static String MemberFilterActionGroup_hide_classes_tooltip;

  public static String MemberFilterActionGroup_hide_classes_description;

  static {
    NLS.initializeMessages(BUNDLE_NAME, ActionMessages.class);
  }

}
