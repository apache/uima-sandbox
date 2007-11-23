
/* First created by JCasGen Thu Nov 22 13:50:18 CET 2007 */
package org.apache.uima.simpleserver.test;

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

/** 
 * Updated by JCasGen Thu Nov 22 13:50:18 CET 2007
 * @generated */
public class TypeWithListsAndArrays_Type extends TOP_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TypeWithListsAndArrays_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TypeWithListsAndArrays_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TypeWithListsAndArrays(addr, TypeWithListsAndArrays_Type.this);
  			   TypeWithListsAndArrays_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TypeWithListsAndArrays(addr, TypeWithListsAndArrays_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TypeWithListsAndArrays.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
 
  /** @generated */
  final Feature casFeat_stringList;
  /** @generated */
  final int     casFeatCode_stringList;
  /** @generated */ 
  public String getStringList(int addr) {
        if (featOkTst && casFeat_stringList == null)
      jcas.throwFeatMissing("stringList", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    return ll_cas.ll_getStringValue(addr, casFeatCode_stringList);
  }
  /** @generated */    
  public void setStringList(int addr, String v) {
        if (featOkTst && casFeat_stringList == null)
      jcas.throwFeatMissing("stringList", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    ll_cas.ll_setStringValue(addr, casFeatCode_stringList, v);}
    
  
 
  /** @generated */
  final Feature casFeat_annotationArray;
  /** @generated */
  final int     casFeatCode_annotationArray;
  /** @generated */ 
  public int getAnnotationArray(int addr) {
        if (featOkTst && casFeat_annotationArray == null)
      jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    return ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray);
  }
  /** @generated */    
  public void setAnnotationArray(int addr, int v) {
        if (featOkTst && casFeat_annotationArray == null)
      jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    ll_cas.ll_setRefValue(addr, casFeatCode_annotationArray, v);}
    
   /** @generated */
  public int getAnnotationArray(int addr, int i) {
        if (featOkTst && casFeat_annotationArray == null)
      jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i);
  }
   
  /** @generated */ 
  public void setAnnotationArray(int addr, int i, int v) {
        if (featOkTst && casFeat_annotationArray == null)
      jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_annotationArray), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TypeWithListsAndArrays_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_stringList = jcas.getRequiredFeatureDE(casType, "stringList", "uima.cas.String", featOkTst);
    casFeatCode_stringList  = (null == casFeat_stringList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_stringList).getCode();

 
    casFeat_annotationArray = jcas.getRequiredFeatureDE(casType, "annotationArray", "uima.cas.FSArray", featOkTst);
    casFeatCode_annotationArray  = (null == casFeat_annotationArray) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_annotationArray).getCode();

  }
}



    