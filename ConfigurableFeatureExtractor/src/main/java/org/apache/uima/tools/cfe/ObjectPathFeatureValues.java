package org.apache.uima.tools.cfe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.tools.cfe.support.FeatureConstrainedIterator;
import org.apache.uima.tools.cfe.support.UIMAAnnotationUtils;


public class ObjectPathFeatureValues extends CollectionFeatureValues<MatchedValue>
{
    String m_object_type_name;
    String m_object_path;
    
    public ObjectPathFeatureValues (String object_type_name, String object_path)
    {
        super(new HashSet<MatchedValue>());
        m_object_type_name = object_type_name;
        m_object_path = object_path;
    }
    
    void update (Object source, Annotation enclosing, Collection<MatchedValue> previosly_matched_objects)
    throws ClassNotFoundException,
           IllegalArgumentException,
           SecurityException,
           IllegalAccessException,
           NoSuchFieldException,
           InvocationTargetException
    {
        m_values.clear();

        JCas jcas = (JCas)source;
        TargetObjectMatcher tom = new TargetObjectMatcher(m_object_type_name, m_object_path, false);

        Class<? extends Annotation> cls_ann = UIMAAnnotationUtils.getAnnotationClass(tom.getRootClass());
        for (FSIterator root_ann_it = FeatureConstrainedIterator.getEnclosedIterator(jcas, cls_ann, enclosing); root_ann_it.hasNext();) {
            List<MatchedValue> mfvs = tom.getFeatureValues(new AnnotationMatchedValue((Annotation)root_ann_it.next(), new ArrayList<Object>()));
            // exclude prioritized models (previously specified in config file)
            if (null != previosly_matched_objects) {
                MatchedValue.removeAll(mfvs, previosly_matched_objects);
                if (!tom.isDetached()) {
                    // add annotation only if it is included into a model
                    previosly_matched_objects.addAll(mfvs);
                }
            }
            m_values.addAll(mfvs);
        }
    }
    
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ObjectPathFeatureValues)) {
            return false;
        }
        ObjectPathFeatureValues other = (ObjectPathFeatureValues)obj;
        if (!m_object_path.equals(other.m_object_path)) {
            return false;
        }
        if (!m_values.equals(other.m_values)) {
            return false;
        }
        return super.equals(obj);
    }
    
    public String getFeatureImage (Object feature)
    {
        return m_object_type_name + "," + m_object_path;
    }
    
    public boolean matches (Object feature)
    {
        return m_values.contains(feature);
    }
    
}
