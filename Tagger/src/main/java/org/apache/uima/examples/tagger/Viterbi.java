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
import java.lang.String;



/**
 * Viterbi Algorithm:  Given a model and a sequence of observations, 
 * what is the most likely state sequence in the model that produces the observations?
 */
public class Viterbi {


  public Viterbi() {
  }
  

  @SuppressWarnings("unchecked")
  public static Map<String, List>  init_probs(String END_OF_SENT_TAG, Map<String, Double> pos_s) {
    Map<String, List> init_probs = new HashMap<String, List>();
    List init_list = new ArrayList(3);
    double p_init = 1.0; // symbolic initial probability, at the moment equal for all tags 
    
   // if (state==END_OF_SENT_TAG){
      Iterator a_pos= pos_s.entrySet().iterator();
      double p; // P(tag|preceding_tag, here it is just a symbolic initial state ".")
      List vpath_list = new ArrayList<String>();
      
      for (int h= 0; h<pos_s.size(); h++){ // for each of them
        Map.Entry entry_init = (Map.Entry) a_pos.next(); 
        String key_init = (String) entry_init.getKey(); // get the tag (state)
        Object value_init = entry_init.getValue();  // get its P(w|t)
        Double p_local = p_init; // get the 'initial' probability of this tag
        
        vpath_list.add(key_init);
        
        p = p_local*(Double)value_init; // Prob(this tag|beginning of sequence) x P(w|t)  
        
        init_list.add(p);
        init_list.add(vpath_list);
        init_list.add(p);
        init_probs.put(key_init, init_list);
      }
  //  }
   //   else {
       // TODO 
        
   //   }
    return init_probs;
  }
  
  
  /**
   * @param N
   * @param sentence
   * @param END_OF_SENT_TAG
   * @param pos_s
   * @param transition_probs
   * @param word_probs
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List process(int N, List<String> sentence, String END_OF_SENT_TAG, List<String> pos_s, Map<String, Double> transition_probs, Map<String, Map<String, Double>> word_probs) 
  {
      
   sentence.add(0, END_OF_SENT_TAG);
  
   /***********************************************************/
    
   Map<String,List> all =  new HashMap<String, List>();    // saves paths to the current token
   
    /************************************************************/
    /**                                               **/
    /************************************************************/

    for (int i=0; i<sentence.size()-1; i++){ // for every token (observation) in a sentence
      
      
      /*
       * Get initial probabilities for the first token in a sentence, at the moment we just make them all equal 1.0 through the init_probs() function  
       */
      Map<String, Double> available_pos = new HashMap<String, Double>();
      if (i==0) {
        
        if (word_probs.containsKey(sentence.get(1))){
          available_pos = word_probs.get(sentence.get(1)); // here we get available states of the first observation
        }
        else {
          // TODO ADD UNKNOWN WORDS HANDLER
          System.out.println("UNKNOWN");
          available_pos = word_probs.get("Apache");  // just for test purposes
        }
        all.putAll(init_probs(END_OF_SENT_TAG, available_pos));
        continue; // go over to the next token
      }
      
      String token = sentence.get(i);
      Map<String,Double> possible_pos = new HashMap<String, Double>();
      if (word_probs.containsKey(sentence.get(i))){
        possible_pos= word_probs.get(sentence.get(i)); // get possible POS of the current word, otherwise iterate over all, or limited set
        } else {
         possible_pos = word_probs.get("Apache"); // just for testing
        }
    
      System.out.println("current word:   "+token);
      System.out.println("available POSs of the current token: " +possible_pos);
  //    Iterator keyValuePairs = possible_pos.entrySet().iterator();
       
      Map<String,List> m =  new HashMap<String, List>();    // saves paths to the next token
      
        /************************************************************/
        /**       INDUCTION (~forward algorithm)                                             **/
        /************************************************************/
        /** 
         * At INDUCTION step the algorithm looks at the total and local possibilities of the next state(=tag), given a current observation (=token)
         * If a token is known, we limit the coming tags by only possible ones for this token
         */
        
      Map<String,Double> possible_pos_next = new HashMap<String, Double>(); // possible pos of the next token
        if (word_probs.containsKey(sentence.get(i+1))){  // if the next token is known
          System.out.println("next token is: "+sentence.get(i+1)+" and it is in our vocabulary");
          possible_pos_next = word_probs.get(sentence.get(i+1)); // get possible POS of the next word, otherwise iterate over all, or limited set
         } 
        else {
          // TODO ADD UNKNOWN WORDS HANDLER
          System.err.println("UNKNOWN NEXT WORD");
          possible_pos_next = word_probs.get("Apache"); // just for test purposes
        }   
        
        Iterator keyValuePairs_next = possible_pos_next.entrySet().iterator(); 
          for (int u = 0; u < possible_pos_next.size(); u++) // for every possible tag of the next token, if the token is known.. otherwise - go through all available POSs
          {
            
            double total_prob = 0.0; // just for fun, for forward algorithm
            List max_viterbi_path = new ArrayList<String>(); // viterbi path
            double max_viterbi_prob = 0.0; // viterbi probability
           
            Map.Entry entry_next = (Map.Entry) keyValuePairs_next.next();
            String key_next = (String) entry_next.getKey();
            Double value_next = (Double)entry_next.getValue(); // get P(next_token|this_next_state) or P(w_i+1|t_i+i)
            
       //     System.out.println("next possible pos of the next word:  "+key_next);
            
            
            /*
             * calculates for the given next token which of the possible paths to the current token are more possible to end in the next token
             */
           Iterator keyValuePairs = possible_pos.entrySet().iterator();
           for (int y = 0; y < possible_pos.size(); y++) // for every possible tag of this token (path till now)
            {
              Map.Entry entry = (Map.Entry) keyValuePairs.next();
              Object key = entry.getKey(); // get possible tag for the current token
              System.out.println("key current:  "+key);
              System.out.println("key next :  "+key_next);
              Double value = (Double)entry.getValue(); // get its P(w|t)
              
              // 
              List probs = all.get(key);
              Double prob_local = (Double) probs.get(0); // total_probability for forward algorihtm
              List path_local = (List) probs.get(1); // path to that token
              
              
           //   int s = path_local.size();
              Double vprob_local = (Double)probs.get(2);  // get viterbi probability for this token
              String ngram = null;
        
              /*
               * The following 4 lines can be modified to get any other number of N-grams 
               */
              
              /*   -- from here --  viterbi_path=viterbi_path.subList(0, path_local.size() */
             
              
              if (N == 3 && i==1) {ngram = path_local.get(path_local.size()-1)+"_"+key_next;  System.out.println("ngram"+ngram); }  // um den ersten trigramm abzufangen
              else if (N==2){ngram = path_local.get(path_local.size()-1)+"_"+key_next;  System.out.println("ngram"+ngram);}  
              else if (N==3 && i !=1) { ngram = path_local.get(path_local.size()-2)+"_"+path_local.get(path_local.size()-1)+"_"+key_next;}
              else { System.err.println("at the moment only bi-and trigramms are supported"); }
              
              /*  -- till here -- */
  
              double pp=0;
           //   System.out.println(transition_probs.containsKey(ngram));
              if (transition_probs.containsKey(ngram)) {
                pp = value_next*transition_probs.get(ngram);  //// P(t2|t1) ||  use logs because of small numbers: log(pq) = log(p) + log(q); if model parameters are stored logged then only addition is performed at runtime
        //        System.out.println("pp:  "+pp);
              } else { 
                System.out.println("unknown ngram");
                
                // TODO add unknown ngram handler
                pp = 0.001; // vorläufig, zum testen
      //          System.out.println("pp:  "+pp);
              }
              prob_local *= pp;
              vprob_local *=pp;
              total_prob += prob_local;
      //        System.out.println("P("+sentence.get(i)+"|"+key_next+")*P("+ngram+")="+vprob_local);
              if(vprob_local>max_viterbi_prob){  // HIER ENTSCHEIDET WELCHE von den möglichen states wird übernehmen
                  max_viterbi_prob = vprob_local;
  
                  max_viterbi_path=new ArrayList(path_local);
                  max_viterbi_path.add(key_next);
                  System.out.println("max viterbi path  :  "+max_viterbi_path);
              }
            }
           
           List v = new ArrayList(3);
            v.add(total_prob);
            v.add(max_viterbi_path);
            v.add(max_viterbi_prob);
            m.put(key_next, v); // m contains total of the probabilities ending in this step,
            // i.e. for every possible pos of the next token we store all the possible paths to it
            
          }
           System.out.println("m:  "+m);
           System.out.println("all:  "+all);
           all = m;
           System.out.println("all:  "+all);
     }   
  

   
    /*********************************************************/
    /*  Best sequence identification / Termination / Backtracking
    /*********************************************************/
    double total_prob = 0.0;  // the overall probability of this sequence of observations, answered by a forward algorithm (braucht man eigentlich nich unbedingt?)
    List max_viterbi_path = new ArrayList<String>(); // viterbi path
    double max_viterbi_prob = 0.0; // viterbi probability
    
    Iterator keyValuePairs_all = all.entrySet().iterator(); // iterate over them
    
    for (int j = 0; j < all.size(); j++) // for all computed possible paths, chosse the one with the maximum local probability
    {
      Map.Entry entry = (Map.Entry) keyValuePairs_all.next();
  //    Object key = entry.getKey();
      ArrayList value = (ArrayList)entry.getValue();
   
      Double total_local = (Double) value.get(0);
      List path_local = (List)value.get(1);
      Double vprob_local = (Double)value.get(2);
      
      total_prob += total_local;
  //    System.out.println("P("+token+"|"+key+")*P("+bigram+")="+p);
      if(vprob_local>max_viterbi_prob){
          max_viterbi_path=path_local;
          max_viterbi_prob = vprob_local;
      }
     
   } 
    List result = new ArrayList(3);
    result.add(total_prob);
    System.out.println("total_prob of this path"+total_prob);
    result.add(max_viterbi_path);
   
    result.add(max_viterbi_prob);
    System.out.println("max viterbi probability of this path"+max_viterbi_prob);
    System.out.println("path: "+max_viterbi_path);
    return max_viterbi_path;
   }
 }
      

  

  
