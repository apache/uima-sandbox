
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
public class AnalystEarningsEstimate_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AnalystEarningsEstimate_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AnalystEarningsEstimate_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AnalystEarningsEstimate(addr, AnalystEarningsEstimate_Type.this);
  			   AnalystEarningsEstimate_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AnalystEarningsEstimate(addr, AnalystEarningsEstimate_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = AnalystEarningsEstimate.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
 
  /** @generated */
  final Feature casFeat_company_source;
  /** @generated */
  final int     casFeatCode_company_source;
  /** @generated */ 
  public int getCompany_source(int addr) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_source);
  }
  /** @generated */    
  public void setCompany_source(int addr, int v) {
        if (featOkTst && casFeat_company_source == null)
      jcas.throwFeatMissing("company_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person_source;
  /** @generated */
  final int     casFeatCode_person_source;
  /** @generated */ 
  public int getPerson_source(int addr) {
        if (featOkTst && casFeat_person_source == null)
      jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person_source);
  }
  /** @generated */    
  public void setPerson_source(int addr, int v) {
        if (featOkTst && casFeat_person_source == null)
      jcas.throwFeatMissing("person_source", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    ll_cas.ll_setRefValue(addr, casFeatCode_person_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_rated;
  /** @generated */
  final int     casFeatCode_company_rated;
  /** @generated */ 
  public int getCompany_rated(int addr) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_rated);
  }
  /** @generated */    
  public void setCompany_rated(int addr, int v) {
        if (featOkTst && casFeat_company_rated == null)
      jcas.throwFeatMissing("company_rated", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_rated, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quarter;
  /** @generated */
  final int     casFeatCode_quarter;
  /** @generated */ 
  public String getQuarter(int addr) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quarter);
  }
  /** @generated */    
  public void setQuarter(int addr, String v) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    ll_cas.ll_setStringValue(addr, casFeatCode_quarter, v);}
    
  
 
  /** @generated */
  final Feature casFeat_year;
  /** @generated */
  final int     casFeatCode_year;
  /** @generated */ 
  public String getYear(int addr) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_year);
  }
  /** @generated */    
  public void setYear(int addr, String v) {
        if (featOkTst && casFeat_year == null)
      jcas.throwFeatMissing("year", "org.apache.uima.calaisType.relation.AnalystEarningsEstimate");
    ll_cas.ll_setStringValue(addr, casFeatCode_year, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public AnalystEarningsEstimate_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_source = jcas.getRequiredFeatureDE(casType, "company_source", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_source  = (null == casFeat_company_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_source).getCode();

 
    casFeat_person_source = jcas.getRequiredFeatureDE(casType, "person_source", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person_source  = (null == casFeat_person_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person_source).getCode();

 
    casFeat_company_rated = jcas.getRequiredFeatureDE(casType, "company_rated", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_rated  = (null == casFeat_company_rated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_rated).getCode();

 
    casFeat_quarter = jcas.getRequiredFeatureDE(casType, "quarter", "uima.cas.String", featOkTst);
    casFeatCode_quarter  = (null == casFeat_quarter) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quarter).getCode();

 
    casFeat_year = jcas.getRequiredFeatureDE(casType, "year", "uima.cas.String", featOkTst);
    casFeatCode_year  = (null == casFeat_year) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_year).getCode();

  }
}



    