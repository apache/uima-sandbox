package org.apache.uima.casviewer.ui.internal.document;

import org.apache.uima.casviewer.core.internal.ICASObjectView;
import org.eclipse.jface.text.source.ISourceViewer;


/**
 *  Provide the information source for:
 *      - TypeSystemProvider to get the current type system
 *
 */
public interface ICASSourceViewer extends ISourceViewer {
    
    public ICASObjectView  getCASView ();
    public void     setCASView (ICASObjectView aCASView);
    
    public Object   getTypeSystem ();
    public void     setTypeSystem (Object typesystem);

}
