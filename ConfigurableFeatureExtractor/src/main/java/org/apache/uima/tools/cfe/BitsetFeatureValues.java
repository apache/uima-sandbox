package org.apache.uima.tools.cfe;

public class BitsetFeatureValues implements FeatureValues
{
    final int m_bitmask;
    final boolean m_exact_match;
    
    public BitsetFeatureValues(int bitmask, boolean exact_match)
    {
        m_bitmask = bitmask;
        m_exact_match = exact_match;
    }
    
    public boolean matches (Object feature)
    {
        if (feature instanceof Integer) {
            int mask = ((Integer)feature).intValue();
            return m_exact_match ? ((m_bitmask & mask) == m_bitmask) : ((m_bitmask & mask) != 0); 
        }
        return false;
    }
    
    public String getFeatureImage (Object feature)
    {
        return feature.toString();
    }
}
