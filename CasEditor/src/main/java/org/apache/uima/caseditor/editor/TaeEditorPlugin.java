/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.caseditor.editor;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.3.2.2 $, $Date: 2007/01/04 15:00:54 $
 */
public class TaeEditorPlugin extends AbstractUIPlugin {
  private static final String ID = "net.sf.tae.editor";

  private static final String ICONS_PATH = "icons/";

  /**
   * The shared instance.
   */
  private static TaeEditorPlugin sPlugin;

  /**
   * Resource bundle.
   */
  private ResourceBundle mResourceBundle;

  /**
   * The constructor.
   */
  public TaeEditorPlugin() {
    super();

    sPlugin = this;
  }

  /**
   * This method is called upon plug-in activation
   * 
   * @param context
   * @throws Exception
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
  }

  /**
   * This method is called when the plug-in is stopped.
   * 
   * @param context
   * @throws Exception
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);

    sPlugin = null;
    mResourceBundle = null;
  }

  /**
   * Returns the shared instance.
   * 
   * @return the TaePlugin
   */
  public static TaeEditorPlugin getDefault() {
    return sPlugin;
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not found.
   * 
   * @param key
   * @return resource string
   */
  public static String getResourceString(String key) {
    ResourceBundle bundle = TaeCorePlugin.getDefault().getResourceBundle();

    try {
      return (bundle != null) ? bundle.getString(key) : key;
    } catch (MissingResourceException e) {
      return key;
    }
  }

  /**
   * Returns the plugin's resource bundle.
   * 
   * @return the ResourceBbundle or null if missing
   */
  public ResourceBundle getResourceBundle() {
    try {
      if (mResourceBundle == null) {
        mResourceBundle = ResourceBundle.getBundle("Annotator.AnnotatorPluginResources");
      }
    } catch (MissingResourceException x) {
      mResourceBundle = null;
    }

    return mResourceBundle;
  }

  /**
   * Retrives an image.
   * 
   * @param image
   * @return the requested image if not available null
   */
  public static ImageDescriptor getTaeImageDescriptor(Images image) {
    return imageDescriptorFromPlugin(ID, ICONS_PATH + image.getPath());
  }
}