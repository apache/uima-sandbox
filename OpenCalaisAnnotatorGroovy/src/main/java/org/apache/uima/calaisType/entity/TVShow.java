

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.entity;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Entity;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class TVShow extends Entity {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TVShow.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TVShow() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TVShow(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TVShow(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: canonicalForm

  /** getter for canonicalForm - gets 
   * @generated */
  public String getCanonicalForm() {
    if (TVShow_Type.featOkTst && ((TVShow_Type)jcasType).casFeat_canonicalForm == null)
      jcasType.jcas.throwFeatMissing("canonicalForm", "org.apache.uima.calaisType.entity.TVShow");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TVShow_Type)jcasType).casFeatCode_canonicalForm);}
    
  /** setter for canonicalForm - sets  
   * @generated */
  public void setCanonicalForm(String v) {
    if (TVShow_Type.featOkTst && ((TVShow_Type)jcasType).casFeat_canonicalForm == null)
      jcasType.jcas.throwFeatMissing("canonicalForm", "org.apache.uima.calaisType.entity.TVShow");
    jcasType.ll_cas.ll_setStringValue(addr, ((TVShow_Type)jcasType).casFeatCode_canonicalForm, v);}    
  }

    