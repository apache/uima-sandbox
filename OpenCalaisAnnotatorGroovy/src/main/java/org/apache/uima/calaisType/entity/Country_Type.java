
/* First created by JCasGen Mon May 26 21:43:18 EDT 2008 */
package org.apache.uima.calaisType.entity;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.calaisType.Entity_Type;

/** 
 * Updated by JCasGen Mon May 26 21:43:18 EDT 2008
 * @generated */
public class Country_Type extends Entity_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Country_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Country_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Country(addr, Country_Type.this);
  			   Country_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Country(addr, Country_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Country.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.entity.Country");
 
  /** @generated */
  final Feature casFeat_canonicalForm;
  /** @generated */
  final int     casFeatCode_canonicalForm;
  /** @generated */ 
  public String getCanonicalForm(int addr) {
        if (featOkTst && casFeat_canonicalForm == null)
      jcas.throwFeatMissing("canonicalForm", "org.apache.uima.calaisType.entity.Country");
    return ll_cas.ll_getStringValue(addr, casFeatCode_canonicalForm);
  }
  /** @generated */    
  public void setCanonicalForm(int addr, String v) {
        if (featOkTst && casFeat_canonicalForm == null)
      jcas.throwFeatMissing("canonicalForm", "org.apache.uima.calaisType.entity.Country");
    ll_cas.ll_setStringValue(addr, casFeatCode_canonicalForm, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Country_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_canonicalForm = jcas.getRequiredFeatureDE(casType, "canonicalForm", "uima.cas.String", featOkTst);
    casFeatCode_canonicalForm  = (null == casFeat_canonicalForm) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_canonicalForm).getCode();

  }
}



    