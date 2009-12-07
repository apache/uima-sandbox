

/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Person;


/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class PersonEducation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(PersonEducation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected PersonEducation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PersonEducation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PersonEducation(JCas jcas) {
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
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonEducation");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_person)));}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(Person v) {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonEducation");
    jcasType.ll_cas.ll_setRefValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_person, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: certification

  /** getter for certification - gets 
   * @generated */
  public String getCertification() {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_certification == null)
      jcasType.jcas.throwFeatMissing("certification", "org.apache.uima.calaisType.relation.PersonEducation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_certification);}
    
  /** setter for certification - sets  
   * @generated */
  public void setCertification(String v) {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_certification == null)
      jcasType.jcas.throwFeatMissing("certification", "org.apache.uima.calaisType.relation.PersonEducation");
    jcasType.ll_cas.ll_setStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_certification, v);}    
   
    
  //*--------------*
  //* Feature: degree

  /** getter for degree - gets 
   * @generated */
  public String getDegree() {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.calaisType.relation.PersonEducation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_degree);}
    
  /** setter for degree - sets  
   * @generated */
  public void setDegree(String v) {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_degree == null)
      jcasType.jcas.throwFeatMissing("degree", "org.apache.uima.calaisType.relation.PersonEducation");
    jcasType.ll_cas.ll_setStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_degree, v);}    
   
    
  //*--------------*
  //* Feature: schoolororganization

  /** getter for schoolororganization - gets 
   * @generated */
  public String getSchoolororganization() {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_schoolororganization == null)
      jcasType.jcas.throwFeatMissing("schoolororganization", "org.apache.uima.calaisType.relation.PersonEducation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_schoolororganization);}
    
  /** setter for schoolororganization - sets  
   * @generated */
  public void setSchoolororganization(String v) {
    if (PersonEducation_Type.featOkTst && ((PersonEducation_Type)jcasType).casFeat_schoolororganization == null)
      jcasType.jcas.throwFeatMissing("schoolororganization", "org.apache.uima.calaisType.relation.PersonEducation");
    jcasType.ll_cas.ll_setStringValue(addr, ((PersonEducation_Type)jcasType).casFeatCode_schoolororganization, v);}    
  }

    