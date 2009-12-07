package org.apache.uima.tools.common.internal.images;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


public class UimaToolsImagesPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.ibm.uima.tools.common.basic";

	// The shared instance
	private static UimaToolsImagesPlugin plugin;
	
	public UimaToolsImagesPlugin() {
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
        ImageLoader.getInstance().initializeImageRegistry();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
        ImageLoader.getInstance().dispose();
	}

	public static UimaToolsImagesPlugin getDefault() {
		return plugin;
	}

}
