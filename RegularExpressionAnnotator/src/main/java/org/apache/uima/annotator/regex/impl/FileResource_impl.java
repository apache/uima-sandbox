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
package org.apache.uima.annotator.regex.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.uima.annotator.regex.FileResource;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Simple implementation of a file resource.
 */
public class FileResource_impl implements SharedResourceObject, FileResource {

  protected File resourceFile;

  public FileResource_impl() {
    super();
    this.resourceFile = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.resource.SharedResourceObject#load(org.apache.uima.resource.DataResource)
   */
  public void load(DataResource aData) throws ResourceInitializationException {
    if (aData != null) {
      URL url = null;
      String path = null;

      try {
        url = aData.getUrl();
        path = URLDecoder.decode(url.getPath(), "UTF-8");
      } catch (UnsupportedEncodingException ex) {
        throw new ResourceInitializationException(ex);
      }

      this.resourceFile = new File(path);

      if (!this.resourceFile.exists() || !this.resourceFile.canRead()) {
        this.resourceFile = null;
        throw new ResourceInitializationException(RegExAnnotator.MESSAGE_DIGEST,
                "regex_annotator_resource_not_found", new Object[] { path });
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.an_regex.FileResource#getFile()
   */
  public File getFile() {
    return this.resourceFile;
  }

}