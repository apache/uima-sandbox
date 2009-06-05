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

package org.apache.uima.lucas.indexer.analysis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * TokenFilter subclass which adds hypernyms to a TokenStream based on a map.
 */
public class HypernymTokenFilter extends TokenFilter {

  private static Logger logger = Logger.getLogger(HypernymTokenFilter.class);

  private Map<String, List<String>> hypernyms;

  private TokenStream tokenStream;

  private int currentHypernymIndex;

  private List<String> currentHypernyms;

  private Token inputToken;

  /**
   * Constructor.
   * 
   * @param input
   *          the input TokenStream
   * @param hypernyms
   *          the hypernym map. key: token text, value: list of hypernyms
   */
  public HypernymTokenFilter(TokenStream input, Map<String, List<String>> hypernyms) {
    super(input);
    this.hypernyms = hypernyms;
    this.tokenStream = input;
    this.currentHypernymIndex = -1;
  }

  @Override
  public Token next() throws IOException {
    if (currentHypernymIndex >= 0 && currentHypernymIndex < currentHypernyms.size()) {
      Token hypernymToken =
              new Token(currentHypernyms.get(currentHypernymIndex), inputToken.startOffset(),
                      inputToken.endOffset());
      hypernymToken.setPositionIncrement(0);
      logger
              .debug("adding hypernym " + hypernymToken.termText() + " for :"
                      + inputToken.termText());
      currentHypernymIndex++;
      return hypernymToken;
    } else if (currentHypernymIndex >= 0 && currentHypernymIndex == currentHypernyms.size()) {
      currentHypernymIndex = -1;
      currentHypernyms = null;
    }

    inputToken = tokenStream.next();
    if (inputToken != null) {
      currentHypernyms = hypernyms.get(inputToken.termText());
      if (currentHypernyms != null)
        currentHypernymIndex = 0;
    }
    return inputToken;
  }

  @Override
  public void reset() throws IOException {
    inputToken = null;
    currentHypernymIndex = -1;
    if (currentHypernyms != null)
      currentHypernyms = null;

    tokenStream.reset();
  }

  public Map<String, List<String>> getHypernyms() {
    return hypernyms;
  }
}