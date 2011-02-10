package org.apache.uima.solrcas;

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

import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

/**
 * CAS Consumer to write on a Solr instance
 */
public class SolrCASConsumer extends CasAnnotator_ImplBase {

  protected SolrServer solrServer;

  private SolrMappingConfiguration mappingConfig;

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    try {
      /* get Solr type*/
      String solrInstanceTypeParam = String.valueOf(context.getConfigParameterValue("solrInstanceType"));

      assert solrInstanceTypeParam != null;


      /* get Solr Path */
      String solrPathParam = String.valueOf(context.getConfigParameterValue("solrPath"));

      assert solrPathParam != null;

      this.solrServer = createServer(solrInstanceTypeParam, solrPathParam);

      assert solrServer != null;

      /* read configuration */
      FieldMappingReader fieldMappingReader = new FieldMappingReader();
      String mappingFileParam = String.valueOf(context.getConfigParameterValue("mappingFile"));
      this.mappingConfig = fieldMappingReader.getConf(mappingFileParam);

    } catch (Exception e) {
      context.getLogger().log(Level.SEVERE, e.toString());
      throw new ResourceInitializationException(e);
    }
    super.initialize(context);
  }

  protected SolrServer createServer(String solrInstanceTypeParam, String solrPathParam) throws Exception {
    SolrServer solrServer = null;
    if (solrInstanceTypeParam.equalsIgnoreCase("http")) {
      URL solrURL = URI.create(solrPathParam).toURL();
      solrServer = new CommonsHttpSolrServer(solrURL);
    } else if (solrInstanceTypeParam.equals("embedded")) {
      URL solrURL;
      if (solrPathParam.startsWith("classpath:")) {
        solrPathParam = solrPathParam.replaceFirst("classpath:", "");
        solrURL = this.getClass().getResource(solrPathParam);
      } else {
        solrURL = URI.create(solrPathParam).toURL();
      }
      System.setProperty("solr.solr.home", solrURL.getFile());
      CoreContainer.Initializer initializer = new CoreContainer.Initializer();
      CoreContainer coreContainer = initializer.initialize();
      solrServer = new EmbeddedSolrServer(coreContainer, "");
    }
    return solrServer;
  }

  public void process(CAS cas) throws AnalysisEngineProcessException {
	  
      SolrInputDocument document = new SolrInputDocument();
      if (mappingConfig.getCasMapping()!=null && mappingConfig.getCasMapping().length()>0)
        document.addField(mappingConfig.getCasMapping(), cas.toString());
      if (mappingConfig.getDocumentTextMapping()!=null && mappingConfig.getDocumentTextMapping().length()>0)
        document.addField(mappingConfig.getDocumentTextMapping(), cas.getDocumentText());
      if (mappingConfig.getDocumentLanguageMapping()!=null && mappingConfig.getDocumentLanguageMapping().length()>0)
        document.addField(mappingConfig.getDocumentLanguageMapping(), cas.getDocumentLanguage());
      for (String key : mappingConfig.getFeatureStructuresMapping().keySet()) {
        Type type = cas.getTypeSystem().getType(key);
        
        for (FSIterator<FeatureStructure> iterator = cas.getIndexRepository().getAllIndexedFS(type); iterator
                .hasNext();) {
          FeatureStructure fs = iterator.next();
          Map<String, String> stringStringMap = mappingConfig.getFeatureStructuresMapping().get(key);
          
          for (String featureName : stringStringMap.keySet()) {
        	  
            String fieldName = stringStringMap.get(featureName);

            String featureValue;
            
            if (fs instanceof AnnotationFS && "coveredText".equals(featureName)) {
              featureValue = ((AnnotationFS) fs).getCoveredText();
            } else {
              Feature feature = type.getFeatureByBaseName(featureName);
              featureValue = fs.getFeatureValueAsString(feature);
            }
            
            document.addField(fieldName, featureValue);
          }
        }
      }
      
      try {
        solrServer.add(document);
        solrServer.commit();
	  } catch (Exception e) {
	    throw new AnalysisEngineProcessException(e);
	  }
  }
}
