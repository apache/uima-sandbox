

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
public class Acquisition extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Acquisition.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Acquisition() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Acquisition(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Acquisition(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: company_acquirer

  /** getter for company_acquirer - gets 
   * @generated */
  public Company getCompany_acquirer() {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_company_acquirer == null)
      jcasType.jcas.throwFeatMissing("company_acquirer", "org.apache.uima.calaisType.relation.Acquisition");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Acquisition_Type)jcasType).casFeatCode_company_acquirer)));}
    
  /** setter for company_acquirer - sets  
   * @generated */
  public void setCompany_acquirer(Company v) {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_company_acquirer == null)
      jcasType.jcas.throwFeatMissing("company_acquirer", "org.apache.uima.calaisType.relation.Acquisition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Acquisition_Type)jcasType).casFeatCode_company_acquirer, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: company_beingacquired

  /** getter for company_beingacquired - gets 
   * @generated */
  public Company getCompany_beingacquired() {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_company_beingacquired == null)
      jcasType.jcas.throwFeatMissing("company_beingacquired", "org.apache.uima.calaisType.relation.Acquisition");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Acquisition_Type)jcasType).casFeatCode_company_beingacquired)));}
    
  /** setter for company_beingacquired - sets  
   * @generated */
  public void setCompany_beingacquired(Company v) {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_company_beingacquired == null)
      jcasType.jcas.throwFeatMissing("company_beingacquired", "org.apache.uima.calaisType.relation.Acquisition");
    jcasType.ll_cas.ll_setRefValue(addr, ((Acquisition_Type)jcasType).casFeatCode_company_beingacquired, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: status

  /** getter for status - gets 
   * @generated */
  public String getStatus() {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Acquisition");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Acquisition_Type)jcasType).casFeatCode_status);}
    
  /** setter for status - sets  
   * @generated */
  public void setStatus(String v) {
    if (Acquisition_Type.featOkTst && ((Acquisition_Type)jcasType).casFeat_status == null)
      jcasType.jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Acquisition");
    jcasType.ll_cas.ll_setStringValue(addr, ((Acquisition_Type)jcasType).casFeatCode_status, v);}    
  }

    