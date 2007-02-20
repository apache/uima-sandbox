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

package org.apache.uima.caseditor.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import org.apache.uima.caseditor.core.IDocument;
import org.apache.uima.caseditor.core.model.delta.INlpElementDelta;
import org.apache.uima.caseditor.core.uima.DocumentUimaImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The document element contains the uima cas document.
 */
public final class DocumentElement extends AbstractNlpElement implements IAdaptable {
  private CorpusElement mParent;

  private IFile mDocumentFile;

  /**
   * The working copy of the document. This instance is
   * shared by everyone who wants to edit the document.
   */
  private SoftReference<DocumentUimaImpl> mWorkingCopy = 
	  new SoftReference<DocumentUimaImpl>(null);

  /**
   * Initializes a new instance.
   * 
   * @param corpus
   * @param documentFile
   */
  DocumentElement(CorpusElement corpus, IFile documentFile) {
	  
	if (corpus == null || documentFile == null) {
	  throw new IllegalArgumentException("Parameters must not be null!");
		  
	}
	
    mParent = corpus;
    mDocumentFile = documentFile;
  }

  /**
   * Retrives the coresponding resource.
   */
  public IFile getResource() {
    return mDocumentFile;
  }

  /**
   * Retrives the name.
   */
  public String getName() {
    return mDocumentFile.getName();
  }


  /**
   * Retrives the parent.
   */
  public INlpElement getParent() {
    return mParent;
  }

  /**
   * Retrives the working copy.
   * 
   * @return the working copy
   * @throws CoreException
   */
  public IDocument getDocument() throws CoreException {
   
	  DocumentUimaImpl document = mWorkingCopy.get();
	  
    if (document == null) {
    	InputStream in = mDocumentFile.getContents();
    	
    	document  = 
    			new DocumentUimaImpl((NlpProject) mParent.getParent());
    	
    	document.setContent(in);
    	document.setDocumentElement(this);
    	
    	mWorkingCopy = new SoftReference<DocumentUimaImpl>(document);
    }
    
    return document;
  }
  
  /**
   * Writes the document element to the file system.
   * 
   * TODO: move it to the document, maybe the document gets not
   * saved if the caller lost the reference to it, befor the this call
   * 
   * @throws CoreException
   */
  public void saveDocument() throws CoreException {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream(40000);
    
    // getDocument().serialize(outStream);

    InputStream stream = new ByteArrayInputStream(outStream.toByteArray());

    mDocumentFile.setContents(stream, true, false, null);
  }

  /**
   * Retrives the coresponding {@link NlpProject} instance.
   * 
   * @return the {@link NlpProject} instance
   */
  public NlpProject getNlpProject() {
    return (NlpProject) getParent().getParent();
  }

  /**
   * Not implemented.
   */
  @Override
  void addResource(IResource resource) {
    // not needed here, there are no resources
  }

  @Override
  void changedResource(IResource resource, INlpElementDelta delta) {
	  
	// TODO: What should happen if the doucment is changed externally 
	// e.g. with a texteditor ?
    mWorkingCopy = new SoftReference<DocumentUimaImpl>(null);
  }

  /**
   * Not implemented.
   */
  @Override
  void removeResource(IResource resource) {
    // not needed here, there are no resources
  }

  /**
   * Generates a hash code for the current instance.
   */
  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}