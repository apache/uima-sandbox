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

package org.apache.uima.tm.textmarker.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.tm.textmarker.kernel.TextMarkerStream;
import org.apache.uima.tm.textmarker.kernel.expression.bool.BooleanExpression;
import org.apache.uima.tm.textmarker.kernel.expression.bool.SimpleBooleanExpression;
import org.apache.uima.tm.textmarker.kernel.expression.string.StringExpression;
import org.apache.uima.tm.textmarker.kernel.rule.EvaluatedCondition;
import org.apache.uima.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import org.apache.uima.tm.textmarker.kernel.type.TextMarkerBasic;
import org.apache.uima.tm.textmarker.kernel.visitor.InferenceCrowd;


public class RegExpCondition extends TerminalTextMarkerCondition {
  private final StringExpression pattern;

  private BooleanExpression ignoreCase;

  public RegExpCondition(StringExpression pattern, BooleanExpression ignoreCase) {
    this.pattern = pattern;
    this.ignoreCase = ignoreCase == null ? new SimpleBooleanExpression(false) : ignoreCase;
  }

  @Override
  public EvaluatedCondition eval(TextMarkerBasic basic, Type matchedType,
          TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd) {
    AnnotationFS annotation = stream.expandAnchor(basic, matchedType);
    String coveredText = annotation.getCoveredText();
    boolean ignore = ignoreCase == null ? false : ignoreCase.getBooleanValue(element.getParent());
    Pattern regularExpPattern = null;
    String stringValue = pattern.getStringValue(element.getParent());
    if (ignore) {
      regularExpPattern = Pattern.compile(stringValue, Pattern.CASE_INSENSITIVE);
    } else {
      regularExpPattern = Pattern.compile(stringValue);
    }
    Matcher macther = regularExpPattern.matcher(coveredText);
    boolean matches = macther.matches();
    return new EvaluatedCondition(this, matches);
  }

  public StringExpression getPattern() {
    return pattern;
  }
}
