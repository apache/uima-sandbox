

/* First created by JCasGen Tue Dec 11 16:46:33 EST 2012 */
package org.uimafit.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 11 16:46:33 EST 2012
 * XML source: file:/C:/au/uimaFit/import/grant-staging/uimaFIT/src/test/resources/org/uimafit/type/AnalyzedText.xml
 * @generated */
public class AnalyzedText extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(AnalyzedText.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected AnalyzedText() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public AnalyzedText(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public AnalyzedText(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public AnalyzedText(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (AnalyzedText_Type.featOkTst && ((AnalyzedText_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.uimafit.type.AnalyzedText");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnalyzedText_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (AnalyzedText_Type.featOkTst && ((AnalyzedText_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "org.uimafit.type.AnalyzedText");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnalyzedText_Type)jcasType).casFeatCode_text, v);}    
  }

    