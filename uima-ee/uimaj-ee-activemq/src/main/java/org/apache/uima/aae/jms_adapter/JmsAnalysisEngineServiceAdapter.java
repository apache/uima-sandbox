package org.apache.uima.aae.jms_adapter;


import java.util.Map;

import org.apache.uima.analysis_engine.service.impl.AnalysisEngineServiceAdapter;
import org.apache.uima.resource.CustomResourceSpecifier;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;

/**
 * Implementation of {@link AnalysisEngineServiceAdapter} for JMS.
 * 
 * 
 */
public class JmsAnalysisEngineServiceAdapter extends AnalysisEngineServiceAdapter {

  /**
   * @see org.apache.uima.resource.Resource#initialize(ResourceSpecifier, Map)
   */
  public boolean initialize(ResourceSpecifier aSpecifier, Map aAdditionalParams)
          throws ResourceInitializationException {
    // aSpecifier must be a CustomResourceSpecifier
    if (!(aSpecifier instanceof CustomResourceSpecifier)) {
      return false;
    }
    
    // create proxy to service
    setStub(new JmsAnalysisEngineServiceStub(this, ((CustomResourceSpecifier)aSpecifier).getParameters()));

    // do superclass initialization, which among other things initializes UimaContext.
    // note we need to establish connection to service before calling this, since
    // superclass initialization depends on having access to the component metadata.
    super.initialize(aSpecifier, aAdditionalParams);

    // Sofa mappings are currently not implemented for remote AEs.  Catch this
    // and report an error.
    if (getUimaContextAdmin().getSofaMap().size() > 0) {
      throw new ResourceInitializationException(ResourceInitializationException.SOFA_MAPPING_NOT_SUPPORTED_FOR_REMOTE,
              new Object[]{getMetaData().getName()});
    }
    
    return true;
  }



}
