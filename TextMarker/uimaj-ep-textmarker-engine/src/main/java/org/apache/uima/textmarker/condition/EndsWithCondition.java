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

package org.apache.uima.textmarker.condition;

import java.util.List;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.textmarker.TextMarkerStream;
import org.apache.uima.textmarker.expression.list.TypeListExpression;
import org.apache.uima.textmarker.expression.type.TypeExpression;
import org.apache.uima.textmarker.rule.EvaluatedCondition;
import org.apache.uima.textmarker.rule.TextMarkerRuleElement;
import org.apache.uima.textmarker.type.TextMarkerBasic;
import org.apache.uima.textmarker.visitor.InferenceCrowd;

public class EndsWithCondition extends TypeSentiveCondition {

  public EndsWithCondition(TypeExpression type) {
    super(type);
  }

  public EndsWithCondition(TypeListExpression list) {
    super(list);
  }

  @Override
  public EvaluatedCondition eval(TextMarkerBasic basic, Type matchedType,
          TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd) {
    AnnotationFS matched = basic.getType(matchedType.getName());
    if (!isWorkingOnList()) {
      Type givenType = type.getType(element.getParent());
      boolean result = check(stream, matched, givenType);
      return new EvaluatedCondition(this, result);
    } else {
      boolean result = false;
      List<Type> types = getList().getList(element.getParent());
      for (Type t : types) {
        result |= check(stream, matched, t);
        if (result) {
          break;
        }
      }
      return new EvaluatedCondition(this, result);
    }
  }

  private boolean check(TextMarkerStream stream, AnnotationFS matched, Type givenType) {
    boolean result = false;
    // List<AnnotationFS> inWindow = stream.getAnnotationsInWindow(matched, givenType);
    // for (AnnotationFS annotationFS : inWindow) {
    // if (annotationFS.getEnd() == matched.getEnd()) {
    // result = true;
    // break;
    // }
    // }
    // TODO rewrite the code above
    // check annotations that start before
    if (!result) {
      List<TextMarkerBasic> basicsInWindow = stream.getBasicsInWindow(matched);
      if (!basicsInWindow.isEmpty()) {
        TextMarkerBasic last = basicsInWindow.get(basicsInWindow.size() - 1);
        if (last.isPartOf(givenType.getName())) {
          // there is something
          if (last.isAnchorOf(givenType.getName())) {
            int end = last.getType(givenType.getName()).getEnd();
            if (end == matched.getEnd()) {
              return true;
            }
          } else {
            stream.moveTo(last);
            while (stream.isValid()) {
              AnnotationFS fs = stream.get();
              if (fs instanceof TextMarkerBasic) {
                TextMarkerBasic b = (TextMarkerBasic) fs;
                if (b.isAnchorOf(givenType.getName())) {
                  int end = b.getType(givenType.getName()).getEnd();
                  if (end == matched.getEnd()) {
                    return true;
                  }
                }
              }
              stream.moveToPrevious();
            }
          }
        }

      }
    }
    return result;
  }

}
