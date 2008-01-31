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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 *  Trains an N-gram model for the tagger, iterating over the files from some predefined training directory 
 *  Writes the resulting model to a binary fileSystem
 * 
 * 
 * NB. At the moment- both bi-and trigram statistics are saved in one model file.. 
 * 
 */


public class ModelGeneration implements java.io.Serializable{

  private static final long serialVersionUID = 1L;

  public Map suffix_tree= new HashMap ( ) ; 

  public Map suffix_tree_capitalized = new HashMap();
  /**
   * Map containing {@code <word,tag>} probabilities, that is probability of a certain word given a certain tag at a time t: P(word<sub>t</sub>|tag<sub>t</sub>)) 
   * 
   */
  public Map<String, Map<String, Double>> word_probs = new HashMap<String, Map<String,Double>>();
  
  /**
   * Map containing N-gram probabilities 
   */
  
  public Map<String, Double> transition_probs = new HashMap<String, Double>() ;
  
  
  static List<String> posList = new ArrayList<String>(); 
  
  int N; // for the N-gram model 
  
  double [] lambdas2 = new double[2];
  double [] lambdas3 = new double[3];
  public double theta; // for suffix probabiliites smoothing
  
 // transient String InputDir;
  transient String OutputFile;
  transient List corpus; 
  
  /**
   * @param N N=1, 2 or 3
   * @param InputDir input directory name
   * @param OutputFile output file name
   *    MapBrownToPenn TagMapping
   */
  @SuppressWarnings("unchecked")
   public ModelGeneration(List<Token> corpus, String OutputFile) {
	  this.OutputFile = OutputFile;
	  this.corpus = corpus;	  
  }
  
  
  private void init(){
	  
	  	List<Map<String, Map<String,Double>>> l = get_word_probs(get_lexicon(corpus));
			
		this.word_probs = (Map) l.get(0);
		this.suffix_tree = (Map) l.get(1);
		this.suffix_tree_capitalized = (Map) l.get(2);
		 /*	 Map test2 = get_ngrams(2);
		 Map test3 = get_ngrams(3);
		 Map test1 = get_ngrams(1);
			 Iterator<Entry<String, Map>> it = test2.entrySet().iterator(); // iterate over words
		   
		 for (int g = 0; g<test2.size(); g++){
			 
			 Map.Entry entry = (Map.Entry) it.next();
		      ArrayList  key = (ArrayList ) entry.getKey();
			  Object value = entry.getValue();
			 System.out.println(key.toString());
			 System.out.println(value);
		 }
	   System.out.println("Number of ngrams: "+(test2.size()+test1.size()+test3.size()));
	 */  
	   System.out.println("Number of different words "+word_probs.size());
	   System.out.println("Number of non-capitalized suffixes: "+suffix_tree.size());
	   System.out.println("Number of capitalized suffixes: "+suffix_tree_capitalized.size());
		  
	   Map pos_probabilities = get_transition_probs(1);
	   this.transition_probs.putAll(pos_probabilities);
	   this.transition_probs.putAll(get_transition_probs(2));
	   this.transition_probs.putAll(get_transition_probs(3));
		
	   this.theta = get_theta(pos_probabilities);
	   System.out.println("theta= "+this.theta);
	   this.lambdas2 = calculate_lambda(2);
	   this.lambdas3 = calculate_lambda(3);
	   write_to_file(this.OutputFile);
  }

  
  /**
   * Check is the token is capitalized
   */
  static boolean capitalized(String word){
	  boolean b;
	  char first_letter = word.charAt(0);
	  char capitalized = word.toUpperCase().charAt(0);
	  if (first_letter == capitalized) { 
		  b = true;
	  } else {
		  b = false;
	  }
	  return b;
  }

  
  static int tokens_count_all_corpus = 0;
  
  /**
   * Reads sentences, extracts {@code <word, possible parts-of-speech>} frequency patterns  
   * @param corpus list containing all tokens of the training corpus of the type {@link Token}}
   * @return map containing frequency counts for {@code <word, its pos>}
   */
  static Map<String,Map<String, Double>> get_lexicon(List<Token> corpus){
    
    
    Map<String,Map<String, Double>> lexicon= new HashMap<String,Map<String, Double>>();
    
    for (int x=0; x<corpus.size(); x++){ // iterate over tokens with their corresponding POS
           
           tokens_count_all_corpus +=1;
           Token current_token = corpus.get(x);
           posList.add(current_token.pos);  // Filling of POS tags list with available in the training corpus POSs.. (TODO: admissable POSs should probably be trasmitted with parameter file..to avoid noise)
           
           if (lexicon.containsKey(current_token.word)){
             // if the token is already in a dictionary, then get its POS-s
             Map<String, Double> pos = lexicon.get(current_token.word);
           //  System.out.println(current_token.word+" "+current_token.pos);
             Double freq=pos.get(current_token.pos);
             // if a given POS is already in its values, then add its corresponding count, otherwise add a POS value with a count of 1 
             pos.put(current_token.pos, (freq == null) ? 1 : freq + 1);
             pos.put("count", pos.get("count")+1);
           } else {
             Map<String, Double> pos = new HashMap<String, Double>();
             pos.put(current_token.pos,new Double(1));
             pos.put("count", new Double(1));
             lexicon.put(current_token.word, pos);
           }
         }              

   return lexicon;  
  }
  
 /**
  * Computes {@code word_probs} using {@link #get_lexicon(List)} frequency counts for known words..  
  * TO_DO: ADD SMOOTHING FOR UNKNOWNS?? OR add smoothing directly when come across unknown.. 
  */

  static Map sm = new HashMap ( ) ; // suffix map for non-capitalized words
  static Map sm2 = new HashMap ( );  // suffix map for capitalized;
  @SuppressWarnings("unchecked")
  static List<Map<String,Map<String, Double>>> get_word_probs(Map<String,Map<String, Double>> corpus){
	
    Map<String,Map<String, Double>> word_counts=corpus;
  
    Map<String,Map<String, Double>> word_probs = new HashMap<String, Map<String,Double>>();
       
    int mapsize = word_counts.size();
    
    Iterator<Entry<String, Map<String, Double>>> keyValuePairs = word_counts.entrySet().iterator(); // iterate over words
    Map<String, Double> pos_counts = get_ngrams(1);

    for (int i = 0; i < mapsize; i++)
    {
      Map.Entry<String, Map<String, Double>> entry = (Map.Entry<String, Map<String, Double>>) keyValuePairs.next();
      Object key = entry.getKey();
	  
	  Map<String, Double> pos2= word_counts.get(key); // map of possible pos-s of the word
	  
	  Iterator<Entry<String, Double>> keyValuePairs_pos = pos2.entrySet().iterator(); // iterate over words
	  Map<String, Double> lokal = new HashMap<String, Double>();

      for (int u = 0; u < pos2.size(); u++)
      {
    	 Map.Entry<String, Double> entry_pos = (Map.Entry<String, Double>) keyValuePairs_pos.next();
    	 Object key2 = entry_pos.getKey(); // pos of a word
        
    	 if (key2 != "count") {
    	
	    	Object value2 = entry_pos.getValue(); // its count
	        double freq_pos=pos_counts.get(key2);
	        Double val2 = (Double)value2 / freq_pos; // Prob(w|t) = freq(w,t)/freq(t)
	        lokal.put((String)key2, val2); // save probability as a log2
         } else {
    	   lokal.remove("count");
         } 
      }
      // insert the word and its corresponding tags as well as their common probabilities into the words maps 
      word_probs.put((String)key, lokal);
      
      /**
       * Get suffix probabilities from the words with frequency lower than 10 in the corpus
       */
      Set local_suffixes = new HashSet();
      
      // We maintain two different suffix counts for capitalized and non-capitalized words 
      
      Set local_suffixes_capitalized = new HashSet();
     
      // here we get words with counts under 10 for suffix probabilities.. the condition can be changed upt o you..
      if (word_counts.get(key).get("count")<10){
  		
  		SuffixTree st = new SuffixTree((String) key); 
  		 
  		Iterator<Entry> kv = st.edges.entrySet().iterator();
  		
  		for (int f= 0; f<st.edges.size();f++){
  		
  	    	Map.Entry m = (Map.Entry) kv.next();
  		//	Object keys = m.getKey();
  			SuffixTree.Edge e = (SuffixTree.Edge) m.getValue();
  			
  			if (st.nodes.get(e.end_node-1).suffix_node == -1){
  				
  				String suffix = st.text.substring(e.first_char_index,e.last_char_index+1);
  				
  				if (capitalized((String)key)){
  					local_suffixes_capitalized.add(suffix);
  				} else {
  					local_suffixes.add(suffix);
  				}
  			} 
  		}
      } 
	  
       // if this word is relevant for suffix tree
     if (!local_suffixes.isEmpty() | !local_suffixes_capitalized.isEmpty()){
    	 Set endings;
    	 Map local_sm = new HashMap ( ); 
    	 if (local_suffixes.isEmpty()){
    		 endings = local_suffixes_capitalized;
    		 local_sm = sm2;
    	 } else {
    		 endings = local_suffixes;
    		 local_sm = sm;
    	 }
    	 Iterator it = endings.iterator();
      // insert or update suffix probabilities 
    	 while (it.hasNext()){
    		 Object element = it.next();
    	 
    	 //for (int u = 0; u< local_suffixes.size(); u++){
    	  Map<String, Double> etwas = new HashMap<String, Double>();

    	  if(local_sm.containsKey(element)){
        	  Map<String, Double> pos_suffix = new HashMap<String, Double>();

    		  // get map of possible pos-s of the suffix
    		  pos_suffix= (Map<String, Double>) local_sm.get(element);
    		  Iterator<Entry<String, Double>> pos_suf = pos_suffix.entrySet().iterator(); // iterate over words
  

    	      for (int k = 0; k < pos_suffix.size(); k++)
    	      {
    	        Map.Entry<String, Double> entry3 = (Map.Entry<String, Double>) pos_suf.next();
    	    
    	        Object key_pos = entry3.getKey(); // pos of a suffix
    	        Object value_pos = entry3.getValue(); // its probability count
    	    
    	        
    	        // If a given pos of a suffix is also present in the pos-s of a corresponding word
    	        // then add both up
    	        
    	        if (word_probs.get(key).containsKey(key_pos)){
    	        	Double val_suffix = (Double)value_pos + word_probs.get(key).get(key_pos); // Prob(w|t) = freq(w,t)/freq(t)
    	        	etwas.put((String)key_pos, val_suffix); 
    	        } else {
    	        	etwas.put((String)key_pos, (Double)value_pos); 
    	    	       
    	        }
    	     // add the pos of a corresponding word with its probability into the suffix map, 
    	       // (which is not yet present in the poss of the suffix) 
    	        
    	        Set smth2 = word_probs.get(key).keySet();
    	        Object [] smth =  smth2.toArray();
    	       
    	        for (int r=0; r<smth.length;r++){
    	        	if (!pos_suffix.containsKey(smth[r]) && !smth[r].equals("count")){
    	        		etwas.put((String)smth[r], pos2.get(smth[r]));
    	        	} 
    	        }    
    	      }
    	  } else {
    		  etwas.putAll(lokal);
    	} 
    	  if (local_suffixes.isEmpty()){
     		 sm2.put(element, etwas);;
     	 } else {
     		 sm.put(element, etwas);;
     	 }
    	  //local_sm.put(element, etwas);
    	 }  
      } 
	
       
    }
    List l = new ArrayList<Map>();
    l.add(word_probs); //add word probabilities
    l.add(sm); // add suffixes of non-capitalized words
    l.add(sm2); // add suffixes of capitalized words
    
    return l;
  }

  /**
   * Make LOGs out of probabilities.. there was a reason to separate it from the get_word_probs method at the initial step
   * 
   */
  
  @SuppressWarnings("unchecked")
  static Map<String,Map<String, Double>> logify_probs(Map<String,Map<String, Double>> probs){
		
	  Map<String,Map<String, Double>> logs = new HashMap<String, Map<String,Double>>();

	  Iterator<Entry<String, Map<String, Double>>> keyValuePairs = probs.entrySet().iterator(); // iterate over words
	  
	  for (int i = 0; i < probs.size(); i++)
	  {
	    Map.Entry<String, Map<String, Double>> entry = (Map.Entry<String, Map<String, Double>>) keyValuePairs.next();
	    Object key = entry.getKey();
	    Map<String, Double> poss= probs.get(key); // map of possible pos-s of the word
		  
	    Object [] pos_s = poss.entrySet().toArray(); // for iteration over possible pos_s
		    
	    for (int u = 0; u < pos_s.length; u++)
	      {
	        
	        Map.Entry entry2 = (Map.Entry) pos_s[u];
	    //    System.out.println(entry);
	        Object key2 = entry2.getKey(); // pos of a word
	        
	        Double value2 = (Double) entry2.getValue(); // its count
	        poss.put((String)key2, Math.log(value2)); // save probability as a log2
	      }
	      // insert the word and its corresponding tags as well as their common probabilities into the words maps 
	      logs.put((String)key, poss);
	      
	  }
	  return logs;
  }
	   
  
  /**
   * Computes N-gram frequencies
   * @param N
   * @return Map<String, Double> N-grams of parts-of-speech, where {@code N = 1, 2 or 3} 
   * @throws IllegalArgumentException
   */
  static Map<String, Double> get_ngrams(int N) throws IllegalArgumentException{
   
    Map<String, Double> ngrams1= new HashMap<String,Double>();
    Map<String, Double> ngrams2= new HashMap<String,Double>();
    Map<String, Double> ngrams3= new HashMap<String,Double>();
    
    if (N==1){
      for (int y=0; y<posList.size(); y++){
        Double freq=ngrams1.get(posList.get(y));
        // if a given POS is already in the Map, then add its corresponding count, otherwise add a POS value with a count of 1 
        ngrams1.put(posList.get(y), (freq == null) ? 1 : freq + 1);
      }
    }
    else if (N==2){
      for (int y=0; y<(posList.size()-1); y++){
       String s2 = posList.get(y)+"_"+posList.get(y+1);
       Double freq=ngrams2.get(s2);
       ngrams2.put(s2, (freq == null) ? 1 : freq + 1);
      }
    }
    else if (N==3){
      for (int y=0; y<(posList.size()-2); y++){
       String s3 = posList.get(y)+"_"+posList.get(y+1)+"_"+posList.get(y+2);
       Double freq=ngrams3.get(s3);
       ngrams3.put(s3, (freq == null) ? 1 : freq + 1);
      }
    } else{ 
        throw new IllegalArgumentException ("N=1, N=2 or N=3, no further N-grams are supported at the moment");
      }
    return ((N==1)? ngrams1: (N == 2) ? ngrams2 : ngrams3);
  }
 
 // @SuppressWarnings("unchecked")
  /**
  * Computes {@code transition_probs} using {@link #get_ngrams(int)} frequency counts for N-grams..  
  */
   
  static Map<String, Double> unigrams;
  static Map<String, Double> bigrams;
  static Map<String, Double> trigrams;

  static Map<String, Double> get_transition_probs(int N) throws IllegalArgumentException{
	Map<String, Double> probs1= new HashMap<String, Double>();
    Map<String, Double> probs2= new HashMap<String, Double>();
    Map<String, Double> probs3= new HashMap<String, Double>();
    unigrams = get_ngrams(1);
    bigrams = get_ngrams(2);
    trigrams = get_ngrams(3);
    
    if (N==1) {
    	Iterator keyValuePairs = unigrams.entrySet().iterator();
        for (int i = 0; i < unigrams.size(); i++) // for all bigrams
        {
          Map.Entry entry = (Map.Entry) keyValuePairs.next();
          Object key = entry.getKey(); // get a bigram
       //   String [] t = ((String)key).split("_");
          double freq1 =  unigrams.get(key); // get a count of a  unigram

    //      Object freq2 = entry.getValue(); // get  a count of a bigram
          
          double prob1 = (Double)freq1 / tokens_count_all_corpus; // Prob(key) = freq(tag)/freq(all_tags)?
          probs1.put((String)key, prob1); // save probability as a log2 : Math.log(prob1)
        
        }
    }
    
    else if (N==2){
      
      Iterator keyValuePairs = bigrams.entrySet().iterator();
      for (int i = 0; i < bigrams.size(); i++) // for all bigrams
      {
        Map.Entry entry = (Map.Entry) keyValuePairs.next();
        Object key = entry.getKey(); // get a bigram
        String [] t = ((String)key).split("_");
        double freq1 =  unigrams.get(t[0]); // get a count of a preceding unigram

        Object freq2 = entry.getValue(); // get  a count of a bigram
        
        double prob2 = (Double)freq2 / freq1; // Prob(t2|t1) = freq(t1,t2)/freq(t1)
        probs2.put((String)key, prob2); // save probability as a log2: Math.log(prob2)
      } 
    } else if (N==3){ // for trigram models
      
      Iterator keyValuePairs = trigrams.entrySet().iterator();
      for (int i = 0; i < trigrams.size(); i++) // for all trigrams
      {
        Map.Entry entry = (Map.Entry) keyValuePairs.next();
        Object key = entry.getKey(); // get a trigram
        String [] t = ((String)key).split("_");
        String tt = t[0]+"_"+t[1];

        Double freq1 =  bigrams.get(tt); // get a count of a preceding bigram

        Object freq2 = entry.getValue(); // get  a count of a trigram
        
        double prob3 = (Double)freq2/freq1; // Prob(t3|(t1_t2)) = freq(t1_t2_t3)/freq(t1_t2)
        probs3.put((String)key, prob3); // save probability as a log2: Math.log(prob3)
     }
    } else{ 
      throw new IllegalArgumentException ("only uni-, bi-, and trigramms are supported at the moment");
      } 
    return ((N == 2) ? probs2 : (N==3)? probs3 : probs1);
  }
  
  // TODO: ADD SMOOTHING, both for ngrams and evtl unknown suffixes (?)
  /**
   * Computes alphas for linear interpolation smoothing of unknown n-grams
   * @param N N-gram
   * currently lambdas are calculated as in (Brants, 2000)
   */
 
  private double [] calculate_lambda(int N){
	  double lambda1 = 0;
	  double lambda2 = 0;
	  double lambda3 = 0;
	 
	  double count2 = 0;
	  double count3 = 0;
	  
	  if (N ==2) {
		  Iterator keyValuePairs = bigrams.entrySet().iterator();
	      for (int i = 0; i < bigrams.size(); i++) // for all bigrams
	      {
	        Map.Entry entry = (Map.Entry) keyValuePairs.next();
	        Object key = entry.getKey(); // get a bigram
	        String [] t = ((String)key).split("_");
	        double freq1 =  unigrams.get(t[0]); // get a count of a preceding unigram
	        Double freq2 = (Double) entry.getValue(); // get  a count of a bigram
	        
	        double f2 = (freq2-1)/(freq1-1);
	        double f1 = (freq1-1)/(tokens_count_all_corpus-1);
	        
	        double freq = get_max(f2, f1, 0);
	        if (freq == f2){lambda2+=freq2;count2+=freq2;}
	        else {lambda1+=freq2;count2+=freq2;}
	        } lambdas2[0] = lambda1/count2; lambdas2[1]= lambda2/count2; 
	  }
	  
	  if (N == 3) {
		  
	      Iterator keyValuePairs = trigrams.entrySet().iterator();
	      for (int i = 0; i < trigrams.size(); i++) // for all trigrams
	      {
	        Map.Entry entry = (Map.Entry) keyValuePairs.next();
	        Object key = entry.getKey(); // get a trigram
	        String [] t = ((String)key).split("_");
	        String tt = t[0]+"_"+t[1];

	        Double freq2 = bigrams.containsKey(tt) ?  bigrams.get(tt) : 0.0; // get a count of a preceding bigram
	        Double freq3 = (Double) entry.getValue(); // get  a count of a trigram
	        Double freq1 = unigrams.containsKey(t[0]) ? unigrams.get(t[0]) : 0.0;
	      
	        double f3 = (freq3-1)/(freq2-1);
	        double f2 = (freq2-1)/(freq1-1);
	        double f1 = (freq1-1)/(tokens_count_all_corpus-1);
	        double freq = get_max(f3, f2, f1);
	        if (freq == f3) {lambda3+= freq3; count3+=freq3;} // or just real frequency?
	        else if (freq == f2){lambda2+=freq3; count3+=freq3;}
	        else {lambda1+=freq3;count3+=freq3;}
	      }	 lambdas3[0] = lambda1/count3; lambdas3[1]=lambda2/count3; lambdas3[2]=lambda3/count3;   
	  }
	 System.out.println("lambdas for 2-grams = "+lambdas2[0]+" "+lambdas2[1]+"\n"+"lambdas for 3-grams"+lambdas3[0]+" "+lambdas3[1]+" "+lambdas3[2]);
	    return ((N == 2) ? lambdas2 : lambdas3);
  }
  
  private double get_theta (Map<String, Double> m) {
	  double d = 0;
	  double sum1 = 0;
	  double tagset_size = m.size();
	  Iterator tags_probs = m.entrySet().iterator();
	  for (int h = 0; h<tagset_size; h++){
		  Map.Entry entry_pos = (Map.Entry) tags_probs.next();
		  // Object key_pos = entry_pos.getKey();
		  Double prob = (Double) entry_pos.getValue();
		  sum1+=prob;
	  }
	  double prob_average = sum1/tagset_size;
	  double sum2 = 0;
	  
	  Iterator tags_probs2 = m.entrySet().iterator();
	  for (int h = 0; h<tagset_size; h++){
		  Map.Entry entry_pos = (Map.Entry) tags_probs2.next();
		  Double prob = (Double) entry_pos.getValue();
		  sum2+=Math.pow((prob-prob_average),2);
	  }
	  d = sum2/(tagset_size-1);
	  
	  return d;
  }
  
  static double get_max(double a, double b, double c){
	  double max = a; 
	  if (b>max){ max = b; }
	  if (c>max) {max = c; }
	  return max;
  }
  
  /**
   * Writes the model to a binary file
   * @param filename output file name
   */
  
  private void write_to_file(String filename){
    
    File file = null; 
    if (filename!=null) { file = new File (filename);}
    // or use a default file name
    if (file == null) {
        System.out.println ("Default: model.dat");
        file = new File ("model.dat");
    }

    try {
      // Create an output stream to the file.
      FileOutputStream file_output = new FileOutputStream (file);
      ObjectOutputStream o = new ObjectOutputStream( file_output ); 
      o.writeObject(this); 
   
      file_output.close ();
    } 
    catch (IOException e) {
       System.err.println ("IO exception = " + e );
    } 
 
  }
  
  public static void main(String[] args) {
	  
   //  PennReader penn = new PennReader("F:/pos/wsj/training");
    // BrownReader brown = new BrownReader("../brown");
    TT_FormatReader tiger = new TT_FormatReader("D:/Jane/IBM/Tagger_komplett/tueba_tigerFormat.txt");
    
	
	 ModelGeneration md = new ModelGeneration(tiger.corpus, "TuebaModel.dat");
	 md.init();
  }
  
}

