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
package org.apache.uima.simpleserver.as;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.simpleserver.Service;
import org.apache.uima.simpleserver.SimpleServerException;
import org.apache.uima.simpleserver.output.Result;
import org.apache.xmlbeans.XmlException;

public class UimaAsSerive extends Service {

    protected UimaAsynchronousEngine uimaASEngine = null;

    /**
     * Start time of the processing - used to compute elapsed time.
     */
    private long mStartTime;
    
    private int replyWindow = 0;

    private File outputDir = null;
    
    private int timeout = 0;

    private boolean ignoreErrors = false;


    public UimaAsSerive() {
        super();
    }
    
    /**
     * Configure UIMA AS Service
     * 
     * @return void
     * @throws Exception 
     */
    public void configureUimaASService (String brokerUrl, String inputQueueName, File resultSpecXMLFile) throws Exception {
       
        // Create Asynchronous Engine
        uimaASEngine = new BaseUIMAAsynchronousEngine_impl();
        
//        uimaASEngine.addStatusCallbackListener(new StatusCallbackListenerImpl());

        //set server URI and Endpoint
        Map appCtx = new HashMap();
        // Add Broker URI
        appCtx.put(UimaAsynchronousEngine.ServerUri, brokerUrl);
        // Add Queue Name
        appCtx.put(UimaAsynchronousEngine.Endpoint, inputQueueName);
        // Add timeout
        if (timeout > 0) {
          appCtx.put(UimaAsynchronousEngine.Timeout, timeout);
        }
        // Add the Cas Pool Size (2 should be the most that's ever needed, one
        // for the request and one for the response)
        appCtx.put(UimaAsynchronousEngine.CasPoolSize, 2);
        
        if (replyWindow > 0) {
          // Allow so many outstanding CASes. This is used to gate how many CASes
          // are sent to a service queue. When the max number of CASes in sent
          // and no reply is received, the code will not send any more and will
          // block the client.
          appCtx.put(UimaAsynchronousEngine.ReplyWindow, replyWindow);
        }
        
        //initialize
        uimaASEngine.initialize(appCtx);

        this.cas = uimaASEngine.getCAS();
        
        configure(resultSpecXMLFile);
    }
    
    /**
     * Calls the services analysis engine on the input text, filters and produces the result.
     */
    @Override
    public synchronized Result process(String text, String lang) {
        
        mStartTime = System.currentTimeMillis();


        // Check that service has been initialized.
        if (!this.initialized) {
          logInitializationError();
          return null;
        }
        this.cas.reset();
        this.cas.setDocumentText(text);
        if (lang != null) {
          this.cas.setDocumentLanguage(lang);
        } else {
            this.cas.setDocumentLanguage("en");
        }
        try {
            uimaASEngine.sendAndReceiveCAS(this.cas);
            // uimaASEngine.collectionProcessingComplete();
        } catch (ResourceProcessException e) {
            e.printStackTrace();
            return null;
        }

        // System.out.println("processed: " + cas.getDocumentText());
        // this.cas.setDocumentText(text);
        return this.resultExtractor.getResult(this.cas, this.serviceSpec);
    }
    
    /**
     * Callback Listener. Receives event notifications from CPE.
     * 
     * 
     */
    class StatusCallbackListenerImpl implements UimaASStatusCallbackListener {
      int entityCount = 0;

      long size = 0;

      /**
       * Called when the initialization is completed.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#initializationComplete()
       */
      public void initializationComplete() {
        System.out.println("UIMAEE Initialization Complete");
      }

      /**
       * Called when the batchProcessing is completed.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#batchProcessComplete()
       * 
       */
      public void batchProcessComplete() {
        System.out.print("Completed " + entityCount + " documents");
        if (size > 0) {
          System.out.print("; " + size + " characters");
        }
        System.out.println();
        long elapsedTime = System.currentTimeMillis() - mStartTime;
        System.out.println("Time Elapsed : " + elapsedTime + " ms ");
      }

      /**
       * Called when the collection processing is completed.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#collectionProcessComplete()
       */
      public void collectionProcessComplete() {
        System.out.print("Completed " + entityCount + " documents");
        if (size > 0) {
          System.out.print("; " + size + " characters");
        }
        System.out.println();
        long elapsedTime = System.currentTimeMillis() - mStartTime;
        System.out.println("Time Elapsed : " + elapsedTime + " ms ");
        
        String perfReport = uimaASEngine.getPerformanceReport();
        if (perfReport != null) {
          System.out.println("\n\n ------------------ PERFORMANCE REPORT ------------------\n");
          System.out.println(uimaASEngine.getPerformanceReport());
        }
        // stop the JVM.
        System.exit(1);
      }

      /**
       * Called when the CPM is paused.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#paused()
       */
      public void paused() {
        System.out.println("Paused");
      }

      /**
       * Called when the CPM is resumed after a pause.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#resumed()
       */
      public void resumed() {
        System.out.println("Resumed");
      }

      /**
       * Called when the CPM is stopped abruptly due to errors.
       * 
       * @see org.apache.uima.collection.processing.StatusCallbackListener#aborted()
       */
      public void aborted() {
        System.out.println("Aborted");
        // stop the JVM. 
        System.exit(1);
      }

      /**
       * Called when the processing of a Document is completed. <br>
       * The process status can be looked at and corresponding actions taken.
       * 
       * @param aCas
       *          CAS corresponding to the completed processing
       * @param aStatus
       *          EntityProcessStatus that holds the status of all the events for aEntity
       */
      public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
        System.out.println("Received Completed Analysis");
        if (aStatus != null && aStatus.isException()) {
          List exceptions = aStatus.getExceptions();
          for (int i = 0; i < exceptions.size(); i++) {
            ((Throwable) exceptions.get(i)).printStackTrace();
          }
          if (!ignoreErrors) {
            System.err.println("Terminating Client...");
            // uimaEEEngine.stop();  TODO: Does not seem to work
            // return;
            System.exit(1);
          }
        }
        
        //if output dir specified, dump CAS to XMI
        if (outputDir != null) {
          // try to retreive the filename of the input file from the CAS
          File outFile = null;
          Type srcDocInfoType = aCas.getTypeSystem().getType("org.apache.uima.examples.SourceDocumentInformation");
          if (srcDocInfoType != null) {
            FSIterator it = aCas.getIndexRepository().getAllIndexedFS(srcDocInfoType);
            if (it.hasNext()) {
              FeatureStructure srcDocInfoFs = it.get();
              Feature uriFeat = srcDocInfoType.getFeatureByBaseName("uri");
              Feature offsetInSourceFeat = srcDocInfoType.getFeatureByBaseName("offsetInSource");
              String uri = srcDocInfoFs.getStringValue(uriFeat);
              int offsetInSource = srcDocInfoFs.getIntValue(offsetInSourceFeat);
              File inFile;
              try {
                inFile = new File(new URL(uri).getPath());
                String outFileName = inFile.getName();
                if (offsetInSource > 0) {
                  outFileName += ("_" + offsetInSource);
                }
                outFileName += ".xmi";
                outFile = new File(outputDir, outFileName);
              } catch (MalformedURLException e1) {
              // invalid URI, use default processing below
              }
            }
          }
          if (outFile == null) {
            outFile = new File(outputDir, "doc" + entityCount);
          }
          try {
            FileOutputStream outStream = new FileOutputStream(outFile);
            try {
              XmiCasSerializer.serialize(aCas, outStream);
            }
            finally {
              outStream.close();
            }
          } catch (Exception e) {
            System.err.println("Could not save CAS to XMI file");
            e.printStackTrace();
          }
        }
        
        //update stats
        entityCount++;
        String docText = aCas.getDocumentText();
        if (docText != null) {
          size += docText.length();
        }      
      }

      public void collectionProcessComplete(EntityProcessStatus arg0) {
          // TODO Auto-generated method stub
          
      }

      public void initializationComplete(EntityProcessStatus arg0) {
          // TODO Auto-generated method stub
          
      }
    }

    
}
