/* 
  Copyright 2009-2010	Regents of the University of Colorado.  
 All rights reserved. 

 Licensed under the Apache License, Version 2.0 (the "License"); 
 you may not use this file except in compliance with the License. 
 You may obtain a copy of the License at 

 http://www.apache.org/licenses/LICENSE-2.0 

 Unless required by applicable law or agreed to in writing, software 
 distributed under the License is distributed on an "AS IS" BASIS, 
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 See the License for the specific language governing permissions and 
 limitations under the License.
 */
package org.uimafit.factory.testAes;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.descriptor.SofaCapability;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.component.ViewCreatorAnnotator;

/**
 * @author Philip Ogren
 */
@SofaCapability(inputSofas = CAS.NAME_DEFAULT_SOFA, outputSofas = ViewNames.REVERSE_VIEW)
public class Annotator3 extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		try {
			jCas = jCas.getView(CAS.NAME_DEFAULT_SOFA);
			String text = jCas.getDocumentText();
			String reverseText = reverse(text);
			JCas reverseView = ViewCreatorAnnotator.createViewSafely(jCas, ViewNames.REVERSE_VIEW);
			reverseView.setDocumentText(reverseText);
		}
		catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private String reverse(String string) {
		int stringLength = string.length();
		StringBuffer returnValue = new StringBuffer();

		for (int i = stringLength - 1; i >= 0; i--) {
			returnValue.append(string.charAt(i));
		}
		return returnValue.toString();
	}

}
