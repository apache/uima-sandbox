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

package org.apache.uima.examples.tagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.tagger.trainAndTest.ModelGeneration;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * UIMA Analysis Engine that invokes HMM POS tagger. HMM POS tagger generates part-of-speech tags 
 * for every token. This annotator assumes that sentences and tokens have already been annotated in the CAS
 * with Sentence and Token annotations, respectively. We iterate over sentences, then iterate over
 * tokens in the current sentence to accumulate a list of words, then invoke the HMM POS tagger on
 * the list of words. For each Token we then update the posTag field with the POS tag. The model
 * file for the HMM POS tagger is specified as a parameter (MODEL_FILE_PARAM). 
 * 
 */

public class HMMTagger extends JCasAnnotator_ImplBase implements Tagger{

  /**
   * Model file name
   */
  String MODEL;

  /**
   * for a bigram model: N = 2, for a trigram model N=3 N is defined in parameter file
   */
  public int N;

  // public String END_OF_SENT_TAG;

  public ModelGeneration my_model;
  
  MappingInterface MAPPING;
  boolean DO_MAPPING;
 
  /**
   * Initialize the Annotator.
   * 
   * @see JCasAnnotator_ImplBase#initialize(UimaContext)
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);

    try {
      // Get configuration parameter values
      String paramFile = (String) aContext.getConfigParameterValue("PARAM_FILE");

      // create and load default properties
      Properties defaultProps = new Properties();
      FileInputStream in = new FileInputStream(paramFile);
      defaultProps.load(in);
      in.close();

      MODEL = defaultProps.getProperty("MODEL");

      String n = defaultProps.getProperty("N");
      N = Integer.parseInt(n);
          
      String b =  defaultProps.getProperty("DO_MAPPING");
      DO_MAPPING = Boolean.valueOf(b);
      
      if (DO_MAPPING){
        String m = defaultProps.getProperty("MAPPING");
         MappingInterface klasse = (MappingInterface)(Class.forName(m)).newInstance();
         MAPPING = klasse;
      } else {
        MAPPING = null;
      }
       my_model = get_model(MODEL);

    } catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
  }

  /**
   * Reads a saved {@code MODEL} object from a file
   * 
   * @param filename
   *          model file
   * @return {@link ModelGeneration}
   */
  public static ModelGeneration get_model(String filename) {

    System.out.println("The used model is:" + filename);
    InputStream model = null;
    ModelGeneration oRead = null;

    try {
      model = new FileInputStream(filename);
      ObjectInputStream p = new ObjectInputStream(model);
      oRead = (ModelGeneration) p.readObject();
    }

    catch (IOException e) {
      System.err.println(e);
    } catch (ClassNotFoundException e) {
      System.err.println(e);
    } finally {
      try {
        model.close();
      } catch (Exception e) {
      }
    }
    return oRead;
  }

  /**
   * Process a CAS.
   * 
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  @SuppressWarnings("unchecked")
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    ArrayList<TokenAnnotation> tokenList = new ArrayList<TokenAnnotation>();
    ArrayList<String> wordList = new ArrayList<String>();

    AnnotationIndex sentenceIndex = aJCas.getAnnotationIndex(SentenceAnnotation.type);
    AnnotationIndex tokenIndex = aJCas.getAnnotationIndex(TokenAnnotation.type);

    // iterate over Sentences
    FSIterator sentenceIterator = sentenceIndex.iterator();

    while (sentenceIterator.hasNext()) {
      SentenceAnnotation sentence = (SentenceAnnotation) sentenceIterator.next();

      tokenList.clear();
      wordList.clear();

      FSIterator tokenIterator = tokenIndex.subiterator(sentence);
      while (tokenIterator.hasNext()) {
        TokenAnnotation token = (TokenAnnotation) tokenIterator.next();

        tokenList.add(token);
        wordList.add(token.getCoveredText());
      }

      List<String> wordTagList = Viterbi.process(N, wordList, ".",
              my_model.suffix_tree, my_model.suffix_tree_capitalized, my_model.transition_probs,
              my_model.word_probs, my_model.lambdas2, my_model.lambdas3, my_model.theta);

      
      if (MAPPING != null){
        wordTagList = MAPPING.map_tags(wordTagList);
      }
      
      try {
        for (int i = 0; i < tokenList.size(); i++) {
          TokenAnnotation token = (TokenAnnotation) tokenList.get(i);
          String posTag = (String) wordTagList.get(i);
          token.setPosTag(posTag);
        }
      } catch (IndexOutOfBoundsException e) {
        System.err.println("POS tagger error - list of tags shorter than list of words");
      }
    }
  }

}
