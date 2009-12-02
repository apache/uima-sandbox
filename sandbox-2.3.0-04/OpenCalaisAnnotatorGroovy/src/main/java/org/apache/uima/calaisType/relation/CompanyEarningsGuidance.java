

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
public class CompanyEarningsGuidance extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyEarningsGuidance.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyEarningsGuidance() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyEarningsGuidance(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyEarningsGuidance(JCas jcas) {
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
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: quarter

  /** getter for quarter - gets 
   * @generated */
  public String getQuarter() {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_quarter);}
    
  /** setter for quarter - sets  
   * @generated */
  public void setQuarter(String v) {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_quarter, v);}    
   
    
  //*--------------*
  //* Feature: year

  /** getter for year - gets 
   * @generated */
  public String getYear() {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_year);}
    
  /** setter for year - sets  
   * @generated */
  public void setYear(String v) {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_year, v);}    
   
    
  //*--------------*
  //* Feature: trend

  /** getter for trend - gets 
   * @generated */
  public String getTrend() {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_trend);}
    
  /** setter for trend - sets  
   * @generated */
  public void setTrend(String v) {
    if (CompanyEarningsGuidance_Type.featOkTst && ((CompanyEarningsGuidance_Type)jcasType).casFeat_trend == null)
      jcasType.jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyEarningsGuidance_Type)jcasType).casFeatCode_trend, v);}    
  }

    