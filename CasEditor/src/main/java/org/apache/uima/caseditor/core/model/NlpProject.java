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


import org.apache.uima.cas.CAS;
import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.model.dotcorpus.EditorAnnotationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * TODO: add comment
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.9.2.2 $, $Date: 2007/01/04 14:56:25 $
 */
public final class NlpProject extends AbstractNlpElement implements IProjectNature, INlpElement,
        IAdaptable {
  /**
   * The ID of the <code>NLPProject</code>
   */
  public static final String ID = "net.sf.tae.core.NLPProject";

  private static final String DOT_CORPUS_FILENAME = ".corpus";

  private NlpModel mModel;

  private IProject mProject;

  private DotCorpusElement mDotCorpusElement;

  private Collection<CorpusElement> mCopora = new LinkedList<CorpusElement>();

  private Collection<UimaSourceFolder> mUimaSourceFolder = new LinkedList<UimaSourceFolder>();

  private boolean mIsDotCorpusDirty;

  private EditorAnnotationStatus mEditorAnnotationStatus;

  private TypesystemElement mTypesystem;

  /**
   * Initializes the current nlp project instance.
   * 
   * @param model
   */
  void setNlpModel(NlpModel model) {
    mModel = model;
  }

  /**
   * Initializes the current instance. This method is called before this instance can be used.
   * Currently it recognizes and loads all nlp resources. Node: This method should only be called
   * during static {@link NlpModel} creation.
   * 
   * @throws CoreException
   */
  void initialize() throws CoreException {
    loadDotCorpus();

    createCorpora();

    for (CorpusElement corpus : getCorpora()) {
      corpus.initialize();
    }

    createUimaSourceFolder();

    for (UimaSourceFolder sourceFolder : getUimaSourceFolder()) {
      sourceFolder.initialize();
    }

    if (getDotCorpus().getTypeSystemFile() != null) {
      mTypesystem = new TypesystemElement(getDotCorpus().getTypeSystemFile(), this);
    }

    if (getTypesystem() != null && getTypesystem().getCAS() != null) {
      mEditorAnnotationStatus = new EditorAnnotationStatus(getTypesystem().getCAS().getTypeSystem()
              .getType(CAS.TYPE_NAME_ANNOTATION), null);
    }
  }

  /**
   * Retrvies the name.
   */
  public String getName() {
    return mProject.getName();
  }

  /**
   * Retrives the resource.
   */
  public IResource getResource() {
    return getProject();
  }

  /**
   * Retrives the parent element {@link NlpModel}.
   */
  public INlpElement getParent() {
    return mModel;
  }

  /**
   * Not implemented, called to configure the project
   */
  public void configure() throws CoreException {
    // not implemented
  }

  /**
   * Not implemented, called to deconfigure the project
   */
  public void deconfigure() throws CoreException {
    // not implemented
  }

  /**
   * Retrives the {@link IProject}.
   */
  public IProject getProject() {
    return mProject;
  }

  /**
   * Sets the {@link IProject}.
   */
  public void setProject(IProject project) {
    mProject = project;
  }

  /**
   * Retrives the {@link EditorAnnotationStatus} for the current project instance.
   * 
   * @return status
   */
  public EditorAnnotationStatus getEditorAnnotationStatus() {
    return mEditorAnnotationStatus;
  }

  /**
   * Sets the {@link EditorAnnotationStatus} for the current project instance.
   * 
   * @param status
   */
  public void setEditorAnnotationStatus(EditorAnnotationStatus status) {
    mEditorAnnotationStatus = status;
  }

  private void loadDotCorpus() {
    IResource dotCorpusResource = getProject().getFile(DOT_CORPUS_FILENAME);

    if (dotCorpusResource instanceof IFile) {
      mDotCorpusElement = DotCorpusElement.createDotCorpus((IFile) dotCorpusResource, this);
    }

    // TODO: What happens when ther is a folder with the name ".corpus"
    // then load default .corpus ...
  }

  /**
   * Retrives the corporas.
   * 
   * @return the corpus collection
   */
  public Collection<CorpusElement> getCorpora() {
    return mCopora;
  }

  private void createCorpora() {
    for (IFolder corpusFolderName : mDotCorpusElement.getCorpusFolderNameList()) {
      if (corpusFolderName.exists()) {
        CorpusElement corpusElement = new CorpusElement(this, corpusFolderName);

        mCopora.add(corpusElement);
      }
    }
  }

  /**
   * Retrives all non-nlp resources of the current instance.
   * 
   * @return the resources
   * @throws CoreException
   */
  public IResource[] getResources() throws CoreException {
    IResource[] resources;

    resources = mProject.members();

    LinkedList<IResource> resourceList = new LinkedList<IResource>();

    for (int i = 0; i < resources.length; i++) {
      if (isSpecialResource(resources[i])) {
        continue;
      }

      if (resources[i] instanceof IFolder) {
        if (mDotCorpusElement.isCorpusFolder((IFolder) resources[i])) {
          continue;
        }

        IFolder configFolderName = mDotCorpusElement.getUimaConfigFolder();

        if (configFolderName != null && configFolderName.equals(resources[i])) {
          continue;
        }
      }

      if (mTypesystem != null && mTypesystem.getResource().equals(resources[i])) {
        continue;
      }

      resourceList.add(resources[i]);
    }

    IResource[] filteredResources = new IResource[resourceList.size()];

    return resourceList.toArray(filteredResources);
  }

  /**
   * Retrives the parent element for the given resource.
   */
  public INlpElement getParent(IResource resource) throws CoreException {
    INlpElement result = super.getParent(resource);

    if (result == null) {
      if (mDotCorpusElement != null) {
        if (mDotCorpusElement.getResource().equals(resource)) {
          return this;
        }
      }

      for (IResource candiadte : getResources()) {
        if (candiadte.equals(resource)) {
          return this;
        }
      }

      for (CorpusElement corpus : getCorpora()) {
        INlpElement element = corpus.getParent(resource);

        if (element != null) {
          return element;
        }
      }

      for (UimaSourceFolder sourceFolder : getUimaSourceFolder()) {
        INlpElement element = sourceFolder.getParent(resource);

        if (element != null) {
          return element;
        }
      }
    }

    return null;
  }

  /**
   * Searchs the {@link INlpElement} for the given resource.
   */
  public INlpElement findMember(IResource resource) {
    INlpElement result = super.findMember(resource);

    if (result == null) {
      if (DOT_CORPUS_FILENAME.equals(resource.getName())) {
        return mDotCorpusElement;
      }

      for (UimaSourceFolder sourceFolder : getUimaSourceFolder()) {
        boolean isElementFound = sourceFolder.findMember(resource) != null;

        if (isElementFound) {
          return sourceFolder.findMember(resource);
        }
      }

      for (CorpusElement corpus : getCorpora()) {
        boolean isElementFound = corpus.findMember(resource) != null;

        if (isElementFound) {
          return corpus.findMember(resource);
        }
      }
    }

    return result;
  }

  /**
   * Retrives the UimaSourceFolder
   * 
   * @return the UimaSourceFolder
   */
  public Collection<UimaSourceFolder> getUimaSourceFolder() {
    return mUimaSourceFolder;
  }

  private void createUimaSourceFolder() {
    IFolder configSourceFolderName = getDotCorpus().getUimaConfigFolder();

    if (configSourceFolderName != null && configSourceFolderName.exists()) {
      IFolder configSourceFolder = configSourceFolderName;

      mUimaSourceFolder.add(new UimaSourceFolder(configSourceFolder, this));
    }
  }

  /**
   * Retrives the {@link DotCorpusElement}.
   * 
   * @return the {@link DotCorpusElement}
   */
  public DotCorpusElement getDotCorpus() {
    return mDotCorpusElement;
  }

  /**
   * Retrives the {@link NlpProject}.
   */
  public NlpProject getNlpProject() {
    return this;
  }

  private boolean isSpecialResource(IResource resource) {
    String specialResource[] = { ".project", DOT_CORPUS_FILENAME };

    for (int i = 0; i < specialResource.length; i++) {
      if (resource.getName().equals(specialResource[i])) {
        return true;
      }
    }

    return false;
  }

  /**
   * Adds a resource to the current project instance.
   */
  public void addResource(IResource resource) {
    if (resource instanceof IFile) {
      IFile file = (IFile) resource;

      if (DOT_CORPUS_FILENAME.equals(file.getName())) {
        mIsDotCorpusDirty = true;
      }
      // check if file is typesystem
      else if (mDotCorpusElement.getTypeSystemFile() != null) {
        mTypesystem = new TypesystemElement((IFile) resource, this);
      }
    } else if (resource instanceof IFolder) {
      // corpus
      if (mDotCorpusElement.isCorpusFolder((IFolder) resource)) {
        mCopora.add(new CorpusElement(getNlpProject(), (IFolder) resource));
      } else if (mDotCorpusElement.isUimaConfigFolder((IFolder) resource)) {
        mUimaSourceFolder.add(new UimaSourceFolder((IFolder) resource, getNlpProject()));
      }
    }
  }

  /**
   * Removes a resource form the current porject instance.
   */
  public void removeResource(IResource resource) {
    if (resource instanceof IFile) {
      IFile file = (IFile) resource;

      if (DOT_CORPUS_FILENAME.equals(file.getName())) {
        mIsDotCorpusDirty = true;
      }

      if (mTypesystem != null && resource.equals(mTypesystem.getResource())) {
        mTypesystem = null;
      }
    }

    for (UimaSourceFolder sourceFolder : mUimaSourceFolder) {
      if (sourceFolder.getResource().equals(resource)) {
        mUimaSourceFolder.remove(resource);
        break;
      }
    }

    for (CorpusElement corpus : mCopora) {
      if (corpus.getResource().equals(resource)) {
        mCopora.remove(corpus);
        break;
      }
    }
  }

  @Override
  void changedResource(IResource resource, INlpElementDelta delta) throws CoreException {
    if (DOT_CORPUS_FILENAME.equals(resource.getName())) {
      mIsDotCorpusDirty = true;
    }

    if (mTypesystem != null && resource.equals(mTypesystem.getResource())) {
      mTypesystem.changedResource(resource, delta);
    }
  }

  void postProcessResourceChanges() throws CoreException {
    if (mIsDotCorpusDirty) {
      mIsDotCorpusDirty = false;

      mDotCorpusElement = null;
      loadDotCorpus();

      mUimaSourceFolder.clear();
      mCopora.clear();

      mTypesystem = null;

      initialize();

      TaeCorePlugin.getNlpModel().fireRefreshEvent(this);
    }
  }

  /**
   * Uses the {@link #getName()} to generate the hash code.
   */
  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  /**
   * Tests if the given object is equal to the current instance.
   */
  @Override
  public boolean equals(Object obj) {
    boolean isEqual;

    if (obj != null && obj instanceof NlpProject) {
      NlpProject project = (NlpProject) obj;

      isEqual = getResource().equals(project.getResource());
    } else {
      isEqual = false;
    }

    return isEqual;
  }

  /**
   * Adds a NLP nature to a project.
   * 
   * @param project -
   *          the project to add the nature
   * 
   * @throws CoreException
   */
  public static void addNLPNature(IProject project) throws CoreException {
    IProjectDescription description = project.getDescription();
    String[] natures = description.getNatureIds();
    String[] newNatures = new String[natures.length + 1];
    System.arraycopy(natures, 0, newNatures, 0, natures.length);
    newNatures[natures.length] = NlpProject.ID;
    description.setNatureIds(newNatures);
    project.setDescription(description, null);
  }

  /**
   * Retrives the typesystem.
   * 
   * @return typesystem
   */
  public TypesystemElement getTypesystem() {
    return mTypesystem;
  }
}