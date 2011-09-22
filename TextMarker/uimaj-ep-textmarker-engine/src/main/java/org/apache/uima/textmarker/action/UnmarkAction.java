/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.textmarker.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.expression.type.TypeExpression;
import org.apache.uima.textmarker.rule.RuleElement;
import org.apache.uima.textmarker.rule.RuleMatch;
import org.apache.uima.textmarker.type.TextMarkerBasic;
import org.apache.uima.textmarker.visitor.InferenceCrowd;

public class UnmarkAction extends TypeSensitiveAction {

  public UnmarkAction(TypeExpression type) {
    super(type);
  }

  @Override
  public void execute(RuleMatch match, RuleElement element, TextMarkerStream stream,
          InferenceCrowd crowd) {
    List<AnnotationFS> matchedAnnotationsOf = match.getMatchedAnnotationsOf(element, stream);
    Type t = type.getType(element.getParent());
    for (AnnotationFS annotationFS : matchedAnnotationsOf) {
      TextMarkerBasic beginAnchor = stream.getBeginAnchor(annotationFS.getBegin());
      Set<AnnotationFS> beginAnchors = beginAnchor.getBeginAnchors(t);
      if (beginAnchors != null) {
        for (AnnotationFS each : new ArrayList<AnnotationFS>(beginAnchors)) {
          if (each.getEnd() == annotationFS.getEnd()) {
            stream.removeAnnotation(each, t);
          }
        }
      }
    }

  }

}
