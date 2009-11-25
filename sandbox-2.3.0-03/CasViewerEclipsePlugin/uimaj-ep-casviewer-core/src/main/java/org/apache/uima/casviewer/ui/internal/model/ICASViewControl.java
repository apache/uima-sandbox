package org.apache.uima.casviewer.ui.internal.model;

import java.util.List;

import org.apache.uima.casviewer.core.internal.AnnotationObject;
import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;

/**
 * 
 *
 */
public interface ICASViewControl {
    
    static final public int     PREF_HIDE_FEATURES_IN_TYPE_SYSTEM   = 1;
    static final public int     PREF_HIDE_FEATURE_NO_VALUE          = 2;
    static final public int     PREF_STYLE_FILE_EXTENSION           = 3;
    static final public int     PREF_DEFAULT_COLORS_FILE            = 4;
    static final public int     PREF_DEFAULT_FOREGROUND             = 5;
    static final public int     PREF_DEFAULT_BACKGROUND             = 6;
    static final public int     PREF_FLAT_LAYOUT_FOR_TYPES          = 7;
    static final public int     PREF_FLAT_LAYOUT_FOR_FS             = 8;
    public void setPreference (int attribute, boolean value);
    public void setPreference (int attribute, String value);
    
    static final public int     ACTION_SELECT_ALL           = 1;
    static final public int     ACTION_DESELECT_ALL         = 2;
    static final public int     ACTION_SET_FILTERS          = 3;
    
    public void createContents();
    public void setStatusLineManager(IStatusLineManager statusLine);
    public void dispose();
    public IAction getGlobalAction(String id);
    
    public void setTypeSelectionByName(List<String> types, boolean selection);
    public List getSelectedTypeNames();
    public void refreshTypeSystemStyle();
    public void setInput(ICASObjectView icasViewObject, String title);
    
    public void onActionInvocation (Object source, int flags, Object obj);
        
    /**
     * Set the annotation object list for the specified view
     */
    public void setAnnotationObjectListForView (int viewIndex, List<AnnotationObject> list);
    
    public void showAnnotationsForView (int forView);
    
    /**
     * Show the annotations of the specified list of type names 
     * and the specified view
     * 
     * @param typeNames
     * @param forView
     * @return void
     */
    public void showAnnotationsByTypenameForView (List<String> typeNames, int forView);

    public void showMoreAnnotationObjects (int viewIndex, List<AnnotationObject> list);
    public void enableTypes (boolean enable, List<String> typeNames);
}
