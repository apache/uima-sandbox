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
package org.apache.uima.bsp;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSPJob;

/**
 * Convenience class to execute a {@link BasicAEProcessingBSPJob}
 */
public class BSPAnalysisEngineExecutor {

  public void executeAE(String aePath, String collectionPath, String outputFilePath, Integer casPoolSize) throws Exception {
    HamaConfiguration conf = new HamaConfiguration();
    // set specific job parameters
    conf.set("uima.ae.path", aePath);
    conf.set("output.file", outputFilePath);
    conf.set("cas.pool.size", String.valueOf(casPoolSize));
    conf.set("collection.path", collectionPath);

    BSPJob job = new BSPJob(conf);
    // set the BSP class which shall be executed
    job.setBspClass(BasicAEProcessingBSPJob.class);
    // help Hama to locale the jar to be distributed
    job.setJarByClass(BasicAEProcessingBSPJob.class);
    // give it a name
    job.setJobName("BSP based UIMA AE executor");
    // use 4 tasks
    job.setNumBspTask(4);
    job.setOutputPath(new Path("/tmp"));
    // submit the job to the localrunner and wait for its completion, while outputting logs
    job.waitForCompletion(true);
  }

}
