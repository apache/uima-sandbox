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

package org.apache.uima.tm.textmarker.cev.explain.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.tm.cev.data.CEVData;
import org.apache.uima.tm.textmarker.cev.TextMarkerCEVPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class BasicTokenStreamTableContentProvider implements IStructuredContentProvider {

  private List<BasicTokenEntry> basicTokenEntries;

  public BasicTokenStreamTableContentProvider(CEVData casData) {
    init(casData);
  }

  public void init(CEVData casData) {
    basicTokenEntries = new ArrayList<BasicTokenEntry>();

    Type basicType = casData.getCAS().getTypeSystem().getType(TextMarkerCEVPlugin.BASIC_TYPE);
    if (basicType != null) {
      AnnotationIndex anInd = casData.getCAS().getAnnotationIndex(basicType);
      FSIterator iti = anInd.iterator(true);
      iti.moveToFirst();
      int i = 0;
      BasicTokenEntry basicTokenEntry;
      while (iti.isValid()) {
        AnnotationFS annot = (AnnotationFS) iti.get();
        i++;

        basicTokenEntry = new BasicTokenEntry(annot, i);
        basicTokenEntries.add(basicTokenEntry);
        iti.moveToNext();
      }
    }
  }

  public Object[] getElements(Object inputElement) {
    return basicTokenEntries.toArray();
  }

  public void dispose() {
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

  public BasicTokenEntry getEntryAt(int offset) {
    for (BasicTokenEntry each : basicTokenEntries) {
      if (each.getAnnotation().getBegin() <= offset && each.getAnnotation().getEnd() >= offset) {
        return each;
      }
    }
    return null;
  }

}
