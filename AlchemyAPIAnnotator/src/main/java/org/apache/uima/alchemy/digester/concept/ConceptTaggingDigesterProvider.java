/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.uima.alchemy.digester.concept;

import org.apache.commons.lang.Validate;
import org.apache.uima.alchemy.digester.DigesterProvider;
import org.apache.uima.alchemy.digester.OutputDigester;
import org.apache.uima.alchemy.digester.exception.UnsupportedResultFormatException;

public class ConceptTaggingDigesterProvider implements DigesterProvider {
  public OutputDigester getDigester(String type) throws UnsupportedResultFormatException {
    Validate.notEmpty(type);
    OutputDigester digester = null;
    if (type.equals("json")) {
      digester = new JsonTextConceptDigester();
    } else if (type.equals("xml")) {
      digester = new XMLTextConceptDigester();
    } else
      throw new UnsupportedResultFormatException(type);
    return digester;
  }
}
