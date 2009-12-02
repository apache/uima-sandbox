

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
public class CompanyEarningsAnnouncement extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyEarningsAnnouncement.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyEarningsAnnouncement() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyEarningsAnnouncement(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyEarningsAnnouncement(JCas jcas) {
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
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: quarter

  /** getter for quarter - gets 
   * @generated */
  public String getQuarter() {
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_quarter);}
    
  /** setter for quarter - sets  
   * @generated */
  public void setQuarter(String v) {
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_quarter == null)
      jcasType.jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_quarter, v);}    
   
    
  //*--------------*
  //* Feature: year

  /** getter for year - gets 
   * @generated */
  public String getYear() {
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_year);}
    
  /** setter for year - sets  
   * @generated */
  public void setYear(String v) {
    if (CompanyEarningsAnnouncement_Type.featOkTst && ((CompanyEarningsAnnouncement_Type)jcasType).casFeat_year == null)
      jcasType.jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsAnnouncement");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyEarningsAnnouncement_Type)jcasType).casFeatCode_year, v);}    
  }

    