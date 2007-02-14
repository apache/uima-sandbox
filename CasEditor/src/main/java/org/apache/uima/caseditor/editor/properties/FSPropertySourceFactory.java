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


import org.apache.uima.cas.ByteArrayFS;
import org.apache.uima.cas.DoubleArrayFS;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.LongArrayFS;
import org.apache.uima.cas.ShortArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.caseditor.editor.ModelFeatureStructure;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.1.2.2 $, $Date: 2007/01/04 15:00:57 $
 */
public class FSPropertySourceFactory {
  public static IPropertySource create(ModelFeatureStructure structure) {
    if (structure.getStructre() instanceof ByteArrayFS) {
      return new ByteArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof ShortArrayFS) {
      return new ShortArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof IntArrayFS) {
      return new IntegerArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof LongArrayFS) {
      return new LongArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof FloatArrayFS) {
      return new FloatArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof DoubleArrayFS) {
      return new DoubleArrayPropertySource(structure);
    } else if (structure.getStructre() instanceof StringArrayFS) {
      return new StringArrayPropertySource(structure);
    } else {
      return new FeatureStructurePropertySource(structure);
    }
  }
}
