

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
public class CompanyInvestment extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyInvestment.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyInvestment() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyInvestment(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyInvestment(JCas jcas) {
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
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyInvestment");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyInvestment");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_investor

  /** getter for company_investor - gets 
   * @generated */
  public Company getCompany_investor() {
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_company_investor == null)
      jcasType.jcas.throwFeatMissing("company_investor", "org.apache.uima.calaisType.relation.CompanyInvestment");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_company_investor)));}
    
  /** setter for company_investor - sets  
   * @generated */
  public void setCompany_investor(Company v) {
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_company_investor == null)
      jcasType.jcas.throwFeatMissing("company_investor", "org.apache.uima.calaisType.relation.CompanyInvestment");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_company_investor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyInvestment");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (CompanyInvestment_Type.featOkTst && ((CompanyInvestment_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyInvestment");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyInvestment_Type)jcasType).casFeatCode_status, v);}    
  }

    