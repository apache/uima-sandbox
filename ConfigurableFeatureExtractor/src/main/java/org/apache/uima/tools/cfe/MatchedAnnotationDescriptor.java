package org.apache.uima.tools.cfe;

import java.util.Collection;

import org.apache.uima.jcas.tcas.Annotation;



public class MatchedAnnotationDescriptor
{
    public final FeatureObjectMatcher                       m_feature_matcher;
    public final AnnotationMatchedValue                     m_feature_mv;
    public final Annotation                                 m_enclosing;
    public final Collection<MatchedSingleFeatureMatcher>    m_sfms_with_values;
    public final int                                        m_direction;
    public final int                                        m_offset;
    public final int                                        m_orderIndex;
    
    public MatchedAnnotationDescriptor(FeatureObjectMatcher fom,
                                       Annotation                               enclosing,
                                       AnnotationMatchedValue                   feature_mv,
                                       Collection<MatchedSingleFeatureMatcher>  sfms_with_values,
                                       int                                      direction,
                                       int                                      offset,
                                       int                                      priorityOrder)
    {
        m_feature_matcher   = fom;
        m_feature_mv        = feature_mv;
        m_enclosing         = enclosing;
        m_sfms_with_values  = sfms_with_values;
        m_direction         = direction;
        m_offset            = offset;
        m_orderIndex        = priorityOrder;
    }
}
