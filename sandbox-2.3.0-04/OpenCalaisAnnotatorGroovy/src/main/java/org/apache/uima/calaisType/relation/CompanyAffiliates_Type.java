
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
public class CompanyAffiliates_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyAffiliates_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyAffiliates_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyAffiliates(addr, CompanyAffiliates_Type.this);
  			   CompanyAffiliates_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyAffiliates(addr, CompanyAffiliates_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyAffiliates.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyAffiliates");
 
  /** @generated */
  final Feature casFeat_company_affiliate;
  /** @generated */
  final int     casFeatCode_company_affiliate;
  /** @generated */ 
  public int getCompany_affiliate(int addr) {
        if (featOkTst && casFeat_company_affiliate == null)
      jcas.throwFeatMissing("company_affiliate", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_affiliate);
  }
  /** @generated */    
  public void setCompany_affiliate(int addr, int v) {
        if (featOkTst && casFeat_company_affiliate == null)
      jcas.throwFeatMissing("company_affiliate", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_affiliate, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_parent;
  /** @generated */
  final int     casFeatCode_company_parent;
  /** @generated */ 
  public int getCompany_parent(int addr) {
        if (featOkTst && casFeat_company_parent == null)
      jcas.throwFeatMissing("company_parent", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_parent);
  }
  /** @generated */    
  public void setCompany_parent(int addr, int v) {
        if (featOkTst && casFeat_company_parent == null)
      jcas.throwFeatMissing("company_parent", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_parent, v);}
    
  
 
  /** @generated */
  final Feature casFeat_relation;
  /** @generated */
  final int     casFeatCode_relation;
  /** @generated */ 
  public String getRelation(int addr) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    return ll_cas.ll_getStringValue(addr, casFeatCode_relation);
  }
  /** @generated */    
  public void setRelation(int addr, String v) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.relation.CompanyAffiliates");
    ll_cas.ll_setStringValue(addr, casFeatCode_relation, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyAffiliates_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_affiliate = jcas.getRequiredFeatureDE(casType, "company_affiliate", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_affiliate  = (null == casFeat_company_affiliate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_affiliate).getCode();

 
    casFeat_company_parent = jcas.getRequiredFeatureDE(casType, "company_parent", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_parent  = (null == casFeat_company_parent) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_parent).getCode();

 
    casFeat_relation = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.String", featOkTst);
    casFeatCode_relation  = (null == casFeat_relation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_relation).getCode();

  }
}



    