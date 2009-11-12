

/* First created by JCasGen Mon May 26 21:43:20 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** Returned value from web service in RDF form
 * Updated by JCasGen Mon May 26 21:43:20 EDT 2008
 * XML source: C:/a/Eclipse/3.3/apache/OpenCalaisAnnotatorGroovy/src/main/descriptors/CalaisTestCollectionReader.xml
 * @generated */
public class RdfText extends TOP {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(RdfText.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected RdfText() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RdfText(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RdfText(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: rdfText

  /** getter for rdfText - gets 
   * @generated */
  public String getRdfText() {
    if (RdfText_Type.featOkTst && ((RdfText_Type)jcasType).casFeat_rdfText == null)
      jcasType.jcas.throwFeatMissing("rdfText", "org.apache.uima.calaisType.RdfText");
    return jcasType.ll_cas.ll_getStringValue(addr, ((RdfText_Type)jcasType).casFeatCode_rdfText);}
    
  /** setter for rdfText - sets  
   * @generated */
  public void setRdfText(String v) {
    if (RdfText_Type.featOkTst && ((RdfText_Type)jcasType).casFeat_rdfText == null)
      jcasType.jcas.throwFeatMissing("rdfText", "org.apache.uima.calaisType.RdfText");
    jcasType.ll_cas.ll_setStringValue(addr, ((RdfText_Type)jcasType).casFeatCode_rdfText, v);}    
  }

    