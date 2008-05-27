

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
public class CompanyLocation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyLocation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyLocation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyLocation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyLocation(JCas jcas) {
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
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyLocation");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyLocation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: city

  /** getter for city - gets 
   * @generated */
  public City getCity() {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyLocation");
    return (City)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_city)));}
    
  /** setter for city - sets  
   * @generated */
  public void setCity(City v) {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyLocation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_city, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: provinceorstate

  /** getter for provinceorstate - gets 
   * @generated */
  public ProvinceOrState getProvinceorstate() {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyLocation");
    return (ProvinceOrState)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_provinceorstate)));}
    
  /** setter for provinceorstate - sets  
   * @generated */
  public void setProvinceorstate(ProvinceOrState v) {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyLocation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_provinceorstate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: country

  /** getter for country - gets 
   * @generated */
  public Country getCountry() {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyLocation");
    return (Country)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_country)));}
    
  /** setter for country - sets  
   * @generated */
  public void setCountry(Country v) {
    if (CompanyLocation_Type.featOkTst && ((CompanyLocation_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyLocation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyLocation_Type)jcasType).casFeatCode_country, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    