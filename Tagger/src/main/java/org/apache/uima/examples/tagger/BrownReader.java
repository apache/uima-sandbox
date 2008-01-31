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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 */
public class BrownReader {

  static boolean MAPPING = true;

  List<Token> corpus;

  String InputDir;

  public BrownReader(String InputDir) {
    this.InputDir = InputDir;
    if (MAPPING) {
      this.corpus = TagMapping.map_tags(read_corpus(read_dir(InputDir))); // in case we need to map
                                                                      // tags, TODO: trasfer to
                                                                      // parameter file
    } else {
      this.corpus = read_corpus(read_dir(InputDir));
    }

  }

  /**
   * Reads file names from Directory
   * 
   * @param directory
   *                name
   * @return an array of file names in the directory
   */

  public static String[] read_dir(String directory) {
    File dir = new File(directory);
    String[] list = dir.list();
    String[] new_list = dir.list();
    for (int i = 0; i < list.length; i++) {
      String dir_list = directory + "/" + list[i];
      new_list[i] = dir_list;
    }

    return new_list;
  }

  /**
   * Reads Brown Corpus from NLTK Distribution Format. Iterates over all files in the directory,
   * which are in a sentence per line format, and returns all tokens in the collection in a List of
   * Tokens {@link Token}}
   * 
   * @param files
   *                an array of file names
   * @return a list of tokens from all files
   * 
   */

  List<String> all_words = new ArrayList<String>();

  public static List<Token> read_corpus(String[] files) {

    String line;
    List<Token> text = new ArrayList<Token>();

    // simple tokenizer: match one or more spaces
    // String delimiters = " +";

    Pattern delimiters = Pattern.compile("[ ]+");
    // Split input with the pattern

    int line_count = 0;

    for (int i = 0; i < files.length; i++) {
      String file = files[i];
      try {
        BufferedReader in = new BufferedReader(new FileReader(file));

        while ((line = in.readLine()) != null) {
          if (line.trim().length() > 0) {
            line_count += 1;
            String[] tokens = delimiters.split(line);

            for (int x = 0; x < tokens.length; x++) { // iterate over tokens with their
                                                      // corresponding POS
              tokens[x] = tokens[x].replaceAll("[\\n\\t]+", "");

              // for cases in Brown corpus like "//in" :(
              if (tokens[x].startsWith("//")) {
                String t = tokens[x].replace("//", "per/");
                tokens[x] = t;
              }

              // and that was not all, further for cases like:
              // "before/in /l//nn and/cc AAb//nn or/cc /r//nn ./. " (text j in NLTK distribution)
              if (tokens[x].startsWith("/", 0)) {
                String t = tokens[x].substring(1);
                tokens[x] = t;
              }
              // for cases like : "AAb//nn" (s. above)
              if (tokens[x].contains("//")) {
                int j = tokens[x].indexOf("//");

                String t = tokens[x].substring(0, j) + tokens[x].substring(j + 1);
                tokens[x] = t;
              }

              // for cases in brown like: "lb/day/nn" (text 'J', sentence N. 8940)
              int first = tokens[x].indexOf("/");
              int last = tokens[x].lastIndexOf("/");
              if (first != last) {
                String[] zw = tokens[x].split("/");
                String t = "";
                for (int w = 0; w < zw.length - 1; w++) {

                  t = t + zw[w];
                }

                t = t + "/" + zw[zw.length - 1];
                tokens[x] = t;
              }

              String[] t = tokens[x].split("/");

              Token token = new Token(t[0], t[1]);

              text.add(token);
            }
          }
        }
        in.close();
      } catch (IOException e) {
        System.out.println(e);
        return null;
      }
    }
    System.out.println(line_count + " sentences in the corpus");

    return text;
  }
  /*
   * public static void main(String[] args) { // BrownReader b = new BrownReader("Brown_test"); }
   */
}
