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

import java.util.*;

import org.apache.uima.TokenAnnotation;

/**
 * Generates model for tagging (= <i>training procedure</i>)
 * Model contains probabilities of tokens and corresponding parts of speech:{@code P(word<sub>i</sub>|tag<sub>y</sub>) }
 * as well as probabilities of n-grams: P(tag<sub>i</sub>|tag<sub>i-1</sub>,tag<sub>i-2</sub>
 * Supported n-grams at the moment are: bigrams and trigrams 
 * 
 */
public interface ModelGenerator {
  
  /**
   *  Reads file names from directory {@code dir}
   *  @param directory name 
   *  @return a list of {@code Token}-s
   */
  

  public List<TokenAnnotation> read_directory(String dir);
  
  /**
   * Computes P(word<sub>i</sub>|tag<sub>y</sub>) and saves it in a {@code word_probs} map
   * @param tokens List of objects of type {@code Token}, containing 
   * @return Map(word<sub>i</sub>, Map((tag<sub>y</sub>, P(word<sub>i</sub>|tag<sub>y</sub>)), (tag<sub>u</sub>, P(word<sub>i</sub>|tag<sub>y</sub>)), etc) } 
   * */
  public Map<String, Map<String, Double>> get_word_probs(List<TokenAnnotation> tokens);
  
  /**
   * Computes {@code transition_probs} map
   * @param N N=2 for bigram model, N=3 for trigram model
   * @return Map(N-gram, P(tag<sub>i</sub>|tag<sub>i-1</sub>,tag<sub>i-2</sub>)
   */
  public Map<String, Double> get_transition_probs(int N);
  
  /**
   * Writes the resulting model to a binary file
   * @param Outputfilename file where the model should be written to
   *
   */
  
  public void write_to_file(String Outputfilename);
  
}
