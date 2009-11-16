package org.apache.uima.casviewer.ui.internal.model;

public interface ICASViewerListener {
    
    static final public int         PAGE_EVENT_REFRESH_INPUT            = 1;
    // Notify when a style file is imported
    static final public int         PAGE_EVENT_REFRESH_STYLE            = 2;
    static final public int         PAGE_EVENT_RESTORE_DEFAULT_STYLE    = 3;
    
    // Type's style (color) is update
    static final public int         PAGE_EVENT_UPDATE_TYPE_STYLE        = 10;
    
    public void onActionInvocation (Object source, int flags, Object obj);

}
