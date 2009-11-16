

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Country;
import org.apache.uima.calaisType.entity.City;
import org.apache.uima.calaisType.entity.ProvinceOrState;
import org.apache.uima.calaisType.entity.Person;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class PersonPolitical extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(PersonPolitical.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected PersonPolitical() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PersonPolitical(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PersonPolitical(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: person

  /** getter for person - gets 
   * @generated */
  public Person getPerson() {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonPolitical");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_person)));}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(Person v) {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonPolitical");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_person, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: position

  /** getter for position - gets 
   * @generated */
  public String getPosition() {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_position == null)
      jcasType.jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonPolitical");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_position);}
    
  /** setter for position - sets  
   * @generated */
  public void setPosition(String v) {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_position == null)
      jcasType.jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonPolitical");
    jcasType.ll_cas.ll_setStringValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_position, v);}    
   
    
  //*--------------*
  //* Feature: country

  /** getter for country - gets 
   * @generated */
  public Country getCountry() {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.PersonPolitical");
    return (Country)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_country)));}
    
  /** setter for country - sets  
   * @generated */
  public void setCountry(Country v) {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_country == null)
      jcasType.jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.PersonPolitical");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_country, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: provinceorstate

  /** getter for provinceorstate - gets 
   * @generated */
  public ProvinceOrState getProvinceorstate() {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.PersonPolitical");
    return (ProvinceOrState)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_provinceorstate)));}
    
  /** setter for provinceorstate - sets  
   * @generated */
  public void setProvinceorstate(ProvinceOrState v) {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_provinceorstate == null)
      jcasType.jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.PersonPolitical");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_provinceorstate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: city

  /** getter for city - gets 
   * @generated */
  public City getCity() {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.PersonPolitical");
    return (City)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_city)));}
    
  /** setter for city - sets  
   * @generated */
  public void setCity(City v) {
    if (PersonPolitical_Type.featOkTst && ((PersonPolitical_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.PersonPolitical");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonPolitical_Type)jcasType).casFeatCode_city, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    