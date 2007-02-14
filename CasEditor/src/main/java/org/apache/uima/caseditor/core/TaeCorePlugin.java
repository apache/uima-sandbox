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

package org.apache.uima.caseditor.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


import org.apache.uima.caseditor.core.model.NlpModel;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The main tae core plugin class.
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.5.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public class TaeCorePlugin extends Plugin {
  /**
   * The Tae core plugin id.
   */
  public static final String ID = "net.sf.tae.core";

  private static NlpModel sNLPModel;

  /**
   * The shared instance.
   */
  private static TaeCorePlugin sPlugin;

  /**
   * Resource bundle.
   */
  private ResourceBundle mResourceBundle;

  /**
   * The constructor.
   */
  public TaeCorePlugin() {
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
  public static TaeCorePlugin getDefault() {
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
   * Retrives the nlp model.
   * 
   * @return the nlp model
   */
  public static NlpModel getNlpModel() {
    if (sNLPModel == null) {
      try {
        sNLPModel = new NlpModel();
      } catch (CoreException e) {
        // TODO: This should not happen, return an emtpy Model
        log(e);
      }
    }

    return sNLPModel;
  }

  /**
   * Log the throwable.
   * 
   * @param t
   */
  public static void log(Throwable t) {
    getDefault().getLog().log(new Status(IStatus.ERROR, ID, IStatus.OK, t.getMessage(), t));
  }

  /**
   * Destroy the nlp model, only for testing.
   */
  public static void destroyNlpModelForTesting() {
    sNLPModel.destroyForTesting();

    sNLPModel = null;
  }
}