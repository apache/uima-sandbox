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

import java.util.ArrayList;
import java.util.List;

/**
 * just a "game"-mapping at the moment..
 */
public class MapBrownToPenn {

  public MapBrownToPenn() {
  }

  public static List<Token> map_tags(List<Token> tokens) {

    List<Token> tokens2 = new ArrayList<Token>(tokens.size());

    for (int x = 0; x < tokens.size(); x++) { // iterate over tokens of the sentence with their
                                              // corresponding POS
      Token current_token = tokens.get(x);
      String[] z = new String[2];

      // First we eliminate compound tags in the corpus //

      if (current_token.pos.contains("+")) {
        z = current_token.pos.split("[+]");
        current_token.pos = z[0];
      }
      // for cases like : BEZ*
      if (current_token.pos.contains("*") && !(current_token.pos.startsWith("*"))) {
        z[0] = current_token.pos.replace("*", "");
        current_token.pos = z[0];
      }

      // for: *-h1
      if (current_token.pos.startsWith("*")) {
        z[0] = "*";
        current_token.pos = z[0];
      }
      if (current_token.pos.contains("-") && !(current_token.pos.startsWith("--"))) {
        z = current_token.pos.split("[-]");
        current_token.pos = z[0];
      }
      if (current_token.pos.startsWith("--")) {
        z[0] = "--";
        current_token.pos = z[0];
      }

      // ******** TILL HERE ************//

      // last but not least, we map the rest of the tags to the Penn tree bank notation
      // first come straightforward mappings

      // ******** FROM HERE ************//

      if (current_token.pos.equalsIgnoreCase("od")) {
        z[0] = "jj";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("jjt")) {
        z[0] = "jjs";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("jjs")) {
        z[0] = "jj";
        current_token.pos = z[0];
      }
      if (current_token.pos.equalsIgnoreCase("*")) {
        z[0] = "rb";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("rbt")) {
        z[0] = "rbs";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("wql")) {
        z[0] = "wrb";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ql")) {
        z[0] = "rb";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("rn")) {
        z[0] = "rb";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("cs")) {
        z[0] = "in";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("dti")) {
        z[0] = "dt";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("dts")) {
        z[0] = "dt";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("abl")) {
        z[0] = "pdt";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("abn")) {
        z[0] = "pdt";
        current_token.pos = z[0];
      }

      // both ??
      if (current_token.pos.equalsIgnoreCase("abx")) {
        z[0] = "dt|cc";
        current_token.pos = z[0];
      }

      // either/neither ???

      if (current_token.pos.equalsIgnoreCase("dtx")) {
        z[0] = "dt|cc";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("at")) {
        z[0] = "dt";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ap")) {
        z[0] = "jj";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("pp$")) {
        z[0] = "prp$";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("pp$$")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("np")) {
        z[0] = "nnp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("nps")) {
        z[0] = "nnps";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("nr")) {
        z[0] = "nn";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("nrs")) {
        z[0] = "nns";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("pn")) {
        z[0] = "nn";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ppss")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("pps")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ppo")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ppl")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ppls")) {
        z[0] = "prp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("wps")) {
        z[0] = "wp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("wpo")) {
        z[0] = "wp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("dod")) {
        z[0] = "vbd";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("doz")) {
        z[0] = "vbz";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("hvd")) {
        z[0] = "vbd";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("hvg")) {
        z[0] = "vbg";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("hvn")) {
        z[0] = "vbn";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("hvz")) {
        z[0] = "vbz";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("bed")) {
        z[0] = "vbd";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("bedz")) {
        z[0] = "vbd";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("beg")) {
        z[0] = "vbg";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ben")) {
        z[0] = "vbn";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("bez")) {
        z[0] = "vbz";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("bem")) {
        z[0] = "vbp";
        current_token.pos = z[0];
      }

      if (current_token.pos.equalsIgnoreCase("ber")) {
        z[0] = "vbp";
        current_token.pos = z[0];
      }

      // ?? preposition "to" should be changed from TO to IN

      if (current_token.pos.equalsIgnoreCase("$")) {
        z[0] = "pos";
        current_token.pos = z[0];
      }

      // in Penn Treebank: extra sign for DOLLAR
      if (current_token.word.equals("$")) {
        z[1] = "$";
        current_token.pos = z[1];
      }

      // // *** Here an ***attempt**** to map some of the syntactic function differences ***, not
      // all are that easily possible////
      /*
       * if (t[0].equals("one") && tokens[x-1].startsWith("the")) { z[0] = "nn";
       * current_token.pos=z[0]; }
       */

      // ?? Verb base present form , non - infinitive
      // if (current_token.pos.equalsIgnoreCase("vb")) { // the same for "do", base non-infinitive
      // z[0] = "vbp"; // -||- "have"
      // current_token.pos=z[0];
      // }
      //
      // if (current_token.pos.equalsIgnoreCase("do")) { // infinitive be, do, have, any verb
      // z[0] = "vb";
      // current_token.pos=z[0];
      // }
      // extra symbols for list items LS and SYM for different non-identifable symbols..
      // not present in Brown Corpus
      // ******** TILL HERE ************//
      Token zwischen = new Token(current_token.word, current_token.pos);

      tokens2.add(zwischen);
    }
    return tokens2;

  }

}
