
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
public class CompanyEarningsGuidance_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyEarningsGuidance_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyEarningsGuidance_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyEarningsGuidance(addr, CompanyEarningsGuidance_Type.this);
  			   CompanyEarningsGuidance_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyEarningsGuidance(addr, CompanyEarningsGuidance_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyEarningsGuidance.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quarter;
  /** @generated */
  final int     casFeatCode_quarter;
  /** @generated */ 
  public String getQuarter(int addr) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quarter);
  }
  /** @generated */    
  public void setQuarter(int addr, String v) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    ll_cas.ll_setStringValue(addr, casFeatCode_quarter, v);}
    
  
 
  /** @generated */
  final Feature casFeat_year;
  /** @generated */
  final int     casFeatCode_year;
  /** @generated */ 
  public String getYear(int addr) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return ll_cas.ll_getStringValue(addr, casFeatCode_year);
  }
  /** @generated */    
  public void setYear(int addr, String v) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    ll_cas.ll_setStringValue(addr, casFeatCode_year, v);}
    
  
 
  /** @generated */
  final Feature casFeat_trend;
  /** @generated */
  final int     casFeatCode_trend;
  /** @generated */ 
  public String getTrend(int addr) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    return ll_cas.ll_getStringValue(addr, casFeatCode_trend);
  }
  /** @generated */    
  public void setTrend(int addr, String v) {
        if (featOkTst && casFeat_trend == null)
      jcas.throwFeatMissing("trend", "org.apache.uima.calaisType.relation.CompanyEarningsGuidance");
    ll_cas.ll_setStringValue(addr, casFeatCode_trend, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyEarningsGuidance_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_quarter = jcas.getRequiredFeatureDE(casType, "quarter", "uima.cas.String", featOkTst);
    casFeatCode_quarter  = (null == casFeat_quarter) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quarter).getCode();

 
    casFeat_year = jcas.getRequiredFeatureDE(casType, "year", "uima.cas.String", featOkTst);
    casFeatCode_year  = (null == casFeat_year) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_year).getCode();

 
    casFeat_trend = jcas.getRequiredFeatureDE(casType, "trend", "uima.cas.String", featOkTst);
    casFeatCode_trend  = (null == casFeat_trend) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_trend).getCode();

  }
}



    