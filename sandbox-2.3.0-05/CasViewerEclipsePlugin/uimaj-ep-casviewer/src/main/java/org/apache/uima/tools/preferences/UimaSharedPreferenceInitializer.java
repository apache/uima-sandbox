package org.apache.uima.tools.preferences;

import org.apache.uima.casviewer.viewer.internal.CasViewerGenericPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;


/**
 * Class used to initialize default preference values.
 */
public class UimaSharedPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() 
    {
		IPreferenceStore store = CasViewerGenericPlugin.getDefault()
				.getPreferenceStore();
//      store.setDefault(UimaSharedPreferencePage.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_PRE_SELECT_ALL_ANNOTATIONS, false);
        store.setDefault(PreferenceConstants.P_HIDE_FEATURES_IN_TYPE_SYSTEM, true);
        store.setDefault(PreferenceConstants.P_HIDE_FEATURE_NO_VALUE, true);
        store.setDefault(PreferenceConstants.P_HIDE_PREFERENCE_PAGE, true);
        store.setDefault(PreferenceConstants.P_STYLE_FILE_EXTENSION, ".style.xml");
        store.setDefault(PreferenceConstants.P_FLAT_LAYOUT_FOR_TYPES, true);
        store.setDefault(PreferenceConstants.P_FLAT_LAYOUT_FOR_FS, true);
        
        PreferenceConverter.setDefault(store, PreferenceConstants.P_DEFAULT_FOREGROUND, 
                new RGB(0,0,0));
        PreferenceConverter.setDefault(store, PreferenceConstants.P_DEFAULT_BACKGROUND, 
                new RGB(255,255,255));
	}

}
