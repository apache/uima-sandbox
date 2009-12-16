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
import org.apache.lucene.analysis.TokenStream;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

public class ConcatFilterTest {

	private ConcatFilter concatFilter;
	private TokenStream tokenStream;
	private Token token1;
	private Token token2;
	private Token token3;
	private Token token;
	
	@Before
	public void setUp() throws IOException{
		tokenStream = createMock(TokenStream.class);
		concatFilter = new ConcatFilter(tokenStream, "|");
		token1 = new Token("token1".toCharArray(), 0 , 6, 0, 5);
		token2 = new Token("token2".toCharArray(), 0 , 6, 6, 11);
		token3 = new Token("token3".toCharArray(), 0 , 6, 12, 17);
		token = new Token();
		
		setUpInputTokenStream();
	}
	
	@Test
	public void testNext() throws IOException{
		Token concatenatedToken = concatFilter.next(token);
		verify(tokenStream);
		
		assertEquals(token, concatenatedToken);
		assertEquals(0, concatenatedToken.startOffset());
		assertEquals(17, concatenatedToken.endOffset());
		assertEquals("token1|token2|token3", concatenatedToken.term());
	}

	private void setUpInputTokenStream() throws IOException {
		expect(tokenStream.next(token)).andReturn(token1);
		expect(tokenStream.next(token)).andReturn(token2);
		expect(tokenStream.next(token)).andReturn(token3);
		expect(tokenStream.next(token)).andReturn(null);
		replay(tokenStream);
	}
}
