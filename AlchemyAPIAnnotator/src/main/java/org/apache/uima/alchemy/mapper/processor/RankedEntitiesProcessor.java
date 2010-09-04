/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.uima.alchemy.mapper.processor;

import org.apache.uima.alchemy.digester.domain.EntitiesResults;
import org.apache.uima.alchemy.digester.domain.Entity;
import org.apache.uima.alchemy.digester.domain.Results;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;

public class RankedEntitiesProcessor implements AlchemyOutputProcessor {

  private static final String ENTITY_PACKAGE_NAME = "org.apache.uima.alchemy.ts.entity.";

  public void process(JCas cas, Results results) throws Exception {

    for (Entity entity : ((EntitiesResults) results).getEntities().getEntities()) {

      FeatureStructure fs = null;
      // get feature structure for the entity
      fs = getFeatureStructure(entity.getType(), cas);

      if (fs != null) {

        Type type = fs.getType();

        /* set each FS feature value */
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
          StringArray quotationsFeatureStructure = new StringArray(cas, entity.getQuotations()
                  .getQuotations().size());
          int i = 0;
          for (String quotation : entity.getQuotations().getQuotations()) {
            quotationsFeatureStructure.set(i, quotation);
            i++;
          }
          fs.setFeatureValue(type.getFeatureByBaseName("quotations"), quotationsFeatureStructure);
        }
        cas.addFsToIndexes(fs);
      }
    }
  }

  private static FeatureStructure getFeatureStructure(String type, JCas aJCas) {
    FeatureStructure fsObject = null;
    String typeName = new StringBuilder(ENTITY_PACKAGE_NAME).append(type).toString();
    Class<?> typeClass = getClassFromName(typeName);
    if (typeClass != null) {
      try {
        /* usually jcas gen creates the constructor with jcas argument as the second one */
        fsObject = (FeatureStructure) typeClass.getConstructors()[1].newInstance(aJCas);
      } catch (Exception e) {
        /* for exceptional cases in which jcas parameter constructor is the first */
        try {
          fsObject = (FeatureStructure) typeClass.getConstructors()[0].newInstance(aJCas);
        } catch (Exception inner) {
          /* could not instantiate a FS via reflection */
          inner.printStackTrace();
        }
      }
    }
    return fsObject;
  }

  private static Class<?> getClassFromName(String typeName) {
    Class<?> toReturn = null;
    try {
      toReturn = Class.forName(typeName);
    } catch (ClassNotFoundException cnfe) {
      // do nothing
    }

    return toReturn;
  }

}
