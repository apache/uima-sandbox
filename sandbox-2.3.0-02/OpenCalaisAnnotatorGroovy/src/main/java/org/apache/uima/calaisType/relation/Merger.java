

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Company;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class Merger extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Merger.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Merger() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Merger(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Merger(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company

  /** getter for company - gets 
   * @generated */
  public FSArray getCompany() {
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Merger");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(FSArray v) {
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Merger");
    jcasType.ll_cas.ll_setRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for company - gets an indexed value - 
   * @generated */
  public Company getCompany(int i) {
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Merger");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company), i);
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company), i)));}

  /** indexed setter for company - sets an indexed value - 
   * @generated */
  public void setCompany(int i, Company v) { 
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Merger");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Merger_Type)jcasType).casFeatCode_company), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Merger");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Merger_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (Merger_Type.featOkTst && ((Merger_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Merger");
    jcasType.ll_cas.ll_setStringValue(addr, ((Merger_Type)jcasType).casFeatCode_status, v);}    
  }

    