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

public class ConcatFilter extends TokenFilter{

	private int startOffset;
	private String concatString;
	private Token lastToken;
	
	protected ConcatFilter(TokenStream input, String concatString) {
		super(input);
		this.concatString = concatString;
		startOffset = -1;
	}
	
	@Override
	public Token next(Token reusableToken) throws IOException {
		String tokenText = concatenatedInputTokenTerms(reusableToken);
		reusableToken.setTermBuffer(tokenText.toCharArray(), 0, tokenText.length());
		reusableToken.setStartOffset(startOffset);
		reusableToken.setEndOffset(lastToken.endOffset());
		return reusableToken;
	}

	private String concatenatedInputTokenTerms(Token reusableToken) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		Token nextToken = input.next(reusableToken);
		lastToken = null;
		
		if( startOffset == -1 )
			startOffset = nextToken.startOffset();
		
		while( nextToken != null ){
			lastToken = nextToken;
			stringBuffer.append(nextToken.term());
			nextToken = input.next(reusableToken);
			if( nextToken != null )
				stringBuffer.append(concatString);
		}
		
		return stringBuffer.toString();
	}

	protected String getConcatString() {
		return concatString;
	}

}
