
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
public class Bankruptcy_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Bankruptcy_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Bankruptcy_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Bankruptcy(addr, Bankruptcy_Type.this);
  			   Bankruptcy_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Bankruptcy(addr, Bankruptcy_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Bankruptcy.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.Bankruptcy");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Bankruptcy");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.Bankruptcy");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_bankruptcystatus;
  /** @generated */
  final int     casFeatCode_bankruptcystatus;
  /** @generated */ 
  public String getBankruptcystatus(int addr) {
        if (featOkTst && casFeat_bankruptcystatus == null)
      jcas.throwFeatMissing("bankruptcystatus", "org.apache.uima.calaisType.relation.Bankruptcy");
    return ll_cas.ll_getStringValue(addr, casFeatCode_bankruptcystatus);
  }
  /** @generated */    
  public void setBankruptcystatus(int addr, String v) {
        if (featOkTst && casFeat_bankruptcystatus == null)
      jcas.throwFeatMissing("bankruptcystatus", "org.apache.uima.calaisType.relation.Bankruptcy");
    ll_cas.ll_setStringValue(addr, casFeatCode_bankruptcystatus, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Bankruptcy");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.Bankruptcy");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Bankruptcy_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_bankruptcystatus = jcas.getRequiredFeatureDE(casType, "bankruptcystatus", "uima.cas.String", featOkTst);
    casFeatCode_bankruptcystatus  = (null == casFeat_bankruptcystatus) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_bankruptcystatus).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

  }
}



    