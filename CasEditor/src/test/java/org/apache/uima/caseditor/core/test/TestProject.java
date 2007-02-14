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

package org.apache.uima.caseditor.core.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.model.dotcorpus.DotCorpus;
import org.apache.uima.caseditor.core.model.dotcorpus.DotCorpusSerializer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.2 $, $Date: 2007/01/04 14:58:09 $
 */
public class TestProject {
  private IProject mProject;

  private IFile mDotCorpus;

  private IFolder mCorpusFolder;

  private IFolder mSourceFolder;

  private IFolder mConfiguration;

  private IFile mTypesystem;

  private IFile mDocument;

  private IFile mAnnotatorFile;

  private IFile mConsumerFile;

  private IResource mContentResources[];

  public TestProject() {
    mProject = ResourcesPlugin.getWorkspace().getRoot().getProject("JUnitTestProject");

    mDotCorpus = mProject.getFile(".corpus");

    mCorpusFolder = mProject.getFolder("corpus");

    mSourceFolder = mProject.getFolder("UimaSourceFolder");
    mConfiguration = mProject.getFolder("UimaSourceFolder/Config");

    mTypesystem = mProject.getFile("Typesystem.xml");
    mDocument = mCorpusFolder.getFile("Document.xcas");
    mAnnotatorFile = mConfiguration.getFile("Annotator.ann");
    mConsumerFile = mConfiguration.getFile("Consumer.con");

    mContentResources = new IResource[] { mCorpusFolder, mSourceFolder, mConfiguration, mDocument,
        mAnnotatorFile, mConsumerFile };
  }

  IFile getDotCorpus() {
    return mDotCorpus;
  }

  IResource[] getResources() {
    return mContentResources;
  }

  IFile getAnnotatorFile() {
    return mAnnotatorFile;
  }

  IFolder getConfiguration() {
    return mConfiguration;
  }

  IFile getConsumerFile() {
    return mConsumerFile;
  }

  IFolder getCorpusFolder() {
    return mCorpusFolder;
  }

  IFile getDocument() {
    return mDocument;
  }

  IFolder getSourceFolder() {
    return mSourceFolder;
  }

  IFile getTypesystem() {
    return mTypesystem;
  }

  IProject getProject() {
    return mProject;
  }

  /**
   * Creates an emtpy project with nlp nature.
   * 
   * @throws CoreException
   */
  void createProject() throws CoreException {
    mProject.create(null);

    mProject.open(null);

    NlpProject.addNLPNature(mProject);
  }

  /**
   * Creates the Nlp Project content.
   * 
   * @throws CoreException
   */
  void createProjectContent() throws CoreException {
    // create corpus folder
    mCorpusFolder.create(true, true, null);

    // create source folder
    mSourceFolder.create(true, true, null);

    // create config folder
    mConfiguration.create(true, true, null);

    // create a typesystem file here ...
    mTypesystem.create(getClass().getResourceAsStream("/net/sf/tae/core/test/Typesystem.xml"),
            true, null);

    // create annotator.ann
    mAnnotatorFile.create(getClass().getResourceAsStream("/net/sf/tae/core/test/Annotator.ann"),
            true, null);

    // careate consumer.con
    mConsumerFile.create(getClass().getResourceAsStream("/net/sf/tae/core/test/Consumer.con"),
            true, null);

    // create a document
    mDocument.create(getClass().getResourceAsStream("/net/sf/tae/core/test/Document.xcas"), true,
            null);
  }

  /**
   * 
   * @throws CoreException
   */
  void createDotCorpus() throws CoreException {
    DotCorpus dotCorpus = new DotCorpus();

    dotCorpus.addCorpusFolder(mCorpusFolder.getName());
    dotCorpus.setUimaConfigFolderName(mSourceFolder.getName());
    dotCorpus.setTypeSystemFilename(mTypesystem.getName());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DotCorpusSerializer.serialize(dotCorpus, out);

    InputStream in = new ByteArrayInputStream(out.toByteArray());

    mDotCorpus.create(in, true, null);
  }

  void removeProject() throws CoreException {
    mProject.delete(true, null);
  }

  void removeProjectContent() throws CoreException {
    mDocument.delete(true, null);
    mCorpusFolder.delete(true, null);
    mAnnotatorFile.delete(true, null);
    mConsumerFile.delete(true, null);
    mConfiguration.delete(true, null);
    mSourceFolder.delete(true, null);
  }

  void validateNlpProject() {
    // TODO: add nlp project validation
    for (IResource resource : mContentResources) {
      assertTrue(resource.getName() + " does not exist!", (TaeCorePlugin.getNlpModel().findMember(
              resource) != null));
    }
  }
}