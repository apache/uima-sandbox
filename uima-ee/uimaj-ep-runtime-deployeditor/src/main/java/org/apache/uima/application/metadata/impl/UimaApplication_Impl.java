/**
 * 
 * Project UIMA Tooling
 * 
 * 
 * creation date: Jan 17, 2007, 8:31:39 AM
 * source:  UimaApplication_Impl.java
 */

/**
 *  Modified Logs:
 *  
 *  07-16-2007
 *      Remove the code that does duplicate operations when adding Collection Reader
 *      and CasProcessors.
 *      Note: Look at "addCpeDescription" by searching for testIt flag
 *
 */
package org.apache.uima.application.metadata.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.aae.deployment.AsyncPrimitiveErrorConfiguration;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.application.metadata.IRuntimeContext;
import org.apache.uima.application.metadata.IStringSubstitution;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.application.metadata.UimaCasProcessor;
import org.apache.uima.application.util.UimaXmlParsingUtil;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CasInitializerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.impl.metadata.cpe.CpeCasProcessorsImpl;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptionImpl;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CasProcessorErrorHandling;
import org.apache.uima.collection.metadata.CasProcessorErrorRateThreshold;
import org.apache.uima.collection.metadata.CasProcessorMaxRestarts;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCheckpoint;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeCollectionReaderCasInitializer;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.cpe.model.ConfigParameterModel;
import org.apache.uima.cpe.model.ConfigParametersModel;
import org.apache.uima.cpe.model.CpeCasProcessorsSettings;
import org.apache.uima.cpe.model.DefaultCasProcessorSettings;
import org.apache.uima.cpe.model.UimaCasInitializer;
import org.apache.uima.cpe.model.UimaCollectionReader;
import org.apache.uima.resource.CustomResourceSpecifier;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.resource.metadata.impl.MetaDataObject_impl;
import org.apache.uima.resource.metadata.impl.PropertyXmlInfo;
import org.apache.uima.resource.metadata.impl.XmlizationInfo;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.apache.uima.util.XMLSerializer;
import org.apache.uima.util.XMLizable;
import org.eclipse.core.variables.VariablesPlugin;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


public class UimaApplication_Impl extends MetaDataObject_impl implements UimaApplication 
{
    
    private static final long           serialVersionUID = -8677679822323086638L;
    private static XMLParser          uimaXMLParser;
    private static UimaApplication    instance = null;
    
    protected IRuntimeContext           runtimeContext = new RuntimeContext ();
    
    // CPE Configuration Settings
    private CpeConfiguration          cpeConfiguration = null;

    // Default Settings for EACH Cas Processor
    private DefaultCasProcessorSettings   defaultCasProcessorSettings = null;
    
    // Default values of CasProcessorErrorHandling as defined by CPE
    // if no explicite settings in CPE Xml descriptor
    // This variable is used to compare with the value returned by
    // CasProcessor.getErrorHandling() which is NOT GOOD enough to identify if
    // ErrorHandling IS SET or NOT in CPE Xml descriptor
    private CasProcessorErrorHandling   cpeDefaultErrorHandling = null;
    
    // Settings for CpeCasProcessors
    private CpeCasProcessorsSettings    cpeCasProcessorsSettings = new CpeCasProcessorsSettings();
    
    // List of UimaCollectionReader
    private List                        uimaCollectionReaders = new ArrayList();
    private List                        uimaCasInitializers = new ArrayList();
    // List of UimaCasProcessors
    private List                        uimaCasProcessors = new ArrayList();
    
    
//    private CpeDescriptorModel          mCpeDescriptorModel = null;
//    private UimaCasProcessor[]          mUimaCasProcessors;
    private String                      cpeHref = null;
    
    static boolean                      isInitialized_UimaApplicationFramework = false;
    
    /*************************************************************************/
        
    static private IStringSubstitution       stringSubstitutionManager = null;
    
    static public IStringSubstitution getStringSubstitutionManager () {
        return stringSubstitutionManager;
    }    
    
    static public void setStringSubstitutionManager (IStringSubstitution manager) {
        stringSubstitutionManager = manager;
    }
    
    static public String resolveUimaXmlDescriptor (String xmlDescriptor)
    {        
        if (stringSubstitutionManager != null) {
            // May return null
            return stringSubstitutionManager.resolveUimaXmlDescriptor(xmlDescriptor);
        } else {
            // Trace.trace("NULL");
            return xmlDescriptor;
        }
    }
    
    static public String generateVariableExpression (String variableName, String arg)
    {
        return VariablesPlugin.getDefault().getStringVariableManager()
                    .generateVariableExpression(variableName, arg); 
    }
    
    
    protected String toWorkspaceRelativePath (String xml)
    {
        String resolvedFileName = resolveUimaXmlDescriptor(xml);
        File f = new File(resolvedFileName);
        if ( !f.isAbsolute() ) {
            // Relative path
            if ( ! f.exists() ) {
                // Can NOT find file.
                // Try to find it within CURRENT DIRECTORY
                f = new File (currentDir, resolvedFileName);
                if ( ! f.exists() ) {
                    Trace.err("*** ERROR: Cannot find file: " + resolvedFileName);
                } else {
                    resolvedFileName = f.getAbsolutePath();
                }
            }
            // Trace.trace("+++ New xml:" + resolvedFileName);
        }
        resolvedFileName = generateVariableExpression("workspace_loc", resolvedFileName); //$NON-NLS-1$
        // File.
        return resolvedFileName;
    }
    
    
    /*************************************************************************/
    
    /**
     * Initialize UIMA XMLParser with new extension tags
     * 
     */
    static public void initUimaApplicationFramework ()
    {
        if (isInitialized_UimaApplicationFramework) return;
        
        isInitialized_UimaApplicationFramework = true;
        uimaXMLParser = org.apache.uima.UIMAFramework.getXMLParser();        
        try {
            uimaXMLParser.addMapping("uimaApplication", 
                    "org.apache.uima.application.metadata.impl.UimaApplication_Impl");
            uimaXMLParser.addMapping(TAG_COLLECTION_READER, 
                    "org.apache.uima.cpe.model.UimaCollectionReader");
            uimaXMLParser.addMapping(TAG_CAS_PROCESSOR, 
            "org.apache.uima.application.metadata.impl.UimaCasProcessor_impl");
            uimaXMLParser.addMapping(TAG_DEPLOYMENT_OVERRIDES, 
                    "org.apache.uima.application.metadata.impl.DeploymentOverrides_impl");
            uimaXMLParser.addMapping(TAG_CONFIG_PARAM_OVERRIDES, 
                    "org.apache.uima.application.metadata.impl.ConfigParamOverrides_impl");
            uimaXMLParser.addMapping(TAG_CONFIG_PARAM_OVERRIDE_SET, 
                    "org.apache.uima.application.metadata.impl.OverrideSet_impl");
            
            //
            // For UIMA-ee Parsing
            //
            uimaXMLParser.addMapping("analysisEngineDeploymentDescription", 
                    "org.apache.uima.aae.deployment.impl.AEDeploymentDescription_Impl");
            uimaXMLParser.addMapping("service", 
                    "org.apache.uima.aae.deployment.impl.AEService_Impl");
            uimaXMLParser.addMapping("analysisEngine", 
                "org.apache.uima.aae.deployment.impl.AEDeploymentMetaData_Impl");
            uimaXMLParser.addMapping("remoteAnalysisEngine", 
                "org.apache.uima.aae.deployment.impl.RemoteAEDeploymentMetaData_Impl");
            uimaXMLParser.addMapping("asyncAggregateErrorConfiguration", 
                "org.apache.uima.aae.deployment.impl.AsyncAggregateErrorConfiguration_Impl");
            uimaXMLParser.addMapping("asyncPrimitiveErrorConfiguration", 
                "org.apache.uima.aae.deployment.impl.AsyncPrimitiveErrorConfiguration_Impl");

            // parse("c:/uima/Test/testApp.xml");
            
            //
            // For object creation
            //
            ResourceSpecifierFactory factory = UIMAFramework.getResourceSpecifierFactory();
            factory.addMapping("org.apache.uima.aae.deployment.AEDeploymentDescription", 
                    "org.apache.uima.aae.deployment.impl.AEDeploymentDescription_Impl");
            factory.addMapping("org.apache.uima.aae.deployment.AEDeploymentMetaData", 
                    "org.apache.uima.aae.deployment.impl.AEDeploymentMetaData_Impl");
            factory.addMapping("org.apache.uima.aae.deployment.RemoteAEDeploymentMetaData", 
            "org.apache.uima.aae.deployment.impl.RemoteAEDeploymentMetaData_Impl");
            factory.addMapping("org.apache.uima.aae.deployment.AsyncAggregateErrorConfiguration", 
                    "org.apache.uima.aae.deployment.impl.AsyncAggregateErrorConfiguration_Impl");
            factory.addMapping("org.apache.uima.aae.deployment.AsyncPrimitiveErrorConfiguration", 
                    "org.apache.uima.aae.deployment.impl.AsyncPrimitiveErrorConfiguration_Impl");
            
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    } // initUimaApplicationFramework
    
    /*************************************************************************/
    
    // Note: cannot be "protected" since it is used by XMLParser
    public UimaApplication_Impl ()
    {
        super();
        instance = this;
        // runtimeContext = new RuntimeContext ();
        defaultCasProcessorSettings = new DefaultCasProcessorSettings();
        cpeDefaultErrorHandling = getCpeDefaultErrorHandling ();
        cpeCasProcessorsSettings = new CpeCasProcessorsSettings();
        // printErrorHandling(cpeDefaultErrorHandling);

    }
    /*************************************************************************/
    // Duplicate in UimaXmlParsingUtil
    public static XMLizable parseUimaXmlDescriptor (String xmlDescriptorFileName)
    {
        XMLizable descriptionObject = null;
        
        File descFile = new File(xmlDescriptorFileName);;
        boolean validArgs = descFile.exists() && !descFile.isDirectory();
        if (!validArgs) {
            Trace.err("Cannot find: " +xmlDescriptorFileName);
            return null;
        }
        
        try {
            // Redirect UIMA log messages to file
            UIMAFramework.getLogger().setOutputStream(
                    new PrintStream(new FileOutputStream("uima.log")));

            // turn off xi:include and environment variable expansion
//            XMLParser.ParsingOptions parsingOptions = new XMLParser.ParsingOptions(
//                    false, false);
            
            // Get Resource Specifier from XML file
            XMLInputSource in;
            in = new XMLInputSource(descFile);
            // descriptionObject = UIMAFramework.getXMLParser().parse(in, parsingOptions);
            descriptionObject = UIMAFramework.getXMLParser().parse(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InvalidXMLException e) {
            Trace.err("xmlDescriptorFileName: " + xmlDescriptorFileName);
            e.printStackTrace();
            return null;
        } catch (UIMA_IllegalStateException e) {
            Trace.err("xmlDescriptorFileName: " + xmlDescriptorFileName);
            // e.printStackTrace();
            return null;
        }
        
        return descriptionObject;
    } // parseUimaXmlDescriptor
    
    static public TypeSystemDescription getTypeSystemDescription (ResourceSpecifier specifier)
    {
        TypeSystemDescription tsDescription = null;
        
        try {
            if (specifier instanceof TaeDescription) {
                TaeDescription tae = (TaeDescription) specifier;
                if ( tae.isPrimitive() ) {
                    tsDescription = tae.getAnalysisEngineMetaData().getTypeSystem();
                } else {
                    tsDescription = CasCreationUtils.mergeDelegateAnalysisEngineTypeSystems(tae);
                }                
            } else if (specifier instanceof CasConsumerDescription) {
                CasConsumerDescription ccDescr = (CasConsumerDescription) specifier;
                tsDescription = ccDescr.getCasConsumerMetaData().getTypeSystem();
            } else if (specifier instanceof CollectionReaderDescription) {
                CollectionReaderDescription crDescr = (CollectionReaderDescription) specifier;
                tsDescription = crDescr.getCollectionReaderMetaData().getTypeSystem();
            }
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }

        return tsDescription;
    } // getTypeSystemDescription

    /*************************************************************************/
    
    public TypeSystemDescription getTypeSystemDescription ()
    {
        TypeSystemDescription   mergedTypeSystemDescription = null;
        List                    typeSystemList = new ArrayList();
        String                  resolvedXmlFile = null;
//        try {
//            
            // Add Collection Reader
            String aCollectionReaderPath = "";
            List list = getUimaCollectionReaders();
            // CpeCollectionReader cr = null;
            if (list != null && list.size() > 0) {
                aCollectionReaderPath = ((UimaCollectionReader)list.get(0)).getXmlDescriptor();

                resolvedXmlFile = resolveUimaXmlDescriptor(aCollectionReaderPath);

                CollectionReaderDescription crDescr = (CollectionReaderDescription) parseUimaXmlDescriptor (resolvedXmlFile);

                typeSystemList.add(crDescr.getCollectionReaderMetaData().getTypeSystem());
            }            
            
            // Add Cas Processors (AE, Cas Consumer)
            list = getUimaCasProcessors();
            for (int i=0; i<list.size(); ++i) {
                UimaCasProcessor u = (UimaCasProcessor) list.get(i);
                resolvedXmlFile = resolveUimaXmlDescriptor(u.getXmlDescriptor());
                ResourceSpecifier specifier = null;
                try {
                    specifier = UIMAFramework.getXMLParser()
                            .parseResourceSpecifier(new XMLInputSource(resolvedXmlFile));
                } catch (InvalidXMLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (specifier == null) {
                    continue;
                }
                typeSystemList.add(getTypeSystemDescription(specifier));
            }        
        
            // Create Merged Type System
            try {
                mergedTypeSystemDescription = CasCreationUtils.mergeTypeSystems(typeSystemList);
            } catch (ResourceInitializationException e) {
                e.printStackTrace();
                return null;
            }
            // Set Name and Description
            mergedTypeSystemDescription.setName("Merged Type System for ???");
            String descr = "Merge TSs of: ";
            for (int i=0; i<typeSystemList.size(); ++i) {
                if (i > 0) {
                    descr += ", ";
                }
                descr += ((TypeSystemDescription) typeSystemList.get(i)).getName();
            }
            mergedTypeSystemDescription.setDescription(descr);
        
            
        return mergedTypeSystemDescription;
    }
    
    public static String toXML (TypeSystemDescription typeSystemDescription)
    {
        String          tsDescString = null;
        StringWriter    sw = new StringWriter();
        try {
            typeSystemDescription.toXML(sw);
            tsDescString = sw.toString();
            sw.close();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tsDescString;
    }
    
    /*************************************************************************/
    
    public void printErrorHandling (CasProcessorErrorHandling errorHandling)
    {
        Trace.trace("ErrorRateThreshold().getAction: " + errorHandling.getErrorRateThreshold().getAction());
        Trace.trace("ErrorRateThreshold().getMaxErrorCount: " + errorHandling.getErrorRateThreshold().getMaxErrorCount());
        Trace.trace("ErrorRateThreshold().getMaxErrorSampleSize: " + errorHandling.getErrorRateThreshold().getMaxErrorSampleSize());
        
    }
    
    static public UimaApplication getInstance () {
        return instance;
    }
    
    public void removeAll ()
    {
        cpeHref          = null;
        cpeConfiguration = null;
        defaultCasProcessorSettings.removeAll();
        uimaCollectionReaders.clear();
        uimaCasProcessors.clear();        
    }
    
    // get default values of CpeCheckpoint as defined by CPE
    // if no explicite settings in CPE Xml descriptor
    static public CpeCheckpoint getCpeDefaultCheckpoint ()
    {
        // CPE BUG!!! Need to call CpeDescriptorFactory.produceDescriptor();
//        CpeDescription cpeDesc = CpeDescriptorFactory.produceDescriptor();
        CpeCheckpoint c = CpeDescriptorFactory.produceCpeCheckpoint();
        // CPE BUG!!! Need to set some value before calling getFrequency()
        c.setFrequency(600000, true);
        return c;
    }
    
    // get default values of CasProcessorErrorHandling as defined by CPE
    // if no explicite settings in CPE Xml descriptor
    static public CasProcessorErrorHandling getCpeDefaultErrorHandling ()
    {
        // CPE BUG!!! Need to call CpeDescriptorFactory.produceDescriptor();
//        CpeDescription cpeDesc = CpeDescriptorFactory.produceDescriptor();
        CpeCasProcessor casProc = CpeDescriptorFactory.produceCasProcessor("dummy name");
        return casProc.getErrorHandling();
    }
    
    static public void setCasProcessorErrorHandling(CpeCasProcessor casProc,
                            CasProcessorErrorHandling errorHandling)
    {
        if (errorHandling == null) {
            return;
        }
        casProc.setActionOnMaxError(errorHandling.getErrorRateThreshold().getAction());
        casProc.setMaxErrorCount(errorHandling.getErrorRateThreshold().getMaxErrorCount());
        casProc.setMaxErrorSampleSize(errorHandling.getErrorRateThreshold().getMaxErrorSampleSize());
        
        casProc.setMaxRestartCount(errorHandling.getMaxConsecutiveRestarts().getRestartCount());
        casProc.setActionOnMaxRestart(errorHandling.getMaxConsecutiveRestarts().getAction());
        casProc.setTimeout(errorHandling.getTimeout().get());
    }
    
//    static public UimaApplication createUimaApplication ()
//    {
//        initUimaApplicationFramework ();
//        instance = new UimaApplication_Impl();
//        
//        return instance;
//    }    
    
    static public UimaApplication createUimaApplication (ResourceManager rm)
    {
        initUimaApplicationFramework ();
        instance = new UimaApplication_Impl();
        instance.setResourceManager(rm);
        return instance;
    }    
    
    /**
     *  Create UimaApplication from Uima Application XML Descriptor
     *  
     */
    static public UimaApplication createUimaApplication (String appXmlDescriptorFile)
    {
        initUimaApplicationFramework ();
        UimaApplication_Impl u = null;
        try {
            u = (UimaApplication_Impl) uimaXMLParser.parse(new XMLInputSource(appXmlDescriptorFile));
            String cpeXml = u.getCpeDescriptor();
            if (cpeXml != null && cpeXml.trim().length()>0) {
                u.cpeHref = cpeXml;
                // Trace.trace("cpeXml: " + cpeXml);
//                u.mCpeDescriptorModel = CpeDescriptorModel.createInstance (cpeXml);
//                u.mCpeDescriptorModel.setUimaApplication (u);
                u.initUimaCasProcessors(); // Right now, DO NOTHING
            }
        
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        instance = u;
        return (UimaApplication) u;
    }
    
    /**
     *  Create UimaApplication from UimaApplication Object
     *  
     */
    static public void populateCpeDescriptor (UimaApplication uimaApp)
                    throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException
    {
        UimaApplication_Impl u = (UimaApplication_Impl) uimaApp;
        
        String cpeXml = resolveUimaXmlDescriptor(uimaApp.getCpeDescriptor());
        if (cpeXml != null && cpeXml.trim().length()>0) {
            u.cpeHref = cpeXml;
            CpeDescription cpeDescription = null;
            try {
                cpeDescription = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(new File(cpeXml)));
            } catch (InvalidXMLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            u.addXMLizable(cpeXml, cpeDescription);
            // Trace.trace("cpeXml: " + cpeXml);
//            u.mCpeDescriptorModel = CpeDescriptorModel.createInstance (cpeXml);
//            u.mCpeDescriptorModel.setUimaApplication (uimaApp);
            // u.initUimaCasProcessors();
        }    
        instance = u;
    }

    static public UimaApplication createUimaApplication (String cpeXml, CpeDescription cpeDescription)
    {
        UimaApplication_Impl uimaApp = new UimaApplication_Impl();
        
        uimaApp.cpeHref = cpeXml;
        // Trace.trace("cpeXml: " + cpeXml);
        // uimaApp.createUimaCasProcessors();
        instance = uimaApp;
        return (UimaApplication) uimaApp;
    }
    
    /**
     *  Create UimaApplication from CpeDescription
     * 
     * @param cpeDescription
     * @return UimaApplication
     * @throws IOException 
     */
//    static public UimaApplication createUimaApplication (CpeDescription cpeDescription)
//    {
//        UimaApplication_Impl uimaApp = new UimaApplication_Impl();
//        
//        // uimaApp.cpeHref = cpeXml;
//        // Trace.trace("cpeXml: " + cpeXml);
////        uimaApp.mCpeDescriptorModel = CpeDescriptorModel.createInstance (cpeXml, cpeDescription);
////        uimaApp.mCpeDescriptorModel.setUimaApplication (uimaApp);
//        // uimaApp.createUimaCasProcessors();
//        instance = uimaApp;
//        return (UimaApplication) uimaApp;
//    }
    
    static public UimaApplication createUimaApplication (UimaApplication uimaApp, String xmlFile,
                                            ResourceManager rm) 
            throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException, IOException
    {
        String resolvedXmlFile = resolveUimaXmlDescriptor(xmlFile);
//        try {
//            resolvedXmlFile = VariablesPlugin.getDefault().getStringVariableManager()
//                                    .performStringSubstitution(xmlFile);
//        } catch (CoreException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

        // XMLizable xmlLizable = UIMATypeSystemUtils.parseUimaXmlDescriptor (xmlFile);
        Object xmlLizable = null;
        InvalidXMLException invalidXMLException = null;
        try {           
            xmlLizable = UimaXmlParsingUtil.parseUimaXmlDescriptor (resolvedXmlFile);
        } catch (InvalidXMLException e) {
            invalidXMLException = e;
            e.printStackTrace();            
        }
        if (xmlLizable == null) {
            // Check for CPE descriptor
            // Trace.trace("XMLizable == NULL for: " + resolvedXmlFile);
            try {
                xmlLizable = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(new File(resolvedXmlFile)));
            } catch (InvalidXMLException e) {
                invalidXMLException = e;
                e.printStackTrace();
            }
            if (xmlLizable == null) {
                if (invalidXMLException != null) {
                    throw invalidXMLException;
                }
                return null;
            }
        }
        if (xmlLizable instanceof UimaApplication) {  
            uimaApp.setResourceManager(rm);
            UimaApplication_Impl.populateCpeDescriptor((UimaApplication)xmlLizable);
            
        } else if (xmlLizable instanceof CpeDescription) {
            // Trace.trace("*** XMLizable: CpeDescription ***");
            if (uimaApp == null) {
                uimaApp = UimaApplication_Impl.createUimaApplication (rm);
            }
            uimaApp.addXMLizable(xmlFile, (CpeDescription)xmlLizable);
            
        } else if (xmlLizable instanceof XMLizable) {
            // Trace.trace("*** XMLizable: CpeDescription ***");
            // Trace.trace("XMLizable: " + xmlLizable.getClass().getName());
            if (uimaApp == null) {
                uimaApp = UimaApplication_Impl.createUimaApplication (rm);
            }
            uimaApp.addXMLizable(xmlFile, (XMLizable) xmlLizable);
//        } else {
//            Trace.trace("XMLizable: " + xmlLizable.getClass().getName());
        }
        // Trace.trace("XMLizable: " + xmlLizable.getClass().getName());
        
        return uimaApp;
    }
    
    static public AbstractUimaCasProcessor createUimaComponent (String xmlDescriptor)             
                            throws InvalidXMLException, 
                                   ResourceInitializationException, IOException
    {
        XMLizable xmlizable = UimaXmlParsingUtil.parseUimaXmlDescriptor (xmlDescriptor);
        if (xmlizable == null) { 
            return null;
        }
        UimaCasProcessor_impl u = new UimaCasProcessor_impl();
        u.xmlizableDescriptor = xmlizable;
        u.xmlDescriptor       = xmlDescriptor; // Xml descriptor
        if (xmlizable instanceof AnalysisEngineDescription) {
            AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData();            
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_AE);
            u.setInstanceName(a.getName());
            u.configParamDecls = a.getConfigurationParameterDeclarations();
            u.configParamSettings = a.getConfigurationParameterSettings();
//            u.typePriorities = getTypePriorities(xmlizable, u.getResourceManager());

        } else if (xmlizable instanceof CasConsumerDescription) {
            ProcessingResourceMetaData p = ((CasConsumerDescription) xmlizable).getCasConsumerMetaData();
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_CAS_CONSUMER);
            u.setInstanceName(p.getName());
            u.configParamDecls    = p.getConfigurationParameterDeclarations();
            u.configParamSettings = p.getConfigurationParameterSettings(); 

        } else if (xmlizable instanceof URISpecifier) {
            URISpecifier uriSpecifier = (URISpecifier) xmlizable;
//            Trace.err("URISpecifier protocol=" + uriSpecifier.getProtocol()
//                    + " resource type=" + uriSpecifier.getResourceType()
//                    + " uri=" + uriSpecifier.getUri());
            u.setCasProcessorCategory(UimaCasProcessor.CASPROCESSOR_CAT_SERVICE);
            // Create a name from URI
            String name = "Unknow";
              try {
                  name = new URI(uriSpecifier.getUri()).getPath();
              } catch (URISyntaxException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
            u.setInstanceName(name);            
//        } else {
//            Trace.err("UNKNOWN Descriptor: " + xmlizable.getClass().getName());
        }

        u.configParamsModel = new ConfigParametersModel(u.configParamDecls, u.configParamSettings, null);
        return u;
    }

    
    /*************************************************************************/    
    
    static public TypePriorities getAnalysisEngineTypePriorities (XMLizable xmlizable, ResourceManager rm) 
                    throws InvalidXMLException, ResourceInitializationException
    {
        TypePriorities tp = null;
        if (xmlizable instanceof AnalysisEngineDescription) {
            AnalysisEngineDescription ae = (AnalysisEngineDescription) xmlizable;
            if ( ae.isPrimitive() ) {
                AnalysisEngineMetaData a = ((AnalysisEngineDescription) xmlizable).getAnalysisEngineMetaData(); 
                tp = ae.getAnalysisEngineMetaData().getTypePriorities();
            } else {
                // Aggregate AE
                if (rm != null) {
                    tp = CasCreationUtils.mergeDelegateAnalysisEngineTypePriorities(ae, rm);
                } else {
                    tp = CasCreationUtils.mergeDelegateAnalysisEngineTypePriorities(ae);
                }
            }
            if (tp != null) {
                if (rm != null) {
                    tp.resolveImports(rm);
                } else {
                    tp.resolveImports();
                }
                // Trace.err(ae.getAnalysisEngineMetaData().getName() + ": HAS Type priorities");
                try {
                    tp.toXML(System.out);
                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//            } else {
//                Trace.err(ae.getAnalysisEngineMetaData().getName() + ": NO Type priorities");
            }
        }
        return tp;
    }
    
    public TypePriorities getMergedTypePriorities () 
            throws InvalidXMLException, ResourceInitializationException
    {
        TypePriorities mergedTp = null;
        TypePriorities tp;
        List typePrioritiesList = new ArrayList();
        
        // Add Collection Reader
        String aCollectionReaderPath = "";
        List list = getUimaCollectionReaders();
        CpeCollectionReader cr = null;
        if (list != null && list.size() > 0) {
            CollectionReaderDescription crDescr 
                = (CollectionReaderDescription) ((UimaCollectionReader) list.get(0)).getXmlizable();
            if (crDescr != null) {
                tp = crDescr.getCollectionReaderMetaData().getTypePriorities();
                if (tp != null) {
                    typePrioritiesList.add(tp);
                }
//            } else {
//                Trace.bug("CR doesn't have XMLizable");
            }
        }            
        
        // Add Cas Processors (AE, Cas Consumer)
        list = getUimaCasProcessors();
        for (int i=0; i<list.size(); ++i) {
            UimaCasProcessor u = (UimaCasProcessor) list.get(i);
            XMLizable specifier = ((UimaCasProcessor_impl) u).getXmlizable();
            if (specifier == null) {
                // Trace.bug("UimaCasProcessor " + u.getInstanceName() + " doesn't have XMLizable");
                continue;
            }

            tp = getAnalysisEngineTypePriorities(specifier, getResourceManager());
            if (tp != null) {
                typePrioritiesList.add(tp);
            }
        }        
        
        // Merge typePrioritiesList
        if (typePrioritiesList.size() > 0) {
            mergedTp = CasCreationUtils.mergeTypePriorities(typePrioritiesList, getResourceManager());
        }
        
        return mergedTp;
    } // getMergedTypePriorities
    
    /*************************************************************************/        
    
    final static private int CPE_DEFAULT_BATCHSIZE      = 10000;
    
    // Current directory used to find "include" in CPE
    private String          currentDir = null;
    private transient ResourceManager resourceManager;
    
    public void setCurrentDirectory (String currDir) {
        currentDir = currDir;
    }
    
    public ResourceManager getResourceManager () {
        return resourceManager;
    }

    public void setResourceManager (ResourceManager rm)
    {
        this.resourceManager = rm;
    }
    
    public String generateUimaApplicationXmlDescriptor (String xmlDescriptorFileName, boolean resolve)
    {
        StringWriter w = new StringWriter();
        FileOutputStream out = null;
        try {
            // Save to file
            if (xmlDescriptorFileName != null && xmlDescriptorFileName.trim().length() > 0) {
                out = new FileOutputStream (xmlDescriptorFileName);
                this.toXML(out);
                // Trace.trace("*** write UIMA App Xml to file: " + xmlDescriptorFileName);
            }
            // Return String
            this.toXML(w);
            // Trace.err(prettyPrintModel(this));
            //System.out.println(w.toString());
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }        
            }
        }
        return w.toString();
    }
    
    public String generateCpeXmlDescriptor (String outputXmlDescriptorFileName, boolean resolve) 
                                throws CpeDescriptorException, SAXException, IOException
    {
        StringWriter w = new StringWriter();
        // Create a empty CpeDescription
        CpeDescription cpeDesc = CpeDescriptorFactory.produceDescriptor();

        // Add Collection Reader
        List list = getUimaCollectionReaders();
        if (list != null && list.size() > 0) {
            String aCollectionReaderPath = "";
            CpeCollectionReader cr = null;
            aCollectionReaderPath = ((UimaCollectionReader)list.get(0)).getXmlDescriptor();
            if (resolve) {
                cr = cpeDesc.addCollectionReader(resolveUimaXmlDescriptor(aCollectionReaderPath));
            } else {
                cr = cpeDesc.addCollectionReader(aCollectionReaderPath);
            }

            // Add Configuration Parameter Overrides for CPE
            UimaCollectionReader ucr = (UimaCollectionReader)list.get(0);
            if (ucr.getConfigParamsModel() != null) {
                List cpeOverrideList = ucr.getConfigParamsModel().getCpeParamModelList();
                if (cpeOverrideList != null && cpeOverrideList.size() > 0) {
                    CasProcessorConfigurationParameterSettings settings =
                        CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
                    cr.setConfigurationParameterSettings(settings);
                    for (int k=0; k<cpeOverrideList.size(); ++k) {
                        ConfigParameterModel m = (ConfigParameterModel) cpeOverrideList.get(k);
                        if (m.getCpeValue() != null) {
                            settings.setParameterValue (m.getName(), m.getCpeValue());
                        }
                    }
                }
            }
        }

        // Add CAS Initializer
        list = getUimaCasInitializers();
        if (list != null && list.size() > 0) {
            String aCasInitializerPath = "";
            CpeCollectionReaderCasInitializer ci = null;
            aCasInitializerPath = ((UimaCasInitializer)list.get(0)).getXmlDescriptor();
            if (resolve) {
                ci = cpeDesc.addCasInitializer(resolveUimaXmlDescriptor(aCasInitializerPath));
            } else {
                ci = cpeDesc.addCasInitializer(aCasInitializerPath);
            }
            // Trace.err("Add Cas Init: " + aCasInitializerPath);
            // Add Configuration Parameter Overrides for CPE
            UimaCasInitializer uci = (UimaCasInitializer)list.get(0);
            if (uci.getConfigParamsModel() != null) {
                List cpeOverrideList = uci.getConfigParamsModel().getCpeParamModelList();
                if (cpeOverrideList != null && cpeOverrideList.size() > 0) {
                    CasProcessorConfigurationParameterSettings settings =
                        CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
                    ci.setConfigurationParameterSettings(settings);
                    for (int k=0; k<cpeOverrideList.size(); ++k) {
                        ConfigParameterModel m = (ConfigParameterModel) cpeOverrideList.get(k);
                        if (m.getCpeValue() != null) {
                            settings.setParameterValue (m.getName(), m.getCpeValue());
                        }
                    }
                }
            }
        }

        // Add Cas Processors (AE, Cas Consumer)
        list = getUimaCasProcessors();
        for (int i=0; i<list.size(); ++i) {
            UimaCasProcessor u = (UimaCasProcessor) list.get(i);
            CpeCasProcessor casProc;
//            if (u.getCasProcessorCategory() == UimaCasProcessor.CASPROCESSOR_CAT_SERVICE) {
//                casProc = CpeDescriptorFactory.produceRemoteCasProcessor(u.getInstanceName());
//            } else {
//                casProc = CpeDescriptorFactory.produceCasProcessor(u.getInstanceName());
//            }
            casProc = CpeDescriptorFactory.produceCasProcessor(u.getInstanceName());

            if (resolve) {
                casProc.setDescriptor(resolveUimaXmlDescriptor(u.getXmlDescriptor()));
            } else {
                casProc.setDescriptor(u.getXmlDescriptor());
            }

            // Add Error Handlings
            if (u.getCasProcessorErrorHandling() != null) {
                setCasProcessorErrorHandling(casProc, u.getCasProcessorErrorHandling());
            } else {
                // Set values from default, if any
//              setCasProcessorErrorHandling(casProc, getDefaultCasProcessorSettings().getErrorHandling());                    
            }

            // Set <checkpoint batch="10000"/>
            if (u.getBatchSize() != -1) {
                casProc.setBatchSize(u.getBatchSize());
            } else {
                // Check if default value is defined
                if (getDefaultCasProcessorSettings().getCasProcBatchSize() != -1) {
                    casProc.setBatchSize(getDefaultCasProcessorSettings().getCasProcBatchSize());
                }
            }

            // Add Configuration Parameter Overrides for CPE
            List cpeOverrideList = u.getConfigParamsModel().getCpeParamModelList();
            if (cpeOverrideList != null && cpeOverrideList.size() > 0) {
                CasProcessorConfigurationParameterSettings settings =
                    CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
                casProc.setConfigurationParameterSettings(settings);
                for (int k=0; k<cpeOverrideList.size(); ++k) {
                    ConfigParameterModel m = (ConfigParameterModel) cpeOverrideList.get(k);
                    if (m.getCpeValue() != null) {
                        settings.setParameterValue (m.getName(), m.getCpeValue());
                    }
                }
            }

//          // Add Error Handlings
//CasProcessorErrorHandling errorHandling = u.getCasProcessorErrorHandling();
//          if (errorHandling != null) {

//          casProc.setTimeout(errorHandling.getTimeout().get());
//          }

            // Add to CPE
            cpeDesc.addCasProcessor(casProc);                
        }

        // Set CpeConfiguration from "existing" CpeConfiguration
        CpeConfiguration cpeConfiguration = getCpeConfiguration();
        if (cpeConfiguration == null) {
            // Trace.bug("getCpeConfiguration() == null (NEVER SET ?)");
        } else {
            cpeDesc.getCpeConfiguration().setNumToProcess(cpeConfiguration.getNumToProcess());
            cpeDesc.getCpeConfiguration().setDeployment(cpeConfiguration.getDeployment());
            cpeDesc.getCpeConfiguration().getCheckpoint()
            .setFrequency(cpeConfiguration.getCheckpoint().getFrequency(),
                    cpeConfiguration.getCheckpoint().isTimeBased());
        }

        // Set CpeCasProcessors Settings
        // Trace.trace("************* Set CpeCasProcessors Settings ************");
        if (cpeDesc.getCpeCasProcessors() != null) {
            cpeDesc.getCpeCasProcessors().setPoolSize(cpeCasProcessorsSettings.getCasPoolSize());
        } else {
            // Trace.err("cpeDesc.getCpeCasProcessors() == null");
        }
        cpeDesc.setProcessingUnitThreadCount(cpeCasProcessorsSettings.getProcessingUnitThreadCount());
        ((CpeCasProcessorsImpl) cpeDesc.getCpeCasProcessors()).setDropCasOnException(cpeCasProcessorsSettings.isDropCasOnException());

        FileOutputStream out = null;
        try {
            // Save to file
            if (outputXmlDescriptorFileName != null && outputXmlDescriptorFileName.trim().length() > 0) {
                out = new FileOutputStream (outputXmlDescriptorFileName);
                cpeDesc.toXML(out);
                // Trace.trace("*** write to file: " + outputXmlDescriptorFileName);
            }
            // Return String
            ((CpeDescriptionImpl) cpeDesc).toXML(w);
            // Trace.err(prettyPrintModel(this));
            //System.out.println(w.toString());
        } catch (SAXException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }        
            }
        }


        return w.toString();
    } // generateCpeXmlDescriptor

    public int generateTypeSystemXmlDescriptor (String xmlFileName)
    {
        TypeSystemDescription tsd = getTypeSystemDescription ();
        
        FileOutputStream out = null;
        try {
            // Save to file
            if (xmlFileName != null && xmlFileName.trim().length() > 0) {
                out = new FileOutputStream (xmlFileName);
                tsd.toXML(out);
                // Trace.trace("*** write to file: " + xmlFileName);
            }
        } catch (SAXException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -3;
                }        
            }
        }
        return 0;
    }    
    
    /*************************************************************************/
    
    /**
     * Return true if the ResourceSpecifier is CasConsumerDescription
     * 
     * @param specifier
     * @return
     * @return boolean
     */
    // Copy from CpeDescriptorModel
    private boolean isCasConsumerSpecifier(ResourceSpecifier specifier)
    {
        if (specifier instanceof CasConsumerDescription) {
            return true;
        } else if (specifier instanceof URISpecifier) {
            URISpecifier uriSpec = (URISpecifier)specifier;
            return URISpecifier.RESOURCE_TYPE_CAS_CONSUMER.equals(uriSpec.getResourceType());
        }
        return false;
    }
    
    /*************************************************************************/
    /*                  compare CasProcessorErrorHandling                    */
    /*************************************************************************/
    
    public boolean compareWithDefaultCasProcessorErrorRateThreshold 
                    (CasProcessorErrorRateThreshold threshold1, CasProcessorErrorRateThreshold threshold2) 
    {
        if (threshold1.getMaxErrorCount() != threshold2.getMaxErrorCount()) {
            return false;
        }
        if (threshold1.getMaxErrorSampleSize() != threshold2.getMaxErrorSampleSize()) {
            return false;
        }
        if (!threshold1.getAction().equalsIgnoreCase(threshold2.getAction())) {
            return false;
        }        
        return true;
    }
    
    public boolean compareWithDefaultCasProcessorMaxRestarts 
                    (CasProcessorMaxRestarts maxRestart1, CasProcessorMaxRestarts maxRestart2) 
    {
        if (maxRestart1.getRestartCount() != maxRestart2.getRestartCount()) {
            return false;
        }
        if (!maxRestart1.getAction().equalsIgnoreCase(maxRestart2.getAction())) {
            return false;
        }        
        return true;
    }    
    
    /**
     * Compare with default settings for each Cas Processor.
     * If the default value is NOT set, use the CPE's default settings
     * 
     * @param errorHandling
     * @return
     * @return boolean
     */
    public boolean compareWithCpeDefaultErrorHandling (CasProcessorErrorHandling errorHandling)
    {
        CasProcessorErrorHandling def = null;
        if (defaultCasProcessorSettings != null) {
            def = defaultCasProcessorSettings.getErrorHandling();
        }
        if (def == null) {
            // Use CPE's default value
            def = cpeDefaultErrorHandling;
        }
        if (def != null) {
            if (!compareWithDefaultCasProcessorErrorRateThreshold(errorHandling.getErrorRateThreshold(),
                    def.getErrorRateThreshold())) {
                return false;
            }
            
            if (!compareWithDefaultCasProcessorMaxRestarts(errorHandling.getMaxConsecutiveRestarts(),
                    def.getMaxConsecutiveRestarts())) {
                return false;
            }
            
            if (errorHandling.getTimeout().get() != def.getTimeout().get()) {
                return false;
            }
        } else {
            Trace.bug("Cannot get CpeDefaultErrorHandling");
            return false;
        }
        return true;
    }
    
    /*************************************************************************/
    /*                  Add CPE Xml Descriptor (CpeDescription)              */
    /*************************************************************************/
        
//    public void addCasProcessor (String cpeXml, CpeDescription cpeDescription)
//            throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException
//    {        
//        cpeHref = cpeXml;
//        // Trace.trace("cpeXml: " + cpeXml);
//        addCasProcessorsFromCpeDescription(cpeDescription);
//    }
    
    // 07-16-2007
    // Remove the code that does duplicate operations when adding Collection Reader
    // and CasProcessors
    // Note: Look at "addCpeDescription" by searching for testIt flag
    public void addCpeDescription (String cpeXml, CpeDescription cpeDescription)
                    throws InvalidXMLException, ResourceInitializationException, 
                           CpeDescriptorException
    {
        int error = 0, details = 0; // Status of each node

        cpeHref = cpeXml;
        // Set CPE Configuration Settings if NOT set
        try {
            if (cpeConfiguration != null) {
                cpeConfiguration = cpeDescription.getCpeConfiguration();
            }
        } catch (CpeDescriptorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // Collection Reader
        CpeCollectionReader[] collRdrs = null;
        try {
            collRdrs = cpeDescription.getAllCollectionCollectionReaders();
        } catch (CpeDescriptorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean testIt = false; // FOR TESTING
        if (testIt && collRdrs != null && collRdrs.length>0) {
            for (int i=0; i<collRdrs.length; ++i) {
                // Create a NEW UimaCollectionReader for its Xml descriptor
                CpeComponentDescriptor cpeComponentDescriptor = collRdrs[i].getDescriptor();
                String xml = null;
                if (cpeComponentDescriptor.getInclude() != null) {
                    xml = cpeComponentDescriptor.getInclude().get();
                } else if (cpeComponentDescriptor.getImport() != null) {
                    xml = cpeComponentDescriptor.getImport().getLocation();
                    if (xml == null) {
                        xml = cpeComponentDescriptor.getImport().getName();
                    }
                }
                if (xml == null || xml.trim().length() == 0) {
                    // EMPTY Collection Reader
                    // Trace.err("EMPTY Collection Reader");
                    continue;
                }
                String newFileLocation = xml;
                String resolvedFileName = UimaApplication_Impl.resolveUimaXmlDescriptor(xml);
                if (resolvedFileName == null) {
                    // Cannot resolve
                    // Trace.err("cannot resolve file: " + resolvedFileName);                    
                } else {
                    File f = new File(resolvedFileName);
                    if ( !f.isAbsolute() ) {
                        // Relative path
                        if ( ! f.exists() ) {
                            // Can NOT find file.
                            // Try to find it within CURRENT DIRECTORY
                            f = new File (currentDir, resolvedFileName);
                            if ( ! f.exists() ) {
                                Trace.trace("*** ERROR: Cannot find file: " + resolvedFileName);
                                error = STATUS_DEPLOYMENT_NODE_ERROR;
                                details = STATUS_DETAILS_FILE_NOT_FOUND;
                            }
                            resolvedFileName = f.getAbsolutePath();
                            newFileLocation = resolvedFileName;
                        }
                        // Trace.trace("+++ New xml:" + resolvedFileName);
                    }
                }
                UimaCollectionReader u = UimaCollectionReader.createUimaCasProcessor (collRdrs[i], 
                        newFileLocation/*resolvedFileName*/, this);
                // Need to check duplication
//                UimaCasProcessor u = getUimaCasProcessor(collRdrs[i].getName());
//                if (u != null) {
//                    UimaCasProcessor_impl.createUimaCasProcessorFrom(u, casProcs[i], specifier, this);
//                } else {
//                    u = UimaCasProcessor_impl.createUimaCasProcessor(casProcs[i], specifier, this);
//                    uimaCasProcessors.add(u);
//                }
                
                if (error > 0) {
                    u.setStatus(error);
                    u.setStatusDetails(details);
                }
                uimaCollectionReaders.add(u);                
            } // for
            
            // ONLY 1 Cas Initializer is allowed (Also true for Collection Reader)
            CpeCollectionReaderCasInitializer casInit = null;
            try {
                casInit = collRdrs[0].getCasInitializer();
            } catch (CpeDescriptorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (casInit != null) {
                // Create a NEW UimaCasInitializer for its Xml descriptor
                String xml = casInit.getDescriptor().getInclude().get();
                if (xml == null || xml.trim().length() == 0) {
                    // EMPTY Cas Initializer
                    // Trace.err("EMPTY Cas Initializer");
                } else {
                    String newFileLocation = xml;
                    String resolvedFileName = resolveUimaXmlDescriptor(xml);
                    File f = new File(resolvedFileName);
                    error = 0;
                    details = 0;
                    if ( !f.isAbsolute() ) {
                        // Relative path
                        if ( ! f.exists() ) {
                            // Can NOT find file.
                            // Try to find it within CURRENT DIRECTORY
                            f = new File (currentDir, resolvedFileName);
                            if ( ! f.exists() ) {
                                // Trace.trace("*** ERROR: Cannot find file: " + resolvedFileName);
                                error = STATUS_DEPLOYMENT_NODE_ERROR;
                                details = STATUS_DETAILS_FILE_NOT_FOUND;
                            }
                            resolvedFileName = f.getAbsolutePath();
                            newFileLocation = resolvedFileName;
                        }
                        // Trace.trace("+++ New xml:" + resolvedFileName);
                    }
        
                    UimaCasInitializer u = UimaCasInitializer.createUimaCasProcessor (casInit, 
                            newFileLocation/*resolvedFileName*/, this);
                    if (error > 0) {
                        u.setStatus(error);
                        u.setStatusDetails(details);
                    }
                    uimaCasInitializers.add(u);     
                }
            }
        }
        
        /*****************************************************************/
        if (cpeCasProcessorsSettings == null) {
            cpeCasProcessorsSettings = new CpeCasProcessorsSettings();
        }
        try {
            cpeCasProcessorsSettings.setCasPoolSize(((CpeCasProcessorsImpl) cpeDescription.getCpeCasProcessors()).getPoolSize());
            // Cannot get processingUnitThreadCount
            cpeCasProcessorsSettings.setDropCasOnException(((CpeCasProcessorsImpl) cpeDescription.getCpeCasProcessors()).getDropCasOnException());
        } catch (CpeDescriptorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // CPE Cas Processors
        CpeCasProcessor[] casProcs = null;
        try {
            casProcs = cpeDescription.getCpeCasProcessors().getAllCpeCasProcessors();
        } catch (CpeDescriptorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }            
        if (casProcs == null) {
            // Empty Cpe Descriptor
            return ;
        }

        // Get list of CpeCasProcessor which can be an AE or a Cas Consumer
        // For each CpeCasProcessor, get its type system (insert in cpeModel.typeSystemList)
        // cpeModel.cpeCasProcessorModelList = new ArrayList();
//      List    specifierList = new ArrayList();
//      List    aeList       = new ArrayList();
//      List    consumerList = new ArrayList();
        // cpeModel.typeSystemList = new ArrayList();
        for (int i=0; i < casProcs.length; i++) {
            CpeComponentDescriptor cpeComponentDescriptor = casProcs[i].getCpeComponentDescriptor();
            String newFileLocation = null;
            if (cpeComponentDescriptor.getInclude() != null) {
                newFileLocation = cpeComponentDescriptor.getInclude().get();
            } else if (cpeComponentDescriptor.getImport() != null) {
                newFileLocation = cpeComponentDescriptor.getImport().getLocation();
                if (newFileLocation == null) {
                    newFileLocation = cpeComponentDescriptor.getImport().getName();
                }
            }
            String specifierFile = resolveUimaXmlDescriptor(newFileLocation);

            ResourceSpecifier specifier = null;
            // Check for file existence
            if ( specifierFile == null || specifierFile.trim().length() == 0 ) {
                // Trace.err("EMPTY (no xml desc) Cas Processor " + casProcs[i].getName());
                continue;
            } else {
                File f = new File(specifierFile);
                error = 0;
                details = 0;
                if ( !f.isAbsolute() ) {
                    // Relative path
                    if ( ! f.exists() ) {
                        // Can NOT find file.
                        // Try to find it within CURRENT DIRECTORY
                        f = new File (currentDir, specifierFile);
                        if ( ! f.exists() ) {
                            // Trace.err("*** ERROR: Cannot find file: " + specifierFile);
                            error = STATUS_DEPLOYMENT_NODE_ERROR;
                            details = STATUS_DETAILS_FILE_NOT_FOUND;
                            // continue;
                        }
                        specifierFile = f.getAbsolutePath();
                        newFileLocation = specifierFile;
                    }
                } else {
                    // Absolute Path
                    if ( ! f.exists() ) {
                        // Trace.err ("Cannot find xml file for " + casProcs[i].getName()
                        //           + ": " + specifierFile);
                        error = STATUS_DEPLOYMENT_NODE_ERROR;
                        details = STATUS_DETAILS_FILE_NOT_FOUND;
                        // continue;
                    }                    
                }

                if (error == 0) {
                    // No error.
                    try {
                        specifier = UIMAFramework.getXMLParser().
                                    parseResourceSpecifier(new XMLInputSource(specifierFile));
                    } catch (InvalidXMLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        continue;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        continue;
                    }
                }
            }
//            if (specifier != null) {
//                specifierList.add(specifier);
//    
//                if (isCasConsumerSpecifier(specifier)) {
//                    consumerList.add(specifier);
//                } else {
//                    aeList.add(specifier);
//                }
//            }
            // Need to check duplication
            UimaCasProcessor u = getUimaCasProcessor(casProcs[i].getName());
            if (u != null) {
                Trace.err("Duplicate with CasProcessor from CPE: " + casProcs[i].getName());
                // Should we call the following code ? 
                if (testIt) {
                    UimaCasProcessor_impl.createUimaCasProcessorFrom(u, casProcs[i], specifier, this);
                } else {
                    u.setCpeCasProcessor(casProcs[i]);
                }
            } else {
                u = UimaCasProcessor_impl.createUimaCasProcessor(casProcs[i], specifier, this);
                uimaCasProcessors.add(u);
            }
            if (error > 0) {
                u.setStatus(error);
                u.setStatusDetails(details);
            }
            // File path may be changed
            // ((AbstractUimaCasProcessor) u).setXmlDescriptor(specifierFile);
            ((AbstractUimaCasProcessor) u).setXmlDescriptor(newFileLocation);

            // Get CasProcessorErrorHandling
            if (cpeDefaultErrorHandling == null) {
                // Trace.bug("NO pre-setting for cpeDefaultErrorHandling");
                cpeDefaultErrorHandling = CpeDescriptorFactory.produceCasProcessorErrorHandling();
            }
            // printErrorHandling(cpeDefaultErrorHandling);

            if ( ! compareWithCpeDefaultErrorHandling(casProcs[i].getErrorHandling()) ) {
                // Not the same values as CPE defaults
                u.setCasProcessorErrorHandling(casProcs[i].getErrorHandling());
            }
            u.setBatchSize(casProcs[i].getBatchSize());

            // Get type system
            // cpeModel.typeSystemList.add(getTypeSystemDescription(specifier));
        } // for    
    } // addCpeDescription 
    
    /*************************************************************************/
    /*                  Add Collection Reader or Cas Processor               */
    /**
     * @throws ResourceInitializationException 
     * @throws InvalidXMLException 
     * ***********************************************************************/

    public Object addXMLizable (String xmlizableXmlFile, XMLizable xmlizable) 
                        throws InvalidXMLException, ResourceInitializationException, CpeDescriptorException
    {
        Object      component = null;

        // Create a NEW CpeCasProcessor
        // CPE BUG!!! Need to call CpeDescriptorFactory.produceDescriptor();
//        CpeDescription cpeDesc = CpeDescriptorFactory.produceDescriptor();
        
        // Trace.trace("xmlizableXmlFile:" + xmlizableXmlFile);
        if (xmlizable instanceof CollectionReaderDescription) {
            UimaCollectionReader u = UimaCollectionReader
            .createUimaCasProcessor ((CollectionReaderDescription) xmlizable, 
                    xmlizableXmlFile, this);
            uimaCollectionReaders.add(u);    
            component = u;
        } else if (xmlizable instanceof CasInitializerDescription) {
            // Trace.err("CasInitializerDescription");
            UimaCasInitializer ci = UimaCasInitializer
            .createUimaCasProcessor ((CasInitializerDescription) xmlizable, 
                    xmlizableXmlFile, this);
            uimaCasInitializers.add(ci);                
            component = ci;
        } else if (xmlizable instanceof CpeDescription) {
            addCpeDescription(xmlizableXmlFile, (CpeDescription) xmlizable);
            component = this;
        } else {
            CpeCasProcessor cpeCasProcessor;
//            if (xmlizable instanceof URISpecifier) {
//                cpeCasProcessor = CpeDescriptorFactory.produceRemoteCasProcessor("xmlizableXmlFile");
//            } else {
//                cpeCasProcessor = CpeDescriptorFactory.produceCasProcessor("xmlizableXmlFile");
//            }
            cpeCasProcessor = CpeDescriptorFactory.produceCasProcessor("xmlizableXmlFile");
            cpeCasProcessor.setDescriptor(xmlizableXmlFile);
            UimaCasProcessor u = UimaCasProcessor_impl.createUimaCasProcessor(cpeCasProcessor, 
                    xmlizable, this);
            uimaCasProcessors.add(u);
            // Reset cpeCasProcessor's name
            cpeCasProcessor.setName(u.getInstanceName());
            component = u;
        }

        return component;
    } // addXMLizable
    
    /**
     *  Check if the specified is valid for addition
     * 
     */
    static public boolean isValidXMLizableComponent (XMLizable xmlizable)
    {
        if (xmlizable instanceof CollectionReaderDescription
            || xmlizable instanceof CasInitializerDescription
            || xmlizable instanceof AnalysisEngineDescription
            || xmlizable instanceof TaeDescription
            || xmlizable instanceof CasConsumerDescription
            || xmlizable instanceof URISpecifier
            || xmlizable instanceof CustomResourceSpecifier
            || xmlizable instanceof CpeDescription) {
            return true;
        }
        return false;
    }
    
    /*************************************************************************/

    public UimaCollectionReader getUimaCollectionReader ()
    {
        if (uimaCollectionReaders != null && uimaCollectionReaders.size() > 0) {
            return (UimaCollectionReader)uimaCollectionReaders.get(0);
        }
        
        return null;
    } // getUimaCollectionReader
    
    public UimaCollectionReader getUimaCollectionReader (String name)
    {
        if (uimaCollectionReaders != null) {
            for (int i=0; i<uimaCollectionReaders.size(); ++i) {
                if (name.equalsIgnoreCase(((UimaCollectionReader)uimaCollectionReaders.get(i)).getInstanceName())) {
                    return (UimaCollectionReader)uimaCollectionReaders.get(i);
                }
            }
        }
        
        return null;
    } // getUimaCollectionReader
    
    public UimaCasProcessor getUimaCasProcessor (String name)
    {
        if (uimaCasProcessors != null) {
            for (int i=0; i<uimaCasProcessors.size(); ++i) {
                if (name.equalsIgnoreCase(((UimaCasProcessor)uimaCasProcessors.get(i)).getInstanceName())) {
                    return (UimaCasProcessor)uimaCasProcessors.get(i);
                }
            }
        }
        
        return null;
    } // getUimaCasProcessor
       
    public boolean deleteUimaCasProcessor (AbstractUimaCasProcessor u)
    {
        List list;
        if (u.isUimaCollectionReader()) {
            list = uimaCollectionReaders;
        } else if (u.isUimaCasInitializer()) {
            list = uimaCasInitializers;
        } else {
            list = uimaCasProcessors;
        }
        for (int i=0; i<list.size(); ++i) {
            if (u == list.get(i)) {
                list.remove(i);
                return true;
            }
        }             
        // Trace.bug("CANNOT find for deletion: " + u.getInstanceName());
        return false;
    } // deleteUimaCasProcessor

    public boolean moveUimaCasProcessor (AbstractUimaCasProcessor u, boolean moveUp)
    {
        List list;
        if (u.isUimaCollectionReader()) {
            list = uimaCollectionReaders;
        } else {
            list = uimaCasProcessors;
        }

        int index = list.indexOf(u);
        if (moveUp) {
            // Move Up if NOT 1st element
            if (index != 0) {
                list.remove(index);
                list.add(index-1, u);
                return true;
            }
        } else {
            // Move Down if NOT last element
            if (index != (list.size()-1)) {
                list.remove(index);
                list.add(index+1, u);
                return true;
            }            
        }
        return false;
    }    
    
    /**
     * Check if AbstractUimaCasProcessor is movable
     * 
     * @param u
     * @return int  0: non-movable; 0x01: up; 0x02: down; 0x003; both dir
     */
    public int isMovable (AbstractUimaCasProcessor u)
    {
        List list;
        if (u.isUimaCollectionReader()) {
            list = uimaCollectionReaders;
        } else if (u.isUimaCasInitializer()) {
            list = uimaCasInitializers;
        } else {
            list = uimaCasProcessors;
        }
        int index = list.indexOf(u);

        int flags = 0;
        // Move Up if NOT 1st element
        if (index != 0) {
            flags = 0x01;
        }

        // Move Down if NOT last element
        if (index != (list.size()-1)) {
            flags = flags | 0x02;
        }            
        return flags;
    }
        
    public boolean deleteUimaCasProcessor_1 (UimaCasProcessor u)
    {
        for (int i=0; i<uimaCasProcessors.size(); ++i) {
            if (u == uimaCasProcessors.get(i)) {
                uimaCasProcessors.remove(i);
                // Trace.trace("Delete " + u.getInstanceName());
                return true;
            }
        }             
        Trace.bug("CANNOT find for deletion: " + u.getInstanceName());
        return false;
    } // deleteUimaCasProcessor
    
    public boolean moveUimaCasProcessor_1 (UimaCasProcessor u, boolean moveUp)
    {
        int index = uimaCasProcessors.indexOf(u);
        if (moveUp) {
            // Move Up if NOT 1st element
            if (index != 0) {
                uimaCasProcessors.remove(index);
                uimaCasProcessors.add(index-1, u);
                return true;
            }
        } else {
            // Move Down if NOT last element
            if (index != (uimaCasProcessors.size()-1)) {
                uimaCasProcessors.remove(index);
                uimaCasProcessors.add(index+1, u);
                return true;
            }            
        }
        return false;
    }
    
    /*************************************************************************/

    public String prettyPrintModel (XMLizable trueDescriptor) {
        StringWriter writer = new StringWriter();
        String parsedText = null;
        try {
            XMLSerializer xmlSerializer = new XMLSerializer(true);
            xmlSerializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            xmlSerializer.setWriter(writer);
            ContentHandler contentHandler = xmlSerializer.getContentHandler();
            contentHandler.startDocument();

            if (trueDescriptor instanceof AnalysisEngineDescription) {
                AnalysisEngineDescription aed = (AnalysisEngineDescription) trueDescriptor;
                aed.toXML(contentHandler, true, true);
            } else {
                trueDescriptor.toXML(contentHandler, true);
            }
            contentHandler.endDocument();
            writer.close();
            parsedText = writer.toString();

        } catch (SAXException e) {
            // throw new InternalErrorCDE(Messages.getString("MultiPageEditor.22"), e); //$NON-NLS-1$
        } catch (IOException e) {
            // throw new InternalErrorCDE(Messages.getString("MultiPageEditor.23"), e); //$NON-NLS-1$
        }
        return parsedText;
    }


    /*************************************************************************/

    /**
     * Overridden to provide custom XML representation.
     * @see org.apache.uima.util.XMLizable#toXML(ContentHandler)
     */
    public void toXML(ContentHandler aContentHandler, 
        boolean aWriteDefaultNamespaceAttribute)
        throws SAXException
    {
        AttributesImpl attrs = new AttributesImpl();
        aContentHandler.startElement(getXmlizationInfo().namespace,
                    "uimaApplication", "uimaApplication", attrs);
        
        if (getCpeDescriptor() != null){
          attrs.addAttribute("","href", "href", null, getCpeDescriptor());
        }

        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", "cpeDescription", attrs);        
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", "cpeDescription");

        attrs.clear();
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", TAG_DEPLOYMENT_DEFAULT_SETTINGS, attrs);        
/*
            <errorHandling>
                <errorRateThreshold action="terminate" count="100" sample_size=1000"/>
                <maxConsecutiveRestarts action="terminate" value="30"/>
                <timeout max="10000"/>
            </errorHandling>
            <checkpoint batch="10000" frequency="" />
        
*/        
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "", "errorHandling", attrs);    
        
        attrs.clear();
        CasProcessorErrorHandling errorHandling = getDefaultCasProcessorSettings().getErrorHandling();
        if (errorHandling != null) {
            
            attrs.addAttribute("","action","action",null,errorHandling.getErrorRateThreshold().getAction());
            attrs.addAttribute("","count","count",null, ""+errorHandling.getErrorRateThreshold().getMaxErrorCount());
            attrs.addAttribute("","sample_size","sample_size",null, ""+errorHandling.getErrorRateThreshold().getMaxErrorSampleSize());
            aContentHandler.startElement(getXmlizationInfo().namespace,
                    "", "errorRateThreshold", attrs);        
            aContentHandler.endElement(getXmlizationInfo().namespace, "","errorRateThreshold");
        }
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "","errorHandling");
        
        attrs.clear();
        attrs.addAttribute("","batch","batch",null, ""+getDefaultCasProcessorSettings().getCasProcBatchSize());
        aContentHandler.startElement(getXmlizationInfo().namespace,
                "","checkpoint",attrs);        
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "","checkpoint");
        
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "", TAG_DEPLOYMENT_DEFAULT_SETTINGS);

        // Add Collection Reader)
        List list = getUimaCollectionReaders();
        for (int i=0; i<list.size(); ++i) {
            ((UimaCollectionReader) list.get(i)).toXML(aContentHandler, 
                    aWriteDefaultNamespaceAttribute);
        }        
        
        // Add Cas Initializer
        list = getUimaCasInitializers();
        for (int i=0; i<list.size(); ++i) {
            attrs.clear();
            UimaCasInitializer u = (UimaCasInitializer) list.get(i);
            attrs.addAttribute("", TAG_NAME, TAG_NAME,
                               null, ""+u.getInstanceName());
            aContentHandler.startElement(getXmlizationInfo().namespace,
                    "", TAG_DEPLOYMENT_OVERRIDES,attrs);        
            attrs.clear();
            
            // <casProcessorSettings>
            aContentHandler.startElement(getXmlizationInfo().namespace,
                    "", TAG_DEPLOYMENT_SETTINGS, attrs);
            // </casProcessorSettings>
            aContentHandler.endElement(getXmlizationInfo().namespace,
                    "", TAG_DEPLOYMENT_SETTINGS);                    
            
            // Add Configuration Parameter Overrides for CPE
            List cpeOverrideList = u.getConfigParamsModel().getCpeParamModelList();
            if (cpeOverrideList != null && cpeOverrideList.size() > 0) {
                // <configurationParameterSettings>
                aContentHandler.startElement(getXmlizationInfo().namespace,
                        "","configurationParameterSettings",attrs);        
                
                CasProcessorConfigurationParameterSettings settings =
                    CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
                // casProc.setConfigurationParameterSettings(settings);
                for (int k=0; k<cpeOverrideList.size(); ++k) {
                    ConfigParameterModel m = (ConfigParameterModel) cpeOverrideList.get(k);
                    if (m.getCpeValue() != null) {
                        settings.setParameterValue (m.getName(), m.getCpeValue());
                        m.toXML(aContentHandler, aWriteDefaultNamespaceAttribute);
                    }
                }
                
                // </configurationParameterSettings>
                aContentHandler.endElement(getXmlizationInfo().namespace,
                        "","configurationParameterSettings");        
            }
            aContentHandler.endElement(getXmlizationInfo().namespace,
                    "", TAG_DEPLOYMENT_OVERRIDES);            
        }         
        
        // Add Cas Processors
        list = getUimaCasProcessors();
        for (int i=0; i<list.size(); ++i) {
            ((UimaCasProcessor) list.get(i)).toXML(aContentHandler, 
                        aWriteDefaultNamespaceAttribute);
        }         
        aContentHandler.endElement(getXmlizationInfo().namespace,
                "uimaApplication","uimaApplication");
    }

    protected XmlizationInfo getXmlizationInfo() {
        // return XMLIZATION_INFO;
        return new XmlizationInfo(null, null);
        //this object has custom XMLization routines
    }
    
    static final private XmlizationInfo XMLIZATION_INFO =
        new XmlizationInfo("uimaApplication",
          new PropertyXmlInfo[]{
             new PropertyXmlInfo("cpeDescriptor", false),
             new PropertyXmlInfo("defaultCasProcessorSettings", false)          
          });

    

    /**
     * Overridden to provide custom XMLization.
     * @see org.apache.uima.util.XMLizable#buildFromXMLElement(org.w3c.dom.Element, org.apache.uima.util.XMLParser)
     */
    public void buildFromXMLElement (Element aElement, XMLParser aParser, XMLParser.ParsingOptions aOptions)
                                                throws InvalidXMLException
    {
        // Trace.trace();        
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node curNode = childNodes.item(i);
            if (curNode instanceof Element) {
                Element elem = (Element)curNode;
                if ("cpeDescription".equals(elem.getTagName())) {
                    setCpeDescriptor(elem.getAttribute("href"));
                    // Trace.trace("HRef: " + getCpeDescriptor());
                    
                } else if (TAG_COLLECTION_READER.equals(elem.getTagName())) {
                    uimaCollectionReaders.add(aParser.buildObject(elem, aOptions)); 

                } else if (TAG_CAS_PROCESSOR.equals(elem.getTagName())) {
                    uimaCasProcessors.add(aParser.buildObject(elem, aOptions)); 

                } else if (TAG_DEPLOYMENT_DEFAULT_SETTINGS.equals(elem.getTagName())) {
                    // defaultCasProcessorSettings ALWAYS exists
                    buildDefaultCasProcessorSettings(elem, aParser);
                } else {
                    Trace.err("Unknown Tag: " + elem.getTagName());
//                    throw new InvalidXMLException(
//                            InvalidXMLException.UNKNOWN_ELEMENT,
//                            new Object[]{elem.getTagName()});
                }
            }
        }
//        UimaCasProcessor[] paramArr = 
//            new UimaCasProcessor[uimaCasProcessors.size()];
//         if (uimaCasProcessors.size() > 0) {
//             uimaCasProcessors.toArray(paramArr);
//         }
//         setUimaCasProcessors(paramArr);              
    }
    
    private void buildCasProcessorSettingsOverride (Element aElement, XMLParser aParser)
                throws InvalidXMLException
    {
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node curNode = childNodes.item(i);
            if (!(curNode instanceof Element)) {
                continue;
            }
            Element elem = (Element)curNode;
            if ("errorHandling".equals(elem.getTagName())) {
            }
        }
    }

    /**
     * 
     * 
     * @param aElement
     * @param aParser
     * @throws InvalidXMLException
     * @return void
     * 
     *      <defaultCasProcessorSettings>
     *          <errorHandling>
     *              <errorRateThreshold action="terminate" count="100" sample_size=1000"/>
     *              <maxConsecutiveRestarts action="terminate" value="30"/>
     *              <timeout max="10000"/>
     *          </errorHandling>
     *          <checkpoint batch="10000" frequency="" />
     *      </defaultCasProcessorSettings>
     *
     */
    private void buildDefaultCasProcessorSettings (Element aElement, XMLParser aParser)
                                throws InvalidXMLException
    {
        NodeList childNodes = aElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node curNode = childNodes.item(i);
            if (!(curNode instanceof Element)) {
                continue;
            }
            Element elem = (Element)curNode;
            if ("errorHandling".equals(elem.getTagName())) {
                NodeList cNodes = elem.getChildNodes();

                CasProcessorErrorHandling errorHandling = null;
                for (int j = 0; j < cNodes.getLength(); j++) {
                    Node node = cNodes.item(j);
                    if (!(node instanceof Element)) {
                        continue;
                    }
                    Element e = (Element) node;
                    if ("errorRateThreshold".equals(e.getTagName())) {
                        if (errorHandling == null) {
                            errorHandling = UimaApplication_Impl.getCpeDefaultErrorHandling();
                        }
                        errorHandling.getErrorRateThreshold().setAction(e.getAttribute("action"));
                        errorHandling.getErrorRateThreshold().setMaxErrorCount(Integer.parseInt(e.getAttribute("count")));
                        errorHandling.getErrorRateThreshold().setMaxErrorSampleSize(Integer.parseInt(e.getAttribute("sample_size")));
                        
                        
                        // Trace.trace("errorRateThreshold action=" + action);
                    } else if ("maxConsecutiveRestarts".equals(e.getTagName())) {
                        if (errorHandling == null) {
                            errorHandling = UimaApplication_Impl.getCpeDefaultErrorHandling();
                        }
                        
                        
                        
                    } else if ("timeout".equals(e.getTagName())) {
                        if (errorHandling == null) {
                            errorHandling = UimaApplication_Impl.getCpeDefaultErrorHandling();
                        }
                        
                        
                    }
                }
                // Set ErrorHandling ?
                if (errorHandling != null) {
                    getDefaultCasProcessorSettings().setErrorHandling(errorHandling);
                }
            } else if ("checkpoint".equals(elem.getTagName())) {

            } else {
                throw new InvalidXMLException(
                        InvalidXMLException.UNKNOWN_ELEMENT,
                        new Object[]{elem.getTagName()});
            }
        }
        
        
        

        
    }
    
    /*************************************************************************/
    
    /**
     * Get UIMA application descriptor.
     * 
     * @return String
     */
    public String getCpeDescriptor ()
    {
        return cpeHref;
    }
    
    /**
     * Set UIMA application descriptor.
     * 
     * @param descriptor
     * @return void
     */
    public void setCpeDescriptor (String descriptor)
    {
        cpeHref = descriptor;
    }
      
//    public CpeDescriptorModel  getCpeDescriptorModel ()
//    {
//        return mCpeDescriptorModel;
//    }
    
    public void test ()
    {
        // UimaCasProcessor[] sets = getUimaCasProcessors();
//        for (int i=0; i<sets.length; ++i) {
//            sets[i].printMe();
//            ConfigurationParameterSettings[] settings = sets[i].getConfigurationParameterSettings();
//            for (int k=0; k<settings.length; ++k) {
//                // Trace.trace(settings[k].toString());
//            }
//        }
    }
    
    /**
     * Initialize UimaCasProcessor from cpe descriptor
     * 
     * @return void
     */
    public void initUimaCasProcessors()
    {
//        if (mCpeDescriptorModel == null) return;
//        for (int i=0; i<mUimaCasProcessors.length; ++i) {
//            CpeCasProcessorModel model = mCpeDescriptorModel
//                    .getCpeCasProcessorModel(mUimaCasProcessors[i].getCasProcessorName());
//            if (model != null) {
//                ((UimaCasProcessor_impl)mUimaCasProcessors[i]).initConfigurationParameters(model);
//            }
//            
//        }
    }
    
//    public void createUimaCasProcessors ()
//    {
//        List list = mCpeDescriptorModel.getCpeCasProcessorModelList();
//        if (list == null || list.size() == 0) {
//            return;
//        }
//        mUimaCasProcessors = new UimaCasProcessor[list.size()];
//        for (int i=0; i<list.size(); ++i) {
//            UimaCasProcessor_impl u = new UimaCasProcessor_impl();
//            u.initConfigurationParameters((CpeCasProcessorModel) list.get(i));
//            mUimaCasProcessors[i] = u;
//        }
//    }

    
    /**
     * Get all UIMA Collection Readers.
     * 
     * @return An arry of UimaCollectionReader
     */
    public List getUimaCollectionReaders() {
        return uimaCollectionReaders;
    }

    public List getUimaCasInitializers() {
        return uimaCasInitializers;
    }
    
    public List getUimaCasProcessors() {
        return uimaCasProcessors;
    }

    /**
     * @return Returns the cpeConfiguration.
     */
    public CpeConfiguration getCpeConfiguration() {
        return cpeConfiguration;
    }

    /**
     * @param cpeConfiguration The cpeConfiguration to set.
     */
    public void setCpeConfiguration(CpeConfiguration cpeConfiguration) {
        this.cpeConfiguration = cpeConfiguration;
    }

    /**
     * @return Returns the defaultCasProcessorSettings.
     */
    public DefaultCasProcessorSettings getDefaultCasProcessorSettings() {
        return defaultCasProcessorSettings;
    }

    /**
     * @param defaultCasProcessorSettings The defaultCasProcessorSettings to set.
     */
    public void setDefaultCasProcessorSettings(
            DefaultCasProcessorSettings defaultCasProcessorSettings) {
        this.defaultCasProcessorSettings = defaultCasProcessorSettings;
    }

//    public void setUimaCasProcessors(UimaCasProcessor[] aParams) {
//        mUimaCasProcessors = aParams;        
//    }
    
//    public UimaCasProcessor getUimaCasProcessor(String casProcessorName)
//    {
//        if (mUimaCasProcessors != null) {
//            for (int i=0; i<mUimaCasProcessors.length; ++i) {
//                if ( casProcessorName.equals(mUimaCasProcessors[i].getCasProcessorName()) ) {
//                    return mUimaCasProcessors[i];
//                }
//            }
//        }
//        return null;
//    }
    
    public void printMe()
    {
        Trace.out("Cpe xml:" + getCpeDescriptor());
        
    }

    /**
     * @return Returns the cpeCasProcessorsSettings.
     */
    public CpeCasProcessorsSettings getCpeCasProcessorsSettings() {
        return cpeCasProcessorsSettings;
    }

    /**
     * @param cpeCasProcessorsSettings The cpeCasProcessorsSettings to set.
     */
    public void setCpeCasProcessorsSettings(
            CpeCasProcessorsSettings cpeCasProcessorsSettings) {
        this.cpeCasProcessorsSettings = cpeCasProcessorsSettings;
    }

    /**
     * @return the runtimeContext
     */
    public IRuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    /**
     * @param runtimeContext the runtimeContext to set
     */
    public void setRuntimeContext(IRuntimeContext runtimeContext) {
        this.runtimeContext = runtimeContext;
    }

}
