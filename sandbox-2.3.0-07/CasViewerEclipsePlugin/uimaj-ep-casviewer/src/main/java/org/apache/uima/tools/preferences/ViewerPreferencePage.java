package org.apache.uima.tools.preferences;

import org.apache.uima.casviewer.viewer.internal.CasViewerGenericPlugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class ViewerPreferencePage  extends FieldEditorPreferencePage
	                               implements IWorkbenchPreferencePage 
{
    public static final String P_PATH = "pathPreference";
    public static final String P_STRING = "stringPreference";

	public ViewerPreferencePage() {
		super(GRID);
		setPreferenceStore(CasViewerGenericPlugin.getDefault().getPreferenceStore());
		setDescription("Specify the preferences used as default by the CAS Viewer.");
        // initializeDefaults();
    }
    
    /**
     * Sets the default values of the preferences.
     * (Already set in plugin.xml)
     */
//    private void initializeDefaults() {
//        IPreferenceStore store = getPreferenceStore();
//        store.setDefault(ViewerPreferencePage.P_ADVANCED_FEATURES, true);
//        store.setDefault(ViewerPreferencePage.P_CHOICE, "choice2");
//        store.setDefault(ViewerPreferencePage.P_STRING, "Default value");
//    }
	
	public void createFieldEditors() 
    {
        addField(new BooleanFieldEditor(PreferenceConstants.P_PRE_SELECT_ALL_ANNOTATIONS,
                "Pre-select ALL annotations when an XCAS file is opened.", getFieldEditorParent()));

        addField(new BooleanFieldEditor(PreferenceConstants.P_HIDE_FEATURES_IN_TYPE_SYSTEM,
                "Hide features in type system's tree", getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PreferenceConstants.P_HIDE_FEATURE_NO_VALUE,
                "Hide feature having no value (in flat structure)", getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PreferenceConstants.P_HIDE_PREFERENCE_PAGE,
                "Hide preference page", getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PreferenceConstants.P_FLAT_LAYOUT_FOR_TYPES,
                "Use flat layout for Types", getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PreferenceConstants.P_FLAT_LAYOUT_FOR_FS,
                "Use flat layout for feature structure", getFieldEditorParent()));
/*        
        addField(new StringFieldEditor(PreferenceConstants.P_STYLE_FILE_EXTENSION, 
                "Style file's extension (e.g., \".style.xml\" without \"):", getFieldEditorParent()));
        
        // Color Settings
        addField(new DefaultColorsFieldEditor(PreferenceConstants.P_DEFAULT_COLORS_FILE, 
                "&Default color file:", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceConstants.P_DEFAULT_FOREGROUND,
                    "Default foreground color", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceConstants.P_DEFAULT_BACKGROUND,
                "Default background color", getFieldEditorParent()));
*/	
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
    /*************************************************************************/
    
    public static RGB getDefaultForegroundColor() {
        IPreferenceStore store= CasViewerGenericPlugin.getDefault().getPreferenceStore();
        return PreferenceConverter.getColor(store, PreferenceConstants.P_DEFAULT_FOREGROUND);
    }
    
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