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

package org.apache.uima.lucas.consumer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.lucas.indexer.AnnotationDescription;
import org.apache.uima.lucas.indexer.AnnotationTokenStreamBuilder;
import org.apache.uima.lucas.indexer.DocumentBuilder;
import org.apache.uima.lucas.indexer.FieldBuilder;
import org.apache.uima.lucas.indexer.FieldDescription;
import org.apache.uima.lucas.indexer.FilterBuilder;
import org.apache.uima.lucas.indexer.IndexMappingFileReader;
import org.apache.uima.lucas.indexer.Tokenizer;
import org.apache.uima.lucas.indexer.util.MapFileReader;
import org.apache.uima.lucas.indexer.util.MultimapFileReader;
import org.apache.uima.lucas.indexer.util.PlainFileReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

/**
 * Reads CAS object and writes the particular information in fields of a Lucene index. requires a
 * mapping file
 */
public class LuceneCASIndexer extends CasConsumer_ImplBase {

  private static final Logger logger = Logger.getLogger(LuceneCASIndexer.class);

  public final static String PARAM_OUTDIR = "indexOutDir";

  public final static String PARAM_MAPPINGFILE = "mappingFile";

  public final static String PARAM_STOPWORDFILE = "stopwordFile";

  public final static String PARAM_HYPERNYMFILE = "hypernymFile";

  public final static String PARAM_TOKEN_MAPPINGFILE = "tokenMappingFile";

  public final static String PARAM_UNIQUE_INDEX = "uniqueIndex";

  public final static String PARAM_RAM_BUFFER_SIZE = "ramBufferSize";

  public final static String PARAM_COMPOUND_FILE_FORMAT = "compoundFileFormat";

  public final static String PARAM_HEADER_TYPE_NAME = "headerTypeName";

  public final static String PARAM_DOC_ID_FEATURE_NAME = "docIdFeatureName";

  private IndexWriter indexWriter;

  private Collection<FieldDescription> fieldDescriptions;

  private DocumentBuilder documentBuilder;

  private FieldBuilder fieldBuilder;

  private FilterBuilder filterBuilder;

  private AnnotationTokenStreamBuilder annotationTokenStreamBuilder;

  private Tokenizer tokenizer;

  // private String headerTypeName;
  // private String docIdFeatureName;

  /**
   * initializes the analyzer
   */
  public void initialize() throws ResourceInitializationException {

    String mappingFile = (String) getConfigParameterValue(PARAM_MAPPINGFILE);
    String stopwordFile = (String) getConfigParameterValue(PARAM_STOPWORDFILE);
    String hypernymFile = (String) getConfigParameterValue(PARAM_HYPERNYMFILE);
    String[] mappingFiles = (String[]) getConfigParameterValue(PARAM_TOKEN_MAPPINGFILE);
    Boolean uniqueIndex = (Boolean) getConfigParameterValue(PARAM_UNIQUE_INDEX);
    String outDir = (String) getConfigParameterValue(PARAM_OUTDIR);

    // headerTypeName = (String) getConfigParameterValue(PARAM_HEADER_TYPE_NAME);
    // docIdFeatureName = (String) getConfigParameterValue(PARAM_DOC_ID_FEATURE_NAME);

    Integer ramBufferSize = (Integer) getConfigParameterValue(PARAM_RAM_BUFFER_SIZE);
    Boolean compoundFileFormat = (Boolean) getConfigParameterValue(PARAM_COMPOUND_FILE_FORMAT);

    try {
      if (uniqueIndex)
        outDir += "-" + getHostName() + "-" + getPID();

      IndexMappingFileReader indexMappingFileReader = new IndexMappingFileReader();
      fieldDescriptions = indexMappingFileReader.readFieldDescriptionsFromFile(mappingFile);

      indexWriter = new IndexWriter(outDir, new StandardAnalyzer());
      if (ramBufferSize != null)
        indexWriter.setRAMBufferSizeMB(ramBufferSize);
      if (compoundFileFormat != null)
        indexWriter.setUseCompoundFile(compoundFileFormat);

      String[] stopwords = null;
      Map<String, Map<String, String>> mappings = null;
      Map<String, List<String>> hypernyms = null;

      if (stopwordFile != null) {
        PlainFileReader stopwordReader =
                new PlainFileReader(new BufferedReader(new FileReader(stopwordFile)));
        stopwords = stopwordReader.readLines();
      } else
        stopwords = new String[0];

      if (hypernymFile != null) {
        MultimapFileReader hypernymReader =
                new MultimapFileReader(new BufferedReader(new FileReader(hypernymFile)));
        hypernyms = hypernymReader.readMultimap();
      } else
        hypernyms = Collections.EMPTY_MAP;

      if (mappingFiles != null && mappingFiles.length != 0)
        mappings = readMappingFiles(mappingFiles);
      else
        mappings = Collections.EMPTY_MAP;

      annotationTokenStreamBuilder = new AnnotationTokenStreamBuilder();
      tokenizer = new Tokenizer();
      filterBuilder = new FilterBuilder(stopwords, hypernyms, mappings);
      fieldBuilder = new FieldBuilder();
      documentBuilder = new DocumentBuilder();

    } catch (Throwable e) {
      logger.error("initialize()", e);
      throw new ResourceInitializationException(e);
    }
  }

  private Map<String, Map<String, String>> readMappingFiles(String[] mappingFiles)
          throws IOException {
    Map<String, Map<String, String>> mappings = new HashMap<String, Map<String, String>>();
    for (String mappingFile : mappingFiles) {
      MapFileReader reader = new MapFileReader(new BufferedReader(new FileReader(mappingFile)));
      Map<String, String> mapping = reader.readMap();

      if (mappingFile.lastIndexOf("/") != -1)
        mappingFile = mappingFile.substring(mappingFile.lastIndexOf("/") + 1);

      mappings.put(mappingFile, mapping);
      reader.close();
    }

    return mappings;
  }

  /**
   * optimizes the index
   * 
   */
  public void optimizeIndex() {
    try {
      logger.info("optimizing the index now!");
      indexWriter.optimize();
      indexWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void destroy() {
    logger.info("destroy " + LuceneCASIndexer.class);
    optimizeIndex();
    super.destroy();
  }

  /**
   * processes the CAS
   */
  public void processCas(CAS cas) throws ResourceProcessException {
    JCas jCas = null;
    try {
      jCas = cas.getJCas();
    } catch (CASException e) {
      logger.error("processCas(CAS)", e);
    }

    /*
     * if( headerTypeName != null && docIdFeatureName != null ){ String docId = getDocID(jCas,
     * headerTypeName, docIdFeatureName); logger.info("Processing document "+ docId ); }
     */

    try {
      Collection<Field> fields = new ArrayList<Field>();
      // iterate over field descriptions from mapping file
      for (FieldDescription fieldDescription : fieldDescriptions) {
        Collection<TokenStream> tokenStreams = new ArrayList<TokenStream>();
        // iterate over annotation descriptions
        for (AnnotationDescription annotationDescription : fieldDescription
                .getAnnotationDescriptions()) {
          // create annotation token stream
          TokenStream tokenStream =
                  annotationTokenStreamBuilder.createAnnotationTokenStream(jCas,
                          annotationDescription);

          // needs (re)tokenization ?
          if (tokenizer.needsTokenization(annotationDescription))
            tokenStream = tokenizer.tokenize(tokenStream, annotationDescription);

          // wrap with filters
          tokenStream = filterBuilder.filter(tokenStream, annotationDescription);
          tokenStreams.add(tokenStream);
        }

        // create fields
        fields.addAll(fieldBuilder.createFields(tokenStreams, fieldDescription));
      }
      // create document and add to index
      Document document = documentBuilder.createDocument(fields);
      indexWriter.addDocument(document);

    } catch (Throwable e) {
      logger.error("processCas(CAS)", e); //$NON-NLS-1$
      throw new ResourceProcessException(e);
    }
  }

  protected FilterBuilder getFilterBuilder() {
    return filterBuilder;
  }

  protected Collection<FieldDescription> getFieldDescriptions() {
    return fieldDescriptions;
  }

  protected IndexWriter getIndexWriter() {
    return indexWriter;
  }

  protected String getPID() {
    String id = ManagementFactory.getRuntimeMXBean().getName();
    return id.substring(0, id.indexOf("@"));
  }

  public String getHostName() {
    InetAddress address;
    String hostName;
    try {
      address = InetAddress.getLocalHost();
      hostName = address.getHostName();
    } catch (UnknownHostException e) {
      throw new IllegalStateException(e);
    }
    return hostName;
  }

  /*
   * public String getDocID(JCas cas, String headerTypeName, String docIdFeatureName){
   * 
   * Type headerType = cas.getTypeSystem().getType(headerTypeName); if( headerType != null ){
   * FSIterator headerIterator = cas.getAnnotationIndex(headerType).iterator(); if(
   * headerIterator.hasNext() ){ FeatureStructure headerFeatureStructure = headerIterator.get();
   * Feature docIdFeature = headerType.getFeatureByBaseName(docIdFeatureName); return
   * headerFeatureStructure.getFeatureValueAsString(docIdFeature); } }
   * 
   * return null; }
   */
}
