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

/* First created by JCasGen Thu Oct 25 11:28:37 CEST 2007 */
package org.apache.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * Single token annotation Updated by JCasGen Thu Oct 25 11:28:37 CEST 2007
 * 
 * @generated
 */
public class TokenAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {
    return fsGenerator;
  }

  /** @generated */
  private final FSGenerator fsGenerator = new FSGenerator() {
    public FeatureStructure createFS(int addr, CASImpl cas) {
      if (TokenAnnotation_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = TokenAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new TokenAnnotation(addr, TokenAnnotation_Type.this);
          TokenAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else
        return new TokenAnnotation(addr, TokenAnnotation_Type.this);
    }
  };

  /** @generated */
  public final static int typeIndexID = TokenAnnotation.typeIndexID;

  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst = JCasRegistry
          .getFeatOkTst("org.apache.uima.TokenAnnotation");

  /** @generated */
  final Feature casFeat_tokenType;

  /** @generated */
  final int casFeatCode_tokenType;

  /** @generated */
  public String getTokenType(int addr) {
    if (featOkTst && casFeat_tokenType == null)
      jcas.throwFeatMissing("tokenType", "org.apache.uima.TokenAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tokenType);
  }

  /** @generated */
  public void setTokenType(int addr, String v) {
    if (featOkTst && casFeat_tokenType == null)
      jcas.throwFeatMissing("tokenType", "org.apache.uima.TokenAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_tokenType, v);
  }

  /** @generated */
  final Feature casFeat_posTag;

  /** @generated */
  final int casFeatCode_posTag;

  /** @generated */
  public String getPosTag(int addr) {
    if (featOkTst && casFeat_posTag == null)
      jcas.throwFeatMissing("posTag", "org.apache.uima.TokenAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_posTag);
  }

  /** @generated */
  public void setPosTag(int addr, String v) {
    if (featOkTst && casFeat_posTag == null)
      jcas.throwFeatMissing("posTag", "org.apache.uima.TokenAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_posTag, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public TokenAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_tokenType = jcas.getRequiredFeatureDE(casType, "tokenType", "uima.cas.String",
            featOkTst);
    casFeatCode_tokenType = (null == casFeat_tokenType) ? JCas.INVALID_FEATURE_CODE
            : ((FeatureImpl) casFeat_tokenType).getCode();

    casFeat_posTag = jcas.getRequiredFeatureDE(casType, "posTag", "uima.cas.String", featOkTst);
    casFeatCode_posTag = (null == casFeat_posTag) ? JCas.INVALID_FEATURE_CODE
            : ((FeatureImpl) casFeat_posTag).getCode();

  }
}
