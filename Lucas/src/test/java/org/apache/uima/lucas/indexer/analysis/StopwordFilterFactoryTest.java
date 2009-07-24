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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.uima.lucas.indexer.util.PlainFileReader;
import org.apache.uima.lucas.indexer.util.PlainFileReaderFactory;
import org.junit.Before;
import org.junit.Test;

public class StopwordFilterFactoryTest {
	  private static final String TEST_FILE_1 = "src/test/resources/ReplaceFilterFactoryTest1.txt";
	  private StopwordFilterFactory stopwordFilterFactory;
	  private PlainFileReaderFactory plainFileReaderFactory;
	  private PlainFileReader plainFileReader;
	  private TokenStream tokenStream;
	  private String[] lines;
	  
	  @Before
	  public void setUp(){
		plainFileReaderFactory = createMock(PlainFileReaderFactory.class);
		plainFileReader= createMock(PlainFileReader.class);
	    stopwordFilterFactory = new StopwordFilterFactory(plainFileReaderFactory);
	    tokenStream = createMock(TokenStream.class);
	    lines = new String[]{};
	  }
	  
	  @Test
	  public void testCreateTokenFilter() throws Exception{
	    Properties properties = new Properties();
	    properties.setProperty(StopwordFilterFactory.FILE_PATH_PARAMETER, TEST_FILE_1);
	    
	    expect(plainFileReaderFactory.createPlainFileReader(TEST_FILE_1)).andReturn(plainFileReader);
	    expect(plainFileReader.readLines()).andReturn(lines);
	    replay(plainFileReaderFactory);
	    replay(plainFileReader);
	    
	    StopFilter stopFilter = (StopFilter) stopwordFilterFactory.createTokenFilter(tokenStream, properties);
	    assertNotNull(stopFilter);  
	    verify(plainFileReaderFactory);
	    verify(plainFileReader);
	    
	    reset(plainFileReaderFactory);
	    reset(plainFileReader);
	    replay(plainFileReaderFactory);
	    replay(plainFileReader);
	    
	    // test caching (no calls of buffered reader)
	    stopFilter = (StopFilter) stopwordFilterFactory.createTokenFilter(tokenStream, properties);
	    verify(plainFileReaderFactory);
	    verify(plainFileReader);
	  }
	  
	  @Test
	  public void testPreloadResources() throws Exception{
		    Properties properties = new Properties();
		    properties.setProperty(ReplaceFilterFactory.FILE_PATH_PARAMETER, TEST_FILE_1);
		    
		    expect(plainFileReaderFactory.createPlainFileReader(TEST_FILE_1)).andReturn(plainFileReader);
		    expect(plainFileReader.readLines()).andReturn(lines);
		    replay(plainFileReaderFactory);
		    replay(plainFileReader);
		    
		    stopwordFilterFactory.preloadResources(properties);
		    verify(plainFileReaderFactory);
		    verify(plainFileReader);
		    
		    reset(plainFileReaderFactory);
		    reset(plainFileReader);
		    replay(plainFileReaderFactory);
		    replay(plainFileReader);
		    
		    // test caching (no calls of buffered reader)
		    stopwordFilterFactory.preloadResources(properties);
		    verify(plainFileReaderFactory);
		    verify(plainFileReader);
	  }
}
