
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
public class CompanyLocation_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyLocation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyLocation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyLocation(addr, CompanyLocation_Type.this);
  			   CompanyLocation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyLocation(addr, CompanyLocation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyLocation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyLocation");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyLocation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyLocation");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_city;
  /** @generated */
  final int     casFeatCode_city;
  /** @generated */ 
  public int getCity(int addr) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyLocation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_city);
  }
  /** @generated */    
  public void setCity(int addr, int v) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyLocation");
    ll_cas.ll_setRefValue(addr, casFeatCode_city, v);}
    
  
 
  /** @generated */
  final Feature casFeat_provinceorstate;
  /** @generated */
  final int     casFeatCode_provinceorstate;
  /** @generated */ 
  public int getProvinceorstate(int addr) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyLocation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_provinceorstate);
  }
  /** @generated */    
  public void setProvinceorstate(int addr, int v) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyLocation");
    ll_cas.ll_setRefValue(addr, casFeatCode_provinceorstate, v);}
    
  
 
  /** @generated */
  final Feature casFeat_country;
  /** @generated */
  final int     casFeatCode_country;
  /** @generated */ 
  public int getCountry(int addr) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyLocation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_country);
  }
  /** @generated */    
  public void setCountry(int addr, int v) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyLocation");
    ll_cas.ll_setRefValue(addr, casFeatCode_country, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyLocation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_city = jcas.getRequiredFeatureDE(casType, "city", "org.apache.uima.calaisType.entity.City", featOkTst);
    casFeatCode_city  = (null == casFeat_city) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_city).getCode();

 
    casFeat_provinceorstate = jcas.getRequiredFeatureDE(casType, "provinceorstate", "org.apache.uima.calaisType.entity.ProvinceOrState", featOkTst);
    casFeatCode_provinceorstate  = (null == casFeat_provinceorstate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_provinceorstate).getCode();

 
    casFeat_country = jcas.getRequiredFeatureDE(casType, "country", "org.apache.uima.calaisType.entity.Country", featOkTst);
    casFeatCode_country  = (null == casFeat_country) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_country).getCode();

  }
}



    