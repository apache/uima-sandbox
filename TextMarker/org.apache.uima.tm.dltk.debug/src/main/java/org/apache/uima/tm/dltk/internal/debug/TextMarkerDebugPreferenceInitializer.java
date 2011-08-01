package org.apache.uima.tm.dltk.internal.debug;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.debug.core.DLTKDebugPreferenceConstants;

public class TextMarkerDebugPreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    Preferences store = TextMarkerDebugPlugin.getDefault().getPluginPreferences();

    store.setDefault(TextMarkerDebugConstants.DEBUGGING_ENGINE_ID_KEY, "");

    store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_BREAK_ON_FIRST_LINE, false);
    store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_ENABLE_LOGGING, false);

    store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_GLOBAL, true);
    store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_CLASS, true);
    store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_LOCAL, true);
  }
}
