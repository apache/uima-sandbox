package org.apache.uima.tools.preferences;

import org.apache.uima.casviewer.viewer.internal.CasViewerGenericPlugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class UimaSharedPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage 
{
    public static final String P_PATH = "pathPreference";
    public static final String P_ADVANCED_FEATURES = "advancedFeaturesPreference";
    public static final String P_CHOICE = "choicePreference";
    public static final String P_STRING = "stringPreference";

	public UimaSharedPreferencePage() {
		super(GRID);
		setPreferenceStore(CasViewerGenericPlugin.getDefault().getPreferenceStore());
		setDescription("General settings for UIMA Tools.");
        // initializeDefaults();
    }
    
    /**
     * Sets the default values of the preferences.
     */
//    private void initializeDefaults() {
//        IPreferenceStore store = getPreferenceStore();
//        store.setDefault(UimaSharedPreferencePage.P_ADVANCED_FEATURES, true);
//        store.setDefault(UimaSharedPreferencePage.P_CHOICE, "choice2");
//        store.setDefault(UimaSharedPreferencePage.P_STRING, "Default value");
//    }
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
	}

    public void createFieldEditors_SAVE() {
        addField(new DirectoryFieldEditor(P_PATH, 
                "&Directory preference:", getFieldEditorParent()));
        addField(
            new BooleanFieldEditor(
                P_ADVANCED_FEATURES,
                "&An example of a boolean preference",
                getFieldEditorParent()));

        addField(new RadioGroupFieldEditor(
                P_CHOICE,
            "An example of a multiple-choice preference",
            1,
            new String[][] { { "&Choice 1", "choice1" }, {
                "C&hoice 2", "choice2" }
        }, getFieldEditorParent()));
        addField(
            new StringFieldEditor(P_STRING, "A &text preference:", getFieldEditorParent()));
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
    /*************************************************************************/
    
    static public String getPreferenceString (String pref, String defaultValue)
    {
        Preferences prefs = CasViewerGenericPlugin.getDefault().getPluginPreferences();
        boolean isDefault = prefs.isDefault(pref);
        if (isDefault) {
            prefs.setDefault(pref, defaultValue);
        }
        return prefs.getString(pref); 
    }
       
    static public boolean getPreferenceBoolean (String pref, boolean defaultValue)
    {
        Preferences prefs = CasViewerGenericPlugin.getDefault().getPluginPreferences();
        boolean isDefault = prefs.isDefault(pref);
        if (isDefault) {
            prefs.setDefault(pref, defaultValue);
        }
        return prefs.getBoolean(pref); 
    }
    
}