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
import org.apache.uima.jcas.cas.TOP_Type;

/** Representation of an attribute
 * Updated by JCasGen Thu Sep 18 08:31:44 BST 2008
 * @generated */
public class AttributeFS_Type extends TOP_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AttributeFS_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AttributeFS_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AttributeFS(addr, AttributeFS_Type.this);
  			   AttributeFS_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AttributeFS(addr, AttributeFS_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = AttributeFS.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.AttributeFS");
 
  /** @generated */
  final Feature casFeat_localName;
  /** @generated */
  final int     casFeatCode_localName;
  /** @generated */ 
  public String getLocalName(int addr) {
        if (featOkTst && casFeat_localName == null)
      jcas.throwFeatMissing("localName", "org.apache.uima.AttributeFS");
    return ll_cas.ll_getStringValue(addr, casFeatCode_localName);
  }
  /** @generated */    
  public void setLocalName(int addr, String v) {
        if (featOkTst && casFeat_localName == null)
      jcas.throwFeatMissing("localName", "org.apache.uima.AttributeFS");
    ll_cas.ll_setStringValue(addr, casFeatCode_localName, v);}
    
  
 
  /** @generated */
  final Feature casFeat_qualifiedName;
  /** @generated */
  final int     casFeatCode_qualifiedName;
  /** @generated */ 
  public String getQualifiedName(int addr) {
        if (featOkTst && casFeat_qualifiedName == null)
      jcas.throwFeatMissing("qualifiedName", "org.apache.uima.AttributeFS");
    return ll_cas.ll_getStringValue(addr, casFeatCode_qualifiedName);
  }
  /** @generated */    
  public void setQualifiedName(int addr, String v) {
        if (featOkTst && casFeat_qualifiedName == null)
      jcas.throwFeatMissing("qualifiedName", "org.apache.uima.AttributeFS");
    ll_cas.ll_setStringValue(addr, casFeatCode_qualifiedName, v);}
    
  
 
  /** @generated */
  final Feature casFeat_uri;
  /** @generated */
  final int     casFeatCode_uri;
  /** @generated */ 
  public String getUri(int addr) {
        if (featOkTst && casFeat_uri == null)
      jcas.throwFeatMissing("uri", "org.apache.uima.AttributeFS");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uri);
  }
  /** @generated */    
  public void setUri(int addr, String v) {
        if (featOkTst && casFeat_uri == null)
      jcas.throwFeatMissing("uri", "org.apache.uima.AttributeFS");
    ll_cas.ll_setStringValue(addr, casFeatCode_uri, v);}
    
  
 
  /** @generated */
  final Feature casFeat_value;
  /** @generated */
  final int     casFeatCode_value;
  /** @generated */ 
  public String getValue(int addr) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.apache.uima.AttributeFS");
    return ll_cas.ll_getStringValue(addr, casFeatCode_value);
  }
  /** @generated */    
  public void setValue(int addr, String v) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.apache.uima.AttributeFS");
    ll_cas.ll_setStringValue(addr, casFeatCode_value, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public AttributeFS_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_localName = jcas.getRequiredFeatureDE(casType, "localName", "uima.cas.String", featOkTst);
    casFeatCode_localName  = (null == casFeat_localName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_localName).getCode();

 
    casFeat_qualifiedName = jcas.getRequiredFeatureDE(casType, "qualifiedName", "uima.cas.String", featOkTst);
    casFeatCode_qualifiedName  = (null == casFeat_qualifiedName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_qualifiedName).getCode();

 
    casFeat_uri = jcas.getRequiredFeatureDE(casType, "uri", "uima.cas.String", featOkTst);
    casFeatCode_uri  = (null == casFeat_uri) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uri).getCode();

 
    casFeat_value = jcas.getRequiredFeatureDE(casType, "value", "uima.cas.String", featOkTst);
    casFeatCode_value  = (null == casFeat_value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_value).getCode();

  }
}



    