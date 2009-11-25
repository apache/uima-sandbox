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
import java.util.ArrayList;
import java.util.List;


import junit.framework.TestCase;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.uima.lucas.indexer.analysis.TokenStreamMerger;
import org.apache.uima.lucas.indexer.test.util.DummyTokenStream;

public class TokenStreamMergerTest extends TestCase {
	private TokenStreamMerger merger;
	@Override
	protected void setUp() throws Exception {
		List<TokenStream> streams = new ArrayList<TokenStream>();
		streams.add(new DummyTokenStream("1111", 1, 4, 0));
		streams.add(new DummyTokenStream("2222", 2, 2, 1));
		streams.add(new DummyTokenStream("3333", 3, 1, 2));
		merger = new TokenStreamMerger(streams);
	}
	
	public void testNext() throws IOException {
		
		Token currentToken = merger.next();
		
		assertEquals("1111", currentToken.term());
		assertEquals(0, currentToken.startOffset());
		assertEquals(4, currentToken.endOffset());
		assertEquals(1, currentToken.getPositionIncrement());
		
		currentToken = merger.next();		
		assertEquals("1111", currentToken.term());
		assertEquals(5, currentToken.startOffset());
		assertEquals(9, currentToken.endOffset());
		assertEquals(1, currentToken.getPositionIncrement());

		currentToken = merger.next();		
		assertEquals("2222", currentToken.term());
		assertEquals(5, currentToken.startOffset());
		assertEquals(9, currentToken.endOffset());
		assertEquals(0, currentToken.getPositionIncrement());

		currentToken = merger.next();		
		assertEquals("1111", currentToken.term());
		assertEquals(10, currentToken.startOffset());
		assertEquals(14, currentToken.endOffset());
		assertEquals(1, currentToken.getPositionIncrement());

		currentToken = merger.next();		
		assertEquals("3333", currentToken.term());
		assertEquals(10, currentToken.startOffset());
		assertEquals(14, currentToken.endOffset());
		assertEquals(0, currentToken.getPositionIncrement());

		currentToken = merger.next();		
		assertEquals("1111", currentToken.term());
		assertEquals(15, currentToken.startOffset());
		assertEquals(19, currentToken.endOffset());
		assertEquals(1, currentToken.getPositionIncrement());

		currentToken = merger.next();		
		assertEquals("2222", currentToken.term());
		assertEquals(15, currentToken.startOffset());
		assertEquals(19, currentToken.endOffset());
		assertEquals(0, currentToken.getPositionIncrement());
	}

}
