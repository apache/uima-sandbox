

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
public class Buybacks extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Buybacks.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Buybacks() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Buybacks(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Buybacks(JCas jcas) {
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
    if (Buybacks_Type.featOkTst && ((Buybacks_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Buybacks");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Buybacks_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (Buybacks_Type.featOkTst && ((Buybacks_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Buybacks");
    jcasType.ll_cas.ll_setRefValue(addr, ((Buybacks_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public String getDate() {
    if (Buybacks_Type.featOkTst && ((Buybacks_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Buybacks");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Buybacks_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(String v) {
    if (Buybacks_Type.featOkTst && ((Buybacks_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Buybacks");
    jcasType.ll_cas.ll_setStringValue(addr, ((Buybacks_Type)jcasType).casFeatCode_date, v);}    
  }

    