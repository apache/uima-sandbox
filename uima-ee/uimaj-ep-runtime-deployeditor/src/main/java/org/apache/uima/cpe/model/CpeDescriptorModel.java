package org.apache.uima.cpe.model;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.application.metadata.UimaApplication;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeCollectionReaderCasInitializer;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.collection.metadata.NameValuePair;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;




/**
 *      CpeDescriptorModel
 *          Collection Reader Model
 *              xml Descriptor
 *          Cas Initializer
 *          cpeCasProcessorModelList
 *              CpeCasProcessorModel 1
 *                  xml Descriptor
 *              CpeCasProcessorModel 2
 *                  xml Descriptor
 *
 */
public class CpeDescriptorModel {
    
    // UIMA Application for this CpeDescriptorModel
    protected UimaApplication       mUimaApplication = null;
    
    // List of CollectionReaderModel
    protected List                  collectionReaderModelList = null;
    
    // List of CpeCasProcessorModel
    protected List                  cpeCasProcessorModelList = null;
    
    // UIMA Objects
    protected CpeDescription        cpeDesc;
    protected CpeCollectionReader[] collRdrs;
    protected CpeCasProcessor[]     casProcs;
    
    // List of ResourceSpecifier
    protected List                  resourceSpecifierList = null;
    // List of ResourceSpecifier for AnalysisEngine
    protected List                  analysisEngineList = null;
    
    protected List                  casConsumerList = null;
    protected List                  typeSystemList  = null;
    protected TypeSystemDescription mergedTypeSystemDescription = null;
    
    /*************************************************************************/
    
    protected CpeDescriptorModel () {        
    }
    
    static public CpeDescriptorModel createInstance (String cpeXmlDescriptorFile) 
    {
        try {
            CpeDescription cpeDescription = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(new File(cpeXmlDescriptorFile)));
            return createInstance (cpeXmlDescriptorFile, cpeDescription);
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    static public CpeDescriptorModel createInstance (String cpeXmlDescriptorFile, 
                                        CpeDescription cpeDescription) 
    {
        CpeDescriptorModel cpeModel = new CpeDescriptorModel ();
        try {
            cpeModel.cpeDesc = cpeDescription;
            
            // Collection Reader
            cpeModel.collRdrs = cpeModel.cpeDesc.getAllCollectionCollectionReaders();
            cpeModel.collectionReaderModelList = new ArrayList();
            if (cpeModel.collRdrs != null && cpeModel.collRdrs.length>0) {
                for (int i=0; i<cpeModel.collRdrs.length; ++i) {
                    cpeModel.collectionReaderModelList.add(new UimaCollectionReader(cpeModel, 
                            cpeModel.collRdrs[i], null));                
                }
            }
            
            /*****************************************************************/
            
            // CPE Cas Processors
            cpeModel.casProcs = cpeModel.cpeDesc.getCpeCasProcessors().getAllCpeCasProcessors();            
            if (cpeModel.casProcs == null) {
                // Empty Cpe Descriptor
                return cpeModel;
            }
            
            // Get list of CpeCasProcessor which can be an AE or a Cas Consumer
            // For each CpeCasProcessor, get its type system (insert in cpeModel.typeSystemList)
            cpeModel.cpeCasProcessorModelList = new ArrayList();
            List    specifierList = new ArrayList();
            List    aeList       = new ArrayList();
            List    consumerList = new ArrayList();
            cpeModel.typeSystemList = new ArrayList();
            for (int i=0; i < cpeModel.casProcs.length; i++) {
                // CpeCasProcessor casProc = model.casProcs[i];
                printCpeCasProcessor(cpeModel.casProcs[i]);
                String specifierFile    = cpeModel.casProcs[i].getDescriptor();
                ResourceSpecifier specifier = UIMAFramework.getXMLParser().
                            parseResourceSpecifier(new XMLInputSource(specifierFile));
                specifierList.add(specifier);
                cpeModel.cpeCasProcessorModelList.add(new CpeCasProcessorModel(cpeModel, cpeModel.casProcs[i], specifier));
                if (cpeModel.isCasConsumerSpecifier(specifier)) {
                    consumerList.add(specifier);
                } else {
                    aeList.add(specifier);
                }
                // Get type system
                cpeModel.typeSystemList.add(getTypeSystemDescription(specifier));
            }                 
            if (specifierList.size() > 0)
                cpeModel.resourceSpecifierList = specifierList;
            if (aeList.size() > 0)
                cpeModel.analysisEngineList = aeList;
            if (consumerList.size() > 0)
                cpeModel.casConsumerList = consumerList;
            
            // Create Merged Type System
            cpeModel.mergedTypeSystemDescription = CasCreationUtils.mergeTypeSystems(cpeModel.typeSystemList);
            // Set Name and Description
            cpeModel.mergedTypeSystemDescription.setName("Merged Type System for ???");
            String descr = "Merge TSs of: ";
            for (int i=0; i<cpeModel.casProcs.length; ++i) {
                if (i > 0) {
                    descr += ", ";
                }
                descr += ((CpeCasProcessor) cpeModel.casProcs[i]).getName();
            }
            cpeModel.mergedTypeSystemDescription.setDescription(descr);
            
            StringWriter sw = new StringWriter();
            cpeModel.mergedTypeSystemDescription.toXML(sw);
//            String taeDescString = new String(sw.toString());
            sw.close();
            
            // Trace.trace(taeDescString);
            
        } catch (InvalidXMLException e) {
            e.printStackTrace(); 
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (CpeDescriptorException e) {
            e.printStackTrace();
            return null;
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return cpeModel;
    } // createInstance
    
    static public void printCpeCasProcessor (CpeCasProcessor cpeCasProc)
    {
        CasProcessorConfigurationParameterSettings settings = cpeCasProc.getConfigurationParameterSettings();
        if (settings != null) {
            printNameValuePairs (settings.getParameterSettings());
        }
    }
        
    static public void printNameValuePairs (NameValuePair[] nvs)
    {
        for (int i=0; i<nvs.length; ++i) {
            String name = nvs[i].getName();
            Trace.trace("Param name:" + nvs[i].getName());
            Object aValue = nvs[i].getValue();
            if (aValue == null) {
                System.out.println("    no value");
            }
            if (aValue instanceof String) {
                
            } else if (aValue instanceof Integer) {
                System.out.println("    Integer" + ((Integer) aValue).intValue());
            } else if (aValue instanceof Float) {
                
            } else if (aValue instanceof Boolean) {
                
            } else if (aValue instanceof Object[]) {
                Object[] obj = (Object[])aValue;
                System.out.println("    Object[]:" + obj.length);
                for (int k = 0; k < obj.length; k++) {
                  if (obj[k] instanceof String) {
                      System.out.println("    [" + k + "]:" + obj[k].toString());
                  }
                  else if (obj[k] instanceof Integer) {
                      System.out.println("    [" + k + "]:" + ((Integer)obj[k]).intValue());      
                  }
                  else if (obj[k] instanceof Float) {
                      System.out.println("    [" + k + "]:" + ((Float)obj[k]).floatValue());
                  } else if (obj[k] instanceof Boolean) {
                      System.out.println("    [" + k + "]:" + ((Boolean)obj[k]).booleanValue());
                  }        
                }            
            } else {
                
            }
        }
        
    }
    
    static public CpeDescriptorModel printCpeXmlDescriptor (String cpeXmlDescriptorFile ) 
    {
        CpeDescriptorModel cpeModel = new CpeDescriptorModel ();
        try {
            cpeModel.cpeDesc =
                UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(new File(cpeXmlDescriptorFile)));
            
            // Collection Reader
            cpeModel.collRdrs = cpeModel.cpeDesc.getAllCollectionCollectionReaders();
            cpeModel.collectionReaderModelList = new ArrayList();
            if (cpeModel.collRdrs != null && cpeModel.collRdrs.length>0) {
                for (int i=0; i<cpeModel.collRdrs.length; ++i) {
                    cpeModel.collectionReaderModelList.add(new UimaCollectionReader(cpeModel, 
                            cpeModel.collRdrs[i], null));                
                }
            }
            
            /*****************************************************************/
            
            // CPE Cas Processors
            cpeModel.casProcs = cpeModel.cpeDesc.getCpeCasProcessors().getAllCpeCasProcessors();            
            if (cpeModel.casProcs == null) {
                // Empty Cpe Descriptor
                return cpeModel;
            }
            
            // Get list of CpeCasProcessor which can be an AE or a Cas Consumer
            // For each CpeCasProcessor, get its type system (insert in cpeModel.typeSystemList)
            cpeModel.cpeCasProcessorModelList = new ArrayList();
            List    specifierList = new ArrayList();
            List    aeList       = new ArrayList();
            List    consumerList = new ArrayList();
            cpeModel.typeSystemList = new ArrayList();
            for (int i=0; i < cpeModel.casProcs.length; i++) {
                // CpeCasProcessor casProc = model.casProcs[i];
                String specifierFile    = cpeModel.casProcs[i].getDescriptor();
                ResourceSpecifier specifier = UIMAFramework.getXMLParser().
                            parseResourceSpecifier(new XMLInputSource(specifierFile));
                specifierList.add(specifier);
                cpeModel.cpeCasProcessorModelList.add(new CpeCasProcessorModel(cpeModel, cpeModel.casProcs[i], specifier));
                if (cpeModel.isCasConsumerSpecifier(specifier)) {
                    consumerList.add(specifier);
                } else {
                    aeList.add(specifier);
                }
                // Get type system
                cpeModel.typeSystemList.add(getTypeSystemDescription(specifier));
            }                 
            if (specifierList.size() > 0)
                cpeModel.resourceSpecifierList = specifierList;
            if (aeList.size() > 0)
                cpeModel.analysisEngineList = aeList;
            if (consumerList.size() > 0)
                cpeModel.casConsumerList = consumerList;
            
            // Create Merged Type System
            cpeModel.mergedTypeSystemDescription = CasCreationUtils.mergeTypeSystems(cpeModel.typeSystemList);
            // Set Name and Description
            cpeModel.mergedTypeSystemDescription.setName("Merged Type System for ???");
            String descr = "Merge TSs of: ";
            for (int i=0; i<cpeModel.casProcs.length; ++i) {
                if (i > 0) {
                    descr += ", ";
                }
                descr += ((CpeCasProcessor) cpeModel.casProcs[i]).getName();
            }
            cpeModel.mergedTypeSystemDescription.setDescription(descr);
            
            StringWriter sw = new StringWriter();
            cpeModel.mergedTypeSystemDescription.toXML(sw);
//            String taeDescString = new String(sw.toString());
            sw.close();
            
            // Trace.trace(taeDescString);
            
        } catch (InvalidXMLException e) {
            e.printStackTrace(); 
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (CpeDescriptorException e) {
            e.printStackTrace();
            return null;
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return cpeModel;
    } // createInstance
    
    /*************************************************************************/
    
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
    
    /**
     * Return true if the ResourceSpecifier is CasConsumerDescription
     * 
     * @param specifier
     * @return
     * @return boolean
     */
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
        
    private void openCpeDescriptor (File aFile)  throws InvalidXMLException, 
                                    IOException, CpeDescriptorException
    {
        // Parse
        cpeDesc =
            UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(aFile));
        
        // Collection Reader
        collRdrs = cpeDesc.getAllCollectionCollectionReaders(); //more than one??
        CpeCollectionReader collRdr = null;
        if (collRdrs != null && collRdrs.length > 0) {
            // collRdr.getDescriptor().getInclude().get();
            collRdr = collRdrs[0];
        }

        // CAS Initializer   
        CpeCollectionReaderCasInitializer casIni = null;
        if (collRdr != null) {
            casIni = collRdr.getCasInitializer();
        }   
        if (casIni != null) {
            String casIniDescFile = casIni.getDescriptor().getInclude().get();
        }
        
        // CAS Processors
        casProcs = cpeDesc.getCpeCasProcessors().getAllCpeCasProcessors();
        for (int i=0; i < casProcs.length; i++) {
            CpeCasProcessor casProc = casProcs[i];
            String specifierFile = casProcs[i].getDescriptor();
            ResourceSpecifier specifier = UIMAFramework.getXMLParser().
            parseResourceSpecifier(new XMLInputSource(specifierFile));
//            if (isCasConsumerSpecifier(specifier)) {
//                addConsumer(specifierFile);
//            } else {
//                addTae(specifierFile);
//            }
        }                 
    }
    
    /*************************************************************************/
        
    public void setUimaApplication (UimaApplication model) {
        mUimaApplication = model;
    }
    
//    public UimaCasProcessor getUimaCasProcessor(String casProcessorName) {
//        return mUimaApplication.getUimaCasProcessor(casProcessorName);
//    }
    
    /*************************************************************************/

    /**
     * @return Returns the casProcs.
     */
    public CpeCasProcessor[] getCasProcs() {
        return casProcs;
    }

    /**
     * @param casProcs The casProcs to set.
     */
    public void setCasProcs(CpeCasProcessor[] casProcs) {
        this.casProcs = casProcs;
    }

    /**
     * @return Returns the collRdrs.
     */
    public CpeCollectionReader[] getCollRdrs() {
        return collRdrs;
    }

    /**
     * @param collRdrs The collRdrs to set.
     */
    public void setCollRdrs(CpeCollectionReader[] collRdrs) {
        this.collRdrs = collRdrs;
    }

    /**
     * @return Returns the cpeDesc.
     */
    public CpeDescription getCpeDesc() {
        return cpeDesc;
    }

    /**
     * @param cpeDesc The cpeDesc to set.
     */
    public void setCpeDesc(CpeDescription cpeDesc) {
        this.cpeDesc = cpeDesc;
    }

    /**
     * @return Returns the analysisEngineList.
     */
    public List getAnalysisEngineList() {
        return analysisEngineList;
    }

    /**
     * @param analysisEngineList The analysisEngineList to set.
     */
    public void setAnalysisEngineList(List analysisEngineList) {
        this.analysisEngineList = analysisEngineList;
    }

    /**
     * @return Returns the casConsumerList.
     */
    public List getCasConsumerList() {
        return casConsumerList;
    }

    /**
     * @param casConsumerList The casConsumerList to set.
     */
    public void setCasConsumerList(List casConsumerList) {
        this.casConsumerList = casConsumerList;
    }

    /**
     * @return Returns the mergedTypeSystemDescription.
     */
    public TypeSystemDescription getMergedTypeSystemDescription() {
        return mergedTypeSystemDescription;
    }

    /**
     * @return Returns the resourceSpecifierList.
     */
    public List getResourceSpecifierList() {
        return resourceSpecifierList;
    }

    /**
     * @return Returns the cpeCasProcessorModelList.
     */
    public List getCpeCasProcessorModelList() {
        return cpeCasProcessorModelList;
    }

    /**
     * @param cpeCasProcessorModelList The cpeCasProcessorModelList to set.
     */
    public void setCpeCasProcessorModelList(List cpeCasProcessorModelList) {
        this.cpeCasProcessorModelList = cpeCasProcessorModelList;
    }
    
    public CpeCasProcessorModel getCpeCasProcessorModel (String name) 
    {
        
        for (int i=0; i<cpeCasProcessorModelList.size(); ++i) {
            if ( name.equals(((CpeCasProcessorModel) cpeCasProcessorModelList.get(i)).getName()) ) {
                return (CpeCasProcessorModel) cpeCasProcessorModelList.get(i);
            }
        }
        Trace.trace(" *** Cannot find CpeCasProcessorModel: " + name + " ***");
        return null;
    }

    /**
     * @return Returns the collectionReaderModelList.
     */
    public List getCollectionReaderModelList() {
        return collectionReaderModelList;
    }

    /**
     * @param collectionReaderModelList The collectionReaderModelList to set.
     */
    public void setCollectionReaderModelList(List collectionReaderModelList) {
        this.collectionReaderModelList = collectionReaderModelList;
    }
    
    
}
