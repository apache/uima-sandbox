

/* First created by JCasGen Thu Nov 22 13:50:18 CET 2007 */
package org.apache.uima.simpleserver.test;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Nov 22 13:50:18 CET 2007
 * XML source: C:/code/ApacheUIMA/SimpleServer/src/test/resources/desc/simpleServerTestDescriptor.xml
 * @generated */
public class CharacterAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CharacterAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CharacterAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CharacterAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CharacterAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public CharacterAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: booleanFeature

  /** getter for booleanFeature - gets 
   * @generated */
  public boolean getBooleanFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_booleanFeature == null)
      jcasType.jcas.throwFeatMissing("booleanFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_booleanFeature);}
    
  /** setter for booleanFeature - sets  
   * @generated */
  public void setBooleanFeature(boolean v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_booleanFeature == null)
      jcasType.jcas.throwFeatMissing("booleanFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_booleanFeature, v);}    
   
    
  //*--------------*
  //* Feature: byteFeature

  /** getter for byteFeature - gets 
   * @generated */
  public byte getByteFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_byteFeature == null)
      jcasType.jcas.throwFeatMissing("byteFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getByteValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_byteFeature);}
    
  /** setter for byteFeature - sets  
   * @generated */
  public void setByteFeature(byte v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_byteFeature == null)
      jcasType.jcas.throwFeatMissing("byteFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setByteValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_byteFeature, v);}    
   
    
  //*--------------*
  //* Feature: shortFeature

  /** getter for shortFeature - gets 
   * @generated */
  public short getShortFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_shortFeature == null)
      jcasType.jcas.throwFeatMissing("shortFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getShortValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_shortFeature);}
    
  /** setter for shortFeature - sets  
   * @generated */
  public void setShortFeature(short v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_shortFeature == null)
      jcasType.jcas.throwFeatMissing("shortFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setShortValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_shortFeature, v);}    
   
    
  //*--------------*
  //* Feature: integerFeature

  /** getter for integerFeature - gets 
   * @generated */
  public int getIntegerFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_integerFeature == null)
      jcasType.jcas.throwFeatMissing("integerFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_integerFeature);}
    
  /** setter for integerFeature - sets  
   * @generated */
  public void setIntegerFeature(int v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_integerFeature == null)
      jcasType.jcas.throwFeatMissing("integerFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_integerFeature, v);}    
   
    
  //*--------------*
  //* Feature: longFeature

  /** getter for longFeature - gets 
   * @generated */
  public long getLongFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_longFeature == null)
      jcasType.jcas.throwFeatMissing("longFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_longFeature);}
    
  /** setter for longFeature - sets  
   * @generated */
  public void setLongFeature(long v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_longFeature == null)
      jcasType.jcas.throwFeatMissing("longFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setLongValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_longFeature, v);}    
   
    
  //*--------------*
  //* Feature: floatFeature

  /** getter for floatFeature - gets 
   * @generated */
  public float getFloatFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_floatFeature == null)
      jcasType.jcas.throwFeatMissing("floatFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_floatFeature);}
    
  /** setter for floatFeature - sets  
   * @generated */
  public void setFloatFeature(float v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_floatFeature == null)
      jcasType.jcas.throwFeatMissing("floatFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setFloatValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_floatFeature, v);}    
   
    
  //*--------------*
  //* Feature: doubleFeature

  /** getter for doubleFeature - gets 
   * @generated */
  public double getDoubleFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_doubleFeature == null)
      jcasType.jcas.throwFeatMissing("doubleFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_doubleFeature);}
    
  /** setter for doubleFeature - sets  
   * @generated */
  public void setDoubleFeature(double v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_doubleFeature == null)
      jcasType.jcas.throwFeatMissing("doubleFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_doubleFeature, v);}    
   
    
  //*--------------*
  //* Feature: stringFeature

  /** getter for stringFeature - gets 
   * @generated */
  public String getStringFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_stringFeature == null)
      jcasType.jcas.throwFeatMissing("stringFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_stringFeature);}
    
  /** setter for stringFeature - sets  
   * @generated */
  public void setStringFeature(String v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_stringFeature == null)
      jcasType.jcas.throwFeatMissing("stringFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_stringFeature, v);}    
   
    
  //*--------------*
  //* Feature: fsFeature

  /** getter for fsFeature - gets 
   * @generated */
  public TypeWithListsAndArrays getFsFeature() {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_fsFeature == null)
      jcasType.jcas.throwFeatMissing("fsFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    return (TypeWithListsAndArrays)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_fsFeature)));}
    
  /** setter for fsFeature - sets  
   * @generated */
  public void setFsFeature(TypeWithListsAndArrays v) {
    if (CharacterAnnotation_Type.featOkTst && ((CharacterAnnotation_Type)jcasType).casFeat_fsFeature == null)
      jcasType.jcas.throwFeatMissing("fsFeature", "org.apache.uima.simpleserver.test.CharacterAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CharacterAnnotation_Type)jcasType).casFeatCode_fsFeature, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    