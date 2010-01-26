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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 * A TokenStreamMerger merges a {@link java.util.List list} of
 * {@link org.apache.lucene.analysis.TokenStream token streams} by the means of their token offsets.
 * Adapts positionIncrement of tokens if their startOffset is exactly the same.
 */
public class TokenStreamMerger extends TokenStream {

  private class TokenComparator implements Comparator<Token> {

    public int compare(Token token1, Token token2) {

      return token2.startOffset() - token1.startOffset();

    }
  }

  private Collection<TokenStream> streams;

  private int currentOffset;

  private TokenComparator comparator;

  private Map<Token, TokenStream> currentTokens;

  private Stack<Token> sortedTokens;

  private boolean initialized;

  public TokenStreamMerger(Collection<TokenStream> streams) throws IOException {
    super();
    this.streams = streams;
    this.comparator = new TokenComparator();
    currentTokens = new LinkedHashMap<Token, TokenStream>();
    currentOffset = -1;
    sortedTokens = new Stack<Token>();

  }

  private void init() throws IOException {
    Token token = new Token();
	for (TokenStream stream : streams) {
      token = stream.next(token);
      if (token != null)
        currentTokens.put(token, stream);
    }
    rebuildSortedTokens();
    initialized = true;
  }

  public void reset() throws IOException {
    currentTokens.clear();
    for (TokenStream stream : streams)
      stream.reset();

    currentOffset = -1;
    sortedTokens.clear();
    initialized = false;
  }

  @Override
  public Token next() throws IOException {
    if (!initialized)
      init();

    if (sortedTokens.size() == 0)
      return null;

    Token currentToken = sortedTokens.pop();
    currentTokens.remove(currentToken);
    rebuildSortedTokens();

    if (currentToken.startOffset() == currentOffset)
      currentToken.setPositionIncrement(0);
    else
      currentToken.setPositionIncrement(1);

    currentOffset = currentToken.startOffset();

    return currentToken;
  }

  private void rebuildSortedTokens() throws IOException {
    Token token = new Token();
	for (TokenStream stream : streams)
      if (!currentTokens.values().contains(stream)) {
        token = stream.next(token);
        if (token != null)
          currentTokens.put(token, stream);
        else
            break;
      }

    sortedTokens.clear();
    sortedTokens.addAll(currentTokens.keySet());
    Collections.sort(sortedTokens, comparator);
  }

}
