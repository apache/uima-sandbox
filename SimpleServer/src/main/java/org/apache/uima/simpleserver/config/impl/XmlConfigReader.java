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
import java.util.Iterator;
import java.util.List;

import org.apache.incubator.uima.simpleserver.config.xml.And;
import org.apache.incubator.uima.simpleserver.config.xml.FilterOperator;
import org.apache.incubator.uima.simpleserver.config.xml.FilterType;
import org.apache.incubator.uima.simpleserver.config.xml.Or;
import org.apache.incubator.uima.simpleserver.config.xml.SimpleFilterType;
import org.apache.incubator.uima.simpleserver.config.xml.TypeElementType;
import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument;
import org.apache.incubator.uima.simpleserver.config.xml.UimaSimpleServerSpecDocument.UimaSimpleServerSpec;
import org.apache.uima.cas.impl.TypeSystemUtils;
import org.apache.uima.simpleserver.config.AndFilter;
import org.apache.uima.simpleserver.config.Condition;
import org.apache.uima.simpleserver.config.ConfigFactory;
import org.apache.uima.simpleserver.config.Filter;
import org.apache.uima.simpleserver.config.FilterOp;
import org.apache.uima.simpleserver.config.OrFilter;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.SimpleFilter;
import org.apache.uima.simpleserver.config.SimpleServerException;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

/**
 * Read server configuration from an XML file or stream.
 */
public final class XmlConfigReader {

  // Constants for filter operators
  private static final int NULL = FilterOperator.Enum.forString("null").intValue();

  private static final int NOT_NULL = FilterOperator.Enum.forString("null").intValue();

  private static final int EQUALS = FilterOperator.Enum.forString("null").intValue();

  private static final int NOT_EQUALS = FilterOperator.Enum.forString("null").intValue();

  private static final int LESS = FilterOperator.Enum.forString("null").intValue();

  private static final int LESS_EQ = FilterOperator.Enum.forString("null").intValue();

  private static final int GREATER = FilterOperator.Enum.forString("null").intValue();

  private static final int GREATER_EQ = FilterOperator.Enum.forString("null").intValue();

  public static ServerSpec readServerSpec(File file) throws IOException, XmlException,
      SimpleServerException {
    return readServerSpec(new BufferedInputStream(new FileInputStream(file)));
  }

  public static ServerSpec readServerSpec(InputStream is) throws IOException, XmlException,
      SimpleServerException {
    UimaSimpleServerSpec specBean = UimaSimpleServerSpecDocument.Factory.parse(is)
        .getUimaSimpleServerSpec();
    ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);

    boolean isValid = specBean.validate(validationOptions);

    // output the errors if the XML is invalid.
    if (!isValid) {
      Iterator<XmlError> iter = validationErrors.iterator();
      StringBuffer errorMessages = new StringBuffer();
      while (iter.hasNext()) {
        errorMessages.append("\n>> ");
        errorMessages.append(iter.next());
      }
      System.err.println(errorMessages);
    }

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
    Filter filter = null;
    if (typeBean.getFilters() != null) {
      FilterType filterBean = typeBean.getFilters().getFilter();
      if (filterBean != null) {
        filter = readFilter(filterBean);
      }
    }
    return ConfigFactory.newTypeMap(typeBean.getName(), filter, typeBean.getOutputTag(),
        coveredText, typeBean.getShortDescription(), typeBean.getLongDescription());
  }

  private static final Filter readFilter(FilterType filterBean) throws SimpleServerException {
    Filter filter = null;
    if (filterBean instanceof And) {
      filter = readAndFilter((And) filterBean);
    } else if (filterBean instanceof Or) {
      filter = readOrFilter((Or) filterBean);
    } else {
      filter = readSimpleFilter((SimpleFilterType) filterBean);
    }
    return filter;
  }

  private static final AndFilter readAndFilter(And filterBean) throws SimpleServerException {
    AndFilter filter = ConfigFactory.newAndFilter();
    FilterType[] filterBeans = filterBean.getFilterTypeArray();
    for (int i = 0; i < filterBeans.length; i++) {
      filter.addFilter(readFilter(filterBeans[i]));
    }
    return filter;
  }

  private static final OrFilter readOrFilter(Or filterBean) throws SimpleServerException {
    OrFilter filter = ConfigFactory.newOrFilter();
    FilterType[] filterBeans = filterBean.getFilterTypeArray();
    for (int i = 0; i < filterBeans.length; i++) {
      filter.addFilter(readFilter(filterBeans[i]));
    }
    return filter;
  }

  private static final SimpleFilter readSimpleFilter(SimpleFilterType filterBean)
      throws SimpleServerException {
    List<String> path = parseFeaturePath(filterBean.getFeaturePath());
    Condition condition = readCondition(filterBean.getOperator(), filterBean.getValue());
    return ConfigFactory.newSimpleFilter(path, condition);
  }

  private static final Condition readCondition(FilterOperator.Enum operator, String value) {
    return ConfigFactory.newCondition(readOperator(operator), value);
  }

  private static final FilterOp readOperator(FilterOperator.Enum operator) {
    final int op = operator.intValue();
    // Can't use switch because enum values aren't constants.
    if (op == NULL) {
      return FilterOp.NULL;
    } else if (op == NOT_NULL) {
      return FilterOp.NOT_NULL;
    } else if (op == EQUALS) {
      return FilterOp.EQUALS;
    } else if (op == NOT_EQUALS) {
      return FilterOp.NOT_EQUALS;
    } else if (op == LESS) {
      return FilterOp.LESS;
    } else if (op == LESS_EQ) {
      return FilterOp.LESS_EQ;
    } else if (op == GREATER) {
      return FilterOp.GREATER;
    } else if (op == GREATER_EQ) {
      return FilterOp.GREATER_EQ;
    }
    assert (false);
    return null;

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
