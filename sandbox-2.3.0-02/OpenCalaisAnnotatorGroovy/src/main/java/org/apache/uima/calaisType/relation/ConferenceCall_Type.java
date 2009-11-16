
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
public class ConferenceCall_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ConferenceCall_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ConferenceCall_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ConferenceCall(addr, ConferenceCall_Type.this);
  			   ConferenceCall_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ConferenceCall(addr, ConferenceCall_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = ConferenceCall.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.ConferenceCall");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.ConferenceCall");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.ConferenceCall");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ccalltype;
  /** @generated */
  final int     casFeatCode_ccalltype;
  /** @generated */ 
  public String getCcalltype(int addr) {
        if (featOkTst && casFeat_ccalltype == null)
      jcas.throwFeatMissing("ccalltype", "org.apache.uima.calaisType.relation.ConferenceCall");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ccalltype);
  }
  /** @generated */    
  public void setCcalltype(int addr, String v) {
        if (featOkTst && casFeat_ccalltype == null)
      jcas.throwFeatMissing("ccalltype", "org.apache.uima.calaisType.relation.ConferenceCall");
    ll_cas.ll_setStringValue(addr, casFeatCode_ccalltype, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quarter;
  /** @generated */
  final int     casFeatCode_quarter;
  /** @generated */ 
  public String getQuarter(int addr) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.ConferenceCall");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quarter);
  }
  /** @generated */    
  public void setQuarter(int addr, String v) {
        if (featOkTst && casFeat_quarter == null)
      jcas.throwFeatMissing("quarter", "org.apache.uima.calaisType.relation.ConferenceCall");
    ll_cas.ll_setStringValue(addr, casFeatCode_quarter, v);}
    
  
 
  /** @generated */
  final Feature casFeat_status;
  /** @generated */
  final int     casFeatCode_status;
  /** @generated */ 
  public String getStatus(int addr) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.ConferenceCall");
    return ll_cas.ll_getStringValue(addr, casFeatCode_status);
  }
  /** @generated */    
  public void setStatus(int addr, String v) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.ConferenceCall");
    ll_cas.ll_setStringValue(addr, casFeatCode_status, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.ConferenceCall");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.ConferenceCall");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public ConferenceCall_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_ccalltype = jcas.getRequiredFeatureDE(casType, "ccalltype", "uima.cas.String", featOkTst);
    casFeatCode_ccalltype  = (null == casFeat_ccalltype) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ccalltype).getCode();

 
    casFeat_quarter = jcas.getRequiredFeatureDE(casType, "quarter", "uima.cas.String", featOkTst);
    casFeatCode_quarter  = (null == casFeat_quarter) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quarter).getCode();

 
    casFeat_status = jcas.getRequiredFeatureDE(casType, "status", "uima.cas.String", featOkTst);
    casFeatCode_status  = (null == casFeat_status) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_status).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

  }
}



    