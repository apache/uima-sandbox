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
package org.apache.uima.rdf;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.admin.CASFactory;
import org.apache.uima.cas.admin.CASMgr;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.test.junit_extension.AnnotatorTester;
import org.junit.Test;


/**
 */
public class RDFCASConsumerTest {
  private static final String text = "President Obama vows to \"make BP pay\" for the Gulf oil spill, and says the US must end its fossil fuel \"addiction\"";

  @Test
  public void testSerializing() {
    try {

      TypeSystem ts = new TypeSystemImpl();

      CASMgr casMgr = CASFactory.createCAS(ts);
      casMgr.initCASIndexes();

      AnnotatorTester tester = new AnnotatorTester("src/test/resources/TestRDFCASConsumerDescriptor.xml");
      casMgr.getIndexRepositoryMgr().commit();

      CAS cas = casMgr.getCAS();

      JCas jCas = cas.getCurrentView().getJCas();
      jCas.setDocumentText(text);
      Annotation annotation = new Annotation(jCas);
      annotation.setBegin(9);
      annotation.setEnd(15);
      annotation.addToIndexes();

      Annotation annotation2 = new Annotation(jCas);
      annotation2.setBegin(16);
      annotation2.setEnd(20);
      annotation2.addToIndexes();

      tester.performTest(cas);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
