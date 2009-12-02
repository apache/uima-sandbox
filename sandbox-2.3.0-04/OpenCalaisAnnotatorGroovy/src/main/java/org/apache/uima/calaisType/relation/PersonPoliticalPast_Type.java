
/* First created by JCasGen Mon May 26 21:43:20 EDT 2008 */
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
 * Updated by JCasGen Mon May 26 21:43:20 EDT 2008
 * @generated */
public class PersonPoliticalPast_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PersonPoliticalPast_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PersonPoliticalPast_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PersonPoliticalPast(addr, PersonPoliticalPast_Type.this);
  			   PersonPoliticalPast_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PersonPoliticalPast(addr, PersonPoliticalPast_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = PersonPoliticalPast.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.PersonPoliticalPast");
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public int getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, int v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_position;
  /** @generated */
  final int     casFeatCode_position;
  /** @generated */ 
  public String getPosition(int addr) {
        if (featOkTst && casFeat_position == null)
      jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    return ll_cas.ll_getStringValue(addr, casFeatCode_position);
  }
  /** @generated */    
  public void setPosition(int addr, String v) {
        if (featOkTst && casFeat_position == null)
      jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    ll_cas.ll_setStringValue(addr, casFeatCode_position, v);}
    
  
 
  /** @generated */
  final Feature casFeat_country;
  /** @generated */
  final int     casFeatCode_country;
  /** @generated */ 
  public int getCountry(int addr) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_country);
  }
  /** @generated */    
  public void setCountry(int addr, int v) {
        if (featOkTst && casFeat_country == null)
      jcas.throwFeatMissing("country", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_country, v);}
    
  
 
  /** @generated */
  final Feature casFeat_provinceorstate;
  /** @generated */
  final int     casFeatCode_provinceorstate;
  /** @generated */ 
  public int getProvinceorstate(int addr) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_provinceorstate);
  }
  /** @generated */    
  public void setProvinceorstate(int addr, int v) {
        if (featOkTst && casFeat_provinceorstate == null)
      jcas.throwFeatMissing("provinceorstate", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_provinceorstate, v);}
    
  
 
  /** @generated */
  final Feature casFeat_city;
  /** @generated */
  final int     casFeatCode_city;
  /** @generated */ 
  public int getCity(int addr) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_city);
  }
  /** @generated */    
  public void setCity(int addr, int v) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "org.apache.uima.calaisType.relation.PersonPoliticalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_city, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public PersonPoliticalPast_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_position = jcas.getRequiredFeatureDE(casType, "position", "uima.cas.String", featOkTst);
    casFeatCode_position  = (null == casFeat_position) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_position).getCode();

 
    casFeat_country = jcas.getRequiredFeatureDE(casType, "country", "org.apache.uima.calaisType.entity.Country", featOkTst);
    casFeatCode_country  = (null == casFeat_country) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_country).getCode();

 
    casFeat_provinceorstate = jcas.getRequiredFeatureDE(casType, "provinceorstate", "org.apache.uima.calaisType.entity.ProvinceOrState", featOkTst);
    casFeatCode_provinceorstate  = (null == casFeat_provinceorstate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_provinceorstate).getCode();

 
    casFeat_city = jcas.getRequiredFeatureDE(casType, "city", "org.apache.uima.calaisType.entity.City", featOkTst);
    casFeatCode_city  = (null == casFeat_city) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_city).getCode();

  }
}



    