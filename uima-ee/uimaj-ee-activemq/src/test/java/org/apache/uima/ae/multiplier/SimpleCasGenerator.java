package org.apache.uima.ae.multiplier;

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

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Random;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * An example CasMultiplier, which generates the specified number of output CASes.
 */
public class SimpleCasGenerator extends CasMultiplier_ImplBase
{
	private String mDoc1;

	private String mDoc2;

	private int mCount;

	private int nToGen;
    
	private String text;

	private Random gen;
	
	long docCount=0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#initialize(org.apache.uima.UimaContext)
	 */
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    this.nToGen = ((Integer) aContext.getConfigParameterValue("NumberToGenerate")).intValue();
    FileInputStream fis = null;
    try
    {
  	  String filename = ((String) aContext.getConfigParameterValue("InputFile")).trim();
  	  	URL url = this.getClass().getClassLoader().getResource(filename);
        System.out.println("************ File::::"+url.getPath());
  	  	// open input stream to file
        File file = new File( url.getPath() );
//        File file = new File( filename );
        fis = new FileInputStream(file);
          byte[] contents = new byte[(int) file.length()];
          fis.read(contents);
          text = new String(contents);
    }
    catch( Exception e)
    {
    	throw new ResourceInitializationException(e);
    }
    finally 
    {
      if (fis != null)
      {
    	  try
    	  {
        	  fis.close();
    	  }
    	  catch( Exception e){}
      }
    }
    
    this.mDoc1 = (String) aContext.getConfigParameterValue("StringOne");
    this.mDoc2 = (String) aContext.getConfigParameterValue("StringTwo");
    this.gen = new Random();
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see JCasMultiplier_ImplBase#process(JCas)
	 */
	public void process(CAS aCas) throws AnalysisEngineProcessException
	{
		this.mCount = 0;
    this.docCount=0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.uima.analysis_component.AnalysisComponent#hasNext()
	 */
	public boolean hasNext() throws AnalysisEngineProcessException
	{
		return this.mCount < this.nToGen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.uima.analysis_component.AnalysisComponent#next()
	 */
	public AbstractCas next() throws AnalysisEngineProcessException
	{

		CAS cas = getEmptyCAS();
/*
		int junk = this.gen.nextInt();
		if ((junk & 1) != 0)
		{
			cas.setDocumentText(this.mDoc1);
		}
		else
		{
			cas.setDocumentText(this.mDoc2);
		}
*/
		if (docCount ==0 )
		{
			System.out.println("Initializing CAS with a Document of Size:"+text.length());
		}
		docCount++;
		System.out.println("CasMult creating document#"+docCount);//+"Initializing CAS with a Document of Size:"+text.length());
		cas.setDocumentText(this.text);
		this.mCount++;
		return cas;
	}

}

