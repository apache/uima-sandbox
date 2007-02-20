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

package org.apache.uima.caseditor.ui.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.caseditor.core.IDocument;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.uima.AnnotatorConfiguration;
import org.apache.uima.resource.ResourceInitializationException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * TODO: synchronize filesystem after annotator run.
 * TODO: move this over to core plugin
 */
public final class AnnotatorActionRunnable implements IRunnableWithProgress
{
    private AnnotatorConfiguration mAnnotatorConfiguration;
    
    private Collection<DocumentElement> mDocuments;

    /**
     * Initializes a new instance.
     * 
     * @param annotator
     * @param documents
     * @param shell
     */
    public AnnotatorActionRunnable(AnnotatorConfiguration annotator,
            Collection<DocumentElement> documents)
    {
        mAnnotatorConfiguration = annotator;
        mDocuments = documents;
    }
    
    /**
     * Excecutes the action.
     */
    public void run(IProgressMonitor monitor) 
            throws InvocationTargetException, InterruptedException
    {
        monitor.beginTask("Tagging", IProgressMonitor.UNKNOWN);
        
        monitor.subTask("Initializing tagger, "
                + "please stand by.");
        
        AnalysisEngine annotatorInstance;
        
        try
        {
            annotatorInstance = mAnnotatorConfiguration
                    .createAnnotator();
        }
        catch (final ResourceInitializationException e)
        {
            throw new InvocationTargetException(e);
        }
        
        monitor.subTask("Tagging, please stand by.");
        
        for (DocumentElement element : mDocuments)
        {
        	
        	IDocument document = null;
			try {
				document = element.getDocument();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
        	
            try
            {
                annotatorInstance.process(document.getCAS());
            }
            catch (AnalysisEngineProcessException e)
            {
                throw new InvocationTargetException(e);
            } 
            
            // TODO: save it and notify other about changes
            
            try
            {
                element.saveDocument();
            }
            catch (CoreException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        annotatorInstance.destroy();
        annotatorInstance = null;
        
        monitor.done();
    }
}