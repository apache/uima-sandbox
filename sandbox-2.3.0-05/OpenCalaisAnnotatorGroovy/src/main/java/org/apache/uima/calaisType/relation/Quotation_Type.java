
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
public class Quotation_Type extends Relation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Quotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Quotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Quotation(addr, Quotation_Type.this);
  			   Quotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Quotation(addr, Quotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Quotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.relation.Quotation");
 
  /** @generated */
  final Feature casFeat_person;
  /** @generated */
  final int     casFeatCode_person;
  /** @generated */ 
  public int getPerson(int addr) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.Quotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_person);
  }
  /** @generated */    
  public void setPerson(int addr, int v) {
        if (featOkTst && casFeat_person == null)
      jcas.throwFeatMissing("person", "org.apache.uima.calaisType.relation.Quotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_person, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quote;
  /** @generated */
  final int     casFeatCode_quote;
  /** @generated */ 
  public String getQuote(int addr) {
        if (featOkTst && casFeat_quote == null)
      jcas.throwFeatMissing("quote", "org.apache.uima.calaisType.relation.Quotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quote);
  }
  /** @generated */    
  public void setQuote(int addr, String v) {
        if (featOkTst && casFeat_quote == null)
      jcas.throwFeatMissing("quote", "org.apache.uima.calaisType.relation.Quotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_quote, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Quotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_person = jcas.getRequiredFeatureDE(casType, "person", "org.apache.uima.calaisType.entity.Person", featOkTst);
    casFeatCode_person  = (null == casFeat_person) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_person).getCode();

 
    casFeat_quote = jcas.getRequiredFeatureDE(casType, "quote", "uima.cas.String", featOkTst);
    casFeatCode_quote  = (null == casFeat_quote) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quote).getCode();

  }
}



    