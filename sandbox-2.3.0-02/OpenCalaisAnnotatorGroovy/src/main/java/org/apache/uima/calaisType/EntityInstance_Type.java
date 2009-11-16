
/* First created by JCasGen Mon May 26 21:43:18 EDT 2008 */
package org.apache.uima.calaisType;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** 
 * Updated by JCasGen Mon May 26 21:43:18 EDT 2008
 * @generated */
public class EntityInstance_Type extends Instance_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EntityInstance_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EntityInstance_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EntityInstance(addr, EntityInstance_Type.this);
  			   EntityInstance_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EntityInstance(addr, EntityInstance_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = EntityInstance.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.calaisType.EntityInstance");
 
  /** @generated */
  final Feature casFeat_entity;
  /** @generated */
  final int     casFeatCode_entity;
  /** @generated */ 
  public int getEntity(int addr) {
        if (featOkTst && casFeat_entity == null)
      jcas.throwFeatMissing("entity", "org.apache.uima.calaisType.EntityInstance");
    return ll_cas.ll_getRefValue(addr, casFeatCode_entity);
  }
  /** @generated */    
  public void setEntity(int addr, int v) {
        if (featOkTst && casFeat_entity == null)
      jcas.throwFeatMissing("entity", "org.apache.uima.calaisType.EntityInstance");
    ll_cas.ll_setRefValue(addr, casFeatCode_entity, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public EntityInstance_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_entity = jcas.getRequiredFeatureDE(casType, "entity", "org.apache.uima.calaisType.Entity", featOkTst);
    casFeatCode_entity  = (null == casFeat_entity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_entity).getCode();

  }
}



    