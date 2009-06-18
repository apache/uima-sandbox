package org.apache.uima.tools.cfe;

public interface FeatureValues
{
    abstract public boolean matches (Object feature);
    abstract public String getFeatureImage (Object feature);
}
