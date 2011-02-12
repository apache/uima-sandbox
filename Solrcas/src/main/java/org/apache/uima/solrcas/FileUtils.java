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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Utility class to retrieve resources both from classpath and URIs
 */
public class FileUtils {

  private static final String CLASSPATH = "classpath:";
  private static final String EMPTY_STRING = "";

  public static InputStream getStream(String path) throws IOException {
    InputStream input;
    if (path.startsWith(CLASSPATH)) {
      input = System.class.getResourceAsStream(path.replaceFirst(CLASSPATH, EMPTY_STRING));
    } else {
      input = URI.create(path).toURL().openStream();
    }
    return input;
  }

  public static URL getURL(String path) throws MalformedURLException {
    URL solrURL;
    if (path.startsWith(CLASSPATH)) {
      path = path.replaceFirst(CLASSPATH, EMPTY_STRING);
      solrURL = System.class.getResource(path);
    } else {
      solrURL = URI.create(path).toURL();
    }
    return solrURL;
  }

}
