package org.apache.uima.tools.cfe;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


public class UIMAFeatureTransformer extends PrimitiveFeatureTransformer
{
    UIMAFeatureTransformer(boolean case_sensitive)
    {
        super(case_sensitive);
    }
    
    public String convert (Object feature)
    {
        if (feature instanceof FSArray) {
            return feature.getClass().getName() + "_" + ((FSArray)feature).size();  
        }
        else if (feature instanceof Annotation) {
            return feature.getClass().getName(); 
        }
        return super.convert(feature);
    }
}
