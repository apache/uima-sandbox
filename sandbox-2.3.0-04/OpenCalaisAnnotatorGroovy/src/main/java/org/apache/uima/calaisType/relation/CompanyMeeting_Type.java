
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
public class CompanyMeeting_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CompanyMeeting_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CompanyMeeting_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CompanyMeeting(addr, CompanyMeeting_Type.this);
  			   CompanyMeeting_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CompanyMeeting(addr, CompanyMeeting_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CompanyMeeting.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.CompanyMeeting");
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_companymeetingtype;
  /** @generated */
  final int     casFeatCode_companymeetingtype;
  /** @generated */ 
  public String getCompanymeetingtype(int addr) {
        if (featOkTst && casFeat_companymeetingtype == null)
      jcas.throwFeatMissing("companymeetingtype", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getStringValue(addr, casFeatCode_companymeetingtype);
  }
  /** @generated */    
  public void setCompanymeetingtype(int addr, String v) {
        if (featOkTst && casFeat_companymeetingtype == null)
      jcas.throwFeatMissing("companymeetingtype", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setStringValue(addr, casFeatCode_companymeetingtype, v);}
    
  
 
  /** @generated */
  final Feature casFeat_country;
  /** @generated */
  final int     casFeatCode_country;
  /** @generated */ 
  public int getCountry(int addr) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_country);
  }
  /** @generated */    
  public void setCountry(int addr, int v) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_country, v);}
    
  
 
  /** @generated */
  final Feature casFeat_city;
  /** @generated */
  final int     casFeatCode_city;
  /** @generated */ 
  public int getCity(int addr) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_city);
  }
  /** @generated */    
  public void setCity(int addr, int v) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_city, v);}
    
  
 
  /** @generated */
  final Feature casFeat_provinceorstate;
  /** @generated */
  final int     casFeatCode_provinceorstate;
  /** @generated */ 
  public int getProvinceorstate(int addr) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_provinceorstate);
  }
  /** @generated */    
  public void setProvinceorstate(int addr, int v) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_provinceorstate, v);}
    
  
 
  /** @generated */
  final Feature casFeat_status;
  /** @generated */
  final int     casFeatCode_status;
  /** @generated */ 
  public String getStatus(int addr) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getStringValue(addr, casFeatCode_status);
  }
  /** @generated */    
  public void setStatus(int addr, String v) {
        if (featOkTst && casFeat_status == null)
      jcas.throwFeatMissing("status", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setStringValue(addr, casFeatCode_status, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  
 
  /** @generated */
  final Feature casFeat_meetingsite;
  /** @generated */
  final int     casFeatCode_meetingsite;
  /** @generated */ 
  public String getMeetingsite(int addr) {
        if (featOkTst && casFeat_meetingsite == null)
      jcas.throwFeatMissing("meetingsite", "org.apache.uima.calaisType.relation.CompanyMeeting");
    return ll_cas.ll_getStringValue(addr, casFeatCode_meetingsite);
  }
  /** @generated */    
  public void setMeetingsite(int addr, String v) {
        if (featOkTst && casFeat_meetingsite == null)
      jcas.throwFeatMissing("meetingsite", "org.apache.uima.calaisType.relation.CompanyMeeting");
    ll_cas.ll_setStringValue(addr, casFeatCode_meetingsite, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CompanyMeeting_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_companymeetingtype = jcas.getRequiredFeatureDE(casType, "companymeetingtype", "uima.cas.String", featOkTst);
    casFeatCode_companymeetingtype  = (null == casFeat_companymeetingtype) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_companymeetingtype).getCode();

 
    casFeat_country = jcas.getRequiredFeatureDE(casType, "country", "org.apache.uima.calaisType.entity.Country", featOkTst);
    casFeatCode_country  = (null == casFeat_country) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_country).getCode();

 
    casFeat_city = jcas.getRequiredFeatureDE(casType, "city", "org.apache.uima.calaisType.entity.City", featOkTst);
    casFeatCode_city  = (null == casFeat_city) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_city).getCode();

 
    casFeat_provinceorstate = jcas.getRequiredFeatureDE(casType, "provinceorstate", "org.apache.uima.calaisType.entity.ProvinceOrState", featOkTst);
    casFeatCode_provinceorstate  = (null == casFeat_provinceorstate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_provinceorstate).getCode();

 
    casFeat_status = jcas.getRequiredFeatureDE(casType, "status", "uima.cas.String", featOkTst);
    casFeatCode_status  = (null == casFeat_status) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_status).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

 
    casFeat_meetingsite = jcas.getRequiredFeatureDE(casType, "meetingsite", "uima.cas.String", featOkTst);
    casFeatCode_meetingsite  = (null == casFeat_meetingsite) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_meetingsite).getCode();

  }
}



    