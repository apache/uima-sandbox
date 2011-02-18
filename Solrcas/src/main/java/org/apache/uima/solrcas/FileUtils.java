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

package org.apache.uima.solrcas;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Utility class to retrieve resources both from classpath and URLs
 */
public class FileUtils {

  private static final String CLASSPATH = "classpath:";
  private static final String EMPTY_STRING = "";

  public static URL getURL(String path) throws MalformedURLException {
    URL solrURL;
    if (path.startsWith(CLASSPATH)) {
      solrURL = System.class.getResource(path.replaceFirst(CLASSPATH, EMPTY_STRING));
    } else {
      solrURL = URI.create(path).toURL();
    }
    return solrURL;
  }

}
