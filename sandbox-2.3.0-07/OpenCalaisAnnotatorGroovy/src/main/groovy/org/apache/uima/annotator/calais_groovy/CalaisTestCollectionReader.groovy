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

package org.apache.uima.annotator.calais_groovy;

import org.apache.uima.calaisType.RdfText

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * Read test cases for OpenCalais, set up the sofa text, and insert the
 *   test data that would be received back from the annotator into the Cas
 * It is configured with the following parameters:
 * <ul>
 * <li><code>InputDirectory</code> - path to directory containing files</li>
 * <li><code>ThisMany</code> - number to read; 0 means read all in the directory </li>
 * </ul>
 * 
 * 
 */
public class CalaisTestCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of a directory containing input
   * files.
   */
  public static final String PARAM_INPUTDIR = "InputDirectory";

  public static final String THIS_MANY = "ThisMany";

  def directory
  
  def textDirectory
  
  def writeText
  
  def mFiles = []

  def nbrToProcess;

  private int mCurrentIndex;

  /**
   * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
   */
  public void initialize() throws ResourceInitializationException {
    nbrToProcess = getConfigParameterValue(THIS_MANY) as int;
    directory = new File(getConfigParameterValue(PARAM_INPUTDIR).trim());
    mCurrentIndex = 0;

    // if input directory does not exist or is not a directory, throw exception
    if (!directory.exists() || !directory.isDirectory()) {
      throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
              [PARAM_INPUTDIR, this.getMetaData().getName(), directory.getPath()]);
    }
    textDirectory = new File("${directory.getAbsolutePath()}/text")
    writeText = !textDirectory.exists()
    if (writeText) {
      textDirectory.mkdir()
    }
    directory.eachFile { if (!it.isDirectory()) 
                           mFiles.add(it) }
    nbrToProcess = Math.min(nbrToProcess, mFiles.size())
  }

  /**
   * @see org.apache.uima.collection.CollectionReader#hasNext()
   */
  public boolean hasNext() {
    return mCurrentIndex < nbrToProcess ;
  }
  
  /**
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */
  public void getNext(CAS aCAS) {
    def jcas = aCAS.getJCas();
    def rdfFile = mFiles[mCurrentIndex++]
    def testData
    rdfFile.withReader{testData = it.getText()}
    def matcher = testData =~ "(?s)<!\\[CDATA\\[(.*?)\\]\\]>"
    matcher.find()
    def inputString = matcher.group(1)
    jcas.setDocumentText(inputString)
    def textFileName = "${directory}/text/${dropExtension(rdfFile.getName())}.txt"
    if (writeText) {
      (new File(textFileName)).withWriter {it.write(inputString)}
    }
    def rdf = new RdfText(jcas)
    rdf.rdfText = testData
    rdf.addToIndexes()
    
    // Also store location of source document in CAS. This information is critical
    // if CAS Consumers will need to know where the original document contents are located.
    // For example, the Semantic Search CAS Indexer writes this information into the
    // search index that it creates, which allows applications that use the search index to
    // locate the documents that satisfy their semantic queries.
    def srcDocInfo = new SourceDocumentInformation(jcas);
    srcDocInfo.uri =  new File(textFileName).getAbsoluteFile().toURL().toString();
    srcDocInfo.offsetInSource = 0;
    srcDocInfo.documentSize = inputString.size();
    srcDocInfo.addToIndexes();
  }

  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
   */
  public void close() {
  }

  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
   */
  public Progress[] getProgress() {
    return [ new ProgressImpl(mCurrentIndex, mFiles.size(), Progress.ENTITIES) ];
  }

  /**
   * Gets the total number of documents that will be returned by this collection reader. This is not
   * part of the general collection reader interface.
   * 
   * @return the number of documents in the collection
   */
  public int getNumberOfDocuments() {
    return nbrToProcess;
  }

  def dropExtension(s) {
    s.substring(0, s.lastIndexOf('.'))
  }
}
