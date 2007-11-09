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

package org.apache.uima.simpleserver.config.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument;
import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec;
import org.apache.uima.simpleserver.config.ConfigFactory;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.xmlbeans.XmlException;

/**
 * Read server configuration from an XML file or stream.
 */
public final class XmlConfigReader {

  public static ServerSpec readServerSpec(File file) throws IOException, XmlException {
    return readServerSpec(new BufferedInputStream(new FileInputStream(file)));
  }

  public static ServerSpec readServerSpec(InputStream is) throws IOException, XmlException {
    UimaSimpleServerSpec specBean = UimaSimpleServerSpecDocument.Factory.parse(is)
        .getUimaSimpleServerSpec();
    ServerSpec spec = ConfigFactory.newServerSpec(specBean.getShortDescription(), specBean
        .getLongDescription());
    
    return spec;
  }

}
