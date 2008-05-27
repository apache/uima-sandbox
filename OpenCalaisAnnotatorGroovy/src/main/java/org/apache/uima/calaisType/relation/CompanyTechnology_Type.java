
/* First created by JCasGen Mon May 26 21:43:19 EDT 2008 */
package org.apache.uima.calaisType.relation;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.calaisType.Relation_Type;

/** 
 * Updated by JCasGen Mon May 26 21:43:19 EDT 2008
 * @generated */
public class CompanyTechnology_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyTechnology_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyTechnology_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyTechnology(addr, CompanyTechnology_Type.this);
  			   CompanyTechnology_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyTechnology(addr, CompanyTechnology_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyTechnology.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyTechnology");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyTechnology");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyTechnology");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_technology;
  /** @generated */
  final int     casFeatCode_technology;
  /** @generated */ 
  public String getTechnology(int addr) {
        if (featOkTst && casFeat_technology == null)
      jcas.throwFeatMissing("technology", "org.apache.uima.calaisType.relation.CompanyTechnology");
    return ll_cas.ll_getStringValue(addr, casFeatCode_technology);
  }
  /** @generated */    
  public void setTechnology(int addr, String v) {
        if (featOkTst && casFeat_technology == null)
      jcas.throwFeatMissing("technology", "org.apache.uima.calaisType.relation.CompanyTechnology");
    ll_cas.ll_setStringValue(addr, casFeatCode_technology, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyTechnology_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_technology = jcas.getRequiredFeatureDE(casType, "technology", "uima.cas.String", featOkTst);
    casFeatCode_technology  = (null == casFeat_technology) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_technology).getCode();

  }
}



    