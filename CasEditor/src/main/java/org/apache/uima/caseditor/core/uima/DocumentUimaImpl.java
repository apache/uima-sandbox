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

package org.apache.uima.caseditor.core.uima;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIntConstraint;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.core.AbstractDocument;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.model.NlpProject;
import org.apache.uima.caseditor.core.util.Span;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * TODO: add javdoc here
 */
public class DocumentUimaImpl extends AbstractDocument {
  private CAS mCAS;
private DocumentElement mDocumentElement;

  /**
   * Initializes a new instance.
   * 
   * @param project
   */
  public DocumentUimaImpl(NlpProject project) {
    mCAS = project.getTypesystem().getCAS();
  }

  /**
   * Retrives the {@link CAS}.
   */
  public CAS getCAS() {
    return mCAS;
  }

  /**
   * Adds the given annotation to the {@link CAS}.
   */
  public void addFeatureStructure(FeatureStructure annotation) {
    mCAS.getIndexRepository().addFS(annotation);

    fireAddedAnnotation(annotation);
  }

  /**
   * 
   */
  public void addFeatureStructures(Collection<FeatureStructure> annotations) {
    for (FeatureStructure annotation : annotations) {
      addFeatureStructure(annotation);
    }
  }

  /**
   * Remove all annotations. TODO: implement it
   */
  public void removeAnnotation() {
    // must be implemented
  }

  /**
   * Internally removes an annoation from the {@link CAS}.
   * 
   * @param featureStructure
   */
  private void removeAnnotationInternal(FeatureStructure featureStructure) {
    getCAS().getIndexRepository().removeFS(featureStructure);
  }

  /**
   * Removes the annoations from the {@link CAS}.
   */
  public void removeFeatureStructure(FeatureStructure annotation) {
    removeAnnotationInternal(annotation);

    fireRemovedAnnotation(annotation);
  }

  /**
   * Removes the given annotations from the {@link CAS}.
   */
  public void removeFeatureStructures(Collection<FeatureStructure> annotationsToRemove) {
    for (FeatureStructure annotationToRemove : annotationsToRemove) {
      removeAnnotationInternal(annotationToRemove);
    }

    fireRemovedAnnotations(annotationsToRemove);
  }

  /**
   * Notifies clients about the changed annotation.
   */
  public void update(FeatureStructure annotation) {
    fireUpdatedFeatureStructure(annotation);
  }

  /**
   * Notifies clients about the changed annotation.
   */
  public void updateFeatureStructure(Collection<FeatureStructure> annotations) {
    fireUpdatedFeatureStructures(annotations);
  }

  /**
   * Retrives annoations of the given type from the {@link CAS}.
   */
  public Collection<AnnotationFS> getAnnotations(Type type) {
    FSIndex annotationIndex = mCAS.getAnnotationIndex(type);

    StrictTypeConstraint typeConstrain = new StrictTypeConstraint(type);

    FSIterator strictTypeIterator = mCAS.createFilteredIterator(annotationIndex.iterator(),
            typeConstrain);

    return fsIteratorToCollection(strictTypeIterator);
  }

  private Collection<AnnotationFS> fsIteratorToCollection(FSIterator iterator) {
    LinkedList<AnnotationFS> annotations = new LinkedList<AnnotationFS>();
    while (iterator.hasNext()) {
      AnnotationFS annotation = (AnnotationFS) iterator.next();

      annotations.addFirst(annotation);
    }

    return annotations;
  }

  /**
   * Retrives the annotations in the given span.
   */
  @Override
  public Collection<AnnotationFS> getAnnotation(Type type, Span span) {
    ConstraintFactory cf = getCAS().getConstraintFactory();

    Type annotationType = getCAS().getAnnotationType();

    FeaturePath beginPath = getCAS().createFeaturePath();
    beginPath.addFeature(annotationType.getFeatureByBaseName("begin"));
    FSIntConstraint beginConstraint = cf.createIntConstraint();
    beginConstraint.geq(span.getStart());

    FSMatchConstraint embeddedBegin = cf.embedConstraint(beginPath, beginConstraint);

    FeaturePath endPath = getCAS().createFeaturePath();
    endPath.addFeature(annotationType.getFeatureByBaseName("end"));
    FSIntConstraint endConstraint = cf.createIntConstraint();
    endConstraint.leq(span.getEnd());

    FSMatchConstraint embeddedEnd = cf.embedConstraint(endPath, endConstraint);

    FSMatchConstraint strictType = new StrictTypeConstraint(type);

    FSMatchConstraint annotatioInSpanConstraint = cf.and(embeddedBegin, embeddedEnd);

    FSMatchConstraint annotationInSpanAndStrictTypeConstraint = cf.and(annotatioInSpanConstraint,
            strictType);

    FSIndex allAnnotations = getCAS().getAnnotationIndex(type);

    FSIterator annotationInsideSpanIndex = getCAS().createFilteredIterator(
            allAnnotations.iterator(), annotationInSpanAndStrictTypeConstraint);

    return fsIteratorToCollection(annotationInsideSpanIndex);
  }

  /**
   * Retrives the given type from the {@link TypeSystem}.
   */
  public Type getType(String type) {
    return getCAS().getTypeSystem().getType(type);
  }

  /**
   * Retrvies the text.
   */
  public String getText() {
    return mCAS.getDocumentText();
  }

  /**
   * Sets the content. The XCAS {@link InputStream} gets parsed.
   */
  public void setContent(InputStream content) throws CoreException {
    XCASDeserializer dezerializer = new XCASDeserializer(mCAS.getTypeSystem());

    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    saxParserFactory.setValidating(false);

    SAXParser saxParser;

    try {
      saxParser = saxParserFactory.newSAXParser();
      saxParser.parse(content, dezerializer.getXCASHandler(mCAS));
    } catch (IOException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    } catch (ParserConfigurationException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    } catch (SAXException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }
  }

  /**
   * Serializes the {@link CAS} to the given {@link OutputStream} in the XCAS format.
   */
  public void serialize(OutputStream out) throws CoreException {
    XCASSerializer xcasSerializer = new XCASSerializer(mCAS.getTypeSystem());

    OutputStreamWriter writer;

    try {
      writer = new OutputStreamWriter(out, "UTF-8");
    } catch (UnsupportedEncodingException e1) {
      // TODO: handle this exception
      throw new RuntimeException(e1);
    }

    XMLSerializer xmlSerialzer = new XMLSerializer(writer, new OutputFormat("XML", "UTF-8", true));

    try {
      xcasSerializer.serialize(mCAS, xmlSerialzer);
    } catch (IOException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    } catch (SAXException e) {
      String message = (e.getMessage() != null ? e.getMessage() : "");

      IStatus s = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK, message, e);

      throw new CoreException(s);
    }
  }

  public void save() throws CoreException {
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream(40000);
	    
	    serialize(outStream);
	    
	    InputStream stream = new ByteArrayInputStream(outStream.toByteArray());

	    mDocumentElement.getResource().setContents(stream, true, false, null);
  }
  
	public void setDocumentElement(DocumentElement element) {
		mDocumentElement = element;
	}
}