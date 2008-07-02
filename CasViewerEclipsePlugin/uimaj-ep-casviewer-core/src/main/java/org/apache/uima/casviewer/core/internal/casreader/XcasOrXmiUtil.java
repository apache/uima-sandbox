package org.apache.uima.casviewer.core.internal.casreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.casviewer.core.internal.BaseCASObject;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.tools.debug.util.Trace;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLSerializer;
import org.apache.uima.util.XmlCasDeserializer;
import org.xml.sax.SAXException;

/**
 *  Utility to de-serialize a CAS from XCAS/XMI file.
 * 
 *  Note: TypePriorities is NOT supported
 */
public class XcasOrXmiUtil 
{
    private static final String         DEFAULT_TYPE_SYSTEM_FOR_XCAS = "typesystem.xml";
    private static final String         DEFAULT_TYPE_PRIORITIES_FOR_XCAS = "typepriorities.xml";

    static public CAS convertXcasToXmi (CAS cas, File xcasFile, File xmiFile) throws ResourceInitializationException, SAXException, IOException {        
        deserializeCAS(cas, xcasFile);
        writeXmi(cas, xmiFile);
        return cas;
    }

    /**
     * Serialize a CAS to a file in XMI format
     * 
     * @param aCas
     *          CAS to serialize
     * @param name
     *          output file
     * @throws SAXException
     * @throws Exception
     * 
     * @throws ResourceProcessException
     */
    static public void writeXmi(CAS aCas, File name) throws IOException, SAXException {
        FileOutputStream out = null;
        try {
            // write XMI
            out = new FileOutputStream(name);
            XmiCasSerializer ser = new XmiCasSerializer(aCas.getTypeSystem());
            XMLSerializer xmlSer = new XMLSerializer(out, false);
            ser.serialize(aCas, xmlSer.getContentHandler());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    /*************************************************************************/
    
    static public TypeSystemDescription deriveTypeSystemDescription (String xcasOrxmiFileName) {        
        String s = xcasOrxmiFileName.replace('\\', '/');
        int lastIndex = s.lastIndexOf('/');
        
        // Look for Type System used with this Xcas or Xmi
        // Prefix with the path from this instance file. Make the file absolute
        String      typesystemFileName = null;
        if ( lastIndex != -1) {
            typesystemFileName = s.substring(0, lastIndex+1) + DEFAULT_TYPE_SYSTEM_FOR_XCAS;
        } else {
            typesystemFileName = DEFAULT_TYPE_SYSTEM_FOR_XCAS;
        }
        if ( ! (new File(typesystemFileName)).exists() ) {
            // No Type System file
            return null;
        }
        
        // Get type system description from input descriptor.
        Object descriptor = null;
        TypeSystemDescription   tsDescription = null;
        try {
            descriptor = UIMAFramework.getXMLParser().parse(new XMLInputSource(typesystemFileName));
            if (descriptor instanceof TypeSystemDescription) {
                tsDescription = (TypeSystemDescription)descriptor;
                try {
                    tsDescription.resolveImports();
                } catch (InvalidXMLException e) {
                    return null;
                }
            } else {
                // Trace.err("Invalid Descriptor File \"" + xmlDescriptor + "\"" +
                //          "Must be either an AnalysisEngine or TypeSystem descriptor.");
                // throw new CASViewerException("Cannot create type system from \"" + xmlDescriptor + "\"");
            }
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return tsDescription;
    } // deriveTypeSystemDescription
    
    @Deprecated
    static private CAS deserializeCAS_SAVE (String xmlDescriptor, String xcasFileName,
            String typePrioritiesFileName) throws IOException, InvalidXMLException, 
            ResourceInitializationException, ParserConfigurationException, SAXException
    {
        TypeSystemDescription   typeSystemDescription = null;

        // Optionally use Type Priorities for indexing
        TypePriorities          typePriorities = null;

        // Comment out by Tong on 03-05-2007 to avoid the following exception:
        // "The type org.apache.uima.examples.SourceDocumentInformation in 
        // the type priority list in the descriptor"
//      if (typePrioritiesFileName != null) {
//      XMLInputSource in = new XMLInputSource(new File(typePrioritiesFileName));
//      Object xmlLizable = UIMAFramework.getXMLParser().parse(in);
//      if (xmlLizable != null && (xmlLizable instanceof TypePriorities)) {
//      typePriorities = (TypePriorities) xmlLizable;
//      }
//      }

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
            // Trace.err("Invalid Descriptor File \"" + xmlDescriptor + "\"" +
            //          "Must be either an AnalysisEngine or TypeSystem descriptor.");
            // throw new CASViewerException("Cannot create type system from \"" + xmlDescriptor + "\"");
        }
        if (typeSystemDescription == null) {
            return null;
        }
        typeSystemDescription.resolveImports();

        ///////////////////////////////////////////////////////////////////

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

        // Deserialize XCAS into CAS (for UIMA 1.4)
//      XCASDeserializer deser = new XCASDeserializer(cas.getTypeSystem());
//      ContentHandler deserHandler = deser.getXCASHandler(cas, new OutOfTypeSystemData());
//      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//      XMLReader xmlReader = parser.getXMLReader();
//      xmlReader.setContentHandler(deserHandler);
//      FileInputStream xcasInStream = null;
//      try {
//      xcasInStream = new FileInputStream(new File(xcasFileName));
//      xmlReader.parse(new InputSource(xcasInStream));
//      } finally {
//      if (xcasInStream != null) {
//      xcasInStream.close();     
//      }
//      }

        ///////////////////////////////////////////////////////////////////

        // For Testing !
        // Iterate over multi-sofa 
//      FSIterator it = cas.getSofaIterator();
//      while (it.hasNext()) {
//      SofaFS sofa = (SofaFS) it.next();
//      Trace.trace("sofa " + sofa.getSofaRef() );
//      }

        return cas;        
    } // deserializeCAS

    static public CAS deserializeCAS (String xmlDescriptor, String xcasFileName)
                  throws IOException, InvalidXMLException, ResourceInitializationException,
                  ParserConfigurationException, SAXException
    {
        TypeSystemDescription   typeSystemDescription = null;

        // Get type system description from input descriptor.
        Object descriptor = UIMAFramework.getXMLParser().parse(new XMLInputSource(xmlDescriptor));
        if (descriptor instanceof AnalysisEngineDescription) {
            typeSystemDescription = ((AnalysisEngineDescription)descriptor).getAnalysisEngineMetaData().getTypeSystem();
        } else if (descriptor instanceof TypeSystemDescription) {
            typeSystemDescription = (TypeSystemDescription)descriptor;
        } else {
            // Trace.err("Invalid Descriptor File \"" + xmlDescriptor + "\"" +
            //          "Must be either an AnalysisEngine or TypeSystem descriptor.");
            // throw new CASViewerException("Cannot create type system from \"" + xmlDescriptor + "\"");
        }
        if (typeSystemDescription == null) {
            return null;
        }
        typeSystemDescription.resolveImports();

        ///////////////////////////////////////////////////////////////////

        return deserializeCAS (new File(xcasFileName), typeSystemDescription);
    }

    static public CAS deserializeCAS (File xcasFile, TypeSystemDescription typeSystemDescription) 
        throws ResourceInitializationException, SAXException, IOException
    {
        // Create a new CAS     
        CAS cas = CasCreationUtils.createCas(typeSystemDescription, null,
                new FsIndexDescription[0],
                UIMAFramework.getDefaultPerformanceTuningProperties());

        ///////////////////////////////////////////////////////////////////

        // For Apache-UIMA
        FileInputStream xcasInStream = null;
        try {
            xcasInStream = new FileInputStream(xcasFile);
            XmlCasDeserializer.deserialize(xcasInStream, cas, true);
        } finally {
            if (xcasInStream != null) {
                xcasInStream.close();
            }
        }

        return cas;        
    } // deserializeCAS

    static public CAS deserializeCAS (CAS cas, File xcasFile) 
                            throws ResourceInitializationException, SAXException, IOException
    {
        // For Apache-UIMA
        FileInputStream xcasInStream = null;
        try {
            xcasInStream = new FileInputStream(xcasFile);
            XmlCasDeserializer.deserialize(xcasInStream, cas, true);
        } finally {
            if (xcasInStream != null) {
                xcasInStream.close();
            }
        }

        return cas;        
    } // deserializeCAS

    
    @Deprecated
    static public BaseCASObject createBaseCASObjectFromFile (String xmlDescriptor, String xmlFileName,
            String typePrioritiesFileName_NOT_USED) throws IOException, InvalidXMLException, 
            ResourceInitializationException, ParserConfigurationException, SAXException
    {
        CAS cas = deserializeCAS(xmlDescriptor, xmlFileName);
        if (cas == null) {
            return null;
        }

        // Use Type System from CAS so that DocumentAnnotation will be in 
        // the Type System if it is not defined by the user 
        return new BaseCASObject (cas, null);                    
    }

//    @Deprecated
//    static public BaseCASObject createBaseCASObjectFromFile (String xmlDescriptor, String xmlFileName,
//            String typePrioritiesFileName_NOT_USED,
//            List<String> excludedTypeNames) throws IOException, InvalidXMLException, 
//            ResourceInitializationException, ParserConfigurationException, SAXException
//    {
//        CAS cas = deserializeCAS(xmlDescriptor, xmlFileName);
//        if (cas == null) {
//            return null;
//        }
//        
//        CAS destCas = null;
//        if (excludedTypeNames != null) {
//            // Create a new CAS     
//            destCas = CasCreationUtils.createCas(cas.getTypeSystem(), null,
//                    new FsIndexDescription[0],
//                    UIMAFramework.getDefaultPerformanceTuningProperties());    
//            CasCopier2.copyCas(cas, destCas, true, excludedTypeNames);
//            cas.reset();
//            cas.release();
//            cas = destCas;
//        }
//        
//
//        // Use Type System from CAS so that DocumentAnnotation will be in 
//        // the Type System if it is not defined by the user 
//        return new BaseCASObject (cas, null);                    
//    }

    /**
     *  Create BaseCASObject from xcas/xmi file
     * 
     * @param xcasOrxmiFileName
     * 
     * Instead use "createBaseCASObjectFromFile" 
     */
    @Deprecated
    static public BaseCASObject createCASObjectFromFile (String xcasOrxmiFileName)
    {
        // Check for null or empty file name
        if (xcasOrxmiFileName == null || xcasOrxmiFileName.trim().length()==0 ) {
            return null;
        }
        // Which file format (XMI or XCAS) ?
        if (!xcasOrxmiFileName.endsWith("xmi") && !xcasOrxmiFileName.endsWith("xcas")) {
            Trace.bug("Cannot open this unknown file: " + xcasOrxmiFileName);
            return null;
        }

        String s = xcasOrxmiFileName.replace('\\', '/');
        int lastIndex = s.lastIndexOf('/');

        // Look for TypePriorities xml file
        String typePrioritiesFileName = null;
        if ( lastIndex != -1) {
            typePrioritiesFileName = s.substring(0, lastIndex+1) + DEFAULT_TYPE_PRIORITIES_FOR_XCAS;
        }
        if ( ! (new File(typePrioritiesFileName)).exists() ) {
            // No Type Priorities file
            // Trace.err("NO priority file: " + typePrioritiesFileName);
            typePrioritiesFileName = null;
        }

        // Look for Type System used with this Xcas or Xmi
        // Prefix with the path from this instance file. Make the file absolute
        String      typesystemFileName = null;
        if ( lastIndex != -1) {
            typesystemFileName = s.substring(0, lastIndex+1) + DEFAULT_TYPE_SYSTEM_FOR_XCAS;
        } else {
            typesystemFileName = DEFAULT_TYPE_SYSTEM_FOR_XCAS;
        }
        if ( ! (new File(typesystemFileName)).exists() ) {
            // No Type System file
            return null;
        }

        // De-serialize and create BaseCASObject
        BaseCASObject   casObject = null;
        try {
            casObject = createBaseCASObjectFromFile(typesystemFileName, 
                    xcasOrxmiFileName, typePrioritiesFileName);
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return casObject;
    } // createCASObjectFromFile   

    /**
     *  Create BaseCASObject from xcas/xmi file
     * 
     * @param xcasOrxmiFile     An existing xcas/xmi File
     * @throws IOException 
     * @throws SAXException 
     * @throws ResourceInitializationException 
     */
    static public BaseCASObject createBaseCASObjectFromFile (File xcasOrxmiFile, 
            TypeSystemDescription typeSystemDescription) throws ResourceInitializationException, SAXException, IOException
    {        
        if (typeSystemDescription == null) {
            // Try to find the type system
            typeSystemDescription = deriveTypeSystemDescription (xcasOrxmiFile.getAbsolutePath());
            if (typeSystemDescription == null) {
                return null;
            }
        }
        
        // De-serialize and create BaseCASObject
        CAS cas = deserializeCAS(xcasOrxmiFile, typeSystemDescription);
        if (cas == null) {
            return null;
        }

        return new BaseCASObject (cas, null);                    
    } // createBaseCASObjectFromFile   

    /**
     *  Create BaseCASObject from reused CAS and xcas/xmi file
     * 
     * @param reuseCAS
     * @param xcasOrxmiFile
     * @throws ResourceInitializationException
     * @throws SAXException
     * @throws IOException
     * @return BaseCASObject
     */
    static public BaseCASObject createBaseCASObjectFromFile (CAS reuseCAS, File xcasOrxmiFile)
                        throws ResourceInitializationException, SAXException, IOException
    {                
        // De-serialize and create BaseCASObject
        CAS cas = deserializeCAS(reuseCAS, xcasOrxmiFile);
        if (cas == null) {
            return null;
        }

        return new BaseCASObject (cas, null);                    
    } // createBaseCASObjectFromFile   

}
