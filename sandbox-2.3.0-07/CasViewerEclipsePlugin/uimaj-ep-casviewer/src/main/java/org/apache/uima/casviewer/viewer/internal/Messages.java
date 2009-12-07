package org.apache.uima.casviewer.viewer.internal;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 */
public class Messages {
    private static final String BUNDLE_NAME = "org.apache.uima.casviewer.viewer.internal.messages";//$NON-NLS-1$
    static {
        // load message values from bundle file
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
    // SelectTypesWizard #####################################
    public static String SelectTypesWizard_Title;
    public static String SelectTypesWizard_Page_Title;
    public static String SelectTypesWizard_Page_Desc;
    public static String SelectTypesWizard_AvailableTypes;
    public static String SelectTypesWizard_SelectedTypes;
    public static String SelectTypesWizard_Locate_Types;
    public static String SelectTypesWizard_search;
    public static String SelectTypesWizard_count;
    public static String SelectTypesWizard_add;
    public static String SelectTypesWizard_remove;
    public static String SelectTypesWizard_removeAll;
    
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    
    private Messages() {
    }
    
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '?' + key + '?';
        }
    }
    
    public static String getFormattedString (String key, String[] args) { 
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args); 
    }
    
}