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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.uima.lucas.indexer.analysis.TokenStreamConcatenator;
import org.apache.uima.lucas.indexer.test.util.CollectionTokenStream;


import junit.framework.TestCase;

public class TokenStreamConcatenatorTest extends TestCase {

	public void testNext() throws Exception{
		Collection<TokenStream> tokenStreams = new ArrayList<TokenStream>();
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("token1", 0, 6));
		tokens.add(new Token("token2", 7, 13));
		tokens.add(new Token("token3", 14, 20));
		
		TokenStream tokenStream = new CollectionTokenStream(tokens );
		tokenStreams.add(tokenStream);

		tokens = new ArrayList<Token>();
		tokens.add(new Token("token4", 21, 27));
		tokens.add(new Token("token5", 28, 33));
		tokens.add(new Token("token6", 34, 40));

		tokenStream = new CollectionTokenStream(tokens );
		tokenStreams.add(tokenStream);
		
		TokenStreamConcatenator concatenator = new TokenStreamConcatenator(tokenStreams);
		
		Token nextToken = concatenator.next();
		assertEquals("token1", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token2", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token3", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token4", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token5", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token6", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		
		concatenator.reset();
		nextToken = concatenator.next();
		assertEquals("token1", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token2", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token3", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token4", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token5", new String(nextToken.termBuffer(), 0, nextToken.termLength()));
		nextToken = concatenator.next();
		assertEquals("token6", new String(nextToken.termBuffer(), 0, nextToken.termLength()));		
	}
}
