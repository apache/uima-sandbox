/* First created by JCasGen Wed Jul 14 10:08:01 MDT 2010 */
package org.uimafit.examples.type;

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
 * Updated by JCasGen Wed Jul 14 10:08:01 MDT 2010
 * 
 * @generated
 */
public class Token_Type extends Annotation_Type {
	/** @generated */
	protected FSGenerator getFSGenerator() {
		return fsGenerator;
	}

	/** @generated */
	private final FSGenerator fsGenerator = new FSGenerator() {
		public FeatureStructure createFS(int addr, CASImpl cas) {
			if (Token_Type.this.useExistingInstance) {
				// Return eq fs instance if already created
				FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
				if (null == fs) {
					fs = new Token(addr, Token_Type.this);
					Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
					return fs;
				}
				return fs;
			}
			else return new Token(addr, Token_Type.this);
		}
	};

	/** @generated */
	public final static int typeIndexID = Token.typeIndexID;

	/**
	 * @generated
	 * @modifiable
	 */
	public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.uimafit.examples.type.Token");

	/** @generated */
	final Feature casFeat_pos;

	/** @generated */
	final int casFeatCode_pos;

	/** @generated */
	public String getPos(int addr) {
		if (featOkTst && casFeat_pos == null) jcas.throwFeatMissing("pos", "org.uimafit.examples.type.Token");
		return ll_cas.ll_getStringValue(addr, casFeatCode_pos);
	}

	/** @generated */
	public void setPos(int addr, String v) {
		if (featOkTst && casFeat_pos == null) jcas.throwFeatMissing("pos", "org.uimafit.examples.type.Token");
		ll_cas.ll_setStringValue(addr, casFeatCode_pos, v);
	}

	/**
	 * initialize variables to correspond with Cas Type and Features
	 * 
	 * @generated
	 */
	public Token_Type(JCas jcas, Type casType) {
		super(jcas, casType);
		casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

		casFeat_pos = jcas.getRequiredFeatureDE(casType, "pos", "uima.cas.String", featOkTst);
		casFeatCode_pos = (null == casFeat_pos) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_pos).getCode();

	}
}
