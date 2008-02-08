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

import org.apache.uima.examples.tagger.trainAndTest.Token;

/**
 *
 */
public class GrobMapping implements MappingInterface{

	    
      /**
       * Defines mapping for List<{@link Token}>
       * E.g. if we need to map tags, given a list of {@code Tokens}, we need to map the 
       * {@code pos} field of every {@code Token} to a different {@code pos}.  
       */
	    public List map_tags(List tokens){
		  
	    	
		//    for (int i=0; i<sentences.size(); i++){ // iterate over sentences
		      
		         List<Token> tokens2 = new ArrayList<Token>(tokens.size());
		         
		         for (int x=0; x<tokens.size(); x++){ // iterate over tokens of the sentence with their corresponding POS
		           Token current_token = (Token)tokens.get(x);
		          
		           if(current_token.pos.startsWith("N")){
		        	   current_token.pos="Noun";
		           } 
		           if(current_token.pos.startsWith("V")){
		        	   current_token.pos="Verb";
		           } 
		           if(current_token.pos.startsWith("ADJ")){
		        	   current_token.pos="Adjective";
		           } 
		           if(current_token.pos.startsWith("P")){
		        	   current_token.pos="Pronoun";
		           } 
		           if(current_token.pos.startsWith("KO")){
		        	   current_token.pos="Conjunction";
		           } 
		           if(current_token.pos.startsWith("AP")){
		        	   current_token.pos="Preposition";
		           } 
		           if(current_token.pos.startsWith("PTK")){
		        	   current_token.pos="PTK";
		           } 
		           if(current_token.pos.startsWith("ADV")){
		        	   current_token.pos="Adverb";
		           } 
		           
		           if(current_token.pos.startsWith("ART")){
		        	   current_token.pos="Article";
		           } 
		           
		           if(current_token.pos.startsWith("ITJ")){
		        	   current_token.pos="Interjection";
		           } 
		           
		           Token zwischen = new Token(current_token.word, current_token.pos);
		   
		           tokens2.add(zwischen);
		         }
            return tokens2;
            
		  }
	    
	    /**
       * Defines mapping for {@code List<String>}.
       * E.g. if we need to map pos-tags given simply as {@code Strings} in a {@code List}. 
       

	     public List<String> map_pos(List<String> pos){
	       
	        
	       //    for (int i=0; i<sentences.size(); i++){ // iterate over sentences
	             
	                List<String> pos2 = new ArrayList<String>(pos.size());
	                
	                for (int x=0; x<pos.size(); x++){ 
	                  String current_pos = pos.get(x);
	                  
	                  if(current_pos.startsWith("N")){
	                    current_pos="Noun";
	                  } 
	                  if(current_pos.startsWith("V")){
	                    current_pos="Verb";
	                  } 
	                  if(current_pos.startsWith("ADJ")){
	                    current_pos="Adjective";
	                  } 
	                  if(current_pos.startsWith("P")){
	                    current_pos="Pronoun";
	                  } 
	                  if(current_pos.startsWith("KO")){
	                    current_pos="Conjunction";
	                  } 
	                  if(current_pos.startsWith("AP")){
	                    current_pos="Preposition";
	                  } 
	                  if(current_pos.startsWith("PTK")){
	                    current_pos="Particle";
	                  } 
	                  if(current_pos.startsWith("ADV")){
	                    current_pos="Adverb";
	                  } 
	                  
	                  if(current_pos.startsWith("ART")){
	                    current_pos="Article";
	                  } 
	                  
	                  if(current_pos.startsWith("ITJ")){
	                    current_pos="Interjection";
	                  } 
	                  
	             //     Token zwischen = new Token(current_token.word, current_token.pos);
	              
	                  pos2.add(current_pos);
	                }
	               return pos2;
	               
	         }
	         */
}
