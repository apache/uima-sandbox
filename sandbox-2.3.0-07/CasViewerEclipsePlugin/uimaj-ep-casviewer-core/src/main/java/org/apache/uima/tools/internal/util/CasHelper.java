package org.apache.uima.tools.internal.util;

import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;

/**
 *  Helper class for CAS
 *
 */
public class CasHelper 
{
    static public int getTotalFSPerCASView (CAS aCas) {
        int totalFSPerCASView = 0;
        
        FSIndexRepository indexRepo = aCas.getIndexRepository();
        Iterator<String> iter = indexRepo.getLabels();
        while (iter.hasNext())
        {
            totalFSPerCASView += indexRepo.getIndex((String) iter.next()).size();
        }
        return totalFSPerCASView;
    }
    
    static public FSIndex getIndexForType(CAS aTCAS, String typeName)
    {
        return getIndexForType(aTCAS, aTCAS.getTypeSystem().getType(typeName));
    }
    
    static public FSIndex getIndexForType(CAS aCas, Type aType)
    {
        if (aType == null) {
            return null;
        }        

        FSIndexRepository indexRepo = aCas.getIndexRepository();
        Iterator<String> iter = indexRepo.getLabels();
        while (iter.hasNext())
        {
            String label = (String) iter.next();
            FSIndex index = indexRepo.getIndex(label);
            // Does aType inherit from index.getType()
            if (aCas.getTypeSystem().subsumes(index.getType(), aType)) {
                return indexRepo.getIndex(label, aType);
            }
        }
        return null;
    }
    
    /**
     * Get total FS for this type (FS for sub-types are NOT included)
     * 
     * @param aCas
     * @param type
     * @return int  Total FS for this type (FS for sub-types are NOT included)
     */
    static public int getTotalFSForType (CAS aCas, Type type) 
    {
        int     total = 0;
        
        FSIndex index = getIndexForType (aCas, type);
        if (index != null) {
            FSIterator it = index.iterator();
            while (it.hasNext()) {
                if (((FeatureStructure) it.next()).getType() == type) {
                    total++;
                }
            }
        }
        
        
//        FSIndexRepository ir = aCas.getIndexRepository();
//        Iterator it = ir.getLabels();
//        while (it.hasNext()) {
//            FSIndex index = ir.getIndex((String) it.next());
//            if (type == index.getType()) {
//                total += index.size();
//            }
//        }        
        return total;
    }
    static public int getTotalFSForType (FSIndex index, Type type) 
    {
        int     total = 0;
        
        if (index != null) {
            FSIterator it = index.iterator();
            while (it.hasNext()) {
                if (((FeatureStructure) it.next()).getType() == type) {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * Extract the AnnnotationFS's text span from the document
     * 
     * @param doc
     * @param f
     * @param maxLength Max length of string to be returned
     * @return String
     */
    static public String getAnnotationTextSpan (String doc, AnnotationFS f, int maxLength) 
    {
        int end = Math.min(maxLength, f.getIntValue(f.getType().getFeatureByBaseName("end"))
                - f.getIntValue(f.getType().getFeatureByBaseName("begin")));
        if (end <= 0) {
            return "";
        }
        return doc.substring(f.getIntValue(f.getType().getFeatureByBaseName("begin")),
                f.getIntValue(f.getType().getFeatureByBaseName("begin")) + end);        
    }

}
