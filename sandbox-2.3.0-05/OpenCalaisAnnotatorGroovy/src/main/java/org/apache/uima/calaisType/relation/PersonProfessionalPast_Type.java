
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
public class PersonProfessionalPast_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PersonProfessionalPast_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PersonProfessionalPast_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PersonProfessionalPast(addr, PersonProfessionalPast_Type.this);
  			   PersonProfessionalPast_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PersonProfessionalPast(addr, PersonProfessionalPast_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = PersonProfessionalPast.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.PersonProfessionalPast");
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public int getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, int v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_position;
  /** @generated */
  final int     casFeatCode_position;
  /** @generated */ 
  public String getPosition(int addr) {
        if (featOkTst && casFeat_position == null)
      jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return ll_cas.ll_getStringValue(addr, casFeatCode_position);
  }
  /** @generated */    
  public void setPosition(int addr, String v) {
        if (featOkTst && casFeat_position == null)
      jcas.throwFeatMissing("position", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    ll_cas.ll_setStringValue(addr, casFeatCode_position, v);}
    
  
 
  /** @generated */
  final Feature casFeat_company;
  /** @generated */
  final int     casFeatCode_company;
  /** @generated */ 
  public int getCompany(int addr) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_company);
  }
  /** @generated */    
  public void setCompany(int addr, int v) {
        if (featOkTst && casFeat_company == null)
      jcas.throwFeatMissing("company", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_company, v);}
    
  
 
  /** @generated */
  final Feature casFeat_organization;
  /** @generated */
  final int     casFeatCode_organization;
  /** @generated */ 
  public int getOrganization(int addr) {
        if (featOkTst && casFeat_organization == null)
      jcas.throwFeatMissing("organization", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    return ll_cas.ll_getRefValue(addr, casFeatCode_organization);
  }
  /** @generated */    
  public void setOrganization(int addr, int v) {
        if (featOkTst && casFeat_organization == null)
      jcas.throwFeatMissing("organization", "org.apache.uima.calaisType.relation.PersonProfessionalPast");
    ll_cas.ll_setRefValue(addr, casFeatCode_organization, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public PersonProfessionalPast_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_position = jcas.getRequiredFeatureDE(casType, "position", "uima.cas.String", featOkTst);
    casFeatCode_position  = (null == casFeat_position) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_position).getCode();

 
    casFeat_company = jcas.getRequiredFeatureDE(casType, "company", "org.apache.uima.calaisType.entity.Company", featOkTst);
    casFeatCode_company  = (null == casFeat_company) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_company).getCode();

 
    casFeat_organization = jcas.getRequiredFeatureDE(casType, "organization", "org.apache.uima.calaisType.entity.Organization", featOkTst);
    casFeatCode_organization  = (null == casFeat_organization) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_organization).getCode();

  }
}



    