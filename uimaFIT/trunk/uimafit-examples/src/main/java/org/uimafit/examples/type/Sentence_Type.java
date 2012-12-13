/* First created by JCasGen Wed Jul 14 10:08:01 MDT 2010 */
package org.uimafit.examples.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * Updated by JCasGen Wed Jul 14 10:08:01 MDT 2010
 * 
 * @generated
 */
public class Sentence_Type extends Annotation_Type {
	/** @generated */
	protected FSGenerator getFSGenerator() {
		return fsGenerator;
	}

	/** @generated */
	private final FSGenerator fsGenerator = new FSGenerator() {
		public FeatureStructure createFS(int addr, CASImpl cas) {
			if (Sentence_Type.this.useExistingInstance) {
				// Return eq fs instance if already created
				FeatureStructure fs = Sentence_Type.this.jcas.getJfsFromCaddr(addr);
				if (null == fs) {
					fs = new Sentence(addr, Sentence_Type.this);
					Sentence_Type.this.jcas.putJfsFromCaddr(addr, fs);
					return fs;
				}
				return fs;
			}
			else return new Sentence(addr, Sentence_Type.this);
		}
	};

	/** @generated */
	public final static int typeIndexID = Sentence.typeIndexID;

	/**
	 * @generated
	 * @modifiable
	 */
	public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.uimafit.examples.type.Sentence");

	/**
	 * initialize variables to correspond with Cas Type and Features
	 * 
	 * @generated
	 */
	public Sentence_Type(JCas jcas, Type casType) {
		super(jcas, casType);
		casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

	}
}
