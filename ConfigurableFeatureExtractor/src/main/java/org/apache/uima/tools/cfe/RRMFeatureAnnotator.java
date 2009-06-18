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
