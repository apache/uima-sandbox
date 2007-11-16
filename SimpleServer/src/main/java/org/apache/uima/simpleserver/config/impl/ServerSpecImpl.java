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


package org.apache.uima.simpleserver.config.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.TypeSystem;
import org.apache.uima.simpleserver.config.ServerSpec;
import org.apache.uima.simpleserver.config.SimpleServerException;
import org.apache.uima.simpleserver.config.TypeMap;

/**
 * Implementation of ServerSpec interface.
 */
public class ServerSpecImpl implements ServerSpec {
  
  private final String shortDescription;
  
  private final String longDescription;
  
  private final List<TypeMap> typeMaps = new ArrayList<TypeMap>();
  
  public ServerSpecImpl(String shortDesc, String longDesc) {
    super();
    this.shortDescription = shortDesc;
    this.longDescription = longDesc;
  }

  public void addTypeMap(TypeMap typeMap) {
    this.typeMaps.add(typeMap);
  }

  public String getLongDescription() {
    return this.longDescription;
  }

  public String getShortDescription() {
    return this.shortDescription;
  }

  public List<TypeMap> getTypeSpecs() {
    return this.typeMaps;
  }

  public List<SimpleServerException> validate(TypeSystem typeSystem) {
    // TODO Auto-generated method stub
    return null;
  }

}
