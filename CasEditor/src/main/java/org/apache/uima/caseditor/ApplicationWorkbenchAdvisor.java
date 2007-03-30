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

package org.apache.uima.caseditor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

  private static final String PERSPECTIVE_ID = "org.apache.uima.caseditor.perspective.NLP";

  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    return new ApplicationWorkbenchWindowAdvisor(configurer);
  }

  @Override
  public void initialize(IWorkbenchConfigurer configurer) {
    configurer.setSaveAndRestore(true);
  }

  public String getInitialWindowPerspectiveId() {
    return PERSPECTIVE_ID;
  }

  @Override
  public void postShutdown() {
    IRunnableWithProgress runnable = new IRunnableWithProgress() {

      public void run(org.eclipse.core.runtime.IProgressMonitor monitor)
              throws InvocationTargetException, InterruptedException {
        try {
          ResourcesPlugin.getWorkspace().save(true, monitor);
        } catch (CoreException e) {
          // fail silently
        }
      }
    };

    try {
      new ProgressMonitorJobsDialog(null).run(true, false, runnable);
    } catch (InvocationTargetException e) {
      // fail silently
    } catch (InterruptedException e) {
      // fail silently
    }
  }
}
