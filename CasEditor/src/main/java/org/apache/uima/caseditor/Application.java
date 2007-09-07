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

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

  private static final String PROP_EXIT_CODE = "eclipse.exitcode";

  public Object start(IApplicationContext context) throws Exception {
    Display display = PlatformUI.createDisplay();

    Shell shell = new Shell(display, SWT.ON_TOP);

//    try {
//      if (!WorkspaceUtil.checkInstanceLocation(shell)) {
//        Platform.endSplash();
//        return EXIT_OK;
//      }
//    } finally {
//      if (shell != null) {
//        shell.dispose();
//      }
//    }

    try {
      int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
      if (returnCode != PlatformUI.RETURN_RESTART) {
        return EXIT_OK;
      }
      
      // the OpenWorkspaceAction sets the PROP_EXIT_CODE before the restart
      return EXIT_RELAUNCH.equals(Integer.getInteger(PROP_EXIT_CODE)) ? EXIT_RELAUNCH
              : EXIT_RESTART;
    } finally {
      display.dispose();
    }
  }

  public void stop() {
  }

}