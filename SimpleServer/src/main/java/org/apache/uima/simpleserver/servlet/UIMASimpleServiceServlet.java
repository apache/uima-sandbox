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

package org.apache.uima.simpleserver.servlet;

import java.io.File;
import java.util.logging.Level;

/**
 * Generic servlet for UIMA Simple Services. Deploy it with the following init-params: PearPath OR
 * DescriptorPath - a path to a PEAR file or to a Analysis Engine descriptor ResultSpecFile - a
 * result specification The servlet will then provide the specified service.
 */
public class UIMASimpleServiceServlet extends UimaServletBase {

  @Override
  public boolean initServer() {
    File resultSpec = new File(this.baseWebappDirectory.getAbsoluteFile(),
        getInitParameter("ResultSpecFile"));
    String pearPath = getInitParameter("PearPath");
    String descriptorPath = getInitParameter("DescriptorPath");

    try {
      if (pearPath == null) {
        File descriptor = new File(this.baseWebappDirectory.getAbsoluteFile(), descriptorPath);
        this.server.configureAnalysisEngine(descriptor, resultSpec);
      } else if (descriptorPath == null) {
        File pear = new File(this.baseWebappDirectory.getAbsoluteFile(), pearPath);
        this.server.configurePear(pear, new File(pearPath), resultSpec);
      }
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, "UIMA Simple Service configuaration failed", e);
      return false;
    }
    return true;
  }
}
