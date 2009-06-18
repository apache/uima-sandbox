package org.apache.uima.tools.cfe;

import java.util.Collection;


public class TargetAnnotationDescriptor
{
    String                  m_class_name;
    String                  m_enclosing_annotation_name;
    
    PartialObjectMatcher                m_target_annotation_matcher;
    Collection<FeatureObjectMatcher>    m_feature_annotation_matchers;
    
    final int               m_priorityOrder;
    
    
    public TargetAnnotationDescriptor(String                            class_name,
                                      String                            enclosing_annotation_name,
                                      PartialObjectMatcher              target_annotation_matcher,
                                      Collection<FeatureObjectMatcher>  feature_annotation_matchers,
                                      int                               priorityOrder)
   {
        m_class_name = class_name;
        m_enclosing_annotation_name = enclosing_annotation_name;
        m_target_annotation_matcher = target_annotation_matcher;
        m_feature_annotation_matchers = feature_annotation_matchers;
        m_priorityOrder = priorityOrder;
   }
    
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TargetAnnotationDescriptor)) {
            return false;
        }
        TargetAnnotationDescriptor other = (TargetAnnotationDescriptor)obj;
        if (!m_class_name.equals(other.m_class_name) ||
            !m_enclosing_annotation_name.equals(other.m_enclosing_annotation_name) ||
            !m_target_annotation_matcher.equals(other.m_target_annotation_matcher) ||
            !m_feature_annotation_matchers.equals(other.m_feature_annotation_matchers)) {
            return false;
        }
        return super.equals(obj);
    }

    String getClassName ()
    {
        return m_class_name;
    }
    
    String getEnclosingAnnotationName ()
    {
        return m_enclosing_annotation_name;
    }
    
    PartialObjectMatcher getTargetAnnotationMatcher ()
    {
        return m_target_annotation_matcher;
    }

    Collection<FeatureObjectMatcher> getFeatureAnnotationMatchers ()
    {
        return m_feature_annotation_matchers;
    }
}
