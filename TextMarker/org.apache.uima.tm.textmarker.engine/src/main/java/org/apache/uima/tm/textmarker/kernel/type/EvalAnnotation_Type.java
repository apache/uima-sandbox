/* First created by JCasGen Tue Jan 20 17:24:50 CET 2009 */
package org.apache.uima.tm.textmarker.kernel.type;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Tue Jan 25 09:29:36 CET 2011
 * @generated */
public class EvalAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EvalAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EvalAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EvalAnnotation(addr, EvalAnnotation_Type.this);
  			   EvalAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EvalAnnotation(addr, EvalAnnotation_Type.this);
  	  }
    };

  /** @generated */
  public final static int typeIndexID = EvalAnnotation.typeIndexID;

  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tm.textmarker.kernel.type.EvalAnnotation");

  /** @generated */
  final Feature casFeat_original;
  /** @generated */
  final int     casFeatCode_original;
  /** @generated */
  public int getOriginal(int addr) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "org.apache.uima.tm.textmarker.kernel.type.EvalAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_original);
  }
  /** @generated */
  public void setOriginal(int addr, int v) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "org.apache.uima.tm.textmarker.kernel.type.EvalAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_original, v);}
    
  



  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public EvalAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_original = jcas.getRequiredFeatureDE(casType, "original", "uima.tcas.Annotation", featOkTst);
    casFeatCode_original  = (null == casFeat_original) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_original).getCode();

  }
}