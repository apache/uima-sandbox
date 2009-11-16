package org.apache.uima.casviewer.core.internal.casreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.casviewer.core.internal.BaseCASObject;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XmlCasDeserializer;
import org.xml.sax.SAXException;

/**
 * 
 *
 */
public class XcasReader 
{
    
    static public CAS deserializeXcas_NOT_USED (String xmlDescriptor, String xmlXcas,
                                String typePrioritiesFileName)
    {
//        File descriptorFile = new File(xmlDescriptor);
        
        // Parse descriptor and create TCas and StyleMap
        Object descriptor;
        try {
            // Create new CAS with the type system from input descriptor.
            // Also build style map file if there is none.
            descriptor = UIMAFramework.getXMLParser().parse(new XMLInputSource(xmlDescriptor));
            
            TypePriorities  typePriorities = null;
            if (typePrioritiesFileName != null) {
                XMLInputSource in = new XMLInputSource(new File(typePrioritiesFileName));
                Object xmlLizable = UIMAFramework.getXMLParser().parse(in);
                if (xmlLizable != null && (xmlLizable instanceof TypePriorities)) {
                    typePriorities = (TypePriorities) xmlLizable;
                }
            }
            CAS tcas;
            // File styleMapFile;
            if (descriptor instanceof AnalysisEngineDescription) {
                tcas = CasCreationUtils.createCas((AnalysisEngineDescription)descriptor);
                // styleMapFile = getStyleMapFile((AnalysisEngineDescription)descriptor, descriptorFile.getPath());
                
            } else if (descriptor instanceof TypeSystemDescription) {
                TypeSystemDescription typeSystemDescription = (TypeSystemDescription)descriptor;
                typeSystemDescription.resolveImports();
                tcas = CasCreationUtils.createCas(typeSystemDescription, typePriorities, new FsIndexDescription[0]);
                // styleMapFile = getStyleMapFile((TypeSystemDescription)descriptor, descriptorFile.getPath());        
            } else {
//                displayError("Invalid Descriptor File \"" + descriptorFile.getPath() + "\"" +
//                "Must be either an AnalysisEngine or TypeSystem descriptor.");
                return null;
            }                    
            
            // Create a new CAS     
            if (typePriorities != null) {
                Trace.err("Used TypePriorities");
            } else {
                Trace.err("NO TypePriorities");
            }
            CAS cas = CasCreationUtils.createCas(tcas.getTypeSystem(), 
                    typePriorities, new FsIndexDescription[0],
                    UIMAFramework.getDefaultPerformanceTuningProperties());
            
            // Deserialize XCAS or XMI into CAS
            File xcasFile = new File(xmlXcas);
            
            // For Apache-UIMA
            FileInputStream xcasInStream = null;
            try {
                xcasInStream = new FileInputStream(xcasFile);
                XmlCasDeserializer.deserialize(xcasInStream, cas, true);
            } finally {
                if (xcasInStream != null)
                    xcasInStream.close();
            }

            // For UIMA 1.4
//            XCASDeserializer deser = new XCASDeserializer(cas.getTypeSystem());
//            ContentHandler deserHandler = deser.getXCASHandler(cas, new OutOfTypeSystemData());
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            XMLReader xmlReader = parser.getXMLReader();
//            xmlReader.setContentHandler(deserHandler);
//            FileInputStream xcasInStream = null;
//            try {
//                xcasInStream = new FileInputStream(xcasFile);
//                xmlReader.parse(new InputSource(xcasInStream));
//            } finally {
//                if (xcasInStream != null)
//                    xcasInStream.close();     
//            }
            
            FSIterator it = cas.getSofaIterator();
            SofaFS sf = null;
            int i=0;
            while (it.hasNext()) {
                SofaFS sofa = (SofaFS) it.next();
                if (++i == 2) sf = sofa;
                Trace.trace("sofa " + sofa.getSofaRef() );
            }
            return cas.getCurrentView();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }     
        return null;
    } // deserializeXcas
    
    /**
     * 
     * @deprecated
     */
    static public BaseCASObject getSofaAwaredCASFromXcas (String xmlDescriptor, String xcasFileName,
            String typePrioritiesFileName) throws IOException, InvalidXMLException, 
            ResourceInitializationException, ParserConfigurationException, SAXException
    {
        TypeSystemDescription   typeSystemDescription = null;
        
        // Optionally use Type Priorities for indexing
        TypePriorities          typePriorities = null;
        
        // Comment out by Tong on 03-05-2007 to avoid the following exception:
        // "The type org.apache.uima.examples.SourceDocumentInformation in 
        // the type priority list in the descriptor"
        if (typePrioritiesFileName != null) {
            XMLInputSource in = new XMLInputSource(new File(typePrioritiesFileName));
            Object xmlLizable = UIMAFramework.getXMLParser().parse(in);
            if (xmlLizable != null && (xmlLizable instanceof TypePriorities)) {
                typePriorities = (TypePriorities) xmlLizable;
            }
        }
        
        ///////////////////////////////////////////////////////////////////
        
        // Get type system description from input descriptor.
        // Also build style map file if there is none.
        Object descriptor = UIMAFramework.getXMLParser().parse(new XMLInputSource(xmlDescriptor));
        // File styleMapFile;
        if (descriptor instanceof AnalysisEngineDescription) {
            typeSystemDescription = ((AnalysisEngineDescription)descriptor).getAnalysisEngineMetaData().getTypeSystem();
            // styleMapFile = getStyleMapFile((AnalysisEngineDescription)descriptor, descriptorFile.getPath());                
        } else if (descriptor instanceof TypeSystemDescription) {
            typeSystemDescription = (TypeSystemDescription)descriptor;
            // styleMapFile = getStyleMapFile((TypeSystemDescription)descriptor, descriptorFile.getPath());        
        } else {
//                Trace.err("Invalid Descriptor File \"" + xmlDescriptor + "\"" +
//                "Must be either an AnalysisEngine or TypeSystem descriptor.");
            throw new CASViewerException("Cannot create type system from \"" + xmlDescriptor + "\"");
        }
        if (typeSystemDescription == null) {
            return null;
        }
        // FIXME Add Resource Manager support
        typeSystemDescription.resolveImports();
        
        return createCASObjectFromFile(xcasFileName, typeSystemDescription, typePriorities);
    }
    
    static public BaseCASObject createCASObjectFromFile (String xcasFileName,
            TypeSystemDescription typeSystemDescription, 
            TypePriorities typePriorities) 
                throws ResourceInitializationException, SAXException, IOException 
    {
        // Create a new CAS     
        CAS cas = CasCreationUtils.createCas(typeSystemDescription, 
                typePriorities, new FsIndexDescription[0],
                UIMAFramework.getDefaultPerformanceTuningProperties());
        
        ///////////////////////////////////////////////////////////////////
        
        // For Apache-UIMA
        FileInputStream xcasInStream = null;
        try {
            xcasInStream = new FileInputStream(xcasFileName);
            XmlCasDeserializer.deserialize(xcasInStream, cas, true);
        } finally {
            if (xcasInStream != null) {
                xcasInStream.close();
            }
        }
        
        ///////////////////////////////////////////////////////////////////
        
        // Use Type System from CAS so that DocumentAnnotation will be in 
        // the Type System if it is not defined by the user 
        // return new CASObject (cas, typeSystemDescription, typePriorities);
        return new BaseCASObject (cas, typePriorities);
            
    } // createCASObjectFromFile
 
}
