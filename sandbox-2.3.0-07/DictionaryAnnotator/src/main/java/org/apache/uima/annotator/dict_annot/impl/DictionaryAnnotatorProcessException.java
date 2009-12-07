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
package org.apache.uima.annotator.dict_annot.impl;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;

/**
 * DictionaryAnnotatorProcessException is thrown if an annotator processing error
 * occurs.
 */
public class DictionaryAnnotatorProcessException extends
      AnalysisEngineProcessException {

   private static final long serialVersionUID = 8446506776134629247L;

   /**
    * Creates a new exception with a the specified message from the
    * DictionaryAnnotator message catalog.
    * 
    * @param aMessageKey
    *           an identifier that maps to the message for this exception. The
    *           message may contain place holders for arguments as defined by
    *           the {@link java.text.MessageFormat MessageFormat} class.
    * @param aArguments
    *           The arguments to the message. <code>null</code> may be used if
    *           the message has no arguments.
    */
   public DictionaryAnnotatorProcessException(String aMessageKey,
         Object[] aArguments) {
      super(DictionaryAnnotator.MESSAGE_DIGEST, aMessageKey, aArguments);
   }
}
