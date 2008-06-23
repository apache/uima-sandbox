

/* First created by JCasGen Sat Dec 22 14:05:15 EST 2007 */
package org.apache.uima.conceptMapper;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.FSArray;


/** Annotation for dictionary lookup matches
 * Updated by JCasGen Tue Mar 25 15:43:47 EDT 2008
 * XML source: /OtherStuff/IBM/eclipse-apacheuima/conceptMapper/src/org/apache/uima/conceptMapper/DictTerm.xml
 * @generated */
public class DictTerm extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(DictTerm.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DictTerm() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DictTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DictTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DictTerm(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: DictCanon

  /** getter for DictCanon - gets canonical form
   * @generated */
  public String getDictCanon() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_DictCanon == null)
      jcasType.jcas.throwFeatMissing("DictCanon", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_DictCanon);}
    
  /** setter for DictCanon - sets canonical form 
   * @generated */
  public void setDictCanon(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_DictCanon == null)
      jcasType.jcas.throwFeatMissing("DictCanon", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_DictCanon, v);}    
   
    
  //*--------------*
  //* Feature: enclosingSpan

  /** getter for enclosingSpan - gets span that this NoTerm is contained within (i.e. its sentence)
   * @generated */
  public Annotation getEnclosingSpan() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "org.apache.uima.conceptMapper.DictTerm");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan)));}
    
  /** setter for enclosingSpan - sets span that this NoTerm is contained within (i.e. its sentence) 
   * @generated */
  public void setEnclosingSpan(Annotation v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: matchedText

  /** getter for matchedText - gets 
   * @generated */
  public String getMatchedText() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedText == null)
      jcasType.jcas.throwFeatMissing("matchedText", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedText);}
    
  /** setter for matchedText - sets  
   * @generated */
  public void setMatchedText(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedText == null)
      jcasType.jcas.throwFeatMissing("matchedText", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedText, v);}    
   
    
  //*--------------*
  //* Feature: matchedTokens

  /** getter for matchedTokens - gets 
   * @generated */
  public FSArray getMatchedTokens() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens)));}
    
  /** setter for matchedTokens - sets  
   * @generated */
  public void setMatchedTokens(FSArray v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for matchedTokens - gets an indexed value - 
   * @generated */
  public TOP getMatchedTokens(int i) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i)));}

  /** indexed setter for matchedTokens - sets an indexed value - 
   * @generated */
  public void setMatchedTokens(int i, TOP v) { 
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    