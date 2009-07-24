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

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

public class DummyTokenStream extends TokenStream{
	private int count = 0;
	private String tokenValue;
	private int distance;
	private int number;
	private int begin;
	private int end;
	
	
	public DummyTokenStream(String tokenValue, int distance, int number, int offset) {
		this.tokenValue = tokenValue;
		this.distance = distance;
		this.number = number;
	
		
		if( offset > 0 ){
			count = offset;
			begin = count * (tokenValue.length() + 1);
			end = count * (tokenValue.length() + 1) + tokenValue.length();
		}
		else{
			begin = 0; 
			end = tokenValue.length();
		}			
	}
	
	public Token next() throws IOException {
		if( number <= count / distance )
			return null;
										
		Token token = new Token(tokenValue, begin, end);			

		count += distance;
		begin+= distance* (tokenValue.length() + 1);
		end+= distance* (tokenValue.length() + 1);
		
		System.out.println(token);
		return token;
	}		
}