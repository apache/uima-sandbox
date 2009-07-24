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
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.uima.lucas.indexer.analysis.AdditionFilter;
import org.apache.uima.lucas.indexer.test.util.CollectionTokenStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdditionFilterTest {

	@Test
	public void testNext() throws Exception{		
		Collection<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("token1", 0, 6));
		tokens.add(new Token("token2", 7, 13));
		tokens.add(new Token("token3", 14, 20));
		tokens.add(new Token("token4", 21, 27));
		
		TokenStream tokenStream = new CollectionTokenStream(tokens);
		AdditionFilter filter = new AdditionFilter(tokenStream, "prefix_", AdditionFilter.PREFIX);
		
		Token next = filter.next();
		assertEquals("prefix_token1", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("prefix_token2", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("prefix_token3", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("prefix_token4", new String(next.termBuffer(), 0, next.termLength()));		

		tokens = new ArrayList<Token>();
		tokens.add(new Token("token1", 0, 6));
		tokens.add(new Token("token2", 7, 13));
		tokens.add(new Token("token3", 14, 20));
		tokens.add(new Token("token4", 21, 27));
		
		tokenStream = new CollectionTokenStream(tokens);
		filter = new AdditionFilter(tokenStream, "_postfix", AdditionFilter.POSTFIX);
		
		next = filter.next();
		assertEquals("token1_postfix", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("token2_postfix", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("token3_postfix", new String(next.termBuffer(), 0, next.termLength()));
		next = filter.next();
		assertEquals("token4_postfix", new String(next.termBuffer(), 0, next.termLength()));				
	}
}
