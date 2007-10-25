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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.TokenAnnotation;

/**
 *  Trains an N-gram model for the tagger, iterating over the files from some predefined training directory 
 *  Writes the resulting model to a binary file
 * 
 */


public class ModelGeneration implements java.io.Serializable, ModelGenerator{

  /**
   * @serial 
   */
  private static final long serialVersionUID = 1L;
  

  /**
   * Map containing {@code <word,tag>} probabilities, that is probability of a certain word given a certain tag at a time t: P(word<sub>t</sub>|tag<sub>t</sub>)) 
   * 
   */
  public Map<String, Map<String, Double>> word_probs = new HashMap<String, Map<String,Double>>();
  
  /**
   * Map containing N-gram probabilities 
   */
  
  public Map<String, Double> transition_probs = new HashMap<String, Double>() ;
  
  
  private  List<String> posList = new ArrayList<String>(); 
  
 int N; // for the N-gram model 
  
  String InputDir;
  String OutputFile;
  
   /*
   public ModelGeneration() {
      this(3, InputDir, OutputFile);
   }
   */
  
  /**
   * @param N N=1, 2 or 3
   * @param InputDir input directory name
   * @param OutputFile output file name
   * 
   */
  public ModelGeneration(int N, String InputDir, String OutputFile) {
    this.N = N;
    this.InputDir = InputDir;
    this.OutputFile = OutputFile;
    word_probs = get_word_probs(InputDir);
    transition_probs = get_transition_probs(2);
    transition_probs.putAll(get_transition_probs(3));
    write_to_file(OutputFile);
  }
  
  /**
   *  Reads file names from Directory
   *  @param directory name 
   *  @return an array of file names in the directory
   */
  
  public static String [] read_dir(String directory){
  File dir = new File(directory);
  String[] list = dir.list(); 
  String[] new_list = dir.list(); 
  for (int i = 0; i < list.length; i++) {
    String dir_list = directory+"/"+list[i];
    new_list[i]=dir_list;
    System.out.println(new_list[i]);
  }

  return new_list;
  }
  
  /** 
   * Reads Brown Corpus from NLTK Distribution Format. Iterates over all files in the directory, which are in a sentence per line format, 
   * and returns all sentences in the collection in a list  
   * @param files an array of file names
   * @return a list of sentences from all files
   *  
   */
    
  
  private List<String> read_corpus(String [] files){
    
   String line;
   List<String> text = new ArrayList<String>();
   
   for (int i = 0; i < files.length; i++) {
     System.out.println(files[i]);
     String file = files[i];
    try
     {    
         BufferedReader in = new BufferedReader(new FileReader(file));
        
         while ((line=in.readLine()) != null){
           if (line.trim().length() > 0){
               text.add(line);
           }
         }
         in.close();
     }
     catch (IOException e)
     {
         System.out.println(e);
         return null;
     }
   }
     return text;
  }
  
  /**
   * Reads sentences, extracts {@code <word, possible parts-of-speech>} frequency patterns  
   * @param corpus list containing all sentences of the training corpus
   * @return map containing frequency counts for {@code <word, its pos>}
   */
  private Map<String,Map<String, Double>> get_lexicon(List<String> corpus){
    
    //simple tokenizer: match one or more spaces
    String delimiters = " +";
    // 
    Map<String,Map<String, Double>> lexicon= new HashMap<String,Map<String, Double>>();
    
   
    for (int i=0; i<corpus.size(); i++){ // iterate over sentences
        
         String [] tokens = corpus.get(i).split(delimiters);
         
         for (int x=0; x<tokens.length; x++){ // iterate over tokens with their corresponding POS
           
           tokens[x] = tokens[x].replaceAll("[\\n$\\t^I]+","");
           String [] t = tokens[x].split("/");
           posList.add(t[1]);  // Filling of POS tags list with available in the training corpus POSs.. ZU UEBERLEGEN: OB DIE NICHT UEBERGEBEN WERDEN ALS PARAMETER
           
           if (lexicon.containsKey(t[0])){
             // if the token is already in a dictionary, then get its POS-s
             Map<String, Double> pos = lexicon.get(t[0]);
             Double freq=pos.get(t[1]);
             // if a given POS is already in its values, then add its corresponding count, otherwise add a POS value with a count of 1 
             pos.put(t[1], (freq == null) ? 1 : freq + 1);
           } else {
             Map<String, Double> pos = new HashMap<String, Double>();
             pos.put(t[1],new Double(1));
             lexicon.put(t[0], pos);
           }
         }              

    } System.out.println(corpus.size() + " sentences in the corpus");
    System.out.println(lexicon.get("baby"));
     return lexicon;  
  }
  
 /**
  * Computes {@code word_probs} using {@link #get_lexicon(List)} frequency counts for known words..  
  * TO_DO: ADD SMOOTHING FOR UNKNOWNS?? OR add smoothing directly when come across unknown.. 
  */
  
  @SuppressWarnings("unchecked")
  public Map<String,Map<String, Double>> get_word_probs(String filename){
  
    
    Map<String,Map<String, Double>> word_counts=get_lexicon(read_corpus(read_dir(filename)));
    Map<String, Double> pos_counts = get_ngrams(1);
  
    Map<String,Map<String, Double>> word_probs = new HashMap<String, Map<String,Double>>();
    
    int mapsize = word_counts.size();
    System.out.println(mapsize + " words in the corpus");
    
    Iterator<Entry<String, Map<String, Double>>> keyValuePairs = word_counts.entrySet().iterator(); // iterate over words
   
    for (int i = 0; i < mapsize; i++)
    {
      Map.Entry<String, Map<String, Double>> entry = (Map.Entry<String, Map<String, Double>>) keyValuePairs.next();
      Object key = entry.getKey();
  
      Map<String, Double> pos= word_counts.get(key); // map of possible pos-s of the word
      Object [] pos_s = pos.entrySet().toArray(); // for iteration over possible pos_s
      
      
      for (int u = 0; u < pos_s.length; u++)
      {
        
        Map.Entry<String, Map<String, Double>> entry2 = (Map.Entry<String, Map<String, Double>>) pos_s[u];
    //    System.out.println(entry);
        Object key2 = entry2.getKey(); // pos of a word
        Object value2 = entry2.getValue(); // its count
        double freq_pos=pos_counts.get(key2);
        Double val2 = (Double)value2 / freq_pos; // Prob(w|t) = freq(w,t)/freq(t)
        pos.put((String)key2, val2);
       System.out.println(value2+":"+freq_pos+"="+
               val2);
      }
      word_probs.put((String)key, pos);
      System.out.println(key+":"+pos);

      } 
    return word_probs;
  }

  /**
   * Computes N-gram frequencies
   * @param N
   * @return Map<String, Double> N-grams of parts-of-speech, where {@code N = 1, 2 or 3} 
   * @throws IllegalArgumentException
   */
  private Map<String, Double> get_ngrams(int N) throws IllegalArgumentException{
   
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
   
  public Map<String, Double> get_transition_probs(int N) throws IllegalArgumentException{
    
    Map<String, Double> probs2= new HashMap<String, Double>();
    Map<String, Double> probs3= new HashMap<String, Double>();
    
    if (N==2){
      
      Map<String, Double> unigrams = get_ngrams(1);
      Map<String, Double> bigrams = get_ngrams(2);
      
      Iterator keyValuePairs = bigrams.entrySet().iterator();
      for (int i = 0; i < bigrams.size(); i++) // for all bigrams
      {
        Map.Entry entry = (Map.Entry) keyValuePairs.next();
        Object key = entry.getKey(); // get a bigram
        String [] t = ((String)key).split("_");
        double freq1 =  unigrams.get(t[0]); // get a count of a preceding unigram

        Object freq2 = entry.getValue(); // get  a count of a bigram
        
        double prob2 = (Double)freq2 / freq1; // Prob(t2|t1) = freq(t1,t2)/freq(t1)
        probs2.put((String)key, prob2);
      } 
    } else if (N==3){ // for trigram models
      
      Map<String, Double> bigrams = get_ngrams(2);
      Map<String, Double> trigrams = get_ngrams(3);
      
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
        probs3.put((String)key, prob3);
     }
    } else{ 
      throw new IllegalArgumentException ("only uni-, bi-, and trigramms are supported at the moment");
      } 
    return ((N == 2) ? probs2 : probs3);
 }
  
  /**
   * Writes the model to a binary file
   * @param filename output file name
   */
  
  public void write_to_file(String filename){
    
    File file = null; 
    if (filename!=null) { file = new File (filename);}
    // or use a default file name
    if (file == null) {
        System.out.println ("Default: params.dat");
        file = new File ("params.dat");
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
  public Map<String, Map<String, Double>> get_word_probs(List<TokenAnnotation> tokens) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<TokenAnnotation> read_directory(String dir) {
    // TODO Auto-generated method stub
    return null;
  }  

  
  /**
   * ONLY FOR TEST
  
  public static void main(String[] args) {
    
   new ModelGeneration(3, "brown", "brown_model_trigramm.dat"); 
  }
*/
  
}

