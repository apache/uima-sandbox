
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
public class JointVenture_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (JointVenture_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = JointVenture_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new JointVenture(addr, JointVenture_Type.this);
  			   JointVenture_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new JointVenture(addr, JointVenture_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = JointVenture.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.JointVenture");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.JointVenture");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.JointVenture");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
   /** @generated */
  public int getCompany(int addr, int i) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.JointVenture");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_company), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_company), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_company), i);
  }
   
  /** @generated */ 
  public void setCompany(int addr, int i, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.JointVenture");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_company), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_company), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_company), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_company_newname;
  /** @generated */
  final int     casFeatCode_company_newname;
  /** @generated */ 
  public String getCompany_newname(int addr) {
        if (featOkTst && casFeat_company_newname == null)
      jcas.throwFeatMissing("company_newname", "org.apache.uima.calaisType.relation.JointVenture");
    return ll_cas.ll_getStringValue(addr, casFeatCode_company_newname);
  }
  /** @generated */    
  public void setCompany_newname(int addr, String v) {
        if (featOkTst && casFeat_company_newname == null)
      jcas.throwFeatMissing("company_newname", "org.apache.uima.calaisType.relation.JointVenture");
    ll_cas.ll_setStringValue(addr, casFeatCode_company_newname, v);}
    
  
 
  /** @generated */
  final Feature casFeat_status;
  /** @generated */
  final int     casFeatCode_status;
  /** @generated */ 
  public String getStatus(int addr) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.JointVenture");
    return ll_cas.ll_getStringValue(addr, casFeatCode_status);
  }
  /** @generated */    
  public void setStatus(int addr, String v) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.JointVenture");
    ll_cas.ll_setStringValue(addr, casFeatCode_status, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public JointVenture_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "uima.cas.FSArray", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_company_newname = jcas.getRequiredFeatureDE(casType, "company_newname", "uima.cas.String", featOkTst);
    casFeatCode_company_newname  = (null == casFeat_company_newname) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company_newname).getCode();

 
    casFeat_status = jcas.getRequiredFeatureDE(casType, "status", "uima.cas.String", featOkTst);
    casFeatCode_status  = (null == casFeat_status) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_status).getCode();

  }
}



    