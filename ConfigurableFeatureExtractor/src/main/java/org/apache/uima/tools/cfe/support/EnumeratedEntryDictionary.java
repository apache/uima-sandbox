package org.apache.uima.tools.cfe.support;

import java.util.Set;

import org.apache.uima.tools.cfe.FeatureValueNormalizer;


public class EnumeratedEntryDictionary extends SimpleFileBasedDictionary<String>
{
    protected FeatureValueNormalizer m_normalizer;
    
    public EnumeratedEntryDictionary(String name, String path, FeatureValueNormalizer normalizer)
    {
        super(name, path, null, null, null);
        m_normalizer = normalizer;
    }
     
    protected void addLine(String[] toks, int cnt)
    {
        String tok = m_normalizer.normalize(toks[0]);
        addEntry(tok, null, cnt);
    }
    
    public Set<String> values()
    {
        return storage().keySet();
    }
}


