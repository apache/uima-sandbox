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

package org.apache.uima.tools.cfe;


import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.analysis_engine.annotator.AnnotatorConfigurationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.analysis_engine.annotator.JTextAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;




public class RRMFeatureAnnotator
    extends JTextAnnotator_ImplBase
{
    private CommonFeatureMatcher m_cfaImpl;
    
    public void initialize (AnnotatorContext ac)
        throws AnnotatorConfigurationException, AnnotatorInitializationException
    {
        super.initialize(ac);
        
        // Process configration parameters
        try {
            
            m_cfaImpl = new RRMFeatureMatcher(
                    ((Boolean)ac.getConfigParameterValue(CommonFeatureMatcher.PARAM_INCLUDEANNOTATIONNAME)).booleanValue(),
                    ((Boolean)ac.getConfigParameterValue(CommonFeatureMatcher.PARAM_INCLUDEFEATURENAME)).booleanValue(),
                    ((Boolean)ac.getConfigParameterValue(RRMFeatureMatcher.PARAM_INCLUDEFEATUREWINDOWIMAGE)).booleanValue());
            m_cfaImpl.initialize(
                    (String)ac.getConfigParameterValue(CommonFeatureMatcher.PARAM_CONFIGURATIONFILE),
                    ((Boolean)ac.getConfigParameterValue(CommonFeatureMatcher.PARAM_XMLBEANSPARSER)).booleanValue());
        }
        catch (Exception e) {
            // TODO UIMA style exception
            throw new AnnotatorConfigurationException(e);
        }
    }

    public void process (JCas jcas, ResultSpecification arg1)
    throws AnnotatorProcessException
    {
        try {
            m_cfaImpl.processJCas(jcas);
        }
        catch (Exception e) {
            throw new AnnotatorProcessException(e);
        }
    }
}
