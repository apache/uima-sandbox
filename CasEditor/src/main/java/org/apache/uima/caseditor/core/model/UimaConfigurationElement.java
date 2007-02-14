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


import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.uima.AnnotatorConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The ConfigurationElement is folder which contains uima descriptors and resources.
 * 
 * TODO: do not include defective elements!
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.5.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public class UimaConfigurationElement extends AbstractNlpElement implements IAdaptable {
  private IFolder mConfigFolder;

  private NlpProject mProject;

  private UimaSourceFolder mSourceFolder;

  private Collection<AnnotatorElement> mAnnotators;

  private Collection<ConsumerElement> mConsumers;

  /**
   * Initializes a new instance.
   * 
   * @param configFolder
   * @param sourceFolder
   * @param project
   * @throws CoreException
   */
  UimaConfigurationElement(IFolder configFolder, UimaSourceFolder sourceFolder, NlpProject project)
          throws CoreException {
    mConfigFolder = configFolder;
    mSourceFolder = sourceFolder;
    mProject = project;
    createAnnotatorConfigurations();
    createConsumerConfigurations();
  }

  /**
   * Retrives the {@link AnnotatorConfiguration}.
   * 
   * @return the {@link AnnotatorConfiguration}
   */
  public Collection<AnnotatorElement> getAnnotators() {
    return mAnnotators;
  }

  private void createAnnotatorConfigurations() throws CoreException {
    mAnnotators = new LinkedList<AnnotatorElement>();

    for (IResource resource : mConfigFolder.members()) {
      if (resource instanceof IFile && resource.getName().endsWith(".ann")) {
        IFile consumerFile = (IFile) resource;

        AnnotatorElement annotator = new AnnotatorElement(this, consumerFile);
        mAnnotators.add(annotator);
      }
    }
  }

  /**
   * Retrives the consumers.
   * 
   * @return consumers
   */
  public Collection<ConsumerElement> getConsumers() {
    return mConsumers;
  }

  private void createConsumerConfigurations() throws CoreException {
    mConsumers = new LinkedList<ConsumerElement>();

    for (IResource resource : mConfigFolder.members()) {
      if (resource instanceof IFile && resource.getName().endsWith(".con")) {
        IFile consumerFile = (IFile) resource;

        ConsumerElement consumer = new ConsumerElement(this, consumerFile);
        mConsumers.add(consumer);
      }
    }

  }

  /**
   * Retrives all containted {@link IFile} and {@link IFolder} resources.
   * 
   * @return {@link IFile}s and {@link IFolder}s
   * @throws CoreException
   */
  public Collection<IResource> getNonNlpResources() throws CoreException {
    // just filter all .con and .ann files
    Collection<IResource> resources = new LinkedList<IResource>();

    for (IResource candidate : mConfigFolder.members()) {
      if (candidate.getName().endsWith(".con") || candidate.getName().endsWith(".ann")) {
        continue;
      }

      resources.add(candidate);
    }

    return resources;
  }

  /**
   * Retrives the {@link NlpProject}.
   * 
   * @return the {@link NlpProject}
   */
  public NlpProject getNlpProject() {
    return mProject;
  }

  /**
   * Searches for members of the given resource.
   */
  @Override
  public INlpElement findMember(IResource resource) {
    if (getResource().equals(resource)) {
      return this;
    }

    Collection<ConsumerElement> consumers = getConsumers();

    for (ConsumerElement consumer : consumers) {
      boolean isElementFound = consumer.findMember(resource) != null;

      if (isElementFound) {
        return consumer.findMember(resource);
      }
    }

    Collection<AnnotatorElement> annotators = getAnnotators();

    for (AnnotatorElement annotator : annotators) {
      boolean isElementFound = annotator.findMember(resource) != null;

      if (isElementFound) {
        return annotator.findMember(resource);
      }
    }

    return null;
  }

  /**
   * Retrives the parent.
   * 
   * @return the parent
   */
  public INlpElement getParent() {
    return mSourceFolder;
  }

  /**
   * Retrives the resource.
   */
  public IResource getResource() {
    return mConfigFolder;
  }

  /**
   * Retrives the parent of the given resource.
   */
  @Override
  public INlpElement getParent(IResource resource) throws CoreException {
    INlpElement result = super.getParent(resource);

    for (IResource member : mConfigFolder.members()) {
      if (member.equals(resource)) {
        result = this;
      }
    }

    return result;
  }

  /**
   * Retrvies the name.
   */
  public String getName() {
    return mConfigFolder.getName();
  }

  /**
   * Not implemented.
   */
  @Override
  void addResource(IResource resource) throws CoreException {
    if (resource instanceof IFile) {
      // if .con file notify it
      if (resource.getName().endsWith(".con")) {
        ConsumerElement consumerElement = new ConsumerElement(this, (IFile) resource);
        mConsumers.add(consumerElement);
      } else if (resource.getName().endsWith(".ann")) {
        AnnotatorElement annotator = new AnnotatorElement(this, (IFile) resource);

        mAnnotators.add(annotator);
      } else {
        // do nothing
      }
    }
  }

  @Override
  void changedResource(IResource resource, INlpElementDelta delta) throws CoreException {
    if (resource instanceof IFile) {
      // if .con file notify it
      if (resource.getName().endsWith(".con")) {
        for (ConsumerElement consumer : mConsumers) {
          if (consumer.getResource().equals(resource)) {
            consumer.changedResource(resource, delta);
            break;
          }
        }
      } else if (resource.getName().endsWith(".ann")) {
        for (AnnotatorElement annotator : mAnnotators) {
          if (annotator.getResource().equals(resource)) {
            annotator.changedResource(resource, delta);
            break;
          }
        }
      } else {
        // do nothing
      }
    }
  }

  /**
   * Not implemented.
   */
  @Override
  void removeResource(IResource resource) {
    if (resource instanceof IFile) {
      // if .con file notify it
      if (resource.getName().endsWith(".con")) {
        for (ConsumerElement consumer : mConsumers) {
          if (consumer.getResource().equals(resource)) {
            mConsumers.remove(consumer);
            break;
          }
        }
      } else if (resource.getName().endsWith(".ann")) {
        for (AnnotatorElement annotator : mAnnotators) {
          if (annotator.getResource().equals(resource)) {
            mAnnotators.remove(annotator);
            break;
          }
        }
      } else {
        // do nothing
      }
    }
  }
}