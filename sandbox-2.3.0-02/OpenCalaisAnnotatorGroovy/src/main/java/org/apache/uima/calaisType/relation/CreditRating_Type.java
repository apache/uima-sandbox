
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
public class CreditRating_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CreditRating_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CreditRating_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CreditRating(addr, CreditRating_Type.this);
  			   CreditRating_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CreditRating(addr, CreditRating_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CreditRating.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CreditRating");
 
  /** @generated */
  final Feature casFeat_company_source;
  /** @generated */
  final int     casFeatCode_company_source;
  /** @generated */ 
  public int getCompany_source(int addr) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_source);
  }
  /** @generated */    
  public void setCompany_source(int addr, int v) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_rated;
  /** @generated */
  final int     casFeatCode_company_rated;
  /** @generated */ 
  public int getCompany_rated(int addr) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_rated);
  }
  /** @generated */    
  public void setCompany_rated(int addr, int v) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_rated, v);}
    
  
 
  /** @generated */
  final Feature casFeat_organization_rated;
  /** @generated */
  final int     casFeatCode_organization_rated;
  /** @generated */ 
  public int getOrganization_rated(int addr) {
        if (featOkTst && casFeat_organization_rated == null)
      jcas.throwFeatMissing("organization_rated", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getRefValue(addr, casFeatCode_organization_rated);
  }
  /** @generated */    
  public void setOrganization_rated(int addr, int v) {
        if (featOkTst && casFeat_organization_rated == null)
      jcas.throwFeatMissing("organization_rated", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setRefValue(addr, casFeatCode_organization_rated, v);}
    
  
 
  /** @generated */
  final Feature casFeat_trend;
  /** @generated */
  final int     casFeatCode_trend;
  /** @generated */ 
  public String getTrend(int addr) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getStringValue(addr, casFeatCode_trend);
  }
  /** @generated */    
  public void setTrend(int addr, String v) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setStringValue(addr, casFeatCode_trend, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rank_new;
  /** @generated */
  final int     casFeatCode_rank_new;
  /** @generated */ 
  public String getRank_new(int addr) {
        if (featOkTst && casFeat_rank_new == null)
      jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rank_new);
  }
  /** @generated */    
  public void setRank_new(int addr, String v) {
        if (featOkTst && casFeat_rank_new == null)
      jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setStringValue(addr, casFeatCode_rank_new, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rank_old;
  /** @generated */
  final int     casFeatCode_rank_old;
  /** @generated */ 
  public String getRank_old(int addr) {
        if (featOkTst && casFeat_rank_old == null)
      jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.CreditRating");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rank_old);
  }
  /** @generated */    
  public void setRank_old(int addr, String v) {
        if (featOkTst && casFeat_rank_old == null)
      jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.CreditRating");
    ll_cas.ll_setStringValue(addr, casFeatCode_rank_old, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CreditRating_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_source = jcas.getRequiredFeatureDE(casType, "company_source", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_source  = (null == casFeat_company_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_source).getCode();

 
    casFeat_company_rated = jcas.getRequiredFeatureDE(casType, "company_rated", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_rated  = (null == casFeat_company_rated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_rated).getCode();

 
    casFeat_organization_rated = jcas.getRequiredFeatureDE(casType, "organization_rated", "org.apache.uima.calaisType.entity.Organization", featOkTst);
    casFeatCode_organization_rated  = (null == casFeat_organization_rated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_organization_rated).getCode();

 
    casFeat_trend = jcas.getRequiredFeatureDE(casType, "trend", "uima.cas.String", featOkTst);
    casFeatCode_trend  = (null == casFeat_trend) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_trend).getCode();

 
    casFeat_rank_new = jcas.getRequiredFeatureDE(casType, "rank_new", "uima.cas.String", featOkTst);
    casFeatCode_rank_new  = (null == casFeat_rank_new) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rank_new).getCode();

 
    casFeat_rank_old = jcas.getRequiredFeatureDE(casType, "rank_old", "uima.cas.String", featOkTst);
    casFeatCode_rank_old  = (null == casFeat_rank_old) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rank_old).getCode();

  }
}



    