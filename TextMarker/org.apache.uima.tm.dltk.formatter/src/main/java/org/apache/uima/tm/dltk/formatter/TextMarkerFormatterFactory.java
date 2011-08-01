package org.apache.uima.tm.dltk.formatter;

import java.net.URL;
import java.util.Map;

import org.apache.uima.tm.dltk.formatter.internal.TextMarkerFormatterPlugin;
import org.apache.uima.tm.dltk.formatter.preferences.TextMarkerFormatterModifyDialog;
import org.apache.uima.tm.dltk.internal.ui.TextMarkerUI;
import org.eclipse.dltk.formatter.AbstractScriptFormatterFactory;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialogOwner;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;
import org.eclipse.dltk.ui.preferences.PreferenceKey;


public class TextMarkerFormatterFactory extends AbstractScriptFormatterFactory {

  private static final String[] KEYS = {
      // TextMarkerUI.PLUGIN_ID :
      TextMarkerFormatterConstants.FORMATTER_TAB_CHAR,
      TextMarkerFormatterConstants.FORMATTER_INDENTATION_SIZE,
      TextMarkerFormatterConstants.FORMATTER_TAB_SIZE,
      // TextMarkerFormatterPlugin.PLUGIN_ID :
      TextMarkerFormatterConstants.INDENT_BLOCK, TextMarkerFormatterConstants.INDENT_STRUCTURE,
      TextMarkerFormatterConstants.LINES_BEFORE_LONG_DECLARATIONS,
      TextMarkerFormatterConstants.MAX_LINE_LENGTH };

  public PreferenceKey[] getPreferenceKeys() {
    final PreferenceKey[] result = new PreferenceKey[KEYS.length];
    for (int i = 0; i < KEYS.length; ++i) {
      final String key = KEYS[i];
      final String qualifier;
      if (TextMarkerFormatterConstants.FORMATTER_TAB_CHAR.equals(key)
              || TextMarkerFormatterConstants.FORMATTER_INDENTATION_SIZE.equals(key)
              || TextMarkerFormatterConstants.FORMATTER_TAB_SIZE.equals(key)) {
        qualifier = TextMarkerUI.PLUGIN_ID;
      } else {
        qualifier = TextMarkerFormatterPlugin.PLUGIN_ID;
      }
      result[i] = new PreferenceKey(qualifier, key);
    }
    return result;
  }

  @Override
  public PreferenceKey getProfilesKey() {
    return new PreferenceKey(TextMarkerFormatterPlugin.PLUGIN_ID,
            TextMarkerFormatterConstants.FORMATTER_PROFILES);
  }

  public PreferenceKey getActiveProfileKey() {
    return new PreferenceKey(TextMarkerFormatterPlugin.PLUGIN_ID,
            TextMarkerFormatterConstants.FORMATTER_ACTIVE_PROFILE);
  }

  public IScriptFormatter createFormatter(String lineDelimiter, Map preferences) {
    return new TextMarkerFormatter(lineDelimiter, preferences);
  }

  @Override
  public URL getPreviewContent() {
    return getClass().getResource("formatPreviewScript.tm"); //$NON-NLS-1$
  }

  public IFormatterModifyDialog createDialog(IFormatterModifyDialogOwner dialogOwner) {
    return new TextMarkerFormatterModifyDialog(dialogOwner, this);
  }

}
