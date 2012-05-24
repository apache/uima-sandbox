package org.apache.uima.bsp;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSPJob;

/**
 * Convenience class to execute a {@link AEProcessingBSPJob}
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
    job.setBspClass(AEProcessingBSPJob.class);
    // help Hama to locale the jar to be distributed
    job.setJarByClass(AEProcessingBSPJob.class);
    // give it a name
    job.setJobName("BSP based UIMA AE executor");
    // use 4 tasks
    job.setNumBspTask(4);
    job.setOutputPath(new Path("/tmp"));
    // submit the job to the localrunner and wait for its completion, while outputting logs
    job.waitForCompletion(true);
  }

}
