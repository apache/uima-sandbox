
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
public class CompanyCustomer_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyCustomer_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyCustomer_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyCustomer(addr, CompanyCustomer_Type.this);
  			   CompanyCustomer_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyCustomer(addr, CompanyCustomer_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyCustomer.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyCustomer");
 
  /** @generated */
  final Feature casFeat_company_provider;
  /** @generated */
  final int     casFeatCode_company_provider;
  /** @generated */ 
  public int getCompany_provider(int addr) {
        if (featOkTst && casFeat_company_provider == null)
      jcas.throwFeatMissing("company_provider", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_provider);
  }
  /** @generated */    
  public void setCompany_provider(int addr, int v) {
        if (featOkTst && casFeat_company_provider == null)
      jcas.throwFeatMissing("company_provider", "org.apache.uima.calaisType.relation.CompanyCustomer");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_provider, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_customer;
  /** @generated */
  final int     casFeatCode_company_customer;
  /** @generated */ 
  public int getCompany_customer(int addr) {
        if (featOkTst && casFeat_company_customer == null)
      jcas.throwFeatMissing("company_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_customer);
  }
  /** @generated */    
  public void setCompany_customer(int addr, int v) {
        if (featOkTst && casFeat_company_customer == null)
      jcas.throwFeatMissing("company_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_customer, v);}
    
  
 
  /** @generated */
  final Feature casFeat_organization_customer;
  /** @generated */
  final int     casFeatCode_organization_customer;
  /** @generated */ 
  public String getOrganization_customer(int addr) {
        if (featOkTst && casFeat_organization_customer == null)
      jcas.throwFeatMissing("organization_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    return ll_cas.ll_getStringValue(addr, casFeatCode_organization_customer);
  }
  /** @generated */    
  public void setOrganization_customer(int addr, String v) {
        if (featOkTst && casFeat_organization_customer == null)
      jcas.throwFeatMissing("organization_customer", "org.apache.uima.calaisType.relation.CompanyCustomer");
    ll_cas.ll_setStringValue(addr, casFeatCode_organization_customer, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyCustomer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_provider = jcas.getRequiredFeatureDE(casType, "company_provider", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_provider  = (null == casFeat_company_provider) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_provider).getCode();

 
    casFeat_company_customer = jcas.getRequiredFeatureDE(casType, "company_customer", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_customer  = (null == casFeat_company_customer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_customer).getCode();

 
    casFeat_organization_customer = jcas.getRequiredFeatureDE(casType, "organization_customer", "uima.cas.String", featOkTst);
    casFeatCode_organization_customer  = (null == casFeat_organization_customer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_organization_customer).getCode();

  }
}



    