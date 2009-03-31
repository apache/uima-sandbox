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

package org.apache.uima.caseditor.ui.model;

import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.Images;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * The {@link IWorkbenchAdapter} for the {@link IProject}.
 */
public class SimpleProjectAdapter implements IWorkbenchAdapter {

  public Object[] getChildren(Object o) {
    IProject project = (IProject) o;

    IResource resources[];

    try {
      resources = project.members();
    } catch (CoreException e) {
      // TODO: log it

      // no children available
      return new Object[0];
    }

    return resources;
  }

  public ImageDescriptor getImageDescriptor(Object object) {

    IProject project = (IProject) object;

    if (project.isOpen())
    {
      return CasEditorPlugin.getTaeImageDescriptor(Images.MODEL_PROJECT_OPEN);
    }
    else
    {
      return CasEditorPlugin.getTaeImageDescriptor(Images.MODEL_PROJECT_CLOSED);
    }
  }

  public String getLabel(Object o) {
    IProject project = (IProject) o;

    return project.getName();
  }

  public Object getParent(Object o) {
    // the nlp model is the parent of all projects, with or without uima nature
    return org.apache.uima.caseditor.CasEditorPlugin.getNlpModel();
  }
}
