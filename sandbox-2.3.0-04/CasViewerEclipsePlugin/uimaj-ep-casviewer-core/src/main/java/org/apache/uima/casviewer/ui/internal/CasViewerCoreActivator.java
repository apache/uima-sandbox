package org.apache.uima.casviewer.ui.internal;

import org.apache.uima.tools.common.internal.images.ImageLoader;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CasViewerCoreActivator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.uima.casviewer.core";

    // The shared instance
    private static CasViewerCoreActivator plugin;
    
    /**
     * The constructor
     */
    public CasViewerCoreActivator() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        ImageLoader.getInstance().initializeImageRegistry();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        ImageLoader.getInstance().dispose();
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static CasViewerCoreActivator getDefault() {
        return plugin;
    }

}
