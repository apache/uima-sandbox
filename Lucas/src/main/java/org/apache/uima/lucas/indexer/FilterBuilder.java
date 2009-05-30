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

package org.apache.uima.lucas.indexer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.uima.lucas.indexer.analysis.AdditionTokenFilter;
import org.apache.uima.lucas.indexer.analysis.HypernymTokenFilter;
import org.apache.uima.lucas.indexer.analysis.PositionFilter;
import org.apache.uima.lucas.indexer.analysis.ReplaceFilter;
import org.apache.uima.lucas.indexer.analysis.SplitterFilter;
import org.apache.uima.lucas.indexer.analysis.UniqueFilter;
import org.apache.uima.lucas.indexer.analysis.UpperCaseTokenFilter;

public class FilterBuilder {

  private String[] stopwords;

  private Map<String, List<String>> hypernyms;

  private Map<String, Map<String, String>> mappings;

  public static final Logger LOGGER = Logger.getLogger(FilterBuilder.class);

  public static final String POSITION_FIRST = "first";

  public static final String POSITION_LAST = "last";

  public FilterBuilder(String[] stopwords, Map<String, List<String>> hypernyms,
          Map<String, Map<String, String>> mappings) {
    super();
    this.stopwords = stopwords;
    this.hypernyms = hypernyms;
    this.mappings = mappings;
  }

  public TokenStream filter(TokenStream tokenStream, AnnotationDescription description)
          throws IOException {

    String mappingFile = description.getMappingFile();
    if (mappingFile != null) {
      Map<String, String> mapping = mappings.get(mappingFile);
      if (mapping == null)
        throw new IllegalStateException(mappingFile + " not found!");
      tokenStream = new ReplaceFilter(tokenStream, mapping);
    }

    // add filters
    if (description.getPosition() != null) {
      String position = description.getPosition();
      if (position.equals(AnnotationDescription.FIRST_POSITION))
        tokenStream = new PositionFilter(tokenStream, PositionFilter.FIRST_POSITION);
      else if (position.equals(AnnotationDescription.LAST_POSITION))
        tokenStream = new PositionFilter(tokenStream, PositionFilter.LAST_POSITION);
    }

    String prefix = description.getPrefix();
    if (prefix != null)
      tokenStream = new AdditionTokenFilter(tokenStream, prefix, AdditionTokenFilter.PREFIX);

    String postfix = description.getPostfix();
    if (postfix != null)
      tokenStream = new AdditionTokenFilter(tokenStream, postfix, AdditionTokenFilter.POSTFIX);

    String splitString = description.getSplitString();
    if (splitString != null) {
      tokenStream = new SplitterFilter(tokenStream, splitString);
    }

    if (description.getLowercase()) {
      tokenStream = new LowerCaseFilter(tokenStream);
    }

    if (description.getUppercase()) {
      tokenStream = new UpperCaseTokenFilter(tokenStream);
    }

    if (description.getStopwordRemove()) {
      tokenStream = new StopFilter(tokenStream, stopwords);
    }
    if (description.getAddHypernyms()) {
      if (hypernyms.size() == 0)
        LOGGER.warn("hypernym adding activatet, but no hypernyms found!");

      tokenStream = new HypernymTokenFilter(tokenStream, hypernyms);
    }
    String snowballFilter = description.getSnowballFilter();
    if (snowballFilter != null) {
      tokenStream = new SnowballFilter(tokenStream, snowballFilter);
    }

    if (description.getUnique()) {
      tokenStream = new UniqueFilter(tokenStream);
    }

    return tokenStream;
  }

  public String[] getStopwords() {
    return stopwords;
  }

  public Map<String, List<String>> getHypernyms() {
    return hypernyms;
  }

  public Map<String, Map<String, String>> getMappings() {
    return mappings;
  }
}
