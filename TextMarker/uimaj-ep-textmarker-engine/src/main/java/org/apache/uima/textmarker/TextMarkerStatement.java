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

package org.apache.uima.textmarker;

import org.apache.uima.textmarker.visitor.InferenceCrowd;

public abstract class TextMarkerStatement extends TextMarkerElement {

  private TextMarkerBlock parent;

  public TextMarkerStatement(TextMarkerBlock parent) {
    super();
    this.parent = parent;
  }

  public abstract ScriptApply apply(TextMarkerStream stream, InferenceCrowd crowd);

  public abstract TextMarkerEnvironment getEnvironment();

  public TextMarkerBlock getParent() {
    return parent;
  }

  public void setParent(TextMarkerBlock parent) {
    this.parent = parent;
  }

}
