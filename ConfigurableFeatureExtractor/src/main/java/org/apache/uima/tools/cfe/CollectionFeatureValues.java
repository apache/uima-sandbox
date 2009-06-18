package org.apache.uima.tools.cfe;

import java.util.Collection;

public abstract class CollectionFeatureValues<T> implements FeatureValues
{

    Collection<T> m_values;

    public CollectionFeatureValues (Collection<T> values)
    {
        m_values = values;
    }
    
    public abstract boolean matches (Object feature);
}
