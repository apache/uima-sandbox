package org.apache.uima.lucas.indexer.util;

import org.apache.lucene.analysis.Token;

public class TokenFactory {

	public static Token newToken(String text, int begin, int end){
		return new Token(text.toCharArray(), 0, text.length(), begin, end);
	}
}
