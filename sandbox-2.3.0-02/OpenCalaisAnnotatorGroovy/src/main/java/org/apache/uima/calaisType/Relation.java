

/* First created by JCasGen Mon May 26 21:43:18 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon May 26 21:43:18 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class Relation extends Base {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Relation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Relation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Relation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Relation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
}

    