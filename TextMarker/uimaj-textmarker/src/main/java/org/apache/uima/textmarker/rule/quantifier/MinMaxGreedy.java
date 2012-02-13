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

package org.apache.uima.textmarker.rule.quantifier;

import java.util.List;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.textmarker.TextMarkerBlock;
import org.apache.uima.textmarker.TextMarkerStatement;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.expression.number.NumberExpression;
import org.apache.uima.textmarker.expression.number.SimpleNumberExpression;
import org.apache.uima.textmarker.rule.ComposedRuleElementMatch;
import org.apache.uima.textmarker.rule.RuleElement;
import org.apache.uima.textmarker.rule.RuleElementMatch;
import org.apache.uima.textmarker.rule.RuleMatch;
import org.apache.uima.textmarker.visitor.InferenceCrowd;

public class MinMaxGreedy implements RuleElementQuantifier {

  private NumberExpression min;

  private NumberExpression max;

  public MinMaxGreedy(NumberExpression min, NumberExpression max, boolean interval) {
    super();
    if (!interval) {
      this.min = min;
      this.max = min;
    } else {
      this.min = min;
      this.max = max;
      if (this.max == null) {
        this.max = new SimpleNumberExpression(Integer.MAX_VALUE);
      }
    }
  }

  public List<RuleElementMatch> evaluateMatches(List<RuleElementMatch> matches,
          TextMarkerStatement element, InferenceCrowd crowd) {
    int minValue = min.getIntegerValue(element.getParent());
    int maxValue = max.getIntegerValue(element.getParent());

    if (matches.size() > 0) {
      RuleElementMatch ruleElementMatch = matches.get(matches.size() - 1);
      if (!ruleElementMatch.matched()) {
        matches.remove(ruleElementMatch);
      }
    }

    int matchedSize = matches.size();

    boolean result = matchedSize >= minValue && matchedSize <= maxValue;
    if (result) {
      return matches;
    } else {
      return null;
    }
  }

  public NumberExpression getMin() {
    return min;
  }

  public NumberExpression getMax() {
    return max;
  }

  public boolean continueMatch(boolean after, AnnotationFS annotation, RuleElement ruleElement,
          RuleMatch extendedMatch, ComposedRuleElementMatch containerMatch,
          TextMarkerStream stream, InferenceCrowd crowd) {
    int minValue = min.getIntegerValue(ruleElement.getParent());
    int maxValue = max.getIntegerValue(ruleElement.getParent());
    List<RuleElementMatch> list = containerMatch.getInnerMatches().get(ruleElement);
    if (list == null && maxValue > 0) {
      return true;
    }
    int matchedSize = list.size();
    if (list == null || list.isEmpty() || matchedSize < minValue) {
      return true;
    }
    RuleElementMatch lastMatch = null;
    if (after) {
      lastMatch = list.get(list.size() - 1);
    } else {
      lastMatch = list.get(0);
    }
    return matchedSize < maxValue
            || (!lastMatch.matched() && matchedSize >= minValue && matchedSize <= maxValue);
  }

  public boolean isOptional(TextMarkerBlock parent) {
    int minValue = min.getIntegerValue(parent);
    return minValue == 0;
  }
}
