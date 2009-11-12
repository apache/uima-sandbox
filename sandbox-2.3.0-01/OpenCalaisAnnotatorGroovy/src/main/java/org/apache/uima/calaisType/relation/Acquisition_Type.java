
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
public class Acquisition_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Acquisition_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Acquisition_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Acquisition(addr, Acquisition_Type.this);
  			   Acquisition_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Acquisition(addr, Acquisition_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Acquisition.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.Acquisition");
 
  /** @generated */
  final Feature casFeat_company_acquirer;
  /** @generated */
  final int     casFeatCode_company_acquirer;
  /** @generated */ 
  public int getCompany_acquirer(int addr) {
        if (featOkTst && casFeat_company_acquirer == null)
      jcas.throwFeatMissing("company_acquirer", "org.apache.uima.calaisType.relation.Acquisition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_acquirer);
  }
  /** @generated */    
  public void setCompany_acquirer(int addr, int v) {
        if (featOkTst && casFeat_company_acquirer == null)
      jcas.throwFeatMissing("company_acquirer", "org.apache.uima.calaisType.relation.Acquisition");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_acquirer, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company_beingacquired;
  /** @generated */
  final int     casFeatCode_company_beingacquired;
  /** @generated */ 
  public int getCompany_beingacquired(int addr) {
        if (featOkTst && casFeat_company_beingacquired == null)
      jcas.throwFeatMissing("company_beingacquired", "org.apache.uima.calaisType.relation.Acquisition");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company_beingacquired);
  }
  /** @generated */    
  public void setCompany_beingacquired(int addr, int v) {
        if (featOkTst && casFeat_company_beingacquired == null)
      jcas.throwFeatMissing("company_beingacquired", "org.apache.uima.calaisType.relation.Acquisition");
    ll_cas.ll_setRefValue(addr, casFeatCode_company_beingacquired, v);}
    
  
 
  /** @generated */
  final Feature casFeat_status;
  /** @generated */
  final int     casFeatCode_status;
  /** @generated */ 
  public String getStatus(int addr) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Acquisition");
    return ll_cas.ll_getStringValue(addr, casFeatCode_status);
  }
  /** @generated */    
  public void setStatus(int addr, String v) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.Acquisition");
    ll_cas.ll_setStringValue(addr, casFeatCode_status, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Acquisition_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company_acquirer = jcas.getRequiredFeatureDE(casType, "company_acquirer", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_acquirer  = (null == casFeat_company_acquirer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_acquirer).getCode();

 
    casFeat_company_beingacquired = jcas.getRequiredFeatureDE(casType, "company_beingacquired", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company_beingacquired  = (null == casFeat_company_beingacquired) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_beingacquired).getCode();

 
    casFeat_status = jcas.getRequiredFeatureDE(casType, "status", "uima.cas.String", featOkTst);
    casFeatCode_status  = (null == casFeat_status) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_status).getCode();

  }
}



    