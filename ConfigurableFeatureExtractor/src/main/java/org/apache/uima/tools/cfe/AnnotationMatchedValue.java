package org.apache.uima.tools.cfe;

import java.util.Collection;
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;

public class AnnotationMatchedValue extends MatchedValue
{

    public AnnotationMatchedValue (MatchedValue mv)
    {
        super(mv.m_matchedObject, mv.m_orderedPath);
        getAnnotation(); // should throw an exception if m_matchedObject is not of Annotation type 
    }

    public AnnotationMatchedValue (Annotation matchedObject)
    {
        super(matchedObject);
    }
    
    public AnnotationMatchedValue (Annotation matchedObject,
                                   List<Object> orderedPath)
    {
        super(matchedObject, orderedPath);
    }

    
    
    public Annotation getAnnotation()
    {
        return (Annotation)super.m_matchedObject;
    }
    
    public static Collection<AnnotationMatchedValue> upcast (Collection<AnnotationMatchedValue> trg,
                                                             Collection<MatchedValue>           src)
    {
        for (MatchedValue mv : src) {
            trg.add(new AnnotationMatchedValue(mv));
        }
        return trg;
    }
}
