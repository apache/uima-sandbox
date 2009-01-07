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

package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** Simple Feature Value
 * Updated by JCasGen Thu Sep 18 08:31:44 BST 2008
 * XML source: /data/gate-plugins/UIMAAnnotationReader/desc/MarkupAnnotationTypeSystem.xml
 * @generated */
public class FeatureValue extends TOP {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(FeatureValue.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected FeatureValue() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public FeatureValue(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public FeatureValue(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: name

  /** getter for name - gets name of the feature
   * @generated */
  public String getName() {
    if (FeatureValue_Type.featOkTst && ((FeatureValue_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.FeatureValue");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FeatureValue_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets name of the feature 
   * @generated */
  public void setName(String v) {
    if (FeatureValue_Type.featOkTst && ((FeatureValue_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.FeatureValue");
    jcasType.ll_cas.ll_setStringValue(addr, ((FeatureValue_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public String getValue() {
    if (FeatureValue_Type.featOkTst && ((FeatureValue_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.uima.FeatureValue");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FeatureValue_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(String v) {
    if (FeatureValue_Type.featOkTst && ((FeatureValue_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.apache.uima.FeatureValue");
    jcasType.ll_cas.ll_setStringValue(addr, ((FeatureValue_Type)jcasType).casFeatCode_value, v);}    
  }

    