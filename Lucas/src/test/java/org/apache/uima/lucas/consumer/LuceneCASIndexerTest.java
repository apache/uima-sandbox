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

package org.apache.uima.lucas.consumer;

import static org.easymock.EasyMock.capture;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.lucene.store.FSDirectory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.lucas.indexer.analysis.TokenFilterFactory;
import org.apache.uima.lucas.indexer.mapping.FieldDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableBiMap;

public class LuceneCASIndexerTest {

  private static final String pathSep = System.getProperty("file.separator");

  private static final String TEST_FILTER_ANNOTATION = "testFilterAnnotation";

  private static final String TEST_FILTER_FIELD = "testFilterField";

  private static final String FIELD_NAME = "annotation1";

  private static final String DESCRIPTOR_FILE = "src/main/resources/LuceneCASIndexer.xml";

  private static final String INDEX_DIRECTORY = "src" + pathSep
      + "test" + pathSep + "resources" + pathSep + "test-index";

  private LuceneCASIndexer consumer;

  @Before
  public void setUp() throws InvalidXMLException, IOException, ResourceInitializationException {

    CasConsumerDescription consumerDescription = (CasConsumerDescription) UIMAFramework
        .getXMLParser().parseCasConsumerDescription(new XMLInputSource(DESCRIPTOR_FILE));
    consumer = (LuceneCASIndexer) UIMAFramework.produceCasConsumer(consumerDescription);
  }

  @After
  public void tearDown() throws Exception {
    FSDirectory directory = (FSDirectory) consumer.getIndexWriter().getDirectory();
    File directoryFile = directory.getFile();
    consumer.destroy();

    directory = FSDirectory.getDirectory(directoryFile);

    for (String file : directory.list())
      directory.deleteFile(file);

    directory.getFile().delete();
  }

  @Test
  public void testIndexOutDir() {
    FSDirectory directory = (FSDirectory) consumer.getIndexWriter().getDirectory();

    String path = directory.getFile().getPath();
    assertTrue(path.contains(INDEX_DIRECTORY));
  }

  @Test
  public void testMappingFile() {
    Collection<FieldDescription> fieldDescriptions = consumer.getFieldDescriptions();
    assertEquals(1, fieldDescriptions.size());
    FieldDescription fieldDescription = fieldDescriptions.iterator().next();
    assertEquals(FIELD_NAME, fieldDescription.getName());
    assertEquals(2, fieldDescription.getAnnotationDescriptions().size());
  }

  @Test
  public void testPreloadResources() throws IOException {
    Collection<FieldDescription> fieldDescriptions = consumer.getFieldDescriptions();
    TokenFilterFactory testFactoryField = createMock(TokenFilterFactory.class);
    TokenFilterFactory testFactoryAnnotation = createMock(TokenFilterFactory.class);

    Capture<Properties> propertiesCaptureField = new Capture<Properties>();
    Capture<Properties> propertiesCaptureAnnotation = new Capture<Properties>();

    testFactoryField.preloadResources(capture(propertiesCaptureField));
    testFactoryAnnotation.preloadResources(capture(propertiesCaptureAnnotation));

    replay(testFactoryField);
    replay(testFactoryAnnotation);

    consumer.preloadResources(fieldDescriptions, ImmutableBiMap.of(TEST_FILTER_ANNOTATION,
        testFactoryAnnotation, TEST_FILTER_FIELD, testFactoryField));
    verify(testFactoryField);
    verify(testFactoryAnnotation);

    Properties fieldFilterProperties = propertiesCaptureField.getValue();
    assertEquals("value1", fieldFilterProperties.getProperty("key1"));

    Properties annotationFilterProperties = propertiesCaptureAnnotation.getValue();
    assertEquals("value2", annotationFilterProperties.getProperty("key2"));
  }

}
