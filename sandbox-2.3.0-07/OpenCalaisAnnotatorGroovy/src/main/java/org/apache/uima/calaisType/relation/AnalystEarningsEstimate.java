

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
public class AnalystEarningsEstimate extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(AnalystEarningsEstimate.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected AnalystEarningsEstimate() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public AnalystEarningsEstimate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public AnalystEarningsEstimate(JCas jcas) {
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
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_company_source)));}
    
  /** setter for company_source - sets  
   * @generated */
  public void setCompany_source(Company v) {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_company_source == null)
      jcasType.jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_company_source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: person_source

  /** getter for person_source - gets 
   * @generated */
  public Person getPerson_source() {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_person_source == null)
      jcasType.jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_person_source)));}
    
  /** setter for person_source - sets  
   * @generated */
  public void setPerson_source(Person v) {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_person_source == null)
      jcasType.jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_person_source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_rated

  /** getter for company_rated - gets 
   * @generated */
  public Company getCompany_rated() {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_company_rated)));}
    
  /** setter for company_rated - sets  
   * @generated */
  public void setCompany_rated(Company v) {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_company_rated == null)
      jcasType.jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_company_rated, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: quarter

  /** getter for quarter - gets 
   * @generated */
  public String getQuarter() {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_quarter);}
    
  /** setter for quarter - sets  
   * @generated */
  public void setQuarter(String v) {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_quarter, v);}    
   
    
  //*--------------*
  //* Feature: year

  /** getter for year - gets 
   * @generated */
  public String getYear() {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_year);}
    
  /** setter for year - sets  
   * @generated */
  public void setYear(String v) {
    if (AnalystEarningsEstimate_Type.featOkTst && ((AnalystEarningsEstimate_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalystEarningsEstimate_Type)jcasType).casFeatCode_year, v);}    
  }

    