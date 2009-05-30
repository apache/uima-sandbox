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

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class UpperCaseTokenFilter extends TokenFilter {

  private TokenStream input;

  public UpperCaseTokenFilter(TokenStream input) {
    super(input);
    this.input = input;
  }

  @Override
  public Token next() throws IOException {
    Token nextToken = input.next();
    if (nextToken == null)
      return null;

    String termText = new String(nextToken.termBuffer(), 0, nextToken.termLength());
    termText = termText.toUpperCase();

    nextToken.setTermBuffer(termText.toCharArray(), 0, termText.length());

    return nextToken;
  }

  @Override
  public void reset() throws IOException {
    input.reset();
  }
}
