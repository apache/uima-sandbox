

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Company;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class ConferenceCall extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(ConferenceCall.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected ConferenceCall() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ConferenceCall(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ConferenceCall(JCas jcas) {
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
  public Company getCompany() {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.ConferenceCall");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.ConferenceCall");
    jcasType.ll_cas.ll_setRefValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: ccalltype

  /** getter for ccalltype - gets 
   * @generated */
  public String getCcalltype() {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_ccalltype == null)
      jcasType.jcas.throwFeatMissing("ccalltype", "org.apache.uima.calaisType.relation.ConferenceCall");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_ccalltype);}
    
  /** setter for ccalltype - sets  
   * @generated */
  public void setCcalltype(String v) {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_ccalltype == null)
      jcasType.jcas.throwFeatMissing("ccalltype", "org.apache.uima.calaisType.relation.ConferenceCall");
    jcasType.ll_cas.ll_setStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_ccalltype, v);}    
   
    
  //*--------------*
  //* Feature: quarter

  /** getter for quarter - gets 
   * @generated */
  public String getQuarter() {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.ConferenceCall");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_quarter);}
    
  /** setter for quarter - sets  
   * @generated */
  public void setQuarter(String v) {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.ConferenceCall");
    jcasType.ll_cas.ll_setStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_quarter, v);}    
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.ConferenceCall");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.ConferenceCall");
    jcasType.ll_cas.ll_setStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_status, v);}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public String getDate() {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.ConferenceCall");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(String v) {
    if (ConferenceCall_Type.featOkTst && ((ConferenceCall_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.ConferenceCall");
    jcasType.ll_cas.ll_setStringValue(addr, ((ConferenceCall_Type)jcasType).casFeatCode_date, v);}    
  }

    