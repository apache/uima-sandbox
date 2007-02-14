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

import java.util.HashSet;
import java.util.Set;


import org.apache.uima.caseditor.core.TaeCorePlugin;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.model.delta.INlpModelChangeListener;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.4.2.2 $, $Date: 2007/01/04 14:58:09 $
 */
public class NlpModelTest {
  private TestProject mProject;

  /**
   * Initializes the current instance.
   */
  public NlpModelTest() {
    mProject = new TestProject();
  }

  /**
   * @throws CoreException
   */
  @Before
  public void clearWorkspace() throws CoreException {
    WorkspaceUtil.clearWorkspace();
  }

  /**
   * Creates the nlp project from an existing project structure.
   * 
   * @throws CoreException
   */
  @Test
  public void testCreationFromExistingProject() throws CoreException {
    mProject.createProject();
    mProject.createProjectContent();
    mProject.createDotCorpus();

    // creates implicit the nlp model
    mProject.validateNlpProject();
  }

  /**
   * Adds the dotcorpus to an empty existing nlp project.
   * 
   * @throws CoreException
   */
  @Test
  public void testAddDotCorpus() throws CoreException {
    TaeCorePlugin.destroyNlpModelForTesting();

    mProject.createProject();
    mProject.createProjectContent();

    // create the nlp model now
    TaeCorePlugin.getNlpModel();

    mProject.createDotCorpus();

    mProject.validateNlpProject();
  }

  /**
   * This creates an nlp project. Then it creates the dot corpus and the NlpModel.
   * 
   * After that all resources are added to the project.
   * 
   * @throws CoreException
   */
  @Test
  public void testAddNlpElements() throws CoreException {
    TaeCorePlugin.destroyNlpModelForTesting();

    mProject.createProject();
    mProject.createDotCorpus();

    TaeCorePlugin.getNlpModel();

    mProject.createProjectContent();

    mProject.validateNlpProject();
  }

  /**
   * 1. create project with nlp nature 2. then create the dot corpus file 3. add all folders and
   * files
   * 
   * Note: the listener must be removed after the test
   * 
   * @throws CoreException
   */
  @Test
  public void testAddChangeEventListener() throws CoreException {
    TaeCorePlugin.destroyNlpModelForTesting();

    final Set<IResource> addedElements = new HashSet<IResource>();

    INlpModelChangeListener listener = new INlpModelChangeListener() {
      public void refresh(INlpElement element) {
      }

      public void resourceChanged(INlpElementDelta delta) {
        if (delta.getKind().equals(Kind.ADDED) && delta.getNlpElement() != null) {
          addedElements.add(delta.getResource());
        }

        for (INlpElementDelta child : delta.getAffectedChildren()) {
          resourceChanged(child);
        }
      }
    };

    TaeCorePlugin.getNlpModel().addNlpModelChangeListener(listener);

    try {
      mProject.createProject();
      mProject.createDotCorpus();
      mProject.createProjectContent();

      // check for add events
      for (IResource resource : mProject.getResources()) {
        assertTrue(resource.getName() + " add event is missing!", addedElements.contains(resource));
      }
    } finally {
      TaeCorePlugin.getNlpModel().removeNlpModelChangeListener(listener);
    }
  }

  /**
   * Tests if a project can be removed.
   * 
   * @throws CoreException
   */
  @Test
  public void testRemoveProject() throws CoreException {
    TaeCorePlugin.destroyNlpModelForTesting();

    mProject.createProject();
    mProject.createDotCorpus();
    mProject.createProjectContent();

    TaeCorePlugin.getNlpModel();

    mProject.removeProjectContent();

    mProject.removeProject();

    // check if nlp elements are all gone
    for (IResource resource : mProject.getResources()) {
      assertTrue(resource.getName() + " must not exist!", TaeCorePlugin.getNlpModel().findMember(
              resource) == null);
    }

    // TODO: check if all resources are there
  }

  /**
   * Note: the listener must be removed after the test.
   * 
   * @throws CoreException
   */
  @Test
  public void testRemoveChangeListener() throws CoreException {
    mProject.createProject();
    mProject.createDotCorpus();
    mProject.createProjectContent();

    final Set<IResource> removedElements = new HashSet<IResource>();

    INlpModelChangeListener listener = new INlpModelChangeListener() {
      public void refresh(INlpElement element) {
      }

      public void resourceChanged(INlpElementDelta delta) {
        if (delta.getKind().equals(Kind.REMOVED) && delta.getNlpElement() != null) {
          removedElements.add(delta.getResource());
        }

        for (INlpElementDelta child : delta.getAffectedChildren()) {
          resourceChanged(child);
        }
      }

    };

    TaeCorePlugin.getNlpModel().addNlpModelChangeListener(listener);

    try {
      mProject.removeProjectContent();

      mProject.removeProject();

      // check for removed events
      for (IResource resource : mProject.getResources()) {
        assertTrue(resource.getName() + " remove event is missing!", removedElements
                .contains(resource));
      }
    } finally {
      TaeCorePlugin.getNlpModel().removeNlpModelChangeListener(listener);
    }
  }
}