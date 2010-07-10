/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.uima.alchemy.annotator;

import org.apache.uima.alchemy.digester.concept.ConceptTaggingDigesterProvider;
import org.apache.uima.alchemy.digester.DigesterProvider;
import org.apache.uima.alchemy.digester.domain.ConceptResults;
import org.apache.uima.alchemy.digester.domain.Results;
import org.apache.uima.alchemy.utils.Alchemy2TypeSystemMapper;
import org.apache.uima.alchemy.utils.exception.MappingException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class TextConceptTaggingAnnotator extends AbstractAlchemyAnnotator {
  @Override
  protected DigesterProvider createDigester() {
    return new ConceptTaggingDigesterProvider();
  }

  @Override
  protected URL createServiceURI() throws MalformedURLException {
    return URI.create("http://access.alchemyapi.com/calls/text/TextGetRankedConcepts").toURL();
  }

  @Override
  protected String[] getServiceParameters() {
    return new String[] { "url", "maxRetrieve", "outputMode", "linkedData", "showSourceText" };
  }

  @Override
  protected void mapResultsToTypeSystem(Results results, JCas aJCas) throws MappingException {
    Alchemy2TypeSystemMapper.mapConceptTagging((ConceptResults) results, aJCas);
  }

  @Override
  protected void initializeRuntimeParameters(JCas aJCas) throws AnalysisEngineProcessException {
    StringBuffer serviceParamsBuf = new StringBuffer();
    serviceParamsBuf.append("&text=");
    String modifiedText = cleanText(aJCas);
    serviceParamsBuf.append(modifiedText);
    this.serviceParams += (serviceParamsBuf.toString());
  }
}
