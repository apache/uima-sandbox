package org.apache.uima.casviewer.core.internal;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.TaeDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;


public class TypeSystemUtils2 
{
    
    /*************************************************************************/
    
    public static String getMyShortName (String fullName) {
        if (fullName == null) return null;
        
        int index = fullName.lastIndexOf('.');
        if ( index < 0 ) {
            return fullName;
        } else {
            return fullName.substring(index+1);
        }
    } // getMyShortName
    
    
    static public TypeSystemDescription getTypeSystemDescription (String xmlDescriptorFile,
                            ResourceManager rm) throws InvalidXMLException, IOException, ResourceInitializationException
    {
        XMLizable specifier = null;
            specifier = UIMAFramework.getXMLParser().
                    parse(new XMLInputSource(xmlDescriptorFile));
        
        return getTypeSystemDescription (specifier, rm);
    }
    
    static public TypeSystemDescription getTypeSystemDescription (XMLizable specifier, ResourceManager rm) throws InvalidXMLException, ResourceInitializationException
    {
        TypeSystemDescription tsDescription = null;
        
            if (specifier instanceof TaeDescription) {
                TaeDescription tae = (TaeDescription) specifier;
                if ( tae.isPrimitive() ) {
                    AnalysisEngineMetaData meta = tae.getAnalysisEngineMetaData();
                    if (rm != null) {    
                        meta.resolveImports(rm);
                    } else {
                        meta.resolveImports();
                    }
                    tsDescription = meta.getTypeSystem();
                } else {
                    if (rm != null) {
                        tsDescription = CasCreationUtils.mergeDelegateAnalysisEngineTypeSystems(tae, rm);                        
                    } else {
                        tsDescription = CasCreationUtils.mergeDelegateAnalysisEngineTypeSystems(tae);
                    }
                }                
            } else if (specifier instanceof CasConsumerDescription) {
                ProcessingResourceMetaData meta = ((CasConsumerDescription)specifier).getCasConsumerMetaData();
                if (rm != null) {    
                    meta.resolveImports(rm);
                } else {
                    meta.resolveImports();
                }
                tsDescription = meta.getTypeSystem();
            } else if (specifier instanceof CollectionReaderDescription) {
                ProcessingResourceMetaData meta = ((CollectionReaderDescription)specifier).getCollectionReaderMetaData();
                if (rm != null) {    
                    meta.resolveImports(rm);
                } else {
                    meta.resolveImports();
                }
                tsDescription = meta.getTypeSystem();
            } else if (specifier instanceof TypeSystemDescription) {
                if (rm != null) {    
                    ((TypeSystemDescription) specifier).resolveImports(rm);
                } else {
                    ((TypeSystemDescription) specifier).resolveImports();
                }
                return (TypeSystemDescription) specifier;
            }

        return tsDescription;
    } // getTypeSystemDescription
    
    /*************************************************************************/
        
    public static final Properties casCreateProperties = new Properties();
    static {
        casCreateProperties.setProperty(UIMAFramework.CAS_INITIAL_HEAP_SIZE, "200");
    }
    
    public static TypeSystem getUIMABuiltInTypeSystem ()
    {
        TypeSystemDescription       typeSystemDesc = null;
        CAS tcas = null;
        try {
            tcas = CasCreationUtils.createCas(typeSystemDesc, null, new FsIndexDescription[0]);
            TypeSystem typeSystem = tcas.getTypeSystem();
            // RepositoryUtil.printTypeSystem (typeSystem);
            return typeSystem;
        } catch (ResourceInitializationException e1) {
            e1.printStackTrace();
            //  StringWriter msgBuffer = new StringWriter();
            //  PrintWriter msgWriter = new PrintWriter( msgBuffer );
            //  msgWriter.println(e1.toString()); //  printStackTrace());
            //  System.err.println("msgBuffer:" + msgBuffer.toString());
//          _errorCode = ERROR_UIMAEXCEPTION;
//          _errorMsg  = new String(e1.toString());
        }
        return null;
    }
        
    public static List createTypeDescriptionListFromUIMATypeSystem (List typeDescriptionList,
            TypeSystem typeSystem, Type typeRoot )
    {
        if (typeRoot == null) {
          return typeDescriptionList;
        }
        TypeDescription typeDesc = TypeSystemUtilModified.type2TypeDescription(typeRoot, typeSystem);
        if (typeDesc != null) {
            typeDescriptionList.add (typeDesc);
        }
        
        Vector children = typeSystem.getDirectlySubsumedTypes(typeRoot);
        for (int i=0 ; i<children.size() ; ++i) {
          createTypeDescriptionListFromUIMATypeSystem (typeDescriptionList, 
                    typeSystem, (Type) children.get(i));
            
        }               
        
        return typeDescriptionList;
    } // createTypeDescriptionListFromUIMATypeSystem

}
