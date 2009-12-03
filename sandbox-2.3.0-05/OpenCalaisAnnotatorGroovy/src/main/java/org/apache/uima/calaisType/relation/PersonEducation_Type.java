
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
public class PersonEducation_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PersonEducation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PersonEducation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PersonEducation(addr, PersonEducation_Type.this);
  			   PersonEducation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PersonEducation(addr, PersonEducation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = PersonEducation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.PersonEducation");
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public int getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonEducation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, int v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonEducation");
    ll_cas.ll_setRefValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_certification;
  /** @generated */
  final int     casFeatCode_certification;
  /** @generated */ 
  public String getCertification(int addr) {
        if (featOkTst && casFeat_certification == null)
      jcas.throwFeatMissing("certification", "org.apache.uima.calaisType.relation.PersonEducation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_certification);
  }
  /** @generated */    
  public void setCertification(int addr, String v) {
        if (featOkTst && casFeat_certification == null)
      jcas.throwFeatMissing("certification", "org.apache.uima.calaisType.relation.PersonEducation");
    ll_cas.ll_setStringValue(addr, casFeatCode_certification, v);}
    
  
 
  /** @generated */
  final Feature casFeat_degree;
  /** @generated */
  final int     casFeatCode_degree;
  /** @generated */ 
  public String getDegree(int addr) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.calaisType.relation.PersonEducation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_degree);
  }
  /** @generated */    
  public void setDegree(int addr, String v) {
        if (featOkTst && casFeat_degree == null)
      jcas.throwFeatMissing("degree", "org.apache.uima.calaisType.relation.PersonEducation");
    ll_cas.ll_setStringValue(addr, casFeatCode_degree, v);}
    
  
 
  /** @generated */
  final Feature casFeat_schoolororganization;
  /** @generated */
  final int     casFeatCode_schoolororganization;
  /** @generated */ 
  public String getSchoolororganization(int addr) {
        if (featOkTst && casFeat_schoolororganization == null)
      jcas.throwFeatMissing("schoolororganization", "org.apache.uima.calaisType.relation.PersonEducation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_schoolororganization);
  }
  /** @generated */    
  public void setSchoolororganization(int addr, String v) {
        if (featOkTst && casFeat_schoolororganization == null)
      jcas.throwFeatMissing("schoolororganization", "org.apache.uima.calaisType.relation.PersonEducation");
    ll_cas.ll_setStringValue(addr, casFeatCode_schoolororganization, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public PersonEducation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_certification = jcas.getRequiredFeatureDE(casType, "certification", "uima.cas.String", featOkTst);
    casFeatCode_certification  = (null == casFeat_certification) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_certification).getCode();

 
    casFeat_degree = jcas.getRequiredFeatureDE(casType, "degree", "uima.cas.String", featOkTst);
    casFeatCode_degree  = (null == casFeat_degree) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_degree).getCode();

 
    casFeat_schoolororganization = jcas.getRequiredFeatureDE(casType, "schoolororganization", "uima.cas.String", featOkTst);
    casFeatCode_schoolororganization  = (null == casFeat_schoolororganization) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_schoolororganization).getCode();

  }
}



    