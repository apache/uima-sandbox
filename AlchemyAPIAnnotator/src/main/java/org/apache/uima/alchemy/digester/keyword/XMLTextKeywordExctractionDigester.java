/**
 * 	Licensed to the Apache Software Foundation (ASF) under one
 * 	or more contributor license agreements.  See the NOTICE file
 * 	distributed with this work for additional information
 * 	regarding copyright ownership.  The ASF licenses this file
 * 	to you under the Apache License, Version 2.0 (the
 * 	"License"); you may not use this file except in compliance
 * 	with the License.  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing,
 * 	software distributed under the License is distributed on an
 * 	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * 	KIND, either express or implied.  See the License for the
 * 	specific language governing permissions and limitations
 * 	under the License.
 */
package org.apache.uima.alchemy.digester.keyword;

import org.apache.commons.digester.Digester;
import org.apache.uima.alchemy.digester.OutputDigester;
import org.apache.uima.alchemy.digester.domain.Keyword;
import org.apache.uima.alchemy.digester.domain.KeywordResults;
import org.apache.uima.alchemy.digester.domain.Results;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class XMLTextKeywordExctractionDigester implements OutputDigester {

  public Results parseAlchemyXML(InputStream xmlReader) throws IOException, SAXException,
          URISyntaxException {
    Digester digester = new Digester();
    digester.setValidating(false);

    digester.addObjectCreate("results", KeywordResults.class);
    digester.addBeanPropertySetter("results/status", "status");
    digester.addBeanPropertySetter("results/statusInfo", "statusInfo");
    digester.addBeanPropertySetter("results/url", "url");
    digester.addBeanPropertySetter("results/language", "language");
    digester.addObjectCreate("results/keywords/keyword", Keyword.class);
    digester.addBeanPropertySetter("results/keywords/keyword", "text");
    digester.addSetNext("results/keywords/keyword", "addKeyword");
    return (Results) digester.parse(xmlReader);
  }

}
