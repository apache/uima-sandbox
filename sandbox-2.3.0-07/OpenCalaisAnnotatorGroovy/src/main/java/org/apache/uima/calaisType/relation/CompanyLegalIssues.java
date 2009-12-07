

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Company;
import org.apache.uima.calaisType.entity.Person;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class CompanyLegalIssues extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyLegalIssues.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyLegalIssues() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyLegalIssues(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyLegalIssues(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company_sued

  /** getter for company_sued - gets 
   * @generated */
  public Company getCompany_sued() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_company_sued == null)
      jcasType.jcas.throwFeatMissing("company_sued", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_company_sued)));}
    
  /** setter for company_sued - sets  
   * @generated */
  public void setCompany_sued(Company v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_company_sued == null)
      jcasType.jcas.throwFeatMissing("company_sued", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_company_sued, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: sueddescription

  /** getter for sueddescription - gets 
   * @generated */
  public String getSueddescription() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_sueddescription == null)
      jcasType.jcas.throwFeatMissing("sueddescription", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_sueddescription);}
    
  /** setter for sueddescription - sets  
   * @generated */
  public void setSueddescription(String v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_sueddescription == null)
      jcasType.jcas.throwFeatMissing("sueddescription", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_sueddescription, v);}    
   
    
  //*--------------*
  //* Feature: company_plaintiff

  /** getter for company_plaintiff - gets 
   * @generated */
  public Company getCompany_plaintiff() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_company_plaintiff == null)
      jcasType.jcas.throwFeatMissing("company_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_company_plaintiff)));}
    
  /** setter for company_plaintiff - sets  
   * @generated */
  public void setCompany_plaintiff(Company v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_company_plaintiff == null)
      jcasType.jcas.throwFeatMissing("company_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_company_plaintiff, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: person_plaintiff

  /** getter for person_plaintiff - gets 
   * @generated */
  public Person getPerson_plaintiff() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_person_plaintiff == null)
      jcasType.jcas.throwFeatMissing("person_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_person_plaintiff)));}
    
  /** setter for person_plaintiff - sets  
   * @generated */
  public void setPerson_plaintiff(Person v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_person_plaintiff == null)
      jcasType.jcas.throwFeatMissing("person_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_person_plaintiff, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: lawsuitclass

  /** getter for lawsuitclass - gets 
   * @generated */
  public String getLawsuitclass() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_lawsuitclass == null)
      jcasType.jcas.throwFeatMissing("lawsuitclass", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_lawsuitclass);}
    
  /** setter for lawsuitclass - sets  
   * @generated */
  public void setLawsuitclass(String v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_lawsuitclass == null)
      jcasType.jcas.throwFeatMissing("lawsuitclass", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_lawsuitclass, v);}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public String getDate() {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(String v) {
    if (CompanyLegalIssues_Type.featOkTst && ((CompanyLegalIssues_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyLegalIssues_Type)jcasType).casFeatCode_date, v);}    
  }

    