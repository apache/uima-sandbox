

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
public class CompanyTechnology extends Relation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(CompanyTechnology.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CompanyTechnology() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CompanyTechnology(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CompanyTechnology(JCas jcas) {
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
    if (CompanyTechnology_Type.featOkTst && ((CompanyTechnology_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyTechnology");
    return (Company)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CompanyTechnology_Type)jcasType).casFeatCode_company)));}
    
  /** setter for company - sets  
   * @generated */
  public void setCompany(Company v) {
    if (CompanyTechnology_Type.featOkTst && ((CompanyTechnology_Type)jcasType).casFeat_company == null)
      jcasType.jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyTechnology");
    jcasType.ll_cas.ll_setRefValue(addr, ((CompanyTechnology_Type)jcasType).casFeatCode_company, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: technology

  /** getter for technology - gets 
   * @generated */
  public String getTechnology() {
    if (CompanyTechnology_Type.featOkTst && ((CompanyTechnology_Type)jcasType).casFeat_technology == null)
      jcasType.jcas.throwFeatMissing("technology", "org.apache.uima.calaisType.relation.CompanyTechnology");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CompanyTechnology_Type)jcasType).casFeatCode_technology);}
    
  /** setter for technology - sets  
   * @generated */
  public void setTechnology(String v) {
    if (CompanyTechnology_Type.featOkTst && ((CompanyTechnology_Type)jcasType).casFeat_technology == null)
      jcasType.jcas.throwFeatMissing("technology", "org.apache.uima.calaisType.relation.CompanyTechnology");
    jcasType.ll_cas.ll_setStringValue(addr, ((CompanyTechnology_Type)jcasType).casFeatCode_technology, v);}    
  }

    