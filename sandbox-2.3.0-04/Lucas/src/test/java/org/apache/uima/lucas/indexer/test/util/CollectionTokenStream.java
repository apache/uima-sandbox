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

package org.apache.uima.lucas.indexer.test.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

public class CollectionTokenStream extends TokenStream{

	private Iterator<Token> tokenIterator;
	private Collection<Token> tokens;
	
	
	public CollectionTokenStream(Collection<Token> tokens) {
		super();
		this.tokenIterator = tokens.iterator();
		this.tokens = tokens;
	}


	@Override
	public Token next(Token nextToken) throws IOException {
		if( tokenIterator.hasNext() ){
			Token nextCollectionToken = tokenIterator.next();
			nextToken.reinit(nextCollectionToken);
			return nextToken;
		}
		else
			return null;
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		this.tokenIterator = tokens.iterator();
	}
}
