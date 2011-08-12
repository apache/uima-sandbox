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

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.rule.RuleElementMatch;
import org.apache.uima.textmarker.rule.RuleMatch;
import org.apache.uima.textmarker.rule.TextMarkerRuleElement;
import org.apache.uima.textmarker.type.TextMarkerBasic;
import org.apache.uima.textmarker.visitor.InferenceCrowd;


public class DelAction extends AbstractTextMarkerAction {

  public DelAction() {
    super();
  }

  @Override
  public void execute(RuleMatch match, TextMarkerRuleElement element, TextMarkerStream stream,
          InferenceCrowd crowd) {
    for (RuleElementMatch each : match.getMatchInfo(element)) {
      for (AnnotationFS eachAnnotation : each.getTextsMatched()) {
        if (eachAnnotation instanceof TextMarkerBasic) {
          TextMarkerBasic basic = (TextMarkerBasic) eachAnnotation;
          basic.setReplacement("");
        }
      }
    }
  }

}