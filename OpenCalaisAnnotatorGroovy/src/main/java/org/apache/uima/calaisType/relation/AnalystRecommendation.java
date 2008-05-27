

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
public class AnalystRecommendation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(AnalystRecommendation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected AnalystRecommendation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public AnalystRecommendation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public AnalystRecommendation(JCas jcas) {
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
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_company_source)));}
    
  /** setter for company_source - sets  
   * @generated */
  public void setCompany_source(Company v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_company_source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: person_source

  /** getter for person_source - gets 
   * @generated */
  public Person getPerson_source() {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_person_source == null)
      jcasType.jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_person_source)));}
    
  /** setter for person_source - sets  
   * @generated */
  public void setPerson_source(Person v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_person_source == null)
      jcasType.jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_person_source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_rated

  /** getter for company_rated - gets 
   * @generated */
  public Company getCompany_rated() {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_company_rated)));}
    
  /** setter for company_rated - sets  
   * @generated */
  public void setCompany_rated(Company v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_company_rated, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: trend

  /** getter for trend - gets 
   * @generated */
  public String getTrend() {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_trend);}
    
  /** setter for trend - sets  
   * @generated */
  public void setTrend(String v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_trend, v);}    
   
    
  //*--------------*
  //* Feature: rank_new

  /** getter for rank_new - gets 
   * @generated */
  public String getRank_new() {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_rank_new == null)
      jcasType.jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_rank_new);}
    
  /** setter for rank_new - sets  
   * @generated */
  public void setRank_new(String v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_rank_new == null)
      jcasType.jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_rank_new, v);}    
   
    
  //*--------------*
  //* Feature: rank_old

  /** getter for rank_old - gets 
   * @generated */
  public String getRank_old() {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_rank_old == null)
      jcasType.jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_rank_old);}
    
  /** setter for rank_old - sets  
   * @generated */
  public void setRank_old(String v) {
    if (AnalystRecommendation_Type.featOkTst && ((AnalystRecommendation_Type)jcasType).casFeat_rank_old == null)
      jcasType.jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalystRecommendation_Type)jcasType).casFeatCode_rank_old, v);}    
  }

    