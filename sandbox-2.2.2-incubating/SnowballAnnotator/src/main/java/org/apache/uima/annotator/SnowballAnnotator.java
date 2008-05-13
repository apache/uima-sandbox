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

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.analysis_engine.annotator.AnnotatorConfigurationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.analysis_engine.annotator.JTextAnnotator_ImplBase;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.Language;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.danishStemmer;
import org.tartarus.snowball.ext.dutchStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.finnishStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.hungarianStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import org.tartarus.snowball.ext.norwegianStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;
import org.tartarus.snowball.ext.russianStemmer;
import org.tartarus.snowball.ext.spanishStemmer;
import org.tartarus.snowball.ext.swedishStemmer;

public class SnowballAnnotator extends JTextAnnotator_ImplBase {

  private static final String TOKEN_ANNOTATION_NAME = "org.apache.uima.TokenAnnotation";

  private static final String TOKEN_ANNOTATION_STEM_FEATURE_NAME = "stem";

  private Type tokenAnnotation;

  private Feature tokenAnnotationStemmFeature;

  private Logger logger;

  private HashMap stemmers;

  private static final Object[] emptyArgs = new Object[0];

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.analysis_engine.annotator.JTextAnnotator#process(org.apache.uima.jcas.impl.JCas,
   *      org.apache.uima.analysis_engine.ResultSpecification)
   */
  public void process(JCas aJCas, ResultSpecification aResultSpec) throws AnnotatorProcessException {

    this.logger.log(Level.INFO, "Snowball annotator starts processing");

    // get get stemmer for the document language
    String language = new Language(aJCas.getDocumentLanguage()).getLanguagePart();
    SnowballProgram stemmer = (SnowballProgram) this.stemmers.get(language);

    // create stemms if stemmer for the current document language is available
    if (stemmer != null) {

      // get stem() method from stemmer
      Method stemmerStemMethod;
      try {
        stemmerStemMethod = stemmer.getClass().getMethod("stem", new Class[0]);
      } catch (Exception ex) {
        throw new AnnotatorProcessException(ex);
      }

      // iterate over all token annotations and add stem if available
      FSIterator tokenIterator = aJCas.getCas().getAnnotationIndex(this.tokenAnnotation).iterator();
      while (tokenIterator.hasNext()) {
        // get token content
        AnnotationFS annot = (AnnotationFS) tokenIterator.next();
        String span = annot.getCoveredText();

        // set annotation content and call stemmer
        try {
          stemmer.setCurrent(span);
          stemmerStemMethod.invoke(stemmer, emptyArgs);
        } catch (Exception ex) {
          throw new AnnotatorProcessException(ex);
        }

        // get stemmer result and set annotation feature
        annot.setStringValue(this.tokenAnnotationStemmFeature, stemmer.getCurrent());
      }
    }
    this.logger.log(Level.INFO, "Snowball annotator processing finished");
  }

  /* (non-Javadoc)
   * @see org.apache.uima.analysis_engine.annotator.Annotator_ImplBase#initialize(org.apache.uima.analysis_engine.annotator.AnnotatorContext)
   */
  public void initialize(AnnotatorContext aContext) throws AnnotatorInitializationException,
          AnnotatorConfigurationException {

    // initialize logger
    try {
      this.logger = aContext.getLogger();

      // initialize stemmers
      this.stemmers = new HashMap();
      this.stemmers.put("da", new danishStemmer());
      this.stemmers.put("nl", new dutchStemmer());
      this.stemmers.put("en", new englishStemmer());
      this.stemmers.put("fi", new finnishStemmer());
      this.stemmers.put("fr", new frenchStemmer());
      this.stemmers.put("de", new germanStemmer());
      this.stemmers.put("hu", new hungarianStemmer());
      this.stemmers.put("it", new italianStemmer());
      this.stemmers.put("no", new norwegianStemmer());
      this.stemmers.put("pt", new portugueseStemmer());
      this.stemmers.put("ru", new russianStemmer());
      this.stemmers.put("es", new spanishStemmer());
      this.stemmers.put("sw", new swedishStemmer());
    } catch (Exception ex) {
      throw new AnnotatorInitializationException(ex);
    }

    this.logger.log(Level.INFO, "Snowball annotator successfully initialized");
  }

  /* (non-Javadoc)
   * @see org.apache.uima.analysis_engine.annotator.Annotator_ImplBase#typeSystemInit(org.apache.uima.cas.TypeSystem)
   */
  public void typeSystemInit(TypeSystem aTypeSystem) throws AnnotatorInitializationException,
          AnnotatorConfigurationException {

    // initialize cas token type
    this.tokenAnnotation = aTypeSystem.getType(TOKEN_ANNOTATION_NAME);

    this.tokenAnnotationStemmFeature = aTypeSystem.getFeatureByFullName(TOKEN_ANNOTATION_NAME
            + TypeSystem.FEATURE_SEPARATOR + TOKEN_ANNOTATION_STEM_FEATURE_NAME);

    this.logger.log(Level.INFO, "Snowball annotator typesystem initialized");
  }

}
