

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
public class BusinessRelation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(BusinessRelation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected BusinessRelation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public BusinessRelation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public BusinessRelation(JCas jcas) {
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
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.BusinessRelation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(FSArray v) {
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.BusinessRelation");
    jcasType.ll_cas.ll_setRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for company - gets an indexed value - 
   * @generated */
  public Company getCompany(int i) {
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.BusinessRelation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company), i);
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company), i)));}

  /** indexed setter for company - sets an indexed value - 
   * @generated */
  public void setCompany(int i, Company v) { 
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.BusinessRelation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_company), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.BusinessRelation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (BusinessRelation_Type.featOkTst && ((BusinessRelation_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.BusinessRelation");
    jcasType.ll_cas.ll_setStringValue(addr, ((BusinessRelation_Type)jcasType).casFeatCode_status, v);}    
  }

    