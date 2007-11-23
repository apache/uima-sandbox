

/* First created by JCasGen Thu Nov 22 13:50:18 CET 2007 */
package org.apache.uima.simpleserver.test;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;


/** 
 * Updated by JCasGen Thu Nov 22 13:50:18 CET 2007
 * XML source: C:/code/ApacheUIMA/SimpleServer/src/test/resources/desc/simpleServerTestDescriptor.xml
 * @generated */
public class TypeWithListsAndArrays extends TOP {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TypeWithListsAndArrays.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TypeWithListsAndArrays() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TypeWithListsAndArrays(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TypeWithListsAndArrays(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: stringList

  /** getter for stringList - gets 
   * @generated */
  public String getStringList() {
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_stringList == null)
      jcasType.jcas.throwFeatMissing("stringList", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_stringList);}
    
  /** setter for stringList - sets  
   * @generated */
  public void setStringList(String v) {
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_stringList == null)
      jcasType.jcas.throwFeatMissing("stringList", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    jcasType.ll_cas.ll_setStringValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_stringList, v);}    
   
    
  //*--------------*
  //* Feature: annotationArray

  /** getter for annotationArray - gets 
   * @generated */
  public FSArray getAnnotationArray() {
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_annotationArray == null)
      jcasType.jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray)));}
    
  /** setter for annotationArray - sets  
   * @generated */
  public void setAnnotationArray(FSArray v) {
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_annotationArray == null)
      jcasType.jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    jcasType.ll_cas.ll_setRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for annotationArray - gets an indexed value - 
   * @generated */
  public TOP getAnnotationArray(int i) {
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_annotationArray == null)
      jcasType.jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray), i)));}

  /** indexed setter for annotationArray - sets an indexed value - 
   * @generated */
  public void setAnnotationArray(int i, TOP v) { 
    if (TypeWithListsAndArrays_Type.featOkTst && ((TypeWithListsAndArrays_Type)jcasType).casFeat_annotationArray == null)
      jcasType.jcas.throwFeatMissing("annotationArray", "org.apache.uima.simpleserver.test.TypeWithListsAndArrays");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TypeWithListsAndArrays_Type)jcasType).casFeatCode_annotationArray), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    