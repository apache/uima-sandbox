
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
public class CompanyLegalIssues_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyLegalIssues_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyLegalIssues_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyLegalIssues(addr, CompanyLegalIssues_Type.this);
  			   CompanyLegalIssues_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyLegalIssues(addr, CompanyLegalIssues_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyLegalIssues.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyLegalIssues");
 
  /** @generated */
  final Feature casFeat_company_sued;
  /** @generated */
  final int     casFeatCode_company_sued;
  /** @generated */ 
  public int getCompany_sued(int addr) {
        if (featOkTst && casFeat_company_sued == null)
      jcas.throwFeatMissing("company_sued", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_sued);
  }
  /** @generated */    
  public void setCompany_sued(int addr, int v) {
        if (featOkTst && casFeat_company_sued == null)
      jcas.throwFeatMissing("company_sued", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_sued, v);}
    
  
 
  /** @generated */
  final Feature casFeat_sueddescription;
  /** @generated */
  final int     casFeatCode_sueddescription;
  /** @generated */ 
  public String getSueddescription(int addr) {
        if (featOkTst && casFeat_sueddescription == null)
      jcas.throwFeatMissing("sueddescription", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getStringValue(addr, casFeatCode_sueddescription);
  }
  /** @generated */    
  public void setSueddescription(int addr, String v) {
        if (featOkTst && casFeat_sueddescription == null)
      jcas.throwFeatMissing("sueddescription", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setStringValue(addr, casFeatCode_sueddescription, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_plaintiff;
  /** @generated */
  final int     casFeatCode_company_plaintiff;
  /** @generated */ 
  public int getCompany_plaintiff(int addr) {
        if (featOkTst && casFeat_company_plaintiff == null)
      jcas.throwFeatMissing("company_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_plaintiff);
  }
  /** @generated */    
  public void setCompany_plaintiff(int addr, int v) {
        if (featOkTst && casFeat_company_plaintiff == null)
      jcas.throwFeatMissing("company_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_plaintiff, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person_plaintiff;
  /** @generated */
  final int     casFeatCode_person_plaintiff;
  /** @generated */ 
  public int getPerson_plaintiff(int addr) {
        if (featOkTst && casFeat_person_plaintiff == null)
      jcas.throwFeatMissing("person_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person_plaintiff);
  }
  /** @generated */    
  public void setPerson_plaintiff(int addr, int v) {
        if (featOkTst && casFeat_person_plaintiff == null)
      jcas.throwFeatMissing("person_plaintiff", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setRefValue(addr, casFeatCode_person_plaintiff, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lawsuitclass;
  /** @generated */
  final int     casFeatCode_lawsuitclass;
  /** @generated */ 
  public String getLawsuitclass(int addr) {
        if (featOkTst && casFeat_lawsuitclass == null)
      jcas.throwFeatMissing("lawsuitclass", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lawsuitclass);
  }
  /** @generated */    
  public void setLawsuitclass(int addr, String v) {
        if (featOkTst && casFeat_lawsuitclass == null)
      jcas.throwFeatMissing("lawsuitclass", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setStringValue(addr, casFeatCode_lawsuitclass, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyLegalIssues");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyLegalIssues_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_sued = jcas.getRequiredFeatureDE(casType, "company_sued", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_sued  = (null == casFeat_company_sued) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_sued).getCode();

 
    casFeat_sueddescription = jcas.getRequiredFeatureDE(casType, "sueddescription", "uima.cas.String", featOkTst);
    casFeatCode_sueddescription  = (null == casFeat_sueddescription) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sueddescription).getCode();

 
    casFeat_company_plaintiff = jcas.getRequiredFeatureDE(casType, "company_plaintiff", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_plaintiff  = (null == casFeat_company_plaintiff) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_plaintiff).getCode();

 
    casFeat_person_plaintiff = jcas.getRequiredFeatureDE(casType, "person_plaintiff", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person_plaintiff  = (null == casFeat_person_plaintiff) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person_plaintiff).getCode();

 
    casFeat_lawsuitclass = jcas.getRequiredFeatureDE(casType, "lawsuitclass", "uima.cas.String", featOkTst);
    casFeatCode_lawsuitclass  = (null == casFeat_lawsuitclass) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lawsuitclass).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

  }
}



    