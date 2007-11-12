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
import java.util.ArrayList;
import java.util.List;

import org.apache.incubator.uima.simpleserver.config.xml.FilterType;
import org.apache.incubator.uima.simpleserver.config.xml.TypeElementType;
import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument;
import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec;
import org.apache.uima.cas.impl.TypeSystemUtils;
import org.apache.uima.simpleserver.config.ConfigFactory;
import org.apache.uima.simpleserver.config.Filter;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.SimpleServerException;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.xmlbeans.XmlException;

/**
 * Read server configuration from an XML file or stream.
 */
public final class XmlConfigReader {

  public static ServerSpec readServerSpec(File file) throws IOException, XmlException,
      SimpleServerException {
    return readServerSpec(new BufferedInputStream(new FileInputStream(file)));
  }

  public static ServerSpec readServerSpec(InputStream is) throws IOException, XmlException,
      SimpleServerException {
    UimaSimpleServerSpec specBean = UimaSimpleServerSpecDocument.Factory.parse(is)
        .getUimaSimpleServerSpec();
    ServerSpec spec = ConfigFactory.newServerSpec(specBean.getShortDescription(), specBean
        .getLongDescription());
    TypeElementType[] typeMaps = specBean.getTypeArray();
    for (int i = 0; i < typeMaps.length; i++) {
      spec.addTypeMap(readTypeMap(typeMaps[i]));
    }
    return spec;
  }

  private static TypeMap readTypeMap(TypeElementType typeBean) throws SimpleServerException {
    boolean coveredText = typeBean.getOutputCoveredText();
    TypeMap typeMap = ConfigFactory.newTypeMap(typeBean.getName(), typeBean.getOutputTag(),
        coveredText, typeBean.getShortDescription(), typeBean.getLongDescription());
    if (typeBean.getFilters() != null) {
      FilterType[] filters = typeBean.getFilters().getFilterArray();
      for (int i = 0; i < filters.length; i++) {
        typeMap.addFilter(readFilter(filters[i]));
      }
    }
    return typeMap;
  }

  private static Filter readFilter(FilterType filterBean) throws SimpleServerException {
    List<String> path = parseFeaturePath(filterBean.getFeaturePath());
    Filter filter = ConfigFactory.newFilter(path, null);
    return filter;
  }

  private static List<String> parseFeaturePath(String path) throws SimpleServerException {
    List<String> featureList = new ArrayList<String>();
    final int max = path.length();
    int pos = 0;
    // Check if path starts with a slash; if so, eliminate.
    if ((max > 0) && (path.charAt(0) == ServerSpec.PATH_SEPARATOR)) {
      pos = 1;
    }
    while (pos < max) {
      // Find the next path separator
      int next = pos;
      while ((next < max) && (path.charAt(next) != ServerSpec.PATH_SEPARATOR)) {
        ++next;
      }
      // Found a slash at next position, invalid path syntax
      if ((next < max) && (next == pos)) {
        throw new SimpleServerException(SimpleServerException.incorrect_path_syntax,
            new Object[] { path });
      }
      String feature = path.substring(pos, next);
      if (!TypeSystemUtils.isIdentifier(feature)) {
        throw new SimpleServerException(SimpleServerException.incorrect_feature_syntax,
            new Object[] { feature, path });
      }
      pos = next + 1;
    }
    return featureList;
  }
}
