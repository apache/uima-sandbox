
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
public class FamilyRelation_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (FamilyRelation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = FamilyRelation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new FamilyRelation(addr, FamilyRelation_Type.this);
  			   FamilyRelation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new FamilyRelation(addr, FamilyRelation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = FamilyRelation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.FamilyRelation");
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public int getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.FamilyRelation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, int v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.FamilyRelation");
    ll_cas.ll_setRefValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_person_relative;
  /** @generated */
  final int     casFeatCode_person_relative;
  /** @generated */ 
  public String getPerson_relative(int addr) {
        if (featOkTst && casFeat_person_relative == null)
      jcas.throwFeatMissing("person_relative", "org.apache.uima.calaisType.relation.FamilyRelation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_person_relative);
  }
  /** @generated */    
  public void setPerson_relative(int addr, String v) {
        if (featOkTst && casFeat_person_relative == null)
      jcas.throwFeatMissing("person_relative", "org.apache.uima.calaisType.relation.FamilyRelation");
    ll_cas.ll_setStringValue(addr, casFeatCode_person_relative, v);}
    
  
 
  /** @generated */
  final Feature casFeat_familyrelationtype;
  /** @generated */
  final int     casFeatCode_familyrelationtype;
  /** @generated */ 
  public String getFamilyrelationtype(int addr) {
        if (featOkTst && casFeat_familyrelationtype == null)
      jcas.throwFeatMissing("familyrelationtype", "org.apache.uima.calaisType.relation.FamilyRelation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_familyrelationtype);
  }
  /** @generated */    
  public void setFamilyrelationtype(int addr, String v) {
        if (featOkTst && casFeat_familyrelationtype == null)
      jcas.throwFeatMissing("familyrelationtype", "org.apache.uima.calaisType.relation.FamilyRelation");
    ll_cas.ll_setStringValue(addr, casFeatCode_familyrelationtype, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public FamilyRelation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_person_relative = jcas.getRequiredFeatureDE(casType, "person_relative", "uima.cas.String", featOkTst);
    casFeatCode_person_relative  = (null == casFeat_person_relative) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person_relative).getCode();

 
    casFeat_familyrelationtype = jcas.getRequiredFeatureDE(casType, "familyrelationtype", "uima.cas.String", featOkTst);
    casFeatCode_familyrelationtype  = (null == casFeat_familyrelationtype) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_familyrelationtype).getCode();

  }
}



    