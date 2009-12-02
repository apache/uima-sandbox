

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
public class CompanyCustomer extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyCustomer.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyCustomer() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyCustomer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyCustomer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company_provider

  /** getter for company_provider - gets 
   * @generated */
  public Company getCompany_provider() {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_company_provider == null)
      jcasType.jcas.throwFeatMissing("company_provider", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_company_provider)));}
    
  /** setter for company_provider - sets  
   * @generated */
  public void setCompany_provider(Company v) {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_company_provider == null)
      jcasType.jcas.throwFeatMissing("company_provider", "org.apache.uima.calaisType.relation.CompanyCustomer");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_company_provider, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_customer

  /** getter for company_customer - gets 
   * @generated */
  public Company getCompany_customer() {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_company_customer == null)
      jcasType.jcas.throwFeatMissing("company_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_company_customer)));}
    
  /** setter for company_customer - sets  
   * @generated */
  public void setCompany_customer(Company v) {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_company_customer == null)
      jcasType.jcas.throwFeatMissing("company_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_company_customer, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: organization_customer

  /** getter for organization_customer - gets 
   * @generated */
  public String getOrganization_customer() {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_organization_customer == null)
      jcasType.jcas.throwFeatMissing("organization_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_organization_customer);}
    
  /** setter for organization_customer - sets  
   * @generated */
  public void setOrganization_customer(String v) {
    if (CompanyCustomer_Type.featOkTst && ((CompanyCustomer_Type)jcasType).casFeat_organization_customer == null)
      jcasType.jcas.throwFeatMissing("organization_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyCustomer_Type)jcasType).casFeatCode_organization_customer, v);}    
  }

    