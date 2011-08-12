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

package org.apache.uima.textmarker.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.textmarker.TextMarkerBlock;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.action.AbstractTextMarkerAction;
import org.apache.uima.textmarker.condition.AbstractTextMarkerCondition;
import org.apache.uima.textmarker.rule.quantifier.RuleElementQuantifier;
import org.apache.uima.textmarker.type.TextMarkerBasic;
import org.apache.uima.textmarker.visitor.InferenceCrowd;

public class TextMarkerRuleElement extends AbstractRuleElement {

  private TextMarkerMatcher matcher;

  private List<AbstractTextMarkerCondition> conditions;

  private List<AbstractTextMarkerAction> actions;

  private TextMarkerBlock parent;

  @SuppressWarnings("unchecked")
  protected final InferenceCrowd emptyCrowd = new InferenceCrowd(Collections.EMPTY_LIST);

  public TextMarkerRuleElement(TextMarkerMatcher matcher, RuleElementQuantifier quantifier,
          List<AbstractTextMarkerCondition> conditions, List<AbstractTextMarkerAction> actions,
          TextMarkerBlock parent) {
    super(quantifier);
    this.matcher = matcher;
    this.conditions = conditions;
    this.actions = actions;
    this.parent = parent;
    if (conditions == null) {
      this.conditions = new ArrayList<AbstractTextMarkerCondition>();
    }
    if (actions == null) {
      this.actions = new ArrayList<AbstractTextMarkerAction>();
    }
  }

  public List<TextMarkerBasic> getAnchors(TextMarkerStream symbolStream) {
    return matcher.getMatchingBasics(symbolStream, getParent());
  }

  public FSIterator<AnnotationFS> getAnchors2(TextMarkerStream symbolStream) {
    return matcher.getMatchingBasics2(symbolStream, getParent());
  }

  public RuleElementMatch match(TextMarkerBasic currentBasic, TextMarkerStream stream,
          InferenceCrowd crowd) {
    RuleElementMatch result = new RuleElementMatch(this);
    List<EvaluatedCondition> conditionResults = new ArrayList<EvaluatedCondition>(conditions.size());

    boolean matched = true;
    boolean base = matcher.match(currentBasic, stream, getParent());
    Type type = matcher.getType(getParent(), stream);
    // TODO rethink cap init
    List<AnnotationFS> textsMatched = new ArrayList<AnnotationFS>(1);
    if (base) {
      for (AbstractTextMarkerCondition condition : conditions) {
        crowd.beginVisit(condition, null);
        EvaluatedCondition eval = condition.eval(currentBasic, type, this, stream, crowd);
        crowd.endVisit(condition, null);
        matched &= eval.isValue();
        conditionResults.add(eval);
      }
    }
    if (currentBasic != null) {
      textsMatched.add(stream.expandAnchor(currentBasic, type));
    }
    result.setMatchInfo(base, textsMatched, conditionResults);
    return result;
  }

  public void apply(RuleMatch matchInfos, TextMarkerStream symbolStream, InferenceCrowd crowd) {
    for (AbstractTextMarkerAction action : actions) {
      crowd.beginVisit(action, null);
      action.execute(matchInfos, this, symbolStream, crowd);
      crowd.endVisit(action, null);
    }
  }

  @Override
  public String toString() {
    return matcher.toString() + " " + quantifier.getClass().getSimpleName()
            + (conditions.isEmpty() ? "" : "(" + conditions.toString() + ")" + "\\n")
            + (actions.isEmpty() ? "" : "{" + actions.toString() + "}");
  }

  @Override
  public RuleElementQuantifier getQuantifier() {
    return quantifier;
  }

  public TextMarkerMatcher getMatcher() {
    return matcher;
  }

  public List<AbstractTextMarkerCondition> getConditions() {
    return conditions;
  }

  public List<AbstractTextMarkerAction> getActions() {
    return actions;
  }

  public TextMarkerBlock getParent() {
    return parent;
  }

}
