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

package org.apache.uima.caseditor.core.model;

import java.io.File;


import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.uima.AnnotatorConfiguration;
import org.apache.uima.caseditor.core.util.MarkerUtil;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.6.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public class AnnotatorElement extends AbstractNlpElement {
  private UimaConfigurationElement mParent;

  private IFile mAnnotatorResource;

  private AnnotatorConfiguration mAnnotatorConfig;

  AnnotatorElement(UimaConfigurationElement config, IFile annotatorFile) throws CoreException {
    mParent = config;
    mAnnotatorResource = annotatorFile;

    mAnnotatorConfig = createAnnotatorConfiguration();
  }

  public AnnotatorConfiguration getAnnotatorConfiguration() {
    return mAnnotatorConfig;
  }

  private AnnotatorConfiguration createAnnotatorConfiguration() throws CoreException {
    Runnable clearMarkers = new Runnable() {
      public void run() {
        try {
          MarkerUtil.clearMarkers(mAnnotatorResource, MarkerUtil.PROBLEM_MARKER);
        } catch (CoreException e) {
          TaeCorePlugin.log(e);
        }
      }
    };
    ((NlpModel) getNlpProject().getParent()).asyncExcuteQueue(clearMarkers);

    XMLInputSource inAnnotator;
    try {
      inAnnotator = new XMLInputSource(mAnnotatorResource.getContents(), new File(""));
    } catch (final CoreException e2) {
      Runnable createMarker = new Runnable() {
        public void run() {
          try {
            MarkerUtil.clearMarkers(mAnnotatorResource, e2.getMessage());
          } catch (CoreException e) {
            TaeCorePlugin.log(e);
          }
        }
      };
      ((NlpModel) getNlpProject().getParent()).asyncExcuteQueue(createMarker);

      return null;
    }

    ResourceSpecifier specifier;
    try {
      specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(inAnnotator);
    } catch (final InvalidXMLException e) {
      Runnable createMarker = new Runnable() {
        public void run() {
          try {
            MarkerUtil.createMarker(mAnnotatorResource, e.getMessage());
          } catch (CoreException e) {
            TaeCorePlugin.log(e);
          }
        }
      };
      ((NlpModel) getNlpProject().getParent()).asyncExcuteQueue(createMarker);

      return null;
    }

    // TODO: refactor here
    AnnotatorConfiguration annotatorConfiguration = new AnnotatorConfiguration(
            (TaeDescription) specifier);

    annotatorConfiguration.setBaseFolder((IFolder) mParent.getResource());

    return annotatorConfiguration;
  }

  // private void createMarker()

  @Override
  void addResource(IResource resource) {
    // just do nothing, no childs
  }

  @Override
  void changedResource(IResource resource, INlpElementDelta delta) throws CoreException {
    if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
      mAnnotatorConfig = createAnnotatorConfiguration();
    }
  }

  @Override
  void removeResource(IResource resource) {
    // just do nothing, no childs
  }

  public String getName() {
    return getResource().getName();
  }

  public NlpProject getNlpProject() {
    return getParent().getNlpProject();
  }

  public INlpElement getParent() {
    return mParent;
  }

  public IResource getResource() {
    return mAnnotatorResource;
  }
}