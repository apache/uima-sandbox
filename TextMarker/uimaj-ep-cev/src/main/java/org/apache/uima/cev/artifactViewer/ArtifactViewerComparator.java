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

package org.apache.uima.cev.artifactViewer;

import java.util.Comparator;

import org.apache.uima.cev.extension.ICEVArtifactViewerFactory;


public class ArtifactViewerComparator implements Comparator<ICEVArtifactViewerFactory> {

  public int compare(ICEVArtifactViewerFactory o1, ICEVArtifactViewerFactory o2) {
    if (o1.getPriority() < o2.getPriority()) {
      return -1;
    } else if (o1.getPriority() > o2.getPriority()) {
      return 1;
    } else {
      return 0;
    }
  }

}
