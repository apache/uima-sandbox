

/* First created by JCasGen Mon May 26 21:43:18 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon May 26 21:43:18 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class RelationInstance extends Instance {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(RelationInstance.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected RelationInstance() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RelationInstance(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RelationInstance(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RelationInstance(JCas jcas, int begin, int end) {
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
  //* Feature: relation

  /** getter for relation - gets 
   * @generated */
  public Relation getRelation() {
    if (RelationInstance_Type.featOkTst && ((RelationInstance_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.RelationInstance");
    return (Relation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RelationInstance_Type)jcasType).casFeatCode_relation)));}
    
  /** setter for relation - sets  
   * @generated */
  public void setRelation(Relation v) {
    if (RelationInstance_Type.featOkTst && ((RelationInstance_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.apache.uima.calaisType.RelationInstance");
    jcasType.ll_cas.ll_setRefValue(addr, ((RelationInstance_Type)jcasType).casFeatCode_relation, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    