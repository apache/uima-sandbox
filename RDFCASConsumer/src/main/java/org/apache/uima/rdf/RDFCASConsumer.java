/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.uima.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.clerezza.uima.utils.UIMAUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;


/**
 * CASConsumer that writes CAS contents to an RDF-like format
 */
public class RDFCASConsumer extends JCasAnnotator_ImplBase {

  private String view;
  private String file;
  private String format;

  @Override
  public void initialize(UimaContext context) {
    view = String.valueOf(context.getConfigParameterValue("view"));
    file = String.valueOf(context.getConfigParameterValue("file"));
    format = String.valueOf(context.getConfigParameterValue("format"));
  }


  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    try {

      JCas selectedView = getCASToSerialize(jCas);

      GraphNode node = createNode(selectedView);

      Type type = selectedView.getCasType(Annotation.type);

      List<FeatureStructure> annotations = getAnnotations(selectedView, type);

      UIMAUtils.enhanceNode(node, annotations);

      writeFile(node);

    } catch (Exception e) {
      throw new AnalysisEngineProcessException(e);
    }

  }

  private JCas getCASToSerialize(JCas jCas) throws CASException {
    JCas selectedView;
    if (view != null && view.length() > 0 && !view.equals("current")) {
      selectedView = jCas.getView(view);
    } else
      selectedView = jCas;
    return selectedView;
  }

  private GraphNode createNode(JCas selectedView) {
    MGraph mGraph = new SimpleMGraph();
    return new GraphNode(new UriRef(selectedView.toString()), mGraph);
  }

  private List<FeatureStructure> getAnnotations(JCas selectedView, Type type) {
    AnnotationIndex<Annotation> annotationIndex = selectedView.getAnnotationIndex(type);
    List<FeatureStructure> annotations = new ArrayList<FeatureStructure>(annotationIndex.size());
    for (Annotation a : annotationIndex) {
      annotations.add(a);
    }
    return annotations;
  }

  private void writeFile(GraphNode node) throws IOException {

    OutputStream outputStream = null;
    try {
      URI uri = UriUtils.create(file);
      File f = new File(uri);
      if (!f.exists()) {
        f.createNewFile();
      }
      outputStream = new FileOutputStream(f);
      Serializer serializer = Serializer.getInstance();
      serializer.serialize(outputStream, node.getGraph(), format);
      outputStream.flush();
      outputStream.close();
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e1) {
          // do nothing
        }
      }
    }
  }
}
