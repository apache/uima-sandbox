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

package org.apache.uima.dde.internal.wizards;

import org.apache.uima.taeconfigurator.wizards.AbstractNewWizard;

public class DDENewWizard extends AbstractNewWizard  {

  static public final String EDITOR_ID="com.ibm.apache.uima.dde.internal.MultiPageEditor";
  static protected String editorId = EDITOR_ID;
  
  public DDENewWizard() {
    super("New Analysis Engine Descriptor File");
  }

  public void addPages() {
    page = new DDENewWizardPage(selection);
    addPage(page);
  }

  public String getPrototypeDescriptor(String name) {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<analysisEngineDeploymentDescription "
    + XMLNS_PART
    + "<name>" + name + "</name>\n"
    + "<description></description>\n" + "<version>1.0</version>\n" + "<vendor></vendor>\n"

    + "<deployment protocol=\"jms\" provider=\"activemq\">\n"
    + "<casPool numberOfCASes=\"5\"/>\n"
    + "<service>"
    + "<inputQueue endpoint=\"myQueueName\" brokerURL=\"tcp://localhost:61616\"/>\n"
    // + "<topDescriptor>\n"
    // + "<import location=\"\"/>\n"
    // + "</topDescriptor>\n"
    + "</service>"
    + "</deployment>\n"

    + "</analysisEngineDeploymentDescription>\n";
  }

}
