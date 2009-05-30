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

/* First created by JCasGen Fri Jan 16 11:22:35 CET 2009 */
package org.apache.uima.lucas.indexer.types.test;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 * Updated by JCasGen Fri Jan 16 11:22:35 CET 2009 XML source:
 * /home/landefeld/workspace/jules-lucene
 * -indexer/src/test/resources/AnnotationTokenStreamTestTypeSystem.xml
 * 
 * @generated
 */
public class FeatureStructure1 extends TOP {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(FeatureStructure1.class);

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
  protected FeatureStructure1() {
  }

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public FeatureStructure1(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public FeatureStructure1(JCas jcas) {
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
  // * Feature: feature1

  /**
   * getter for feature1 - gets
   * 
   * @generated
   */
  public String getFeature1() {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature1 == null)
      jcasType.jcas.throwFeatMissing("feature1", "de.julielab.jules.types.test.FeatureStructure1");
    return jcasType.ll_cas.ll_getStringValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_feature1);
  }

  /**
   * setter for feature1 - sets
   * 
   * @generated
   */
  public void setFeature1(String v) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature1 == null)
      jcasType.jcas.throwFeatMissing("feature1", "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.ll_cas.ll_setStringValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_feature1, v);
  }

  // *--------------*
  // * Feature: feature2

  /**
   * getter for feature2 - gets
   * 
   * @generated
   */
  public String getFeature2() {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature2 == null)
      jcasType.jcas.throwFeatMissing("feature2", "de.julielab.jules.types.test.FeatureStructure1");
    return jcasType.ll_cas.ll_getStringValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_feature2);
  }

  /**
   * setter for feature2 - sets
   * 
   * @generated
   */
  public void setFeature2(String v) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature2 == null)
      jcasType.jcas.throwFeatMissing("feature2", "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.ll_cas.ll_setStringValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_feature2, v);
  }

  // *--------------*
  // * Feature: feature3

  /**
   * getter for feature3 - gets
   * 
   * @generated
   */
  public FeatureStructure2 getFeature3() {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature3 == null)
      jcasType.jcas.throwFeatMissing("feature3", "de.julielab.jules.types.test.FeatureStructure1");
    return (FeatureStructure2) (jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_feature3)));
  }

  /**
   * setter for feature3 - sets
   * 
   * @generated
   */
  public void setFeature3(FeatureStructure2 v) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_feature3 == null)
      jcasType.jcas.throwFeatMissing("feature3", "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.ll_cas.ll_setRefValue(addr, ((FeatureStructure1_Type) jcasType).casFeatCode_feature3,
            jcasType.ll_cas.ll_getFSRef(v));
  }

  // *--------------*
  // * Feature: featureStructures2

  /**
   * getter for featureStructures2 - gets
   * 
   * @generated
   */
  public FSArray getFeatureStructures2() {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_featureStructures2 == null)
      jcasType.jcas.throwFeatMissing("featureStructures2",
              "de.julielab.jules.types.test.FeatureStructure1");
    return (FSArray) (jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2)));
  }

  /**
   * setter for featureStructures2 - sets
   * 
   * @generated
   */
  public void setFeatureStructures2(FSArray v) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_featureStructures2 == null)
      jcasType.jcas.throwFeatMissing("featureStructures2",
              "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.ll_cas.ll_setRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2, jcasType.ll_cas
                    .ll_getFSRef(v));
  }

  /**
   * indexed getter for featureStructures2 - gets an indexed value -
   * 
   * @generated
   */
  public FeatureStructure2 getFeatureStructures2(int i) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_featureStructures2 == null)
      jcasType.jcas.throwFeatMissing("featureStructures2",
              "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2), i);
    return (FeatureStructure2) (jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(
            jcasType.ll_cas.ll_getRefValue(addr,
                    ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2), i)));
  }

  /**
   * indexed setter for featureStructures2 - sets an indexed value -
   * 
   * @generated
   */
  public void setFeatureStructures2(int i, FeatureStructure2 v) {
    if (FeatureStructure1_Type.featOkTst
            && ((FeatureStructure1_Type) jcasType).casFeat_featureStructures2 == null)
      jcasType.jcas.throwFeatMissing("featureStructures2",
              "de.julielab.jules.types.test.FeatureStructure1");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr,
            ((FeatureStructure1_Type) jcasType).casFeatCode_featureStructures2), i, jcasType.ll_cas
            .ll_getFSRef(v));
  }
}
