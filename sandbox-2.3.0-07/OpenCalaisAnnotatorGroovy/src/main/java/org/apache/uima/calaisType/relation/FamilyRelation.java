

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
public class FamilyRelation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(FamilyRelation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected FamilyRelation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public FamilyRelation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public FamilyRelation(JCas jcas) {
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
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.FamilyRelation");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_person)));}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(Person v) {
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.FamilyRelation");
    jcasType.ll_cas.ll_setRefValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_person, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: person_relative

  /** getter for person_relative - gets 
   * @generated */
  public String getPerson_relative() {
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_person_relative == null)
      jcasType.jcas.throwFeatMissing("person_relative", "org.apache.uima.calaisType.relation.FamilyRelation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_person_relative);}
    
  /** setter for person_relative - sets  
   * @generated */
  public void setPerson_relative(String v) {
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_person_relative == null)
      jcasType.jcas.throwFeatMissing("person_relative", "org.apache.uima.calaisType.relation.FamilyRelation");
    jcasType.ll_cas.ll_setStringValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_person_relative, v);}    
   
    
  //*--------------*
  //* Feature: familyrelationtype

  /** getter for familyrelationtype - gets 
   * @generated */
  public String getFamilyrelationtype() {
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_familyrelationtype == null)
      jcasType.jcas.throwFeatMissing("familyrelationtype", "org.apache.uima.calaisType.relation.FamilyRelation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_familyrelationtype);}
    
  /** setter for familyrelationtype - sets  
   * @generated */
  public void setFamilyrelationtype(String v) {
    if (FamilyRelation_Type.featOkTst && ((FamilyRelation_Type)jcasType).casFeat_familyrelationtype == null)
      jcasType.jcas.throwFeatMissing("familyrelationtype", "org.apache.uima.calaisType.relation.FamilyRelation");
    jcasType.ll_cas.ll_setStringValue(addr, ((FamilyRelation_Type)jcasType).casFeatCode_familyrelationtype, v);}    
  }

    