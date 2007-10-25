/*
 *Licensed to the Apache Software Foundation (ASF) under one
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
 * 
 */
package org.apache.uima.examples.tagger;

import java.io.*;
import java.util.*;


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

public class HmmTaggerTest{
  
  
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
  
  /**
   * Model used for current tagging
   */
 
  ModelGeneration my_model;
  
  /**
   * Construct a HMM tagger with default parameter settings.
   * The defaults are ... TO_DO
   */
  
  public HmmTaggerTest() {
  }

  /**
   * @param text
   * 
   */
  public HmmTaggerTest(List<String> text) {
    this.read_params();
    System.out.println(MODEL);
    System.out.println(TRAINING);
    System.out.println(T_CORPUS);
    System.out.println(SMOOTHING);
    System.out.println(N);
    this.init();
  }
  
  
  
  /**
  * Reads parameters for tagging from a parameter file
  */
 public void read_params(){
    System.out.println("Reading parameters from the file");
    // InputStream params = null; 
    String line = null;
 
     try
     {    
         BufferedReader in = new BufferedReader(new FileReader("params.txt"));
        
         while ((line=in.readLine()) != null){
           if (line.trim().length() > 0){
           
               if (line.startsWith("MODEL")) {
                  int s = line.indexOf("'");
                 int e = line.lastIndexOf("'");
                 MODEL = line.substring(s+1, e); 
                 continue;
                 }
               
               if (line.startsWith("TRAINING")) { 
                 
                 int s = line.indexOf("'");
                 int e = line.lastIndexOf("'");
                 String t =line.substring(s+1, e); 
                 if (String.valueOf(TRAINING) !=t) {
                 TRAINING = true ; 
                 continue;
                 }
               }
               if (line.startsWith("T_CORPUS")) { 
                 int s = line.indexOf("'");
                 int e = line.lastIndexOf("'");
                 T_CORPUS = line.substring(s+1, e); 
                 continue;
                }
               
               if (line.startsWith("N")) {
                 System.out.println(line);
                 int s = line.indexOf("'");
                 int e = line.lastIndexOf("'");
                 String n = line.substring(s+1, e);
                 N = Integer.parseInt(n);
                }
           }
               else {
                 continue;
               }
       //    }
         }
         in.close();
     }
     catch (IOException e)
     {
         System.out.println(e);
     }
   }
   
  
  /**
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
 //     System.out.println(oRead.InputDir);
 //     System.out.println(oRead.OutputFile);
 //     System.out.println(oRead.N);
  //    System.out.println(oRead.word_probs);
 //     System.out.println(oRead.transition_probs);
  
    } 
     
    catch ( IOException e ) { System.err.println( e ); } 
    catch ( ClassNotFoundException e ) { System.err.println( e ); } 
    finally { try {model.close(); } catch ( Exception e ) { } }
    return oRead;
  }
  
  /**
   * Initiates the model used for current tagging.
   * If {@link #TRAINING} is defined, then trains a new Model from a reference corpus;
   * otherwise reads a ready model from {@link #MODEL} defined in parameter file   
   */
  
  public void init(){
    if (TRAINING) {train();} 
    else my_model = get_model(MODEL);
  }
  
 
 /**
  * Trains a new model {@link #N} - gram probabilistic model from a predefined training corpus ({@code #T_CORPUS}),
  * write a new model to the {@link #MODEL} - file
  */
  
  public void train() {
      my_model = new ModelGeneration(N, T_CORPUS, MODEL);
   }
    
  /**
   * Tests tagging results
   */
   
  public void test(){
     
  }
  
  public void process() {
    // TODO Auto-generated method stub
    
  }

  public boolean set_smoothing() {
    // TODO Auto-generated method stub
    return false;
  }
  
  public static double log2(double d){
    return  Math.log(d)/Math.log(2.0);
  }
  

  
  /**
   * @param args
   */
  public static void main(String[] args) {
  
    List<String> sent = new ArrayList<String>();
    
    sent.add("Clinton");     
    sent.add("loves");
    sent.add("Wansley");
    sent.add(".");
 
    System.out.println(sent);   
    HmmTaggerTest hmm = new HmmTaggerTest(sent);
    
    System.out.println("going over to Viterbi");
  
    List sent_tagging = Viterbi.process(hmm.N, sent,"." ,  null , hmm.my_model.transition_probs, hmm.my_model.word_probs);
    System.out.println(sent_tagging);

    
    /**
     * TEST
    

    System.out.println("IN_DT_IN =   "+hmm.my_model.transition_probs.get("in_dt_in"));
    System.out.println(-4/2*log2(2048/2)-2*log2(1024));
   
    
    double log_test = -86/143*log2(86/143)-10/143*log2(10/143)-45/143*log2(45/143)-2/143*log2(2/143); 
    System.out.println(log_test); */
  }

 



}

  

  
