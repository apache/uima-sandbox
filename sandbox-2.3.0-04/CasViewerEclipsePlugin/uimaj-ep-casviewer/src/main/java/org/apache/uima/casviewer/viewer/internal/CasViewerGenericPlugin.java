package org.apache.uima.casviewer.viewer.internal;

import java.net.URL;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;


/**
 * The main plugin class to be used in the desktop.
 */
public class CasViewerGenericPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.uima.castoolkit.viewer.generic";
    
	//The shared instance.
	private static CasViewerGenericPlugin   plugin;
    private BundleContext                   context = null;
    private static ILog                     logger;
	
	public CasViewerGenericPlugin() {
		plugin = this;
	}
    
    public String getVersion () {
        return (String) context.getBundle().getHeaders().get(Constants.BUNDLE_VERSION);
    }
    

	public void start(BundleContext context) throws Exception {
		super.start(context);
        this.context = context;
        logger = plugin.getLog();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CasViewerGenericPlugin getDefault() {
		return plugin;
	}

    /**
     * Get and cache the given Image 
     * @param relativeURL String describing the image's location, as "icons/myimage.png" 
     * @return Image, possibly <code>null</code> 
     */
    public static Image getImage(final String relativeURL) { 
        final ImageRegistry registry = getDefault().getImageRegistry();
        Image image = registry.get(relativeURL);

        if (null == image) {
            final URL imageURL = getDefault().getBundle().getEntry(relativeURL);
            final ImageDescriptor descriptor = 
                ImageDescriptor.createFromURL(imageURL);
            image = descriptor.createImage();
            registry.put(relativeURL, image);
        }
        return image; 
    } 
    
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
    
    /*************************************************************************/
    /*                              Log Support                              */
    /*************************************************************************/
    
    public static void logInfo(String message) {
        logger.log(new Status(IStatus.INFO, PLUGIN_ID, 0, message, null));
    }
    
    public static void logWarning(String message) {
        logger.log(new Status(IStatus.WARNING, PLUGIN_ID, 0, message, null));
    }
    public static void logError(String message) {
        logger.log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, null));
    }
    
    public static void log(int severity, String message) {
        logger.log(new Status(severity, PLUGIN_ID, 0, message, null));
    }
    
    public static void log(int severity, String message, Throwable error) {
        logger.log(new Status(severity, PLUGIN_ID, 0, message, error));
    }

    /**
     * @return the logger
     */
    public static ILog getLogger() {
        return logger;
    }
    
    
}
