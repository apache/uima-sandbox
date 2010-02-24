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
/* First created by JCasGen Mon Nov 09 23:02:37 CET 2009 */
package org.apache.uima.alchemy.ts.entity;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 * Updated by JCasGen Sun Nov 15 23:15:41 CET 2009 XML source:
 * 
 * @generated
 */
public class Movie extends TOP {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(Movie.class);

  /**
   * @generated
   * @ordered
   */
  public final static int type = typeIndexID;

  /** @generated */
  public int getTypeIndexID() {
    return typeIndexID;
  }

  /**
   * Never called. Disable default constructor
   * 
   * @generated
   */
  protected Movie() {
  }

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public Movie(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public Movie(JCas jcas) {
    super(jcas);
    readObject();
  }

  /**
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   * 
   * @generated modifiable
   */
  private void readObject() {
  }

  // *--------------*
  // * Feature: text

  /**
   * getter for text - gets
   * 
   * @generated
   */
  public String getText() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_text);
  }

  /**
   * setter for text - sets
   * 
   * @generated
   */
  public void setText(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_text, v);
  }

  // *--------------*
  // * Feature: relevance

  /**
   * getter for relevance - gets
   * 
   * @generated
   */
  public String getRelevance() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_relevance == null)
      jcasType.jcas.throwFeatMissing("relevance", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_relevance);
  }

  /**
   * setter for relevance - sets
   * 
   * @generated
   */
  public void setRelevance(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_relevance == null)
      jcasType.jcas.throwFeatMissing("relevance", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_relevance, v);
  }

  // *--------------*
  // * Feature: count

  /**
   * getter for count - gets
   * 
   * @generated
   */
  public String getCount() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_count);
  }

  /**
   * setter for count - sets
   * 
   * @generated
   */
  public void setCount(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_count, v);
  }

  // *--------------*
  // * Feature: disambiguation

  /**
   * getter for disambiguation - gets
   * 
   * @generated
   */
  public String getDisambiguation() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_disambiguation == null)
      jcasType.jcas.throwFeatMissing("disambiguation", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr,
            ((Movie_Type) jcasType).casFeatCode_disambiguation);
  }

  /**
   * setter for disambiguation - sets
   * 
   * @generated
   */
  public void setDisambiguation(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_disambiguation == null)
      jcasType.jcas.throwFeatMissing("disambiguation", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_disambiguation, v);
  }

  // *--------------*
  // * Feature: subType

  /**
   * getter for subType - gets
   * 
   * @generated
   */
  public String getSubType() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_subType == null)
      jcasType.jcas.throwFeatMissing("subType", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_subType);
  }

  /**
   * setter for subType - sets
   * 
   * @generated
   */
  public void setSubType(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_subType == null)
      jcasType.jcas.throwFeatMissing("subType", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_subType, v);
  }

  // *--------------*
  // * Feature: website

  /**
   * getter for website - gets
   * 
   * @generated
   */
  public String getWebsite() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_website == null)
      jcasType.jcas.throwFeatMissing("website", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_website);
  }

  /**
   * setter for website - sets
   * 
   * @generated
   */
  public void setWebsite(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_website == null)
      jcasType.jcas.throwFeatMissing("website", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_website, v);
  }

  // *--------------*
  // * Feature: geo

  /**
   * getter for geo - gets
   * 
   * @generated
   */
  public String getGeo() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_geo == null)
      jcasType.jcas.throwFeatMissing("geo", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_geo);
  }

  /**
   * setter for geo - sets
   * 
   * @generated
   */
  public void setGeo(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_geo == null)
      jcasType.jcas.throwFeatMissing("geo", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_geo, v);
  }

  // *--------------*
  // * Feature: dbpedia

  /**
   * getter for dbpedia - gets
   * 
   * @generated
   */
  public String getDbpedia() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_dbpedia == null)
      jcasType.jcas.throwFeatMissing("dbpedia", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_dbpedia);
  }

  /**
   * setter for dbpedia - sets
   * 
   * @generated
   */
  public void setDbpedia(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_dbpedia == null)
      jcasType.jcas.throwFeatMissing("dbpedia", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_dbpedia, v);
  }

  // *--------------*
  // * Feature: yago

  /**
   * getter for yago - gets
   * 
   * @generated
   */
  public String getYago() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_yago == null)
      jcasType.jcas.throwFeatMissing("yago", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_yago);
  }

  /**
   * setter for yago - sets
   * 
   * @generated
   */
  public void setYago(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_yago == null)
      jcasType.jcas.throwFeatMissing("yago", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_yago, v);
  }

  // *--------------*
  // * Feature: opencyc

  /**
   * getter for opencyc - gets
   * 
   * @generated
   */
  public String getOpencyc() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_opencyc == null)
      jcasType.jcas.throwFeatMissing("opencyc", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_opencyc);
  }

  /**
   * setter for opencyc - sets
   * 
   * @generated
   */
  public void setOpencyc(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_opencyc == null)
      jcasType.jcas.throwFeatMissing("opencyc", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_opencyc, v);
  }

  // *--------------*
  // * Feature: umbel

  /**
   * getter for umbel - gets
   * 
   * @generated
   */
  public String getUmbel() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_umbel == null)
      jcasType.jcas.throwFeatMissing("umbel", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_umbel);
  }

  /**
   * setter for umbel - sets
   * 
   * @generated
   */
  public void setUmbel(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_umbel == null)
      jcasType.jcas.throwFeatMissing("umbel", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_umbel, v);
  }

  // *--------------*
  // * Feature: freebase

  /**
   * getter for freebase - gets
   * 
   * @generated
   */
  public String getFreebase() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_freebase == null)
      jcasType.jcas.throwFeatMissing("freebase", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_freebase);
  }

  /**
   * setter for freebase - sets
   * 
   * @generated
   */
  public void setFreebase(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_freebase == null)
      jcasType.jcas.throwFeatMissing("freebase", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_freebase, v);
  }

  // *--------------*
  // * Feature: ciaFactbook

  /**
   * getter for ciaFactbook - gets
   * 
   * @generated
   */
  public String getCiaFactbook() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_ciaFactbook == null)
      jcasType.jcas.throwFeatMissing("ciaFactbook", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_ciaFactbook);
  }

  /**
   * setter for ciaFactbook - sets
   * 
   * @generated
   */
  public void setCiaFactbook(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_ciaFactbook == null)
      jcasType.jcas.throwFeatMissing("ciaFactbook", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_ciaFactbook, v);
  }

  // *--------------*
  // * Feature: census

  /**
   * getter for census - gets
   * 
   * @generated
   */
  public String getCensus() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_census == null)
      jcasType.jcas.throwFeatMissing("census", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_census);
  }

  /**
   * setter for census - sets
   * 
   * @generated
   */
  public void setCensus(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_census == null)
      jcasType.jcas.throwFeatMissing("census", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_census, v);
  }

  // *--------------*
  // * Feature: geonames

  /**
   * getter for geonames - gets
   * 
   * @generated
   */
  public String getGeonames() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_geonames == null)
      jcasType.jcas.throwFeatMissing("geonames", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_geonames);
  }

  /**
   * setter for geonames - sets
   * 
   * @generated
   */
  public void setGeonames(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_geonames == null)
      jcasType.jcas.throwFeatMissing("geonames", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_geonames, v);
  }

  // *--------------*
  // * Feature: musicBrainz

  /**
   * getter for musicBrainz - gets
   * 
   * @generated
   */
  public String getMusicBrainz() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_musicBrainz == null)
      jcasType.jcas.throwFeatMissing("musicBrainz", "org.apache.uima.alchemy.ts.entity.Movie");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Movie_Type) jcasType).casFeatCode_musicBrainz);
  }

  /**
   * setter for musicBrainz - sets
   * 
   * @generated
   */
  public void setMusicBrainz(String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_musicBrainz == null)
      jcasType.jcas.throwFeatMissing("musicBrainz", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setStringValue(addr, ((Movie_Type) jcasType).casFeatCode_musicBrainz, v);
  }

  // *--------------*
  // * Feature: quotations

  /**
   * getter for quotations - gets
   * 
   * @generated
   */
  public StringArray getQuotations() {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_quotations == null)
      jcasType.jcas.throwFeatMissing("quotations", "org.apache.uima.alchemy.ts.entity.Movie");
    return (StringArray) (jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr,
            ((Movie_Type) jcasType).casFeatCode_quotations)));
  }

  /**
   * setter for quotations - sets
   * 
   * @generated
   */
  public void setQuotations(StringArray v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_quotations == null)
      jcasType.jcas.throwFeatMissing("quotations", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.ll_cas.ll_setRefValue(addr, ((Movie_Type) jcasType).casFeatCode_quotations,
            jcasType.ll_cas.ll_getFSRef(v));
  }

  /**
   * indexed getter for quotations - gets an indexed value -
   * 
   * @generated
   */
  public String getQuotations(int i) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_quotations == null)
      jcasType.jcas.throwFeatMissing("quotations", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr,
            ((Movie_Type) jcasType).casFeatCode_quotations), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr,
            ((Movie_Type) jcasType).casFeatCode_quotations), i);
  }

  /**
   * indexed setter for quotations - sets an indexed value -
   * 
   * @generated
   */
  public void setQuotations(int i, String v) {
    if (Movie_Type.featOkTst && ((Movie_Type) jcasType).casFeat_quotations == null)
      jcasType.jcas.throwFeatMissing("quotations", "org.apache.uima.alchemy.ts.entity.Movie");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr,
            ((Movie_Type) jcasType).casFeatCode_quotations), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr,
            ((Movie_Type) jcasType).casFeatCode_quotations), i, v);
  }
}
