

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
public class Bankruptcy extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Bankruptcy.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Bankruptcy() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Bankruptcy(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Bankruptcy(JCas jcas) {
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
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Bankruptcy");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Bankruptcy");
    jcasType.ll_cas.ll_setRefValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: bankruptcystatus

  /** getter for bankruptcystatus - gets 
   * @generated */
  public String getBankruptcystatus() {
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_bankruptcystatus == null)
      jcasType.jcas.throwFeatMissing("bankruptcystatus", "org.apache.uima.calaisType.relation.Bankruptcy");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_bankruptcystatus);}
    
  /** setter for bankruptcystatus - sets  
   * @generated */
  public void setBankruptcystatus(String v) {
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_bankruptcystatus == null)
      jcasType.jcas.throwFeatMissing("bankruptcystatus", "org.apache.uima.calaisType.relation.Bankruptcy");
    jcasType.ll_cas.ll_setStringValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_bankruptcystatus, v);}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public String getDate() {
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Bankruptcy");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(String v) {
    if (Bankruptcy_Type.featOkTst && ((Bankruptcy_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Bankruptcy");
    jcasType.ll_cas.ll_setStringValue(addr, ((Bankruptcy_Type)jcasType).casFeatCode_date, v);}    
  }

    