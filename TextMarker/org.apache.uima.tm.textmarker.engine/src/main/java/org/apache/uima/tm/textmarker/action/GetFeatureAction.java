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

package org.apache.uima.tm.textmarker.action;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.tm.textmarker.kernel.TextMarkerEnvironment;
import org.apache.uima.tm.textmarker.kernel.TextMarkerStream;
import org.apache.uima.tm.textmarker.kernel.expression.string.StringExpression;
import org.apache.uima.tm.textmarker.kernel.rule.RuleMatch;
import org.apache.uima.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import org.apache.uima.tm.textmarker.kernel.visitor.InferenceCrowd;


public class GetFeatureAction extends AbstractTextMarkerAction {

  private StringExpression featureStringExpression;

  private String variable;

  public GetFeatureAction(StringExpression f, String variable) {
    super();
    this.featureStringExpression = f;
    this.variable = variable;
  }

  @Override
  public void execute(RuleMatch match, TextMarkerRuleElement element, TextMarkerStream stream,
          InferenceCrowd crowd) {
    Type type = element.getMatcher().getType(element.getParent(), stream);
    if (type == null)
      return;
    String stringValue = featureStringExpression.getStringValue(element.getParent());
    Feature featureByBaseName = type.getFeatureByBaseName(stringValue);
    AnnotationFS expandAnchor = stream.expandAnchor(match.getFirstBasic(), type);
    TextMarkerEnvironment environment = element.getParent().getEnvironment();

    if (expandAnchor.getType().getFeatureByBaseName(stringValue) == null) {
      System.out.println("Can't access feature " + stringValue
              + ", because it's not defined in the matched type: " + expandAnchor.getType());
      return;
    }

    if (environment.getVariableType(variable).equals(String.class)
            && featureByBaseName.getRange().getName().equals("uima.cas.String")) {
      Object value = expandAnchor.getStringValue(featureByBaseName);
      environment.setVariableValue(variable, value);
    } else if (environment.getVariableType(variable).equals(Integer.class)
            && featureByBaseName.getRange().getName().equals("uima.cas.Integer")) {
      Object value = expandAnchor.getIntValue(featureByBaseName);
      environment.setVariableValue(variable, value);
    } else if (environment.getVariableType(variable).equals(Double.class)
            && featureByBaseName.getRange().getName().equals("uima.cas.Double")) {
      Object value = expandAnchor.getDoubleValue(featureByBaseName);
      environment.setVariableValue(variable, value);
    } else if (environment.getVariableType(variable).equals(Boolean.class)
            && featureByBaseName.getRange().getName().equals("uima.cas.Boolean")) {
      Object value = expandAnchor.getBooleanValue(featureByBaseName);
      environment.setVariableValue(variable, value);
    } else if (environment.getVariableType(variable).equals(Type.class)
            && featureByBaseName.getRange().getName().equals("uima.cas.String")) {
      Object value = expandAnchor.getStringValue(featureByBaseName);
      Type t = stream.getCas().getTypeSystem().getType((String) value);
      if (t != null) {
        environment.setVariableValue(variable, t);
      }
    }
  }

  public StringExpression getFeatureStringExpression() {
    return featureStringExpression;
  }

  public String getVariable() {
    return variable;
  }
}
