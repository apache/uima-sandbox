/**
 * 	Licensed to the Apache Software Foundation (ASF) under one
 * 	or more contributor license agreements.  See the NOTICE file
 * 	distributed with this work for additional information
 * 	regarding copyright ownership.  The ASF licenses this file
 * 	to you under the Apache License, Version 2.0 (the
 * 	"License"); you may not use this file except in compliance
 * 	with the License.  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing,
 * 	software distributed under the License is distributed on an
 * 	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * 	KIND, either express or implied.  See the License for the
 * 	specific language governing permissions and limitations
 * 	under the License.
 */
package org.apache.uima.alchemy.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.alchemy.digester.domain.AnnotatedResults;
import org.apache.uima.alchemy.digester.domain.CategorizationResults;
import org.apache.uima.alchemy.digester.domain.EntitiesResults;
import org.apache.uima.alchemy.digester.domain.Entity;
import org.apache.uima.alchemy.digester.domain.Keyword;
import org.apache.uima.alchemy.digester.domain.KeywordResults;
import org.apache.uima.alchemy.digester.domain.LanguageDetectionResults;
import org.apache.uima.alchemy.digester.domain.Microformat;
import org.apache.uima.alchemy.digester.domain.MicroformatsResults;
import org.apache.uima.alchemy.digester.domain.Results;
import org.apache.uima.alchemy.ts.categorization.Category;
import org.apache.uima.alchemy.ts.entity.AlchemyAnnotation;
import org.apache.uima.alchemy.ts.keywords.KeywordFS;
import org.apache.uima.alchemy.ts.language.LanguageFS;
import org.apache.uima.alchemy.ts.microformats.MicroformatFS;
import org.apache.uima.alchemy.utils.exception.MappingException;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;

public class Alchemy2TypeSystemMapper {

  public static void mapRankedEntities(EntitiesResults results, JCas aJCas) throws MappingException {
    setLanaguage(results, aJCas);
    for (Entity entity : results.getEntities().getEntities()) {
      try {
        // use reflection to instantiate classes of the proper type in the type system
        Object fsObject;
        try {// usually jcas gen creates the constructor with jcas argument as the second one
          fsObject = Class.forName("org.apache.uima.alchemy.ts.entity." + entity.getType())
                  .getConstructors()[1].newInstance(aJCas);
        } catch (Exception e) { // for exceptional cases in which jcas parameter constructor is the
          // first
          fsObject = Class.forName("org.apache.uima.alchemy.ts.entity." + entity.getType())
                  .getConstructors()[0].newInstance(aJCas);
        }
        FeatureStructure fs = (FeatureStructure) fsObject;

        Type type = fs.getType();

        fs.setFeatureValueFromString(type.getFeatureByBaseName("count"), entity.getCount()); // count
        fs.setFeatureValueFromString(type.getFeatureByBaseName("text"), entity.getText()); // text
        fs.setFeatureValueFromString(type.getFeatureByBaseName("relevance"), entity.getRelevance()); // relevance
        if (entity.getDisambiguated() != null) {
          fs.setFeatureValueFromString(type.getFeatureByBaseName("disambiguation"), entity
                  .getDisambiguated().getName()); // disambiguation name
          fs.setFeatureValueFromString(type.getFeatureByBaseName("dbpedia"), entity
                  .getDisambiguated().getDbpedia()); // dbpedia
          fs.setFeatureValueFromString(type.getFeatureByBaseName("website"), entity
                  .getDisambiguated().getWebsite()); // website
          fs.setFeatureValueFromString(type.getFeatureByBaseName("subType"), entity
                  .getDisambiguated().getSubType()); // subtype
          fs.setFeatureValueFromString(type.getFeatureByBaseName("geo"), entity.getDisambiguated()
                  .getGeo()); // geo
          fs.setFeatureValueFromString(type.getFeatureByBaseName("opencyc"), entity
                  .getDisambiguated().getOpencyc()); // opencyc
          fs.setFeatureValueFromString(type.getFeatureByBaseName("yago"), entity.getDisambiguated()
                  .getYago()); // yago
          fs.setFeatureValueFromString(type.getFeatureByBaseName("umbel"), entity
                  .getDisambiguated().getUmbel()); // umbel
          fs.setFeatureValueFromString(type.getFeatureByBaseName("freebase"), entity
                  .getDisambiguated().getFreebase()); // freebase
          fs.setFeatureValueFromString(type.getFeatureByBaseName("ciaFactbook"), entity
                  .getDisambiguated().getCiaFactbook()); // ciaFactbook
          fs.setFeatureValueFromString(type.getFeatureByBaseName("census"), entity
                  .getDisambiguated().getCensus()); // census
          fs.setFeatureValueFromString(type.getFeatureByBaseName("geonames"), entity
                  .getDisambiguated().getGeonames()); // geonames
          fs.setFeatureValueFromString(type.getFeatureByBaseName("musicBrainz"), entity
                  .getDisambiguated().getMusicBrainz()); // musicBrainz
        }
        if (entity.getQuotations() != null && entity.getQuotations().getQuotations() != null
                && entity.getQuotations().getQuotations().size() > 0) {
          StringArray quotationsFeatureStructure = new StringArray(aJCas, entity.getQuotations()
                  .getQuotations().size());
          int i = 0;
          for (String quotation : entity.getQuotations().getQuotations()) {
            quotationsFeatureStructure.set(i, quotation);
            i++;
          }
          fs.setFeatureValue(type.getFeatureByBaseName("quotatiotans"), quotationsFeatureStructure);
        }
        aJCas.addFsToIndexes(fs);
      } catch (Exception e) {
        throw new MappingException(e);
      }
    }

  }

  private static void setLanaguage(Results results, JCas aJCas) {
    aJCas.setDocumentLanguage(results.getLanguage());
  }

  public static void mapAnnotatedEntities(AnnotatedResults results, JCas aJCas) {
    setLanaguage(results, aJCas);
    String annotatedText = results.getAnnotatedText();

    // find strings of pattern 'TYPE[TEXT'
    String[] ants = StringUtils.substringsBetween(annotatedText, "[", "]");

    // map the ants to UIMA CAS
    for (String ant : ants) {
      if (ant.indexOf("[") > 0) {
        AlchemyAnnotation alchemyAnnotation = new AlchemyAnnotation(aJCas);

        int indexOfAnt = annotatedText.indexOf(ant);
        alchemyAnnotation.setBegin(indexOfAnt - 1);

        String antText = ant.substring(ant.indexOf("[") + 1);
        alchemyAnnotation.setEnd(indexOfAnt + antText.length() - 1);

        String antType = ant.substring(0, ant.indexOf("["));
        alchemyAnnotation.setAlchemyType(antType);
        alchemyAnnotation.addToIndexes();

        annotatedText = annotatedText.replaceFirst("\\[" + ant.replace("[", "\\[") + "\\]\\]",
                antText);
      }
    }

  }

  public static void mapCategorizationEntity(CategorizationResults results, JCas aJCas)
          throws MappingException {
    setLanaguage(results, aJCas);
    try {
      FeatureStructure fs = new Category(aJCas);
      Type type = fs.getType();
      fs.setFeatureValueFromString(type.getFeatureByBaseName("score"), results.getScore());
      fs.setFeatureValueFromString(type.getFeatureByBaseName("text"), results.getCategory());
      aJCas.addFsToIndexes(fs);
    } catch (Exception e) {
      e.printStackTrace();
      throw new MappingException(e);
    }
  }

  public static void mapKeywordEntity(KeywordResults results, JCas aJCas) throws MappingException {
    setLanaguage(results, aJCas);
    for (Keyword k : results.getKeywords()) {
      try {
        KeywordFS fs = new KeywordFS(aJCas);
        Type type = fs.getType();
        fs.setFeatureValueFromString(type.getFeatureByBaseName("text"), k.getText()); // text
        fs.addToIndexes();
      } catch (Exception e) {
        throw new MappingException(e);
      }
    }
  }

  public static void mapMicroformats(MicroformatsResults results, JCas aJCas) {
    setLanaguage(results, aJCas);
    for (Microformat microformat : results.getMicroformats()) {
      MicroformatFS microformatFS = new MicroformatFS(aJCas);
      Type type = microformatFS.getType();
      microformatFS.setFeatureValueFromString(type.getFeatureByBaseName("fieldName"), microformat
              .getFieldName());
      microformatFS.setFeatureValueFromString(type.getFeatureByBaseName("fieldData"), microformat
              .getFieldData());
      microformatFS.addToIndexes();
    }

  }

  public static void mapLanguageDetection(LanguageDetectionResults results, JCas aJCas) {
    setLanaguage(results, aJCas);
    LanguageFS languageFS = new LanguageFS(aJCas);
    Type type = languageFS.getType();
    languageFS.setFeatureValueFromString(type.getFeatureByBaseName("language"), results
            .getLanguage());
    languageFS
            .setFeatureValueFromString(type.getFeatureByBaseName("iso6391"), results.getIso6391());
    languageFS
            .setFeatureValueFromString(type.getFeatureByBaseName("iso6392"), results.getIso6392());
    languageFS
            .setFeatureValueFromString(type.getFeatureByBaseName("iso6393"), results.getIso6393());
    languageFS.setFeatureValueFromString(type.getFeatureByBaseName("ethnologue"), results
            .getEthnologue());
    languageFS.setFeatureValueFromString(type.getFeatureByBaseName("nativeSpeakers"), results
            .getNativeSpeakers());
    languageFS.setFeatureValueFromString(type.getFeatureByBaseName("wikipedia"), results
            .getWikipedia());
    languageFS.addToIndexes();
  }

}
