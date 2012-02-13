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

import java.util.List;

import org.apache.uima.cas.Type;
import org.apache.uima.textmarker.TextMarkerBlock;
import org.apache.uima.textmarker.TextMarkerEnvironment;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.expression.TextMarkerExpression;
import org.apache.uima.textmarker.expression.bool.BooleanExpression;
import org.apache.uima.textmarker.expression.list.ListExpression;
import org.apache.uima.textmarker.expression.number.NumberExpression;
import org.apache.uima.textmarker.expression.string.StringExpression;
import org.apache.uima.textmarker.expression.type.TypeExpression;
import org.apache.uima.textmarker.rule.RuleElement;
import org.apache.uima.textmarker.rule.RuleMatch;
import org.apache.uima.textmarker.visitor.InferenceCrowd;

public class AddAction extends AbstractTextMarkerAction {

  private String var;

  private List<TextMarkerExpression> elements;

  public AddAction(String var, List<TextMarkerExpression> list) {
    super();
    this.var = var;
    this.elements = list;
  }

  public String getListExpr() {
    return var;
  }

  public List<TextMarkerExpression> getElements() {
    return elements;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void execute(RuleMatch match, RuleElement element, TextMarkerStream stream,
          InferenceCrowd crowd) {
    TextMarkerBlock parent = element.getParent();
    TextMarkerEnvironment environment = parent.getEnvironment();
    List list = environment.getVariableValue(var, List.class);
    // Class<?> vtype = environment.getVariableType(var);
    Class<?> vgtype = environment.getVariableGenericType(var);
    for (TextMarkerExpression each : elements) {
      if (each instanceof ListExpression) {
        ListExpression l = (ListExpression) each;
        list.addAll(l.getList(parent));
      } else if (vgtype.equals(Boolean.class) && each instanceof BooleanExpression) {
        list.add(((BooleanExpression) each).getBooleanValue(parent));
      } else if (vgtype.equals(Integer.class) && each instanceof NumberExpression) {
        list.add(((NumberExpression) each).getIntegerValue(parent));
      } else if (vgtype.equals(Double.class) && each instanceof NumberExpression) {
        list.add(((NumberExpression) each).getDoubleValue(parent));
      } else if (vgtype.equals(Type.class) && each instanceof TypeExpression) {
        list.add(((TypeExpression) each).getType(parent));
      } else if (vgtype.equals(String.class) && each instanceof StringExpression) {
        list.add(((StringExpression) each).getStringValue(parent));
      }
    }
  }
}
