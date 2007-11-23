
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Nov 22 13:50:18 CET 2007
 * @generated */
public class CharacterAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CharacterAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CharacterAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CharacterAnnotation(addr, CharacterAnnotation_Type.this);
  			   CharacterAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CharacterAnnotation(addr, CharacterAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CharacterAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.simpleserver.test.CharacterAnnotation");
 
  /** @generated */
  final Feature casFeat_booleanFeature;
  /** @generated */
  final int     casFeatCode_booleanFeature;
  /** @generated */ 
  public boolean getBooleanFeature(int addr) {
        if (featOkTst && casFeat_booleanFeature == null)
      jcas.throwFeatMissing("booleanFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_booleanFeature);
  }
  /** @generated */    
  public void setBooleanFeature(int addr, boolean v) {
        if (featOkTst && casFeat_booleanFeature == null)
      jcas.throwFeatMissing("booleanFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_booleanFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_byteFeature;
  /** @generated */
  final int     casFeatCode_byteFeature;
  /** @generated */ 
  public byte getByteFeature(int addr) {
        if (featOkTst && casFeat_byteFeature == null)
      jcas.throwFeatMissing("byteFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getByteValue(addr, casFeatCode_byteFeature);
  }
  /** @generated */    
  public void setByteFeature(int addr, byte v) {
        if (featOkTst && casFeat_byteFeature == null)
      jcas.throwFeatMissing("byteFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setByteValue(addr, casFeatCode_byteFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_shortFeature;
  /** @generated */
  final int     casFeatCode_shortFeature;
  /** @generated */ 
  public short getShortFeature(int addr) {
        if (featOkTst && casFeat_shortFeature == null)
      jcas.throwFeatMissing("shortFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getShortValue(addr, casFeatCode_shortFeature);
  }
  /** @generated */    
  public void setShortFeature(int addr, short v) {
        if (featOkTst && casFeat_shortFeature == null)
      jcas.throwFeatMissing("shortFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setShortValue(addr, casFeatCode_shortFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_integerFeature;
  /** @generated */
  final int     casFeatCode_integerFeature;
  /** @generated */ 
  public int getIntegerFeature(int addr) {
        if (featOkTst && casFeat_integerFeature == null)
      jcas.throwFeatMissing("integerFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getIntValue(addr, casFeatCode_integerFeature);
  }
  /** @generated */    
  public void setIntegerFeature(int addr, int v) {
        if (featOkTst && casFeat_integerFeature == null)
      jcas.throwFeatMissing("integerFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setIntValue(addr, casFeatCode_integerFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_longFeature;
  /** @generated */
  final int     casFeatCode_longFeature;
  /** @generated */ 
  public long getLongFeature(int addr) {
        if (featOkTst && casFeat_longFeature == null)
      jcas.throwFeatMissing("longFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_longFeature);
  }
  /** @generated */    
  public void setLongFeature(int addr, long v) {
        if (featOkTst && casFeat_longFeature == null)
      jcas.throwFeatMissing("longFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_longFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_floatFeature;
  /** @generated */
  final int     casFeatCode_floatFeature;
  /** @generated */ 
  public float getFloatFeature(int addr) {
        if (featOkTst && casFeat_floatFeature == null)
      jcas.throwFeatMissing("floatFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getFloatValue(addr, casFeatCode_floatFeature);
  }
  /** @generated */    
  public void setFloatFeature(int addr, float v) {
        if (featOkTst && casFeat_floatFeature == null)
      jcas.throwFeatMissing("floatFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setFloatValue(addr, casFeatCode_floatFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_doubleFeature;
  /** @generated */
  final int     casFeatCode_doubleFeature;
  /** @generated */ 
  public double getDoubleFeature(int addr) {
        if (featOkTst && casFeat_doubleFeature == null)
      jcas.throwFeatMissing("doubleFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_doubleFeature);
  }
  /** @generated */    
  public void setDoubleFeature(int addr, double v) {
        if (featOkTst && casFeat_doubleFeature == null)
      jcas.throwFeatMissing("doubleFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_doubleFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_stringFeature;
  /** @generated */
  final int     casFeatCode_stringFeature;
  /** @generated */ 
  public String getStringFeature(int addr) {
        if (featOkTst && casFeat_stringFeature == null)
      jcas.throwFeatMissing("stringFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_stringFeature);
  }
  /** @generated */    
  public void setStringFeature(int addr, String v) {
        if (featOkTst && casFeat_stringFeature == null)
      jcas.throwFeatMissing("stringFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_stringFeature, v);}
    
  
 
  /** @generated */
  final Feature casFeat_fsFeature;
  /** @generated */
  final int     casFeatCode_fsFeature;
  /** @generated */ 
  public int getFsFeature(int addr) {
        if (featOkTst && casFeat_fsFeature == null)
      jcas.throwFeatMissing("fsFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_fsFeature);
  }
  /** @generated */    
  public void setFsFeature(int addr, int v) {
        if (featOkTst && casFeat_fsFeature == null)
      jcas.throwFeatMissing("fsFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_fsFeature, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CharacterAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_booleanFeature = jcas.getRequiredFeatureDE(casType, "booleanFeature", "uima.cas.Boolean", featOkTst);
    casFeatCode_booleanFeature  = (null == casFeat_booleanFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_booleanFeature).getCode();

 
    casFeat_byteFeature = jcas.getRequiredFeatureDE(casType, "byteFeature", "uima.cas.Byte", featOkTst);
    casFeatCode_byteFeature  = (null == casFeat_byteFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_byteFeature).getCode();

 
    casFeat_shortFeature = jcas.getRequiredFeatureDE(casType, "shortFeature", "uima.cas.Short", featOkTst);
    casFeatCode_shortFeature  = (null == casFeat_shortFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_shortFeature).getCode();

 
    casFeat_integerFeature = jcas.getRequiredFeatureDE(casType, "integerFeature", "uima.cas.Integer", featOkTst);
    casFeatCode_integerFeature  = (null == casFeat_integerFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_integerFeature).getCode();

 
    casFeat_longFeature = jcas.getRequiredFeatureDE(casType, "longFeature", "uima.cas.Long", featOkTst);
    casFeatCode_longFeature  = (null == casFeat_longFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_longFeature).getCode();

 
    casFeat_floatFeature = jcas.getRequiredFeatureDE(casType, "floatFeature", "uima.cas.Float", featOkTst);
    casFeatCode_floatFeature  = (null == casFeat_floatFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_floatFeature).getCode();

 
    casFeat_doubleFeature = jcas.getRequiredFeatureDE(casType, "doubleFeature", "uima.cas.Double", featOkTst);
    casFeatCode_doubleFeature  = (null == casFeat_doubleFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_doubleFeature).getCode();

 
    casFeat_stringFeature = jcas.getRequiredFeatureDE(casType, "stringFeature", "uima.cas.String", featOkTst);
    casFeatCode_stringFeature  = (null == casFeat_stringFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_stringFeature).getCode();

 
    casFeat_fsFeature = jcas.getRequiredFeatureDE(casType, "fsFeature", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays", featOkTst);
    casFeatCode_fsFeature  = (null == casFeat_fsFeature) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_fsFeature).getCode();

  }
}



    