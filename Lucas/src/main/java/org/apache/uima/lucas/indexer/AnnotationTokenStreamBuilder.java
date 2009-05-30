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

package org.apache.uima.lucas.indexer;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.lucas.indexer.analysis.AnnotationTokenStream;

/**
 * Builds annotation token streams wrapped with filters according to annotation descriptions.
 */
public class AnnotationTokenStreamBuilder {

  public AnnotationTokenStream createAnnotationTokenStream(JCas jCas,
          AnnotationDescription annotationDescription) throws CASException {

    String sofaName = annotationDescription.getSofa();
    sofaName = sofaName == null ? CAS.NAME_DEFAULT_SOFA : sofaName;

    // create tokenstream from annotations

    AnnotationTokenStream annotationTokenStream = null;

    String typeName = annotationDescription.getType();
    if (annotationDescription.getFeatureDescriptions().size() == 0) // use coveredText
      annotationTokenStream = new AnnotationTokenStream(jCas, sofaName, typeName);
    else {
      String featurePath = annotationDescription.getFeaturePath();
      String delimiter = annotationDescription.getConcatString();

      List<String> featureNames = new ArrayList<String>();
      Map<String, Format> featureFormats = new HashMap<String, Format>();
      for (FeatureDescription featureDescription : annotationDescription.getFeatureDescriptions()) {
        featureNames.add(featureDescription.getFeatureName());
        if (featureDescription.getNumberFormat() != null) {
          Format format = new DecimalFormat(featureDescription.getNumberFormat());
          featureFormats.put(featureDescription.getFeatureName(), format);
        }
      }

      if (featurePath != null)
        annotationTokenStream =
                new AnnotationTokenStream(jCas, sofaName, typeName, featurePath, featureNames,
                        delimiter, featureFormats);
      else
        annotationTokenStream =
                new AnnotationTokenStream(jCas, sofaName, typeName, featureNames, delimiter,
                        featureFormats);

    }

    return annotationTokenStream;
  }
}
