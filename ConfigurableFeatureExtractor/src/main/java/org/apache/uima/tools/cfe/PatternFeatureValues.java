package org.apache.uima.tools.cfe;

import java.util.regex.Pattern;

public class PatternFeatureValues implements FeatureValues
{
    final Pattern m_pattern;
    
    public PatternFeatureValues(String pattern)
    {
        m_pattern = Pattern.compile(pattern);
    }
    
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PatternFeatureValues)) {
            return false;
        }
        PatternFeatureValues other = (PatternFeatureValues)obj;
        if (m_pattern == other.m_pattern) {
            return super.equals(obj);
        }
        return false;
    }

    public boolean matches (Object feature)
    {
        return m_pattern.matcher(getFeatureImage(feature)).find();
    }
    
    public String getFeatureImage (Object feature)
    {
        return feature.toString();
    }
}
