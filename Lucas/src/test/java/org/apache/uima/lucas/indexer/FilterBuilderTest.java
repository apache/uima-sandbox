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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.uima.lucas.indexer.AnnotationDescription;
import org.apache.uima.lucas.indexer.FilterBuilder;
import org.apache.uima.lucas.indexer.analysis.AdditionTokenFilter;
import org.apache.uima.lucas.indexer.analysis.HypernymTokenFilter;
import org.apache.uima.lucas.indexer.analysis.PositionFilter;
import org.apache.uima.lucas.indexer.analysis.ReplaceFilter;
import org.apache.uima.lucas.indexer.analysis.SplitterFilter;
import org.apache.uima.lucas.indexer.analysis.UniqueFilter;
import org.apache.uima.lucas.indexer.analysis.UpperCaseTokenFilter;
import org.apache.uima.lucas.indexer.test.util.CollectionTokenStream;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FilterBuilderTest {

  private static final String PORTER = "Porter";

  private static final String SUFFIX = "suffix";

  private static final String PREFIX = "prefix";

  private static final String MAP_FILE_NAME = "mapfile.txt";

  private FilterBuilder filterBuilder;

  private AnnotationDescription annotationDescription;

  private String[] stopwords;

  private Map<String, Map<String, String>> tokenMappings;

  private Map<String, List<String>> hypernyms;

  private TokenStream tokenStream;

  private Map<String, String> tokenMapping;

  @Before
  public void setUp() {
    annotationDescription = new AnnotationDescription(null);
    Collection<Token> tokens = new ArrayList<Token>();
    tokens.add(new Token("token1".toCharArray(), 0, 6, 0, 6));
    tokens.add(new Token("token2".toCharArray(), 0, 6, 7, 13));
    tokens.add(new Token("token3".toCharArray(), 0, 6, 14, 20));

    tokenMappings = new HashMap<String, Map<String, String>>();
    tokenMapping = new HashMap<String, String>();
    tokenMappings.put(MAP_FILE_NAME, tokenMapping);

    tokenStream = new CollectionTokenStream(tokens);
    stopwords = new String[] { "na", "und", "nu" };
    hypernyms = new HashMap<String, List<String>>();
    hypernyms.put("token1", Lists.newArrayList("token111", "token11", "token1"));

    filterBuilder = new FilterBuilder(stopwords, hypernyms, tokenMappings);
  }

  @Test
  public void testFilterMapping() throws Exception {
    annotationDescription.setMappingFile(MAP_FILE_NAME);
    ReplaceFilter replaceFilter =
            (ReplaceFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(tokenMapping, replaceFilter.getMapping());
  }

  @Test
  public void testFilterPosition() throws Exception {
    annotationDescription.setPosition(FilterBuilder.POSITION_FIRST);
    PositionFilter positionFilter =
            (PositionFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(PositionFilter.FIRST_POSITION, positionFilter.getPosition());

    annotationDescription.setPosition(FilterBuilder.POSITION_LAST);
    positionFilter = (PositionFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(PositionFilter.LAST_POSITION, positionFilter.getPosition());
  }

  @Test
  public void testFilterAddition() throws Exception {
    annotationDescription.setPrefix(PREFIX);
    AdditionTokenFilter additionFilter =
            (AdditionTokenFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(AdditionTokenFilter.PREFIX, additionFilter.getPosition());
    assertEquals(PREFIX, additionFilter.getAddition());

    annotationDescription.setPostfix(SUFFIX);
    additionFilter = (AdditionTokenFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(PositionFilter.LAST_POSITION, additionFilter.getPosition());
    assertEquals(SUFFIX, additionFilter.getAddition());
  }

  @Test
  public void testFilterSplit() throws Exception {
    annotationDescription.setSplitString(" ");
    SplitterFilter splitterFilter =
            (SplitterFilter) filterBuilder.filter(tokenStream, annotationDescription);
    assertEquals(" ", splitterFilter.getSplitString());
  }

  @Test
  public void testFilterLowercase() throws Exception {
    annotationDescription.setLowercase(true);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof LowerCaseFilter);
  }

  @Test
  public void testFilterUpperCase() throws Exception {
    annotationDescription.setUppercase(true);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof UpperCaseTokenFilter);
  }

  @Test
  public void testFilterStopFilter() throws Exception {
    annotationDescription.setStopwordRemove(true);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof StopFilter);
  }

  @Test
  public void testFilterHypernyms() throws Exception {
    annotationDescription.setAddHypernyms(true);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof HypernymTokenFilter);
    HypernymTokenFilter hypernymTokenFilter = (HypernymTokenFilter) filteredTokenStream;
    assertEquals(hypernyms, hypernymTokenFilter.getHypernyms());
  }

  @Test
  public void testFilterPorter() throws Exception {
    annotationDescription.setSnowballFilter(PORTER);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof SnowballFilter);
  }

  @Test
  public void testFilterUnique() throws Exception {
    annotationDescription.setUnique(true);
    TokenStream filteredTokenStream = filterBuilder.filter(tokenStream, annotationDescription);
    assertTrue(filteredTokenStream instanceof UniqueFilter);
  }

}
