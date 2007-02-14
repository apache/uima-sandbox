/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.caseditor.editor.properties;


import org.apache.uima.cas.ShortArrayFS;
import org.apache.uima.caseditor.editor.ModelFeatureStructure;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.2 $, $Date: 2007/01/04 15:00:57 $
 */
public class ShortArrayPropertySource extends AbstractArrayPropertySource {
  ShortArrayPropertySource(ModelFeatureStructure structure) {
    super(structure);
  }

  @Override
  protected String get(int i) {
    return Short.toString(((ShortArrayFS) mArray).get(i));
  }

  @Override
  protected Class<Short> getPrimitiveType() {
    return Short.class;
  }

  @Override
  protected void set(int i, String value) {
    ((ShortArrayFS) mArray).set(i, Short.parseShort(value));
  }
}
