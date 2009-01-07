/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.tika;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.uima.FeatureValue;
import org.apache.uima.SourceDocumentAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;


/** Uses TIKA to convert original markup into UIMA annotations**/
public class MarkupAnnotator extends CasAnnotator_ImplBase {


	private final static String ORIGINAL_VIEW_PARAM_NAME = "ORIGINAL_VIEW_PARAM_NAME";
	private final static String TEXT_VIEW_PARAM_NAME = "TEXT_VIEW_PARAM_NAME";
	private final static String SET_TEXT_VIEW_DEFAULT_PARAM_NAME = "SET_TEXT_VIEW_DEFAULT_PARAM_NAME";
	
	private final static String tika_file_param = "tikaConfigFile";
	
	// takes an option indicating the name of the view containing the binary document
	private String originalViewName = "_InitialView";
	
	// takes an option indicating the name of the view containing the text version of the document
	private String textViewName = "textView";
	
	// whether to make the text view default or not
	private Boolean makeTextDefaultView = true;
	
	// configuration for TIKA - can be created by specifying a custom resource
	private TikaConfig config = null;
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		// Get config param setting
		originalViewName  = (String) aContext.getConfigParameterValue(ORIGINAL_VIEW_PARAM_NAME);

		textViewName = (String) aContext.getConfigParameterValue(TEXT_VIEW_PARAM_NAME);
		if (textViewName==null) {
			System.err.println("Parameter TEXT_VIEW_PARAM_NAME is null; setting to \"textView\"");
			textViewName = "textView";
		}
		else System.err.println("Parameter TEXT_VIEW_PARAM_NAME is "+textViewName);
		
		makeTextDefaultView = (Boolean) aContext.getConfigParameterValue(SET_TEXT_VIEW_DEFAULT_PARAM_NAME);
		if (makeTextDefaultView==null) {
			System.err.println("Parameter SET_TEXT_VIEW_DEFAULT_PARAM_NAME is null; setting to \"true\"");
			makeTextDefaultView = new Boolean(true);
		}
		else System.err.println("Parameter SET_TEXT_VIEW_DEFAULT_PARAM_NAME is "+makeTextDefaultView);
		
		// initialise TIKA parser
		// try to get a custom config
		URL tikaConfigURL = null;
		try {
			tikaConfigURL = getContext().getResourceURL(tika_file_param);
			config = new TikaConfig(tikaConfigURL);
		} catch (Exception e1) {
			// to log
			System.err.println("Failed to load TIKA config file from "+tikaConfigURL);
			config = null;
		}

		// if not rely on default one
		if (config==null){
			try {
				config = TikaConfig.getDefaultConfig();
			} catch (TikaException e) {
				throw new ResourceInitializationException(e);
			}
		}
		
	}
	
	public void process(CAS cas) throws AnalysisEngineProcessException {
	    CAS originalCas = null;
	    try {
	    originalCas = cas.getView(originalViewName);
	    }
	    catch (Exception e){
	    	String viewName = cas.getViewName();
	    	// can't find originalViewName
	    	System.err.println("can't find view "+originalViewName+" using "+viewName+" instead");
	    	originalCas = cas.getCurrentView();
	    }
	    
	    InputStream originalStream = originalCas.getSofa().getSofaDataStream();
		
	    String lang = null;
	    
	    // parsing with TIKA
	    
	    // TODO if content type is known then we use it 
	    // otherwise we guess
	    
	    Parser parser = new AutoDetectParser(config);

	    Metadata md = new Metadata();
	    MarkupHandler handler  = new MarkupHandler();		  

	    try {
	    	parser.parse(originalStream,handler , md);
	    }
	    catch (Exception e){
	    	// if we have a problem just dump the message and continue
	    	System.err.println("Problem converting file : "+e.getMessage());
	    	// PROBLEM => trying to serialize binary content in XML crash!
	    	return;
	    }
	    finally {
	    	try {
				originalStream.close();
			} catch (IOException e) {
			}
	    }
	    
	    CAS plainTextView = cas.createView(textViewName);
	    

	    handler.populateCAS(plainTextView);
	    plainTextView.setDocumentLanguage(lang);
	    
	    // get additional metadata about the document
	    // e.g content type etc...
	    // TODO add possibility to define type as parameter and discover
	    // feature names on the fly
	    JCas ptv=null;
		try {
			ptv = plainTextView.getJCas();
		} catch (CASException e) {
			e.printStackTrace();
			return;
		}
	    
	    Type docAnnotationType = ptv.getTypeSystem().getType("org.apache.uima.SourceDocumentAnnotation");
	    Iterator iter = ptv.getAnnotationIndex(docAnnotationType).iterator();
	    SourceDocumentAnnotation docAnnotation = null;
	    // do we already have one?
	    if (iter.hasNext()) docAnnotation = (SourceDocumentAnnotation) iter.next();
	    // otherwise let's create a new annotation
	    else docAnnotation = new SourceDocumentAnnotation(ptv);
	    
	    // now iterate on the metadata found by Tika and add them to the info
	    if (docAnnotation.getFeatures()==null)
	    	docAnnotation.setFeatures((FSArray) cas.createArrayFS(md.size())) ;
	    
	    for (int i=0;i<md.size();i++){
	    	String name = md.names()[i];
	    	String value = md.get(name);
	    	FeatureValue fv = new FeatureValue(ptv);
	    	fv.setName(name);
	    	fv.setValue(value);
	    	docAnnotation.setFeatures(i,fv);
	    }
	    docAnnotation.addToIndexes();
	   
	}

}
