package org.apache.uima.casviewer.ui.internal.model;

import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;

public interface ICASViewer {
    
    /**
     * Editor ID of the generic CAS Viewer.
     * 
     * Example of usage:
     *  ICASViewer casViewer = page.openEditor(new FileEditorInput(xcas), 
     *                                 ICASViewer.GENERIC_CASVIEWER_ID);
     *
     */
    static public String GENERIC_CASVIEWER_ID = "com.ibm.uima.casviewer.GenericCasViewer";
  
    /**
     *  Set the input to this CAS Viewer from an xcas/xmi file.
     *  The viewer will open this file and display the deserialized CAS.
     * 
     * @param xcasOrxmiFileName Full path name of xcas/xmi file
     * @param title             Title to be shown in each page (can be null)
     * @return void
     */
    public void setInput(String xcasOrxmiFileName, String title);
    
    /**
     *  Set the input to this CAS Viewer from a CAS object.
     *  The viewer will display the specified CAS object.
     * 
     * @param aCAS      CAS object to be displayed (can be multi-sofa enable CAS)
     * @param title     Title to be shown in each page (can be null)
     * @return void
     */
    public void setInput(CAS aCAS, String title);
    
    /**
     * Refresh current input file
     * 
     * @return void
     */
    public void refreshInput ();
        
    /**
     * Get CAS which is deserialized from xcas/xmi file. 
     * 
     * @return CAS      CAS object which can contains multiple sofa
     */
    public CAS getCAS ();
    
    /**
     * Get currently selected type names from the Type System tree
     * 
     * @return List List of type full names. Return null if no selection
     */
    public List getSelectedTypeNames ();
    
    /**
     * Select types in the Type System tree.
     * The associated annotations of the specified types will be shown in the document.
     * 
     * @param typeNames List of type full names. If typeNames is null, all types will be assumed
     * @return void
     */
    public void selectTypesByName (List typeNames);
    
    
    /**
     * Deselect types in the Type System tree.
     * The associated annotations of the specified types will be removed from the document.
     * 
     * @param typeNames List of type full names. If typeNames is null, all types will be assumed
     * @return void
     */
    public void deselectTypesByName (List typeNames);
    
    // highlight some annotation in the CAS Viewer and make sure it's visible 
    // (i.e. scroll to it)
    // Note: This method might NOT consistent withh CAS Viewer user-interface.
    // THIS API IS NOT YET IMPLEMENTED
    public void selectAndReveal(FeatureStructure featureStructure);
    
    public void setSelectedAnnotation (Object annotation);
    
    public String getImportedStyleFileName ();
    
}
