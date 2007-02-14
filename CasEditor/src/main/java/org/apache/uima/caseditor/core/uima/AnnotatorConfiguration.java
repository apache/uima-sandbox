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

package org.apache.uima.caseditor.core.uima;

import java.net.MalformedURLException;


import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.analysis_engine.TextAnalysisEngine;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.caseditor.core.TaeError;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;

/**
 * TODO: add java doc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.3.2.2 $, $Date: 2007/01/04 14:56:24 $
 */
public class AnnotatorConfiguration {
  private TaeDescription mDescriptor;

  private IPath mResourceBasePath;

  /**
   * Initializes the instance.
   * 
   * @param descriptor
   */
  public AnnotatorConfiguration(TaeDescription descriptor) {
    mDescriptor = descriptor;
  }

  /**
   * Retrives the annotator name.
   * 
   * @return name of the annotator
   */
  public String getAnnotatorName() {
    AnalysisEngineMetaData analysisEngineMetaData = mDescriptor.getAnalysisEngineMetaData();

    return analysisEngineMetaData.getName();
  }

  /**
   * Only text analysis engines are supported.
   * 
   * @return the text analysis engine
   * @throws ResourceInitializationException
   */
  public TextAnalysisEngine createAnnotator() throws ResourceInitializationException {
    ResourceManager resourceManager = UIMAFramework.newDefaultResourceManager();

    if (mResourceBasePath != null) {
      try {
        // resourceManager.setExtensionClassPath(
        // mResourceBasePath.toOSString(), true);
        resourceManager.setDataPath(mResourceBasePath.toOSString());
      } catch (MalformedURLException e) {
        // this will not happen
        throw new TaeError("Unexpexted exceptioon", e);
      }
    }

    return UIMAFramework.produceTAE(mDescriptor, resourceManager, null);
  }

  /**
   * Sets the base folder
   * 
   * @param baseFolder
   */
  public void setBaseFolder(IFolder baseFolder) {
    mResourceBasePath = baseFolder.getLocation();
  }
}