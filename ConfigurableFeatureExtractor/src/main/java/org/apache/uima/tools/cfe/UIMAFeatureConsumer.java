package org.apache.uima.tools.cfe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.tools.cfe.support.ComparableArray;

;

public class UIMAFeatureConsumer
    extends CasConsumer_ImplBase
{
    public static final String PARAM_OUTPUTDIRECTORY = "OutputDirectory";
    

    private String m_outdir;
    private UIMAFeatureMatcher m_uimaFM;


    public void initialize ()
    throws ResourceInitializationException
    {
        super.initialize();
        
        try {
            m_uimaFM = new UIMAFeatureMatcher(
                    ((Boolean)getConfigParameterValue(CommonFeatureMatcher.PARAM_INCLUDETARGETID)).booleanValue());
            
            String cfgFile = (String)getConfigParameterValue(CommonFeatureMatcher.PARAM_CONFIGURATIONFILE);
            if (null != cfgFile) {
                Boolean isXMLBeansarser = (Boolean)getConfigParameterValue(CommonFeatureMatcher.PARAM_XMLBEANSPARSER);
                m_uimaFM.initialize(cfgFile, null == isXMLBeansarser ? false : isXMLBeansarser.booleanValue());;
            }

            m_outdir = (String)getConfigParameterValue(PARAM_OUTPUTDIRECTORY);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResourceInitializationException(e);
        }
    }
    
    public void processCas (CAS cas)
    throws ResourceProcessException
    {
        try {

            JCas jcas = cas.getJCas();
            
            String docId = CommonFeatureMatcher.getDocumentId(jcas, null);
            System.out.println("Processing " + docId);
            
            m_uimaFM.processJCas(jcas);
            if (m_uimaFM.m_featureImages.isEmpty()) {
                return;
            }
            
            File f = new File(m_outdir);
            if (!f.exists()) {
                System.err.println("Directory " + m_outdir + " does not exist, creating");
                f.mkdirs();
            }
            
            FileOutputStream fos = new FileOutputStream(m_outdir + "/" + docId + ".fve");
            for (Iterator<ComparableArray> it = m_uimaFM.m_featureImages.keySet().iterator(); it.hasNext();) {
                fos.write((m_uimaFM.m_featureImages.get(it.next()) + "\n").getBytes());
            }
        } 
        catch (Exception e) {
            throw new ResourceProcessException(e);
        }   
    }
}
