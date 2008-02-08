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
package org.apache.uima.examples.tagger.test;

import junit.framework.TestCase;
import java.util.*;
import java.util.Map.Entry;

import org.apache.uima.examples.tagger.HMMTagger;
import org.apache.uima.examples.tagger.Viterbi;


public class TaggerTest extends TestCase {

  private HMMTagger hmm;

  private List<String> sent; // sentence

  private List<String> gold_standard;

  private List<String> tagger_output;

  /**
   * Set up the test fixture
   */

  protected void setUp() {

    hmm = new HMMTagger();
    gold_standard = new ArrayList<String>();
    sent = new ArrayList<String>();
    tagger_output = new ArrayList<String>();

  }

  /**
   * Tests tagging for German.
   * 
   */
  @SuppressWarnings("unchecked")
  public void testGermanTagger() {

    System.out.println("Tesing German Model... ");
    List POS = new ArrayList();

    try {
      hmm.my_model = HMMTagger.get_model("resources/german/TuebaModel.dat");
    } catch (Exception e) {
      System.out.println("Model which is supposed to be used for testing does not exist");
    }
    System.out.println(hmm.my_model.word_probs.size() + " distinct words in the model");

    Iterator<Entry<String, Map<String, Double>>> keyValuePairs = hmm.my_model.word_probs.entrySet()
            .iterator(); // iterate over words

    for (int i = 0; i < hmm.my_model.word_probs.size(); i++) {
      Map.Entry<String, Map<String, Double>> entry = (Map.Entry<String, Map<String, Double>>) keyValuePairs
              .next();
      Object key = entry.getKey();
      Map<String, Double> pos = (Map) hmm.my_model.word_probs.get(key); // map of possible pos-s of
                                                                        // the word
      Object[] pos_s = pos.entrySet().toArray(); // for iteration over possible pos_s

      for (int u = 0; u < pos_s.length; u++) {

        Map.Entry<String, Map<String, Double>> entry2 = (Map.Entry<String, Map<String, Double>>) pos_s[u];
        Object key2 = entry2.getKey(); // pos of a word
        if (POS.contains(key2)) {
          continue;

        } else {
          POS.add(key2);
        }
      }

    }
    Collections.sort(POS);
    System.out.println("Number of part-of-speech tags used: " + POS.size());
    System.out.println("These are:  " + POS);

    System.out.println("Testing German trigram tagger..");

    sent.add("Jerry");
    sent.add("liebt");
    sent.add("Wansley");
    sent.add(".");

    System.out.println(sent);

    hmm.N = 3;
   // hmm.END_OF_SENT_TAG = "$.";

    String[] out = new String[] { "NE", "VVFIN", "NE", "$." };
    gold_standard.addAll(Arrays.asList(out));
    tagger_output = Viterbi.process(hmm.N, sent, "$.", hmm.my_model.suffix_tree,
            hmm.my_model.suffix_tree_capitalized, hmm.my_model.transition_probs,
            hmm.my_model.word_probs, hmm.my_model.lambdas2, hmm.my_model.lambdas3,
            hmm.my_model.theta);
    System.out.println("expected: " + gold_standard);
    System.out.println("tagger output: " + tagger_output);
    assertEquals(gold_standard, tagger_output);
    System.out.println("Very Good!");
    System.out.println("==========================================================");
  }

  /**
   * Tests English trigram tagger
   * 
   */
  @SuppressWarnings("unchecked")
  public void testEnglishTagger() {

    System.out.println("Tesing English Model... ");
    List POS = new ArrayList();

    try {
      hmm.my_model = HMMTagger.get_model("resources/english/BrownModel.dat");
    } catch (Exception e) {
      System.out.println("Model which is supposed to be used for testing does not exist");
    }
    System.out.println(hmm.my_model.word_probs.size() + " distinct words in the model");

    Iterator<Entry<String, Map<String, Double>>> keyValuePairs = hmm.my_model.word_probs.entrySet()
            .iterator(); // iterate over words

    for (int i = 0; i < hmm.my_model.word_probs.size(); i++) {
      Map.Entry<String, Map<String, Double>> entry = (Map.Entry<String, Map<String, Double>>) keyValuePairs
              .next();
      Object key = entry.getKey();
      Map<String, Double> pos = (Map) hmm.my_model.word_probs.get(key); // map of possible pos-s of
                                                                        // the word
      Object[] pos_s = pos.entrySet().toArray(); // for iteration over possible pos_s

      for (int u = 0; u < pos_s.length; u++) {

        Map.Entry<String, Map<String, Double>> entry2 = (Map.Entry<String, Map<String, Double>>) pos_s[u];
        Object key2 = entry2.getKey(); // pos of a word
        if (POS.contains(key2)) {
          continue;

        } else {
          POS.add(key2);
        }
      }

    }
    Collections.sort(POS);
    System.out.println("Number of part-of-speech tags used: " + POS.size());
    System.out.println("These are:  " + POS);

    System.out.println("Testing English trigram tagger...");

    sent.add("Jerry");
    sent.add("loves");
    sent.add("Wansley");
    sent.add(".");

    System.out.println(sent);

    hmm.N = 3;
 //   hmm.END_OF_SENT_TAG = "$.";

    String[] out = new String[] { "np", "vbz", "np", "." };
    gold_standard.addAll(Arrays.asList(out));
    tagger_output = Viterbi.process(hmm.N, sent, ".", hmm.my_model.suffix_tree,
            hmm.my_model.suffix_tree_capitalized, hmm.my_model.transition_probs,
            hmm.my_model.word_probs, hmm.my_model.lambdas2, hmm.my_model.lambdas3,
            hmm.my_model.theta);
    System.out.println("expected: " + gold_standard);
    System.out.println("tagger output: " + tagger_output);
    assertEquals(gold_standard, tagger_output);
    System.out.println("Very Good!");
  }

}
