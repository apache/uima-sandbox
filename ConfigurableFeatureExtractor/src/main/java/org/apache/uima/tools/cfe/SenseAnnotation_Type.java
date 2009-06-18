
/* First created by JCasGen Fri Mar 07 11:37:18 EST 2008 */
package org.apache.uima.tools.cfe;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** contains manually preannotated sense, you can derive this with your own type, that might have additional information
 * Updated by JCasGen Tue Mar 18 14:49:11 EDT 2008
 * @generated */
public class SenseAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SenseAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SenseAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SenseAnnotation(addr, SenseAnnotation_Type.this);
  			   SenseAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SenseAnnotation(addr, SenseAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = SenseAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.cfe.SenseAnnotation");
 
  /** @generated */
  final Feature casFeat_SENSE;
  /** @generated */
  final int     casFeatCode_SENSE;
  /** @generated */ 
  public String getSENSE(int addr) {
        if (featOkTst && casFeat_SENSE == null)
      jcas.throwFeatMissing("SENSE", "org.apache.uima.cfe.SenseAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_SENSE);
  }
  /** @generated */    
  public void setSENSE(int addr, String v) {
        if (featOkTst && casFeat_SENSE == null)
      jcas.throwFeatMissing("SENSE", "org.apache.uima.cfe.SenseAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_SENSE, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SenseAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_SENSE = jcas.getRequiredFeatureDE(casType, "SENSE", "uima.cas.String", featOkTst);
    casFeatCode_SENSE  = (null == casFeat_SENSE) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_SENSE).getCode();

  }
}



    