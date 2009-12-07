

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Organization;
import org.apache.uima.calaisType.entity.Company;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class CreditRating extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CreditRating.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CreditRating() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CreditRating(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CreditRating(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company_source

  /** getter for company_source - gets 
   * @generated */
  public Company getCompany_source() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.CreditRating");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_company_source)));}
    
  /** setter for company_source - sets  
   * @generated */
  public void setCompany_source(Company v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_company_source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_rated

  /** getter for company_rated - gets 
   * @generated */
  public Company getCompany_rated() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.CreditRating");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_company_rated)));}
    
  /** setter for company_rated - sets  
   * @generated */
  public void setCompany_rated(Company v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_company_rated, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: organization_rated

  /** getter for organization_rated - gets 
   * @generated */
  public Organization getOrganization_rated() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_organization_rated == null)
      jcasType.jcas.throwFeatMissing("organization_rated", "org.apache.uima.calaisType.relation.CreditRating");
    return (Organization)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_organization_rated)));}
    
  /** setter for organization_rated - sets  
   * @generated */
  public void setOrganization_rated(Organization v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_organization_rated == null)
      jcasType.jcas.throwFeatMissing("organization_rated", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setRefValue(addr, ((CreditRating_Type)jcasType).casFeatCode_organization_rated, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: trend

  /** getter for trend - gets 
   * @generated */
  public String getTrend() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CreditRating");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_trend);}
    
  /** setter for trend - sets  
   * @generated */
  public void setTrend(String v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_trend, v);}    
   
    
  //*--------------*
  //* Feature: rank_new

  /** getter for rank_new - gets 
   * @generated */
  public String getRank_new() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_rank_new == null)
      jcasType.jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.CreditRating");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_rank_new);}
    
  /** setter for rank_new - sets  
   * @generated */
  public void setRank_new(String v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_rank_new == null)
      jcasType.jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_rank_new, v);}    
   
    
  //*--------------*
  //* Feature: rank_old

  /** getter for rank_old - gets 
   * @generated */
  public String getRank_old() {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_rank_old == null)
      jcasType.jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.CreditRating");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_rank_old);}
    
  /** setter for rank_old - sets  
   * @generated */
  public void setRank_old(String v) {
    if (CreditRating_Type.featOkTst && ((CreditRating_Type)jcasType).casFeat_rank_old == null)
      jcasType.jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.CreditRating");
    jcasType.ll_cas.ll_setStringValue(addr, ((CreditRating_Type)jcasType).casFeatCode_rank_old, v);}    
  }

    