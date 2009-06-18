package org.apache.uima.tools.cfe;

public class PrimitiveFeatureTransformer implements FeatureValueConverter, FeatureValueNormalizer
{
    final boolean m_case_sensitive;
    static public String nullValueImage = "null";
    
    public PrimitiveFeatureTransformer (boolean case_sensitive)
    {
        m_case_sensitive = case_sensitive;
    }
    
    static public String capitalize (String str)
    {
        StringBuffer strBuf = new StringBuffer(str);
        
        if (strBuf.length() > 1) {
            if (Character.isLetter(strBuf.charAt(0))) {
                strBuf.setCharAt(0, Character.toUpperCase(strBuf.charAt(0)));
            }
        }
        return strBuf.toString();
    }
        
    public String convert (Object feature)
    {
        if (null == feature) {
            return nullValueImage;
        }
        return feature.toString();
    }
        
    public String normalize (String feature)
    {
        return m_case_sensitive ?  capitalize(feature) : feature.toLowerCase();
    }
        
    public String getValue (Object feature)
    {
        return normalize(convert(feature));
    }
    
}
