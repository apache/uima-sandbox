

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
public class CompanyAffiliates extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyAffiliates.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyAffiliates() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyAffiliates(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyAffiliates(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company_affiliate

  /** getter for company_affiliate - gets 
   * @generated */
  public Company getCompany_affiliate() {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_company_affiliate == null)
      jcasType.jcas.throwFeatMissing("company_affiliate", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_company_affiliate)));}
    
  /** setter for company_affiliate - sets  
   * @generated */
  public void setCompany_affiliate(Company v) {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_company_affiliate == null)
      jcasType.jcas.throwFeatMissing("company_affiliate", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_company_affiliate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_parent

  /** getter for company_parent - gets 
   * @generated */
  public Company getCompany_parent() {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_company_parent == null)
      jcasType.jcas.throwFeatMissing("company_parent", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_company_parent)));}
    
  /** setter for company_parent - sets  
   * @generated */
  public void setCompany_parent(Company v) {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_company_parent == null)
      jcasType.jcas.throwFeatMissing("company_parent", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_company_parent, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: relation

  /** getter for relation - gets 
   * @generated */
  public String getRelation() {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_relation);}
    
  /** setter for relation - sets  
   * @generated */
  public void setRelation(String v) {
    if (CompanyAffiliates_Type.featOkTst && ((CompanyAffiliates_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyAffiliates_Type)jcasType).casFeatCode_relation, v);}    
  }

    