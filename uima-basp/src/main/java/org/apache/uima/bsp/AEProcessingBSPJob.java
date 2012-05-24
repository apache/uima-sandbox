package org.apache.uima.bsp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hama.bsp.BSP;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.message.type.BSPMessage;
import org.apache.hama.bsp.message.type.ByteMessage;
import org.apache.hama.bsp.sync.SyncException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasPool;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.XMLInputSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * A {@link BSP} which gets files from a directory and pass them to an {@link AnalysisEngine} for processing.
 * Finally a file is written which collects all the {@link ProcessTrace}s from the different nodes.
 */
public class AEProcessingBSPJob<KI, VI, KO, VO, M extends ByteMessage> extends BSP<KI, VI, KO, VO, BSPMessage> {

  private CasPool casPool;

  private String master;
  private final Random r = new Random();

  @Override
  public void setup(BSPPeer<KI, VI, KO, VO, BSPMessage> peer) throws IOException, SyncException, InterruptedException {
    super.setup(peer);
    master = peer.getAllPeerNames()[0];
  }

  @Override
  public void bsp(BSPPeer<KI, VI, KO, VO, BSPMessage> bspPeer) throws IOException, SyncException, InterruptedException {
    try {
      Configuration configuration = bspPeer.getConfiguration();
      // first superstep

      // AE instantiation
      String aePath = configuration.get("uima.ae.path");
      AnalysisEngineDescription analysisEngineDescription = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(new XMLInputSource(aePath));
      AnalysisEngine analysisEngine = UIMAFramework.produceAnalysisEngine(analysisEngineDescription);

      // AE initialization
      try {
        analysisEngine.initialize(analysisEngineDescription, new HashMap<String, Object>());
      } catch (Exception e) {
        // do nothing
      }
      Integer casPoolSize = Integer.valueOf(bspPeer.getConfiguration().get("cas.pool.size"));
      casPool = new CasPool(casPoolSize, analysisEngine);

      // collection distribution
      if (isMaster(bspPeer)) {
        String dirPath = configuration.get("collection.path");
        int i = 0;
        for (File f : new File(dirPath).listFiles()) {
          FileReader fileReader = new FileReader(f);
          ByteMessage byteMessage = new ByteMessage(UUID.randomUUID().toString().getBytes("UTF-8"), fileReader.toString().getBytes("UTF-8"));
          fileReader.close();
          String toPeer = bspPeer.getAllPeerNames()[r.nextInt(bspPeer.getAllPeerNames().length)];
          bspPeer.send(toPeer, byteMessage);
          i++;
        }
      }
      bspPeer.sync();

      // second superstep

      // receive files to analyze
      ByteMessage currentMessage;
      while ((currentMessage = (ByteMessage) bspPeer.getCurrentMessage()) != null) {
        // get a CAS
        CAS cas = casPool.getCas();
        // populate with received text
        cas.setDocumentText(new String(currentMessage.getData()));
        // AE execution
        ProcessTrace pt = analysisEngine.process(cas);
        // release CAS
        casPool.releaseCas(cas);
        // send results
        bspPeer.send(master, new ByteMessage(UUID.randomUUID().toString().getBytes("UTF-8"), pt.toString().getBytes("UTF-8")));
      }
      bspPeer.sync();

      // third superstep

      // collect analysis results
      if (isMaster(bspPeer)) {
        StringBuilder stringBuilder = new StringBuilder();
        ByteMessage bspMessage;
        while ((bspMessage = (ByteMessage) bspPeer.getCurrentMessage()) != null) {
          stringBuilder.append(new String(bspMessage.getData()));
        }
        File f = new File(configuration.get("output.file"));
        f.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        fileOutputStream.write(stringBuilder.toString().getBytes("UTF-8"));
        fileOutputStream.flush();
        fileOutputStream.close();
      }

      // destroy the used AE
      try {
        analysisEngine.destroy();
      } catch (Exception e) {
        // do nothing
      }

    } catch (InvalidXMLException e) {
      throw new InterruptedException(e.getLocalizedMessage());
    } catch (AnalysisEngineProcessException e) {
      throw new InterruptedException(e.getLocalizedMessage());
    } catch (ResourceInitializationException e) {
      throw new InterruptedException(e.getLocalizedMessage());
    }
  }

  private boolean isMaster(BSPPeer<KI, VI, KO, VO, BSPMessage> bspPeer) {
    return bspPeer.getPeerName().equals(master);
  }
}
