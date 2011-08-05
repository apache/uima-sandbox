/* First created by JCasGen Fri Jun 27 14:32:41 CEST 2008 */
package org.apache.uima.tm.textmarker.kernel.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Tue Jan 25 09:29:36 CET 2011
 * XML source: D:/work/workspace-tm/org.apache.uima.tm.textmarker.engine/desc/InternalTypeSystem.xml
 * @generated */
public class DebugRuleMatch extends ProfiledAnnotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(DebugRuleMatch.class);

  /**
   * @generated
   * @ordered
   */
  public final static int type = typeIndexID;

  /** @generated */
  @Override
  public int getTypeIndexID() {return typeIndexID;}
 
  /**
   * Never called. Disable default constructor
   * 
   * @generated
   */
  protected DebugRuleMatch() {}
    
  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public DebugRuleMatch(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DebugRuleMatch(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */
  public DebugRuleMatch(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {
  }

  // *--------------*
  // * Feature: Elements

  /**
   * getter for Elements - gets
   * 
   * @generated
   */
  public FSArray getElements() {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_elements == null)
      jcasType.jcas.throwFeatMissing("elements", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements)));}
    
  /**
   * setter for Elements - sets
   * 
   * @generated
   */
  public void setElements(FSArray v) {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_elements == null)
      jcasType.jcas.throwFeatMissing("elements", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.ll_cas.ll_setRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /**
   * indexed getter for Elements - gets an indexed value -
   * 
   * @generated
   */
  public DebugRuleElementMatches getElements(int i) {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_elements == null)
      jcasType.jcas.throwFeatMissing("elements", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements), i);
    return (DebugRuleElementMatches)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements), i)));}

  /**
   * indexed setter for Elements - sets an indexed value -
   * 
   * @generated
   */
  public void setElements(int i, DebugRuleElementMatches v) { 
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_elements == null)
      jcasType.jcas.throwFeatMissing("elements", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_elements), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  // *--------------*
  // * Feature: Matched

  /**
   * getter for Matched - gets
   * 
   * @generated
   */
  public boolean getMatched() {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_matched == null)
      jcasType.jcas.throwFeatMissing("matched", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_matched);}
    
  /**
   * setter for Matched - sets
   * 
   * @generated
   */
  public void setMatched(boolean v) {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_matched == null)
      jcasType.jcas.throwFeatMissing("matched", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_matched, v);}    
   
    
  // *--------------*
  // * Feature: Delegates

  /**
   * getter for Delegates - gets
   * 
   * @generated
   */
  public FSArray getDelegates() {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_delegates == null)
      jcasType.jcas.throwFeatMissing("delegates", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates)));}
    
  /**
   * setter for Delegates - sets
   * 
   * @generated
   */
  public void setDelegates(FSArray v) {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_delegates == null)
      jcasType.jcas.throwFeatMissing("delegates", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.ll_cas.ll_setRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /**
   * indexed getter for Delegates - gets an indexed value -
   * 
   * @generated
   */
  public DebugScriptApply getDelegates(int i) {
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_delegates == null)
      jcasType.jcas.throwFeatMissing("delegates", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates), i);
    return (DebugScriptApply)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates), i)));}

  /**
   * indexed setter for Delegates - sets an indexed value -
   * 
   * @generated
   */
  public void setDelegates(int i, DebugScriptApply v) { 
    if (DebugRuleMatch_Type.featOkTst && ((DebugRuleMatch_Type)jcasType).casFeat_delegates == null)
      jcasType.jcas.throwFeatMissing("delegates", "org.apache.uima.tm.textmarker.kernel.type.DebugRuleMatch");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DebugRuleMatch_Type)jcasType).casFeatCode_delegates), i, jcasType.ll_cas.ll_getFSRef(v));}
  }