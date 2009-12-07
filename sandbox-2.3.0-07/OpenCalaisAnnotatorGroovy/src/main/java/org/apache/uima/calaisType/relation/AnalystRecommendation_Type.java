
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
public class AnalystRecommendation_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AnalystRecommendation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AnalystRecommendation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AnalystRecommendation(addr, AnalystRecommendation_Type.this);
  			   AnalystRecommendation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AnalystRecommendation(addr, AnalystRecommendation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = AnalystRecommendation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.AnalystRecommendation");
 
  /** @generated */
  final Feature casFeat_company_source;
  /** @generated */
  final int     casFeatCode_company_source;
  /** @generated */ 
  public int getCompany_source(int addr) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_source);
  }
  /** @generated */    
  public void setCompany_source(int addr, int v) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person_source;
  /** @generated */
  final int     casFeatCode_person_source;
  /** @generated */ 
  public int getPerson_source(int addr) {
        if (featOkTst && casFeat_person_source == null)
      jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person_source);
  }
  /** @generated */    
  public void setPerson_source(int addr, int v) {
        if (featOkTst && casFeat_person_source == null)
      jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setRefValue(addr, casFeatCode_person_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_rated;
  /** @generated */
  final int     casFeatCode_company_rated;
  /** @generated */ 
  public int getCompany_rated(int addr) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_rated);
  }
  /** @generated */    
  public void setCompany_rated(int addr, int v) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_rated, v);}
    
  
 
  /** @generated */
  final Feature casFeat_trend;
  /** @generated */
  final int     casFeatCode_trend;
  /** @generated */ 
  public String getTrend(int addr) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_trend);
  }
  /** @generated */    
  public void setTrend(int addr, String v) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setStringValue(addr, casFeatCode_trend, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rank_new;
  /** @generated */
  final int     casFeatCode_rank_new;
  /** @generated */ 
  public String getRank_new(int addr) {
        if (featOkTst && casFeat_rank_new == null)
      jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rank_new);
  }
  /** @generated */    
  public void setRank_new(int addr, String v) {
        if (featOkTst && casFeat_rank_new == null)
      jcas.throwFeatMissing("rank_new", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setStringValue(addr, casFeatCode_rank_new, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rank_old;
  /** @generated */
  final int     casFeatCode_rank_old;
  /** @generated */ 
  public String getRank_old(int addr) {
        if (featOkTst && casFeat_rank_old == null)
      jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rank_old);
  }
  /** @generated */    
  public void setRank_old(int addr, String v) {
        if (featOkTst && casFeat_rank_old == null)
      jcas.throwFeatMissing("rank_old", "org.apache.uima.calaisType.relation.AnalystRecommendation");
    ll_cas.ll_setStringValue(addr, casFeatCode_rank_old, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public AnalystRecommendation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_source = jcas.getRequiredFeatureDE(casType, "company_source", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_source  = (null == casFeat_company_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_source).getCode();

 
    casFeat_person_source = jcas.getRequiredFeatureDE(casType, "person_source", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person_source  = (null == casFeat_person_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person_source).getCode();

 
    casFeat_company_rated = jcas.getRequiredFeatureDE(casType, "company_rated", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_rated  = (null == casFeat_company_rated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_rated).getCode();

 
    casFeat_trend = jcas.getRequiredFeatureDE(casType, "trend", "uima.cas.String", featOkTst);
    casFeatCode_trend  = (null == casFeat_trend) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_trend).getCode();

 
    casFeat_rank_new = jcas.getRequiredFeatureDE(casType, "rank_new", "uima.cas.String", featOkTst);
    casFeatCode_rank_new  = (null == casFeat_rank_new) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rank_new).getCode();

 
    casFeat_rank_old = jcas.getRequiredFeatureDE(casType, "rank_old", "uima.cas.String", featOkTst);
    casFeatCode_rank_old  = (null == casFeat_rank_old) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rank_old).getCode();

  }
}



    