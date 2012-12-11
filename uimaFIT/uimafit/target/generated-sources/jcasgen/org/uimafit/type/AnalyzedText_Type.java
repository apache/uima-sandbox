
/* First created by JCasGen Tue Dec 11 16:46:33 EST 2012 */
package org.uimafit.type;

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

/** 
 * Updated by JCasGen Tue Dec 11 16:46:33 EST 2012
 * @generated */
public class AnalyzedText_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AnalyzedText_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AnalyzedText_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AnalyzedText(addr, AnalyzedText_Type.this);
  			   AnalyzedText_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AnalyzedText(addr, AnalyzedText_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = AnalyzedText.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.uimafit.type.AnalyzedText");
 
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int     casFeatCode_text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "org.uimafit.type.AnalyzedText");
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "org.uimafit.type.AnalyzedText");
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public AnalyzedText_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text  = (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_text).getCode();

  }
}



    