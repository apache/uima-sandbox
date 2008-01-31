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



// 

package org.apache.uima.examples.tagger;

import java.util.List;

/**
 * known implementations: - HMMTagger using Viterbi algorithm 
 * to compute the most probable path of parts of speech for a given sequence of tokens
 * @see Viterbi
 */

// AT THE MOMENT IS USELESS TODO: check if we need it at all..and integrate if :)

public interface Tagger {

  /**
   * Initiates smoothing procedure for unknown words and N-grams
   * @return true or false, depending on whether {@code smoothing}  is set in a {@code param.txt} file
   */
  public boolean set_smoothing();
  
  /**
   * Instantiates {@code MODEL} for current tagger
   */
  
  public void init(); 
  
  /**
   * Trains a new model for tagger, if a training is defined in {@code tagger.properties} file
   * @see ModelGenerator  
   */
  
  public void train();
  
  /**
   * Tags a sequence 
   * @param wordList
   */
  
  public List process(List wordList);
  
  /**
   * Tests tagging accuracy if reference corpus is available
   */
  public void test();
  
}
