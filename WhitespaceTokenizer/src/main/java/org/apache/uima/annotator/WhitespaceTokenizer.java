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
package org.apache.uima.annotator;

import java.util.Arrays;
import java.util.List;

import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.analysis_engine.annotator.AnnotatorConfigurationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorContextException;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.analysis_engine.annotator.JTextAnnotator_ImplBase;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class WhitespaceTokenizer extends JTextAnnotator_ImplBase {

  private static final int CH_SPECIAL = 0;

  private static final int CH_NUMBER = 1;

  private static final int CH_LETTER = 2;

  private static final int CH_WHITESPACE = 4;

  private static final int CH_PUNCTUATION = 5;

  private static final int CH_NEWLINE = 6;

  private static final int UNDEFINED = -1;

  private static final int INVALID_CHAR = 0;

  private static final String TOKEN_ANNOTATION_NAME = "org.apache.uima.TokenAnnotation";

  private static final String SENTENCE_ANNOTATION_NAME = "org.apache.uima.SentenceAnnotation";

  private Type tokenType;

  private Type sentenceType;

  private CAS cas = null;

  private Logger logger;

  private static List punctuations = Arrays.asList(new String[] { ".", "!", "?" });

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.analysis_engine.annotator.JTextAnnotator#process(org.apache.uima.jcas.impl.JCas,
   *      org.apache.uima.analysis_engine.ResultSpecification)
   */
  public void process(JCas aJCas, ResultSpecification aResultSpec) throws AnnotatorProcessException {
    this.logger.log(Level.INFO, "Whitespace tokenizer starts processing");

    // get text content from teh CAS
    char[] textContent = aJCas.getCas().getDocumentText().toCharArray();
    this.cas = aJCas.getCas();
    int tokenStart = UNDEFINED;
    int currentCharPos = 0;
    int sentenceStart = 0;
    int nextCharType = UNDEFINED;
    char nextChar = INVALID_CHAR;

    while (currentCharPos < textContent.length) {
      char currentChar = textContent[currentCharPos];
      int currentCharType = getCharacterType(currentChar);

      //get character class for current and next character
      if ((currentCharPos + 1) < textContent.length) {
        nextChar = textContent[currentCharPos + 1];
        nextCharType = getCharacterType(nextChar);
      } else {
        nextCharType = UNDEFINED;
        nextChar = INVALID_CHAR;
      }

      // check if current character is a letter or number
      if (currentCharType == CH_LETTER || currentCharType == CH_NUMBER) {

        // check if it is the frist letter of a token
        if (tokenStart == UNDEFINED) {
          // start new token here
          tokenStart = currentCharPos;
        }
      }

      // check if current character is a whitespace character
      else if (currentCharType == CH_WHITESPACE) {

        // terminate current token
        if (tokenStart != UNDEFINED) {
          // end of current word
          createAnnotation(this.tokenType, tokenStart, currentCharPos);
          tokenStart = UNDEFINED;
        }
      }

      // check if current character is a special character
      else if (currentCharType == CH_SPECIAL) {

        // terminate current token
        if (tokenStart != UNDEFINED) {
          // end of current word
          createAnnotation(this.tokenType, tokenStart, currentCharPos);
          tokenStart = UNDEFINED;
        }

        // create token for special character
        createAnnotation(this.tokenType, currentCharPos, currentCharPos + 1);
      }

      // check if current character is new line character
      else if (currentCharType == CH_NEWLINE) {
        // terminate current token
        if (tokenStart != UNDEFINED) {
          // end of current word
          createAnnotation(this.tokenType, tokenStart, currentCharPos);
          tokenStart = UNDEFINED;
        } 
      }

      // check if current character is new punctuation character
      else if (currentCharType == CH_PUNCTUATION) {

        // terminates the current token
        if (tokenStart != UNDEFINED) {
          createAnnotation(this.tokenType, tokenStart, currentCharPos);
          tokenStart = UNDEFINED;
        }

        // check next token type so see if we have a sentence end
        if (((nextCharType == CH_WHITESPACE) || (nextCharType == CH_NEWLINE))
                && (punctuations.contains(new String(new char[] { currentChar })))) {
          // terminate sentence
          createAnnotation(this.sentenceType, sentenceStart, currentCharPos + 1);
          sentenceStart = currentCharPos + 1;
        }
        // create token for punctuation character
        createAnnotation(this.tokenType, currentCharPos, currentCharPos + 1);
      }
      // go to the next token
      currentCharPos++;
    } //end of character loop

    // we are at the end of the text terminate open token annotations
    if (tokenStart != UNDEFINED) {
      // end of current word
      createAnnotation(this.tokenType, tokenStart, currentCharPos);
      tokenStart = UNDEFINED;
    }

    // we are at the end of the text terminate open sentence annotations
    if (sentenceStart != UNDEFINED) {
      // end of current word
      createAnnotation(this.sentenceType, sentenceStart, currentCharPos);
      sentenceStart = UNDEFINED;
    }

    this.logger.log(Level.INFO, "Whitespace tokenizer finished processing");
  }

  /**
   * create an annoation of the given type in the CAS using startPos and endPos.
   * 
   * @param annotationType annotation type
   * @param startPos annotation start position
   * @param endPos annotation end position
   */
  private void createAnnotation(Type annotationType, int startPos, int endPos) {

    AnnotationFS annot = this.cas.createAnnotation(annotationType, startPos, endPos);
    this.cas.addFsToIndexes(annot);
  }

  /**
   * returns the character type of the given character. Possible character classes are:
   * CH_LETTER for all letters
   * CH_NUMBER for all numbers
   * CH_WHITESPACE for all whitespace characters
   * CH_PUNCTUATUATION for all puctuation characters
   * CH_NEWLINE for all new line characters 
   * CH_SPECIAL for all other characters that are not in any of the groups above
   * 
   * @param character aCharacter
   * 
   * @return returns the character type of the given character
   */
  private static int getCharacterType(char character) {

    switch (Character.getType(character)) {

      // letter characters
      case Character.UPPERCASE_LETTER:
      case Character.LOWERCASE_LETTER:
      case Character.TITLECASE_LETTER:
      case Character.MODIFIER_LETTER:
      case Character.OTHER_LETTER:
      case Character.NON_SPACING_MARK:
      case Character.ENCLOSING_MARK:
      case Character.COMBINING_SPACING_MARK:
      case Character.PRIVATE_USE:
      case Character.SURROGATE:
      case Character.MODIFIER_SYMBOL:
        return CH_LETTER;

        // number characters
      case Character.DECIMAL_DIGIT_NUMBER:
      case Character.LETTER_NUMBER:
      case Character.OTHER_NUMBER:
        return CH_NUMBER;

        // whitespace characters
      case Character.SPACE_SEPARATOR:
      case Character.CONNECTOR_PUNCTUATION:
        return CH_WHITESPACE;

      case Character.DASH_PUNCTUATION:
      case Character.START_PUNCTUATION:
      case Character.END_PUNCTUATION:
      case Character.OTHER_PUNCTUATION:
        return CH_PUNCTUATION;

      case Character.LINE_SEPARATOR:
      case Character.PARAGRAPH_SEPARATOR:
        return CH_NEWLINE;

      case Character.CONTROL:
        if (character == '\n' || character == '\r') {
          return CH_NEWLINE;
        } else {
          return CH_SPECIAL;
        }

      default:
        return CH_SPECIAL;
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.analysis_engine.annotator.BaseAnnotator#initialize(com.ibm.uima.analysis_engine.annotator.AnnotatorContext)
   */
  public void initialize(AnnotatorContext aContext) throws AnnotatorInitializationException,
          AnnotatorConfigurationException {

    // initialize logger
    try {
      this.logger = aContext.getLogger();
    } catch (AnnotatorContextException ex) {
      throw new AnnotatorInitializationException(ex);
    }
    
    this.logger.log(Level.INFO, "Whitespace tokenizer successfully initialized");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.uima.analysis_engine.annotator.BaseAnnotator#typeSystemInit(com.ibm.uima.cas.TypeSystem)
   */
  public void typeSystemInit(TypeSystem aTypeSystem) throws AnnotatorInitializationException,
          AnnotatorConfigurationException {

    // initialize cas token type
    this.tokenType = aTypeSystem.getType(TOKEN_ANNOTATION_NAME);

    this.sentenceType = aTypeSystem.getType(SENTENCE_ANNOTATION_NAME);
    
    this.logger.log(Level.INFO, "Whitespace tokenizer typesystem initialized");
  }

}
