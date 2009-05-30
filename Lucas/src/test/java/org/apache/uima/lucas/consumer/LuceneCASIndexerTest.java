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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.FSDirectory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.lucas.consumer.LuceneCASIndexer;
import org.apache.uima.lucas.indexer.FieldDescription;
import org.apache.uima.util.XMLInputSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LuceneCASIndexerTest {

  private static final String TOKEN_MAPPING_TXT = "tokenMapping.txt";

  private static final String HYPERNYM_ID1 = "id1";

  private static final Object HYPERNYM_ID2 = "id2";

  private static final String[] STOP_WORDS = new String[] { "na", "und", "nu" };

  private static final String FIELD_NAME = "annotation1";

  private static final String WRITE_LOCK = "write.lock";

  private static final String DESCRIPTOR_FILE = "src/main/resources/LuceneCASIndexer.xml";

  private static final String INDEX_DIRECTORY = "src/test/resources/testIndex";

  private LuceneCASIndexer consumer;

  @Before
  public void setUp() throws Exception {
    CasConsumerDescription consumerDescription =
            (CasConsumerDescription) UIMAFramework.getXMLParser().parseCasConsumerDescription(
                    new XMLInputSource(DESCRIPTOR_FILE));
    consumer = (LuceneCASIndexer) UIMAFramework.produceCasConsumer(consumerDescription);
  }

  @After
  public void tearDown() throws Exception {

    FSDirectory directory = (FSDirectory) consumer.getIndexWriter().getDirectory();
    File directoryFile = directory.getFile();
    consumer.destroy();

    directory = FSDirectory.getDirectory(directoryFile);

    // directory.deleteFile(WRITE_LOCK);
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
  public void testStopwordFile() {
    String[] stopwords = consumer.getFilterBuilder().getStopwords();
    assertArrayEquals(STOP_WORDS, stopwords);
  }

  @Test
  public void testHypernymFile() {
    Map<String, List<String>> hypernyms = consumer.getFilterBuilder().getHypernyms();
    assertEquals(2, hypernyms.size());
    assertTrue(hypernyms.containsKey(HYPERNYM_ID1));
    assertTrue(hypernyms.containsKey(HYPERNYM_ID2));
  }

  @Test
  public void testTokenMappingFile() {
    Map<String, Map<String, String>> tokenMappings = consumer.getFilterBuilder().getMappings();
    assertTrue(tokenMappings.containsKey(TOKEN_MAPPING_TXT));
    assertEquals(2, tokenMappings.get(TOKEN_MAPPING_TXT).size());
  }

  @Test
  public void testUniqueIndex() {
    String hostname = getHostName();
    String pid = getPID();

    FSDirectory directory = (FSDirectory) consumer.getIndexWriter().getDirectory();

    String path = directory.getFile().getPath();
    assertTrue(path.endsWith(INDEX_DIRECTORY + "-" + hostname + "-" + pid));
  }

  @Test
  public void testRamBufferSize() {
    assertEquals(512, consumer.getIndexWriter().getRAMBufferSizeMB(), 0);
  }

  @Test
  public void testCompoundFileFormat() {
    assertTrue(consumer.getIndexWriter().getUseCompoundFile());
  }

  protected String getPID() {
    String id = ManagementFactory.getRuntimeMXBean().getName();
    return id.substring(0, id.indexOf("@"));
  }

  public String getHostName() {
    InetAddress address;
    String hostName;
    try {
      address = InetAddress.getLocalHost();
      hostName = address.getHostName();
    } catch (UnknownHostException e) {
      throw new IllegalStateException(e);
    }

    return hostName;
  }
}
