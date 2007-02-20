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

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The UimaSourceFolder contains folders, each of these folders can contain uima consumer or
 * annoatator configurations.
 */
public class UimaSourceFolder extends AbstractNlpElement implements IAdaptable {
  private NlpProject mProject;

  private IFolder mUimaSourceFolder;

  private Collection<UimaConfigurationElement> mConfigurationElement = new LinkedList<UimaConfigurationElement>();

  /**
   * Initializes a new instance.
   * 
   * @param configSourceFolder
   * @param project
   */
  UimaSourceFolder(IFolder configSourceFolder, NlpProject project) {
    mUimaSourceFolder = configSourceFolder;
    mProject = project;

  }

  void initialize() throws CoreException {
    createUimaConfigurationElements();
  }

  /**
   * Retrive the config elements.
   * 
   * @return the config elements
   */
  public Collection<UimaConfigurationElement> getUimaConfigurationElements() {
    return mConfigurationElement;
  }

  private void createUimaConfigurationElements() throws CoreException {
    IFolder configSourceFolder = mProject.getDotCorpus().getUimaConfigFolder();

    IResource configFolderCandidates[] = configSourceFolder.members();

    for (IResource resource : configFolderCandidates) {
      if ((resource instanceof IFolder)) {
        // filter everything else out than folders
        IFolder configFolder = (IFolder) resource;

        mConfigurationElement.add(new UimaConfigurationElement(configFolder, this, mProject));
      }
    }
  }

  /**
   * Retrives the parent (NLPProject) of the current instance.
   */
  public INlpElement getParent() {
    return mProject;
  }

  /**
   * Retrives the parent element if element is containded by this instance.
   */
  @Override
  public INlpElement getParent(IResource resource) throws CoreException {
    INlpElement result = super.getParent(resource);

    for (UimaConfigurationElement config : getUimaConfigurationElements()) {
      INlpElement element = config.getParent(resource);

      if (element != null) {
        result = element;
        break;
      }
    }

    return result;
  }

  /**
   * Retrives the resource.
   */
  public IResource getResource() {
    return mUimaSourceFolder;
  }

  /**
   * Retrives the nlp project.
   */
  public NlpProject getNlpProject() {
    return mProject;
  }

  /**
   * Retrives the name.
   */
  public String getName() {
    return mUimaSourceFolder.getName();
  }

  /**
   * Finds a resources containted by this element.
   */
  public INlpElement findMember(IResource resource) {
    INlpElement result = super.findMember(resource);

    if (result == null) {
      for (UimaConfigurationElement configElement : mConfigurationElement) {
        result = configElement.findMember(resource);

        if (result != null) {
          break;
        }
      }
    }

    return result;
  }

  void addResource(IResource resource) throws CoreException {
    if ((resource instanceof IFolder)) {
      IFolder configFolder = (IFolder) resource;

      mConfigurationElement.add(new UimaConfigurationElement(configFolder, this, mProject));
    }
  }

  void removeResource(IResource resource) {
    for (UimaConfigurationElement configElement : mConfigurationElement) {
      if (configElement.getResource().equals(resource)) {
        mConfigurationElement.remove(configElement);
        break;
      }
    }
  }
}