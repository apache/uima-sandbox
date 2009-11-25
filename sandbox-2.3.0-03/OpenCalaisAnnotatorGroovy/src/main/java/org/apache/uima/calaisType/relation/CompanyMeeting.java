

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Country;
import org.apache.uima.calaisType.entity.Company;
import org.apache.uima.calaisType.entity.ProvinceOrState;
import org.apache.uima.calaisType.entity.City;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class CompanyMeeting extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyMeeting.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyMeeting() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyMeeting(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyMeeting(JCas jcas) {
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
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: companymeetingtype

  /** getter for companymeetingtype - gets 
   * @generated */
  public String getCompanymeetingtype() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_companymeetingtype == null)
      jcasType.jcas.throwFeatMissing("companymeetingtype", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_companymeetingtype);}
    
  /** setter for companymeetingtype - sets  
   * @generated */
  public void setCompanymeetingtype(String v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_companymeetingtype == null)
      jcasType.jcas.throwFeatMissing("companymeetingtype", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_companymeetingtype, v);}    
   
    
  //*--------------*
  //* Feature: country

  /** getter for country - gets 
   * @generated */
  public Country getCountry() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return (Country)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_country)));}
    
  /** setter for country - sets  
   * @generated */
  public void setCountry(Country v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_country, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: city

  /** getter for city - gets 
   * @generated */
  public City getCity() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return (City)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_city)));}
    
  /** setter for city - sets  
   * @generated */
  public void setCity(City v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_city, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: provinceorstate

  /** getter for provinceorstate - gets 
   * @generated */
  public ProvinceOrState getProvinceorstate() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return (ProvinceOrState)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_provinceorstate)));}
    
  /** setter for provinceorstate - sets  
   * @generated */
  public void setProvinceorstate(ProvinceOrState v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_provinceorstate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_status, v);}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public String getDate() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(String v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_date, v);}    
   
    
  //*--------------*
  //* Feature: meetingsite

  /** getter for meetingsite - gets 
   * @generated */
  public String getMeetingsite() {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_meetingsite == null)
      jcasType.jcas.throwFeatMissing("meetingsite", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_meetingsite);}
    
  /** setter for meetingsite - sets  
   * @generated */
  public void setMeetingsite(String v) {
    if (CompanyMeeting_Type.featOkTst && ((CompanyMeeting_Type)jcasType).casFeat_meetingsite == null)
      jcasType.jcas.throwFeatMissing("meetingsite", "org.apache.uima.calaisType.relation.CompanyMeeting");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyMeeting_Type)jcasType).casFeatCode_meetingsite, v);}    
  }

    