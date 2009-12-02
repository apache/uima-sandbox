
/* First created by JCasGen Mon May 26 21:43:20 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** Returned value from web service in RDF form
 * Updated by JCasGen Mon May 26 21:43:20 EDT 2008
 * @generated */
public class RdfText_Type extends TOP_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RdfText_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RdfText_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RdfText(addr, RdfText_Type.this);
  			   RdfText_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RdfText(addr, RdfText_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = RdfText.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.RdfText");
 
  /** @generated */
  final Feature casFeat_rdfText;
  /** @generated */
  final int     casFeatCode_rdfText;
  /** @generated */ 
  public String getRdfText(int addr) {
        if (featOkTst && casFeat_rdfText == null)
      jcas.throwFeatMissing("rdfText", "org.apache.uima.calaisType.RdfText");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rdfText);
  }
  /** @generated */    
  public void setRdfText(int addr, String v) {
        if (featOkTst && casFeat_rdfText == null)
      jcas.throwFeatMissing("rdfText", "org.apache.uima.calaisType.RdfText");
    ll_cas.ll_setStringValue(addr, casFeatCode_rdfText, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RdfText_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rdfText = jcas.getRequiredFeatureDE(casType, "rdfText", "uima.cas.String", featOkTst);
    casFeatCode_rdfText  = (null == casFeat_rdfText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rdfText).getCode();

  }
}



    