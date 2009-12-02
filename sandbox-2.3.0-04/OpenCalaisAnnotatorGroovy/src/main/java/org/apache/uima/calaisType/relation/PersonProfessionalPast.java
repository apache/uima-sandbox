

/* First created by JCasGen Mon May 26 21:43:20 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Organization;
import org.apache.uima.calaisType.entity.Company;
import org.apache.uima.calaisType.entity.Person;


/** 
 * Updated by JCasGen Mon May 26 21:43:20 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class PersonProfessionalPast extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(PersonProfessionalPast.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected PersonProfessionalPast() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PersonProfessionalPast(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PersonProfessionalPast(JCas jcas) {
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
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_person)));}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(Person v) {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_person, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: position

  /** getter for position - gets 
   * @generated */
  public String getPosition() {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_position == null)
      jcasType.jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_position);}
    
  /** setter for position - sets  
   * @generated */
  public void setPosition(String v) {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_position == null)
      jcasType.jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    jcasType.ll_cas.ll_setStringValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_position, v);}    
   
    
  //*--------------*
  //* Feature: company

  /** getter for company - gets 
   * @generated */
  public Company getCompany() {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: organization

  /** getter for organization - gets 
   * @generated */
  public Organization getOrganization() {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_organization == null)
      jcasType.jcas.throwFeatMissing("organization", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return (Organization)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_organization)));}
    
  /** setter for organization - sets  
   * @generated */
  public void setOrganization(Organization v) {
    if (PersonProfessionalPast_Type.featOkTst && ((PersonProfessionalPast_Type)jcasType).casFeat_organization == null)
      jcasType.jcas.throwFeatMissing("organization", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonProfessionalPast_Type)jcasType).casFeatCode_organization, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    