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
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * UIMA Analysis Engine that invokes HMM POS tagger. HMM POS tagger generates a
 * Brown Corpus style POS tags. This annotator assumes that sentences and tokens have already been
 * annotated in the CAS with Sentence and Token annotations, respectively. We iterate over
 * sentences, then iterate over tokens in the current sentence to accumlate a list of words, then
 * invoke the HMM POS tagger on the list of words. For each Token we then update the posTag
 * field with the POS tag. The model file for the HMM POS tagger is specified as a parameter
 * (MODEL_FILE_PARAM).
 *  Implements {@link Tagger}    
 *     
 */         

public class HMMTagger extends JCasAnnotator_ImplBase{

  
  //public static final String PARAM_FILE = "tagger.properties";
  
  /**
   * Defines whether {@code TRAINING} of a new model should be done, or whether we apply a ready pre-trained model from a parameter file
   */
  boolean  TRAINING=false;
  
  /**
   * Defines whether smoothing should be applied to UNKNOWN
   */
  boolean SMOOTHING=false;
  /**
   * Model file name
   */
   String MODEL;
  
  /**
   * Training corpus file name
   */
  String T_CORPUS;
  
  /**
   * for a bigram model: N = 2, for a trigram model N=3 
   * N is defined in parameter file
   */
  int N; 
  
  String END_OF_SENT_TAG;
  
  /**
   * Model used for current tagging
   */
 
  ModelGeneration my_model;
  HMMTagger tagger;
  
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
      String train = defaultProps.getProperty("TRAINING");
      
      if (train == "true") {
        TRAINING = true ; 
      } else {TRAINING = false; }
      
      T_CORPUS = defaultProps.getProperty("T_CORPUS");
      
      String smooth = defaultProps.getProperty("SMOOTHING");
      if (smooth == "true") {
        SMOOTHING = true ; 
      } else {SMOOTHING = false;}
      
      String n = defaultProps.getProperty("N");
      N = Integer.parseInt(n);

      END_OF_SENT_TAG = defaultProps.getProperty("END_OF_SENT_TAG");
      my_model = get_model(MODEL);
    
    } catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
  }

  /**
   * Reads a saved {@code MODEL} object from a file
   * @param filename model file
   * @return {@link ModelGeneration}
   */
  public ModelGeneration get_model(String filename){
  // filename = "brown_model_trigramm.dat"; // model - trigram file
    System.out.println("The used model is:" + filename);
    InputStream model = null; 
    ModelGeneration oRead = null;
    
    try 
    { 
      model = new FileInputStream( filename ); 
      ObjectInputStream p = new ObjectInputStream( model); 
      oRead = (ModelGeneration) p.readObject();
    } 
     
    catch ( IOException e ) { System.err.println( e ); } 
    catch ( ClassNotFoundException e ) { System.err.println( e ); } 
    finally { try {model.close(); } catch ( Exception e ) { } }
    return oRead;
  }
  
  /**
   * Initiates {@code MODEL} used for current tagging.
   * If {@link #TRAINING} is defined, then trains a new Model from a reference corpus;
   * otherwise reads a ready model from {@link #MODEL} defined in parameter file   
   */
  
  public void init(){
  
  }
  
 
 /**
  * Trains a new model {@link #N} - gram probabilistic model from a predefined training corpus ({@code #T_CORPUS}),
  * write a new model to the {@link #MODEL} - file
  */
  
  public void train() {
      my_model = new ModelGeneration(N, T_CORPUS, MODEL);
   }
    
  /**
   * TODO Tests tagging results
   */
   
  public void test(){
     }
  
  public boolean set_smoothing() {
    // TODO Auto-generated method stub
    return false;
  }
  
  /*
  public static double log2(double d){
    return  Math.log(d)/Math.log(2.0);
  }
 
  
  public List process(List wordList) {
   // Viterbi v = new Viterbi();
   
    List sent_tagging = Viterbi.process(N, wordList, END_OF_SENT_TAG,  null , my_model.transition_probs, my_model.word_probs);
    System.out.println(sent_tagging);
    return sent_tagging;
   }
 */
 
  
  /**
   * Process a CAS.
   * 
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    ArrayList tokenList = new ArrayList();
    ArrayList wordList = new ArrayList();
  

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
  
    List wordTagList = Viterbi.process(N, wordList, END_OF_SENT_TAG,  null , my_model.transition_probs, my_model.word_probs);
    System.out.println(wordTagList);
    
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
