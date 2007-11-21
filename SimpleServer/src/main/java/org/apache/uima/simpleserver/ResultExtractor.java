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

package org.apache.uima.simpleserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.LowLevelCAS;
import org.apache.uima.cas.impl.TypeSystemUtils;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.simpleserver.config.Filter;
import org.apache.uima.simpleserver.config.Output;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.TypeMap;
import org.apache.uima.simpleserver.output.Result;
import org.apache.uima.simpleserver.output.ResultEntry;
import org.apache.uima.simpleserver.output.impl.ResultEntryImpl;
import org.apache.uima.simpleserver.output.impl.ResultImpl;

/*
 * This class contains static methods responsible for iterating over annotations, applying filters
 * and building outputs
 */
public class ResultExtractor {

  /*
   * this is the main method of this class, intended to be called from outside.
   * 
   * It receives a CAS and a ResultSpecification object and produces a Result object which contains
   * annotations from the CAS which have passed the filters.
   */
  public static Result getResult(CAS cas, ServerSpec rspec) {

    // this collection will store the result - some number of ResultEntry
    // instances
    List<ResultEntry> resultEntries = new ArrayList<ResultEntry>();

    try {
      processTypes(cas, rspec, resultEntries);

      return new ResultImpl(resultEntries, cas.getDocumentText());
    } catch (Throwable t) {
      throw new RuntimeException(
          "the CAS has been processed, but the result extraction from CAS failed");
    }
  }

  /*
   * this method perform iteration over all <type .. > specifications of the ResultSpecification XML
   */
  private static void processTypes(CAS cas, ServerSpec rspec, List<ResultEntry> resultEntries) {
    Type annotationType = cas.getTypeSystem().getType(CAS.TYPE_NAME_ANNOTATION);
    // we iterate over all <type> specifications of the ResultSpec
    for (TypeMap tspec : rspec.getTypeSpecs()) {
      TypeSystem typeSystem = cas.getTypeSystem();
      Type type = typeSystem.getType(tspec.getTypeName());

      // Check that type exists and is an annotation.
      if ((type == null) || !typeSystem.subsumes(annotationType, type)) {
        continue;
      }

      // iterate over all annotations of the specified type
      // and its subtypes
      FSIterator iterator = cas.getAnnotationIndex(type).iterator();
      for (; iterator.isValid(); iterator.moveToNext()) {
        // this fs is an annotation
        AnnotationFS annotation = (AnnotationFS) iterator.get();
        // if the annotation passes all filters...
        if (tspec.getFilter().match(annotation)) {
          ResultEntryImpl resultEntry = new ResultEntryImpl(tspec.getOutputTag());
          // ...then we use it to make output obect
          makeOutputs(resultEntry, annotation, tspec, typeSystem);
          // and add it to the result collection
          resultEntries.add(resultEntry);
        }

      }
    }

  }

  /*
   * This method produces the output as specified by the ResultSpecification XML file
   */
  public static void makeOutputs(ResultEntryImpl resultEntry, AnnotationFS annotation,
      TypeMap tspec, TypeSystem typeSystem) {

    // TODO covered text - DONE

    String coveredText = annotation.getCoveredText();

    resultEntry.setCoveredText(coveredText);

    /*
     * 
     * if (tspec.isOutputCoveredText()) { resultEntry.setAttributeValue("coveredText", coveredText); }
     */

    for (Output outSpec : tspec.getOutputs()) {
      Object value = evaluatePath(annotation, outSpec.getFeaturePath());
      if (value != null) {
        String stringValue = value.toString();
        resultEntry.setAttributeValue(outSpec.getAttribute(), stringValue);
      }
    }

  }

  private static final String evaluatePath(FeatureStructure fs, List<String> path) {
    for (int i = 0; i < path.size(); i++) {
      String f = path.get(i);
      Feature feat = fs.getType().getFeatureByBaseName(f);
      if (feat == null) {
        return null;
      }
      int typeClass = TypeSystemUtils.classifyType(fs.getType());
      if (typeClass == LowLevelCAS.TYPE_CLASS_FS) {
        fs = fs.getFeatureValue(feat);
        if (fs == null) {
          return null;
        }
      } else {
        if (i == (path.size() - 1)) {
          return fs.getFeatureValueAsString(feat);
        }
        return null;
      }
    }
    return fs.toString();
  }

}
