package org.apache.uima.tools.cfe;

import java.util.Iterator;
import java.util.List;


public class MatchedSingleFeatureMatcher
{
    final public SingleFeatureMatcher                   m_matcher; 
    final public List<MatchedValue> m_values; 
    MatchedSingleFeatureMatcher(SingleFeatureMatcher matcher, List<MatchedValue> values)
    {
        m_matcher   = matcher;
        m_values    = values;
    }
    
    String getFeatureImage(boolean feat_name, String[] value_separators)
    {
        String result = "";
        ArrayDelimiterObject max_level = null;
        if (!m_values.isEmpty()) {
            // first element must be max_level
            max_level = (ArrayDelimiterObject) m_values.get(0).m_matchedObject;
        }
        
        for (Iterator<MatchedValue> it = m_values.iterator(); it.hasNext();) {
            Object obj = it.next().m_matchedObject;
            if (max_level == obj) {
                continue;
            }
            if (obj instanceof ArrayDelimiterObject) {
                ArrayDelimiterObject ado = (ArrayDelimiterObject)obj; 
                int ind = Math.min(max_level.m_level - ado.m_level, value_separators.length - 1);
                result += value_separators[ind];
            }
            else {
                result += m_matcher.m_feature_values.getFeatureImage(obj);
            }
        }
        if (feat_name) {
            result = m_matcher.m_feature_matcher.getFeaturePathImage() + value_separators[0] + result;
        }
        return result;
    }
}
