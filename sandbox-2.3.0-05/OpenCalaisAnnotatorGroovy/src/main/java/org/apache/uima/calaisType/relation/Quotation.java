

/* First created by JCasGen Mon May 26 21:43:20 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.calaisType.Relation;
import org.apache.uima.calaisType.entity.Person;


/** 
 * Updated by JCasGen Mon May 26 21:43:20 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class Quotation extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Quotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Quotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Quotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Quotation(JCas jcas) {
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
    if (Quotation_Type.featOkTst && ((Quotation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.Quotation");
    return (Person)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Quotation_Type)jcasType).casFeatCode_person)));}
    
  /** setter for person - sets  
   * @generated */
  public void setPerson(Person v) {
    if (Quotation_Type.featOkTst && ((Quotation_Type)jcasType).casFeat_person == null)
      jcasType.jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.Quotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Quotation_Type)jcasType).casFeatCode_person, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: quote

  /** getter for quote - gets 
   * @generated */
  public String getQuote() {
    if (Quotation_Type.featOkTst && ((Quotation_Type)jcasType).casFeat_quote == null)
      jcasType.jcas.throwFeatMissing("quote", "org.apache.uima.calaisType.relation.Quotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Quotation_Type)jcasType).casFeatCode_quote);}
    
  /** setter for quote - sets  
   * @generated */
  public void setQuote(String v) {
    if (Quotation_Type.featOkTst && ((Quotation_Type)jcasType).casFeat_quote == null)
      jcasType.jcas.throwFeatMissing("quote", "org.apache.uima.calaisType.relation.Quotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Quotation_Type)jcasType).casFeatCode_quote, v);}    
  }

    