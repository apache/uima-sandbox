

/* First created by JCasGen Mon May 26 21:43:18 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon May 26 21:43:18 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class EntityInstance extends Instance {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(EntityInstance.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected EntityInstance() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public EntityInstance(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public EntityInstance(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public EntityInstance(JCas jcas, int begin, int end) {
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
  //* Feature: entity

  /** getter for entity - gets 
   * @generated */
  public Entity getEntity() {
    if (EntityInstance_Type.featOkTst && ((EntityInstance_Type)jcasType).casFeat_entity == null)
      jcasType.jcas.throwFeatMissing("entity", "org.apache.uima.calaisType.EntityInstance");
    return (Entity)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EntityInstance_Type)jcasType).casFeatCode_entity)));}
    
  /** setter for entity - sets  
   * @generated */
  public void setEntity(Entity v) {
    if (EntityInstance_Type.featOkTst && ((EntityInstance_Type)jcasType).casFeat_entity == null)
      jcasType.jcas.throwFeatMissing("entity", "org.apache.uima.calaisType.EntityInstance");
    jcasType.ll_cas.ll_setRefValue(addr, ((EntityInstance_Type)jcasType).casFeatCode_entity, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    