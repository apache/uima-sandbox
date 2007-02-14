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

package org.apache.uima.caseditor.editor.outline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.core.IDocument;
import org.apache.uima.caseditor.editor.AnnotationDocument;

/**
 * TODO: add javadoc here
 * 
 * @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
 * @version $Revision: 1.4.2.2 $, $Date: 2007/01/04 15:00:58 $
 */
public class AnnotationTreeNodeList {
  private List<AnnotationTreeNode> mElements = new ArrayList<AnnotationTreeNode>();

  private IDocument mDocument;

  AnnotationTreeNodeList(IDocument document) {
    mDocument = document;
  }

  AnnotationTreeNodeList(AnnotationDocument document, Collection<AnnotationFS> annotations) {
    mDocument = document;

    for (AnnotationFS annotation : annotations) {
      mElements.add(new AnnotationTreeNode(mDocument, annotation));
    }

    // buildTree();
  }

  List<AnnotationTreeNode> getElements() {
    return mElements;
  }

  void add(AnnotationTreeNode node) {
    mElements.add(node);
  }

  void remove(AnnotationTreeNode node) {
    if (mElements.contains(node)) {
      // insert childs in the list
      // remove the node from the list
      AnnotationTreeNode nodeFromList = mElements.get(mElements.indexOf(node));
    } else {
      // search the node
    }

    mElements.remove(node);
  }

  void buildTree() {
    for (Iterator it = mElements.iterator(); it.hasNext();) {
      AnnotationTreeNode aNode = (AnnotationTreeNode) it.next();

      boolean isMoved = false;

      for (AnnotationTreeNode bNode : mElements) {
        // if identical do nothing and go on
        if (aNode == bNode) {
          continue;
        }

        if (bNode.isChild(aNode)) {
          bNode.addChild(aNode);
          isMoved = true;
          break;
        }
      }

      if (isMoved) {
        it.remove();
      }
    }
  }
}