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

/* First created by JCasGen Mon Mar 09 21:09:21 CET 2009 */
package org.apache.uima.lucas.indexer.types.test;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.StringArray;


/** 
 * Updated by JCasGen Mon Mar 09 21:09:39 CET 2009
 * XML source: /home/landefeld/workspace/jules-lucene-indexer/src/test/resources/AnnotationTokenStreamTestTypeSystem.xml
 * @generated */
public class Annotation1 extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Annotation1.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Annotation1() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Annotation1(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Annotation1(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Annotation1(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: featureString

  /** getter for featureString - gets 
   * @generated */
  public String getFeatureString() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureString == null)
      jcasType.jcas.throwFeatMissing("featureString", "org.apache.uima.indexer.types.test.Annotation1");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureString);}
    
  /** setter for featureString - sets  
   * @generated */
  public void setFeatureString(String v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureString == null)
      jcasType.jcas.throwFeatMissing("featureString", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setStringValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureString, v);}    
   
    
  //*--------------*
  //* Feature: featureStringArray

  /** getter for featureStringArray - gets 
   * @generated */
  public StringArray getFeatureStringArray() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStringArray == null)
      jcasType.jcas.throwFeatMissing("featureStringArray", "org.apache.uima.indexer.types.test.Annotation1");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray)));}
    
  /** setter for featureStringArray - sets  
   * @generated */
  public void setFeatureStringArray(StringArray v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStringArray == null)
      jcasType.jcas.throwFeatMissing("featureStringArray", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for featureStringArray - gets an indexed value - 
   * @generated */
  public String getFeatureStringArray(int i) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStringArray == null)
      jcasType.jcas.throwFeatMissing("featureStringArray", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray), i);}

  /** indexed setter for featureStringArray - sets an indexed value - 
   * @generated */
  public void setFeatureStringArray(int i, String v) { 
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStringArray == null)
      jcasType.jcas.throwFeatMissing("featureStringArray", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStringArray), i, v);}
   
    
  //*--------------*
  //* Feature: featureInteger

  /** getter for featureInteger - gets 
   * @generated */
  public int getFeatureInteger() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureInteger == null)
      jcasType.jcas.throwFeatMissing("featureInteger", "org.apache.uima.indexer.types.test.Annotation1");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureInteger);}
    
  /** setter for featureInteger - sets  
   * @generated */
  public void setFeatureInteger(int v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureInteger == null)
      jcasType.jcas.throwFeatMissing("featureInteger", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setIntValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureInteger, v);}    
   
    
  //*--------------*
  //* Feature: featureFloat

  /** getter for featureFloat - gets 
   * @generated */
  public float getFeatureFloat() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureFloat == null)
      jcasType.jcas.throwFeatMissing("featureFloat", "org.apache.uima.indexer.types.test.Annotation1");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureFloat);}
    
  /** setter for featureFloat - sets  
   * @generated */
  public void setFeatureFloat(float v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureFloat == null)
      jcasType.jcas.throwFeatMissing("featureFloat", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setFloatValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureFloat, v);}    
   
    
  //*--------------*
  //* Feature: featureStructure1

  /** getter for featureStructure1 - gets 
   * @generated */
  public FeatureStructure1 getFeatureStructure1() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructure1 == null)
      jcasType.jcas.throwFeatMissing("featureStructure1", "org.apache.uima.indexer.types.test.Annotation1");
    return (FeatureStructure1)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructure1)));}
    
  /** setter for featureStructure1 - sets  
   * @generated */
  public void setFeatureStructure1(FeatureStructure1 v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructure1 == null)
      jcasType.jcas.throwFeatMissing("featureStructure1", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructure1, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: featureStructures1

  /** getter for featureStructures1 - gets 
   * @generated */
  public FSArray getFeatureStructures1() {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructures1 == null)
      jcasType.jcas.throwFeatMissing("featureStructures1", "org.apache.uima.indexer.types.test.Annotation1");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1)));}
    
  /** setter for featureStructures1 - sets  
   * @generated */
  public void setFeatureStructures1(FSArray v) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructures1 == null)
      jcasType.jcas.throwFeatMissing("featureStructures1", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.ll_cas.ll_setRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for featureStructures1 - gets an indexed value - 
   * @generated */
  public FeatureStructure1 getFeatureStructures1(int i) {
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructures1 == null)
      jcasType.jcas.throwFeatMissing("featureStructures1", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1), i);
    return (FeatureStructure1)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1), i)));}

  /** indexed setter for featureStructures1 - sets an indexed value - 
   * @generated */
  public void setFeatureStructures1(int i, FeatureStructure1 v) { 
    if (Annotation1_Type.featOkTst && ((Annotation1_Type)jcasType).casFeat_featureStructures1 == null)
      jcasType.jcas.throwFeatMissing("featureStructures1", "org.apache.uima.indexer.types.test.Annotation1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Annotation1_Type)jcasType).casFeatCode_featureStructures1), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    