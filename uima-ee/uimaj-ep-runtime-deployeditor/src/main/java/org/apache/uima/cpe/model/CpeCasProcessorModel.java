/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 11, 2006, 4:47:13 PM
 * source:  CpeCasProcessorModel.java
 */
package org.apache.uima.cpe.model;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CasProcessorDeploymentParams;
import org.apache.uima.collection.metadata.CasProcessorErrorHandling;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCheckpoint;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.tools.debug.util.Trace;


/**
 * 
 *
 */
public class CpeCasProcessorModel 
{
    // Link back to CPE Descriptor Model
//    private CpeDescriptorModel          mCpeDescriptorModel;

    private CpeCasProcessor             cpeCasProcessor;
//    private ResourceSpecifier           specifier;
    private ConfigParametersModel       configParamsModel;
    private ConfigurationParameterDeclarations  configParamDecls = null;
    private ConfigurationParameterSettings      configParamSettings = null;
    
    /*************************************************************************/
    
    public CpeCasProcessorModel(CpeDescriptorModel parentModel,
                                CpeCasProcessor cpeCasProcessor, ResourceSpecifier specifier) {
        super();
//        this.mCpeDescriptorModel = parentModel;
        this.cpeCasProcessor = cpeCasProcessor;
//        this.specifier       = specifier;
        if (specifier instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) specifier).getAnalysisEngineMetaData();            
            configParamDecls = a.getConfigurationParameterDeclarations();
            configParamSettings = a.getConfigurationParameterSettings();
            
        } else if (specifier instanceof CasConsumerDescription) {
            configParamDecls = ((CasConsumerDescription) specifier).getCasConsumerMetaData()
                        .getConfigurationParameterDeclarations();
            configParamSettings = ((CasConsumerDescription) specifier).getCasConsumerMetaData()
                        .getConfigurationParameterSettings();            
        }
        Trace.trace("Create CpeCasProcessorModel for "+ cpeCasProcessor.getName());
        configParamsModel = new ConfigParametersModel(configParamDecls, configParamSettings,
                                        cpeCasProcessor.getConfigurationParameterSettings());
    }
    
    /*************************************************************************/

//    public UimaCasProcessor getUimaCasProcessor ()
//    {
//        return mCpeDescriptorModel.getUimaCasProcessor(this.getName());
//    }
        
    /**
     * @return Returns the configParamsModel.
     */
    public ConfigParametersModel getConfigParamsModel() {
        return configParamsModel;
    }

    /**
     * @param configParamsModel The configParamsModel to set.
     */
    public void setConfigParamsModel(ConfigParametersModel configParamsModel) {
        this.configParamsModel = configParamsModel;
    }

    public ConfigurationParameterDeclarations getConfigurationParameterDeclarations () {
        return configParamDecls;
    }

    public ConfigurationParameterSettings getConfigurationParameterSettings () {
        return configParamSettings;
    }

    /*************************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getConfigurationParameterSettings()
     */
    public CasProcessorConfigurationParameterSettings getCasProcessorConfigurationParameterSettings() {
        return cpeCasProcessor.getConfigurationParameterSettings();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getDescriptor()
     */
    public String getDescriptor() {
        return cpeCasProcessor.getDescriptor();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getName()
     */
    public String getName() {
        return cpeCasProcessor.getName();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getActionOnMaxError()
     */
    public String getActionOnMaxError() {
        return cpeCasProcessor.getActionOnMaxError();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getActionOnMaxRestart()
     */
    public String getActionOnMaxRestart() {
        return cpeCasProcessor.getActionOnMaxRestart();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getBatchSize()
     */
    public int getBatchSize() {
        return cpeCasProcessor.getBatchSize();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getCasProcessorFilter()
     */
    public String getCasProcessorFilter() {
        return cpeCasProcessor.getCasProcessorFilter();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getCheckpoint()
     */
    public CpeCheckpoint getCheckpoint() {
        return cpeCasProcessor.getCheckpoint();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getDeployment()
     */
    public String getDeployment() {
        return cpeCasProcessor.getDeployment();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getDeploymentParams()
     */
    public CasProcessorDeploymentParams getDeploymentParams() {
        return cpeCasProcessor.getDeploymentParams();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getErrorHandling()
     */
    public CasProcessorErrorHandling getErrorHandling() {
        return cpeCasProcessor.getErrorHandling();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getMaxErrorCount()
     */
    public int getMaxErrorCount() {
        return cpeCasProcessor.getMaxErrorCount();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getMaxErrorSampleSize()
     */
    public int getMaxErrorSampleSize() {
        return cpeCasProcessor.getMaxErrorSampleSize();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getMaxRestartCount()
     */
    public int getMaxRestartCount() {
        return cpeCasProcessor.getMaxRestartCount();
    }

    /* (non-Javadoc)
     * @see org.apache.uima.collection.metadata.CpeCasProcessor#getTimeout()
     */
    public int getTimeout() {
        return cpeCasProcessor.getTimeout();
    }

}
