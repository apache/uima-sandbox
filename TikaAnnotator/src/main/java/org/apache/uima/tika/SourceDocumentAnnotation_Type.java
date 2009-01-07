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
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.DocumentAnnotation_Type;

/** 
 * Updated by JCasGen Thu Sep 18 08:31:44 BST 2008
 * @generated */
public class SourceDocumentAnnotation_Type extends DocumentAnnotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SourceDocumentAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SourceDocumentAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SourceDocumentAnnotation(addr, SourceDocumentAnnotation_Type.this);
  			   SourceDocumentAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SourceDocumentAnnotation(addr, SourceDocumentAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = SourceDocumentAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.SourceDocumentAnnotation");
 
  /** @generated */
  final Feature casFeat_uri;
  /** @generated */
  final int     casFeatCode_uri;
  /** @generated */ 
  public String getUri(int addr) {
        if (featOkTst && casFeat_uri == null)
      jcas.throwFeatMissing("uri", "org.apache.uima.SourceDocumentAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uri);
  }
  /** @generated */    
  public void setUri(int addr, String v) {
        if (featOkTst && casFeat_uri == null)
      jcas.throwFeatMissing("uri", "org.apache.uima.SourceDocumentAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_uri, v);}
    
  
 
  /** @generated */
  final Feature casFeat_contentType;
  /** @generated */
  final int     casFeatCode_contentType;
  /** @generated */ 
  public String getContentType(int addr) {
        if (featOkTst && casFeat_contentType == null)
      jcas.throwFeatMissing("contentType", "org.apache.uima.SourceDocumentAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_contentType);
  }
  /** @generated */    
  public void setContentType(int addr, String v) {
        if (featOkTst && casFeat_contentType == null)
      jcas.throwFeatMissing("contentType", "org.apache.uima.SourceDocumentAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_contentType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_features;
  /** @generated */
  final int     casFeatCode_features;
  /** @generated */ 
  public int getFeatures(int addr) {
        if (featOkTst && casFeat_features == null)
      jcas.throwFeatMissing("features", "org.apache.uima.SourceDocumentAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_features);
  }
  /** @generated */    
  public void setFeatures(int addr, int v) {
        if (featOkTst && casFeat_features == null)
      jcas.throwFeatMissing("features", "org.apache.uima.SourceDocumentAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_features, v);}
    
   /** @generated */
  public int getFeatures(int addr, int i) {
        if (featOkTst && casFeat_features == null)
      jcas.throwFeatMissing("features", "org.apache.uima.SourceDocumentAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_features), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_features), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_features), i);
  }
   
  /** @generated */ 
  public void setFeatures(int addr, int i, int v) {
        if (featOkTst && casFeat_features == null)
      jcas.throwFeatMissing("features", "org.apache.uima.SourceDocumentAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_features), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_features), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_features), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SourceDocumentAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_uri = jcas.getRequiredFeatureDE(casType, "uri", "uima.cas.String", featOkTst);
    casFeatCode_uri  = (null == casFeat_uri) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uri).getCode();

 
    casFeat_contentType = jcas.getRequiredFeatureDE(casType, "contentType", "uima.cas.String", featOkTst);
    casFeatCode_contentType  = (null == casFeat_contentType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_contentType).getCode();

 
    casFeat_features = jcas.getRequiredFeatureDE(casType, "features", "uima.cas.FSArray", featOkTst);
    casFeatCode_features  = (null == casFeat_features) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_features).getCode();

  }
}



    