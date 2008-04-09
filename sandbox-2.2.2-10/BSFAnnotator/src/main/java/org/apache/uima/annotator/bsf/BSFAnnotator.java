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
package org.apache.uima.annotator.bsf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.IOUtils;
import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.analysis_engine.annotator.AnnotatorConfigurationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorContextException;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.analysis_engine.annotator.JTextAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Logger;

/**
 * This class enables the Java scripting of an Annotator using the Apache Bean
 * Scripting Framework. It has a mandatory <code>SourceFile</code> attribute
 * containing the script's source file to execute.
 * <p>
 */
public class BSFAnnotator extends JTextAnnotator_ImplBase {

	public static final String MESSAGE_DIGEST = "org.apache.uima.annotator.bsf.BSFAnnotatorMessages";

	public static final String PATH_SEPARATOR = System.getProperty("path.separator");

	public static final String SCRIPT_SOURCE_FILE = "SourceFile";

	private Logger logger;
	private BSFManager manager;
	private BSFEngine engine;
	String scriptLanguage;
	String scriptFileName;


	/**
	 * Initializes the annotator by compiling the script.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.uima.analysis_engine.annotator.Annotator_ImplBase#initialize(org.apache.uima.analysis_engine.annotator.AnnotatorContext)
	 */
	public void initialize(AnnotatorContext aContext)
			throws AnnotatorInitializationException,
			AnnotatorConfigurationException {
		super.initialize(aContext);
		// Initialize a BSF manager and do some 'cooking' to adapt the class loader
		manager = new BSFManager();
		ClassLoader classLoader = this.getClass().getClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		manager.setClassLoader(classLoader);
		// Is a UIMAClassLoader
		if (classLoader instanceof URLClassLoader) {
			manager.setClassPath(classpathFromUrls(((URLClassLoader) classLoader).getURLs()));
		}
		Reader reader = null;
		scriptFileName = null;
		try {
			logger = aContext.getLogger();
			// get UIMA datapath and tokenize it into its elements
			StringTokenizer tokenizer = new StringTokenizer(aContext
					.getDataPath(), PATH_SEPARATOR);
			ArrayList datapathElements = new ArrayList();
			while (tokenizer.hasMoreTokens()) {
				// add datapath elements to the 'datapathElements' array list
				datapathElements.add(new File(tokenizer.nextToken()));
			}

			// Get config. parameter values
			scriptFileName = (String) aContext.getConfigParameterValue(SCRIPT_SOURCE_FILE);
			File scriptFile = new File(scriptFileName);
			if (!scriptFile.isAbsolute()) {
				// try to resolve the relative file name with classpath or
				// datapath
				scriptFile = resolveRelativeFilePath(scriptFileName, datapathElements);

				// if the current script file wasn't found, throw an exception
				if (scriptFile == null) {
					throw new BSFAnnotatorConfigurationException(
							"bsf_annotator_resource_not_found",
							new Object[] { scriptFileName });
				}
			}
			reader = new FileReader(scriptFile);
		} catch (AnnotatorContextException ex) {
		    throw new AnnotatorInitializationException(ex);
	    } catch (FileNotFoundException fnfe) {
			throw new BSFAnnotatorConfigurationException(
					"bsf_annotator_resource_not_found",
					new Object[] { scriptFileName }, fnfe);
		}

		try {
			scriptLanguage = BSFManager.getLangFromFilename(scriptFileName);
			engine = manager.loadScriptingEngine(scriptLanguage);
		} catch (BSFException bsfe) {
			Throwable cause = bsfe.getTargetException();
			if (cause == null) cause = bsfe;
			throw new BSFAnnotatorConfigurationException(
					"bsf_annotator_language_not_supported",
					new Object[] { scriptLanguage }, cause);
		}

		// read and execute the script
		try {
			String script = IOUtils.getStringFromReader(reader);
			engine.exec(scriptFileName, 0, 0, script);
		} catch (IOException ioe) {
			throw new BSFAnnotatorInitializationException(
					"bsf_annotator_error_reading_script",
					new Object[] { scriptFileName }, ioe);
		} catch (BSFException bsfe) {
			Throwable cause = bsfe.getTargetException();
			if (cause == null) cause = bsfe;
			throw new BSFAnnotatorInitializationException(
					"bsf_annotator_error_executing_script",
					new Object[] { scriptFileName }, cause);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		// Call the initialize function implemented in the script
		String methodName = null;
		try {
			methodName = "initialize";
			engine.call(null, methodName, new Object[] { aContext });
		} catch (BSFException bsfe) {
			Throwable cause = bsfe.getTargetException();
			if (cause == null) cause = bsfe;
			throw new BSFAnnotatorInitializationException(
					"bsf_annotator_error_calling_method",
					new Object[] { methodName }, cause);
		}

	}

	/**
	 * Call the process function implemented in the script
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.uima.analysis_engine.annotator.JTextAnnotator#process(org.apache.uima.jcas.JCas,
	 *      org.apache.uima.analysis_engine.ResultSpecification)
	 */
	public void process(JCas jcas, ResultSpecification result)
			throws AnnotatorProcessException {
		
		String methodName = null;
		try {
			methodName = "process";
			engine.call(null, methodName, new Object[] { jcas, result });
		} catch (BSFException bsfe) {
			Throwable cause = bsfe.getTargetException();
			if (cause == null) cause = bsfe;
			throw new BSFAnnotatorProcessException(
					"bsf_annotator_error_calling_method",
					new Object[] { methodName }, cause);
		}
	}
	/**
	 * @param fileName
	 * @param datapathElements
	 * @return
	 */
	private File resolveRelativeFilePath(String fileName,
			ArrayList datapathElements) {
		URL url;
		// try to use the class loader to load the file resource
		if ((url = this.getClass().getClassLoader().getResource(fileName)) != null) {
			return new File(url.getFile());
		} else {
			if (datapathElements == null || datapathElements.size() == 0) {
				return null;
			}
			// try to use the datapath to load the file resource
			for (int i = 0; i < datapathElements.size(); i++) {
				File testFile = new File((File) datapathElements.get(i),
						fileName);
				if (testFile.exists()) {
					return testFile;
				}
			}
		}
		return null;

	}
	/**
	 * @param urls
	 * @return
	 */
	private String classpathFromUrls(URL[] urls) {
		String classpath = null;
		for (int i = 0; i < urls.length; i++) {
			File filepath = new File(urls[i].getPath());
			if (i == 0)
				classpath = filepath.getPath();
			else
				classpath = classpath + File.pathSeparator + filepath.getPath();
		}
		return classpath;
	}

}
