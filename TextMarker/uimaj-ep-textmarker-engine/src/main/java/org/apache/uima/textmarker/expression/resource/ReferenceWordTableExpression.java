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

package org.apache.uima.textmarker.expression.resource;

import org.apache.uima.textmarker.TextMarkerStatement;
import org.apache.uima.textmarker.resource.TextMarkerTable;

public class ReferenceWordTableExpression extends WordTableExpression {

  private String ref;

  public ReferenceWordTableExpression(String ref) {
    super();
    this.ref = ref;
  }

  @Override
  public TextMarkerTable getTable(TextMarkerStatement element) {
    return element.getEnvironment().getVariableValue(ref, TextMarkerTable.class);
  }

  public String getRef() {
    return ref;
  }

}
